<template>
  <div class="specification-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceStoreSpecificationManage.queryConditions') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="toggleMoreCondition">
          {{ moreCondition ? $t('resourceStoreSpecificationManage.hide') : $t('resourceStoreSpecificationManage.more') }}
        </el-button>
      </div>
      <el-form :model="conditions" label-width="0">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item>
              <el-select v-model="conditions.parentRstId"
                :placeholder="$t('resourceStoreSpecificationManage.selectItemType')" @change="handleParentTypeChange"
                style="width:100%">
                <el-option v-for="item in resourceStoreTypes" :key="item.rstId" :label="item.name" :value="item.rstId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-select v-model="conditions.rstId"
                :placeholder="$t('resourceStoreSpecificationManage.selectSubCategory')" style="width:100%">
                <el-option v-for="item in resourceStoreSonTypes" :key="item.rstId" :label="item.name" :value="item.rstId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-input v-model="conditions.specName"
                :placeholder="$t('resourceStoreSpecificationManage.specNamePlaceholder')">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="queryResourceStoreSpecification">
              <i class="el-icon-search"></i>
              {{ $t('resourceStoreSpecificationManage.query') }}
            </el-button>
            <el-button @click="resetResourceStoreSpecification">
              <i class="el-icon-refresh"></i>
              {{ $t('resourceStoreSpecificationManage.reset') }}
            </el-button>
          </el-col>
        </el-row>

        <el-row v-show="moreCondition" :gutter="20">
          <el-col :span="6">
            <el-form-item>
              <el-input v-model="conditions.rssId"
                :placeholder="$t('resourceStoreSpecificationManage.specIdPlaceholder')">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 规格列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceStoreSpecificationManage.itemSpecifications') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="openAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('resourceStoreSpecificationManage.add') }}
        </el-button>
      </div>

      <el-table :data="resourceStoreSpecifications" border v-loading="loading">
        <el-table-column prop="rssId" :label="$t('resourceStoreSpecificationManage.specId')" align="center">
        </el-table-column>
        <el-table-column :label="$t('resourceStoreSpecificationManage.typeName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName }}{{ scope.row.rstName ? ' > ' + scope.row.rstName : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="specName" :label="$t('resourceStoreSpecificationManage.specName')" align="center">
        </el-table-column>
        <el-table-column prop="description" :label="$t('resourceStoreSpecificationManage.description')" align="center">
        </el-table-column>
        <el-table-column :label="$t('resourceStoreSpecificationManage.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="openEditModal(scope.row)">
              {{ $t('resourceStoreSpecificationManage.edit') }}
            </el-button>
            <el-button type="danger" size="mini" @click="openDeleteModal(scope.row)">
              {{ $t('resourceStoreSpecificationManage.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper" :total="page.total">
      </el-pagination>
    </el-card>

    <!-- 子组件 -->
    <add-resource-store-specification ref="addModal" @success="handleSuccess"></add-resource-store-specification>
    <edit-resource-store-specification ref="editModal" @success="handleSuccess"></edit-resource-store-specification>
    <delete-resource-store-specification ref="deleteModal" @success="handleSuccess"></delete-resource-store-specification>
  </div>
</template>

<script>
import { listResourceStoreSpecifications, listResourceStoreTypes } from '@/api/resource/resourceStoreSpecificationManageApi'
import AddResourceStoreSpecification from '@/components/resource/addResourceStoreSpecification'
import EditResourceStoreSpecification from '@/components/resource/editResourceStoreSpecification'
import DeleteResourceStoreSpecification from '@/components/resource/deleteResourceStoreSpecification'

export default {
  name: 'ResourceStoreSpecificationManageList',
  data() {
    return {
      loading: false,
      moreCondition: false,
      conditions: {
        specName: '',
        parentRstId: '',
        rstId: '',
        rssId: ''
      },
      resourceStoreSpecifications: [],
      resourceStoreTypes: [],
      resourceStoreSonTypes: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  components: {
    AddResourceStoreSpecification,
    EditResourceStoreSpecification,
    DeleteResourceStoreSpecification
  },
  created() {
    this.getList()
    this.getResourceStoreTypes()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }

        const res = await listResourceStoreSpecifications(params)
        this.resourceStoreSpecifications = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('Failed to get specification list:', error)
        this.$message.error(this.$t('common.queryFailed'))
      } finally {
        this.loading = false
      }
    },

    async getResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          parentId: '0'
        }

        const res = await listResourceStoreTypes(params)
        this.resourceStoreTypes = res.data
      } catch (error) {
        console.error('Failed to get item types:', error)
      }
    },

    async getResourceStoreSonTypes(parentRstId) {
      try {
        const params = {
          page: 1,
          row: 100,
          rstId: parentRstId,
          flag: "0"
        }

        const res = await listResourceStoreTypes(params)
        this.resourceStoreSonTypes = res.data
      } catch (error) {
        console.error('Failed to get sub item types:', error)
      }
    },

    handleParentTypeChange(val) {
      this.conditions.rstId = ''
      if (!val) {
        this.resourceStoreSonTypes = []
        return
      }
      this.getResourceStoreSonTypes(val)
    },

    toggleMoreCondition() {
      this.moreCondition = !this.moreCondition
    },

    queryResourceStoreSpecification() {
      this.page.current = 1
      this.getList()
    },

    resetResourceStoreSpecification() {
      this.conditions = {
        specName: '',
        parentRstId: '',
        rstId: '',
        rssId: ''
      }
      this.resourceStoreSonTypes = []
      this.page.current = 1
      this.getList()
    },

    openAddModal() {
      this.$refs.addModal.open()
    },

    openEditModal(row) {
      this.$refs.editModal.open(row)
    },

    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    },

    handleSuccess() {
      this.getList()
    },

    handleSizeChange(size) {
      this.page.size = size
      this.getList()
    },

    handleCurrentChange(current) {
      this.page.current = current
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.specification-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>