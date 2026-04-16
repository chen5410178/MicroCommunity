<template>
  <div class="complaint-type-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('complaintType.title') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="_openAddComplaintTypeModal">
          {{ $t('complaintType.add') }}
        </el-button>
      </div>

      <el-table :data="complaintTypeInfo.complaintTypes" border style="width: 100%" v-loading="loading">
        <el-table-column prop="typeName" :label="$t('complaintType.typeName')" align="center" />
        <el-table-column prop="notifyWay" :label="$t('complaintType.notifyWay')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.notifyWay === 'SMS'">
              {{ $t('complaintType.sms') }}
            </span>
            <span v-else-if="scope.row.notifyWay === 'WORK_LICENSE'">
              {{ $t('complaintType.wechatWorkLicense') }}
            </span>
            <span v-else>
              {{ $t('complaintType.wechat') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="appraiseReply" :label="$t('complaintType.appraiseReply')" align="center">
          <template slot-scope="scope">
            {{ scope.row.appraiseReply === 'Y' ? $t('complaintType.autoReply') : $t('complaintType.manualReply') }}
          </template>
        </el-table-column>
        <el-table-column prop="staffs" :label="$t('complaintType.handler')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.staffs" :key="index">
              {{ item.staffName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('complaintType.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_openEditComplaintTypeModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteComplaintTypeModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="complaintTypeInfo.total" style="margin-top: 20px;" />
    </el-card>

    <delete-complaint-type ref="deleteComplaintType" @success="handleSuccess" />
  </div>
</template>

<script>
import { listComplaintType } from '@/api/oa/complaintTypeApi'
import DeleteComplaintType from '@/components/oa/deleteComplaintType'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ComplaintTypeList',
  components: {
    DeleteComplaintType
  },
  data() {
    return {
      loading: false,
      complaintTypeInfo: {
        complaintTypes: [],
        total: 0,
        conditions: {
          typeCd: '',
          typeName: '',
          notifyWay: ''
        }
      },
      page: {
        current: 1,
        size: 10
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listComplaintTypes()
  },
  methods: {
    async _listComplaintTypes() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          ...this.complaintTypeInfo.conditions
        }
        const { data, total } = await listComplaintType(params)
        this.complaintTypeInfo.complaintTypes = data
        this.complaintTypeInfo.total = total
      } catch (error) {
        this.$message.error(this.$t('complaintType.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddComplaintTypeModal() {
      this.$router.push('/views/oa/addComplaintType')
    },
    _openEditComplaintTypeModel(complaintType) {
      this.$router.push(`/views/oa/editComplaintType?typeCd=${complaintType.typeCd}`)
    },
    _openDeleteComplaintTypeModel(complaintType) {
      this.$refs.deleteComplaintType.open(complaintType)
    },
    handleSuccess() {
      this._listComplaintTypes()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listComplaintTypes()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listComplaintTypes()
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-type-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>