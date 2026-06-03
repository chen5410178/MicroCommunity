<template>
  <div class="inspection-item-manage-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="item-card">
          <div style="display: flex; align-items: center; margin-bottom: 12px; gap: 6px;">
            <el-input :placeholder="$t('inspectionItemManage.itemNamePlaceholder')"
              v-model="itemInfo.conditions.itemName" clearable size="small" style="flex: 1;" />
            <el-button type="text" size="small" @click="_queryItemMethod()">
              {{ $t('common.search') }}
            </el-button>
            <el-button type="text" size="small" @click="_openAddItemModal()">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <div class="item-list-wrapper">
            <ul class="item-list">
              <li v-for="(item, index) in itemInfo.items" :key="index" @click="_switchItem(item)"
                :class="{ 'selected': item.itemId == itemInfo.currentItem.itemId }" class="item-list-item">
                {{ item.itemName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-row v-if="itemInfo.currentItem.itemId">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('inspectionItemManage.itemDetail') }}</span>
                <div>
                  <el-button size="small" @click="_openEditItemModal(itemInfo.currentItem)">
                    {{ $t('common.edit') }}
                  </el-button>
                  <el-button size="small" type="danger" @click="_openDeleteItemModal(itemInfo.currentItem)">
                    {{ $t('common.delete') }}
                  </el-button>
                </div>
              </div>

              <div class="detail-wrapper margin-top">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionItemManage.id') }}：</span>
                      <span>{{ itemInfo.currentItem.itemId }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionItemManage.itemName') }}：</span>
                      <span>{{ itemInfo.currentItem.itemName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionItemManage.remark') }}：</span>
                      <span>{{ itemInfo.currentItem.remark }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionItemManage.createTime') }}：</span>
                      <span>{{ itemInfo.currentItem.createTime }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>

          <el-col :span="24" class="margin-top">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('inspectionItemManage.question') }}</span>
                <div>
                  <el-button type="primary" size="small" @click="_openAddTitleModal()">
                    <i class="el-icon-plus"></i>
                    {{ $t('common.add') }}
                  </el-button>
                </div>
              </div>

              <el-row :gutter="20" class="margin-bottom-20">
                <el-col :span="6">
                  <el-input v-model="titleSearchForm.itemTitle"
                    :placeholder="$t('inspectionItemTitleManage.placeholderItemTitle')" clearable />
                </el-col>
                <el-col :span="6">
                  <el-select v-model="titleSearchForm.titleType"
                    :placeholder="$t('inspectionItemTitleManage.placeholderTitleType')" style="width:100%">
                    <el-option :label="$t('inspectionItemTitleManage.selectTitleType')" value="" />
                    <el-option :label="$t('inspectionItemTitleManage.singleChoice')" value="1001" />
                    <el-option :label="$t('inspectionItemTitleManage.multipleChoice')" value="2002" />
                    <el-option :label="$t('inspectionItemTitleManage.shortAnswer')" value="3003" />
                  </el-select>
                </el-col>
                <el-col :span="6">
                  <el-button type="primary" @click="_handleTitleSearch()">
                    {{ $t('common.search') }}
                  </el-button>
                  <el-button @click="_handleTitleReset()">
                    {{ $t('common.reset') }}
                  </el-button>
                </el-col>
              </el-row>

              <el-table :data="titleTableData" border v-loading="titleLoading" style="width:100%">
                <el-table-column prop="titleId" :label="$t('inspectionItemTitleManage.titleId')" align="center" />
                <el-table-column prop="itemTitle" :label="$t('inspectionItemTitleManage.title')" align="center" />
                <el-table-column prop="titleType" :label="$t('inspectionItemTitleManage.titleType')" align="center">
                  <template slot-scope="scope">
                    {{ _getTitleTypeName(scope.row.titleType) }}
                  </template>
                </el-table-column>
                <el-table-column prop="seq" :label="$t('inspectionItemTitleManage.seq')" align="center" />
                <el-table-column prop="createTime" :label="$t('inspectionItemTitleManage.createTime')" align="center" />
                <el-table-column :label="$t('common.operation')" align="center" width="200">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="_openEditTitleModal(scope.row)">
                      {{ $t('common.edit') }}
                    </el-button>
                    <el-button size="mini" type="danger" @click="_openDeleteTitleModal(scope.row)">
                      {{ $t('common.delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <el-pagination class="pagination" :current-page.sync="titlePagination.current"
                :page-sizes="[10, 20, 30, 50]" :page-size="titlePagination.size" :total="titlePagination.total"
                layout="total, sizes, prev, pager, next, jumper" @size-change="_handleTitleSizeChange"
                @current-change="_handleTitlePageChange" />
            </el-card>
          </el-col>
        </el-row>

        <el-row v-else>
          <el-col :span="24">
            <el-card>
              <div class="text-center" style="padding: 40px 0; color: #909399;">
                {{ $t('inspectionItemManage.noItemSelected') }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <add-inspection-item ref="addInspectionItem" @success="_handleItemSuccess" />
    <edit-inspection-item ref="editInspectionItem" @success="_handleItemSuccess" />
    <delete-inspection-item ref="deleteInspectionItem" @success="_handleItemSuccess" />
    <add-inspection-item-title ref="addInspectionItemTitle" @success="_fetchTitleList" />
    <edit-inspection-item-title ref="editInspectionItemTitle" @success="_fetchTitleList" />
    <delete-inspection-item-title ref="deleteInspectionItemTitle" @success="_fetchTitleList" />
  </div>
</template>

<script>
import { listInspectionItem } from '@/api/inspection/inspectionItemManageApi'
import { listInspectionItemTitle } from '@/api/inspection/inspectionItemTitleManageApi'
import AddInspectionItem from '@/components/inspection/AddInspectionItem'
import EditInspectionItem from '@/components/inspection/EditInspectionItem'
import DeleteInspectionItem from '@/components/inspection/DeleteInspectionItem'
import AddInspectionItemTitle from '@/components/inspection/addInspectionItemTitle'
import EditInspectionItemTitle from '@/components/inspection/editInspectionItemTitle'
import DeleteInspectionItemTitle from '@/components/inspection/deleteInspectionItemTitle'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InspectionItemManageList',
  components: {
    AddInspectionItem,
    EditInspectionItem,
    DeleteInspectionItem,
    AddInspectionItemTitle,
    EditInspectionItemTitle,
    DeleteInspectionItemTitle
  },
  data() {
    return {
      communityId: '',
      itemInfo: {
        items: [],
        currentItem: {},
        conditions: {
          itemName: '',
          communityId: ''
        }
      },
      titleSearchForm: {
        itemTitle: '',
        titleType: ''
      },
      titleTableData: [],
      titleLoading: false,
      titlePagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.itemInfo.conditions.communityId = this.communityId
    this._listItems()
  },
  methods: {
    async _listItems() {
      try {
        const params = {
          ...this.itemInfo.conditions,
          page: 1,
          row: 1000
        }
        const res = await listInspectionItem(params)
        this.itemInfo.items = res.data || []

        if (this.itemInfo.items.length > 0) {
          if (this.itemInfo.currentItem.itemId) {
            var found = this.itemInfo.items.find(function (i) {
              return i.itemId === this.itemInfo.currentItem.itemId
            }.bind(this))
            if (found) {
              this.itemInfo.currentItem = found
            } else {
              this._switchItem(this.itemInfo.items[0])
            }
          } else {
            this._switchItem(this.itemInfo.items[0])
          }
        } else {
          this.itemInfo.currentItem = {}
          this.titleTableData = []
        }
      } catch (error) {
        console.error('获取巡检项目列表失败:', error)
      }
    },
    _queryItemMethod() {
      this.itemInfo.currentItem = {}
      this._listItems()
    },
    _switchItem(item) {
      this.itemInfo.currentItem = item
      this.titleSearchForm = {
        itemTitle: '',
        titleType: ''
      }
      this.titlePagination.current = 1
      this._fetchTitleList()
    },
    _openAddItemModal() {
      if (this.$refs.addInspectionItem) {
        this.$refs.addInspectionItem.open()
      }
    },
    _openEditItemModal(item) {
      if (this.$refs.editInspectionItem) {
        this.$refs.editInspectionItem.open(item)
      }
    },
    _openDeleteItemModal(item) {
      if (this.$refs.deleteInspectionItem) {
        this.$refs.deleteInspectionItem.open(item)
      }
    },
    _handleItemSuccess() {
      this._listItems()
    },
    async _fetchTitleList() {
      var itemId = this.itemInfo.currentItem.itemId
      if (!itemId) return

      this.titleLoading = true
      try {
        var params = {
          itemId: itemId,
          itemTitle: this.titleSearchForm.itemTitle,
          titleType: this.titleSearchForm.titleType,
          page: this.titlePagination.current,
          row: this.titlePagination.size,
          communityId: this.communityId
        }
        var res = await listInspectionItemTitle(params)
        this.titleTableData = res.data
        this.titlePagination.total = res.total
      } catch (error) {
        console.error('获取巡检题目列表失败:', error)
      } finally {
        this.titleLoading = false
      }
    },
    _handleTitleSearch() {
      this.titlePagination.current = 1
      this._fetchTitleList()
    },
    _handleTitleReset() {
      this.titleSearchForm.itemTitle = ''
      this.titleSearchForm.titleType = ''
      this._handleTitleSearch()
    },
    _handleTitleSizeChange(size) {
      this.titlePagination.size = size
      this._fetchTitleList()
    },
    _handleTitlePageChange(page) {
      this.titlePagination.current = page
      this._fetchTitleList()
    },
    _getTitleTypeName(type) {
      var types = {
        '1001': this.$t('inspectionItemTitleManage.singleChoice'),
        '2002': this.$t('inspectionItemTitleManage.multipleChoice'),
        '3003': this.$t('inspectionItemTitleManage.shortAnswer')
      }
      return types[type] || type
    },
    _openAddTitleModal() {
      if (this.$refs.addInspectionItemTitle) {
        this.$refs.addInspectionItemTitle.open({
          itemId: this.itemInfo.currentItem.itemId
        })
      }
    },
    _openEditTitleModal(row) {
      if (this.$refs.editInspectionItemTitle) {
        this.$refs.editInspectionItemTitle.open(row)
      }
    },
    _openDeleteTitleModal(row) {
      if (this.$refs.deleteInspectionItemTitle) {
        this.$refs.deleteInspectionItemTitle.open(row)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-item-manage-container {
  padding: 20px;
}

.margin-top {
  margin-top: 15px;
}

.margin-bottom-20 {
  margin-bottom: 20px;
}

.left-panel {
  .item-card {
    height: 700px;

    .item-list-wrapper {
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

      .item-list {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .item-list-item {
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
}

.right-panel {
  padding-left: 5px;
}

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

.text-title {
  font-size: 16px;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
