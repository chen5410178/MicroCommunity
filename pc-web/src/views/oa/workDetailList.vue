<template>
  <div class="work-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div class="flex justify-between">
          <div class="text-title">{{ $t('workDetail.title') }}</div>
          <div>
            <el-button type="primary" size="small" @click="goBack">
              <i class="el-icon-close"></i>{{ $t('common.back') }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 业主信息 -->
      <div class="margin-top">
        <el-row :gutter="20">
          <el-col :span="24" class="text-left">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.workId') }}:
                  </label>
                  <label>{{ workDetailInfo.workId }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.workName') }}:
                  </label>
                  <label>{{ workDetailInfo.workName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.typeName') }}:
                  </label>
                  <label>{{ workDetailInfo.typeName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.workCycle') }}:
                  </label>
                  <label>{{ workDetailInfo.workCycle == '1001' ? $t('workDetail.once') : $t('workDetail.periodic')
                  }}</label>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.startTime') }}:
                  </label>
                  <label>{{ workDetailInfo.startTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.endTime') }}:
                  </label>
                  <label>{{ workDetailInfo.endTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.createUserName') }}:
                  </label>
                  <label>{{ workDetailInfo.createUserName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.curStaffName') }}:
                  </label>
                  <label>{{ workDetailInfo.curStaffName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.curCopyName') }}:
                  </label>
                  <label>{{ workDetailInfo.curCopyName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.stateName') }}:
                  </label>
                  <label>{{ workDetailInfo.stateName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('workDetail.createTime') }}:
                  </label>
                  <label>{{ workDetailInfo.createTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group flex justify-start">
                  <label class="col-form-label">
                    {{ $t('workDetail.attachment') }}:
                  </label>
                  <div class="flex justify-start" v-if="workDetailInfo.pathUrls">
                    <div v-for="(url, index) in workDetailInfo.pathUrls" :key="index">
                      <div v-if="url.endsWith('jpg') || url.endsWith('png')">
                        <img style="width: 60px; height: 60px;" class="border-radius" :src="url"
                          @click="viewTaskFileImg(url)" />
                      </div>
                      <div v-else>
                        <a :href="url" target="_blank">{{ $t('common.download') }}</a>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>

      <divider></divider>

      <div class="margin-top-sm">
        <el-tabs v-model="workDetailInfo._currentTab" @tab-click="changeTab(workDetailInfo._currentTab)">
          <el-tab-pane :label="$t('workDetail.content')" name="workDetailContent"></el-tab-pane>
          <el-tab-pane :label="$t('workDetail.handler')" name="workDetailTask"></el-tab-pane>
          <el-tab-pane :label="$t('workDetail.handlerContent')" name="workDetailTaskItem"></el-tab-pane>
          <el-tab-pane :label="$t('workDetail.copyPerson')" name="workDetailCopy"></el-tab-pane>
          <el-tab-pane :label="$t('workDetail.workFlow')" name="workDetailEvent"></el-tab-pane>
          <el-tab-pane :label="$t('workDetail.flowAttachment')" name="workDetailFile"></el-tab-pane>
          <el-tab-pane :label="$t('workDetail.workType')" name="workDetailType"></el-tab-pane>
          <el-tab-pane v-if="workDetailInfo.workCycle == '2002'" :label="$t('workDetail.workCycle')"
            name="workDetailCycle"></el-tab-pane>
        </el-tabs>
      </div>

      <div v-if="workDetailInfo._currentTab == 'workDetailContent'">
        <div class="padding flex justify-start" v-for="(item, index) in workDetailInfo.contents" :key="index">
          <div>{{ item.seqNum }}、</div>
          <div v-html="item.content" style="width: 80%;" class="text-left"></div>
        </div>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailTask'">
        <work-detail-task ref="workDetailTask"></work-detail-task>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailTaskItem'">
        <work-detail-task-item ref="workDetailTaskItem"></work-detail-task-item>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailCopy'">
        <work-detail-copy ref="workDetailCopy"></work-detail-copy>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailEvent'">
        <work-detail-event ref="workDetailEvent"></work-detail-event>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailFile'">
        <work-detail-file ref="workDetailFile"></work-detail-file>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailType'">
        <work-detail-type ref="workDetailType"></work-detail-type>
      </div>
      <div v-if="workDetailInfo._currentTab == 'workDetailCycle'">
        <work-detail-cycle ref="workDetailCycle"></work-detail-cycle>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getWorkPoolDetail } from '@/api/oa/workDetailApi'
import WorkDetailTask from '@/components/oa/WorkDetailTask'
import WorkDetailTaskItem from '@/components/oa/WorkDetailTaskItem'
import WorkDetailCopy from '@/components/oa/WorkDetailCopy'
import WorkDetailEvent from '@/components/oa/WorkDetailEvent'
import WorkDetailFile from '@/components/oa/WorkDetailFile'
import WorkDetailType from '@/components/oa/WorkDetailType'
import WorkDetailCycle from '@/components/oa/WorkDetailCycle'
import Divider from '@/components/system/divider'

export default {
  name: 'WorkDetailList',
  components: {
    WorkDetailTask,
    WorkDetailTaskItem,
    WorkDetailCopy,
    WorkDetailEvent,
    WorkDetailFile,
    WorkDetailType,
    WorkDetailCycle,
    Divider
  },
  data() {
    return {
      workDetailInfo: {
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
        content: '',
        pathUrls: [],
        _currentTab: 'workDetailContent',
        contents: []
      }
    }
  },
  created() {
    this.workDetailInfo.workId = this.$route.query.workId
    if (!this.workDetailInfo.workId) {
      return
    }
    const currentTab = this.$route.query.currentTab
    if (currentTab) {
      this.workDetailInfo._currentTab = currentTab
    }
    this.loadWorkInfo()
    this.changeTab(this.workDetailInfo._currentTab)
  },
  methods: {
    async loadWorkInfo() {
      try {
        const params = {
          workId: this.workDetailInfo.workId,
          page: 1,
          row: 1,
        }
        const { data } = await getWorkPoolDetail(params)
        this.workDetailInfo = { ...this.workDetailInfo, ...data[0] }
      } catch (error) {
        console.error('获取工作单详情失败:', error)
      }
    },
    changeTab(tab) {
      this.workDetailInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].open({
            taskId: this.workDetailInfo.taskId,
            workId: this.workDetailInfo.workId,
            wtId: this.workDetailInfo.wtId,
            contents: this.workDetailInfo.contents
          })
        }
      }, 500)
    },
    viewTaskFileImg(url) {
      this.$emit('viewImage', { url })
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.work-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .text-title {
    font-size: 18px;
    font-weight: bold;
  }

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }

  .padding {
    padding: 10px;
  }

  .border-radius {
    border-radius: 4px;
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

  .form-group {
    margin-bottom: 15px;

    .col-form-label {
      display: inline-block;
      margin-right: 10px;
    }
  }
}
</style>