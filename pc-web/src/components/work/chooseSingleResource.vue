<template>
  <el-dialog :title="$t('chooseSingleResource.title')" :visible.sync="dialogVisible" width="60%" @close="handleClose">

    <el-form :model="form" label-width="120px">
      <el-form-item :label="$t('chooseSingleResource.goodsType')">
        <el-select v-model="form.rsId" :placeholder="$t('chooseSingleResource.selectGoodsType')" style="width:100%"
          @change="handleRsIdChange">
          <el-option v-for="item in resourceStoreTypes" :key="item.rstId" :label="item.name" :value="item.rstId">
          </el-option>
        </el-select>
      </el-form-item>

      <template v-if="form.isCustom">
        <el-form-item :label="$t('chooseSingleResource.goodsName')">
          <el-input v-model="form.customGoodsName" :placeholder="$t('chooseSingleResource.inputGoodsName')">
          </el-input>
        </el-form-item>

        <el-form-item :label="$t('chooseSingleResource.customPrice')" v-if="form.maintenanceType === '1001'">
          <el-input-number v-model="form.price" :placeholder="$t('chooseSingleResource.inputPrice')" :min="0"
            style="width:100%">
          </el-input-number>
        </el-form-item>
      </template>

      <template v-else>
        <el-form-item :label="$t('chooseSingleResource.subCategory')">
          <el-select v-model="form.rstId" :placeholder="$t('chooseSingleResource.selectSubCategory')" style="width:100%"
            @change="handleRstIdChange">
            <el-option v-for="item in sonResourceStoreTypes" :key="item.rstId" :label="item.name" :value="item.rstId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('chooseSingleResource.goods')" v-if="form.rstId">
          <el-select v-model="form.resId" :placeholder="$t('chooseSingleResource.selectGoods')" style="width:100%"
            @change="handleResIdChange">
            <el-option v-for="item in resourceStores" :key="item.resId" :label="item.resName" :value="item.resId">
            </el-option>
          </el-select>
        </el-form-item>

        <template v-if="form.resId">
          <el-form-item :label="$t('chooseSingleResource.goodsPrice')"
            v-if="form.maintenanceType === '1001' && form.outLowPrice === form.outHighPrice">
            <el-input-number v-model="form.price" disabled style="width:100%">
            </el-input-number>
          </el-form-item>

          <el-form-item :label="$t('chooseSingleResource.customPrice')"
            v-if="form.maintenanceType === '1001' && form.outLowPrice !== form.outHighPrice">
            <el-input-number v-model="form.price" :min="form.outLowPrice" :max="form.outHighPrice" style="width:100%">
            </el-input-number>
            <span>{{ $t('chooseSingleResource.priceRange') }} {{ form.outLowPrice }}-{{ form.outHighPrice }}</span>
          </el-form-item>

          <el-form-item :label="$t('chooseSingleResource.spec')">
            <el-input v-model="form.selectedGoodsInfo.specName" disabled>
            </el-input>
          </el-form-item>
        </template>
      </template>

      <el-form-item :label="$t('chooseSingleResource.quantity')">
        <el-input-number v-model="form.useNumber" :min="1" @change="handleQuantityChange">
        </el-input-number>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
//import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listResourceStoreTypes, listUserStorehouses } from '@/api/work/finishRepairApi'

