<template>
  <el-dialog
    :title="$t('deleteCommunity.title')"
    :visible.sync="visible"
    width="30%"
    @close="closeDeleteCommunityModel"
  >
    <div style="text-align: center">
      <p>{{ $t('deleteCommunity.confirmText') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDeleteCommunityModel">{{ $t('deleteCommunity.cancel') }}</el-button>
      <el-button type="primary" @click="deleteCommunity">{{ $t('deleteCommunity.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteCommunity } from '@/api/community/communityManageApi'

export default {
  name: 'DeleteCommunity',
  data() {
    return {
      visible: false,
      deleteCommunityInfo: {}
    }
  },

  methods: {
    openModal(params) {
      this.deleteCommunityInfo = params
      this.visible = true
    },
    deleteCommunity() {
      deleteCommunity(this.deleteCommunityInfo).then(res => {
        console.log(res)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('listData')
      }).catch(error => {
        this.$message.error(error.message)
      })
    },
    closeDeleteCommunityModel() {
      this.visible = false
    }
  }
}
</script>