<template>
  <div class="attendance-classes-staff-manage-container">
    <el-row :gutter="10">
      <el-col :span="3">
        <div class="list-group-border-radius">
          <div class=" treeview">
            <ul class="list-group text-center ">
              <li v-for="(item, index) in attendanceClassesStaffManageInfo.attendanceClassess" :key="index" @click="swatchClass(item)"
                :class="{ 'vc-node-selected': attendanceClassesStaffManageInfo.conditions.classesId == item.classesId }"
                class="list-group-item node-orgTree">
                {{ item.classesName }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="21">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('attendanceClassesStaffManage.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input v-model="attendanceClassesStaffManageInfo.conditions.staffNameLike"
                :placeholder="$t('attendanceClassesStaffManage.search.staffName')" clearable />
            </el-col>
            <el-col :span="16">
              <el-button type="primary" @click="_queryAttendanceClassesStaffMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetAttendanceClassesStaffMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('attendanceClassesStaffManage.list.title') }}</span>
            <el-button type="primary" size="small" style="float: right;" @click="_openAddAttendanceClassesStaffModal">
              <i class="el-icon-plus"></i>
              {{ $t('common.add') }}
            </el-button>
          </div>
          <el-table :data="attendanceClassesStaffManageInfo.attendanceClassesStaffs" border style="width: 100%"
            v-loading="loading">
            <el-table-column prop="personFace" :label="$t('attendanceClassesStaffManage.table.face')" align="center"
              width="100">
              <template slot-scope="scope">
                <div style="position: relative; display: inline-block;"
                  @click="showImg(scope.row.personFace || '/img/noPhoto.jpg')">
                  <el-image style="width: 50px; height: 50px" :src="scope.row.personFace || '/img/noPhoto.jpg'"
                    fit="cover">
                    <div slot="error" class="image-slot">
                      <img src="/img/noPhoto.jpg" style="width: 50px; height: 50px">
                    </div>
                  </el-image>
                  <img src="/img/icon-bigimg.png" style="position: absolute;right: 0;bottom: 0;" width="50" height="50"
                    alt="">
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="staffId" :label="$t('attendanceClassesStaffManage.table.staffId')" align="center" />
            <el-table-column prop="staffName" :label="$t('attendanceClassesStaffManage.table.staffName')"
              align="center" />
            <el-table-column prop="createTime" :label="$t('attendanceClassesStaffManage.table.createTime')"
              align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="_openDeleteAttendanceClassesStaffModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
                <el-button size="mini" type="primary" @click="openStaffDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" style="margin-top: 20px;" />
        </el-card>
      </el-col>
    </el-row>

    <delete-attendance-classes-staff ref="deleteDialog" @success="handleSuccess" />
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listAttendanceClassesStaff, listAttendanceClassess } from '@/api/oa/attendanceClassesStaffManageApi'
import DeleteAttendanceClassesStaff from '@/components/oa/deleteAttendanceClassesStaff'
import ViewImage from '@/components/system/viewImage'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AttendanceClassesStaffManageList',
  components: {
    DeleteAttendanceClassesStaff,
    ViewImage
  },
  data() {
    return {
      loading: false,
      attendanceClassesStaffManageInfo: {
        attendanceClassesStaffs: [],
        total: 0,
        records: 1,
        moreCondition: false,
        csId: '',
        attendanceClassess: [],
        conditions: {
          classesId: '',
          staffId: '',
          staffNameLike: '',
          page: 1,
          row: 10
        }
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
    this._loadAttendanceClass()
  },
  methods: {
    async _listAttendanceClassesStaffs(page = 1, rows = 10) {
      try {
        this.loading = true
        this.attendanceClassesStaffManageInfo.conditions.page = page
        this.attendanceClassesStaffManageInfo.conditions.row = rows

        const { data, total } = await listAttendanceClassesStaff(this.attendanceClassesStaffManageInfo.conditions)
        this.attendanceClassesStaffManageInfo.attendanceClassesStaffs = data
        this.page.total = total
      } catch (error) {
        console.error('获取考勤员工列表失败:', error)
        this.$message.error(this.$t('attendanceClassesStaffManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddAttendanceClassesStaffModal() {
      this.$router.push(`/views/oa/addAttendanceClassesStaff?classesId=${this.attendanceClassesStaffManageInfo.conditions.classesId}`)
    },
    _openDeleteAttendanceClassesStaffModel(row) {
      this.$refs.deleteDialog.open(row)
    },
    _queryAttendanceClassesStaffMethod() {
      this.page.current = 1
      this._listAttendanceClassesStaffs()
    },
    _resetAttendanceClassesStaffMethod() {
      this.attendanceClassesStaffManageInfo.conditions.staffNameLike = ''
      this._queryAttendanceClassesStaffMethod()
    },
    swatchClass(item) {
      this.attendanceClassesStaffManageInfo.conditions.classesId = item.classesId
      this._listAttendanceClassesStaffs()
    },
    async _loadAttendanceClass() {
      try {
        const { data } = await listAttendanceClassess({ page: 1, row: 100 })
        this.attendanceClassesStaffManageInfo.attendanceClassess = data
        if (data && data.length > 0) {
          this.swatchClass(data[0])
        }
      } catch (error) {
        console.error('获取考勤班次失败:', error)
        this.$message.error(this.$t('attendanceClassesStaffManage.fetchClassError'))
      }
    },
    showImg(url) {
      this.$refs.viewImage.open(url)
    },
    openStaffDetail(staff) {
      this.$router.push(`/views/staff/staffDetail?staffId=${staff.staffId}`)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAttendanceClassesStaffs(this.page.current, val)
    },
    handleCurrentChange(val) {
      this._listAttendanceClassesStaffs(val, this.page.size)
    },
    handleSuccess() {
      this._listAttendanceClassesStaffs(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.attendance-classes-staff-manage-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .treeview {
      height: 100%;

      .list-group {
        list-style: none;
        padding: 0;
        margin: 0;

        .list-group-item {
          padding: 10px 15px;
          margin-bottom: 5px;
          border: 1px solid #ddd;
          border-radius: 4px;
          cursor: pointer;
          text-align: center;

          &:hover {
            background-color: #f5f5f5;
          }

          &.vc-node-selected {
            background-color: #409EFF;
            color: white;
            border-color: #409EFF;
          }
        }
      }
    }
  }
}
</style>