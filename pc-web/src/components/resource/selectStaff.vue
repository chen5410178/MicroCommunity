<template>
  <el-dialog
    :title="$t('transferGoodsManage.selectRecipient')"
    :visible.sync="visible"
    width="60%"
  >
    <el-row :gutter="20">
      <el-col :span="12" class="border-right">
        <div class="text-center title">
          {{ $t('transferGoodsManage.orgInfo') }}
        </div>
        <div class="org-tree-container">
          <el-tree
            :data="orgs"
            :props="defaultProps"
            @node-click="handleNodeClick"
            node-key="id"
            default-expand-all
            highlight-current
          ></el-tree>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="text-center title">
          {{ $t('transferGoodsManage.staffInfo') }}
        </div>
        <div class="staff-list-container">
          <el-scrollbar style="height: 400px">
            <div
              v-for="item in staffs"
              :key="item.userId"
              class="staff-item"
              :class="{ active: currentStaffId === item.userId }"
              @click="selectStaff(item)"
            >
              <div>
                <i class="el-icon-user"></i>
                {{ item.userName }}
              </div>
              <div>{{ item.tel }}</div>
            </div>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="confirmSelection">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SelectStaff',
  data() {
    return {
      visible: false,
      orgs: [],
      staffs: [],
      currentOrgId: '',
      currentStaffId: '',
      selectedStaff: null,
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.currentOrgId = ''
      this.currentStaffId = ''
      this.selectedStaff = null
      this.loadOrgs()
    },
    async loadOrgs() {
      try {
        const params = {
          communityId: getCommunityId()
        }

        const res = await this.$api.resource.transferGoodsManageApi.listOrgTree(
          params
        )
        this.orgs = res.data
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    handleNodeClick(data) {
      this.currentOrgId = data.id
      this.loadStaffs(data.id)
    },
    async loadStaffs(orgId) {
      try {
        const params = {
          page: 1,
          row: 50,
          orgId: orgId
        }

        const res = await this.$api.resource.transferGoodsManageApi.queryStaffInfos(
          params
        )
        this.staffs = res.staffs
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    selectStaff(staff) {
      this.currentStaffId = staff.userId
      this.selectedStaff = staff
    },
    confirmSelection() {
      if (!this.selectedStaff) {
        this.$message.warning(this.$t('transferGoodsManage.selectStaffFirst'))
        return
      }
      this.$emit('chooseStaff', this.selectedStaff)
      this.visible = false
    }
  }
}
</script>

<style scoped>
.border-right {
  border-right: 1px solid #ebeef5;
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
}

.org-tree-container {
  height: 400px;
  overflow: auto;
}

.staff-list-container {
  height: 400px;
}

.staff-item {
  padding: 10px;
  margin: 5px 0;
  cursor: pointer;
  border-radius: 4px;
}

.staff-item:hover {
  background-color: #f5f7fa;
}

.staff-item.active {
  background-color: #ecf5ff;
  color: #409eff;
}

.el-icon-user {
  margin-right: 5px;
}
</style>