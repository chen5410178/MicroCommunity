<template>
  <div class="attendance-classes-manage-container">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('attendanceClassesManage.title') }}</span>
            <div class="header-tools">
              <el-button type="primary" size="small" @click="showMarkdown">
                <i class="el-icon-document"></i>
                {{ $t('attendanceClassesManage.document') }}
              </el-button>
              <el-button type="primary" size="small" @click="openAddAttendanceClassesModal">
                <i class="el-icon-plus"></i>
                {{ $t('attendanceClassesManage.add') }}
              </el-button>
            </div>
          </div>

          <el-table :data="attendanceClassesManageInfo.attendanceClassess" border style="width: 100%"
            v-loading="loading">
            <el-table-column prop="classesId" :label="$t('attendanceClassesManage.classesId')" align="center" />
            <el-table-column prop="classesName" :label="$t('attendanceClassesManage.classesName')" align="center" />
            <el-table-column :label="$t('attendanceClassesManage.timeOffset')" align="center">
              <template slot-scope="scope">
                {{ scope.row.timeOffset }} {{ $t('attendanceClassesManage.minute') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('attendanceClassesManage.lateOffset')" align="center">
              <template slot-scope="scope">
                {{ scope.row.lateOffset }} {{ $t('attendanceClassesManage.minute') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('attendanceClassesManage.leaveOffset')" align="center">
              <template slot-scope="scope">
                {{ scope.row.leaveOffset }} {{ $t('attendanceClassesManage.minute') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('attendanceClassesManage.maxLastOffset')" align="center">
              <template slot-scope="scope">
                {{ scope.row.maxLastOffset }} {{ $t('attendanceClassesManage.minute') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="openEditAttendanceClassesModel(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="openDeleteAttendanceClassesModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" class="pagination" />
        </el-card>
      </el-col>
    </el-row>

    <add-attendance-classes ref="addAttendanceClasses" @success="listAttendanceClasses" />
    <edit-attendance-classes ref="editAttendanceClasses" @success="listAttendanceClasses" />
    <delete-attendance-classes ref="deleteAttendanceClasses" @success="listAttendanceClasses" />
  </div>
</template>

<script>
import { listAttendanceClassess } from '@/api/oa/attendanceClassesManageApi'
import AddAttendanceClasses from '@/components/oa/addAttendanceClasses'
import EditAttendanceClasses from '@/components/oa/editAttendanceClasses'
import DeleteAttendanceClasses from '@/components/oa/deleteAttendanceClasses'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AttendanceClassesManageList',
  components: {
    AddAttendanceClasses,
    EditAttendanceClasses,
    DeleteAttendanceClasses
  },
  data() {
    return {
      loading: false,
      attendanceClassesManageInfo: {
        attendanceClassess: [],
        classesObjId: '',
        classesObjName: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.listAttendanceClasses()
  },
  methods: {
    async listAttendanceClasses() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }
        const { data, total } = await listAttendanceClassess(params)
        this.attendanceClassesManageInfo.attendanceClassess = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('attendanceClassesManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    showMarkdown() {
      // TODO: 实现文档显示逻辑
    },
    openAddAttendanceClassesModal() {
      this.$refs.addAttendanceClasses.open({
        classesObjId: this.attendanceClassesManageInfo.classesObjId,
        classesObjName: this.attendanceClassesManageInfo.classesObjName
      })
    },
    openEditAttendanceClassesModel(row) {
      this.$refs.editAttendanceClasses.open(row)
    },
    openDeleteAttendanceClassesModel(row) {
      this.$refs.deleteAttendanceClasses.open(row)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listAttendanceClasses()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listAttendanceClasses()
    }
  }
}
</script>

<style lang="scss" scoped>
.attendance-classes-manage-container {
  padding: 20px;

  .header-tools {
    float: right;
    margin-top: -5px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>