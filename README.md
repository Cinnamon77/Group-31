# Group-31
以下是一个简单的 README 文件示例，这个 README 主要围绕项目的版本控制系统配置和工作区设置进行介绍：

# 项目 README

## 项目概述
本项目主要涉及版本控制系统（VCS）的配置以及工作区的相关设置，使用了 XML 文件来定义这些配置信息。目前项目中包含了 `vcs.xml` 和 `workspace.xml` 两个配置文件，分别用于 VCS 目录映射和工作区的各项设置。

## 版本控制系统配置（vcs.xml）
`vcs.xml` 文件定义了项目的版本控制系统目录映射。具体配置如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="VcsDirectoryMappings">
    <mapping directory="$PROJECT_DIR$/../../../zuoye" vcs="Git" />
    <mapping directory="$PROJECT_DIR$" vcs="Git" />
  </component>
</project>
```
### 配置说明
- `VcsDirectoryMappings` 组件用于管理版本控制系统的目录映射。
- 项目目录 `$PROJECT_DIR$` 和 `$PROJECT_DIR$/../../../zuoye` 都被映射到 Git 版本控制系统。

## 工作区设置（workspace.xml）
`workspace.xml` 文件包含了项目工作区的各种设置，如自动导入、变更列表管理、Git 设置等。

### 主要设置项
1. **自动导入设置**
```xml
<component name="AutoImportSettings">
  <option name="autoReloadType" value="SELECTIVE" />
</component>
```
自动导入类型设置为 `SELECTIVE`，即选择性自动重新加载。

2. **变更列表管理**
```xml
<component name="ChangeListManager">
  <list default="true" id="0ebca4bd-d00b-4cfb-8dba-2a7a27ffa8ba" name="Changes" comment="" />
  <option name="SHOW_DIALOG" value="false" />
  <option name="HIGHLIGHT_CONFLICTS" value="true" />
  <option name="HIGHLIGHT_NON_ACTIVE_CHANGELIST" value="false" />
  <option name="LAST_RESOLUTION" value="IGNORE" />
</component>
```
- 默认变更列表名为 `Changes`。
- 不显示对话框，高亮显示冲突，不高亮显示非活动变更列表，最后一次冲突解决策略为 `IGNORE`。

3. **Git 设置**
```xml
<component name="Git.Settings">
  <option name="RECENT_GIT_ROOT_PATH" value="$PROJECT_DIR$/../../../zuoye" />
</component>
```
最近使用的 Git 根路径设置为 `$PROJECT_DIR$/../../../zuoye`。

## 其他设置
`workspace.xml` 还包含了项目颜色信息、项目 ID、项目视图状态、属性设置、共享索引、拼写检查设置、任务管理、TypeScript 生成文件管理、VCS 管理器配置和 XSLT 支持文件关联 UI 状态等设置。

## 使用说明
1. **版本控制系统**：确保项目目录和 `$PROJECT_DIR$/../../../zuoye` 目录都使用 Git 进行版本控制。
2. **工作区设置**：根据需要调整 `workspace.xml` 中的各项设置，如自动导入类型、变更列表管理策略等。

## 注意事项
- 对配置文件的修改可能会影响项目的正常运行，请谨慎操作。
- 确保 Git 客户端已正确安装并配置，以便与版本控制系统正常交互。
