<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('selectStaff.title')"
    width="70%"
    @close="handleClose"
  >
    <el-row :gutter="20">
      <el-col :span="12" class="border-right">
        <div class="text-center mb-20">
          <h4>{{ $t('selectStaff.orgInfo') }}</h4>
        </div>
        <div class="org-tree-container">
          <org-tree-show 
            ref="orgTree"
            @switch-org="handleSwitchOrg"
          />
        </div>
      </el-col>
      
      <el-col :span="12">
        <div class="text-center mb-20">
          <h4>{{ $t('selectStaff.staffInfo') }}</h4>
        </div>
        <div class="staff-list">
          <div
            v-for="(item,index) in staffList"
            :key="index"
            class="staff-item"
            :class="{ 'active': currentStaffId === item.userId }"
            @click="handleSelectStaff(item)"
          >
            <div>
              <i class="el-icon-user margin-right-xs"></i>
              {{ item.userName }}
            </div>
            <div>{{ item.tel }}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <div 
      v-if="staff.from === 'bpmn' || staff.from === 'purchase' || staff.from === 'contract'"
      slot="footer"
      class="dialog-footer"
    >
      <el-button @click="handleFirstUser">{{ $t('selectStaff.submitter') }}</el-button>
      <el-button @click="handleCustomUser">{{ $t('selectStaff.dynamicAssign') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { queryStaffInfos } from '@/api/system/workflowSettingManageApi'
import OrgTreeShow from './OrgTreeShow'

export default {
  name: 'SelectStaff',
  components: {
    OrgTreeShow
  },
  data() {
    return {
      visible: false,
      staff: {},
      staffList: [],
      currentStaffId: '',
      currentOrg: null
    }
  },
  methods: {
    open(staff) {
      this.staff = staff
      this.visible = true
      this.$nextTick(() => {
        this.$refs.orgTree.initTree()
      })
    },
    
    handleClose() {
      this.staffList = []
      this.currentStaffId = ''
      this.currentOrg = null
    },
    
    async handleSwitchOrg(org) {
      this.currentOrg = org
      try {
        const { data } = await queryStaffInfos({
          page: 1,
          row: 50,
          orgId: org.orgId
        })
        this.staffList = data.staffs || []
        if (this.staffList.length > 0) {
          this.currentStaffId = this.staffList[0].userId
        }
      } catch (error) {
        this.$message.error(this.$t('selectStaff.fetchStaffError'))
      }
    },
    
    handleSelectStaff(item) {
      this.staff.staffId = item.userId
      this.staff.staffName = item.userName
      this.visible = false
      if (this.staff.call && typeof this.staff.call === 'function') {
        this.staff.call(this.staff)
      }
    },
    
    handleFirstUser() {
      this.staff.staffId = '${startUserId}'
      this.staff.staffName = this.$t('selectStaff.submitter')
      this.visible = false
      if (this.staff.call && typeof this.staff.call === 'function') {
        this.staff.call(this.staff)
      }
    },
    
    handleCustomUser() {
      this.staff.staffId = '${nextUserId}'
      this.staff.staffName = this.$t('selectStaff.dynamicAssign')
      this.visible = false
      if (this.staff.call && typeof this.staff.call === 'function') {
        this.staff.call(this.staff)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.border-right {
  border-right: 1px solid #ebeef5;
}

.org-tree-container {
  height: 400px;
  overflow-y: auto;
}

.staff-list {
  height: 400px;
  overflow-y: auto;
  
  .staff-item {
    padding: 10px;
    margin-bottom: 10px;
    cursor: pointer;
    border-radius: 4px;
    
    &:hover {
      background-color: #f5f7fa;
    }
    
    &.active {
      background-color: #ecf5ff;
      color: #409eff;
    }
  }
}

.margin-right-xs {
  margin-right: 5px;
}

.mb-20 {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>