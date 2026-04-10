<template>
  <div class="store-info-manage-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('storeInfoManage.basicInfo') }}</span>
          </div>
          <el-table :data="listStoreManageInfo.listStores" border style="width: 100%" v-loading="loading">
            <el-table-column prop="name" :label="$t('storeInfoManage.storeName')" align="center" />
            <el-table-column prop="address" :label="$t('storeInfoManage.storeAddress')" align="center" />
            <el-table-column prop="tel" :label="$t('storeInfoManage.contactNumber')" align="center" />
            <el-table-column prop="storeTypeName" :label="$t('storeInfoManage.storeType')" align="center" />
            <el-table-column prop="artificialPerson" :label="$t('storeInfoManage.legalPerson')" align="center" />
            <el-table-column prop="nearByLandmarks" :label="$t('storeInfoManage.nearbyLandmark')" align="center" />
            <el-table-column :label="$t('storeInfoManage.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="_openEditStoreInfoModel(scope.row)">
                  {{ $t('storeInfoManage.edit') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('storeInfoManage.storeInfo') }}</span>
      </div>
      <el-table :data="listStoreManageInfo.storeAttrDtoList" border style="width: 100%">
        <el-table-column prop="name" :label="$t('storeInfoManage.attributeName')" align="center" />
        <el-table-column prop="value" :label="$t('storeInfoManage.attributeValue')" align="center" />
        <el-table-column :label="$t('storeInfoManage.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_openEditStoreAttrModel(scope.row)">
              {{ $t('storeInfoManage.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <edit-store-info ref="editStoreInfo" @success="handleSuccess" />
    <edit-store-attr ref="editStoreAttr" @success="handleSuccess" />
  </div>
</template>

<script>
import { listStores } from '@/api/system/storeInfoManageApi'
import EditStoreInfo from '@/components/system/editStoreInfo'
import EditStoreAttr from '@/components/system/editStoreAttr'

export default {
  name: 'StoreInfoManageList',
  components: {
    EditStoreInfo,
    EditStoreAttr
  },
  data() {
    return {
      loading: false,
      listStoreManageInfo: {
        listStores: [],
        storeAttrDtoList: [],
        conditions: {
          name: '',
          storeTypeCd: '',
          tel: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._listListStores()
  },
  methods: {
    async _listListStores() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.listStoreManageInfo.conditions
        }
        const { data, total } = await listStores(params)
        this.listStoreManageInfo.listStores = data
        this.listStoreManageInfo.storeAttrDtoList = data[0].storeAttrDtoList || []
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('storeInfoManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openEditStoreInfoModel(storeInfo) {
      this.$refs.editStoreInfo.open(storeInfo)
    },
    _openEditStoreAttrModel(storeAttr) {
      this.$refs.editStoreAttr.open(storeAttr)
    },
    handleSuccess() {
      this._listListStores()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listListStores()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listListStores()
    }
  }
}
</script>

<style lang="scss" scoped>
.store-info-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 15px;
    text-align: right;
  }
}
</style>