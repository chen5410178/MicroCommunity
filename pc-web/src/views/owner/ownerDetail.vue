<template>
  <div class="white-bg padding-lg padding-top border-radius margin">
    <el-row type="flex" justify="space-between">
      <div class="text-title">{{ $t('ownerDetail.ownerInfo') }}</div>
      <div>
        <el-button v-if="ownerDetailInfo.needBack" type="primary" size="small" style="margin-left:10px"
          @click="$router.go(-1)">
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" @click="_openEditOwnerDetailModel">
          <i class="el-icon-edit"></i>{{ $t('common.edit') }}
        </el-button>
      </div>
    </el-row>

    <!-- Owner Information -->
    <div class="margin-top ">
      <el-row style="min-height: 160px;">
        <el-col :span="3">
          <div class="text-center vc-float-left" style="width: 150px;" v-if="ownerDetailInfo.faceUrl">
            <img width="120px" height="140px" class="border-radius" :src="ownerDetailInfo.faceUrl"
              @error="errorLoadImg" />
          </div>
          <div class="text-center vc-float-left" style="width: 150px;" v-else>
            <img width="120px" height="140px" class="border-radius" src="/img/noPhoto.jpg" />
          </div>
        </el-col>
        <el-col :span="21" class="text-left">
          <el-form>
            <el-row>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.ownerId')">
                  <div>{{ ownerDetailInfo.ownerId }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.name')">
                  <div>{{ ownerDetailInfo.name }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6" v-if="ownerDetailInfo.personType == 'P'">
                <el-form-item :label="$t('ownerDetail.gender')">
                  <div>
                    {{ ownerDetailInfo.sex == '0' ? $t('common.male') : (ownerDetailInfo.sex == '1' ?
                      $t('common.female')
                      : '') }}
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.personType')">
                  <div>{{ ownerDetailInfo.personTypeName }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.personRole')">
                  <div>{{ ownerDetailInfo.personRoleName }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.idCard')">
                  <div>{{ ownerDetailInfo.idCard }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.contact')">
                  <div>{{ ownerDetailInfo.link }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.backupPhone')">
                  <div>{{ ownerDetailInfo.concactLink }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6" v-if="ownerDetailInfo.personType == 'C'">
                <el-form-item :label="$t('ownerDetail.contactPerson')">
                  <div>{{ ownerDetailInfo.concactPerson }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('ownerDetail.creator')">
                  <div>{{ ownerDetailInfo.userName }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('ownerDetail.remark')">
                  <div>{{ ownerDetailInfo.remark }}</div>
                </el-form-item>
              </el-col>
            </el-row>
            <div v-for="(item, index) in ownerDetailInfo.ownerAttrDtos" :key="index">
              <el-row v-if="index % 4 == 0">
                <el-col :span="6">
                  <el-form-item :label="item.specName + ':'">
                    <div>{{ item.valueName }}</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6" v-if="index < ownerDetailInfo.ownerAttrDtos.length - 1">
                  <el-form-item :label="ownerDetailInfo.ownerAttrDtos[index + 1].specName + ':'">
                    <div>{{ ownerDetailInfo.ownerAttrDtos[index + 1].valueName }}</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6" v-if="index < ownerDetailInfo.ownerAttrDtos.length - 2">
                  <el-form-item :label="ownerDetailInfo.ownerAttrDtos[index + 2].specName + ':'">
                    <div>{{ ownerDetailInfo.ownerAttrDtos[index + 2].valueName }}</div>
                  </el-form-item>
                </el-col>
                <el-col :span="6" v-if="index < ownerDetailInfo.ownerAttrDtos.length - 3">
                  <el-form-item :label="ownerDetailInfo.ownerAttrDtos[index + 3].specName + ':'">
                    <div>{{ ownerDetailInfo.ownerAttrDtos[index + 3].valueName }}</div>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </div>

    <divider />

    <div class="margin-top-sm">
      <el-tabs v-model="ownerDetailInfo._currentTab" @tab-click="changeTab(ownerDetailInfo._currentTab)">
        <el-tab-pane :label="$t('ownerDetail.room')" name="ownerDetailRoom"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.car')" name="ownerDetailCar"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.member')" name="ownerDetailMember"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.history')" name="ownerDetailHis"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.roomFee')" name="ownerDetailRoomFee"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.paymentHistory')" name="ownerDetailHisFee"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.account')" name="ownerDetailAccount"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.coupon')" name="ownerDetailCoupon"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.ownerBinding')" name="ownerDetailAppUser"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.complaint')" name="ownerDetailComplaint"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.repair')" name="ownerDetailRepair"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.visitor')" name="ownerDetailVisit"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.contract')" name="ownerDetailContract"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.receipt')" name="ownerDetailReceipt"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.depositReceipt')" name="ownerDetailAccountReceipt"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.faceSync')" name="ownerDetailAccessControl"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.accessRecord')" name="ownerDetailAccessControlRecord"></el-tab-pane>
        <el-tab-pane :label="$t('ownerDetail.chargeOrder')" name="ownerDetailChargeMachineOrder"></el-tab-pane>
      </el-tabs>
    </div>

    <component :is="ownerDetailInfo._currentTab" ref="currentTabComponent"></component>

    <edit-owner ref="editOwner" @refresh="loadOwnerInfo"></edit-owner>
    <delete-fee ref="deleteFee"></delete-fee>
    <edit-fee ref="editFee"></edit-fee>
    <!-- <finish-fee ref="finishFee"></finish-fee> -->
  </div>
</template>

<script>
import OwnerDetailRoom from '@/components/owner/ownerDetailRoom'
import OwnerDetailCar from '@/components/owner/ownerDetailCar'
import OwnerDetailMember from '@/components/owner/ownerDetailMember'
import OwnerDetailHis from '@/components/owner/ownerDetailHis'
import OwnerDetailAccount from '@/components/owner/ownerDetailAccount'
import OwnerDetailCoupon from '@/components/owner/ownerDetailCoupon'
import OwnerDetailAppUser from '@/components/owner/ownerDetailAppUser'
import OwnerDetailComplaint from '@/components/owner/ownerDetailComplaint'
import OwnerDetailRepair from '@/components/owner/ownerDetailRepair'
import OwnerDetailVisit from '@/components/owner/ownerDetailVisit'
import OwnerDetailContract from '@/components/owner/ownerDetailContract'
import OwnerDetailRoomFee from '@/components/owner/ownerDetailRoomFee'
import OwnerDetailHisFee from '@/components/owner/ownerDetailHisFee'
import OwnerDetailReceipt from '@/components/owner/ownerDetailReceipt'
import OwnerDetailAccountReceipt from '@/components/owner/ownerDetailAccountReceipt'
import OwnerDetailAccessControl from '@/components/owner/ownerDetailAccessControl'
import OwnerDetailAccessControlRecord from '@/components/owner/ownerDetailAccessControlRecord'
import OwnerDetailChargeMachineOrder from '@/components/owner/ownerDetailChargeMachineOrder'
import EditOwner from '@/components/owner/editOwner'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
//import FinishFee from '@/components/fee/finishFee'
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwners } from '@/api/owner/ownerDetailApi'
import divider from '@/components/system/divider'

export default {
  name: 'OwnerDetail',
  components: {
    OwnerDetailRoom,
    OwnerDetailCar,
    OwnerDetailMember,
    OwnerDetailHis,
    OwnerDetailAccount,
    OwnerDetailCoupon,
    OwnerDetailAppUser,
    OwnerDetailComplaint,
    OwnerDetailRepair,
    OwnerDetailVisit,
    OwnerDetailContract,
    OwnerDetailRoomFee,
    OwnerDetailHisFee,
    OwnerDetailReceipt,
    OwnerDetailAccountReceipt,
    OwnerDetailAccessControl,
    OwnerDetailAccessControlRecord,
    OwnerDetailChargeMachineOrder,
    EditOwner,
    DeleteFee,
    EditFee,
    //FinishFee,
    divider
  },
  data() {
    return {
      ownerDetailInfo: {
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
        _currentTab: 'ownerDetailRoom',
        personType: '',
        personTypeName: '',
        personRole: '',
        personRoleName: '',
        concactLink: '',
        concactPerson: '',
        needBack: false
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      this.ownerDetailInfo.ownerId = this.$route.query.ownerId
      this.ownerDetailInfo.needBack = this.$route.query.needBack
      if (!this.ownerDetailInfo.ownerId) {
        return
      }
      const currentTab = this.$route.query.currentTab
      if (currentTab) {
        this.ownerDetailInfo._currentTab = currentTab
      }
      this.loadOwnerInfo()
    },
    loadOwnerInfo() {
      const params = {
        ownerId: this.ownerDetailInfo.ownerId,
        page: 1,
        row: 1,
        communityId: getCommunityId(),
        ownerTypeCd: '1001'
      }

      queryOwners(params).then(response => {
        const data = response.data[0]
        Object.assign(this.ownerDetailInfo, data)
        this.ownerDetailInfo.ownerAttrDtos = data.ownerAttrDtos
        this.changeTab(this.ownerDetailInfo._currentTab)
      }).catch(error => {
        console.error('Failed to load owner info:', error)
      })
    },
    changeTab(tab) {
      this.ownerDetailInfo._currentTab = tab || tab
      setTimeout(() => {
        if (this.$refs.currentTabComponent) {
          if (this.ownerDetailInfo._currentTab == 'ownerDetailRoomFee') {
            this.$refs.currentTabComponent.open({
              ownerId:this.ownerDetailInfo.ownerId,
              name:this.ownerDetailInfo.name,
              link:this.ownerDetailInfo.link
            }
            )
            return;
          }
          this.$refs.currentTabComponent.open(
            this.ownerDetailInfo.ownerId,
            this.ownerDetailInfo.name,
            this.ownerDetailInfo.link
          )
        }
      }, 1000);

    },
    errorLoadImg() {
      this.ownerDetailInfo.ownerPhoto = "/img/noPhoto.jpg"
    },
    _openEditOwnerDetailModel() {
      this.$refs.editOwner.open(this.ownerDetailInfo)
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

.margin-top {
  margin-top: 15px;
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

.vc-line-primary {
  border-top: 1px solid #409EFF;
}
</style>