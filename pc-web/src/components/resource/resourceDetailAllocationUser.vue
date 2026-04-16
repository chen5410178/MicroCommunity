<template>
  <div class="resource-detail-allocation-user">
    <el-table
      v-loading="loading"
      :data="allocationUserStorehouses"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="ausId"
        :label="$t('resourceDetail.allocationUser.id')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.allocationUser.itemType')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
        </template>
      </el-table-column>
      <el-table-column
        prop="resName"
        :label="$t('resourceDetail.allocationUser.itemName')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.allocationUser.itemSpec')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.specName || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="isFixedName"
        :label="$t('resourceDetail.allocationUser.fixedItem')"
        align="center"
      />
      <el-table-column
        prop="startUserName"
        :label="$t('resourceDetail.allocationUser.transferUser')"
        align="center"
      />
      <el-table-column
        prop="acceptUserName"
        :label="$t('resourceDetail.allocationUser.transferTarget')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.allocationUser.originalStock')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.stock }}{{ scope.row.unitCodeName }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('resourceDetail.allocationUser.transferQuantity')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.giveQuantity }}{{ scope.row.miniUnitCodeName }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        :label="$t('resourceDetail.allocationUser.createTime')"
        align="center"
      />
      <el-table-column
        prop="remark"
        :label="$t('resourceDetail.allocationUser.remark')"
        align="center"
      />
    </el-table>

    <el-pagination
      :current-page.sync="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { getAllocationUserStorehouseList } from '@/api/resource/resourceDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceDetailAllocationUser',
  data() {
    return {
      loading: false,
      allocationUserStorehouses: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      resId: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    async loadData(params) {
      this.resId = params.resId
      await this.getList()
    },
    async getList() {
      try {
        this.loading = true
        const { data, total } = await getAllocationUserStorehouseList({
          resId: this.resId,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.allocationUserStorehouses = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load allocation user details:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-detail-allocation-user {
  padding: 20px;
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>