<template>
  <div class="supplier-manage-container">
    <el-card class="search-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('supplierManage.searchCondition') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="toggleMoreCondition">
          {{ moreCondition ? $t('supplierManage.hide') : $t('supplierManage.moreCondition') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="conditions.stId"
            :placeholder="$t('supplierManage.pleaseSelect') + $t('supplierManage.supplierType')" clearable class="w-100">
            <el-option v-for="item in supplierTypes" :key="item.stId" :label="item.typeName" :value="item.stId" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="conditions.supplierName"
            :placeholder="$t('supplierManage.pleaseSelect') + $t('supplierManage.supplierName')" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="conditions.personName"
            :placeholder="$t('supplierManage.pleaseSelect') + $t('supplierManage.contactPerson')" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="querySupplierMethod">{{ $t('supplierManage.query') }}</el-button>
        </el-col>
      </el-row>

      <el-row v-if="moreCondition" :gutter="20" class="mt-10">
        <el-col :span="6">
          <el-input v-model="conditions.personTel"
            :placeholder="$t('supplierManage.pleaseSelect') + $t('supplierManage.contactTel')" />
        </el-col>
      </el-row>
    </el-card>

    <el-card class="mt-20">
      <div slot="header" class="clearfix  flex justify-between">
        <span>{{ $t('supplierManage.supplierInfo') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="openAddSupplierModal">
          <i class="el-icon-plus"></i>
          {{ $t('supplierManage.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="suppliers" border style="width: 100%">
        <el-table-column prop="typeName" :label="$t('supplierManage.supplierType')" align="center" />
        <el-table-column prop="supplierName" :label="$t('supplierManage.supplierName')" align="center" />
        <el-table-column prop="personName" :label="$t('supplierManage.contactPerson')" align="center" />
        <el-table-column prop="personTel" :label="$t('supplierManage.contactTel')" align="center" />
        <el-table-column prop="startTime" :label="$t('supplierManage.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('supplierManage.endTime')" align="center" />
        <el-table-column prop="createTime" :label="$t('supplierManage.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('supplierManage.remark')" align="center" />
        <el-table-column :label="$t('supplierManage.operation')" align="center" width="220">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditSupplierModel(scope.row)">{{ $t('supplierManage.edit') }}</el-button>
            <el-button size="mini" type="primary" @click="openConfigSupplierModel(scope.row)">{{
              $t('supplierManage.config') }}</el-button>
            <el-button size="mini" type="danger" @click="openDeleteSupplierModel(scope.row)">{{
              $t('supplierManage.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="mt-20" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper" :total="page.total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-supplier ref="addSupplier" @success="listSuppliers" />
    <edit-supplier ref="editSupplier" @success="listSuppliers" />
    <delete-supplier ref="deleteSupplier" @success="listSuppliers" />
    <config-supplier ref="configSupplier" />
  </div>
</template>

<script>
import { listSupplier, listSupplierType } from '@/api/scm/supplierManageApi'
import AddSupplier from '@/components/scm/AddSupplier'
import EditSupplier from '@/components/scm/EditSupplier'
import DeleteSupplier from '@/components/scm/DeleteSupplier'
import ConfigSupplier from '@/components/scm/ConfigSupplier'

export default {
  name: 'SupplierManageList',
  components: {
    AddSupplier,
    EditSupplier,
    DeleteSupplier,
    ConfigSupplier
  },
  data() {
    return {
      loading: false,
      moreCondition: false,
      conditions: {
        stId: '',
        supplierName: '',
        personName: '',
        personTel: ''
      },
      suppliers: [],
      supplierTypes: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.listSuppliers()
    this.listSupplierTypes()
  },
  methods: {
    async listSuppliers() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.conditions
        }
        const res = await listSupplier(params)
        this.suppliers = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('查询供应商列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    async listSupplierTypes() {
      try {
        const res = await listSupplierType({ page: 1, row: 100 })
        this.supplierTypes = res.data
      } catch (error) {
        console.error('查询供应商类型失败:', error)
      }
    },

    querySupplierMethod() {
      this.page.current = 1
      this.listSuppliers()
    },

    toggleMoreCondition() {
      this.moreCondition = !this.moreCondition
    },

    openAddSupplierModal() {
      this.$refs.addSupplier.open()
    },

    openEditSupplierModel(row) {
      this.$refs.editSupplier.open(row)
    },

    openConfigSupplierModel(row) {
      this.$refs.configSupplier.open(row)
    },

    openDeleteSupplierModel(row) {
      this.$refs.deleteSupplier.open(row)
    },

    handleSizeChange(size) {
      this.page.size = size
      this.listSuppliers()
    },

    handleCurrentChange(current) {
      this.page.current = current
      this.listSuppliers()
    }
  }
}
</script>

<style lang="scss" scoped>
.supplier-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
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
}
</style>