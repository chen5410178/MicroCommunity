<template>
  <div class="order-manage-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="border-radius">
          <div class="treeview attendance-staff">
            <ul class="list-group text-center border-radius">
              <li v-for="(item, index) in orderManageInfo.apps" :key="index" class="list-group-item node-orgTree"
                :class="{ 'vc-node-selected': orderManageInfo.conditions.appId == item.appId }" @click="swatchApp(item)">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('orderManage.queryCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="orderManageInfo.conditions.oId" :placeholder="$t('orderManage.orderId')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="orderManageInfo.conditions.extTransactionId" :placeholder="$t('orderManage.externalId')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="orderManageInfo.conditions.bId" :placeholder="$t('orderManage.businessId')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="orderManageInfo.conditions.startTime" type="datetime"
                :placeholder="$t('orderManage.startTime')" class="w-100" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="orderManageInfo.conditions.endTime" type="datetime"
                :placeholder="$t('orderManage.endTime')" class="w-100" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryOrdersMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="mt-10">
            <el-col :span="4">
              <el-input v-model="orderManageInfo.conditions.staffNameLike" :placeholder="$t('orderManage.operator')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="orderManageInfo.conditions.businessTypeNameLike"
                :placeholder="$t('orderManage.businessName')" clearable />
            </el-col>
          </el-row>
        </el-card>

        <el-card class="mt-20">
          <div slot="header" class="text-left">
            <span>{{ $t('orderManage.orderList') }}</span>
          </div>
          <el-table :data="orderManageInfo.orderDataVos" border style="width: 100%">
            <el-table-column prop="appName" :label="$t('orderManage.appName')" align="center" />
            <el-table-column :label="$t('orderManage.orderId')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.oId }}</div>
                <div>({{ scope.row.extTransactionId }})</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('orderManage.businessName')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.name }}</div>
                <div>({{ scope.row.businessTypeCd }})</div>
              </template>
            </el-table-column>
            <el-table-column prop="bId" :label="$t('orderManage.businessId')" align="center" />
            <el-table-column prop="createTime" :label="$t('orderManage.createTime')" align="center" />
            <el-table-column :label="$t('orderManage.action')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.action == 'ADD'">{{ $t('orderManage.add') }}</span>
                <span v-else-if="scope.row.action == 'MOD'">{{ $t('orderManage.modify') }}</span>
                <span v-else-if="scope.row.action == 'DEL'">{{ $t('orderManage.delete') }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="actionObj" :label="$t('orderManage.actionObj')" align="center" />
            <el-table-column prop="userName" :label="$t('orderManage.operator')" align="center" />
            <el-table-column :label="$t('orderManage.status')" align="center">
              <template slot-scope="scope">
                {{ scope.row.statusCd == 'C' ? $t('orderManage.completed') : $t('orderManage.failed') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center">
              <template slot-scope="scope">
                <el-button v-if="scope.row.bId" size="mini" @click="_showUnitemLog(scope.row)">
                  {{ $t('orderManage.log') }}
                </el-button>
                <span v-else>{{ $t('orderManage.noLog') }}</span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <view-un-item-log ref="viewUnItemLog" />
  </div>
</template>

<script>
import { listCorders, listPublicApps } from '@/api/admin/orderManageApi'
import ViewUnItemLog from '@/components/admin/viewUnItemLog'

export default {
  name: 'OrderManageList',
  components: {
    ViewUnItemLog
  },
  data() {
    return {
      orderManageInfo: {
        orderDataVos: [],
        apps: [],
        total: 0,
        records: 1,
        conditions: {
          oId: '',
          extTransactionId: '',
          orderTypeCd: '',
          appId: '',
          startTime: '',
          endTime: '',
          staffNameLike: '',
          bId: '',
          businessTypeNameLike: '',
          page: 1,
          row: 20
        }
      },
      page: {
        current: 1,
        size: 20,
        total: 0
      }
    }
  },
  created() {
    this._listOrders(this.page.current, this.page.size)
    this._loadApps()
  },
  methods: {
    async _listOrders(page, rows) {
      try {
        this.orderManageInfo.conditions.page = page
        this.orderManageInfo.conditions.row = rows
        const params = { ...this.orderManageInfo.conditions }

        const res = await listCorders(params)
        this.orderManageInfo.total = res.total
        this.orderManageInfo.records = res.records
        this.orderManageInfo.orderDataVos = res.data
        this.page.total = res.total
      } catch (error) {
        this.$message.error(this.$t('orderManage.fetchError'))
      }
    },
    async _loadApps() {
      try {
        this.orderManageInfo.apps = [{
          appId: '',
          name: this.$t('orderManage.allApps')
        }]

        const res = await listPublicApps({ page: 1, row: 50 })
        this.orderManageInfo.apps = [...this.orderManageInfo.apps, ...res.apps]
      } catch (error) {
        this.$message.error(this.$t('orderManage.fetchAppsError'))
      }
    },
    _queryOrdersMethod() {
      this.page.current = 1
      this._listOrders(this.page.current, this.page.size)
    },
    swatchApp(app) {
      this.orderManageInfo.conditions.appId = app.appId
      this._listOrders(this.page.current, this.page.size)
    },
    _showUnitemLog(order) {
      this.$refs.viewUnItemLog.open(order)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listOrders(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listOrders(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.order-manage-container {
  padding: 20px;
}

.border-radius {
  border-radius: 4px;
}

.list-group {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list-group-item {
  padding: 10px;
  cursor: pointer;
}

.list-group-item:hover {
  background-color: #f5f7fa;
}

.vc-node-selected {
  background-color: #ecf5ff;
  color: #409eff;
}

.mt-10 {
  margin-top: 10px;
}

.mt-20 {
  margin-top: 20px;
}

.w-100 {
  width: 100%;
}
</style>