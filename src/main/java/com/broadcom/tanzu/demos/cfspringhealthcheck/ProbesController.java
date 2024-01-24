/*
 * Copyright (c) 2024 Broadcom, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.broadcom.tanzu.demos.cfspringhealthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
class ProbesController {
    private final Logger logger = LoggerFactory.getLogger(ProbesController.class);
    private final ApplicationEventPublisher eventPublisher;
    private final TaskExecutor taskExecutor;

    ProbesController(ApplicationEventPublisher eventPublisher, TaskExecutor taskExecutor) {
        this.eventPublisher = eventPublisher;
        this.taskExecutor = taskExecutor;
    }

    @GetMapping(value = "/probes/not-ready", produces = MediaType.TEXT_PLAIN_VALUE)
    String moveToNotReady() {
        logger.info("Readiness state moving to REFUSING_TRAFFIC");
        AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);

        logger.debug("Scheduling readiness probe update");
        taskExecutor.execute(this::moveToReady);

        return "REFUSING_TRAFFIC";
    }

    private void moveToReady() {
        try {
            Thread.sleep(Duration.ofMinutes(1).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected exception", e);
        }

        logger.info("Readiness state moving to ACCEPTING_TRAFFIC");
        AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
    }

    @GetMapping(value = "/probes/not-live", produces = MediaType.TEXT_PLAIN_VALUE)
    String moveToNotLive() {
        logger.info("Liveness state moving to BROKEN");
        AvailabilityChangeEvent.publish(this.eventPublisher, this, LivenessState.BROKEN);
        return "BROKEN";
    }
}
