<template>
  <div class="margin-top">
    <el-table :data="ownerDetailAppUserInfo.appUsers" style="width: 100%">
      <el-table-column prop="communityName" :label="$t('ownerDetailAppUser.communityName')"
        align="center"></el-table-column>
      <el-table-column prop="appUserName" :label="$t('ownerDetailAppUser.relatedOwner')"
        align="center"></el-table-column>
      <el-table-column prop="idCard" :label="$t('ownerDetailAppUser.idCard')" align="center"></el-table-column>
      <el-table-column prop="link" :label="$t('ownerDetailAppUser.phone')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('ownerDetailAppUser.status')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('ownerDetailAppUser.applyTime')" align="center"></el-table-column>
      <el-table-column prop="appTypeName" :label="$t('ownerDetailAppUser.appType')" align="center"></el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="300">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" v-if="scope.row.state == '10000'"
              @click="_openAuditAppUserBindingOwnerModel(scope.row)">
              {{ $t('common.audit') }}
            </el-button>
            <el-button size="mini" @click="_deleteAppUserBindingOwnerModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
            <el-button size="mini" @click="_resetUserPwdModel(scope.row)">
              {{ $t('auditAuthOwner.resetPwd') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
      :page-size="pagination.pageSize" :total="pagination.total" layout="total, prev, pager, next, jumper">
    </el-pagination>

    <audit ref="audit" @auditMessage="auditMessage"></audit>
    <delete-app-user-binding-owner ref="deleteAppUserBindingOwner"
      @refresh="_loadOwnerDetailAppUserData"></delete-app-user-binding-owner>
    <reset-staff-pwd ref="resetStaffPwd"></reset-staff-pwd>
  </div>
</template>

<script>
import Audit from '@/components/staff/Audit'
import DeleteAppUserBindingOwner from './DeleteAppUserBindingOwner'
import ResetStaffPwd from '@/components/staff/resetStaffPwd'
import { listAuditAppUserBindingOwners, updateAppUserBindingOwner } from '@/api/owner/ownerDetailAppUserApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailAppUser',
  components: {
    Audit,
    DeleteAppUserBindingOwner,
    ResetStaffPwd
  },
  data() {
    return {
      ownerDetailAppUserInfo: {
        appUsers: [],
        ownerId: '',
        name: '',
        currentAppUserId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName, link) {
      this.ownerDetailAppUserInfo.ownerId = ownerId
      this.ownerDetailAppUserInfo.ownerName = ownerName
      this.ownerDetailAppUserInfo.link = link
      this._loadOwnerDetailAppUserData(1, this.pagination.pageSize)
    },
    _loadOwnerDetailAppUserData(page, row) {
      const params = {
        communityId: getCommunityId(),
        memberId: this.ownerDetailAppUserInfo.ownerId,
        name: this.ownerDetailAppUserInfo.name,
        page: page,
        row: row
      }

      listAuditAppUserBindingOwners(params).then(response => {
        this.ownerDetailAppUserInfo.appUsers = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _openAuditAppUserBindingOwnerModel(auditAppUserBindingOwner) {
      this.ownerDetailAppUserInfo.currentAppUserId = auditAppUserBindingOwner.appUserId
      this.$refs.audit.open({})
    },
    _auditAppUserBindingOwner(auditInfo) {
      auditInfo.communityId = getCommunityId()
      auditInfo.appUserId = this.ownerDetailAppUserInfo.currentAppUserId

      updateAppUserBindingOwner(auditInfo).then(() => {
        this.$message.success(this.$t('common.operationSuccess'))
        this._loadOwnerDetailAppUserData(1, this.pagination.pageSize)
      }).catch(error => {
        this.$message.error(this.$t('common.processFailed') + error)
      })
    },
    _deleteAppUserBindingOwnerModel(auditAppUserBindingOwner) {
      this.$refs.deleteAppUserBindingOwner.open(auditAppUserBindingOwner)
    },
    _resetUserPwdModel(staff) {
      this.$refs.resetStaffPwd.open(staff)
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailAppUserData(val, this.pagination.pageSize)
    },
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailAppUserInfo.ownerId = data.ownerId
      this._loadOwnerDetailAppUserData(1, this.pagination.pageSize)
    })
  }
}
</script>