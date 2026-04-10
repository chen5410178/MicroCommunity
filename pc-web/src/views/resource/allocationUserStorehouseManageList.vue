<template>
  <div class="allocation-user-storehouse-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationUserStorehouseManage.search.title') }}</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="toggleMoreCondition"
        >
          {{ allocationUserStorehouseManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.ausId"
            :placeholder="$t('allocationUserStorehouseManage.search.ausId')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.resId"
            :placeholder="$t('allocationUserStorehouseManage.search.resId')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.resName"
            :placeholder="$t('allocationUserStorehouseManage.search.resName')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="queryAllocationUserStorehouse">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="resetAllocationUserStorehouse">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <!-- 更多查询条件 -->
      <el-row v-show="allocationUserStorehouseManageInfo.moreCondition" :gutter="20" style="margin-top: 20px;">
        <el-col :span="6">
          <el-select
            v-model="allocationUserStorehouseManageInfo.conditions.parentRstId"
            :placeholder="$t('allocationUserStorehouseManage.search.parentRstId')"
            style="width: 100%"
            @change="listResourceStoreSonTypes"
          >
            <el-option
              v-for="item in allocationUserStorehouseManageInfo.resourceStoreTypes"
              :key="item.rstId"
              :label="item.name"
              :value="item.rstId"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="allocationUserStorehouseManageInfo.conditions.rstId"
            :placeholder="$t('allocationUserStorehouseManage.search.rstId')"
            style="width: 100%"
            @change="listResourceStoreSpecifications"
          >
            <el-option
              v-for="item in allocationUserStorehouseManageInfo.resourceStoreSonTypes"
              :key="item.rstId"
              :label="item.name"
              :value="item.rstId"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="allocationUserStorehouseManageInfo.conditions.rssId"
            :placeholder="$t('allocationUserStorehouseManage.search.rssId')"
            style="width: 100%"
          >
            <el-option
              v-for="item in allocationUserStorehouseManageInfo.resourceStoreSpecifications"
              :key="item.rssId"
              :label="item.specName"
              :value="item.rssId"
            />
          </el-select>
        </el-col>
      </el-row>

      <el-row v-show="allocationUserStorehouseManageInfo.moreCondition" :gutter="20" style="margin-top: 20px;">
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.startUserId"
            :placeholder="$t('allocationUserStorehouseManage.search.startUserId')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.startUserName"
            :placeholder="$t('allocationUserStorehouseManage.search.startUserName')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.acceptUserId"
            :placeholder="$t('allocationUserStorehouseManage.search.acceptUserId')"
            clearable
          />
        </el-col>
      </el-row>

      <el-row v-show="allocationUserStorehouseManageInfo.moreCondition" :gutter="20" style="margin-top: 20px;">
        <el-col :span="6">
          <el-input
            v-model="allocationUserStorehouseManageInfo.conditions.acceptUserName"
            :placeholder="$t('allocationUserStorehouseManage.search.acceptUserName')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-date-picker
            v-model="allocationUserStorehouseManageInfo.conditions.startTime"
            type="datetime"
            :placeholder="$t('allocationUserStorehouseManage.search.startTime')"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="6">
          <el-date-picker
            v-model="allocationUserStorehouseManageInfo.conditions.endTime"
            type="datetime"
            :placeholder="$t('allocationUserStorehouseManage.search.endTime')"
            style="width: 100%"
          />
        </el-col>
      </el-row>
    </el-card>

    <!-- 转赠记录 -->
    <el-card class="list-wrapper" style="margin-top: 20px;">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationUserStorehouseManage.list.title') }}</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="exportExcel"
        >
          <i class="el-icon-download"></i>
          {{ $t('common.export') }}
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="allocationUserStorehouseManageInfo.allocationUserStorehouses"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="ausId"
          :label="$t('allocationUserStorehouseManage.table.ausId')"
          align="center"
        />
        <el-table-column
          prop="resId"
          :label="$t('allocationUserStorehouseManage.table.resId')"
          align="center"
        />
        <el-table-column
          :label="$t('allocationUserStorehouseManage.table.resType')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
          </template>
        </el-table-column>
        <el-table-column
          prop="resName"
          :label="$t('allocationUserStorehouseManage.table.resName')"
          align="center"
        />
        <el-table-column
          :label="$t('allocationUserStorehouseManage.table.specName')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.specName || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="isFixedName"
          :label="$t('allocationUserStorehouseManage.table.isFixedName')"
          align="center"
        />
        <el-table-column
          prop="startUserId"
          :label="$t('allocationUserStorehouseManage.table.startUserId')"
          align="center"
        />
        <el-table-column
          prop="startUserName"
          :label="$t('allocationUserStorehouseManage.table.startUserName')"
          align="center"
        />
        <el-table-column
          prop="acceptUserId"
          :label="$t('allocationUserStorehouseManage.table.acceptUserId')"
          align="center"
        />
        <el-table-column
          prop="acceptUserName"
          :label="$t('allocationUserStorehouseManage.table.acceptUserName')"
          align="center"
        />
        <el-table-column
          :label="$t('allocationUserStorehouseManage.table.stock')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.stock }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('allocationUserStorehouseManage.table.giveQuantity')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.giveQuantity }}{{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('allocationUserStorehouseManage.table.createTime')"
          align="center"
        />
        <el-table-column
          prop="remark"
          :label="$t('allocationUserStorehouseManage.table.remark')"
          align="center"
        />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-row>
          <el-col :span="12">
            <div class="total-info">
              <span class="sub-total">
                <b>{{ $t('allocationUserStorehouseManage.total.subTotal') }}</b>
                <span>{{ $t('allocationUserStorehouseManage.total.quantity') }}</span>
                {{ allocationUserStorehouseManageInfo.subTotalQuantity }}
              </span>
              <br>
              <span class="high-total">
                <b>{{ $t('allocationUserStorehouseManage.total.highTotal') }}</b>
                <span>{{ $t('allocationUserStorehouseManage.total.quantity') }}</span>
                {{ allocationUserStorehouseManageInfo.highTotalQuantity }}
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <el-pagination
              :current-page.sync="page.current"
              :page-sizes="[10, 20, 30, 50]"
              :page-size="page.size"
              :total="page.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listAllocationUserStorehouses, listResourceStoreTypes, listResourceStoreSpecifications, exportData } from '@/api/resource/allocationUserStorehouseManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AllocationUserStorehouseManageList',
  data() {
    return {
      loading: false,
      communityId: '',
      allocationUserStorehouseManageInfo: {
        allocationUserStorehouses: [],
        total: 0,
        records: 1,
        moreCondition: false,
        subTotalQuantity: '0',
        highTotalQuantity: '0',
        conditions: {
          resId: '',
          resName: '',
          acceptUserId: '',
          acceptUserName: '',
          startUserId: '',
          startUserName: '',
          parentRstId: '',
          rstId: '',
          rssId: '',
          ausId: '',
          startTime: '',
          endTime: '',
          communityId: ''
        },
        resourceStoreTypes: [],
        resourceStoreSonTypes: [],
        resourceStoreSpecifications: []
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.allocationUserStorehouseManageInfo.conditions.communityId = this.communityId
    this.initData()
  },
  methods: {
    async initData() {
      await this.listAllocationUserStorehouses()
      await this.listResourceStoreTypes()
    },
    async listAllocationUserStorehouses() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.allocationUserStorehouseManageInfo.conditions
        }
        const { data, total } = await listAllocationUserStorehouses(params)
        this.allocationUserStorehouseManageInfo.allocationUserStorehouses = data
        this.page.total = total
        if (data.length > 0) {
          this.allocationUserStorehouseManageInfo.subTotalQuantity = data[0].subTotalQuantity || '0'
          this.allocationUserStorehouseManageInfo.highTotalQuantity = data[0].highTotalQuantity || '0'
        } else {
          this.allocationUserStorehouseManageInfo.subTotalQuantity = '0'
          this.allocationUserStorehouseManageInfo.highTotalQuantity = '0'
        }
      } catch (error) {
        console.error('Failed to fetch allocation user storehouses:', error)
      } finally {
        this.loading = false
      }
    },
    async listResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: '0'
        }
        const { data } = await listResourceStoreTypes(params)
        this.allocationUserStorehouseManageInfo.resourceStoreTypes = data
      } catch (error) {
        console.error('Failed to fetch resource store types:', error)
      }
    },
    async listResourceStoreSonTypes() {
      this.allocationUserStorehouseManageInfo.conditions.rstId = ''
      this.allocationUserStorehouseManageInfo.resourceStoreSonTypes = []
      if (!this.allocationUserStorehouseManageInfo.conditions.parentRstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: this.allocationUserStorehouseManageInfo.conditions.parentRstId
        }
        const { data } = await listResourceStoreTypes(params)
        this.allocationUserStorehouseManageInfo.resourceStoreSonTypes = data
      } catch (error) {
        console.error('Failed to fetch resource store son types:', error)
      }
    },
    async listResourceStoreSpecifications() {
      this.allocationUserStorehouseManageInfo.resourceStoreSpecifications = []
      this.allocationUserStorehouseManageInfo.conditions.rssId = ''
      if (!this.allocationUserStorehouseManageInfo.conditions.rstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          rstId: this.allocationUserStorehouseManageInfo.conditions.rstId
        }
        const { data } = await listResourceStoreSpecifications(params)
        this.allocationUserStorehouseManageInfo.resourceStoreSpecifications = data
      } catch (error) {
        console.error('Failed to fetch resource store specifications:', error)
      }
    },
    queryAllocationUserStorehouse() {
      this.page.current = 1
      this.listAllocationUserStorehouses()
    },
    resetAllocationUserStorehouse() {
      this.allocationUserStorehouseManageInfo.conditions = {
        resId: '',
        resName: '',
        acceptUserId: '',
        acceptUserName: '',
        startUserId: '',
        startUserName: '',
        parentRstId: '',
        rstId: '',
        rssId: '',
        ausId: '',
        startTime: '',
        endTime: '',
        communityId: this.communityId
      }
      this.allocationUserStorehouseManageInfo.resourceStoreSonTypes = []
      this.allocationUserStorehouseManageInfo.resourceStoreSpecifications = []
      this.page.current = 1
      this.listAllocationUserStorehouses()
    },
    toggleMoreCondition() {
      this.allocationUserStorehouseManageInfo.moreCondition = !this.allocationUserStorehouseManageInfo.moreCondition
    },
    async exportExcel() {
      try {
        const params = {
          pagePath: 'allocationUserStorehouseManage',
          ...this.allocationUserStorehouseManageInfo.conditions
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('Failed to export:', error)
        this.$message.error(this.$t('allocationUserStorehouseManage.exportFailed'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listAllocationUserStorehouses()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listAllocationUserStorehouses()
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-user-storehouse-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    .pagination-wrapper {
      margin-top: 20px;

      .total-info {
        padding: 10px 0;
        line-height: 2;

        .sub-total, .high-total {
          margin-right: 20px;
        }
      }
    }
  }
}
</style>