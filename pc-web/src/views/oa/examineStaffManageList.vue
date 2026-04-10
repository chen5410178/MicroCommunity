<template>
  <div class="examine-staff-manage-container">
    <!-- 查询条件 -->
    <el-card class="">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('examineStaffManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.staffName" :placeholder="$t('examineStaffManage.search.staffName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.projectName" :placeholder="$t('examineStaffManage.search.projectName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 考核人员列表 -->
    <el-card class="list-wrapper margin-top">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('examineStaffManage.list.title') }}</div>
        <el-button type="primary" size="small"  @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column :label="$t('examineStaffManage.table.face')" align="center" width="120">
          <template slot-scope="scope">
            <div style="position: relative; display: inline-block; cursor: pointer;"
              @click="showImage(scope.row.headerImg || '/img/noPhoto.jpg')">
              <el-image style="width: 50px; height: 50px;" :src="scope.row.headerImg || '/img/noPhoto.jpg'" fit="cover" />
              <img src="/img/icon-bigimg.png" style="position: absolute; right: 0; bottom: 0;" width="20" height="20"
                alt="">
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="staffId" :label="$t('examineStaffManage.table.staffId')" align="center" />
        <el-table-column prop="staffName" :label="$t('examineStaffManage.table.staffName')" align="center" />
        <el-table-column prop="post" :label="$t('examineStaffManage.table.post')" align="center" />
        <el-table-column :label="$t('examineStaffManage.table.projects')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.projects" :key="index">
              {{ item.projectName }} 
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <delete-examine-staff ref="deleteDialog" @success="handleSuccess" />
    <view-image ref="imageViewer" />
  </div>
</template>

<script>
import { listExamineStaff } from '@/api/oa/examineStaffManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteExamineStaff from '@/components/oa/deleteExamineStaff'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'ExamineStaffManageList',
  components: {
    DeleteExamineStaff,
    ViewImage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        staffName: '',
        projectName: '',
        communityId: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await listExamineStaff(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('examineStaffManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.staffName = ''
      this.searchForm.projectName = ''
      this.handleSearch()
    },
    handleAdd() {
      this.$router.push('/views/oa/addExamineStaff')
    },
    handleEdit(row) {
      this.$router.push(`/views/oa/editExamineStaff?esId=${row.esId}`)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    showImage(url) {
      this.$refs.imageViewer.open(url)
    },
    handleSuccess() {
      this.getList()
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
.examine-staff-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>