<template>
  <div class="activities-manage-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('activitiesManage.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="_moreCondition">
          {{ activitiesManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="activitiesManageInfo.conditions.activitiesId"
            :placeholder="$t('activitiesManage.search.activitiesId')" clearable />
        </el-col>
        <el-col :span="8">
          <el-input v-model="activitiesManageInfo.conditions.title" :placeholder="$t('activitiesManage.search.title')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="activitiesManageInfo.conditions.typeCd" :placeholder="$t('activitiesManage.search.typeCd')"
            style="width:100%">
            <el-option v-for="item in activitiesManageInfo.typeCds" :key="item.typeCd" :label="item.typeName"
              :value="item.typeCd" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryActivitiesMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetActivitiesMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row v-if="activitiesManageInfo.moreCondition" :gutter="20" style="margin-top:15px">
        <el-col :span="6">
          <el-input v-model="activitiesManageInfo.conditions.staffName"
            :placeholder="$t('activitiesManage.search.staffName')" clearable />
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card" style="margin-top:20px">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('activitiesManage.list.title') }}</span>
        <el-button type="primary" size="small" @click="_openAddActivitiesModal">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>
      <el-table :data="activitiesManageInfo.activitiess" border style="width: 100%">
        <el-table-column prop="activitiesId" :label="$t('activitiesManage.table.activitiesId')" align="center" />
        <el-table-column prop="title" :label="$t('activitiesManage.table.title')" align="center" />
        <el-table-column prop="typeCdName" :label="$t('activitiesManage.table.typeCd')" align="center" />
        <el-table-column prop="startTime" :label="$t('activitiesManage.table.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('activitiesManage.table.endTime')" align="center" />
        <el-table-column prop="userName" :label="$t('activitiesManage.table.userName')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openEditActivitiesModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteActivitiesModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" style="margin-top:20px;text-align:right" />
    </el-card>

    <add-activities-view ref="addActivitiesView" @success="handleSuccess" />
    <edit-activities-view ref="editActivitiesView" @success="handleSuccess" />
    <delete-activities ref="deleteActivities" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listActivities, listActivitiesType } from '@/api/oa/activitiesManageApi'
import AddActivitiesView from '@/components/oa/addActivitiesView'
import EditActivitiesView from '@/components/oa/editActivitiesView'
import DeleteActivities from '@/components/oa/deleteActivities'

export default {
  name: 'ActivitiesManageList',
  components: {
    AddActivitiesView,
    EditActivitiesView,
    DeleteActivities
  },
  data() {
    return {
      activitiesManageInfo: {
        activitiess: [],
        total: 0,
        records: 1,
        moreCondition: false,
        title: '',
        typeCds: [],
        conditions: {
          title: '',
          typeCd: '',
          staffName: '',
          activitiesId: '',
          endTimeFlag: '1',
          page: 1,
          row: 10,
          communityId: ''
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
    this.activitiesManageInfo.conditions.communityId = this.communityId
    this._listActivitiess()
    this._loadActivitiesType()
  },
  methods: {
    async _listActivitiess() {
      try {
        const params = {
          ...this.activitiesManageInfo.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await listActivities(params)
        this.activitiesManageInfo.activitiess = data
        this.page.total = total
      } catch (error) {
        console.error('获取活动列表失败:', error)
      }
    },
    async _loadActivitiesType() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: this.communityId
        }
        const { data } = await listActivitiesType(params)
        this.activitiesManageInfo.typeCds = data
      } catch (error) {
        console.error('获取活动类型失败:', error)
      }
    },
    _openAddActivitiesModal() {
      this.$refs.addActivitiesView.open()
    },
    _openEditActivitiesModel(row) {
      this.$refs.editActivitiesView.open(row)
    },
    _openDeleteActivitiesModel(row) {
      this.$refs.deleteActivities.open(row)
    },
    _queryActivitiesMethod() {
      this.page.current = 1
      this._listActivitiess()
    },
    _resetActivitiesMethod() {
      this.activitiesManageInfo.conditions = {
        title: '',
        typeCd: '',
        staffName: '',
        activitiesId: '',
        endTimeFlag: '1',
        page: 1,
        row: 10,
        communityId: this.communityId
      }
      this._listActivitiess()
    },
    _moreCondition() {
      this.activitiesManageInfo.moreCondition = !this.activitiesManageInfo.moreCondition
    },
    handleSuccess() {
      this._listActivitiess()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listActivitiess()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listActivitiess()
    }
  }
}
</script>

<style lang="scss" scoped>
.activities-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .no_display {
    display: none;
  }
}
</style>