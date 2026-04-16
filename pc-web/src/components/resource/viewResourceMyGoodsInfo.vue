<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('viewResourceMyGoodsInfo.goodsInfo') }}</span>
      <div class="action-buttons">
        <el-button 
          type="primary" 
          size="small" 
          @click="handleGoBack">
          <i class="el-icon-close"></i>
          {{ $t('viewResourceMyGoodsInfo.back') }}
        </el-button>
        <el-button 
          type="primary" 
          size="small"
          @click="handleOpenSelectModal">
          <i class="el-icon-search"></i>
          {{ $t('viewResourceMyGoodsInfo.selectGoods') }}
        </el-button>
      </div>
    </div>

    <el-table 
      :data="resourceStores" 
      border 
      style="width: 100%">
      <el-table-column 
        prop="resName" 
        :label="$t('viewResourceMyGoodsInfo.goodsName')" 
        align="center" />
      <el-table-column 
        prop="resCode" 
        :label="$t('viewResourceMyGoodsInfo.goodsCode')" 
        align="center" />
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.goodsType')" 
        align="center">
        <template slot-scope="scope">
          {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
        </template>
      </el-table-column>
      <el-table-column 
        prop="isFixedName" 
        :label="$t('viewResourceMyGoodsInfo.isFixedGoods')" 
        align="center" />
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.goodsStock')" 
        align="center">
        <template slot-scope="scope">
          {{ scope.row.stock }}{{ scope.row.unitCodeName }}
        </template>
      </el-table-column>
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.miniStock')" 
        align="center">
        <template slot-scope="scope">
          {{ scope.row.miniStock }}{{ scope.row.miniUnitCodeName }}
        </template>
      </el-table-column>
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.lossQuantity')" 
        align="center">
        <template slot-scope="scope">
          <el-input-number
            v-model="scope.row.giveQuantity"
            :min="0"
            :max="parseFloat(scope.row.miniStock)"
            :precision="2"
            controls-position="right"
            style="width: 120px">
          </el-input-number>
          {{ scope.row.miniUnitCodeName }}
        </template>
      </el-table-column>
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.usageType')" 
        align="center">
        <template slot-scope="scope">
          <el-select 
            v-model="scope.row.state" 
            style="width: 100%">
            <el-option 
              :label="$t('viewResourceMyGoodsInfo.selectUsageType')" 
              value=""></el-option>
            <el-option 
              :label="$t('viewResourceMyGoodsInfo.scrapRecycle')" 
              value="1001"></el-option>
            <el-option 
              :label="$t('viewResourceMyGoodsInfo.publicLoss')" 
              value="3003"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.remark')" 
        align="center">
        <template slot-scope="scope">
          <el-input 
            v-model="scope.row.purchaseRemark" 
            :placeholder="$t('viewResourceMyGoodsInfo.requiredRemark')">
          </el-input>
        </template>
      </el-table-column>
      <el-table-column 
        :label="$t('viewResourceMyGoodsInfo.operation')" 
        align="center">
        <template slot-scope="scope">
          <el-button 
            type="danger" 
            size="mini" 
            @click="handleRemoveItem(scope.row.resId, scope.$index)">
            <i class="el-icon-delete"></i>
            {{ $t('viewResourceMyGoodsInfo.remove') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <choose-resource-staff 
      ref="chooseResourceStaff"
      @setSelectResourceStores="handleSetSelectedStores">
    </choose-resource-staff>
  </el-card>
</template>

<script>
import ChooseResourceStaff from './chooseResourceStaff.vue'

export default {
  name: 'ViewResourceMyGoodsInfo',
  components: {
    ChooseResourceStaff
  },
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      resourceStores: [],
      index: 0
    }
  },
  methods: {
    handleGoBack() {
      this.$router.go(-1)
    },
    handleOpenSelectModal() {
      this.$refs.chooseResourceStaff.open()
    },
    handleSetSelectedStores(resourceStores) {
      this.resourceStores = resourceStores.map(item => {
        return {
          ...item,
          state: '',
          purchaseRemark: '',
          giveQuantity: 0
        }
      })
      this.$emit(this.callBackFunction, this.resourceStores)
    },
    handleRemoveItem(resId, index) {
      this.resourceStores.splice(index, 1)
      this.$emit(this.callBackFunction, this.resourceStores)
      this.$refs.chooseResourceStaff.removeSelectedItem(resId)
    }
  }
}
</script>

<style scoped>
.action-buttons {
  float: right;
}
</style>