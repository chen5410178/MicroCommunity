<template>
  <div class="complaint-detail-type">
    <el-table :data="complaintTypes" border style="width: 100%" v-loading="loading">
      <el-table-column prop="typeName" :label="$t('complaintDetailType.typeName')" align="center" />
      <el-table-column prop="notifyWay" :label="$t('complaintDetailType.notifyWay')" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.notifyWay === 'SMS' ? $t('complaintDetailType.sms') : $t('complaintDetailType.wechat')
            }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="appraiseReply" :label="$t('complaintDetailType.appraiseReply')" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.appraiseReply === 'Y' ? $t('complaintDetailType.autoReply') :
            $t('complaintDetailType.manualReply') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('complaintDetailType.handler')" align="center">
        <template slot-scope="scope">
          <div v-for="(item, index) in scope.row.staffs" :key="index">
            {{ item.staffName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('complaintDetailType.createTime')" align="center" />
    </el-table>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listComplaintType } from '@/api/oa/complaintDetailApi'

export default {
  name: 'ComplaintDetailType',
  data() {
    return {
      complaintTypes: [],
      loading: false,
      typeCd: '',
      communityId: ''
    }
  },
  methods: {
    async initData(params) {
      this.communityId = getCommunityId()
      this.typeCd = params.typeCd
      await this.loadData()
    },
    async loadData() {
      try {
        this.loading = true
        const params = {
          communityId: this.communityId,
          typeCd: this.typeCd,
          page: 1,
          row: 100
        }
        const { data } = await listComplaintType(params)
        this.complaintTypes = data || []
      } catch (error) {
        console.error('获取工单类型数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-detail-type {
  margin-top: 20px;
}
</style>