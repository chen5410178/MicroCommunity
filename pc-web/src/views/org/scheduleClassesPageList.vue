<template>
  <div class="schedule-classes-container animated fadeInRight">
    <!-- 查询条件 -->
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('scheduleClassesPage.searchConditions') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="_moreCondition()">
          {{ scheduleClassesPageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="scheduleClassesPageInfo.conditions.staffNameLike"
            :placeholder="$t('scheduleClassesPage.staffNamePlaceholder')" clearable />
        </el-col>
        <el-col :span="8">
          <el-select v-model="scheduleClassesPageInfo.conditions.scheduleId" style="width:100%"
            :placeholder="$t('scheduleClassesPage.schedulePlaceholder')">
            <el-option :label="$t('scheduleClassesPage.selectSchedule')" value="" />
            <el-option v-for="(item, index) in scheduleClassesPageInfo.scheduleClassess" :key="index" :label="item.name"
              :value="item.scheduleId" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-date-picker v-model="scheduleClassesPageInfo.conditions.curDate" type="month"
            :placeholder="$t('scheduleClassesPage.datePlaceholder')" format="yyyy-MM" value-format="yyyy-MM"
            style="width:100%" @change="handleDateChange" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryScheduleClassesMethod()">
            {{ $t('common.search') }}
          </el-button>
          <el-button type="primary" @click="_exportScheduleClasses()">
            <i class="el-icon-upload" />
            {{ $t('common.export') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row v-show="scheduleClassesPageInfo.moreCondition" :gutter="20" style="margin-top:15px">
        <el-col :span="6">
          <el-input v-model="scheduleClassesPageInfo.conditions.orgName" readonly
            :placeholder="$t('scheduleClassesPage.orgPlaceholder')" @focus="_staffChangeOrg()" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 排班表 -->
    <el-card class="box-card" style="margin-top:20px">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('scheduleClassesPage.scheduleTable') }}</span>
      </div>
      <div class="hc-table-div" >
        <table class="custom-table">
          <thead>
            <tr>
              <th
                style=" text-align: center;  border: 1px solid #ebeef5; background-color: #fafafa;">
                {{ $t('scheduleClassesPage.staffName') }}
              </th>
              <th v-for="index in scheduleClassesPageInfo.maxDay" :key="index"
                style="text-align: center;  border: 1px solid #ebeef5; background-color: #fafafa;">
                {{ index }}{{ $t('scheduleClassesPage.day') }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(staff, staffIndex) in scheduleClassesPageInfo.staffs" :key="staffIndex">
              <td style="text-align: center;  border: 1px solid #ebeef5;" class="padding-lg">
                {{ staff.staffName }}
              </td>
              <td v-for="(day, dayIndex) in staff.days" :key="dayIndex"
                style="text-align: center; border: 1px solid #ebeef5; vertical-align: top;">
                <div class="text-center border padding-lg labeling-strip"
                  style="border-radius: 5px; cursor: pointer; margin: 2px 0;">
                  <div>{{ day.workdayName || $t('scheduleClassesPage.rest') }}</div>
                  <div v-for="(time, timeIndex) in day.times" :key="timeIndex">
                    {{ time.startTime }}-{{ time.endTime }}
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页 -->
      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" style="margin-top:20px;text-align:right"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <!-- 选择组织弹窗 -->
    <choose-org-tree ref="chooseOrgTree" @switchOrg="handleSwitchOrg" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listScheduleClasses, staffMonthScheduleClasses, exportData } from '@/api/org/scheduleClassesPageApi'
import ChooseOrgTree from '@/components/org/ChooseOrgTree'

export default {
  name: 'ScheduleClassesPageList',
  components: {
    ChooseOrgTree
  },
  data() {
    return {
      communityId: '',
      scheduleClassesPageInfo: {
        staffs: [],
        scheduleClassess: [],
        maxDay: 30,
        curMonth: '',
        curYear: '',
        total: 0,
        records: 1,
        moreCondition: false,
        conditions: {
          staffNameLike: '',
          scheduleId: '',
          curDate: '',
          orgId: '',
          orgName: ''
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
    this.initStaffDate()
    this._listScheduleClassess()
    this._listStaffScheduleClassess(this.page.current, this.page.size)
  },
  methods: {
    initStaffDate() {
      const date = new Date()
      this.scheduleClassesPageInfo.curMonth = date.getMonth() + 1
      this.scheduleClassesPageInfo.curYear = date.getFullYear()
      this.scheduleClassesPageInfo.conditions.curDate = `${date.getFullYear()}-${date.getMonth() + 1}`
      this.scheduleClassesPageInfo.maxDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate()
    },
    handleDateChange(val) {
      if (val) {
        const values = val.split('-')
        this.scheduleClassesPageInfo.curYear = values[0]
        this.scheduleClassesPageInfo.curMonth = values[1]
        this._listStaffScheduleClassess(this.page.current, this.page.size)
      }
    },
    async _listStaffScheduleClassess(page, size) {
      try {
        const params = {
          ...this.scheduleClassesPageInfo.conditions,
          page,
          row: size,
          communityId: this.communityId
        }
        const { data, total, records } = await staffMonthScheduleClasses(params)
        this.scheduleClassesPageInfo.staffs = data
        this.scheduleClassesPageInfo.total = total
        this.scheduleClassesPageInfo.records = records
        this.page.total = total
      } catch (error) {
        console.error('获取员工排班表失败:', error)
      }
    },
    async _listScheduleClassess() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listScheduleClasses(params)
        this.scheduleClassesPageInfo.scheduleClassess = data
      } catch (error) {
        console.error('获取班次列表失败:', error)
      }
    },
    _queryScheduleClassesMethod() {
      this.page.current = 1
      this._listStaffScheduleClassess(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.scheduleClassesPageInfo.moreCondition = !this.scheduleClassesPageInfo.moreCondition
    },
    async _exportScheduleClasses() {
      try {
        const params = {
          ...this.scheduleClassesPageInfo.conditions,
          communityId: this.communityId,
          pagePath: 'reportStaffMonthScheduleClasses'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFailed'))
      }
    },
    _staffChangeOrg() {
      this.$refs.chooseOrgTree.open()
    },
    handleSwitchOrg(org) {
      this.scheduleClassesPageInfo.conditions.orgId = org.orgId
      this.scheduleClassesPageInfo.conditions.orgName = org.allOrgName
    },

    handleSizeChange(val) {
      this.page.size = val
      this._listStaffScheduleClassess(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listStaffScheduleClassess(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.schedule-classes-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .hc-table-div {
    overflow-x: auto;
  }

  .custom-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #ebeef5;

    th,
    td {
      border: 1px solid #ebeef5;
      padding: 12px 8px;
      text-align: center;
    }

    th {
      background-color: #fafafa;
      font-weight: 500;
      color: #606266;
    }

    td {
      background-color: #fff;
    }

    tbody tr:hover {
      background-color: #f5f7fa;
    }
  }

  .labeling-strip {
    margin: 2px;
    border: 1px solid #ebeef5;
  }
}
</style>