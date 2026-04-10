<template>
  <el-row>
    <el-col :span="24">
      <el-card>
        <div slot="header" class="clearfix">
          <span>{{ $t('viewServiceInfo.title') }}</span>
          <div style="float: right;">
            <el-button v-if="index !== 2" type="primary" size="small" icon="el-icon-search"
              @click="openSelectServiceModel">
              {{ $t('viewServiceInfo.selectService') }}
            </el-button>
          </div>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.serviceId')">
              <span>{{ serviceId }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.name')">
              <span>{{ name }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.serviceCode')">
              <span>{{ serviceCode }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.messageQueue')">
              <span>{{ messageQueueName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.isInstance')">
              <span>{{ isInstance }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.url')">
              <span>{{ url }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.method')">
              <span>{{ method }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.timeout')">
              <span>{{ timeout }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('viewServiceInfo.retryCount')">
              <span>{{ retryCount }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'ViewServiceInfo',
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    },
    index: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      serviceId: '',
      name: '',
      serviceCode: '',
      messageQueueName: '',
      isInstance: '',
      url: '',
      method: '',
      timeout: '',
      retryCount: ''
    }
  },
  created() {
    this.$on('chooseService', this.chooseService)
    this.$on('onIndex', this.onIndex)
  },
  methods: {
    openSelectServiceModel() {
      this.$emit('openChooseServiceModel')
    },
    openAddServiceModel() {
      this.$emit('openAddServiceModal')
    },
    chooseService(service) {
      Object.assign(this, service)
      this.$emit(this.callBackFunction, this)
    },
    onIndex(index) {
      this.index = index
    }
  }
}
</script>