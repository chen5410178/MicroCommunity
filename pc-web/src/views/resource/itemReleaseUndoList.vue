<template>
  <div class="item-release-undo-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('itemReleaseUndo.title') }}</span>
        <div class="header-actions">
          <el-button size="small" @click="goBack()">
            {{ $t('itemReleaseUndo.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="_queryUndoOrdersMethod()">
            {{ $t('itemReleaseUndo.refresh') }}
          </el-button>
        </div>
      </div>

      <el-table :data="itemReleaseUndoInfo.undos" border style="width: 100%" v-loading="loading">
        <el-table-column prop="irId" :label="$t('itemReleaseUndo.orderNo')" align="center" />
        <el-table-column prop="typeName" :label="$t('itemReleaseUndo.releaseType')" align="center" />
        <el-table-column prop="applyCompany" :label="$t('itemReleaseUndo.applyUnit')" align="center" />
        <el-table-column prop="applyPerson" :label="$t('itemReleaseUndo.applicant')" align="center" />
        <el-table-column prop="idCard" :label="$t('itemReleaseUndo.idCard')" align="center" />
        <el-table-column prop="applyTel" :label="$t('itemReleaseUndo.phone')" align="center" />
        <el-table-column prop="passTime" :label="$t('itemReleaseUndo.passTime')" align="center" />
        <el-table-column :label="$t('itemReleaseUndo.items')" align="center">
          <template slot-scope="scope">
            {{ scope.row.amount }}(<el-link type="primary" @click="viewResName(scope.row)">{{
              $t('itemReleaseUndo.viewItems') }}</el-link>)
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('itemReleaseUndo.status')" align="center" />
        <el-table-column prop="carNum" :label="$t('itemReleaseUndo.plateNo')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carNum || $t('itemReleaseUndo.none') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('itemReleaseUndo.operations')" align="center" width="180">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="_openAuditUndoDetail(scope.row)">
                {{ $t('itemReleaseUndo.process') }}
              </el-button>
              <el-button size="mini" @click="_openDetail(scope.row)">
                {{ $t('itemReleaseUndo.detail') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" class="pagination" />

      <view-item-release-res ref="viewItemReleaseRes" />
    </el-card>
  </div>
</template>

<script>
import { queryUndoItemRelease } from '@/api/resource/itemReleaseUndoApi'
import ViewItemReleaseRes from '@/components/work/viewItemReleaseRes'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'ItemReleaseUndoList',
  components: {
    ViewItemReleaseRes
  },
  data() {
    return {
      loading: false,
      itemReleaseUndoInfo: {
        undos: [],
        conditions: {
          irId: '',
          userName: '',
          auditLink: '',
          page: 1,
          row: 10
        },
        currentUserId: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.itemReleaseUndoInfo.currentUserId = getUserId()
    this._listUndoOrders()
  },
  methods: {
    async _listUndoOrders() {
      try {
        this.loading = true
        const params = {
          ...this.itemReleaseUndoInfo.conditions,
          communityId: getCommunityId(),
          audit: '1'
        }
        const { data, total } = await queryUndoItemRelease(params)
        this.itemReleaseUndoInfo.undos = data
        this.pagination.total = total
      } catch (error) {
        console.error('Error fetching undo orders:', error)
      } finally {
        this.loading = false
      }
    },
    _queryUndoOrdersMethod() {
      this.pagination.current = 1
      this._listUndoOrders()
    },
    _openDetail(item) {
      this.$router.push({
        path: '/views/work/itemReleaseDetail',
        query: {
          irId: item.irId,
          flowId: item.flowId
        }
      })
    },
    _openAuditUndoDetail(undo) {
      this.$router.push({
        path: '/views/work/itemReleaseDetail',
        query: {
          irId: undo.irId,
          flowId: undo.flowId,
          action: 'Audit',
          taskId: undo.taskId
        }
      })
    },
    viewResName(item) {
      this.$refs.viewItemReleaseRes.open(item)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.itemReleaseUndoInfo.conditions.row = val
      this._listUndoOrders()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.itemReleaseUndoInfo.conditions.page = val
      this._listUndoOrders()
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.item-release-undo-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>