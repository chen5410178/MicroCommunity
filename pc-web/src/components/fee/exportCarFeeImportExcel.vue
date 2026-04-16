<template>
  <el-dialog :title="$t('exportCarFeeImportExcel.templateExport')" :visible.sync="visible" width="50%">
    <el-form label-width="150px" class="text-left">
      <el-form-item :label="$t('exportCarFeeImportExcel.parkingLot')">
        <div>
          <el-checkbox v-model="isParkingAreaAll" @change="changeAllParkingAreas">
            {{ $t('exportCarFeeImportExcel.all') }}
          </el-checkbox>
          <el-checkbox-group v-model="selectedParkingAreas" @change="changeItemParkingArea">
            <el-checkbox v-for="item in parkingAreas" :key="item.paId" :label="item.paId" style="margin-left:15px">
              {{ item.num }}{{ $t('exportCarFeeImportExcel.parkingLotPlaceholder') }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </el-form-item>

      <el-form-item :label="$t('exportCarFeeImportExcel.feeItem')">
        <div>
          <el-checkbox v-model="isConfigAll" @change="changeAllConfigs">
            {{ $t('exportCarFeeImportExcel.all') }}
          </el-checkbox>
          <el-checkbox-group v-model="selectedConfigs" @change="changeItemConfig">
            <template v-for="item in configs">
              <el-checkbox :key="item.configId" :label="item.configId" style="margin-left:15px"
                v-if="item.feeTypeCd !== '888800010001' && item.feeTypeCd !== '888800010009' && item.feeTypeCd !== '888800010011'">
                {{ item.feeName }}
              </el-checkbox>
            </template>
          </el-checkbox-group>
        </div>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('exportCarFeeImportExcel.cancel') }}</el-button>
      <el-button type="primary" @click="handleExport">{{ $t('exportCarFeeImportExcel.export') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listParkingAreas, listFeeConfigs, exportCarFeeExcel } from '@/api/fee/carCreateFeeApi'

export default {
  name: 'ExportCarFeeImportExcel',
  data() {
    return {
      visible: false,
      isParkingAreaAll: true,
      isConfigAll: true,
      selectedParkingAreas: [],
      selectedConfigs: [],
      parkingAreas: [],
      configs: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        // 获取停车场
        const parkingRes = await listParkingAreas({
          page: 1,
          row: 150
        })
        this.parkingAreas = parkingRes.parkingAreas || []
        this.selectedParkingAreas = this.parkingAreas.map(item => item.paId)

        // 获取费用配置
        const configRes = await listFeeConfigs({
          page: 1,
          row: 100,
          isDefault: 'F'
        })
        this.configs = configRes.feeConfigs || []
        this.selectedConfigs = this.configs
          .filter(item => item.feeTypeCd !== '888800010001' && item.feeTypeCd !== '888800010009' && item.feeTypeCd !== '888800010011')
          .map(item => item.configId)

      } catch (error) {
        console.error('获取数据失败:', error)
      }
    },
    open() {
      this.visible = true
    },
    changeAllParkingAreas() {
      if (this.isParkingAreaAll) {
        this.selectedParkingAreas = this.parkingAreas.map(item => item.paId)
      } else {
        this.selectedParkingAreas = []
      }
    },
    changeItemParkingArea() {
      this.isParkingAreaAll = this.selectedParkingAreas.length === this.parkingAreas.length
    },
    changeAllConfigs() {
      if (this.isConfigAll) {
        this.selectedConfigs = this.configs
          .filter(item => item.feeTypeCd !== '888800010001' && item.feeTypeCd !== '888800010009' && item.feeTypeCd !== '888800010011')
          .map(item => item.configId)
      } else {
        this.selectedConfigs = []
      }
    },
    changeItemConfig() {
      this.isConfigAll = this.selectedConfigs.length === this.configs
        .filter(item => item.feeTypeCd !== '888800010001' && item.feeTypeCd !== '888800010009' && item.feeTypeCd !== '888800010011')
        .length
    },
    async handleExport() {
      try {
        const paIds = this.selectedParkingAreas.join(',')
        const configIds = this.selectedConfigs.join(',')

        await exportCarFeeExcel({
          paIds,
          configIds
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('common.exportError'))
      }
    }
  }
}
</script>