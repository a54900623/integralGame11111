package com.party.game.mobile.web.security;

import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import com.party.game.common.constant.Constant;
import com.party.game.common.redis.StringJedis;
import com.party.game.mobile.web.dto.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 生成登陆验证码
 * User:Created by wei.li
 * Date: on 2015/9/2.
 * Time:9:47
 */

@Controller
@RequestMapping(value = "/verification")
public class InsuranceVerificationCodeController {

    protected static Logger logger = LoggerFactory.getLogger(InsuranceVerificationCodeController.class);
    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
    private static String CHARACTERS = "23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ";
    private static Random random = new Random();

    //随机生成字母，数字，颜色
    static {
        cs.setColorFactory(new ColorFactory() {
            @Override
            public Color getColor(int i) {
                int[] c = new int[3];

                //生成随机数
                int x = random.nextInt(c.length);

                for (int j = 0; j < c.length; j++) {
                    if (j == x) {
                        c[j] = random.nextInt(71);
                    } else {
                        c[j] = random.nextInt(256);
                    }
                }
                return new Color(c[0], c[1], c[2]);
            }
        });
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters(CHARACTERS);
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }

    @Autowired
    private StringJedis stringJedis;

    /**
     * 根据身份证号码获取图片验证码
     *
     * @param request  请求参数
     * @param response 响应参数
     * @param idCard   身份证号码
     * @throws IOException
     */
    @RequestMapping("/idCard/crimg/{idCard}")
    public void mobileCrimg(HttpServletRequest request, HttpServletResponse response, @PathVariable String idCard)
            throws IOException {

        //随机样式
        switch (random.nextInt(5)) {
            case 0:
                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
                break;
            case 1:
                cs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                cs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                cs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                cs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
        }
        setResponseHeaders(response);
        Captcha captcha = cs.getCaptcha();
        String code = captcha.getChallenge();
        stringJedis.setValue(Constant.IDCARD_VERIFY_CODE + idCard, code);
        logger.debug("生成图片验证码{}", code);
        BufferedImage bufferedImage = captcha.getImage();
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }


    protected void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }


    /**
     * 获取验证码明文
     *
     * @param idCard 手机号
     * @return 验证码明文
     */
    @ResponseBody
    @RequestMapping(value = "/idCard/getExpress")
    public AjaxResult getExpress(String idCard) {
        AjaxResult ajaxResult = new AjaxResult();
        //随机样式
        switch (random.nextInt(5)) {
            case 0:
                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
                break;
            case 1:
                cs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                cs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                cs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                cs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
        }
        Captcha captcha = cs.getCaptcha();
        String code = captcha.getChallenge();
        stringJedis.setValue(Constant.IDCARD_VERIFY_CODE + idCard, code);
        logger.debug("生成图片验证码{}", code);
        ajaxResult.setData(code);
        ajaxResult.setSuccess(true);
        return ajaxResult;
    }


}
