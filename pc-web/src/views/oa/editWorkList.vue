<template>
  <div class="edit-work-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('editWork.title') }}</span>
      </div>

      <el-form ref="form" :model="editWorkInfo" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editWork.workName')" prop="workName">
              <el-input v-model.trim="editWorkInfo.workName" :placeholder="$t('editWork.workNamePlaceholder')"
                maxlength="20" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editWork.workType')" prop="wtId">
              <el-select v-model="editWorkInfo.wtId" :placeholder="$t('editWork.workTypePlaceholder')" style="width:100%"
                clearable>
                <el-option v-for="item in editWorkInfo.workTypes" :key="item.wtId" :label="item.typeName"
                  :value="item.wtId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editWork.processor')">
              <div class="staff-tags">
                <el-tag v-for="(staff, index) in editWorkInfo.staffs" :key="index" closable
                  @close="_deleteWorkStaff(staff)">
                  {{ staff.staffName }}
                </el-tag>
                <el-button type="text" @click="_chooseWorkStaff">
                  <i class="el-icon-search"></i>{{ $t('editWork.choose') }}
                </el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editWork.ccPerson')">
              <div class="staff-tags">
                <el-tag v-for="(staff, index) in editWorkInfo.copyStaffs" :key="index" closable
                  @close="_deleteCopyWorkStaff(staff)">
                  {{ staff.staffName }}
                </el-tag>
                <el-button type="text" @click="_chooseCopyWorkStaff">
                  <i class="el-icon-search"></i>{{ $t('editWork.choose') }}
                </el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editWork.workSign')" prop="workCycle">
              <el-select v-model="editWorkInfo.workCycle" :placeholder="$t('editWork.workSignPlaceholder')"
                style="width:100%" @change="handleWorkCycleChange">
                <el-option :label="$t('editWork.onceWork')" value="1001" />
                <el-option :label="$t('editWork.cycleWork')" value="2002" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editWork.attachment')">
              <upload-file ref="uploadFile" @notify="notifyFile" :defaultFiles="editWorkInfo.pathUrls" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editWork.startTime')" prop="startTime">
              <el-date-picker v-model="editWorkInfo.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('editWork.startTimePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editWork.endTime')" prop="endTime">
              <el-date-picker v-model="editWorkInfo.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('editWork.endTimePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="editWorkInfo.workCycle === '2002'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="$t('editWork.period')" prop="period">
                <el-select v-model="editWorkInfo.period" :placeholder="$t('editWork.periodPlaceholder')"
                  style="width:100%">
                  <el-option :label="$t('editWork.monthDay')" value="2020022" />
                  <el-option :label="$t('editWork.byWeek')" value="2020023" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('editWork.workHours')">
                <el-input v-model.trim="editWorkInfo.hours" :placeholder="$t('editWork.workHoursPlaceholder')">
                  <template slot="append">{{ $t('editWork.hoursComplete') }}</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <template v-if="editWorkInfo.period === '2020022'">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('editWork.month')">
                  <el-checkbox-group v-model="editWorkInfo.months">
                    <el-checkbox v-for="index in 12" :key="index" :label="index">
                      {{ index }}{{ $t('editWork.monthUnit') }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('editWork.day')">
                  <el-checkbox-group v-model="editWorkInfo.days">
                    <el-checkbox v-for="index in 31" :key="index" :label="index">
                      {{ index }}{{ $t('editWork.dayUnit') }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
          </template>

          <template v-if="editWorkInfo.period === '2020023'">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('editWork.week')">
                  <el-checkbox-group v-model="editWorkInfo.workdays">
                    <el-checkbox v-for="(day, index) in weekDays" :key="index" :label="index + 1">
                      {{ day }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
          </template>
        </template>

        <el-row v-for="(item, index) in editWorkInfo.contents" :key="item.id" :gutter="20">
          <el-col :span="18">
            <el-form-item :label="`${$t('editWork.content')}${index + 1}`">
              <el-input v-model="item.content" type="textarea" :rows="5"
                :placeholder="$t('editWork.contentPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="6" class="flex align-center">
            <el-button v-if="index === 0" type="text" @click="addWorkContent">
              {{ $t('editWork.add') }}
            </el-button>
            <el-button v-else type="text" @click="deleteWorkContent(item)">
              {{ $t('editWork.delete') }}
            </el-button>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="goBack">
              <i class="el-icon-close"></i>{{ $t('common.back') }}
            </el-button>
            <el-button type="primary" @click="saveWorkPool">
              <i class="el-icon-check"></i>{{ $t('common.submit') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <select-staff ref="selectStaff" @selectStaff="selectStaff" />
  </div>
</template>

<script>
import { updateWorkPool, listWorkType, queryStartWork, listWorkTask, listWorkCopy, listWorkCycle } from '@/api/oa/editWorkApi'
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'
import UploadFile from '@/components/upload/uploadFile'
import { getUuid } from '@/utils/commonUtil'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'EditWorkList',
  components: {
    SelectStaff,
    UploadFile
  },
  data() {
    return {
      chooseStaffFlag: '1',
      editWorkInfo: {
        workId: '',
        workName: '',
        workTypes: [],
        wtId: '',
        workCycle: '1001',
        startTime: '',
        endTime: '',
        staffs: [],
        copyStaffs: [],
        pathUrls: [],
        contents: [],
        period: '',
        months: [],
        days: [],
        workdays: [],
        hours: '24',
        communityId: ''
      },
      weekDays: [
        this.$t('editWork.monday'),
        this.$t('editWork.tuesday'),
        this.$t('editWork.wednesday'),
        this.$t('editWork.thursday'),
        this.$t('editWork.friday'),
        this.$t('editWork.saturday'),
        this.$t('editWork.sunday')
      ]
    }
  },
  created() {
    this.editWorkInfo.workId = this.$route.query.workId
    this.editWorkInfo.communityId = getCommunityId()
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        await this._loadWorkPool()
        await this._loadTaskStaff()
        await this._loadWorkCopy()
        await this._listWorkTypes()
        await this._loadWorkCycle()
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    editWorkValidate() {
      return this.$refs.form.validate()
    },
    async saveWorkPool() {
      try {
        const valid = await this.editWorkValidate()
        if (!valid) return

        const params = {
          ...this.editWorkInfo,
          // 处理月份和日期数组为字符串
          months: this.editWorkInfo.months.join(','),
          days: this.editWorkInfo.days.join(','),
          workdays: this.editWorkInfo.workdays.join(',')
        }

        const res = await updateWorkPool(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('保存工作单失败:', error)
        this.$message.error(this.$t('common.submitFailed'))
      }
    },
    async _listWorkTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.editWorkInfo.communityId
        }
        const res = await listWorkType(params)
        this.editWorkInfo.workTypes = res.data
      } catch (error) {
        console.error('获取工作单类型失败:', error)
      }
    },
    _chooseWorkStaff() {
      this.chooseStaffFlag = '1'
      this.$refs.selectStaff.open({
        call: () => {
        }
      })
    },
    selectStaff(staff) {
      if (this.chooseStaffFlag === '1') {
        this.editWorkInfo.staffs.push({
          staffId: staff.userId,
          staffName: staff.userName
        })
      } else {
        this.editWorkInfo.copyStaffs.push({
          staffId: staff.userId,
          staffName: staff.userName
        })
      }
    },
    _deleteWorkStaff(staff) {
      this.editWorkInfo.staffs = this.editWorkInfo.staffs.filter(item => item.staffId !== staff.staffId)
    },
    _chooseCopyWorkStaff() {
      this.chooseStaffFlag = '2'
      this.$refs.selectStaff.open({
        call: () => {
        }
      })
    },
    _deleteCopyWorkStaff(staff) {
      this.editWorkInfo.copyStaffs = this.editWorkInfo.copyStaffs.filter(item => item.staffId !== staff.staffId)
    },
    async _loadWorkPool() {
      const params = {
        page: 1,
        row: 1,
        workId: this.editWorkInfo.workId
      }
      const { data } = await queryStartWork(params)
      if (data && data.length > 0) {
        Object.assign(this.editWorkInfo, data[0])
        // 格式化日期
        if (data[0].startTime) {
          this.editWorkInfo.startTime = dateFormat(new Date(data[0].startTime))
        }
        if (data[0].endTime) {
          this.editWorkInfo.endTime = dateFormat(new Date(data[0].endTime))
        }
        // 处理内容
        this.editWorkInfo.contents.forEach(c => {
          c.id =  getUuid()
        })
        if (data[0].pathUrls && data[0].pathUrls.length > 0) {
          this.editWorkInfo.pathUrls = data[0].pathUrls
          setTimeout(() => {
            this.$refs.uploadFile.setFilePath(data[0].pathUrls[0])
          }, 500)
        }
      }
    },
    async _loadTaskStaff() {
      const params = {
        page: 1,
        row: 100,
        workId: this.editWorkInfo.workId
      }
      const { data } = await listWorkTask(params)
      this.editWorkInfo.staffs = data || []
    },
    async _loadWorkCopy() {
      const params = {
        page: 1,
        row: 100,
        workId: this.editWorkInfo.workId
      }
      const { data } = await listWorkCopy(params)
      this.editWorkInfo.copyStaffs = data || []
    },
    async _loadWorkCycle() {
      const params = {
        page: 1,
        row: 1,
        workId: this.editWorkInfo.workId
      }
      const { data } = await listWorkCycle(params)
      if (data && data.length > 0) {
        this.editWorkInfo.period = data[0].period
        this.editWorkInfo.workCycle = data[0].workCycle // 设置为周期工作
        this.editWorkInfo.hours = data[0].hours || '24'
        
        // 处理月份、日期和工作日
        if (data[0].periodMonth) {
          this.editWorkInfo.months = data[0].periodMonth.split(',').map(Number)
        }
        if (data[0].periodDay) {
          this.editWorkInfo.days = data[0].periodDay.split(',').map(Number)
        }
        if (data[0].workdays) {
          this.editWorkInfo.workdays = data[0].workdays.split(',').map(Number)
        }
      }
    },
    addWorkContent() {
      this.editWorkInfo.contents.push({
        id: getUuid(),
        content: ''
      })
    },
    deleteWorkContent(content) {
      this.editWorkInfo.contents = this.editWorkInfo.contents.filter(item => item.id !== content.id)
    },
    handleWorkCycleChange(val) {
      if (val === '1001') {
        this.editWorkInfo.period = ''
        this.editWorkInfo.months = []
        this.editWorkInfo.days = []
        this.editWorkInfo.workdays = []
      }
    },
    notifyFile(data) {
      this.editWorkInfo.pathUrls = []
      this.editWorkInfo.pathUrls.push(data.realFileName)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-work-container {
  padding: 20px;

  .staff-tags {
    display: flex;
    flex-wrap: wrap;
    align-items: center;

    .el-tag {
      margin-right: 10px;
      margin-bottom: 5px;
    }
  }

  .flex {
    display: flex;
  }

  .align-center {
    align-items: center;
  }

  .text-right {
    text-align: right;
  }

  .el-checkbox-group {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}
</style>