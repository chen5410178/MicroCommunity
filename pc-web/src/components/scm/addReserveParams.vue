<template>
  <el-dialog
    :title="$t('reserveParamsManage.addTitle')"
    :visible.sync="visible"
    width="70%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item
        :label="$t('reserveParamsManage.name')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="$t('reserveParamsManage.namePlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('reserveParamsManage.paramWay')"
        prop="paramWay"
      >
        <el-select
          v-model="formData.paramWay"
          :placeholder="$t('reserveParamsManage.paramWayPlaceholder')"
          style="width: 100%"
          @change="handleParamWayChange"
        >
          <el-option
            :label="$t('reserveParamsManage.day')"
            value="1"
          />
          <el-option
            :label="$t('reserveParamsManage.week')"
            value="2"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="formData.paramWay === '1'"
        :label="$t('reserveParamsManage.days')"
        prop="days"
      >
        <el-checkbox-group v-model="formData.days">
          <el-checkbox
            v-for="day in 31"
            :key="day"
            :label="day"
          >
            {{ day }}{{ $t('reserveParamsManage.dayUnit') }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item
        v-if="formData.paramWay === '2'"
        :label="$t('reserveParamsManage.weekdays')"
        prop="workdays"
      >
        <el-checkbox-group v-model="formData.workdays">
          <el-checkbox
            v-for="(weekday, index) in weekdays"
            :key="index"
            :label="index + 1"
          >
            {{ weekday }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item
        :label="$t('reserveParamsManage.startTime')"
        prop="startTime"
      >
        <el-time-picker
          v-model="formData.startTime"
          :placeholder="$t('reserveParamsManage.startTimePlaceholder')"
          value-format="HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item
        :label="$t('reserveParamsManage.maxQuantity')"
        prop="maxQuantity"
      >
        <el-input-number
          v-model="formData.maxQuantity"
          :min="1"
          :placeholder="$t('reserveParamsManage.maxQuantityPlaceholder')"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item
        :label="$t('reserveParamsManage.hoursMaxQuantity')"
        prop="hoursMaxQuantity"
      >
        <el-input-number
          v-model="formData.hoursMaxQuantity"
          :min="1"
          :placeholder="$t('reserveParamsManage.hoursMaxQuantityPlaceholder')"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveReserveParams } from '@/api/scm/reserveParamsManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddReserveParams',
  data() {
    return {
      visible: false,
      formData: {
        name: '',
        paramWay: '',
        days: [],
        workdays: [],
        startTime: '',
        maxQuantity: '',
        hoursMaxQuantity: '',
        communityId: ''
      },
      weekdays: [
        this.$t('common.monday'),
        this.$t('common.tuesday'),
        this.$t('common.wednesday'),
        this.$t('common.thursday'),
        this.$t('common.friday'),
        this.$t('common.saturday'),
        this.$t('common.sunday')
      ],
      rules: {
        name: [
          { required: true, message: this.$t('reserveParamsManage.nameRequired'), trigger: 'blur' }
        ],
        paramWay: [
          { required: true, message: this.$t('reserveParamsManage.paramWayRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('reserveParamsManage.startTimeRequired'), trigger: 'change' }
        ],
        maxQuantity: [
          { required: true, message: this.$t('reserveParamsManage.maxQuantityRequired'), trigger: 'blur' }
        ],
        hoursMaxQuantity: [
          { required: true, message: this.$t('reserveParamsManage.hoursMaxQuantityRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.formData.communityId = getCommunityId()
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    handleParamWayChange(val) {
      if (val === '1') {
        this.formData.days = Array.from({ length: 31 }, (_, i) => i + 1)
        this.formData.workdays = []
      } else {
        this.formData.workdays = Array.from({ length: 7 }, (_, i) => i + 1)
        this.formData.days = []
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const params = {
              ...this.formData,
              paramWayText: this.formData.paramWay === '1' 
                ? this.formData.days.join(',') 
                : this.formData.workdays.join(',')
            }
            await saveReserveParams(params)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.addFailed'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>