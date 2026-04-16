<template>
  <div class="admin-work-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('adminWorkDetail.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
      </div>

      <div class="work-info-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.workId') }}:</label>
              <span>{{ adminWorkDetailInfo.workId }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.workName') }}:</label>
              <span>{{ adminWorkDetailInfo.workName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.typeName') }}:</label>
              <span>{{ adminWorkDetailInfo.typeName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.workCycle') }}:</label>
              <span>{{ adminWorkDetailInfo.workCycle == '1001' ? $t('adminWorkDetail.once') : $t('adminWorkDetail.periodic') }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.startTime') }}:</label>
              <span>{{ adminWorkDetailInfo.startTime }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.endTime') }}:</label>
              <span>{{ adminWorkDetailInfo.endTime }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.createUserName') }}:</label>
              <span>{{ adminWorkDetailInfo.createUserName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.curStaffName') }}:</label>
              <span>{{ adminWorkDetailInfo.curStaffName }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.curCopyName') }}:</label>
              <span>{{ adminWorkDetailInfo.curCopyName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.stateName') }}:</label>
              <span>{{ adminWorkDetailInfo.stateName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.createTime') }}:</label>
              <span>{{ adminWorkDetailInfo.createTime }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminWorkDetail.attachment') }}:</label>
              <span>
                <el-link v-if="adminWorkDetailInfo.pathUrl" :href="adminWorkDetailInfo.pathUrl" target="_blank">
                  {{ $t('common.download') }}
                </el-link>
                <span v-else>-</span>
              </span>
            </div>
          </el-col>
        </el-row>
      </div>

      <divider></divider>

      <el-tabs v-model="adminWorkDetailInfo._currentTab" @tab-click="changeTab(adminWorkDetailInfo._currentTab)">
        <el-tab-pane :label="$t('adminWorkDetail.content')" name="adminWorkDetailContent">
          <div class="content-section" v-for="(item,index) in adminWorkDetailInfo.contents" :key="index">
            <div>{{item.seqNum}}、</div>
            <div v-html="item.content" style="width: 80%;" class="text-left"></div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('adminWorkDetail.processor')" name="aWorkDetailTask">
          <a-work-detail-task ref="aWorkDetailTask" :work-id="adminWorkDetailInfo.workId"></a-work-detail-task>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('adminWorkDetail.processContent')" name="aWorkDetailTaskItem">
          <a-work-detail-task-item ref="aWorkDetailTaskItem" :work-id="adminWorkDetailInfo.workId" :contents="adminWorkDetailInfo.contents"></a-work-detail-task-item>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('adminWorkDetail.copyTo')" name="aWorkDetailCopy">
          <a-work-detail-copy ref="aWorkDetailCopy" :work-id="adminWorkDetailInfo.workId"></a-work-detail-copy>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('adminWorkDetail.workFlow')" name="aWorkDetailEvent">
          <a-work-detail-event ref="aWorkDetailEvent" :work-id="adminWorkDetailInfo.workId" :contents="adminWorkDetailInfo.contents"></a-work-detail-event>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('adminWorkDetail.flowAttachment')" name="aWorkDetailFile">
          <a-work-detail-file ref="aWorkDetailFile" :work-id="adminWorkDetailInfo.workId" :contents="adminWorkDetailInfo.contents"></a-work-detail-file>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('adminWorkDetail.workType')" name="aWorkDetailType">
          <a-work-detail-type ref="aWorkDetailType" :wt-id="adminWorkDetailInfo.wtId"></a-work-detail-type>
        </el-tab-pane>
        
        <el-tab-pane v-if="adminWorkDetailInfo.workCycle == '2002'" :label="$t('adminWorkDetail.workCycle')" name="aWorkDetailCycle">
          <a-work-detail-cycle ref="aWorkDetailCycle" :work-id="adminWorkDetailInfo.workId"></a-work-detail-cycle>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getWorkDetail } from '@/api/work/adminWorkDetailApi'
import AWorkDetailTask from '@/components/work/aWorkDetailTask'
import AWorkDetailTaskItem from '@/components/work/aWorkDetailTaskItem'
import AWorkDetailCopy from '@/components/work/aWorkDetailCopy'
import AWorkDetailEvent from '@/components/work/aWorkDetailEvent'
import AWorkDetailFile from '@/components/work/aWorkDetailFile'
import AWorkDetailType from '@/components/work/aWorkDetailType'
import AWorkDetailCycle from '@/components/work/aWorkDetailCycle'
import divider from '@/components/system/divider'

export default {
  name: 'AdminWorkDetail',
  components: {
    AWorkDetailTask,
    AWorkDetailTaskItem,
    AWorkDetailCopy,
    AWorkDetailEvent,
    AWorkDetailFile,
    AWorkDetailType,
    AWorkDetailCycle,
    divider
  },
  data() {
    return {
      adminWorkDetailInfo: {
        workId: '',
        wtId: '',
        workName: '',
        typeName: '',
        workCycle: '',
        startTime: '',
        endTime: '',
        createUserName: '',
        curStaffName: '',
        curCopyName: '',
        stateName: '',
        createTime: '',
        pathUrl: '',
        _currentTab: 'adminWorkDetailContent',
        contents: []
      }
    }
  },
  created() {
    this.adminWorkDetailInfo.workId = this.$route.query.workId
    if (this.$route.query.currentTab) {
      this.adminWorkDetailInfo._currentTab = this.$route.query.currentTab
    }
    this.loadWorkInfo()
  },
  methods: {
    async loadWorkInfo() {
      try {
        const params = {
          workId: this.adminWorkDetailInfo.workId,
          page: 1,
          row: 1
        }
        const { data } = await getWorkDetail(params)
        if (data && data.length > 0) {
          Object.assign(this.adminWorkDetailInfo, data[0])
        }
      } catch (error) {
        this.$message.error(this.$t('adminWorkDetail.fetchError'))
      }
    },
    changeTab(tab) {
      setTimeout(()=>{
        if (this.$refs[tab] ) {
          this.$refs[tab].loadData()
        }
      },500)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-work-detail-container {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
  }
  
  .work-info-section {
    margin-bottom: 20px;
    
    .info-item {
      margin-bottom: 15px;
      text-align: left;
      
      label {
        color: #909399;
        font-size: 14px;
        margin-bottom: 5px;
        margin-right: 5px;
      }
      
      span {
        font-size: 14px;
        color: #606266;
      }
    }
  }
  
  .content-section {
    display: flex;
    padding: 10px 0;
    border-bottom: 1px solid #ebeef5;
    
    &:last-child {
      border-bottom: none;
    }
  }
}
</style>