package com.thinkgem.jeesite.modules.lianlianPay.until;

public interface Globals{

    // XML打包选项
    String  TRANS_TRANSSVR_MODE               = "0";                                                                         // 交易服务远程调用
    String  TRANS_DATASVR_MODE                = "1";                                                                         // 数据服务远程调用
    String  TRANS_SQLMODE_QUERY               = "0";                                                                         // 查询模式
    String  TRANS_SQLMODE_UPDATE              = "1";                                                                         // 更新修改模式
    String  TRANS_RPC_RETCODE                 = "0";                                                                         // 远程调用返回成功码
    String  TRANS_RPC_ERROR_MSG               = "交易失败";                                                                      // SQL语句返回成功消息
    int     TRANS_RETURN_SUCCESS_LENGTH       = 8;                                                                           // 前置返回的代码长度
    String  TRANS_FILE_FLAG                   = "1";                                                                         // 远程调用返回文件
    String  TRANS_SQLCODE                     = "0";                                                                         // SQL语句返回成功代码
    String  TRANS_SQLMSG                      = "SQL执行错误";                                                                   // SQL语句返回错误信息
    String  TRANS_RETCODE                     = "0";                                                                         // 交易返回成功代码
    String  TRANS_RETMSG                      = "交易失败";                                                                      // 交易返回错误信息
    String  TRANS_SPTRIM                      = "1";                                                                         // 截取前后空格
    String  TRANS_RETURN_COUNT                = "1";                                                                         // 需要返回总记录数
    String  TRANS_MAX_COUNT                   = "100";                                                                       // 返回最大总记录数
    String  TRANS_NORMAL_DADE                 = "2";                                                                         // 标准时间格式YYYY-MM-DD
    String  TRANS_NORMAL_DADETIME             = "8";                                                                         // 标准时间格式YYYYMMDDHHMMSS
    String  TRANS_NORMAL_DADE2                = "4";                                                                         // 标准时间格式YYYYMMDD
    String  TRANS_RESULTMODE                  = "1";                                                                         // 以文件形式返回结果
    String  TRANS_FILEMODE                    = "1";                                                                         // 以普通形式返回文件
    String  TRANS_NOPURVIUE_MSG               = "对不起，你无权限进行此操作!";                                                            // 无权限提示信息
    String  TRANS_XRPC_VERSION                = "1.0";                                                                       // xrpc版本号
    String  TRANS_MAXCODE_VALUE               = "6";                                                                         // 返回最大条数6条
    String  TRANS_SERIAL_PERPAGE_COUNT        = "50";                                                                        // 流水查询每页显示60条
    String  TRANS_QUERY_NORESULT_MSG          = "无此相关信息!";
    String  TRANS_WEB_DITCH                   = "03";                                                                        // web渠道代码
    // XML消息解包lsit节点
    String  TRANS_RESULT_LIST_NODE_NAME       = "//transsvr_resp/records/rows";
    String  TRANS_RESULT_BODY_NODE_NAME       = "//transsvr_resp/body";
    String  TRANS_RESULT_DISTRIBUTE_NODE_NAME = "//transsvr_resp/body/PARAMETER_DICT";                                       // 分布
    String  ACTIVEMQ_MESSAGE_DISTRIBUTE_RULE  = "ddbs";                                                                      // 数据库分布规则
    int     TRANSMODE_SOCKET                  = 1;                                                                           // socket通讯
    int     TRANSMODE_ACTIVEMQ                = 2;                                                                           // activemq通讯;

    String  SMS_SYS_NAME                      = "ZJB";                                                                       // 短信系统名称(资金宝)
    Integer MAX                               = new Integer(2147483647);
    String  REQUEST_USERINFO                  = "userInfo";                                                                  // request中的userinfo
    String  PAGE_TOKEN_KEY                    = "token";
    String  PAGE_TOKEN_VALUE                  = "yes";
    String  MANAGE_DATA_FLUSH                 = "MANAGE_DATA_FLUSH";

    String  TRANS_QUERY_RESULT_LIST           = "result";
    String  TRANS_QUERY_RESULT_COUNT          = "count";
    String  URL_PATTEN                        = ".htm";
    String  URL_PAGE                          = "page";
    String  URL_PERPAGENUM                    = "perpagenum";
    String  URL_TOTALCOUNTS                   = "totalcounts";

