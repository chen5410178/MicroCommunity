<template>
  <el-dialog
    :title="$t('repairDispatchManage.confirmOperation')"
    :visible.sync="visible"
    width="30%"
  >
    <div class="text-center">
      <p>{{ $t('repairDispatchManage.confirmStartRepair') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { repairStart } from '@/api/work/repairDispatchManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'StartRepair',
  data() {
    return {
      visible: false,
      form: {}
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
    },
    async submitForm() {
      try {
        const data = {
          ...this.form,
          communityId: getCommunityId()
        }
        
        await repairStart(data)
        this.$emit('success')
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error('启动失败:', error)
        this.$message.error(this.$t('repairDispatchManage.startFailed'))
      }
    }
  }
}
</script>