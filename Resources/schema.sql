Create Database If Not Exists supply Character Set UTF8;
Use supply;
Create Table If Not Exists `iouLimitEntity`(
  `orgID` varchar(64) NOT NULL COMMENT '机构ID',
  `orgName` varchar(100) NOT NULL COMMENT '机构名称',
  `password` varchar(300) NOT NULL COMMENT '登录密码',
  `iouLimit` int(11) NOT NULL COMMENT '白条额度',
  `createTime` varchar(32) NOT NULL COMMENT '创建时间',
  `updateTime` varchar(32) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`orgID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构';
--主键可以是orgID吗?auto_increatement=1000有影响吗?

Create Table If Not Exists `iouRecord`(
  `iouId` varchar(64) NOT NULL COMMENT '白条ID',
  `fromOrg` varchar(64) NOT NULL COMMENT '发行机构',
  `recvOrg` varchar(64) NOT NULL COMMENT '接收机构',
  `transTime` varchar(32) NOT NULL COMMENT '交易时间',
  `amount` int(11) NOT NULL COMMENT '交易金额',
  `paidAmt` int(11) NOT NULL COMMENT '已还金额',
  `iouStatus` varchar(4) NOT NULL COMMENT '白条状态',
  `updateTime` varchar(32) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`iouId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='白条记录';
--主键可以是iouId吗?auto_increatement=1000有影响吗?

Create Table If Not Exists `transaction`(
  `conID` varchar(64) NOT NULL COMMENT '合同ID',
  `saleOrg` varchar(64) NOT NULL COMMENT '销售方机构',
  `buyOrg` varchar(64) NOT NULL COMMENT '购买方机构',
  `transType` varchar(4) NOT NULL COMMENT '交易类型',
  `amount` int(11) NOT NULL COMMENT '白条金额',
  `conHash` varchar(300) NOT NULL COMMENT '合同hash',
  `latestStatus` varchar(4) NOT NULL COMMENT '最新状态',
  `transTime` varchar(32) NOT NULL COMMENT '交易时间',
  `updateTime` varchar(32) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`conID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易';
--主键可以是conID吗?auto_increatement=1000有影响吗?




-- 初始化图书数据
--INSERT INTO `book` (`book_id`, `bookName`, `bookPrice`)
--VALUES
--    (1000, 'Java程序设计', 10),
--    (1001, '数据结构', 20),
--    (1002, '设计模式', 30),
--    (1003, '编译原理', 40);