    String  XML_HEADER                        = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";                                // xml头
    String  CONTENT_TYPE_JSON_UTF8            = "application/json; charset=UTF-8";

    String  TRANS_FAIL_RETCODE                = "999999";                                                                    // 交易链路成功返回代码
    String  TRANS_FAIL_RETMSG                 = "交易失败";

    String  TRANS_SUCCESS_RETCODE             = "000000";                                                                    // 交易链路成功返回代码
    String  TRANS_SUCCESS_RETMSG              = "交易成功";

    String  TRANS_USER_IFEXISTS               = "111000";                                                                    // 用户信息不存在
    String  TRANS_USER_NOEXISTS               = "111047";                                                                    // 用户登陆信息不存在
    String  BIND_USER                         = "111024";
    String  TRANS_CHECK_INVALID               = "111005";                                                                    // 验证码失效
    String  TRANS_CHECK_NBR                   = "111038";                                                                    // 验证码验证次数超限
    String  SECUREQA_CHECK_NBR                = "111021";                                                                    // 密保错误次数超出允许次数

    /**
     * 手机、邮件验证码不匹配:111004
     */
    String  MOB_EML_CHKVALID_WARING           = "111004";                                                                    // 手机、邮件验证码不匹配
    /**
     * 手机、邮件验证码已失效:111005
     */
    String  MOB_EML_CHKVALID_ABATE            = "111005";                                                                    // 手机、邮件验证码已失效
    /**
     * 手机、邮件验证次数超限:111038
     */
    String  MOB_EML_CHLVALID_OUTTMS           = "111038";                                                                    // 手机、邮件验证次数超限

    String  SYS_SUCCESS                       = "0000";                                                                      // 其他系统成功状态

    String  USER_ACCT_NOTEXSIT                = "112000";
    String  LOGIN_STAT_NOTEXSIT               = "111000";
    String  LOGIN_STAT_PASSWORD_WRONG         = "111001";
    String  LOGIN_STAT_LOGOUT                 = "111106";                                                                    // 登录状态注销
    String  LOGIN_STAT_APPLY                  = "111103";                                                                    // 登录状态注销
    String  LOGIN_STAT_LOCKED                 = "111107";                                                                    // 登录状态申请
    String  LOGIN_STAT_CONTINUE               = "111108";                                                                    // 登录状态暂停

    // 数字标识定义
    String  FLAG_NEG_1                        = "-1";
    String  FLAG_0                            = "0";
    String  FLAG_1                            = "1";
    String  FLAG_2                            = "2";
    String  FLAG_3                            = "3";
    String  FLAG_4                            = "4";
    String  FLAG_5                            = "5";
    String  FLAG_8                            = "8";
    String  FLAG_9                            = "9";

    int     CERTIFICATE_EXEC                  = 999;

    String  ACTION_GO_FINISH                  = "GO_FINISH";
    String  ACTION_GO_VERIFY                  = "GO_VERIFY";

    // 密码找回方式
    String  FIND_MOB                          = "FIND_MOB";
    String  FIND_EMAIL                        = "FIND_EMAIL";
    String  FIND_SEC_PRO                      = "FIND_SEC_PRO";

    int     LOG_TYPE_LOG                      = 1;                                                                           // 记log日志
    int     LOG_TYPE_ERR                      = 2;                                                                           // 记错误日志
    int     PACK_HEAD_LENGTH                  = 4;                                                                           // 包头长度的存放长度位为4位
    String  MOBILE_SERPRATE_FLAG              = "#";                                                                         // 手机分隔符

    // 单点登录相关参数
    String  SINGLE_LOGIN_OID_USERLOGIN        = "oid_userlogin";                                                             // 用户登录名
    String  SINGLE_LOGIN_OID_SESSION          = "sessionId";                                                                 // sessionID
    // cookie授权码
    String  USER_LOGIN_LICENSE_COOKIE         = "license";                                                                   // cookie中授权码的key值
    // 通讯相关配置参数
    String  ACTIVEMQ_MESSAGE_MACHINE_ID       = "machineid";                                                                 // 机器名

    // 模拟包头的选项,以后从session中去取
    String  TRANS_PRO_NO                      = "01";                                                                        // 省份代码
    String  TRANS_AREA_NO                     = "0001";                                                                      // 地区代码
    String  TRANS_OPER_NO                     = "9993";                                                                      // 操作员
    String  TRANS_OPER_PASSWD                 = "031111";                                                                    // 密码

