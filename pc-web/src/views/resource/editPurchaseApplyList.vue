<template>
  <div class="edit-purchase-apply-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('editPurchaseApply.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('editPurchaseApply.back') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-card>
            <el-form label-width="120px">
              <el-form-item :label="$t('editPurchaseApply.contactPerson')">
                <el-input v-model="editPurchaseApplyInfo.endUserName"
                  :placeholder="$t('editPurchaseApply.contactPersonPlaceholder')" clearable>
                </el-input>
              </el-form-item>

              <el-form-item :label="$t('editPurchaseApply.contactPhone')">
                <el-input v-model="editPurchaseApplyInfo.endUserTel"
                  :placeholder="$t('editPurchaseApply.contactPhonePlaceholder')" clearable>
                </el-input>
              </el-form-item>

              <el-form-item :label="$t('editPurchaseApply.applyDescription')">
                <el-input type="textarea" v-model="editPurchaseApplyInfo.description"
                  :placeholder="$t('editPurchaseApply.applyDescriptionPlaceholder')" :rows="3">
                </el-input>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="clearfix">
              <span>{{ $t('editPurchaseApply.purchaseItems') }}</span>
              <div class="header-tools">
                <el-button type="primary" size="small" @click="openSelectResourceStoreDialog">
                  <i class="el-icon-search"></i>
                  {{ $t('editPurchaseApply.select') }}
                </el-button>
              </div>
            </div>

            <el-table :data="editPurchaseApplyInfo.resourceStores" border style="width: 100%">
              <el-table-column prop="type" :label="$t('editPurchaseApply.itemType')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
                </template>
              </el-table-column>

              <el-table-column prop="name" :label="$t('editPurchaseApply.itemName')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.resName }}({{ scope.row.resCode }})
                </template>
              </el-table-column>

              <el-table-column prop="spec" :label="$t('editPurchaseApply.itemSpec')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.rssName || '-' }}
                </template>
              </el-table-column>

              <el-table-column prop="price" :label="$t('editPurchaseApply.price')" align="center">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.timesId" @change="changeTimesId($event, scope.$index)"
                    style="width: 100%">
                    <el-option :value="''" :label="$t('editPurchaseApply.selectPrice')">
                    </el-option>
                    <el-option v-for="time in scope.row.times" :key="time.timesId" :value="time.timesId"
                      :label="time.price">
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>

              <el-table-column prop="stock" :label="$t('editPurchaseApply.itemStock')" align="center">
                <template slot-scope="scope">
                  {{ getTimesStock(scope.row) }}{{ scope.row.unitCodeName }}
                </template>
              </el-table-column>

              <el-table-column prop="quantity" :label="$t('editPurchaseApply.applyQuantity')" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.quantity"
                    :placeholder="$t('editPurchaseApply.applyQuantityPlaceholder')" :min="1" controls-position="right"
                    style="width: 120px">
                  </el-input-number>
                  {{ scope.row.unitCodeName }}
                </template>
              </el-table-column>

              <el-table-column prop="remark" :label="$t('editPurchaseApply.remark')" align="center">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.remark" :placeholder="$t('editPurchaseApply.remarkPlaceholder')"
                    clearable>
                  </el-input>
                </template>
              </el-table-column>

              <el-table-column :label="$t('editPurchaseApply.operation')" align="center" width="120">
                <template slot-scope="scope">
                  <el-button type="danger" size="mini" @click="removeSelectResourceStoreItem(scope.row.resId)">
                    {{ $t('editPurchaseApply.remove') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px;">
        <el-col :span="24" style="text-align: right;">
          <el-button type="primary" @click="editApplyPurchaseSummit">
            <i class="el-icon-check"></i>
            {{ $t('editPurchaseApply.submit') }}
          </el-button>
        </el-col>
      </el-row>

      <choose-resource-store2 ref="chooseResourceStoreDialog" @setSelectResourceStores="handleSelectResourceStores">
      </choose-resource-store2>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listPurchaseApplys, updatePurchaseApply } from '@/api/resource/editPurchaseApplyApi'
import ChooseResourceStore2 from '@/components/resource/chooseResourceStore2'

export default {
  name: 'EditPurchaseApplyList',
  components: {
    ChooseResourceStore2
  },
  data() {
    return {
      editPurchaseApplyInfo: {
        applyOrderId: '',
        resourceStores: [],
        description: '',
        endUserName: '',
        endUserTel: '',
        resOrderType: '10000',
        communityId: getCommunityId()
      }
    }
  },
  created() {
    this.editPurchaseApplyInfo.applyOrderId = this.$route.query.applyOrderId
    this.editPurchaseApplyInfo.resOrderType = this.$route.query.resOrderType || '10000'
    this.listPurchaseApply()
  },
  methods: {
    async listPurchaseApply() {
      try {
        const params = {
          page: 1,
          row: 1,
          applyOrderId: this.editPurchaseApplyInfo.applyOrderId,
          resOrderType: this.editPurchaseApplyInfo.resOrderType
        }
        const { data } = await listPurchaseApplys(params)
        if (data && data.length > 0) {
          const purchaseApply = data[0]
          Object.assign(this.editPurchaseApplyInfo, purchaseApply)
          this.editPurchaseApplyInfo.resourceStores = purchaseApply.purchaseApplyDetailVo || []
        }
      } catch (error) {
        console.error('获取采购申请详情失败:', error)
      }
    },
    openSelectResourceStoreDialog() {
      this.$refs.chooseResourceStoreDialog.open({
        shId: this.editPurchaseApplyInfo.shId
      })
    },
    handleSelectResourceStores(resourceStores) {
      const oldList = this.editPurchaseApplyInfo.resourceStores
      // 过滤重复选择的商品
      const newItems = resourceStores.filter(newItem => {
        return !oldList.some(oldItem => oldItem.resId === newItem.resId && (!newItem.times || newItem.times.length < 2))
      })
      this.editPurchaseApplyInfo.resourceStores = [...newItems, ...oldList]
    },
    async editApplyPurchaseSummit() {
      if (!this.editPurchaseApplyInfo.resourceStores || this.editPurchaseApplyInfo.resourceStores.length === 0) {
        this.$message.warning(this.$t('editPurchaseApply.noItemsSelected'))
        return
      }

      try {
        await updatePurchaseApply(this.editPurchaseApplyInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        console.error('提交采购申请失败:', error)
        this.$message.error(error.message || this.$t('editPurchaseApply.updateFailed'))
      }
    },
    removeSelectResourceStoreItem(resId) {
      this.editPurchaseApplyInfo.resourceStores = this.editPurchaseApplyInfo.resourceStores.filter(
        item => item.resId !== resId
      )
    },
    changeTimesId(timesId, index) {
      const times = this.editPurchaseApplyInfo.resourceStores[index].times
      const selectedTime = times.find(time => time.timesId === timesId)
      if (selectedTime) {
        this.editPurchaseApplyInfo.resourceStores[index].selectedStock = selectedTime.stock
      }
    },
    getTimesStock(resourceStore) {
      if (!resourceStore.timesId) return '-'
      if (!resourceStore.times) return 0

      const selectedTime = resourceStore.times.find(time => time.timesId === resourceStore.timesId)
      return selectedTime ? selectedTime.stock : 0
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-purchase-apply-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .header-tools {
    float: right;
  }

  .el-form-item {
    margin-bottom: 20px;
  }

  .el-select {
    width: 100%;
  }
}
</style>