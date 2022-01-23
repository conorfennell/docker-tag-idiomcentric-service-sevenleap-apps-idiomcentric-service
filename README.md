## Micronaut 3.2.1 Documentation

- [User Guide](https://docs.micronaut.io/3.2.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.2.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.2.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)


# setup

```bash
./gradlew dockerBuildNative
```

# upgrade gradlew
```sh
./gradlew wrapper --gradle-version 7.3.3
```

# check dependenices
```sh
./gradlew dependencyUpdates
```

# code formatting
```sh
./gradlew ktlintCheck
./gradlew ktlintFormat
```

# code coverage
[code coverage generated](./build/reports/jacoco/test/html/index.html)
