<template>
  <el-dialog :title="$t('task.dialog.addTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('task.form.taskName')" prop="taskName">
        <el-input v-model="form.taskName" />
      </el-form-item>
      <el-form-item :label="$t('task.form.templateId')" prop="templateId">
        <el-select v-model="form.templateId" :placeholder="$t('task.form.templateId')" style="width: 100%">
          <el-option :label="item.templateName" :value="item.templateId" v-for="(item,index) in  templates" :key="index"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('task.form.taskCron')" prop="taskCron">
        <el-input v-model="form.taskCron" />
      </el-form-item>
      <el-form-item :label="$t('task.form.state')" prop="state">
        <el-select v-model="form.state" :placeholder="$t('task.form.state')" style="width: 100%">
          <el-option label="STOP" value="001" />
          <el-option label="RUN" value="002" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('task.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('task.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addTask ,getTaskTemplateList} from '@/api/dev/taskApi'

export default {
  name: 'AddTask',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      templates:[],
      form: {
        taskName: '',
        templateId: '',
        taskCron: '',
        state: '',
        createTime: '',
      },
      rules: {
        taskName: [
          { required: true, message: this.$t('task.validate.taskName'), trigger: 'blur' }
        ],
        templateId: [
          { required: true, message: this.$t('task.validate.templateId'), trigger: 'blur' }
        ],
        taskCron: [
          { required: true, message: this.$t('task.validate.taskCron'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('task.validate.state'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('task.validate.createTime'), trigger: 'blur' }
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
  created(){
    this.getTaskTemplateList()
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    async getTaskTemplateList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 100,
        }
        const { data } = await getTaskTemplateList(params)
        this.templates = data
      } catch (error) {
        this.$message.error(this.$t('task.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await addTask(this.form)
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