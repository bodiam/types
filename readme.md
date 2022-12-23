# Kotools Types

[![Kotlin](https://img.shields.io/badge/kotlin-1.5.31-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Kotools Types](https://img.shields.io/maven-central/v/kotools/types)](https://search.maven.org/artifact/kotools/types)

This multiplatform library provides explicit types like `NonZeroInt` or
`NotBlankString`.

```kotlin
data class Person(val name: NotBlankString, val age: StrictlyPositiveInt)

fun main() {
    val name: NotBlankString = "Somebody".toNotBlankString()
        .getOrThrow()
    val age: StrictlyPositiveInt = 42.toStrictlyPositiveInt()
        .getOrThrow()
    val somebody = Person(name, age)
    println(somebody) // Person(name=Somebody, age=42)
}
```

## Installation

### Gradle

#### Kotlin DSL

```kotlin
implementation("kotools:types:0.1.0")
```

#### Groovy DSL

```groovy
implementation "kotools:types:0.1.0"
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>kotools</groupId>
        <artifactId>types</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

## Contributing

Feel free to contribute to this project with
[issues](https://github.com/kotools/types/issues) and
[pull requests](https://github.com/kotools/types/pulls).

This project follows the [Conventional Commits][conventional-commits] guidelines
for committing with Git.
Please read [the specifications][conventional-commits] before committing to this
project.

[conventional-commits]: https://www.conventionalcommits.org/en/v1.0.0

## License

This project is licensed under the
[MIT License](https://choosealicense.com/licenses/mit).
