<template>
  <div class="shops-container">
    <div class="bg-white padding-sm text-left">
      <el-button type="primary" size="small" @click="openAddLocation">
        <i class="el-icon-plus"></i>{{ $t('shops.addLocation') }}
      </el-button>
      <el-button type="primary" size="small" @click="openEditLocation" v-if="selectedLocation.locationId">
        <i class="el-icon-edit"></i>{{ $t('shops.editLocation') }}
      </el-button>
      <el-button type="primary" size="small" @click="openDeleteLocation" v-if="selectedLocation.locationId">
        <i class="el-icon-close"></i>{{ $t('shops.deleteLocation') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openAddChildLocation" v-if="selectedLocation.locationId">
        <i class="el-icon-plus"></i>{{ $t('shops.addChildLocation') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openAddShop" v-if="hasPrivilege('502020082453087940')">
        <i class="el-icon-plus"></i>
        <span>{{ $t('shops.addShop') }}</span>
      </el-button>
    </div>

    <div class="resizable-layout margin-top-sm">
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <shop-location-tree ref="locationTree" @select="onLocationSelect"></shop-location-tree>
      </div>

      <div class="resize-handle" @mousedown="startResize" @mouseover="showResizeCursor" @mouseleave="hideResizeCursor">
        <div class="resize-line"></div>
      </div>

      <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
        <el-card class="ibox">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('shops.queryCondition') }}</span>
          </div>
          <el-form :inline="true" class="text-left">
            <el-form-item>
              <el-input v-model.trim="queryParams.shopNum" :placeholder="$t('shops.shopNumberPlaceholder')"
                size="small" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryParams.state" :placeholder="$t('shops.statePlaceholder')" clearable
                size="small" style="width: 100%">
                <el-option :label="$t('shops.idle')" value="2008"></el-option>
                <el-option :label="$t('shops.rented')" value="2006"></el-option>
                <el-option :label="$t('shops.sold')" value="2007"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="handleQuery">{{ $t('common.search') }}</el-button>
              <el-button size="small" @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card>
          <div class="border-radius">
            <el-table :data="shopsData" border stripe v-loading="loading">
              <el-table-column prop="shopNum" :label="$t('shops.shopNumber')" width="120" fixed="left" align="center">
                <template slot-scope="scope">
                  <div class="hc-td">{{ scope.row.shopNum }}</div>
                </template>
              </el-table-column>
              <el-table-column prop="locationName" :label="$t('shops.location')" width="150" align="center">
                <template slot-scope="scope">
                  <div class="hc-td">{{ scope.row.locationName }}</div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('shops.owner')" width="150" align="center">
                <template slot-scope="scope">
                  <div v-if="scope.row.ownerName" class="hc-td">
                    <a target="_blank" :href="'/#/views/owner/ownerDetail?ownerId=' + scope.row.ownerId">
                      {{ scope.row.ownerName }}
                    </a>
                  </div>
                  <div v-else class="hc-td textAuto">-</div>
                </template>
              </el-table-column>
              <el-table-column prop="link" :label="$t('shops.contactNumber')" width="120" align="center">
                <template slot-scope="scope">
                  <div class="hc-td">{{ scope.row.link || '-' }}</div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('shops.builtUpArea')" width="120" align="center">
                <template slot-scope="scope">
                  <div class="hc-td">
                    {{ scope.row.builtUpArea }}{{ $t('shops.squareMeter') }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('shops.shopArea')" width="120" align="center">
                <template slot-scope="scope">
                  <div class="hc-td">
                    {{ scope.row.shopArea || '-' }}{{ scope.row.shopArea ? $t('shops.squareMeter') : '' }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('shops.state')" width="100" align="center">
                <template slot-scope="scope">
                  <div class="hc-td">
                    <el-tag v-if="scope.row.state === '2008'" type="success" size="mini">{{ $t('shops.idle') }}</el-tag>
                    <el-tag v-else-if="scope.row.state === '2006'" type="warning" size="mini">{{ $t('shops.rented') }}</el-tag>
                    <el-tag v-else-if="scope.row.state === '2007'" type="info" size="mini">{{ $t('shops.sold') }}</el-tag>
                    <span v-else>-</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('shops.operation')" fixed="right" width="180" align="center">
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

            <el-row class="margin-top">
              <el-col :span="12" class="float-right">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                  :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
                  layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"></el-pagination>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
    </div>

    <el-dialog :title="locationDialogTitle" :visible.sync="locationDialogVisible" width="500px" @close="onLocationDialogClose">
      <el-form :model="locationForm" :rules="locationRules" ref="locationFormRef" label-width="100px">
        <el-form-item :label="$t('shops.locationName')" prop="locationName">
          <el-input v-model="locationForm.locationName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shops.parentLocation')" prop="parentId">
          <el-input :value="parentLocationName" readonly></el-input>
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

    <el-dialog :title="$t('shops.confirmDelete')" :visible.sync="deleteLocationDialogVisible" width="400px">
      <span>{{ $t('shops.deleteLocationConfirmMsg') }}</span>
      <span slot="footer">
        <el-button @click="deleteLocationDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="danger" @click="confirmDeleteLocation">{{ $t('common.confirm') }}</el-button>
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
import { saveShopLocation, updateShopLocation, deleteShopLocation } from '@/api/room/shopLocationApi'
import ShopLocationTree from '@/components/room/ShopLocationTree'
import {getCommunityId} from '@/api/community/communityApi'


export default {
  name: 'ShopsList',
  components: {
    ShopLocationTree
  },
  data() {
    return {
      loading: false,
      treeWidth: 300,
      minTreeWidth: 200,
      maxTreeWidth: 600,
      isResizing: false,
      startX: 0,
      startWidth: 0,
      selectedLocation: {
        locationId: '',
        locationName: '',
        parentId: ''
      },
      queryParams: {
        shopNum: '',
        state: '',
        locationId: '',
        communityId:getCommunityId(),
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
      deleteShopId: '',
      deleteLocationDialogVisible: false
    }
  },
  computed: {
    parentLocationName() {
      if (!this.locationForm.parentId || this.locationForm.parentId === '-1') {
        return this.$t('shops.topLevel')
      }
      return this.selectedLocation.locationName || ''
    }
  },
  mounted() {
    this.restoreTreeWidth()
    document.addEventListener('mousemove', this.onMouseMove)
    document.addEventListener('mouseup', this.onMouseUp)
  },
  beforeDestroy() {
    document.removeEventListener('mousemove', this.onMouseMove)
    document.removeEventListener('mouseup', this.onMouseUp)
  },
  created() {
    this.loadData()
    this.loadLocationOptions()
  },
  methods: {
    hasPrivilege() {
      return true
    },
    loadData() {
      this.loading = true
      listBuildingShops(this.queryParams)
        .then(res => {
          const data = res.data || res
          this.shopsData = Array.isArray(data) ? data : (data.data || [])
          this.pagination.total = data.total || (Array.isArray(data.data) ? data.data.length : 0)
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
      this.selectedLocation = {
        locationId: data.locationId || '',
        locationName: data.locationName || '',
        parentId: data.parentId || '-1'
      }
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
    openEditLocation() {
      if (!this.selectedLocation.locationId) {
        this.$message.warning(this.$t('shops.pleaseSelectLocation'))
        return
      }
      this.locationDialogTitle = this.$t('shops.editLocation')
      this.locationForm = {
        locationId: this.selectedLocation.locationId,
        locationName: this.selectedLocation.locationName,
        parentId: this.selectedLocation.parentId,
        seq: this.selectedLocation.seq || 1,
        remark: this.selectedLocation.remark || ''
      }
      this.locationDialogVisible = true
    },
    openDeleteLocation() {
      if (!this.selectedLocation.locationId) {
        this.$message.warning(this.$t('shops.pleaseSelectLocation'))
        return
      }
      this.deleteLocationDialogVisible = true
    },
    confirmDeleteLocation() {
      deleteShopLocation(this.selectedLocation.locationId).then(() => {
        this.$message.success(this.$t('common.operationSuccess'))
        this.deleteLocationDialogVisible = false
        this.selectedLocation = { locationId: '', locationName: '', parentId: '' }
        this.$refs.locationTree.loadData()
        this.loadData()
      })
    },
    openAddChildLocation() {
      if (!this.selectedLocation.locationId) {
        this.$message.warning(this.$t('shops.pleaseSelectLocation'))
        return
      }
      this.locationDialogTitle = this.$t('shops.addChildLocation')
      this.locationForm = {
        locationId: '',
        locationName: '',
        parentId: this.selectedLocation.locationId,
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
    },
    restoreTreeWidth() {
      const savedWidth = localStorage.getItem('shopsListTreeWidth')
      if (savedWidth) {
        const width = parseInt(savedWidth)
        if (width >= this.minTreeWidth && width <= this.maxTreeWidth) {
          this.treeWidth = width
        }
      }
    },
    saveTreeWidth() {
      localStorage.setItem('shopsListTreeWidth', this.treeWidth.toString())
    },
    startResize(event) {
      event.preventDefault()
      this.isResizing = true
      this.startX = event.clientX
      this.startWidth = this.treeWidth
      document.body.style.cursor = 'col-resize'
      document.body.style.userSelect = 'none'
    },
    onMouseMove(event) {
      if (!this.isResizing) return
      const deltaX = event.clientX - this.startX
      const newWidth = this.startWidth + deltaX
      if (newWidth >= this.minTreeWidth && newWidth <= this.maxTreeWidth) {
        this.treeWidth = newWidth
      }
    },
    onMouseUp() {
      if (this.isResizing) {
        this.isResizing = false
        this.saveTreeWidth()
        document.body.style.cursor = ''
        document.body.style.userSelect = ''
      }
    },
    showResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = 'col-resize'
      }
    },
    hideResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = ''
      }
    }
  }
}
</script>

<style scoped>
.shops-container {
  padding: 15px;
  height: calc(100vh - 120px);
  overflow: hidden;
}

.resizable-layout {
  display: flex;
  height: 100%;
  position: relative;
}

.tree-panel {
  background: #fff;
  border-radius: 4px;
  padding: 10px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  min-height: 450px;
}

.content-panel {
  overflow-y: auto;
}

.resize-handle {
  width: 10px;
  cursor: col-resize;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-handle:hover .resize-line {
  background-color: #409eff;
}

.resize-line {
  width: 2px;
  height: 30px;
  background-color: #dcdfe6;
  border-radius: 1px;
  transition: background-color 0.2s;
}

.bg-white {
  background-color: #fff;
}

.padding-sm {
  padding: 10px;
}

.margin-left-sm {
  margin-left: 10px;
}

.margin-top-sm {
  margin-top: 10px;
}

.margin-top {
  margin-top: 15px;
}

.ibox {
  margin-bottom: 15px;
}

.border-radius {
  border-radius: 4px;
}

.hc-td {
  padding: 5px;
  text-align: center;
}

.textAuto {
  text-align: center;
}

.float-right {
  float: right;
}

.tree-panel * {
  user-select: none;
}

.resizable-layout.resizing * {
  user-select: none !important;
}
</style>