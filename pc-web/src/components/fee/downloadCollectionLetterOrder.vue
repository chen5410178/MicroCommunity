<template>
  <el-dialog
    :title="$t('downloadCollectionLetterOrder.title')"
    :visible.sync="visible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form label-width="120px">
      <template v-if="roomId">
        <el-alert
          :title="$t('downloadCollectionLetterOrder.confirmExport')"
          type="info"
          show-icon
        ></el-alert>
      </template>
      <template v-else>
        <el-form-item :label="$t('downloadCollectionLetterOrder.selectFloor')">
          <el-select
            v-model="floorId"
            :placeholder="$t('downloadCollectionLetterOrder.floorPlaceholder')"
            style="width: 100%"
          >
            <el-option
              v-for="item in floors"
              :key="item.floorId"
              :label="item.floorNum"
              :value="item.floorId"
            ></el-option>
          </el-select>
        </el-form-item>
      </template>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button 
        type="primary" 
        @click="handleExport"
        :disabled="!floorId && !roomId"
      >{{ $t('common.export') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { exportData, queryFloors } from '@/api/fee/downloadCollectionLetterOrderApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DownloadCollectionLetterOrder',
  data() {
    return {
      visible: false,
      roomId: '',
      floorId: '',
      floors: []
    }
  },
  computed: {
    communityId() {
      return getCommunityId()
    }
  },
  methods: {
    open(params) {
      this.visible = true
      this.roomId = params.roomId || ''
      this.loadFloors()
    },
    
    loadFloors() {
      queryFloors({
        page: 1,
        row: 100,
        communityId: this.communityId
      }).then(response => {
        this.floors = response.data.apiFloorDataVoList
      })
    },
    
    handleExport() {
      const params = {
        communityId: this.communityId,
        pagePath: 'dataFeeManualCollection',
        floorId: this.floorId,
        roomId: this.roomId
      }
      
      exportData(params).then(response => {
        const res = response.data
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.handleClose()
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      }).catch(error => {
        this.$message.error(error.message)
      })
    },
    
    handleClose() {
      this.visible = false
      this.reset()
    },
    
    reset() {
      this.roomId = ''
      this.floorId = ''
    }
  }
}
</script>