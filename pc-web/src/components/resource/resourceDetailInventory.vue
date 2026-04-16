<template>
  <div class="resource-detail-inventory">
    <el-table
      v-loading="loading"
      :data="assetInventorys"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="aiId"
        :label="$t('resourceDetail.inventory.inventoryId')"
        align="center"
      />
      <el-table-column
        prop="name"
        :label="$t('resourceDetail.inventory.inventoryName')"
        align="center"
      />
      <el-table-column
        prop="invTime"
        :label="$t('resourceDetail.inventory.inventoryTime')"
        align="center"
      />
      <el-table-column
        prop="shName"
        :label="$t('resourceDetail.inventory.warehouse')"
        align="center"
      />
      <el-table-column
        prop="staffName"
        :label="$t('resourceDetail.inventory.inventoryPerson')"
        align="center"
      />
      <el-table-column
        prop="stateName"
        :label="$t('resourceDetail.inventory.status')"
        align="center"
      />
      <el-table-column
        prop="createTime"
        :label="$t('resourceDetail.inventory.createTime')"
        align="center"
      />
      <el-table-column
        :label="$t('common.operation')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="openDetail(scope.row)"
          >
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
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
import { getAssetInventoryList } from '@/api/resource/resourceDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceDetailInventory',
  data() {
    return {
      loading: false,
      assetInventorys: [],
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
        const { data, total } = await getAssetInventoryList({
          resId: this.resId,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.assetInventorys = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load inventory details:', error)
      } finally {
        this.loading = false
      }
    },
    openDetail(row) {
      this.$router.push({
        path: '/pages/property/assetInventoryInStock',
        query: { aiId: row.aiId }
      })
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
.resource-detail-inventory {
  padding: 20px;
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>