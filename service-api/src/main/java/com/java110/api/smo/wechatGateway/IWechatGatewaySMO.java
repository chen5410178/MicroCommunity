package com.java110.api.smo.wechatGateway;

import com.java110.core.context.IPageData;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 组织管理管理服务接口类
 * <p>
 * add by wuxw 2019-06-29
 */
public interface IWechatGatewaySMO {

    /**
     * 获取微信回话信息
     *
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> gateway(String fromUserName, String toUserName, String keyword, String msgType, String event, String eventKey,String appId);

    /**
     * 查询微信信息
     *
     * @param smallWeChatDto
     * @return
     */
    SmallWeChatDto getSmallWechat(IPageData pd, SmallWeChatDto smallWeChatDto);

}
