<template>
  <el-dialog :visible.sync="visible" :title="$t('searchCommunityData.title')" width="80%" @close="handleClose">
    <el-row class="margin-bottom">
      <el-col :span="14" :offset="2">
        <el-input v-model="searchValue" :placeholder="$t('searchCommunityData.placeholder')"
          @keyup.enter.native="doSearch" />
      </el-col>
      <el-col :span="3" :offset="1">
        <el-button type="primary" @click="doSearch">
          {{ $t('common.search') }}
        </el-button>
      </el-col>
      <el-col :span="3">
        <el-button @click="queryCallInTel">
          {{ $t('searchCommunityData.callInNumber') }}
        </el-button>
      </el-col>
    </el-row>
    <el-divider />
    <div class="margin-top margin-bottom" style="min-height: 200px;">
      <template v-if="!noData">
        <div class="vc-search-community-item" v-if="rooms.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.roomInfo') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in rooms" :key="'room' + index" @click="toSimplifyAcceptance(item)">
              {{ item.roomName }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="owners.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.ownerInfo') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in owners" :key="'owner' + index"
              :href="'/#/views/owner/ownerDetail?ownerId=' + item.ownerId" target="_blank">
              {{ item.name }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="ownerMembers.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.ownerMember') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in ownerMembers" :key="'ownerMember' + index"
              :href="'/#/views/owner/ownerDetail?ownerId=' + item.ownerId" target="_blank">
              {{ item.name }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="cars.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.communityCar') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in cars" :key="'car' + index"
              :href="'/#/views/car/carDetail?memberId=' + item.carId" target="_blank">
              {{ item.carNum }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="carMembers.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.memberCar') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in carMembers" :key="'carMember' + index"
              :href="'/#/views/car/carDetail?memberId=' + item.carId" target="_blank">
              {{ item.carNum }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="contracts.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.contract') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in contracts" :key="'contract' + index"
              :href="'/#/views/contract/contractDetail?contractId=' + item.contractId" target="_blank">
              {{ item.contractName }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="repairs.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.repairOrder') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in repairs" :key="'repair' + index"
              :href="'/#/pages/property/ownerRepairDetail?repairId=' + item.repairId" target="_blank">
              {{ item.repairName }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="visits.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.visitor') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in visits" :key="'visit' + index"
              :href="'/#/pages/property/visitDetail?vId=' + item.vId + '&flowId=' + item.flowId" target="_blank">
              {{ item.vName }}
            </el-link>
          </div>
        </div>
        <div class="vc-search-community-item" v-if="staffs.length > 0">
          <div class="item-title">{{ $t('searchCommunityData.staff') }}</div>
          <div class="item-content">
            <el-link v-for="(item, index) in staffs" :key="'staff' + index"
              :href="'/#/views/staff/staffDetail?staffId=' + item.userId" target="_blank">
              {{ item.name }}
            </el-link>
          </div>
        </div>
      </template>
      <div v-else class="text-center" style="font-size: 38px;color: #aaa;margin-top: 100px;">
        {{ $t('searchCommunityData.noData') }}
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.close') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { searchCommunityData, getTelMachineInfo, saveTelMachineMsg } from '@/api/system/searchCommunityDataApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SearchCommunityData',
  data() {
    return {
      visible: false,
      searchValue: '',
      rooms: [],
      owners: [],
      ownerMembers: [],
      cars: [],
      carMembers: [],
      contracts: [],
      repairs: [],
      visits: [],
      staffs: [],
      noData: true,
      machine: {},
      ws: null,
      componentName: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initTelMachine()
  },
  methods: {
    open() {
      this.visible = true
      this.clearSearchData()
    },
    handleClose() {
      this.clearSearchData()
    },
    initTelMachine() {
      if (!this.communityId || this.communityId === '-1') {
        return
      }
      getTelMachineInfo({
        page: 1,
        row: 1,
        communityId: this.communityId
      }).then(res => {
        if (res.code !== 0 || res.data.length < 1) {
          return
        }
        this.machine = res.data[0]
        this.connectTelMachine(this.machine)
      }).catch(error => {
        console.error('Failed to get tel machine:', error)
      })
    },
    doSearch() {
      if (!this.searchValue) {
        this.$message.warning(this.$t('searchCommunityData.inputTip'))
        return
      }

      searchCommunityData({
        searchValue: this.searchValue,
        communityId: this.communityId
      }).then(res => {
        if (res.code !== 0) {
          this.$message.error(res.msg)
          return
        }

        this.noData = false
        const data = res.data
        this.rooms = data.rooms || []
        this.owners = data.owners || []
        this.ownerMembers = data.ownerMembers || []
        this.cars = data.cars || []
        this.carMembers = data.carMembers || []
        this.contracts = data.contracts || []
        this.repairs = data.repairs || []
        this.visits = data.visitDtos || []
        this.staffs = data.staffs || []

        this.noData = !(this.rooms.length > 0 || this.owners.length > 0 ||
          this.ownerMembers.length > 0 || this.cars.length > 0 ||
          this.carMembers.length > 0 || this.contracts.length > 0 ||
          this.repairs.length > 0 || this.visits.length > 0 ||
          this.staffs.length > 0)
      }).catch(error => {
        console.error('Search failed:', error)
      })
    },
    toSimplifyAcceptance(room) {
      this.visible = false
      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${room.floorNum}-${room.unitNum}-${room.roomNum}`)

    },
    clearSearchData() {
      const ws = this.ws
      const machine = this.machine

      this.searchValue = ''
      this.noData = true
      this.rooms = []
      this.owners = []
      this.ownerMembers = []
      this.cars = []
      this.carMembers = []
      this.contracts = []
      this.repairs = []
      this.visits = []
      this.staffs = []
      this.machine = machine
      this.ws = ws
    },
    queryCallInTel() {
      if (!this.ws) {
        this.$message.warning(this.$t('searchCommunityData.telNotConnected'))
        return
      }
      this.ws.send(JSON.stringify({
        action: 'Query',
        type: 'Device',
        cb: 'cb_data'
      }))
    },
    connectTelMachine(machine) {
      const protocol = window.location.protocol
      let url = "ws://"
      if (protocol.startsWith('https')) {
        url = "wss://"
      }
      url = `${url}${machine.machineIp}:${machine.machinePort}/APP_2AD85C71-BEF8-463C-9B4B-B672F603542A_fast`

      const ws = new WebSocket(url)

      ws.onerror = (event) => {
        console.log('WebSocket error:', event)
        setTimeout(() => {
          this.connectTelMachine(machine)
        }, 2000)
      }

      ws.onclose = () => {
        console.log('WebSocket closed')
      }

      ws.onopen = () => {
        console.log('WebSocket connected')
      }

      ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)
          console.log('WebSocket message:', data)

          if (data.message === 'query') {
            if (data.name === 'Device' && data.param.CallerId) {
              this.clearSearchData()
              this.searchValue = data.param.CallerId
              this.doSearch()
              return
            }
            if (data.name === 'Device' && Object.prototype.hasOwnProperty.call(data.param, 'CallerId')) {
              this.$message.warning(this.$t('searchCommunityData.noCallInNumber'))
              return
            }
            return
          }

          const param = data.param

          if (param && param.status === 'CallIn') {
            this.clearSearchData()
            this.searchValue = param.number
            this.visible = true
            this.doSearch()
            return
          }

          if (param && param.status === 'TalkingStart') {
            this.$emit('TalkingStart', param)
            return
          }

          if (param && param.status === 'TalkingEnd') {
            this.$emit('TalkingEnd', param)
            return
          }

          this.saveTelMachineMsg(data)
        } catch (error) {
          console.error('Error parsing WebSocket message:', error)
        }
      }

      this.ws = ws
    },
    saveTelMachineMsg(data) {
      data.communityId = this.communityId
      data.machineId = this.machine.machineId

      saveTelMachineMsg(data).then(res => {
        if (res.code !== 0) {
          console.error('Failed to save tel machine message:', res.msg)
          return
        }

        const param = res.data || {}
        if (param.action === 'sms') {
          this.$emit('sendTelMsg', {
            tel: param.tel,
            msgText: param.msgText
          })
        }
      }).catch(error => {
        console.error('Failed to post tel machine message:', error)
      })
    },
    playTts(data) {
      if (this.ws) {
        this.ws.send(JSON.stringify({
          action: 'PlayMusic',
          url: data.url,
          file_name: this.uuid() + '.wav',
          volume: 100,
          loop: true
        }))
      }
    },
    hangup() {
      if (this.ws) {
        this.ws.send(JSON.stringify({
          action: 'Hangup'
        }))
      }
    },
    callTel(data) {
      if (this.ws) {
        this.ws.send(JSON.stringify({
          action: 'CallOut',
          number: data.tel,
          cb: 'callout_cb'
        }))
        this.componentName = data.componentName
      }
    },
    sendTelMsg(data) {
      if (this.ws) {
        this.ws.send(JSON.stringify({
          action: 'SmsSend',
          number: data.tel,
          content: data.msgText
        }))
      }
    },
    uuid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0
        const v = c === 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
      })
    }
  }
}
</script>

<style scoped>
.vc-search-community-item {
  text-align: left;
  margin-bottom: 20px;
}

.item-title {
  font-weight: bold;
  margin-bottom: 10px;
}

.item-content {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.margin-top {
  margin-top: 20px;
}

.margin-bottom {
  margin-bottom: 20px;
}

.text-center {
  text-align: center;
}
</style>