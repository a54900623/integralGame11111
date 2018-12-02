package com.party.game.common.utils;

/**
 * 异常编码
 * party
 * Created by wei.li
 * on 2016/9/22 0022.
 */
public class PartyCode {


    //参数不合法
    public final static Integer PARAMETER_ILLEGAL = 10000;

    //数据被删除
    public final static Integer DATA_IS_BE_DELETE = 10001;

    //数据被不存在
    public final static Integer DATA_UNEXIST = 10002;

    //手机验证码不一致
    public final static Integer VERIFY_CODE_ERROR = 10010;

    //会员密码不匹配
    public final static Integer PASSWORD_UNMATCH = 10020;

    //会员用户不存在
    public final static Integer MEMBER_UNEXIST = 10021;

    //手机号码已经存在
    public final static Integer PHONE_EXIST = 10030;

    //该手机号与当前登录用户手机号一致，不用修改
    public final static Integer RESET_MOBILE_SAME_PHONE_CODE = 10031;

    //当前用户数据错误,请重新登录
    public final static Integer CURRENT_USER_DATA_ERROR = 10032;

    //当前输入的地区id有误，没有找到相应的地区信息
    public final static Integer AREA_DATA_ERROR = 10033;

    //当前输入的行业id有误，没有找到相应的地区信息
    public final static Integer INDUSTRY_DATA_ERROR = 10034;


    //活动不存在
    public final static Integer ACTIVITY_UNEXIST = 10040;

    //商品不存在
    public final static Integer GOODS_UNEXIST = 10050;

    //获取微信授权错误
    public final static Integer OATHORIZEURL_ERROR = 10070;

    //订单号不存在
    public final static Integer ORDER_UNEXIST = 10060;

    //订单状态不正确
    public final static Integer PAYMENT_STATUS_ERROR = 10061;


    //微信统一下单异常
    public final static Integer UNIFIEDORDER_ERROR = 10062;

    //生成核销码异常，订单数量为0
    public final static Integer CREATE_VERIFY_CODE_ERROR = 10063;

    //生成签名错误
    public final static Integer ALIPAY_SIGN_ERROR = 10064;


    //订单已支付，不能取消
    public final static Integer ORDER_HAVE_PAID = 10065;

    //获取微信用户信息错误
    public final static Integer OPEN_ID_ERROR = 10080;

    //分销功能未开放
    public final static Integer DISTRIBUTION_NOT_OPEN = 10090;

    //用户不存在
    public final static Integer USER_UNEXIST = 10100;

    //用户密码不匹配
    public final static Integer USER_PASSWORD_UNMATCH = 10101;

    //商铺商品不存在
    public final static Integer STORE_GOODS_UNEXIST = 10110;

    //未使用第三方授权账号登陆
    public final static Integer BIND_PHONE_UNUSE_OPEN_ID = 10120;

    //已经绑定手机号
    public final static Integer BIND_PHONE_HAS_BINDED = 10121;

    //广告不存在
    public final static Integer AD_NOT_EXIST = 10130;

    //报名数据异常
    public final static Integer JOIN_ACT_MEMBERACT_ERROR = 10140;

    //活动订单数据异常
    public final static Integer JOIN_ACT_ORDER_ERROR = 10141;

    //当前用户与报名用户不是同一个人
    public final static Integer JOIN_ACT_MEMBER_ERROR = 10142;

    //活动报名状态错误
    public final static Integer JOIN_ACT_STATUS_ERROR = 10143;

    //活动报名截止时间错误
    public final static Integer JOIN_ACT_ENDDATE_ERROR = 10144;

    //报名人数上限错误
    public final static Integer JOIN_ACT_LIMITNUM_ERROR = 10145;

    //删除消息错误，消息主键id错误
    public final static Integer DEL_MSG_ERROR = 10150;

    //删除消息失败
    public final static Integer DEL_MSG_FAIL = 10151;

    //消息不存在
    public final static Integer MSG_NON_EXISTENTT = 10152;

    //没有关注的人，改为推荐达人动态列表
    public final static Integer LIST_FOCUS_DYNAMIC_ERROR = 10160;

    //没有登录，改为推荐达人动态列表
    public final static Integer LIST_FOCUS_DYNAMIC_NOT_LOGIN_ERROR = 10161;

    //订单不存在
    public final static Integer ORDER_FORM_NOT_EXIT = 10170;

    //订单数据错误，下单人与当前用户不是同一人
    public final static Integer ORDER_FORM_DATA_ERROR = 10171;

    //订单数据错误，商品或活动不存在
    public final static Integer ORDER_FORM_GOODS_NOT_EXIT = 10172;

    //要关注的会员不存在
    public final static Integer FOCUS_ON_MEMBER_NOT_EXIT = 10180;

    //要取消关注的会员不存在
    public final static Integer REMOVE_FOCUS_ON_MEMBER_NOT_EXIT = 10181;

    //当前登录用户已关注该会员
    public final static Integer FOCUS_ON_MEMBER_HAVE_DOWN = 10182;

    //当前登录用户没有关注该会员
    public final static Integer REMOVE_FOCUS_ON_MEMBER_HAVE_DOWN = 10183;

    //动态发布不成功
    public final static Integer PUBLISH_DYNAMIC_ERROR = 10184;

    //动态的主键id错误，数据不存在
    public final static Integer DYNAMIC_NOT_EXIT = 10185;

    //生成名片信息错误，会员数据不存在
    public final static Integer CREATE_BUSINESS_CARD_ERROR = 10186;

    //会员资料不公开
    public final static Integer CREATE_BUSINESS_NOT_OPEN = 10187;

    //会员资料未审核通过
    public final static Integer USER_PROFILE_UN_PASS = 10188;

    //文章内容不存在
    public final static Integer ARTICLE_NOT_EXIT = 10190;
    
    // 商品超出每人限购量
    public final static Integer GOODS_NUM_MEMBER_MORE = 10191;
    
    // 商品购买量超出库存
    public final static Integer GOODS_NUM_MORE = 10192;

    //分销已经存在
    public final static Integer DISTRIBUTOR_EXSIT = 10200;

    //众筹支持金额不能大于剩余金额
    public final static Integer CROWDFUNF_SUPPORT_PASS = 10300;
    
    //当前用户不是该圈子的创建者
    public final static Integer CIRCLE_NO_CREATOR = 10401;

    //当前用户是圈子成员
    public final static Integer CIRCLE_IS_CIRCLE_MEMBER = 10402;

    //已经加入小组
    public final static Integer SIGIN_GROUP_HAVE = 10500;

    //未报名
    public final static Integer SIGN_APPLY_NOT = 10501;

    // 角色不匹配
    public final static Integer ROLE_NOT_MATCH = 10700;

    //数据不存在
    public final static Integer IS_NO_DATA = 10600;

    //邀请码不存在
    public final static Integer INVITE_CODE_ERROR = 10107;
}
