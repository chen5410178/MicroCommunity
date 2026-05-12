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
package com.java110.fee.cmd.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.payFee.ReturnPayFeeDto;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.dto.refundFeeDetail.RefundFeeDetailDto;
import com.java110.dto.refundFeeOrder.RefundFeeOrderDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.fee.FeeReceiptDetailPo;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.onlinePayRefund.OnlinePayRefundPo;
import com.java110.po.refundFeeOrder.RefundFeeOrderPo;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类表述：退费审核接口
 * 服务编码：returnPayFee.updateReturnPayFee
 * 请求路劲：/app/returnPayFee.UpdateReturnPayFee
 * add by 吴学文 at 2022-02-21 12:20:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "returnPayFee.updateReturnPayFee")
public class UpdateReturnPayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateReturnPayFeeCmd.class);

    @Autowired
    private IReturnPayFeeV1InnerServiceSMO returnPayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountNewV1InnerServiceSMO payFeeDetailDiscountNewV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeOrderV1InnerServiceSMO refundFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeDetailV1InnerServiceSMO refundFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;


    private static final String SPEC_RATE = "89002020980015"; //赠送月份

    private static final String SPEC_MONTH = "89002020980014"; //月份

    public static final String CODE_PREFIX_ID = "10";

    //键(积分账户抵扣比例)
    public static final String DEDUCTION_PROPORTION = "DEDUCTION_PROPORTION";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "refundOrderId", "refundOrderId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "state不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");

        RefundFeeOrderDto refundFeeOrderDto = new RefundFeeOrderDto();
        refundFeeOrderDto.setRefundOrderId(reqJson.getString("refundOrderId"));
        refundFeeOrderDto.setCommunityId(reqJson.getString("communityId"));
        refundFeeOrderDto.setState(ReturnPayFeeDto.STATE_WAIT);
        List<RefundFeeOrderDto> refundFeeOrderDtos = refundFeeOrderV1InnerServiceSMOImpl.queryRefundFeeOrders(refundFeeOrderDto);
        if (ListUtil.isNull(refundFeeOrderDtos)) {
            throw new CmdException("退费订单不存在");
        }

        RefundFeeOrderPo refundFeeOrderPo = new RefundFeeOrderPo();
        refundFeeOrderPo.setRefundOrderId(refundFeeOrderDtos.get(0).getRefundOrderId());
        refundFeeOrderPo.setState(reqJson.getString("state"));
        refundFeeOrderPo.setAuditPersonId(userId);
        refundFeeOrderPo.setAuditPersonName(userDtos.get(0).getName());
        refundFeeOrderV1InnerServiceSMOImpl.updateRefundFeeOrder(refundFeeOrderPo);

        RefundFeeDetailDto refundFeeDetailDto = new RefundFeeDetailDto();
        refundFeeDetailDto.setRefundOrderId(refundFeeOrderDtos.get(0).getRefundOrderId());
        List<RefundFeeDetailDto> refundFeeDetailDtos = refundFeeDetailV1InnerServiceSMOImpl.queryRefundFeeDetails(refundFeeDetailDto);
        PayFeeDetailPo payFeeDetailPo = null;
        //不通过
        if ("1200".equals(reqJson.getString("state"))) {

            for (RefundFeeDetailDto refundFeeDetailDto1 : refundFeeDetailDtos) {
                payFeeDetailPo = new PayFeeDetailPo();
                payFeeDetailPo.setDetailId(refundFeeDetailDto1.getDetailId());
                payFeeDetailPo.setState(FeeDetailDto.STATE_NORMAL);
                payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
            }
            return;
        }


        //退费审核通过
        FeeDetailDto feeDetailDto = null;

        List<FeeDetailDto> feeDetailDtos = null;
        PayFeePo payFeePo = null;
        for (RefundFeeDetailDto refundFeeDetailDto1 : refundFeeDetailDtos) {
            payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(refundFeeDetailDto1.getDetailId());
            payFeeDetailPo.setState(FeeDetailDto.STATE_RETURNED);
            payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);

            feeDetailDto = new FeeDetailDto();
            feeDetailDto.setDetailId(refundFeeDetailDto1.getDetailId());
            feeDetailDtos    = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

            if (ListUtil.isNull(feeDetailDtos)) {
                continue;
            }

            payFeeDetailPo = BeanConvertUtil.covertBean(feeDetailDtos.get(0), PayFeeDetailPo.class);
            payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
            payFeeDetailPo.setState(FeeDetailDto.STATE_RETURN_ORDER);
            payFeeDetailPo.setCycles(unum(feeDetailDtos.get(0).getCycles()) + "");
            payFeeDetailPo.setReceivableAmount(unum(feeDetailDtos.get(0).getReceivableAmount()) + "");
            payFeeDetailPo.setReceivedAmount(unum(feeDetailDtos.get(0).getReceivedAmount()) + "");
            payFeeDetailPo.setCreateTime(DateUtil.getFormatTimeStringA(feeDetailDtos.get(0).getCreateTime()));
            payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);

            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(feeDetailDtos.get(0).getFeeId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            if (ListUtil.isNull(feeDtos)) {
                continue;
            }

            BigDecimal oweAmount = new BigDecimal(feeDtos.get(0).getOweAmount());
            oweAmount = oweAmount.add(new BigDecimal(feeDetailDtos.get(0).getReceivedAmount()))
                    .add(new BigDecimal(feeDetailDtos.get(0).getAcctAmount()))
                    .add(new BigDecimal(feeDetailDtos.get(0).getDeductionAmount()))
                    .add(new BigDecimal(feeDetailDtos.get(0).getDeductionAmount()))
                    .add(new BigDecimal(feeDetailDtos.get(0).getGiftAmount()));
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(feeDetailDtos.get(0).getFeeId());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setOweAmount(oweAmount.doubleValue() + "");
            feeInnerServiceSMOImpl.updateFee(payFeePo);

            //todo 检查是否现金账户抵扣
            returnAccount(feeDetailDtos.get(0), feeDtos.get(0));

            // 修改订单应收
            PayFeeOrderDto payFeeOrderDto = new PayFeeOrderDto();
            payFeeOrderDto.setPayOrderId(feeDetailDtos.get(0).getPayOrderId());
            generatorPayFeeOrderImpl.computePayFeeOrder(payFeeOrderDto);


        }
