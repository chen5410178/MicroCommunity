<template>
  <div class="admin-meter-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminMeter.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="searchForm.machineNameLike" :placeholder="$t('adminMeter.search.machineName')"
                clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model="searchForm.address" :placeholder="$t('adminMeter.search.address')" clearable />
            </el-col>
            <el-col :span="6">
              <el-select v-model="searchForm.machineModel" :placeholder="$t('adminMeter.search.machineModel')" clearable>
                <el-option :label="$t('adminMeter.model.recharge')" value="1001" />
                <el-option :label="$t('adminMeter.model.reading')" value="2002" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-input v-model="searchForm.roomNameLike" :placeholder="$t('adminMeter.search.roomName')" clearable />
            </el-col>
            <el-col :span="24" style="margin-top: 10px">
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminMeter.table.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="machineId" :label="$t('adminMeter.table.machineId')" align="center" />
            <el-table-column prop="communityName" :label="$t('adminMeter.table.communityName')" align="center" />
            <el-table-column prop="machineName" :label="$t('adminMeter.table.machineName')" align="center" />
            <el-table-column prop="address" :label="$t('adminMeter.table.address')" align="center" />
            <el-table-column :label="$t('adminMeter.table.meterType')" align="center">
              <template slot-scope="scope">
                {{ getMeterTypeName(scope.row.meterType) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminMeter.table.machineModel')" align="center">
              <template slot-scope="scope">
                {{ scope.row.machineModel === '1001' ? $t('adminMeter.model.recharge') : $t('adminMeter.model.reading') }}
              </template>
            </el-table-column>
            <el-table-column prop="roomName" :label="$t('adminMeter.table.roomName')" align="center" />
            <el-table-column prop="implBeanName" :label="$t('adminMeter.table.implBeanName')" align="center" />
            <el-table-column :label="$t('adminMeter.table.degree')" align="center">
              <template slot-scope="scope">
                {{ scope.row.machineModel === '1001' ? (scope.row.degree || scope.row.prestoreDegrees) : '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminMeter.table.curDegrees')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.machineModel === '1001'">
                  {{ scope.row.curDegrees }}
                </span>
                <span v-else>
                  {{ scope.row.curDegrees }}({{ $t('adminMeter.table.readingTime', {
                    day: scope.row.readDay, hour:
                      scope.row.readHours
                  }) }})
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="curReadingTime" :label="$t('adminMeter.table.curReadingTime')" align="center" />
            <el-table-column prop="stateName" :label="$t('adminMeter.table.stateName')" align="center" />
            <el-table-column prop="heartbeatTime" :label="$t('adminMeter.table.heartbeatTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { listAdminMeterMachine } from '@/api/iot/adminMeterApi'
import { jumpToIot } from '@/api/user/menuApi'

export default {
  name: 'AdminMeterList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        machineNameLike: '',
        address: '',
        machineModel: '',
        roomNameLike: '',
        communityId: ''
      },
      tableData: [],
      meterTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
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
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await listAdminMeterMachine(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminMeter.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
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
    getMeterTypeName(meterType) {
      const type = this.meterTypes.find(item => item.typeId === meterType)
      return type ? type.typeName : '-'
    },
    handleDetail(row) {
      jumpToIot('/#/pages/meter/adminMeterDetail?machineId=' + row.machineId + '&roomId=' + row.roomId)
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-meter-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .el-select {
    width: 100%;
  }
}
</style>