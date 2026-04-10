<template>
  <div class="user-detail-staff-container">
    <el-table :data="staffs" border style="width: 100%">
      <el-table-column prop="name" :label="$t('userDetailStaff.name')" align="center"></el-table-column>
      <el-table-column prop="tel" :label="$t('userDetailStaff.tel')" align="center"></el-table-column>
      <el-table-column prop="storeName" :label="$t('userDetailStaff.storeName')" align="center"></el-table-column>
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
import { listAdminStoreUser } from '@/api/staff/systemUserDetailApi'

export default {
  name: 'AUserDetailStaff',
  props: {
    userId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      staffs: [],
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
          staffId: this.userId
        }
        const res = await listAdminStoreUser(params)
        if (res.code === 0) {
          this.staffs = res.data
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
.user-detail-staff-container {
  margin-top: 20px;
}
</style>