<template>
  <div class="maintainance-standard-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="standard-card">
          <div style="display: flex; align-items: center; margin-bottom: 12px; gap: 6px;">
            <el-input :placeholder="$t('maintainanceStandard.search.standardName')"
              v-model="searchForm.standardName" clearable size="small" style="flex: 1;" />
            <el-button type="text" size="small" @click="handleSearch()">
              {{ $t('common.search') }}
            </el-button>
            <el-button type="text" size="small" @click="handleAdd()">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <div class="standard-list-wrapper">
            <div v-for="(item, index) in tableData" :key="index"
              class="standard-list-item"
              :class="{ 'selected': currentStandard.standardId == item.standardId }"
              @click="_switchStandard(item)">
              {{ item.standardName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-row v-if="currentStandard.standardId">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('maintainanceStandard.standardDetail') }}</span>
                <div>
                  <el-button size="small" @click="handleEdit(currentStandard)">
                    {{ $t('common.edit') }}
                  </el-button>
                  <el-button size="small" type="danger" @click="handleDelete(currentStandard)">
                    {{ $t('common.delete') }}
                  </el-button>
                </div>
              </div>

              <div class="detail-wrapper margin-top">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('maintainanceStandard.table.standardName') }}：</span>
                      <span>{{ currentStandard.standardName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('maintainanceStandard.table.itemCount') }}：</span>
                      <span>{{ currentStandard.itemCount }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('maintainanceStandard.table.remark') }}：</span>
                      <span>{{ currentStandard.remark }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('maintainanceStandard.table.createTime') }}：</span>
                      <span>{{ currentStandard.createTime }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>

          <el-col :span="24" class="margin-top">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('maintainanceStandard.items') }}</span>
                <div>
                  <el-button type="primary" size="small" @click="_openAddItemModal()">
                    <i class="el-icon-plus"></i>
                    {{ $t('common.add') }}
                  </el-button>
                </div>
              </div>

              <el-table :data="itemTableData" border v-loading="itemLoading" style="width:100%">
                <el-table-column prop="itemTitle" :label="$t('maintainanceStandard.table.item')" align="center" />
                <el-table-column prop="itemId" :label="$t('maintainanceStandard.itemId')" align="center" />
                <el-table-column :label="$t('common.operation')" align="center" width="150">
                  <template slot-scope="scope">
                    <el-button size="mini" type="danger" @click="_openDeleteItemModal(scope.row)">
                      {{ $t('common.delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <el-pagination class="pagination" :current-page.sync="itemPagination.current"
                :page-sizes="[10, 20, 30, 50]" :page-size="itemPagination.size" :total="itemPagination.total"
                layout="total, sizes, prev, pager, next, jumper" @size-change="_handleItemSizeChange"
                @current-change="_handleItemPageChange" />
            </el-card>
          </el-col>
        </el-row>

        <el-row v-else>
          <el-col :span="24">
            <el-card>
              <div class="text-center" style="padding: 40px 0; color: #909399;">
                {{ $t('maintainanceStandard.noStandardSelected') }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <AddMaintainanceStandard ref="addDialog" @success="_handleStandardSuccess" />
    <EditMaintainanceStandard ref="editDialog" @success="_handleStandardSuccess" />
    <DeleteMaintainanceStandard ref="deleteDialog" @success="_handleStandardSuccess" />
    <ChooseMaintainanceStandardItem ref="chooseItemModal" @success="_listItems" />
    <DeleteMaintainanceStandardItem ref="deleteItemModal" @success="_listItems" />
  </div>
</template>

<script>
import {
  listMaintainanceStandard
} from '@/api/inspection/maintainanceStandardManageApi'
import { listMaintainanceStandardItem } from '@/api/inspection/maintainanceStandardItemApi'
import AddMaintainanceStandard from '@/components/inspection/addMaintainanceStandard'
import EditMaintainanceStandard from '@/components/inspection/editMaintainanceStandard'
import DeleteMaintainanceStandard from '@/components/inspection/deleteMaintainanceStandard'
import ChooseMaintainanceStandardItem from '@/components/inspection/chooseMaintainanceStandardItem'
import DeleteMaintainanceStandardItem from '@/components/inspection/deleteMaintainanceStandardItem'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainanceStandardManageList',
  components: {
    AddMaintainanceStandard,
    EditMaintainanceStandard,
    DeleteMaintainanceStandard,
    ChooseMaintainanceStandardItem,
    DeleteMaintainanceStandardItem
  },
  data() {
    return {
      loading: false,
      searchForm: {
        standardName: '',
        communityId: ''
      },
      tableData: [],
      currentStandard: {},
      page: {
        current: 1,
        size: 1000,
        total: 0
      },
      itemTableData: [],
      itemLoading: false,
      itemPagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: this.page.size,
          ...this.searchForm
        }
        const { data } = await listMaintainanceStandard(params)
        this.tableData = data || []

        if (this.tableData.length > 0) {
          if (this.currentStandard.standardId) {
            var found = this.tableData.find(function (i) {
              return i.standardId === this.currentStandard.standardId
            }.bind(this))
            if (found) {
              this.currentStandard = found
            } else {
              this._switchStandard(this.tableData[0])
            }
          } else {
            this._switchStandard(this.tableData[0])
          }
        } else {
          this.currentStandard = {}
          this.itemTableData = []
        }
      } catch (error) {
        console.error('获取保养标准列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.currentStandard = {}
      this.getList()
    },
    _switchStandard(item) {
      this.currentStandard = item
      this.itemPagination.current = 1
      this._listItems()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    _handleStandardSuccess() {
      this.getList()
    },
    async _listItems() {
      var standardId = this.currentStandard.standardId
      if (!standardId) return

      this.itemLoading = true
      try {
        var params = {
          page: this.itemPagination.current,
          row: this.itemPagination.size,
          communityId: getCommunityId(),
          standardId: standardId
        }
        var res = await listMaintainanceStandardItem(params)
        this.itemTableData = res.data || []
        this.itemPagination.total = res.total || 0
      } catch (error) {
        console.error('获取检查项列表失败:', error)
      } finally {
        this.itemLoading = false
      }
    },
    _openAddItemModal() {
      this.$refs.chooseItemModal.open({
        standardId: this.currentStandard.standardId
      })
    },
    _openDeleteItemModal(item) {
      this.$refs.deleteItemModal.open({
        ...item,
        standardId: this.currentStandard.standardId
      })
    },
    _handleItemSizeChange(val) {
      this.itemPagination.size = val
      this._listItems()
    },
    _handleItemPageChange(val) {
      this.itemPagination.current = val
      this._listItems()
    }
  }
}
</script>

<style lang="scss" scoped>
.maintainance-standard-container {
  padding: 20px;
}

.margin-top {
  margin-top: 15px;
}

.left-panel {
  .standard-card {
    height: 700px;

    .standard-list-wrapper {
      height: 600px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #dcdfe6;
        border-radius: 2px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .standard-list-item {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;
        transition: all 0.3s;
        font-size: 14px;
        color: #606266;

        &:hover {
          background-color: #f5f7fa;
        }

        &.selected {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }
}

.right-panel {
  padding-left: 5px;

  .detail-wrapper {
    .detail-item {
      padding: 8px 0;
      font-size: 14px;
      color: #606266;
    }

    .detail-label {
      margin-right: 4px;
    }
  }
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
