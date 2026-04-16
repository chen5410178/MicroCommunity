<template>
  <div class="supplier-coupon-container">


      <el-row :gutter="20">
        <el-col :span="4">
          <el-card class="supplier-list">
            <ul class="supplier-ul">
              <li v-for="(supplier, index) in suppliers" :key="index"
                :class="{ 'active': supplier.supplierId === curSupplier.supplierId }" @click="switchSupplier(supplier)">
                {{ supplier.supplierName }}
              </li>
            </ul>
          </el-card>
        </el-col>

        <el-col :span="20">
          <el-card>
            <div slot="header" class="text-left">
              <span>{{ $t('supplierCoupon.searchConditions') }}</span>
            </div>

            <el-form :inline="true" :model="conditions" class="demo-form-inline text-left">
              <el-form-item :label="$t('supplierCoupon.name')">
                <el-input v-model="conditions.name" :placeholder="$t('supplierCoupon.name')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('supplierCoupon.businessId')">
                <el-input v-model="conditions.businessKey" :placeholder="$t('supplierCoupon.businessId')"></el-input>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="querySupplierCoupon">
                  {{ $t('supplierCoupon.search') }}
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <el-card class="coupon-list">
            <div slot="header" class="flex justify-between">
              <span>{{ $t('supplierCoupon.couponInfo') }}</span>
              <el-button type="primary" size="small" style="float: right;" @click="openAddDialog">
                {{ $t('supplierCoupon.add') }}
              </el-button>
            </div>

            <el-table :data="supplierCoupons" style="width: 100%" v-loading="loading">
              <el-table-column prop="couponId" :label="$t('supplierCoupon.name')" align="center" />
              <el-table-column prop="name" :label="$t('supplierCoupon.name')" align="center" />
              <el-table-column prop="supplierName" :label="$t('supplierCoupon.supplier')" align="center" />
              <el-table-column prop="businessKey" :label="$t('supplierCoupon.thirdPartyId')" align="center" />
              <el-table-column prop="valuePrice" :label="$t('supplierCoupon.price')" align="center" />
              <el-table-column prop="remark" :label="$t('supplierCoupon.remark')" align="center" />
              <el-table-column :label="$t('supplierCoupon.operation')" align="center">
                <template slot-scope="scope">
                  <el-button size="mini" @click="openEditDialog(scope.row)">
                    {{ $t('supplierCoupon.edit') }}
                  </el-button>
                  <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
                    {{ $t('supplierCoupon.delete') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
              layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px;">
            </el-pagination>
          </el-card>
        </el-col>
      </el-row>

    <add-supplier-coupon ref="addDialog" @success="handleAddSuccess" />
    <edit-supplier-coupon ref="editDialog" @success="handleEditSuccess" />
    <delete-supplier-coupon ref="deleteDialog" @success="handleDeleteSuccess" />
  </div>
</template>

<script>
import { getSupplierList, getSupplierCouponList } from '@/api/scm/supplierCouponApi'
import AddSupplierCoupon from '@/components/scm/addSupplierCoupon'
import EditSupplierCoupon from '@/components/scm/editSupplierCoupon'
import DeleteSupplierCoupon from '@/components/scm/deleteSupplierCoupon'

export default {
  name: 'SupplierCouponList',
  components: {
    AddSupplierCoupon,
    EditSupplierCoupon,
    DeleteSupplierCoupon
  },
  data() {
    return {
      suppliers: [],
      curSupplier: {},
      supplierCoupons: [],
      loading: false,
      conditions: {
        name: '',
        businessKey: '',
        supplierId: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.getSupplierList()
  },
  methods: {
    async getSupplierList() {
      try {
        const res = await getSupplierList({
          page: 1,
          row: 100
        })
        this.suppliers = res.data
        if (this.suppliers.length > 0) {
          this.switchSupplier(this.suppliers[0])
        }
      } catch (error) {
        this.$message.error(this.$t('supplierCoupon.fetchError'))
      }
    },

    async getSupplierCouponList() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const res = await getSupplierCouponList(params)
        this.supplierCoupons = res.data
        this.pagination.total = res.total
      } catch (error) {
        this.$message.error(this.$t('supplierCoupon.fetchError'))
      } finally {
        this.loading = false
      }
    },

    switchSupplier(supplier) {
      this.curSupplier = supplier
      this.conditions.supplierId = supplier.supplierId
      this.pagination.current = 1
      this.getSupplierCouponList()
    },

    querySupplierCoupon() {
      this.pagination.current = 1
      this.getSupplierCouponList()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.getSupplierCouponList()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.getSupplierCouponList()
    },

    openAddDialog() {
      if (!this.curSupplier.supplierId) {
        this.$message.warning(this.$t('supplierCoupon.supplierRequired'))
        return
      }
      this.$refs.addDialog.open({
        supplierId: this.curSupplier.supplierId
      })
    },

    openEditDialog(row) {
      this.$refs.editDialog.open(row)
    },

    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },

    handleAddSuccess() {
      this.getSupplierCouponList()
    },

    handleEditSuccess() {
      this.getSupplierCouponList()
    },

    handleDeleteSuccess() {
      this.getSupplierCouponList()
    }
  }
}
</script>

<style scoped>
.supplier-coupon-container {
  padding: 20px;
}

.supplier-list {
  height: calc(100vh - 200px);
  overflow-y: auto;
}

.supplier-ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.supplier-ul li {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.supplier-ul li:hover {
  background-color: #f5f7fa;
}

.supplier-ul li.active {
  background-color: #409eff;
  color: white;
}

.coupon-list {
  margin-top: 20px;
}

.demo-form-inline {
  margin-bottom: 0;
}
</style>