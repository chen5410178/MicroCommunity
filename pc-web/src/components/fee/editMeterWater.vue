<template>
  <el-dialog
    :title="$t('meterWater.editMeterReading')"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item :label="$t('meterWater.preDegrees')">
        <el-input
          v-model="form.preDegrees"
          :placeholder="$t('meterWater.inputPreDegrees')"
          disabled
        />
      </el-form-item>

      <el-form-item :label="$t('meterWater.curDegrees')" prop="curDegrees">
        <el-input
          v-model="form.curDegrees"
          :placeholder="$t('meterWater.inputCurDegrees')"
          @change="handleDegreesChange"
        />
      </el-form-item>

      <el-form-item :label="$t('meterWater.preReadingTime')">
        <el-input
          v-model="form.preReadingTime"
          :placeholder="$t('meterWater.selectPreReadingTime')"
          disabled
        />
      </el-form-item>

      <el-form-item :label="$t('meterWater.curReadingTime')" prop="curReadingTime">
        <el-date-picker
          v-model="form.curReadingTime"
          type="datetime"
          :placeholder="$t('meterWater.selectCurReadingTime')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item :label="$t('meterWater.remark')" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="$t('meterWater.inputRemark')"
          :rows="2"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMeterWater } from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMeterWater',
  data() {
    return {
      dialogVisible: false,
      form: {
        waterId: '',
        curDegrees: '',
        curReadingTime: '',
        remark: '',
        communityId: '',
        preDegrees: '',
        preReadingTime: ''
      },
      rules: {
        curDegrees: [
          { required: true, message: this.$t('meterWater.curDegreesRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('meterWater.degreesFormatError') }
        ],
        curReadingTime: [
          { required: true, message: this.$t('meterWater.curReadingTimeRequired'), trigger: 'blur' }
        ],
        remark: [
          { max: 500, message: this.$t('meterWater.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.form = {
        waterId: data.waterId,
        curDegrees: data.curDegrees,
        curReadingTime: data.curReadingTime,
        remark: data.remark,
        communityId: this.form.communityId,
        preDegrees: data.preDegrees,
        preReadingTime: data.preReadingTime
      }
      this.dialogVisible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleDegreesChange() {
      const pre = parseFloat(this.form.preDegrees) || 0
      const cur = parseFloat(this.form.curDegrees) || 0
      if (pre > cur) {
        this.$message.warning(this.$t('meterWater.degreesCompareError'))
        this.form.curDegrees = ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          await updateMeterWater(this.form)
          this.$message.success(this.$t('common.operationSuccess'))
          this.dialogVisible = false
          this.$emit('success')
        } catch (error) {
          console.error('Failed to update meter water:', error)
          this.$message.error(error.message || this.$t('common.saveFailed'))
        }
      })
    }
  }
}
</script>