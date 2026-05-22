<template>
  <div class="mqtt-log-container">
    <!-- 查询条件 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('mqttLog.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item :label="$t('mqttLog.search.startTime')">
              <el-date-picker
                v-model="searchForm.startTime"
                type="datetime"
                :placeholder="$t('mqttLog.search.startTimePlaceholder')"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
            <el-form-item :label="$t('mqttLog.search.endTime')">
              <el-date-picker
                v-model="searchForm.endTime"
                type="datetime"
                :placeholder="$t('mqttLog.search.endTimePlaceholder')"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 日志列表 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('mqttLog.list.title') }}</span>
          </div>
          <el-table
            v-loading="loading"
            :data="tableData"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="mqttId"
              :label="$t('mqttLog.table.id')"
              align="center"
            />
            <el-table-column
              prop="businessId"
              :label="$t('mqttLog.table.businessId')"
              align="center"
            />
            <el-table-column
              prop="businessKey"
              :label="$t('mqttLog.table.business')"
              align="center"
            />
            <el-table-column
              prop="topic"
              :label="$t('mqttLog.table.topic')"
              align="center"
            />
            <el-table-column
              prop="context"
              :label="$t('mqttLog.table.content')"
              align="center"
            >
              <template slot-scope="scope">
                <el-tooltip :content="scope.row.context" placement="top">
                  <div class="text-ellipsis">{{ scope.row.context }}</div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column
              prop="remark"
              :label="$t('mqttLog.table.remark')"
              align="center"
            />
          </el-table>

          <el-pagination
            :current-page.sync="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listMqttLog } from '@/api/dev/mqttLogApi'

export default {
  name: 'MqttLogList',
  data() {
    return {
      loading: false,
      searchForm: {
        startTime: '',
        endTime: ''
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
          startTime: this.searchForm.startTime,
          endTime: this.searchForm.endTime
        }
        const { data, total } = await listMqttLog(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('mqttLog.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
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
.mqtt-log-container {
  padding: 20px;

  .search-form {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
  }

  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 200px;
  }
}
</style>