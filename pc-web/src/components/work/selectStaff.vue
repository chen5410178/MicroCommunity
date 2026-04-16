<template>
  <el-dialog 
    :visible.sync="visible" 
    :title="$t('selectStaff.selectStaff')"
    width="80%"
    @close="handleClose"
  >
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="section-title">{{ $t('selectStaff.orgInfo') }}</div>
        <org-tree-show ref="orgTree" @switchOrg="handleSwitchOrg" />
      </el-col>
      <el-col :span="12">
        <div class="section-title">{{ $t('selectStaff.staffInfo') }}</div>
        <div class="staff-list">
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
    <div slot="footer" class="dialog-footer">
      <el-button 
        v-if="staff.from === 'bpmn' || staff.from === 'purchase' || staff.from === 'contract'"
        @click="handleFirstUser"
      >
        {{ $t('selectStaff.submitter') }}
      </el-button>
      <el-button 
        v-if="staff.from === 'bpmn' || staff.from === 'purchase' || staff.from === 'contract'"
        @click="handleCustomUser"
      >
        {{ $t('selectStaff.dynamicAssign') }}
      </el-button>
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import OrgTreeShow from './OrgTreeShow'
import { getCommunityId } from '@/api/community/communityApi'
import { queryStaffInfos } from '@/api/work/itemReleaseDetailApi'

export default {
  name: 'SelectStaff',
  components: {
    OrgTreeShow
  },
  data() {
    return {
      visible: false,
      communityId: '',
      staff: {},
      selectStaffInfo: {
        staffs: [],
        curStaffId: '',
        curStaffName: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(staff) {
      this.staff = staff || {}
      this.visible = true
      this.$nextTick(() => {
        this.$refs.orgTree.refreshTree()
      })
    },
    handleClose() {
      this.selectStaffInfo.staffs = []
      this.selectStaffInfo.curStaffId = ''
    },
    async handleSwitchOrg(org) {
      try {
        const { data } = await queryStaffInfos({
          page: 1,
          rows: 50,
          orgId: org.orgId,
          communityId: this.communityId
        })
        this.selectStaffInfo.staffs = data.staffs
        if (data.staffs.length > 0) {
          this.selectStaffInfo.curStaffId = data.staffs[0].orgId
        }
      } catch (error) {
        console.error('获取员工信息失败:', error)
      }
    },
    handleChangeStaff(item) {
      this.staff.staffId = item.userId
      this.staff.staffName = item.userName
      this.staff.staffTel = item.tel
      this.visible = false
      
      if (typeof this.staff.call === 'function') {
        this.staff.call(this.staff)
      }
    },
    handleFirstUser() {
      this.staff.staffId = '${startUserId}'
      this.staff.staffName = this.$t('selectStaff.submitter')
      this.visible = false
      
      if (typeof this.staff.call === 'function') {
        this.staff.call(this.staff)
      }
    },
    handleCustomUser() {
      this.staff.staffId = '${nextUserId}'
      this.staff.staffName = this.$t('selectStaff.dynamicAssign')
      this.visible = false
      
      if (typeof this.staff.call === 'function') {
        this.staff.call(this.staff)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.section-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  text-align: center;
}

.staff-list {
  max-height: 400px;
  overflow-y: auto;
}

.staff-item {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    background-color: #f5f7fa;
  }
  
  &.selected {
    background-color: #ecf5ff;
    border-color: #d9ecff;
  }
  
  .el-icon-user {
    margin-right: 5px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>