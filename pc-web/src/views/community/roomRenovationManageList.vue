<template>
  <div class="room-renovation-container">
    <!-- 查询条件 -->
    <el-card class="box-card">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('roomRenovationManage.queryCondition') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="toggleMoreCondition">
          {{ roomRenovationManageInfo.moreCondition ? $t('roomRenovationManage.hide') : $t('roomRenovationManage.more')
          }}
        </el-button>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input v-model.trim="roomRenovationManageInfo.conditions.roomName"
              :placeholder="$t('roomRenovationManage.roomNumberPlaceholder')" clearable />
          </el-col>
          <el-col :span="6">
            <el-input v-model.trim="roomRenovationManageInfo.conditions.personName"
              :placeholder="$t('roomRenovationManage.contactPersonPlaceholder')" clearable />
          </el-col>
          <el-col :span="6">
            <el-input v-model.trim="roomRenovationManageInfo.conditions.personTel"
              :placeholder="$t('roomRenovationManage.phonePlaceholder')" clearable maxlength="11" />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="queryRoomRenovationMethod">
              <i class="el-icon-search"></i>
              {{ $t('roomRenovationManage.search') }}
            </el-button>
            <el-button @click="resetRoomRenovationMethod" style="margin-left: 20px;">
              <i class="el-icon-refresh"></i>
              {{ $t('roomRenovationManage.reset') }}
            </el-button>
          </el-col>
        </el-row>

        <div v-show="roomRenovationManageInfo.moreCondition">
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="6">
              <el-select v-model="roomRenovationManageInfo.conditions.state"
                :placeholder="$t('roomRenovationManage.statusPlaceholder')" clearable>
                <el-option v-for="(item, index) in roomRenovationManageInfo.states" :key="index" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="roomRenovationManageInfo.conditions.isPostpone"
                :placeholder="$t('roomRenovationManage.postponePlaceholder')" clearable>
                <el-option :label="$t('roomRenovationManage.yes')" value="Y" />
                <el-option :label="$t('roomRenovationManage.no')" value="N" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-date-picker v-model="roomRenovationManageInfo.conditions.renovationTime" type="date"
                :placeholder="$t('roomRenovationManage.renovationTimePlaceholder')" value-format="yyyy-MM-dd" />
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="6">
              <el-date-picker v-model="roomRenovationManageInfo.conditions.startTime" type="date"
                :placeholder="$t('roomRenovationManage.startTimePlaceholder')" value-format="yyyy-MM-dd" />
            </el-col>
            <el-col :span="6">
              <el-date-picker v-model="roomRenovationManageInfo.conditions.endTime" type="date"
                :placeholder="$t('roomRenovationManage.endTimePlaceholder')" value-format="yyyy-MM-dd" />
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>

    <!-- 装修信息 -->
    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('roomRenovationManage.renovationInfo') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="openAddRoomRenovationModal">
          <i class="el-icon-plus"></i>
          {{ $t('roomRenovationManage.add') }}
        </el-button>
      </div>
      <div>
        <el-table :data="roomRenovationManageInfo.roomRenovations" border style="width: 100%" v-loading="loading">
          <el-table-column prop="roomName" :label="$t('roomRenovationManage.room')" align="center" />
          <el-table-column prop="personName" :label="$t('roomRenovationManage.contactPerson')" align="center" />
          <el-table-column prop="personTel" :label="$t('roomRenovationManage.phone')" align="center" />
          <el-table-column :label="$t('roomRenovationManage.renovationTime')" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.startTime }}</div>
              <div>{{ scope.row.endTime }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('roomRenovationManage.applyTime')" align="center" />
          <el-table-column prop="renovationCompany" :label="$t('roomRenovationManage.renovationCompany')"
            align="center" />
          <el-table-column prop="personMain" :label="$t('roomRenovationManage.renovationManager')" align="center" />
          <el-table-column prop="personMainTel" :label="$t('roomRenovationManage.managerPhone')" align="center" />
          <el-table-column prop="stateName" :label="$t('roomRenovationManage.status')" align="center" />
          <el-table-column :label="$t('roomRenovationManage.isPostponed')" align="center">
            <template slot-scope="scope">
              {{ scope.row.isPostpone === 'Y' ? $t('roomRenovationManage.yes') : $t('roomRenovationManage.no') }}
            </template>
          </el-table-column>
          <el-table-column prop="postponeTime" :label="$t('roomRenovationManage.postponeTime')" align="center" />
          <el-table-column :label="$t('roomRenovationManage.isViolation')" align="center">
            <template slot-scope="scope">
              {{ scope.row.isViolation === 'Y' ? $t('roomRenovationManage.yes') : $t('roomRenovationManage.no') }}
            </template>
          </el-table-column>
          <el-table-column prop="violationDesc" :label="$t('roomRenovationManage.violationDesc')" align="center" />
          <el-table-column prop="remark" :label="$t('roomRenovationManage.remark')" align="center" />
          <el-table-column :label="$t('roomRenovationManage.operation')" align="center" width="150">
            <template slot-scope="scope">
              <!-- <el-button v-if="scope.row.state === '1000'" size="mini" type="text"
                @click="openRoomRenovationFee(scope.row)">
                {{ $t('roomRenovationManage.fee') }}
              </el-button> -->
              <el-button size="mini" v-if="scope.row.state === '1000' && scope.row.isViolation === 'N'" type="text"
                @click="openToExamine(scope.row)">
                {{ $t('roomRenovationManage.review') }}
              </el-button>

              <el-button size="mini" v-if="scope.row.state === '3000' && scope.row.isViolation === 'N'" type="text"
                @click="openDecorationCompleted(scope.row)">
                {{ $t('roomRenovationManage.completeRenovation') }}
              </el-button>
              <el-button size="mini" v-if="scope.row.state === '4000' && scope.row.isViolation === 'N'" type="text"
                @click="openDecorationAcceptanceModel(scope.row)">
                {{ $t('roomRenovationManage.renovationAcceptance') }}
              </el-button>
              <el-button size="mini" v-if="scope.row.state === '5000' || scope.row.state === '6000'" type="text"
                @click="openRoomRenovationDetail(scope.row)">
                {{ $t('roomRenovationManage.acceptanceDetail') }}
              </el-button>
              <el-button size="mini" type="text" @click="openEditRoomRenovationModel(scope.row)">
                {{ $t('roomRenovationManage.modify') }}
              </el-button>
              <el-button size="mini" type="text" @click="openDeleteRoomRenovationModel(scope.row)">
                {{ $t('roomRenovationManage.delete') }}
              </el-button>
              <el-button size="mini" type="text" @click="openRoomDecorationRecord(scope.row)">
                {{ $t('roomRenovationManage.trackRecord') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="page.current" :page-sizes="[10, 20, 50]" :page-size="page.size"
          layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="margin-top: 20px;" />
      </div>
    </el-card>

    <!-- 子组件 -->
    <add-room-renovation ref="addRoomRenovation" @success="listRoomRenovation" />
    <room-decoration-acceptance ref="roomDecorationAcceptance" @success="listRoomRenovation" />
    <room-to-examine ref="roomToExamine" @success="listRoomRenovation" />
    <edit-room-renovation ref="editRoomRenovation" @success="listRoomRenovation" />
    <delete-room-renovation ref="deleteRoomRenovation" @success="listRoomRenovation" />
    <room-renovation-completed ref="roomRenovationCompleted" @success="listRoomRenovation" />
  </div>
</template>

<script>
import { listRoomRenovation } from '@/api/community/roomRenovationManageApi'
import AddRoomRenovation from '@/components/community/AddRoomRenovation'
import RoomDecorationAcceptance from '@/components/community/RoomDecorationAcceptance'
import RoomToExamine from '@/components/community/RoomToExamine'
import EditRoomRenovation from '@/components/community/EditRoomRenovation'
import DeleteRoomRenovation from '@/components/community/DeleteRoomRenovation'
import RoomRenovationCompleted from '@/components/community/RoomRenovationCompleted'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'RoomRenovationManageList',
  components: {
    AddRoomRenovation,
    RoomDecorationAcceptance,
    RoomToExamine,
    EditRoomRenovation,
    DeleteRoomRenovation,
    RoomRenovationCompleted
  },
  data() {
    return {
      loading: false,
      roomRenovationManageInfo: {
        roomRenovations: [],
        moreCondition: false,
        states: [],
        conditions: {
          roomName: '',
          personName: '',
          personTel: '',
          state: '',
          isPostpone: '',
          renovationTime: '',
          startTime: '',
          endTime: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
    this.listRoomRenovation()
  },
  methods: {
    // 获取字典数据
    async getDictData() {
      try {
        const res = await getDict('room_renovation', 'state')
        this.roomRenovationManageInfo.states = res || []
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    // 查询装修信息
    async listRoomRenovation() {
      this.loading = true
      try {
        const params = {
          ...this.roomRenovationManageInfo.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: this.getCommunityId()
        }

        const { data, total } = await listRoomRenovation(params)
        this.roomRenovationManageInfo.roomRenovations = data
        this.page.total = total
      } catch (error) {
        console.error('查询装修信息失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 切换更多条件
    toggleMoreCondition() {
      this.roomRenovationManageInfo.moreCondition = !this.roomRenovationManageInfo.moreCondition
    },

    // 查询
    queryRoomRenovationMethod() {
      this.page.current = 1
      this.listRoomRenovation()
    },

    // 重置
    resetRoomRenovationMethod() {
      this.roomRenovationManageInfo.conditions = {
        roomName: '',
        personName: '',
        personTel: '',
        state: '',
        isPostpone: '',
        renovationTime: '',
        startTime: '',
        endTime: '',
        communityId: this.getCommunityId()
      }
      this.page.current = 1
      this.listRoomRenovation()
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.page.size = val
      this.listRoomRenovation()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.page.current = val
      this.listRoomRenovation()
    },

    // 打开添加模态框
    openAddRoomRenovationModal() {
      this.$refs.addRoomRenovation.open()
    },

    // 打开修改模态框
    openEditRoomRenovationModel(row) {
      this.$refs.editRoomRenovation.open(row)
    },

    // 打开删除模态框
    openDeleteRoomRenovationModel(row) {
      this.$refs.deleteRoomRenovation.open(row)
    },

    // 打开审核模态框
    openToExamine(row) {
      this.$refs.roomToExamine.open(row)
    },

    // 打开验收模态框
    openDecorationAcceptanceModel(row) {
      this.$refs.roomDecorationAcceptance.open(row)
    },

    // 打开装修完成模态框
    openDecorationCompleted(row) {
      this.$refs.roomRenovationCompleted.open(row)
    },

    // 打开费用页面
    openRoomRenovationFee(row) {
      this.$router.push({
        path: '/fee/room-fee',
        query: { roomId: row.roomId }
      })
    },

    // 打开跟踪记录
    openRoomDecorationRecord(row) {
      this.$router.push({
        path: '/views/community/listRoomDecorationRecord',
        query: {
          roomId: row.roomId,
          rId: row.rId,
          roomName: row.roomName,
          state: row.state,
          stateName: row.stateName
        }
      })
    },

    // 打开验收明细
    openRoomRenovationDetail(row) {
      this.$router.push({
        path: '/views/community/listRoomDecorationRecord',
        query: {
          roomId: row.roomId,
          rId: row.rId,
          roomName: row.roomName,
          state: row.state,
          stateName: row.stateName
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.room-renovation-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both;
  }

  .el-date-editor {
    width: 100%;
  }

  .el-date-editor.el-input {
    width: 100%;
  }

}
</style>