
-- ----------------------------
-- Table structure for managers
-- ----------------------------
DROP TABLE IF EXISTS managers;
CREATE TABLE managers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL COMMENT '��ɫID',
  `user_name` varchar(100) NOT NULL COMMENT '�û�����',
  `password` varchar(100) NOT NULL COMMENT '����',
  `password_salt` varchar(100) NOT NULL COMMENT '�������',
  `create_date` datetime NOT NULL COMMENT '��������',
  `remark` varchar(1000) DEFAULT NULL,
  `real_name` varchar(1000) DEFAULT NULL COMMENT '��ʵ����',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;