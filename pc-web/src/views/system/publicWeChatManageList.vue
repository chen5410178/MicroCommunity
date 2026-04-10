<template>
  <div class="public-wechat-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('publicWeChatManage.title') }}</span>
        <div class="header-tools">
          <el-button v-if="smallWeChatManageInfo.smallWeChats.length === 0" type="primary" size="small"
            @click="_openAddSmallWeChatModal(1100)">
            <i class="el-icon-plus"></i>{{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table :data="smallWeChatManageInfo.smallWeChats" border style="width: 100%">
        <el-table-column prop="name" :label="$t('publicWeChatManage.table.name')" align="center">
        </el-table-column>
        <el-table-column prop="appId" label="APPID" align="center">
        </el-table-column>
        <el-table-column :label="$t('publicWeChatManage.table.appSecret')" align="center">
          <template >
            ********
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('publicWeChatManage.table.remarks')" align="center">
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openEditSmallWeChatModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <add-small-wechat ref="addSmallWeChat" @success="handleSuccess"></add-small-wechat>
    <edit-small-wechat ref="editSmallWeChat" @success="handleSuccess"></edit-small-wechat>
    <delete-small-wechat ref="deleteSmallWeChat" @success="handleSuccess"></delete-small-wechat>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listSmallWeChats } from '@/api/system/publicWeChatManageApi'
import AddSmallWechat from '@/components/system/addSmallWechat'
import EditSmallWechat from '@/components/system/editSmallWechat'
import DeleteSmallWechat from '@/components/system/deleteSmallWechat'

export default {
  name: 'PublicWeChatManageList',
  components: {
    AddSmallWechat,
    EditSmallWechat,
    DeleteSmallWechat
  },
  data() {
    return {
      smallWeChatManageInfo: {
        smallWeChats: [],
        total: 0,
        records: 1,
        conditions: {
          name: '',
          appId: '',
          weChatType: '1100',
          communityId: ''
        }
      }
    }
  },
  created() {
    this.smallWeChatManageInfo.conditions.communityId = getCommunityId()
    this._listSmallWeChats(1, 10)
  },
  methods: {
    async _listSmallWeChats(page, rows) {
      try {
        this.smallWeChatManageInfo.conditions.page = page
        this.smallWeChatManageInfo.conditions.row = rows

        const { data, total } = await listSmallWeChats(this.smallWeChatManageInfo.conditions)
        this.smallWeChatManageInfo.smallWeChats = data
        this.smallWeChatManageInfo.total = total
      } catch (error) {
        this.$message.error(this.$t('publicWeChatManage.fetchError'))
      }
    },
    _openAddSmallWeChatModal(type) {
      this.$refs.addSmallWeChat.open(type)
    },
    _openEditSmallWeChatModel(smallWeChat) {
      this.$refs.editSmallWeChat.open(smallWeChat)
    },
    handleSuccess() {
      this._listSmallWeChats(1, 10)
    },
    showMarkdown(path) {
      // 显示markdown文档的方法
      console.log(path)
    }
  }
}
</script>

<style lang="scss" scoped>
.public-wechat-manage-container {
  padding: 20px;

  .header-tools {
    float: right;
    margin-right: 10px;
  }

  .el-card {
    margin-bottom: 20px;
  }
}
</style>