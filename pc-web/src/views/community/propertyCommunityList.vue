<template>
  <div class="property-community-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('propertyCommunity.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.communityId" :placeholder="$t('propertyCommunity.search.communityId')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="searchForm.name" :placeholder="$t('propertyCommunity.search.name')" clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 小区信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('propertyCommunity.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">
            <i class="el-icon-plus"></i>
            {{ $t('propertyCommunity.list.add') }}
          </el-button>
          <el-button size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="communityId" :label="$t('propertyCommunity.table.communityId')" align="center" />
        <el-table-column prop="name" :label="$t('propertyCommunity.table.name')" align="center" />
        <el-table-column prop="nearbyLandmarks" :label="$t('propertyCommunity.table.nearbyLandmarks')" align="center" />
        <el-table-column prop="cityName" :label="$t('propertyCommunity.table.cityCode')" align="center" />
        <el-table-column prop="stateName" :label="$t('propertyCommunity.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleDelete(scope.row)">
              {{ $t('propertyCommunity.list.quit') }}
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-property-community ref="addDialog" @success="handleSuccess" />
    <edit-property-community ref="editDialog" @success="handleSuccess" />
    <delete-property-community ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import {
  listCommunitys,
} from '@/api/community/propertyCommunityApi'
import AddPropertyCommunity from '@/components/community/AddPropertyCommunity'
import EditPropertyCommunity from '@/components/community/EditPropertyCommunity'
import DeletePropertyCommunity from '@/components/community/DeletePropertyCommunity'

export default {
  name: 'PropertyCommunityList',
  components: {
    AddPropertyCommunity,
    EditPropertyCommunity,
    DeletePropertyCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        name: '',
        memberId: this.$route.query.storeId || ''
      },
      tableData: [],
      page: {
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
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await listCommunitys(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('propertyCommunity.fetchError'))
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
        communityId: '',
        name: '',
        memberId: this.$route.query.storeId || ''
      }
      this.getList()
    },
    handleAdd() {
      this.$refs.addDialog.open({
        storeId: this.searchForm.memberId
      })
    },
    handleEdit(row) {
      this.$refs.editDialog.open({
        ...row,
        storeId: this.searchForm.memberId
      })
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open({
        ...row,
        memberId: this.searchForm.memberId
      })
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
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.property-community-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>