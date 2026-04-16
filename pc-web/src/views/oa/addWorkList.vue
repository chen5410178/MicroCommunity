<template>
  <div class="add-work-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addWork.title') }}</span>
      </div>

      <el-form ref="form" :model="addWorkInfo" label-width="120px" label-position="right" class="text-left">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addWork.workName')" prop="workName">
              <el-input v-model.trim="addWorkInfo.workName" :placeholder="$t('addWork.workNamePlaceholder')"
                maxlength="20" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addWork.workType')" prop="wtId">
              <el-select v-model="addWorkInfo.wtId" :placeholder="$t('addWork.workTypePlaceholder')" style="width:100%"
                clearable>
                <el-option v-for="item in addWorkInfo.workTypes" :key="item.wtId" :label="item.typeName"
                  :value="item.wtId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addWork.processor')">
              <div class="staff-tags">
                <el-tag v-for="(staff, index) in addWorkInfo.staffs" :key="index" closable
                  @close="_deleteWorkStaff(staff)">
                  {{ staff.staffName }}
                </el-tag>
                <el-button type="text" @click="_chooseWorkStaff">
                  <i class="el-icon-search"></i>{{ $t('addWork.choose') }}
                </el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addWork.cc')">
              <div class="staff-tags">
                <el-tag v-for="(staff, index) in addWorkInfo.copyStaffs" :key="index" closable
                  @close="_deleteCopyWorkStaff(staff)">
                  {{ staff.staffName }}
                </el-tag>
                <el-button type="text" @click="_chooseCopyWorkStaff">
                  <i class="el-icon-search"></i>{{ $t('addWork.choose') }}
                </el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addWork.workCycle')" prop="workCycle">
              <el-select v-model="addWorkInfo.workCycle" :placeholder="$t('addWork.workCyclePlaceholder')"
                style="width:100%" @change="handleWorkCycleChange">
                <el-option :label="$t('addWork.onceWork')" value="1001" />
                <el-option :label="$t('addWork.periodWork')" value="2002" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addWork.attachment')">
              <upload-file ref="uploadFile" @notify="notifyFile" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addWork.startTime')" prop="startTime">
              <el-date-picker v-model="addWorkInfo.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('addWork.startTimePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addWork.endTime')" prop="endTime">
              <el-date-picker v-model="addWorkInfo.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('addWork.endTimePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="addWorkInfo.workCycle === '2002'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="$t('addWork.period')" prop="period">
                <el-select v-model="addWorkInfo.period" :placeholder="$t('addWork.periodPlaceholder')"
                  style="width:100%">
                  <el-option :label="$t('addWork.monthDay')" value="2020022" />
                  <el-option :label="$t('addWork.week')" value="2020023" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('addWork.hours')" prop="hours">
                <el-input v-model.trim="addWorkInfo.hours" :placeholder="$t('addWork.hoursPlaceholder')">
                  <template slot="append">{{ $t('addWork.hoursUnit') }}</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <template v-if="addWorkInfo.period === '2020022'">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('addWork.month')">
                  <el-checkbox-group v-model="addWorkInfo.months">
                    <el-checkbox v-for="index in 12" :key="index" :label="index">
                      {{ index }}{{ $t('addWork.monthUnit') }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('addWork.day')">
                  <el-checkbox-group v-model="addWorkInfo.days">
                    <el-checkbox v-for="index in 31" :key="index" :label="index">
                      {{ index }}{{ $t('addWork.dayUnit') }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
          </template>

          <template v-if="addWorkInfo.period === '2020023'">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('addWork.week')">
                  <el-checkbox-group v-model="addWorkInfo.workdays">
                    <el-checkbox v-for="(day, index) in weekDays" :key="index" :label="index + 1">
                      {{ day }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
          </template>
        </template>

        <el-row v-for="(item, index) in addWorkInfo.contents" :key="item.id" :gutter="20">
          <el-col :span="18">
            <el-form-item :label="`${$t('addWork.content')}${index + 1}`">
              <el-input v-model="item.content" type="textarea" :rows="5"
                :placeholder="$t('addWork.contentPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="6" class="flex align-center">
            <el-button v-if="index === 0" type="text" @click="addWorkContent">
              {{ $t('addWork.addContent') }}
            </el-button>
            <el-button v-else type="text" @click="deleteWorkContent(item)">
              {{ $t('addWork.deleteContent') }}
            </el-button>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="goBack">
              <i class="el-icon-arrow-left"></i>{{ $t('common.back') }}
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
import { saveWorkPool, listWorkType } from '@/api/oa/addWorkApi'
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'
import UploadFile from '@/components/upload/uploadFile'
import { getUuid } from '@/utils/commonUtil'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'AddWorkList',
  components: {
    SelectStaff,
    UploadFile
  },
  data() {
    return {
      chooseStaffFlag:'1',
      addWorkInfo: {
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
        this.$t('addWork.monday'),
        this.$t('addWork.tuesday'),
        this.$t('addWork.wednesday'),
        this.$t('addWork.thursday'),
        this.$t('addWork.friday'),
        this.$t('addWork.saturday'),
        this.$t('addWork.sunday')
      ]
    }
  },
  created() {
    this.addWorkInfo.communityId = getCommunityId()
    this._listWorkTypes()
    this.addWorkContent()
    this.initDateTime()
  },
  methods: {
    initDateTime() {
      const now = new Date()
      this.addWorkInfo.startTime = dateFormat(now)+' 00:00:00'
      const tomorrow = new Date(now)
      tomorrow.setDate(tomorrow.getDate() + 1)
      this.addWorkInfo.endTime = dateFormat(tomorrow)+' 23:59:59'
    },
    addWorkValidate() {
      return this.$refs.form.validate()
    },
    async saveWorkPool() {
      try {
        const valid = await this.addWorkValidate()
        if (!valid) return

        const params = {
          ...this.addWorkInfo,
        }

        const res = await saveWorkPool(params)
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
          communityId: this.addWorkInfo.communityId
        }
        const res = await listWorkType(params)
        this.addWorkInfo.workTypes = res.data
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
      if(this.chooseStaffFlag === '1'){
      this.addWorkInfo.staffs.push({
        staffId: staff.userId,
        staffName: staff.userName
      })
      }else{
        this.addWorkInfo.copyStaffs.push({
          staffId: staff.userId,
          staffName: staff.userName
        })
      }
    },
    _deleteWorkStaff(staff) {
      this.addWorkInfo.staffs = this.addWorkInfo.staffs.filter(item => item.staffId !== staff.staffId)
    },
    _chooseCopyWorkStaff() {
      this.chooseStaffFlag = '2'
      this.$refs.selectStaff.open({
        call: () => {
        }
      })
    },
    _deleteCopyWorkStaff(staff) {
      this.addWorkInfo.copyStaffs = this.addWorkInfo.copyStaffs.filter(item => item.staffId !== staff.staffId)
    },
    addWorkContent() {
      this.addWorkInfo.contents.push({
        id: getUuid(),
        content: ''
      })
    },
    deleteWorkContent(content) {
      this.addWorkInfo.contents = this.addWorkInfo.contents.filter(item => item.id !== content.id)
    },
    handleWorkCycleChange(val) {
      if (val === '1001') {
        this.addWorkInfo.period = ''
        this.addWorkInfo.months = []
        this.addWorkInfo.days = []
        this.addWorkInfo.workdays = []
      }
    },
    notifyFile(data) {
      this.addWorkInfo.pathUrls = []
      this.addWorkInfo.pathUrls.push(data.realFileName)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-work-container {
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
}
</style>