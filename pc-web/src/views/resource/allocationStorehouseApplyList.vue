<template>
  <div class="allocation-storehouse-apply-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationStorehouseApply.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="goBack">
          <i class="el-icon-close"></i>
          {{ $t('allocationStorehouseApply.back') }}
        </el-button>
      </div>

      <el-form label-width="120px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('allocationStorehouseApply.warehouse')">
              <el-select v-model="form.shId" :placeholder="$t('allocationStorehouseApply.warehousePlaceholder')"
                @change="computeFlow" style="width:100%">
                <el-option v-for="item in storehouses" :key="item.shId" :label="item.shName" :value="item.shId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item :label="$t('allocationStorehouseApply.remark')">
              <el-input type="textarea" v-model="form.remark"
                :placeholder="$t('allocationStorehouseApply.remarkPlaceholder')" :rows="3"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationStorehouseApply.itemsTitle') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="openChooseResourceDialog">
          <i class="el-icon-search"></i>
          {{ $t('allocationStorehouseApply.selectItems') }}
        </el-button>
      </div>

      <table class="custom-table">
        <thead>
          <tr>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.itemType') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.itemName') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.itemSpec') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.fixedItem') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.sourceWarehouse') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.referencePrice') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.stock') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.targetWarehouse') }}
            </th>
            <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
              {{ $t('allocationStorehouseApply.transferQuantity') }}
            </th>
            <th
              style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa; width: 150px;">
              {{ $t('allocationStorehouseApply.operation') }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in resourceStores" :key="index" style="border-bottom: 1px solid #ebeef5;">
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              {{ item.parentRstName || '-' }} > {{ item.rstName || '-' }}
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              {{ item.resName }}({{ item.resCode }})
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              {{ item.rssName || '-' }}
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              {{ item.isFixedName }}
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              {{ item.shaName }}
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              <select v-model="item.timesId" @change="changeTimesId($event, index)" class="custom-select">
                <option value="">{{ $t('allocationStorehouseApply.selectPrice') }}</option>
                <option v-for="time in item.times" :key="time.timesId" :value="time.timesId">
                  {{ time.price }}
                </option>
              </select>
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              {{ getTimesStock(item) }}{{ item.unitCodeName }}
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              <select v-model="item.shzId" class="custom-select">
                <option value="">{{ $t('allocationStorehouseApply.selectTargetWarehouse') }}</option>
                <template v-for="storehouse in storehouses">
                  <option :key="storehouse.shId" :value="storehouse.shId"
                    v-if="storehouse.shId != item.shId && storehouse.communityId == communityId">
                    {{ storehouse.shName }}
                  </option>
                </template>
              </select>
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              <el-input-number v-model="item.curStock" :min="0" :precision="0"
                controls-position="right"></el-input-number>
              {{ item.unitCodeName }}
            </td>
            <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
              <el-button type="danger" size="mini" @click="removeItem(item)">
                {{ $t('allocationStorehouseApply.cancelTransfer') }}
              </el-button>
            </td>
          </tr>
          <tr v-if="resourceStores.length === 0">
            <td colspan="10" style="text-align: center; padding: 20px; color: #909399;">
              {{ $t('common.noData') }}
            </td>
          </tr>
        </tbody>
      </table>
    </el-card>

    <el-card class="box-card margin-top" v-if="form.audit.assignee === '-2'">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationStorehouseApply.approverTitle') }}</span>
      </div>

      <el-form label-width="120px">
        <el-row :gutter="20">
          <el-col :span="18">
            <el-form-item :label="$t('allocationStorehouseApply.approver')">
              <el-input v-model="form.audit.staffName" :placeholder="$t('allocationStorehouseApply.selectApprover')"
                disabled></el-input>

            </el-form-item>
          </el-col>
          <el-col :span="6" class="text-left">
            <el-button type="primary"  @click="chooseStaff">
              <i class="el-icon-search"></i>
              {{ $t('allocationStorehouseApply.select') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="submit-wrapper">
      <el-button type="primary" @click="submitApply">
        {{ $t('allocationStorehouseApply.submit') }}
      </el-button>
    </div>

    <choose-resource-store ref="chooseResourceStore" @choose="handleChooseResource" />

    <select-staff ref="selectStaff" @selectStaff="handleSelectStaff" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStore from '@/components/resource/chooseResourceStore'
import SelectStaff from '@/components/staff/SelectStaff'
import {
  listStorehouses,
  saveAllocationStorehouse,
  queryFirstAuditStaff
} from '@/api/resource/allocationStorehouseApplyApi'

export default {
  name: 'AllocationStorehouseApplyList',
  components: {
    ChooseResourceStore,
    SelectStaff
  },
  data() {
    return {
      communityId: '',
      form: {
        resourceStores: [],
        storehouses: [],
        remark: '',
        communityId: '',
        shId: '',
        flowId: '',
        apply_type: 10000,
        audit: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: ''
        }
      },
      storehouses: [],
      resourceStores: []
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.form.communityId = this.communityId
    this.loadStorehouses()
  },
  methods: {
    async loadStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          flag: "1",
          communityId: this.communityId
        }
        const res = await listStorehouses(params)
        this.storehouses = res.data
        this.form.storehouses = res.data
      } catch (error) {
        console.error(error)
      }
    },
    openChooseResourceDialog() {
      if (!this.form.shId) {
        this.$message.error(this.$t('allocationStorehouseApply.selectWarehouseFirst'))
        return
      }
      if (!this.form.remark) {
        this.$message.error(this.$t('allocationStorehouseApply.fillRemarkFirst'))
        return
      }
      this.$refs.chooseResourceStore.open({
        resOrderType: '20000',
        shId: this.form.shId,
        isAllocation: true
      })
    },
    handleChooseResource(resources) {
      const oldList = this.resourceStores
      // 过滤重复选择的商品
      const newResources = resources.filter(newItem => {
        return !oldList.some(oldItem => oldItem.resId === newItem.resId)
      })

      // 设置默认值
      newResources.forEach(item => {
        item.shaName = item.shName
        item.shzId = ''
        item.curStock = 0
        if (item.times && item.times.length > 0) {
          item.timesId = item.times[0].timesId
        }
      })

      this.resourceStores = [...newResources, ...oldList]
      this.form.resourceStores = this.resourceStores
    },
    removeItem(item) {
      this.resourceStores = this.resourceStores.filter(i => i.resId !== item.resId)
      this.form.resourceStores = this.resourceStores
      this.$refs.chooseResourceStore.removeSelectedItem(item.resId)
    },
    changeTimesId(value, index) {
      const times = this.resourceStores[index].times
      times.forEach(item => {
        if (item.timesId === value) {
          this.resourceStores[index].selectedStock = item.stock
        }
      })
      this.$nextTick(() => {
        this.$forceUpdate()
      })
    },
    getTimesStock(item) {
      if (!item.timesId) return "-"
      let stock = 0
      item.times.forEach(time => {
        if (time.timesId === item.timesId) {
          stock = time.stock
        }
      })
      return stock
    },
    computeFlow() {
      // 清空已选择的商品
      this.resourceStores = []
      this.form.resourceStores = []

      const storehouse = this.storehouses.find(item => item.shId === this.form.shId)
      if (!storehouse) return

      this.form.flowId = storehouse.allocationFlowId
      if (!this.form.flowId) return

      this.loadStaffOrg(this.form.flowId)
    },
    async loadStaffOrg(flowId) {
      try {
        const params = {
          communityId: this.communityId,
          flowId: flowId
        }
        const res = await queryFirstAuditStaff(params)
        if (res.code !== 0) return

        const data = res.data[0]
        Object.assign(this.form.audit, data)
        if (!data.assignee.startsWith('-')) {
          this.form.audit.staffId = data.assignee
        }
      } catch (error) {
        console.error(error)
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.form.audit)
    },
    handleSelectStaff(staff) {
      this.form.audit.staffId = staff.staffId
      this.form.audit.staffName = staff.staffName
    },
    async submitApply() {
      // 验证数据
      if (!this.form.remark) {
        this.$message.error(this.$t('allocationStorehouseApply.remarkRequired'))
        return
      }

      if (this.resourceStores.length === 0) {
        this.$message.error(this.$t('allocationStorehouseApply.selectItemsFirst'))
        return
      }

      if (!this.resourcesFromSameHouse(this.resourceStores)) {
        this.$message.error(this.$t('allocationStorehouseApply.sameWarehouseRequired'))
        return
      }

      let isValid = true
      this.resourceStores.forEach(item => {
        if (!item.timesId) {
          this.$message.error(`${item.resName}${this.$t('allocationStorehouseApply.selectPriceFirst')}`)
          isValid = false
          return
        }

        item.curStock = parseFloat(item.curStock)
        if (item.curStock > parseFloat(item.selectedStock)) {
          this.$message.error(`${item.resName}${this.$t('allocationStorehouseApply.stockNotEnough')}`)
          isValid = false
          return
        }

        if (!item.shzId) {
          this.$message.error(this.$t('allocationStorehouseApply.selectTargetWarehouseFirst'))
          isValid = false
          return
        }

        if (item.curStock <= 0) {
          this.$message.error(this.$t('allocationStorehouseApply.fillQuantityFirst'))
          isValid = false
          return
        }
      })

      if (!isValid) return

      try {
        const res = await saveAllocationStorehouse(this.form)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error(error)
        this.$message.error(this.$t('allocationStorehouseApply.submitFailed'))
      }
    },
    resourcesFromSameHouse(resources) {
      if (!resources || resources.length < 2) return true

      const firstShId = resources[0].shId
      return resources.every(item => item.shId === firstShId)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-storehouse-apply-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .float-right {
    float: right;
  }

  .submit-wrapper {
    text-align: right;
    margin-top: 20px;
  }

  .custom-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #ebeef5;
  }

  .custom-table th {
    background-color: #fafafa;
    color: #606266;
    font-weight: 500;
    font-size: 14px;
  }

  .custom-table td {
    color: #606266;
    font-size: 14px;
  }

  .custom-table tr:hover {
    background-color: #f5f7fa;
  }

  .custom-select {
    width: 100%;
    height: 32px;
    line-height: 32px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 0 12px;
    font-size: 14px;
    color: #606266;
    background-color: #fff;
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  }

  .custom-select:focus {
    outline: none;
    border-color: #409eff;
  }

  .custom-select:hover {
    border-color: #c0c4cc;
  }
}
</style>