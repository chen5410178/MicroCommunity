<template>
  <el-dialog :title="$t('menuGroup.dialog.addTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('menuGroup.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('menuGroup.form.storeType')" prop="storeType">
        <el-select v-model="form.storeType" placeholder="请选择" style="width: 100%">
          <el-option label="运营团队" value="800900000001" />
          <el-option label="物业" value="800900000003" />
          <el-option label="开发团队" value="800900000000" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('menuGroup.form.icon')" prop="icon">
        <el-input v-model="form.icon" />
      </el-form-item>

      <el-form-item :label="$t('menuGroup.form.seq')" prop="seq">
        <el-input v-model="form.seq" />
      </el-form-item>

  
      <el-form-item :label="$t('menuGroup.form.groupType')" prop="groupType">
        <el-select v-model="form.groupType" placeholder="请选择" style="width: 100%">
          <el-option label="WEB端" value="P_WEB" />
          <el-option label="APP端" value="P_APP" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('menuGroup.form.label')" prop="label">
        <el-select v-model="form.label" placeholder="请选择" style="width: 100%">
          <el-option label="HOT" value="HOT" />
          <el-option label="TOP" value="TOP" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('menuGroup.form.description')" prop="description">
        <el-input type="textarea" v-model="form.description" />
      </el-form-item>


    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menuGroup.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menuGroup.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addMenuGroup } from '@/api/dev/menuGroupApi'

export default {
  name: 'AddMenuGroup',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        name: '',
        icon: '',
        label: '',
        seq: '',
        description: '',
        groupType: '',
        storeType: '',
        createTime: '',
      },
      rules: {
        name: [
          { required: true, message: this.$t('menuGroup.validate.name'), trigger: 'blur' }
        ],
        icon: [
          { required: true, message: this.$t('menuGroup.validate.icon'), trigger: 'blur' }
        ],
        label: [
          { required: true, message: this.$t('menuGroup.validate.label'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menuGroup.validate.seq'), trigger: 'blur' }
        ],
        groupType: [
          { required: true, message: this.$t('menuGroup.validate.groupType'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('menuGroup.validate.createTime'), trigger: 'blur' }
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
        await addMenuGroup(this.form)
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