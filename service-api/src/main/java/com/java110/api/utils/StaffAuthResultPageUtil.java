package com.java110.api.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 员工认证结果页渲染工具
 * <p>
 * 员工扫码认证完成后由 service-api 直出 HTML 结果页，不再跳转业主端首页
 * 页面样式内联，不依赖任何外部资源，保证微信内置浏览器弱网下也能正常展示
 */
public final class StaffAuthResultPageUtil {

    private static final String APP_TYPE_WECHAT = "WECHAT";

    private static final String APP_TYPE_WECHAT_NAME = "微信";

    private static final String SUCCESS_TIP = "请返回电脑端页面点击【扫码完成】刷新认证状态";

    private static final String FAIL_TIP = "请返回电脑端重新生成二维码后再次扫码";

    private StaffAuthResultPageUtil() {
    }

    /**
     * 认证成功结果页
     *
     * @param openName 微信昵称
     * @param appType  认证方式
     * @param authTime 认证时间
     * @return HTML 响应
     */
    public static ResponseEntity<String> success(String openName, String appType, String authTime) {
        StringBuilder rows = new StringBuilder();
        rows.append(buildRow("微信昵称", openName));
        rows.append(buildRow("认证方式", getAppTypeName(appType)));
        rows.append(buildRow("认证时间", authTime));
        return buildResponseEntity(buildPage(true, "员工认证成功", rows.toString()));
    }

    /**
     * 认证失败结果页
     *
     * @param reason 失败原因
     * @return HTML 响应
     */
    public static ResponseEntity<String> fail(String reason) {
        StringBuilder rows = new StringBuilder();
        rows.append(buildRow("失败原因", reason));
        return buildResponseEntity(buildPage(false, "员工认证失败", rows.toString()));
    }

    private static String buildRow(String label, String value) {
        String displayValue = (value == null || value.trim().isEmpty()) ? "-" : value;
        return "        <div class=\"row\">"
                + "<span class=\"label\">" + escapeHtml(label) + "</span>"
                + "<span class=\"value\">" + escapeHtml(displayValue) + "</span>"
                + "</div>\n";
    }

    private static String getAppTypeName(String appType) {
        if (APP_TYPE_WECHAT.equals(appType)) {
            return APP_TYPE_WECHAT_NAME;
        }
        return appType;
    }

    private static String buildPage(boolean success, String title, String rows) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html lang=\"zh-CN\">\n")
                .append("<head>\n")
                .append("<meta charset=\"UTF-8\">\n")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n")
                .append("<title>").append(escapeHtml(title)).append("</title>\n")
                .append("<style>\n")
                .append("* { margin: 0; padding: 0; box-sizing: border-box; }\n")
                .append("body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background: #f5f5f5; color: #333; line-height: 1.6; }\n")
                .append(".container { max-width: 100%; padding: 40px 20px; text-align: center; }\n")
                .append(".icon { margin: 0 auto 20px; width: 72px; height: 72px; border-radius: 50%; color: #fff; font-size: 40px; line-height: 72px; }\n")
                .append(".icon-success { background: #07c160; }\n")
                .append(".icon-fail { background: #fa5151; }\n")
                .append("h1 { font-size: 20px; font-weight: 500; margin-bottom: 20px; }\n")
                .append(".card { background: #fff; border-radius: 8px; padding: 4px 16px; text-align: left; }\n")
                .append(".row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #f0f0f0; font-size: 15px; }\n")
                .append(".row:last-child { border-bottom: none; }\n")
                .append(".label { color: #999; flex: none; margin-right: 12px; }\n")
                .append(".value { color: #333; max-width: 65%; word-break: break-all; text-align: right; }\n")
                .append(".tip { margin-top: 20px; font-size: 14px; color: #999; }\n")
                .append(".btn { margin-top: 24px; padding: 12px 40px; background: #07c160; color: #fff; border: none; border-radius: 4px; font-size: 16px; }\n")
                .append("</style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("<div class=\"container\">\n")
                .append("    <div class=\"icon ").append(success ? "icon-success" : "icon-fail").append("\">")
                .append(success ? "&#10003;" : "&#10005;").append("</div>\n")
                .append("    <h1>").append(escapeHtml(title)).append("</h1>\n")
                .append("    <div class=\"card\">\n")
                .append(rows)
                .append("    </div>\n")
                .append("    <p class=\"tip\">").append(escapeHtml(success ? SUCCESS_TIP : FAIL_TIP)).append("</p>\n");

        if (success) {
            html.append("    <button class=\"btn\" onclick=\"closePage()\">关闭本页</button>\n")
                    .append("    <p class=\"tip\" id=\"closeTip\" style=\"display:none;\">如未能自动关闭，请点击右上角「···」选择关闭</p>\n");
        }
        html.append("</div>\n");

        if (success) {
            html.append("<script>\n")
                    .append("function closePage() {\n")
                    .append("    if (typeof WeixinJSBridge !== 'undefined' && WeixinJSBridge.invoke) {\n")
                    .append("        WeixinJSBridge.call('closeWindow');\n")
                    .append("    } else {\n")
                    .append("        document.getElementById('closeTip').style.display = 'block';\n")
                    .append("    }\n")
                    .append("}\n")
                    .append("</script>\n");
        }
        html.append("</body>\n</html>");
        return html.toString();
    }

    private static ResponseEntity<String> buildResponseEntity(String html) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/html;charset=UTF-8"));
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
    }

    private static String escapeHtml(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder escaped = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '&':
                    escaped.append("&amp;");
                    break;
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '"':
                    escaped.append("&quot;");
                    break;
                case '\'':
                    escaped.append("&#39;");
                    break;
                default:
                    escaped.append(c);
                    break;
            }
        }
        return escaped.toString();
    }
}
