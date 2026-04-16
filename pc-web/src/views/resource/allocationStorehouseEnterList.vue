<template>
  <div class="allocation-storehouse-enter-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationStorehouseEnter.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
            <el-table :data="allocationStorehouseEnterInfo.resourceStores" border style="width: 100%"
              v-loading="loading"  @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="55" align="center">
              </el-table-column>
              <el-table-column prop="parentRstName" :label="$t('allocationStorehouseEnter.resourceType')"
                align="center">
                <template slot-scope="scope">
                  {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
                </template>
              </el-table-column>
              <el-table-column prop="resName" :label="$t('allocationStorehouseEnter.resourceName')"
                align="center"></el-table-column>
              <el-table-column prop="specName" :label="$t('allocationStorehouseEnter.specification')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.specName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="resCode" :label="$t('allocationStorehouseEnter.resourceCode')"
                align="center"></el-table-column>
              <el-table-column prop="isFixedName" :label="$t('allocationStorehouseEnter.fixedResource')"
                align="center"></el-table-column>
              <el-table-column
                :label="allocationStorehouseEnterInfo.applyType == '10000' || allocationStorehouseEnterInfo.applyType == '30000' ? $t('allocationStorehouseEnter.transferredWarehouse') : $t('allocationStorehouseEnter.returnPerson')"
                align="center">
                <template slot-scope="scope">
                  {{ allocationStorehouseEnterInfo.applyType == '10000' || allocationStorehouseEnterInfo.applyType ==
                    '30000' ? scope.row.shaName : allocationStorehouseEnterInfo.startUserName }}
                </template>
              </el-table-column>
              <el-table-column prop="shaName" :label="$t('allocationStorehouseEnter.originalWarehouse')"
                align="center"></el-table-column>
              <el-table-column prop="shzName" :label="$t('allocationStorehouseEnter.targetWarehouse')"
                align="center"></el-table-column>
              <el-table-column
                :label="$t('allocationStorehouseEnter.originalStock') + '(' + (allocationStorehouseEnterInfo.applyType == '30000' ? $t('allocationStorehouseEnter.targetWarehouse') : $t('allocationStorehouseEnter.transferredWarehouse')) + ')'"
                align="center">
                <template slot-scope="scope">
                  {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
                </template>
              </el-table-column>
              <el-table-column prop="stock" :label="$t('allocationStorehouseEnter.transferQuantity')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.stock }}{{ scope.row.applyType == 20000 ? scope.row.miniUnitCodeName :
                    scope.row.unitCodeName }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('allocationStorehouseEnter.actualQuantity')" align="center">
                <template slot-scope="scope">
                  <el-input v-model.number="scope.row.quantity" type="number"
                    :placeholder="$t('allocationStorehouseEnter.quantityPlaceholder')" style="width: 150px"></el-input>
                </template>
              </el-table-column>
            </el-table>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="24" style="text-align: right">
          <el-button type="primary" @click="handleSubmit">
            <i class="el-icon-check"></i>
            {{ $t('allocationStorehouseEnter.submit') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { listAllocationStorehouses, allocationStoreEnter } from '@/api/resource/allocationStorehouseEnterApi'

export default {
  name: 'AllocationStorehouseEnterList',
  data() {
    return {
      loading: false,
      allocationStorehouseEnterInfo: {
        resourceStores: [],
        applyId: '',
        taskId: '',
        selectResIds: []
      }
    }
  },
  created() {
    this.allocationStorehouseEnterInfo.applyId = this.$route.query.applyId
    this.allocationStorehouseEnterInfo.taskId = this.$route.query.taskId
    this.listAllocationStorehouseApply()
  },
  methods: {
    async listAllocationStorehouseApply() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 100,
          applyId: this.allocationStorehouseEnterInfo.applyId
        }
        const { data } = await listAllocationStorehouses(params)
        this.allocationStorehouseEnterInfo.resourceStores = data
      } catch (error) {
        this.$message.error(this.$t('allocationStorehouseEnter.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSelectionChange(val) {
      this.allocationStorehouseEnterInfo.selectResIds = val.map(item => item.resId)
    },
    async handleSubmit() {
      // Validation
      const selectedItems = this.allocationStorehouseEnterInfo.resourceStores.filter(item =>
        this.allocationStorehouseEnterInfo.selectResIds.includes(item.resId)
      )

      if (selectedItems.length === 0) {
        this.$message.warning(this.$t('allocationStorehouseEnter.selectItemsWarning'))
        return
      }

      let hasError = false
      selectedItems.forEach(item => {
        if (!item.quantity || parseFloat(item.quantity) <= 0) {
          hasError = true
          return
        }
        item.quantity = parseFloat(item.quantity)
      })

      if (hasError) {
        this.$message.warning(this.$t('allocationStorehouseEnter.quantityWarning'))
        return
      }

      this.allocationStorehouseEnterInfo.resourceStores = selectedItems

      try {
        await allocationStoreEnter(this.allocationStorehouseEnterInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.go(-1)
      } catch (error) {
        this.$message.error(error.message || this.$t('allocationStorehouseEnter.submitError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-storehouse-enter-container {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;
  }

  .clearfix {

    &:before,
    &:after {
      display: table;
      content: "";
    }

    &:after {
      clear: both;
    }
  }
}
</style>