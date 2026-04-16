<template>
  <div class="discount-type-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('discountType.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.applyType" :placeholder="$t('discountType.search.applyType')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="searchForm.typeName" :placeholder="$t('discountType.search.typeName')" clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('discountType.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('common.add') }}
          </el-button>
          <el-button size="small" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="applyType" :label="$t('discountType.table.applyType')" align="center" />
        <el-table-column prop="typeName" :label="$t('discountType.table.typeName')" align="center" />
        <el-table-column prop="typeDesc" :label="$t('discountType.table.typeDesc')" align="center" />
        <el-table-column prop="createTime" :label="$t('discountType.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-apply-room-discount-type ref="addDialog" @success="handleSuccess" />
    <edit-apply-room-discount-type ref="editDialog" @success="handleSuccess" />
    <delete-apply-room-discount-type ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryApplyRoomDiscountType } from '@/api/fee/discountTypeApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddApplyRoomDiscountType from '@/components/fee/addApplyRoomDiscountType'
import EditApplyRoomDiscountType from '@/components/fee/editApplyRoomDiscountType'
import DeleteApplyRoomDiscountType from '@/components/fee/deleteApplyRoomDiscountType'

export default {
  name: 'DiscountTypeList',
  components: {
    AddApplyRoomDiscountType,
    EditApplyRoomDiscountType,
    DeleteApplyRoomDiscountType
  },
  data() {
    return {
      loading: false,
      searchForm: {
        applyType: '',
        typeName: '',
        communityId: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        this.searchForm.communityId = getCommunityId()
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await queryApplyRoomDiscountType(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('discountType.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.discount-type-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>