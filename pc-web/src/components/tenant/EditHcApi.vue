<template>
  <el-dialog
    :title="$t('hcApi.dialog.editTitle')"
    :visible.sync="dialogVisible"
    width="500px"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="100px"
    >

      <el-form-item
        :label="$t('hcApi.form.apiCode')"
        prop="apiCode"
      >
        <el-input v-model="form.apiCode" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.apiName')"
        prop="apiName"
      >
        <el-input v-model="form.apiName" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.typeId')"
        prop="typeId"
      >
        <el-select v-model="form.typeId" class="text-left" style="width: 100%" placeholder="请选择类型">
          <el-option :label="item.typeName" :value="item.typeId" v-for="(item,index) in apiTypes" :key="index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.seq')"
        prop="seq"
      >
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.docUrl')"
        prop="docUrl"
      >
        <el-input v-model="form.docUrl" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
        />
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('hcApi.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('hcApi.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateHcApi } from '@/api/tenant/hcApiApi'
import { getApiTypeList } from '@/api/tenant/apiTypeApi'

export default {
  name: 'EditHcApi',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      apiTypes:[],
      form: {
        apiId: '',
        apiCode: '',
        apiName: '',
        typeId: '',
        seq: '',
        docUrl: '',
        remark: '',
        createTime: '',
      },
      rules: {
        apiId: [
          { required: true, message: this.$t('hcApi.validate.apiId'), trigger: 'blur' }
        ],
        apiCode: [
          { required: true, message: this.$t('hcApi.validate.apiCode'), trigger: 'blur' }
        ],
        apiName: [
          { required: true, message: this.$t('hcApi.validate.apiName'), trigger: 'blur' }
        ],
        typeId: [
          { required: true, message: this.$t('hcApi.validate.typeId'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('hcApi.validate.createTime'), trigger: 'blur' }
        ],
      }
    }
  },
  created(){
    this.getApiTypeList();
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val && this.formData) {
          this.form = { ...this.formData }
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    async getApiTypeList() {
      const params = {
        page: 1,
        row: 100,
      }
      const { data } = await getApiTypeList(params)
      this.apiTypes = data.data
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateHcApi(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('编辑失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>