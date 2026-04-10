<template>
  <div class="animated fadeInRight ecommerce padding">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('returnStorehouseApplyManage.title') }}</span>
            <div class="ibox-tools" style="top:10px;">
              <el-button type="primary" size="small" @click="_goBack()">
                <i class="el-icon-close"></i>
                <span>{{ $t('returnStorehouseApplyManage.back') }}</span>
              </el-button>
              <el-button type="primary" size="small" @click="_openReturnStorehouseModel()">
                <i class="el-icon-plus"></i>
                <span>{{ $t('returnStorehouseApplyManage.selectItems') }}</span>
              </el-button>
            </div>
          </div>
          <div class="">
            <table class="custom-table">
              <thead>
                <tr>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.itemId') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.itemType') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.itemName') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.itemSpec') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.stock') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.miniStock') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.targetWarehouse') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.returnQuantity') }}
                  </th>
                  <th style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('returnStorehouseApplyManage.operation') }}
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in returnStorehouseManageInfo.resourceStores" :key="index" 
                    style="border-bottom: 1px solid #ebeef5;">
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ item.resId }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ item.parentRstName ? item.parentRstName : '-' }} >
                    {{ item.rstName ? item.rstName : '-' }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ item.resName }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ item.rssName ? item.rssName : '-' }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ item.stock }}{{ item.unitCodeName }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ item.miniStock }}{{ item.miniUnitCodeName }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    <select v-model="item.shzId" class="custom-select">
                      <option value="">{{ $t('returnStorehouseApplyManage.requiredSelect') }}</option>
                      <template v-for="(storehouse, storeIndex) in returnStorehouseManageInfo.storehouses" >
                      <option :key="storeIndex" :value="storehouse.shId" v-if="storehouse.shId != item.shId">
                        {{ storehouse.shName }}
                      </option>
                      </template>
                    </select>
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    <el-input-number v-model="item.curStock" :min="0" :max="item.miniStock"
                      style="width:70%;margin-right:10px" />
                    {{ item.miniUnitCodeName }}
                    <el-button type="info" @click="_itemReturnAll(item)">
                      {{ $t('returnStorehouseApplyManage.returnAll') }}
                    </el-button>
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    <el-button type="text" @click="_openDeleteResourceStoreModel(item)">
                      {{ $t('returnStorehouseApplyManage.cancelReturn') }}
                    </el-button>
                  </td>
                </tr>
                <tr v-if="returnStorehouseManageInfo.resourceStores.length === 0">
                  <td colspan="9" style="text-align: center; padding: 20px; color: #909399;">
                    {{ $t('common.noData') }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="margin-top">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('returnStorehouseApplyManage.returnDescription') }}</span>
          </div>
          <div class="">
            <el-form label-width="120px">
              <el-form-item :label="$t('returnStorehouseApplyManage.returnDescription')">
                <el-input type="textarea" v-model="returnStorehouseManageInfo.remark"
                  :placeholder="$t('returnStorehouseApplyManage.requiredDescription')" />
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24" style="text-align:right;margin-bottom:20px">
        <el-button type="primary" @click="_submitApply()">
          {{ $t('returnStorehouseApplyManage.submit') }}
        </el-button>
      </el-col>
    </el-row>

    <choose-resource-staff ref="chooseResourceStaff" @setSelectResourceStores="handleSetSelectResourceStores" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStaff from '@/components/resource/chooseResourceStaff.vue'
import {
  listStorehouses,
  saveAllocationStorehouse
} from '@/api/resource/returnStorehouseApplyManageApi'

export default {
  name: 'ReturnStorehouseApplyManageList',
  components: {
    ChooseResourceStaff
  },
  data() {
    return {
      returnStorehouseManageInfo: {
        resourceStores: [],
        storehouses: [],
        remark: '',
        communityId: '',
        apply_type: 20000
      }
    }
  },
  created() {
    this.returnStorehouseManageInfo.communityId = getCommunityId()
    this._listReturnStorehouse()
  },
  methods: {
    _openDeleteResourceStoreModel(resourceStore) {
      this.returnStorehouseManageInfo.resourceStores =
        this.returnStorehouseManageInfo.resourceStores.filter(
          item => item.resId !== resourceStore.resId
        )
      this.$refs.chooseResourceStaff.removeSelectItem(resourceStore.resId)
    },
    _itemReturnAll(resourceStore) {
      const item = this.returnStorehouseManageInfo.resourceStores.find(
        item => item.resId === resourceStore.resId
      )
      if (item) {
        item.curStock = item.miniStock
      }
    },
    _openReturnStorehouseModel() {
      this.$refs.chooseResourceStaff.open()
    },
    async _listReturnStorehouse() {
      try {
        const params = {
          page: 1,
          row: 100,
          isShow: true,
          shType: '2807',
          communityId: this.returnStorehouseManageInfo.communityId
        }
        const { data } = await listStorehouses(params)
        this.returnStorehouseManageInfo.storehouses = data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    async _submitApply() {
      if (!this.returnStorehouseManageInfo.remark) {
        this.$message.error(this.$t('returnStorehouseApplyManage.descriptionRequired'))
        return
      }

      if (this.returnStorehouseManageInfo.resourceStores.length === 0) {
        this.$message.error(this.$t('returnStorehouseApplyManage.selectItemsRequired'))
        return
      }

      let isValid = true
      this.returnStorehouseManageInfo.resourceStores.forEach(item => {
        item.curStock = parseFloat(item.curStock)
        if (item.curStock <= 0) {
          this.$message.error(this.$t('returnStorehouseApplyManage.quantityRequired'))
          isValid = false
          return
        }
        if (item.curStock > parseFloat(item.miniStock)) {
          this.$message.error(`${item.resName}${this.$t('returnStorehouseApplyManage.stockNotEnough')}`)
          isValid = false
          return
        }
        if (!item.shzId) {
          this.$message.error(this.$t('returnStorehouseApplyManage.targetWarehouseRequired'))
          isValid = false
          return
        }
      })

      if (!isValid) return

      try {
        const res = await saveAllocationStorehouse(this.returnStorehouseManageInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('提交失败:', error)
        this.$message.error(this.$t('returnStorehouseApplyManage.returnFailed'))
      }
    },
    handleSetSelectResourceStores(resourceStores) {
      const oldList = this.returnStorehouseManageInfo.resourceStores
      const newList = resourceStores.filter(newItem =>
        !oldList.some(oldItem => oldItem.resId === newItem.resId)
      )
      newList.forEach(item => {
        item.shaName = item.shName
        item.shzId = ''
        item.curStock = '0'
      })
      this.returnStorehouseManageInfo.resourceStores = [...newList, ...oldList]
    }
  }
}
</script>

<style scoped>
.ibox-tools {
  position: absolute;
  right: 15px;
  top: 15px;
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
</style>