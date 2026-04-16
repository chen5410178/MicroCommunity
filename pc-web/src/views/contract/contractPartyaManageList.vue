<template>
  <div class="contract-partya-manage-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('contractPartyaManage.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form text-left">
            <el-form-item>
              <el-input v-model="searchForm.partyA" :placeholder="$t('contractPartyaManage.search.partyA')" clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="searchForm.aContacts" :placeholder="$t('contractPartyaManage.search.aContacts')"
                clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="searchForm.aLink" :placeholder="$t('contractPartyaManage.search.aLink')" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" >
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('contractPartyaManage.list.title') }}</span>
            <el-button type="primary" size="small" style="float: right;" @click="handleAdd">
              {{ $t('common.add') }}
            </el-button>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="partyA" :label="$t('contractPartyaManage.table.partyA')" align="center" />
            <el-table-column prop="aContacts" :label="$t('contractPartyaManage.table.aContacts')" align="center" />
            <el-table-column prop="aLink" :label="$t('contractPartyaManage.table.aLink')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" style="margin-top: 20px;"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <add-contract-partya ref="addContractPartya" @success="handleSuccess" />
    <edit-contract-partya ref="editContractPartya" @success="handleSuccess" />
    <delete-contract-partya ref="deleteContractPartya" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryContractPartya } from '@/api/contract/contractPartyaManageApi'
import AddContractPartya from '@/components/contract/addContractPartya'
import EditContractPartya from '@/components/contract/editContractPartya'
import DeleteContractPartya from '@/components/contract/deleteContractPartya'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractPartyaManageList',
  components: {
    AddContractPartya,
    EditContractPartya,
    DeleteContractPartya
  },
  data() {
    return {
      loading: false,
      searchForm: {
        partyA: '',
        aContacts: '',
        aLink: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          partyA: this.searchForm.partyA,
          aContacts: this.searchForm.aContacts,
          aLink: this.searchForm.aLink,
          communityId: this.communityId
        }
        const { data, total } = await queryContractPartya(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('contractPartyaManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        partyA: '',
        aContacts: '',
        aLink: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addContractPartya.open()
    },
    handleEdit(row) {
      this.$refs.editContractPartya.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteContractPartya.open(row)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-partya-manage-container {
  padding: 20px;

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }

  .el-card {
    margin-bottom: 20px;
  }
}
</style>