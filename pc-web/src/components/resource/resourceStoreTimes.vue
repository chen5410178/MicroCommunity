<template>
  <el-dialog
    id="resourceStoreTimesModel"
    :title="$t('resourceStoreTimes.title')"
    :visible.sync="visible"
    width="80%"
    :before-close="handleClose"
  >
    <el-table :data="resourceStoreTimesInfo.timeses" border style="width: 100%">
      <el-table-column prop="resCode" :label="$t('resourceStoreTimes.itemCode')" align="center" />
      <el-table-column prop="createTime" :label="$t('resourceStoreTimes.inboundTime')" align="center" />
      <el-table-column prop="price" :label="$t('resourceStoreTimes.unitPrice')" align="center" />
      <el-table-column prop="stock" :label="$t('resourceStoreTimes.stock')" align="center" />
      <el-table-column prop="totalPrice" :label="$t('resourceStoreTimes.totalPrice')" align="center" />
    </el-table>
    <!-- <div style="text-align: right; margin-top: 20px;">
      <span>{{ $t('resourceStoreTimes.totalPrice') }}: {{ resourceStoreTimesInfo.totalPrice }}</span>
    </div> -->
  </el-dialog>
</template>

<script>
import { listResourceStoreTimes } from '@/api/resource/resourceStoreManageApi'

export default {
  name: 'ResourceStoreTimes',
  data() {
    return {
      visible: false,
      resourceStoreTimesInfo: {
        timeses: [],
        resCode: '',
        totalPrice: 0.0
      }
    }
  },
  methods: {
    open(resourceStore) {
      this.visible = true
      this._loadAllResourceStoreTimes(1, 10, resourceStore.resCode, resourceStore.shId)
    },
    handleClose() {
      this.visible = false
      this.resourceStoreTimesInfo = {
        timeses: [],
        resCode: '',
        totalPrice: 0.0
      }
    },
    async _loadAllResourceStoreTimes(page, rows, resCode, shId) {
      try {
 
        const { data, totalPrice } = await listResourceStoreTimes({
            page: page,
            row: rows,
            resCode: resCode,
            shId: shId
          })
        this.resourceStoreTimesInfo.timeses = data
        this.resourceStoreTimesInfo.totalPrice = totalPrice
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    }
  }
}
</script>

<style scoped>
</style>