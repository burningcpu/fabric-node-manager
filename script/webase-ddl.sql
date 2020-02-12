
    -- ----------------------------
    -- Table structure for tb_channel
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_channel (
            channel_id int(11) NOT NULL AUTO_INCREMENT COMMENT '通道编号',
            channel_name varchar(64) NOT NULL COMMENT '通道名称',
            channel_status int(1) DEFAULT '1' COMMENT '状态（1-正常 2-异常）',
            peer_count int DEFAULT '0' COMMENT '通道下节点数',
            create_time datetime DEFAULT NULL COMMENT '创建时间',
            modify_time datetime DEFAULT NULL COMMENT '修改时间',
            PRIMARY KEY (channel_id)
        ) COMMENT='通道信息表' ENGINE=InnoDB CHARSET=utf8;



    -- ----------------------------
    -- Table structure for tb_peer
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_peer (
      peer_id int(11) NOT NULL AUTO_INCREMENT COMMENT '节点编号',
      channel_id int(11) NOT NULL COMMENT '所属通道编号',
      peer_name varchar(120) NOT NULL COMMENT '节点名称',
      peer_url varchar(50) NOT NULL COMMENT '节点请求路径',
      peer_ip varchar(16) DEFAULT NULL COMMENT '节点ip',
      peer_port int(11) NOT NULL COMMENT '节点端口',
      block_number bigint(20) DEFAULT '0' COMMENT '节点块高',
      description text DEFAULT NULL COMMENT '描述',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (peer_id),
      unique  unique_channel_peer (channel_id,peer_name)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点表';


    -- ----------------------------
    -- Table structure for tb_chain_code
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_chain_code (
      chain_code_pk int(11) NOT NULL AUTO_INCREMENT COMMENT '链码自增编号',
      channel_id int(11) NOT NULL COMMENT '所属通道编号',
      chain_code_lang varchar(64) DEFAULT "GO" COMMENT '链码语言（JAVA、GO）',
      chain_code_name varchar(120) NOT NULL COMMENT '链码名称',
      chain_code_version varchar(120) DEFAULT NULL COMMENT '链码版本',
      chain_code_source_base64 text COMMENT 'base64转码后的合约源码',
      chain_code_id varchar(64) DEFAULT NULL COMMENT '合约编号',
      deploy_time datetime DEFAULT NULL COMMENT '部署时间',
      chain_code_status int(1) DEFAULT '1' COMMENT '部署状态（1：未部署，2：部署成功，3：部署失败）',
      description text COMMENT '描述',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (chain_code_pk),
      unique  unique_name_version (channel_id,chain_code_name,chain_code_version),
      unique  unique_id (channel_id,chain_code_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='链码表';


    -- ----------------------------
    -- Table structure for tb_front
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_front (
      front_id int(11) NOT NULL AUTO_INCREMENT COMMENT '前置服务编号',
      front_ip varchar(16) NOT NULL COMMENT '前置服务ip',
      front_port int(11) DEFAULT NULL COMMENT '前置服务端口',
      agency varchar(32) NOT NULL COMMENT '所属机构名称',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (front_id),
      unique  unique_ip_port (front_ip,front_port)
    ) ENGINE=InnoDB AUTO_INCREMENT=500001 DEFAULT CHARSET=utf8 COMMENT='前置服务信息表';


    -- ----------------------------
    -- Table structure for tb_user_key_mapping
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_front_channel_map (
      map_id int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
      front_id int(11) NOT NULL COMMENT '前置服务编号',
      channel_id int(11) NOT NULL COMMENT '通道编号',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (map_id),
      unique  unique_front_channel (front_id,channel_id)
    ) ENGINE=InnoDB AUTO_INCREMENT=600001 DEFAULT CHARSET=utf8 COMMENT='前置通道映射表';




    -- ----------------------------
    -- Table structure for tb_trans_daily
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_trans_daily (
      channel_id int(11) NOT NULL COMMENT '所属通道编号',
      trans_day date NOT NULL COMMENT '日期',
      trans_count int(11) DEFAULT '0' COMMENT '交易数量',
      trans_number int(11) DEFAULT '0' COMMENT '当前统计到的交易编号',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (channel_id,trans_day)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日交易数据表';




    -- ----------------------------
    -- Table structure for tb_account_info
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_account_info (
      account varchar(50) binary NOT NULL COMMENT '系统账号',
      account_pwd varchar(250) NOT NULL COMMENT '登录密码',
      role_id int(11) NOT NULL COMMENT '所属角色编号',
      login_fail_time int(2) NOT NULL DEFAULT '0' COMMENT '登录失败次数,默认0，登录成功归0',
      account_status int(1) NOT NULL DEFAULT '1' COMMENT '状态（1-未更新密码 2-正常） 默认1',
      description text COMMENT '备注',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (account)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统账号信息表';


    -- ----------------------------
    -- Table structure for tb_role
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_role (
      role_id int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
      role_name varchar(120) DEFAULT NULL COMMENT '角色英文名称',
      role_name_zh varchar(120) DEFAULT NULL COMMENT '角色中文名称',
      role_status int(1) DEFAULT '1' COMMENT '状态（1-正常2-无效） 默认1',
      description text COMMENT '备注',
      create_time datetime DEFAULT NULL COMMENT '创建时间',
      modify_time datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (role_id),
      UNIQUE KEY UK_role_Name (role_name)
    ) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8 COMMENT='角色信息表';


    -- ----------------------------
    -- Table structure for tb_token
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS tb_token (
      token varchar(120) NOT NULL PRIMARY KEY COMMENT 'token',
      value varchar(50) NOT NULL COMMENT '与token相关的值（如：用户编号，图形验证码值）',
      expire_time datetime DEFAULT NULL COMMENT '失效时间',
      create_time datetime DEFAULT NULL COMMENT '创建时间'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='token信息表';



-- ---------------------------------------------------------------------------------
------------------------------ 以下根据channel动态建表 ------------------------------
-- ---------------------------------------------------------------------------------


-- ----------------------------
-- Table structure for tb_block_{channelId}
-- ----------------------------
 CREATE TABLE IF NOT EXISTS ${tableName}(
        pk_hash varchar(128) NOT NULL COMMENT '块hash值（由块hash转成的字符串）',
        block_number bigint(20) NOT NULL COMMENT '快高',
        trans_count bigint(20) DEFAULT '0' COMMENT '块包含的交易数',
        create_time datetime DEFAULT NULL COMMENT '创建时间',
        modify_time datetime DEFAULT NULL COMMENT '修改时间',
        PRIMARY KEY (pk_hash),
        KEY index_number (block_number)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区块信息表';


-- ----------------------------
-- Table structure for tb_transaction_{channelId}
-- ----------------------------
 CREATE TABLE IF NOT EXISTS ${tableName} (
        txId varchar(128) NOT NULL COMMENT 'txId',
        block_number bigint(25) NOT NULL COMMENT '所属区块',
        action_count int(11) DEFAULT NULL COMMENT '事件数（即：transactionActionInfo）',
        trans_timestamp datetime NOT NULL COMMENT '交易发生时间',
        envelope_type varchar(25) NOT NULL COMMENT '交易类型（TRANSACTION_ENVELOPE,ENVELOPE）',
        create_time datetime DEFAULT NULL COMMENT '创建时间',
        modify_time datetime DEFAULT NULL COMMENT '修改时间',
        PRIMARY KEY (txId),
        KEY index_flag (statistics_flag)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易信息表';



-- ----------------------------
-- Table structure for tb_transaction_action_{channelId}
-- ----------------------------
 CREATE TABLE IF NOT EXISTS ${tableName} (
        action_id int(11) NOT NULL AUTO_INCREMENT COMMENT '事件编号',
        trans_id varchar(128) NOT NULL COMMENT '所属交易id',
        chain_code_name varchar(128) NOT NULL COMMENT '链码名称',
        chain_code_path varchar(128) NOT NULL COMMENT '链码路径',
        chain_code_version varchar(128) NOT NULL COMMENT '链码版本',
        input_args text NOT NULL COMMENT '请求参数（数组形式记录多个入参）',
        response_msg varchar(128) NOT NULL COMMENT '返回信息',
        response_status varchar(10) NOT NULL COMMENT '返回状态',
        create_time datetime DEFAULT NULL COMMENT '创建时间',
        modify_time datetime DEFAULT NULL COMMENT '修改时间',
        PRIMARY KEY (action_id),
        KEY index_chain_code (chain_code_name)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件信息表';