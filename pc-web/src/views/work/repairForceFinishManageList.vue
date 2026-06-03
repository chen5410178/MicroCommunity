<template>
  <div class="animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li class="parking-area-item" :class="{ 'selected': selectedRepairType === '' }"
                @click="switchRepairType('')">
                {{ $t('repairForceFinishManage.all') }}
              </li>
              <li v-for="item in repairSettings" :key="item.repairType"
                class="parking-area-item"
                :class="{ 'selected': selectedRepairType === item.repairType }"
                @click="switchRepairType(item.repairType)">
                {{ item.repairTypeName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="21" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairForceFinishManage.searchCondition') }}</span>
          </div>

          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.repairName" :placeholder="$t('repairForceFinishManage.repairPersonPlaceholder')"
                clearable />
            </el-col>

            <el-col :span="4">
              <el-input v-model="searchForm.tel" :placeholder="$t('repairForceFinishManage.repairPhonePlaceholder')"
                clearable />
            </el-col>

            <el-col :span="4">
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

        <el-card class="list-card mt-20">
          <div slot="header" class="flex justify-between">
            <span>{{ searchForm.roomName }} {{ $t('repairForceFinishManage.forceFinish') }}</span>
          </div>

          <el-table :data="repairPools" border stripe v-loading="loading">
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
            <el-col :span="12" class="text-left repair-detail-desc">
              <div>{{ $t('repairForceFinishManage.description') }}</div>
            </el-col>
            <el-col :span="12">
              <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
                :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" style="text-align:right" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

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
      selectedRepairType: '',
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
    switchRepairType(repairType) {
      this.selectedRepairType = repairType
      this.searchForm.repairType = repairType
      this.pagination.current = 1
      this.listRepairPools()
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
      this.searchForm.repairName = ''
      this.searchForm.tel = ''
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
.animated {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.right-panel {
  padding-left: 5px;
}

.left-panel {
  .parking-area-card {
    height: 700px;

    .parking-area-list-wrapper {
      height: 630px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #dcdfe6;
        border-radius: 2px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .parking-area-list {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .parking-area-item {
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
      }
    }
  }
}

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

  .repair-detail-desc {
    text-align: left;
    font-size: 13px;
    color: #606266;
  }
}
</style>