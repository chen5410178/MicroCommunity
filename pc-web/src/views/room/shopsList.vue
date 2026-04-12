<template>
  <div class="shops-container">
    <div class="bg-white padding-sm text-left">
      <el-button type="primary" size="small" @click="handleAddLocation">
        <i class="el-icon-plus"></i>{{ $t('shops.addLocation') }}
      </el-button>
      <el-button type="primary" size="small" @click="handleEditLocation" v-if="selectedLocation.locationId">
        <i class="el-icon-edit"></i>{{ $t('shops.editLocation') }}
      </el-button>
      <el-button type="primary" size="small" @click="handleDeleteLocation" v-if="selectedLocation.locationId">
        <i class="el-icon-close"></i>{{ $t('shops.deleteLocation') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="handleAddChildLocation" v-if="selectedLocation.locationId">
        <i class="el-icon-plus"></i>{{ $t('shops.addChildLocation') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="handleAddShop" v-if="selectedLocation.locationId">
        <i class="el-icon-plus"></i>{{ $t('shops.addShop') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openImportShop">
        <i class="el-icon-upload2"></i>{{ $t('shops.importShop.button') }}
      </el-button>
    </div>

    <div class="resizable-layout margin-top-sm">
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <shop-location-tree ref="locationTree" @select="onLocationSelect"></shop-location-tree>
      </div>

      <div class="resize-handle" @mousedown="startResize">
        <div class="resize-line"></div>
      </div>

      <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
        <el-card class="ibox">
          <div slot="header" class="flex justify-content">{{ $t('shops.queryCondition') }}</div>
          <el-form :inline="true" class="text-left">
            <el-form-item>
              <el-input v-model.trim="queryParams.shopNum" :placeholder="$t('shops.shopNumberPlaceholder')" size="small" clearable @keyup.enter.native="handleQuery"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryParams.state" :placeholder="$t('shops.statePlaceholder')" clearable size="small" style="width: 100%">
                <el-option :label="$t('shops.idle')" value="2008"></el-option>
                <el-option :label="$t('shops.rented')" value="2006"></el-option>
                <el-option :label="$t('shops.sold')" value="2007"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input v-model.trim="queryParams.ownerName" :placeholder="$t('shops.ownerNamePlaceholder')" size="small" clearable @keyup.enter.native="handleQuery"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model.trim="queryParams.link" :placeholder="$t('shops.linkPlaceholder')" size="small" clearable @keyup.enter.native="handleQuery"></el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="queryParams.rentTime"
                type="daterange"
                range-separator="~"
                :start-placeholder="$t('shops.rentStartPlaceholder')"
                :end-placeholder="$t('shops.rentEndPlaceholder')"
                size="small"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 240px"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="handleQuery">{{ $t('common.search') }}</el-button>
              <el-button size="small" @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="margin-top">
          <el-table :data="shopsData" border stripe v-loading="loading">
            <el-table-column prop="shopNum" :label="$t('shops.shopNumber')" width="120" fixed="left" align="center">
              <template slot-scope="scope">
                <div class="hc-td">
                  <a target="_blank" :href="'/#/views/shop/shopDetail?shopId=' + scope.row.shopId">{{ scope.row.shopNum }}</a>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="locationName" :label="$t('shops.location')"  align="center">
              <template slot-scope="scope"><div class="hc-td">{{ scope.row.locationName }}</div></template>
            </el-table-column>
            <el-table-column :label="$t('shops.owner')" align="center">
              <template slot-scope="scope">
                <div v-if="scope.row.ownerName" class="hc-td">
                  <a target="_blank" :href="'/#/views/owner/ownerDetail?ownerId=' + scope.row.ownerId">{{ scope.row.ownerName }}</a>
                </div>
                <div v-else class="hc-td textAuto">-</div>
              </template>
            </el-table-column>
            <el-table-column prop="link" :label="$t('shops.contactNumber')"  align="center">
              <template slot-scope="scope"><div class="hc-td">{{ scope.row.link || '-' }}</div></template>
            </el-table-column>
            <el-table-column :label="$t('shops.builtUpArea')" width="120" align="center">
              <template slot-scope="scope"><div class="hc-td">{{ scope.row.builtUpArea }}{{ $t('shops.squareMeter') }}</div></template>
            </el-table-column>
            <el-table-column :label="$t('shops.shopArea')" width="120" align="center">
              <template slot-scope="scope"><div class="hc-td">{{ scope.row.shopArea || '-' }}{{ scope.row.shopArea ? $t('shops.squareMeter') : '' }}</div></template>
            </el-table-column>
            <el-table-column prop="shopRent" :label="$t('shops.rent')" width="100" align="center">
              <template slot-scope="scope"><div class="hc-td">{{ scope.row.shopRent || '-' }}</div></template>
            </el-table-column>
            <el-table-column :label="$t('shops.leasePeriod')" width="180" align="center">
              <template slot-scope="scope">
                <div v-if="scope.row.startTime || scope.row.endTime" class="hc-td">
                  {{ scope.row.startTime || '-' }} ~ {{ scope.row.endTime || '-' }}
                </div>
                <div v-else class="hc-td textAuto">-</div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('shops.rentTime')" width="160" align="center">
              <template slot-scope="scope"><div class="hc-td">{{ scope.row.createTime || '-' }}</div></template>
            </el-table-column>
            <el-table-column :label="$t('shops.state')" width="100" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.state === '2008'" type="success" size="mini">{{ $t('shops.idle') }}</el-tag>
                <el-tag v-else-if="scope.row.state === '2006'" type="warning" size="mini">{{ $t('shops.rented') }}</el-tag>
                <el-tag v-else-if="scope.row.state === '2007'" type="info" size="mini">{{ $t('shops.sold') }}</el-tag>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('shops.operation')" fixed="right" width="240" align="center">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state === '2008'" size="mini" type="primary" @click="toShopBindOwner(scope.row)">{{ $t('shops.handOver') }}</el-button>
                <el-button v-if="scope.row.state !== '2008'" size="mini" type="warning" @click="toShopUnbindOwner(scope.row)">{{ $t('shops.checkOut') }}</el-button>
                <el-button size="mini" type="primary" @click="handleEditShop(scope.row)">{{ $t('shops.edit') }}</el-button>
                <el-button size="mini" type="danger" @click="handleDeleteShop(scope.row)">{{ $t('shops.delete') }}</el-button>
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
        </el-card>
      </div>
    </div>

    <add-location ref="addLocationRef" parent-id="-1" :parent-name="$t('shops.topLevel')" @success="onLocationSuccess" />
    <add-location ref="addChildLocationRef" :parent-id="selectedLocation.locationId" :parent-name="selectedLocation.locationName" @success="onLocationSuccess" />
    <edit-location ref="editLocationRef" :parent-name="parentLocationName" @success="onLocationSuccess" />
    <del-location ref="delLocationRef" @success="onLocationSuccess" />
    <add-shop ref="addShopRef" :location-id="selectedLocation.locationId" :location-name="selectedLocation.locationName" @success="loadData" />
    <edit-shop ref="editShopRef" @success="loadData" />
    <del-shop ref="delShopRef" @success="loadData" />
    <import-shop ref="importShop" />
  </div>
</template>

<script>
import { listBuildingShops } from '@/api/room/buildingShopApi'
import ShopLocationTree from '@/components/room/ShopLocationTree'
import AddLocation from '@/components/room/AddLocation'
import EditLocation from '@/components/room/EditLocation'
import DelLocation from '@/components/room/DelLocation'
import AddShop from '@/components/room/AddShop'
import EditShop from '@/components/room/EditShop'
import DelShop from '@/components/room/DelShop'
import ImportShop from '@/components/room/importShop'

export default {
  name: 'ShopsList',
  components: {
    ShopLocationTree,
    AddLocation,
    EditLocation,
    DelLocation,
    AddShop,
    EditShop,
    DelShop,
    ImportShop
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
      selectedLocation: { locationId: '', locationName: '', parentId: '', seq: 0, remark: '' },
      queryParams: { shopNum: '', state: '', ownerName: '', link: '', rentTime: '', locationId: '', page: 1, row: 10 },
      shopsData: [],
      pagination: { current: 1, size: 10, total: 0 }
    }
  },
  computed: {
    parentLocationName() {
      return (!this.selectedLocation.parentId || this.selectedLocation.parentId === '-1') ? this.$t('shops.topLevel') : this.selectedLocation.locationName
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
  },
  methods: {
    loadData() {
      this.loading = true
      listBuildingShops(this.queryParams)
        .then(res => {
          const data = res.data
          this.shopsData = Array.isArray(data) ? data : (data && data.data) || []
          this.pagination.total = (data && data.total) || this.shopsData.length
        })
        .catch(console.error)
        .finally(() => { this.loading = false })
    },
    onLocationSelect(data) {
      this.selectedLocation = { locationId: data.locationId || '', locationName: data.locationName || '', parentId: data.parentId || '-1', seq: data.seq, remark: data.remark }
      this.queryParams.locationId = data.locationId || ''
      this.loadData()
    },
    handleQuery() { this.queryParams.page = 1; this.loadData() },
    handleReset() { this.queryParams = { shopNum: '', state: '', ownerName: '', link: '', rentTime: '', locationId: '', page: 1, row: 10 }; this.loadData() },
    handleSizeChange(size) { this.queryParams.row = size; this.loadData() },
    handleCurrentChange(page) { this.queryParams.page = page; this.loadData() },
    handleAddLocation() { this.$refs.addLocationRef.open() },
    handleEditLocation() {
      if (!this.selectedLocation.locationId) { this.$message.warning(this.$t('shops.pleaseSelectLocation')); return }
      this.$refs.editLocationRef.open(this.selectedLocation)
    },
    handleDeleteLocation() {
      if (!this.selectedLocation.locationId) { this.$message.warning(this.$t('shops.pleaseSelectLocation')); return }
      this.$refs.delLocationRef.open(this.selectedLocation)
    },
    handleAddChildLocation() {
      if (!this.selectedLocation.locationId) { this.$message.warning(this.$t('shops.pleaseSelectLocation')); return }
      this.$refs.addChildLocationRef.open()
    },
    onLocationSuccess() {
      if (this.$refs.locationTree) this.$refs.locationTree.loadData()
      this.selectedLocation = { locationId: '', locationName: '', parentId: '', seq: 0, remark: '' }
      this.loadData()
    },
    handleAddShop() {
      var locationPath = ''
      if (this.selectedLocation.locationId && this.$refs.locationTree) {
        var node = this.$refs.locationTree.findNode(this.$refs.locationTree.treeData, this.selectedLocation.locationId)
        if (node) {
          locationPath = this.getFullPath(this.$refs.locationTree.treeData, node)
        }
      }
      this.$refs.addShopRef.open(locationPath)
    },
    getFullPath(nodes, targetNode, parentPath) {
      parentPath = parentPath || ''
      for (var i = 0; i < nodes.length; i++) {
        var currentPath = parentPath ? parentPath + '-' + nodes[i].locationName : nodes[i].locationName
        if (nodes[i].locationId === targetNode.locationId) {
          return currentPath
        }
        if (nodes[i].children && nodes[i].children.length > 0) {
          var found = this.getFullPath(nodes[i].children, targetNode, currentPath)
          if (found) {
            return found
          }
        }
      }
      return ''
    },
    handleEditShop(row) { this.$refs.editShopRef.open(row) },
    handleDeleteShop(row) { this.$refs.delShopRef.open(row) },
    toShopBindOwner(row) {
      this.$router.push({
        path: '/views/room/shopBindOwner',
        query: {
          shopId: row.shopId,
          shopNum: row.shopNum
        }
      })
    },
    toShopUnbindOwner(row) {
      this.$router.push({
        path: '/views/room/shopUnbindOwner',
        query: {
          shopId: row.shopId,
          shopNum: row.shopNum,
          ownerId: row.ownerId,
          ownerName: row.ownerName,
          link: row.link,
          state: row.state
        }
      })
    },
    openImportShop() { this.$refs.importShop.open() },
    restoreTreeWidth() {
      const saved = localStorage.getItem('shopsListTreeWidth')
      if (saved) { const w = parseInt(saved); if (w >= this.minTreeWidth && w <= this.maxTreeWidth) this.treeWidth = w }
    },
    saveTreeWidth() { localStorage.setItem('shopsListTreeWidth', this.treeWidth.toString()) },
    startResize(e) { e.preventDefault(); this.isResizing = true; this.startX = e.clientX; this.startWidth = this.treeWidth; document.body.style.cursor = 'col-resize'; document.body.style.userSelect = 'none' },
    onMouseMove(e) {
      if (!this.isResizing) return
      const newWidth = this.startWidth + (e.clientX - this.startX)
      if (newWidth >= this.minTreeWidth && newWidth <= this.maxTreeWidth) this.treeWidth = newWidth
    },
    onMouseUp() {
      if (this.isResizing) { this.isResizing = false; this.saveTreeWidth(); document.body.style.cursor = ''; document.body.style.userSelect = '' }
    }
  }
}
</script>

<style scoped>
.shops-container { padding: 15px; height: calc(100vh - 120px); overflow: hidden; }
.resizable-layout { display: flex; height: calc(100% - 50px); }
.tree-panel { background: #fff; border-radius: 4px; padding: 10px; overflow-y: auto; border: 1px solid #e4e7ed; min-height: 450px; }
.content-panel { flex: 1; overflow-y: auto; margin-left: 10px; }
.resize-handle { width: 10px; cursor: col-resize; display: flex; align-items: center; justify-content: center; }
.resize-handle:hover .resize-line { background-color: #409eff; }
.resize-line { width: 2px; height: 30px; background-color: #dcdfe6; border-radius: 1px; }
.margin-top-sm { margin-top: 10px; }
.margin-top { margin-top: 15px; }
.bg-white { background-color: #fff; }
.padding-sm { padding: 10px; }
.text-left { text-align: left; }
.margin-left-sm { margin-left: 10px; }
.hc-td { padding: 5px; text-align: center; }
.textAuto { text-align: center; }
.float-right { float: right; }
.tree-panel * { user-select: none; }
</style>
