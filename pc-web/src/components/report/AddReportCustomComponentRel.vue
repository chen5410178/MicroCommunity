<template>
  <el-row>
    <el-col :span="24">
      <el-card>
        <div slot="header" class="clearfix">
          <span>{{ $t('addReportCustomComponentRel.title') }}</span>
        </div>

        <el-form :model="form" :rules="rules" ref="form" label-width="150px">
          <el-form-item :label="$t('addReportCustomComponentRel.component')" prop="componentId">
            <el-input v-model="form.name" disabled style="width: calc(100% - 120px)" />
            <el-button type="primary" style="margin-left: 10px; width: 110px" @click="openChooseComponentDialog">
              {{ $t('addReportCustomComponentRel.chooseComponent') }}
            </el-button>
          </el-form-item>

          <el-form-item :label="$t('addReportCustomComponentRel.seq')" prop="seq">
            <el-input v-model="form.seq" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm">{{ $t('common.save') }}</el-button>
            <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>

    <choose-report-custom-component ref="chooseComponentDialog" @selected="handleComponentSelected" />
  </el-row>
</template>

<script>
import { saveReportCustomComponentRel } from '@/api/report/reportCustomComponentRelManageApi'
import ChooseReportCustomComponent from '@/components/report/ChooseReportCustomComponent'

export default {
  name: 'AddReportCustomComponentRel',
  props: {
    customId: {
      type: String,
      required: true
    }
  },
  components: {
    ChooseReportCustomComponent
  },
  data() {
    return {
      form: {
        customId: this.customId,
        componentId: '',
        name: '',
        seq: ''
      },
      rules: {
        componentId: [
          { required: true, message: this.$t('addReportCustomComponentRel.componentRequired'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('addReportCustomComponentRel.seqRequired'), trigger: 'blur' },
          { pattern: /^\d+$/, message: this.$t('addReportCustomComponentRel.seqMustNumber'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveReportCustomComponentRel(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('addReportCustomComponentRel.saveError'))
          }
        }
      })
    },
    cancel() {
      this.$emit('cancel')
    },
    openChooseComponentDialog() {
      this.$refs.chooseComponentDialog.open()
    },
    handleComponentSelected(component) {
      this.form.componentId = component.componentId
      this.form.name = component.name
    }
  }
}
</script>