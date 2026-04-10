<template>
  <el-dialog
    :title="$t('oaWorkflowManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item
        :label="$t('oaWorkflowManage.add.flowName')"
        prop="flowName"
      >
        <el-input
          v-model="form.flowName"
          :placeholder="$t('oaWorkflowManage.add.flowNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('oaWorkflowManage.add.flowType')"
        prop="flowType"
      >
        <el-select
          v-model="form.flowType"
          :placeholder="$t('oaWorkflowManage.add.flowTypePlaceholder')"
          style="width:100%"
        >
          <el-option
            v-for="item in flowTypes"
            :key="item.statusCd"
            :label="item.name"
            :value="item.statusCd"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oaWorkflowManage.add.describle')">
        <el-input
          v-model="form.describle"
          type="textarea"
          :placeholder="$t('oaWorkflowManage.add.describlePlaceholder')"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveOaWorkflow } from '@/api/oa/oaWorkflowManageApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AddOaWorkflow',
  data() {
    return {
      visible: false,
      form: {
        flowName: '',
        flowType: '',
        describle: ''
      },
      flowTypes: [],
      rules: {
        flowName: [
          { required: true, message: this.$t('oaWorkflowManage.validate.flowNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('oaWorkflowManage.validate.flowNameMaxLength'), trigger: 'blur' }
        ],
        flowType: [
          { required: true, message: this.$t('oaWorkflowManage.validate.flowTypeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.getDictData()
    },
    async getDictData() {
      try {
        this.flowTypes = await getDict('oa_workflow', 'flow_type')
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        flowName: '',
        flowType: '',
        describle: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveOaWorkflow(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(this.$t('oaWorkflowManage.add.error'))
          }
        }
      })
    }
  }
}
</script>