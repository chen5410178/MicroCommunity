<template>
  <el-dialog :title="$t('communityDataToIot.title')" :visible.sync="visible" width="30%"
    @close="closeCommunityDataToIotModel">
    <div style="text-align: center">
      <p>{{ $t('communityDataToIot.confirmText') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeCommunityDataToIotModel">{{ $t('communityDataToIot.cancel') }}</el-button>
      <el-button type="primary" @click="_toSendCommunityDataToIot">{{ $t('communityDataToIot.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { sendCommunityToIot } from '@/api/community/communityManageApi'

export default {
  name: 'CommunityDataToIot',
  data() {
    return {
      visible: false,
      communityDataToIotInfo: {
        communityId: '',
        communityName: ''
      }
    }
  },

  methods: {
    openModal(params) {
      Object.assign(this.communityDataToIotInfo, params)
      this.visible = true
    },
    _toSendCommunityDataToIot() {
      sendCommunityToIot(this.communityDataToIotInfo).then(res => {
        console.log(res)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
      }).catch(error => {
        this.$message.error(error.message)
      })
    },
    closeCommunityDataToIotModel() {
      this.visible = false
    }
  }
}
</script>