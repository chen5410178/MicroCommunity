<template>
  <div class="complaint-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="list-group-border-radius">
          <div class=" treeview">
            <ul class="list-group text-center ">
              <li v-for="(item, index) in complaintInfo.states" :key="index" @click="swatchComplaintState(item)"
                :class="{ 'vc-node-selected': complaintInfo.conditions.state == item.statusCd }"
                class="list-group-item node-orgTree">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('complaint.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="complaintInfo.conditions.typeCd" :placeholder="$t('complaint.search.type')"
                style="width:100%">
                <el-option v-for="item in complaintInfo.complaintTypes" :key="item.typeCd" :label="item.typeName"
                  :value="item.typeCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.roomName" :placeholder="$t('complaint.search.room')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.complaintName" :placeholder="$t('complaint.search.contact')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.tel" :placeholder="$t('complaint.search.phone')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="complaintInfo.conditions.complaintId" :placeholder="$t('complaint.search.id')" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryComplaintMethod()">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top:15px">
            <el-col :span="4">
              <el-date-picker v-model="complaintInfo.conditions.startTime" type="datetime"
                :placeholder="$t('complaint.search.startTime')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="complaintInfo.conditions.endTime" type="datetime"
                :placeholder="$t('complaint.search.endTime')" style="width:100%" />
            </el-col>
          </el-row>
        </el-card>

        <el-card style="margin-top:20px">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('complaint.list.title') }}</span>
            <el-button type="primary" size="small" style="float:right" @click="_openAddComplaintModal()">
              <i class="el-icon-plus"></i>{{ $t('complaint.list.add') }}
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
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      complaintInfo: {
        complaints: [],
        states: [
          { statusCd: '', name: this.$t('complaint.status.all') },
          { statusCd: 'W', name: this.$t('complaint.status.processing') },
          { statusCd: 'C', name: this.$t('complaint.status.completed') }
        ],
        complaintTypes: [],
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
        this.complaintInfo.complaintTypes = data
      } catch (error) {
        console.error('获取投诉类型失败:', error)
      }
    },
    swatchComplaintState(item) {
      this.complaintInfo.conditions.state = item.statusCd
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

  .border-radius {
    border-radius: 4px;
  }

  
}
</style>