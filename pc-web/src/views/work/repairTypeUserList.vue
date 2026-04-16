<template>
  <div class="padding">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairTypeUser.title') }}</span>
            <div class="ibox-tools" >
              <el-button type="primary" size="small" @click="_goBack">
                <i class="el-icon-close"></i>{{ $t('common.back') }}
              </el-button>
              <el-button type="primary" size="small" @click="_openAddRepairTypeUserModal">
                <i class="el-icon-plus"></i>{{ $t('common.add') }}
              </el-button>
            </div>
          </div>
          <div >
            <el-table :data="repairTypeUserManageInfo.repairTypeUsers" style="width: 100%" border stripe>
              <el-table-column prop="staffId" :label="$t('repairTypeUser.repairMasterId')" align="center" />
              <el-table-column prop="staffName" :label="$t('repairTypeUser.masterName')" align="center" />
              <el-table-column prop="repairTypeName" :label="$t('repairTypeUser.repairType')" align="center" />
              <el-table-column prop="stateName" :label="$t('repairTypeUser.status')" align="center" />
              <el-table-column prop="remark" :label="$t('repairTypeUser.description')" align="center" />
              <el-table-column prop="createTime" :label="$t('repairTypeUser.createTime')" align="center" />
              <el-table-column :label="$t('common.operation')" align="center">
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button size="mini" @click="_openDeleteTypeUserModel(scope.row)">
                      {{ $t('common.delete') }}
                    </el-button>
                    <el-button size="mini" @click="_openEditrepairTypeUserModel(scope.row)">
                      {{ $t('common.change') }}
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper" :total="repairTypeUserManageInfo.total"
              @size-change="handleSizeChange" @current-change="handleCurrentChange" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <SelectStaff ref="selectStaff" @selectStaff="handleSelectStaff" />
    <DeleteRepairTypeUser ref="deleteRepairTypeUser" @success="handleDeleteSuccess" />
    <EditRepairTypeUser ref="editRepairTypeUser" @success="handleEditSuccess" />
  </div>
</template>

<script>
import { listRepairTypeUsers, saveRepairTypeUser } from '@/api/work/repairTypeUserApi'
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'
import DeleteRepairTypeUser from '@/components/work/DeleteRepairTypeUser'
import EditRepairTypeUser from '@/components/work/EditRepairTypeUser'

export default {
  name: 'RepairTypeUserList',
  components: {
    SelectStaff,
    DeleteRepairTypeUser,
    EditRepairTypeUser
  },
  data() {
    return {
      repairTypeUserManageInfo: {
        repairTypeUsers: [],
        total: 0,
        records: 1,
        repairType: '',
        conditions: {
          repairTypeName: '',
          repairWay: '',
          repairType: ''
        }
      },
      currentPage: 1,
      pageSize: 10
    }
  },
  created() {
    this.repairTypeUserManageInfo.repairType = this.$route.query.repairType
    this._listRepairTypeUsers(this.currentPage, this.pageSize)
  },
  methods: {
    async _listRepairTypeUsers(page, size) {
      try {
        const communityId = await getCommunityId()
        const params = {
          page: page,
          row: size,
          communityId: communityId,
          repairType: this.repairTypeUserManageInfo.repairType
        }

        const response = await listRepairTypeUsers(params)
        if (response.code === 0) {
          this.repairTypeUserManageInfo.repairTypeUsers = response.data
          this.repairTypeUserManageInfo.total = response.total
        } else {
          this.$message.error(response.msg || this.$t('common.requestFailed'))
        }
      } catch (error) {
        console.error('Error fetching repair type users:', error)
        this.$message.error(this.$t('common.requestFailed'))
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listRepairTypeUsers(this.currentPage, val)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listRepairTypeUsers(val, this.pageSize)
    },
    _openAddRepairTypeUserModal() {
      this.$refs.selectStaff.open()
    },
    _openEditrepairTypeUserModel(row) {
      this.$refs.editRepairTypeUser.open(row)
    },
    _openDeleteTypeUserModel(row) {
      this.$refs.deleteRepairTypeUser.open(row)
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSelectStaff(staff) {
      this.saveRepairTypeUserInfo(staff)
    },
    async saveRepairTypeUserInfo(staff) {
      try {
        const communityId = await getCommunityId()
        const data = {
          communityId: communityId,
          staffId: staff.staffId,
          staffName: staff.staffName,
          repairType: this.repairTypeUserManageInfo.repairType
        }

        const response = await saveRepairTypeUser(data)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._listRepairTypeUsers(this.currentPage, this.pageSize)
        } else {
          this.$message.error(response.msg || this.$t('common.saveFailed'))
        }
      } catch (error) {
        console.error('Error saving repair type user:', error)
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    handleDeleteSuccess() {
      this._listRepairTypeUsers(this.currentPage, this.pageSize)
    },
    handleEditSuccess() {
      this._listRepairTypeUsers(this.currentPage, this.pageSize)
    }
  }
}
</script>

<style scoped>
.ibox-tools {
  position: absolute;
  right: 15px;
  top: 10px;
}

.clearfix {
  position: relative;
}
</style>