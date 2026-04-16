<template>
  <div class="admin-owe-fee-detail-container animated fadeInRight">
    <el-row :gutter="20" class="flex justify-start">
      <el-col :span="4" class="room-floor-unit-tree">
        <community-floor-tree ref="communityFloorTree" @chooseFloor="handleChooseFloor" />
      </el-col>
      <el-col :span="20" class="margin-top-xs">
        <el-card class="box-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('adminOweFeeDetail.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="searchForm.feeTypeCd" :placeholder="$t('adminOweFeeDetail.search.feeTypeCd')" clearable
                class="w-full">
                <el-option v-for="item in feeTypeCds" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.objNameLike" :placeholder="$t('adminOweFeeDetail.search.objNameLike')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.startTime" type="date"
                :placeholder="$t('adminOweFeeDetail.search.startTime')" class="w-full" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.endTime" type="date"
                :placeholder="$t('adminOweFeeDetail.search.endTime')" class="w-full" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.ownerNameLike" :placeholder="$t('adminOweFeeDetail.search.ownerNameLike')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset" style="margin-left: 10px;">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="clearfix  text-left" >
            <span>{{ $t('adminOweFeeDetail.list.title') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="index" :label="$t('adminOweFeeDetail.table.index')" width="80" align="center" />
            <el-table-column prop="communityName" :label="$t('adminOweFeeDetail.table.communityName')" align="center" />
            <el-table-column prop="objName" :label="$t('adminOweFeeDetail.table.objName')" align="center" />
            <el-table-column prop="ownerName" :label="$t('adminOweFeeDetail.table.ownerName')" align="center" />
            <el-table-column prop="ownerTel" :label="$t('adminOweFeeDetail.table.ownerTel')" align="center" />
            <el-table-column prop="builtUpArea" :label="$t('adminOweFeeDetail.table.builtUpArea')" align="center" />
            <el-table-column prop="feeName" :label="$t('adminOweFeeDetail.table.feeName')" align="center" />
            <el-table-column prop="startTime" :label="$t('adminOweFeeDetail.table.startTime')" align="center" />
            <el-table-column prop="endTime" :label="$t('adminOweFeeDetail.table.endTime')" align="center" />
            <el-table-column prop="oweDay" :label="$t('adminOweFeeDetail.table.oweDay')" align="center" />
            <el-table-column prop="oweAmount" :label="$t('adminOweFeeDetail.table.oweAmount')" align="center" />
          </el-table>

          <el-row class="margin-top  text-left">
            <el-col :span="24">
              <div>
                <span class="font-bold">{{ $t('adminOweFeeDetail.summary.subtotal') }}</span>
                <span class="margin-left">
                  {{ $t('adminOweFeeDetail.summary.owe') }}: {{ totalPreferentialAmount }}
                  {{ $t('adminOweFeeDetail.summary.yuan') }}
                </span>
              </div>
              <div>
                <span class="font-bold">{{ $t('adminOweFeeDetail.summary.total') }}</span>
                <span class="margin-left">
                  {{ $t('adminOweFeeDetail.summary.owe') }}: {{ allOweAmount }}
                  {{ $t('adminOweFeeDetail.summary.yuan') }}
                </span>
              </div>
            </el-col>
          </el-row>

          <el-row class="margin-top  text-left">
            <el-col :span="12">
              <div>{{ $t('adminOweFeeDetail.tips.startTime') }}</div>
              <div>{{ $t('adminOweFeeDetail.tips.oweDayDeposit') }}</div>
              <div>{{ $t('adminOweFeeDetail.tips.oweDayOther') }}</div>
              <div>{{ $t('adminOweFeeDetail.tips.oweAmount') }}</div>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminOweFeeDetail } from '@/api/fee/adminOweFeeDetailApi'
import CommunityFloorTree from '@/components/fee/communityFloorTree'

export default {
  name: 'AdminOweFeeDetailList',
  components: {
    CommunityFloorTree
  },
  data() {
    return {
      loading: false,
      searchForm: {
        floorId: '',
        floorName: '',
        roomNum: '',
        objNameLike: '',
        feeTypeCd: '',
        startTime: '',
        endTime: '',
        communityId: '',
        ownerNameLike: ''
      },
      feeTypeCds: [],
      tableData: [],
      totalPreferentialAmount: 0,
      allOweAmount: 0,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      try {
        this.loading = true
        await this.getFeeTypeCds()
        await this.getList()
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async getFeeTypeCds() {
      // 这里应该是获取费用类型的字典数据
      // 假设从字典接口获取数据
      this.feeTypeCds = [
        { statusCd: '1', name: this.$t('adminOweFeeDetail.feeType.property') },
        { statusCd: '2', name: this.$t('adminOweFeeDetail.feeType.parking') }
      ]
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryAdminOweFeeDetail(params)
        this.tableData = data.map((item, index) => ({
          ...item,
          index: index + 1
        }))
        this.pagination.total = total

        // 计算小计
        let _totalPreferentialAmount = 0.0
        data.forEach(item => {
          _totalPreferentialAmount += parseFloat(item.oweAmount)
        })
        this.totalPreferentialAmount = _totalPreferentialAmount.toFixed(2)

        if (data.length > 0) {
          this.allOweAmount = data[0].allOweAmount || 0.0.toFixed(2)
        } else {
          this.allOweAmount = 0.0.toFixed(2)
        }
      } catch (error) {
        console.error(error)
        this.$message.error(this.$t('adminOweFeeDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        floorId: '',
        floorName: '',
        roomNum: '',
        objNameLike: '',
        feeTypeCd: '',
        startTime: '',
        endTime: '',
        communityId: '',
        ownerNameLike: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleChooseFloor(floor) {
      this.searchForm.floorId = floor.floorId
      this.searchForm.floorName = floor.floorNum
      this.searchForm.communityId = floor.communityId
      this.searchForm.communityName = floor.communityName
      this.handleSearch()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-owe-fee-detail-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .margin-left {
    margin-left: 10px;
  }

  .font-bold {
    font-weight: bold;
  }

  .text-right {
    text-align: right;
  }

  .room-floor-unit-tree {
    padding-right: 0;
  }
}
</style>