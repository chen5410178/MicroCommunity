<template>
  <el-dialog 
    :title="$t('common.delete')" 
    :visible.sync="visible" 
    width="30%"
  >
    <p>{{ $t('inspectionItemTitleManage.confirmDelete') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="confirmDelete">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteInspectionItemTitle } from '@/api/inspection/inspectionItemTitleManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteInspectionItemTitle',
  data() {
    return {
      visible: false,
      currentData: {}
    }
  },
  methods: {
    open(data) {
      this.currentData = { ...data }
      this.visible = true
    },
    async confirmDelete() {
      try {
        const params = {
          ...this.currentData,
          communityId: getCommunityId()
        }
        
        await deleteInspectionItemTitle(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteError'))
      }
    }
  }
}
</script>