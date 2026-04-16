<template>
  <div class="pay-fee-batch-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('payFeeBatch.search.title') }}</span>
          </div>
          <div class="search-content">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input v-model="payFeeBatchInfo.conditions.batchId" :placeholder="$t('payFeeBatch.search.batchId')"
                  clearable />
              </el-col>
              <el-col :span="6">
                <el-select v-model="payFeeBatchInfo.conditions.state" :placeholder="$t('payFeeBatch.search.state')"
                  style="width:100%">
                  <el-option :label="$t('payFeeBatch.state.normal')" value="2006001" />
                  <el-option :label="$t('payFeeBatch.state.applyCancel')" value="2007001" />
                  <el-option :label="$t('payFeeBatch.state.auditPass')" value="2008001" />
                  <el-option :label="$t('payFeeBatch.state.auditFail')" value="2009001" />
                </el-select>
              </el-col>
              <el-col :span="6">
                <el-input v-model="payFeeBatchInfo.conditions.createUserName"
                  :placeholder="$t('payFeeBatch.search.createUserName')" clearable />
              </el-col>
              <el-col :span="6">
                <el-button type="primary" @click="_queryPayFeeBatchMethod">
                  <i class="el-icon-search"></i>
                  {{ $t('common.search') }}
                </el-button>
                <el-button @click="_resetPayFeeBatchMethod">
                  <i class="el-icon-refresh"></i>
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('payFeeBatch.list.title') }}</span>
          </div>
          <el-table :data="payFeeBatchInfo.payFeeBatchs" border style="width:100%">
            <el-table-column prop="batchId" :label="$t('payFeeBatch.table.batchId')" align="center" />
            <el-table-column prop="createUserName" :label="$t('payFeeBatch.table.createUserName')" align="center" />
            <el-table-column prop="createTime" :label="$t('payFeeBatch.table.createTime')" align="center" />
            <el-table-column prop="remark" :label="$t('payFeeBatch.table.remark')" align="center">
              <template slot-scope="scope">
                {{ scope.row.remark || $t('payFeeBatch.table.noRemark') }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('payFeeBatch.table.stateName')" align="center" />
            <el-table-column prop="msg" :label="$t('payFeeBatch.table.msg')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state === '2006001'" size="mini" @click="_openApply(scope.row)">
                  {{ $t('payFeeBatch.button.applyCancel') }}
                </el-button>
                <el-button v-if="scope.row.state === '2007001'" size="mini" type="primary"
                  @click="_openPayFeeBatchAuditModel(scope.row)">
                  {{ $t('payFeeBatch.button.audit') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <apply-delete-fee-batch ref="applyDeleteFeeBatch" @success="handleSuccess" />
    <audit ref="audit" @success="_auditPayFeeBatchState" />
  </div>
</template>

<script>
import { listPayFeeBatch, updatePayFeeBatch } from '@/api/fee/payFeeBatchApi'
import { getCommunityId } from '@/api/community/communityApi'
import ApplyDeleteFeeBatch from '@/components/fee/applyDeleteFeeBatch'
import Audit from '@/components/fee/audit'

export default {
  name: 'PayFeeBatchList',
  components: {
    ApplyDeleteFeeBatch,
    Audit
  },
  data() {
    return {
      payFeeBatchInfo: {
        payFeeBatchs: [],
        conditions: {
          communityId: '',
          state: '',
          batchId: '',
          createUserName: ''
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.payFeeBatchInfo.conditions.communityId = getCommunityId()
    this._listPayFeeBatchs(this.pagination.current, this.pagination.size)
  },
  methods: {
    async _listPayFeeBatchs(page, size) {
      try {
        const params = {
          ...this.payFeeBatchInfo.conditions,
          page,
          row: size
        }
        const { data, total } = await listPayFeeBatch(params)
        this.payFeeBatchInfo.payFeeBatchs = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取数据失败:', error)
      }
    },
    _queryPayFeeBatchMethod() {
      this.pagination.current = 1
      this._listPayFeeBatchs(this.pagination.current, this.pagination.size)
    },
    _resetPayFeeBatchMethod() {
      this.payFeeBatchInfo.conditions = {
        ...this.payFeeBatchInfo.conditions,
        state: '',
        batchId: '',
        createUserName: ''
      }
      this._queryPayFeeBatchMethod()
    },
    _openApply(payFee) {
      this.$refs.applyDeleteFeeBatch.open(payFee)
    },
    _openPayFeeBatchAuditModel(payFee) {
      this.payFeeBatchInfo.payFeeBatch = payFee
      this.$refs.audit.open()
    },
    async _auditPayFeeBatchState(auditInfo) {
      try {
        const payFeeBatch = {
          ...this.payFeeBatchInfo.payFeeBatch,
          state: auditInfo.state,
          msg: auditInfo.remark
        }
        await updatePayFeeBatch(payFeeBatch)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listPayFeeBatchs(this.pagination.current, this.pagination.size)
      } catch (error) {
        console.error('审核失败:', error)
      }
    },
    handleSuccess() {
      this._listPayFeeBatchs(this.pagination.current, this.pagination.size)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listPayFeeBatchs(this.pagination.current, this.pagination.size)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listPayFeeBatchs(this.pagination.current, this.pagination.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.pay-fee-batch-container {
  padding: 20px;

  .search-content {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>