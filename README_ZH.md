# easy-pojo2json

Easy POJO to JSON 是一个 IntelliJ IDEA 插件，用于从 Java/Kotlin POJO 生成 JSON 示例，或生成带字段注释的 JSON 风格文本。

<!-- Plugin description -->

## Easy POJO to JSON

Easy POJO to JSON 用于在 IntelliJ IDEA 中快速从 Java/Kotlin POJO 生成 JSON 示例，或生成带字段注释的 JSON 风格文本。

核心能力：

- 支持 Java 常见类型、集合、数组、枚举、内部类、泛型等 POJO 结构。
- 支持 Java 17 及 Java 14 Records。
- 支持 Kotlin POJO 转换。
- 部分支持 Jackson、Fastjson 注解。
- 支持从类、成员变量、局部变量、构造参数、方法参数生成内容。
- 支持编辑器右键菜单、Generate 菜单、项目视图批量转换。
- 支持 `Copy JSON With JavaDoc`，会在属性上方输出 `//` 单行注释。
- 支持通过 SpEL 配置字段名与类型默认值生成规则。

<!-- Plugin description end -->

## 项目信息

- 插件 ID：`com.augustlee.tool.easypojo2json`
- 主源码包：`com.augustlee.tool.easypojo2json`
- Gradle root project：`easy-pojo2json`
- Java 版本：17
- IntelliJ Platform 最低基线：2023.3+

## 使用方式

1. 打开 Java/Kotlin 类文件。
2. 将光标移动到类、变量或参数上。
3. 可通过以下任一入口操作：
   - 编辑器内右键菜单
   - 按 `Alt + Insert` 打开 Generate 菜单
4. 选择以下任一动作：
   - **Copy JSON**
   - **Copy JSON With JavaDoc**
5. `Copy JSON` 会复制标准 JSON 示例内容。
6. `Copy JSON With JavaDoc` 会复制带单行注释的 JSON 风格文本，例如：

```json
{
  // 用户名称
  "username": "",
  // 角色列表
  "roles": []
}
```

7. 在项目视图中多选文件时，生成结果会写入 Scratches。

## 示例

![file single](screenshot/file_single.gif)
![file generate](screenshot/file_generate.gif)
![list single](screenshot/list_single.gif)
![batch](screenshot/batch.gif)

## 构建

请确保 Gradle 使用 JDK 17 运行，例如在 Windows PowerShell 中：

```powershell
$env:JAVA_HOME="D:\java\jdk\jdk17"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

```powershell
.\gradlew.bat buildPlugin -x test
```

构建产物：

- ZIP 插件包：`build/distributions/*.zip`
- 可直接安装的 fat jar：`build/libs/*-standalone.jar`

如需运行测试，当前测试环境依赖 `build.gradle.kts` 中 `idea.home.path` 指向的本地 IntelliJ Community 源码/运行环境。

## 许可证

本项目当前按 **GPL-3.0-only** 分发，详见 `LICENSE`。

同时，仓库中仍保留上游继承代码对应的 MIT 版权与许可声明，详见：

- `NOTICE`
- `LICENSES/pojo2json-MIT.txt`
