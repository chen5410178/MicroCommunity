<template>
  <el-dialog :title="$t('chooseMaintainanceStandardItem.title')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="24">

          <el-row :gutter="20">
            <el-col :span="12">
              <el-input v-model="form.itemTitle" :placeholder="$t('chooseMaintainanceStandardItem.searchPlaceholder')"
                clearable @keyup.enter.native="queryItems">
              </el-input>
            </el-col>
            <el-col :span="12">
              <el-button type="primary" @click="queryItems">
                <i class="el-icon-search"></i>
                {{ $t('chooseMaintainanceStandardItem.search') }}
              </el-button>
              <el-button @click="resetItems" style="margin-left: 10px;">
                <i class="el-icon-refresh"></i>
                {{ $t('chooseMaintainanceStandardItem.reset') }}
              </el-button>
            </el-col>
          </el-row>

          <el-table :data="tableData" border style="width: 100%; margin-top: 15px;" v-loading="loading">
            <el-table-column width="50" align="center">
              <template slot="header">
                <el-checkbox v-model="isAllChecked" @change="handleCheckAllChange"></el-checkbox>
              </template>
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row.checked" @change="handleItemCheckChange(scope.row)">
                </el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="itemId" :label="$t('chooseMaintainanceStandardItem.id')" align="center">
            </el-table-column>
            <el-table-column prop="itemTitle" :label="$t('chooseMaintainanceStandardItem.item')" align="center">
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
            style="margin-top: 20px; text-align: right;">
          </el-pagination>

          <div style="margin-top: 20px; text-align: right;" v-if="tableData.length > 0">
            <el-button type="primary" @click="submit">
              <i class="el-icon-check"></i>
              {{ $t('chooseMaintainanceStandardItem.submit') }}
            </el-button>
            <el-button @click="handleClose" style="margin-left: 10px;">
              <i class="el-icon-close"></i>
              {{ $t('chooseMaintainanceStandardItem.cancel') }}
            </el-button>
          </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { listMaintainanceItem, saveMaintainanceStandardItem } from '@/api/inspection/maintainanceStandardItemApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ChooseMaintainanceStandardItem',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        itemTitle: '',
        standardId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      isAllChecked: false
    }
  },
  methods: {
    open(params) {
      this.resetForm()
      this.form.standardId = params.standardId
      this.visible = true
      this.loadItems()
    },
    async loadItems() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          itemTitle: this.form.itemTitle,
          communityId: getCommunityId()
        }
        const { data, records } = await listMaintainanceItem(params)
        this.tableData = data.map(item => ({
          ...item,
          checked: false
        }))
        this.pagination.total = records
      } catch (error) {
        this.$message.error(this.$t('chooseMaintainanceStandardItem.fetchError'))
      } finally {
        this.loading = false
      }
    },
    queryItems() {
      this.pagination.current = 1
      this.loadItems()
    },
    resetItems() {
      this.form.itemTitle = ''
      this.queryItems()
    },
    handleCheckAllChange(val) {
      this.tableData.forEach(item => {
        item.checked = val
      })
    },
    handleItemCheckChange() {
      this.isAllChecked = this.tableData.every(item => item.checked)
    },
    async submit() {
      const selectedItems = this.tableData
        .filter(item => item.checked)
        .map(item => item.itemId)

      if (selectedItems.length === 0) {
        this.$message.warning(this.$t('chooseMaintainanceStandardItem.selectWarning'))
        return
      }

      try {
        const params = {
          communityId: getCommunityId(),
          standardId: this.form.standardId,
          items: selectedItems
        }
        await saveMaintainanceStandardItem(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('chooseMaintainanceStandardItem.submitError'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadItems()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadItems()
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        itemTitle: '',
        standardId: ''
      }
      this.tableData = []
      this.pagination = {
        current: 1,
        size: 10,
        total: 0
      }
      this.isAllChecked = false
    }
  }
}
</script>

<style scoped>
.el-input {
  width: 100%;
}
</style>