<template>
  <el-dialog
    :title="$t('activitiesTypeManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color:#E6A23C;font-size:24px;"></i>
      <span style="margin-left:10px;">{{ $t('activitiesTypeManage.delete.confirm') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteActivitiesType } from '@/api/oa/activitiesTypeManageApi'

export default {
  name: 'DeleteActivitiesType',
  data() {
    return {
      visible: false,
      deleteData: {
        typeCd: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.deleteData = {
        typeCd: row.typeCd,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.deleteData = {
        typeCd: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        await deleteActivitiesType(this.deleteData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('activitiesTypeManage.delete.error'))
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}
</style>