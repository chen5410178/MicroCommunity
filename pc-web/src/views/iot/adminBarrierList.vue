<template>
  <div class="padding">
    <el-row :gutter="20">
      <el-col :span="4" class="padding-r-0">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminBarrier.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input :placeholder="$t('adminBarrier.search.machineCode')"
                v-model="adminBarrierInfo.conditions.machineCode" class="form-control" />
            </el-col>
            <el-col :span="6">
              <el-input :placeholder="$t('adminBarrier.search.machineName')"
                v-model="adminBarrierInfo.conditions.machineName" class="form-control" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" size="small" @click="_queryAdminBarrierMethod()">
                <i class="el-icon-search"></i>
                <span>{{ $t('common.search') }}</span>
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="clearfix  flex justify-between">
            <span>{{ $t('adminBarrier.list.title') }}</span>
          </div>
          <el-table :data="adminBarrierInfo.barriers" border style="width: 100%" v-loading="loading">
            <el-table-column prop="communityName" :label="$t('adminBarrier.table.communityName')" align="center" />
            <el-table-column prop="machineCode" :label="$t('adminBarrier.table.machineCode')" align="center" />
            <el-table-column prop="machineName" :label="$t('adminBarrier.table.machineName')" align="center" />
            <el-table-column prop="machineIp" :label="$t('adminBarrier.table.machineIp')" align="center" />
            <el-table-column :label="$t('adminBarrier.table.direction')" align="center">
              <template slot-scope="scope">
                {{ scope.row.direction === '3306' ? $t('adminBarrier.direction.in') : $t('adminBarrier.direction.out') }}
              </template>
            </el-table-column>
            <el-table-column prop="implBeanName" :label="$t('adminBarrier.table.implBeanName')" align="center" />
            <el-table-column prop="boxName" :label="$t('adminBarrier.table.boxName')" align="center" />
            <el-table-column prop="stateName" :label="$t('adminBarrier.table.stateName')" align="center" />
            <el-table-column prop="monitorName" :label="$t('adminBarrier.table.monitorName')" align="center" />
            <el-table-column prop="heartbeatTime" :label="$t('adminBarrier.table.heartbeatTime')" align="center" />
            <el-table-column prop="createTime" :label="$t('adminBarrier.table.createTime')" align="center" />
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="adminBarrierInfo.conditions.page" :page-sizes="[10, 20, 30, 50]"
            :page-size="adminBarrierInfo.conditions.row" layout="total, sizes, prev, pager, next, jumper"
            :total="adminBarrierInfo.total" class="pagination" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { listAdminBarrier } from '@/api/iot/adminBarrierApi'

export default {
  name: 'AdminBarrierList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      adminBarrierInfo: {
        barriers: [],
        parkingBoxs: [],
        total: 0,
        records: 1,
        moreCondition: false,
        machineId: '',
        conditions: {
          machineCode: '',
          machineName: '',
          boxId: '',
          communityId: '',
          page: 1,
          row: 10
        }
      }
    }
  },
  created() {
    this._listAdminBarriers(this.adminBarrierInfo.conditions.page, this.adminBarrierInfo.conditions.row)
  },
  methods: {
    handleCommunityChange(community) {
      this.adminBarrierInfo.conditions.communityId = community.communityId
      this._listAdminBarriers(this.adminBarrierInfo.conditions.page, this.adminBarrierInfo.conditions.row)
    },
    async _listAdminBarriers(page, rows) {
      try {
        this.loading = true
        this.adminBarrierInfo.conditions.page = page
        this.adminBarrierInfo.conditions.row = rows

        const res = await listAdminBarrier(this.adminBarrierInfo.conditions)
        this.adminBarrierInfo.total = res.total
        this.adminBarrierInfo.records = res.records
        this.adminBarrierInfo.barriers = res.data
      } catch (error) {
        console.error('请求失败处理', error)
      } finally {
        this.loading = false
      }
    },
    _queryAdminBarrierMethod() {
      this._listAdminBarriers(1, this.adminBarrierInfo.conditions.row)
    },
    handleSizeChange(val) {
      this._listAdminBarriers(this.adminBarrierInfo.conditions.page, val)
    },
    handleCurrentChange(val) {
      this._listAdminBarriers(val, this.adminBarrierInfo.conditions.row)
    }
  }
}
</script>

<style scoped>
.padding-r-0 {
  padding-right: 0;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>