<template>
  <el-dialog :title="$t('dataPrivilege.staff')" :visible.sync="visible" width="70%" top="5vh" @close="handleClose">
    <div class="search-bar">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="searchForm.staffId" :placeholder="$t('dataPrivilege.inputStaffId')" clearable>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchForm.staffName" :placeholder="$t('dataPrivilege.inputStaffName')" clearable>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>{{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table :data="tableData" border style="width: 100%; margin-top: 15px" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center">
      </el-table-column>
      <el-table-column prop="userId" :label="$t('dataPrivilege.staffId')" align="center">
      </el-table-column>
      <el-table-column prop="name" :label="$t('dataPrivilege.staffName')" align="center">
      </el-table-column>
      <el-table-column prop="address" :label="$t('dataPrivilege.staffAddress')" align="center">
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit" :disabled="selectedStaffs.length === 0">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listStaffNotInDataPrivilege, saveDataPrivilegeStaff } from '@/api/org/dataPrivilegeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddDataPrivilegeStaff',
  data() {
    return {
      visible: false,
      dpId: '',
      searchForm: {
        staffId: '',
        staffName: ''
      },
      tableData: [],
      selectedStaffs: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.dpId = params.dpId
      this.visible = true
      this.loadData()
    },
    handleClose() {
      this.searchForm = {
        staffId: '',
        staffName: ''
      }
      this.selectedStaffs = []
    },
    async loadData() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          dpId: this.dpId,
          staffId: this.searchForm.staffId,
          staffName: this.searchForm.staffName,
          communityId: getCommunityId()
        }
        const { data, total } = await listStaffNotInDataPrivilege(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load staffs:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        staffId: '',
        staffName: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    handleSelectionChange(val) {
      this.selectedStaffs = val
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    async handleSubmit() {
      try {
        const staffs = this.selectedStaffs.map(item => ({
          staffId: item.userId,
          staffName: item.name
        }))

        await saveDataPrivilegeStaff({
          dpId: this.dpId,
          staffs,
          communityId: getCommunityId()
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to associate staffs:', error)
      }
    }
  }
}
</script>

<style scoped>
.search-bar {
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}
</style>