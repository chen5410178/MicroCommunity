<template>
  <div class="type-container">
    <el-table :data="complaintTypes" border style="width: 100%">
      <el-table-column prop="typeName" :label="$t('complaintDetailType.typeName')" align="center"></el-table-column>
      <el-table-column prop="notifyWay" :label="$t('complaintDetailType.notifyWay')" align="center">
        <template slot-scope="scope">
          {{ scope.row.notifyWay === 'SMS' ? $t('complaintDetailType.sms') : $t('complaintDetailType.wechat') }}
        </template>
      </el-table-column>
      <el-table-column prop="appraiseReply" :label="$t('complaintDetailType.appraiseReply')" align="center">
        <template slot-scope="scope">
          {{ scope.row.appraiseReply === 'Y' ? $t('complaintDetailType.autoReply') : $t('complaintDetailType.manualReply') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('complaintDetailType.handler')" align="center">
        <template slot-scope="scope">
          <div v-for="(staff, index) in scope.row.staffs" :key="index">
            {{ staff.staffName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('complaintDetailType.createTime')" align="center"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listComplaintTypes } from '@/api/complaint/adminComplaintDetailApi'

export default {
  name: 'AComplaintDetailType',
  props: {
    complaintId: {
      type: String,
      default: ''
    },
    typeCd: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      complaintTypes: []
    }
  },
  methods: {
    async loadData() {
      try {
        const params = {
          typeCd: this.typeCd,
          page: 1,
          row: 100
        }
        const { data } = await listComplaintTypes( params )
        this.complaintTypes = data || []
      } catch (error) {
        this.$message.error(this.$t('complaintDetailType.fetchError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.type-container {
  padding: 20px 0;
}
</style>