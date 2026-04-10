<template>
  <el-dialog
    :title="$t('machineTranslateManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('machineTranslateManage.add.machineCode')" prop="machineCode">
        <el-input
          v-model="formData.machineCode"
          :placeholder="$t('machineTranslateManage.add.machineCodePlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.add.machineId')" prop="machineId">
        <el-input
          v-model="formData.machineId"
          :placeholder="$t('machineTranslateManage.add.machineIdPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.add.typeCd')" prop="typeCd">
        <el-select
          v-model="formData.typeCd"
          :placeholder="$t('machineTranslateManage.add.typeCdPlaceholder')"
          style="width: 100%"
        >
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.add.objName')" prop="objName">
        <el-input
          v-model="formData.objName"
          :placeholder="$t('machineTranslateManage.add.objNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.add.objId')" prop="objId">
        <el-input
          v-model="formData.objId"
          :placeholder="$t('machineTranslateManage.add.objIdPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.add.state')" prop="state">
        <el-select
          v-model="formData.state"
          :placeholder="$t('machineTranslateManage.add.statePlaceholder')"
          style="width: 100%"
        >
          <el-option
            :label="$t('machineTranslateManage.state.notSync')"
            value="10000"
          />
          <el-option
            :label="$t('machineTranslateManage.state.synced')"
            value="20000"
          />
        </el-select>
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
import { saveMachineTranslate } from '@/api/machine/machineTranslateManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddMachineTranslate',
  data() {
    return {
      visible: false,
      formData: {
        machineCode: '',
        machineId: '',
        typeCd: '',
        objName: '',
        objId: '',
        state: '',
        communityId: ''
      },
      typeOptions: [
        { value: '8899', label: this.$t('machineTranslateManage.type.owner') },
        { value: '9988', label: this.$t('machineTranslateManage.type.community') }
      ],
      rules: {
        machineCode: [
          { required: true, message: this.$t('machineTranslateManage.validate.machineCodeRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('machineTranslateManage.validate.machineCodeMaxLength'), trigger: 'blur' }
        ],
        machineId: [
          { required: true, message: this.$t('machineTranslateManage.validate.machineIdRequired'), trigger: 'blur' }
        ],
        typeCd: [
          { required: true, message: this.$t('machineTranslateManage.validate.typeCdRequired'), trigger: 'change' }
        ],
        objName: [
          { required: true, message: this.$t('machineTranslateManage.validate.objNameRequired'), trigger: 'blur' }
        ],
        objId: [
          { required: true, message: this.$t('machineTranslateManage.validate.objIdRequired'), trigger: 'blur' },
          { pattern: /^\d+$/, message: this.$t('machineTranslateManage.validate.objIdNumber'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('machineTranslateManage.validate.stateRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.formData.communityId = getCommunityId()
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveMachineTranslate(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('保存失败:', error)
          }
        }
      })
    }
  }
}
</script>