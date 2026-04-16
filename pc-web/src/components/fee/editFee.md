<div id="editFeeModel" class="modal fade" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-body">
                <h3 class="m-t-none m-b ">
                    <vc:i18n name="费用变更" namespace="editFee"></vc:i18n>
                </h3>
                <div class="ibox-content">

                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="开始时间" namespace="editFee"></vc:i18n>
                        </label>
                        <div class="col-sm-10">
                            <input v-model="editFeeInfo.startTime" type="text"
                                :placeholder="vc.i18n('必填，请填写应收开始时间','editFee')" class="form-control editFeeStartTime">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="结束时间" namespace="editFee"></vc:i18n>
                        </label>
                        <div class="col-sm-10">
                            <input v-model="editFeeInfo.endTime" type="text"
                                :placeholder="vc.i18n('必填，请填写应收结束时间','editFee')" class="form-control editFeeEndTime">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">
                            <vc:i18n name="应收金额" namespace="editFee"></vc:i18n>
                        </label>
                        <div class="col-sm-10">
                            <input v-model="editFeeInfo.oweAmount" type="text"
                                :placeholder="vc.i18n('必填，请填写应收金额','editFee')" class="form-control">
                        </div>
                    </div>
                    <div class="ibox-content">
                        <button class="btn btn-primary float-right" type="button" v-on:click="_doEidtFee()">
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
            editFeeInfo: {
                feeId: '',
                startTime: '',
                endTime: '',
                oweAmount: '',
            }
        },
        _initMethod: function () {
            $that._initEditFeeDateInfo();
        },
        _initEvent: function () {
            vc.on('editFee', 'openEditFeeModal',
                function (_fee) {
                    $that.clearAddFeeConfigInfo();
                    vc.copyObject(_fee, $that.editFeeInfo);
                    if (_fee.startTime.indexOf(":") == -1) {
                        $that.editFeeInfo.startTime = $that.editFeeInfo.startTime;
                    }
                    /*if (_fee.endTime.indexOf(":") == -1) {
                        $that.editFeeInfo.endTime = $that.editFeeInfo.endTime;
                    }*/
                    $that.editFeeInfo.endTime = _fee.endTime.split(' ')[0];
                    $('#editFeeModel').modal('show');
                });
        },
        methods: {
            _initEditFeeDateInfo: function () {

                vc.initDate('editFeeStartTime', function (_value) {
                    let start = Date.parse(new Date(_value));
                    let end = Date.parse(new Date($that.editFeeInfo.endTime));
                    if (start - end >= 0) {
                        vc.toast("建账时间必须小于计费起始时间");
                        $(".editFeeStartTime").val('');
                        $that.editFeeInfo.startTime = "";
                    } else {
                        $that.editFeeInfo.startTime = _value;
                    }
                });
                vc.initDate('editFeeEndTime', function (_value) {
                    let start = Date.parse(new Date($that.editFeeInfo.startTime))
                    let end = Date.parse(new Date(_value))
                    if (start - end >= 0) {
                        vc.toast("计费起始时间必须大于建账时间");
                        $(".editFeeEndTime").val('');
                        $that.editFeeInfo.endTime = "";
                    } else {
                        $that.editFeeInfo.endTime = _value;
                    }
                })

            },
            editFeeValidate() {
                return vc.validate.validate({
                    editFeeInfo: $that.editFeeInfo
                }, {
                    'editFeeInfo.startTime': [{
                        limit: "required",
                        param: "",
                        errInfo: "建账时间不能为空"
                    }],
                    'editFeeInfo.endTime': [{
                        limit: "required",
                        param: "",
                        errInfo: "计费起始时间不能为空"
                    }]
                });
            },
            _doEidtFee: function () {
                if (!$that.editFeeValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }
                $that.editFeeInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.apiPost('/fee.updateFee', JSON.stringify($that.editFeeInfo), {
                    emulateJSON: true
                },
                    function (json, res) {
                        let _json = JSON.parse(json);
                        if (_json.code == 0) {
                            //关闭model
                            $('#editFeeModel').modal('hide');
                            $that.clearAddFeeConfigInfo();
                            vc.emit('roomCreateFee', 'notify', {});
                            vc.emit('listParkingSpaceFee', 'notify', {});
                            vc.emit('listContractFee', 'notify', {});
                            vc.emit('simplifyRoomFee', 'notify', {});
                            vc.emit('simplifyCarFee', 'notify', {});
                            vc.emit('simplifyContractFee', 'notify', {});
                            vc.emit('carDetailFee', 'notify', {});
                            vc.emit('contractDetailRoomFee', 'notify', {});
                            vc.emit('ownerDetailRoomFee', 'notify', {});
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
            clearAddFeeConfigInfo: function () {
                $that.editFeeInfo = {
                    feeId: '',
                    startTime: '',
                    endTime: '',
                    oweAmount: '',
                };
            }
        }
    });
})(window.vc);