<template>
  <div class="owner-dining-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('ownerDining.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-date-picker
            v-model="searchForm.startDate"
            type="date"
            :placeholder="$t('ownerDining.search.startDate')"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
        </el-col>
        <el-col :span="6">
          <el-date-picker
            v-model="searchForm.endDate"
            type="date"
            :placeholder="$t('ownerDining.search.endDate')"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="searchForm.nameLike"
            :placeholder="$t('ownerDining.search.nameLike')"
            clearable
            style="width: 100%"
          />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 15px">
        <el-col :span="6">
          <el-input
            v-model="searchForm.link"
            :placeholder="$t('ownerDining.search.link')"
            clearable
            style="width: 100%"
          />
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('ownerDining.list.title') }}</span>
        <div style="float: right">
          <el-button type="primary" size="small" @click="handleExport">
            {{ $t('common.export') }}
          </el-button>
          <el-button type="default" size="small" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="startDate"
          :label="$t('ownerDining.table.startDate')"
          align="center"
        />
        <el-table-column
          prop="endDate"
          :label="$t('ownerDining.table.endDate')"
          align="center"
        />
        <el-table-column
          prop="name"
          :label="$t('ownerDining.table.name')"
          align="center"
        />
        <el-table-column
          prop="link"
          :label="$t('ownerDining.table.link')"
          align="center"
        />
        <el-table-column
          prop="goodsName"
          :label="$t('ownerDining.table.goodsName')"
          align="center"
        />
        <el-table-column
          prop="frequency"
          :label="$t('ownerDining.table.frequency')"
          align="center"
        />
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script>
import { queryOwnerReserveGoods, exportData } from '@/api/scm/ownerDiningApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDiningList',
  data() {
    return {
      loading: false,
      searchForm: {
        startDate: '',
        endDate: '',
        nameLike: '',
        link: '',
        communityId: ''
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
    this.initDate()
    this.searchForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    initDate() {
      const date = new Date()
      const month = date.getMonth() + 1
      const newMonth = month < 10 ? `0${month}` : month
      
      this.searchForm.startDate = `${date.getFullYear()}-${newMonth}-01`
      
      date.setMonth(date.getMonth() + 1)
      const nextMonth = date.getMonth() + 1
      const newNextMonth = nextMonth < 10 ? `0${nextMonth}` : nextMonth
      this.searchForm.endDate = `${date.getFullYear()}-${newNextMonth}-01`
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryOwnerReserveGoods(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('ownerDining.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    async handleExport() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          pagePath: 'ownerDining'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        this.$message.error(this.$t('ownerDining.exportError'))
      } finally {
        this.loading = false
      }
    },
    goBack() {
      this.$router.go(-1)
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
.owner-dining-container {
  padding: 20px;
  
  .search-wrapper {
    margin-bottom: 20px;
  }
  
  .list-wrapper {
    margin-top: 20px;
  }
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>