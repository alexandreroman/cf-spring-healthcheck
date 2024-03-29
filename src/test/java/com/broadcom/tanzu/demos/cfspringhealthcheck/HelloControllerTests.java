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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "app.instance=foo")
@Import(RedisTestConfiguration.class)
class HelloControllerTests {
    @Autowired
    private TestRestTemplate client;

    @Test
    void testHello() {
        final var resp = client.getForEntity("/", HelloController.Greeting.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getHeaders().getFirst("X-Hello")).isNotBlank();

        final var greeting = resp.getBody();
        assertThat(greeting).isNotNull();
        assertThat(greeting.instance()).isEqualTo("foo");
        assertThat(greeting.message()).isEqualTo("Hello world!");
        assertThat(greeting.pageViews()).isGreaterThan(0);
    }
}
