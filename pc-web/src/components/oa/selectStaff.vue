<template>
  <el-dialog 
    :visible.sync="visible" 
    :title="$t('selectStaff.title')"
    width="80%"
    @close="handleClose"
  >
    <el-row :gutter="20">
      <el-col :span="12" class="border-right">
        <div class="text-center mb-20">
          <h4>{{ $t('selectStaff.orgInfo') }}</h4>
        </div>
        <div class="staff-container">
          <org-tree-show ref="orgTree" @switchOrg="handleSwitchOrg" />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="text-center mb-20">
          <h4>{{ $t('selectStaff.staffInfo') }}</h4>
        </div>
        <div class="staff-container">
          <div 
            v-for="(item,index) in selectStaffInfo.staffs" 
            :key="index"
            class="staff-item"
            :class="{ 'selected': selectStaffInfo.curStaffId === item.staffId }"
            @click="handleChangeStaff(item)"
          >
            <div>
              <i class="el-icon-user"></i>
              {{ item.name }}
            </div>
            <div>{{ item.tel }}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <div 
      v-if="selectStaffInfo.staff.from === 'bpmn' || 
            selectStaffInfo.staff.from === 'purchase' || 
            selectStaffInfo.staff.from === 'contract'"
      slot="footer"
      class="dialog-footer"
    >
      <el-button @click="handleFirstUser">
        {{ $t('selectStaff.submitter') }}
      </el-button>
      <el-button @click="handleCustomUser">
        {{ $t('selectStaff.customAssign') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { queryStaffInfos } from '@/api/oa/newOaWorkflowDetailApi'
import OrgTreeShow from './OrgTreeShow'

export default {
  name: 'SelectStaff',
  components: {
    OrgTreeShow
  },
  data() {
    return {
      visible: false,
      selectStaffInfo: {
        staffs: [],
        curStaffId: '',
        curStaffName: '',
        staff: {}
      }
    }
  },
  methods: {
    open(staff) {
      this.selectStaffInfo.staff = staff
      this.visible = true
      this.$nextTick(() => {
        this.$refs.orgTree.refresh()
      })
    },
    handleClose() {
      this.selectStaffInfo.staffs = []
      this.selectStaffInfo.curStaffId = ''
    },
    handleSwitchOrg({ orgId, orgName }) {
      this.loadStaff(orgId)
    },
    async loadStaff(orgId) {
      try {
        const params = {
          page: 1,
          rows: 50,
          row: 50,
          orgId
        }
        const response = await queryStaffInfos(params)
        this.selectStaffInfo.staffs = response.staffs
        if (response.staffs.length > 0) {
          this.selectStaffInfo.curStaffId = response.staffs[0].orgId
        }
      } catch (error) {
        console.error('加载员工失败:', error)
      }
    },
    handleChangeStaff(item) {
      this.selectStaffInfo.staff.staffId = item.userId
      this.selectStaffInfo.staff.staffName = item.userName
      this.selectStaffInfo.staff.staffTel = item.tel
      this.visible = false
      if (this.selectStaffInfo.staff.call) {
        this.selectStaffInfo.staff.call(this.selectStaffInfo.staff)
      }
    },
    handleFirstUser() {
      this.selectStaffInfo.staff.staffId = '${startUserId}'
      this.selectStaffInfo.staff.staffName = this.$t('selectStaff.submitter')
      this.visible = false
      if (this.selectStaffInfo.staff.call) {
        this.selectStaffInfo.staff.call(this.selectStaffInfo.staff)
      }
    },
    handleCustomUser() {
      this.selectStaffInfo.staff.staffId = '${nextUserId}'
      this.selectStaffInfo.staff.staffName = this.$t('selectStaff.customAssign')
      this.visible = false
      if (this.selectStaffInfo.staff.call) {
        this.selectStaffInfo.staff.call(this.selectStaffInfo.staff)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.border-right {
  border-right: 1px solid #eee;
}

.staff-container {
  height: 400px;
  overflow-y: auto;
  padding: 10px;
}

.staff-item {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #eee;
  border-radius: 4px;
  cursor: pointer;
  
  &:hover {
    background-color: #f5f7fa;
  }
  
  &.selected {
    background-color: #ecf5ff;
    border-color: #c6e2ff;
  }
}

.text-center {
  text-align: center;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>