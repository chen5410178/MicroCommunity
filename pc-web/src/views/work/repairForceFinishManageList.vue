<template>
  <div class="repair-force-finish-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('repairForceFinishManage.searchCondition') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="searchForm.repairType" :placeholder="$t('repairForceFinishManage.repairTypePlaceholder')"
            clearable style="width:100%">
            <el-option v-for="item in repairSettings" :key="item.repairType" :label="item.repairTypeName"
              :value="item.repairType" />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-input v-model="searchForm.repairName" :placeholder="$t('repairForceFinishManage.repairPersonPlaceholder')"
            clearable />
        </el-col>

        <el-col :span="6">
          <el-input v-model="searchForm.tel" :placeholder="$t('repairForceFinishManage.repairPhonePlaceholder')"
            clearable />
        </el-col>

        <el-col :span="6">
          <el-button type="primary" @click="queryRepairPool">
            <i class="el-icon-search"></i>
            {{ $t('repairForceFinishManage.search') }}
          </el-button>
          <el-button @click="resetRepairPool" style="margin-left:10px">
            <i class="el-icon-refresh"></i>
            {{ $t('repairForceFinishManage.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 报修列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ searchForm.roomName }} {{ $t('repairForceFinishManage.forceFinish') }}</span>
      </div>

      <el-table :data="repairPools" border v-loading="loading">
        <el-table-column prop="repairId" :label="$t('repairForceFinishManage.repairId')" align="center" />
        <el-table-column prop="repairObjName" :label="$t('repairForceFinishManage.location')" align="center" />
        <el-table-column prop="repairTypeName" :label="$t('repairForceFinishManage.repairType')" align="center" />
        <el-table-column prop="repairName" :label="$t('repairForceFinishManage.repairPerson')" align="center" />
        <el-table-column prop="tel" :label="$t('repairForceFinishManage.contact')" align="center" />
        <el-table-column prop="appointmentTime" :label="$t('repairForceFinishManage.appointmentTime')" align="center" />
        <el-table-column prop="createTime" :label="$t('repairForceFinishManage.submitTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('repairForceFinishManage.status')" align="center" />
        <el-table-column :label="$t('repairForceFinishManage.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openForceFinishRepair(scope.row)">
              {{ $t('repairForceFinishManage.forceFinish') }}
            </el-button>
            <el-button size="mini" @click="openRepairDetail(scope.row)">
              {{ $t('repairForceFinishManage.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="footer-row">
        <el-col :span="18">
          <div class="description">
            {{ $t('repairForceFinishManage.description') }}
          </div>
        </el-col>
        <el-col :span="6">
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 强制回单弹窗 -->
    <force-finish-repair ref="forceFinishDialog" :visible.sync="forceFinishVisible" @success="handleForceFinishSuccess" />
  </div>
</template>

<script>
import {
  listRepairSettings,
  listOwnerRepairs,
  getDict
} from '@/api/work/repairForceFinishManageApi'
import ForceFinishRepair from '@/components/work/forceFinishRepair'

export default {
  name: 'RepairForceFinishManageList',
  components: {
    ForceFinishRepair
  },
  data() {
    return {
      searchForm: {
        repairType: '',
        repairName: '',
        tel: '',
        roomId: '',
        roomName: '',
        state: '1100'
      },
      repairPools: [],
      repairSettings: [],
      states: [],
      loading: false,
      forceFinishVisible: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.loadRepairSettings()
    this.loadDictData()
    this.listRepairPools()
  },
  methods: {
    async loadRepairSettings() {
      try {
        this.repairSettings = await listRepairSettings()
      } catch (error) {
        console.error('获取报修设置失败:', error)
        this.$message.error('获取报修设置失败')
      }
    },
    async loadDictData() {
      try {
        this.states = await getDict('r_repair_pool', 'state')
      } catch (error) {
        console.error('获取状态字典失败:', error)
        this.$message.error('获取状态字典失败')
      }
    },
    async listRepairPools() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }

        // 清理空值
        Object.keys(params).forEach(key => {
          if (params[key] === '') delete params[key]
        })

        const { data, total } = await listOwnerRepairs(params)
        this.repairPools = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取报修列表失败:', error)
        this.$message.error('获取报修列表失败')
      } finally {
        this.loading = false
      }
    },
    queryRepairPool() {
      this.pagination.current = 1
      this.listRepairPools()
    },
    resetRepairPool() {
      this.searchForm = {
        repairType: '',
        repairName: '',
        tel: '',
        state: '1100'
      }
      this.queryRepairPool()
    },
    openForceFinishRepair(repair) {
      this.forceFinishVisible = true
      this.$nextTick(() => {
        this.$refs.forceFinishDialog.open(repair)
      })
    },
    openRepairDetail(repair) {
      this.$router.push(`/views/work/repairDetail?repairId=${repair.repairId}`)
    },
    handleForceFinishSuccess() {
      this.listRepairPools()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.listRepairPools()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.listRepairPools()
    }
  }
}
</script>

<style lang="scss" scoped>
.repair-force-finish-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .footer-row {
    margin-top: 20px;
    display: flex;
    align-items: center;

    .description {
      font-size: 14px;
      color: #999;
    }
  }

  .el-pagination {
    display: flex;
    justify-content: flex-end;
  }
}
</style>