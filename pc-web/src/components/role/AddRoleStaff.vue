<template>
  <el-dialog :title="$t('role.staff')" :visible.sync="visible" width="70%" @close="resetForm">
    <el-row>
      <el-col :span="24">
        <el-input v-model="searchForm.staffName" :placeholder="$t('role.staffName')"
          style="width: 300px; margin-right: 10px" />
        <el-button type="primary" @click="queryStaffs">
          {{ $t('common.search') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="staffs" style="width: 100%; margin-top: 15px" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="userId" :label="$t('role.staffId')" align="center" />
      <el-table-column prop="name" :label="$t('role.staffName')" align="center" />
      <el-table-column prop="address" :label="$t('role.address')" align="center" />
    </el-table>

    <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('role.cancel') }}</el-button>
      <el-button type="primary" @click="submit">{{ $t('role.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { listStaffsNoRole, saveRoleStaff } from '@/api/role/roleApi'

export default {
  name: 'AddRoleStaff',
  props: {
    roleId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      searchForm: {
        staffName: ''
      },
      staffs: [],
      selectedStaffs: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    show() {
      this.visible = true
      this.queryStaffs()
    },
    async queryStaffs() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          searchUserName: this.searchForm.staffName,
          roleId: this.roleId
        }
        const res = await listStaffsNoRole(params)
        this.staffs = res.data
        this.page.total = res.total
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSelectionChange(val) {
      this.selectedStaffs = val
    },
    async submit() {
      if (this.selectedStaffs.length === 0) {
        this.$message.warning(this.$t('role.selectStaff'))
        return
      }

      try {
        const staffs = this.selectedStaffs.map(item => ({
          staffId: item.userId,
          staffName: item.name
        }))

        await saveRoleStaff({
          roleId: this.roleId,
          staffs
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.queryStaffs()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.queryStaffs()
    },
    resetForm() {
      this.searchForm.staffName = ''
      this.selectedStaffs = []
    }
  }
}
</script>