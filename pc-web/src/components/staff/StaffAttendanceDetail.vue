<template>
    <el-dialog
      :title="$t('staffAttendanceDetail.title')"
      :visible.sync="visible"
      width="80%"
      @close="close"
    >
      <el-table :data="staffAttendanceDetailInfo.details" border style="width: 100%">
        <el-table-column :label="$t('staffAttendanceDetail.face')" align="center">
          <template slot-scope="scope">
            <el-image
              :src="scope.row.facePath || '/img/noPhoto.jpg'"
              style="width: 60px; height: 60px; border-radius: 4px;"
            />
          </template>
        </el-table-column>
        <el-table-column prop="staffName" :label="$t('staffAttendanceDetail.staffName')" align="center" />
        <el-table-column prop="clockTime" :label="$t('staffAttendanceDetail.clockTime')" align="center" />
      </el-table>
    </el-dialog>
  </template>
  
  <script>
  import { queryAttendanceLog } from '@/api/staff/adminStaffDetailApi'
  import { getCommunityId } from '@/api/community/communityApi' 
  
  export default {
    name: 'StaffAttendanceDetail',
    data() {
      return {
        visible: false,
        staffAttendanceDetailInfo: {
          details: []
        }
      }
    },
    methods: {
      async open(param) {
        this.visible = true
        try {
          const { data } = await queryAttendanceLog({
            communityId: getCommunityId(),
            staffId: param.staffId,
            date: param.date,
            page: 1,
            row: 30
          })
          this.staffAttendanceDetailInfo.details = data
        } catch (error) {
          this.$message.error(this.$t('staffAttendanceDetail.fetchError'))
        }
      },
      close() {
        this.visible = false
        this.staffAttendanceDetailInfo.details = []
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  </style>