# Using health check probes with Spring Boot on Cloud Foundry

This project showcases the use of health check probes with Spring Boot
when using Cloud Foundry.

This sample highlights best practices around health checks:

- use a liveness probe to check that the app instance can receive inbound HTTP requests
  (you should not rely on external systems when computing the liveness state, as it may lead
  to cascading failures when a single dependency is down),
- when available (starting with CAPI v3), use a readiness probe to mark an app instance
  as routable (unlike the liveness state, you may want to include external systems state
  in this case).

Please read this page to learn about
[app health checks on Cloud Foundry](https://docs.cloudfoundry.org/devguide/deploy-apps/healthchecks.html#understand-healthchecks).

Check out the [Spring Boot configuration properties](src/main/resources/application.properties)
to see how to properly configure liveness and readiness health check probes.

Also check out the [Cloud Foundry manifest](manifest.yaml) to see how to configure
those probes with the platform.

## How to build and run this app?

You need to have a Docker daemon up and running when building and running this app,
as unit tests leverage [Testcontainers](https://testcontainers.com/) to spin up a Redis
database. A Redis database container is also automatically started as you run this app,
leveraging Docker Compose.

Use this command to build the app:

```shell
./mvnw clean package
```

Use this command to run the app on your local workstation:

```shell
./mvnw spring-boot:run
```

The app is available at http://localhost:8080:
```shell
curl -v localhost:8080
```

## How to deploy this app to Cloud Foundry?

Make sure you have a Redis database service created.

You may run this command to create a database instance:

```shell
cf create-service p-redis shared-vm redis
```

Run this command to deploy the app:

```shell
cf push
```

## Contribute

Contributions are always welcome!

Feel free to open issues & send PR.

## License

Copyright &copy; 2024 [Broadcom, Inc. or its affiliates](https://vmware.com).

This project is licensed under the [Apache Software License version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
