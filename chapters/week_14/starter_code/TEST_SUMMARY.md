# Week 14 Starter Smoke Test Summary

本起步包只包含 smoke tests，用来确认骨架可编译、模板存在、待办标记没被误删。

```bash
mvn test
```

完成作业后，你应该补充自己的测试，例如：

- 待办：`Version.parse` 的合法和非法输入。
- 待办：`Version.compareTo` 的 MAJOR/MINOR/PATCH 顺序。
- 待办：`Config` 的 dev/test/prod 加载。
- 待办：环境变量和 `${PORT:8080}` 占位符覆盖。
