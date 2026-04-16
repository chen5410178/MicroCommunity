<template>
  <el-dialog :title="$t('splitFee.title')" :visible.sync="visible" width="60%">
    <div class="ibox-content">
      <el-row :gutter="20">
        <el-col :span="4">
          <label class="col-form-label">
            {{ $t('splitFee.feeName') }}
          </label>
        </el-col>
        <el-col :span="8">
          <div class="  text-left">
            {{ splitFeeInfo.feeName }}
          </div>
        </el-col>
        <el-col :span="4">
          <label class="col-form-label">
            {{ $t('splitFee.timePeriod') }}
          </label>
        </el-col>
        <el-col :span="8">
          <div class="  text-left">
            {{ splitFeeInfo.startTime }}~{{ splitFeeInfo.endTime }}
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="4">
          <label class="col-form-label">
            {{ $t('splitFee.oweAmount') }}
          </label>
        </el-col>
        <el-col :span="20">
          <div class="  text-left">
            {{ splitFeeInfo.oweAmount }}
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="15">
            <thead>
              <tr>
                <th class="text-center">
                  {{ $t('splitFee.oweAmount') }}
                </th>
                <th class="text-center">
                  {{ $t('splitFee.remark') }}
                </th>
                <th class="text-center">
                  {{ $t('splitFee.operation') }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(fee, index) in splitFeeInfo.subFees" :key="index">
                <td class="text-center">
                  <input v-model="fee.oweAmount" type="number" :placeholder="$t('splitFee.oweAmountPlaceholder')"
                    class="form-control">
                </td>
                <td class="text-center">
                  <input v-model="fee.remark" type="text" :placeholder="$t('splitFee.remarkPlaceholder')"
                    class="form-control">
                </td>
                <td class="text-center">
                  <el-button type="text" @click="_addSplitSubFee">{{ $t('splitFee.add') }}</el-button>
                  <el-button type="text" v-if="splitFeeInfo.subFees.length > 1"
                    @click="_deleteSplitSubFee(index)">{{ $t('splitFee.delete') }}</el-button>
                </td>
              </tr>
            </tbody>
          </table>
        </el-col>
      </el-row>
      <div class="ibox-content">
        <el-button type="primary" class="float-right" @click="_doSplitFee">
          <i class="fa fa-check"></i>&nbsp;{{ $t('common.submit') }}
        </el-button>
        <el-button class="float-right" style="margin-right:20px;" @click="close">
          <i class="fa fa-times"></i>&nbsp;{{ $t('common.cancel') }}
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { splitFee } from '@/api/fee/listCarFeeApi'
import { dateFormat } from '@/utils/dateUtil'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SplitFee',
  data() {
    return {
      visible: false,
      splitFeeInfo: {
        feeId: '',
        feeName: '',
        startTime: '',
        endTime: '',
        oweAmount: '',
        subFees: []
      }
    }
  },
  methods: {
    open(fee) {
      this.splitFeeInfo.feeId = fee.feeId
      this.splitFeeInfo.feeName = fee.feeName
      this.splitFeeInfo.startTime = dateFormat(fee.startTime)
      this.splitFeeInfo.endTime = dateFormat(fee.endTime)
      this.splitFeeInfo.oweAmount = fee.oweAmount
      this.splitFeeInfo.subFees = []
      this._addSplitSubFee()
      this._addSplitSubFee()
      this.visible = true
    },
    close() {
      this.visible = false
    },
    _doSplitFee() {
      this.splitFeeInfo.communityId = getCommunityId()
      let _subFees = this.splitFeeInfo.subFees
      let sumAmount = 0
      _subFees.forEach(_fee => {
        sumAmount += parseFloat(_fee.oweAmount || 0)
      })
      sumAmount = sumAmount.toFixed(4)
      if (sumAmount != parseFloat(this.splitFeeInfo.oweAmount)) {
        this.$message.error(this.$t('splitFee.amountSumError'))
        return
      }

      splitFee(this.splitFeeInfo).then(response => {
        if (response.code == 0) {
          this.close()
          this.$emit('success')
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(response.msg)
        }
      }).catch(error => {
        console.log('请求失败处理')
        this.$message.error(error.message)
      })
    },
    _addSplitSubFee() {
      this.splitFeeInfo.subFees.push({
        oweAmount: '',
        remark: ''
      })
    },
    _deleteSplitSubFee(index) {
      this.splitFeeInfo.subFees.splice(index, 1)
    },
    dateFormat,
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 15px;
}

.col-form-label {
  padding-top: 7px;
  margin-bottom: 0;
  text-align: right;
}

.form-control {
  display: block;
  width: 90%;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  color: #495057;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

.form-control.border-0 {
  border: none;
  background-color: transparent;
  padding: 0.375rem 0;
}

.form-control:focus {
  color: #495057;
  background-color: #fff;
  border-color: #80bdff;
  outline: 0;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.btn {
  display: inline-block;
  font-weight: 400;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  user-select: none;
  border: 1px solid transparent;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  border-radius: 0.25rem;
  transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  cursor: pointer;
}

.btn-primary {
  color: #fff;
  background-color: #007bff;
  border-color: #007bff;
}

.btn-warning {
  color: #212529;
  background-color: #ffc107;
  border-color: #ffc107;
}


.float-right {
  float: right;
}

.ibox-content {
  padding: 15px 20px 20px 20px;
}

.table {
  width: 100%;
  margin-bottom: 1rem;
  color: #212529;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
}

.table thead th {
  vertical-align: bottom;
  border-bottom: 2px solid #dee2e6;
}

.table-stripped tbody tr:nth-of-type(odd) {
  background-color: rgba(0, 0, 0, 0.05);
}

.text-center {
  text-align: center;
}
</style>