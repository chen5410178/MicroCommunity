<template>
  <el-dialog
    :title="$t('propertyRightRegistration.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="visible = false"
  >
    <div class="delete-content">
      <p>{{ $t('propertyRightRegistration.delete.confirm') }}</p>
      <p class="delete-tip">{{ $t('propertyRightRegistration.delete.tip') }}</p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deletePropertyRightRegistration } from '@/api/room/propertyRightRegistrationManageApi'

export default {
  name: 'DeletePropertyRightRegistration',
  data() {
    return {
      visible: false,
      deleteData: {
        prrId: '',
        communityId: getCommunityId()
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.deleteData = {
        prrId: data.prrId,
        communityId: getCommunityId()
      }
    },
    async handleConfirm() {
      try {
        const res = await deletePropertyRightRegistration(this.deleteData.prrId)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg || this.$t('propertyRightRegistration.delete.error'))
        }
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error(this.$t('propertyRightRegistration.delete.error'))
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}

.delete-tip {
  color: #f56c6c;
  margin-top: 10px;
  font-size: 14px;
}

.dialog-footer {
  text-align: right;
}
</style>