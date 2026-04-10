<template>
  <div class="shops-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shops.queryCondition') }}</span>
          </div>
          <el-form :inline="true" :model="queryParams" class="demo-form-inline text-left">
            <el-form-item>
              <el-input v-model.trim="queryParams.roomId" :placeholder="$t('shops.shopIdPlaceholder')"
                clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model.trim="queryParams.roomNum" :placeholder="$t('shops.shopNumberPlaceholder')"
                clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryParams.state" :placeholder="$t('shops.statePlaceholder')" clearable
                style="width: 100%">
                <el-option :label="$t('shops.rented')" value="2006"></el-option>
                <el-option :label="$t('shops.sold')" value="2007"></el-option>
                <el-option :label="$t('shops.idle')" value="2008"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleQuery">{{ $t('common.search') }}</el-button>
              <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shops.shopInfo') }}</span>
            <el-button v-if="hasPrivilege('502020082453087940')" type="primary" size="small" style="float: right"
              @click="openAddDialog">
              {{ $t('shops.addShop') }}
            </el-button>
          </div>

          <el-table :data="shopsData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="roomId" :label="$t('shops.shopId')" align="center" />
            <el-table-column :label="$t('shops.shopNumber')" align="center">
              <template slot-scope="scope">
                {{ scope.row.floorNum }}-{{ scope.row.roomNum }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.floor')" align="center">
              <template slot-scope="scope">
                {{ scope.row.layer }}{{ $t('shops.layerUnit') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.owner')" align="center">
              <template slot-scope="scope">
                <router-link v-if="scope.row.ownerName" :to="`/views/owner/ownerDetail?ownerId=${scope.row.ownerId}`"
                  target="_blank">
                  {{ scope.row.ownerName }}
                </router-link>
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column prop="link" :label="$t('shops.contactNumber')" align="center">
              <template slot-scope="scope">
                {{ scope.row.link || '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.builtUpArea')" align="center">
              <template slot-scope="scope">
                {{ scope.row.builtUpArea }}{{ $t('shops.squareMeter') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.roomArea')" align="center">
              <template slot-scope="scope">
                {{ scope.row.roomArea }}{{ $t('shops.squareMeter') }}
              </template>
            </el-table-column>
            <el-table-column prop="startTime" :label="$t('shops.startTime')" align="center" />
            <el-table-column prop="endTime" :label="$t('shops.endTime')" align="center" />
            <el-table-column prop="roomRent" :label="$t('shops.rent')" align="center" />
            <el-table-column prop="feeCoefficient" :label="$t('shops.feeCoefficient')" align="center" />
            <el-table-column prop="stateName" :label="$t('shops.state')" align="center" />
            <el-table-column v-for="(item, index) in listColumns" :key="index" :label="item" align="center">
              <template slot-scope="scope">
                {{ scope.row.listValues[index] }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.operation')" align="center" fixed="right" width="250">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button v-if="hasPrivilege('502020082493857941') && scope.row.state === '2008'" size="mini"
                    @click="openRentDialog(scope.row)">
                    {{ $t('shops.rentAction') }}
                  </el-button>
                  <el-button v-if="hasPrivilege('502020082493857941') && scope.row.state === '2008'" size="mini"
                    @click="openSellDialog(scope.row)">
                    {{ $t('shops.sellAction') }}
                  </el-button>
                  <el-button v-if="hasPrivilege('502020082493857941') && scope.row.state !== '2008'" size="mini"
                    @click="openUnbind(scope.row)">
                    {{ $t('shops.unbind') }}
                  </el-button>
                  <el-button v-if="hasPrivilege('502020082493857941')" size="mini" type="primary"
                    @click="openEditDialog(scope.row)">
                    {{ $t('shops.edit') }}
                  </el-button>
                  <el-button v-if="hasPrivilege('502020082417457942')" size="mini" type="danger"
                    @click="openDeleteDialog(scope.row)">
                    {{ $t('shops.delete') }}
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <add-shops ref="addDialog" @success="loadData"></add-shops>
    <bind-owner-shops ref="bindDialog" @success="loadData"></bind-owner-shops>
    <edit-shops ref="editDialog" @success="loadData"></edit-shops>
    <delete-room ref="deleteDialog" @handleRefreshRoom="loadData"></delete-room>
  </div>
</template>

<script>
import { queryShops } from '@/api/room/shopsApi'
import AddShops from '@/components/room/addShops'
import BindOwnerShops from '@/components/room/bindOwnerShops'
import EditShops from '@/components/room/editShops'
import DeleteRoom from '@/components/room/deleteRoom'

export default {
  name: 'ShopsList',
  components: {
    AddShops,
    BindOwnerShops,
    EditShops,
    DeleteRoom
  },
  data() {
    return {
      loading: false,
      queryParams: {
        roomId: '',
        roomNum: '',
        state: '',
        roomType: '2020602',
        page: 1,
        row: 10
      },
      shopsData: [],
      listColumns: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      queryShops(this.queryParams)
        .then(res => {
          this.shopsData = res.rooms
          this.pagination.total = res.total
          this.dealShopsAttr(res.rooms)
        })
        .catch(error => {
          console.error(error)
          this.$message.error(this.$t('common.requestError'))
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleQuery() {
      this.queryParams.page = 1
      this.loadData()
    },
    handleReset() {
      this.queryParams = {
        roomId: '',
        roomNum: '',
        state: '',
        roomType: '2020602',
        page: 1,
        row: 10
      }
      this.loadData()
    },
    handleSizeChange(size) {
      this.queryParams.row = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.queryParams.page = page
      this.loadData()
    },
    openAddDialog() {
      this.$refs.addDialog.open()
    },
    openRentDialog(row) {
      this.$refs.bindDialog.open(row, '2006')
    },
    openSellDialog(row) {
      this.$refs.bindDialog.open(row, '2007')
    },
    openEditDialog(row) {
      this.$refs.editDialog.open(row)
    },
    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },
    openUnbind(row) {
      this.$router.push(`/views/owner/deleteOwnerRoom?ownerId=${row.ownerId}`)
    },
    dealShopsAttr(shopss) {
      // 处理商铺属性列
      // this.listColumns = ['Attr1', 'Attr2'] // 实际应从API获取
      shopss.forEach(shop => {
        shop.listValues = [] // 实际应从API获取
      })
    },
  }
}
</script>

<style scoped>
.shops-container {
  padding: 20px;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}
</style>