<template>
  <div class="owner-committee-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('ownerCommitteeManage.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('ownerCommitteeManage.namePlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.link" :placeholder="$t('ownerCommitteeManage.phonePlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('ownerCommitteeManage.statePlaceholder')"
            style="width:100%">
            <el-option :label="$t('ownerCommitteeManage.onDuty')" value="1000" />
            <el-option :label="$t('ownerCommitteeManage.resigned')" value="2000" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 业委会列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('ownerCommitteeManage.listTitle') }}</span>
        <el-button type="primary" icon="el-icon-plus" size="small"  @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="ocId" :label="$t('ownerCommitteeManage.id')" align="center" />
        <el-table-column prop="name" :label="$t('ownerCommitteeManage.name')" align="center" />
        <el-table-column prop="sex" :label="$t('ownerCommitteeManage.gender')" align="center">
          <template slot-scope="scope">
            {{ scope.row.sex === 'B' ? $t('common.male') : $t('common.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="link" :label="$t('ownerCommitteeManage.phone')" align="center" />
        <el-table-column prop="idCard" :label="$t('ownerCommitteeManage.idCard')" align="center" />
        <el-table-column prop="address" :label="$t('ownerCommitteeManage.address')" align="center" />
        <el-table-column prop="position" :label="$t('ownerCommitteeManage.position')" align="center" />
        <el-table-column prop="post" :label="$t('ownerCommitteeManage.post')" align="center" />
        <el-table-column prop="appointTime" :label="$t('ownerCommitteeManage.appointTime')" align="center" />
        <el-table-column prop="curTime" :label="$t('ownerCommitteeManage.term')" align="center" />
        <el-table-column prop="state" :label="$t('ownerCommitteeManage.status')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === '1000' ? $t('ownerCommitteeManage.onDuty') : $t('ownerCommitteeManage.resigned') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
            <el-button size="mini" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" @size-change="handleSizeChange"
        @current-change="handlePageChange" />
    </el-card>

    <!-- 删除对话框 -->
    <delete-owner-committee ref="deleteDialog" @success="handleDeleteSuccess" />
  </div>
</template>

<script>
import { listOwnerCommittee } from '@/api/owner/ownerCommitteeManageApi'
import DeleteOwnerCommittee from '@/components/owner/DeleteOwnerCommittee'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerCommitteeManageList',
  components: {
    DeleteOwnerCommittee
  },
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        link: '',
        state: '',
        communityId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await listOwnerCommittee(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('ownerCommitteeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        name: '',
        link: '',
        state: '',
        communityId: getCommunityId()
      }
      this.pagination.current = 1
      this.getList()
    },
    handleAdd() {
      this.$router.push({ path: '/views/owner/addOwnerCommittee' })
    },
    handleEdit(row) {
      this.$router.push({ path: `/views/owner/editOwnerCommittee?ocId=${row.ocId}` })
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleDetail(row) {
      this.$router.push({ path: `/views/owner/ownerCommitteeDetail?ocId=${row.ocId}` })
    },
    handleDeleteSuccess() {
      this.getList()
      this.$message.success(this.$t('common.operationSuccess'))
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handlePageChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.owner-committee-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -10px;
    }

    .el-col {
      padding-bottom: 10px;
    }
  }

  .list-wrapper {
    .el-table {
      margin-top: 15px;
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>