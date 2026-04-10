<template>
  <div class="small-wechat-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('smallWeChatManage.title') }}</span>
        <el-button v-if="smallWeChatManageInfo.smallWeChats.length === 0" type="primary" size="small"
          class="float-right" @click="_openAddSmallWeChatModal(1000)">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table :data="smallWeChatManageInfo.smallWeChats" border style="width: 100%" v-loading="loading">
        <el-table-column prop="name" :label="$t('smallWeChatManage.name')" align="center" />
        <el-table-column prop="appId" label="APPID" align="center" />
        <el-table-column :label="$t('smallWeChatManage.appSecret')" align="center">
          <template>
            ********
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('smallWeChatManage.remarks')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="_openEditSmallWeChatModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="page.total" class="pagination">
      </el-pagination>
    </el-card>

    <add-small-wechat ref="addSmallWeChat" @success="handleSuccess" />
    <edit-small-wechat ref="editSmallWeChat" @success="handleSuccess" />
    <delete-small-wechat ref="deleteSmallWeChat" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listSmallWeChats } from '@/api/system/smallWeChatManageApi'
import AddSmallWechat from '@/components/system/addSmallWechat'
import EditSmallWechat from '@/components/system/editSmallWechat'
import DeleteSmallWechat from '@/components/system/deleteSmallWechat'

export default {
  name: 'SmallWeChatManageList',
  components: {
    AddSmallWechat,
    EditSmallWechat,
    DeleteSmallWechat
  },
  data() {
    return {
      loading: false,
      smallWeChatManageInfo: {
        smallWeChats: [],
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
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listSmallWeChats()
  },
  methods: {
    async _listSmallWeChats() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          ...this.smallWeChatManageInfo.conditions
        }
        const { smallWeChats, total } = await listSmallWeChats(params)
        this.smallWeChatManageInfo.smallWeChats = smallWeChats
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('smallWeChatManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddSmallWeChatModal(type) {
      this.$refs.addSmallWeChat.open(type)
    },
    _openEditSmallWeChatModel(smallWeChat) {
      this.$refs.editSmallWeChat.open(smallWeChat)
    },
    _openDeleteSmallWeChatModel(smallWeChat) {
      this.$refs.deleteSmallWeChat.open(smallWeChat)
    },
    handleSuccess() {
      this._listSmallWeChats()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listSmallWeChats()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listSmallWeChats()
    }
  }
}
</script>

<style lang="scss" scoped>
.small-wechat-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }

  .float-right {
    float: right;
  }
}
</style>