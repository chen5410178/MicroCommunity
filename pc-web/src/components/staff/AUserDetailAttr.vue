<template>
  <div class="user-detail-attr-container">
    <el-table :data="attrs" border style="width: 100%">
      <el-table-column prop="specCd" :label="$t('userDetailAttr.specCd')" align="center"></el-table-column>
      <el-table-column prop="specCdName" :label="$t('userDetailAttr.specName')" align="center"></el-table-column>
      <el-table-column prop="value" :label="$t('userDetailAttr.value')" align="center"></el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { listSystemUserAttr } from '@/api/staff/systemUserDetailApi'

export default {
  name: 'AUserDetailAttr',
  props: {
    userId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      attrs: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    async loadData() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          systemUserId: this.userId
        }
        const res = await listSystemUserAttr(params)
        if (res.code === 0) {
          this.attrs = res.data
          this.page.total = res.total
        }
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.user-detail-attr-container {
  margin-top: 20px;
}
</style>