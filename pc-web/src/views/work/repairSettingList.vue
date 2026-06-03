<template>
  <div class="repair-setting-container">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li class="parking-area-item" :class="{ 'selected': selectedPublicArea === '' }"
                @click="switchPublicArea('')">
                {{ $t('repairSetting.all') }}
              </li>
              <li class="parking-area-item" :class="{ 'selected': selectedPublicArea === 'T' }"
                @click="switchPublicArea('T')">
                {{ $t('repairSetting.nonHouse') }}
              </li>
              <li class="parking-area-item" :class="{ 'selected': selectedPublicArea === 'F' }"
                @click="switchPublicArea('F')">
                {{ $t('repairSetting.house') }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="21" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairSetting.queryCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="5">
              <el-input v-model="searchForm.repairTypeName" :placeholder="$t('repairSetting.repairTypeNamePlaceholder')"
                clearable />
            </el-col>
            <el-col :span="5">
              <el-select v-model="searchForm.repairWay" :placeholder="$t('repairSetting.repairWayPlaceholder')"
                style="width:100%">
                <el-option :label="$t('common.all')" value=""></el-option>
                <el-option v-for="item in repairWays" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="5">
              <el-select v-model="searchForm.repairSettingType"
                :placeholder="$t('repairSetting.repairSettingTypePlaceholder')" style="width:100%">
                <el-option :label="$t('common.all')" value=""></el-option>
                <el-option v-for="item in repairSettingTypes" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="5">
              <el-button type="primary" @click="handleSearch">{{ $t('repairSetting.search') }}</el-button>
              <el-button @click="handleReset">{{ $t('repairSetting.reset') }}</el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="list-card mt-20">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairSetting.repairSettingTitle') }}</span>
            <div style="float: right">
              <el-button type="primary" size="small" @click="openAddDialog">
                {{ $t('repairSetting.add') }}
              </el-button>
            </div>
          </div>

          <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
            <el-table-column prop="repairTypeName" :label="$t('repairSetting.typeName')" align="center" />
            <el-table-column prop="repairSettingTypeName" :label="$t('repairSetting.repairSettingType')" align="center" />
            <el-table-column prop="repairWayName" :label="$t('repairSetting.dispatchMethod')" align="center" />
            <el-table-column prop="publicArea" :label="$t('repairSetting.area')" align="center">
              <template slot-scope="scope">
                {{ scope.row.publicArea === 'T' ? $t('repairSetting.nonHouse') : $t('repairSetting.house') }}
              </template>
            </el-table-column>
            <el-table-column prop="isShow" :label="$t('repairSetting.ownerDisplay')" align="center">
              <template slot-scope="scope">
                {{ scope.row.isShow === 'Y' ? $t('repairSetting.yes') : $t('repairSetting.no') }}
              </template>
            </el-table-column>
            <el-table-column prop="notifyWay" :label="$t('repairSetting.notificationMethod')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.notifyWay === 'SMS'">{{ $t('repairSetting.sms') }}</span>
                <span v-else-if="scope.row.notifyWay === 'WORK_LICENSE'">{{ $t('repairSetting.wechatWorkLicense') }}</span>
                <span v-else>{{ $t('repairSetting.wechat') }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="returnVisitFlagName" :label="$t('repairSetting.returnVisit')" align="center" />
            <el-table-column prop="doTime" :label="$t('repairSetting.processingTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.doTime }}{{ $t('repairSetting.processingTimeUnit') }}
              </template>
            </el-table-column>
            <el-table-column prop="warningTime" :label="$t('repairSetting.timeoutWarning')" align="center">
              <template slot-scope="scope">
                {{ scope.row.warningTime }}{{ $t('repairSetting.timeoutWarningUnit') }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('repairSetting.createTime')" align="center" />
            <el-table-column :label="$t('repairSetting.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="openEditDialog(scope.row)">
                  {{ $t('repairSetting.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
                  {{ $t('repairSetting.delete') }}
                </el-button>
                <div class="margin-top-sm">
                <el-button size="mini"  @click="bindRepairMaster(scope.row)">
                  {{ $t('repairSetting.bindRepairMaster') }}
                </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <el-row class="margin-top">
            <el-col :span="12" class="text-left repair-detail-desc">
              <div><strong>{{ $t('repairSetting.dispatchExplanation') }}</strong></div>
              <div>{{ $t('repairSetting.dispatchPoint1') }}</div>
              <div>{{ $t('repairSetting.dispatchPoint2') }}</div>
              <div>{{ $t('repairSetting.dispatchPoint3') }}</div>
              <div style="margin-top:8px"><strong>{{ $t('repairSetting.areaExplanation') }}</strong></div>
              <div>{{ $t('repairSetting.areaPoint1') }}</div>
              <div>{{ $t('repairSetting.areaPoint2') }}</div>
            </el-col>
            <el-col :span="12">
              <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange" @current-change="handleCurrentChange"
                style="text-align:right" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <add-repair-setting ref="addDialog" @success="fetchData" />
    <edit-repair-setting ref="editDialog" @success="fetchData" />
    <delete-repair-setting ref="deleteDialog" @success="fetchData" />
  </div>
</template>

<script>
import { listRepairSettings } from '@/api/work/repairSettingApi'
import AddRepairSetting from '@/components/work/AddRepairSetting'
import EditRepairSetting from '@/components/work/EditRepairSetting'
import DeleteRepairSetting from '@/components/work/DeleteRepairSetting'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'RepairSettingList',
  components: {
    AddRepairSetting,
    EditRepairSetting,
    DeleteRepairSetting
  },
  data() {
    return {
      loading: false,
      selectedPublicArea: '',
      searchForm: {
        repairTypeName: '',
        repairWay: '',
        repairSettingType: '',
        returnVisitFlag: ''
      },
      tableData: [],
      repairWays: [],
      repairSettingTypes: [],
      publicAreas: [],
      returnVisitFlags: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchData()
    this.loadDictData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          publicArea: this.selectedPublicArea,
          ...this.searchForm
        }

        const { data, records } = await listRepairSettings(params)
        this.tableData = data
        this.pagination.total = records
      } catch (error) {
        console.error('获取报修设置列表失败:', error)
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },

    async loadDictData() {
      try {
        this.repairWays = await getDict('r_repair_setting', 'repair_way')
        this.repairSettingTypes = await getDict('r_repair_setting', 'repair_setting_type')
        this.publicAreas = await getDict('r_repair_setting', 'public_area')
        this.returnVisitFlags = await getDict('r_repair_setting', 'return_visit_flag')
      } catch (error) {
        console.error('加载字典数据失败:', error)
      }
    },

    switchPublicArea(val) {
      this.selectedPublicArea = val
      this.pagination.current = 1
      this.fetchData()
    },

    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    handleReset() {
      this.searchForm = {
        repairTypeName: '',
        repairWay: '',
        repairSettingType: '',
        returnVisitFlag: ''
      }
      this.handleSearch()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchData()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchData()
    },

    openAddDialog() {
      this.$refs.addDialog.open()
    },

    openEditDialog(row) {
      this.$refs.editDialog.open(row)
    },

    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },

    bindRepairMaster(row) {
      // 跳转到绑定维修师傅页面
      this.$router.push({
        path: '/views/work/repairTypeUser',
        query: {
          settingId: row.settingId,
          repairType: row.repairType,
          repairTypeName: row.repairTypeName
        }
      })
    },

    showDocumentation() {
      // 显示文档逻辑
      console.log('Show documentation')
    }
  }
}
</script>

<style lang="scss" scoped>
.repair-setting-container {
  padding: 20px;

  .right-panel {
    padding-left: 5px;
  }

  .left-panel {
    .parking-area-card {
      .parking-area-list-wrapper {
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

  .mt-20 {
    margin-top: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .repair-detail-desc {
    text-align: left;
    font-size: 13px;
    color: #606266;

    div {
      margin-bottom: 2px;
    }
  }

  .list-card {
    margin-bottom: 20px;
  }
}
</style>