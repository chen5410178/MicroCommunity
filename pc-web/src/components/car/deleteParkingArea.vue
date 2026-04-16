<template>
  <el-dialog :title="$t('parkingAreaManage.deleteTitle')" :visible.sync="visible" width="30%" center>
    <div style="text-align: center">
      <p>{{ $t('parkingAreaManage.deleteConfirm') }}</p>
      <p style="font-weight: bold; color: #e6a23c; margin-top: 10px">
        {{ deleteParkingAreaInfo.num }} ({{ parkingTypeLabel }})
      </p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="deleteParkingArea" :loading="deleting">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteParkingArea } from '@/api/car/parkingAreaManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteParkingArea',
  data() {
    return {
      visible: false,
      deleting: false,
      deleteParkingAreaInfo: {}
    }
  },
  computed: {
    parkingTypeLabel() {
      return this.deleteParkingAreaInfo.typeCd === '1001'
        ? this.$t('parkingAreaManage.aboveGround')
        : this.$t('parkingAreaManage.underground')
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.deleteParkingAreaInfo = { ...row }
    },

    async deleteParkingArea() {
      try {
        this.deleting = true
        const params = {
          paId: this.deleteParkingAreaInfo.paId,
          communityId: getCommunityId()
        }

        await deleteParkingArea(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除停车场失败:', error)
        this.$message.error(this.$t('parkingAreaManage.deleteFailed'))
      } finally {
        this.deleting = false
      }
    }
  }
}
</script>