<template>
  <div class="community-container">
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>{{ $t('layout.home') }}</el-breadcrumb-item>
        <el-breadcrumb-item>{{ $t('layout.myCommunity') }}</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button type="primary" @click="handleAdd">{{ $t('community.addCommunity') }}</el-button>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item :label="$t('community.province')">
          <el-select v-model="searchForm.province" placeholder="请选择省份">
            <el-option label="北京市" value="北京市"></el-option>
            <!-- 其他省份选项 -->
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('community.city')">
          <el-select v-model="searchForm.city" placeholder="请选择城市">
            <el-option label="北京市" value="北京市"></el-option>
            <!-- 其他城市选项 -->
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('community.district')">
          <el-select v-model="searchForm.district" placeholder="请选择区/县">
            <el-option label="西城区" value="西城区"></el-option>
            <!-- 其他区县选项 -->
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="province"
          :label="$t('community.province')"
          width="120">
        </el-table-column>
        <el-table-column
          prop="city"
          :label="$t('community.city')"
          width="120">
        </el-table-column>
        <el-table-column
          prop="district"
          :label="$t('community.district')"
          width="120">
        </el-table-column>
        <el-table-column
          prop="communityName"
          :label="$t('community.communityName')"
          width="180">
        </el-table-column>
        <el-table-column
          prop="communityCode"
          :label="$t('community.communityCode')"
          width="180">
        </el-table-column>
        <el-table-column
          prop="phone"
          :label="$t('community.phone')"
          width="120">
        </el-table-column>
        <el-table-column
          prop="area"
          :label="$t('community.area')"
          width="100">
        </el-table-column>
        <el-table-column
          prop="startTime"
          :label="$t('community.startTime')"
          width="160">
        </el-table-column>
        <el-table-column
          prop="endTime"
          :label="$t('community.endTime')"
          width="160">
        </el-table-column>
        <el-table-column
          prop="status"
          :label="$t('community.status')"
          width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '入驻成功' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('common.operation')"
          fixed="right"
          width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button type="text" @click="handleView(scope.row)">{{ $t('common.view') }}</el-button>
            <el-button type="text" @click="handleDelete(scope.row)" class="delete-btn">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Community',
  data() {
    return {
      searchForm: {
        province: '',
        city: '',
        district: ''
      },
      tableData: [{
        province: '北京市',
        city: '北京市',
        district: '西城区',
        communityName: '阳光家园',
        communityCode: '202504125114067',
        phone: '13255555555',
        area: '100.00',
        startTime: '2025-04-12 11:53:35',
        endTime: '2026-04-12 11:53:35',
        status: '入驻成功'
      }],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    handleAdd() {
      // 处理添加小区
    },
    handleSearch() {
      // 处理搜索
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    handleReset() {
      this.searchForm = {
        province: '',
        city: '',
        district: ''
      }
    },
    handleEdit(row) {
      console.log('编辑', row)
    },
    handleView(row) {
      console.log('查看', row)
    },
    handleDelete(row) {
      this.$confirm(this.$t('common.deleteConfirm'), this.$t('common.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        console.log('删除', row)
        this.$message({
          type: 'success',
          message: this.$t('common.deleteSuccess')
        })
      }).catch(() => {})
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.handleSearch()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.handleSearch()
    }
  }
}
</script>

<style scoped>
.community-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.delete-btn {
  color: #F56C6C;
}

.delete-btn:hover {
  color: #f78989;
}
</style> 