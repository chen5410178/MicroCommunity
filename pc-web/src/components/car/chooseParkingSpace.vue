<template>
  <el-dialog :title="$t('auditParkingSpaceApply.selectParkingSpace')" :visible.sync="visible" width="80%"
    @close="handleClose">
    <el-card>
      <el-row :gutter="20">
        <el-col :span="18"></el-col>
        <el-col :span="6">
          <el-input-group>
            <el-input v-model="searchParams.areaNum" :placeholder="$t('auditParkingSpaceApply.inputParkingLot')" />
            <el-button type="primary" @click="queryParkingSpaces">
              {{ $t('common.search') }}
            </el-button>
          </el-input-group>
        </el-col>
      </el-row>

      <el-table :data="parkingSpaces" style="width: 100%" border>
        <el-table-column prop="areaNum" :label="$t('auditParkingSpaceApply.parkingLot')" align="center">
          <template slot-scope="scope">
            {{ scope.row.areaNum }}{{ $t('auditParkingSpaceApply.parkingLotUnit') }}
          </template>
        </el-table-column>
        <el-table-column prop="num" :label="$t('auditParkingSpaceApply.parkingSpace')" align="center">
          <template slot-scope="scope">
            {{ scope.row.num }}{{ $t('auditParkingSpaceApply.parkingSpaceUnit') }}
          </template>
        </el-table-column>
        <el-table-column prop="state" :label="$t('auditParkingSpaceApply.parkingStatus')" align="center">
          <template slot-scope="scope">
            {{ viewParkingSpaceState(scope.row.state) }}
          </template>
        </el-table-column>
        <el-table-column prop="parkingTypeName" :label="$t('auditParkingSpaceApply.parkingType')" align="center" />
        <el-table-column prop="area" :label="$t('auditParkingSpaceApply.area')" align="center" />
        <el-table-column prop="psId" :label="$t('auditParkingSpaceApply.parkingSpace') + 'ID'" align="center" />
        <el-table-column :label="$t('auditParkingSpaceApply.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="chooseParkingSpace(scope.row)">
              {{ $t('auditParkingSpaceApply.choose') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.currentPage" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>
  </el-dialog>
</template>

<script>
import { queryParkingSpaces } from '@/api/car/auditParkingSpaceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ChooseParkingSpace',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    paId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      searchParams: {
        areaNum: '',
        state: 'F' // 默认查询空闲车位
      },
      parkingSpaces: [],
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      communityId: getCommunityId()
    }
  },
  methods: {
    open() {
      this.visible = true
      this.queryParkingSpaces()
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    async queryParkingSpaces() {
      try {
        const params = {
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          communityId: this.communityId,
          ...this.searchParams,
          paId: this.paId
        }

        const res = await queryParkingSpaces(params)
        this.parkingSpaces = res.data.parkingSpaces
        this.pagination.total = res.data.total
      } catch (error) {
        this.$message.error(this.$t('common.queryFailed'))
      }
    },
    chooseParkingSpace(parkingSpace) {
      this.$emit('choose', parkingSpace)
      this.handleClose()
    },
    viewParkingSpaceState(state) {
      const states = {
        'F': this.$t('auditParkingSpaceApply.free'),
        'S': this.$t('auditParkingSpaceApply.sold'),
        'H': this.$t('auditParkingSpaceApply.rented'),
      }
      return states[state] || this.$t('auditParkingSpaceApply.unknown')
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.queryParkingSpaces()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.queryParkingSpaces()
    }
  }
}
</script>

<style scoped>
.el-input-group {
  display: flex;
}
</style>