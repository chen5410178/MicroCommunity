<template>
  <div class="repair-setting-container">
    <!-- 查询条件 -->
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
          <el-select v-model="searchForm.publicArea" :placeholder="$t('repairSetting.publicAreaPlaceholder')"
            style="width:100%">
            <el-option :label="$t('common.all')" value=""></el-option>
            <el-option v-for="item in publicAreas" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">{{ $t('repairSetting.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('repairSetting.reset') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 报修设置列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('repairSetting.repairSettingTitle') }}</span>
        <div style="float: right">
          <!-- <el-button type="text" @click="showDocumentation">
            {{ $t('repairSetting.documentation') }}
          </el-button> -->
          <el-button type="primary" size="small" @click="openAddDialog">
            {{ $t('repairSetting.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
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
        <el-table-column :label="$t('repairSetting.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditDialog(scope.row)">
              {{ $t('repairSetting.edit') }}
            </el-button>
            <el-button size="mini" @click="bindRepairMaster(scope.row)">
              {{ $t('repairSetting.bindRepairMaster') }}
            </el-button>
            <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
              {{ $t('repairSetting.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页和说明 -->
      <el-row :gutter="20" class="margin-top-xs text-left">
        <el-col :span="18">
          <div class="explanation-section">
            <p><strong>{{ $t('repairSetting.dispatchExplanation') }}</strong></p>
            <p>{{ $t('repairSetting.dispatchPoint1') }}</p>
            <p>{{ $t('repairSetting.dispatchPoint2') }}</p>
            <p>{{ $t('repairSetting.dispatchPoint3') }}</p>
            <p><strong>{{ $t('repairSetting.areaExplanation') }}</strong></p>
            <p>{{ $t('repairSetting.areaPoint1') }}</p>
            <p>{{ $t('repairSetting.areaPoint2') }}</p>
          </div>
        </el-col>
        <el-col :span="6">
          <el-pagination background :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 子组件 -->
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
      searchForm: {
        repairTypeName: '',
        repairWay: '',
        repairSettingType: '',
        publicArea: '',
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

    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    handleReset() {
      this.searchForm = {
        repairTypeName: '',
        repairWay: '',
        repairSettingType: '',
        publicArea: '',
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

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .margin-top-xs {
    margin-top: 20px;
  }

  .explanation-section {
    font-size: 14px;

    p {
      margin-bottom: 2px;
    }
  }
}
</style>