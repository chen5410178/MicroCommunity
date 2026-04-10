<template>
  <el-dialog :title="$t('org.relatedStaff')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="18" :offset="12">
        <el-input v-model="queryParams.staffName" :placeholder="$t('org.name')" clearable style="width: 300px">
          <el-button slot="append" icon="el-icon-search" @click="handleQuery" />
        </el-input>
      </el-col>
    </el-row>

    <el-table ref="multipleTable" :data="staffList" style="width: 100%; margin-top: 20px"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" :label="$t('org.name')" align="center" />
      <el-table-column prop="tel" :label="$t('org.phone')" align="center" />
      <el-table-column prop="userId" :label="$t('org.staffId')" align="center" />
    </el-table>
    <el-pagination v-show="total > 0" :total="total" :current-page.sync="queryParams.page"
      :page-size.sync="queryParams.rows" @current-change="getList" @size-change="getList"
      layout="total, sizes, prev, pager, next, jumper" />


    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('org.close') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('org.submit') }}</el-button>
    </span>
  </el-dialog>
</template>
  
<script>
import { listStaffsNoInOrg, saveStaffOrgRel } from '@/api/org/orgApi'

export default {
  name: 'OrgRelStaff',
  data() {
    return {
      visible: false,
      queryParams: {
        page: 1,
        row: 10,
        staffName: ''
      },
      orgId: '',
      staffList: [],
      total: 0,
      selectedStaffs: []
    }
  },
  methods: {
    show(_orgId) {
      this.visible = true
      this.orgId = _orgId
      this.getList()
    },
    getList() {
      listStaffsNoInOrg({
          ...this.queryParams,
          orgId: this.orgId
      }).then(response => {
        this.staffList = response.data
        this.total = response.total
      })
    },
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    handleReset() {
      this.queryParams.staffName = ''
      this.getList()
    },
    handleSelectionChange(val) {
      this.selectedStaffs = val.map(item => item.userId)
    },
    handleSubmit() {
      if (this.selectedStaffs.length === 0) {
        this.$message.warning(this.$t('org.selectStaffFirst'))
        return
      }
      saveStaffOrgRel({
        orgId: this.orgId,
        staffIds: this.selectedStaffs.join(',')
      }).then(response => {
        console.log(response)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      })
    },
    handleClose() {
      this.queryParams = {
        page: 1,
        rows: 10,
        staffName: ''
      }
      this.$refs.multipleTable.clearSelection()
    }
  }
}
</script>