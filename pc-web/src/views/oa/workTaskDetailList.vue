<template>
  <div class="work-task-detail-container">
    <el-card class="box-card">
      <div class="flex justify-between">
        <div class="text-title">{{ $t('workTaskDetail.title') }}</div>
        <div>
          <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="margin-top">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.workId') }}：
              </label>
              <label>{{ workTaskDetailInfo.workId }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.workName') }}：
              </label>
              <label>{{ workTaskDetailInfo.workName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.typeName') }}：
              </label>
              <label>{{ workTaskDetailInfo.typeName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.workCycle') }}：
              </label>
              <label>{{ workTaskDetailInfo.workCycle == '1001' ? $t('workTaskDetail.once') :
                $t('workTaskDetail.periodic') }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.startTime') }}：
              </label>
              <label>{{ workTaskDetailInfo.startTime }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.endTime') }}：
              </label>
              <label>{{ workTaskDetailInfo.endTime }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.createUserName') }}：
              </label>
              <label>{{ workTaskDetailInfo.createUserName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.curStaffName') }}：
              </label>
              <label>{{ workTaskDetailInfo.curStaffName || '-' }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.curCopyName') }}：
              </label>
              <label>{{ workTaskDetailInfo.curCopyName || '-' }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.stateName') }}：
              </label>
              <label>{{ workTaskDetailInfo.stateName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.createTime') }}：
              </label>
              <label>{{ workTaskDetailInfo.createTime }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('workTaskDetail.attachment') }}：
              </label>
              <label>
                <el-link v-if="workTaskDetailInfo.pathUrl" :href="workTaskDetailInfo.pathUrl" target="_blank">
                  {{ $t('common.download') }}
                </el-link>
                <span v-else>-</span>
              </label>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 工作单内容 -->
    <el-card class="margin-top">
      <el-table :data="workTaskDetailInfo.items" border style="width: 100%">
        <el-table-column v-if="workTaskDetailInfo.todo == 'ON'"  :label="$t('workTaskDetail.select')" width="250"
          align="center">
          <template slot-scope="{row}">
            <el-radio v-if="row.state == 'W'" v-model="workTaskDetailInfo.audit.itemId" :label="row.itemId"
              @change="selectContent(row)"></el-radio>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('workTaskDetail.content')" prop="content">
          <template slot-scope="{row, $index}">
            <div class="flex justify-start">
              <div>{{ $index + 1 }}、</div>
              <div v-html="row.content"></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('workTaskDetail.status')" width="100" align="center">
          <template slot-scope="{row}">
            {{ row.state == 'W' ? $t('workTaskDetail.pending') : $t('workTaskDetail.processed') }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 工作单流转 -->
    <el-card v-if="workTaskDetailInfo.todo != 'ON' && workTaskDetailInfo.events && workTaskDetailInfo.events.length > 0"
      class="margin-top ">
      <div slot="header" class="text-left">
        <span>{{ $t('workTaskDetail.flow') }}</span>
      </div>
      <el-table :data="workTaskDetailInfo.events" border style="width: 100%">
        <el-table-column :label="$t('workTaskDetail.preStaff')" align="center" prop="preStaffName"></el-table-column>
        <el-table-column :label="$t('workTaskDetail.staff')" align="center" prop="staffName"></el-table-column>
        <el-table-column :label="$t('workTaskDetail.validTime')" align="center" prop="startTime">
          <template slot-scope="{row}">
            {{ row.startTime }}<br>~{{ row.endTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('workTaskDetail.time')" align="center" prop="endTime"></el-table-column>
        <el-table-column :label="$t('workTaskDetail.type')" align="center" prop="eventType">
          <template slot-scope="{row}">
            <span v-if="row.eventType == '1001'">{{ $t('workTaskDetail.handler') }}</span>
            <span v-else-if="row.eventType == '2002'">{{ $t('workTaskDetail.copy') }}</span>
            <span v-else>{{ $t('workTaskDetail.process') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('workTaskDetail.remark')" align="center" prop="remark"></el-table-column>
      </el-table>
    </el-card>

    <!-- 工作单附件 -->
    <el-card v-if="workTaskDetailInfo.todo != 'ON' && workTaskDetailInfo.files && workTaskDetailInfo.files.length > 0"
      class="margin-top">
      <div slot="header" class="text-left">
        <span>{{ $t('workTaskDetail.attachmentList') }}</span>
      </div>
      <el-table :data="workTaskDetailInfo.files" border style="width: 100%">
        <el-table-column :label="$t('workTaskDetail.staff')" align="center"></el-table-column>
        <el-table-column :label="$t('workTaskDetail.attachment')" align="center">
          <template slot-scope="{row}">
            <el-link :href="row.pathUrl" target="_blank">{{ $t('common.download') }}</el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 工单办理 -->
    <el-card v-if="workTaskDetailInfo.todo == 'ON' && workTaskDetailInfo.audit.itemId" class="margin-top text-left">
      <div slot="header" class="text-left">
        <span>{{ $t('workTaskDetail.process') }}</span>
      </div>
      <el-form label-width="120px">
        <el-form-item :label="$t('workTaskDetail.action')">
          <el-select v-model="workTaskDetailInfo.audit.auditCode" style="width:100%">
            <el-option :label="$t('workTaskDetail.pleaseSelect')" value="" disabled></el-option>
            <el-option :label="$t('workTaskDetail.process')" value="C"></el-option>
            <el-option :label="$t('workTaskDetail.transfer')" value="T"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="workTaskDetailInfo.audit.auditCode == 'T'" :label="$t('workTaskDetail.nextHandler')">
          <el-input v-model="workTaskDetailInfo.audit.staffName" :placeholder="$t('workTaskDetail.selectNextHandler')"
            disabled>
            <el-button slot="append" icon="el-icon-search" @click="chooseStaff"></el-button>
          </el-input>
        </el-form-item>

        <el-form-item :label="$t('workTaskDetail.attachment')">
          <upload-file ref="uploadFile" @notify="handleNotifyFile"></upload-file>
        </el-form-item>

        <el-form-item :label="$t('workTaskDetail.remark')">
          <el-input type="textarea" :placeholder="$t('workTaskDetail.inputRemark')"
            v-model="workTaskDetailInfo.audit.auditMessage"></el-input>
        </el-form-item>

        <el-form-item class="text-right">
          <el-button type="primary" @click="auditSubmit">{{ $t('common.submit') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <select-staff ref="selectStaff" @selectStaff="handleStaffChange"></select-staff>
  </div>
</template>

<script>
import { getWorkDetail, getWorkTaskItems, getWorkEvents, getWorkFiles, finishWorkTask } from '@/api/oa/workTaskDetailApi'
import UploadFile from '@/components/upload/uploadFile'
import SelectStaff from '@/components/staff/SelectStaff'

export default {
  name: 'WorkTaskDetailList',
  components: {
    UploadFile,
    SelectStaff
  },
  data() {
    return {
      workTaskDetailInfo: {
        viewWorkFlag: '',
        workId: "",
        wtId: '',
        workName: '',
        typeName: "",
        workCycle: "",
        startTime: "",
        endTime: "",
        createUserName: "",
        curStaffName: "",
        curCopyName: "",
        stateName: "",
        createTime: '',
        items: [],
        pathUrl: '',
        taskId: '',
        todo: 'OFF',
        events: [],
        files: [],
        audit: {
          taskId: '',
          auditCode: 'C',
          auditMessage: this.$t('workTaskDetail.processed'),
          staffId: '',
          staffName: '',
          pathUrls: '',
          itemId: '',
        }
      }
    }
  },
  created() {
    this.workTaskDetailInfo.workId = this.$route.query.workId
    this.workTaskDetailInfo.taskId = this.$route.query.taskId
    const todo = this.$route.query.todo
    if (todo) {
      this.workTaskDetailInfo.todo = todo
      this.workTaskDetailInfo.audit.taskId = this.$route.query.taskId
    }
    if (!this.workTaskDetailInfo.workId) return

    this.loadWorkInfo()
    this.loadWorkTaskItem()
    this.loadWorkEventData()
    this.loadWorkFileData()
  },
  methods: {
    async loadWorkInfo() {
      try {
        const params = {
          workId: this.workTaskDetailInfo.workId,
          page: 1,
          row: 1,
        }
        const { data } = await getWorkDetail(params)
        Object.assign(this.workTaskDetailInfo, data[0])
      } catch (error) {
        console.error('获取工单详情失败:', error)
      }
    },
    async loadWorkTaskItem() {
      try {
        const params = {
          taskId: this.workTaskDetailInfo.taskId,
          page: 1,
          row: 100,
        }
        const { data } = await getWorkTaskItems(params)
        this.workTaskDetailInfo.items = data
      } catch (error) {
        console.error('获取工单项失败:', error)
      }
    },
    async loadWorkEventData() {
      try {
        const params = {
          taskId: this.workTaskDetailInfo.taskId,
          workId: this.workTaskDetailInfo.workId,
          page: 1,
          row: 100
        }
        const { data } = await getWorkEvents(params)
        this.workTaskDetailInfo.events = data
      } catch (error) {
        console.error('获取工单事件失败:', error)
      }
    },
    async loadWorkFileData() {
      try {
        const params = {
          taskId: this.workTaskDetailInfo.taskId,
          workId: this.workTaskDetailInfo.workId,
          page: 1,
          row: 100
        }
        const { data } = await getWorkFiles(params)
        this.workTaskDetailInfo.files = data
      } catch (error) {
        console.error('获取工单文件失败:', error)
      }
    },
    selectContent(item) {
      if (item.state == 'C') return
      this.workTaskDetailInfo.audit.itemId = item.itemId
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.workTaskDetailInfo.audit)
    },
    handleStaffChange(staff) {
      this.workTaskDetailInfo.audit.staffId = staff.staffId
      this.workTaskDetailInfo.audit.staffName = staff.staffName
    },
    handleNotifyFile(file) {
      this.workTaskDetailInfo.audit.pathUrls = []
      this.workTaskDetailInfo.audit.pathUrls.push(file.realFileName)
    },
    async auditSubmit() {
      try {
        await finishWorkTask(this.workTaskDetailInfo.audit)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('workTaskDetail.submitFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.work-task-detail-container {
  padding: 20px;
}

.margin-top {
  margin-top: 20px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.justify-start {
  justify-content: flex-start;
}

.text-title {
  font-size: 18px;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

.col-form-label {
  margin-right: 10px;
}
</style>