<template>
  <div class="complaint-detail-container padding">
    <el-card class="box-card">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('complaintDetail.title') }}</span>
        <el-button type="link" size="mini" @click="goBack">
          {{ $t('common.back') }}
        </el-button>
      </div>
      <div class="card-content">
        <el-row :gutter="20" class="info-section text-left">
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.complaintId') }}:</label>
              <span>{{ complaintDetail.complaintId }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.type') }}:</label>
              <span>{{ complaintDetail.typeName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.room') }}:</label>
              <span>{{ complaintDetail.roomName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.contact') }}:</label>
              <span>{{ complaintDetail.complaintName }}</span>
            </div>
          </el-col>
        </el-row>


        <el-row :gutter="20" class="info-section text-left">
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.contactTel') }}:</label>
              <span>{{ complaintDetail.tel }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.status') }}:</label>
              <span>{{ complaintDetail.stateName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.createTime') }}:</label>
              <span>{{ complaintDetail.createTime }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="info-section text-left">
          <el-col :span="24">
            <div class="info-item">
              <label>{{ $t('adminComplaint.table.content') }}:</label>
              <span>{{ complaintDetail.context }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
      <divider></divider>

      <el-tabs v-model="activeTab" @tab-click="handleTabClick(activeTab)">
        <el-tab-pane :label="$t('complaintDetail.workflow')" name="aComplaintDetailEvent">
          <a-complaint-detail-event ref="aComplaintDetailEvent"
            :complaint-id="complaintDetail.complaintId"></a-complaint-detail-event>
        </el-tab-pane>
        <el-tab-pane :label="$t('complaintDetail.evaluation')" name="aComplaintDetailAppraise">
          <a-complaint-detail-appraise ref="aComplaintDetailAppraise"
            :complaint-id="complaintDetail.complaintId"></a-complaint-detail-appraise>
        </el-tab-pane>
        <el-tab-pane :label="$t('complaintDetail.type')" name="aComplaintDetailType">
          <a-complaint-detail-type ref="aComplaintDetailType" :complaint-id="complaintDetail.complaintId"
            :type-cd="complaintDetail.typeCd"></a-complaint-detail-type>
        </el-tab-pane>
      </el-tabs>

    </el-card>
  </div>
</template>

<script>
import { getComplaintDetail } from '@/api/complaint/adminComplaintDetailApi'
import AComplaintDetailEvent from '@/components/complaint/aComplaintDetailEvent'
import AComplaintDetailAppraise from '@/components/complaint/aComplaintDetailAppraise'
import AComplaintDetailType from '@/components/complaint/aComplaintDetailType'
import divider from '@/components/system/divider'

export default {
  name: 'AdminComplaintDetail',
  components: {
    AComplaintDetailEvent,
    AComplaintDetailAppraise,
    AComplaintDetailType,
    divider
  },
  data() {
    return {
      complaintDetail: {
        complaintId: '',
        typeName: '',
        typeCd: '',
        roomName: '',
        complaintName: '',
        tel: '',
        stateName: '',
        createTime: '',
        context: ''
      },
      activeTab: 'aComplaintDetailEvent'
    }
  },
  created() {
    this.complaintDetail.complaintId = this.$route.query.complaintId
    if (this.$route.query.currentTab) {
      this.activeTab = this.$route.query.currentTab
    }
    this.loadComplaintDetail()
  },
  methods: {
    async loadComplaintDetail() {
      try {
        const params = {
          complaintId: this.complaintDetail.complaintId,
          page: 1,
          row: 1,
          ownerTypeCd: '1001'
        }
        const { data } = await getComplaintDetail(params)
        if (data && data.length > 0) {
          Object.assign(this.complaintDetail, data[0])
        }
      } catch (error) {
        this.$message.error(this.$t('complaintDetail.fetchError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    handleTabClick(tab) {
      this.activeTab = tab
      setTimeout(() => {
        this.$refs[tab].loadData()
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-detail-container {


  .header-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .title {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .info-section {
    margin-bottom: 15px;
    color: #666;
  }

  .info-item {
    margin-bottom: 10px;

    label {
      margin-right: 10px;
    }
  }
}
</style>