<template>
  <el-dialog
    :title="$t('selectStaff.title')"
    :visible.sync="dialogVisible"
    width="80%"
  >
    <el-row>
      <el-col :span="12" class="border-right">
        <div class="text-center">
          <h4>{{ $t('selectStaff.orgInfo') }}</h4>
        </div>
        <div class="tree-container">
          <org-tree-show
            ref="orgTreeShow"
            :call-back-listener="callBackListener"
          />
        </div>
      </el-col>
      
      <el-col :span="12">
        <div class="text-center">
          <h4>{{ $t('selectStaff.staffInfo') }}</h4>
        </div>
        <div class="staff-list">
          <div
            v-for="(item, index) in selectStaffInfo.staffs"
            :key="index"
            class="staff-item"
            :class="{ 'selected': selectStaffInfo.curStaffId === item.staffId }"
            @click="changeStaff(item)"
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
      <el-button @click="firstUser">
        {{ $t('selectStaff.submitter') }}
      </el-button>
      <el-button type="primary" @click="customUser">
        {{ $t('selectStaff.dynamicAssign') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { queryStaffInfos } from '@/api/contract/contractChangeDetailApi'
import OrgTreeShow from '@/components/contract/OrgTreeShow'

export default {
  name: 'SelectStaff',
  components: {
    OrgTreeShow
  },
  data() {
    return {
      dialogVisible: false,
      selectStaffInfo: {
        staffs: [],
        curStaffId: '',
        curStaffName: '',
        staff: {}
      },
      callBackListener: 'selectStaff'
    }
  },
  methods: {
    open(staff) {
      this.selectStaffInfo.staff = staff
      this.dialogVisible = true
      this.$refs.orgTreeShow.loadOrgsShow()
    },
    changeStaff(item) {
      this.selectStaffInfo.curStaffId = item.staffId
      this.selectStaffInfo.curStaffName = item.name
      
      if (this.selectStaffInfo.staff) {
        this.selectStaffInfo.staff.staffId = item.userId
        this.selectStaffInfo.staff.staffName = item.userName
        this.selectStaffInfo.staff.staffTel = item.tel
        
        if (this.selectStaffInfo.staff.call) {
          this.selectStaffInfo.staff.call(this.selectStaffInfo.staff)
        }
      }
      
      this.dialogVisible = false
    },
    loadStaff(org) {
      const param = {
        page: 1,
        rows: 50,
        orgId: org.orgId
      }

      queryStaffInfos(param)
        .then(response => {
          const staffInfo = response.data
          this.selectStaffInfo.staffs = staffInfo.staffs
          if (staffInfo.staffs.length > 0) {
            this.selectStaffInfo.curStaffId = staffInfo.staffs[0].orgId
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    firstUser() {
      if (this.selectStaffInfo.staff) {
        this.selectStaffInfo.staff.staffId = '${startUserId}'
        this.selectStaffInfo.staff.staffName = this.$t('selectStaff.submitter')
        
        if (this.selectStaffInfo.staff.call) {
          this.selectStaffInfo.staff.call(this.selectStaffInfo.staff)
        }
      }
      this.dialogVisible = false
    },
    customUser() {
      if (this.selectStaffInfo.staff) {
        this.selectStaffInfo.staff.staffId = '${nextUserId}'
        this.selectStaffInfo.staff.staffName = this.$t('selectStaff.dynamicAssign')
        
        if (this.selectStaffInfo.staff.call) {
          this.selectStaffInfo.staff.call(this.selectStaffInfo.staff)
        }
      }
      this.dialogVisible = false
    }
  },
  created() {
    this.$on('switchOrg', param => {
      this.loadStaff(param)
    })
  }
}
</script>

<style scoped>
.border-right {
  border-right: 1px solid #eee;
  padding-right: 20px;
}

.tree-container {
  height: 400px;
  overflow-y: auto;
}

.staff-list {
  height: 400px;
  overflow-y: auto;
  padding-left: 20px;
}

.staff-item {
  padding: 10px;
  margin-bottom: 5px;
  cursor: pointer;
  border-radius: 4px;
}

.staff-item:hover {
  background-color: #f5f7fa;
}

.staff-item.selected {
  background-color: #ecf5ff;
  color: #409eff;
}

.text-center {
  text-align: center;
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}
</style>