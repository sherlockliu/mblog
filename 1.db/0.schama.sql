
-- ----------------------------
-- Table structure for managers
-- ----------------------------
DROP TABLE IF EXISTS managers;
CREATE TABLE managers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `user_name` varchar(100) NOT NULL COMMENT '用户名称',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `password_salt` varchar(100) NOT NULL COMMENT '密码加盐',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `remark` varchar(1000) DEFAULT NULL,
  `real_name` varchar(1000) DEFAULT NULL COMMENT '真实名称',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;