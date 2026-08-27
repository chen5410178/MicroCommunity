<template>
  <div class="white-bg padding-top-lg margin">
    <div class="margin-top padding-bottom-lg flex justify-center">
      <div class="search-switch">
        <el-select v-model="simplifyAcceptanceInfo.searchType" @change="_changeSearchType">
          <el-option value="1" :label="$t('simplifyAcceptance.houseNumber')"></el-option>
          <el-option value="2" :label="$t('simplifyAcceptance.ownerName')"></el-option>
          <el-option value="3" :label="$t('simplifyAcceptance.ownerPhone')"></el-option>
          <el-option value="4" :label="$t('simplifyAcceptance.ownerIdCard')"></el-option>
          <el-option value="5" :label="$t('simplifyAcceptance.licensePlate')"></el-option>
          <el-option value="6" :label="$t('simplifyAcceptance.memberName')"></el-option>
          <el-option value="7" :label="$t('simplifyAcceptance.memberPhone')"></el-option>
          <el-option value="8" :label="$t('simplifyAcceptance.memberIdCard')"></el-option>
          <el-option value="9" :label="$t('simplifyAcceptance.shopNumber')"></el-option>
          <el-option value="10" :label="$t('simplifyAcceptance.contractNumber')"></el-option>
        </el-select>
      </div>
      <div class="search-input">
        <el-input :placeholder="$t(simplifyAcceptanceInfo.searchPlaceholder)"
          v-model="simplifyAcceptanceInfo.searchValue" @input="_simplifyInputOwner"
          @keyup.enter.native="_doSearch"></el-input>
        <input-search-owner ref="inputSearchOwner"></input-search-owner>
        <input-search-room ref="inputSearchRoom"></input-search-room>
      </div>
      <div>
        <el-button type="primary" @click="_doSearch">
          <i class="el-icon-search"></i>
          <span>{{ $t('simplifyAcceptance.search') }}</span>
        </el-button>
      </div>
      <div class="padding-lr-0 margin-left-sm" v-if="simplifyAcceptanceInfo.searchType == '1'">
        <el-button type="primary" @click="_simplifyAcceptanceChooseRoom">
          <i class="el-icon-plus"></i>
          <span>{{ $t('simplifyAcceptance.selectHouse') }}</span>
        </el-button>
      </div>
      <div class="padding-lr-0 margin-left-sm">
        <el-dropdown>
          <el-button type="primary">
            {{ $t('simplifyAcceptance.moreOperations') }}<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="_handover">{{ $t('simplifyAcceptance.handover') }}</el-dropdown-item>
            <el-dropdown-item @click.native="_simplifyOwnerExitRoom">{{ $t('simplifyAcceptance.exitRoom')
            }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <divider />
    <div class="padding margin-top-xs text-left">
      <div>
        <div class="text-center vc-float-left" style="width: 150px;" v-if="simplifyAcceptanceInfo.ownerPhoto">
          <img width="120px" height="140px" class="border-radius" :src="simplifyAcceptanceInfo.ownerPhoto"
            @error="errorLoadImg" />
        </div>
        <div class="text-center vc-float-left" style="width: 150px;" v-else>
          <img width="120px" height="140px" class="border-radius" src="/img/noPhoto.jpg" />
        </div>
        <div class="simplify-acceptance margin-bottom">
          <el-row>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.ownerId') }}</label>
                <label>{{ simplifyAcceptanceInfo.ownerId }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.ownerName') }}</label>
                <label>
                  <a target="_blank" :href="'/#/views/owner/ownerDetail?ownerId=' + simplifyAcceptanceInfo.ownerId">
                    {{ simplifyAcceptanceInfo.name }}
                  </a>
                </label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.contactPhone') }}</label>
                <label>{{ simplifyAcceptanceInfo.link }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.idCard') }}</label>
                <label>{{ simplifyAcceptanceInfo.idCard }}</label>
              </div>
            </el-col>

            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.checkInDate') }}</label>
                <label>{{ simplifyAcceptanceInfo.createTime }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.gender') }}</label>
                <label>
                  {{ simplifyAcceptanceInfo.sex == '0' ? $t('simplifyAcceptance.male') :
                    (simplifyAcceptanceInfo.sex == '1' ? $t('simplifyAcceptance.female') : '') }}
                </label>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.ownerRemark') }}</label>
                <label class="fix-width" :title="simplifyAcceptanceInfo.ownerRemark">
                  {{ simplifyAcceptanceInfo.ownerRemark }}
                </label>
              </div>
            </el-col>

            <el-col :span="6" >
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.houseNumber') }}</label>
                <label>
                  {{ simplifyAcceptanceInfo.floorNum }}-{{ simplifyAcceptanceInfo.unitNum }}-{{
                    simplifyAcceptanceInfo.roomNum }}
                </label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.houseArea') }}</label>
                <label>{{ simplifyAcceptanceInfo.builtUpArea }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.houseType') }}</label>
                <label>{{ simplifyAcceptanceInfo.roomSubTypeName }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.layout') }}</label>
                <label>{{ simplifyAcceptanceInfo.apartmentName }}</label>
              </div>
            </el-col>

            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.houseStatus') }}</label>
                <label>{{ simplifyAcceptanceInfo.stateName }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.indoorArea') }}</label>
                <label>{{ simplifyAcceptanceInfo.roomArea }}</label>
              </div>
            </el-col>
            <el-col :span="6" v-if="simplifyAcceptanceInfo.roomSubType && simplifyAcceptanceInfo.roomSubType != '110'">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.rent') }}</label>
                <label>{{ simplifyAcceptanceInfo.roomRent }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.houseRemark') }}</label>
                <label class="fix-width" :title="simplifyAcceptanceInfo.roomRemark">
                  {{ simplifyAcceptanceInfo.roomRemark }}
                </label>
              </div>
            </el-col>

            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.accountBalance') }}</label>
                <label>
                  <span v-if="simplifyAcceptanceInfo.acctAmount">
                    {{ simplifyAcceptanceInfo.acctAmount }}(
                    <a target="_blank" v-if="hasPrivilege('502023032809461736')"
                      :href="'/#/views/owner/ownerDetail?ownerId=' + simplifyAcceptanceInfo.ownerId + '&currentTab=ownerDetailAccount'">
                      {{ $t('simplifyAcceptance.preDeposit') }}
                    </a>
                    )
                  </span>
                </label>
              </div>
            </el-col>
            <el-col :span="6" v-if="simplifyAcceptanceInfo.roomId">
              <div class="form-group">
                <label class="col-form-label">{{ $t('simplifyAcceptance.depositAmount') }}</label>
                <label>
                  {{ simplifyAcceptanceInfo.depositAmount }}
                  (<a href="javascript:void(0)" @click="_toRefundDepositFee">{{ $t('simplifyAcceptance.refundDeposit')
                  }}</a>)
                </label>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <divider />
      <div class="margin-top">
        <el-tabs v-model="simplifyAcceptanceInfo._currentTab"
          @tab-click="changeTab(simplifyAcceptanceInfo._currentTab)">
          <el-tab-pane :label="$t('simplifyAcceptance.houseFee')" name="simplifyRoomFee">
            <simplify-room-fee ref="simplifyRoomFee"></simplify-room-fee>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.parkingFee')" name="simplifyParkingSpaceFee">
            <simplify-parking-space-fee ref="simplifyParkingSpaceFee"></simplify-parking-space-fee>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.paymentHistory')" name="simplifyHisFee">
            <simplify-his-fee ref="simplifyHisFee"></simplify-his-fee>
          </el-tab-pane>
                <el-tab-pane :label="$t('simplifyAcceptance.shopFee')" name="simplifyOwnerShopFee">
            <simplify-owner-shop-fee ref="simplifyOwnerShopFee" @notifyShop="handleNotifyShop"></simplify-owner-shop-fee>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.house')" name="simplifyOwnerRooms">
            <simplify-owner-rooms ref="simplifyOwnerRooms"></simplify-owner-rooms>
          </el-tab-pane>
    
          <el-tab-pane :label="$t('simplifyAcceptance.ownerShops')" name="simplifyOwnerShops">
            <simplify-owner-shops ref="simplifyOwnerShops" @notifyShop="handleNotifyShop"></simplify-owner-shops>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.familyMember')" name="simplifyOwnerMember">
            <simplify-owner-member ref="simplifyOwnerMember"></simplify-owner-member>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.vehicle')" name="simplifyOwnerCar">
            <simplify-owner-car ref="simplifyOwnerCar"></simplify-owner-car>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.paymentReminder')" name="simplifyCallable">
            <simplify-callable ref="simplifyCallable"></simplify-callable>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.ownerFeedback')" name="simplifyNotepad">
            <a href="javascript:void(0)" @click="_toSimplifyNotepadPage">{{ $t('simplifyAcceptance.ownerFeedback')
            }}</a>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.repairOrder')" name="simplifyOwnerRepair">
            <simplify-owner-repair ref="simplifyOwnerRepair"></simplify-owner-repair>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.complaintOrder')" name="simplifyOwnerComplaint">
            <simplify-owner-complaint ref="simplifyOwnerComplaint"></simplify-owner-complaint>
          </el-tab-pane>
          <!-- <el-tab-pane :label="$t('simplifyAcceptance.ownerSync')" name="simplifyOwnerAccessContol">
            <simplify-owner-access-contol ref="simplifyOwnerAccessContol"></simplify-owner-access-contol>
          </el-tab-pane> -->
          <el-tab-pane :label="$t('simplifyAcceptance.vehicleSync')" name="simplifyOwnerTransactionCar">
            <simplify-owner-transaction-car ref="simplifyOwnerTransactionCar"></simplify-owner-transaction-car>
          </el-tab-pane>
          <el-tab-pane v-if="hasPrivilege('502020092373407363')" :label="$t('simplifyAcceptance.reprintReceipt')"
            name="simplifyFeeReceipt">
            <simplify-fee-receipt ref="simplifyFeeReceipt"></simplify-fee-receipt>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.historyOwner')" name="simplifyShopsHireLog">
            <simplify-shops-hire-log ref="simplifyShopsHireLog"></simplify-shops-hire-log>
          </el-tab-pane>
          <el-tab-pane :label="$t('simplifyAcceptance.meterReadingRecord')" name="simplifyMeterWaterLog">
            <simplify-meter-water-log ref="simplifyMeterWaterLog"></simplify-meter-water-log>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <delete-fee ref="deleteFee"></delete-fee>
    <edit-fee ref="editFee"></edit-fee>
    <finish-fee ref="finishFee"></finish-fee>
    <room-create-fee-add ref="roomCreateFeeAdd"></room-create-fee-add>
    <parking-space-create-fee-add ref="parkingSpaceCreateFeeAdd"></parking-space-create-fee-add>
    <add-meter-water ref="addMeterWater"></add-meter-water>
    <add-proxy-fee ref="addProxyFee"></add-proxy-fee>
    <edit-machine-translate ref="editMachineTranslate"></edit-machine-translate>
    <room-tree ref="roomTree" @selectRoom="selectRoom" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { comprehensiveQuery, queryFeeDeposit } from '@/api/simplify/simplifyAcceptanceApi'
import SimplifyRoomFee from '@/components/fee/simplifyRoomFee'
import SimplifyParkingSpaceFee from '@/components/simplify/simplifyParkingSpaceFee'
import SimplifyHisFee from '@/components/fee/simplifyHisFee'
import SimplifyOwnerRooms from '@/components/fee/simplifyOwnerRooms'
import SimplifyOwnerShopFee from '@/components/simplify/simplifyOwnerShopFee'
import SimplifyOwnerShops from '@/components/simplify/simplifyOwnerShops'
import SimplifyOwnerMember from '@/components/simplify/simplifyOwnerMember'
import SimplifyOwnerCar from '@/components/simplify/simplifyOwnerCar'
import SimplifyOwnerRepair from '@/components/simplify/simplifyOwnerRepair'
import SimplifyOwnerComplaint from '@/components/simplify/simplifyOwnerComplaint'
// import SimplifyOwnerAccessContol from '@/components/simplify/simplifyOwnerAccessContol'
import SimplifyOwnerTransactionCar from '@/components/simplify/simplifyOwnerTransactionCar'
import SimplifyFeeReceipt from '@/components/fee/simplifyFeeReceipt'
import SimplifyShopsHireLog from '@/components/simplify/simplifyShopsHireLog'
import SimplifyMeterWaterLog from '@/components/fee/simplifyMeterWaterLog'
import SimplifyCallable from '@/components/fee/simplifyCallable'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import FinishFee from '@/components/fee/finishFee'
import RoomCreateFeeAdd from '@/components/fee/roomCreateFeeAdd'
import ParkingSpaceCreateFeeAdd from '@/components/fee/parkingSpaceCreateFeeAdd'
import AddMeterWater from '@/components/fee/addMeterWater'
import AddProxyFee from '@/components/fee/addProxyFee'
import roomTree from '@/components/room/roomTree'
import EditMachineTranslate from '@/components/machine/editMachineTranslate'
import InputSearchOwner from '@/components/report/InputSearchOwner'
import InputSearchRoom from '@/components/fee/inputSearchRoom'
import divider from '@/components/system/divider'

export default {
  name: 'SimplifyAcceptanceList',
  components: {
    SimplifyRoomFee,
    SimplifyParkingSpaceFee,
    SimplifyHisFee,
    SimplifyOwnerRooms,
    SimplifyOwnerShopFee,
    SimplifyOwnerShops,
    SimplifyOwnerMember,
    SimplifyOwnerCar,
    SimplifyOwnerRepair,
    SimplifyOwnerComplaint,
   // SimplifyOwnerAccessContol,
    SimplifyOwnerTransactionCar,
    SimplifyFeeReceipt,
    SimplifyShopsHireLog,
    SimplifyMeterWaterLog,
    SimplifyCallable,
    DeleteFee,
    EditFee,
    FinishFee,
    RoomCreateFeeAdd,
    ParkingSpaceCreateFeeAdd,
    AddMeterWater,
    AddProxyFee,
    roomTree,
    EditMachineTranslate,
    InputSearchOwner,
    InputSearchRoom,
    divider
  },
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      TEMP_SEARCH: 'simplifyAcceptanceSearch',
      simplifyAcceptanceInfo: {
        searchType: '1',
        searchValue: '',
        searchPlaceholder: 'simplifyAcceptance.houseNumberPlaceholder',
        ownerPhoto: '',
        _currentTab: 'simplifyRoomFee',
        roomId: '',
        ownerId: '',
        ownerRemark: '',
        roomRemark: '',
        name: '',
        idCard: '',
        link: '',
        createTime: '',
        apartmentName: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        builtUpArea: '',
        feeCoefficient: '',
        stateName: '',
        roomName: '',
        roomType: '',
        roomSubTypeName: '',
        roomSubType: '',
        roomArea: '',
        sex: '',
        roomRent: '',
        acctAmount: '',
        depositAmount: '0',
        timer: {},
        selectedShopId: '',
        shopList: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initMethod()
    // 添加恢复搜索参数的逻辑
    this._restoreSearchData()
  },
  methods: {
    _initMethod() {
      if (!this.$route.query.searchValue) {
        return
      }
      this.simplifyAcceptanceInfo.searchType = '1'
      this.simplifyAcceptanceInfo.searchValue = this.$route.query.searchValue
      this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.houseNumberPlaceholder'
      this._doSearch()
    },
    _changeSearchType() {
      switch (this.simplifyAcceptanceInfo.searchType) {
        case '1':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.houseNumberPlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '2':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.ownerNamePlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '3':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.ownerPhonePlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '4':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.ownerIdCardPlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '5':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.licensePlatePlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '6':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.memberNamePlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '7':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.memberPhonePlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '8':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.memberIdCardPlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
          break
        case '9':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.shopNumberPlaceholder'
          break
        case '10':
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.contractNumberPlaceholder'
          break
        default:
          this.simplifyAcceptanceInfo.searchPlaceholder = 'simplifyAcceptance.houseNumberPlaceholder'
          this.simplifyAcceptanceInfo.searchValue = ""
      }
    },
    async _doSearch() {
      if (!this.simplifyAcceptanceInfo.searchValue) {
        this.$message.warning(this.$t('simplifyAcceptance.inputSearchCondition'))
        return
      }
      // 清理信息
      this._clearData()
      try {
        const res = await comprehensiveQuery({
          searchType: this.simplifyAcceptanceInfo.searchType,
          searchValue: this.simplifyAcceptanceInfo.searchValue,
          communityId: this.communityId
        })

        if (res.code != 0) {
          this.$message.error(res.msg)
          return
        }

        this.saveTempSearchData()
        const _owner = res.data
        Object.assign(this.simplifyAcceptanceInfo, _owner)
        this.simplifyAcceptanceInfo.ownerRemark = _owner.remark
        this.simplifyAcceptanceInfo.ownerPhoto = _owner.faceUrl
        this.simplifyAcceptanceInfo.ownerName = _owner.name
        if (!Object.prototype.hasOwnProperty.call(_owner, 'rooms')) {
          return
        }

        const _rooms = _owner.rooms
        if (_rooms.length > 1) {
          this.$refs.searchRoom.$emit('showOwnerRooms', _rooms)
          return
        }

        Object.assign(this.simplifyAcceptanceInfo, _rooms[0])
        this.simplifyAcceptanceInfo.roomRemark = _rooms[0].remark
        this.simplifyAcceptanceInfo.roomName = _rooms[0].floorNum + '栋' + _rooms[0].unitNum + '单元' + _rooms[0].roomNum + '室'
        this.changeTab('simplifyRoomFee')
        // 计算押金
        this.computeDeposit()
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    saveTempSearchData() {
      const _searchType = this.simplifyAcceptanceInfo.searchType
      const _searchValue = this.simplifyAcceptanceInfo.searchValue
      const _searchPlaceholder = this.simplifyAcceptanceInfo.searchPlaceholder
      // 缓存起来
      localStorage.setItem(this.TEMP_SEARCH, JSON.stringify({
        searchType: _searchType,
        searchValue: _searchValue,
        searchPlaceholder: _searchPlaceholder
      }))
    },
    // 新增：恢复搜索参数的方法
    _restoreSearchData() {
      try {
        const savedData = localStorage.getItem(this.TEMP_SEARCH)
        if (savedData) {
          const parsedData = JSON.parse(savedData)
          // 只有在没有路由参数时才恢复本地存储的数据
          if (!this.$route.query.searchValue) {
            this.simplifyAcceptanceInfo.searchType = parsedData.searchType || '1'
            this.simplifyAcceptanceInfo.searchValue = parsedData.searchValue || ''
            this.simplifyAcceptanceInfo.searchPlaceholder = parsedData.searchPlaceholder || 'simplifyAcceptance.houseNumberPlaceholder'

            // 如果有搜索值，自动执行搜索
            if (this.simplifyAcceptanceInfo.searchValue) {
              this._doSearch()
            }
          }
        }
      } catch (error) {
        console.error('恢复搜索参数失败:', error)
        // 清除可能损坏的数据
        localStorage.removeItem(this.TEMP_SEARCH)
      }
    },
    // 新增：清除保存的搜索数据
    _clearSavedSearchData() {
      localStorage.removeItem(this.TEMP_SEARCH)
    },
    changeTab(tab) {
      this.simplifyAcceptanceInfo._currentTab = tab
      setTimeout(() => {
        if (tab === 'simplifyNotepad') {
          this._toSimplifyNotepadPage()
          return
        }
        if (this.$refs[tab]) {
          if (tab === 'ownerDetailAccount' || tab === 'ownerDetailAccountReceipt') {
            this.$refs[tab].open(this.simplifyAcceptanceInfo.ownerId, this.simplifyAcceptanceInfo.ownerName, this.simplifyAcceptanceInfo.roomId)
            return;
          }
          this.$refs[tab].open({
            ownerId: this.simplifyAcceptanceInfo.ownerId,
            ownerName: this.simplifyAcceptanceInfo.ownerName,
            roomId: this.simplifyAcceptanceInfo.roomId,
            roomName: this.simplifyAcceptanceInfo.roomName,
            objId: this.simplifyAcceptanceInfo.roomId
          })
        }
      }, 1000);

    },
    _toSimplifyNotepadPage() {
      if (!this.simplifyAcceptanceInfo.ownerId) {
        this.$message.warning(this.$t('simplifyAcceptance.selectHouseFirst'))
        return
      }
      this.$router.push({
        path: '/pages/property/simplifyNotepadManage',
        query: {
          roomId: this.simplifyAcceptanceInfo.roomId,
          ownerId: this.simplifyAcceptanceInfo.ownerId
        }
      })
    },
    errorLoadImg() {
      this.simplifyAcceptanceInfo.ownerPhoto = "/img/noPhoto.jpg"
    },
    _clearData() {
      this._clearSavedSearchData()
      const _searchType = this.simplifyAcceptanceInfo.searchType
      const _searchValue = this.simplifyAcceptanceInfo.searchValue
      const _searchPlaceholder = this.simplifyAcceptanceInfo.searchPlaceholder
      this.simplifyAcceptanceInfo = {
        searchType: _searchType,
        searchValue: _searchValue,
        searchPlaceholder: _searchPlaceholder,
        ownerPhoto: '',
        _currentTab: 'simplifyRoomFee',
        roomId: '',
        ownerId: '',
        name: '',
        idCard: '',
        link: '',
        createTime: '',
        apartmentName: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        builtUpArea: '',
        feeCoefficient: '',
        stateName: '',
        roomName: '',
        sex: '',
        ownerRemark: '',
        roomRemark: '',
        roomType: '',
        roomSubTypeName: '',
        roomSubType: '',
        roomArea: '',
        roomRent: '',
        acctAmount: '',
        depositAmount: '0',
      }
      this.changeTab('simplifyRoomFee')
    },
    selectRoom(room) {
      this.simplifyAcceptanceInfo.roomId = room.roomId
      this.simplifyAcceptanceInfo.roomName = room.roomName
      this.simplifyAcceptanceInfo.searchValue = room.roomName
      this._doSearch()
    },
    _simplifyAcceptanceChooseRoom() {
      this.$refs.roomTree.open()
    },
    _handover() {
      this.$router.push('/views/room/handover')
    },
    _simplifyOwnerExitRoom() {
      this.$router.push('/views/room/ownerExitRoom')
    },
    _simplifyInputOwner() {
      if (this.simplifyAcceptanceInfo.searchType != "2" &&
        this.simplifyAcceptanceInfo.searchType != "6" &&
        this.simplifyAcceptanceInfo.searchType != "1") {
        return
      }

      if (this.simplifyAcceptanceInfo.timer) {
        clearTimeout(this.simplifyAcceptanceInfo.timer)
      }

      if (this.simplifyAcceptanceInfo.searchType == "1") {
        this.simplifyAcceptanceInfo.timer = setTimeout(() => {
          this.$refs.inputSearchRoom.$emit('searchRoom', {
            callComponent: 'simplifyAcceptance',
            roomName: this.simplifyAcceptanceInfo.searchValue
          })
        }, 1500)
        return
      }

      const _ownerTypeCd = this.simplifyAcceptanceInfo.searchType == "2" ? '1001' : '1002,1003,1005'
      this.simplifyAcceptanceInfo.timer = setTimeout(() => {
        this.$refs.inputSearchOwner.$emit('searchOwner', {
          callComponent: 'simplifyAcceptance',
          ownerTypeCd: _ownerTypeCd,
          ownerName: this.simplifyAcceptanceInfo.searchValue
        })
      }, 1500)
    },
    _toRefundDepositFee() {
      const _roomId = this.simplifyAcceptanceInfo.roomId
      const _ownerId = this.simplifyAcceptanceInfo.ownerId
      this.$router.push({
        path: '/views/fee/refundDepositFee',
        query: {
          roomId: _roomId,
          ownerId: _ownerId
        }
      })
    },
    async computeDeposit() {
      const param = {
        page: 1,
        row: 100,
        payerObjId: this.simplifyAcceptanceInfo.roomId,
        ownerId: this.simplifyAcceptanceInfo.ownerId,
        communityId: this.communityId,
        state: '1400',
      }

      this.simplifyAcceptanceInfo.depositAmount = '0'

      try {
        const res = await queryFeeDeposit(param)
        if (res.code != 0) {
          return
        }

        if (!res.data || res.data.length < 1) {
          return
        }

        let _dispositAmount = 0
        res.data.forEach(_data => {
          _dispositAmount += parseFloat(_data.receivedAmount)
        })
        this.simplifyAcceptanceInfo.depositAmount = _dispositAmount.toFixed(2)
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    handleNotifyShop(shop) {
      this.simplifyAcceptanceInfo.selectedShopId = shop.shopId
    }
  }
}
</script>

<style scoped>
.white-bg {
  background-color: #fff;
}

.padding-top-lg {
  padding-top: 20px;
}

.margin-top {
  margin-top: 15px;
}

.padding-bottom-lg {
  padding-bottom: 20px;
}

.text-center {
  text-align: center;
}

.padding-lr-0 {
  padding-left: 0;
  padding-right: 0;
}

.margin-left-xs {
  margin-left: 5px;
}

.margin-left-sm {
  margin-left: 10px;
}


.padding {
  padding: 15px;
}

.vc-float-left {
  float: left;
}

.border-radius {
  border-radius: 4px;
}

.fix-width {
  display: inline-block;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.simplify-acceptance {
  margin-left: 160px;
}

.margin-bottom {
  margin-bottom: 15px;
}

.search-input {
  width: 600px;
}

.search-switch {
  width: 150px;
}

.form-group {
  margin-bottom: 20px;
}
</style>