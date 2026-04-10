<template>
  <div class="finish-repair-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('finishRepair.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="form-wrapper">
            <el-form label-width="120px">
              <el-form-item :label="$t('finishRepair.materialUsage')" v-if="finishRepairInfo.repairObjType != '004'">
                <el-select v-model="finishRepairInfo.maintenanceType"
                  :placeholder="$t('finishRepair.selectMaterialUsage')" style="width:100%">
                  <template v-for="(item, index) in finishRepairInfo.maintenanceTypes">
                    <el-option :key="index" :label="item.name" :value="item.statusCd"
                      v-if="item.statusCd != '1001' && item.statusCd != '1002'">
                    </el-option>
                  </template>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('finishRepair.repairType')" v-if="finishRepairInfo.repairObjType == '004'">
                <el-select v-model="finishRepairInfo.maintenanceType" :placeholder="$t('finishRepair.selectRepairType')"
                  style="width:100%">
                  <template v-for="(item, index) in finishRepairInfo.maintenanceTypes">
                    <el-option :key="index" :label="item.name" :value="item.statusCd"
                      v-if="item.statusCd != '1003' && item.statusCd != '1004'">
                    </el-option>
                  </template>
                </el-select>
              </el-form-item>

              <el-form-item
                v-if="finishRepairInfo.maintenanceType == '1001' || finishRepairInfo.maintenanceType == '1003'">
                <el-button type="primary" @click="openChooseSingleResourceModel">
                  <i class="el-icon-plus"></i>{{ $t('finishRepair.selectGoods') }}
                </el-button>
              </el-form-item>

              <!-- 商品列表展示 -->
              <div
                v-if="(finishRepairInfo.maintenanceType == '1001' || finishRepairInfo.maintenanceType == '1003') && finishRepairInfo.choosedGoodsList.length > 0">
                <el-form-item :label="$t('finishRepair.usedGoods')">
                  <el-table :data="finishRepairInfo.choosedGoodsList" border>
                    <el-table-column prop="rstName" :label="$t('finishRepair.category')" ></el-table-column>
                    <el-table-column prop="resName" :label="$t('finishRepair.goods')"></el-table-column>
                    <el-table-column prop="price" :label="$t('finishRepair.price')" 
                      v-if="finishRepairInfo.maintenanceType == '1001'">
                      <template slot-scope="scope">
                        <el-input-number v-model="scope.row.price"
                          :disabled="!scope.row.isCustom && scope.row.outHighPrice == scope.row.outLowPrice"
                          @change="updateTotalPrice" :min="scope.row.outLowPrice" :max="scope.row.outHighPrice">
                        </el-input-number>
                        <span v-if="!scope.row.isCustom && scope.row.outHighPrice != scope.row.outLowPrice">
                          ({{ scope.row.outLowPrice }} - {{ scope.row.outHighPrice }})
                        </span>
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('finishRepair.quantity')" >
                      <template slot-scope="scope">
                        <el-input-number v-model="scope.row.useNumber" @change="updateTotalPrice" :min="1">
                        </el-input-number>
                      </template>
                    </el-table-column>
                    <el-table-column prop="miniUnitCodeName" :label="$t('finishRepair.unit')"
                      width="80"></el-table-column>
                    <el-table-column prop="specName" :label="$t('finishRepair.spec')" width="80"></el-table-column>
                    <el-table-column :label="$t('finishRepair.operation')" width="120">
                      <template slot-scope="scope">
                        <el-button type="danger" size="mini" @click="removeChoosedGoodsItem(scope.$index)">
                          <i class="el-icon-delete"></i>{{ $t('common.remove') }}
                        </el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-form-item>
              </div>

              <el-form-item :label="$t('finishRepair.totalAmount')" v-if="finishRepairInfo.maintenanceType == '1001'">
                <el-input v-model="finishRepairInfo.totalPrice" disabled></el-input>
              </el-form-item>

              <el-form-item :label="$t('finishRepair.paymentMethod')" v-if="finishRepairInfo.maintenanceType == '1001'">
                <el-select v-model="finishRepairInfo.payType" :placeholder="$t('finishRepair.selectPaymentMethod')"
                  style="width:100%">
                  <el-option v-for="(item, index) in finishRepairInfo.payTypes" :key="index" :label="item.name"
                    :value="item.statusCd">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('finishRepair.suggestion')">
                <el-input type="textarea" :rows="4" :placeholder="$t('finishRepair.inputSuggestion')"
                  v-model="finishRepairInfo.context">
                </el-input>
              </el-form-item>

              <el-form-item :label="$t('finishRepair.beforeRepairPhoto')">
                <upload-image-url ref="beforeRepairUpload" @notifyUploadCoverImage="handleBeforeRepairImage">
                </upload-image-url>
              </el-form-item>

              <el-form-item :label="$t('finishRepair.afterRepairPhoto')">
                <upload-image-url ref="afterRepairUpload" @notifyUploadCoverImage="handleAfterRepairImage">
                </upload-image-url>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="submitFinishRepair">{{ $t('common.submit') }}</el-button>
                <el-button @click="backToList">{{ $t('common.back') }}</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <choose-single-resource ref="chooseSingleResource" @choose-single-resource="chooseSingleResource"></choose-single-resource>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { repairFinish } from '@/api/work/finishRepairApi'
