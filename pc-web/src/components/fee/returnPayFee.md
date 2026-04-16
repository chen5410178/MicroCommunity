
<!--这个是 returnPayFee.html 的html内容-->
<div id="returnPayFeeModel" class="modal fade" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-body">
                <h3 class="m-t-none m-b ">
                    <vc:i18n name="退费" namespace="returnPayFee"></vc:i18n>
                </h3>
                <div class="ibox-content">
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="订单号" namespace="returnPayFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4">
                            {{returnPayFeeInfo.payOrderId}}
                        </div>
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="订单类型" namespace="returnPayFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4">
                            {{returnPayFeeInfo.orderType == '2002'?'缴费单':'预存单'}}
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="收费对象" namespace="returnPayFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4">
                            {{returnPayFeeInfo.payerObjName}}
                        </div>
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="业主" namespace="returnPayFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4">
                            {{returnPayFeeInfo.ownerName}}
                        </div>
                    </div>
                    <div class="form-group row">
                        <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="15">
                            <thead>
                                <tr>
                                    <th class="text-center">
                                        <input type="checkbox" class="i-checks all-check"
                                            :checked="returnPayFeeInfo.selectDetailIds.length >= returnPayFeeInfo.refundFees.length"
                                            @click="selectAllRule($event)">
                                    </th>
                                    <th class="text-center">
                                        <vc:i18n name="费用项" namespace="prePayFeeOrder"></vc:i18n>
                                    </th>
                                    <th class="text-center">
                                        <vc:i18n name="缴费时间段" namespace="prePayFeeOrder"></vc:i18n>
                                    </th>
                                    <th class="text-center">
                                        <vc:i18n name="实收金额" namespace="prePayFeeOrder"></vc:i18n>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <template v-for="fee in returnPayFeeInfo.refundFees">
                                    <tr>
                                        <td class="text-center">
                                            <input type="checkbox" class="i-checks checkItem" v-bind:value="fee.detailId"
                                                @change="selectOneRule(fee)" v-model="returnPayFeeInfo.selectDetailIds"
                                                >
                                        </td>
                                        <td class="text-center">{{fee.feeName}}</td>
                                        <td class="text-center">{{fee.startTime}}~{{fee.endTime}}</td>
                                        <td class="text-center">{{fee.receivedAmount}}</td>
                                    </tr>
                                </template>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">
                            <span style="color: red">*</span>
                            <span>
                                <vc:i18n name="退费原因" namespace="returnPayFee"></vc:i18n>
                            </span>
                        </label>
                        <div class="col-sm-10">
                            <textarea placeholder="" class="form-control" v-model="returnPayFeeInfo.reason">
                                    </textarea>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <button type="button" class="btn btn-warning float-right" style="margin-right:20px;"
                            data-dismiss="modal">
                            <vc:i18n name="取消" namespace="returnPayFee"></vc:i18n>
                        </button>
                        <button v-on:click="submitReturnPayFee()" type="button" class="btn btn-primary float-right"
                            style="margin-right:20px;">
                            <vc:i18n name="提交退费申请" namespace="returnPayFee"></vc:i18n>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JS Content -->
//这个是 returnPayFee.js 的js内容
(function (vc, vm) {
    vc.extends({
        data: {
            returnPayFeeInfo: {
                feeId:'',
                payOrderId: '',
                orderType: '',
                payerObjName: '',
                ownerName: '',
                communityId: '',
                selectDetailIds: [],
                refundFees: [],
                reason:''
            }
        },
        _initMethod: function () {
        },
        _initEvent: function () {
            vc.on('returnPayFee', 'openReturnPayFeeModel', function (_params) {
                $that.refreshFeeDetailInfo();
                vc.copyObject(_params, $that.returnPayFeeInfo);
                $that._listPayFeeOrders();
                $('#returnPayFeeModel').modal('show');
            });
        },
        methods: {
            refreshFeeDetailInfo: function () {
                $that.returnPayFeeInfo = {
                    feeId:'',
                    payOrderId: '',
                    orderType: '',
                    payerObjName: '',
                    ownerName: '',
                    communityId: '',
                    selectDetailIds: [],
                    refundFees: [],
                    reason:''
                }
            },
            _listPayFeeOrders: function () {
                let param = {
                    params: {
                        page:1,
                        row:1,
                        communityId:vc.getCurrentCommunity().communityId,
                        payOrderId: $that.returnPayFeeInfo.payOrderId
                    }
                };
                $that.returnPayFeeInfo.selectDetailIds = [];
                //发送get请求
                vc.http.apiGet('/feeOrder.listPayFeeOrder',
                    param,
                    function (json, res) {
                        let _json = JSON.parse(json);
                        vc.copyObject(_json.data[0], $that.returnPayFeeInfo);
                        $that.returnPayFeeInfo.refundFees = _json.data[0].payFeeOrderDetailDtos;
                        _json.data[0].payFeeOrderDetailDtos.forEach(_item => {
                            $that.returnPayFeeInfo.selectDetailIds.push(_item.detailId);
                        })
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            selectOneRule:function (_item) {

            },
            returnPayFeeValidate: function () {
                return vc.validate.validate({
                    returnPayFeeInfo: $that.returnPayFeeInfo
                }, {
                    'returnPayFeeInfo.reason': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "退费原因不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,200",
                            errInfo: "退费原因不能超过200位"
                        }
                    ]
                });
            },
            submitReturnPayFee: function () {
                if (!$that.returnPayFeeValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }
                let _selectDetailIds = $that.returnPayFeeInfo.selectDetailIds;
                if(_selectDetailIds.length == 0){
                    vc.toast("请选择需要退费的费用项");
                    return;
                }
                $that.returnPayFeeInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.apiPost(
                    '/returnPayFee.saveReturnPayFee',
                    JSON.stringify($that.returnPayFeeInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if (_json.code == 0) {
                            $('#returnPayFeeModel').modal('hide');
                            vc.emit('propertyFee', 'listFeeDetail', $that.returnPayFeeInfo);
                            vc.emit('refundDepositFee', 'listFeeDeposit', $that.returnPayFeeInfo);

                            $that.refreshFeeDetailInfo();
                            vc.toast("操作成功");
                            return;
                        } else {
                            vc.toast(_json.msg);
                        }
                    },
                    function (errInfo, error) {
                        vc.toast(errInfo);
                    });
            }
        }
    });
})(window.vc, window.$that);
