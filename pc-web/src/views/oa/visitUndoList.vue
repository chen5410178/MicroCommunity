<template>
  <div class="animated fadeInRight visit-undo-container">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('visitUndo.title') }}</span>
            <div class="card-header-actions">
              <el-button size="small" @click="goBack">{{ $t('visitUndo.back') }}</el-button>
              <el-button type="primary" size="small" @click="queryUndoOrders">{{ $t('visitUndo.refresh') }}</el-button>
            </div>
          </div>

          <el-table
            v-loading="loading"
            :data="visitUndoInfo.undos"
            border
            style="width: 100%"
          >
            <el-table-column prop="vId" :label="$t('visitUndo.visitorId')" align="center" />
            <el-table-column :label="$t('visitUndo.visitor')" align="center">
              <template slot-scope="scope">
                {{ scope.row.vName }}({{ scope.row.phoneNumber }})/{{ scope.row.visitGender === '0' ? $t('visitUndo.male') : $t('visitUndo.female') }}
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" :label="$t('visitUndo.ownerName')" align="center" />
            <el-table-column :label="$t('visitUndo.visitReason')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitCase }}({{ scope.row.reasonTypeName }})
              </template>
            </el-table-column>
            <el-table-column prop="carNum" :label="$t('visitUndo.carNumber')" align="center" />
            <el-table-column prop="entourage" :label="$t('visitUndo.entourageCount')" align="center" />
            <el-table-column prop="createTime" :label="$t('visitUndo.createTime')" align="center" />
            <el-table-column :label="$t('visitUndo.visitTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitTime }}<br>{{ scope.row.departureTime }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('visitUndo.status')" align="center" />
            <el-table-column :label="$t('visitUndo.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" @click="openAuditUndoDetail(scope.row)">{{ $t('visitUndo.process') }}</el-button>
                  <el-button size="mini" @click="openDetail(scope.row)">{{ $t('visitUndo.detail') }}</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            :current-page.sync="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            :total="page.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryUndoVisit } from '@/api/oa/visitUndoApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'VisitUndoList',
  data() {
    return {
      loading: false,
      communityId: '',
      visitUndoInfo: {
        undos: [],
        conditions: {
          vId: '',
          userName: '',
          auditLink: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.listUndoOrders()
  },
  methods: {
    async listUndoOrders() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.visitUndoInfo.conditions,
          communityId: this.communityId,
          audit: '1'
        }
        const { data, total } = await queryUndoVisit(params)
        this.visitUndoInfo.undos = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to fetch undo orders:', error)
      } finally {
        this.loading = false
      }
    },
    queryUndoOrders() {
      this.page.current = 1
      this.listUndoOrders()
    },
    openDetail(item) {
      this.$router.push({
        path: '/views/property/visitDetail',
        query: {
          vId: item.vId,
          flowId: item.flowId
        }
      })
    },
    openAuditUndoDetail(undo) {
      this.$router.push({
        path: '/views/property/visitDetail',
        query: {
          vId: undo.vId,
          flowId: undo.flowId,
          action: 'Audit',
          taskId: undo.taskId
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listUndoOrders()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listUndoOrders()
    }
  }
}
</script>

<style lang="scss" scoped>
.visit-undo-container {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>