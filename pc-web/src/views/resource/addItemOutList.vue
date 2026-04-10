<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('addItemOut.recipient') }}</div>
            <div class="ibox-tools">
              <el-button type="primary" size="small" @click="goBack">
                <i class="el-icon-close"></i>
                {{ $t('addItemOut.return') }}
              </el-button>
            </div>
          </div>
          <div class="">
            <el-form :model="addItemOutInfo" label-position="right" label-width="120px">
              <el-form-item :label="$t('addItemOut.warehouse')">
                <el-select v-model="addItemOutInfo.shId" @change="_computeFlow" style="width:100%"
                  :placeholder="$t('addItemOut.warehousePlaceholder')">
                  <el-option v-for="(item, index) in addItemOutInfo.storehouses" :key="index" :label="item.shName"
                    :value="item.shId"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('addItemOut.recipient')">
                <el-row>
                  <el-col :span="addItemOutInfo.canChooseEndUserOrNot ? 18 : 24">
                    <el-input :placeholder="$t('addItemOut.contactPlaceholder')"
                      v-model.trim="addItemOutInfo.endUserInfo.staffName" style="width:100%"></el-input>
                  </el-col>
                  <el-col :span="6" v-if="addItemOutInfo.canChooseEndUserOrNot">
                    <el-button type="primary" @click="chooseEndUser">
                      <i class="el-icon-search"></i> {{ $t('addItemOut.selectRecipient') }}
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>

              <el-form-item :label="$t('addItemOut.contactPhone')">
                <el-input :placeholder="$t('addItemOut.phonePlaceholder')"
                  v-model.trim="addItemOutInfo.endUserInfo.staffTel" style="width:100%"></el-input>
              </el-form-item>

              <el-form-item :label="$t('addItemOut.applicationNote')">
                <el-input type="textarea" :placeholder="$t('addItemOut.notePlaceholder')"
                  v-model.trim="addItemOutInfo.description" style="width:100%"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 选择物品 -->
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('addItemOut.itemInfo') }}</div>
            <div class="ibox-tools">
              <el-button type="primary" size="small" style="margin-right:10px;"
                @click="_openSelectResourceStoreInfoModel">
                <i class="el-icon-plus"></i>
                {{ $t('addItemOut.selectItem') }}
              </el-button>
            </div>
          </div>
          <div class="">
            <table class="custom-table">
              <thead>
                <tr>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.itemType') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.itemName') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.itemSpec') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.price') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.stock') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.applyQuantity') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.remark') }}
                  </th>
                  <th
                    style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5; background-color: #fafafa;">
                    {{ $t('addItemOut.operation') }}
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in addItemOutInfo.resourceStores" :key="index"
                  style="border-bottom: 1px solid #ebeef5;">
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
                    <select v-model="item.timesId" @change="_changeTimesId($event, index)" class="custom-select">
                      <option value="">{{ $t('addItemOut.selectPrice') }}</option>
                      <option v-for="time in item.times" :key="time.timesId" :value="time.timesId">
                        {{ time.price }}
                      </option>
                    </select>
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    {{ _getTimesStock(item) }}{{ item.unitCodeName }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    <el-input v-model.trim="item.quantity" type="number"
                      :placeholder="$t('addItemOut.quantityPlaceholder')"
                      style="width:70%;display:inline-block;"></el-input>
                    &nbsp;&nbsp;{{ item.unitCodeName }}
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    <el-input v-model.trim="item.remark" type="text"
                      :placeholder="$t('addItemOut.remarkPlaceholder')"></el-input>
                  </td>
                  <td style="text-align: center; padding: 12px 8px; border: 1px solid #ebeef5;">
                    <el-button type="danger" size="small" @click="_removeSelectResourceStoreItem(item.resId)">
                      <i class="el-icon-delete"></i>
                      {{ $t('addItemOut.remove') }}
                    </el-button>
                  </td>
                </tr>
                <tr v-if="addItemOutInfo.resourceStores.length === 0">
                  <td colspan="8" style="text-align: center; padding: 20px; color: #909399;">
                    {{ $t('common.noData') }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 审批人 -->
    <el-row v-if="addItemOutInfo.audit.assignee === '-2'">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <h5>{{ $t('addItemOut.approver') }}</h5>
          </div>
          <div class="">
            <el-form :model="addItemOutInfo" label-position="right" label-width="120px">
              <el-form-item :label="$t('addItemOut.approver')">
                <el-row :gutter="20">
                  <el-col :span="17">
                    <el-input :placeholder="$t('addItemOut.approverPlaceholder')"
                      v-model="addItemOutInfo.audit.staffName" disabled></el-input>
                  </el-col>
                  <el-col :span="6">
                    <el-button type="primary" @click="chooseStaff">
                      <i class="el-icon-search"></i> {{ $t('addItemOut.select') }}
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="22" :offset="2" style="margin-bottom:20px; text-align:right">
        <el-button type="primary" @click="_finishStep">
          {{ $t('addItemOut.submit') }}
        </el-button>
      </el-col>
    </el-row>

    <choose-resource-store2 ref="chooseResourceStore2"
      @setSelectResourceStores="handleSetSelectResourceStores"></choose-resource-store2>

    <select-staff ref="selectStaff" @selectStaff="handleStaffChange"></select-staff>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStore2 from '@/components/resource/chooseResourceStore2'
import SelectStaff from '@/components/staff/SelectStaff'
import { goodsCollection, listStorehouses, queryFirstAuditStaff } from '@/api/resource/addItemOutApi'
import { getUserName, getUserTel } from '@/api/user/userApi'

export default {
  name: 'AddItemOutList',
  components: {
    ChooseResourceStore2,
    SelectStaff,
  },
  data() {
    return {
      addItemOutInfo: {
        resourceStores: [],
        resourceSuppliers: [],
        audit: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        endUserInfo: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: '',
          staffTel: ''
        },
        description: '',
        endUserName: '',
        endUserTel: '',
        acceptStaffId: '',
        file: '',
        resOrderType: '20000',
        staffId: '',
        staffName: '',
        communityId: '',
        shId: '',
        storehouses: [],
        flowId: '',
        canChooseEndUserOrNot: false,
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.addItemOutInfo.communityId = this.communityId
    this._reset()
    this.initPage()
  },
  mounted() {
  },
  beforeDestroy() {
  },
  methods: {
    _reset() {
      this.addItemOutInfo = {
        resourceStores: [],
        resourceSuppliers: [],
        audit: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        endUserInfo: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: '',
          staffTel: ''
        },
        description: '',
        endUserName: '',
        endUserTel: '',
        acceptStaffId: '',
        file: '',
        resOrderType: '20000',
        staffId: '',
        staffName: '',
        communityId: this.communityId,
        shId: '',
        storehouses: [],
        flowId: '',
        canChooseEndUserOrNot: false,
      }
    },
    initPage() {
      const from = this.$route.query.from
      if (from === 'resourceStore') {
        this.addItemOutInfo.canChooseEndUserOrNot = true
      }

      this.addItemOutInfo.endUserInfo.staffName = getUserName() || ''
      this.addItemOutInfo.endUserInfo.staffTel = getUserTel() || ''

      this._loadResourceSuppliers()
      this._listPurchaseStorehouses()
    },
    goBack() {
      this.$router.go(-1)
    },
    _resourcesFromSameHouse(resourcesList) {
      if (!resourcesList || resourcesList.length < 2) return true

      let lastHouse = ''
      for (let i = 0; i < resourcesList.length; i++) {
        if (!lastHouse) {
          lastHouse = resourcesList[i].shId
          continue
        }
        if (lastHouse !== resourcesList[i].shId) {
          return false
        }
      }
      return true
    },
    _openSelectResourceStoreInfoModel() {
      if (!this.addItemOutInfo.shId) {
        this.$message.error(this.$t('addItemOut.warehouseRequired'))
        return
      }
      if (!this.addItemOutInfo.endUserInfo.staffName) {
        this.$message.error(this.$t('addItemOut.contactRequired'))
        return
      }
      if (!this.addItemOutInfo.endUserInfo.staffTel) {
        this.$message.error(this.$t('addItemOut.phoneRequired'))
        return
      }
      if (!this.addItemOutInfo.description) {
        this.$message.error(this.$t('addItemOut.noteRequired'))
        return
      }

      this.$refs.chooseResourceStore2.open({
        shId: this.addItemOutInfo.shId
      })
    },
    async _finishStep() {
      const resourceStores = this.addItemOutInfo.resourceStores
      if (!resourceStores || resourceStores.length === 0) {
        this.$message.error(this.$t('addItemOut.itemRequired'))
        return
      }

      if (!this._resourcesFromSameHouse(resourceStores)) {
        this.$message.error(this.$t('addItemOut.sameWarehouseRequired'))
        return
      }

      //let validate = true
      // resourceStores.forEach(item => {
      //   const selectedStock = item.selectedStock || 0
      //   const quantity = parseFloat(item.quantity) || 0
      //   console.log('item', item, selectedStock, quantity)
      //   if (quantity > selectedStock) {
      //     validate = false
      //   }
      // })

      // if (!validate) {
      //   this.$message.error(this.$t('addItemOut.insufficientStock'))
      //   return
      // }

      this.addItemOutInfo.endUserName = this.addItemOutInfo.endUserInfo.staffName
      this.addItemOutInfo.endUserTel = this.addItemOutInfo.endUserInfo.staffTel
      this.addItemOutInfo.acceptStaffId = this.addItemOutInfo.endUserInfo.staffId

      if (this.addItemOutInfo.canChooseEndUserOrNot) {
        this.addItemOutInfo.useSwitch = "OFF"
      }

      try {
        const res = await goodsCollection(this.addItemOutInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg || this.$t('addItemOut.submitFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('addItemOut.submitError'))
        console.error(error)
      }
    },
    _loadResourceSuppliers() {
      // 实际调用API的方法
      // 这里保留占位，实际实现需要调用API
    },
    async _listPurchaseStorehouses() {
      try {
        const res = await listStorehouses({
          page: 1,
          row: 100,
          communityId: "",
          allowPurchase: 'ON'
        })
        this.addItemOutInfo.storehouses = res.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _removeSelectResourceStoreItem(resId) {
      this.addItemOutInfo.resourceStores = this.addItemOutInfo.resourceStores.filter(
        item => item.resId !== resId
      )
    },
    _changeTimesId(value, index) {
      const times = this.addItemOutInfo.resourceStores[index].times
      times.forEach(item => {
        if (item.timesId === value) {
          this.addItemOutInfo.resourceStores[index].selectedStock = item.stock
        }
      })
      this.$nextTick(() => {
        this.$forceUpdate()
      })
    },
    _getTimesStock(resourceStore) {
      if (!resourceStore.timesId) return "-"

      let stock = 0
      resourceStore.times.forEach(item => {
        if (item.timesId === resourceStore.timesId) {
          stock = item.stock
        }
      })


      return stock
    },
    async _loadStaffOrg(flowId) {
      // 实际调用API的方法
      // 这里保留占位，实际实现需要调用API
      const res = await queryFirstAuditStaff({
        communityId: this.communityId,
        flowId: flowId
      })
      if (res.code != 0) {
        return;
      }
      let _data = res.data
      Object.assign(this.addItemOutInfo.audit, _data[0])
      if (!_data[0].assignee.startsWith('-')) {
        this.addItemOutInfo.audit.staffId = this.addItemOutInfo.audit.assignee;
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.addItemOutInfo.audit)
    },
    chooseEndUser() {
      this.$refs.selectStaff.open(this.addItemOutInfo.endUserInfo)
    },
    _computeFlow() {
      this.addItemOutInfo.resourceStores = []
      const storehouse = this.addItemOutInfo.storehouses.find(
        item => item.shId === this.addItemOutInfo.shId
      )

      if (storehouse) {
        this.addItemOutInfo.flowId = storehouse.useFlowId
        this._loadStaffOrg(storehouse.useFlowId)
      }
    },
    handleSetSelectResourceStores(resourceStores) {
      const oldList = this.addItemOutInfo.resourceStores
      const newList = [...resourceStores]

      newList.forEach((newItem, newIndex) => {
        newItem.rsId = ''
        newItem.timesId = ''
        if (newItem.times && newItem.times.length > 0) {
          newItem.timesId = newItem.times[0].timesId
        }

        oldList.forEach(oldItem => {
          if (oldItem.resId === newItem.resId && newItem.times && newItem.times.length < 2) {
            delete newList[newIndex]
          }
        })
      })

      const filteredList = newList.filter(item => item['resId'])
      this.addItemOutInfo.resourceStores = [...oldList, ...filteredList]
    },
    handleStaffChange(staff) {
      this.addItemOutInfo.audit.staffId = staff.staffId
      this.addItemOutInfo.audit.staffName = staff.staffName
      this.addItemOutInfo.endUserInfo.staffId = staff.staffId
      this.addItemOutInfo.endUserInfo.staffName = staff.staffName
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}

.ibox-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ibox-tools {
  position: relative;
  top: 10px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
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