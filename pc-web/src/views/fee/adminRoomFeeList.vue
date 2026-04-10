<template>
  <div class="admin-room-fee-container">
    <el-row :gutter="10">
      <el-col :span="4" class="tree-container">
        <community-room-tree ref="communityTree" @select-room="handleSelectRoom" />
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ adminRoomFeeInfo.roomName }}</span>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="adminRoomFeeInfo.condition.roomNum" :placeholder="$t('adminRoomFee.roomNumPlaceholder')"
                clearable @blur="listRoomInAdminRoomFee" />
            </el-col>
            <el-col :span="6">
              <el-input v-model="adminRoomFeeInfo.condition.ownerName"
                :placeholder="$t('adminRoomFee.ownerNamePlaceholder')" clearable @input="_inputRoomByOwner" />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="_queryAdminRoomFeeMethod">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetAdminRoomFeeMethod">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>

          <el-tabs class="margin-top" v-model="adminRoomFeeInfo._currentTab" @tab-click="changeTab(adminRoomFeeInfo._currentTab)">
            <el-tab-pane label="房屋费用" name="aRoomDetailRoomFee" />
            <el-tab-pane v-if="adminRoomFeeInfo.ownerId" label="缴费历史" name="aRoomDetailHisFee" />
            <el-tab-pane v-if="adminRoomFeeInfo.ownerId" label="房屋" name="aRoomDetailRoom" />
            <el-tab-pane v-if="adminRoomFeeInfo.ownerId" label="业主" name="aRoomDetailOwner" />
            <el-tab-pane label="催缴" name="aRoomFeeCallable" />
            <el-tab-pane label="收据" name="aRoomDetailReceipt" />
            <el-tab-pane label="抄表记录" name="aRoomDetailMeterWaterLog" />
            <el-tab-pane v-if="adminRoomFeeInfo.ownerId" label="预存账户" name="aOwnerDetailAccount" />
            <el-tab-pane v-if="adminRoomFeeInfo.ownerId" label="预存补打" name="aOwnerDetailAcctReceipt" />
            <el-tab-pane label="押金" name="aRefundDeposit" />
          </el-tabs>

          <component :is="adminRoomFeeInfo._currentTab" :ref="adminRoomFeeInfo._currentTab"
            :room-id="adminRoomFeeInfo.roomId" :owner-id="adminRoomFeeInfo.ownerId" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CommunityRoomTree from '@/components/fee/communityRoomTree'
import ARoomDetailRoomFee from '@/components/fee/aRoomDetailRoomFee'
import ARoomDetailHisFee from '@/components/fee/aRoomDetailHisFee'
import ARoomDetailRoom from '@/components/fee/aRoomDetailRoom'
import ARoomDetailOwner from '@/components/fee/aRoomDetailOwner'
import ARoomDetailMeterWaterLog from '@/components/fee/aRoomDetailMeterWaterLog'
import ARoomDetailReceipt from '@/components/fee/aRoomDetailReceipt'
import AOwnerDetailAccount from '@/components/fee/aOwnerDetailAccount'
import AOwnerDetailAcctReceipt from '@/components/fee/aOwnerDetailAcctReceipt'
import ARoomFeeCallable from '@/components/fee/aRoomFeeCallable'
import ARefundDeposit from '@/components/fee/aRefundDeposit'

import { queryAdminRoom } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'AdminRoomFeeList',
  components: {
    CommunityRoomTree,
    ARoomDetailRoomFee,
    ARoomDetailHisFee,
    ARoomDetailRoom,
    ARoomDetailOwner,
    ARoomDetailMeterWaterLog,
    ARoomDetailReceipt,
    AOwnerDetailAccount,
    AOwnerDetailAcctReceipt,
    ARoomFeeCallable,
    ARefundDeposit
  },
  data() {
    return {
      adminRoomFeeInfo: {
        roomName: '',
        roomId: '',
        ownerId: '',
        ownerName: '',
        roomType: '',
        _currentTab: 'aRoomDetailRoomFee',
        condition: {
          roomNum: '',
          ownerName: ''
        }
      }
    }
  },
  created() {
    //this.$refs.communityTree.initCommunityRoomTree('adminRoomFee')
  },
  methods: {
    handleSelectRoom(room) {
      this.adminRoomFeeInfo.ownerName = ''
      this.adminRoomFeeInfo.ownerId = ''
      this.adminRoomFeeInfo.roomId = room.roomId
      this.adminRoomFeeInfo.roomName = room.roomName
      this.adminRoomFeeInfo.condition.ownerName = ''
      this.adminRoomFeeInfo.condition.roomNum = ''
      this.listRoomInAdminRoomFee()
    },
    async listRoomInAdminRoomFee() {
      const param = {
        page: 1,
        row: 1,
        roomId: this.adminRoomFeeInfo.roomId
      }

      if (this.adminRoomFeeInfo.condition.roomNum) {
        param.flag = '1'
        param.roomNum = this.adminRoomFeeInfo.condition.roomNum
        param.roomId = ''
        this.adminRoomFeeInfo.roomName = ''
        this.adminRoomFeeInfo.ownerName = ''
        this.adminRoomFeeInfo.ownerId = ''
        this.adminRoomFeeInfo.roomId = ''
      }

      const res = await queryAdminRoom(param)
      const room = res.data[0]
      Object.assign(this.adminRoomFeeInfo, room)
      this.changeTab(this.adminRoomFeeInfo._currentTab)

    },
    _queryAdminRoomFeeMethod() {
      this.listRoomInAdminRoomFee()
    },
    _resetAdminRoomFeeMethod() {
      this.adminRoomFeeInfo.roomNum = ''
      this.adminRoomFeeInfo.ownerName = ''
      this.adminRoomFeeInfo.ownerId = ''
      this.adminRoomFeeInfo.condition.ownerName = ''
      this.adminRoomFeeInfo.condition.roomNum = ''
      this.changeTab(this.adminRoomFeeInfo._currentTab)
    },
    changeTab(tab) {
      this.adminRoomFeeInfo._currentTab = tab
      if(!this.adminRoomFeeInfo.roomId){
        return
      }
      setTimeout(() => {
        const ref = this.$refs[this.adminRoomFeeInfo._currentTab];
        if (ref && ref.open) {
          ref.open(this.adminRoomFeeInfo.ownerId, this.adminRoomFeeInfo.roomId);
        }
      },500)
    },
    _inputRoomByOwner() {
      // Implement input room by owner logic
    }
  }
}
</script>

<style scoped>
.tree-container {


  overflow-y: auto;
  padding-right: 0;
}
</style>