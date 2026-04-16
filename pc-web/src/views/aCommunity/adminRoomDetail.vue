<template>
  <div class="white-bg padding-lg padding-top border-radius">
    <div class="flex justify-between">
      <div class="text-title">{{ $t('adminRoomDetail.title') }}</div>
      <div>
        <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
          <i class="el-icon-close"></i>{{ $t('adminRoomDetail.back') }}
        </el-button>
      </div>
    </div>

    <!-- 房屋信息 -->
    <div class="margin-top">
      <el-row style="min-height: 160px;">
        <el-col :span="24">
          <el-form class="text-left">
            <el-row>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.roomCode')">
                  <label>{{ adminRoomDetailInfo.floorNum }}-{{ adminRoomDetailInfo.unitNum }}-{{
                    adminRoomDetailInfo.roomNum }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.floor')">
                  <label>{{ adminRoomDetailInfo.layer }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.roomArea')">
                  <label>{{ adminRoomDetailInfo.builtUpArea }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.roomType')">
                  <label>{{ adminRoomDetailInfo.roomSubTypeName }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.roomStatus')">
                  <label>{{ adminRoomDetailInfo.stateName }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.indoorArea')">
                  <label>{{ adminRoomDetailInfo.roomArea }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.rent')">
                  <label>{{ adminRoomDetailInfo.roomRent }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.validity')">
                  <label>{{ adminRoomDetailInfo.startTime }}~{{ adminRoomDetailInfo.endTime }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.ownerName')">
                  <label>{{ adminRoomDetailInfo.ownerName }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.contact')">
                  <label>{{ adminRoomDetailInfo.link }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('adminRoomDetailInfo.remark')">
                  <label>{{ adminRoomDetailInfo.remark }}</label>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminRoomDetailInfo.communityName')">
                  <label>{{ adminRoomDetailInfo.communityName }}</label>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </div>

    <divider></divider>

    <div class="margin-top-sm">
      <el-tabs v-model="adminRoomDetailInfo._currentTab" @tab-click="handleTabClick(adminRoomDetailInfo._currentTab)">
        <el-tab-pane :label="$t('adminRoomDetail.owner')" name="aRoomDetailOwner"></el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.member')" name="aRoomDetailMember">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.ownerRoom')" name="aRoomDetailRoom">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.ownerCar')" name="aRoomDetailCar">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.contract')"
          name="aRoomDetailContract">
        </el-tab-pane>
        <el-tab-pane :label="$t('adminRoomDetail.roomFee')" name="aRoomDetailRoomFee">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.carFee')" name="aRoomDetailCarFee">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.paymentHistory')"
          name="aRoomDetailHisFee">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.receipt')"
          name="aRoomDetailReceipt">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.repair')" name="aRoomDetailRepair">
        </el-tab-pane>
        <el-tab-pane v-if="adminRoomDetailInfo.ownerId" :label="$t('adminRoomDetail.complaint')"
          name="aRoomDetailComplaint">
        </el-tab-pane>
        <el-tab-pane :label="$t('adminRoomDetail.meterReading')" name="aRoomDetailMeterWaterLog">
        </el-tab-pane>
        <el-tab-pane :label="$t('adminRoomDetail.historyOwner')" name="aRoomDetailHisOwner">
        </el-tab-pane>
      </el-tabs>
    </div>

    <component :is="adminRoomDetailInfo._currentTab" :ref="adminRoomDetailInfo._currentTab"
      :roomId="adminRoomDetailInfo.roomId" :ownerId="adminRoomDetailInfo.ownerId"
      :ownerName="adminRoomDetailInfo.ownerName" :link="adminRoomDetailInfo.link"></component>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getRoomDetail } from '@/api/aCommunity/adminRoomDetailApi'
import ARoomDetailOwner from '@/components/fee/aRoomDetailOwner'
import ARoomDetailMember from '@/components/aCommunity/aRoomDetailMember'
import ARoomDetailRoom from '@/components/fee/aRoomDetailRoom'
import ARoomDetailCar from '@/components/aCommunity/aRoomDetailCar'
import ARoomDetailContract from '@/components/aCommunity/aRoomDetailContract'
import ARoomDetailRoomFee from '@/components/fee/aRoomDetailRoomFee'
import ARoomDetailCarFee from '@/components/aCommunity/aRoomDetailCarFee'
import ARoomDetailHisFee from '@/components/fee/aRoomDetailHisFee'
import ARoomDetailReceipt from '@/components/fee/aRoomDetailReceipt'
import ARoomDetailRepair from '@/components/aCommunity/aRoomDetailRepair'
import ARoomDetailComplaint from '@/components/aCommunity/aRoomDetailComplaint'
import ARoomDetailMeterWaterLog from '@/components/fee/aRoomDetailMeterWaterLog'
import ARoomDetailHisOwner from '@/components/aCommunity/aRoomDetailHisOwner'
import Divider from '@/components/system/divider'

export default {
  components: {
    ARoomDetailOwner,
    ARoomDetailMember,
    ARoomDetailRoom,
    ARoomDetailCar,
    ARoomDetailContract,
    ARoomDetailRoomFee,
    ARoomDetailCarFee,
    ARoomDetailHisFee,
    ARoomDetailReceipt,
    ARoomDetailRepair,
    ARoomDetailComplaint,
    ARoomDetailMeterWaterLog,
    ARoomDetailHisOwner,
    Divider
  },
  data() {
    return {
      adminRoomDetailInfo: {
        viewOwnerFlag: '',
        roomId: "",
        floorNum: '',
        unitNum: '',
        roomNum: '',
        layer: "",
        builtUpArea: "",
        roomSubTypeName: "",
        stateName: "",
        roomArea: "",
        roomRent: "",
        startTime: "",
        endTime: "",
        _currentTab: 'aRoomDetailOwner',
        remark: '',
        ownerId: '',
        ownerName: '',
        link: '',
        communityId: '',
        communityName: ''
      }
    }
  },
  created() {
    this.adminRoomDetailInfo.roomId = this.$route.query.roomId
    if (!this.adminRoomDetailInfo.roomId) {
      return
    }
    const currentTab = this.$route.query.currentTab
    if (currentTab) {
      this.adminRoomDetailInfo._currentTab = currentTab
    }
    this.loadAdminRoomInfo()
    this.communityId = getCommunityId()
  },
  methods: {
    loadAdminRoomInfo() {
      getRoomDetail({
        roomId: this.adminRoomDetailInfo.roomId,
        page: 1,
        row: 1
      }).then(res => {
        Object.assign(this.adminRoomDetailInfo, res.data[0])

        this.handleTabClick(this.adminRoomDetailInfo._currentTab)
      }).catch(err => {
        console.error('请求失败:', err)
      })
    },
    handleTabClick(tab) {
      this.adminRoomDetailInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab] && this.$refs[tab].open) {
          this.$refs[tab].open({
            roomId: this.adminRoomDetailInfo.roomId,
            ownerId: this.adminRoomDetailInfo.ownerId,
            ownerName: this.adminRoomDetailInfo.ownerName,
            link: this.adminRoomDetailInfo.link
          })
        }
      }, 500)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.white-bg {
  background: #fff;
}

.padding-lg {
  padding: 20px;
}

.padding-top {
  padding-top: 20px;
}

.border-radius {
  border-radius: 4px;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
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
</style>