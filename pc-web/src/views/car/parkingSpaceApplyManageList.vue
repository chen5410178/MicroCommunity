<template>
  <div class="parking-space-apply-manage padding">
    <el-card class="search-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('parkingSpaceApplyManage.searchCondition') }}</span>
      </div>
      <el-form :model="conditions" label-width="0">
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item>
              <el-input v-model="conditions.carNum" :placeholder="$t('parkingSpaceApplyManage.licensePlate')" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-input v-model="conditions.applyPersonName" :placeholder="$t('parkingSpaceApplyManage.applicantName')"
                clearable />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-input v-model="conditions.applyPersonLink" :placeholder="$t('parkingSpaceApplyManage.applicantPhone')"
                clearable />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-select v-model="conditions.state" :placeholder="$t('parkingSpaceApplyManage.selectStatus')"
                style="width: 100%">
                <el-option :label="$t('parkingSpaceApplyManage.selectStatus')" value="" />
                <el-option :label="$t('parkingSpaceApplyManage.pendingReview')" value="1001" />
                <el-option :label="$t('parkingSpaceApplyManage.pendingPayment')" value="2002" />
                <el-option :label="$t('parkingSpaceApplyManage.completed')" value="3003" />
                <el-option :label="$t('parkingSpaceApplyManage.applicationFailed')" value="4004" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-button type="primary" @click="_queryParkingSpaceApplyMethod">
              {{ $t('parkingSpaceApplyManage.search') }}
            </el-button>
            <el-button @click="_resetParkingSpaceApplyMethod">
              {{ $t('parkingSpaceApplyManage.reset') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('parkingSpaceApplyManage.parkingSpaceApplication') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="_openAddParkingSpaceApplyModal">
          {{ $t('parkingSpaceApplyManage.applyForSpace') }}
        </el-button>
      </div>

      <el-table :data="parkingSpaceApplys" border v-loading="loading">
        <el-table-column prop="applyId" :label="$t('parkingSpaceApplyManage.applicationId')" align="center" />
        <el-table-column prop="carNum" :label="$t('parkingSpaceApplyManage.licensePlate')" align="center" />
        <el-table-column :label="$t('parkingSpaceApplyManage.parkingSpace')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.psId">
              {{ scope.row.areaNum }} - {{ scope.row.num }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('parkingSpaceApplyManage.vehicleType')" align="center">
          <template slot-scope="scope">
            {{ _getCatType(scope.row.carType) }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('parkingSpaceApplyManage.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('parkingSpaceApplyManage.endTime')" align="center" />
        <el-table-column prop="applyPersonName" :label="$t('parkingSpaceApplyManage.applicant')" align="center" />
        <el-table-column prop="applyPersonLink" :label="$t('parkingSpaceApplyManage.phoneNumber')" align="center" />
        <el-table-column :label="$t('parkingSpaceApplyManage.reviewResult')" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.state)" size="small">
              {{ _getState(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('parkingSpaceApplyManage.operation')" align="center" width="250">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === '1001'" size="mini" type="primary"
              @click="_openAuditParkingSpaceApplyModal(scope.row)">
              {{ $t('parkingSpaceApplyManage.audit') }}
            </el-button>
            <el-button v-if="scope.row.state === '1001'" size="mini" type="warning"
              @click="_openEditParkingSpaceApplyModel(scope.row)">
              {{ $t('parkingSpaceApplyManage.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteParkingSpaceApplyModel(scope.row)">
              {{ $t('parkingSpaceApplyManage.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" style="margin-top: 20px;" />
    </el-card>

    <edit-parking-space-apply ref="editComponent" @success="handleSuccess" />
    <delete-parking-space-apply ref="deleteComponent" @success="handleSuccess" />
  </div>
</template>

<script>
import { listParkingSpaceApply } from '@/api/car/parkingSpaceApplyManageApi'
import EditParkingSpaceApply from '@/components/car/editParkingSpaceApply'
import DeleteParkingSpaceApply from '@/components/car/deleteParkingSpaceApply'

export default {
  name: 'ParkingSpaceApplyManageList',
  components: {
    EditParkingSpaceApply,
    DeleteParkingSpaceApply
  },
  data() {
    return {
      loading: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      conditions: {
        state: '',
        carNum: '',
        applyPersonName: '',
        applyPersonLink: '',
        communityId: ''
      },
      parkingSpaceApplys: []
    }
  },
  created() {
    this._listParkingSpaceApplys(1, this.page.size)
  },
  methods: {
    async _listParkingSpaceApplys(page, size) {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: page,
          row: size
        }
        const response = await listParkingSpaceApply(params)
        if (response.code === 0) {
          this.parkingSpaceApplys = response.data
          this.page.total = response.total
          this.page.current = page
        } else {
          this.$message.error(response.msg || this.$t('common.requestFailed'))
        }
      } catch (error) {
        console.error('Error fetching parking space applications:', error)
        this.$message.error(this.$t('common.requestError'))
      } finally {
        this.loading = false
      }
    },

    _queryParkingSpaceApplyMethod() {
      this._listParkingSpaceApplys(1, this.page.size)
    },

    _resetParkingSpaceApplyMethod() {
      this.conditions = {
        state: '',
        carNum: '',
        applyPersonName: '',
        applyPersonLink: '',
        communityId: ''
      }
      this._listParkingSpaceApplys(1, this.page.size)
    },

    _openAddParkingSpaceApplyModal() {
      // 跳转到申请页面
      this.$router.push('/views/car/addParkingSpaceApply')
    },

    _openAuditParkingSpaceApplyModal(apply) {
      // 跳转到审核页面
      this.$router.push(`/views/car/auditParkingSpaceApply?applyId=${apply.applyId}`)
    },

    _openEditParkingSpaceApplyModel(apply) {
      this.$refs.editComponent.open(apply)
    },

    _openDeleteParkingSpaceApplyModel(apply) {
      this.$refs.deleteComponent.open(apply)
    },

    handleSizeChange(size) {
      this.page.size = size
      this._listParkingSpaceApplys(1, size)
    },

    handleCurrentChange(page) {
      this._listParkingSpaceApplys(page, this.page.size)
    },

    handleSuccess() {
      this._listParkingSpaceApplys(this.page.current, this.page.size)
    },

    getStatusTagType(state) {
      switch (state) {
        case '1001': return 'warning'
        case '2002': return 'primary'
        case '3003': return 'success'
        case '4004': return 'danger'
        default: return 'info'
      }
    },

    _getState(state) {
      switch (state) {
        case '1001': return this.$t('parkingSpaceApplyManage.pendingReview')
        case '2002': return this.$t('parkingSpaceApplyManage.pendingPayment')
        case '3003': return this.$t('parkingSpaceApplyManage.completed')
        case '4004': return this.$t('parkingSpaceApplyManage.applicationFailed')
        default: return this.$t('parkingSpaceApplyManage.statusAbnormal')
      }
    },

    _getCatType(type) {
      switch (type) {
        case '9901': return this.$t('parkingSpaceApplyManage.homeCar')
        case '9902': return this.$t('parkingSpaceApplyManage.bus')
        case '9903': return this.$t('parkingSpaceApplyManage.truck')
        default: return this.$t('parkingSpaceApplyManage.abnormalVehicle')
      }
    }
  }
}
</script>

<style scoped>
.search-card {
  margin-bottom: 20px;
}

.list-card {
  margin-top: 20px;
}
</style>