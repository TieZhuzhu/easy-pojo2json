# Easy POJO to JSON Changelog

## [1.0.2]
### Added
- Support generating JSON sample content from Java and Kotlin POJOs.
- Support conversion from classes, fields, local variables, constructor parameters, and method parameters.
- Support editor popup actions, `Alt + Insert` Generate actions, and project-view batch conversion.
- Support `Copy JSON With JavaDoc` for copying JSON-like text with field comments rendered as `//` single-line comments above properties.
- Support common Java types, arrays, collections, enums, inner classes, generics, Java 14 records, and partial Jackson/Fastjson annotation handling.
- Support SpEL-based field name and default value generation rules.
- Provide ZIP plugin packages and standalone installable plugin jars.

### Changed
- Reset the current project release line to `1.0.2` and consolidate meaningful capabilities into a single changelog entry.
- Update the plugin README/plugin description content to satisfy JetBrains Marketplace descriptor validation requirements.
- Standardize the current project packaging, naming, and publishing-facing metadata for the `easy-pojo2json` release line.

## [1.0.1]
### Added
- Add `Copy JSON With JavaDoc` action in editor popup menu and Generate menu.
- Support copying JSON-like content with field JavaDoc comments rendered above properties.
### Changed
- Upgrade plugin version to `1.0.1`.

## [1.0.0]
### Add
- Support for @JsonNaming
- Add Lower Case expression
- Add Lower Dot Case expression