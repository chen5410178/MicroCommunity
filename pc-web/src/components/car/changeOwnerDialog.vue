<template>
  <el-dialog
    :title="$t('listOwnerCar.changeOwner')"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div>
      <el-form inline>
        <el-form-item :label="$t('listOwnerCar.ownerName')">
          <el-input
            v-model.trim="searchForm.ownerName"
            :placeholder="$t('listOwnerCar.inputOwnerName')"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            {{ $t('listOwnerCar.query') }}
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
        class="mt-10"
        :data="ownerList"
        border
        v-loading="loading"
        height="400px"
      >
        <el-table-column :label="$t('listOwnerCar.select')" width="80" align="center">
          <template slot-scope="scope">
            <el-radio v-model="selectedOwnerId" :label="scope.row.ownerId">
              &nbsp;
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('listOwnerCar.ownerName')" align="center" />
        <el-table-column prop="link" :label="$t('listOwnerCar.phoneNumber')" align="center" />
      </el-table>

      <el-pagination
        class="mt-10"
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('listOwnerCar.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('listOwnerCar.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { queryOwners, changeCarOwner } from '@/api/owner/ownerApi'

export default {
  name: 'ChangeOwnerDialog',
  data() {
    return {
      visible: false,
      currentCar: null,
      searchForm: {
        ownerName: ''
      },
      ownerList: [],
      loading: false,
      selectedOwnerId: '',
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(car) {
      this.currentCar = car
      this.visible = true
      this.selectedOwnerId = ''
      this.searchForm.ownerName = ''
      this.ownerList = []
      this.currentPage = 1
      this.total = 0
      this.handleQuery()
    },

    async handleQuery() {
      this.loading = true
      try {
        const params = {
          name: this.searchForm.ownerName || '',
          page: this.currentPage,
          row: this.pageSize,
          personRole: '', // 如需只查业主，可传 '1'
          roomName: '',
          link: '',
          idCard: '',
          personType: ''
        }
        const res = await queryOwners(params)
        // res.data 为后端返回的业主列表
        this.ownerList = res.data || []
        this.total = res.total || 0
      } catch (error) {
        console.error('查询业主列表失败:', error)
        this.$message.error(this.$t('listOwnerCar.changeOwnerQueryFailed'))
      } finally {
        this.loading = false
      }
    },

    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.handleQuery()
    },

    handleCurrentChange(val) {
      this.currentPage = val
      this.handleQuery()
    },

    async handleConfirm() {
      if (!this.selectedOwnerId) {
        this.$message.warning(this.$t('listOwnerCar.changeOwnerSelectTip'))
        return
      }
      try {
        const payload = {
          memberId: this.currentCar.memberId,
          newOwnerId: this.selectedOwnerId
        }
        await changeCarOwner(payload)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('修改业主失败:', error)
        this.$message.error(this.$t('listOwnerCar.changeOwnerFailed'))
      }
    },

    handleClose() {
      this.currentCar = null
      this.selectedOwnerId = ''
      this.searchForm.ownerName = ''
      this.ownerList = []
      this.currentPage = 1
      this.total = 0
    }
  }
}
</script>