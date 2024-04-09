CREATE DATABASE  IF NOT EXISTS `sky_take_out` ;
USE `sky_take_out`;
SET CHARACTER SET utf8;
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `consignee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '默认 0 否 1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='地址簿';
insert into sky_take_out.address_book (id, user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default)
values  (2, 4, '耿泽雨', '0', '17868226921', '11', '北京市', '1101', '市辖区', '110102', '西城区', '幸福小区2号楼305', '2', 0),
        (3, 4, '王小美', '1', '15968256325', '61', '陕西省', '6101', '西安市', '610116', '长安区', '西安邮电大学', '3', 1);
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT '0' COMMENT '顺序',
  `status` int DEFAULT NULL COMMENT '分类状态 0:禁用，1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='菜品及套餐分类';

INSERT INTO `category` VALUES (11,1,'酒水饮料',10,1,'2022-06-09 22:09:18','2022-06-09 22:09:18',1,1);
INSERT INTO `category` VALUES (12,1,'传统主食',9,1,'2022-06-09 22:09:32','2022-06-09 22:18:53',1,1);
INSERT INTO `category` VALUES (13,2,'人气套餐',12,1,'2022-06-09 22:11:38','2022-06-10 11:04:40',1,1);
INSERT INTO `category` VALUES (15,2,'商务套餐',13,1,'2022-06-09 22:14:10','2022-06-10 11:04:48',1,1);
INSERT INTO `category` VALUES (16,1,'蜀味烤鱼',4,1,'2022-06-09 22:15:37','2022-08-31 14:27:25',1,1);
INSERT INTO `category` VALUES (17,1,'蜀味牛蛙',5,1,'2022-06-09 22:16:14','2022-08-31 14:39:44',1,1);
INSERT INTO `category` VALUES (18,1,'特色蒸菜',6,1,'2022-06-09 22:17:42','2022-06-09 22:17:42',1,1);
INSERT INTO `category` VALUES (19,1,'新鲜时蔬',7,1,'2022-06-09 22:18:12','2022-06-09 22:18:28',1,1);
INSERT INTO `category` VALUES (20,1,'水煮鱼',8,1,'2022-06-09 22:22:29','2022-06-09 22:23:45',1,1);
INSERT INTO `category` VALUES (21,1,'汤类',11,1,'2022-06-10 10:51:47','2022-06-10 10:51:47',1,1);

DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '菜品名称',
  `category_id` bigint NOT NULL COMMENT '菜品分类id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '菜品价格',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
  `status` int DEFAULT '1' COMMENT '0 停售 1 起售',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_dish_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='菜品';

INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (46, '王老吉', 11, 6.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/51903dd6-53a7-451e-a875-f773572d82f6.png', '', 1, '2022-06-09 22:40:47', '2024-03-11 11:22:54', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (47, '北冰洋', 11, 4.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/81e15eff-846e-4361-abe1-b6154b3cfc5f.png', '还是小时候的味道', 1, '2022-06-10 09:18:49', '2024-03-11 11:22:43', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (48, '雪花啤酒', 11, 4.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/e8e8d292-7f34-4025-bb49-5c2b7c661ae0.png', '', 1, '2022-06-10 09:22:54', '2024-03-11 11:22:33', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (49, '米饭', 12, 2.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/d4a66e37-dea3-4b32-8667-ac6af028ac5b.png', '精选五常大米', 1, '2022-06-10 09:30:17', '2024-03-11 11:22:21', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (50, '馒头', 12, 1.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/43f3ba51-5ea3-4031-8217-fd8316743d3a.png', '优质面粉', 1, '2022-06-10 09:34:28', '2024-03-11 11:22:09', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (51, '老坛酸菜鱼', 20, 56.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/c17de3c9-136e-45bb-9a65-c29bcc3d5910.png', '原料：汤，草鱼，酸菜', 1, '2022-06-10 09:40:51', '2024-03-11 11:22:00', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (52, '经典酸菜鮰鱼', 20, 66.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/e90ad78e-0925-4a58-be0a-43714f66443e.png', '原料：酸菜，江团，鮰鱼', 1, '2022-06-10 09:46:02', '2024-03-11 11:21:50', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (53, '蜀味水煮草鱼', 20, 38.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/bd488854-c921-4431-9cb1-73deae15eb33.png', '原料：草鱼，汤', 1, '2022-06-10 09:48:37', '2024-03-11 11:21:35', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (54, '清炒小油菜', 19, 18.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/dd1da7e1-aa14-4de5-9f6d-7083f15ebc53.png', '原料：小油菜', 1, '2022-06-10 09:51:46', '2024-03-11 11:21:23', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (55, '蒜蓉娃娃菜', 19, 18.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/d087b4c8-3a19-4f5a-8b90-e140081ffb1c.png', '原料：蒜，娃娃菜', 1, '2022-06-10 09:53:37', '2024-03-11 11:21:11', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (56, '清炒西兰花', 19, 18.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/61b7d2be-daed-43cb-a37d-a56470eab371.png', '原料：西兰花', 1, '2022-06-10 09:55:44', '2024-03-11 11:20:59', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (57, '炝炒圆白菜', 19, 18.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/ec723fe5-1de2-4e4a-96fc-124b495d3b71.png', '原料：圆白菜', 1, '2022-06-10 09:58:35', '2024-03-11 11:20:46', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (58, '清蒸鲈鱼', 18, 98.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/c67179b1-fdc0-4d5c-99bf-2369191ec5e2.png', '原料：鲈鱼', 1, '2022-06-10 10:12:28', '2024-03-11 11:20:34', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (59, '东坡肘子', 18, 138.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/6a9c6f8a-6f38-47f8-befd-603c65522e01.png', '原料：猪肘棒', 1, '2022-06-10 10:24:03', '2024-03-11 11:20:21', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (60, '梅菜扣肉', 18, 58.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/4fa3cd71-8a8f-4629-8afa-5a49122991f6.png', '原料：猪肉，梅菜', 1, '2022-06-10 10:26:03', '2024-03-11 11:20:09', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (61, '剁椒鱼头', 18, 66.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/9694058a-65d9-4ea5-a4c9-2d2ab9321f34.png', '原料：鲢鱼，剁椒', 1, '2022-06-10 10:28:54', '2024-03-11 11:19:56', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (62, '金汤酸菜牛蛙', 17, 88.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/a9b4ec08-9e9e-44ba-bc4d-9a32676c94f9.png', '原料：鲜活牛蛙，酸菜', 1, '2022-06-10 10:33:05', '2024-03-11 11:19:42', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (63, '香锅牛蛙', 17, 88.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/41e3f761-796c-4de4-a342-135b3d2042c7.png', '配料：鲜活牛蛙，莲藕，青笋', 1, '2022-06-10 10:35:40', '2024-03-11 11:19:31', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (64, '馋嘴牛蛙', 17, 88.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/195d6721-b661-4978-8200-52e0a3a91724.png', '配料：鲜活牛蛙，丝瓜，黄豆芽', 1, '2022-06-10 10:37:52', '2024-03-11 11:19:21', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (65, '草鱼2斤', 16, 68.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/f5c19be1-d37a-4eb0-9d36-54cea54421cb.png', '原料：草鱼，黄豆芽，莲藕', 1, '2022-06-10 10:41:08', '2024-03-11 11:19:06', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (66, '江团鱼2斤', 16, 119.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/029db081-71b8-43e4-8546-940c0d19057d.png', '配料：江团鱼，黄豆芽，莲藕', 1, '2022-06-10 10:42:42', '2024-03-11 11:18:53', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (67, '鮰鱼2斤', 16, 72.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/cac5c578-013b-40cc-886a-644516a66622.png', '原料：鮰鱼，黄豆芽，莲藕', 1, '2022-06-10 10:43:56', '2024-03-11 11:18:40', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (68, '鸡蛋汤', 21, 4.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/bff3dce5-a9ac-4699-b16f-ef839e4e050b.png', '配料：鸡蛋，紫菜', 1, '2022-06-10 10:54:25', '2024-03-11 11:18:09', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (69, '平菇豆腐汤', 21, 6.00, 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/d94d7b96-26af-40d1-8df4-a9fb3b57b74c.png', '配料：豆腐，平菇', 1, '2022-06-10 10:55:02', '2024-03-11 11:17:56', 1, 1);

DROP TABLE IF EXISTS `dish_flavor`;
CREATE TABLE `dish_flavor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dish_id` bigint NOT NULL COMMENT '菜品',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '口味名称',
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '口味数据list',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='菜品口味关系表';

INSERT INTO `dish_flavor` VALUES (40,10,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (41,7,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (42,7,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]');
INSERT INTO `dish_flavor` VALUES (45,6,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (46,6,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (47,5,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (48,5,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (49,2,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (50,4,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (51,3,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (52,3,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (86,52,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (87,52,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (88,51,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (89,51,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (92,53,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (93,53,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (94,54,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\"]');
INSERT INTO `dish_flavor` VALUES (95,56,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (96,57,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (97,60,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (101,66,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (102,67,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (103,65,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) COLLATE utf8_bin NOT NULL COMMENT '身份证号',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='员工信息';

INSERT INTO `employee` VALUES (1,'管理员','admin','123456','13812312312','1','110101199001010047',1,'2022-02-15 15:51:20','2022-02-17 09:16:20',10,1);

DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '名字',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `dish_id` bigint DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '口味',
  `number` int NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='订单明细表';
insert into sky_take_out.order_detail (id, name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount)
values  (14, '江团鱼2斤', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/029db081-71b8-43e4-8546-940c0d19057d.png', 13, 66, null, '不辣', 1, 119.00),
        (15, '蒜蓉娃娃菜', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/d087b4c8-3a19-4f5a-8b90-e140081ffb1c.png', 14, 55, null, null, 1, 18.00),
        (16, '清炒西兰花', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/61b7d2be-daed-43cb-a37d-a56470eab371.png', 14, 56, null, '不要葱', 1, 18.00),
        (17, '清炒西兰花', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/61b7d2be-daed-43cb-a37d-a56470eab371.png', 14, 56, null, '不要香菜', 1, 18.00),
        (18, '江团鱼2斤', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/029db081-71b8-43e4-8546-940c0d19057d.png', 15, 66, null, '不辣', 1, 119.00),
        (19, '清炒小油菜', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/dd1da7e1-aa14-4de5-9f6d-7083f15ebc53.png', 16, 54, null, '不要葱', 1, 18.00),
        (20, '鮰鱼2斤', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/cac5c578-013b-40cc-886a-644516a66622.png', 17, 67, null, '不辣', 1, 72.00),
        (21, '草鱼2斤', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/f5c19be1-d37a-4eb0-9d36-54cea54421cb.png', 17, 65, null, '不辣', 1, 68.00),
        (22, '北冰洋', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/81e15eff-846e-4361-abe1-b6154b3cfc5f.png', 17, 47, null, null, 1, 4.00);
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单号',
  `status` int NOT NULL DEFAULT '1' COMMENT '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
  `user_id` bigint NOT NULL COMMENT '下单用户',
  `address_book_id` bigint NOT NULL COMMENT '地址id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `checkout_time` datetime DEFAULT NULL COMMENT '结账时间',
  `pay_method` int NOT NULL DEFAULT '1' COMMENT '支付方式 1微信,2支付宝',
  `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态 0未支付 1已支付 2退款',
  `amount` decimal(10,2) NOT NULL COMMENT '实收金额',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地址',
  `user_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名称',
  `consignee` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `cancel_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '订单取消原因',
  `rejection_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '订单拒绝原因',
  `cancel_time` datetime DEFAULT NULL COMMENT '订单取消时间',
  `estimated_delivery_time` datetime DEFAULT NULL COMMENT '预计送达时间',
  `delivery_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '配送状态  1立即送出  0选择具体时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '送达时间',
  `pack_amount` int DEFAULT NULL COMMENT '打包费',
  `tableware_number` int DEFAULT NULL COMMENT '餐具数量',
  `tableware_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '餐具数量状态  1按餐量提供  0选择具体数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='订单表';
insert into sky_take_out.orders (id, number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status)
values  (13, '4:1710128551818', 5, 4, 2, '2024-03-10 11:42:32', null, 1, 1, 126.00, '', '17868226921', null, null, 'gengzeyu', null, null, null, '2024-03-10 11:49:45', 1, '2024-03-10 11:49:53', 1, 0, 0),
        (14, '4:1710128699581', 5, 4, 3, '2024-03-10 11:45:00', null, 1, 1, 63.00, '', '15968256325', null, null, '王小美', null, null, null, '2024-03-10 11:49:45', 1, '2024-03-10 11:49:53', 3, 2, 0),
        (15, '4:1710128918263', 5, 4, 3, '2024-03-10 11:48:38', null, 1, 1, 126.00, '', '15968256325', null, null, '王小美', null, null, null, '2024-03-10 11:49:45', 1, '2024-03-10 11:49:53', 1, 0, 0),
        (16, '4:1710128955348', 6, 4, 3, '2024-03-10 11:49:15', null, 1, 1, 25.00, '', '15968256325', null, null, '王小美', '客户电话取消', null, '2024-03-11 11:49:57', '2024-03-10 11:49:46', 1, null, 1, 0, 0),
        (17, '4:1710129692093', 5, 4, 3, '2024-03-11 12:01:32', null, 1, 1, 153.00, '', '15968256325', null, null, '王小美', null, null, null, '2024-03-11 12:02:16', 1, '2024-03-11 12:02:18', 3, 0, 0);
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '菜品分类id',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL COMMENT '套餐价格',
  `status` int DEFAULT '1' COMMENT '售卖状态 0:停售 1:起售',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_setmeal_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='套餐';
insert into sky_take_out.setmeal (id, category_id, name, price, status, description, image, create_time, update_time, create_user, update_user)
values  (32, 13, '双人同享', 148.00, 1, '价格实惠，养生套餐', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/3911a23e-8caf-4549-b77d-a37fa5c257d9.png', '2024-01-17 10:44:13', '2024-03-11 11:39:38', 1, 1),
        (35, 15, '单人豪华', 128.00, 1, '一个人的餐桌，尽享其中！', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/f8e66b52-c9da-4a6b-b0c9-aa4cc04140c6.png', '2024-03-11 11:26:15', '2024-03-11 11:39:36', 1, 1),
        (36, 15, '单人经典', 88.00, 1, '你懂的，非一般的感觉！', 'https://gengzeyu-bucket.oss-cn-hangzhou.aliyuncs.com/f1482760-50eb-4b06-bde2-f99951a54692.png', '2024-03-11 11:37:39', '2024-03-11 11:39:35', 1, 1);
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
  `dish_id` bigint DEFAULT NULL COMMENT '菜品id',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '菜品名称 （冗余字段）',
  `price` decimal(10,2) DEFAULT NULL COMMENT '菜品单价（冗余字段）',
  `copies` int DEFAULT NULL COMMENT '菜品份数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='套餐菜品关系';
insert into sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies)
values  (55, 35, 58, '清蒸鲈鱼', 98.00, 1),
        (56, 35, 55, '蒜蓉娃娃菜', 18.00, 1),
        (57, 35, 49, '米饭', 2.00, 2),
        (58, 35, 69, '平菇豆腐汤', 6.00, 1),
        (59, 35, 47, '北冰洋', 4.00, 1),
        (74, 36, 49, '米饭', 2.00, 2),
        (75, 36, 69, '平菇豆腐汤', 6.00, 1),
        (76, 36, 48, '雪花啤酒', 4.00, 1),
        (77, 36, 61, '剁椒鱼头', 66.00, 1),
        (78, 36, 56, '清炒西兰花', 18.00, 1),
        (79, 32, 63, '香锅牛蛙', 88.00, 1),
        (80, 32, 57, '炝炒圆白菜', 18.00, 1),
        (81, 32, 54, '清炒小油菜', 18.00, 1),
        (82, 32, 49, '米饭', 2.00, 2);
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `user_id` bigint NOT NULL COMMENT '主键',
  `dish_id` bigint DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '口味',
  `number` int NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='购物车';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '微信用户唯一标识',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `id_number` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户信息';
insert into sky_take_out.user (id, openid, name, phone, sex, id_number, avatar, create_time)
values  (4, 'oSlON6ytAAmiI5YmKRK-NXCjlrNk', null, null, null, null, null, '2024-03-10 15:58:35');