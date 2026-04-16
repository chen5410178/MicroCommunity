<template>
  <el-dialog :title="$t('common.edit')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('reportCustomComponentFooterManage.name')" prop="name" required>
        <el-input v-model="formData.name" :placeholder="$t('reportCustomComponentFooterManage.namePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('reportCustomComponentFooterManage.queryMethod')" prop="queryModel" required>
        <el-select v-model="formData.queryModel"
           style="width: 100%">
          <el-option label="SQL" value="1" />
          <el-option label="Java" value="2" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.queryModel === '1'" label="sql"
        prop="componentSql">
        <el-input v-model="formData.componentSql" type="textarea" :rows="15" />
      </el-form-item>

      <el-form-item v-if="formData.queryModel === '2'" label="java"
        prop="javaScript">
        <el-input v-model="formData.javaScript" type="textarea" :rows="15"/>
      </el-form-item>

      <el-form-item :label="$t('reportCustomComponentFooterManage.description')" prop="remark">
        <el-input v-model="formData.remark" type="textarea"/>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateReportCustomComponentFooter } from '@/api/report/reportCustomComponentFooterManageApi'

export default {
  name: 'EditReportCustomComponentFooter',
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
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateReportCustomComponentFooter(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>