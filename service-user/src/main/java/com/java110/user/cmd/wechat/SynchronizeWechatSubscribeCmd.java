/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.user.cmd.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.user.IWechatSubscribeV1InnerServiceSMO;
import com.java110.po.wechat.WechatSubscribePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：wechatSubscribe.saveWechatSubscribe
 * 请求路劲：/app/wechatSubscribe.SaveWechatSubscribe
 * add by 吴学文 at 2022-02-22 16:17:23 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "wechat.synchronizeWechatSubscribe")
public class SynchronizeWechatSubscribeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SynchronizeWechatSubscribeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validateProperty(cmdDataFlowContext);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区编号");

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(reqJson.getString("communityId"));
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            throw new CmdException("未配置公众号");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_SYNCHRONIZE_WECHAT_SUBSCRIBE, reqJson));


        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "已提交获取关注用户信息"));
    }
}
