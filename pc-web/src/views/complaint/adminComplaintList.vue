<template>
  <div class="admin-complaint-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminComplaint.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.roomName" :placeholder="$t('adminComplaint.search.roomNamePlaceholder')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.complaintName"
                :placeholder="$t('adminComplaint.search.complaintNamePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.tel" :placeholder="$t('adminComplaint.search.telPlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.complaintId" :placeholder="$t('adminComplaint.search.complaintIdPlaceholder')"
                clearable />
            </el-col>
            <el-col :span="4" class="flex justify-start">
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="margin-top">
            <el-col :span="4">
              <el-date-picker v-model="searchForm.startTime" type="datetime"
                :placeholder="$t('adminComplaint.search.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.endTime" type="datetime"
                :placeholder="$t('adminComplaint.search.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-col>


          </el-row>
        </el-card>

        <el-card class="box-card margin-top-20">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminComplaint.list.title') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="complaintId" :label="$t('adminComplaint.table.complaintId')" align="center" />
            <el-table-column prop="typeName" :label="$t('adminComplaint.table.type')" align="center" />
            <el-table-column prop="roomName" :label="$t('adminComplaint.table.room')" align="center" />
            <el-table-column prop="complaintName" :label="$t('adminComplaint.table.contact')" align="center" />
            <el-table-column prop="tel" :label="$t('adminComplaint.table.contactTel')" align="center" />
            <el-table-column prop="stateName" :label="$t('adminComplaint.table.status')" align="center" />
            <el-table-column :label="$t('adminComplaint.table.handler')" align="center">
              <template slot-scope="scope">
                <span v-for="(item, index) in scope.row.staffs" :key="index">{{ item.staffName }}<br /></span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminComplaint.table.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination class="margin-top-20" :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listAdminComplaints } from '@/api/complaint/adminComplaintApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import ViewImage from '@/components/community/viewImage'

export default {
  name: 'AdminComplaintList',
  components: {
    SelectAdminCommunity,
    ViewImage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        complaintId: '',
        typeCd: '',
        complaintName: '',
        tel: '',
        roomName: '',
        state: '',
        startTime: '',
        endTime: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await listAdminComplaints(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminComplaint.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.getList()
    },
    handleDetail(row) {
      this.$router.push(`/views/complaint/adminComplaintDetail?complaintId=${row.complaintId}`)
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
.admin-complaint-container {
  padding: 20px;

  .search-form {
    display: flex;
    flex-wrap: wrap;
  }

  .margin-top-20 {
    margin-top: 20px;
  }
}
</style>