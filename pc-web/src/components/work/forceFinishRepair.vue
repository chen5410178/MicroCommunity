<template>
  <el-dialog :title="$t('forceFinishRepair.title')" :visible.sync="visible" width="40%" @close="closeDialog">
    <el-form label-width="120px">
      <!-- <el-form-item v-if="form.repairObjType !== '004'" :label="$t('forceFinishRepair.useMaterial')">
        <el-select v-model="form.maintenanceType" placeholder="请选择" style="width:100%">
          <template v-for="item in maintenanceTypes">
            <el-option :key="item.statusCd" :label="item.name" :value="item.statusCd"
              v-if="item.statusCd !== '1001' && item.statusCd !== '1002'" />
          </template>
        </el-select>
      </el-form-item>

      <el-form-item v-else :label="$t('forceFinishRepair.maintenanceType')">
        <el-select v-model="form.maintenanceType" placeholder="请选择" style="width:100%">
          <template v-for="item in maintenanceTypes">
            <el-option :key="item.statusCd" :label="item.name" :value="item.statusCd"
              v-if="item.statusCd !== '1003' && item.statusCd !== '1004'" />
          </template>
        </el-select>
      </el-form-item> -->

      <template v-if="form.maintenanceType === '1001' || form.maintenanceType === '1003'">
        <el-form-item :label="$t('forceFinishRepair.goodsType')">
          <el-select v-model="form.rsId" placeholder="请选择" style="width:100%" @change="listSonResourceStoreType">
            <el-option v-for="item in form.resourceStoreTypes" :key="item.rstId" :label="item.name" :value="item.rstId" />
          </el-select>
        </el-form-item>

        <template v-if="form.isCustom">
          <el-form-item :label="$t('forceFinishRepair.goodsName')">
            <el-input v-model="form.customGoodsName" :placeholder="$t('forceFinishRepair.goodsName')" />
          </el-form-item>

          <el-form-item v-if="form.maintenanceType === '1001'" :label="$t('forceFinishRepair.customPrice')">
            <el-input v-model="form.price" placeholder="必填，请填写自定义价格" />
          </el-form-item>
        </template>

        <template v-else>
          <el-form-item :label="$t('forceFinishRepair.secondaryCategory')">
            <el-select v-model="form.rstId" placeholder="请选择" style="width:100%" @change="choseGoods">
              <el-option v-for="item in form.sonResourceStoreTypes" :key="item.rstId" :label="item.name"
                :value="item.rstId" />
            </el-select>
          </el-form-item>

          <el-form-item v-if="form.rstId" :label="$t('forceFinishRepair.goods')">
            <el-select v-model="form.resId" placeholder="请选择" style="width:100%" @change="chosePrice">
              <el-option v-for="item in form.resourceStores" :key="item.resId" :label="item.resName" :value="item.resId" />
            </el-select>
          </el-form-item>
        </template>

        <template v-if="form.resId || form.isCustom">
          <el-form-item :label="$t('forceFinishRepair.quantity')">
            <el-input-number v-model="form.useNumber" :min="1" label="数量" />
          </el-form-item>

          <el-form-item v-if="form.maintenanceType === '1001'" :label="$t('forceFinishRepair.paymentMethod')">
            <el-select v-model="form.payType" placeholder="请选择" style="width:100%">
              <el-option v-for="item in payTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </template>
      </template>

      <el-form-item :label="$t('forceFinishRepair.explanation')">
        <el-input v-model="form.context" type="textarea" :placeholder="$t('forceFinishRepair.explanation')" :rows="4" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('forceFinishRepair.cancel') }}</el-button>
      <el-button type="primary" @click="submitForceFinish">{{ $t('forceFinishRepair.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  listResourceStoreTypes,
  listUserStorehouses,
  repairForceFinish
} from '@/api/work/repairForceFinishManageApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'ForceFinishRepair',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      form: {
        repairId: '',
        repairType: '',
        repairObjType: '',
        context: '',
        maintenanceType: '1002',
        resId: '',
        rsId: '',
        isCustom: false,
        customGoodsName: '',
        price: '',
        useNumber: 1,
        rstId: '',
        payType: '',
        selectedGoodsInfo: {},
        payTypes: [],
        resourceStores: [],
        sonResourceStoreTypes: [],
        resourceStoreTypes: [],
        maintenanceTypes: []
      },
      payTypes: [],
      maintenanceTypes: []
    }
  },
  mounted() {
    this.loadDictData()
  },
  methods: {
    open(repair) {
      this.form = {
        ...this.form,
        repairId: repair.repairId,
        repairType: repair.repairType,
        repairObjType: repair.repairObjType
      }
      this.listResourceStoreType()
    },
    closeDialog() {
      this.$emit('update:visible', false)
    },
    async loadDictData() {
      try {
        this.maintenanceTypes = await getDict('r_repair_pool', 'maintenance_type')
        this.payTypes = await getDict('r_repair_pool', 'pay_type')
      } catch (error) {
        console.error('获取字典数据失败:', error)
        this.$message.error('获取字典数据失败')
      }
    },
    async listResourceStoreType() {
      try {
        const data = await listResourceStoreTypes('0')
        this.form.resourceStoreTypes = [...data, { rstId: 'custom', name: '自定义' }]
      } catch (error) {
        console.error('获取资源类型失败:', error)
        this.$message.error('获取资源类型失败')
      }
    },
    async listSonResourceStoreType() {
      if (this.form.rsId === 'custom') {
        this.form.isCustom = true
        return
      }

      this.form.isCustom = false
      try {
        const data = await listResourceStoreTypes(this.form.rsId)
        this.form.sonResourceStoreTypes = data
      } catch (error) {
        console.error('获取子资源类型失败:', error)
        this.$message.error('获取子资源类型失败')
      }
    },
    async choseGoods() {
      if (!this.form.rstId) return

      try {
        const params = {
          repairId: this.form.repairId,
          rstId: this.form.rstId,
          page:1,
          row:50
        }
        const data = await listUserStorehouses(params)
        this.form.resourceStores = data
      } catch (error) {
        console.error('获取商品失败:', error)
        this.$message.error(error)
      }
    },
    chosePrice() {
      const selected = this.form.resourceStores.find(item => item.resId === this.form.resId)
      if (selected) {
        this.form.selectedGoodsInfo = selected
        if (selected.outLowPrice === selected.outHighPrice) {
          this.form.price = selected.outLowPrice
        } else {
          this.form.price = ''
        }
      }
    },
    async submitForceFinish() {
      if (this.validateForm()) {
        try {
          await repairForceFinish(this.form)
          this.$message.success('强制回单成功')
          this.closeDialog()
          this.$emit('success')
        } catch (error) {
          console.error('强制回单失败:', error)
          this.$message.error('强制回单失败')
        }
      }
    },
    validateForm() {
      if (!this.form.maintenanceType) {
        this.$message.warning('请选择维修类型')
        return false
      }

      if (this.form.maintenanceType === '1001' || this.form.maintenanceType === '1003') {
        if (!this.form.rsId) {
          this.$message.warning('请选择商品类型')
          return false
        }

        if (this.form.isCustom) {
          if (!this.form.customGoodsName) {
            this.$message.warning('请填写商品名')
            return false
          }
          if (this.form.maintenanceType === '1001' && !this.form.price) {
            this.$message.warning('请填写价格')
            return false
          }
        } else {
          if (!this.form.rstId) {
            this.$message.warning('请选择二级分类')
            return false
          }
          if (!this.form.resId) {
            this.$message.warning('请选择商品')
            return false
          }
        }

        if (this.form.maintenanceType === '1001' && !this.form.payType) {
          this.$message.warning('请选择支付方式')
          return false
        }
      }

      if (!this.form.context) {
        this.$message.warning('请填写说明')
        return false
      }

      return true
    }
  }
}
</script>