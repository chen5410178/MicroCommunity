<template>
  <el-dialog :title="$t('allocationStorehouse.title')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('allocationStorehouse.sourceWarehouse')" prop="shIda">
        <el-select v-model="form.shIda" :placeholder="$t('allocationStorehouse.requiredSourceWarehouse')"
          style="width:100%" @change="handleSourceWarehouseChange">
          <template v-for="item in storehouses">
            <el-option :key="item.shId" :label="item.shName" :value="item.shId" v-if="item.shId !== form.shIdz" />
          </template>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('allocationStorehouse.itemName')" prop="resName">
        <el-input v-model="form.resName" :placeholder="$t('allocationStorehouse.requiredItemName')" />
      </el-form-item>

      <el-form-item :label="$t('allocationStorehouse.stock')">
        <el-input v-model="form.curStock" disabled :placeholder="$t('allocationStorehouse.requiredStock')" />
      </el-form-item>

      <el-form-item :label="$t('allocationStorehouse.targetWarehouse')" prop="shIdz">
        <el-select v-model="form.shIdz" :placeholder="$t('allocationStorehouse.requiredTargetWarehouse')"
          style="width:100%" @change="handleTargetWarehouseChange">
          <template v-for="item in storehouses">
            <el-option :key="item.shId" :label="item.shName" :value="item.shId" v-if="item.shId !== form.shIda" />
          </template>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('allocationStorehouse.allocationCount')" prop="stock">
        <el-input v-model="form.stock" type="number" :placeholder="$t('allocationStorehouse.requiredAllocationCount')" />
      </el-form-item>

      <el-form-item :label="$t('allocationStorehouse.remark')" prop="remark">
        <el-input type="textarea" :rows="3" v-model="form.remark"
          :placeholder="$t('allocationStorehouse.requiredRemark')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { listStorehouses } from '@/api/resource/allocationStorehouseManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AllocationStorehouse',
  data() {
    return {
      visible: false,
      storehouses: [],
      form: {
        resId: '',
        resName: '',
        resCode: '',
        remark: '',
        stock: 0,
        curStock: 0,
        shIdz: '',
        shIda: '',
        shName: '',
        communityId: getCommunityId()
      },
      rules: {
        shIda: [
          { required: true, message: this.$t('allocationStorehouse.requiredSourceWarehouse'), trigger: 'blur' }
        ],
        resName: [
          { required: true, message: this.$t('allocationStorehouse.requiredItemName'), trigger: 'blur' }
        ],
        shIdz: [
          { required: true, message: this.$t('allocationStorehouse.requiredTargetWarehouse'), trigger: 'blur' }
        ],
        stock: [
          { required: true, message: this.$t('allocationStorehouse.requiredAllocationCount'), trigger: 'blur' },
          { type: 'number', min: 1, message: this.$t('allocationStorehouse.positiveNumber'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('allocationStorehouse.requiredRemark'), trigger: 'blur' },
          { max: 512, message: this.$t('allocationStorehouse.remarkTooLong'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.getStorehouses()
    },
    async getStorehouses() {
      try {
        const res = await listStorehouses({
          page: 1,
          row: 100,
          communityId: this.form.communityId
        })
        this.storehouses = res.data
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    handleSourceWarehouseChange(val) {
      const warehouse = this.storehouses.find(item => item.shId === val)
      if (warehouse) {
        this.form.shName = warehouse.shName
      }
    },
    handleTargetWarehouseChange(val) {
      const warehouse = this.storehouses.find(item => item.shId === val)
      if (warehouse) {
        this.form.shName = warehouse.shName
      }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$emit('submit', this.form)
          this.visible = false
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        resId: '',
        resName: '',
        resCode: '',
        remark: '',
        stock: 0,
        curStock: 0,
        shIdz: '',
        shIda: '',
        shName: '',
        communityId: getCommunityId()
      }
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>