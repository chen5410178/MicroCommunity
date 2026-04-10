<template>
  <div class="community-mini-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community :community-id="communityId" @changeCommunity="handleCommunityChange" />
      </el-col>

      <el-col :span="20">
        <el-card>
          <div slot="header" class=" flex justify-between">
            <span>{{ $t('communityMini.title') }}</span>
            <div class="header-tools">
              <el-button v-if="smallWeChats.length === 0 && conditions.communityId" type="primary" size="small"
                icon="el-icon-plus" @click="openAddModal">
                {{ $t('communityMini.add') }}
              </el-button>
            </div>
          </div>

          <el-table :data="smallWeChats" border style="width: 100%">
            <el-table-column prop="name" :label="$t('communityMini.name')" align="center" />
            <el-table-column prop="communityName" :label="$t('communityMini.communityName')" align="center" />
            <el-table-column prop="appId" label="APPID" align="center" />
            <el-table-column :label="$t('communityMini.appSecret')" align="center">
              <template >
                ********
              </template>
            </el-table-column>
            <el-table-column prop="remarks" :label="$t('communityMini.description')" align="center" />
            <el-table-column :label="$t('communityMini.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="openEditModal(scope.row)">
                  {{ $t('communityMini.edit') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <add-community-wechat ref="addModal" @success="loadSmallWeChats" />
    <edit-community-wechat ref="editModal" @success="loadSmallWeChats" />
  </div>
</template>

<script>
import { listAdminSmallWeChats } from '@/api/community/communityMiniApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import AddCommunityWechat from '@/components/community/addCommunityWechat'
import EditCommunityWechat from '@/components/community/editCommunityWechat'

export default {
  name: 'CommunityMiniList',
  components: {
    SelectAdminCommunity,
    AddCommunityWechat,
    EditCommunityWechat
  },
  data() {
    return {
      conditions: {
        communityId: '',
        weChatType: '1000',
        page: 1,
        row: 100
      },
      smallWeChats: [],
      loading: false
    }
  },
  mounted() {
    this.loadSmallWeChats()
  },
  methods: {
    async loadSmallWeChats() {
      this.loading = true
      try {
        const res = await listAdminSmallWeChats(this.conditions)
        if (res.code === 0) {
          this.smallWeChats = res.data || []
        } else {
          this.$message.error(res.msg || '加载小程序列表失败')
        }
      } catch (error) {
        console.error('加载小程序列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleCommunityChange(community) {
      this.conditions.communityId = community.communityId
      this.loadSmallWeChats()
    },
    openAddModal() {
      const params = {
        objId: this.conditions.communityId,
        communityId: this.conditions.communityId,
        weChatType: '1000'
      }
      this.$refs.addModal.open(params)
    },
    openEditModal(row) {
      this.$refs.editModal.open(row)
    }
  }
}
</script>

<style scoped>
.community-mini-container {
  padding: 20px;
}

.header-tools {
  float: right;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}
</style>