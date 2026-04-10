<template>
  <div class="admin-equipment-container">
    <el-row :gutter="10">
      <el-col :span="4" class="pr-0">
        <community-machine-type-tree ref="machineTypeTree" @selectMachineType="handleSelectMachineType" />
      </el-col>
      <el-col :span="20" >
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminEquipment.search.title') }}</span>
          </div>
          <div class="search-content">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-input v-model.trim="searchForm.machineName" :placeholder="$t('adminEquipment.search.machineName')"
                  clearable />
              </el-col>
              <el-col :span="4">
                <el-input v-model.trim="searchForm.machineCode" :placeholder="$t('adminEquipment.search.machineCode')"
                  clearable />
              </el-col>
              <el-col :span="4">
                <el-select v-model="searchForm.state" :placeholder="$t('adminEquipment.search.state')" clearable>
                  <el-option v-for="item in useStatus" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-select v-model="searchForm.importanceLevel" :placeholder="$t('adminEquipment.search.importanceLevel')"
                  clearable>
                  <el-option v-for="item in importanceLevels" :key="item.statusCd" :label="item.name"
                    :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col :span="4" >
                <el-button type="primary" @click="handleSearch">
                  {{ $t('common.search') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card mt-10">
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="machineName" :label="$t('adminEquipment.table.machineName')" align="center" />
            <el-table-column prop="machineCode" :label="$t('adminEquipment.table.machineCode')" align="center" />
            <el-table-column prop="brand" :label="$t('adminEquipment.table.brand')" align="center" />
            <el-table-column prop="model" :label="$t('adminEquipment.table.model')" align="center" />
            <el-table-column prop="locationDetail" :label="$t('adminEquipment.table.locationDetail')" align="center" />
            <el-table-column prop="firstEnableTime" :label="$t('adminEquipment.table.firstEnableTime')" align="center" />
            <el-table-column prop="warrantyDeadline" :label="$t('adminEquipment.table.warrantyDeadline')"
              align="center" />
            <el-table-column prop="usefulLife" :label="$t('adminEquipment.table.usefulLife')" align="center" />
            <el-table-column prop="levelName" :label="$t('adminEquipment.table.levelName')" align="center" />
            <el-table-column prop="stateName" :label="$t('adminEquipment.table.stateName')" align="center" />
            <el-table-column prop="purchasePrice" :label="$t('adminEquipment.table.purchasePrice')" align="center" />
            <el-table-column prop="netWorth" :label="$t('adminEquipment.table.netWorth')" align="center" />
          </el-table>

          <el-row class="mt-10">
            <el-col :span="12" class="text-left">
              <div>{{ $t('adminEquipment.statusTip') }}</div>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminEquipmentAccount } from '@/api/resource/adminEquipmentApi'
import CommunityMachineTypeTree from '@/components/resource/CommunityMachineTypeTree'
import {getDict} from '@/api/community/communityApi'

export default {
  name: 'AdminEquipmentList',
  components: {
    CommunityMachineTypeTree
  },
  data() {
    return {
      loading: false,
      searchForm: {
        machineName: '',
        machineCode: '',
        communityId: '',
        state: '',
        importanceLevel: '',
        typeId: ''
      },
      tableData: [],
      useStatus: [],
      importanceLevels: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
    this.getList()
    this.$refs.machineTypeTree.ini
  },
  methods: {
    async getDictData() {
      try {
        const stateData = await getDict('equipment_account','state')
        this.useStatus = stateData

        const levelData = await getDict('equipment_account','importance_level')
        this.importanceLevels = levelData
      } catch (error) {
        console.error('Failed to load dictionary data:', error)
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const { data, total } = await listAdminEquipmentAccount(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminEquipment.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSelectMachineType(data) {
      this.searchForm.typeId = data.typeId
      this.searchForm.communityId = data.communityId
      this.handleSearch()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-equipment-container {
  padding: 20px;

  .pr-0 {
    padding-right: 0;
  }

  .mt-10 {
    margin-top: 10px;
  }

  .text-right {
    text-align: right;
  }

  .search-content {
    padding: 20px 0;
  }
}
</style>