<template>
  <div class="wechat-subscribe-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between items-center">
        <span>{{ $t('wechatSubscribe.title') }}</span>
        <div class="card-header-actions">
          <el-button size="small" @click="_queryWechatSubscribeMethod()">
            {{ $t('common.refresh') }}
          </el-button>
          <el-button 
            type="primary" 
            size="small" 
            @click="_openOpenSyncDataModel()">
            {{ $t('wechatSubscribe.syncUser') }}
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="wechatSubscribeInfo.users"
        border
        style="width: 100%">
        <el-table-column
          prop="openType"
          :label="$t('wechatSubscribe.type')"
          align="center">
          <template slot-scope="scope">
            {{ scope.row.openType === 'WECHAT' ? $t('wechatSubscribe.wechat') : $t('wechatSubscribe.other') }}
          </template>
        </el-table-column>
        <el-table-column
          prop="openId"
          :label="$t('wechatSubscribe.openId')"
          align="center">
        </el-table-column>
        <el-table-column
          prop="unionId"
          :label="$t('wechatSubscribe.unionId')"
          align="center">
        </el-table-column>
        <el-table-column
          prop="appId"
          label="app_id"
          align="center">
        </el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('wechatSubscribe.syncTime')"
          align="center">
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination-wrapper"
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
      </el-pagination>
    </el-card>

    <sync-subscribe-wechat ref="syncSubscribeWechat" @success="handleSyncSuccess"></sync-subscribe-wechat>
  </div>
</template>

<script>
import { listWechatSubscribe } from '@/api/community/wechatSubscribeApi'
import { getCommunityId } from '@/api/community/communityApi'
import SyncSubscribeWechat from '@/components/community/syncSubscribeWechat'

export default {
  name: 'WechatSubscribeList',
  components: {
    SyncSubscribeWechat
  },
  data() {
    return {
      loading: false,
      wechatSubscribeInfo: {
        users: [],
        conditions: {
          name: '',
          appId: '',
          weChatType: '1000'
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
    this._listWechatSubscribes(this.page.current, this.page.size)
  },
  methods: {
    async _listWechatSubscribes(page, size) {
      try {
        this.loading = true
        const params = {
          page,
          row: size,
          communityId: getCommunityId(),
          ...this.wechatSubscribeInfo.conditions
        }
        const { data, total } = await listWechatSubscribe(params)
        this.wechatSubscribeInfo.users = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('wechatSubscribe.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openOpenSyncDataModel() {
      this.$refs.syncSubscribeWechat.open()
    },
    _queryWechatSubscribeMethod() {
      this.page.current = 1
      this._listWechatSubscribes(this.page.current, this.page.size)
    },
    handleSyncSuccess() {
      this._listWechatSubscribes(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listWechatSubscribes(this.page.current, val)
    },
    handleCurrentChange(val) {
      this._listWechatSubscribes(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.wechat-subscribe-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }
}
</style>