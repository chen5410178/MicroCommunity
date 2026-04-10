<template>
  <div class="system-user-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div class="flex justify-between">
          <div class="text-title">{{ $t('systemUserDetail.title') }}</div>
          <div>
            <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
              <i class="el-icon-close"></i>{{ $t('common.back') }}
            </el-button>
          </div>
        </div>
      </div>

      <div class="margin-top">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('systemUserDetail.userId') }}：
              </label>
              <label>{{ systemUserDetailInfo.userId }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('systemUserDetail.userName') }}：
              </label>
              <label>{{ systemUserDetailInfo.userName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('systemUserDetail.idCard') }}：
              </label>
              <label>{{ systemUserDetailInfo.idCard }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('systemUserDetail.tel') }}：
              </label>
              <label>{{ systemUserDetailInfo.tel }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('systemUserDetail.sex') }}：
              </label>
              <label>{{ systemUserDetailInfo.sex == '0' ? $t('common.male') : $t('common.female') }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('systemUserDetail.address') }}：
              </label>
              <label>{{ systemUserDetailInfo.address }}</label>
            </div>
          </el-col>
        </el-row>
      </div>

      <divider></divider>

      <div class="margin-top-sm">
        <el-tabs v-model="systemUserDetailInfo._currentTab" @tab-click="changeTab(systemUserDetailInfo._currentTab)">
          <el-tab-pane :label="$t('systemUserDetail.userAttr')" name="aUserDetailAttr"></el-tab-pane>
          <el-tab-pane :label="$t('systemUserDetail.relatedStaff')" name="aUserDetailStaff"></el-tab-pane>
          <el-tab-pane :label="$t('systemUserDetail.relatedOwner')" name="aOwnerDetailAppUser"></el-tab-pane>
        </el-tabs>
      </div>

      <div v-if="systemUserDetailInfo._currentTab === 'aUserDetailAttr'">
        <a-user-detail-attr ref="aUserDetailAttr" :user-id="systemUserDetailInfo.userId"></a-user-detail-attr>
      </div>
      <div v-if="systemUserDetailInfo._currentTab === 'aUserDetailStaff'">
        <a-user-detail-staff ref="aUserDetailStaff" :user-id="systemUserDetailInfo.userId"></a-user-detail-staff>
      </div>
      <div v-if="systemUserDetailInfo._currentTab === 'aOwnerDetailAppUser'">
        <a-owner-detail-app-user ref="aOwnerDetailAppUser"
          :user-id="systemUserDetailInfo.userId"></a-owner-detail-app-user>
      </div>
    </el-card>

    <audit ref="audit" @auditMessage="handleAuditMessage"></audit>
    <delete-app-user-binding-owner ref="deleteAppUserBindingOwner"></delete-app-user-binding-owner>
    <reset-staff-pwd ref="resetStaffPwd"></reset-staff-pwd>
  </div>
</template>

<script>
import { getSystemUserDetail } from '@/api/staff/systemUserDetailApi'
import AUserDetailAttr from '@/components/staff/AUserDetailAttr'
import AUserDetailStaff from '@/components/staff/AUserDetailStaff'
import AOwnerDetailAppUser from '@/components/staff/AOwnerDetailAppUser'
import Audit from '@/components/staff/Audit'
import DeleteAppUserBindingOwner from '@/components/staff/DeleteAppUserBindingOwner'
import ResetStaffPwd from '@/components/staff/resetStaffPwd'
import divider from '@/components/system/divider.vue'

export default {
  name: 'SystemUserDetailList',
  components: {
    AUserDetailAttr,
    AUserDetailStaff,
    AOwnerDetailAppUser,
    Audit,
    DeleteAppUserBindingOwner,
    ResetStaffPwd,
    divider
  },
  data() {
    return {
      systemUserDetailInfo: {
        userId: '',
        userName: '',
        idCard: '',
        tel: '',
        sex: '',
        address: '',
        _currentTab: 'aUserDetailAttr'
      }
    }
  },
  created() {
    this.systemUserDetailInfo.userId = this.$route.query.userId
    if (this.systemUserDetailInfo.userId) {
      this.loadSystemUser()
    }
  },
  methods: {
    async loadSystemUser() {
      try {
        const params = {
          page: 1,
          row: 1,
          systemUserId: this.systemUserDetailInfo.userId
        }
        const res = await getSystemUserDetail(params)
        if (res.code === 0 && res.data.length > 0) {
          Object.assign(this.systemUserDetailInfo, res.data[0])
          this.changeTab(this.systemUserDetailInfo._currentTab)
        }
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    changeTab(tab) {
      this.systemUserDetailInfo._currentTab = tab
      if (tab === 'aUserDetailAttr') {
        setTimeout(() => {
          this.$refs.aUserDetailAttr.loadData()
        }, 500)
      } else if (tab === 'aUserDetailStaff') {
        setTimeout(() => {
          this.$refs.aUserDetailStaff.loadData()
        }, 500)
      } else if (tab === 'aOwnerDetailAppUser') {
        setTimeout(() => {
          this.$refs.aOwnerDetailAppUser.loadData()
        }, 500)
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    handleAuditMessage(auditInfo) {
      if (this.$refs.aOwnerDetailAppUser) {
        this.$refs.aOwnerDetailAppUser.handleAudit(auditInfo)
      }
    }
  }
}
</script>

<style scoped>
.system-user-detail-container {
  padding: 20px;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

.col-form-label {
  margin-right: 10px;
}
</style>