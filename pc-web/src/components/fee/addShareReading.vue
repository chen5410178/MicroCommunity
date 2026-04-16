<template>
  <el-dialog :title="$t('shareReading.add.title')" :visible.sync="dialogVisible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('shareReading.add.meter')" prop="fsmId">
        <el-select v-model="form.fsmId" :placeholder="$t('shareReading.add.selectMeter')" style="width:100%"
          @change="handleMeterChange">
          <el-option v-for="(item, index) in floorShares" :key="index" :label="`${item.floorNum}栋-${item.meterTypeName}`"
            :value="item.fsmId" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('shareReading.add.preDegrees')" prop="preDegrees">
        <el-input v-model="form.preDegrees" :placeholder="$t('shareReading.add.inputPreDegrees')" />
      </el-form-item>
      <el-form-item :label="$t('shareReading.add.curDegrees')" prop="curDegrees">
        <el-input v-model="form.curDegrees" :placeholder="$t('shareReading.add.inputCurDegrees')" />
      </el-form-item>
      <el-form-item :label="$t('shareReading.add.preReadingTime')" prop="preReadingTime">
        <el-date-picker v-model="form.preReadingTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
          :placeholder="$t('shareReading.add.selectPreTime')" style="width:100%" />
      </el-form-item>
      <el-form-item :label="$t('shareReading.add.curReadingTime')" prop="curReadingTime">
        <el-date-picker v-model="form.curReadingTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
          :placeholder="$t('shareReading.add.selectCurTime')" style="width:100%" />
      </el-form-item>
      <el-form-item :label="$t('shareReading.add.remark')">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('shareReading.add.inputRemark')" :rows="3" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { saveFloorShareReading, listFloorShareMeter } from '@/api/fee/shareReadingApi'

export default {
  name: 'AddShareReading',
  data() {
    return {
      dialogVisible: false,
      floorShares: [],
      form: {
        fsmId: '',
        preDegrees: '',
        curDegrees: '',
        preReadingTime: '',
        curReadingTime: '',
        remark: '',
        communityId: ''
      },
      rules: {
        fsmId: [
          { required: true, message: this.$t('shareReading.validate.selectMeter'), trigger: 'change' }
        ],
        preDegrees: [
          { required: true, message: this.$t('shareReading.validate.inputPreDegrees'), trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: this.$t('shareReading.validate.number'), trigger: 'blur' }
        ],
        curDegrees: [
          { required: true, message: this.$t('shareReading.validate.inputCurDegrees'), trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: this.$t('shareReading.validate.number'), trigger: 'blur' }
        ],
        preReadingTime: [
          { required: true, message: this.$t('shareReading.validate.selectPreTime'), trigger: 'change' }
        ],
        curReadingTime: [
          { required: true, message: this.$t('shareReading.validate.selectCurTime'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.getFloorShares()
    },
    async getFloorShares() {
      try {
        const params = {
          page: 1,
          row: 300,
          communityId: getCommunityId()
        }
        const { data } = await listFloorShareMeter(params)
        this.floorShares = data
      } catch (error) {
        console.error('获取公摊表列表失败:', error)
      }
    },
    handleMeterChange(val) {
      const selectedMeter = this.floorShares.find(item => item.fsmId === val)
      if (selectedMeter) {
        this.form.preDegrees = selectedMeter.curDegree
        this.form.preReadingTime = selectedMeter.curReadingTime
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            this.form.communityId = getCommunityId()
            await saveFloorShareReading(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加公摊抄表失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        fsmId: '',
        preDegrees: '',
        curDegrees: '',
        preReadingTime: '',
        curReadingTime: '',
        remark: '',
        communityId: ''
      }
    }
  }
}
</script>