package com.yongqi.sell.controller;

import com.yongqi.sell.enums.ResultEnum;
import com.yongqi.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URLEncoder;

@Controller
@Slf4j
@RequestMapping("/wechat")
public class WechatController {
    @Autowired
    WxMpService wxMpService;
    @RequestMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){
//        首先构造网页授权url，然后构成超链接让用户点击：
        String redirectUrl = "http://emkav3.natappfree.cc/sell/wechat/userInfo";
//        如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE。
        String resultUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl,
                WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));
        return "redirect:" + resultUrl;
    }

    /**
     * 当用户同意授权后，会回调所设置的url并把authorization code传过来，
     * 然后用这个code获得access token，其中也包含用户的openid等信息
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code ,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
           wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (WxErrorException e) {
            throw new  SellException(ResultEnum.WXMP_ERROE.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openId=" + openId;
    }

}
