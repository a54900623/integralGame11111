package com.party.game.common.constant;

/**
 * 静态常量类
 * Created by yifeng
 *
 * @date 2017/2/25 0025
 * @time 10:01
 */
public class Constant {

    //成功
    public static final Integer IS_SUCCESS = 1;

    //失败
    public static final Integer IS_FAIL = 0;

    // 众筹中
    public static final Integer IS_CROWFUND_ING = 0;
    // 成功
    public static final Integer IS_CROWFUND_SUCCESS = 1;
    // 失败
    public static final Integer IS_CROWFUND_FAIL = 2;

    //众筹成功
    public static final Integer CROWDFUND_PROJECT_SUCCESS = 1;

    //众筹失败
    public static final Integer CROEDFUND_PROJECT_FAIL = 2;

    //众筹退款中
    public static final Integer CROWDFUND_PROJECT_REFUNDING = 3;

    //众筹退款成功
    public static final Integer CROWDFUND_PROJECT_REFUNDED = 4;

    public static final Integer SUPPORT_PAY_STATUS_UNPAID = 0;
    //支持支付成功
    public static final Integer SUPPORT_PAY_STATUS_SUCCESS = 1;
    //支持退款中
    public static final Integer SUPPORT_PAY_STATUS_REFUNDING = 2;
    //支持支付状态
    public static final Integer SUPPORT_PAY_STATUS_REFUND = 3;
    //退款失败
    public static final Integer SUPPORT_PAY_STATUS_FAIL = 4;

    //是众筹活动
    public static final Integer IS_CROWDFUNDED = 1;

    //是直接付款众筹活动
    public static final Integer IS_DIRECTPAY_CROWDFUNDED = 1;

    //不是众筹活动
    public static final Integer NOT_CROWDFUNDED = 0;

    //创建者的分销父编号
    public static final String BUILDER_PARENT = "0";

    //允许分销
    public static final Integer ALLOW_DISTRIBUTION = 1;

    //禁止分销
    public static final Integer BAN_DISTRIBUTION = 0;

    public static final String BUILDER_DECLARATION = "世界上一定还有另外一个我， 做着我想做而又不能做的事";

    //微信支付成功
    public static final String WECHAT_SUCCESS = "SUCCESS";

    //utf-8编码方式
    public static final String UTF_8 = "utf-8";

    //支付宝支付状态码支付成功
    public static final String ALI_SUCCESS_CODE = "10000";

    //微信支付交易类型
    public static final String APP = "APP";
    //微信支付交易类型
    public static final String JSAPI = "JSAPI";
    // 小程序
    public static final String CLIENT_WX_XCX = "wx_xcx";
    // 微网站
    public static final String CLIENT_WX_WWZ = "wx_wwz";
    // PC
    public static final String CLIENT_WX_PC = "wx_pc";
    // APP
    public static final String CLIENT_WX_APP = "wx_app";
    // APP
    public static final String CLIENT_ALI_APP = "ali_app";
    // PC
    public static final String CLIENT_ALI_PC = "ali_pc";

    //众筹类型
    public static final String CROWD_FUND_TYPE_ACTIVITY = "ACTIVITY";

    //短信通道
    public static final String MESSAGE_CHANNEL_SMS = "sms";

    //jpush通道
    public static final String MESSAGE_CHANNEL_JPUSH = "jpush";

    //app通道
    public static final String MESSAGE_CHANNEL_APP = "app";

    //email通道
    public static final String MESSAGE_CHANNEL_EMAIL = "email";

    //wechat通道
    public static final String MESSAGE_CHANNEL_WECHAT = "wechat";

    // 微信小程序通道
    public static final String MESSAGE_CHANNEL_WECHAT_XCX = "wechat_xcx";

    //type的key
    public static final String TYPE_KEY = "type";

    //object的key
    public static final String OBJECT_KEY = "object";

    //是否写入key
    public static final String IS_WRITE = "isWrite";

    //任务调度的参数key
    public static final String JOB_PARAM_KEY    = "jobParam";

    //自动事件
    public static final String EVENT_AUTO = "auto";

    //手动事件
    public static final String EVENT_HAND = "hand";

    //随机数长度
    public static final int RANDOM_LENGTH = 32;

    //MD5 编码
    public static final String MD5_TYPE = "MD5";

    //用户未审核
    public static final Integer USER_STATUS_UNAUDITED = 0;

    //微信返回状态码
    public static final String WECHAT_ERR_CODE = "errcode";

    //成功状态
    public static final Integer WECHAT_CODE_SUCCESS = 0;

    //微信消息字体颜色
    public static final String WECHAT_MESSAGE_COLOR = "#173177";

    public static final String WECHAT_NOTIFY_KEY = "wechatNotify";

