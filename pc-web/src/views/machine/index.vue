<template>
  <div class="machine-container">
    <div class="menu-group-wrapper">
      <div class="group-content">
        <ProjectTab @selectProject="handleSelectProject" />
      </div>
    </div>
    <div class="menu-list-wrapper">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('machine.searchCondition') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.machineCode"
          :placeholder="$t('machine.machineCode')"
          class="search-item"
        />
        <el-input
          v-model="searchForm.machineName"
          :placeholder="$t('machine.machineName')"
          class="search-item"
        />
        <el-input
          v-model="searchForm.location"
          :placeholder="$t('machine.location')"
          class="search-item"
        />
        <el-button type="primary" @click="handleSearch">{{ $t('machine.search') }}</el-button>
      </div>
    </div>

    <!-- 设备列表 -->
    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('machine.machineList') }}</div>
        <el-button type="primary" v-if="searchForm.projectId" @click="handleAdd">{{ $t('machine.machineAdd') }}</el-button>
      </div>
      
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="machineCode" :label="$t('machine.machineCode')" />
        <el-table-column prop="machineName" :label="$t('machine.machineName')" />
        <el-table-column prop="machineIp" :label="$t('machine.machineIp')"  />
        <el-table-column prop="machineMac" :label="$t('machine.machineMac')"  />
        <el-table-column prop="implBean" :label="$t('machine.implBean')"  />
        <el-table-column prop="heartbeatTime" :label="$t('machine.heartbeatTime')"  />
        <el-table-column prop="statusCd" :label="$t('machine.status')" >
          <template slot-scope="scope">
            <el-tag :type="scope.row.statusCd === '0' ? 'success' : 'danger'">
              {{ scope.row.statusCd === '0' ? $t('machine.inUse') : $t('machine.invalid') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('machine.remark')" />
        <el-table-column :label="$t('machine.operation')" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">{{ $t('machine.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('machine.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    </div>

    <!-- 添加对话框 -->
    <add-machine
      ref="addMachineRef"
      :visible.sync="addDialogVisible"
      @success="handleSuccess"
    />

    <!-- 编辑对话框 -->
    <edit-machine
      :visible.sync="editDialogVisible"
      :form-data="formData"
      @success="handleSuccess"
    />

    <!-- 删除对话框 -->
    <del-machine
      :visible.sync="delDialogVisible"
      :machine-id="delMachineId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getMachineList } from '@/api/machine/machineApi'
import AddMachine from '@/components/machine/AddMachine'
import EditMachine from '@/components/machine/EditMachine'
import DelMachine from '@/components/machine/DelMachine'
import ProjectTab from '@/components/project/ProjectTab'
export default {
  name: 'MachineManagement',
  components: {
    AddMachine,
    EditMachine,
    DelMachine,
    ProjectTab
  },
  data() {
    return {
      searchForm: {
        machineCode: '',
        machineName: '',
        location: '',
        projectId: ''
      },
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      addDialogVisible: false,
      editDialogVisible: false,
      delDialogVisible: false,
      formData: {},
      delMachineId: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleSearch() {
      this.currentPage = 1
      this.fetchData()
    },
    handleSelectProject(projectId) {
      this.searchForm.projectId = projectId
      this.fetchData()
    },
    async fetchData() {
      try {
        const res = await getMachineList({
          page: this.currentPage,
          row: this.pageSize,
          ...this.searchForm
        })
        this.tableData = res.data
        this.total = res.total
      } catch (error) {
        this.$message.error(this.$t('machine.fetchError'))
      }
    },
    handleAdd() {
      this.$refs.addMachineRef.initData(this.searchForm.projectId)
      this.addDialogVisible = true
    },
    handleEdit(row) {
      this.formData = { ...row }
      this.editDialogVisible = true
    },
    handleDelete(row) {
      this.delMachineId = row.machineId
      this.delDialogVisible = true
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchData()
    },
    handleSuccess() {
      this.fetchData()
    }
  }
}
</script>

<style scoped>
.machine-container {
  display: flex;
  padding:0px;
  margin:0px;
}
.menu-group-wrapper {
  width: 200px;
  height: 100%;
}
.menu-list-wrapper {
  flex: 1;
  padding-left: 10px;
}

.search-wrapper {
  background-color: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.search-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: left;
  color: #333;
}

.search-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.search-item {
  width: 200px;
}

.list-wrapper {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.list-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 