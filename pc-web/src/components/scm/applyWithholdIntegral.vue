<template>
  <div>
    <el-table v-loading="loading" :data="applys" border style="width: 100%">
      <el-table-column prop="iwId" :label="$t('applyWithholdIntegral.id')" align="center" />
      <el-table-column prop="integralId" :label="$t('applyWithholdIntegral.integralId')" align="center" />
      <el-table-column prop="integralName" :label="$t('applyWithholdIntegral.integralName')" align="center" />
      <el-table-column prop="amount" :label="$t('applyWithholdIntegral.withdrawAmount')" align="center" />
      <el-table-column prop="applyUserName" :label="$t('applyWithholdIntegral.applicant')" align="center" />
      <el-table-column prop="applyUserTel" :label="$t('applyWithholdIntegral.applicantPhone')" align="center" />
      <el-table-column prop="bankNum" :label="$t('applyWithholdIntegral.bankAccount')" align="center" />
      <el-table-column prop="bankNumName" :label="$t('applyWithholdIntegral.accountName')" align="center" />
      <el-table-column prop="bankName" :label="$t('applyWithholdIntegral.bank')" align="center" />
      <el-table-column prop="remark" :label="$t('applyWithholdIntegral.applicationNote')" align="center" />
      <el-table-column prop="stateName" :label="$t('applyWithholdIntegral.status')" align="center" />
      <el-table-column prop="stateMsg" :label="$t('applyWithholdIntegral.reviewNote')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <delete-apply-withhold-integral ref="deleteDialog" @success="handleDeleteSuccess" />
  </div>
</template>

<script>
import { queryCommunityIntegralApply } from '@/api/scm/communityIntegralApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteApplyWithholdIntegral from './deleteApplyWithholdIntegral'

export default {
  name: 'ApplyWithholdIntegral',
  components: {
    DeleteApplyWithholdIntegral
  },

  data() {
    return {
      integralId: '',
      integralName: '',
      loading: false,
      applys: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(integral) {
      this.integralId = integral.integralId
      this.integralName = integral.integralName
      this.loadData()
    },
    async loadData() {
      try {
        this.loading = true
        const params = {
          communityId: getCommunityId(),
          integralId: this.integralId,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryCommunityIntegralApply(params)
        this.applys = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load apply withhold integral data:', error)
      } finally {
        this.loading = false
      }
    },
    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleDeleteSuccess() {
      this.loadData()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>