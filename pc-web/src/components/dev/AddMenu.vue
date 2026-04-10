<template>
  <el-dialog :title="$t('menu.dialog.addTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('menu.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('menu.form.url')" prop="url">
        <el-input v-model="form.url" />
      </el-form-item>
      <el-form-item :label="$t('menu.form.seq')" prop="seq">
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item :label="$t('menu.form.description')" prop="description">
        <el-input v-model="form.description" />
      </el-form-item>
      <el-form-item :label="$t('menu.form.isShow')" prop="isShow">
        <el-select v-model="form.isShow" placeholder="请选择" style="width: 100%">
          <el-option label="显示" value="Y" />
            <el-option label="不显示" value="N" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menu.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menu.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addMenu } from '@/api/dev/menuApi'

export default {
  name: 'AddMenu',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    gId: {
      type: String,
      default: ""
    },
    storeType: {
      type: String,
      default: ""
    }

  },
  data() {
    return {
      dialogVisible: false,
      form: {
        name: '',
        gId: '',
        url: '',
        seq: '',
        description: '',
        isShow: '',
        storeType: '',
        createTime: '',
      },
      rules: {
        name: [
          { required: true, message: this.$t('menu.validate.name'), trigger: 'blur' }
        ],
        gId: [
          { required: true, message: this.$t('menu.validate.gId'), trigger: 'blur' }
        ],
        url: [
          { required: true, message: this.$t('menu.validate.url'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menu.validate.seq'), trigger: 'blur' }
        ],
        isShow: [
          { required: true, message: this.$t('menu.validate.isShow'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('menu.validate.createTime'), trigger: 'blur' }
        ],
      }
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        let _data = []
        _data.push({
          flowComponent: "viewMenuGroupInfo",
          gId: this.gId
        })
        _data.push({
          flowComponent: "addMenuView",
          name: this.form.name,
          url: this.form.url,
          seq: this.form.seq,
          isShow: this.form.isShow,
          description: this.form.description
        })
        _data.push({
          flowComponent: "addPrivilegeView",
          name: this.form.name,
          resource: this.form.url,
          domain: this.storeType,
          description: this.form.description
        })
        await addMenu({
          data: _data
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('添加失败:', error)
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