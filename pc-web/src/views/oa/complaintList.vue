<template>
  <div class="complaint-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="type-card">
          <div class="type-list-wrapper">
            <div class="type-list-item" :class="{ 'selected': selectedType === '' }"
              @click="switchType('')">
              {{ $t('complaint.allTypes') }}
            </div>
            <div v-for="(item, index) in complaintTypes" :key="index"
              class="type-list-item"
              :class="{ 'selected': selectedType === item.typeCd }"
              @click="switchType(item.typeCd)">
              {{ item.typeName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('complaint.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.roomName" :placeholder="$t('complaint.search.room')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.complaintName" :placeholder="$t('complaint.search.contact')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.tel" :placeholder="$t('complaint.search.phone')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.complaintId" :placeholder="$t('complaint.search.id')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="complaintInfo.conditions.startTime" type="datetime"
                :placeholder="$t('complaint.search.startTime')" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryComplaintMethod()">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetComplaintMethod()">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <el-tabs v-model="activeStateTab" @tab-click="handleStateTabClick">
            <el-tab-pane :label="$t('complaint.status.all')" name="ALL" />
            <el-tab-pane :label="$t('complaint.status.processing')" name="W" />
            <el-tab-pane :label="$t('complaint.status.completed')" name="C" />
          </el-tabs>

          <div style="text-align:right;margin-bottom:10px">
            <el-button type="primary" size="small" @click="_openAddComplaintModal()">
              <i class="el-icon-plus"></i>
              {{ $t('complaint.list.add') }}
            </el-button>
          </div>

          <el-table :data="complaintInfo.complaints" border style="width:100%" v-loading="loading">
            <el-table-column prop="complaintId" :label="$t('complaint.table.id')" align="center" />
            <el-table-column prop="typeName" :label="$t('complaint.table.type')" align="center" />
            <el-table-column prop="roomName" :label="$t('complaint.table.room')" align="center" />
            <el-table-column prop="complaintName" :label="$t('complaint.table.contact')" align="center" />
            <el-table-column prop="tel" :label="$t('complaint.table.phone')" align="center" />
            <el-table-column prop="stateName" :label="$t('complaint.table.status')" align="center" />
            <el-table-column :label="$t('complaint.table.handler')" align="center">
              <template slot-scope="scope">
                <span v-for="(item, index) in scope.row.staffs" :key="index">{{ item.staffName }}<br /></span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('complaint.table.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="220">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" @click="_openComplaintDetailModel(scope.row)">
                    {{ $t('common.detail') }}
                  </el-button>
                  <el-button size="mini" @click="_openEditComplaintModel(scope.row)">
                    {{ $t('common.edit') }}
                  </el-button>
                  <el-button size="mini" type="danger" @click="_openDeleteComplaintModel(scope.row)">
                    {{ $t('common.delete') }}
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination style="margin-top:15px" @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" />
        </el-card>
      </el-col>
    </el-row>

    <view-image ref="viewImage" />
    <edit-complaint ref="editComplaint" @success="handleSuccess" />
    <delete-complaint ref="deleteComplaint" @success="handleSuccess" />
    <complaint-detail ref="complaintDetail" />
  </div>
</template>

<script>
import { listComplaints, listComplaintTypes } from '@/api/oa/complaintApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'
import EditComplaint from '@/components/oa/editComplaint'
import DeleteComplaint from '@/components/oa/deleteComplaint'
import ComplaintDetail from '@/components/oa/complaintDetail'

export default {
  name: 'ComplaintList',
  components: {
    ViewImage,
    EditComplaint,
    DeleteComplaint,
    ComplaintDetail,
  },
  data() {
    return {
      loading: false,
      selectedType: '',
      activeStateTab: 'ALL',
      complaintTypes: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      complaintInfo: {
        complaints: [],
        conditions: {
          complaintId: '',
          typeCd: '',
          complaintName: '',
          tel: '',
          roomName: '',
          state: '',
          startTime: '',
          endTime: '',
          communityId: ''
        }
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.complaintInfo.conditions.communityId = this.communityId
    this._listComplaintTypes()
    this._listComplaints()
  },
  methods: {
    async _listComplaints() {
      try {
        this.loading = true
        const params = {
          ...this.complaintInfo.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await listComplaints(params)
        this.complaintInfo.complaints = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('complaint.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async _listComplaintTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listComplaintTypes(params)
        this.complaintTypes = data
      } catch (error) {
        console.error('获取投诉类型失败:', error)
      }
    },
    switchType(typeCd) {
      this.selectedType = typeCd
      this.complaintInfo.conditions.typeCd = typeCd
      this.page.current = 1
      this._listComplaints()
    },
    handleStateTabClick() {
      this.complaintInfo.conditions.state = this.activeStateTab === 'ALL' ? '' : this.activeStateTab
      this.page.current = 1
      this._listComplaints()
    },
    _openAddComplaintModal() {
      this.$router.push('/views/oa/addComplaint')
    },
    _openEditComplaintModel(row) {
      this.$refs.editComplaint.open(row)
    },
    _openDeleteComplaintModel(row) {
      this.$refs.deleteComplaint.open(row)
    },
    _openComplaintDetailModel(row) {
      this.$router.push(`/pages/complaint/complaintDetail?complaintId=${row.complaintId}`)
    },
    _queryComplaintMethod() {
      this.page.current = 1
      this._listComplaints()
    },
    _resetComplaintMethod() {
      this.complaintInfo.conditions.complaintId = ''
      this.complaintInfo.conditions.complaintName = ''
      this.complaintInfo.conditions.tel = ''
      this.complaintInfo.conditions.roomName = ''
      this.complaintInfo.conditions.startTime = ''
      this.complaintInfo.conditions.endTime = ''
      this.page.current = 1
      this._listComplaints()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listComplaints()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listComplaints()
    },
    handleSuccess() {
      this._listComplaints()
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-container {
  padding: 20px;

  .left-panel {
    .type-card {
      .type-list-wrapper {
        max-height: 600px;
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
      }
    }
  }

  .right-panel {
    padding-left: 5px;
  }

  .search-card {
    margin-bottom: 20px;
  }
}
</style>