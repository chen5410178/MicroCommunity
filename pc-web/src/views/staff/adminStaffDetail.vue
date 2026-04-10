<template>
  <div class="admin-staff-detail-container">
    <el-card class="box-card">
      <div slot="header" class="header">
        <span>{{ $t('adminStaffDetail.title') }}</span>
        <el-button type="primary" size="small" @click="goBack">
          <i class="el-icon-back"></i> {{ $t('common.back') }}
        </el-button>
      </div>
      <div class="content">
        <el-row :gutter="20">
          <el-col :span="4" class="text-center">
            <el-image :src="adminStaffDetailInfo.photo || '/img/noPhoto.jpg'"
              style="width: 120px; height: 140px; border-radius: 4px;" @error="errorLoadImg" />
          </el-col>
          <el-col :span="20">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form label-width="80px">
                  <el-form-item :label="$t('adminStaffDetail.staffId')">
                    <span>{{ adminStaffDetailInfo.staffId }}</span>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col :span="8">
                <el-form label-width="80px">
                  <el-form-item :label="$t('adminStaffDetail.userName')">
                    <span>{{ adminStaffDetailInfo.userName }}</span>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col :span="8">
                <el-form label-width="80px">
                  <el-form-item :label="$t('adminStaffDetail.idCard')">
                    <span>{{ adminStaffDetailInfo.idCard }}</span>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form label-width="80px">
                  <el-form-item :label="$t('adminStaffDetail.tel')">
                    <span>{{ adminStaffDetailInfo.tel }}</span>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col :span="8">
                <el-form label-width="80px">
                  <el-form-item :label="$t('adminStaffDetail.sex')">
                    <span>{{ adminStaffDetailInfo.sex === '0' ? $t('adminStaffDetail.male') :
                      $t('adminStaffDetail.female') }}</span>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col :span="8">
                <el-form label-width="80px">
                  <el-form-item :label="$t('adminStaffDetail.address')">
                    <span>{{ adminStaffDetailInfo.address }}</span>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <divider></divider>
        <el-tabs v-model="adminStaffDetailInfo._currentTab"
          @tab-click="handleTabChange(adminStaffDetailInfo._currentTab)">
          <el-tab-pane :label="$t('adminStaffDetail.privilege')" name="aStaffDetailPrivilege">
            <a-staff-detail-privilege ref="aStaffDetailPrivilege"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailPrivilege'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.classes')" name="aStaffDetailClasses">
            <a-staff-detail-classes ref="aStaffDetailClasses"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailClasses'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.attendance')" name="aStaffDetailAttendance">
            <a-staff-detail-attendance ref="aStaffDetailAttendance"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailAttendance'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.repair')" name="aStaffDetailRepair">
            <a-staff-detail-repair ref="aStaffDetailRepair"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailRepair'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.inspection')" name="aStaffDetailInspection">
            <a-staff-detail-inspection ref="aStaffDetailInspection"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailInspection'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.maintainance')" name="aStaffDetailMaintainance">
            <a-staff-detail-maintainance ref="aStaffDetailMaintainance"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailMaintainance'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.purchaseApply')" name="aStaffDetailPurchaseApply">
            <a-staff-detail-purchase-apply ref="aStaffDetailPurchaseApply"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailPurchaseApply'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.itemOutApply')" name="aStaffDetailItemOutApply">
            <a-staff-detail-item-out-apply ref="aStaffDetailItemOutApply"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailItemOutApply'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.hisFee')" name="aStaffDetailHisFee">
            <a-staff-detail-his-fee ref="aStaffDetailHisFee"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailHisFee'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.applyReturnFee')" name="aStaffDetailApplyReturnFee">
            <a-staff-detail-apply-return-fee ref="aStaffDetailApplyReturnFee"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailApplyReturnFee'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.contract')" name="aStaffDetailContract">
            <a-staff-detail-contract ref="aStaffDetailContract"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailContract'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.appAuth')" name="aStaffDetailAppAuth">
            <a-staff-detail-app-auth ref="aStaffDetailAppAuth"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailAppAuth'" />
          </el-tab-pane>
          <el-tab-pane :label="$t('adminStaffDetail.accessControl')" name="aStaffDetailAccessControl">
            <a-staff-detail-access-control ref="aStaffDetailAccessControl"
              v-if="adminStaffDetailInfo._currentTab === 'aStaffDetailAccessControl'" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>
  