//todo 提交线上退费
        returnOnlinePayMoney(refundFeeOrderDtos.get(0), refundFeeDetailDtos);

    }


    private void returnAccount(FeeDetailDto feeDetailDto, FeeDto feeDto) {

        String account = feeDetailDto.getAcctAmount();
        if (StringUtil.isEmpty(account)) {
            return;
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(feeDto.getOwnerId());
        accountDto.setCommunityId(feeDto.getCommunityId());
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (ListUtil.isNull(accountDtos)) {
            return;
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setAmount(account);
        accountDetailPo.setRemark("退费账户预存");
        accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);

    }


    private double unum(String value) {
        double dValue = Double.parseDouble(value);
        return dValue * -1;
    }


    /**
     * 发起退款
     * 这里发起退款后 通过databus 触发 ReturnPayFeeMoneyAdapt 这个类 退款
     *
     * @param refundFeeOrderDto
     */
    private void returnOnlinePayMoney(RefundFeeOrderDto refundFeeOrderDto, List<RefundFeeDetailDto> refundFeeDetailDtos) {
        if (ListUtil.isNull(refundFeeDetailDtos)) {
            return;
        }
        if (StringUtil.isEmpty(refundFeeOrderDto.getPayOrderId())) {
            return;
        }
        BigDecimal refundFee = new BigDecimal("0");
        for (RefundFeeDetailDto refundFeeDetailDto : refundFeeDetailDtos) {
            refundFee = refundFee.add(new BigDecimal(refundFeeDetailDto.getReceivedAmount()));
        }

        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setOrderId(refundFeeOrderDto.getPayOrderId());
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if (ListUtil.isNull(onlinePayDtos)) {
            return;
        }
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setOrderId(onlinePayDtos.get(0).getOrderId());
        onlinePayPo.setPayId(onlinePayDtos.get(0).getPayId());
        onlinePayPo.setState(OnlinePayDto.STATE_WT);
        onlinePayPo.setRefundFee(refundFee.doubleValue() + "");
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);

        //todo 保存 退费明细

        OnlinePayRefundPo onlinePayRefundPo = new OnlinePayRefundPo();
        onlinePayRefundPo.setPayId(onlinePayDtos.get(0).getPayId());
        onlinePayRefundPo.setRefundId(GenerateCodeFactory.getGeneratorId("11"));
        onlinePayRefundPo.setState(OnlinePayDto.STATE_WT);
        onlinePayRefundPo.setMessage("待退费");
        onlinePayRefundPo.setBusiId(refundFeeOrderDto.getRefundOrderId());
        onlinePayRefundPo.setRefundFee(refundFee.doubleValue() + "");
        onlinePayRefundPo.setCommunityId(refundFeeOrderDto.getCommunityId());
        onlinePayRefundV1InnerServiceSMOImpl.saveOnlinePayRefund(onlinePayRefundPo);
    }
}
