<template>
  <el-dialog
    :title="$t('inspectionRoute.addRouteTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="addInspectionRouteInfo"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('inspectionRoute.routeName')"
        prop="routeName"
      >
        <el-input
          v-model.trim="addInspectionRouteInfo.routeName"
          :placeholder="$t('inspectionRoute.routeNamePlaceholder')"
          clearable
        />
      </el-form-item>
      <el-form-item
        :label="$t('inspectionRoute.sequence')"
        prop="seq"
      >
        <el-input
          v-model.number="addInspectionRouteInfo.seq"
          :placeholder="$t('inspectionRoute.sequencePlaceholder')"
          clearable
        />
      </el-form-item>
      <el-form-item
        :label="$t('inspectionRoute.remark')"
        prop="remark"
      >
        <el-input
          v-model.trim="addInspectionRouteInfo.remark"
          :placeholder="$t('inspectionRoute.remarkPlaceholder')"
          type="textarea"
          :rows="3"
          clearable
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="saveInspectionRouteInfo"
      >
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { saveInspectionRoute } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'AddInspectionRoute',
  data() {
    return {
      visible: false,
      addInspectionRouteInfo: {
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
          { type: 'number', message: this.$t('inspectionRoute.sequenceMustBeNumber'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('inspectionRoute.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    async saveInspectionRouteInfo() {
      try {
        await this.$refs.form.validate()
        
        const params = {
          ...this.addInspectionRouteInfo,
          communityId: this.communityId
        }
        
        await saveInspectionRoute(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        if (error !== 'validate') {
          console.error('添加巡检路线失败:', error)
          this.$message.error(this.$t('inspectionRoute.addFailed'))
        }
      }
    },
    handleClose() {
      this.visible = false
      this.addInspectionRouteInfo = {
        routeName: '',
        seq: 1,
        remark: ''
      }
      this.$refs.form.clearValidate()
    }
  }
}
</script>