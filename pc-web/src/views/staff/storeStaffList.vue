<template>
  <div class="store-staff-container">
    <el-row :gutter="20">
      <el-col :span="5" class="tree-container">
        <store-org-tree ref="storeOrgTree" @switchOrg="handleSwitchOrg" />
      </el-col>
      <el-col :span="19">
        <el-card class="search-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('storeStaff.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item :label="$t('storeStaff.search.staffName')">
              <el-input v-model.trim="searchForm.staffName" :placeholder="$t('storeStaff.search.staffNamePlaceholder')"
                clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="staff-list-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ orgName }}{{ $t('storeStaff.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="staffList" border style="width: 100%">
            <el-table-column prop="name" :label="$t('storeStaff.table.name')" align="center" />
            <el-table-column prop="tel" :label="$t('storeStaff.table.phone')" align="center" />
            <el-table-column prop="relCdName" :label="$t('storeStaff.table.position')" align="center" />
            <el-table-column prop="email" :label="$t('storeStaff.table.email')" align="center" />
            <el-table-column prop="address" :label="$t('storeStaff.table.address')" align="center" />
            <el-table-column prop="sex" :label="$t('storeStaff.table.gender')" align="center">
              <template slot-scope="scope">
                {{ scope.row.sex === 0 ? $t('storeStaff.gender.male') : $t('storeStaff.gender.female') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
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
  </div>
</template>

<script>
import { queryAdminPropertyStaff } from '@/api/staff/storeStaffApi'
import StoreOrgTree from '@/components/staff/storeOrgTree'

export default {
  name: 'StoreStaffList',
  components: {
    StoreOrgTree
  },
  data() {
    return {
      loading: false,
      orgName: '',
      searchForm: {
        orgId: '',
        staffName: ''
      },
      staffList: [],
      relCds: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await this.$store.dispatch('dict/getDict', 'u_org_staff_rel')
        this.relCds = data
      } catch (error) {
        console.error('Failed to get dict data:', error)
      }
    },
    async fetchStaffList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          orgId: this.searchForm.orgId,
          staffName: this.searchForm.staffName
        }
        const { data, total } = await queryAdminPropertyStaff(params)

        // Process data with dict
        const staffList = data.map(staff => {
          const relCdItem = this.relCds.find(item => item.statusCd === staff.relCd)
          return {
            ...staff,
            relCdName: relCdItem ? relCdItem.name : ''
          }
        })

        this.staffList = staffList
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('storeStaff.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSwitchOrg({ orgId, orgName }) {
      this.searchForm.orgId = orgId
      this.orgName = orgName
      this.pagination.current = 1
      this.fetchStaffList()
    },
    handleSearch() {
      this.pagination.current = 1
      this.fetchStaffList()
    },
    handleDetail(staff) {
      this.$router.push(`/views/staff/adminStaffDetail?staffId=${staff.userId}`)
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchStaffList()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchStaffList()
    }
  }
}
</script>

<style lang="scss" scoped>
.store-staff-container {
  padding: 20px;

  .tree-container {
    padding-right: 0;
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      margin-top: 10px;
      text-align: left;
    }
  }

  .staff-list-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 15px;
    text-align: right;
  }
}
</style>