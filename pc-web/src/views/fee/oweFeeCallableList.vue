<template>
  <div class="owe-fee-callable-container">
    <el-row :gutter="20">
      <el-col :span="4" class="tree-container">
        <room-tree-div-has-owner ref="roomTree" @selectRoom="handleSelectRoom"></room-tree-div-has-owner>
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('oweFeeCallable.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form text-left">
            <el-form-item >
              <el-input v-model="searchForm.ownerName"
                :placeholder="$t('oweFeeCallable.search.ownerNamePlaceholder')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-input v-model="searchForm.feeName"
                :placeholder="$t('oweFeeCallable.search.feeNamePlaceholder')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-select v-model="searchForm.callableWay"
                :placeholder="$t('oweFeeCallable.search.callableWayPlaceholder')" style="width:100%">
                <el-option label="全部" value=""></el-option>
                <el-option label="微信模板消息" value="WECHAT"></el-option>
                <el-option label="短信" value="SMS"></el-option>
                <el-option label="上门催缴" value="PRINT"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item >
              <el-input v-model="searchForm.staffName"
                :placeholder="$t('oweFeeCallable.search.staffNamePlaceholder')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-select v-model="searchForm.state" :placeholder="$t('oweFeeCallable.search.statePlaceholder')"
                style="width:100%">
                <el-option label="全部" value=""></el-option>
                <el-option label="待催缴" value="W"></el-option>
                <el-option label="催缴完成" value="C"></el-option>
                <el-option label="催缴失败" value="F"></el-option>
                <el-option label="催缴中" value="D"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
              <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="list-card">
          <div slot="header" class="flex justify-between">
            <span>{{ roomName || $t('oweFeeCallable.list.title') }}</span>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="handleWritePrint">{{ $t('oweFeeCallable.list.register')
              }}</el-button>
              <el-button type="primary" size="small" @click="handleAdd">{{ $t('oweFeeCallable.list.call') }}</el-button>
            </div>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="ofcId" :label="$t('oweFeeCallable.table.id')" align="center">
            </el-table-column>
            <el-table-column prop="ownerName" :label="$t('oweFeeCallable.table.ownerName')" align="center">
            </el-table-column>
            <el-table-column prop="payerObjName" :label="$t('oweFeeCallable.table.payerObjName')" align="center">
            </el-table-column>
            <el-table-column prop="feeName" :label="$t('oweFeeCallable.table.feeName')" align="center">
            </el-table-column>
            <el-table-column prop="amountdOwed" :label="$t('oweFeeCallable.table.amountdOwed')" align="center">
            </el-table-column>
            <el-table-column :label="$t('oweFeeCallable.table.timeRange')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }} ~ {{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column prop="callableWayName" :label="$t('oweFeeCallable.table.callableWayName')" align="center">
            </el-table-column>
            <el-table-column prop="staffName" :label="$t('oweFeeCallable.table.staffName')" align="center">
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('oweFeeCallable.table.stateName')" align="center">
            </el-table-column>
            <el-table-column prop="remark" :label="$t('oweFeeCallable.table.remark')" align="center" width="200">
              <template slot-scope="scope">
                <el-tooltip :content="scope.row.remark" placement="top">
                  <div class="text-ellipsis">{{ scope.row.remark }}</div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('oweFeeCallable.table.createTime')" align="center">
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete')
                }}</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handlePageChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" class="pagination">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <write-owe-fee-callable ref="writeOweFeeCallable" @success="handleSuccess"></write-owe-fee-callable>
    <delete-owe-fee-callable ref="deleteOweFeeCallable" @success="handleSuccess"></delete-owe-fee-callable>
  </div>
</template>

<script>
import { listOweFeeCallable } from '@/api/fee/oweFeeCallableApi'
import { getCommunityId } from '@/api/community/communityApi'
import RoomTreeDivHasOwner from '@/components/room/roomTreeDivHasOwner'
import WriteOweFeeCallable from '@/components/fee/writeOweFeeCallable'
import DeleteOweFeeCallable from '@/components/fee/deleteOweFeeCallable'

export default {
  name: 'OweFeeCallableList',
  components: {
    RoomTreeDivHasOwner,
    WriteOweFeeCallable,
    DeleteOweFeeCallable
  },
  data() {
    return {
      loading: false,
      roomName: '',
      roomId: '',
      searchForm: {
        ownerName: '',
        feeName: '',
        callableWay: '',
        staffName: '',
        state: '',
        payerObjId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId,
          ...this.searchForm
        }
        const { data, total } = await listOweFeeCallable(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('oweFeeCallable.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        ownerName: '',
        feeName: '',
        callableWay: '',
        staffName: '',
        state: '',
        payerObjId: this.searchForm.payerObjId
      }
      this.handleSearch()
    },
    handleSelectRoom({ roomId, roomName }) {
      this.roomId = roomId
      this.roomName = roomName
      this.searchForm.payerObjId = roomId
      this.handleSearch()
    },
    handleWritePrint() {
      if (!this.roomId) {
        this.$message.warning(this.$t('oweFeeCallable.selectRoomFirst'))
        return
      }
      this.$refs.writeOweFeeCallable.open({
        roomId: this.roomId,
        roomName: this.roomName
      })
    },
    handleAdd() {
      this.$router.push('/views/fee/addOweFeeCallable')
    },
    handleDelete(row) {
      this.$refs.deleteOweFeeCallable.open(row)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handlePageChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.owe-fee-callable-container {
  padding: 20px;

  .tree-container {
    min-height: 450px;
    overflow-y: auto;
    background: #fff;
    padding: 10px;
    border-radius: 4px;
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .list-card {
    .pagination {
      margin-top: 20px;
      text-align: right;
    }
  }

  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>