<template>
  <el-dialog
    :title="$t('common.edit')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item
        :label="$t('reportCustomGroupManage.table.groupId')"
        prop="groupId"
      >
        <el-input v-model="form.groupId" disabled />
      </el-form-item>
      <el-form-item
        :label="$t('reportCustomGroupManage.table.name')"
        prop="name"
      >
        <el-input
          v-model="form.name"
          :placeholder="$t('reportCustomGroupManage.edit.namePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('reportCustomGroupManage.table.url')"
        prop="url"
      >
        <el-input
          v-model="form.url"
          :placeholder="$t('reportCustomGroupManage.edit.urlPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('reportCustomGroupManage.table.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="$t('reportCustomGroupManage.edit.remarkPlaceholder')"
        />
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
import { updateReportCustomGroup } from '@/api/report/reportCustomGroupManageApi'

export default {
  name: 'EditReportCustomGroup',
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
      form: {
        groupId: '',
        name: '',
        url: '',
        remark: ''
      },
      rules: {
        groupId: [
          {
            required: true,
            message: this.$t('reportCustomGroupManage.validate.groupIdRequired'),
            trigger: 'blur'
          }
        ],
        name: [
          {
            required: true,
            message: this.$t('reportCustomGroupManage.validate.nameRequired'),
            trigger: 'blur'
          },
          {
            max: 128,
            message: this.$t('reportCustomGroupManage.validate.nameMaxLength'),
            trigger: 'blur'
          }
        ],
        url: [
          {
            required: true,
            message: this.$t('reportCustomGroupManage.validate.urlRequired'),
            trigger: 'blur'
          },
          {
            max: 512,
            message: this.$t('reportCustomGroupManage.validate.urlMaxLength'),
            trigger: 'blur'
          }
        ],
        remark: [
          {
            required: true,
            message: this.$t('reportCustomGroupManage.validate.remarkRequired'),
            trigger: 'blur'
          },
          {
            max: 512,
            message: this.$t('reportCustomGroupManage.validate.remarkMaxLength'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  watch: {
    formData: {
      handler(val) {
        if (val) {
          this.form = { ...val }
        }
      },
      immediate: true
    }
  },
  methods: {
    handleClose() {
      this.$refs.form.resetFields()
      this.$emit('update:visible', false)
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateReportCustomGroup(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.handleClose()
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>