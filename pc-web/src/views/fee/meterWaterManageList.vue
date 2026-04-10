<template>
  <div class="meter-water-manage-container">
    <el-row :gutter="20">
      <el-col :span="4" class="tree-container">
        <room-tree-div ref="roomTree" @selectRoom="handleSelectRoom" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('meterWater.queryConditions') }}</span>
          </div>
          <el-form :inline="true" :model="conditions" class="demo-form-inline text-left">
            <el-form-item >
              <el-select v-model="conditions.meterType" :placeholder="$t('meterWater.selectMeterType')" clearable
                style="width: 100%">
                <el-option v-for="item in meterTypes" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
              </el-select>
            </el-form-item>
            <el-form-item >
              <el-input v-model="conditions.waterId" :placeholder="$t('meterWater.inputMeterId')" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleQuery">{{
                $t('common.query')
              }}</el-button>
              <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('meterWater.meterReadingInfo') }}</span>
            <div style="float: right">
              <el-button v-if="conditions.objId" type="primary" size="small" @click="handleOpenAddMeter">
                {{ $t('meterWater.readMeter') }}
              </el-button>
              <el-button type="text" size="small" @click="handleOpenMeterType">
                {{ $t('meterWater.meterType') }}
              </el-button>
              <el-button v-if="conditions.objId" type="text" size="small" @click="handleOpenQrCode">
                {{ $t('meterWater.qrCodeMeter') }}
              </el-button>
              <el-button type="text" size="small" style="margin-left: 10px" @click="handleOpenImport1">
                <i class="el-icon-plus"></i>{{ $t('meterWater.import1') }}
              </el-button>
              <el-button type="text" size="small" style="margin-left: 10px" @click="handleOpenImport2">
                <i class="el-icon-plus"></i>{{ $t('meterWater.import2') }}
              </el-button>
            </div>
          </div>

          <el-table :data="meterWaters" border style="width: 100%">
            <el-table-column prop="waterId" :label="$t('meterWater.meterId')" align="center" />
            <el-table-column prop="meterTypeName" :label="$t('meterWater.meterType')" align="center" />
            <el-table-column prop="objName" :label="$t('meterWater.objectName')" align="center" />
            <el-table-column prop="preDegrees" :label="$t('meterWater.preDegrees')" align="center" />
            <el-table-column prop="curDegrees" :label="$t('meterWater.curDegrees')" align="center" />
            <el-table-column prop="preReadingTime" :label="$t('meterWater.preReadingTime')" align="center" />
            <el-table-column prop="curReadingTime" :label="$t('meterWater.curReadingTime')" align="center" />
            <el-table-column prop="createTime" :label="$t('meterWater.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="180">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleEdit(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button type="text" size="small" @click="handleDelete(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 组件 -->
    <add-meter-water ref="addMeterWater" @success="handleSuccess" />
    <edit-meter-water ref="editMeterWater" @success="handleSuccess" />
    <delete-meter-water ref="deleteMeterWater" @success="handleSuccess" />
    <import-meter-water-fee ref="importMeterWaterFee" />
    <import-meter-water-fee2 ref="importMeterWaterFee2" />
    <room-meter-qrcode ref="roomMeterQrcode" />
  </div>
</template>

<script>
import {
  listMeterWaters,
  listMeterTypes
} from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import RoomTreeDiv from '@/components/room/roomTreeDiv'
import AddMeterWater from '@/components/fee/addMeterWater'
import EditMeterWater from '@/components/fee/editMeterWater'
import DeleteMeterWater from '@/components/fee/deleteMeterWater'
import ImportMeterWaterFee from '@/components/fee/importMeterWaterFee'
import ImportMeterWaterFee2 from '@/components/fee/importMeterWaterFee2'
import RoomMeterQrcode from '@/components/fee/roomMeterQrcode'

export default {
  name: 'MeterWaterManageList',
  components: {
    RoomTreeDiv,
    AddMeterWater,
    EditMeterWater,
    DeleteMeterWater,
    ImportMeterWaterFee,
    ImportMeterWaterFee2,
    RoomMeterQrcode
  },
  data() {
    return {
      conditions: {
        waterId: '',
        meterType: '',
        roomNum: '',
        objId: '',
        page: 1,
        row: 10,
        communityId: ''
      },
      meterWaters: [],
      meterTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      roomName: ''
    }
  },
  created() {
    this.conditions.communityId = getCommunityId()
    this.loadData()
    this.loadMeterTypes()
  },
  methods: {
    async loadData() {
      try {
        const { data, total } = await listMeterWaters(this.conditions)
        this.meterWaters = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load meter waters:', error)
      }
    },
    async loadMeterTypes() {
      try {
        const { data } = await listMeterTypes({
          communityId: this.conditions.communityId,
          page: 1,
          row: 100
        })
        this.meterTypes = data
      } catch (error) {
        console.error('Failed to load meter types:', error)
      }
    },
    handleQuery() {
      this.conditions.page = 1
      this.loadData()
    },
    handleReset() {
      this.conditions.waterId = ''
      this.conditions.meterType = ''
      this.conditions.roomNum = ''
      this.loadData()
    },
    handleSizeChange(val) {
      this.conditions.row = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.conditions.page = val
      this.loadData()
    },
    handleSelectRoom({ roomId, roomName }) {
      this.conditions.objId = roomId
      this.roomName = roomName
      this.loadData()
    },
    handleOpenAddMeter() {
      this.$refs.addMeterWater.open({
        roomId: this.conditions.objId,
        roomName: this.roomName
      })
    },
    handleEdit(row) {
      this.$refs.editMeterWater.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteMeterWater.open(row)
    },
    handleOpenImport1() {
      this.$refs.importMeterWaterFee.open()
    },
    handleOpenImport2() {
      this.$refs.importMeterWaterFee2.open()
    },
    handleOpenQrCode() {
      this.$refs.roomMeterQrcode.open({
        roomId: this.conditions.objId,
        roomName: this.roomName
      })
    },
    handleOpenMeterType() {
      this.$router.push('/views/fee/meterTypeManage')
    },
    handleSuccess() {
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.meter-water-manage-container {
  padding: 20px;

  .tree-container {
    min-height: 450px;
    overflow-y: auto;
    background: #fff;
    padding: 10px;
    border-radius: 4px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: '';
  }

  .clearfix:after {
    clear: both;
  }
}
</style>