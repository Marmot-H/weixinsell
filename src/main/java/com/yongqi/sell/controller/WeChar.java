package com.yongqi.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
//@RequestMapping("/wechar")
public class WeChar {
    //接入认证
    @RequestMapping("/wechar")
    @ResponseBody
    public String getToken(@RequestParam(value = "signature") String signature,
                           @RequestParam("echostr") String echostr) {
        log.info("接入 signature={}",signature);

        return echostr;
    }
    /**
     * 如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE。
     * @param code
     * @param
     */
    @RequestMapping("/authorize")
    public String auth(@RequestParam("code") String code ,@RequestParam("state") String returnUrl) {
        log.info("授权回调成功");
        log.info("code={}",code);
        log.info("state={}",returnUrl);
        /**
         通过code换取网页授权access_token,获取code后，请求以下链接获取access_token：
         *正确时返回的JSON数据包如下：
         * {
         *     "access_token":"ACCESS_TOKEN",
         *     "expires_in":7200,
         *     "refresh_token":"REFRESH_TOKEN",
         *     "openid":"OPENID",
         *     "scope":"SCOPE"
         *  }
         */
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=wx1c90f15baea25f5b&secret=8c2a1993bb061aa84b9ba851af1ee036&" +
                "code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        log.info("返回数据={}",responseEntity);
        return "redirect:" + returnUrl;
    }
}
