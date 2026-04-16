<template>
  <div class="admin-auth-owner-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminAuthOwner.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="searchForm.state" :placeholder="$t('adminAuthOwner.search.state')" clearable
                class="filter-item">
                <el-option v-for="item in statusOptions" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.appUserName" :placeholder="$t('adminAuthOwner.search.appUserName')"
                clearable class="filter-item" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.idCard" :placeholder="$t('adminAuthOwner.search.idCard')" clearable
                class="filter-item" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.link" :placeholder="$t('adminAuthOwner.search.link')" clearable
                class="filter-item" />
            </el-col>

            <el-col :span="4" >
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminAuthOwner.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="communityName" :label="$t('adminAuthOwner.table.communityName')" align="center" />
            <el-table-column :label="$t('adminAuthOwner.table.appUserName')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.appUserName }}</div>
                <div v-if="scope.row.ownerId">({{ scope.row.ownerId }})</div>
              </template>
            </el-table-column>
            <el-table-column prop="link" :label="$t('adminAuthOwner.table.link')" align="center" />
            <el-table-column prop="roomName" :label="$t('adminAuthOwner.table.roomName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.roomName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="ownerTypeCdName" :label="$t('adminAuthOwner.table.ownerTypeCdName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.ownerTypeCdName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="idCard" :label="$t('adminAuthOwner.table.idCard')" align="center">
              <template slot-scope="scope">
                {{ scope.row.idCard || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('adminAuthOwner.table.stateName')" align="center" />
            <el-table-column prop="remark" :label="$t('adminAuthOwner.table.remark')" align="center">
              <template slot-scope="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminAuthOwner.table.createTime')" align="center" />
            <el-table-column prop="appTypeName" :label="$t('adminAuthOwner.table.appTypeName')" align="center" />
          </el-table>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" style="margin-top: 15px;"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminAppUserOwners } from '@/api/owner/adminAuthOwnerApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'

export default {
  name: 'AdminAuthOwnerList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        appUserName: '',
        idCard: '',
        link: '',
        state: '',
        communityId: ''
      },
      statusOptions: [
        { name: this.$t('adminAuthOwner.status.all'), statusCd: '' },
        { name: this.$t('adminAuthOwner.status.pending'), statusCd: '10000' },
        { name: this.$t('adminAuthOwner.status.success'), statusCd: '12000' },
        { name: this.$t('adminAuthOwner.status.failed'), statusCd: '13000' }
      ],
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
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
        const { data, total } = await listAdminAppUserOwners(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminAuthOwner.fetchError'))
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
        appUserName: '',
        idCard: '',
        link: '',
        state: '',
        communityId: this.searchForm.communityId
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.handleSearch()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-auth-owner-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .filter-item {
    width: 100%;
  }
}
</style>