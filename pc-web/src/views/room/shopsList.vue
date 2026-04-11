<template>
  <div class="shops-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="location-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shops.shopLocation') }}</span>
            <el-button type="text" size="mini" @click="openAddLocation">{{ $t('common.add') }}</el-button>
          </div>
          <shop-location-tree ref="locationTree" @select="onLocationSelect"></shop-location-tree>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shops.queryCondition') }}</span>
          </div>
          <el-form :inline="true" :model="queryParams" class="demo-form-inline text-left">
            <el-form-item>
              <el-input v-model.trim="queryParams.shopNum" :placeholder="$t('shops.shopNumberPlaceholder')"
                clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryParams.state" :placeholder="$t('shops.statePlaceholder')" clearable
                style="width: 100%">
                <el-option :label="$t('shops.idle')" value="2008"></el-option>
                <el-option :label="$t('shops.rented')" value="2006"></el-option>
                <el-option :label="$t('shops.sold')" value="2007"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleQuery">{{ $t('common.search') }}</el-button>
              <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shops.shopInfo') }}</span>
            <el-button type="primary" size="small" style="float: right" @click="openAddShop">
              {{ $t('shops.addShop') }}
            </el-button>
          </div>

          <el-table :data="shopsData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="shopNum" :label="$t('shops.shopNumber')" align="center" />
            <el-table-column prop="locationName" :label="$t('shops.location')" align="center" />
            <el-table-column :label="$t('shops.owner')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.ownerName">{{ scope.row.ownerName }}</span>
                <span v-else>-</span>
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
            <el-table-column :label="$t('shops.shopArea')" align="center">
              <template slot-scope="scope">
                {{ scope.row.shopArea || '-' }}{{ scope.row.shopArea ? $t('shops.squareMeter') : '' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.state')" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.state === '2008'" type="success">{{ $t('shops.idle') }}</el-tag>
                <el-tag v-else-if="scope.row.state === '2006'" type="warning">{{ $t('shops.rented') }}</el-tag>
                <el-tag v-else-if="scope.row.state === '2007'" type="info">{{ $t('shops.sold') }}</el-tag>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.operation')" align="center" fixed="right" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="openEditShop(scope.row)">
                  {{ $t('shops.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="openDeleteShop(scope.row)">
                  {{ $t('shops.delete') }}
                </el-button>
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

    <el-dialog :title="locationDialogTitle" :visible.sync="locationDialogVisible" width="500px" @close="onLocationDialogClose">
      <el-form :model="locationForm" :rules="locationRules" ref="locationFormRef" label-width="100px">
        <el-form-item :label="$t('shops.locationName')" prop="locationName">
          <el-input v-model="locationForm.locationName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shops.parentLocation')" prop="parentId">
          <el-input v-model="locationForm.parentId" readonly></el-input>
        </el-form-item>
        <el-form-item :label="$t('shops.seq')" prop="seq">
          <el-input-number v-model="locationForm.seq" :min="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item :label="$t('shops.remark')" prop="remark">
          <el-input type="textarea" v-model="locationForm.remark" rows="3"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="locationDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="saveLocation">{{ $t('common.submit') }}</el-button>
      </span>
    </el-dialog>

    <el-dialog :title="shopDialogTitle" :visible.sync="shopDialogVisible" width="600px" @close="onShopDialogClose">
      <el-form :model="shopForm" :rules="shopRules" ref="shopFormRef" label-width="100px">
        <el-form-item :label="$t('shops.shopNumber')" prop="shopNum">
          <el-input v-model="shopForm.shopNum"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shops.location')" prop="locationId">
          <el-select v-model="shopForm.locationId" style="width: 100%">
            <el-option v-for="loc in locationOptions" :key="loc.locationId" :label="loc.label" :value="loc.locationId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shops.builtUpArea')" prop="builtUpArea">
          <el-input v-model="shopForm.builtUpArea"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shops.shopArea')">
          <el-input v-model="shopForm.shopArea"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shops.state')" prop="state">
          <el-select v-model="shopForm.state" style="width: 100%">
            <el-option :label="$t('shops.idle')" value="2008"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shops.remark')">
          <el-input type="textarea" v-model="shopForm.remark" rows="3"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="shopDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="saveShop">{{ $t('common.submit') }}</el-button>
      </span>
    </el-dialog>

    <el-dialog :title="$t('shops.confirmDelete')" :visible.sync="deleteDialogVisible" width="400px">
      <span>{{ $t('shops.deleteConfirmMsg') }}</span>
      <span slot="footer">
        <el-button @click="deleteDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="danger" @click="confirmDeleteShop">{{ $t('common.confirm') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listBuildingShops, saveBuildingShop, updateBuildingShop, deleteBuildingShop } from '@/api/room/buildingShopApi'
import { saveShopLocation, updateShopLocation } from '@/api/room/shopLocationApi'
import ShopLocationTree from '@/components/room/ShopLocationTree'

export default {
  name: 'ShopsList',
  components: {
    ShopLocationTree
  },
  data() {
    return {
      loading: false,
      queryParams: {
        shopNum: '',
        state: '',
        locationId: '',
        page: 1,
        row: 10
      },
      shopsData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      locationDialogVisible: false,
      locationDialogTitle: '',
      locationForm: {
        locationId: '',
        locationName: '',
        parentId: '-1',
        seq: 1,
        remark: ''
      },
      locationRules: {
        locationName: [{ required: true, message: this.$t('shops.locationNameRequired'), trigger: 'blur' }]
      },
      shopDialogVisible: false,
      shopDialogTitle: '',
      shopForm: {
        shopId: '',
        shopNum: '',
        locationId: '',
        builtUpArea: '',
        shopArea: '',
        state: '2008',
        remark: ''
      },
      shopRules: {
        shopNum: [{ required: true, message: this.$t('shops.shopNumberRequired'), trigger: 'blur' }],
        locationId: [{ required: true, message: this.$t('shops.locationRequired'), trigger: 'change' }],
        builtUpArea: [{ required: true, message: this.$t('shops.builtUpAreaRequired'), trigger: 'blur' }]
      },
      locationOptions: [],
      deleteDialogVisible: false,
      deleteShopId: ''
    }
  },
  created() {
    this.loadData()
    this.loadLocationOptions()
  },
  methods: {
    loadData() {
      this.loading = true
      listBuildingShops(this.queryParams)
        .then(res => {
          this.shopsData = res.data
          this.pagination.total = res.total
        })
        .catch(error => {
          console.error(error)
          this.$message.error(this.$t('common.requestError'))
        })
        .finally(() => {
          this.loading = false
        })
    },
    loadLocationOptions() {
      this.$refs.locationTree && this.$refs.locationTree.getLocations().then(locations => {
        this.locationOptions = this.flattenLocations(locations)
      })
    },
    flattenLocations(locations, level = 0) {
      let result = []
      locations.forEach(loc => {
        result.push({
          locationId: loc.locationId,
          label: '　'.repeat(level) + loc.locationName
        })
        if (loc.children && loc.children.length > 0) {
          result = result.concat(this.flattenLocations(loc.children, level + 1))
        }
      })
      return result
    },
    onLocationSelect(data) {
      this.queryParams.locationId = data.locationId || ''
      this.loadData()
    },
    handleQuery() {
      this.queryParams.page = 1
      this.loadData()
    },
    handleReset() {
      this.queryParams = {
        shopNum: '',
        state: '',
        locationId: '',
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
    openAddLocation() {
      this.locationDialogTitle = this.$t('shops.addLocation')
      this.locationForm = {
        locationId: '',
        locationName: '',
        parentId: '-1',
        seq: 1,
        remark: ''
      }
      this.locationDialogVisible = true
    },
    onLocationDialogClose() {
      this.$refs.locationFormRef && this.$refs.locationFormRef.resetFields()
    },
    saveLocation() {
      this.$refs.locationFormRef.validate(valid => {
        if (!valid) return
        const data = { ...this.locationForm }
        if (data.locationId) {
          updateShopLocation(data).then(() => {
            this.$message.success(this.$t('common.operationSuccess'))
            this.locationDialogVisible = false
            this.$refs.locationTree.loadData()
          })
        } else {
          saveShopLocation(data).then(() => {
            this.$message.success(this.$t('common.operationSuccess'))
            this.locationDialogVisible = false
            this.$refs.locationTree.loadData()
            this.loadLocationOptions()
          })
        }
      })
    },
    openAddShop() {
      this.shopDialogTitle = this.$t('shops.addShop')
      this.shopForm = {
        shopId: '',
        shopNum: '',
        locationId: this.queryParams.locationId || '',
        builtUpArea: '',
        shopArea: '',
        state: '2008',
        remark: ''
      }
      this.shopDialogVisible = true
    },
    openEditShop(row) {
      this.shopDialogTitle = this.$t('shops.editShop')
      this.shopForm = {
        shopId: row.shopId,
        shopNum: row.shopNum,
        locationId: row.locationId,
        builtUpArea: row.builtUpArea,
        shopArea: row.shopArea,
        state: row.state,
        remark: row.remark
      }
      this.shopDialogVisible = true
    },
    onShopDialogClose() {
      this.$refs.shopFormRef && this.$refs.shopFormRef.resetFields()
    },
    saveShop() {
      this.$refs.shopFormRef.validate(valid => {
        if (!valid) return
        const data = { ...this.shopForm }
        if (data.shopId) {
          updateBuildingShop(data).then(() => {
            this.$message.success(this.$t('common.operationSuccess'))
            this.shopDialogVisible = false
            this.loadData()
          })
        } else {
          saveBuildingShop(data).then(() => {
            this.$message.success(this.$t('common.operationSuccess'))
            this.shopDialogVisible = false
            this.loadData()
          })
        }
      })
    },
    openDeleteShop(row) {
      this.deleteShopId = row.shopId
      this.deleteDialogVisible = true
    },
    confirmDeleteShop() {
      deleteBuildingShop(this.deleteShopId).then(() => {
        this.$message.success(this.$t('common.operationSuccess'))
        this.deleteDialogVisible = false
        this.loadData()
      })
    }
  }
}
</script>

<style scoped>
.shops-container {
  padding: 20px;
}

.location-card {
  height: calc(100vh - 140px);
  overflow-y: auto;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}
</style>