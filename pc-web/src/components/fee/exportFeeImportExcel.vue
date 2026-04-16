<template>
  <el-dialog :title="$t('exportFeeImportExcel.title')" :visible.sync="visible" width="70%" :before-close="handleClose">
    <el-form label-width="120px" class="text-left">
      <el-form-item :label="$t('exportFeeImportExcel.floor')">
        <div>
          <el-checkbox v-model="isFloorAll" @change="toggleAllFloors">{{ $t('exportFeeImportExcel.all') }}</el-checkbox>
          <el-checkbox v-for="item in floors" :key="item.floorId" v-model="selectedFloorIds" :label="item.floorId"
            @change="handleFloorChange" class="margin-left">{{ item.floorName }}</el-checkbox>
        </div>
      </el-form-item>

      <el-form-item :label="$t('exportFeeImportExcel.feeItem')">
        <div>
          <el-checkbox v-model="isConfigAll" @change="toggleAllConfigs">{{ $t('exportFeeImportExcel.all')
            }}</el-checkbox>
          <el-checkbox v-for="item in configs" :key="item.configId" v-model="selectedConfigIds" :label="item.configId"
            @change="handleConfigChange" class="margin-left">{{ item.feeName }}</el-checkbox>
        </div>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleExport">{{ $t('common.export') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { exportData, listFeeConfigs, queryFloors } from '@/api/fee/exportFeeImportExcelApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ExportFeeImportExcel',
  data() {
    return {
      visible: false,
      isFloorAll: true,
      isConfigAll: true,
      selectedFloorIds: [],
      selectedConfigIds: [],
      floors: [],
      configs: []
    }
  },
  computed: {
    communityId() {
      return getCommunityId()
    }
  },
  methods: {
    open() {
      this.visible = true
      this.loadFloors()
      this.loadConfigs()
    },

    loadFloors() {
      queryFloors({
        page: 1,
        row: 150,
        communityId: this.communityId
      }).then(response => {
        this.floors = response.apiFloorDataVoList
        this.selectedFloorIds = this.floors.map(item => item.floorId)
      })
    },

    loadConfigs() {
      listFeeConfigs({
        page: 1,
        row: 100,
        communityId: this.communityId,
        isDefault: 'F'
      }).then(response => {
        this.configs = response.feeConfigs
        this.selectedConfigIds = this.configs.map(item => item.configId)
      })
    },

    toggleAllFloors() {
      this.selectedFloorIds = this.isFloorAll
        ? this.floors.map(item => item.floorId)
        : []
    },

    toggleAllConfigs() {
      this.selectedConfigIds = this.isConfigAll
        ? this.configs.map(item => item.configId)
        : []
    },

    handleFloorChange() {
      this.isFloorAll = this.selectedFloorIds.length === this.floors.length
    },

    handleConfigChange() {
      this.isConfigAll = this.selectedConfigIds.length === this.configs.length
    },

    handleExport() {
      const params = {
        floorIds: this.selectedFloorIds.join(','),
        configIds: this.selectedConfigIds.join(','),
        communityId: this.communityId,
        type: '1001',
        pagePath: 'exportCreateFeeTemplate'
      }

      exportData(params).then(response => {
        this.$message.success(response.msg)
        if (response.code === 0) {
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
      this.isFloorAll = true
      this.isConfigAll = true
      this.selectedFloorIds = []
      this.selectedConfigIds = []
    }
  }
}
</script>

<style scoped>
.margin-left {
  margin-left: 15px;
}
</style>