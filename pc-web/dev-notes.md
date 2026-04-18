# 项目开发笔记

## 当前任务
为各目录的*Lang.js文件添加zh-TW（繁体中文）翻译

## 目录处理状态

### 已完成
- contract目录: 27个*Lang.js
- aCommunity目录: 31个*Lang.js
- inspection目录: 24个*Lang.js
- room目录: 8个*Lang.js
- tenant目录
- shop目录
- store目录

### 处理中
- car目录: 22个*Lang.js文件
  - 已完成: addParkingSpaceApplyLang.js
  - 待处理: adminCarLang.js, auditParkingSpaceApplyLang.js, carAddParkingSpaceLang.js, carDetailApplyParkingSpaceLang.js等

### 待处理
- simplify目录: 18个*Lang.js
- dev目录: menuGroupLang.js, menuLang.js等

## 翻译繁化规则
- 计划→計劃
- 任务→任務
- 保养→保養
- 巡检→巡檢
- 确认删除→確認刪除
- 查询→查詢
- 重设→重設
- 执行人→執行人
- 车主→車主
- 车牌号→車牌號
- 汽车品牌→汽車品牌
- 车辆类型→車輛類型
- 停车位→停車位
- 申请→申請

## 常见语法错误修复
- 语言块(en/zh-TW)结束后缺少逗号: `}`后缺`,`
- 文件结尾缺花括号: `}`
- 多余闭合括号

## 项目信息
- 项目路径: D:\project\hcv2\MicroCommunityV2\pc-web
- 编译命令: npm run build