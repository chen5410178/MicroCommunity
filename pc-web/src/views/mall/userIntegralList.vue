<template>
  <div class="user-integral-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <el-card class="box-card">
          <div class="type-selector">
            <ul class="type-list">
              <li v-for="type in integralTypes" :key="type.value" class="type-item"
                :class="{ 'active': searchForm.objType === type.value }" @click="switchIntegralType(type.value)">
                {{ $t(`userIntegral.type.${type.label}`) }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('userIntegral.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form  text-left">
            <el-form-item :label="$t('userIntegral.search.integralName')">
              <el-input v-model="searchForm.integralName" :placeholder="$t('userIntegral.search.integralNamePlaceholder')"
                clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="clearfix  text-left">
            <span>{{ $t('userIntegral.list.title') }}</span>
            <el-button type="primary" size="mini" style="float: right;" @click="handleManage">
              {{ $t('common.manage') }}
            </el-button>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="integralId" :label="$t('userIntegral.table.id')" align="center" />
            <el-table-column prop="integralName" :label="$t('userIntegral.table.name')" align="center" />
            <el-table-column :label="$t('userIntegral.table.type')" align="center">
              <template slot-scope="scope">
                {{ scope.row.objType === '7007' ? $t('userIntegral.type.merchant') : $t('userIntegral.type.personal') }}
              </template>
            </el-table-column>
            <el-table-column prop="objId" :label="$t('userIntegral.table.userId')" align="center" />
            <el-table-column prop="link" :label="$t('userIntegral.table.phone')" align="center" />
            <el-table-column prop="amount" :label="$t('userIntegral.table.amount')" align="center" />
          </el-table>
          <pagination :current-page="pagination.current" :page-size="pagination.size" :total="pagination.total"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryIntegralList } from '@/api/mall/userIntegralApi'
import Pagination from '@/components/mall/Pagination'
import {jumpToMall} from '@/api/user/menuApi'

export default {
  name: 'UserIntegralList',
  components: {
    Pagination
  },
  data() {
    return {
      loading: false,
      integralTypes: [
        { value: '6006', label: 'personal' },
        { value: '7007', label: 'merchant' }
      ],
      searchForm: {
        integralName: '',
        objType: '6006',
        mallApiCode: 'queryIntegralBmoImpl'
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
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await queryIntegralList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('userIntegral.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleManage() {
      const path = this.searchForm.objType === '6006'
        ? '/#/pages/admin/userIntegral'
        : '/#/pages/admin/storeIntegral'
     // this.$router.push({ path })
      jumpToMall(path)
    },
    switchIntegralType(type) {
      this.searchForm.objType = type
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.user-integral-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .type-selector {
    .type-list {
      list-style: none;
      padding: 0;
      margin: 0;

      .type-item {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .search-form {
    margin-bottom: -18px;
  }
}
</style>