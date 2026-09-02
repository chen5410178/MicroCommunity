package com.java110.api.controller.app.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.payFee.PayFeeQrcodeDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.fee.IPayFeeQrcodeV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 收费二维码扫码落地控制器（免登录，JwtFilter 白名单）
 * 二维码内容仅依赖 pfqId，微信扫码后由本控制器转跳对应缴费端
 * - 公众号：/app/qrcode/payFeeQrcodeH5/{id} 302 跳转 H5 缴费页
 * - 小程序：/app/qrcode/payFeeQrcodeMini/{id} 生成 URLScheme 拉起小程序缴费页
 */
@RestController
@RequestMapping(path = "/app/qrcode")
public class PayFeeQrcodeController {

    private final static Logger logger = LoggerFactory.getLogger(PayFeeQrcodeController.class);

    private static final String PAY_QRCODE_H5_PATH = "/#/pages/fee/payQrCode";

    private static final String PAY_QRCODE_MINI_PATH = "pages/fee/payQrCode";

    private static final String WECHAT_TYPE_MINI = "1000";

    @Autowired
    private IPayFeeQrcodeV1InnerServiceSMO payFeeQrcodeV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    /**
     * H5（公众号）扫码落地：302 跳转 H5 缴费页
     *
     * @param id 收费二维码主键 pfqId
     */
    @RequestMapping(path = "/payFeeQrcodeH5/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> payFeeQrcodeH5(@PathVariable String id, HttpServletRequest request) {
        PayFeeQrcodeDto payFeeQrcodeDto = queryPayFeeQrcode(id);
        String appId = getPublicAppId(payFeeQrcodeDto.getCommunityId());
        if (StringUtil.isEmpty(appId)) {
            return ResultVo.error("未配置公众号");
        }
        String ownerUrl = UrlCache.getOwnerUrl();
        String url = ownerUrl + PAY_QRCODE_H5_PATH + "?communityId=" + payFeeQrcodeDto.getCommunityId()
                + "&pfqId=" + payFeeQrcodeDto.getPfqId()
                + "&wAppId=" + appId;
        return ResultVo.redirectPage(url);
    }

    /**
     * 小程序扫码落地：生成 URLScheme 自动拉起小程序缴费页
     *
     * @param id 收费二维码主键 pfqId
     */
    @RequestMapping(path = "/payFeeQrcodeMini/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> payFeeQrcodeMini(@PathVariable String id, HttpServletRequest request) {
        PayFeeQrcodeDto payFeeQrcodeDto = queryPayFeeQrcode(id);
        String urlScheme = getWechatURLScheme(payFeeQrcodeDto.getCommunityId(),
                PAY_QRCODE_MINI_PATH,
                "communityId=" + payFeeQrcodeDto.getCommunityId() + "&pfqId=" + payFeeQrcodeDto.getPfqId());
        if (StringUtil.isEmpty(urlScheme)) {
            return ResultVo.error("未配置小程序");
        }
        return ResponseEntity.ok().body(getHtmlPage(urlScheme));
    }

    /**
     * 按 pfqId 查询收费二维码配置
     */
    private PayFeeQrcodeDto queryPayFeeQrcode(String pfqId) {
        PayFeeQrcodeDto payFeeQrcodeDto = new PayFeeQrcodeDto();
        payFeeQrcodeDto.setPfqId(pfqId);
        List<PayFeeQrcodeDto> payFeeQrcodeDtos = payFeeQrcodeV1InnerServiceSMOImpl.queryPayFeeQrcodes(payFeeQrcodeDto);
        Assert.listOnlyOne(payFeeQrcodeDtos, "二维码不存在");
        return payFeeQrcodeDtos.get(0);
    }

    /**
     * 查小区公众号 appId，未配置返回空
     */
    private String getPublicAppId(String communityId) {
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityId);
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            return "";
        }
        return smallWeChatDtos.get(0).getAppId();
    }

    /**
     * 生成微信小程序 URLScheme
     * 未配置小程序返回空，微信接口调用失败抛异常
     */
    private String getWechatURLScheme(String communityId, String path, String query) {
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityId);
        smallWeChatDto.setWeChatType(WECHAT_TYPE_MINI);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            return "";
        }

        String accessToken = WechatFactory.getAccessToken(smallWeChatDtos.get(0).getAppId(), smallWeChatDtos.get(0).getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            throw new IllegalArgumentException("获取小程序 access_token 失败");
        }

        String generateSchemeUrl = "https://api.weixin.qq.com/wxa/generatescheme?access_token=" + accessToken;

        JSONObject paramIn = new JSONObject();
        JSONObject jumpWxa = new JSONObject();
        jumpWxa.put("path", path);
        jumpWxa.put("query", query);
        paramIn.put("jump_wxa", jumpWxa);

        ResponseEntity<String> responseStr = outRestTemplate.postForEntity(
                generateSchemeUrl, paramIn.toJSONString(), String.class);
        JSONObject response = JSONObject.parseObject(responseStr.getBody());

        if (response.getIntValue("errcode") != 0) {
            throw new IllegalArgumentException(response.getString("errmsg"));
        }
        return response.getString("openlink");
    }

    /**
     * 拼装自动跳转小程序 HTML（1.5s 后自动跳转，含立即跳转兜底按钮）
     */
    private String getHtmlPage(String urlScheme) {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n" +
                "    <title>跳转小程序</title>\n" +
                "    <style>\n" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background: #f5f5f5; color: #333; line-height: 1.6; }\n" +
                "        .container { max-width: 100%; padding: 20px; text-align: center; }\n" +
                "        .loading { margin: 50px auto 30px; width: 60px; height: 60px; border: 4px solid rgba(0, 122, 255, 0.2); border-radius: 50%; border-top-color: #007aff; animation: spin 1s ease-in-out infinite; }\n" +
                "        @keyframes spin { to { transform: rotate(360deg); } }\n" +
                "        h1 { font-size: 20px; margin-bottom: 20px; color: #333; }\n" +
                "        .btn { display: inline-block; margin-top: 20px; padding: 12px 25px; background: #07C160; color: white; text-decoration: none; border-radius: 5px; font-weight: 500; font-size: 16px; box-shadow: 0 2px 10px rgba(7, 193, 96, 0.3); }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"loading\"></div>\n" +
                "        <h1>正在为您跳转小程序...</h1>\n" +
                "        <p>如果跳转失败，请点击下方按钮</p>\n" +
                "        <a href=\"" + urlScheme + "\" class=\"btn\">立即跳转</a>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        setTimeout(function() {\n" +
                "            window.location.href = \"" + urlScheme + "\";\n" +
                "        }, 1500);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
        return html;
    }
}
