<template>
  <el-dialog :visible="visible" :title="$t('selectStaff.title')" width="80%" @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="12" class="border-right">
        <div class="text-center">
          <h4>{{ $t('selectStaff.orgInfo') }}</h4>
        </div>
        <div class="staff padding">
          <OrgTreeShow ref="orgTree" @org-selected="handleOrgSelected" />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="text-center">
          <h4>{{ $t('selectStaff.staffInfo') }}</h4>
        </div>
        <div class="padding-left staff padding padding-top-xs">
          <div v-for="(item, index) in staffs" :key="index" class="staff-item" 
          :class="{'selected': curStaffId == item.userId }" @click="selectStaff(item)">
            <div>
              <i class="el-icon-user margin-right-xs"></i>
              {{ item.name }}
            </div>
            <div>{{ item.tel }}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import OrgTreeShow from '@/components/org/OrgTreeShow'
import { getStaffInfos } from '@/api/work/repairTypeUserApi'

export default {
  name: 'SelectStaff',
  components: {
    OrgTreeShow
  },
  data() {
    return {
      visible: false,
      staffs: [],
      curStaffId: '',
      selectedStaff: null,
      staffConfig: {},
      currentOrgId: ''
    }
  },
  methods: {
    open() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.orgTree.refreshTree()
      })
    },
    handleClose() {
      this.visible = false
      this.staffs = []
      this.curStaffId = ''
      this.selectedStaff = null
    },
    handleOrgSelected(org) {
      this.currentOrgId = org.orgId
      this.loadStaff(org.orgId)
    },
    async loadStaff(orgId) {
      try {
        const params = {
          orgId: orgId,
          row: 50,
          page: 1
        }
        const {staffs} = await getStaffInfos(params)

          this.staffs = staffs
          if (this.staffs.length > 0) {
            this.curStaffId = this.staffs[0].userId
            this.selectedStaff = this.staffs[0]
          }
      } catch (error) {
        console.error('Error loading staff:', error)
        this.$message.error(this.$t('common.requestFailed'))
      }
    },
    selectStaff(staff) {
      this.curStaffId = staff.userId
      this.selectedStaff = staff
    },
    handleSubmit() {
      if (this.selectedStaff) {
        this.selectedStaff.staffId = this.selectedStaff.userId
        this.selectedStaff.staffName = this.selectedStaff.userName
        this.$emit('selectStaff', this.selectedStaff)
        this.handleClose()
      } else {
        this.$message.warning(this.$t('selectStaff.selectStaffFirst'))
      }
    }
  }
}
</script>

<style scoped>
.border-right {
  border-right: 1px solid #ebeef5;
}

.padding {
  padding: 15px;
}

.staff-item {
  padding: 10px;
  margin-bottom: 10px;
  
  border-radius: 4px;
  cursor: pointer;
}

.staff-item:hover {
  background-color: #f5f7fa;
}

.selected {
  background-color: #ecf5ff;
  border: 1px solid #409eff;
}

.text-center {
  text-align: center;
  margin-bottom: 15px;
}
</style>