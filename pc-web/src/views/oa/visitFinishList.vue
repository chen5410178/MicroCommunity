<template>
  <div class="animated fadeInRight ecommerce">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('visitFinish.title') }}</span>
            <div class="ibox-tools" style="float: right;">
              <el-button size="small" @click="goBack">{{ $t('visitFinish.back') }}</el-button>
              <el-button type="primary" size="small" @click="_queryFinishOrdersMethod">{{ $t('visitFinish.refresh')
                }}</el-button>
            </div>
          </div>

          <el-table v-loading="loading" :data="visitFinishInfo.finishs" border style="width: 100%">
            <el-table-column prop="vId" :label="$t('visitFinish.visitorId')" align="center" width="120">
            </el-table-column>
            <el-table-column :label="$t('visitFinish.visitor')" align="center">
              <template slot-scope="scope">
                {{ scope.row.vName }}({{ scope.row.phoneNumber }})/{{ scope.row.visitGender == '0' ? $t('visitFinish.male') : $t('visitFinish.female') }}
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" :label="$t('visitFinish.ownerName')" align="center">
            </el-table-column>
            <el-table-column :label="$t('visitFinish.visitReason')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitCase }}({{ scope.row.reasonType == 0 ? $t('visitFinish.happyEvent') :
                  scope.row.reasonType == 1 ? $t('visitFinish.funeral') :
                    scope.row.reasonType == 2 ? $t('visitFinish.other') : '-' }})
              </template>
            </el-table-column>
            <el-table-column prop="carNum" :label="$t('visitFinish.carNumber')" align="center">
            </el-table-column>
            <el-table-column prop="entourage" :label="$t('visitFinish.entourage')" align="center">
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('visitFinish.createTime')" align="center">
            </el-table-column>
            <el-table-column :label="$t('visitFinish.visitTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitTime }}<br />{{ scope.row.departureTime }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('visitFinish.status')" align="center">
            </el-table-column>
            <el-table-column :label="$t('visitFinish.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openDetail(scope.row)">{{ $t('visitFinish.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryFinishVisit } from '@/api/oa/visitFinishApi'

export default {
  name: 'VisitFinishList',
  data() {
    return {
      loading: false,
      visitFinishInfo: {
        finishs: [],
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
    this._listFinishOrders(this.page.current, this.page.size)
  },
  methods: {
    async _listFinishOrders(page, size) {
      try {
        this.loading = true
        const params = {
          page: page,
          row: size,
          ...this.visitFinishInfo.conditions
        }
        const { data, total } = await queryFinishVisit(params)
        this.visitFinishInfo.finishs = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('visitFinish.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryFinishOrdersMethod() {
      this.page.current = 1
      this._listFinishOrders(this.page.current, this.page.size)
    },
    _openDetail(item) {
      this.$router.push({
        path: '/pages/property/visitDetail',
        query: {
          vId: item.vId,
          flowId: item.flowId
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listFinishOrders(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listFinishOrders(val, this.page.size)
    }
  }
}
</script>

<style scoped>
.ibox-tools {
  display: inline-block;
  float: right;
  margin-top: 0;
  position: relative;
  padding: 0;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>