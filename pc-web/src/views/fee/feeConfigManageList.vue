<template>
  <div class="fee-config-manage-container animated ">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="room-floor-unit-tree">
          <fee-type-tree ref="feeTypeTree"  @selectFeeType="handleSelectFeeType" />
        </div>
      </el-col>
      <el-col :span="20">
        <!-- 查询条件 -->
        <el-card class="margin-top-xs">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('feeConfigManage.search.title') }}</span>
            <div >
              <el-button type="text" @click="_moreCondition()">
                {{ feeConfigManageInfo.moreCondition ? $t('feeConfigManage.search.hide') :
                  $t('feeConfigManage.search.more') }}
              </el-button>
            </div>
          </div>
          <el-form :model="feeConfigManageInfo.conditions" label-width="0">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="feeConfigManageInfo.conditions.configId"
                    :placeholder="$t('feeConfigManage.search.configIdPlaceholder')" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="feeConfigManageInfo.conditions.feeName"
                    :placeholder="$t('feeConfigManage.search.feeNamePlaceholder')" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="feeConfigManageInfo.conditions.feeFlag"
                    :placeholder="$t('feeConfigManage.search.feeFlagPlaceholder')" style="width: 100%" clearable>
                    <el-option v-for="(item, index) in feeConfigManageInfo.feeFlags" :key="index" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="feeConfigManageInfo.conditions.paymentCd"
                    :placeholder="$t('feeConfigManage.search.paymentCdPlaceholder')" style="width: 100%" clearable>
                    <el-option v-for="(item, index) in feeConfigManageInfo.paymentCds" :key="index" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4" style="text-align: right;">
                <el-button type="primary" size="small" @click="_queryFeeConfigMethod()">
                  {{ $t('feeConfigManage.search.query') }}
                </el-button>
                <el-button size="small" @click="_resetFeeConfigMethod()" style="margin-left: 10px;">
                  {{ $t('feeConfigManage.search.reset') }}
                </el-button>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-show="feeConfigManageInfo.moreCondition">
              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="feeConfigManageInfo.conditions.deductFrom"
                    :placeholder="$t('feeConfigManage.search.deductFromPlaceholder')" style="width: 100%" clearable>
                    <el-option label="是" value="Y" />
                    <el-option label="否" value="N" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="feeConfigManageInfo.conditions.isDefault"
                    :placeholder="$t('feeConfigManage.search.isDefaultPlaceholder')" style="width: 100%" clearable>
                    <el-option v-for="(item, index) in feeConfigManageInfo.isDefaults" :key="index" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

          </el-form>
        </el-card>

        <!-- 费用项列表 -->
        <el-card class="margin-top-xs">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('feeConfigManage.list.title') }}</span>
            <div style="float: right;">
             
              <el-button type="primary" size="small" @click="_openAddFeeConfigModal()"
                v-if="hasPrivilege('502022022571930560') && feeConfigManageInfo.conditions.ftId"
                style="margin-left: 10px;">
                {{ $t('feeConfigManage.list.add') }}
              </el-button>
            </div>
          </div>

          <el-table v-loading="loading" :data="feeConfigManageInfo.feeConfigs" border style="width: 100%">
            <el-table-column prop="configId" :label="$t('feeConfigManage.table.number')" align="center" width="100" />
            <el-table-column :label="$t('feeConfigManage.table.feeType')" align="center" min-width="120">
              <template slot-scope="scope">
                {{ scope.row.feeTypeCdName }}>{{ scope.row.ftName }}
              </template>
            </el-table-column>
            <el-table-column prop="feeName" :label="$t('feeConfigManage.table.feeName')" align="center"
              min-width="120" />
            <el-table-column prop="feeFlagName" :label="$t('feeConfigManage.table.feeFlag')" align="center"
              width="100" />
            <el-table-column :label="$t('feeConfigManage.table.paymentType')" align="center" width="100">
              <template slot-scope="scope">
                {{ scope.row.paymentCd == '1200' ? '预付费' : '后付费' }}
              </template>
            </el-table-column>
            <el-table-column prop="paymentCycle" :label="$t('feeConfigManage.table.paymentCycle')" align="center"
              width="120" />
            <el-table-column prop="computingFormulaName" :label="$t('feeConfigManage.table.formula')" align="center"
              width="100" />
            <el-table-column :label="$t('feeConfigManage.table.unitPrice')" align="center" width="120">
              <template slot-scope="scope">
                {{ scope.row.computingFormula == '2002' ? '-' : scope.row.squarePrice }}
              </template>
            </el-table-column>
            <el-table-column prop="additionalAmount" :label="$t('feeConfigManage.table.additionalAmount')"
              align="center" width="140" />
            <el-table-column :label="$t('feeConfigManage.table.billing')" align="center" width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.deductFrom == 'C'">当月</span>
                <span v-if="scope.row.deductFrom == 'H'">上月</span>
                <span v-if="scope.row.deductFrom == 'N'">下月</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('feeConfigManage.table.status')" align="center" width="80">
              <template slot-scope="scope">
                {{ scope.row.state == 'Y' ? '启用' : '停用' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('feeConfigManage.table.operation')" align="center" width="280" fixed="right">
              <template slot-scope="scope">
                <el-button v-if="hasPrivilege('502022022549630561')" type="text" size="mini"
                  @click="_openEditFeeConfigModel(scope.row)">
                  {{ $t('feeConfigManage.table.edit') }}
                </el-button>
                <el-button type="text" size="mini" @click="_settingConfigDiscount(scope.row)">
                  {{ $t('feeConfigManage.table.discount') }}
                </el-button>
                <el-button v-if="scope.row.isDefault == 'F' && hasPrivilege('502022022586400562')" type="text" size="mini"
                  @click="_openDeleteFeeConfigModel(scope.row)">
                  {{ $t('feeConfigManage.table.delete') }}
                </el-button>
                <el-button type="text" size="mini" @click="_openFeeConfigDetail(scope.row)">
                  {{ $t('feeConfigManage.table.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-row class="margin-top-xs">
            <el-col :span="18">
              <div class="explanation-text">
                <div>{{ $t('feeConfigManage.list.explanation1') }}</div>
                <div>{{ $t('feeConfigManage.list.explanation2') }}</div>
                <div>{{ $t('feeConfigManage.list.explanation3') }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <el-pagination :current-page.sync="feeConfigManageInfo.curPage" :page-sizes="[10, 20, 30, 50]"
                :page-size="page_size" :total="feeConfigManageInfo.total"
                layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" style="float: right;" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <add-fee-config ref="addFeeConfig" />
    <edit-fee-config ref="editFeeConfig" />
    <delete-fee-config ref="deleteFeeConfig" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listFeeConfigs } from '@/api/fee/feeConfigManageApi'
import FeeTypeTree from '@/components/fee/FeeTypeTree'
import AddFeeConfig from '@/components/fee/addFeeConfig'
import EditFeeConfig from '@/components/fee/editFeeConfig'
import DeleteFeeConfig from '@/components/fee/deleteFeeConfig'


export default {
  name: 'FeeConfigManageList',
  components: {
    FeeTypeTree,
    AddFeeConfig,
    EditFeeConfig,
    DeleteFeeConfig
  },
  data() {
    return {
      loading: false,
      page_size:10,
      feeConfigManageInfo: {
        feeConfigs: [],
        total: 0,
        records: 1,
        moreCondition: false,
        feeName: '',
        feeTypeCds: [],
        feeFlags: [],
        paymentCds: [],
        billTypes: [],
        isDefaults: [],
        curPage: 1,
        conditions: {
          configId: '',
          feeFlag: '',
          billType: '',
          feeName: '',
          feeTypeCd: '',
          isDefault: 'F',
          paymentCd: '',
          deductFrom: '',
          ftId: ''
        }
      }
    }
  },
  created() {
    this._initMethod()
  },
  mounted() {
    this._initEvent()
  },
  methods: {
    async _initMethod() {
      await this._listFeeConfigs(1, 10)
      await this.loadDictData()
    },

    _initEvent() {
      // 事件监听器已移除，使用 this.$emit 直接通知
    },

    async loadDictData() {
      try {
        // 费用类型字典
        const feeTypeData = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeConfigManageInfo.feeTypeCds = [{
          statusCd: '',
          name: this.$t('feeConfigManage.search.all')
        }, ...feeTypeData]

        // 费用标识字典
        this.feeConfigManageInfo.feeFlags = await getDict('pay_fee_config', 'fee_flag')

        // 付费类型字典
        this.feeConfigManageInfo.paymentCds = await getDict('pay_fee_config', 'payment_cd')

        // 默认费用字典
        this.feeConfigManageInfo.isDefaults = await getDict('pay_fee_config', 'is_default')
      } catch (error) {
        console.error('加载字典数据失败:', error)
        this.$message.error(this.$t('feeConfigManage.loadDictError'))
      }
    },

    handleSelectFeeTypeCd(param) {
      this.feeConfigManageInfo.conditions.feeTypeCd = param.feeTypeCd
      this.feeConfigManageInfo.conditions.ftId = ''
      this._listFeeConfigs(1, 10)
    },

    handleSelectFeeType(param) {
      this.feeConfigManageInfo.conditions.feeTypeCd = param.feeTypeCd
      this.feeConfigManageInfo.conditions.ftId = param.ftId
      this._listFeeConfigs(1, 10)
    },

    handleListFeeConfig() {
      this._listFeeConfigs(this.feeConfigManageInfo.curPage, 10)
    },

    async _listFeeConfigs(page, rows) {
      try {
        this.loading = true
        this.feeConfigManageInfo.conditions.page = page
        this.feeConfigManageInfo.conditions.row = rows
        this.feeConfigManageInfo.conditions.communityId = getCommunityId()

        // 清理参数
        const params = {
          ...this.feeConfigManageInfo.conditions,
          feeName: this.feeConfigManageInfo.conditions.feeName.trim(),
          configId: this.feeConfigManageInfo.conditions.configId.trim()
        }

        const { feeConfigs, total, records } = await listFeeConfigs(params)
        this.feeConfigManageInfo.total = total
        this.feeConfigManageInfo.records = records
        this.feeConfigManageInfo.feeConfigs = feeConfigs
      } catch (error) {
        console.error('查询费用配置失败:', error)
        this.$message.error(this.$t('feeConfigManage.queryError'))
      } finally {
        this.loading = false
      }
    },

    _resetListFeeConfigs() {
      this.feeConfigManageInfo.conditions.configId = ''
      this.feeConfigManageInfo.conditions.feeName = ''
      this.feeConfigManageInfo.conditions.feeTypeCd = ''
      this.feeConfigManageInfo.conditions.feeFlag = ''
      this.feeConfigManageInfo.conditions.paymentCd = ''
      this.feeConfigManageInfo.conditions.billType = ''
      this.feeConfigManageInfo.conditions.isDefault = ''
      this.feeConfigManageInfo.conditions.deductFrom = ''
      this._listFeeConfigs(1, 10)
    },

    _openAddFeeConfigModal() {
      this.$refs.addFeeConfig.open({
        ftId: this.feeConfigManageInfo.conditions.ftId,
        feeTypeCd: this.feeConfigManageInfo.conditions.feeTypeCd
      })
    },

    _openEditFeeConfigModel(feeConfig) {
      this.$refs.editFeeConfig.open(feeConfig)
    },

    _openDeleteFeeConfigModel(feeConfig) {
      this.$refs.deleteFeeConfig.open(feeConfig)
    },

    _queryFeeConfigMethod() {
      this._listFeeConfigs(1, 10)
    },

    _resetFeeConfigMethod() {
      this._resetListFeeConfigs()
    },

    _moreCondition() {
      this.feeConfigManageInfo.moreCondition = !this.feeConfigManageInfo.moreCondition
    },

    _settingConfigDiscount(feeConfig) {
      this.$router.push({
        path: '/views/fee/payFeeConfigDiscountManage',
        query: {
          configId: feeConfig.configId,
          feeName: feeConfig.feeName
        }
      })
    },

    _openFeeConfigDetail(feeConfig) {
      window.open(`/#/views/fee/feeConfigDetail?configId=${feeConfig.configId}`)
    },

    handleSizeChange(val) {
      this.feeConfigManageInfo.curPage = 1
      this._listFeeConfigs(1, val)
    },

    handleCurrentChange(val) {
      this._listFeeConfigs(val, 10)
    },


  },


}
</script>

<style lang="scss" scoped>
.fee-config-manage-container {
  padding: 20px;
  margin: 0;

  .room-floor-unit-tree {
    background-color: #fff;
    padding: 10px;
    border-radius: 4px;
    height: calc(100vh - 200px);
    overflow-y: auto;
  }

  .margin-top-xs {
    margin-top: 10px;
  }



}


</style>