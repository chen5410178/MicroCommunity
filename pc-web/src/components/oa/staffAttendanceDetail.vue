<template>
  <el-dialog
    :title="$t('staffAttendance.attendanceDetail')"
    :visible.sync="visible"
    width="70%"
  >
    <el-table
      :data="details"
      border
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column
        prop="staffName"
        :label="$t('staffAttendance.staffName')"
        align="center"
      ></el-table-column>
      
      <el-table-column
        :label="$t('staffAttendance.face')"
        align="center"
      >
        <template slot-scope="scope">
          <el-image
            style="width: 60px; height: 60px; border-radius: 4px;"
            :src="scope.row.facePath || '/img/noPhoto.jpg'"
            fit="cover"
          ></el-image>
        </template>
      </el-table-column>
      
      <el-table-column
        prop="clockTime"
        :label="$t('staffAttendance.clockTime')"
        align="center"
      ></el-table-column>
    </el-table>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.close') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryAttendanceLog } from '@/api/oa/staffAttendanceManageApi'

export default {
  name: 'StaffAttendanceDetail',
  data() {
    return {
      visible: false,
      loading: false,
      details: [],
      params: {}
    }
  },
  methods: {
    open(params) {
      this.params = params || {}
      this.visible = true
      this.loadDetails()
    },
    
    async loadDetails() {
      try {
        this.loading = true
        const { data } = await queryAttendanceLog({
          ...this.params,
          page: 1,
          row: 30,
          communityId: getCommunityId()
        })
        this.details = data || []
      } catch (error) {
        console.error('Failed to load attendance details:', error)
        this.$message.error(this.$t('staffAttendance.loadDetailFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>