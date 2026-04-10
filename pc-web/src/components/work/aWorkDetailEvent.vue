<template>
  <div class="a-work-detail-event">
    <el-row :gutter="20">
      <el-col :span="4" class="content-list">
        <div class="content-item" 
             v-for="(item,index) in contentList" 
             :key="index" 
             @click="handleContentClick(item)"
             :class="{ 'active': activeContentId === item.contentId }">
          问题{{ item.seqNum }}
        </div>
      </el-col>
      <el-col :span="20">
        <el-row :gutter="20" class="search-row">
          <el-col :span="6">
            <el-input v-model="searchForm.staffNameLike" :placeholder="$t('adminWorkDetail.processor')" clearable />
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="searchForm.queryStartTime"
              type="datetime"
              :placeholder="$t('adminWorkDetail.startTime')"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="searchForm.queryEndTime"
              type="datetime"
              :placeholder="$t('adminWorkDetail.endTime')"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
          </el-col>
        </el-row>

        <el-table :data="tableData" border style="width: 100%">
          <el-table-column prop="preStaffName" :label="$t('adminWorkDetail.preProcessor')" align="center" />
          <el-table-column prop="staffName" :label="$t('adminWorkDetail.processor')" align="center" />
          <el-table-column :label="$t('adminWorkDetail.taskPeriod')" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.startTime }}</div>
              <div>~{{ scope.row.endTime }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="remark" :label="$t('adminWorkDetail.remark')" align="center" />
          <el-table-column prop="createTime" :label="$t('adminWorkDetail.createTime')" align="center" />
        </el-table>

        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminWorkEvent } from '@/api/work/adminWorkDetailApi'

export default {
  name: 'AWorkDetailEvent',
  props: {
    workId: {
      type: String,
      required: true
    },
    contents: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      contentList: [],
      tableData: [],
      activeContentId: '',
      searchForm: {
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false
    }
  },
  watch: {
    contents: {
      immediate: true,
      handler(val) {
        this.contentList = val
        if (this.contentList.length > 0) {
          this.handleContentClick(this.contentList[0])
        }
      }
    }
  },
  methods: {
    handleContentClick(content) {
      this.activeContentId = content.contentId
      this.loadEventData()
    },
    loadEventData() {
      if (!this.activeContentId) return
      
      this.loading = true
      const params = {
        workId: this.workId,
        contentId: this.activeContentId,
        staffNameLike: this.searchForm.staffNameLike,
        queryStartTime: this.searchForm.queryStartTime,
        queryEndTime: this.searchForm.queryEndTime,
        page: this.pagination.current,
        row: this.pagination.size
      }

      listAdminWorkEvent(params)
        .then(response => {
          this.tableData = response.data
          this.pagination.total = response.total
        })
        .catch(error => {
          this.$message.error(this.$t('adminWorkDetail.fetchError'))
          console.error(error)
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadEventData()
    },
    handleReset() {
      this.searchForm = {
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadEventData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadEventData()
    },
    loadData() {
      if (this.contentList.length > 0) {
        this.handleContentClick(this.contentList[0])
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.a-work-detail-event {
  padding: 20px;
  
  .content-list {
    border-right: 1px solid #ebeef5;
    padding-right: 20px;
    
    .content-item {
      padding: 10px;
      margin-bottom: 10px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      cursor: pointer;
      text-align: center;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      &.active {
        background-color: #ecf5ff;
        border-color: #d9ecff;
      }
    }
  }
  
  .search-row {
    margin-bottom: 20px;
    
    .el-col {
      display: flex;
      align-items: center;
      
      .el-button {
        margin-left: 10px;
      }
    }
  }
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>