<template>
  <div class="scrap-goods-step-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('viewResourceMyGoodsInfo.goodsInfo') }}</span>
        <div class="action-buttons">
          <el-button type="primary" size="small" @click="handleGoBack">
            <i class="el-icon-close"></i>
            {{ $t('viewResourceMyGoodsInfo.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleOpenSelectModal">
            <i class="el-icon-search"></i>
            {{ $t('viewResourceMyGoodsInfo.selectGoods') }}
          </el-button>
        </div>
      </div>

      <el-table :data="form.resourceStores" border style="width: 100%">
        <el-table-column prop="resName" :label="$t('viewResourceMyGoodsInfo.goodsName')" align="center" />
        <el-table-column prop="resCode" :label="$t('viewResourceMyGoodsInfo.goodsCode')" align="center" />
        <el-table-column :label="$t('viewResourceMyGoodsInfo.goodsType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="isFixedName" :label="$t('viewResourceMyGoodsInfo.isFixedGoods')" align="center" />
        <el-table-column :label="$t('viewResourceMyGoodsInfo.goodsStock')" align="center">
          <template slot-scope="scope">
            {{ scope.row.stock }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('viewResourceMyGoodsInfo.miniStock')" align="center">
          <template slot-scope="scope">
            {{ scope.row.miniStock }}{{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('viewResourceMyGoodsInfo.lossQuantity')" align="center">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.giveQuantity" :min="0" :max="parseFloat(scope.row.miniStock)"
              :precision="2" controls-position="right" style="width: 120px">
            </el-input-number>
            {{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('viewResourceMyGoodsInfo.usageType')" align="center">
          <template slot-scope="scope">
            <el-select v-model="scope.row.state" style="width: 100%">
              <el-option :label="$t('viewResourceMyGoodsInfo.selectUsageType')" value=""></el-option>
              <el-option :label="$t('viewResourceMyGoodsInfo.scrapRecycle')" value="1001"></el-option>
              <el-option :label="$t('viewResourceMyGoodsInfo.publicLoss')" value="3003"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('viewResourceMyGoodsInfo.remark')" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.purchaseRemark" :placeholder="$t('viewResourceMyGoodsInfo.requiredRemark')">
            </el-input>
          </template>
        </el-table-column>
        <el-table-column :label="$t('viewResourceMyGoodsInfo.operation')" align="center">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="handleRemoveItem(scope.row.resId, scope.$index)">
              <i class="el-icon-delete"></i>
              {{ $t('viewResourceMyGoodsInfo.remove') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('scrapGoodsStep.lossDescription') }}</span>
          </div>
          <el-form :model="form" label-width="120px">
            <el-form-item :label="$t('scrapGoodsStep.lossDescription')">
              <el-input type="textarea" v-model="form.remark" :placeholder="$t('scrapGoodsStep.requiredRemark')"
                :rows="4">
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="22" :offset="2">
        <div class="action-buttons">
          <el-button type="primary" @click="handleSubmit">
            {{ $t('scrapGoodsStep.submit') }}
          </el-button>
        </div>
      </el-col>
    </el-row>
    <choose-resource-staff ref="chooseResourceStaff" @setSelectResourceStores="handleSetSelectedStores">
    </choose-resource-staff>
  </div>
  
</template>

<script>
import { saveAllocationUserStorehouse } from '@/api/resource/scrapGoodsStepApi'
import { getCommunityId } from '@/api/community/communityApi'

import ChooseResourceStaff from '@/components/resource/chooseResourceStaff'

export default {
  name: 'ScrapGoodsStepList',
  components: {
    ChooseResourceStaff

  },
  data() {
    return {
      form: {
        resourceStores: [],
        remark: '',
        communityId: '',
        acceptUserId: '',
        acceptUserName: '',
        flag: '1'
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    handleGoBack() {
      this.$router.go(-1)
    },
    handleNotify(goodsInfo) {
      this.form.resourceStores = goodsInfo
    },
    handleOpenSelectModal() {
      this.$refs.chooseResourceStaff.open()
    },
    handleSetSelectedStores(resourceStores) {
      this.form.resourceStores = resourceStores.map(item => {
        return {
          ...item,
          state: '',
          purchaseRemark: '',
          giveQuantity: 0
        }
      })
    },
    handleRemoveItem(resId, index) {
      this.form.resourceStores.splice(index, 1)
      this.$refs.chooseResourceStaff.removeSelectedItem(resId)
    },
    handleSubmit() {
      if (this.form.resourceStores.length === 0) {
        this.$message.error(this.$t('scrapGoodsStep.selectGoodsFirst'))
        return
      }

      if (!this.form.remark) {
        this.$message.error(this.$t('scrapGoodsStep.requiredRemark'))
        return
      }

      // 校验商品信息
      for (const item of this.form.resourceStores) {
        if (!item.giveQuantity || parseFloat(item.giveQuantity) <= 0) {
          this.$message.error(this.$t('scrapGoodsStep.requiredQuantity'))
          return
        }

        if (parseFloat(item.giveQuantity) > parseFloat(item.miniStock)) {
          this.$message.error(`${item.resName} ${this.$t('scrapGoodsStep.stockNotEnough')}`)
          return
        }

        if (!item.state) {
          this.$message.error(this.$t('scrapGoodsStep.requiredUsageType'))
          return
        }

        if (!item.purchaseRemark) {
          this.$message.error(this.$t('scrapGoodsStep.requiredNote'))
          return
        }
      }

      saveAllocationUserStorehouse(this.form)
        .then(response => {
          if (response.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.$router.push('/resource/myResourceStoreManage')
          } else {
            this.$message.error(response.msg)
          }
        })
        .catch(error => {
          this.$message.error(this.$t('scrapGoodsStep.submitFailed'))
          console.error(error)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.scrap-goods-step-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .action-buttons {
    text-align: right;
    margin-bottom: 20px;
  }
}
</style>