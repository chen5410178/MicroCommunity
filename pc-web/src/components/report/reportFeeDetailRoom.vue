<template>
  <div class="report-fee-detail-room">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="floor-tree">
          <el-scrollbar style="height: 650px;">
            <el-menu>
              <el-menu-item @click="swatchFloor('')" :class="{ 'is-active': reportFeeDetailRoomInfo.floorId === '' }">
                {{ $t('reportFeeDetailRoom.all') }}
              </el-menu-item>
              <el-menu-item v-for="(item, index) in reportFeeDetailRoomInfo.floors" :key="index"
                @click="swatchFloor(item.floorId)"
                :class="{ 'is-active': reportFeeDetailRoomInfo.floorId === item.floorId }">
                {{ item.floorName }}
              </el-menu-item>
            </el-menu>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="20">
        <div class="operation-bar">
          <el-button type="primary" size="small" @click="exportReportFeeDetailRoomExcel">
            <i class="el-icon-download"></i>
            {{ $t('common.export') }}
          </el-button>
        </div>

        <el-table :data="reportFeeDetailRoomInfo.fees" border style="width: 100%" v-loading="loading">
          <el-table-column prop="roomName" :label="$t('reportFeeDetailRoom.room')" align="center">
          </el-table-column>
          <el-table-column prop="ownerName" :label="$t('reportFeeDetailRoom.owner')" align="center">
            <template slot-scope="scope">
              {{ scope.row.ownerName }}({{ scope.row.link }})
            </template>
          </el-table-column>
          <el-table-column prop="oweFee" :label="$t('reportFeeDetailRoom.oweFee')" align="center">
            <template slot-scope="scope">
              {{ scope.row.oweFee || '0' }}
            </template>
          </el-table-column>
          <el-table-column prop="receivedFee" :label="$t('reportFeeDetailRoom.receivedFee')" align="center">
            <template slot-scope="scope">
              {{ scope.row.receivedFee || '0' }}
            </template>
          </el-table-column>
          <template v-for="(item, index) in reportFeeDetailRoomInfo.feeTypeCds" >
            <el-table-column :key="index" :label="item.name + $t('reportFeeDetailRoom.owe')" align="center">
              <template slot-scope="scope">
                {{ scope.row['oweFee' + item.statusCd] || '0' }}
              </template>
            </el-table-column>
            <el-table-column :key="index+'received'" :label="item.name + $t('reportFeeDetailRoom.received')"
              align="center">
              <template slot-scope="scope">
                {{ scope.row['receivedFee' + item.statusCd] || '0' }}
              </template>
            </el-table-column>
          </template>
        </el-table>

        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryReportFeeDetailRoom, exportReportFeeDetailRoom } from '@/api/report/reportFeeDetailApi'
import {queryFloors} from '@/api/report/reportFeeSummaryApi'

export default {
  name: 'ReportFeeDetailRoom',
  data() {
    return {
      reportFeeDetailRoomInfo: {
        fees: [],
        floors: [],
        floorId: '',
        feeTypeCds: [],
        conditions: {}
      },
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.reportFeeDetailRoomInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async open(conditions) {
      this.reportFeeDetailRoomInfo.conditions = conditions
      await this.listReportFeeDetailRoomFloors()
      this.listReportFeeDetailRooms()
    },
    async listReportFeeDetailRoomFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.reportFeeDetailRoomInfo.conditions.communityId
        }

        const data  = await queryFloors(params)
        this.reportFeeDetailRoomInfo.floors = data.apiFloorDataVoList
      } catch (error) {
        console.error('获取楼层列表失败:', error)
      }
    },
    swatchFloor(floorId) {
      this.reportFeeDetailRoomInfo.floorId = floorId
      this.listReportFeeDetailRooms()
    },
    async listReportFeeDetailRooms() {
      try {
        this.loading = true
        const params = {
          ...this.reportFeeDetailRoomInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          floorId: this.reportFeeDetailRoomInfo.floorId
        }
        const { data, total } = await queryReportFeeDetailRoom(params)
        this.reportFeeDetailRoomInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取房屋费用明细失败:', error)
      } finally {
        this.loading = false
      }
    },
    async exportReportFeeDetailRoomExcel() {
      try {
        const params = {
          ...this.reportFeeDetailRoomInfo.conditions,
          pagePath: 'reportFeeDetailRoom'
        }
        await exportReportFeeDetailRoom(params)

        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')

      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFailed'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.listReportFeeDetailRooms()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.listReportFeeDetailRooms()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-room {
  .floor-tree {
    height: 700px;

    .el-menu {
      border-right: none;
    }

    .is-active {
      color: #409EFF;
      background-color: #ecf5ff;
    }
  }

  .operation-bar {
    margin-bottom: 20px;
    text-align: right;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>