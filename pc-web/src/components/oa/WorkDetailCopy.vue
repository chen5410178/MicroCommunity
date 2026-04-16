<template>
  <div class="work-detail-copy">
    <el-row :gutter="20" class="margin-top-lg">
      <el-col :span="6">
        <el-input
          v-model.trim="workDetailCopyInfo.staffNameLike"
          :placeholder="$t('workDetailCopy.staffNamePlaceholder')"
          clearable
        />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="queryWorkDetailCopy">
          <i class="el-icon-search"></i>{{ $t('common.search') }}
        </el-button>
        <el-button @click="resetWorkDetailCopy">
          <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table
        :data="workDetailCopyInfo.copys"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="copyId"
          :label="$t('workDetailCopy.copyId')"
          align="center"
        />
        <el-table-column
          prop="staffName"
          :label="$t('workDetailCopy.staffName')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('workDetailCopy.createTime')"
          align="center"
        />
        <el-table-column
          prop="stateName"
          :label="$t('workDetailCopy.stateName')"
          align="center"
        />
        <el-table-column
          prop="remark"
          :label="$t('workDetailCopy.remark')"
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
  </div>
</template>

<script>
import { listWorkCopy } from '@/api/oa/workDetailApi'

export default {
  name: 'WorkDetailCopy',
  data() {
    return {
      workDetailCopyInfo: {
        copys: [],
        workId: '',
        staffNameLike: ''
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
      this.workDetailCopyInfo.workId = params.workId
      this.loadWorkDetailCopyData(this.page.current, this.page.size)
    },
    async loadWorkDetailCopyData(page, size) {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailCopyInfo.workId,
          staffNameLike: this.workDetailCopyInfo.staffNameLike,
          page,
          row: size
        }
        const { data, total } = await listWorkCopy(params)
        this.workDetailCopyInfo.copys = data
        this.page.total = total
      } catch (error) {
        console.error('获取抄送人列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryWorkDetailCopy() {
      this.page.current = 1
      this.loadWorkDetailCopyData(this.page.current, this.page.size)
    },
    resetWorkDetailCopy() {
      this.workDetailCopyInfo.staffNameLike = ''
      this.queryWorkDetailCopy()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadWorkDetailCopyData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadWorkDetailCopyData(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.margin-top-lg {
  margin-top: 20px;
}
.margin-top {
  margin-top: 20px;
}
</style>