<template>
  <div>
    <el-row class="margin-top-lg">
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-select v-model="simplifyMeterWaterFeeInfo.meterType" size="small" @change="_listSimplifyMeterWaterFee(1, 10)">
          <el-option :label="$t('simplifyMeterWaterLog.selectMeterType')" value=""></el-option>
          <el-option v-for="(item, index) in simplifyMeterWaterFeeInfo.meterTypes" :key="index" :label="item.typeName"
            :value="item.typeId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="20" class="text-right" v-if="simplifyMeterWaterFeeInfo.roomId">
        <!-- <el-button type="primary" size="small" style="margin-left:10px" @click="_openAddMeterWaterModal()">
          <i class="el-icon-plus"></i> {{ $t('simplifyMeterWaterLog.addMeterReading') }}
        </el-button> -->
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="simplifyMeterWaterFeeInfo.meterWaters" style="width: 100%; margin-top:10px" border>
        <el-table-column prop="meterTypeName" :label="$t('simplifyMeterWaterLog.meterType')"
          align="center"></el-table-column>
        <el-table-column prop="objName" :label="$t('simplifyMeterWaterLog.objectName')" align="center"></el-table-column>
        <el-table-column prop="preDegrees" :label="$t('simplifyMeterWaterLog.previousReading')"
          align="center"></el-table-column>
        <el-table-column prop="curDegrees" :label="$t('simplifyMeterWaterLog.currentReading')"
          align="center"></el-table-column>
        <el-table-column prop="preReadingTime" :label="$t('simplifyMeterWaterLog.previousReadingTime')"
          align="center"></el-table-column>
        <el-table-column prop="curReadingTime" :label="$t('simplifyMeterWaterLog.currentReadingTime')"
          align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('simplifyMeterWaterLog.createTime')"
          align="center"></el-table-column>
      </el-table>

      <el-row>
        <el-col :span="12">&nbsp;</el-col>
        <el-col :span="12" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="simplifyMeterWaterFeeInfo.total"
            style="margin-top: 20px"></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listMeterWaters, listMeterTypes } from '@/api/fee/simplifyMeterWaterLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SimplifyMeterWaterLog',
  data() {
    return {
      simplifyMeterWaterFeeInfo: {
        meterTypes: [],
        meterWaters: [],
        total: 0,
        records: 1,
        roomId: '',
        roomName: '',
        ownerName: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        meterType: "",
      },
      currentPage: 1,
      pageSize: 10
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.switchData(params)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listSimplifyMeterWaterFee(val, this.pageSize)
    },
    _listSimplifyMeterWaterFee(page, row) {
      const param = {
        page,
        row,
        communityId: this.communityId,
        objId: this.simplifyMeterWaterFeeInfo.roomId,
        meterType: this.simplifyMeterWaterFeeInfo.meterType
      }

      listMeterWaters(param).then(response => {
        const data = response.data
        this.simplifyMeterWaterFeeInfo.total = data.total
        this.simplifyMeterWaterFeeInfo.records = data.records
        this.simplifyMeterWaterFeeInfo.meterWaters = data.data
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _openAddMeterWaterModal() {
      this.$emit('openAddModal', {
        roomId: this.simplifyMeterWaterFeeInfo.roomId,
        roomName: this.simplifyMeterWaterFeeInfo.roomName,
        ownerName: this.simplifyMeterWaterFeeInfo.ownerName
      })
    },
    clearSimplifyMeterWaterFeeInfo() {
      this.simplifyMeterWaterFeeInfo = {
        meterTypes: [],
        meterWaters: [],
        total: 0,
        records: 1,
        roomId: '',
        ownerName: '',
        roomName: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        meterType: ""
      }
    },
    _listMeterTypes() {
      const param = {
        page: 1,
        row: 100,
        communityId: this.communityId
      }

      listMeterTypes(param).then(response => {
        const data = response.data
        this.simplifyMeterWaterFeeInfo.meterTypes = data.data
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    switchData(param) {
      if (param.roomId === '') return
      this.clearSimplifyMeterWaterFeeInfo()
      Object.assign(this.simplifyMeterWaterFeeInfo, param)
      this._listMeterTypes()
      this._listSimplifyMeterWaterFee(1, 10)
    },

  }
}
</script>

<style scoped>
.margin-top-lg {
  margin-top: 20px;
}

.margin-top {
  margin-top: 15px;
}

.padding-right-xs {
  padding-right: 5px;
}

.padding-left-xl {
  padding-left: 20px;
}

.text-right {
  text-align: right;
}
</style>