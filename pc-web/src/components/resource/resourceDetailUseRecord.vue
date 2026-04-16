<template>
  <div class="resource-detail-use-record">
    <el-table
      v-loading="loading"
      :data="resourceStoreUseRecords"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="rsurId"
        :label="$t('resourceDetail.useRecord.id')"
        align="center"
      />
      <el-table-column
        prop="repairId"
        :label="$t('resourceDetail.useRecord.repairId')"
        align="center"
      />
      <el-table-column
        prop="resId"
        :label="$t('resourceDetail.useRecord.itemId')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.useRecord.itemType')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('resourceDetail.useRecord.itemName')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.resourceStoreName || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('resourceDetail.useRecord.itemSpec')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.specName || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="isFixedName"
        :label="$t('resourceDetail.useRecord.fixedItem')"
        align="center"
      />
      <el-table-column
        prop="stateName"
        :label="$t('resourceDetail.useRecord.useType')"
        align="center"
      />
      <el-table-column
        :label="$t('resourceDetail.useRecord.useQuantity')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.quantity }}{{ scope.row.miniUnitCodeName }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('resourceDetail.useRecord.itemPrice')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.unitPrice || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createUserId"
        :label="$t('resourceDetail.useRecord.userId')"
        align="center"
      />
      <el-table-column
        prop="createUserName"
        :label="$t('resourceDetail.useRecord.userName')"
        align="center"
      />
      <el-table-column
        prop="createTime"
        :label="$t('resourceDetail.useRecord.createTime')"
        align="center"
      />
      <el-table-column
        prop="remark"
        :label="$t('resourceDetail.useRecord.remark')"
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
import { getResourceStoreUseRecordList } from '@/api/resource/resourceDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceDetailUseRecord',
  data() {
    return {
      loading: false,
      resourceStoreUseRecords: [],
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
        const { data, total } = await getResourceStoreUseRecordList({
          resId: this.resId,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.resourceStoreUseRecords = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load use record details:', error)
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
.resource-detail-use-record {
  padding: 20px;
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>