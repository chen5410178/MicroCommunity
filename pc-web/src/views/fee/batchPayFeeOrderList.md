代码内容：<!-- HTML Content -->
<!--这个是 batchPayFeeOrder.html 的html内容-->
<div>
    <div class="flex justify-between">
        <div class="" style="width: 300px;">
            <div class=" border-radius">
                <div class="">
                    <ul class="list-group text-center border-radius">
                        <li class="list-group-item node-orgTree " v-for="(item,index) in batchPayFeeOrderInfo.batchFees"
                            :key="index" @click="_swatchPayerObj(item)"
                            :class="{'vc-node-selected':item.payerObjId == batchPayFeeOrderInfo.payerObjId}">
                            <div class="text-bold" style="font-size: 18px;">{{item.payerObjName}}</div>
                            <div>业主：{{item.ownerName}}|欠费：{{item.oweAmount}}</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="margin-left" style="flex-grow: 1;">
            <div class="flex justify-between"
                style="padding: 15px 20px;background: #f0f7ff;border-bottom: 1px solid #d0e3ff;font-size: 14px;color: #666;">
                <div class="flex justify-start" style="align-items: center;">
                    <div class="margin-right">账户金额：<span
                            style="color: #4caf50;">￥{{batchPayFeeOrderInfo.acctAmount}}</span></div>
                    <div class="margin-right">可用积分：<span
                            style="color: #4caf50;">{{batchPayFeeOrderInfo.integralQuantity}}</span></div>
                    <div class="margin-right">押金金额：<span
                            style="color: #4caf50;">￥{{batchPayFeeOrderInfo.depositAmount}}</span>
                        <span v-if="batchPayFeeOrderInfo.depositAmount > 0">(<a href="javascript:void(0);"
                                @click="depositPay">退押金</a>)</span>
                    </div>
                    <div class="margin-right">预付款：<span
                            style="color: #4caf50;">￥{{batchPayFeeOrderInfo.ruleAmount}}</span>
                        <span>(<a href="javascript:void(0);" @click="_openBatchPrePayDialog()">预付</a>)</span>
                    </div>
                </div>
                <div>
                    <button type="button" class="btn btn-white btn-sm " @click="_allSelectOrNot('Y')">
                        <vc:i18n name="全部选择" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                    <button type="button" class="btn  btn-white btn-sm margin-left-xs" @click="_allSelectOrNot('N')">
                        <vc:i18n name="全部取消" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                    <button type="button" class="btn btn-white btn-sm " @click="_currentSelectOrNot('Y')">
                        <vc:i18n name="当前选择" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                    <button type="button" class="btn  btn-white btn-sm margin-left-xs"
                        @click="_currentSelectOrNot('N')">
                        <vc:i18n name="当前取消" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                    <button type="button" class="btn btn-success btn-sm " @click="vc.goBack()">
                        <vc:i18n name="返回" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                </div>

            </div>
            <div style="max-height: 650px; overflow-y: scroll">
                <div class="" v-for="(feeConfig,index) in batchPayFeeOrderInfo.fees" :key="index">
                    <div class="bg-white  flex justify-between padding margin-top-sm hand"
                        style="width: 100%;align-items: center;" @click="switchSimplifyFeeFlag(feeConfig)">
                        <div class="flex justify-start">
                            <input type="checkbox" class="i-checks checkItem" style="margin-top: 15px;"
                                :checked="feeConfig.selectedFeeIds.length >= feeConfig.childs.length" @click.stop
                                @change="selectAllFee(feeConfig)">
                            <div class="margin-left">
                                <div style="font-weight: 600; font-size: 16px;margin-bottom: 5px;">{{feeConfig.feeName}}
                                </div>
                                <div>{{feeConfig.startTime}}~{{feeConfig.endTime}}</div>
                            </div>
                        </div>
                        <div style="flex-grow: 1;" class="text-right">
                            <span style="font-weight: 600;font-size: 16px;color: #f44336;"
                                class="margin-right">欠费总额：￥{{feeConfig.oweAmount}}</span>
                            <span class="fa fa-chevron-down" v-if="feeConfig.showFlag == 'Y'"></span>
                            <span class="fa fa-chevron-right" v-else></span>
                        </div>
                    </div>
                    <div v-if="feeConfig.showFlag == 'Y'">
                        <div v-for="(cFee,cIndex) in feeConfig.childs" :key="cIndex">
                            <div class="flex justify-between padding"
                                style="background: #f0f7ff;border-bottom: 1px solid #d0e3ff; align-items: center;">
                                <div class="flex justify-start">
                                    <input type="checkbox" class="i-checks checkItem" style="font-size: 16px;"
                                        v-bind:value="cFee.feeId" v-model="feeConfig.selectedFeeIds"
                                        @change="selectOneFee(feeConfig)">
                                    <div style="font-weight: 600; font-size: 16px;" class="margin-left">
                                        {{vc.dateFormat(cFee.startTime)}}~{{vc.dateFormat(cFee.endTime)}}</div>

                                </div>

                                <div class="multiline-text">
                                    <span style="font-size: 12px;" class="margin-right">{{cFee.remark}}</span>
                                </div>
                                <div>
                                    <span style="font-weight: 600;font-size: 16px;color: #f44336;"
                                        class="margin-right">应收金额：￥{{cFee.oweAmount}}</span>
                                </div>
                                <!-- <div style="width: 200px;">
                                    <input type="text" :placeholder="vc.i18n('请输入实际金额','batchPayFeeOrder')"
                                        v-model="cFee.receivedAmount" class="form-control"
                                        @change="_doComputeTotalFee()">
                                </div> -->
                            </div>
                        </div>

                        <div v-for="(discount,dIndex) in feeConfig.discounts" :key="dIndex" class="margin-top-xs">
                            <div class="flex justify-between padding"
                                style="background: #f0f7ff;border-bottom: 1px solid #d0e3ff; align-items: center;">
                                <div class="flex justify-start">
                                    <input type="checkbox" class="i-checks checkItem" style="font-size: 16px;"
                                        v-bind:value="discount.discountId" v-model="feeConfig.selectedDiscountIds"
                                        @change="selectOneDiscount(feeConfig)">
                                    <div style="font-weight: 600; font-size: 16px;" class="margin-left">
                                        {{discount.discountName}}</div>
                                </div>

                                <div class="multiline-text">
                                    <span style="font-size: 12px;" class="margin-right">{{discount.ruleName}}</span>
                                </div>
                                <div>
                                    <span style="font-weight: 600;font-size: 16px;color: #f44336;"
                                        class="margin-right">金额：￥{{discount.discountPrice}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="flex justify-end margin-top">

                <div class="margin-right">
                    优惠金额：<span style="font-size: 30px; padding-left: 0px;">{{batchPayFeeOrderInfo.discount}}元</span>
                </div>
                <div class="margin-right">
                    缴费金额：<span
                        style="font-size: 30px; color: red; padding-left: 0px;">{{batchPayFeeOrderInfo.feePrices}}元</span>
                </div>
                <div class=" text-right">
                    <button type="button" class="btn btn-success btn-lg " @click="_openPayFee('qrCode')">
                        <vc:i18n name="扫码收费" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                    <button type="button" class="btn  btn-primary btn-lg margin-left-xs"
                        v-on:click="_openPayFee('common')">
                        <vc:i18n name="提交收费" namespace="batchPayFeeOrder"></vc:i18n>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <vc:create path="fee/batchPayConfirm"></vc:create>
    <vc:create path="fee/batchPrePayFeeDialog"></vc:create>
</div>
<!--这个是 batchPayConfirm.html 的html内容-->
<div>
  <div id="doBatchPayFeeModal" class="modal" role="dialog">
    <div class="modal-dialog  modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">
            <vc:i18n name="收费确认" namespace="batchPayConfirm"></vc:i18n>
          </h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row">
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="缴费金额：" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <div class="col-sm-4">
              <input v-model="batchPayConfirmInfo.feePrices" type="number" @blur="_computeTransforAmount()"
                class="form-control" />
            </div>
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="优惠金额：" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <label class="col-sm-4 col-form-label">{{batchPayConfirmInfo.discount}}<span>
                <vc:i18n name="元" namespace="batchPayConfirm"></vc:i18n>
              </span></label>
          </div>

          <div class="row margin-top" v-if="batchPayConfirmInfo.transforAmount >0">
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="转存金额：" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <label class="col-sm-4 col-form-label">{{batchPayConfirmInfo.transforAmount}}<span>
                <vc:i18n name="元" namespace="batchPayConfirm"></vc:i18n>
              </span></label>
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="转存对象：" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <div class="col-sm-4">
              <select class="custom-select" v-model="batchPayConfirmInfo.payerObjId" @change="_selectTransforPreFee()">
                <option selected disabled value="">
                  {{vc.i18n('必填，请选择转存对象','payFeeOrder')}}
                </option>
                <option v-for="(item,index) in batchPayConfirmInfo.payerObjs" :key="index" :value="item.payerObjId">
                  {{item.payerObjName}}
                </option>
              </select>
            </div>
          </div>
          <div class="row margin-top">
            <template v-if="batchPayConfirmInfo.transforAmount >0">
              <label class="col-sm-2 col-form-label text-right">
                转存费用
              </label>
              <div class="col-sm-4">
                <select class="custom-select" v-model="batchPayConfirmInfo.ruleId">
                  <option selected disabled value="">
                    {{vc.i18n('必填，请选择转存费用','payFeeOrder')}}
                  </option>
                  <option v-for="(item,index) in batchPayConfirmInfo.transforPreFees" :key="index" :value="item.ruleId">
                    {{item.feeName}}
                  </option>
                </select>
              </div>
            </template>
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="账户扣款：" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <div class="col-sm-4">
              <select class="custom-select" v-model="batchPayConfirmInfo.acctId">
                <option selected value="">{{vc.i18n('请选择扣款账户','batchPayConfirm')}}</option>
                <option v-for="(item,index) in batchPayConfirmInfo.accounts" :key="index" :value="item.amount">
                  {{item.acctName}}~{{item.amount}}元
                </option>
              </select>
            </div>

          </div>
          <div class="row form-group margin-top" v-if="batchPayConfirmInfo.payType == 'common'">
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="支付方式:" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <div class="col-sm-4">
              <select class="custom-select" v-model="batchPayConfirmInfo.primeRate">
                <option selected disabled value="">{{vc.i18n('必填，请选择支付方式','batchPayConfirm')}}</option>
                <option v-for="(item,index) in batchPayConfirmInfo.primeRates" :key="index"
                  v-if="item.statusCd != '5' && item.statusCd != '6' && item.statusCd != '8'" :value="item.statusCd">
                  {{item.name}}
                </option>
              </select>
            </div>
          </div>
          <div class="row form-group margin-top" v-else>
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="支付方式:" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <div class="col-sm-4">
              <select class="custom-select" v-model="batchPayConfirmInfo.primeRate">
                <option selected disabled value="">{{vc.i18n('必填，请选择支付方式','batchPayConfirm')}}</option>
                <option v-for="(item,index) in batchPayConfirmInfo.primeRates" :key="index"
                  v-if="item.statusCd == '3' || item.statusCd == '4' || item.statusCd == '10' || item.statusCd == '12' "
                  :value="item.statusCd">
                  {{item.name}}
                </option>
              </select>
            </div>
          </div>
          <div class="row">
            <label class="col-sm-2 col-form-label  text-right">
              <vc:i18n name="备注：" namespace="batchPayConfirm"></vc:i18n>
            </label>
            <div class="col-sm-4">
              <textarea v-model="batchPayConfirmInfo.remark" :placeholder="vc.i18n('请填写备注','batchPayConfirm')"
                class="form-control"></textarea>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <input v-model="batchPayConfirmInfo.authCode" id="authCode" class="form-control" style="width: 60%;"
            v-if="batchPayConfirmInfo.payType == 'qrCode' && (batchPayConfirmInfo.primeRate == '3' || batchPayConfirmInfo.primeRate == '4' || batchPayConfirmInfo.primeRate == '10')"
            type="text" @keyup.enter="_qrCodePayFee" :placeholder="vc.i18n('请用扫码枪扫码','batchPayConfirm')">
          <button type="button" class="btn btn-secondary" @click="_closeDoBatchPayFeeModal()">
            <vc:i18n name="关闭" namespace="batchPayConfirm"></vc:i18n>
          </button>
          <button type="button" class="btn btn-primary" @click="_doPayFee()"
            v-if="batchPayConfirmInfo.payType == 'common'">
            <vc:i18n name="确定收费" namespace="batchPayConfirm"></vc:i18n>
          </button>
        </div>
        <div class="modal-footer">
          <!-- 渝地付支付显示二维码URL -->
          <div v-if="batchPayConfirmInfo.payType == 'qrCode' && batchPayConfirmInfo.primeRate == '12'"
            class="qr-code-container" style="width: 100%; text-align: center;">
            <div v-if="batchPayConfirmInfo.qrCodeUrl">
              <img :src="batchPayConfirmInfo.qrCodeUrl" style="max-width: 240px; margin-bottom: 10px;">
              <p>请使用微信/支付宝扫描二维码完成支付</p>
            </div>
            <div v-else>
              <p>正在生成支付二维码...</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div id="payFeeResult" class="modal" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">
            <vc:i18n name="缴费提示" namespace="batchPayConfirm"></vc:i18n>
          </h5>
          <button type="button " class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <p>
            <vc:i18n name="缴费成功" namespace="batchPayConfirm"></vc:i18n>
          </p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="_back()">
            <vc:i18n name="返回" namespace="batchPayConfirm"></vc:i18n>
          </button>
          <button type="button" class="btn btn-primary" @click="_printAndBack('ON')">
            <vc:i18n name="合并打印" namespace="batchPayConfirm"></vc:i18n>
          </button>
          <button type="button" class="btn btn-primary" @click="_printAndBack('OFF')">
            <vc:i18n name="打印收据" namespace="batchPayConfirm"></vc:i18n>
          </button>
        </div>
      </div>
    </div>
  </div>
</div>
<!--这个是 batchPrePayFeeDialog.html 的html内容-->
<div id="batchPrePayFeeDialogModel" class="modal fade" role="dialog" aria-labelledby="exampleModalLabel"
    aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-body">
                <h3 class="m-t-none m-b ">
                    <vc:i18n name="预付款" namespace="batchPrePayFeeDialog"></vc:i18n>
                </h3>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-4">
                            <select class="custom-select" v-model="batchPrePayFeeDialogInfo.payerObjId" @change="_selectBatchPrePayObj()">
                                <option selected disabled value="">
                                    {{vc.i18n('必填，请选择转存费用','payFeeOrder')}}
                                </option>
                                <option v-for="(item,index) in batchPrePayFeeDialogInfo.payerObjs" :key="index"
                                    :value="item.payerObjId">
                                    {{item.payerObjName}}
                                </option>
                            </select>
                        </div>
                    </div>
                    <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="15">
                        <thead>
                            <tr>
                                <th class="text-center">
                                    <input type="checkbox" class="i-checks all-check"
                                        :checked="batchPrePayFeeDialogInfo.selectRuleIds.length >= batchPrePayFeeDialogInfo.rules.length"
                                        @click="selectAllRule($event)">
                                </th>
                                <th class="text-center">
                                    <vc:i18n name="费用名称" namespace="prePayFeeOrder"></vc:i18n>
                                </th>
                                <th class="text-center">
                                    <vc:i18n name="月单价" namespace="prePayFeeOrder"></vc:i18n>
                                </th>
                                <th class="text-center">
                                    <vc:i18n name="说明" namespace="prePayFeeOrder"></vc:i18n>
                                </th>
                                <th class="text-center">
                                    <vc:i18n name="应收金额" namespace="prePayFeeOrder"></vc:i18n>
                                </th>
                                <th class="text-center">
                                    <vc:i18n name="月份" namespace="prePayFeeOrder"></vc:i18n>
                                </th>
                                <th class="text-center">
                                    <vc:i18n name="起始月" namespace="prePayFeeOrder"></vc:i18n>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <template v-for="rule in batchPrePayFeeDialogInfo.rules">
                                <tr>
                                    <td class="text-center">
                                        <input type="checkbox" class="i-checks checkItem" v-bind:value="rule.ruleId"
                                            @change="selectOneRule(rule)"
                                            v-model="batchPrePayFeeDialogInfo.selectRuleIds" @click.stop>
                                    </td>
                                    <td class="text-center" @click="switchRuleFlag(rule)">{{rule.feeName}}</td>
                                    <td class="text-center" @click="switchRuleFlag(rule)">{{rule.monthAmount}}</td>
                                    <td class="text-center multiline-text" @click="switchRuleFlag(rule)">{{rule.remark}}
                                    </td>
                                    <td class="text-center" @click="switchRuleFlag(rule)" style="width: 150px;">
                                        <input type="number" class="form-control" :disabled="rule.selected == 'N'"
                                            @change="_computeReceivedAmount()" v-model="rule.receivedAmount">
                                    </td>
                                    <td class="text-center" style="width: 120px;">
                                        <select class="custom-select" v-model="rule.cycles"
                                            :disabled="rule.selected == 'N'" @change="_changeMonth(rule.cycles,rule)">
                                            <option selected disabled value="">{{vc.i18n('选择月份','batchPayFeeOrder')}}
                                            </option>
                                            <option v-for="item in 24" :value="item">
                                                {{item}}{{vc.i18n('个月','batchPayFeeOrder')}}</option>
                                        </select>
                                    </td>
                                    <td class="text-center" style="width: 120px;">
                                        <input type="text" class="form-control" readonly
                                            :class="rule.startDateInputClass" v-model="rule.startMonth">
                                    </td>
                                </tr>
                                <tr v-for="discount in rule.discounts" style="background-color: #f9f9f9;"
                                    v-if="rule.showFlag == 'Y'">
                                    <td class="text-center">
                                        <input type="checkbox" class="i-checks checkItem"
                                            v-bind:value="discount.discountId" @change="selectOneDiscount(discount)"
                                            v-model="rule.selectedDiscountIds">
                                    </td>
                                    <td class="text-center">{{discount.discountName}}</td>
                                    <td class="text-center">-</td>
                                    <td class="text-center">{{discount.ruleName}}</td>
                                    <td class="text-center">{{discount.discountPrice}}</td>
                                    <td class="text-center" colspan="2"></td>
                                </tr>
                            </template>
                        </tbody>
                    </table>

                    <div class="ibox-content">
                        <button class="btn btn-primary float-right" type="button"
                            v-on:click="_doBatchPrePayFeeDialog()">
                            <vc:i18n name="确定"></vc:i18n>
                        </button>
                        <button type="button" class="btn btn-warning float-right" style="margin-right:20px;"
                            data-dismiss="modal">
                            <vc:i18n name="取消"></vc:i18n>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JS Content -->
//这个是 batchPayConfirm.js 的js内容
(function (vc) {
    vc.extends({

        data: {
            batchPayConfirmInfo: {
                qrCodeUrl: null,  // 新增字段，用于存储二维码URL
                payType: '',
                fees: [],
                primeRate: '',
                primeRates: [],
                payerObjName: '',
                orderId: '',
                paymentPoolId: '',
                authCode: '',
                feePrices: '',
                orgFeePrices: 0.0,
                receiptIds: '',
                printUrl: '/#/pages/property/printPayFee',
                accountAmount: '',
                acctId: '',
                isBatch: '',
                accounts: [],
                ownerId: '',
                rules: [],
                ruleAmount: 0.0,
                transforAmount: 0,
                ruleId: '',
                transforPreFees: [],
                payerObjId:'',
                payerObjType:'',
                payerObjs:[]
            }
        },
        _initMethod: function () {
            //与字典表支付方式关联
            vc.getDict('pay_fee_detail', "prime_rate", function (_data) {
                $that.batchPayConfirmInfo.primeRates = _data;
            });
            $that._listFeePrintPages();
        },
        _initEvent: function () {
            vc.on('batchPayConfirm', 'openBatchPayFee', function (_param) {
                $that._resetBatchPayConfirm();
                $that.batchPayConfirmInfo.payType = _param.payType;
                $that.batchPayConfirmInfo.fees = _param.fees;
                $that.batchPayConfirmInfo.feePrices = _param.feePrices;
                $that.batchPayConfirmInfo.orgFeePrices = _param.feePrices;
                $that.batchPayConfirmInfo.discount = _param.discount;
                $that.batchPayConfirmInfo.payerObjName = _param.payerObjName;
                $that.batchPayConfirmInfo.ownerId = _param.ownerId;
                $that.batchPayConfirmInfo.rules = _param.rules;
                $that.batchPayConfirmInfo.ruleAmount = _param.ruleAmount;
                $that.batchPayConfirmInfo.transforAmount = _param.transforAmount;
                $that.batchPayConfirmInfo.payerObjs = _param.payerObjs;
                $("#doBatchPayFeeModal").modal('show');
                // 监听 primeRate 变化
                $that.$watch('batchPayConfirmInfo.primeRate', $that._watchPrimeRateChange);
                if (_param.payType == 'qrCode') {
                    setTimeout('document.getElementById("authCode").focus()', 500);
                }
                $that._loadOwnerAccts();
            });
        },
        methods: {
            _closeDoBatchPayFeeModal: function () {
                $("#doBatchPayFeeModal").modal('hide');
            },
            _doPayFee: function () {
                if (!$that.batchPayConfirmInfo.primeRate) {
                    console.log(" $that.batchPayConfirmInfo--------->" + $that.batchPayConfirmInfo);
                    vc.toast('请选择支付方式');
                    return;
                }
                /**
                 *
                 * [{
                        payerObj...
                        "childs": [
                        {
                            feeConfig...,
                            selectedFeeIds:[],
                            selectedDiscountIds:[]
                            discounts:[],
                            "childs": [
                            {
                                fee....
                            }
                            ],

                        },

                        ],
                    }]
                 */
                let _fees = $that.batchPayConfirmInfo.fees;
                let _payFees = [];
                _fees.forEach(_fee => {
                    _fee.childs.forEach(_feeConfig => {
                        const payFees = _feeConfig.childs.filter(child =>
                            _feeConfig.selectedFeeIds.includes(child.feeId)
                        );

                        _payFees.push({
                            configId: _feeConfig.configId,
                            selectedDiscountIds: _feeConfig.selectedDiscountIds,
                            payFees: payFees
                        });
                    });
                });

                let _paramData = $that._mergeTransforPreFee();

                let _data = {
                    communityId: vc.getCurrentCommunity().communityId,
                    feeConfigs: _payFees,
                    primeRate: $that.batchPayConfirmInfo.primeRate,
                    acctId: $that.batchPayConfirmInfo.acctId,
                    remark: $that.batchPayConfirmInfo.remark,
                    amount: _paramData.feePrices,
                    receivedAmount: $that.batchPayConfirmInfo.feePrices,
                    discountAmount: $that.batchPayConfirmInfo.discount,
                    rules:_paramData.rules
                }
                vc.http.apiPost(
                    '/fee.payBatchFee',
                    JSON.stringify(_data), {
                    emulateJSON: true
                },
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if (_json.code == 0) {
                            $that._doDealPayResult(_json);
                            return;
                        }
                        vc.toast(_json.msg);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        $that._closeDoBatchPayFeeModal();
                        vc.toast(errInfo);
                    });
            },
            _qrCodePayFee: function () {
                if ($that.batchPayConfirmInfo.primeRate == '') {
                    vc.toast('请选择支付方式');
                    return;
                }
                let _fees = $that.batchPayConfirmInfo.fees;
                let _payFees = [];
                _fees.forEach(_fee => {
                    _fee.childs.forEach(_feeConfig => {
                        const payFees = _feeConfig.childs.filter(child =>
                            _feeConfig.selectedFeeIds.includes(child.feeId)
                        );
                        _payFees.push({
                            configId: _feeConfig.configId,
                            selectedDiscountIds: _feeConfig.selectedDiscountIds,
                            payFees: payFees
                        });
                    });
                });
                let payType = ($that.batchPayConfirmInfo.primeRate == '12') ? 'yudifu' : 'qrCode';
                let _paramData = $that._mergeTransforPreFee();

                let _data = {
                    communityId: vc.getCurrentCommunity().communityId,
                    feeConfigs: _payFees,
                    primeRate: $that.batchPayConfirmInfo.primeRate,
                    acctId: $that.batchPayConfirmInfo.acctId,
                    remark: $that.batchPayConfirmInfo.remark,
                    amount: _paramData.feePrices,
                    receivedAmount: $that.batchPayConfirmInfo.feePrices,
                    discountAmount: $that.batchPayConfirmInfo.discount,
                    rules:_paramData.rules,
                    subServiceCode: 'fee.payBatchFee',
                    payType: payType,  // 新增参数
                    isBatch: 'yes'
                }
                vc.http.apiPost(
                    '/payment.qrCodePayment',
                    JSON.stringify(_data), {
                    emulateJSON: true
                },
                    function (json, res) {
                        console.log("执行到了这里---------》h获取到了二维码")
                        let _data = JSON.parse(json);
                        // 渝地付支付(primeRate=12)的特殊处理
                        if (payType == 'yudifu') {
                            if (_data.code == 0) {
                                // 显示二维码
                                console.log("_data.data.qrCode----->" + _data.data.qrCode);
                                vc.component.batchPayConfirmInfo.qrCodeUrl = _data.data.qrCode;
                                $that.$forceUpdate();
                            } else {
                                vc.toast(_data.msg || '生成二维码失败');
                            }
                            return;
                        }
                        if (_data.code == 404) {
                            vc.toast(_data.msg);
                            if (_data.data && _data.data.orderId) {
                                $that.batchPayConfirmInfo.orderId = _data.data.orderId;
                                $that.batchPayConfirmInfo.paymentPoolId = _data.data.paymentPoolId;
                                setTimeout('$that._qrCodeCheckPayFinish()', 5000);
                            }
                            return;
                        } else if (_data.code == 0) {
                            $that._doDealPayResult(_data);
                        } else {
                            vc.toast(_data.msg);
                        }
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.toast(errInfo);
                    }
                );
            },
            // 监听 primeRate 变化，如果是 12 则自动调用 _qrCodePayFee
            _watchPrimeRateChange: function (newVal, oldVal) {
                if (newVal == '12' && oldVal != '12') {
                    this._qrCodePayFee();
                }
            },
            _qrCodeCheckPayFinish: function () {
                let _payFees = $that.batchPayConfirmInfo.fees;
                _payFees.forEach(_fee => {
                    _fee.primeRate = $that.batchPayConfirmInfo.primeRate;
                });
                let _paramData = $that._mergeTransforPreFee();

                let _data = {
                    communityId: vc.getCurrentCommunity().communityId,
                    feeConfigs: _payFees,
                    primeRate: $that.batchPayConfirmInfo.primeRate,
                    acctId: $that.batchPayConfirmInfo.acctId,
                    remark: $that.batchPayConfirmInfo.remark,
                    amount: _paramData.feePrices,
                    receivedAmount: $that.batchPayConfirmInfo.feePrices,
                    discountAmount: $that.batchPayConfirmInfo.discount,
                    rules:_paramData.rules,
                    subServiceCode: 'fee.payBatchFee',
                    orderId: $that.batchPayConfirmInfo.orderId,
                    paymentPoolId: $that.batchPayConfirmInfo.paymentPoolId

                }
                vc.http.apiPost(
                    '/payment.checkPayFinish',
                    JSON.stringify(_data), {
                    emulateJSON: true
                },
                    function (json, res) {
                        let _data = JSON.parse(json);
                        if (_data.code == 404) {
                            vc.toast(_data.msg);
                            return;
                        } else if (_data.code == 41) {
                            setTimeout('$that._qrCodeCheckPayFinish()', 5000);
                            return;
                        }
                        $that._doDealPayResult(_data);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.toast(errInfo);
                    }
                );
            },
            _doDealPayResult: function (_json) {
                $that._closeDoBatchPayFeeModal();
                let _data = _json.data;
                //todo 这里等三秒，有可能收据队列还没有生成
                setTimeout(function () {
                    $("#payFeeResult").modal({
                        backdrop: "static", //点击空白处不关闭对话框
                        show: true
                    });
                }, 2000);
                vc.emit('batchPayFeeOrder','loadBatchFees', {});
                let _param = {
                    params: {
                        receiptCode: _data.receiptCode,
                        communityId: vc.getCurrentCommunity().communityId,
                        page: 1,
                        row: 100
                    }
                }
                vc.http.apiGet(
                    '/feeReceipt/queryFeeReceipt',
                    _param,
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        let _json = JSON.parse(json)
                        if (_json.code == 0 && _json.data && _json.data.length > 0) {
                            let receiptIds = '';
                            _json.data.forEach(item => {
                                receiptIds += (item.receiptId + ',');
                            })
                            $that.batchPayConfirmInfo.receiptIds = receiptIds;
                        }
                        $("#payFeeResult").modal({
                            backdrop: "static", //点击空白处不关闭对话框
                            show: true
                        });
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.toast(errInfo);
                    }
                );
            },
            _printAndBack: function (_merge) {
                $('#payFeeResult').modal("hide");
                window.open($that.batchPayConfirmInfo.printUrl + "?receiptIds=" + $that.batchPayConfirmInfo.receiptIds + "&merge=" + _merge)
            },

            _back: function () {
                $('#payFeeResult').modal("hide");
                vc.getBack();
            },
            _listFeePrintPages: function (_page, _rows) {
                let param = {
                    params: {
                        page: 1,
                        row: 1,
                        state: 'T',
                        communityId: vc.getCurrentCommunity().communityId
                    }
                };
                //发送get请求
                vc.http.apiGet('/feePrintPage.listFeePrintPage',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        let feePrintPages = _json.data;
                        if (feePrintPages && feePrintPages.length > 0) {
                            $that.batchPayConfirmInfo.printUrl = feePrintPages[0].url;
                        }
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _loadOwnerAccts: function () {
                let param = {
                    params: {
                        page: 1,
                        row: 100,
                        ownerId: $that.batchPayConfirmInfo.ownerId,
                        communityId: vc.getCurrentCommunity().communityId,
                    }
                };
                //发送get请求
                vc.http.apiGet('/account.queryCommunityOwnerAccount',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if (_json.data.length < 1) {
                            //vc.toast('当前没有可用账户');
                            return;
                        }
                        //账户余额
                        $that.batchPayConfirmInfo.accounts = _json.data;
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );

            },
            _computeTransforAmount: function () {
                let _feePrices = $that.batchPayConfirmInfo.feePrices;
                let _orgFeePrices = $that.batchPayConfirmInfo.orgFeePrices;
                let _transforAmount = parseFloat(_feePrices) - parseFloat(_orgFeePrices);
                if (_transforAmount < 0) {
                    $that.batchPayConfirmInfo.feePrices = _orgFeePrices;
                    vc.toast('输入金额不能小于' + _orgFeePrices);
                    return;
                }
                $that.batchPayConfirmInfo.transforAmount = _transforAmount.toFixed(2);
            },

            _resetBatchPayConfirm: function () {
                let _primeRates = $that.batchPayConfirmInfo.primeRates;
                let _printUrl = $that.batchPayConfirmInfo.printUrl;
                $that.batchPayConfirmInfo = {
                    payType: '',
                    fees: [],
                    primeRate: '',
                    primeRates: _primeRates,
                    payerObjName: '',
                    orderId: '',
                    paymentPoolId: '',
                    authCode: '',
                    feePrices: '',
                    orgFeePrices: 0.0,
                    receiptIds: '',
                    printUrl: _printUrl,
                    accountAmount: '',
                    acctId: '',
                    accounts: [],
                    ownerId: '',
                    rules: [],
                    ruleAmount: 0.0,
                    transforAmount: 0,
                    ruleId: '',
                    transforPreFees: [],
                    payerObjId:'',
                    payerObjType:'',
                    payerObjs:[]
                }
            },
            _mergeTransforPreFee:function(){
                let _data = vc.deepClone($that.batchPayConfirmInfo);
                let _ruleId = _data.ruleId;
                let _rules = _data.rules;
                if(_ruleId){
                    _rules.push({
                        ruleId:_ruleId,
                        cycles:24,
                        receivedAmount:_data.transforAmount,
                        startMonth:vc.dateFormatH(new Date())
                    });
                }
                let _feePrices = _data.feePrices;
                //计算费用实收=实收-预付款 
                if(_rules.length <1){
                    _data.feePrices = _feePrices;
                    return _data;
                }
                let _ruleAmount = 0;
                _rules.forEach(_rule => {
                    _ruleAmount += parseFloat(_rule.receivedAmount);
                });
                 _feePrices =  parseFloat(_feePrices)  - _ruleAmount;
                _data.feePrices = _feePrices.toFixed(2);
                return _data;
            },
            _selectTransforPreFee:function(){
                let _payerObjId = $that.batchPayConfirmInfo.payerObjId;
                if(!_payerObjId){
                    return;
                }
                $that.batchPayConfirmInfo.payerObjs.forEach(obj =>{
                    if(obj.payerObjId == _payerObjId){
                        $that.batchPayConfirmInfo.payerObjType = obj.payerObjType
                    }
                })
                $that._loadTransforPrePayFees();
            },
            //查询收据
            _loadTransforPrePayFees: function () {
                let param = {
                    params: {
                        communityId: vc.getCurrentCommunity().communityId,
                        payerObjId: $that.batchPayConfirmInfo.payerObjId,
                        payerObjType: $that.batchPayConfirmInfo.payerObjType,
                    }
                };
                //发送get请求
                vc.http.apiGet('/prePayFee.listPrePayFeeRule',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        $that.batchPayConfirmInfo.transforPreFees = _json.data.rules;
                        $that.$forceUpdate();
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
        }
    });
})(window.vc);

//这个是 batchPrePayFeeDialog.js 的js内容
(function (vc) {

    vc.extends({
        data: {
            batchPrePayFeeDialogInfo: {
                payerObjs:[],
                payerObjId: '',
                payerObjType: '',
                rules: [],
                selectRuleIds: [],
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('batchPrePayFeeDialog', 'openBatchPrePayFeeDialogModal', function (_param) {
                vc.copyObject(_param, $that.batchPrePayFeeDialogInfo);
                $('#batchPrePayFeeDialogModel').modal('show');
               // $that._loadFeeRuleInfo();

            });
        },
        methods: {
            _selectBatchPrePayObj:function(){
                let _payerObjId = $that.batchPrePayFeeDialogInfo.payerObjId;
                if(!_payerObjId){
                    return;
                }
                $that.batchPrePayFeeDialogInfo.payerObjs.forEach(obj =>{
                    if(obj.payerObjId == _payerObjId){
                        $that.batchPrePayFeeDialogInfo.payerObjType = obj.payerObjType
                    }
                })
                $that._loadFeeRuleInfo();
            },
            _loadFeeRuleInfo: function () {
                let param = {
                    params: {
                        communityId: vc.getCurrentCommunity().communityId,
                        payerObjId: $that.batchPrePayFeeDialogInfo.payerObjId,
                        payerObjType: $that.batchPrePayFeeDialogInfo.payerObjType,
                    }
                };
                //发送get请求
                vc.http.apiGet('/prePayFee.listPrePayFeeRule',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        _json.data.rules = _json.data.rules.map(item => {
                            item.receivedAmount = 0.00;
                            item.cycles = '';
                            item.startDateInputClass = 'startDateInputClass' + item.ruleId;
                            let _date = new Date(item.curYearMonth);
                            item.startMonth = _date.getFullYear() + '-' + (_date.getMonth() + 1);
                            item.selected = "N";
                            return item
                        });
                        vc.copyObject(_json.data, $that.batchPrePayFeeDialogInfo);
                        $that._computeReceivedAmount();
                        $that._initPageData();
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },

            _initPageData: function () {
                setTimeout(function () {
                    $that.batchPrePayFeeDialogInfo.rules.forEach(item => {
                        vc.initDateMonth(item.startDateInputClass, function (_value) {
                            item.startMonth = _value;
                        })
                    })
                }, 1000)
            },

            selectAllRule: function (event) {
                if (event.target.checked) {
                    $that.batchPrePayFeeDialogInfo.selectRuleIds = $that.batchPrePayFeeDialogInfo.rules.map(item => item.ruleId);
                    $that.batchPrePayFeeDialogInfo.rules.forEach(item => item.selected = "Y");
                } else {
                    $that.batchPrePayFeeDialogInfo.selectRuleIds = [];
                    $that.batchPrePayFeeDialogInfo.rules.forEach(item => item.selected = "N");
                }

                $that._computeReceivedAmount();

            },
            selectOneRule: function (rule) {
                let _selectedRuleIds = $that.batchPrePayFeeDialogInfo.selectRuleIds;
                if (_selectedRuleIds.includes(rule.ruleId)) {
                    rule.selected = "Y";
                } else {
                    rule.selected = "N";
                }
                $that._computeReceivedAmount();
            },

             /**
             * 下拉 change 事件
             * @param {*} _cycles
             */
             _changeMonth: function (_cycles, _rule) {
                let _rules = $that.batchPrePayFeeDialogInfo.rules;
                _rules.forEach(item => {
                    item.receivedAmount = 0.00;
                    if (item.cycles != '') {
                        item.receivedAmount = (parseFloat(item.cycles) * parseFloat(item.monthAmount)).toFixed(2);
                    }
                });
                $that._computeReceivedAmount();
                //  $that.listAndComputeFeeInfo(_newCycles);
                $that.computePreFeeDiscount(_cycles, _rule);
            },
            _computeReceivedAmount: function () {
                let _rules = $that.batchPrePayFeeDialogInfo.rules;
                if (_rules.length < 1) {
                    $that.batchPrePayFeeDialogInfo.receivedAmount = '0.00';
                    return;
                }
                let _receivedAmount = 0.00;

                let _selectedRuleIds = $that.batchPrePayFeeDialogInfo.selectRuleIds;
                if (_selectedRuleIds.length < 1) {
                    $that.batchPrePayFeeDialogInfo.receivedAmount = '0.00';
                    return;
                }

                _rules.forEach(item => {
                    if (_selectedRuleIds.includes(item.ruleId)) {
                        _receivedAmount += parseFloat(item.receivedAmount);
                        // todo 处理优惠折扣
                        if (item.selectedDiscountIds && item.selectedDiscountIds.length > 0) {
                            item.discounts.forEach(_discount => {
                                if (item.selectedDiscountIds.includes(_discount.discountId)) {
                                    _receivedAmount += parseFloat(_discount.discountPrice);
                                }
                            });
                        }
                    }
                });
                $that.batchPrePayFeeDialogInfo.receivedAmount = _receivedAmount.toFixed(2);
            },
            computePreFeeDiscount: function (_cycles, _rule) {
                _rule.showFlag = 'Y';
                let param = {
                    params: {
                        communityId: vc.getCurrentCommunity().communityId,
                        ruleId: _rule.ruleId,
                        cycles: _cycles
                    }
                };
                //发送get请求
                vc.http.apiGet('/feeDiscount.computePreFeeDiscount',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if (_json.data.length < 1) {
                            return;
                        }
                        _rule.discounts = _json.data;
                        _rule.selectedDiscountIds = _rule.discounts.map(_discount => _discount.discountId);
                        $that._computeReceivedAmount();
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            
            _doBatchPrePayFeeDialog: function () {
                let _rules = [];
                $that.batchPrePayFeeDialogInfo.rules.forEach(item => {
                    if ($that.batchPrePayFeeDialogInfo.selectRuleIds.includes(item.ruleId)) {
                        _rules.push(item);
                    }
                });

                vc.emit('batchPayFeeOrder','notifyPreFeeOrder',{
                    rules:_rules,
                    ruleAmount:$that.batchPrePayFeeDialogInfo.receivedAmount
                })
                $('#batchPrePayFeeDialogModel').modal('hide');
            },
            clearBatchPrePayFeeDialogInfo: function () {
                $that.batchPrePayFeeDialogInfo = {
                    payerObjs:[],
                    payerObjId: '',
                    payerObjType: '',
                    rules: [],
                    selectRuleIds: [],
                };
            }
        }
    });

})(window.vc);

//这个是 batchPayFeeOrder.js 的js内容
/**
 * 
 * batchFees:[{
    payerObj...
    "childs": [
    {
        feeConfig...,
        selectedFeeIds:[],
        discounts:[],
        "childs": [
        {
            fee....
        }
        ],

    },
	
    ],
}]
 */
(function (vc) {
    vc.extends({
        data: {
            batchPayFeeOrderInfo: {
                batchFees: [],
                payerObjId: '',
                fees: [],
                feePrices: 0.00,
                discount:0.0,
                communityId: vc.getCurrentCommunity().communityId,
                ownerId: '',
                payerObjType: '',
                remark: '',
                acctAmount: 0.0,
                acctId: '',
                depositAmount: 0.0,
                integralQuantity: 0,
                rules: [],
                ruleAmount: 0
            }
        },
        _initMethod: function () {
            let _ownerId = vc.getParam('ownerId');
            let _payerObjType = vc.getParam('payerObjType');
            let _payerObjId = vc.getParam('payerObjId');
            if (!vc.notNull(_ownerId)) {
                vc.toast('非法操作');
                vc.getBack();
                return;
            }
            $that.batchPayFeeOrderInfo.ownerId = _ownerId;
            $that.batchPayFeeOrderInfo.payerObjType = _payerObjType;
            $that.batchPayFeeOrderInfo.payerObjId = _payerObjId;
            $that._loadBatchFees();
        },
        _initEvent: function () {
            vc.on('batchPayFeeOrder','loadBatchFees',function(_param){
                $that.batchPayFeeOrderInfo.rules = [];
                $that.batchPayFeeOrderInfo.ruleAmount = '';
                $that._loadBatchFees();
            })
            vc.on('batchPayFeeOrder','notifyPreFeeOrder',function(_param){
                $that.batchPayFeeOrderInfo.rules = _param.rules;
                $that.batchPayFeeOrderInfo.ruleAmount = _param.ruleAmount;
                $that._doComputeTotalFee();
            })
        },
        methods: {
            _loadBatchFees: function () {
                let param = {
                    params: {
                        page: 1,
                        row: 500,
                        communityId: vc.getCurrentCommunity().communityId,
                        ownerId: $that.batchPayFeeOrderInfo.ownerId,
                        payerObjType: $that.batchPayFeeOrderInfo.payerObjType,
                        payerObjId: $that.batchPayFeeOrderInfo.payerObjId,
                        state: '2008001'
                    }
                };
                //发送get请求
                vc.http.apiGet('/fee.listOwnerFee',
                    param,
                    function (json) {
                        let _json = JSON.parse(json);
                        let _fees = _json.data;
                        if (!_fees || _fees.length < 1) {
                            return;
                        }
                        _fees = $that._initSelectAll(_fees);
                        $that.batchPayFeeOrderInfo.batchFees = _fees;
                        $that._swatchPayerObj(_fees[0]);
                        $that._doComputeTotalFee();
                    },
                    function () {
                        console.log('请求失败处理');
                    }
                );
            },
            _initSelectAll: function (_fees) {
                _fees.forEach(_fee => {
                    _fee.childs.forEach(_feeConfig => {
                        _feeConfig.selectedFeeIds = [];
                        _feeConfig.selectedFeeIds = _feeConfig.childs.map(_fee => _fee.feeId);
                        _feeConfig.childs.map(_fee => { _fee.receivedAmount = _fee.oweAmount; });
                        $that._computeDiscount(_feeConfig);
                    })
                });
                return _fees;
            },
            selectAllFee: function (_feeConfig) {
                if (_feeConfig.selectedFeeIds.length == _feeConfig.childs.length) {
                    _feeConfig.selectedFeeIds = [];
                } else {
                    _feeConfig.selectedFeeIds = _feeConfig.childs.map(_fee => _fee.feeId);
                }
                $that._computeDiscount(_feeConfig);
                $that._doComputeTotalFee();
            },
            selectOneFee: function (_feeConfig) {
                $that._computeDiscount(_feeConfig);
                $that._doComputeTotalFee();
            },
            selectOneDiscount : function (_feeConfig) {
                $that._doComputeTotalFee();
            },
            _swatchPayerObj: function (_payerObj) {
                $that.batchPayFeeOrderInfo.payerObjId = _payerObj.payerObjId;
                $that.batchPayFeeOrderInfo.acctAmount = _payerObj.acctAmount;
                $that.batchPayFeeOrderInfo.integralQuantity = _payerObj.integralQuantity;
                $that.batchPayFeeOrderInfo.fees = _payerObj.childs;
                $that._queryFeeDeposit(_payerObj);
            },
            switchSimplifyFeeFlag: function (_fee) {
                _fee.showFlag = (_fee.showFlag == 'Y') ? 'N' : 'Y';
                $that.$forceUpdate()
            },
            _openPayFee: function (_payType) {
                if ($that.batchPayFeeOrderInfo.feePrices <= 0) {
                    vc.toast('未选择费用');
                    return;
                }
                let payerObjs = [];
                $that.batchPayFeeOrderInfo.batchFees.forEach((_fee)=>{
                    payerObjs.push({
                        payerObjId:_fee.payerObjId,
                        payerObjName:_fee.payerObjName,
                        payerObjType:_fee.payerObjType
                    })
                })
                //打开model
                vc.emit('batchPayConfirm', 'openBatchPayFee', {
                    fees: $that.batchPayFeeOrderInfo.batchFees,
                    feePrices: $that.batchPayFeeOrderInfo.feePrices,
                    payType: _payType,
                    discount: $that.batchPayFeeOrderInfo.discount,
                    ownerId: $that.batchPayFeeOrderInfo.ownerId,
                    rules: $that.batchPayFeeOrderInfo.rules,
                    ruleAmount: $that.batchPayFeeOrderInfo.ruleAmount,
                    payerObjs:payerObjs
                });
            },
            _printOwnOrder: function () {
                vc.saveData('java110_printFee', {
                    fees: $that.batchPayFeeOrderInfo.batchFees,
                    roomName: $that.batchPayFeeOrderInfo.roomName
                });
                //打印催交单
                window.open('/#/pages/property/printBatchFee?roomId=' + $that.batchPayFeeOrderInfo.payObjId)
            },
            _doComputeTotalFee: function () {
                let _payObjs = $that.batchPayFeeOrderInfo.batchFees;
                let _totalFee = 0;
                let _discountFee = 0;
                _payObjs.forEach(obj => {
                    obj.childs.forEach(_feeConfig => {
                        _feeConfig.selectedFeeIds.forEach(_feeId => {
                            _feeConfig.childs.forEach(_fee => {
                                if (_fee.feeId == _feeId) {
                                    _totalFee += parseFloat(_fee.receivedAmount);
                                }
                            })
                        })
                        if(!_feeConfig.selectedDiscountIds || _feeConfig.selectedDiscountIds.length == 0){
                            return;
                        }
                        // 优惠是负数
                        _feeConfig.selectedDiscountIds.forEach(_discountId => {
                            _feeConfig.discounts.forEach(_discount => {
                                if (_discount.discountId == _discountId) {
                                    _totalFee += parseFloat(_discount.discountPrice);
                                    _discountFee += parseFloat(_discount.discountPrice);
                                }
                            })
                        })
                    })
                });

                let _ruleAmount = $that.batchPayFeeOrderInfo.ruleAmount;
                _totalFee += parseFloat(_ruleAmount);

                $that.batchPayFeeOrderInfo.feePrices = _totalFee.toFixed(2);
                $that.batchPayFeeOrderInfo.discount = _discountFee.toFixed(2);
                $that.$forceUpdate()

            },
            _computeDiscount: function (_feeConfig) {
                if(_feeConfig.selectedFeeIds.length == 0){
                    _feeConfig.discounts = [];
                    _feeConfig.selectedDiscountIds = [];
                    return ;
                }
                let data = {
                    communityId: vc.getCurrentCommunity().communityId,
                    feeIds: _feeConfig.selectedFeeIds,
                };
                //发送get请求
                vc.http.apiPost('/feeDiscount.computeFeeDiscount',
                    JSON.stringify(data),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if(_json.data.length< 1){
                            return;
                        }
                        _feeConfig.discounts = _json.data;
                        _feeConfig.selectedDiscountIds = _feeConfig.discounts.map(_discount => _discount.discountId);
                        $that._doComputeTotalFee();
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _settingsFeeCycle: function (_batchFee) {
                vc.emit('batchFeeCycle', 'openBatchCycle', _batchFee);
            },
            depositPay: function () {
                window.open('/#/pages/fee/refundDepositFee?roomId=' + $that.batchPayFeeOrderInfo.payObjId + '&ownerId=' + $that.batchPayFeeOrderInfo.ownerId);
            },
            _queryFeeDeposit: function (_payerObj) {
                let param = {
                    params: {
                        communityId: vc.getCurrentCommunity().communityId,
                        payerObjId: _payerObj.payerObjId,
                        ownerId: _payerObj.ownerId,
                        page: 1,
                        row: 100,
                        state: '1400'
                    }
                };

                //发送get请求
                vc.http.apiGet('/fee.queryFeeDeposit',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        let _deposit = 0.0;
                        if (_json.data && _json.data.length > 0) {
                            _json.data.forEach(_data => {
                                _deposit += parseFloat(_data.receivedAmount);
                            })
                        }
                        $that.batchPayFeeOrderInfo.depositAmount = _deposit.toFixed(2);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _allSelectOrNot:function(_selectType){
                let _batchFees = $that.batchPayFeeOrderInfo.batchFees;
                _batchFees.forEach(_fee => {
                    _fee.childs.forEach(_feeConfig => {
                        _feeConfig.selectedFeeIds = [];
                        $that._computeDiscount(_feeConfig);
                    })
                });
                if(_selectType != 'Y'){
                    $that._doComputeTotalFee();
                    return;
                }
                _batchFees.forEach(_fee => {
                    _fee.childs.forEach(_feeConfig => {
                        _feeConfig.selectedFeeIds = [];
                        _feeConfig.selectedFeeIds = _feeConfig.childs.map(_fee => _fee.feeId);
                        _feeConfig.childs.map(_fee => { _fee.receivedAmount = _fee.oweAmount; });
                        $that._computeDiscount(_feeConfig);
                    })
                });
                $that._doComputeTotalFee();

            },
            _currentSelectOrNot:function(_selectType){
                let _payerObjId = $that.batchPayFeeOrderInfo.payerObjId;
                let _batchFees = $that.batchPayFeeOrderInfo.batchFees;
                let _roomConfig = _batchFees.find(_fee => _fee.payerObjId == _payerObjId);
                _roomConfig.childs.forEach(_feeConfig => {
                    _feeConfig.selectedFeeIds = [];
                    $that._computeDiscount(_feeConfig);
                })
                if(_selectType != 'Y'){
                    $that._doComputeTotalFee();
                    return;
                }
                _roomConfig.childs.forEach(_feeConfig => {
                    _feeConfig.selectedFeeIds = [];
                    _feeConfig.selectedFeeIds = _feeConfig.childs.map(_fee => _fee.feeId);
                    _feeConfig.childs.map(_fee => { _fee.receivedAmount = _fee.oweAmount; });
                    $that._computeDiscount(_feeConfig);
                });
               
                $that._doComputeTotalFee();
            },
            _openBatchPrePayDialog:function(){
                let payerObjs = [];
                $that.batchPayFeeOrderInfo.batchFees.forEach((_fee)=>{
                    payerObjs.push({
                        payerObjId:_fee.payerObjId,
                        payerObjName:_fee.payerObjName,
                        payerObjType:_fee.payerObjType
                    })
                })
                vc.emit('batchPrePayFeeDialog', 'openBatchPrePayFeeDialogModal',{
                    payerObjs:payerObjs
                })
            }
        }
    });
})(window.vc);