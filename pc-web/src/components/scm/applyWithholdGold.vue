<template>
  <div class="apply-withhold-gold-container">
    <el-table v-loading="loading" :data="applys" border style="width: 100%">
      <el-table-column prop="gwId" :label="$t('applyWithholdGold.id')" align="center" />
      <el-table-column prop="goldId" :label="$t('applyWithholdGold.goldId')" align="center" />
      <el-table-column prop="goldName" :label="$t('applyWithholdGold.accountName')" align="center" />
      <el-table-column prop="communityId" :label="$t('applyWithholdGold.communityId')" align="center" />
      <el-table-column prop="amount" :label="$t('applyWithholdGold.withdrawAmount')" align="center" />
      <el-table-column prop="applyUserName" :label="$t('applyWithholdGold.applicant')" align="center" />
      <el-table-column prop="applyUserTel" :label="$t('applyWithholdGold.applicantPhone')" align="center" />
      <el-table-column prop="bankNum" :label="$t('applyWithholdGold.bankAccount')" align="center" />
      <el-table-column prop="bankNumName" :label="$t('applyWithholdGold.accountName')" align="center" />
      <el-table-column prop="bankName" :label="$t('applyWithholdGold.bankName')" align="center" />
      <el-table-column prop="remark" :label="$t('applyWithholdGold.applicationNote')" align="center" />
      <el-table-column prop="stateName" :label="$t('applyWithholdGold.status')" align="center" />
      <el-table-column prop="stateMsg" :label="$t('applyWithholdGold.reviewNote')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <delete-apply-withhold-gold ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getPropertyGoldApplys } from '@/api/scm/goldApi'
import DeleteApplyWithholdGold from '@/components/scm/deleteApplyWithholdGold'

export default {
  name: 'ApplyWithholdGold',
  components: {
    DeleteApplyWithholdGold
  },
  data() {
    return {
      goldId: '',
      goldName: '',
      loading: false,
      applys: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(gold) {
      this.goldId = gold.goldId
      this.goldName = gold.goldName
      this.loadData()
    },
    async loadData() {
      if (!this.goldId) return

      try {
        this.loading = true
        const params = {
          communityId: getCommunityId(),
          goldId: this.goldId,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await getPropertyGoldApplys(params)
        this.applys = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load apply withhold gold:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    },
    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleSuccess() {
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.apply-withhold-gold-container {
  padding: 20px 0;

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>