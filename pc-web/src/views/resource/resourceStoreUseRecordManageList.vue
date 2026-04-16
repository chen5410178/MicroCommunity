<template>
  <div class="resource-store-use-record-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceStoreUseRecordManage.search.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="_moreCondition()">
          {{ resourceStoreUseRecordManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="resourceStoreUseRecordManageInfo.conditions.rsurId"
            :placeholder="$t('resourceStoreUseRecordManage.search.rsurId')" clearable />
        </el-col>
        <el-col :span="8">
          <el-input v-model="resourceStoreUseRecordManageInfo.conditions.repairId"
            :placeholder="$t('resourceStoreUseRecordManage.search.repairId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="resourceStoreUseRecordManageInfo.conditions.resId"
            :placeholder="$t('resourceStoreUseRecordManage.search.resId')" clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryResourceStoreUseRecordMethod()">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetResourceStoreUseRecordMethod()">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <!-- 更多查询条件 -->
      <div v-show="resourceStoreUseRecordManageInfo.moreCondition">
        <el-row :gutter="20" class="mt-20">
          <el-col :span="6">
            <el-input v-model="resourceStoreUseRecordManageInfo.conditions.createUserId"
              :placeholder="$t('resourceStoreUseRecordManage.search.createUserId')" clearable />
          </el-col>
          <el-col :span="8">
            <el-input v-model="resourceStoreUseRecordManageInfo.conditions.createUserName"
              :placeholder="$t('resourceStoreUseRecordManage.search.createUserName')" clearable />
          </el-col>
          <el-col :span="6">
            <el-input v-model="resourceStoreUseRecordManageInfo.conditions.resName"
              :placeholder="$t('resourceStoreUseRecordManage.search.resName')" clearable />
          </el-col>
        </el-row>

        <el-row :gutter="20" class="mt-20">
          <el-col :span="6">
            <el-select v-model="resourceStoreUseRecordManageInfo.conditions.parentRstId"
              :placeholder="$t('resourceStoreUseRecordManage.search.parentRstId')" style="width:100%"
              @change="_listResourceStoreSonTypes">
              <el-option v-for="item in resourceStoreUseRecordManageInfo.resourceStoreTypes" :key="item.rstId"
                :label="item.name" :value="item.rstId" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="resourceStoreUseRecordManageInfo.conditions.rstId"
              :placeholder="$t('resourceStoreUseRecordManage.search.rstId')" style="width:100%"
              @change="_listResourceStoreSpecifications">
              <el-option v-for="item in resourceStoreUseRecordManageInfo.resourceStoreSonTypes" :key="item.rstId"
                :label="item.name" :value="item.rstId" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="resourceStoreUseRecordManageInfo.conditions.rssId"
              :placeholder="$t('resourceStoreUseRecordManage.search.rssId')" style="width:100%">
              <el-option v-for="item in resourceStoreUseRecordManageInfo.resourceStoreSpecifications" :key="item.rssId"
                :label="item.specName" :value="item.rssId" />
            </el-select>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="mt-20">
          <el-col :span="6">
            <el-select v-model="resourceStoreUseRecordManageInfo.conditions.state"
              :placeholder="$t('resourceStoreUseRecordManage.search.state')" style="width:100%">
              <el-option v-for="item in resourceStoreUseRecordManageInfo.states" :key="item.statusCd" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-date-picker v-model="resourceStoreUseRecordManageInfo.conditions.startTime" type="datetime"
              :placeholder="$t('resourceStoreUseRecordManage.search.startTime')" style="width:100%" />
          </el-col>
          <el-col :span="6">
            <el-date-picker v-model="resourceStoreUseRecordManageInfo.conditions.endTime" type="datetime"
              :placeholder="$t('resourceStoreUseRecordManage.search.endTime')" style="width:100%" />
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 使用记录列表 -->
    <el-card class="list-wrapper mt-20">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceStoreUseRecordManage.list.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="_exportExcel()">
          <i class="el-icon-download"></i>
          {{ $t('common.export') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="resourceStoreUseRecordManageInfo.resourceStoreUseRecords" border
        style="width: 100%">
        <el-table-column prop="rsurId" :label="$t('resourceStoreUseRecordManage.table.rsurId')" align="center" />
        <el-table-column prop="repairId" :label="$t('resourceStoreUseRecordManage.table.repairId')" align="center" />
        <el-table-column prop="resId" :label="$t('resourceStoreUseRecordManage.table.resId')" align="center" />
        <el-table-column :label="$t('resourceStoreUseRecordManage.table.resourceType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('resourceStoreUseRecordManage.table.resourceStoreName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.resourceStoreName || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('resourceStoreUseRecordManage.table.specName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.specName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="isFixedName" :label="$t('resourceStoreUseRecordManage.table.isFixedName')"
          align="center" />
        <el-table-column prop="stateName" :label="$t('resourceStoreUseRecordManage.table.stateName')" align="center" />
        <el-table-column :label="$t('resourceStoreUseRecordManage.table.quantity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.quantity }}{{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('resourceStoreUseRecordManage.table.unitPrice')" align="center">
          <template slot-scope="scope">
            {{ scope.row.unitPrice || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createUserId" :label="$t('resourceStoreUseRecordManage.table.createUserId')"
          align="center" />
        <el-table-column prop="createUserName" :label="$t('resourceStoreUseRecordManage.table.createUserName')"
          align="center" />
        <el-table-column prop="createTime" :label="$t('resourceStoreUseRecordManage.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('resourceStoreUseRecordManage.table.remark')" align="center" />
      </el-table>

      <el-pagination class="mt-20" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper" :total="resourceStoreUseRecordManageInfo.total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { listResourceStoreUseRecords, listResourceStoreTypes, listResourceStoreSpecifications, exportData } from '@/api/resource/resourceStoreUseRecordManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceStoreUseRecordManageList',
  data() {
    return {
      loading: false,
      resourceStoreUseRecordManageInfo: {
        resourceStoreUseRecords: [],
        total: 0,
        records: 1,
        moreCondition: false,
        states: [],
        conditions: {
          rsurId: '',
          repairId: '',
          resId: '',
          resName: '',
          parentRstId: '',
          rstId: '',
          rssId: '',
          createUserId: '',
          createUserName: '',
          startTime: '',
          endTime: '',
          state: '',
          userId: '',
          userName: '',
          communityId: ''
        },
        resourceStoreTypes: [],
        resourceStoreSonTypes: [],
        resourceStoreSpecifications: []
      },
      page: {
        current: 1,
        size: 10
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.resourceStoreUseRecordManageInfo.conditions.communityId = this.communityId
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      try {
        this.loading = true
        await this.getDictData()
        await this._listResourceStoreTypes()
        await this._listResourceStoreUseRecords()
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        const data = await getDict('resource_store_use_record', 'state')
        this.resourceStoreUseRecordManageInfo.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listResourceStoreUseRecords() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.resourceStoreUseRecordManageInfo.conditions
        }

        const { data, total } = await listResourceStoreUseRecords(params)
        this.resourceStoreUseRecordManageInfo.resourceStoreUseRecords = data
        this.resourceStoreUseRecordManageInfo.total = total
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async _listResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: '0'
        }
        const { data } = await listResourceStoreTypes(params)
        this.resourceStoreUseRecordManageInfo.resourceStoreTypes = data
      } catch (error) {
        console.error(error)
      }
    },
    async _listResourceStoreSonTypes() {
      try {
        this.resourceStoreUseRecordManageInfo.conditions.rstId = ''
        this.resourceStoreUseRecordManageInfo.resourceStoreSonTypes = []

        if (!this.resourceStoreUseRecordManageInfo.conditions.parentRstId) return

        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: this.resourceStoreUseRecordManageInfo.conditions.parentRstId
        }
        const { data } = await listResourceStoreTypes(params)
        this.resourceStoreUseRecordManageInfo.resourceStoreSonTypes = data
      } catch (error) {
        console.error(error)
      }
    },
    async _listResourceStoreSpecifications() {
      try {
        this.resourceStoreUseRecordManageInfo.resourceStoreSpecifications = []
        this.resourceStoreUseRecordManageInfo.conditions.rssId = ''

        if (!this.resourceStoreUseRecordManageInfo.conditions.rstId) return

        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          rstId: this.resourceStoreUseRecordManageInfo.conditions.rstId
        }
        const { data } = await listResourceStoreSpecifications(params)
        this.resourceStoreUseRecordManageInfo.resourceStoreSpecifications = data
      } catch (error) {
        console.error(error)
      }
    },
    _queryResourceStoreUseRecordMethod() {
      this.page.current = 1
      this._listResourceStoreUseRecords()
    },
    _resetResourceStoreUseRecordMethod() {
      this.resourceStoreUseRecordManageInfo.conditions = {
        rsurId: '',
        repairId: '',
        resId: '',
        resName: '',
        parentRstId: '',
        rstId: '',
        rssId: '',
        createUserId: '',
        createUserName: '',
        startTime: '',
        endTime: '',
        state: '',
        userId: this.resourceStoreUseRecordManageInfo.conditions.userId,
        userName: this.resourceStoreUseRecordManageInfo.conditions.userName,
        communityId: this.communityId
      }
      this.resourceStoreUseRecordManageInfo.resourceStoreSonTypes = []
      this.resourceStoreUseRecordManageInfo.resourceStoreSpecifications = []
      this.page.current = 1
      this._listResourceStoreUseRecords()
    },
    _moreCondition() {
      this.resourceStoreUseRecordManageInfo.moreCondition = !this.resourceStoreUseRecordManageInfo.moreCondition
    },
    async _exportExcel() {
      try {
        this.loading = true
        const params = {
          pagePath: 'resourceStoreUseRecordManage',
          ...this.resourceStoreUseRecordManageInfo.conditions
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error(error)
        this.$message.error(this.$t('common.exportFailed'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listResourceStoreUseRecords()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listResourceStoreUseRecords()
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-store-use-record-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .mt-20 {
    margin-top: 20px;
  }

  .el-date-editor,
  .el-select,
  .el-input {
    width: 100%;
  }
}
</style>