<script>
import { getStaffInfo } from '@/api/staff/adminStaffDetailApi'
import AStaffDetailPrivilege from '@/components/staff/AStaffDetailPrivilege.vue'
import AStaffDetailClasses from '@/components/staff/AStaffDetailClasses.vue'
import AStaffDetailAttendance from '@/components/staff/AStaffDetailAttendance.vue'
import AStaffDetailRepair from '@/components/staff/AStaffDetailRepair.vue'
import AStaffDetailInspection from '@/components/staff/AStaffDetailInspection.vue'
import AStaffDetailMaintainance from '@/components/staff/AStaffDetailMaintainance.vue'
import AStaffDetailPurchaseApply from '@/components/staff/AStaffDetailPurchaseApply.vue'
import AStaffDetailItemOutApply from '@/components/staff/AStaffDetailItemOutApply.vue'
import AStaffDetailHisFee from '@/components/staff/AStaffDetailHisFee.vue'
import AStaffDetailApplyReturnFee from '@/components/staff/AStaffDetailApplyReturnFee.vue'
import AStaffDetailContract from '@/components/staff/AStaffDetailContract.vue'
import AStaffDetailAppAuth from '@/components/staff/AStaffDetailAppAuth.vue'
import AStaffDetailAccessControl from '@/components/staff/AStaffDetailAccessControl.vue'
import divider from '@/components/system/divider.vue'

export default {
  name: 'AdminStaffDetail',
  components: {
    AStaffDetailPrivilege,
    AStaffDetailClasses,
    AStaffDetailAttendance,
    AStaffDetailRepair,
    AStaffDetailInspection,
    AStaffDetailMaintainance,
    AStaffDetailPurchaseApply,
    AStaffDetailItemOutApply,
    AStaffDetailHisFee,
    AStaffDetailApplyReturnFee,
    AStaffDetailContract,
    AStaffDetailAppAuth,
    AStaffDetailAccessControl,
    divider
  },
  data() {
    return {
      adminStaffDetailInfo: {
        staffId: '',
        userName: '',
        idCard: '',
        tel: '',
        sex: '',
        address: '',
        photo: '/img/noPhoto.jpg',
        url: '',
        relCd: '',
        _currentTab: 'aStaffDetailPrivilege',
        needBack: false
      }
    }
  },
  created() {
    this.adminStaffDetailInfo.staffId = this.$route.query.staffId
    this.loadStaffInfo()
    this.handleTabChange(this.adminStaffDetailInfo._currentTab)

  },
  methods: {
    async loadStaffInfo() {
      try {
        const { data } = await getStaffInfo({ staffId: this.adminStaffDetailInfo.staffId, page: 1, row: 1 })
        Object.assign(this.adminStaffDetailInfo, data.staffs[0])
      } catch (error) {
        this.$message.error(this.$t('adminStaffDetail.fetchError'))
      }
    },
    handleTabChange(_tabName) {
      const refName = _tabName
      setTimeout(() => {
        this.$refs[refName].switchTab({
          staffId: this.adminStaffDetailInfo.staffId,
          staffName: this.adminStaffDetailInfo.userName,
          tel: this.adminStaffDetailInfo.tel
        })
      }, 500)

    },
    errorLoadImg() {
      this.adminStaffDetailInfo.photo = '/img/noPhoto.jpg'
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>
  
<style lang="scss" scoped>
.admin-staff-detail-container {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .content {
    margin-top: 20px;
  }

  .el-divider {
    margin: 20px 0;
  }
}
</style>