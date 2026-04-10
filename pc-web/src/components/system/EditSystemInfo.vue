<template>
  <el-dialog :title="$t('systemInfo.editTitle')" :visible="visible" width="70%" :close-on-click-modal="true" @close="close">
    <el-form ref="form" :model="formData" label-width="150px" :rules="rules">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.systemTitle')" prop="systemTitle">
            <el-input v-model="formData.systemTitle"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.systemTitle') })" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.subSystemTitle')" prop="subSystemTitle">
            <el-input v-model="formData.subSystemTitle"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.subSystemTitle') })" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.systemSimpleTitle')" prop="systemSimpleTitle">
            <el-input v-model="formData.systemSimpleTitle"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.systemSimpleTitle') })" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.companyName')" prop="companyName">
            <el-input v-model="formData.companyName"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.companyName') })" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.logoUrl')" prop="logoUrl">
            <el-input v-model="formData.logoUrl"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.logoUrl') })" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.imgUrl')" prop="imgUrl">
            <el-input v-model="formData.imgUrl"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.imgUrl') })" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.defaultCommunityId')" prop="defaultCommunityId">
            <el-input v-model="formData.defaultCommunityId"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.defaultCommunityId') })" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.ownerTitle')" prop="ownerTitle">
            <el-input v-model="formData.ownerTitle"
              :placeholder="$t('systemInfo.required', { field: $t('systemInfo.ownerTitle') })" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.propertyTitle')" prop="propertyTitle">
            <el-input v-model="formData.propertyTitle" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.qqMapKey')" prop="qqMapKey">
            <el-input v-model="formData.qqMapKey" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('systemInfo.mallUrl')" prop="mallUrl">
            <el-input v-model="formData.mallUrl" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.save') }}</el-button>

    </div>
  </el-dialog>
</template>

<script>
import { updateSystemInfo } from '@/api/system/systemInfoManageApi'

export default {
  name: 'EditSystemInfo',
  props: {
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      visible: false,
      rules: {
        systemTitle: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.systemTitle') }), trigger: 'blur' },
          { max: 64, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.systemTitle'), length: 64 }), trigger: 'blur' }
        ],
        subSystemTitle: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.subSystemTitle') }), trigger: 'blur' },
          { max: 64, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.subSystemTitle'), length: 64 }), trigger: 'blur' }
        ],
        companyName: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.companyName') }), trigger: 'blur' },
          { max: 128, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.companyName'), length: 128 }), trigger: 'blur' }
        ],
        logoUrl: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.logoUrl') }), trigger: 'blur' },
          { max: 200, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.logoUrl'), length: 200 }), trigger: 'blur' }
        ],
        imgUrl: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.imgUrl') }), trigger: 'blur' },
          { max: 512, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.imgUrl'), length: 512 }), trigger: 'blur' }
        ],
        ownerTitle: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.ownerTitle') }), trigger: 'blur' },
          { max: 64, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.ownerTitle'), length: 64 }), trigger: 'blur' }
        ],
        propertyTitle: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.propertyTitle') }), trigger: 'blur' },
          { max: 64, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.propertyTitle'), length: 64 }), trigger: 'blur' }
        ],
        qqMapKey: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.qqMapKey') }), trigger: 'blur' },
          { max: 64, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.qqMapKey'), length: 64 }), trigger: 'blur' }
        ],
        mallUrl: [
          { required: true, message: this.$t('systemInfo.required', { field: this.$t('systemInfo.mallUrl') }), trigger: 'blur' },
          { max: 128, message: this.$t('systemInfo.maxLength', { field: this.$t('systemInfo.mallUrl'), length: 128 }), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateSystemInfo(this.formData)
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('common.updateFailed'))
          }
        }
      })
    },
    close() {
      this.visible = false
    }
  }
}
</script>