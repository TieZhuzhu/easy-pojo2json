# easy-pojo2json

Easy POJO to JSON is an IntelliJ IDEA plugin for generating JSON samples or comment-enriched JSON-like text from Java/Kotlin POJOs.

<!-- Plugin description -->

## Easy POJO to JSON

Easy POJO to JSON helps you quickly generate JSON samples or comment-enriched JSON-like text from Java/Kotlin POJOs inside IntelliJ IDEA.

Key features:

- Supports common Java types, collections, arrays, enums, inner classes, and generics.
- Supports Java 17 and Java 14 records.
- Supports Kotlin POJO conversion.
- Partial support for Jackson and Fastjson annotations.
- Supports generating content from classes, fields, local variables, constructor parameters, and method parameters.
- Supports editor popup actions, the Generate menu, and project-view batch conversion.
- Supports `Copy JSON With JavaDoc`, which renders field descriptions as `//` comments above properties.
- Supports SpEL-based field name and default value rules.

<!-- Plugin description end -->

## Project information

- Plugin ID: `com.augustlee.tool.easypojo2json`
- Main package: `com.augustlee.tool.easypojo2json`
- Gradle root project: `easy-pojo2json`
- Java version: 17
- IntelliJ Platform baseline: 2023.3+

## Usage

1. Open a Java or Kotlin class file.
2. Put the caret on a class, variable, or parameter.
3. Use either of the following entry points:
   - Right-click inside the editor
   - Press `Alt + Insert` to open the Generate menu
4. Choose one of the following actions:
   - **Copy JSON**
   - **Copy JSON With JavaDoc**
5. `Copy JSON` copies standard JSON sample content.
6. `Copy JSON With JavaDoc` copies JSON-like text with single-line comments above fields, for example:

```json
{
  // User name
  "username": "",
  // Roles
  "roles": []
}
```

7. When multiple files are selected in the project view, generated results are written to Scratches.

## Screenshots

![file single](screenshot/file_single.gif)
![file generate](screenshot/file_generate.gif)
![list single](screenshot/list_single.gif)
![batch](screenshot/batch.gif)

## Build

Make sure Gradle runs with JDK 17. For example on Windows PowerShell:

```powershell
$env:JAVA_HOME="D:\java\jdk\jdk17"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

```powershell
.\gradlew.bat buildPlugin -x test
```

Build outputs:

- ZIP plugin package: `build/distributions/*.zip`
- Direct-install fat jar: `build/libs/*-standalone.jar`

If you need to run tests, note that the current test setup depends on a local IntelliJ Community source/runtime path configured through `idea.home.path` in `build.gradle.kts`.

## License

This project is distributed under **GPL-3.0-only**. See `LICENSE`.

The repository also retains the upstream MIT notice for inherited portions of the codebase:

- `NOTICE`
- `LICENSES/pojo2json-MIT.txt`
