<template>
  <div class="room-container">
    <div class="bg-white padding-sm text-left">
      <el-button type="primary" size="small" @click="openAddRoomFloorModal">
        <i class="el-icon-plus"></i>{{ $t('roomList.addBuilding') }}
      </el-button>
      <el-button type="primary" size="small" @click="openUpdateRoomFloorModal" v-if="roomInfo.conditions.floorId">
        <i class="el-icon-edit"></i>{{ $t('roomList.editBuilding') }}
      </el-button>
      <el-button type="primary" size="small" @click="openDeleteRoomFloorModal" v-if="roomInfo.conditions.floorId">
        <i class="el-icon-close"></i>{{ $t('roomList.deleteBuilding') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openAddRoomUnitModal"
        v-if="roomInfo.conditions.floorId">
        <i class="el-icon-plus"></i>{{ $t('roomList.addUnit') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openUpdateRoomUnitModal"
        v-if="roomInfo.conditions.unitId">
        <i class="el-icon-edit"></i>{{ $t('roomList.editUnit') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openDeleteRoomUnitModal"
        v-if="roomInfo.conditions.unitId">
        <i class="el-icon-close"></i>{{ $t('roomList.deleteUnit') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openAddRoom"
        v-if="hasPrivilege('502020082453087940')">
        <i class="el-icon-plus"></i>
        <span>{{ $t('roomList.addRoom') }}</span>
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openImportRoomInfo">
        <i class="el-icon-plus"></i>
        <span>{{ $t('roomList.importRoom') }}</span>
      </el-button>
      <!-- <el-button type="primary" size="small" @click="showMarkdown">
        <i class="el-icon-document"></i>
        <span>{{ $t('roomList.document') }}</span>
      </el-button> -->
    </div>

    <!-- 替换原有的 el-row 布局为可拖拽布局 -->
    <div class="resizable-layout margin-top-sm">
      <!-- 左侧树形组件 -->
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <floor-unit-tree ref="floorUnitTree" @switchFloorUnit="switchFloorUnit" />
      </div>

      <!-- 拖拽分隔线 -->
      <div class="resize-handle" @mousedown="startResize" @mouseover="showResizeCursor" @mouseleave="hideResizeCursor">
        <div class="resize-line"></div>
      </div>

      <!-- 右侧内容区域 -->
      <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
        <el-card class="ibox">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('roomList.queryCondition') }}</span>
          </div>
          <el-form :inline="true" class="text-left">
            <el-form-item>
              <el-input :placeholder="$t('roomList.roomIdPlaceholder')" size="small"
                v-model="roomInfo.conditions.roomId" class="form-control form-control-sm"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="roomInfo.conditions.state" size="small" :placeholder="$t('roomList.selectState')">
                <el-option v-for="(item, index) in roomInfo.states" :key="index" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select v-model="roomInfo.conditions.roomSubType" size="small"
                :placeholder="$t('roomList.selectRoomType')">
                <el-option v-for="(item, index) in roomInfo.roomSubTypes" :key="index" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select v-model="roomInfo.isAll" size="small" @change="changeIsAll">
                <el-option label="当前楼栋单元" value="N"></el-option>
                <el-option label="全部楼栋单元" value="Y"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="queryRoomMethod">
                <i class="el-icon-search"></i>{{ $t('roomList.query') }}
              </el-button>
              <el-button type="info" size="small" @click="resetRoomInfo">
                <i class="el-icon-refresh"></i>{{ $t('roomList.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="">
          <div class=" border-radius">
            <div>
              <el-table :data="roomInfo.rooms" style="width: 100%" border stripe>
                <el-table-column prop="roomId" :label="$t('roomList.room')" width="120" fixed="left"
                  class-name="text-center hc-table-fix-left-td">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="layer" :label="$t('roomList.floor')" width="100" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      {{ scope.row.layer }}{{ $t('roomList.layer') }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="ownerName" :label="$t('roomList.owner')" width="150" align="center">
                  <template slot-scope="scope">
                    <div v-if="scope.row.ownerName" class="hc-td hand">
                      <a target="_blank" :href="'/#/views/owner/ownerDetail?ownerId=' + scope.row.ownerId">
                        {{ scope.row.ownerName }}({{ scope.row.link }})
                      </a>
                    </div>
                    <div v-else class="hc-td textAuto">-</div>
                  </template>
                </el-table-column>
                <el-table-column prop="roomSubTypeName" :label="$t('roomList.type')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">{{ scope.row.roomSubTypeName }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.area')" width="150" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      {{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}
                      {{ $t('roomList.squareMeter') }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="roomRent" :label="$t('roomList.rent')" width="100" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">{{ scope.row.roomRent }}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="stateName" :label="$t('roomList.roomStatus')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">{{ scope.row.stateName }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.validity')" width="150" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      {{ scope.row.startTime }}<br>
                      ~{{ scope.row.endTime }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.ownerMembers')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewOwnerMembers(scope.row)">
                        {{ scope.row.memberCount || 0 }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.ownerCars')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewOwnerCars(scope.row)">
                        {{ scope.row.carCount || 0 }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.ownerRooms')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewOwnerRooms(scope.row)">
                        {{ scope.row.roomCount || 0 }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.complaints')" width="100" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewComplaints(scope.row)">
                        {{ scope.row.complaintCount || 0 }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.repairs')" width="100" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewRepairs(scope.row)">
                        {{ scope.row.repairCount || 0 }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.roomOweFee')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewRoomOweFees(scope.row)">
                        {{ scope.row.roomOweFee || '0.00' }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.ownerOweFee')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewOweFees(scope.row)">
                        {{ scope.row.oweFee || '0.00' }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.roomContracts')" width="120" align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">
                      <a href="javascript:void(0)" @click="viewRoomContracts(scope.row)">
                        {{ scope.row.contractCount || 0 }}
                      </a>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column v-for="(item, index) in roomInfo.listColumns" :key="index" :label="item" width="120"
                  align="center">
                  <template slot-scope="scope">
                    <div class="hc-td">{{ scope.row.listValues[index] || '' }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('roomList.operation')" fixed="right" width="180" align="center"
                  class-name="text-center">
                  <template slot-scope="scope" class="custom-button-group">
                    <el-button-group>
                      <el-button v-if="hasPrivilege('502020082493857941')" size="mini"
                        @click="openEditRoomModel(scope.row)">
                        {{ $t('roomList.edit') }}
                      </el-button>
                      <el-button v-if="hasPrivilege('502020082417457942')" size="mini"
                        @click="openDelRoomModel(scope.row)">
                        {{ $t('roomList.delete') }}
                      </el-button>
                      <el-button v-if="scope.row.state == '2002' && hasPrivilege('502023021978930012')" size="mini"
                        @click="toRoomBindOwner(scope.row)">
                        {{ $t('roomList.handOver') }}
                      </el-button>
                      <el-button
                        v-if="scope.row.state != '2002' && scope.row.ownerId && hasPrivilege('502023021973760015')"
                        size="mini" @click="toRoomUnBindOwner(scope.row)">
                        {{ $t('roomList.checkOut') }}
                      </el-button>
                      <el-button v-if="scope.row.state != '2002'" size="mini" @click="toSimplifyAcceptance(scope.row)">
                        {{ $t('roomList.businessAcceptance') }}
                      </el-button>
                    </el-button-group>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <el-row class="margin-top">
              <el-col :span="12" class="text-left room-detail-desc">
                <div>{{ $t('roomList.roomStatusDesc1') }}</div>
                <div>{{ $t('roomList.roomStatusDesc2') }}</div>
                <div>{{ $t('roomList.roomStatusDesc3') }}</div>
                <div>{{ $t('roomList.rentDesc') }}</div>
              </el-col>
              <el-col :span="12" class="float-right">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                  :current-page="roomInfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="DEFAULT_ROW"
                  layout="total, sizes, prev, pager, next, jumper" :total="roomInfo.total"></el-pagination>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 子组件 -->
    <add-floor ref="addFloor" @handleRefreshTree="floorUnitTree" />
    <edit-floor ref="editFloor" @handleRefreshTree="floorUnitTree" />
    <delete-floor ref="deleteFloor" @handleRefreshTree="floorUnitTree" />
    <add-unit ref="addUnit" @handleRefreshTree="floorUnitTree" />
    <edit-unit ref="editUnit" @handleRefreshTree="floorUnitTree" />
    <delete-unit ref="deleteUnit" @handleRefreshTree="floorUnitTree" />
    <search-floor ref="searchFloor" emit-choose-floor="room" emit-load-data="room" />
    <edit-room ref="editRoom" @handleRefreshRoom="handleRefreshRoom" />
    <delete-room ref="deleteRoom" @handleRefreshRoom="handleRefreshRoom" />
    <import-owner-room ref="importOwnerRoom" />

    <owner-rooms ref="ownerRooms" />
    <owner-members ref="ownerMembers" />
    <owner-cars ref="ownerCars" />
    <owner-complaints ref="ownerComplaints" />
    <owner-repairs ref="ownerRepairs" />
    <owner-owe-fees ref="ownerOweFees" />
    <room-owe-fees ref="roomOweFees" />
    <room-contracts ref="roomContracts" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryRooms, getFloors, getUnits } from '@/api/room/roomApi'
import FloorUnitTree from '@/components/room/floorUnitTree'
import SearchFloor from '@/components/room/searchFloor'
import EditRoom from '@/components/room/editRoom'
import DeleteRoom from '@/components/room/deleteRoom'
import ImportOwnerRoom from '@/components/room/importOwnerRoom'
import AddFloor from '@/components/room/addFloor'
import EditFloor from '@/components/room/editFloor'
import DeleteFloor from '@/components/room/deleteFloor'
import AddUnit from '@/components/room/addUnit'
import EditUnit from '@/components/room/editUnit'
import DeleteUnit from '@/components/room/deleteUnit'
import OwnerRooms from '@/components/owner/ownerRooms'
import OwnerMembers from '@/components/owner/ownerMembers'
import OwnerCars from '@/components/owner/ownerCars'
import OwnerComplaints from '@/components/owner/ownerComplaints'
import OwnerRepairs from '@/components/owner/ownerRepairs'
import OwnerOweFees from '@/components/owner/ownerOweFees'
import RoomOweFees from '@/components/room/roomOweFees'
import RoomContracts from '@/components/room/roomContracts'

import { getAttrSpecList } from '@/api/dev/attrSpecApi'
// import { getAttrValueList } from '@/api/dev/attrValueApi'

const DEFAULT_PAGE = 1
const DEFAULT_ROW = 10

export default {
  name: 'RoomList',
  components: {
    FloorUnitTree,
    SearchFloor,
    EditRoom,
    DeleteRoom,
    ImportOwnerRoom,
    AddFloor,
    EditFloor,
    DeleteFloor,
    AddUnit,
    EditUnit,
    DeleteUnit,
    OwnerRooms,
    OwnerMembers,
    OwnerCars,
    OwnerComplaints,
    OwnerRepairs,
    OwnerOweFees,
    RoomOweFees,
    RoomContracts
  },
  data() {
    return {
      DEFAULT_PAGE,
      DEFAULT_ROW,
      callbackListener: 'room',
      roomInfo: {
        rooms: [],
        total: 0,
        records: 1,
        floorId: '',
        unitId: '',
        states: [],
        roomNum: '',
        isAll: 'N',
        moreCondition: false,
        conditions: {
          floorId: '',
          floorName: '',
          unitId: '',
          roomNum: '',
          roomId: '',
          state: '',
          section: '',
          roomSubType: '',
          flag: ''
        },
        currentPage: DEFAULT_PAGE,
        listColumns: [],
        roomSubTypes: []
      },
      // 新增：拖拽相关数据
      treeWidth: 300, // 默认树形组件宽度
      minTreeWidth: 200, // 最小宽度
      maxTreeWidth: 600, // 最大宽度
      isResizing: false,
      startX: 0,
      startWidth: 0
    }
  },
  mounted() {
    this.initData()
    // 恢复保存的宽度
    this.restoreTreeWidth()
    // 添加全局鼠标事件监听
    document.addEventListener('mousemove', this.onMouseMove)
    document.addEventListener('mouseup', this.onMouseUp)
  },
  beforeDestroy() {
    // 移除全局事件监听
    document.removeEventListener('mousemove', this.onMouseMove)
    document.removeEventListener('mouseup', this.onMouseUp)
  },
  methods: {
    async initData() {
      this.roomInfo.conditions.floorId = this.$route.query.floorId || ''
      this.roomInfo.conditions.floorName = this.$route.query.floorName || ''

      try {
        // 获取房屋状态字典
        const stateData = await getDict('building_room', 'state')
        this.roomInfo.states = stateData

        // 获取房屋类型字典
        const roomTypeData = await getDict('building_room', 'room_sub_type')
        this.roomInfo.roomSubTypes = roomTypeData

        this.listRoom(DEFAULT_PAGE, DEFAULT_ROW)
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    handleRefreshRoom() {
      this.listRoom(this.roomInfo.conditions.page, this.roomInfo.conditions.row)
    },
    async listRoom(page, row) {
      this.roomInfo.conditions.page = page
      this.roomInfo.conditions.row = row
      this.roomInfo.conditions.communityId = this.getCommunityId()

      const params = { ...this.roomInfo.conditions }
      delete params.roomType


      // 处理房间号格式
      const allNum = params.roomId || ''
      const allNums = allNum.split('-')
      if (allNums.length >= 3) {
        params.floorNum = allNums[0].trim()
        params.unitNum = allNums[1].trim()
        params.roomNum = allNums.slice(2).join('-').trim()
        params.roomId = ''
        params.unitId = ''
        params.floorId = ''
      }

      try {
        const res = await queryRooms(params)
        this.roomInfo.total = res.total
        this.roomInfo.records = res.records
        this.roomInfo.rooms = res.rooms
        this.dealRoomAttr(res.rooms)
      } catch (error) {
        console.error('获取房间列表失败:', error)
        this.$message.error('获取房间列表失败')
      }
    },

    dealRoomAttr(rooms) {
      if (!rooms || !rooms.length) return

      // 获取表格列配置
      this.getColumns(rooms, () => {
        rooms.forEach(item => {
          this.getColumnsValue(item)
        })
      })
    },

    getColumnsValue(room) {
      room.listValues = []
      if (!room.roomAttrDto || !room.roomAttrDto.length) {
        this.roomInfo.listColumns.forEach(() => {
          room.listValues.push('')
        })
        return
      }

      this.roomInfo.listColumns.forEach(column => {
        const attr = room.roomAttrDto.find(dto => dto.specName === column)
        room.listValues.push(attr ? attr.valueName : '')
      })
    },

    async getColumns(rooms, callback) {
      try {
        const { data } = await getAttrSpecList({
          page: 1,
          row: 100,
          tableName: 'building_room_attr'
        })
        this.roomInfo.listColumns = data
          .filter(item => item.listShow === 'Y')
          .map(item => item.specName)
        callback()
      } catch (error) {
        console.error('获取房屋属性失败:', error)
      }
    },

    switchFloorUnit(data) {
      this.roomInfo.conditions.floorId = data.floorId
      this.roomInfo.conditions.unitId = data.unitId
      this.roomInfo.conditions.flag = 1
      this.roomInfo.isAll = 'N'
      this.listRoom(DEFAULT_PAGE, DEFAULT_ROW)
    },

    handleSizeChange(val) {
      this.listRoom(this.roomInfo.currentPage, val)
    },

    handleCurrentChange(val) {
      this.roomInfo.currentPage = val
      this.listRoom(val, DEFAULT_ROW)
    },


    handleResize() {
      this.$forceUpdate()
    },

    floorUnitTree(param) {
      this.$refs.floorUnitTree.refreshTree(param)
    },
    // 按钮操作相关方法
   async openAddRoom() {
    let _floorId = this.roomInfo.conditions.floorId;
      if(this.roomInfo.conditions.unitId){
        const unitData = await this.loadUnits(this.roomInfo.conditions.unitId)
        _floorId = unitData.floorId;
      }
      this.$router.push('/views/room/addRoomView?floorId='+_floorId+"&unitId="+this.roomInfo.conditions.unitId)
    },

    openEditRoomModel(room) {
      this.$refs.editRoom.open(room)
    },

    openDelRoomModel(room) {
      this.$refs.deleteRoom.open(room)
    },

    queryRoomMethod() {
      this.roomInfo.conditions.flag = 0
      this.listRoom(DEFAULT_PAGE, DEFAULT_ROW)
    },

    resetRoomInfo() {
      this.roomInfo.conditions = {
        floorId: '',
        floorName: '',
        unitId: '',
        roomNum: '',
        roomId: '',
        state: '',
        section: '',
        roomSubType: '',
        flag: ''
      }
      this.listRoom(DEFAULT_PAGE, DEFAULT_ROW)
    },

    openImportRoomInfo() {
      this.$refs.importOwnerRoom.open()
    },

    openAddRoomFloorModal() {
      this.$refs.addFloor.open()
    },

    async openUpdateRoomFloorModal() {
      if (!this.roomInfo.conditions.floorId) {
        this.$message.warning('请先选择楼栋')
        return
      }

      try {
        const floorData = await this.loadFloor(this.roomInfo.conditions.floorId)
        this.$refs.editFloor.open(floorData)
      } catch (error) {
        console.error('加载楼栋信息失败:', error)
      }
    },

    async openDeleteRoomFloorModal() {
      if (!this.roomInfo.conditions.floorId) {
        this.$message.warning('请先选择楼栋')
        return
      }

      try {
        const floorData = await this.loadFloor(this.roomInfo.conditions.floorId)
        this.$refs.deleteFloor.open(floorData)
      } catch (error) {
        console.error('加载楼栋信息失败:', error)
      }
    },

    openAddRoomUnitModal() {
      if (!this.roomInfo.conditions.floorId) {
        this.$message.warning('请先选择楼栋')
        return
      }
      this.$refs.addUnit.open({ floorId: this.roomInfo.conditions.floorId })
    },

    async openUpdateRoomUnitModal() {
      if (!this.roomInfo.conditions.unitId) {
        this.$message.warning('请先选择单元')
        return
      }

      try {
        const unitData = await this.loadUnits(this.roomInfo.conditions.unitId)
        this.$refs.editUnit.open(unitData)
      } catch (error) {
        console.error('加载单元信息失败:', error)
      }
    },

    async openDeleteRoomUnitModal() {
      if (!this.roomInfo.conditions.unitId) {
        this.$message.warning('请先选择单元')
        return
      }

      try {
        const unitData = await this.loadUnits(this.roomInfo.conditions.unitId)
        this.$refs.deleteUnit.open(unitData)
      } catch (error) {
        console.error('加载单元信息失败:', error)
      }
    },

    changeIsAll() {
      if (this.roomInfo.isAll === 'Y') {
        this.roomInfo.conditions.floorId = ''
        this.roomInfo.conditions.unitId = ''
      }
      this.listRoom(this.roomInfo.currentPage, DEFAULT_ROW)
    },

    toSimplifyAcceptance(room) {
      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${room.floorNum}-${room.unitNum}-${room.roomNum}`)
    },

    viewOwnerRooms(room) {
      this.$refs.ownerRooms.open(room)
    },

    viewOwnerMembers(room) {
      this.$refs.ownerMembers.open(room.ownerId)
    },

    viewOwnerCars(room) {
      this.$refs.ownerCars.open(room.ownerId)
    },

    viewComplaints(room) {
      this.$refs.ownerComplaints.open(room.ownerId)
    },

    viewRepairs(room) {
      this.$refs.ownerRepairs.open(room.ownerId)
    },

    viewOweFees(room) {
      this.$refs.ownerOweFees.open(room.ownerId)
    },

    viewRoomOweFees(room) {
      this.$refs.roomOweFees.open(room.roomId)
    },

    viewRoomContracts(room) {
      this.$refs.roomContracts.open({ roomId: room.roomId })
    },

    toRoomBindOwner(room) {
      this.$router.push(`/views/owner/roomBindOwner?roomId=${room.roomId}`)
    },

    toRoomUnBindOwner(room) {
      this.$router.push(`/views/owner/deleteOwnerRoom?ownerId=${room.ownerId}`)
    },

    showMarkdown() {
      this.$router.push('/pages/property/room')
    },

    async loadFloor(floorId) {
      const params = {
        page: 1,
        row: 1,
        communityId: this.getCommunityId(),
        floorId: floorId
      }
      const res = await getFloors(params)
      return res.apiFloorDataVoList[0]
    },

    async loadUnits(unitId) {
      const params = {
        unitId: unitId,
        communityId: this.getCommunityId(),
      }
      const res = await getUnits(params)
      return res[0]
    },
     // 新增：恢复保存的树形组件宽度
     restoreTreeWidth() {
      const savedWidth = localStorage.getItem('roomListTreeWidth')
      if (savedWidth) {
        const width = parseInt(savedWidth)
        if (width >= this.minTreeWidth && width <= this.maxTreeWidth) {
          this.treeWidth = width
        }
      }
    },
    // 新增：保存树形组件宽度
    saveTreeWidth() {
      localStorage.setItem('roomListTreeWidth', this.treeWidth.toString())
    },
    // 新增：开始拖拽调整大小
    startResize(event) {
      event.preventDefault()
      this.isResizing = true
      this.startX = event.clientX
      this.startWidth = this.treeWidth
      
      // 添加拖拽时的样式
      document.body.style.cursor = 'col-resize'
      document.body.style.userSelect = 'none'
    },
    // 新增：鼠标移动处理
    onMouseMove(event) {
      if (!this.isResizing) return
      
      const deltaX = event.clientX - this.startX
      const newWidth = this.startWidth + deltaX
      
      // 限制宽度范围
      if (newWidth >= this.minTreeWidth && newWidth <= this.maxTreeWidth) {
        this.treeWidth = newWidth
      }
    },
    // 新增：鼠标释放处理
    onMouseUp() {
      if (this.isResizing) {
        this.isResizing = false
        this.saveTreeWidth()
        
        // 恢复鼠标样式
        document.body.style.cursor = ''
        document.body.style.userSelect = ''
      }
    },
    // 新增：显示调整大小光标
    showResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = 'col-resize'
      }
    },
    // 新增：隐藏调整大小光标
    hideResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = ''
      }
    },
  }
}
</script>
<style scoped>
.room-container {
  padding: 15px;
  height: calc(100vh - 120px);
  overflow: hidden;
}

/* 新增：可调整大小的布局样式 */
.resizable-layout {
  display: flex;
  height: 100%;
  position: relative;
}

.tree-panel {
  background: #fff;
  border-radius: 4px;
  padding: 10px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  min-height: 450px;
}

.content-panel {
  overflow-y: auto;
}

/* 新增：拖拽分隔线样式 */
.resize-handle {

  width: 10px;
  cursor: col-resize;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-handle:hover .resize-line {
  background-color: #409eff;
}

.resize-line {
  width: 2px;
  height: 30px;
  background-color: #dcdfe6;
  border-radius: 1px;
  transition: background-color 0.2s;
}

/* 拖拽时的样式 */
.resizable-layout.resizing {
  cursor: col-resize;
}

.resizable-layout.resizing .tree-panel,
.resizable-layout.resizing .content-panel {
  pointer-events: none;
}

.resizable-layout.resizing .resize-handle {
  pointer-events: auto;
}

/* 原有样式保持不变 */
.bg-white {
  background-color: #fff;
}

.padding-sm {
  padding: 10px;
}

.margin-left-sm {
  margin-left: 10px;
}

.margin-top-xs {
  margin-top: 5px;
}

.ibox {
  margin-bottom: 15px;
}

.ibox-title {
  border-color: #e7eaec;
  background-color: #f9f9f9;
  border-bottom: 1px solid #e7eaec;
  padding: 10px 15px;
}

.ibox-content {
  padding: 15px;
}

.hc-table-div {
  overflow-x: auto;
}

.hc-td {
  padding: 5px;
  text-align: center;
}

.text-center {
  text-align: center;
}

.hand {
  cursor: pointer;
}

.textAuto {
  text-align: center;
}

.float-right {
  float: right;
}

.border-radius {
  border-radius: 4px;
}

/* 在全局样式文件中 */
.el-button-group .el-button {
  margin-right: 5px;
  margin-bottom: 5px;
}

.el-button-group .el-button:last-child {
  margin-right: 0;
}

.room-detail-desc {
  text-align: left;
  font-size: 13px;
  color: #606266;
}

/* 确保树形组件在拖拽时不被选中 */
.tree-panel * {
  user-select: none;
}

/* 拖拽时禁用文本选择 */
.resizable-layout.resizing * {
  user-select: none !important;
}
</style>