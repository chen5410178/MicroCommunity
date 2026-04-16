<template>
  <el-dialog
    :title="$t('inspectionRoute.editPointTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="editInspectionRoutePointInfo"
      label-width="120px"
    >
      <el-form-item
        :label="$t('inspectionRoute.startTime')"
        prop="pointStartTime"
        :rules="[
          { required: true, message: $t('inspectionRoute.startTimeRequired') }
        ]"
      >
        <el-time-picker
          v-model="editInspectionRoutePointInfo.pointStartTime"
          :placeholder="$t('inspectionRoute.startTimePlaceholder')"
          value-format="HH:mm"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item
        :label="$t('inspectionRoute.endTime')"
        prop="pointEndTime"
        :rules="[
          { required: true, message: $t('inspectionRoute.endTimeRequired') }
        ]"
      >
        <el-time-picker
          v-model="editInspectionRoutePointInfo.pointEndTime"
          :placeholder="$t('inspectionRoute.endTimePlaceholder')"
          value-format="HH:mm"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item
        :label="$t('inspectionRoute.sortNumber')"
        prop="sortNumber"
        :rules="[
          { required: true, message: $t('inspectionRoute.sortNumberRequired') },
          { type: 'number', message: $t('inspectionRoute.sortNumberMustBeNumber') }
        ]"
      >
        <el-input
          v-model.number="editInspectionRoutePointInfo.sortNumber"
          :placeholder="$t('inspectionRoute.sortNumberPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="editInspectionRoutePoint"
      >
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateInspectionRoutePointRel } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'EditInspectionRoutePoint',
  data() {
    return {
      visible: false,
      editInspectionRoutePointInfo: {
        communityId: '',
        inspectionId: '',
        irpRelId: '',
        inspectionName: '',
        inspectionRouteId: '',
        pointEndTime: '',
        pointObjId: '',
        pointObjName: '',
        pointObjType: '',
        pointStartTime: '',
        pointTypeName: '',
        remark: '',
        sortNumber: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.visible = true
      this.editInspectionRoutePointInfo = { ...params }
      this.editInspectionRoutePointInfo.communityId = this.communityId
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    async editInspectionRoutePoint() {
      try {
        this.$refs.form.validate(async valid => {
          if (!valid) return

          await updateInspectionRoutePointRel(this.editInspectionRoutePointInfo)
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        })
      } catch (error) {
        console.error('修改巡检点失败:', error)
        this.$message.error(this.$t('inspectionRoute.editFailed'))
      }
    },
    handleClose() {
      this.visible = false
      this.editInspectionRoutePointInfo = {
        communityId: '',
        inspectionId: '',
        irpRelId: '',
        inspectionName: '',
        inspectionRouteId: '',
        pointEndTime: '',
        pointObjId: '',
        pointObjName: '',
        pointObjType: '',
        pointStartTime: '',
        pointTypeName: '',
        remark: '',
        sortNumber: ''
      }
    }
  }
}
</script>