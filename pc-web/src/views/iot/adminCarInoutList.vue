<template>
  <div class="admin-car-inout-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminCarInout.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.carNum" :placeholder="$t('adminCarInout.search.carNum')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.state" :placeholder="$t('adminCarInout.search.state')" clearable>
                <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.carType" :placeholder="$t('adminCarInout.search.carType')" clearable>
                <el-option v-for="item in carTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.startTime" type="datetime"
                :placeholder="$t('adminCarInout.search.startTime')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.endTime" type="datetime"
                :placeholder="$t('adminCarInout.search.endTime')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-col>
            <el-col :span="4" class="text-right">
              <el-button type="primary" @click="handleSearch">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminCarInout.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column :label="$t('adminCarInout.table.photo')" align="center" width="100">
              <template slot-scope="scope">
                <el-image style="width: 60px; height: 60px; cursor: pointer;"
                  :src="scope.row.photoJpg || '/img/noPhoto.jpg'" :preview-src-list="[scope.row.photoJpg]" fit="cover" />
              </template>
            </el-table-column>
            <el-table-column prop="communityName" :label="$t('adminCarInout.table.communityName')" align="center" />
            <el-table-column prop="ciId" :label="$t('adminCarInout.table.ciId')" align="center" />
            <el-table-column :label="$t('adminCarInout.table.state')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.carInout === '3306' ? $t('adminCarInout.state.in') : $t('adminCarInout.state.out') }}({{
                  scope.row.stateName }})</div>
                <div>{{ scope.row.machineName }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="carNum" :label="$t('adminCarInout.table.carNum')" align="center" />
            <el-table-column prop="paNum" :label="$t('adminCarInout.table.paNum')" align="center" />
            <el-table-column prop="feeName" :label="$t('adminCarInout.table.feeName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.feeName }}
                <i class="el-icon-info"></i>
              </template>
            </el-table-column>
            <el-table-column prop="carTypeName" :label="$t('adminCarInout.table.carTypeName')" align="center" />
            <el-table-column prop="inTime" :label="$t('adminCarInout.table.inTime')" align="center" />
            <el-table-column :label="$t('adminCarInout.table.outTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.carInout !== '3307' ? '-' : scope.row.openTime }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminCarInout.table.parkingTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.hours }}{{ $t('adminCarInout.time.hour') }}{{ scope.row.min }}{{ $t('adminCarInout.time.min')
                }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminCarInout.table.charge')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.carType === '1001'">-</span>
                <span v-else>
                  {{ scope.row.payCharge }}
                  <span v-if="['100300', '100400', '100600'].includes(scope.row.inState)">
                    (<a href="javascript:void(0)" @click="handleCarOut(scope.row)">{{ $t('adminCarInout.action.out')
                    }}</a>)
                  </span>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('adminCarInout.table.remark')" align="center" />
          </el-table>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminCarInoutDetail } from '@/api/iot/adminCarInoutApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'

export default {
  name: 'AdminCarInoutList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        carNum: '',
        state: '',
        startTime: '',
        endTime: '',
        carType: '',
        paId: '',
        boxId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      stateOptions: [
        { value: '', label: this.$t('adminCarInout.state.select') },
        { value: '100300', label: this.$t('adminCarInout.state.inStatus') },
        { value: '100400', label: this.$t('adminCarInout.state.paid') },
        { value: '100500', label: this.$t('adminCarInout.state.outStatus') },
        { value: '100600', label: this.$t('adminCarInout.state.repay') }
      ],
      carTypeOptions: [
        { value: '', label: this.$t('adminCarInout.carType.select') },
        { value: 'T', label: this.$t('adminCarInout.carType.temp') },
        { value: 'S', label: this.$t('adminCarInout.carType.sale') },
        { value: 'H', label: this.$t('adminCarInout.carType.monthly') },
        { value: 'W', label: this.$t('adminCarInout.carType.white') },
        { value: 'B', label: this.$t('adminCarInout.carType.black') },
        { value: 'I', label: this.$t('adminCarInout.carType.internal') },
        { value: 'NM', label: this.$t('adminCarInout.carType.free') }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, records } = await listAdminCarInoutDetail(params)
        this.tableData = data
        this.pagination.total = records
      } catch (error) {
        this.$message.error(this.$t('adminCarInout.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.handleSearch()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleCarOut(row) {
      // TODO: Implement car out logic
      console.log('Car out:', row)
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-car-inout-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .text-right {
    text-align: right;
  }
}
</style>