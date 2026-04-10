<template>
  <div class="fee-print-spec-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feePrintSpecManage.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="openAddDialog">
          <i class="el-icon-plus"></i>
          {{ $t('feePrintSpecManage.add') }}
        </el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="qrImg" :label="$t('feePrintSpecManage.image')" align="center">
          <template slot-scope="scope">
            <el-image v-if="scope.row.qrImg" style="width: 100px; height: 100px; border-radius: 5px;"
              :src="scope.row.qrImg" :preview-src-list="[scope.row.qrImg]">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="printName" :label="$t('feePrintSpecManage.name')" align="center">
        </el-table-column>
        <el-table-column prop="specCd" :label="$t('feePrintSpecManage.spec')" align="center">
          <template slot-scope="scope">
            {{ scope.row.specCd === '1010' ? $t('feePrintSpecManage.reminderPrint') :
              $t('feePrintSpecManage.receiptPrint') }}
          </template>
        </el-table-column>
        <el-table-column prop="printId" :label="$t('feePrintSpecManage.code')" align="center">
        </el-table-column>
        <el-table-column :label="$t('feePrintSpecManage.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditDialog(scope.row)">
              {{ $t('feePrintSpecManage.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
              {{ $t('feePrintSpecManage.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
      </el-pagination>
    </el-card>

    <add-fee-print-spec ref="addDialog" @success="fetchData"></add-fee-print-spec>
    <edit-fee-print-spec ref="editDialog" @success="fetchData"></edit-fee-print-spec>
    <delete-fee-print-spec ref="deleteDialog" @success="fetchData"></delete-fee-print-spec>
  </div>
</template>

<script>
import { queryFeePrintSpec } from '@/api/system/feePrintSpecManageApi'
import AddFeePrintSpec from '@/components/system/addFeePrintSpec'
import EditFeePrintSpec from '@/components/system/editFeePrintSpec'
import DeleteFeePrintSpec from '@/components/system/deleteFeePrintSpec'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeePrintSpecManageList',
  components: {
    AddFeePrintSpec,
    EditFeePrintSpec,
    DeleteFeePrintSpec
  },
  data() {
    return {
      loading: false,
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      queryParams: {
        communityId: '',
        specCd: ''
      }
    }
  },
  created() {
    this.queryParams.communityId = getCommunityId()
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.queryParams
        }
        const { data, total } = await queryFeePrintSpec(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('feePrintSpecManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },
    openAddDialog() {
      this.$refs.addDialog.open()
    },
    openEditDialog(row) {
      this.$refs.editDialog.open(row)
    },
    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-print-spec-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .float-right {
    float: right;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>