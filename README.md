# takeout-ordering-system

![Static Badge](https://img.shields.io/badge/Java-JDK-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Static Badge](https://img.shields.io/badge/UI_Framework-Java_Swing-528DD7?style=for-the-badge)
![Static Badge](https://img.shields.io/badge/ORM-Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Static Badge](https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Static Badge](https://img.shields.io/badge/Built_with-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Static Badge](https://img.shields.io/badge/Status-Alpha-Green?style=for-the-badge)
[![Static Badge](https://img.shields.io/badge/License-WTFPL-yellow?style=for-the-badge)
](LICENCE)

Java 程序设计课大作业——**外卖订餐管理系统**。

系统区分顾客和商家两种角色，提供不同的操作界面和功能。

**完善的购物流程**:

- **顾客端**:
    - 注册与登录
    - 菜品浏览与瀑布流展示，关键词搜索
    - 购物车管理 (添加商品、修改数量、删除商品、实时总价计算)
    - 便捷下单 (可直接从菜品列表下单或通过购物车结算)
    - 订单历史查看与状态跟踪
    - 个人信息维护 (修改联系方式、地址、登录密码，含输入验证)
- **商家端**:
    - 商品管理 (发布新菜品、编辑现有菜品信息、设置菜品上下架状态)
    - 订单管理 (查看顾客订单、更新订单状态)
    - 商家信息维护

## 技术栈

- **核心语言**: Java
- **用户界面**: Java Swing
- **ORM框架**: Hibernate
- **数据库**: MySQL 5.7+
- **项目管理与构建**: Apache Maven
- **版本控制**: Git

### 持久层

使用 Hibernate 连接 MySQL 数据库。

数据库 SQL 代码，可以直接用：

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for carts
-- ----------------------------
DROP TABLE IF EXISTS `carts`;
CREATE TABLE `carts`
(
    `id`          int      NOT NULL AUTO_INCREMENT,
    `dish_id`     int      NOT NULL,
    `customer_id` int      NOT NULL,
    `merchant_id` int      NOT NULL,
    `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    `quantity`    int      NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK99i1rh5nm7r3f1b3wdcuq5h57` (`customer_id` ASC) USING BTREE,
    INDEX `FKld7lksantqgrs1nsfp267ygxc` (`dish_id` ASC) USING BTREE,
    INDEX `FKkhon7hsf7t976af8pej3a0kct` (`merchant_id` ASC) USING BTREE,
    CONSTRAINT `FK99i1rh5nm7r3f1b3wdcuq5h57` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKkhon7hsf7t976af8pej3a0kct` FOREIGN KEY (`merchant_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKld7lksantqgrs1nsfp267ygxc` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dishes
-- ----------------------------
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes`
(
    `id`           int                                                           NOT NULL AUTO_INCREMENT,
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
    `price`        decimal(10, 2)                                                NOT NULL,
    `description`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL,
    `merchant_id`  int                                                           NOT NULL,
    `is_available` int                                                           NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `dishes_merchant_id` (`merchant_id` ASC) USING BTREE,
    CONSTRAINT `dishes_merchant_id` FOREIGN KEY (`merchant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `id`          int                                                                                  NOT NULL AUTO_INCREMENT,
    `customer_id` int                                                                                  NOT NULL,
    `merchant_id` int                                                                                  NOT NULL,
    `dish_id`     int                                                                                  NOT NULL,
    `status`      enum ('pending','paid','completed') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `add_time`    datetime                                                                             NOT NULL,
    `quantity`    int                                                                                  NOT NULL DEFAULT 1,
    `price`       decimal(10, 2)                                                                       NOT NULL,
    `total_price` decimal(10, 2)                                                                       NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `orders_customer_id` (`customer_id` ASC) USING BTREE,
    INDEX `orders_merchant_id` (`merchant_id` ASC) USING BTREE,
    INDEX `FK9jv57worcx6rqcdenldm4gant` (`dish_id` ASC) USING BTREE,
    CONSTRAINT `FK9jv57worcx6rqcdenldm4gant` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `orders_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `orders_dish_id` FOREIGN KEY (`dish_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `orders_merchant_id` FOREIGN KEY (`merchant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`       int                                                                           NOT NULL AUTO_INCREMENT,
    `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                 NOT NULL DEFAULT '',
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                 NOT NULL DEFAULT '',
    `role`     enum ('customer','merchant') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'customer',
    `phone`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                  NOT NULL DEFAULT '',
    `address`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                 NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```

使用前请在 MySQL 中创建一个新的数据库，例如 `takeout`。

随后修改 Hibernate 配置文件 `src/main/resources/hibernate.cfg.xml` 中的数据库连接信息，以匹配自己的 MySQL 设置：
```xml
<property name="connection.url">jdbc:mysql://localhost:3306/takeout_db?useSSL=false&serverTimezone=UTC</property>
<property name="connection.username">你的MySQL用户名</property>
<property name="connection.password">你的MySQL密码</property>
```

### UI

使用 Swing 框架，有一些自定义的组件和布局。

### 逻辑层

一些基础的 CURD。

## 许可证

本项目遵循 [WTFPL 许可证](LICENCE)。

## 致谢