    // 用户登录
    String  USER_LOGIN                        = "USER_LOGIN";                                                                // 用户登录
    // 用户密码相关交易代码定义
    String  TRANS_RESULT_ERRORMSG             = "errorMsg";                                                                  // 交易结果消息
    String  TYPE_LOGIN_PASS                   = "0";                                                                         // 登录密码类型
    String  TYPE_PAY_PASS                     = "1";                                                                         // 支付密码类型
    String  SERVICE_ID                        = "004";                                                                       // 生成用户号的服务编号
    String  TYPE_LOGMAP_EMAIL                 = "1";                                                                         // 登录名类型：邮箱
    String  TYPE_LOGMAP_TEL                   = "0";                                                                         // 登录名类型：手机
    String  TYPE_LOGMAP_DEFINE                = "2";                                                                         // 登录名类型：自定义
    String  TYPE_LOGMAP_FIXED                 = "3";                                                                         // 固定电话
    String  TYPE_CARD_SFZ                     = "1";                                                                         // 身份证
    String  TYPE_CARD_HZ                      = "2";                                                                         // 护照
    String  TYPE_CARD_JGZ                     = "3";                                                                         // 军官证
    String  TYPE_CARD_SBZ                     = "4";                                                                         // 士兵证
    String  TYPE_LOGINA_USERINFO_NOPAYPWS     = "0";                                                                         // 未设置支付密码

    // UDP服务编号定义
    String  UDP_OID_ACCTNO                    = "1";                                                                         // 获取账户号
    String  UDP_OID_USERNO                    = "10";                                                                        // 获取用户号UDP
    String  UDP_CHARTEBILL_NO                 = "11";                                                                        // 充值单号

    String  STEP_COMMIT                       = "COMMIT";                                                                    // 步骤标识
    String  ACTION_ID_COMMIT                  = "ACTION_COMMIT";                                                             // 提交操作

    String  RESULT_KEY                        = "res_key";                                                                   // 结果标识
    String  RESULT_SUCC                       = "SUCC";                                                                      // 结果：成功
    String  RESULT_FAIL                       = "FAIL";                                                                      // 结果：失败

    String  INPOUR_EBANK                      = "EBANK";                                                                     // 充值--网银
    String  INPOUR_TENPAY                     = "TENPAY";                                                                    // 充值--财付通

    // 页面处理菜单值
    String  MENU_VALUES                       = "menu_values";
    // 图片内容类型定义
    String  IMAGE_CONTENT_TYPES               = "image/bmp,image/png,image/gif,image/jpeg,image/jpg,image/x-png,image/pjpeg";

    String  ACTION_ID_INDEX_ALL               = "aindex";
    String  ACTION_ID_INDEX_WFC               = "wfcindex";

    // help服务器地址
    String  HELPSYSTEM_NAME                   = "helpServer";
    // 静态服务器地址
    String  STATICSERVER_NAME                 = "staticServer";
    // 数字证书签发者
    String  ISSUER_DN                         = "issuerDN";
    /**
     * 本地已安装数字证书
     */
    String  CERT_INSTALLED                    = "1";
    /**
     * 本地未安装数字证书
     */
    String  CERT_UNINSTALL                    = "0";

    /**
     * session中验证码的key
     */
    String  CHECK_CODE                        = "check_code";
    /**
     * 登录页面跳转参数
     */
    String  GOTO_PRE                          = "goto";
    /**
     * 服务器启动模式:开发模式
     */
    String  SERVER_MODE_DEV                   = "dev";
    /**
     * 服务器启动模式:测试模式
     */
    String  SERVER_MODE_TEST                  = "test";
    /**
     * 服务器启动模式:生产模式
     */
    String  SERVER_MODE_RELEASE               = "release";
    String  RES_MSG                           = "res_msg";

    // **********************************************用户签约维护********************
    String  BANKSIGN_SIQ                      = "signedQueryBYNo";                                                           // 用户签约单条查询
    String  BANKSIGN_QUERY                    = "bankSignedQuery";                                                           // 用户签约多条查询
    String  BANKSIGN_EDIT                     = "bankSignedUpd";                                                             // 用户签约信息修改

