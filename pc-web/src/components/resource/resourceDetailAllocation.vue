<template>
  <div class="resource-detail-allocation">
    <el-table
      v-loading="loading"
      :data="resourceStores"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="applyId"
        :label="$t('resourceDetail.allocation.allocationId')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.allocation.itemType')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
        </template>
      </el-table-column>
      <el-table-column
        prop="resName"
        :label="$t('resourceDetail.allocation.itemName')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.allocation.itemSpec')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.specName || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="isFixedName"
        :label="$t('resourceDetail.allocation.fixedItem')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.allocation.originalStock')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('resourceDetail.allocation.allocationQuantity')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.stock }}{{ scope.row.applyType == 20000 ? scope.row.miniUnitCodeName : scope.row.unitCodeName }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('resourceDetail.allocation.sourceWarehouse')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.applyType == '20000' ? scope.row.startUserName : scope.row.shaName }}
        </template>
      </el-table-column>
      <el-table-column
        prop="shzName"
        :label="$t('resourceDetail.allocation.targetWarehouse')"
        align="center"
      />
      <el-table-column
        prop="startUserName"
        :label="$t('resourceDetail.allocation.applicant')"
        align="center"
      />
      <el-table-column
        prop="remark"
        :label="$t('resourceDetail.allocation.remark')"
        align="center"
      />
      <el-table-column
        prop="applyTypeName"
        :label="$t('resourceDetail.allocation.allocationType')"
        align="center"
      />
      <el-table-column
        prop="stateName"
        :label="$t('resourceDetail.allocation.status')"
        align="center"
      />
      <el-table-column
        prop="createTime"
        :label="$t('resourceDetail.allocation.createTime')"
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
import { getAllocationStorehouseList } from '@/api/resource/resourceDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceDetailAllocation',
  data() {
    return {
      loading: false,
      resourceStores: [],
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
        const { data, total } = await getAllocationStorehouseList({
          resId: this.resId,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.resourceStores = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load allocation details:', error)
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
.resource-detail-allocation {
  padding: 20px;
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>