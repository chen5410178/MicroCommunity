<template>
  <div class="staff-community-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="box-card">
          <div class="margin-bottom">
            <el-input v-model="staffCommunityInfo.orgName" readonly @focus="handleOrgChange"
              :placeholder="$t('staffCommunity.orgPlaceholder')" />
          </div>
          <div class="margin-bottom">
            <el-input v-model="staffCommunityInfo.staffNameLike"
              :placeholder="$t('staffCommunity.staffNamePlaceholder')" @keyup.enter.native="loadStaffs" />
          </div>
          <div class="text-right">
            <el-button type="primary" @click="loadStaffs">
              {{ $t('common.search') }}
            </el-button>
          </div>

          <div class="staff-list">
            <ul class="staff-ul">
              <li v-for="(item, index) in staffCommunityInfo.staffs" :key="index" @click="switchStaff(item)"
                :class="{ 'active-staff': staffCommunityInfo.curStaffId === item.userId }">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div class="text-right margin-bottom" v-if="staffCommunityInfo.curStaffId" >
            <el-button type="primary" size="small" @click="openAddModal">
              {{ $t('staffCommunity.linkCommunity') }}
            </el-button>
          </div>

          <el-table :data="staffCommunityInfo.communitys"  border style="width: 100%" v-loading="loading">
            <el-table-column prop="staffName" :label="$t('staffCommunity.staffName')" align="center" />
            <el-table-column prop="staffId" :label="$t('staffCommunity.staffId')" align="center" />
            <el-table-column prop="communityId" :label="$t('staffCommunity.communityId')" align="center" />
            <el-table-column prop="communityName" :label="$t('staffCommunity.communityName')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <choose-org-tree ref="chooseOrgTree" @switchOrg="handleSwitchOrg" />
    <add-staff-community ref="addStaffCommunity" @success="handleSuccess" />
    <delete-staff-community ref="deleteStaffCommunity" @success="handleSuccess" />
  </div>
</template>

<script>
import { listStaffs, listStaffCommunity } from '@/api/staff/staffCommunityApi'
import ChooseOrgTree from '@/components/org/ChooseOrgTree'
import AddStaffCommunity from '@/components/staff/addStaffCommunity'
import DeleteStaffCommunity from '@/components/staff/deleteStaffCommunity'

export default {
  name: 'StaffCommunityList',
  components: {
    ChooseOrgTree,
    AddStaffCommunity,
    DeleteStaffCommunity
  },
  data() {
    return {
      loading: false,
      staffCommunityInfo: {
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
        this.loading = true
        const params = {
          page: 1,
          row: 100,
          orgId: this.staffCommunityInfo.orgId,
          staffName: this.staffCommunityInfo.staffNameLike
        }
        const data  = await listStaffs(params)
        this.staffCommunityInfo.staffs = data.staffs || []
        if (this.staffCommunityInfo.staffs.length > 0) {
          this.staffCommunityInfo.curStaffId = this.staffCommunityInfo.staffs[0].userId
          this.loadStaffCommunitys()
        }
      } catch (error) {
        this.$message.error(this.$t('staffCommunity.fetchStaffError'))
      } finally {
        this.loading = false
      }
    },
    async loadStaffCommunitys() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          staffId: this.staffCommunityInfo.curStaffId
        }
        const { data, total } = await listStaffCommunity(params)
        this.staffCommunityInfo.communitys = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('staffCommunity.fetchCommunityError'))
      } finally {
        this.loading = false
      }
    },
    handleOrgChange() {
      this.$refs.chooseOrgTree.open()
    },
    handleSwitchOrg(org) {
      this.staffCommunityInfo.orgId = org.orgId
      this.staffCommunityInfo.orgName = org.allOrgName
      this.loadStaffs()
    },
    switchStaff(staff) {
      this.staffCommunityInfo.curStaffId = staff.userId
      this.pagination.current = 1
      this.loadStaffCommunitys()
    },
    openAddModal() {
      this.$refs.addStaffCommunity.open({
        staffId: this.staffCommunityInfo.curStaffId
      })
    },
    openDeleteModal(row) {
      this.$refs.deleteStaffCommunity.open(row)
    },
    handleSuccess() {
      this.loadStaffCommunitys()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadStaffCommunitys()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadStaffCommunitys()
    }
  }
}
</script>

<style lang="scss" scoped>
.staff-community-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .margin-bottom {
    margin-bottom: 15px;
  }

  .staff-list {
    margin-top: 15px;
    max-height: 500px;
    overflow-y: auto;

    .staff-ul {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        text-align: center;
        cursor: pointer;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active-staff {
          background-color: #409EFF;
          color: white;
        }
      }
    }
  }

  .el-pagination {
    margin-top: 15px;
    text-align: right;
  }
}
</style>