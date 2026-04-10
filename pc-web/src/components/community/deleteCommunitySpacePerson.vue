<template>
  <el-dialog
    :title="$t('deleteCommunitySpacePerson.confirmTitle')"
    :visible.sync="visible"
    width="30%"
    @close="close"
  >
    <div>
      <p>{{ $t('deleteCommunitySpacePerson.confirmContent') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">{{ $t('deleteCommunitySpacePerson.cancel') }}</el-button>
      <el-button type="primary" @click="deleteCommunitySpacePerson">{{ $t('deleteCommunitySpacePerson.confirmCancel') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCommunitySpacePerson } from '@/api/community/communitySpacePersonManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteCommunitySpacePerson',
  data() {
    return {
      visible: false,
      deleteData: {}
    }
  },
  methods: {
    open(data) {
      this.deleteData = { ...data }
      this.visible = true
    },
    close() {
      this.visible = false
    },
    async deleteCommunitySpacePerson() {
      try {
        const communityId = getCommunityId()
        const data = {
          ...this.deleteData,
          communityId
        }
        await deleteCommunitySpacePerson(data)
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.close()
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>