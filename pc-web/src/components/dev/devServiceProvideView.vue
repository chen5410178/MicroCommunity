<template>
  <el-row>
    <el-col :span="24">
      <el-card>
        <div slot="header">
          <span>{{ $t('devServiceProvideView.title') }}</span>
        </div>
        <el-form label-width="120px">
          <el-form-item :label="$t('devServiceProvideView.implMethod')" required>
            <el-select 
              v-model="form.queryModel" 
              style="width: 100%"
              @change="saveInfo">
              <el-option 
                :label="$t('devServiceProvideView.required') + $t('devServiceProvideView.implMethod')" 
                value="" 
                disabled />
              <el-option label="sql方式" value="1" />
              <el-option label="java方式" value="3" />
              <el-option :label="$t('devServiceProvideView.proc')" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('devServiceProvideView.params')" required>
            <el-input 
              v-model="form.params" 
              :placeholder="$t('devServiceProvideView.required') + $t('devServiceProvideView.params')"
              @input="saveInfo" />
          </el-form-item>
          <el-form-item v-if="form.queryModel === '1'" label="sql">
            <el-input
              v-model="form.sql"
              type="textarea"
              :rows="15"
              :placeholder="$t('devServiceProvideView.optional') + $t('devServiceProvideView.sql')"
              @input="saveInfo" />
          </el-form-item>
          <el-form-item v-if="form.queryModel === '1'" :label="$t('devServiceProvideView.template')">
            <el-input
              v-model="form.template"
              type="textarea"
              :rows="5"
              :placeholder="$t('devServiceProvideView.optional') + $t('devServiceProvideView.template')"
              @input="saveInfo" />
          </el-form-item>
          <el-form-item v-if="form.queryModel === '2'" :label="$t('devServiceProvideView.proc')">
            <el-input
              v-model="form.proc"
              type="textarea"
              :rows="5"
              :placeholder="$t('devServiceProvideView.optional') + $t('devServiceProvideView.proc')"
              @input="saveInfo" />
          </el-form-item>
          <el-form-item v-if="form.queryModel === '3'" label="java">
            <el-input
              v-model="form.javaScript"
              type="textarea"
              :rows="20"
              :placeholder="$t('devServiceProvideView.optional') + $t('devServiceProvideView.javaScript')"
              @input="saveInfo" />
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'DevServiceProvideView',
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
        queryModel: '',
        params: '',
        sql: '',
        template: '',
        proc: '',
        javaScript: ''
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
      if (this.validate()) {
        this.$emit(this.callBackFunction, this.form)
      }
    },
    validate() {
      if (!this.form.queryModel) {
        this.$message.error(this.$t('devServiceProvideView.implMethod') + this.$t('devServiceProvideView.required'))
        return false
      }
      if (!this.form.params) {
        this.$message.error(this.$t('devServiceProvideView.params') + this.$t('devServiceProvideView.required'))
        return false
      }
      return true
    },
    onIndex(index) {
      this.index = index
    }
  }
}
</script>