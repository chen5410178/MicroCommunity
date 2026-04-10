<template>
  <el-dialog
    :title="$t('editReportCustom.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('editReportCustom.group')"
        prop="groupId"
      >
        <el-select
          v-model="form.groupId"
          :placeholder="$t('editReportCustom.selectGroup')"
          style="width: 100%"
        >
          <el-option
            v-for="item in reportCustomGroups"
            :key="item.groupId"
            :label="item.name"
            :value="item.groupId"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('editReportCustom.optionTitle')"
        prop="title"
      >
        <el-input
          v-model="form.title"
          :placeholder="$t('editReportCustom.inputTitle')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('editReportCustom.sequence')"
        prop="seq"
      >
        <el-input
          v-model="form.seq"
          :placeholder="$t('editReportCustom.inputSeq')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('editReportCustom.description')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="$t('editReportCustom.inputDesc')"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('reportCustomManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('editReportCustom.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateReportCustom, listReportCustomGroup } from '@/api/report/reportCustomManageApi'

export default {
  name: 'EditReportCustom',
  props: {
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      visible: false,
      form: {
        customId: '',
        groupId: '',
        title: '',
        seq: '',
        remark: ''
      },
      rules: {
        groupId: [
          { required: true, message: this.$t('editReportCustom.selectGroup'), trigger: 'change' },
          { max: 30, message: this.$t('editReportCustom.group') + this.$t('editReportCustom.maxLength30'), trigger: 'blur' }
        ],
        title: [
          { required: true, message: this.$t('editReportCustom.inputTitle'), trigger: 'blur' },
          { max: 64, message: this.$t('editReportCustom.optionTitle') + this.$t('editReportCustom.maxLength64'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('editReportCustom.inputSeq'), trigger: 'blur' },
          { max: 11, message: this.$t('editReportCustom.sequence') + this.$t('editReportCustom.maxLength11'), trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: this.$t('editReportCustom.description') + this.$t('editReportCustom.maxLength512'), trigger: 'blur' }
        ],
        customId: [
          { required: true, message: this.$t('editReportCustom.reportIdRequired'), trigger: 'blur' }
        ]
      },
      reportCustomGroups: []
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.form = { ...this.formData }
        this.getReportCustomGroups()
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    async getReportCustomGroups() {
      try {
        const { data } = await listReportCustomGroup({
          page: 1,
          row: 50
        })
        this.reportCustomGroups = data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateReportCustom(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>