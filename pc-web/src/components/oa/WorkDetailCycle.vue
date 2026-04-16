<template>
  <div class="work-detail-cycle">
    <div class="margin-top">
      <el-table
        :data="workDetailCycleInfo.cycles"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="staffName"
          :label="$t('workDetailCycle.staffName')"
          align="center"
        />
        <el-table-column
          :label="$t('workDetailCycle.workCycle')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.workCycle === '1001' ? $t('workDetailCycle.once') : $t('workDetailCycle.periodic') }}
          </template>
        </el-table-column>
        <el-table-column
          prop="hours"
          :label="$t('workDetailCycle.hours')"
          align="center"
        />
        <el-table-column
          :label="$t('workDetailCycle.period')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.period === '2020022' ? $t('workDetailCycle.monthDay') : $t('workDetailCycle.weekly') }}
          </template>
        </el-table-column>
        <el-table-column
          prop="periodMonth"
          :label="$t('workDetailCycle.month')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.periodMonth || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="periodDay"
          :label="$t('workDetailCycle.day')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.periodDay || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="periodWorkday"
          :label="$t('workDetailCycle.week')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.periodWorkday || '-' }}
          </template>
        </el-table-column>
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
import { listWorkCycle } from '@/api/oa/workDetailApi'

export default {
  name: 'WorkDetailCycle',
  data() {
    return {
      workDetailCycleInfo: {
        cycles: [],
        workId: ''
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
      this.workDetailCycleInfo.workId = params.workId
      this.loadWorkDetailCycleData(this.page.current, this.page.size)
    },
    async loadWorkDetailCycleData(page, size) {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailCycleInfo.workId,
          page,
          row: size
        }
        const { data, total } = await listWorkCycle(params)
        this.workDetailCycleInfo.cycles = data
        this.page.total = total
      } catch (error) {
        console.error('获取工作单周期列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadWorkDetailCycleData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadWorkDetailCycleData(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>