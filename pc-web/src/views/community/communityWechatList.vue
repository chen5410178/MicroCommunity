<template>
  <div class="community-wechat-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community :community-id="communityId" @changeCommunity="handleCommunityChange" />
      </el-col>

      <el-col :span="20">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('communityWechat.title') }}</span>
            <el-button v-if="communityId && tableData.length === 0" type="primary" size="small" class="float-right"
              @click="openAddDialog">
              <i class="el-icon-plus"></i>
              {{ $t('communityWechat.add') }}
            </el-button>
          </div>

          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column :label="$t('communityWechat.name')" prop="name" align="center" />

            <el-table-column :label="$t('communityWechat.communityName')" prop="communityName" align="center" />

            <el-table-column label="APPID" prop="appId" align="center" />

            <el-table-column :label="$t('communityWechat.appSecret')" align="center">
              <template >
                ********
              </template>
            </el-table-column>

            <el-table-column :label="$t('communityWechat.description')" prop="remarks" align="center" />

            <el-table-column :label="$t('communityWechat.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="openEditDialog(scope.row)">
                  {{ $t('communityWechat.edit') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <add-community-wechat ref="addDialog" @success="loadData" />

    <edit-community-wechat ref="editDialog" @success="loadData" />
  </div>
</template>

<script>
import { listAdminSmallWeChats } from '@/api/community/communityWechatApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import AddCommunityWechat from '@/components/community/addCommunityWechat'
import EditCommunityWechat from '@/components/community/editCommunityWechat'

export default {
  name: 'CommunityWechatList',
  components: {
    SelectAdminCommunity,
    AddCommunityWechat,
    EditCommunityWechat
  },
  data() {
    return {
      loading: false,
      communityId: '',
      tableData: [],
      page: {
        current: 1,
        size: 100,
        total: 0
      }
    }
  },
  watch: {
    communityId() {
      this.loadData()
    }
  },
  methods: {
    handleCommunityChange(community) {
      this.communityId = community.communityId
    },
    async loadData() {
      if (!this.communityId) {
        this.tableData = []
        return
      }

      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          weChatType: '1100',
          communityId: this.communityId
        }

        const res = await listAdminSmallWeChats(params)
        if (res.code === 0) {
          this.tableData = res.data || []
          this.page.total = res.total || 0
        } else {
          this.$message.error(res.msg || this.$t('communityWechat.loadFailed'))
        }
      } catch (error) {
        console.error('获取公众号列表失败:', error)
        this.$message.error(this.$t('communityWechat.loadFailed'))
      } finally {
        this.loading = false
      }
    },
    openAddDialog() {
      this.$refs.addDialog.open({
        communityId: this.communityId,
        objId: this.communityId,
        weChatType: '1100'
      })
    },
    openEditDialog(row) {
      this.$refs.editDialog.open(row)
    }
  }
}
</script>

<style scoped>
.community-wechat-container {
  padding: 20px;
}

.float-right {
  float: right;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}
</style>