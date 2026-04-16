<template>
  <div class="edit-allocation-storehouse-apply-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editAllocationStorehouseApply.title') }}</span>
        <div class="card-header-right">
          <el-button type="primary" size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('editAllocationStorehouseApply.back') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form label-width="120px">
            <el-form-item :label="$t('editAllocationStorehouseApply.applyRemark')">
              <el-input type="textarea" :placeholder="$t('editAllocationStorehouseApply.remarkPlaceholder')"
                v-model="formData.remark" :rows="3"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span>{{ $t('editAllocationStorehouseApply.purchaseItems') }}</span>
        <div class="card-header-right">
          <el-button type="primary" size="small" style="margin-right: 10px;" @click="openChooseResourceStoreDialog">
            <i class="el-icon-search"></i>
            {{ $t('editAllocationStorehouseApply.select') }}
          </el-button>
        </div>
      </div>

      <el-table :data="formData.resourceStores" border style="width: 100%" v-loading="loading">
        <el-table-column prop="resourceType" :label="$t('editAllocationStorehouseApply.resourceType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="resourceName" :label="$t('editAllocationStorehouseApply.resourceName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.resName }}({{ scope.row.resCode }})
          </template>
        </el-table-column>
        <el-table-column prop="specification" :label="$t('editAllocationStorehouseApply.specification')" align="center">
          <template slot-scope="scope">
            {{ scope.row.rssName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="isFixed" :label="$t('editAllocationStorehouseApply.isFixed')" align="center">
          <template slot-scope="scope">
            {{ scope.row.isFixedName }}
          </template>
        </el-table-column>
        <el-table-column prop="sourceWarehouse" :label="$t('editAllocationStorehouseApply.sourceWarehouse')"
          align="center">
          <template slot-scope="scope">
            {{ scope.row.shaName }}
          </template>
        </el-table-column>
        <el-table-column prop="referencePrice" :label="$t('editAllocationStorehouseApply.referencePrice')"
          align="center">
          <template slot-scope="scope">
            <el-select v-model="scope.row.timesId" @change="handleTimesChange($event, scope.$index)"
              style="width: 100%">
              <el-option :label="$t('editAllocationStorehouseApply.selectPrice')" value=""></el-option>
              <el-option v-for="time in scope.row.times" :key="time.timesId" :label="time.price"
                :value="time.timesId"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="stock" :label="$t('editAllocationStorehouseApply.stock')" align="center">
          <template slot-scope="scope">
            {{ getTimesStock(scope.row) }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column prop="targetWarehouse" :label="$t('editAllocationStorehouseApply.targetWarehouse')"
          align="center">
          <template slot-scope="scope">
            <el-select v-model="scope.row.shzId" style="width: 100%">
              <el-option :label="$t('editAllocationStorehouseApply.selectTargetWarehouse')" value=""></el-option>
              <template v-for="(item, index) in storehouses">
                <el-option :key="index" :label="item.shName" :value="item.shId"
                  v-if="item.shId != scope.row.shId"></el-option>
              </template>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="allocationQuantity" :label="$t('editAllocationStorehouseApply.allocationQuantity')"
          align="center">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.curStock" :min="0"
              :placeholder="$t('editAllocationStorehouseApply.allocationQuantityPlaceholder')"
              style="width: 120px"></el-input-number>
            {{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('editAllocationStorehouseApply.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="handleDeleteResourceStore(scope.row)">
              {{ $t('editAllocationStorehouseApply.cancelAllocation') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24" style="text-align: right;">
        <el-button type="primary" @click="handleSubmit">
          {{ $t('editAllocationStorehouseApply.submit') }}
        </el-button>
      </el-col>
    </el-row>

    <choose-resource-store ref="chooseResourceStoreDialog"
      @chooseResourceStore="handleChooseResourceStore"></choose-resource-store>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  listAllocationStorehouseApply,
  listAllocationStorehouses,
  listStorehouses,
  updateAllocationStorehouse
} from '@/api/resource/editAllocationStorehouseApplyApi'
import ChooseResourceStore from '@/components/resource/chooseResourceStore'

export default {
  name: 'EditAllocationStorehouseApplyList',
  components: {
    ChooseResourceStore
  },
  data() {
    return {
      loading: false,
      communityId: '',
      formData: {
        resourceStores: [],
        remark: '',
        applyId: '',
        shId: ''
      },
      storehouses: []
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.formData.applyId = this.$route.query.applyId
    this.initData()
  },
  methods: {
    async initData() {
      try {
        this.loading = true
        await Promise.all([
          this.getAllocationStorehouseApply(),
          this.getAllocationStorehouses(),
          this.getStorehouses()
        ])
      } catch (error) {
        console.error('初始化数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async getAllocationStorehouseApply() {
      try {
        const params = {
          page: 1,
          row: 1,
          applyId: this.formData.applyId
        }
        const { data } = await listAllocationStorehouseApply(params)
        const applyData = data[0]
        Object.assign(this.formData, applyData)
      } catch (error) {
        console.error('获取调拨申请失败:', error)
      }
    },
    async getAllocationStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          applyId: this.formData.applyId
        }
        const { data } = await listAllocationStorehouses(params)
        data.forEach(item => {
          item.shzId = item.shIdz
          item.shId = item.shIda
          this.formData.shId = item.shIda
          item.curStock = item.stock
        })
        this.formData.resourceStores = data
      } catch (error) {
        console.error('获取调拨物品失败:', error)
      }
    },
    async getStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listStorehouses(params)
        this.storehouses = data
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    handleTimesChange(value, index) {
      const times = this.formData.resourceStores[index].times
      times.forEach(item => {
        if (item.timesId === value) {
          this.formData.resourceStores[index].selectedStock = item.stock
        }
      })
    },
    getTimesStock(resourceStore) {
      if (!resourceStore.timesId || !resourceStore.times) {
        return "-"
      }
      let stock = 0
      resourceStore.times.forEach(item => {
        if (item.timesId === resourceStore.timesId) {
          stock = item.stock
        }
      })
      return stock
    },
    openChooseResourceStoreDialog() {
      this.$refs.chooseResourceStoreDialog.open({
        resOrderType: '20000',
        shId: this.formData.shId
      })
    },
    handleChooseResourceStore(resourceStores) {
      const oldList = this.formData.resourceStores
      // 过滤重复选择的商品
      const newItems = resourceStores.filter(newItem => {
        return !oldList.some(oldItem => oldItem.resId === newItem.resId)
      })
      // 设置新选择的商品默认值
      newItems.forEach(item => {
        item.shaName = item.shName
        item.shzId = ''
        item.timesId = ''
        item.curStock = '0'
      })
      // 合并已有商品和新添加商品
      this.formData.resourceStores = [...oldList, ...newItems]
    },
    handleDeleteResourceStore(resourceStore) {
      this.formData.resourceStores = this.formData.resourceStores.filter(
        item => item.resId !== resourceStore.resId
      )
      this.$refs.chooseResourceStoreDialog.removeSelectItem(resourceStore.resId)
    },
    async handleSubmit() {
      if (!this.formData.resourceStores || this.formData.resourceStores.length === 0) {
        this.$message.warning(this.$t('editAllocationStorehouseApply.noPurchaseItems'))
        return
      }

      try {
        this.loading = true
        const response = await updateAllocationStorehouse(this.formData)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('提交调拨申请失败:', error)
        this.$message.error(this.$t('editAllocationStorehouseApply.updateFailed'))
      } finally {
        this.loading = false
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-allocation-storehouse-apply-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .el-table {
    margin-top: 20px;
  }
}
</style>