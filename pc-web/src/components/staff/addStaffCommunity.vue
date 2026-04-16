<template>
  <el-dialog :title="$t('staffCommunity.addCommunityTitle')" :visible.sync="visible" width="70%">

    <el-row :gutter="20">
      <el-col :span="8">
        <el-input v-model="form.communityName" :placeholder="$t('staffCommunity.communityNamePlaceholder')" clearable
          @keyup.enter.native="queryCommunitys" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="queryCommunitys">
          {{ $t('common.search') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="communitys" border style="width: 100%" class="margin-top"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="communityId" :label="$t('staffCommunity.communityId')" align="center" />
      <el-table-column prop="name" :label="$t('staffCommunity.communityName')" align="center" />
      <el-table-column prop="address" :label="$t('staffCommunity.communityAddress')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <div class="text-right margin-top">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submit">{{ $t('common.submit') }}</el-button>
    </div>

  </el-dialog>
</template>

<script>
import { listWaitStaffCommunity, saveStaffCommunity } from '@/api/staff/staffCommunityApi'

export default {
  name: 'AddStaffCommunity',
  data() {
    return {
      visible: false,
      form: {
        staffId: '',
        staffName: '',
        communityName: '',
        selectCommunitys: []
      },
      communitys: [],
      selectedCommunities: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.visible = true
      this.form.staffId = params.staffId
      this.form.staffName = params.staffName || ''
      this.queryCommunitys()
    },
    async queryCommunitys() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          nameLike: this.form.communityName,
          staffId: this.form.staffId
        }
        const { data, total } = await listWaitStaffCommunity(params)
        this.communitys = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('staffCommunity.fetchCommunityError'))
      }
    },
    handleSelectionChange(val) {
      this.selectedCommunities = val
    },
    async submit() {
      if (this.selectedCommunities.length === 0) {
        this.$message.warning(this.$t('staffCommunity.selectCommunityTip'))
        return
      }

      try {
        const params = {
          staffId: this.form.staffId,
          staffName: this.form.staffName,
          communitys: this.selectedCommunities.map(item => ({
            communityId: item.communityId,
            communityName: item.name
          }))
        }
        await saveStaffCommunity(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('staffCommunity.addError'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.queryCommunitys()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.queryCommunitys()
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
</style>