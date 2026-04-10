<template>
  <div class="animated fadeInRight ecommerce">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemReleaseFinish.title') }}</span>
            <div style="float: right;">
              <el-button size="small" @click="goBack()">
                {{ $t('itemReleaseFinish.back') }}
              </el-button>
              <el-button type="primary" size="small" @click="_queryUndoOrdersMethod()">
                {{ $t('itemReleaseFinish.refresh') }}
              </el-button>
            </div>
          </div>

          <el-table :data="itemReleaseFinishInfo.finishs" border style="width: 100%" v-loading="loading">
            <el-table-column prop="irId" :label="$t('itemReleaseFinish.orderNo')" align="center" />
            <el-table-column prop="typeName" :label="$t('itemReleaseFinish.releaseType')" align="center" />
            <el-table-column prop="applyCompany" :label="$t('itemReleaseFinish.applyUnit')" align="center" />
            <el-table-column prop="applyPerson" :label="$t('itemReleaseFinish.applicant')" align="center" />
            <el-table-column prop="idCard" :label="$t('itemReleaseFinish.idCard')" align="center" />
            <el-table-column prop="applyTel" :label="$t('itemReleaseFinish.phone')" align="center" />
            <el-table-column prop="passTime" :label="$t('itemReleaseFinish.passTime')" align="center" />
            <el-table-column :label="$t('itemReleaseFinish.items')" align="center">
              <template slot-scope="scope">
                {{ scope.row.amount }}(<el-link type="primary" @click="viewResName(scope.row)">
                  {{ $t('itemReleaseFinish.viewItems') }}
                </el-link>)
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('itemReleaseFinish.status')" align="center" />
            <el-table-column prop="carNum" :label="$t('itemReleaseFinish.plateNo')" align="center">
              <template slot-scope="scope">
                {{ scope.row.carNum || $t('itemReleaseFinish.none') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('itemReleaseFinish.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openDetail(scope.row)">
                  {{ $t('itemReleaseFinish.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="margin-top: 20px;" />
        </el-card>
      </el-col>
    </el-row>

    <view-item-release-res ref="viewItemReleaseRes" />
  </div>
</template>

<script>
import { queryFinishItemRelease } from '@/api/resource/itemReleaseFinishApi'
import ViewItemReleaseRes from '@/components/work/viewItemReleaseRes'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ItemReleaseFinishList',
  components: {
    ViewItemReleaseRes
  },
  data() {
    return {
      loading: false,
      itemReleaseFinishInfo: {
        finishs: [],
        conditions: {
          irId: '',
          userName: '',
          auditLink: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listUndoOrders(this.page.current, this.page.size)
  },
  methods: {
    async _listUndoOrders(page, size) {
      try {
        this.loading = true
        const params = {
          page,
          row: size,
          communityId: this.communityId,
          ...this.itemReleaseFinishInfo.conditions
        }
        const { data, total } = await queryFinishItemRelease(params)
        this.itemReleaseFinishInfo.finishs = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('itemReleaseFinish.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryUndoOrdersMethod() {
      this.page.current = 1
      this._listUndoOrders(this.page.current, this.page.size)
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
    viewResName(item) {
      this.$refs.viewItemReleaseRes.open(item)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listUndoOrders(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listUndoOrders(val, this.page.size)
    }
  }
}
</script>

<style scoped>
.ecommerce {
  padding: 20px;
}
</style>