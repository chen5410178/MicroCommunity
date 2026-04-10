<template>
  <el-dialog :title="$t('addAStaffCommunity.community')" :visible.sync="visible" width="60%" :before-close="handleClose">
    <el-card>
      <el-row>
        <el-col :span="12">
          <el-input v-model="searchForm.communityName" :placeholder="$t('addAStaffCommunity.searchPlaceholder')" clearable
            @keyup.enter.native="searchCommunities">
            <el-button slot="append" icon="el-icon-search" @click="searchCommunities" />
          </el-input>
        </el-col>
      </el-row>

      <el-table class="margin-top" :data="communities" border style="width: 100%">
        <el-table-column width="55">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked" @change="handleCheckChange(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column prop="communityId" :label="$t('addAStaffCommunity.communityId')" align="center" />
        <el-table-column prop="name" :label="$t('addAStaffCommunity.communityName')" align="center" />
        <el-table-column prop="address" :label="$t('addAStaffCommunity.communityAddress')" align="center" />
      </el-table>

      <el-pagination class="margin-top" :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('addAStaffCommunity.cancel') }}</el-button>
      <el-button type="primary" @click="addStaffCommunity">{{ $t('addAStaffCommunity.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listWaitStaffCommunities, saveStaffCommunity } from '@/api/staff/aStaffDetailApi'

export default {
  name: 'AddAStaffCommunity',
  data() {
    return {
      visible: false,
      searchForm: {
        communityName: ''
      },
      communities: [],
      selectedCommunities: [],
      staffId: '',
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.staffId = params.staffId
      this.visible = true
      this.loadCommunities()
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.searchForm.communityName = ''
      this.communities = []
      this.selectedCommunities = []
      this.pagination.current = 1
      this.pagination.total = 0
    },
    async loadCommunities() {
      try {
        const response = await listWaitStaffCommunities({
          staffId: this.staffId,
          page: this.pagination.current,
          row: this.pagination.size,
          nameLike: this.searchForm.communityName
        })
        this.communities = (response.data || []).map(item => {
          return {
            ...item,
            checked: false
          }
        })
        this.pagination.total = response.total || 0
      } catch (error) {
        this.$message.error(this.$t('addAStaffCommunity.loadError'))
      }
    },
    searchCommunities() {
      this.pagination.current = 1
      this.loadCommunities()
    },
    handleCheckChange(row) {
      if (row.checked) {
        this.selectedCommunities.push({
          communityId: row.communityId,
          communityName: row.name
        })
      } else {
        this.selectedCommunities = this.selectedCommunities.filter(
          item => item.communityId !== row.communityId
        )
      }
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadCommunities()
    },
    handleCurrentChange(page) {
      this.pagination.current = page
      this.loadCommunities()
    },
    async addStaffCommunity() {
      if (this.selectedCommunities.length === 0) {
        this.$message.warning(this.$t('addAStaffCommunity.selectCommunity'))
        return
      }

      try {
        await saveStaffCommunity({
          staffId: this.staffId,
          communitys: this.selectedCommunities
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('addAStaffCommunity.addError'))
      }
    }
  }
}
</script>

