<template>
  <div class="equipment-account-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="type-card">
          <div class="type-list-item"
            :class="{ 'selected': equipmentAccountInfo.conditions.typeId == '' }"
            @click="handleSwitchType({ typeId: '', typeName: '' })">
            {{ $t('common.all') }}
          </div>
          <machine-type-tree ref="machineTypeTree" @switchType="handleSwitchType"></machine-type-tree>
        </el-card>
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-card class="box-card text-left">
          <div slot="header" class="">
            <span>{{ $t('equipmentAccount.searchCondition') }}</span>
          </div>
          <el-form :inline="true" :model="equipmentAccountInfo.conditions" class="search-form">
            <el-form-item >
              <el-input v-model.trim="equipmentAccountInfo.conditions.machineName"
                :placeholder="$t('equipmentAccount.machineNamePlaceholder')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-input v-model.trim="equipmentAccountInfo.conditions.machineCode"
                :placeholder="$t('equipmentAccount.machineCodePlaceholder')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-select v-model="equipmentAccountInfo.conditions.state" style="width:100%">
                <el-option :label="$t('equipmentAccount.selectState')" value=""></el-option>
                <el-option v-for="item in equipmentAccountInfo.useStatus" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item >
              <el-select v-model="equipmentAccountInfo.conditions.importanceLevel" style="width:100%">
                <el-option :label="$t('equipmentAccount.selectImportanceLevel')" value=""></el-option>
                <el-option v-for="item in equipmentAccountInfo.importanceLevels" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="queryEquipmentAccountMethod">{{ $t('common.search') }}</el-button>
              <el-button @click="resetEquipmentAccountMethod">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>

        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('equipmentAccount.listTitle') }}</span>
            <div>
              <el-button type="primary" size="small" @click="openAddEquipmentAccountModal">
                <i class="el-icon-plus"></i>{{ $t('equipmentAccount.addEquipment') }}
              </el-button>
              <el-button type="primary" size="small" @click="printEquipmentAccounts">
                <i class="el-icon-printer"></i>{{ $t('equipmentAccount.batchPrint') }}
              </el-button>
            </div>
          </div>
          <el-table :data="equipmentAccountInfo.equipmentAccounts" border style="width: 100%"   @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"></el-table-column>
            <el-table-column prop="machineName" :label="$t('equipmentAccount.machineName')"
              align="center"></el-table-column>
            <el-table-column prop="machineCode" :label="$t('equipmentAccount.machineCode')"
              align="center"></el-table-column>
            <el-table-column prop="brand" :label="$t('equipmentAccount.brand')" align="center"></el-table-column>
            <el-table-column prop="model" :label="$t('equipmentAccount.model')" align="center"></el-table-column>
            <el-table-column prop="locationDetail" :label="$t('equipmentAccount.locationDetail')"
              align="center"></el-table-column>
            <el-table-column prop="firstEnableTime" :label="$t('equipmentAccount.firstEnableTime')"
              align="center"></el-table-column>
            <el-table-column prop="warrantyDeadline" :label="$t('equipmentAccount.warrantyDeadline')"
              align="center"></el-table-column>
            <el-table-column prop="usefulLife" :label="$t('equipmentAccount.usefulLife')"
              align="center"></el-table-column>
            <el-table-column prop="levelName" :label="$t('equipmentAccount.importanceLevel')"
              align="center"></el-table-column>
            <el-table-column prop="stateName" :label="$t('equipmentAccount.state')" align="center"></el-table-column>
            <el-table-column prop="purchasePrice" :label="$t('equipmentAccount.purchasePrice')"
              align="center"></el-table-column>
            <el-table-column prop="netWorth" :label="$t('equipmentAccount.netWorth')" align="center"></el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="300">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" @click="printEquipmentAccount(scope.row)">{{ $t('common.print') }}</el-button>
                  <el-button size="mini" @click="openEquipmentAccountDetail(scope.row)">{{ $t('common.detail')
                  }}</el-button>
                  <el-button size="mini" @click="openEditEquipmentAccountModel(scope.row)">{{ $t('common.edit')
                  }}</el-button>
                  <el-button size="mini" type="danger" @click="openDeleteEquipmentAccountModel(scope.row)">{{
                    $t('common.delete') }}</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-row class="pagination-wrapper">
            <el-col :span="12" class="table-desc">
              <div>{{ $t('equipmentAccount.stateDescription') }}</div>
            </el-col>
            <el-col :span="12">
              <el-pagination :current-page.sync="equipmentAccountInfo.currentPage" :page-sizes="[10, 20, 30, 50]"
                :page-size="10" layout="total, sizes, prev, pager, next, jumper" :total="equipmentAccountInfo.total"
                @size-change="handleSizeChange" @current-change="handleCurrentChange">
              </el-pagination>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <delete-equipment-account ref="deleteEquipmentAccount" @success="listEquipmentAccounts"></delete-equipment-account>
    <view-equipment-account ref="viewEquipmentAccount"></view-equipment-account>
    <print-equipment-account ref="printEquipmentAccount"></print-equipment-account>
    <import-equipment ref="importEquipment"></import-equipment>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listEquipmentAccount } from '@/api/machine/equipmentAccountApi'
