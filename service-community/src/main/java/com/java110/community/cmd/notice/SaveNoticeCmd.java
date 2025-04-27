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
package com.java110.community.cmd.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.dto.privilege.RoleCommunityDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.INoticeV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.notice.NoticePo;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：notice.saveNotice
 * 请求路劲：/app/notice.SaveNotice
 * add by 吴学文 at 2022-07-22 19:20:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "notice.saveNotice")
public class SaveNoticeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveNoticeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private INoticeV1InnerServiceSMO noticeV1InnerServiceSMOImpl;

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "title", "必填，请填写标题");
        Assert.hasKeyAndValue(reqJson, "noticeTypeCd", "必填，请选择公告类型");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写公告内容");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        NoticePo noticePo = BeanConvertUtil.covertBean(reqJson, NoticePo.class);
        noticePo.setUserId(userId);
        noticePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        noticePo.setEndTime(DateUtil.LAST_TIME);
        noticePo.setNoticeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        noticePo.setObjType(NoticeDto.OBJ_TYPE_ALL);
        noticePo.setObjId(reqJson.getString("communityId"));
        noticePo.setState(NoticeDto.STATE_WAIT);
        int flag = noticeV1InnerServiceSMOImpl.saveNotice(noticePo);
        if (flag < 1) {
            throw new CmdException("发布失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
