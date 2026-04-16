<template>
  <el-dialog
    :title="$t('addReportCustom.title')"
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
        :label="$t('addReportCustom.group')"
        prop="groupId"
      >
        <el-select
          v-model="form.groupId"
          :placeholder="$t('addReportCustom.selectGroup')"
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
        :label="$t('addReportCustom.optionTitle')"
        prop="title"
      >
        <el-input
          v-model="form.title"
          :placeholder="$t('addReportCustom.inputTitle')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('addReportCustom.sequence')"
        prop="seq"
      >
        <el-input
          v-model="form.seq"
          :placeholder="$t('addReportCustom.inputSeq')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('addReportCustom.description')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="$t('addReportCustom.inputDesc')"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('reportCustomManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('addReportCustom.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveReportCustom, listReportCustomGroup } from '@/api/report/reportCustomManageApi'

export default {
  name: 'AddReportCustom',

  data() {
    return {
      visible: false,
      form: {
        groupId: '',
        title: '',
        seq: '',
        remark: ''
      },
      rules: {
        groupId: [
          { required: true, message: this.$t('addReportCustom.selectGroup'), trigger: 'change' },
          { max: 30, message: this.$t('addReportCustom.group') + this.$t('addReportCustom.maxLength30'), trigger: 'blur' }
        ],
        title: [
          { required: true, message: this.$t('addReportCustom.inputTitle'), trigger: 'blur' },
          { max: 64, message: this.$t('addReportCustom.optionTitle') + this.$t('addReportCustom.maxLength64'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('addReportCustom.inputSeq'), trigger: 'blur' },
          { max: 11, message: this.$t('addReportCustom.sequence') + this.$t('addReportCustom.maxLength11'), trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: this.$t('addReportCustom.description') + this.$t('addReportCustom.maxLength512'), trigger: 'blur' }
        ]
      },
      reportCustomGroups: []
    }
  },
  watch: {
    visible(val) {
      if (val) {
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
            await saveReportCustom(this.form)
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
      this.form = {
        groupId: '',
        title: '',
        seq: '',
        remark: ''
      }
    }
  }
}
</script>