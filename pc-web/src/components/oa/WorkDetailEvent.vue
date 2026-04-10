<template>
  <div class="work-detail-event">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="tree-container">
          <el-scrollbar>
            <ul class="content-list">
              <li
                v-for="(item,index) in workDetailEventInfo.contents"
                :key="index"
                @click="swatchEventContentId(item)"
                :class="{'active-content': workDetailEventInfo.contentId === item.contentId}"
              >
                问题{{ item.seqNum }}
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="20">
        <el-row :gutter="20" class="margin-top-lg">
          <el-col :span="6">
            <el-input
              v-model.trim="workDetailEventInfo.staffNameLike"
              :placeholder="$t('workDetailEvent.staffNamePlaceholder')"
              clearable
            />
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="workDetailEventInfo.queryStartTime"
              type="datetime"
              :placeholder="$t('workDetailEvent.startTimePlaceholder')"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="workDetailEventInfo.queryEndTime"
              type="datetime"
              :placeholder="$t('workDetailEvent.endTimePlaceholder')"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="queryWorkDetailEvent">
              <i class="el-icon-search"></i>{{ $t('common.search') }}
            </el-button>
            <el-button @click="resetWorkDetailEvent">
              <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
            </el-button>
          </el-col>
        </el-row>

        <div class="margin-top">
          <el-table
            :data="workDetailEventInfo.events"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column
              prop="preStaffName"
              :label="$t('workDetailEvent.preStaffName')"
              align="center"
            />
            <el-table-column
              prop="staffName"
              :label="$t('workDetailEvent.staffName')"
              align="center"
            />
            <el-table-column
              :label="$t('workDetailEvent.taskValidPeriod')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column
              prop="remark"
              :label="$t('workDetailEvent.remark')"
              align="center"
            />
            <el-table-column
              prop="createTime"
              :label="$t('workDetailEvent.createTime')"
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
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listWorkEvent } from '@/api/oa/workDetailApi'

export default {
  name: 'WorkDetailEvent',
  data() {
    return {
      workDetailEventInfo: {
        events: [],
        contents: [],
        workId: '',
        contentId: '',
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      },
      loading: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.workDetailEventInfo.workId = params.workId
      this.workDetailEventInfo.contents = params.contents
      if (params.contents.length > 0) {
        this.swatchEventContentId(params.contents[0])
      }
    },
    async loadWorkDetailEventData(page, size) {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailEventInfo.workId,
          contentId: this.workDetailEventInfo.contentId,
          staffNameLike: this.workDetailEventInfo.staffNameLike,
          queryStartTime: this.workDetailEventInfo.queryStartTime,
          queryEndTime: this.workDetailEventInfo.queryEndTime,
          page,
          row: size
        }
        const { data, total } = await listWorkEvent(params)
        this.workDetailEventInfo.events = data
        this.page.total = total
      } catch (error) {
        console.error('获取工作单流转列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryWorkDetailEvent() {
      this.page.current = 1
      this.loadWorkDetailEventData(this.page.current, this.page.size)
    },
    swatchEventContentId(content) {
      this.workDetailEventInfo.contentId = content.contentId
      this.loadWorkDetailEventData(this.page.current, this.page.size)
    },
    resetWorkDetailEvent() {
      this.workDetailEventInfo.staffNameLike = ''
      this.workDetailEventInfo.queryStartTime = ''
      this.workDetailEventInfo.queryEndTime = ''
      this.queryWorkDetailEvent()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadWorkDetailEventData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadWorkDetailEventData(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.tree-container {
  height: 500px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}

.content-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.content-list li {
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
}

.content-list li:hover {
  background-color: #f5f7fa;
}

.active-content {
  background-color: #ecf5ff;
  color: #409eff;
  border: 1px solid #d9ecff;
}

.margin-top-lg {
  margin-top: 20px;
}
.margin-top {
  margin-top: 20px;
}
</style>