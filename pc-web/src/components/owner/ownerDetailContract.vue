<template>
  <div class="margin-top">
    <el-table :data="ownerDetailContractInfo.contracts" style="width: 100%">
      <el-table-column prop="contractName" :label="$t('ownerDetailContract.contractName')" align="center"></el-table-column>
      <el-table-column prop="contractCode" :label="$t('ownerDetailContract.contractCode')" align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailContract.parentContractCode')" align="center">
        <template slot-scope="scope">
          {{scope.row.parentContractCode?scope.row.parentContractCode:'-'}}
        </template>
      </el-table-column>
      <el-table-column prop="contractTypeName" :label="$t('ownerDetailContract.contractType')" align="center"></el-table-column>
      <el-table-column prop="operator" :label="$t('ownerDetailContract.operator')" align="center"></el-table-column>
      <el-table-column prop="amount" :label="$t('ownerDetailContract.amount')" align="center"></el-table-column>
      <el-table-column prop="startTime" :label="$t('ownerDetailContract.startTime')" align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('ownerDetailContract.endTime')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('ownerDetailContract.createTime')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('ownerDetailContract.status')" align="center"></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      layout="total, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
import { queryContract } from '@/api/owner/ownerDetailContractApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailContract',
  data() {
    return {
      ownerDetailContractInfo: {
        contracts: [],
        ownerId: '',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    _loadOwnerDetailContractData(page, row) {
      const params = {
        communityId: getCommunityId(),
        objId: this.ownerDetailContractInfo.ownerId,
        page: page,
        row: row
      }

      queryContract(params).then(response => {
        this.ownerDetailContractInfo.contracts = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailContractData(val, this.pagination.pageSize)
    }
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailContractInfo.ownerId = data.ownerId
      this._loadOwnerDetailContractData(1, this.pagination.pageSize)
    })
  }
}
</script>