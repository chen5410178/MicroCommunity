<template>
  <div class="admin-fee-config-container">
    <el-row class="animated fadeInRight" :gutter="20">
      <el-col :span="4" class="room-floor-unit-tree">
        <community-fee-type-tree ref="communityFeeTypeTree" @selectCommunity="handleSelectCommunity" 
          @selectFeeTypeCd="handleSelectFeeTypeCd" @selectFeeFlag="handleSelectFeeFlag" />
      </el-col>
      <el-col :span="20" >
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminFeeConfig.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="demo-form-inline text-left">
            <el-form-item :label="$t('adminFeeConfig.search.configId')">
              <el-input v-model="searchForm.configId" :placeholder="$t('adminFeeConfig.search.configIdPlaceholder')" />
            </el-form-item>
            <el-form-item :label="$t('adminFeeConfig.search.feeName')">
              <el-input v-model="searchForm.feeName" :placeholder="$t('adminFeeConfig.search.feeNamePlaceholder')" />
            </el-form-item>
            <el-form-item :label="$t('adminFeeConfig.search.feeFlag')">
              <el-select v-model="searchForm.feeFlag" :placeholder="$t('adminFeeConfig.search.feeFlagPlaceholder')">
                <el-option v-for="item in feeFlags" :key="item.statusCd" 
                  :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adminFeeConfig.search.paymentCd')">
              <el-select v-model="searchForm.paymentCd" :placeholder="$t('adminFeeConfig.search.paymentCdPlaceholder')">
                <el-option v-for="item in paymentCds" :key="item.statusCd" 
                  :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adminFeeConfig.search.deductFrom')">
              <el-select v-model="searchForm.deductFrom" :placeholder="$t('adminFeeConfig.search.deductFromPlaceholder')">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
              <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminFeeConfig.list.title') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="configId" :label="$t('adminFeeConfig.table.configId')" align="center" />
            <el-table-column prop="feeTypeCdName" :label="$t('adminFeeConfig.table.feeTypeCd')" align="center" />
            <el-table-column prop="feeName" :label="$t('adminFeeConfig.table.feeName')" align="center" />
            <el-table-column prop="feeFlagName" :label="$t('adminFeeConfig.table.feeFlag')" align="center" />
            <el-table-column :label="$t('adminFeeConfig.table.paymentCd')" align="center">
              <template slot-scope="scope">
                {{ scope.row.paymentCd === '1200' ? '预付费' : '后付费' }}
              </template>
            </el-table-column>
            <el-table-column prop="paymentCycle" :label="$t('adminFeeConfig.table.paymentCycle')" align="center" />
            <el-table-column prop="computingFormulaName" :label="$t('adminFeeConfig.table.computingFormula')" align="center" />
            <el-table-column :label="$t('adminFeeConfig.table.squarePrice')" align="center">
              <template slot-scope="scope">
                {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
              </template>
            </el-table-column>
            <el-table-column prop="additionalAmount" :label="$t('adminFeeConfig.table.additionalAmount')" align="center" />
            <el-table-column :label="$t('adminFeeConfig.table.deductFrom')" align="center">
              <template slot-scope="scope">
                {{ scope.row.deductFrom === 'Y' ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminFeeConfig.table.state')" align="center">
              <template slot-scope="scope">
                {{ scope.row.state === 'Y' ? '启用' : '停用' }}
              </template>
            </el-table-column>
          </el-table>

          <el-row class="margin-top text-left" >
            <el-col :span="12">
              <div class="fee-description">
                <p>{{ $t('adminFeeConfig.description.feeFlag') }}</p>
                <p>{{ $t('adminFeeConfig.description.oneTimeFee') }}</p>
                <p>{{ $t('adminFeeConfig.description.paymentType') }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <el-pagination
                :current-page="pagination.current"
                :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size"
                :total="pagination.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminFeeConfigs } from '@/api/fee/adminFeeConfigApi'
import CommunityFeeTypeTree from '@/components/fee/CommunityFeeTypeTree'
import {getDict} from '@/api/community/communityApi'

export default {
  name: 'AdminFeeConfigList',
  components: {
    CommunityFeeTypeTree
  },
  data() {
    return {
      searchForm: {
        configId: '',
        feeName: '',
        feeFlag: '',
        paymentCd: '',
        deductFrom: '',
        feeTypeCd: '',
        communityId: ''
      },
      tableData: [],
      feeFlags: [],
      paymentCds: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
    
    this.getList()
    setTimeout(() => {
      this.$refs.communityFeeTypeTree.open()
    },1000)
  },
  methods: {
    async getList() {
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { feeConfigs, total } = await queryAdminFeeConfigs(params)
        this.tableData = feeConfigs
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminFeeConfig.fetchError'))
      }
    },
    async getDictData() {
      try {
        // 这里应该调用获取字典数据的API
        this.feeFlags = await getDict('pay_fee_config','fee_flag')
         this.paymentCds = await getDict('pay_fee_config','payment_cd')
      } catch (error) {
        console.error('Failed to load dictionary data:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        configId: '',
        feeName: '',
        feeFlag: '',
        paymentCd: '',
        deductFrom: '',
        feeTypeCd: '',
        communityId: ''
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
    handleSelectCommunity({ communityId }) {
      this.searchForm.communityId = communityId
      this.searchForm.feeTypeCd = ''
      this.searchForm.feeFlag = ''
      this.handleSearch()
    },
    handleSelectFeeTypeCd({ feeTypeCd, communityId }) {
      this.searchForm.communityId = communityId
      this.searchForm.feeTypeCd = feeTypeCd
      this.searchForm.feeFlag = ''
      this.handleSearch()
    },
    handleSelectFeeFlag({ feeFlag, feeTypeCd, communityId }) {
      this.searchForm.communityId = communityId
      this.searchForm.feeTypeCd = feeTypeCd
      this.searchForm.feeFlag = feeFlag
      this.handleSearch()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-fee-config-container {
  padding: 20px;
  
  .room-floor-unit-tree {
    padding-right: 0;
  }
  
  .margin-top {
    margin-top: 20px;
  }
  
  .margin-left-sm {
    margin-left: 10px;
  }
  
  .fee-description {
    padding: 10px;
    color: #666;
    font-size: 14px;
    line-height: 1.5;
  }
}
</style>