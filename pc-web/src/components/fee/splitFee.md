<div id="splitFeeModel" class="modal fade" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-body">
                <h3 class="m-t-none m-b ">
                    <vc:i18n name="拆分费用" namespace="splitFee"></vc:i18n>
                </h3>
                <div class="ibox-content">
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label ">
                            <vc:i18n name="费用项目:" namespace="splitFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4 ">
                            <div class="form-control border-0">
                                {{splitFeeInfo.feeName}}
                            </div>
                        </div>
                        <label class="col-sm-2 col-form-label ">
                            <vc:i18n name="时间段:" namespace="splitFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4 ">
                            <div class="form-control border-0">
                                {{splitFeeInfo.startTime}}~{{splitFeeInfo.endTime}}
                            </div>
                        </div>
                        <label class="col-sm-2 col-form-label ">
                            <vc:i18n name="应收金额:" namespace="splitFee"></vc:i18n>
                        </label>
                        <div class="col-sm-4">
                            <div class="form-control border-0">
                                {{splitFeeInfo.oweAmount}}
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">

                        <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="15">
                            <thead>
                                <tr>
                                    <th class="text-center">
                                        <vc:i18n name="应收金额" namespace="splitFee"></vc:i18n>
                                    </th>
                                    <th class="text-center">
                                        <vc:i18n name="说明" namespace="splitFee"></vc:i18n>
                                    </th>
                                    <th class="text-center">
                                        <vc:i18n name="操作" namespace="splitFee"></vc:i18n>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(fee,index) in splitFeeInfo.subFees">
                                    <td class="text-center">
                                        <input v-model="fee.oweAmount" type="number"
                                            :placeholder="vc.i18n('必填，请填写应收金额','splitFee')" class="form-control">
                                    </td>
                                    <td class="text-center">
                                        <input v-model="fee.remark" type="text"
                                            :placeholder="vc.i18n('选填，请填写说明','splitFee')" class="form-control">
                                    </td>
                                    <td class="text-center">
                                        <button type="button" class="btn btn-link" @click="_addSplitSubFee()">增加</button>
                                        <button type="button" class="btn btn-link" v-if="splitFeeInfo.subFees.length>1" @click="_deleteSplitSubFee(index)">删除</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="ibox-content">
                        <button class="btn btn-primary float-right" type="button" v-on:click="_doSplitFee()">
                            <i class="fa fa-check"></i>&nbsp;提交
                        </button>
                        <button type="button" class="btn btn-warning float-right" style="margin-right:20px;"
                            data-dismiss="modal">
                            <i class="fa fa-times"></i>&nbsp;取消
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
(function (vc) {
    vc.extends({
        data: {
            splitFeeInfo: {
                feeId: '',
                feeName: '',
                startTime: '',
                remark: '',
                endTime: '',
                oweAmount: '',
                subFees: []
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('splitFee', 'openSplitFeeModal',
                function (_fee) {
                    $that.splitFeeInfo.feeId = _fee.feeId;
                    $that.splitFeeInfo.feeName = _fee.feeName;
                    $that.splitFeeInfo.startTime = vc.dateFormat(_fee.startTime);
                    $that.splitFeeInfo.endTime = vc.dateFormat(_fee.endTime);
                    $that.splitFeeInfo.oweAmount = _fee.oweAmount;
                    $that.splitFeeInfo.subFees = [];
                    $that._addSplitSubFee();
                    $that._addSplitSubFee();
                    $('#splitFeeModel').modal('show');
                });
        },
        methods: {
            _doSplitFee: function () {
                $that.splitFeeInfo.communityId = vc.getCurrentCommunity().communityId;
                let _subFees = $that.splitFeeInfo.subFees;
                let sumAmount = 0;
                _subFees.forEach(_fee => {
                    sumAmount += parseFloat(_fee.oweAmount);
                });
                sumAmount = sumAmount.toFixed(4);
                if (sumAmount != parseFloat($that.splitFeeInfo.oweAmount)) {
                    vc.toast("分摊金额之和不等于应收金额!");
                    return;
                }
                vc.http.apiPost('/feeSub.splitPayFee',
                    JSON.stringify($that.splitFeeInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if (_json.code == 0) {
                            //关闭model
                            $('#splitFeeModel').modal('hide');
                            vc.emit('feeDetailSub', 'loadSub', {});
                            vc.emit('listParkingSpaceFee', 'notify', {});
                            vc.emit('simplifyRoomFee', 'notify',{})
                            vc.toast("操作成功");
                            return;
                        } else {
                            vc.toast(_json.msg);
                        }
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.toast(errInfo);
                    });
            },
            _addSplitSubFee: function () {
                $that.splitFeeInfo.subFees.push({
                    oweAmount: '',
                    remark: '',
                });
            },
            _deleteSplitSubFee: function (index) {
                $that.splitFeeInfo.subFees.splice(index, 1);
            }
        }
    });
})(window.vc);