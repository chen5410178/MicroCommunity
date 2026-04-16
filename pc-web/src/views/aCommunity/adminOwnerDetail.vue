<template>
  <div class="padding">
    <div class="white-bg padding border-radius">
      <div class="flex justify-between">
        <div class="text-title">{{ $t('adminOwnerDetail.ownerInfo') }}</div>
        <div>
          <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <div class="flex justify-start margin-top">
        <div class="text-center vc-float-left" style="width: 150px;" v-if="adminOwnerDetailInfo.faceUrl">
          <img width="120px" height="140px" class="border-radius" :src="adminOwnerDetailInfo.faceUrl"
            @error="errorLoadImg" />
        </div>
        <div class="text-center vc-float-left" style="width: 150px;" v-else>
          <img width="120px" height="140px" class="border-radius" src="/img/noPhoto.jpg" />
        </div>

        <el-row style="min-height: 160px;width: 100%;" class="text-left margin-left">
          <el-col :span="24">
            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.name') }}：</label>
                  <label>{{ adminOwnerDetailInfo.name }}</label>
                </div>
              </el-col>
              <el-col :span="6" v-if="adminOwnerDetailInfo.personType == 'P'">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.sex') }}：</label>
                  <label>{{ adminOwnerDetailInfo.sex == '0' ? $t('common.male') : (adminOwnerDetailInfo.sex == '1' ?
                    $t('common.female') : '') }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.communityName') }}：</label>
                  <label>{{ adminOwnerDetailInfo.communityName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.personType') }}：</label>
                  <label>{{ adminOwnerDetailInfo.personTypeName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.personRole') }}：</label>
                  <label>{{ adminOwnerDetailInfo.personRoleName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.idCard') }}：</label>
                  <label>{{ adminOwnerDetailInfo.idCard }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.contact') }}：</label>
                  <label>{{ adminOwnerDetailInfo.link }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.backupPhone') }}：</label>
                  <label>{{ adminOwnerDetailInfo.concactLink }}</label>
                </div>
              </el-col>
              <el-col :span="6" v-if="adminOwnerDetailInfo.personType == 'C'">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.contactPerson') }}：</label>
                  <label>{{ adminOwnerDetailInfo.concactPerson }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.creator') }}：</label>
                  <label>{{ adminOwnerDetailInfo.userName }}</label>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <label class="col-form-label">{{ $t('adminOwnerDetail.remark') }}：</label>
                  <label>{{ adminOwnerDetailInfo.remark }}</label>
                </div>
              </el-col>
            </el-row>

            <div v-for="(item, index) in adminOwnerDetailInfo.ownerAttrDtos" :key="index">
              <el-row v-if="index % 4 == 0">
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">{{ item.specName }}：</label>
                    <label>{{ item.valueName }}</label>
                  </div>
                </el-col>
                <el-col :span="6" v-if="index < adminOwnerDetailInfo.ownerAttrDtos.length - 1">
                  <div class="form-group">
                    <label class="col-form-label">{{ adminOwnerDetailInfo.ownerAttrDtos[index + 1].specName }}：</label>
                    <label>{{ adminOwnerDetailInfo.ownerAttrDtos[index + 1].valueName }}</label>
                  </div>
                </el-col>
                <el-col :span="6" v-if="index < adminOwnerDetailInfo.ownerAttrDtos.length - 2">
                  <div class="form-group">
                    <label class="col-form-label">{{ adminOwnerDetailInfo.ownerAttrDtos[index + 2].specName }}：</label>
                    <label>{{ adminOwnerDetailInfo.ownerAttrDtos[index + 2].valueName }}</label>
                  </div>
                </el-col>
                <el-col :span="6" v-if="index < adminOwnerDetailInfo.ownerAttrDtos.length - 3">
                  <div class="form-group">
                    <label class="col-form-label">{{ adminOwnerDetailInfo.ownerAttrDtos[index + 3].specName }}：</label>
                    <label>{{ adminOwnerDetailInfo.ownerAttrDtos[index + 3].valueName }}</label>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>

      <Divider />

      <div class="margin-top-sm">
        <el-tabs v-model="adminOwnerDetailInfo._currentTab"
          @tab-click="handleTabClick(adminOwnerDetailInfo._currentTab)">
          <el-tab-pane :label="$t('adminOwnerDetail.house')" name="aRoomDetailRoom"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.car')" name="aRoomDetailCar"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.contract')" name="aRoomDetailContract"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.member')" name="aRoomDetailMember"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.modifyRecord')" name="aOwnerDetailHis"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.houseFee')" name="aOwnerDetailRoomFee"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.paymentHistory')" name="aRoomDetailHisFee"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.account')" name="aOwnerDetailAccount"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.coupon')" name="aOwnerDetailCoupon"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.ownerBinding')" name="aOwnerDetailAppUser"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.complaint')" name="aRoomDetailComplaint"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.repair')" name="aRoomDetailRepair"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.visitor')" name="aOwnerDetailVisit"
            v-if="adminOwnerDetailInfo.link"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.paymentReceipt')" name="aRoomDetailReceipt"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.depositReceipt')" name="aOwnerDetailAcctReceipt"></el-tab-pane>
          <el-tab-pane :label="$t('adminOwnerDetail.faceSync')" name="aOwnerDetailAccessControl"></el-tab-pane>
        </el-tabs>
      </div>

      <component :is="adminOwnerDetailInfo._currentTab" :ref="adminOwnerDetailInfo._currentTab"></component>
    </div>
  </div>
</template>

<script>
import { getOwnerDetail } from '@/api/aCommunity/adminOwnerDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
//import { getDict } from '@/api/community/communityApi'

// 导入所有子组件
import ARoomDetailRoom from '@/components/fee/aRoomDetailRoom'
import ARoomDetailCar from '@/components/aCommunity/aRoomDetailCar'
import ARoomDetailContract from '@/components/aCommunity/aRoomDetailContract'
import ARoomDetailMember from '@/components/aCommunity/aRoomDetailMember'
import ARoomDetailHisFee from '@/components/fee/aRoomDetailHisFee'
import ARoomDetailComplaint from '@/components/aCommunity/aRoomDetailComplaint'
import ARoomDetailRepair from '@/components/aCommunity/aRoomDetailRepair'
import ARoomDetailReceipt from '@/components/fee/aRoomDetailReceipt'
import AOwnerDetailHis from '@/components/aCommunity/aOwnerDetailHis'
import AOwnerDetailRoomFee from '@/components/aCommunity/aOwnerDetailRoomFee'
import AOwnerDetailAccount from '@/components/fee/aOwnerDetailAccount'
import AOwnerDetailCoupon from '@/components/aCommunity/aOwnerDetailCoupon'
import AOwnerDetailAppUser from '@/components/staff/AOwnerDetailAppUser'
import AOwnerDetailVisit from '@/components/aCommunity/aOwnerDetailVisit'
import AOwnerDetailAcctReceipt from '@/components/fee/aOwnerDetailAcctReceipt'
import AOwnerDetailAccessControl from '@/components/aCommunity/aOwnerDetailAccessControl'
import Divider from '@/components/system/divider'

export default {
  name: 'AdminOwnerDetail',
  components: {
    ARoomDetailRoom,
    ARoomDetailCar,
    ARoomDetailContract,
    ARoomDetailMember,
    ARoomDetailHisFee,
    ARoomDetailComplaint,
    ARoomDetailRepair,
    ARoomDetailReceipt,
    AOwnerDetailHis,
    AOwnerDetailRoomFee,
    AOwnerDetailAccount,
    AOwnerDetailCoupon,
    AOwnerDetailAppUser,
    AOwnerDetailVisit,
    AOwnerDetailAcctReceipt,
    AOwnerDetailAccessControl,
    Divider
  },
  data() {
    return {
      adminOwnerDetailInfo: {
        viewOwnerFlag: '',
        ownerId: "",
        memberId: '',
        name: "",
        age: "",
        sex: "",
        userName: "",
        remark: "",
        idCard: "",
        link: "",
        ownerPhoto: "/img/noPhoto.jpg",
        ownerAttrDtos: [],
        faceUrl: '',
        _currentTab: 'aRoomDetailRoom',
        personType: '',
        personTypeName: '',
        personRole: '',
        personRoleName: '',
        concactLink: '',
        concactPerson: '',
        communityName: '',
        needBack: false
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.adminOwnerDetailInfo.ownerId = this.$route.query.ownerId
    this.adminOwnerDetailInfo.needBack = this.$route.query.needBack

    if (!this.adminOwnerDetailInfo.ownerId) {
      return
    }

    const currentTab = this.$route.query.currentTab
    if (currentTab) {
      this.adminOwnerDetailInfo._currentTab = currentTab
    }

    this.loadAdminOwnerInfo()
  },
  methods: {
    async loadAdminOwnerInfo() {
      try {
        const params = {
          ownerId: this.adminOwnerDetailInfo.ownerId,
          page: 1,
          row: 1,
          ownerTypeCd: '1001'
        }

        const response = await getOwnerDetail(params)
        const data = response.data[0]
        Object.assign(this.adminOwnerDetailInfo, data)
        this.adminOwnerDetailInfo.ownerAttrDtos = response.data[0].ownerAttrDtos

        // 加载当前标签页组件
        this.handleTabClick(this.adminOwnerDetailInfo._currentTab)
      } catch (error) {
        console.error('获取业主详情失败:', error)
      }
    },
    handleTabClick(tab) {
      this.adminOwnerDetailInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab] && this.$refs[tab].open) {
          this.$refs[tab].open({
            ownerId: this.adminOwnerDetailInfo.ownerId,
            ownerName: this.adminOwnerDetailInfo.name,
            link: this.adminOwnerDetailInfo.link,
          })
        }
      }, 500)
    },
    goBack() {
      this.$router.go(-1)
    },
    errorLoadImg() {
      this.adminOwnerDetailInfo.ownerPhoto = "/img/noPhoto.jpg"
    }
  }
}
</script>

<style scoped>
.white-bg {
  background-color: #fff;
}

.padding-lg {
  padding: 20px;
}

.padding-top {
  padding-top: 15px;
}

.border-radius {
  border-radius: 4px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.margin-top {
  margin-top: 15px;
}

.margin-top-sm {
  margin-top: 10px;
}

.vc-float-left {
  float: left;
}

.text-center {
  text-align: center;
}

.vc-line-primary {
  border-top: 1px solid #409EFF;
}

.form-group {
  margin-bottom: 15px;
}

.col-form-label {
  margin-right: 5px;
}
</style>