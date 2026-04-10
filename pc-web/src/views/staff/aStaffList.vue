<template>
  <div class="padding">
    <el-card class="box-card margin-bottom">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('staff.searchConditions') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input :placeholder="$t('staff.staffId')" v-model="searchConditions.staffId" clearable />
        </el-col>
        <el-col :span="6">
          <el-input :placeholder="$t('staff.staffName')" v-model="searchConditions.name" clearable />
        </el-col>
        <el-col :span="6">
          <el-input :placeholder="$t('staff.phoneNumber')" v-model="searchConditions.tel" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="searchStaff">
            {{ $t('staff.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="clearfix  flex justify-between">
        <span>{{ $t('staff.staffManagement') }}</span>
        <el-button v-if="hasPrivilege(['502022082992310001', '502022101889270183'])" type="primary" size="small"
          class="float-right" @click="openAddStaffPage">
          {{ $t('staff.add') }}
        </el-button>
      </div>

      <el-table :data="staffList" border style="width: 100%">
        <el-table-column prop="userId" :label="$t('staff.staffId')" align="center" />
        <el-table-column prop="name" :label="$t('staff.name')" align="center" />
        <el-table-column prop="tel" :label="$t('staff.phone')" align="center" />
        <el-table-column prop="orgName" :label="$t('staff.relatedOrg')" align="center" />
        <el-table-column prop="relCdName" :label="$t('staff.position')" align="center" />
        <el-table-column prop="idCard" :label="$t('staff.idCard')" align="center" />
        <el-table-column prop="address" :label="$t('staff.address')" align="center" />
        <el-table-column :label="$t('staff.gender')" align="center">
          <template slot-scope="scope">
            {{ scope.row.sex == 0 ? $t('staff.male') : $t('staff.female') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('staff.operations')" align="center" width="300">
          <template slot-scope="scope">
            <el-button v-if="hasPrivilege(['502022082992300002', '502022101832220184'])" size="mini"
              @click="openEditStaff(scope.row)">
              {{ $t('staff.edit') }}
            </el-button>
            <el-button size="mini" @click="openResetPwd(scope.row)">
              {{ $t('staff.resetPassword') }}
            </el-button>
            <el-button
              v-if="scope.row.relCd !== '600311000001' && hasPrivilege(['502022082920350003', '502022101887680185'])"
              size="mini" type="danger" @click="openDeleteStaff(scope.row)">
              {{ $t('staff.delete') }}
            </el-button>
            <el-button size="mini" type="info" @click="openStaffDetail(scope.row)">
              {{ $t('staff.details') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="16">
          <div class="tip-text"></div>
        </el-col>
        <el-col :span="8" class="text-right">
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handlePageChange" />
        </el-col>
      </el-row>
    </el-card>

    <reset-staff-pwd ref="resetPwdDialog" @success="queryStaff" />
    <edit-staff ref="editStaffDialog" @success="queryStaff" />
    <delete-staff ref="deleteStaffDialog" @success="queryStaff" />
  </div>
</template>

<script>
import { queryStaffInfos } from '@/api/staff/aStaffApi'
import ResetStaffPwd from '@/components/staff/resetStaffPwd'
import EditStaff from '@/components/staff/editStaff'
import DeleteStaff from '@/components/staff/deleteStaff'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AStaffList',
  components: {
    ResetStaffPwd,
    EditStaff,
    DeleteStaff
  },
  data() {
    return {
      searchConditions: {
        staffId: '',
        name: '',
        tel: ''
      },
      staffList: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      relCds: []
    }
  },
  created() {
    this.getDictData()
    this.queryStaff()
  },
  methods: {
    async getDictData() {
      try {
        const res = await getDict('u_org_staff_rel', 'rel_cd')
        if (res.code === 0) {
          this.relCds = res.data
        }
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    searchStaff() {
      this.pagination.current = 1
      this.queryStaff()
    },
    async queryStaff() {
      try {
        const params = {
          ...this.searchConditions,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const res = await queryStaffInfos(params)
        console.log(res)

        this.staffList = res.staffs.map(staff => {
          const relCdItem = this.relCds.find(item => item.statusCd === staff.relCd)
          return {
            ...staff,
            relCdName: relCdItem ? relCdItem.name : ''
          }
        })
        this.pagination.total = res.total

      } catch (error) {
        console.error('查询员工信息失败:', error)
      }
    },
    openResetPwd(staff) {
      this.$refs.resetPwdDialog.open(staff)
    },
    openEditStaff(staff) {
      this.$refs.editStaffDialog.open(staff)
    },
    openDeleteStaff(staff) {
      this.$refs.deleteStaffDialog.open(staff)
    },
    openStaffDetail(staff) {
      this.$router.push(`/views/staff/aStaffDetail?staffId=${staff.userId}`)
    },
    openAddStaffPage() {
      this.$router.push('/views/staff/addStaff')
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.queryStaff()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.queryStaff()
    },

  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}

.float-right {
  float: right;
}

.margin-top {
  margin-top: 20px;
}

.margin-bottom {
  margin-bottom: 20px;
}

.text-right {
  text-align: right;
}

.tip-text {
  color: #999;
  font-size: 12px;
  padding: 10px 0;
}
</style>