import {getUserName} from '@/api/user/userApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import ChooseSingleResource from '@/components/work/chooseSingleResource'

export default {
  name: 'FinishRepairList',
  components: {
    UploadImageUrl,
    ChooseSingleResource
  },
  data() {
    return {
      finishRepairInfo: {
        repairId: '',
        repairType: '',
        context: '',
        feeFlag: '200',
        repairObjType: '',
        publicArea: '',
        repairChannel: '',
        maintenanceTypes: [],
        maintenanceType: '',
        totalPrice: 0,
        choosedGoodsList: [],
        beforeRepairPhotos: [],
        afterRepairPhotos: [],
        payTypes: [],
        payType: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    chooseSingleResource(goods) {
      this.finishRepairInfo.choosedGoodsList.push(goods)
      this.updateTotalPrice()
    },
    async initData() {
      this.finishRepairInfo.repairType = this.$route.query.repairType
      this.finishRepairInfo.repairId = this.$route.query.repairId
      this.finishRepairInfo.repairObjType = this.$route.query.repairObjType
      this.finishRepairInfo.publicArea = this.$route.query.publicArea
      this.finishRepairInfo.repairChannel = this.$route.query.repairChannel

      try {
        const maintenanceTypes = await getDict('r_repair_pool', 'maintenance_type')
        this.finishRepairInfo.maintenanceTypes = maintenanceTypes

        const payTypes = await getDict('r_repair_pool', 'pay_type')
        this.finishRepairInfo.payTypes = payTypes
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    openChooseSingleResourceModel() {
      this.$refs.chooseSingleResource.open({
        maintenanceType: this.finishRepairInfo.maintenanceType
      })
    },
    handleBeforeRepairImage(photos) {
      this.finishRepairInfo.beforeRepairPhotos = photos.map(item => ({
        photo: item
      }))
    },
    handleAfterRepairImage(photos) {
      this.finishRepairInfo.afterRepairPhotos = photos.map(item => ({
        photo: item
      }))
    },
    removeChoosedGoodsItem(index) {
      this.finishRepairInfo.choosedGoodsList.splice(index, 1)
      this.updateTotalPrice()
    },
    updateTotalPrice() {
      let totalPrice = 0
      this.finishRepairInfo.choosedGoodsList.forEach(item => {
        totalPrice += item.price * item.useNumber
      })
      this.finishRepairInfo.totalPrice = totalPrice.toFixed(2)
    },
    validateForm() {
      if (!this.finishRepairInfo.repairId) {
        this.$message.error(this.$t('finishRepair.validate.repairIdRequired'))
        return false
      }
      if (!this.finishRepairInfo.maintenanceType) {
        this.$message.error(this.$t('finishRepair.validate.maintenanceTypeRequired'))
        return false
      }
      if (!this.finishRepairInfo.context) {
        this.$message.error(this.$t('finishRepair.validate.contextRequired'))
        return false
      }
      if (this.finishRepairInfo.beforeRepairPhotos.length === 0) {
        this.$message.error(this.$t('finishRepair.validate.beforePhotoRequired'))
        return false
      }
      if (this.finishRepairInfo.afterRepairPhotos.length === 0) {
        this.$message.error(this.$t('finishRepair.validate.afterPhotoRequired'))
        return false
      }
      if ((this.finishRepairInfo.maintenanceType === '1001' || this.finishRepairInfo.maintenanceType === '1003') &&
        this.finishRepairInfo.choosedGoodsList.length === 0) {
        this.$message.error(this.$t('finishRepair.validate.goodsRequired'))
        return false
      }
      if (this.finishRepairInfo.maintenanceType === '1001' && !this.finishRepairInfo.payType) {
        this.$message.error(this.$t('finishRepair.validate.paymentMethodRequired'))
        return false
      }
      return true
    },
    async submitFinishRepair() {
      if (!this.validateForm()) return

      try {
        const params = {
          ...this.finishRepairInfo,
          communityId: this.communityId,
          userName:getUserName()
        }
        const res = await repairFinish(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.backToList()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('finishRepair.submitError'))
      }
    },
    backToList() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.finish-repair-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.form-wrapper {
  padding: 20px;
}
</style>