    // *******************************************提现卡维护*************************
    String  HTTP_USERBANK_ADD                 = "http_userBankAdd";                                                          // 新增提现卡
    String  HTTP_USERBANK_DELETE              = "http_userBankDelete";                                                       // 删除提现卡
    String  HTTP_USERBANK_UPDATE              = "http_userBankModify";                                                       // 修改提现卡
    String  HTTP_USERBANK_SINGLEQUERY         = "http_userBankQuery";                                                        // 查询单条提现卡信息
    String  HTTP_USERBANK_MULTQUERY           = "http_userBankQuery";                                                        // 查询多条提现卡信息
    String  HTTP_CASH_APPLAY                  = "payBill_freezePay";
    String  HTTP_PAY_TOUSER                   = "payToUser.cgi";                                                             // 直接支付

    String  HTTP_VER_CTL_QUERY                = "wap/getVerCtlList";
    String  HTTP_CONFIG_QUERY                 = "wap/getConfigMap";                                                          // 查询多条提现卡信息
    // *********************** 订单相关 ***************
    String  HTTP_ORDER_CREATE                 = "createOrder";                                                               // 订单创建
    String  HTTP_GOODSITEM_QUERY              = "goodsitemSingleQuery";                                                      // 商品单条记录查询
    String  HTTP_CARD_GOODSITEM_QUERY         = "wap/singleQueryGI";                                                         // 点卡商品单条记录查询
    String  HTTP_BANK_BOC_SIGN                = "bocSign";                                                                   // 点卡商品单条记录查询
    String  HTTP_BANK_BOC_SIGN_QUERY          = "bocSignQuery";                                                              // 点卡商品单条记录查询
    String  HTTP_BANK_BOC_SIGN_MUL_QUERY      = "bocSignMulQuery";                                                           // 点卡商品单条记录查询
    String  HTTP_BANK_BOC_SIGN_CANCEL         = "bocSignCancel";                                                             // 点卡商品单条记录查询
    String  ORDER_TYPE_QB                     = "2";                                                                         // 订单类型：Q币充值
    String  ORDER_TYPE_HUAFEI                 = "0";                                                                         // 订单类型：话费充值
    String  ORDER_TYPE_DIANKA                 = "1";
    String  ORDER_TYPE_YOUHUI                 = "3";                                                                         // 订单类型：优惠券
    // 订单类型：点卡
    String  ORDER_GOODSITEM_STATE_AVAILABLE   = "0";                                                                         // 商品可用
    String  ORDER_GOODSITEM_STATE_UNAVAILABLE = "1";                                                                         // 商品不可用

    String  SYS_TAG_PHONE                     = "4";                                                                         // 银通手机服务端、日志系统标识
    // *******************订单页面类型说明***************
    String  BIZ_PAY                           = "付款";
    String  BIZ_RECHARGE                      = "充值";
    String  BIZ_PAYTRANS                      = "转账";
    String  BIZ_CASH                          = "提现";
    String  BIZ_CASH_GET                      = "收款";
    String  BIZ_OTHER                         = "其他";
    String  PWD_VALID_TIMES                   = "pwd_valid_times";
    String  PWD_LOGIN_FIX                     = "_login_pwd";
    String  PWD_PAY_FIX                       = "_pay_pwd";
    String  PWD_SIGNS_FIX                     = "_pay_signs";
    String  NOCARD_SIGN_FIX                   = "_nocard_sign";
    String  BIND_IDCARD_FIX                   = "_bind_idcard";
    String  USER_IDCARD_FIX                   = "_user_idcard";
    String  USER_AUTH_FIX                     = "_user_auth";

    // 用户注册默认信息
    String  DEFAULT_PAYPWD                    = "      ";                                                                    // 默认支付密码
    String  DEFAULT_LOGINPWD                  = "";                                                                          // 默认登陆密码
    String  DEFAULT_ID_CARD                   = "330101201312318683";                                                        // 默认身份证号
    String  DEFAULT_NAME_USER                 = "匿名用户";                                                                      // 默认用户姓名
    /*
     * 多条查询结果key
     */
    String  LIST_KEY                          = "result";                                                                    // 分页查询结果集key
    String  COUNT_KEY                         = "count";
    String  IS_REG                            = "1";
    String  IS_UN_REG                         = "0";                                                                         // 分页查询总记录数key

    String  OID_BIZ_REAL                      = "109001";                                                                    // 实物商品销售
    String  OID_BIZ_RECHARGE                  = "110001";                                                                    // 内部账户充值

    String                     ERROR_NOTARDER                    = "100000";
    String                     ERROR_PASSWORD                    = "200000";
}
