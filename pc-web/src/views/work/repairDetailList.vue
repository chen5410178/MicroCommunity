<template>
  <div class="repair-detail-container padding">
  <el-card class="">
    <div class="flex justify-between">
      <div class="text-title">{{ $t('repairDetail.title') }}</div>
      <div>
        <el-button type="primary" size="small" @click="printRepairDetail">
          {{ $t('repairDetail.print') }}
        </el-button>
        <el-button type="primary" size="small" @click="goBack">
          {{ $t('repairDetail.back') }}
        </el-button>
      </div>
    </div>

    <div class="margin-top">
      <el-form :model="repairDetailInfo" label-width="auto" class="text-left">
        <el-row :gutter="20">
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.orderCode')">
              <span>{{ repairDetailInfo.repairId }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.repairType')">
              <span>{{ repairDetailInfo.repairTypeName }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.repairer')">
              <span>{{ repairDetailInfo.repairName }}</span>
            </el-form-item>
          </el-col>

          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.contact')">
              <span>{{ repairDetailInfo.tel }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.location')">
              <span>{{ repairDetailInfo.repairObjName }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.appointmentStart')">
              <span>{{ repairDetailInfo.appointmentTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.appointmentEnd')">
              <span>{{ repairDetailInfo.timeout }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.submitDuration')">
              <span>{{ repairDetailInfo.submitHours || '0' }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.finishTime')">
              <span>{{ repairDetailInfo.finishTime || '-' }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.status')">
              <span>{{ repairDetailInfo.stateName }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.content')">
              <span>{{ repairDetailInfo.context }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="repairDetailInfo.maintenanceType" :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.maintenanceType')">
              <span>
                <template v-if="repairDetailInfo.maintenanceType === '1001'">
                  {{ $t('repairDetail.paidService') }}
                </template>
                <template v-else-if="repairDetailInfo.maintenanceType === '1002'">
                  {{ $t('repairDetail.freeService') }}
                </template>
                <template v-else-if="repairDetailInfo.maintenanceType === '1003'">
                  {{ $t('repairDetail.needMaterials') }}
                </template>
                <template v-else-if="repairDetailInfo.maintenanceType === '1004'">
                  {{ $t('repairDetail.noMaterials') }}
                </template>
              </span>
            </el-form-item>
          </el-col>

          <el-col v-if="repairDetailInfo.maintenanceType === '1001' || repairDetailInfo.maintenanceType === '1003'" :sm="8"
            :xs="24">
            <el-form-item :label="$t('repairDetail.materials')">
              <span>{{ repairDetailInfo.repairMaterials }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="repairDetailInfo.maintenanceType === '1001'" :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.costDetail')">
              <span>
                {{ repairDetailInfo.repairFee }}
                <span>{{ $t('repairDetail.yuan') }}</span>
              </span>
            </el-form-item>
          </el-col>
          <el-col v-if="repairDetailInfo.appraiseScore !== '0'" :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.comprehensiveScore')">
              <span>
                {{ repairDetailInfo.appraiseScore }}
                <span>{{ $t('repairDetail.points') }}</span>
              </span>
            </el-form-item>
          </el-col>

          <el-col v-if="repairDetailInfo.doorSpeedScore !== '0'" :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.doorSpeedScore')">
              <span>
                {{ repairDetailInfo.doorSpeedScore }}
                <span>{{ $t('repairDetail.points') }}</span>
              </span>
            </el-form-item>
          </el-col>
          <el-col v-if="repairDetailInfo.repairmanServiceScore !== '0'" :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.serviceScore')">
              <span>
                {{ repairDetailInfo.repairmanServiceScore }}
                <span>{{ $t('repairDetail.points') }}</span>
              </span>
            </el-form-item>
          </el-col>
          <el-col v-if="repairDetailInfo.average !== '0.0'" :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.avgScore')">
              <span>
                {{ repairDetailInfo.average }}
                {{ $t('repairDetail.points') }}
              </span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="repairDetailInfo.visitType" :gutter="20">
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.visitSatisfaction')">
              <span>{{ repairDetailInfo.visitType === '1001' ? $t('repairDetail.satisfied') :
                $t('repairDetail.notSatisfied') }}</span>
            </el-form-item>
          </el-col>
          <el-col :sm="8" :xs="24">
            <el-form-item :label="$t('repairDetail.visitContent')">
              <span>{{ repairDetailInfo.visitContext }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <divider></divider>

    <div class="margin-top-sm">
      <el-tabs v-model="currentTab" @tab-click="changeTab(currentTab)">
        <el-tab-pane :label="$t('repairDetail.orderPhotos')" name="repairDetailPhotos"></el-tab-pane>
        <el-tab-pane :label="$t('repairDetail.repairItems')" name="repairDetailResource"></el-tab-pane>
        <el-tab-pane :label="$t('repairDetail.orderFlow')" name="repairDetailUser"></el-tab-pane>
        <el-tab-pane :label="$t('repairDetail.orderEvents')" name="repairDetailEvent"></el-tab-pane>
      </el-tabs>
    </div>

    <div v-if="currentTab === 'repairDetailPhotos'">
      <repair-detail-photos ref="repairDetailPhotos" :repair-id="repairDetailInfo.repairId"></repair-detail-photos>
    </div>
    <div v-if="currentTab === 'repairDetailResource'">
      <repair-detail-resource ref="repairDetailResource" :repair-id="repairDetailInfo.repairId"></repair-detail-resource>
    </div>
    <div v-if="currentTab === 'repairDetailUser'">
      <repair-detail-user ref="repairDetailUser" :repair-id="repairDetailInfo.repairId"></repair-detail-user>
    </div>
    <div v-if="currentTab === 'repairDetailEvent'">
      <repair-detail-event ref="repairDetailEvent" :repair-id="repairDetailInfo.repairId"></repair-detail-event>
    </div>

    <view-image ref="viewImage"></view-image>
    <reply-repair-appraise ref="replyAppraise"></reply-repair-appraise>
  </el-card>
</div>
</template>

<script>
import { getRepairDetail } from '@/api/work/repairDetailApi'
import RepairDetailPhotos from '@/components/work/repairDetailPhotos'
import RepairDetailResource from '@/components/work/repairDetailResource'
import RepairDetailUser from '@/components/work/repairDetailUser'
import RepairDetailEvent from '@/components/work/repairDetailEvent'
import ViewImage from '@/components/work/viewImage'
import ReplyRepairAppraise from '@/components/work/ReplyRepairAppraise'
import divider from '@/components/system/divider'

export default {
  name: 'RepairDetailList',
  components: {
    RepairDetailPhotos,
    RepairDetailResource,
    RepairDetailUser,
    RepairDetailEvent,
    ViewImage,
    ReplyRepairAppraise,
    divider
  },
  data() {
    return {
      currentTab: 'repairDetailPhotos',
      repairDetailInfo: {
        repairId: '',
        repairType: '',
        repairTypeName: '',
        repairName: '',
        tel: '',
        roomId: '',
        roomName: '',
        repairObjName: '',
        appointmentTime: '',
        submitHours: '',
        timeout: '',
        finishTime: '',
        context: '',
        stateName: '',
        userId: '',
        userName: '',
        photos: [],
        visitType: '',
        visitContext: '',
        maintenanceType: '',
        repairMaterials: '',
        repairFee: '',
        appraiseScore: 0,
        doorSpeedScore: 0,
        repairmanServiceScore: 0,
        average: 0.0
      }
    }
  },
  created() {
    const repairId = this.$route.query.repairId
    this.repairDetailInfo.repairId = repairId
    this.loadRepairDetail()
  },
  methods: {
    async loadRepairDetail() {
      try {
        const data = await getRepairDetail(this.repairDetailInfo.repairId)
        Object.assign(this.repairDetailInfo, data)
        this.changeTab('repairDetailPhotos')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    printRepairDetail() {
      window.open(`/#/pages/property/printRepairDetail?repairId=${this.repairDetailInfo.repairId}&repairType=${this.repairDetailInfo.repairType}`)
    },
    changeTab(tab) {
      this.currentTab = tab
      setTimeout(() => {
        if (this.$refs[`${tab}`]) {
          this.$refs[`${tab}`].loadData()
        }
      },500)
    }
  }
}
</script>

<style scoped>
.repair-detail-container {
  padding: 20px;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.el-divider {
  margin: 20px 0;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>