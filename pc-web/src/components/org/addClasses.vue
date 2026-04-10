<template>
  <el-dialog :title="$t('classesManage.add.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px" class="text-left">
      <el-form-item :label="$t('classesManage.add.name')" prop="name" :rules="[
        { required: true, message: $t('classesManage.validate.nameRequired'), trigger: 'blur' },
        { max: 100, message: $t('classesManage.validate.nameMaxLength'), trigger: 'blur' }
      ]">
        <el-input v-model="formData.name" :placeholder="$t('classesManage.add.namePlaceholder')" />
      </el-form-item>

      <el-form-item v-for="(time, index) in formData.times" :key="time.id"
        :label="index === 0 ? $t('classesManage.add.workTime') : ''">
        <el-col :span="10">
          <el-time-picker v-model="time.startTime" :placeholder="$t('classesManage.add.startTimePlaceholder')"
            format="HH:mm" value-format="HH:mm" />
        </el-col>
        <el-col :span="2" style="text-align: center">-</el-col>
        <el-col :span="10">
          <el-time-picker v-model="time.endTime" :placeholder="$t('classesManage.add.endTimePlaceholder')"
            format="HH:mm" value-format="HH:mm" />
        </el-col>
        <el-col :span="2">
          <el-button type="danger" icon="el-icon-delete" circle @click="removeTime(index)" />
        </el-col>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="addTime">
          {{ $t('classesManage.add.addTime') }}
        </el-button>
      </el-form-item>

      <el-form-item :label="$t('classesManage.add.remark')" prop="remark" :rules="[
        { required: true, message: $t('classesManage.validate.remarkRequired'), trigger: 'blur' },
        { max: 200, message: $t('classesManage.validate.remarkMaxLength'), trigger: 'blur' }
      ]">
        <el-input v-model="formData.remark" type="textarea" :placeholder="$t('classesManage.add.remarkPlaceholder')"
          :rows="3" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveClasses } from '@/api/org/classesManageApi'
import { getUuid } from '@/utils/commonUtil'

export default {
  name: 'AddClasses',
  data() {
    return {
      visible: false,
      formData: {
        name: '',
        remark: '',
        times: []
      }
    }
  },
  methods: {
    open() {
      this.resetForm()
      this.addTime()
      this.visible = true
    },
    resetForm() {
      this.formData = {
        name: '',
        remark: '',
        times: []
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    addTime() {
      this.formData.times.push({
        id: getUuid(),
        startTime: '',
        endTime: ''
      })
    },
    removeTime(index) {
      this.formData.times.splice(index, 1)
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true
        await saveClasses(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        if (error !== 'validate') {
          this.$message.error(error.message || this.$t('common.saveFailed'))
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>