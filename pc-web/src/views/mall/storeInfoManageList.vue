<template>
  <div class="store-info-manage-container">
    <!-- 查询条件 -->
    <el-card v-if="componentShow === 'storeInfoManage'" class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('storeInfoManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="conditions.name" :placeholder="$t('storeInfoManage.search.storeName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="conditions.convenienceMenusId" :placeholder="$t('storeInfoManage.search.convenienceType')"
            clearable>
            <el-option v-for="item in menuInfos" :key="item.convenienceMenusId" :label="item.name"
              :value="item.convenienceMenusId" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="conditions.isShow" :placeholder="$t('storeInfoManage.search.isShow')" clearable>
            <el-option :label="$t('storeInfoManage.common.yes')" value="Y" />
            <el-option :label="$t('storeInfoManage.common.no')" value="N" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleQuery">
            <i class="el-icon-search"></i>
            {{ $t('storeInfoManage.common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 商户信息列表 -->
    <el-card v-if="componentShow === 'storeInfoManage'" class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('storeInfoManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('storeInfoManage.common.add') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="storeInfos" border style="width: 100%">
        <el-table-column prop="storeInfoId" :label="$t('storeInfoManage.table.storeInfoId')" align="center" />
        <el-table-column prop="name" :label="$t('storeInfoManage.table.storeName')" align="center" />
        <el-table-column :label="$t('storeInfoManage.table.icon')" align="center">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px; border-radius: 5px;" :src="scope.row.icon"
              :preview-src-list="[scope.row.icon]" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="tel" :label="$t('storeInfoManage.table.tel')" align="center" />
        <el-table-column prop="site" :label="$t('storeInfoManage.table.site')" align="center" />
        <el-table-column prop="seq" :label="$t('storeInfoManage.table.seq')" align="center" />
        <el-table-column :label="$t('storeInfoManage.table.isShow')" align="center">
          <template slot-scope="scope">
            {{ scope.row.isShow === 'Y' ? $t('storeInfoManage.common.yes') : $t('storeInfoManage.common.no') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('storeInfoManage.common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('storeInfoManage.common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('storeInfoManage.common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-store-info v-if="componentShow === 'addStoreInfo'" ref="addStoreInfo" @success="handleSuccess"
      @close="handleClose" />
    <edit-store-info v-if="componentShow === 'editStoreInfo'" ref="editStoreInfo" :store-info="currentStoreInfo"
      @success="handleSuccess" @close="handleClose" />
    <delete-store-info ref="deleteStoreInfo" :store-info="currentStoreInfo" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryStoreInfo, queryConvenienceMenus } from '@/api/mall/storeInfoManageApi'
import AddStoreInfo from '@/components/mall/AddStoreInfo'
import EditStoreInfo from '@/components/mall/EditStoreInfo'
import DeleteStoreInfo from '@/components/mall/DeleteStoreInfo'

export default {
  name: 'StoreInfoManageList',
  components: {
    AddStoreInfo,
    EditStoreInfo,
    DeleteStoreInfo
  },
  data() {
    return {
      loading: false,
      componentShow: 'storeInfoManage',
      conditions: {
        name: '',
        isShow: '',
        convenienceMenusId: ''
      },
      storeInfos: [],
      menuInfos: [],
      currentStoreInfo: {},
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getStoreInfoList()
    this.getConvenienceMenusList()
  },
  methods: {
    async getStoreInfoList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.conditions
        }
        const { data, total } = await queryStoreInfo(params)
        this.storeInfos = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('storeInfoManage.error.fetchStoreInfoFailed'))
      } finally {
        this.loading = false
      }
    },
    async getConvenienceMenusList() {
      try {
        const params = {
          page: 1,
          row: 50
        }
        const { data } = await queryConvenienceMenus(params)
        this.menuInfos = data
      } catch (error) {
        this.$message.error(this.$t('storeInfoManage.error.fetchConvenienceMenusFailed'))
      }
    },
    handleQuery() {
      this.page.current = 1
      this.getStoreInfoList()
    },
    handleAdd() {
      this.componentShow = 'addStoreInfo'
      this.$nextTick(() => {
        this.$refs.addStoreInfo.open()
      })
    },
    handleEdit(row) {
      this.componentShow = 'editStoreInfo'
      this.currentStoreInfo = { ...row }
      this.$nextTick(() => {
        this.$refs.editStoreInfo.open()
      })
    },
    handleDelete(row) {
      this.currentStoreInfo = { ...row }
      this.$nextTick(() => {
        this.$refs.deleteStoreInfo.open()
      })
    },
    handleSuccess() {
      this.componentShow = 'storeInfoManage'
      this.getStoreInfoList()
    },
    handleClose() {
      this.componentShow = 'storeInfoManage'
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getStoreInfoList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getStoreInfoList()
    }
  }
}
</script>

<style lang="scss" scoped>
.store-info-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-select {
      width: 100%;
    }
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>