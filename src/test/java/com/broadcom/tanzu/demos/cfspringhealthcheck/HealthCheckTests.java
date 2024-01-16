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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RedisTestConfiguration.class)
@AutoConfigureObservability
class HealthCheckTests {
    @Autowired
    private TestRestTemplate client;

    @Test
    void testLivenessProbe() {
        assertThat(client.getForEntity("/actuator/health/liveness", String.class).getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void testReadinessProbe() {
        assertThat(client.getForEntity("/actuator/health/readiness", String.class).getStatusCode().is2xxSuccessful()).isTrue();
    }
}
