<template>
  <div class="padding">
    <el-card class="box-card">
      <div class="flex justify-between">
        <div class="text-title">{{ $t('aStaffDetailList.staffInfo') }}</div>
        <div>
          <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
            <i class="el-icon-close"></i>{{ $t('aStaffDetailList.back') }}
          </el-button>
          <el-button v-if="hasPrivilege('502022082992300002,502022101832220184')" type="primary" size="small"
            style="margin-left:10px" @click="openEditStaffModel">
            <i class="el-icon-edit"></i>{{ $t('aStaffDetailList.edit') }}
          </el-button>
          <el-button type="primary" size="small" style="margin-left:10px" @click="resetStaffPwd">
            <i class="el-icon-refresh"></i>{{ $t('aStaffDetailList.resetPwd') }}
          </el-button>
        </div>
      </div>

      <div class="margin-top flex justify-start">
        <div class="text-center vc-float-left" style="width: 150px;" v-if="staffDetailInfo.photo">
          <img width="120px" height="140px" class="border-radius" :src="staffDetailInfo.photo" @error="errorLoadImg" />
        </div>
        <div class="text-center vc-float-left" style="width: 150px;" v-else>
          <img width="120px" height="140px" class="border-radius" src="/img/noPhoto.jpg" />
        </div>
        <el-row style="min-height: 160px;width: 100%; " class="text-left">
          <el-col :span="24">
            <el-row class="margin-bottom margin-top">
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('aStaffDetailList.staffId') }}：</label>
                  <label>{{ staffDetailInfo.staffId }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('aStaffDetailList.userName') }}：</label>
                  <label>{{ staffDetailInfo.userName }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('aStaffDetailList.idCard') }}：</label>
                  <label>{{ staffDetailInfo.idCard }}</label>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('aStaffDetailList.tel') }}：</label>
                  <label>{{ staffDetailInfo.tel }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('aStaffDetailList.sex') }}：</label>
                  <label>{{ staffDetailInfo.sex == '0' ? $t('aStaffDetailList.male') : $t('aStaffDetailList.female')
                  }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('aStaffDetailList.address') }}：</label>
                  <label>{{ staffDetailInfo.address }}</label>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>

      <divider />

      <div class="margin-top-sm">
        <el-tabs v-model="staffDetailInfo.currentTab">
          <el-tab-pane :label="$t('aStaffDetailList.orgPrivilege')" name="staffDetailOrgPrivilege"></el-tab-pane>
          <el-tab-pane :label="$t('aStaffDetailList.community')" name="aStaffDetailCommunitys"></el-tab-pane>
        </el-tabs>
      </div>
      <div v-if="staffDetailInfo.currentTab !== 'staffDetailOrgPrivilege'">
        <a-staff-detail-communitys ref="communitys" :staff-id="staffDetailInfo.staffId" />
      </div>
    </el-card>

    <div v-if="staffDetailInfo.currentTab === 'staffDetailOrgPrivilege'">
      <staff-detail-org-privilege ref="orgPrivilege" :staff-id="staffDetailInfo.staffId" />
    </div>


    <reset-staff-pwd ref="resetPwd" @success="handleResetSuccess" />
    <edit-staff ref="editStaff" @success="handleEditSuccess" />
  </div>
</template>

<script>
import { getStaffInfo } from '@/api/staff/aStaffDetailApi'
import StaffDetailOrgPrivilege from '@/components/staff/staffDetailOrgPrivilege'
import AStaffDetailCommunitys from '@/components/staff/aStaffDetailCommunitys'
import ResetStaffPwd from '@/components/staff/resetStaffPwd'
import EditStaff from '@/components/staff/editStaff'

import divider from '@/components/system/divider'

export default {
  name: 'AStaffDetailList',
  components: {
    StaffDetailOrgPrivilege,
    AStaffDetailCommunitys,
    ResetStaffPwd,
    EditStaff,
    divider
  },
  data() {
    return {
      staffDetailInfo: {
        staffId: '',
        userName: '',
        idCard: '',
        tel: '',
        sex: '',
        address: '',
        photo: '/img/noPhoto.jpg',
        currentTab: 'staffDetailOrgPrivilege'
      }
    }
  },
  created() {
    this.staffDetailInfo.staffId = this.$route.query.staffId
    if (this.staffDetailInfo.staffId) {
      this.loadStaffInfo()
    }
  },
  methods: {
    async loadStaffInfo() {
      try {
        const response = await getStaffInfo({
          staffId: this.staffDetailInfo.staffId,
          row: 1,
          page: 1
        })
        Object.assign(this.staffDetailInfo, response.staffs[0])
        //this.staffDetailInfo.photo = `/callComponent/download/getFile/fileByObjId?objId=${this.staffDetailInfo.staffId}&communityId=${this.getCommunityId()}&fileTypeCd=12000&time=${new Date().getTime()}`
      } catch (error) {
        this.$message.error(this.$t('aStaffDetailList.loadError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    openEditStaffModel() {
      this.$refs.editStaff.open(this.staffDetailInfo)
    },
    resetStaffPwd() {
      this.$refs.resetPwd.open({
        userId: this.staffDetailInfo.staffId,
        name: this.staffDetailInfo.userName
      })
    },
    errorLoadImg() {
      this.staffDetailInfo.photo = "/img/noPhoto.jpg"
    },
    handleResetSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    },
    handleEditSuccess() {
      this.loadStaffInfo()
      this.$message.success(this.$t('common.operationSuccess'))
    },

  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.vc-float-left {
  float: left;
}

.border-radius {
  border-radius: 4px;
}

.vc-line-primary {
  height: 1px;
  background-color: #409EFF;
  margin: 20px 0;
}
</style>