import MachineTypeTree from '@/components/machine/machineTypeTree'
import DeleteEquipmentAccount from '@/components/machine/deleteEquipmentAccount'
import ViewEquipmentAccount from '@/components/machine/viewEquipmentAccount'
import PrintEquipmentAccount from '@/components/machine/printEquipmentAccount'
import ImportEquipment from '@/components/machine/importEquipment'

export default {
  name: 'EquipmentAccountList',
  components: {
    MachineTypeTree,
    DeleteEquipmentAccount,
    ViewEquipmentAccount,
    PrintEquipmentAccount,
    ImportEquipment
  },
  data() {
    return {
      communityId: '',
      equipmentAccountInfo: {
        currentPage: 1,
        equipmentAccounts: [],
        total: 0,
        moreCondition: false,
        machineIds: [],
        conditions: {
          machineName: '',
          machineCode: '',
          state: '',
          importanceLevel: '',
          typeId: ''
        },
        useStatus: [],
        importanceLevels: []
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getDictData()
    this.listEquipmentAccounts()
  },
  methods: {
    async getDictData() {
      try {
        const stateData = await getDict('equipment_account', 'state')
        const levelData = await getDict('equipment_account', 'importance_level')
        this.equipmentAccountInfo.useStatus = stateData
        this.equipmentAccountInfo.importanceLevels = levelData
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSelectionChange(selectedRows) {
    this.equipmentAccountInfo.machineIds = selectedRows.map(row => row.machineId)
  },
    async listEquipmentAccounts() {
      try {
        const params = {
          page: this.equipmentAccountInfo.currentPage,
          row: 10,
          communityId: this.communityId,
          ...this.equipmentAccountInfo.conditions
        }
        const { data, total } = await listEquipmentAccount(params)
        this.equipmentAccountInfo.equipmentAccounts = data
        this.equipmentAccountInfo.total = total
      } catch (error) {
        console.error('获取设备台账列表失败:', error)
      }
    },
    handleSwitchType(type) {
      this.equipmentAccountInfo.conditions.typeId = type.typeId
      this.listEquipmentAccounts()
    },
    openAddEquipmentAccountModal() {
      if (!this.equipmentAccountInfo.conditions.typeId) {
        this.$message.warning(this.$t('equipmentAccount.selectTypeFirst'))
        return
      }
      this.$router.push(`/views/machine/addEquipmentAccount?typeId=${this.equipmentAccountInfo.conditions.typeId}`)
    },
    openEditEquipmentAccountModel(row) {
      this.$router.push(`/views/machine/editEquipmentAccount?machineId=${row.machineId}`)
    },
    openEquipmentAccountDetail(row) {
      this.$router.push(`/views/machine/equipmentAccountDetail?machineId=${row.machineId}`)
    },
    openDeleteEquipmentAccountModel(row) {
      this.$refs.deleteEquipmentAccount.open(row)
    },
    printEquipmentAccount(row) {
      //this.$refs.printEquipmentAccount.open(row)
      window.open(`/#/views/resource/printEquipmentAccountLabel?machineId=${row.machineId}`)

    },
    printEquipmentAccounts() {
      if (this.equipmentAccountInfo.machineIds.length === 0) {
        this.$message.warning(this.$t('equipmentAccount.selectEquipmentFirst'))
        return
      }
      window.open(`/#/views/resource/printEquipmentAccountLabel?machineIds=${this.equipmentAccountInfo.machineIds}`)
    },
    queryEquipmentAccountMethod() {
      this.equipmentAccountInfo.currentPage = 1
      this.listEquipmentAccounts()
    },
    resetEquipmentAccountMethod() {
      this.equipmentAccountInfo.conditions = {
        machineName: '',
        machineCode: '',
        state: '',
        importanceLevel: '',
        typeId: this.equipmentAccountInfo.conditions.typeId
      }
      this.listEquipmentAccounts()
    },
    toggleMoreCondition() {
      this.equipmentAccountInfo.moreCondition = !this.equipmentAccountInfo.moreCondition
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.equipmentAccountInfo.currentPage = 1
      this.listEquipmentAccounts()
    },
    handleCurrentChange(val) {
      this.equipmentAccountInfo.currentPage = val
      this.listEquipmentAccounts()
    }
  }
}
</script>

<style lang="scss" scoped>
.equipment-account-container {
  padding: 20px;

  .left-panel {
    .type-card {
      .type-list-item {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;
        transition: all 0.3s;
        font-size: 14px;
        color: #606266;

        &:hover {
          background-color: #f5f7fa;
        }

        &.selected {
          background-color: #409eff;
          color: white;
        }
      }

      ::v-deep .tree-container {
        border: none;
        box-shadow: none;
        padding: 0;

        .el-card__body {
          padding: 0;
        }
      }
    }
  }

  .right-panel {
    padding-left: 5px;
  }

  .search-form {
    margin-bottom: 0px;
  }

  .pagination-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.margin-top {
  margin-top: 20px;
}
</style>