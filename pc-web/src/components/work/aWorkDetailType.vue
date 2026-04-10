<template>
  <div class="a-work-detail-type">
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="typeName" :label="$t('adminWorkDetail.typeName')" align="center" />
      <el-table-column :label="$t('adminWorkDetail.notifyType')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.smsWay === 'WECHAT'">{{ $t('adminWorkDetail.wechat') }}</span>
          <span v-else-if="scope.row.smsWay === 'ALI_SMS'">{{ $t('adminWorkDetail.sms') }}</span>
          <span v-else>{{ $t('adminWorkDetail.unknown') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('adminWorkDetail.createTime')" align="center" />
      <el-table-column prop="remark" :label="$t('adminWorkDetail.remark')" align="center" />
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
import { listAdminWorkType } from '@/api/work/adminWorkDetailApi'

export default {
  name: 'AWorkDetailType',
  props: {
    wtId: {
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
        wtId: this.wtId,
        page: this.pagination.current,
        row: this.pagination.size
      }

      listAdminWorkType(params)
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
.a-work-detail-type {
  padding: 20px;
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>