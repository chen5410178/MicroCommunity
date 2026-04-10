<template>
  <el-dialog :title="$t('dataPrivilege.buildingUnit')" :visible.sync="visible" width="70%" top="5vh"
    @close="handleClose">
    <div class="search-bar">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="searchForm.floorNum" :placeholder="$t('dataPrivilege.inputBuildingNum')" clearable>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchForm.unitNum" :placeholder="$t('dataPrivilege.inputUnitNum')" clearable>
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
      <el-table-column prop="floorNum" :label="$t('dataPrivilege.buildingNum')" align="center">
      </el-table-column>
      <el-table-column prop="unitNum" :label="$t('dataPrivilege.unitNum')" align="center">
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit" :disabled="selectedUnits.length === 0">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listUnitsNotInDataPrivilege, saveDataPrivilegeUnit } from '@/api/org/dataPrivilegeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddDataPrivilegeUnit',
  data() {
    return {
      visible: false,
      dpId: '',
      searchForm: {
        floorNum: '',
        unitNum: ''
      },
      tableData: [],
      selectedUnits: [],
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
        floorNum: '',
        unitNum: ''
      }
      this.selectedUnits = []
    },
    async loadData() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          dpId: this.dpId,
          floorNum: this.searchForm.floorNum,
          unitNum: this.searchForm.unitNum,
          communityId: getCommunityId()
        }
        const { data, total } = await listUnitsNotInDataPrivilege(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load units:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        floorNum: '',
        unitNum: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    handleSelectionChange(val) {
      this.selectedUnits = val
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
        const units = this.selectedUnits.map(item => ({
          unitId: item.unitId,
          unitNum: item.unitNum,
          floorId: item.floorId,
          floorNum: item.floorNum
        }))

        await saveDataPrivilegeUnit({
          dpId: this.dpId,
          units,
          communityId: getCommunityId()
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to associate units:', error)
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