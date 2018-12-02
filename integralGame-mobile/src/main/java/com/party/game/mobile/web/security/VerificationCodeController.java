package com.party.game.mobile.web.security;

import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import com.party.game.common.redis.StringJedis;
import com.party.game.mobile.web.dto.AjaxResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
public class VerificationCodeController {

    protected static Logger logger = LoggerFactory.getLogger(VerificationCodeController.class);
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

    @RequestMapping("/crimg")
    public void crimg(HttpServletRequest request, HttpServletResponse response)
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
        String token = captcha.getChallenge();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("verificationCode", token);
        logger.debug("生成图片验证码,session{},code{}", session.getId(), token);
        BufferedImage bufferedImage = captcha.getImage();
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }

    /**
     * 小程序获取图片验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/miniProgram/crimg/{openId}")
    public void miniProgramCrimg(HttpServletRequest request, HttpServletResponse response, @PathVariable String openId)
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
        stringJedis.setValue(openId, code);
        BufferedImage bufferedImage = captcha.getImage();
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }

    /**
     * 根据手机号码获取图片验证码
     *
     * @param request  请求参数
     * @param response 响应参数
     * @param mobile   手机号码
     * @throws IOException
     */
    @RequestMapping("/mobile/crimg/{mobile}")
    public void mobileCrimg(HttpServletRequest request, HttpServletResponse response, @PathVariable String mobile)
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
        stringJedis.setValue(mobile + "VERIFY_CODE", code);
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
     * @param mobile 手机号
     * @return 验证码明文
     */
    @ResponseBody
    @RequestMapping(value = "/getExpress")
    public AjaxResult getExpress(String mobile) {
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
        stringJedis.setValue(mobile + "VERIFY_CODE", code);
        logger.debug("生成图片验证码{}", code);
        ajaxResult.setData(code);
        ajaxResult.setSuccess(true);
        return ajaxResult;
    }


}
