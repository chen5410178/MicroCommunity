<template>
  <div class="padding">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="box-card">
          <div class="margin-bottom">
            <el-input v-model="aStaffCommunityInfo.orgName" readonly @focus="openChooseOrgTree"
              :placeholder="$t('aStaffCommunity.orgPlaceholder')" />
          </div>
          <div class="margin-bottom">
            <el-input v-model="aStaffCommunityInfo.staffNameLike" :placeholder="$t('aStaffCommunity.staffPlaceholder')"
              @keyup.enter.native="loadStaffs" />
          </div>
          <div class="text-right">
            <el-button type="primary" @click="loadStaffs">{{ $t('common.search') }}</el-button>
          </div>

          <div class="treeview attendance-staff">
            <ul class="list-group">
              <li v-for="(item, index) in aStaffCommunityInfo.staffs" :key="index" @click="swatchStaff(item)"
                :class="{ 'vc-node-selected': aStaffCommunityInfo.curStaffId == item.userId }" class="list-group-item">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20">
        <el-card class="box-card">
          <div class="text-right margin-bottom" v-if="aStaffCommunityInfo.curStaffId">
            <el-button type="primary" size="small" @click="openAddModal">
              {{ $t('aStaffCommunity.linkCommunity') }}
            </el-button>
          </div>

          <el-table :data="aStaffCommunityInfo.communitys" style="width: 100%">
            <el-table-column prop="staffName" :label="$t('aStaffCommunity.staffName')" align="center" />
            <el-table-column prop="staffId" :label="$t('aStaffCommunity.staffId')" align="center" />
            <el-table-column prop="communityId" :label="$t('aStaffCommunity.communityId')" align="center" />
            <el-table-column prop="communityName" :label="$t('aStaffCommunity.communityName')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center">
              <template slot-scope="scope">
                <el-button type="danger" size="mini" @click="openDeleteModal(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="pagination" :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" layout="total, sizes, prev, pager, next" :total="pagination.total"
            @size-change="handleSizeChange" @current-change="handlePageChange" />
        </el-card>
      </el-col>
    </el-row>

    <choose-org-tree ref="chooseOrgTree" @switch-org="handleSwitchOrg" />
    <add-a-staff-community ref="addModal" @success="loadAStaffCommunitys" />
    <delete-a-staff-community ref="deleteModal" @success="loadAStaffCommunitys" />
  </div>
</template>

<script>
import { getStaffInfos, listAStaffCommunity } from '@/api/staff/aStaffCommunityApi'
import ChooseOrgTree from '@/components/org/ChooseOrgTree'
import AddAStaffCommunity from '@/components/staff/addAStaffCommunity'
import DeleteAStaffCommunity from '@/components/staff/deleteAStaffCommunity'

export default {
  name: 'AStaffCommunityList',
  components: {
    ChooseOrgTree,
    AddAStaffCommunity,
    DeleteAStaffCommunity
  },
  data() {
    return {
      aStaffCommunityInfo: {
        staffs: [],
        communitys: [],
        orgId: '',
        orgName: '',
        curStaffId: '',
        staffNameLike: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadStaffs()
  },
  methods: {
    async loadStaffs() {
      try {
        const params = {
          orgId: this.aStaffCommunityInfo.orgId,
          staffName: this.aStaffCommunityInfo.staffNameLike,
          page: 1,
          row: 100
        }

        const {data} = await getStaffInfos(params)
        this.aStaffCommunityInfo.staffs = data.staffs || []

        if (this.aStaffCommunityInfo.staffs.length > 0) {
          this.aStaffCommunityInfo.curStaffId = this.aStaffCommunityInfo.staffs[0].userId
          this.loadAStaffCommunitys()
        }
      } catch (error) {
        console.error('加载员工列表失败:', error)
      }
    },

    async loadAStaffCommunitys() {
      if (!this.aStaffCommunityInfo.curStaffId) return

      try {
        const params = {
          staffId: this.aStaffCommunityInfo.curStaffId,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const {data} = await listAStaffCommunity(params)
        this.aStaffCommunityInfo.communitys = data.data || []
        this.pagination.total = data.total || 0
      } catch (error) {
        console.error('加载小区列表失败:', error)
      }
    },

    openChooseOrgTree() {
      this.$refs.chooseOrgTree.open()
    },

    handleSwitchOrg(org) {
      this.aStaffCommunityInfo.orgId = org.orgId
      this.aStaffCommunityInfo.orgName = org.allOrgName
      this.loadStaffs()
    },

    swatchStaff(staff) {
      this.aStaffCommunityInfo.curStaffId = staff.userId
      this.loadAStaffCommunitys()
    },

    openAddModal() {
      if (!this.aStaffCommunityInfo.curStaffId) {
        this.$message.warning(this.$t('aStaffCommunity.selectStaffFirst'))
        return
      }

      const staffInfo = this.aStaffCommunityInfo.staffs.find(
        s => s.userId === this.aStaffCommunityInfo.curStaffId
      )

      this.$refs.addModal.open({
        staffId: this.aStaffCommunityInfo.curStaffId,
        staffName: staffInfo ? staffInfo.name : ''
      })
    },

    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.loadAStaffCommunitys()
    },

    handlePageChange(current) {
      this.pagination.current = current
      this.loadAStaffCommunitys()
    }
  }
}
</script>

<style scoped>
.margin-bottom {
  margin-bottom: 15px;
}

.text-right {
  text-align: right;
}

.list-group {
  list-style: none;
  padding: 0;
  margin-top: 15px;
}

.list-group-item {
  padding: 10px 15px;
  border: 1px solid #ebeef5;
  cursor: pointer;
  margin-bottom: 5px;
  border-radius: 4px;
}

.list-group-item:hover {
  background-color: #f5f7fa;
}

.vc-node-selected {
  background-color: #ecf5ff;
  color: #409eff;
  border-color: #d9ecff;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>