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

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {
    private final AppInfo appInfo;
    private final StringRedisTemplate redis;

    HelloController(AppInfo appInfo, StringRedisTemplate redis) {
        this.appInfo = appInfo;
        this.redis = redis;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Greeting> hello() {
        final Long inc = redis.opsForValue().increment("hello");
        assert inc != null;
        return ResponseEntity.ok()
                .header("X-Hello", String.valueOf(inc))
                .body(new Greeting(appInfo.instance(), "Hello world!", inc));
    }

    record Greeting(String instance, String message, long pageViews) {
    }
}
