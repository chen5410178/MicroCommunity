<template>
  <el-dialog
    :title="$t('junkRequirement.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="close"
  >
    <el-form ref="form" :model="editJunkRequirementInfo" label-width="120px">
      <el-form-item :label="$t('junkRequirement.edit.classification')">
        <el-select
          v-model="editJunkRequirementInfo.classification"
          :disabled="true"
          class="form-item"
        >
          <el-option
            v-for="item in classificationOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('junkRequirement.edit.context')">
        <el-input
          v-model="editJunkRequirementInfo.context"
          :disabled="true"
          class="form-item"
        />
      </el-form-item>
      <el-form-item :label="$t('junkRequirement.edit.referencePrice')">
        <el-input
          v-model="editJunkRequirementInfo.referencePrice"
          :disabled="true"
          class="form-item"
        />
      </el-form-item>
      <el-form-item :label="$t('junkRequirement.edit.publishUserName')">
        <el-input
          v-model="editJunkRequirementInfo.publishUserName"
          :disabled="true"
          class="form-item"
        />
      </el-form-item>
      <el-form-item :label="$t('junkRequirement.edit.publishUserLink')">
        <el-input
          v-model="editJunkRequirementInfo.publishUserLink"
          :disabled="true"
          class="form-item"
        />
      </el-form-item>
      <el-form-item
        :label="$t('junkRequirement.edit.state')"
        prop="state"
        :rules="[
          { required: true, message: $t('junkRequirement.edit.stateRequired'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="editJunkRequirementInfo.state"
          class="form-item"
        >
          <el-option
            v-for="item in stateOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="editJunkRequirement">{{ $t('common.review') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateJunkRequirement } from '@/api/mall/junkRequirementManageApi'

export default {
  name: 'EditJunkRequirement',
  data() {
    return {
      visible: false,
      editJunkRequirementInfo: {
        junkRequirementId: '',
        classification: '',
        context: '',
        referencePrice: '',
        publishUserName: '',
        publishUserLink: '',
        state: ''
      },
      classificationOptions: [
        { value: '10001', label: this.$t('junkRequirement.classification.furniture') },
        { value: '10002', label: this.$t('junkRequirement.classification.appliance') }
      ],
      stateOptions: [
        { value: '12001', label: this.$t('junkRequirement.state.unreviewed') },
        { value: '13001', label: this.$t('junkRequirement.state.reviewed') },
        { value: '14001', label: this.$t('junkRequirement.state.reviewFailed') },
        { value: '15001', label: this.$t('junkRequirement.state.completed') }
      ]
    }
  },
  methods: {
    open(row) {
      this.resetForm()
      this.editJunkRequirementInfo = { ...row }
      this.visible = true
    },
    close() {
      this.visible = false
      this.$refs.form.resetFields()
    },
    editJunkRequirement() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateJunkRequirement({
              ...this.editJunkRequirementInfo,
              communityId: this.getCommunityId()
            })
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.close()
          } catch (error) {
            this.$message.error(error.message || this.$t('junkRequirement.edit.error'))
          }
        }
      })
    },
    resetForm() {
      this.editJunkRequirementInfo = {
        junkRequirementId: '',
        classification: '',
        context: '',
        referencePrice: '',
        publishUserName: '',
        publishUserLink: '',
        state: ''
      }
    }
  }
}
</script>

<style scoped>
.form-item {
  width: 100%;
}
</style>