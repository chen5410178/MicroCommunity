<template>
  <div class="a-work-detail-cycle">
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="staffName" :label="$t('adminWorkDetail.processor')" align="center" />
      <el-table-column :label="$t('adminWorkDetail.workCycle')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.workCycle === '1001'">{{ $t('adminWorkDetail.once') }}</span>
          <span v-else>{{ $t('adminWorkDetail.periodic') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="hours" :label="$t('adminWorkDetail.completeHours')" align="center" />
      <el-table-column :label="$t('adminWorkDetail.cycle')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.period === '2020022'">{{ $t('adminWorkDetail.monthDay') }}</span>
          <span v-else>{{ $t('adminWorkDetail.weekly') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="periodMonth" :label="$t('adminWorkDetail.month')" align="center" />
      <el-table-column prop="periodDay" :label="$t('adminWorkDetail.day')" align="center" />
      <el-table-column prop="periodWorkday" :label="$t('adminWorkDetail.week')" align="center" />
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
  </div>
</template>

<script>
import { listAdminWorkCycle } from '@/api/work/adminWorkDetailApi'

export default {
  name: 'AWorkDetailCycle',
  props: {
    workId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false
    }
  },
  methods: {
    loadData() {
      this.loading = true
      const params = {
        workId: this.workId,
        page: this.pagination.current,
        row: this.pagination.size
      }

      listAdminWorkCycle(params)
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
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  },
}
</script>

<style lang="scss" scoped>
.a-work-detail-cycle {
  padding: 20px;
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>