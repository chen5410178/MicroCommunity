<template>
  <el-row>
    <el-col :span="24">
      <el-card>
        <div slot="header">
          <span>{{ $t('serviceProvideRemarkView.title') }}</span>
        </div>
        <el-form label-width="120px">
          <el-form-item :label="$t('serviceProvideRemarkView.description')">
            <el-input
              v-model="form.description"
              type="textarea"
              :rows="10"
              :placeholder="$t('serviceProvideRemarkView.optional') + $t('serviceProvideRemarkView.description')"
              @input="saveInfo" />
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'ServiceProvideRemarkView',
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
      form: {
        description: ''
      }
    }
  },
  watch: {
    form: {
      deep: true,
      handler() {
        this.saveInfo()
      }
    }
  },
  created() {
    this.$on('onIndex', this.onIndex)
  },
  methods: {
    saveInfo() {
      this.$emit(this.callBackFunction, this.form)
    },
    onIndex(index) {
      this.index = index
    }
  }
}
</script>