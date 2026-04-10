<template>
  <el-row :gutter="20">
    <el-col :span="6" class="border-right">
      <div class="text-center">
        <h4>{{ $t('selectStaff.orgInfo') }}</h4>
      </div>
      <div class="tree-container">
        <org-tree-show @org-selected="handleNodeClick" />
      </div>
    </el-col>

    <el-col :span="6" class="border-right">
      <div class="text-center">
        <h4>{{ $t('selectStaff.staffInfo') }}</h4>
      </div>
      <div class="staff-list">
        <div v-for="staff in staffList" :key="staff.staffId" class="staff-item"
          :class="{ selected: currentStaffId === staff.userId }" @click="selectStaff(staff)">
          <i class="el-icon-user"></i>
          <span>{{ staff.name }}</span>
          <div>{{ staff.tel }}</div>
        </div>
      </div>
    </el-col>

    <el-col :span="6">
      <div class="text-center">
        <h4>{{ $t('selectStaff.selectedStaff') }}</h4>
      </div>
      <div class="selected-staff">
        <div v-for="staff in selectedStaffs" :key="staff.userId" class="selected-item">
          <span>{{ staff.name }}</span>
          <i class="el-icon-close remove-icon" @click="removeStaff(staff)"></i>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { listStaffsByOrgId } from '@/api/inspection/inspectionPlanApi'
import OrgTreeShow from '@/components/org/OrgTreeShow'

export default {
  name: 'SelectStaffsDiv',
  components: {
    OrgTreeShow
  },
  data() {
    return {
      staffList: [],
      selectedStaffs: [],
      currentStaffId: null,
      currentOrgId: null
    }
  },
  methods: {
    setStaffs(staffs) {
      this.selectedStaffs = [...staffs]
    },

    getSelectedStaffs() {
      return this.selectedStaffs
    },

    async handleNodeClick(node) {
      this.currentOrgId = node.orgId
      try {
        const { staffs } = await listStaffsByOrgId({
          orgId: node.orgId,
          page: 1,
          row: 100
        })
        this.staffList = staffs
      } catch (error) {
        console.error('Failed to load staffs:', error)
      }
    },

    selectStaff(staff) {
      this.currentStaffId = staff.userId
      // 检查是否已选择
      const isSelected = this.selectedStaffs.some(s => s.userId === staff.userId)
      if (!isSelected) {
        this.selectedStaffs.push({
          userId: staff.userId,
          name: staff.userName
        })
      }
      this.$emit('selectStaffs', this.selectedStaffs)
    },

    removeStaff(staff) {
      this.selectedStaffs = this.selectedStaffs.filter(s => s.staffId !== staff.staffId)
    }
  }
}
</script>

<style scoped>
.border-right {
  border-right: 1px solid #eee;
}

.tree-container {
  height: 300px;
  overflow: auto;
  padding: 10px;
}

.staff-list {
  height: 300px;
  overflow: auto;
  padding: 10px;
}

.staff-item {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.staff-item:hover {
  background-color: #f5f7fa;
}

.staff-item.selected {
  background-color: #ecf5ff;
}

.selected-staff {
  height: 300px;
  overflow: auto;
  padding: 10px;
}

.selected-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  margin-bottom: 5px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.remove-icon {
  cursor: pointer;
  color: #f56c6c;
}

.text-center {
  text-align: center;
  padding: 10px 0;
}
</style>