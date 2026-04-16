<template>
  <div class="maintainance-standard-item-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('maintainanceStandardItem.title') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="_goBack" style="margin-right:10px">
            <i class="el-icon-close"></i>
            <span>{{ $t('maintainanceStandardItem.back') }}</span>
          </el-button>
          <el-button type="primary" size="small" @click="_openAddMaintainanceStandardItemModal">
            <i class="el-icon-plus"></i>
            <span>{{ $t('maintainanceStandardItem.add') }}</span>
          </el-button>
        </div>
      </div>

      <el-table :data="maintainanceStandardItemInfo.items" border style="width: 100%" v-loading="loading">
        <el-table-column prop="itemTitle" :label="$t('maintainanceStandardItem.item')" align="center">
        </el-table-column>
        <el-table-column prop="itemId" :label="$t('maintainanceStandardItem.id')" align="center">
        </el-table-column>
        <el-table-column :label="$t('maintainanceStandardItem.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="_openDeleteMaintainanceStandardItemModel(scope.row)">
              {{ $t('maintainanceStandardItem.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
        style="margin-top: 20px; text-align: right;">
      </el-pagination>
    </el-card>

    <choose-maintainance-standard-item ref="chooseModal" @success="_listMaintainanceStandardItems"></choose-maintainance-standard-item>
    <delete-maintainance-standard-item ref="deleteModal" @success="_listMaintainanceStandardItems"></delete-maintainance-standard-item>
  </div>
</template>

<script>
import { listMaintainanceStandardItem } from '@/api/inspection/maintainanceStandardItemApi'
import ChooseMaintainanceStandardItem from '@/components/inspection/chooseMaintainanceStandardItem'
import DeleteMaintainanceStandardItem from '@/components/inspection/deleteMaintainanceStandardItem'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainanceStandardItemList',
  components: {
    ChooseMaintainanceStandardItem,
    DeleteMaintainanceStandardItem
  },
  data() {
    return {
      loading: false,
      maintainanceStandardItemInfo: {
        items: [],
        standardId: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.maintainanceStandardItemInfo.standardId = this.$route.query.standardId
    this._listMaintainanceStandardItems()
  },
  methods: {
    async _listMaintainanceStandardItems() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId(),
          standardId: this.maintainanceStandardItemInfo.standardId
        }
        const { data, records } = await listMaintainanceStandardItem(params)
        this.maintainanceStandardItemInfo.items = data
        this.pagination.total = records
      } catch (error) {
        this.$message.error(this.$t('maintainanceStandardItem.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddMaintainanceStandardItemModal() {
      this.$refs.chooseModal.open({
        standardId: this.maintainanceStandardItemInfo.standardId
      })
    },
    _openDeleteMaintainanceStandardItemModel(item) {
      this.$refs.deleteModal.open({
        ...item,
        standardId: this.maintainanceStandardItemInfo.standardId
      })
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listMaintainanceStandardItems()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listMaintainanceStandardItems()
    }
  }
}
</script>

<style lang="scss" scoped>
.maintainance-standard-item-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-header-actions {
      display: flex;
      align-items: center;
    }
  }
}
</style>