    //微信账户类型系统类型
    public static final String WECHAT_ACCOUNT_TYPE_SYSTEM ="SYSTEM";

    // 微信模板类型
    public static final String WECHAT_TEMPLATE_TYPE = "SYSTEM_XCX";

    // 微信小程序——运动打卡
    public static final String WECHAT_ACCOUNT_TYPE_SYSTEM_XCX_SIGN = "SYSTEM_XCX_SIGN";

    // 微信小程序——相册
    public static final String WECHAT_ACCOUNT_TYPE_SYSTEM_XCX_PHOTO = "SYSTEM_XCX_PHOTO";

    //微信账户类型合作商类型
    public static final String WECHAT_ACCOUNT_TYPE_PARTNER ="PARTNER";

    // 活动
    public static final String BUSINESS_TYPE_ACTIVITY = "ACTIVITY";

    // 课程
    public static final String BUSINESS_TYPE_COURSE = "COURSE";

    public static final String REFUND_ALL_DES = "订单已全额退款";

    /********************  短信发送常量  **********************/
    // 发给众筹项目
    public static final Integer SEND_SHORT_MESSAGE_TYPE_CROWDFUN_PROJECT = 1;

    // 发给众筹事项
    public static final Integer SEND_SHORT_MESSAGE_TYPE_Event = 2;

    // 发给活动
    public static final Integer SEND_SHORT_MESSAGE_TYPE_ACTIVITY = 3;

    /********************  短信发送常量  **********************/

    //excel路径
    public static final String EXCEL_URL = "/excel/";

    //压缩包路径
    public static final String ZIP_URL = "/zip/";

    //批量导出路径
    public static final String BATCH = "batch";

    //压缩包名称前缀
    public static final String PRE_ZIP_URL = "zip_url_";

    //是否需要验证码
    public static final String IS_NEED_VERIFY = "is_need_verify";

    //成功
    public static final String STR_TRUE = "true";

    //失败
    public static final String STR_FALSE = "false";

    //父级编号
    public static final String TOP_PARENT_ID = "0";

    //关注连接
    public static final String ATTENTION_URL_KEY = "ATTENTION_URL_KEY";

    public static final String ATTENTION_UNIQUE_CODE_KEY = "ATTENTION_UNIQUE_CODE_KEY";

    public static final String ATTENTION_PARTNER_NAME = "ATTENTION_PARTNER_NAME";

    //授权配置
    public static final Integer DEPLOY_TYPE_AUTH = 0;

    //填写配置
    public static final Integer DEPLOY_TYPE_FILL = 1;

    //中国大陆手机区号
    public static final String CHINA_MOBILE_CODE = "+86";

    //身份证验证码
    public static final String IDCARD_VERIFY_CODE = "idcard_verify_code";

    //是否需要身份验证
    public static final String IS_NEED_IDCARD_VERIFY = "is_need_idcard_verify";

    public static final String FIELD_RESOURCE = "FIELD_RESOURCE";

    public static final String FIELD_RESOURCE_TITLE = "来源";

    public static final String FIELD_RESOURCE_DESCRIPTION = "请输入来源";

    public static final String FIELD_RESOURCELINK = "FIELD_RESOURCELINK";

    public static final String FIELD_RESOURCELINK_TITLE = "来源链接";

    public static final String FIELD_RESOURCELINK_DESCRIPTION = "请输入来源链接";

    public static final String MY_CROWDFUND = "MY_CROWDFUND";

    public static final String MY_CIRCLE = "MY_CIRCLE";

    public static final String MY_SIGN = "MY_SIGN";

    public static final String MY_PHOTO = "MY_PHOTO";

    public static final String MY_SUBJECT = "MY_SUBJECT";

    //验证框弹出剩于次数
    public static final String VERIFYBOX_POPUP_NUM = "verifyBox_popup_num";

    // 腾讯云Excel上传路径
    public static final String UPLOAD_EXCEL = "/" + BATCH + EXCEL_URL + "%s/%s";
    // 腾讯云Zip上传路径
    public static final String UPLOAD_ZIP = "/" + BATCH + ZIP_URL + "%s";
    // 文件夹名称不能包含的特殊字符
    public static final String[] DIR_NOT_CONTAIN = new String[]{
            "\\\\", "/", ":", "\\*", "\\?", "\\\"", "<", ">", "\\|"
    };

    // 加
    public static final String OPTION_ADD = "add";
    // 减
    public static final String OPTION_SUB = "sub";

    /**
     * 逐个替换特殊字符
     *
     * @param dirFileName 源文件名
     * @return 替换后的新文件名
     */
    public static String replaceDirSpecial(String dirFileName) {
        for (String special : DIR_NOT_CONTAIN) {
            dirFileName = dirFileName.replaceAll(special, "");
        }
        return dirFileName;
    }
}