export default {
  name: 'ChooseSingleResource',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      form: {
        maintenanceType: '',
        resourceStoreTypes: [],
        sonResourceStoreTypes: [],
        rsId: '',
        rstId: '',
        resId: '',
        repairId: '',
        selectedGoodsInfo: {},
        resourceStores: [],
        isCustom: false,
        customGoodsName: '',
        price: '',
        outLowPrice: '',
        outHighPrice: '',
        useNumber: 1,
        sign: ''
      },
      resourceStoreTypes: [],
      sonResourceStoreTypes: [],
      resourceStores: [],
      communityId: ''
    }
  },
  methods: {
    open(params) {
      this.resetForm()
      this.communityId = getCommunityId()
      this.form.maintenanceType = params.maintenanceType
      if (params.sign) {
        this.form.sign = params.sign
      }
      if (params.repairId) {
        this.form.repairId = params.repairId
      }
      this.dialogVisible = true
      this.loadResourceStoreTypes()
    },
    async loadResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: '0',
          giveType: 1
        }
        const { data } = await listResourceStoreTypes(params)
        this.resourceStoreTypes = data
        // 添加自定义选项
        this.resourceStoreTypes.push({
          rstId: 'custom',
          name: this.$t('chooseSingleResource.custom')
        })
      } catch (error) {
        console.error('加载商品类型失败:', error)
      }
    },
    async handleRsIdChange(val) {
      this.form.rstId = ''
      this.sonResourceStoreTypes = []
      this.form.resId = ''
      this.resourceStores = []

      if (val === 'custom') {
        this.form.isCustom = true
        return
      }

      this.form.isCustom = false
      try {
        const params = {
          page: 1,
          row: 100,
          parentId: val,
          communityId: this.communityId
        }
        const { data } = await listResourceStoreTypes(params)
        this.sonResourceStoreTypes = data
      } catch (error) {
        console.error('加载二级分类失败:', error)
      }
    },
    async handleRstIdChange(val) {
      this.form.resId = ''
      this.resourceStores = []

      try {
        const params = {
          page: 1,
          row: 100,
          repairId: this.form.repairId,
          rstId: val,
          resId: this.form.resId,
          communityId: this.communityId,
          chooseType: "repair",
          flag: 1,
          sign: this.form.sign,
          giveType: 1
        }
        const { data } = await listUserStorehouses(params)
        this.resourceStores = data
      } catch (error) {
        console.error('加载商品失败:', error)
      }
    },
    handleResIdChange(val) {
      this.resourceStores.forEach(item => {
        if (item.resId === val) {
          this.form.selectedGoodsInfo = item
          if (item.outLowPrice === item.outHighPrice) {
            this.form.price = item.outLowPrice
          } else {
            this.form.price = ''
          }
          this.form.outLowPrice = item.outLowPrice
          this.form.outHighPrice = item.outHighPrice
        }
      })
    },
    handleQuantityChange(val) {
      if (val < 1) {
        this.form.useNumber = 1
      }
    },
    handleClose() {
      this.resetForm()
    },
    resetForm() {
      this.form = {
        maintenanceType: '',
        resourceStoreTypes: [],
        sonResourceStoreTypes: [],
        rsId: '',
        rstId: '',
        resId: '',
        repairId: '',
        selectedGoodsInfo: {},
        resourceStores: [],
        isCustom: false,
        customGoodsName: '',
        price: '',
        outLowPrice: '',
        outHighPrice: '',
        useNumber: 1,
        sign: ''
      }
    },
    validateForm() {
      if (this.form.isCustom) {
        if (!this.form.customGoodsName) {
          this.$message.error(this.$t('chooseSingleResource.validate.goodsNameRequired'))
          return false
        }
        if (this.form.maintenanceType === '1001' && !this.form.price) {
          this.$message.error(this.$t('chooseSingleResource.validate.priceRequired'))
          return false
        }
      } else {
        if (!this.form.rsId) {
          this.$message.error(this.$t('chooseSingleResource.validate.goodsTypeRequired'))
          return false
        }
        if (!this.form.rstId) {
          this.$message.error(this.$t('chooseSingleResource.validate.subCategoryRequired'))
          return false
        }
        if (!this.form.resId) {
          this.$message.error(this.$t('chooseSingleResource.validate.goodsRequired'))
          return false
        }
        if (this.form.maintenanceType === '1001' && !this.form.price) {
          this.$message.error(this.$t('chooseSingleResource.validate.priceRequired'))
          return false
        }
      }
      return true
    },
    handleConfirm() {
      if (!this.validateForm()) return

      const selectedGoods = {
        ...this.form.selectedGoodsInfo,
        price: this.form.price,
        useNumber: this.form.useNumber,
        isCustom: this.form.isCustom,
        customGoodsName: this.form.customGoodsName,
        resName:this.form.customGoodsName
      }

      if (this.form.isCustom) {
        selectedGoods.rstName = this.$t('chooseSingleResource.custom')
      }

      this.$emit('choose-single-resource', selectedGoods)
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.el-input-number {
  width: 100%;
}
</style>