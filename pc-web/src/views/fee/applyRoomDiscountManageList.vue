<template>
  <div class="apply-room-discount-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('applyRoomDiscount.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.roomName" :placeholder="$t('applyRoomDiscount.search.roomName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.applyType" :placeholder="$t('applyRoomDiscount.search.applyType')"
            style="width:100%">
            <el-option v-for="item in applyTypes" :key="item.applyType" :label="item.typeName"
              :value="item.applyType" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('applyRoomDiscount.search.state')" style="width:100%">
            <el-option v-for="item in states" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('applyRoomDiscount.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleOpenDiscountType">
            {{ $t('applyRoomDiscount.button.discountType') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleOpenAddApply">
            <i class="el-icon-plus"></i>
            {{ $t('applyRoomDiscount.button.phoneApply') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleExport">
            <i class="el-icon-download"></i>
            {{ $t('applyRoomDiscount.button.export') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="roomName" :label="$t('applyRoomDiscount.table.roomName')" align="center" />
        <el-table-column prop="discountId" :label="$t('applyRoomDiscount.table.discountId')" align="center" />
        <el-table-column prop="discountName" :label="$t('applyRoomDiscount.table.discountName')" align="center" />
        <el-table-column prop="applyTypeName" :label="$t('applyRoomDiscount.table.applyType')" align="center" />
        <el-table-column :label="$t('applyRoomDiscount.table.feeNames')" align="center">
          <template slot-scope="scope">
            {{ scope.row.feeNames && scope.row.feeNames.length > 0 ? scope.row.feeNames.join('、') : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createUserName" :label="$t('applyRoomDiscount.table.createUser')" align="center" />
        <el-table-column prop="createUserTel" :label="$t('applyRoomDiscount.table.createUserTel')" align="center" />
        <el-table-column prop="startTime" :label="$t('applyRoomDiscount.table.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('applyRoomDiscount.table.endTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('applyRoomDiscount.table.state')" align="center" />
        <el-table-column prop="createTime" :label="$t('applyRoomDiscount.table.createTime')" align="center" />
        <el-table-column :label="$t('applyRoomDiscount.table.inUse')" align="center">
          <template slot-scope="scope">
            {{ scope.row.inUse === '0' ? $t('applyRoomDiscount.status.notUsed') : $t('applyRoomDiscount.status.used') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('applyRoomDiscount.table.returnType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.discountId ? (scope.row.returnWay === '1002' ? $t('applyRoomDiscount.returnType.account') :
              $t('applyRoomDiscount.returnType.discount')) : '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('applyRoomDiscount.table.returnAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.returnAmount ? scope.row.returnAmount : '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === '1' && hasPrivilege('502021010723590006')" size="mini"
              @click="handleOpenCheck(scope.row)">
              {{ $t('applyRoomDiscount.button.check') }}
            </el-button>
            <el-button size="mini" @click="handleOpenRecord(scope.row)">
              {{ $t('applyRoomDiscount.button.record') }}
            </el-button>
            <el-button v-if="scope.row.state === '2' && hasPrivilege('502021010761730007')" size="mini"
              @click="handleOpenReview(scope.row)">
              {{ $t('applyRoomDiscount.button.review') }}
            </el-button>
            <el-button size="mini" @click="handleOpenEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleOpenDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <edit-apply-room-discount ref="editApplyRoomDiscount" @success="handleSuccess" />
    <edit-apply-room-discount-record ref="editApplyRoomDiscountRecord" @success="handleSuccess" />
    <review-apply-room-discount ref="reviewApplyRoomDiscount" @success="handleSuccess" />
    <delete-apply-room-discount ref="deleteApplyRoomDiscount" @success="handleSuccess" />
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { getDict,getCommunityId } from '@/api/community/communityApi'
import { queryApplyRoomDiscount, queryApplyRoomDiscountType } from '@/api/fee/applyRoomDiscountManageApi'
import EditApplyRoomDiscount from '@/components/fee/editApplyRoomDiscount'
import EditApplyRoomDiscountRecord from '@/components/fee/editApplyRoomDiscountRecord'
import ReviewApplyRoomDiscount from '@/components/fee/reviewApplyRoomDiscount'
import DeleteApplyRoomDiscount from '@/components/fee/deleteApplyRoomDiscount'
import ViewImage from '@/components/system/viewImage'
import {  exportData } from '@/api/report/reportFeeSummaryApi'


export default {
  name: 'ApplyRoomDiscountManageList',
  components: {
    EditApplyRoomDiscount,
    EditApplyRoomDiscountRecord,
    ReviewApplyRoomDiscount,
    DeleteApplyRoomDiscount,
    ViewImage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        roomName: '',
        applyType: '',
        state: '',
        communityId: ''
      },
      tableData: [],
      applyTypes: [],
      states: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getApplyTypes()
    this.getDictData()
    this.searchForm.communityId = getCommunityId()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await queryApplyRoomDiscount(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('applyRoomDiscount.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getApplyTypes() {
      try {
        const { data } = await queryApplyRoomDiscountType({
          page: 1,
          row: 50,
          communityId: this.searchForm.communityId
        })
        this.applyTypes = data
      } catch (error) {
        console.error('获取申请类型失败:', error)
      }
    },
    async getDictData() {
      try {
        const data = await getDict('apply_room_discount', 'state')
        this.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        roomName: '',
        applyType: '',
        state: '',
        communityId: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleOpenAddApply() {
      this.$router.push('/views/fee/addApplyRoomDiscount')
    },
    handleOpenEdit(row) {
      this.$refs.editApplyRoomDiscountRecord.open(row)
    },
    handleOpenCheck(row) {
      this.$refs.editApplyRoomDiscount.open(row)
    },
    handleOpenRecord(row) {
      this.$router.push({
        path: '/views/fee/listApplyRoomDiscountRecord',
        query: {
          ardId: row.ardId,
          roomId: row.roomId,
          roomName: row.roomName,
          state: row.state,
          stateName: row.stateName
        }
      })
    },
    handleOpenReview(row) {
      this.$refs.reviewApplyRoomDiscount.open(row)
    },
    handleOpenDelete(row) {
      this.$refs.deleteApplyRoomDiscount.open(row)
    },
    handleOpenDiscountType() {
      this.$router.push('/views/fee/discountType')
    },
   async handleExport() {
      // 导出逻辑
      try {
        const params = {
          ...this.searchForm,
          communityId: this.searchForm.communityId,
          pagePath: 'applyRoomDiscount'
        }

        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('Failed to export:', error)
        this.$message.error(this.$t('reportFeeSummary.exportFailed'))
      }
    },
    handleSuccess() {
      this.getList()
    },
  }
}
</script>

<style lang="scss" scoped>
.apply-room-discount-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    margin-bottom: 20px;

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>