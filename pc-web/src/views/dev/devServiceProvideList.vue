<template>
  <div class="dev-service-provide-container">
    <el-steps :active="activeStep" finish-status="success" simple>
      <el-step v-for="(title, index) in $t('devServiceProvide.stepTitles')" :key="index" :title="title" />
    </el-steps>

    <view-service-info v-if="activeStep === 0" ref="viewServiceInfo" :call-back-listener="callBackListener"
      :call-back-function="callBackFunction" @openChooseServiceModel="openChooseServiceModel"
      @openAddServiceModal="openAddServiceModal" />

    <dev-service-provide-view v-if="activeStep === 1" ref="devServiceProvideView" :call-back-listener="callBackListener"
      :call-back-function="callBackFunction" />

    <service-provide-remark-view v-if="activeStep === 2" ref="serviceProvideRemarkView"
      :call-back-listener="callBackListener" :call-back-function="callBackFunction" />

    <div class="action-buttons">
      <el-button @click="prevStep">{{ $t('devServiceProvide.prevStep') }}</el-button>
      <el-button v-if="activeStep !== 2" type="primary" @click="nextStep">
        {{ $t('devServiceProvide.nextStep') }}
      </el-button>
      <el-button v-if="activeStep === 2" type="primary" @click="finishStep">
        {{ $t('devServiceProvide.finish') }}
      </el-button>
    </div>

    <choose-service ref="chooseService" :emit-choose-service="callBackListener" :emit-load-data="callBackListener" />


  </div>
</template>

<script>
import ViewServiceInfo from '@/components/dev/viewServiceInfo'
import DevServiceProvideView from '@/components/dev/devServiceProvideView'
import ServiceProvideRemarkView from '@/components/dev/serviceProvideRemarkView'
import ChooseService from '@/components/dev/chooseService'
import { saveServiceProvide } from '@/api/dev/devServiceProvideApi'

export default {
  name: 'DevServiceProvideList',
  components: {
    ViewServiceInfo,
    DevServiceProvideView,
    ServiceProvideRemarkView,
    ChooseService,
  },
  data() {
    return {
      activeStep: 0,
      callBackListener: 'devServiceProvide',
      callBackFunction: 'notify',
      infos: []
    }
  },
  created() {
    this.$on('notify', this.notify)
  },
  methods: {
    notify(info) {
      this.infos[this.activeStep] = info
    },
    prevStep() {
      if (this.activeStep > 0) {
        this.activeStep--
      }
    },
    nextStep() {
      if (!this.infos[this.activeStep]) {
        this.$message.error(this.$t('devServiceProvide.requiredInfo'))
        return
      }
      if (this.activeStep < 2) {
        this.activeStep++
      }
    },
    finishStep() {
      if (!this.infos[this.activeStep]) {
        this.$message.error(this.$t('devServiceProvide.requiredInfo'))
        return
      }

      saveServiceProvide({ data: this.infos })
        .then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        })
        .catch(error => {
          this.$message.error(error.message)
        })
    },
    openChooseServiceModel() {
      this.$refs.chooseService.open()
    },
    openAddServiceModal() {
      this.$refs.addService.open()
    }
  }
}
</script>

<style scoped>
.dev-service-provide-container {
  padding: 20px;
}

.action-buttons {
  margin-top: 20px;
  text-align: right;
}
</style>