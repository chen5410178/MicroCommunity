<template>
  <div class="margin-top">
    <div class="margin-top">
      <el-table :data="aRoomDetailContractInfo.contracts" style="width: 100%">
        <el-table-column prop="contractName" :label="$t('aRoomDetailContract.contractName')" align="center"></el-table-column>
        <el-table-column prop="contractCode" :label="$t('aRoomDetailContract.contractCode')" align="center"></el-table-column>
        <el-table-column prop="parentContractCode" :label="$t('aRoomDetailContract.parentContractCode')" align="center">
          <template slot-scope="scope">
            {{scope.row.parentContractCode || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="contractTypeName" :label="$t('aRoomDetailContract.contractType')" align="center"></el-table-column>
        <el-table-column prop="operator" :label="$t('aRoomDetailContract.operator')" align="center"></el-table-column>
        <el-table-column prop="amount" :label="$t('aRoomDetailContract.amount')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('aRoomDetailContract.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('aRoomDetailContract.endTime')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('aRoomDetailContract.createTime')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('aRoomDetailContract.state')" align="center"></el-table-column>
      </el-table>
      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryAdminOwnerContract } from '@/api/aCommunity/aRoomDetailContractApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ARoomDetailContract',
  data() {
    return {
      aRoomDetailContractInfo: {
        contracts: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.aRoomDetailContractInfo.ownerId = data.ownerId
      this._loadARoomDetailContractData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadARoomDetailContractData(val, this.pageSize)
    },
    async _loadARoomDetailContractData(page, row) {
      try {
        const res = await queryAdminOwnerContract({
          communityId: this.communityId,
          ownerId: this.aRoomDetailContractInfo.ownerId,
          page,
          row
        })
        this.aRoomDetailContractInfo.contracts = res.data
        this.total = res.total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _qureyARoomDetailContract() {
      this.currentPage = 1
      this._loadARoomDetailContractData(1, this.pageSize)
    }
  }
}
</script>

<style scoped>
/* Same styles as aRoomDetailMember */
</style>