<template>
  <el-dialog :title="$t('inspectionRoute.editRouteTitle')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="editInspectionRouteInfo" :rules="rules" label-width="120px">
      <el-form-item :label="$t('inspectionRoute.routeName')" prop="routeName">
        <el-input v-model.trim="editInspectionRouteInfo.routeName"
          :placeholder="$t('inspectionRoute.routeNamePlaceholder')" clearable />
      </el-form-item>
      <el-form-item :label="$t('inspectionRoute.sequence')" prop="seq">
        <el-input v-model.number="editInspectionRouteInfo.seq" :placeholder="$t('inspectionRoute.sequencePlaceholder')"
          clearable />
      </el-form-item>
      <el-form-item :label="$t('inspectionRoute.remark')" prop="remark">
        <el-input v-model.trim="editInspectionRouteInfo.remark" :placeholder="$t('inspectionRoute.remarkPlaceholder')"
          type="textarea" :rows="3" clearable />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="editInspectionRoute">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateInspectionRoute } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'EditInspectionRoute',
  data() {
    return {
      visible: false,
      editInspectionRouteInfo: {
        inspectionRouteId: '',
        routeName: '',
        seq: 1,
        remark: ''
      },
      communityId: '',
      rules: {
        routeName: [
          { required: true, message: this.$t('inspectionRoute.routeNameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('inspectionRoute.routeNameMaxLength'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('inspectionRoute.sequenceRequired'), trigger: 'blur' },
        ],
        remark: [
          { max: 200, message: this.$t('inspectionRoute.remarkMaxLength'), trigger: 'blur' }
        ],
        inspectionRouteId: [
          { required: true, message: this.$t('inspectionRoute.routeIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(route) {
      this.visible = true
      this.editInspectionRouteInfo = { ...route }
      this.editInspectionRouteInfo.communityId = this.communityId
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    async editInspectionRoute() {
      try {
        await this.$refs.form.validate()

        await updateInspectionRoute(this.editInspectionRouteInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        if (error !== 'validate') {
          console.error('修改巡检路线失败:', error)
          this.$message.error(this.$t('inspectionRoute.editFailed'))
        }
      }
    },
    handleClose() {
      this.visible = false
      this.editInspectionRouteInfo = {
        inspectionRouteId: '',
        routeName: '',
        seq: 1,
        remark: ''
      }
      this.$refs.form.clearValidate()
    }
  }
}
</script>