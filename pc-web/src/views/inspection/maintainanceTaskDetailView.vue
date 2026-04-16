<template>
    <div class="maintainance-task-detail-view">
      <el-card class="detail-card">
        <div slot="header" class="flex justify-between">
          <span>{{ $t('maintainanceTaskDetailView.title') }}</span>
          <el-button type="primary" size="mini" @click="goBack">
            <i class="el-icon-back"></i>
            {{ $t('maintainanceTaskDetailView.back') }}
          </el-button>
        </div>
  
        <el-card shadow="never" class="info-card text-left">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskDetailView.basicInfo') }}</span>
          </div>
  
          <el-row :gutter="24" class="">
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.taskDetailId') }}：</span>
                <span>{{ detail.taskDetailId || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.machineName') }}：</span>
                <span>{{ detail.machineName || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.planUserName') }}：</span>
                <span>{{ detail.planUserName || '-' }}</span>
              </div>
            </el-col>
          </el-row>
  
          <el-row :gutter="24">
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.planTime') }}：</span>
                <span>{{ formatPlanTime(detail.planInsTime, detail.planEndTime) }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.actUserName') }}：</span>
                <span>{{ detail.actUserName || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.inspectionTime') }}：</span>
                <span>{{ detail.inspectionTime || '-' }}</span>
              </div>
            </el-col>
          </el-row>
  
          <el-row :gutter="24">
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.stateName') }}：</span>
                <span>{{ detail.stateName || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.planName') }}：</span>
                <span>{{ detail.planName || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">{{ $t('maintainanceTaskDetailView.fields.standardName') }}：</span>
                <span>{{ detail.standardName || '-' }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
  
        <el-card shadow="never" class="info-card text-left">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskDetailView.photosTitle') }}</span>
          </div>
  
          <div v-if="photoList.length" class="photo-list">
            <el-image
              v-for="(photo, index) in photoList"
              :key="index"
              :src="photo"
              :preview-src-list="photoList"
              fit="cover"
              class="photo-item"
            />
          </div>
          <div v-else class="empty-text">{{ $t('maintainanceTaskDetailView.noPhotos') }}</div>
        </el-card>
  
        <el-card shadow="never" class="info-card text-left">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskDetailView.descriptionTitle') }}</span>
          </div>
          <div class="description">
            {{ detail.description || $t('maintainanceTaskDetailView.noDescription') }}
          </div>
        </el-card>
      </el-card>
  
      <view-image ref="viewImage" />
    </div>
  </template>
  
  <script>
  import { getMaintainanceTaskDetailList } from '@/api/inspection/maintainanceTaskDetailsApi'
  import ViewImage from '@/components/system/viewImage'
  import { messages } from './maintainanceTaskDetailViewLang'
  
  export default {
    name: 'MaintainanceTaskDetailView',
    components: {
      ViewImage
    },
    i18n: {
      messages
    },
    data() {
      return {
        detail: {
          taskDetailId: '',
          machineName: '',
          planName: '',
          standardName: '',
          planUserName: '',
          planInsTime: '',
          planEndTime: '',
          actUserName: '',
          inspectionTime: '',
          stateName: '',
          description: '',
          photos: []
        },
        loading: false
      }
    },
    computed: {
      photoList() {
        if (!this.detail.photos || !this.detail.photos.length) {
          return []
        }
        return this.detail.photos.map(photo => photo.url).filter(Boolean)
      }
    },
    created() {
      this.fetchDetail()
    },
    methods: {
      async fetchDetail() {
        const taskDetailId = this.$route.query.taskDetailId
        if (!taskDetailId) {
          return
        }
        try {
          this.loading = true
          const params = {
            page: 1,
            row: 1,
            taskDetailId
          }
          const res = await getMaintainanceTaskDetailList(params)
          if (res && res.data && res.data.length) {
            this.detail = { ...this.detail, ...res.data[0] }
          }
        } catch (error) {
          this.$message.error(this.$t('maintainanceTaskDetailView.fetchError'))
        } finally {
          this.loading = false
        }
      },
      formatPlanTime(start, end) {
        if (!start && !end) {
          return '-'
        }
        if (!start) {
          return end
        }
        if (!end) {
          return start
        }
        return `${start} ~ ${end}`
      },
      goBack() {
        if (window.history.length > 1) {
          this.$router.back()
        } else {
          this.$router.push({ path: '/pages/property/maintainanceTaskDetails' })
        }
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .maintainance-task-detail-view {
    padding: 20px;
  
    .detail-card {
      .card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
  
      .info-card {
        margin-bottom: 20px;
      }
  
      .section-title {
        font-weight: 600;
      }
  
      .info-item {
        margin-bottom: 16px;
        .label {
          color: #606266;
          margin-right: 4px;
        }
      }
  
      .photo-list {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
  
        .photo-item {
          width: 120px;
          height: 120px;
          border-radius: 4px;
          overflow: hidden;
          border: 1px solid #ebeef5;
        }
      }
  
      .empty-text {
        color: #999;
      }
  
      .description {
        min-height: 80px;
        line-height: 1.6;
        white-space: pre-wrap;
      }
    }
  }
  </style>
  
  