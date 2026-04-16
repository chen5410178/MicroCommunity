<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="24" class="text-right">
        <el-button 
          type="primary" 
          size="small" 
          v-if="simplifyCallableInfo.roomId"
          @click="_openWritePrintOweFeeCallableModal"
        >
          <i class="el-icon-plus"></i>
          {{ $t('simplifyCallable.register') }}
        </el-button>
        <el-button 
          type="primary" 
          size="small" 
          v-if="simplifyCallableInfo.roomId"
          @click="_openAddOweFeeCallableModal"
        >
          <i class="el-icon-plus"></i>
          {{ $t('simplifyCallable.callable') }}
        </el-button>
        <el-button 
          type="primary" 
          size="small" 
          v-if="simplifyCallableInfo.roomId"
          @click="_printOwnOrder"
        >
          <i class="el-icon-plus"></i>
          {{ $t('simplifyCallable.callableOrder') }}
        </el-button>
      </el-col>
    </el-row>
    <div>
      <el-table
        :data="simplifyCallableInfo.callables"
        style="margin-top:10px"
        border
        stripe
      >
        <el-table-column prop="ofcId" :label="$t('simplifyCallable.id')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('simplifyCallable.ownerName')" align="center"></el-table-column>
        <el-table-column prop="payerObjName" :label="$t('simplifyCallable.payerObjName')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('simplifyCallable.feeName')" align="center"></el-table-column>
        <el-table-column prop="amountdOwed" :label="$t('simplifyCallable.amount')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyCallable.timePeriod')" align="center">
          <template slot-scope="scope">
            {{scope.row.startTime}}<br>~{{scope.row.endTime}}
          </template>
        </el-table-column>
        <el-table-column prop="callableWayName" :label="$t('simplifyCallable.callableWay')" align="center"></el-table-column>
        <el-table-column prop="staffName" :label="$t('simplifyCallable.staffName')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('simplifyCallable.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('simplifyCallable.remark')" align="center">
          <template slot-scope="scope">
            <div class="textAuto" style="max-width: 200px;">{{scope.row.remark}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('simplifyCallable.createTime')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyCallable.actions')" align="center">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="_openDeleteOweFeeCallableModel(scope.row)">
                {{ $t('simplifyCallable.delete') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
    <write-owe-fee-callable ref="writeOweFeeCallable"></write-owe-fee-callable>
    <delete-owe-fee-callable ref="deleteOweFeeCallable"></delete-owe-fee-callable>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOweFeeCallable } from '@/api/fee/simplifyCallableApi'
import WriteOweFeeCallable from './writeOweFeeCallable'
import DeleteOweFeeCallable from './deleteOweFeeCallable'

export default {
  name: 'SimplifyCallable',
  components: {
    WriteOweFeeCallable,
    DeleteOweFeeCallable
  },
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      simplifyCallableInfo: {
        callables: [],
        ownerId: '',
        roomId: '',
        roomName: '',
        total: 0,
        records: 0
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this._initEvent()
  },
  methods: {
    _initEvent() {
      this.$on('switch', this.handleSwitch)
      this.$on('listOwnerData', this._listSimplifyCallable)
    },
    handleSwitch(param) {
      if (!param.roomId) return
      this.clearSimplifyCallableInfo()
      Object.assign(this.simplifyCallableInfo, param)
      this._listSimplifyCallable(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this._listSimplifyCallable(val, this.DEFAULT_ROWS)
    },
    async _listSimplifyCallable(page, row) {
      try {
        const res = await listOweFeeCallable({
          page,
          row,
          payerObjId: this.simplifyCallableInfo.roomId,
          communityId: getCommunityId()
        })
        this.simplifyCallableInfo.total = res.total
        this.simplifyCallableInfo.records = res.records
        this.simplifyCallableInfo.callables = res.data
        this.total = res.total
      } catch (error) {
        console.error('Request failed:', error)
      }
    },
    clearSimplifyCallableInfo() {
      this.simplifyCallableInfo = {
        callables: [],
        ownerId: '',
        roomId: '',
        roomName: '',
        total: 0,
        records: 0
      }
    },
    _printOwnOrder() {
      window.open(`/#/views/fee/printOweFee?payObjId=${this.simplifyCallableInfo.roomId}&payObjType=3333&payObjName=${this.simplifyCallableInfo.roomName}`)
    },
    _openWritePrintOweFeeCallableModal() {
      if (!this.simplifyCallableInfo.roomId) {
        this.$message.warning(this.$t('simplifyCallable.noRoomSelected'))
        return
      }
      this.$refs.writeOweFeeCallable.open({
        roomId: this.simplifyCallableInfo.roomId,
        roomName: this.simplifyCallableInfo.roomName
      })
    },
    _openAddOweFeeCallableModal() {
      this.$router.push(`/views/fee/roomOweFeeCallable?roomId=${this.simplifyCallableInfo.roomId}`)
    },
    _openDeleteOweFeeCallableModel(oweFeeCallable) {
      this.$refs.deleteOweFeeCallable.open(oweFeeCallable)
    },
    open(params) {
      this.handleSwitch(params)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.text-right {
  text-align: right;
}
.textAuto {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>