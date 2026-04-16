<template>
  <el-card class="add-publicity-container">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('addCommunityPublicity.title') }}</span>
      <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">
        <i class="el-icon-close"></i>{{ $t('addCommunityPublicity.back') }}
      </el-button>
    </div>

    <el-form ref="form" :model="formData" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addCommunityPublicity.pubTitle')" prop="title"
            :rules="[{ required: true, message: $t('addCommunityPublicity.requiredTitle'), trigger: 'blur' }]">
            <el-input v-model="formData.title" :placeholder="$t('addCommunityPublicity.requiredTitle')" clearable>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunityPublicity.pubType')" prop="pubType"
            :rules="[{ required: true, message: $t('addCommunityPublicity.requiredType'), trigger: 'change' }]">
            <el-select v-model="formData.pubType" :placeholder="$t('addCommunityPublicity.requiredType')"
              style="width: 100%">
              <el-option v-for="item in pubTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item :label="$t('addCommunityPublicity.headerPhoto')" prop="headerImg"
        :rules="[{ required: true, message: $t('addCommunityPublicity.requiredHeaderPhoto'), trigger: 'change' }]">
        <upload-image-url ref="uploadImage" :image-count="1" @notifyUploadCoverImage="handleImageUpload">
        </upload-image-url>
      </el-form-item>

      <el-form-item :label="$t('addCommunityPublicity.activityContent')" prop="context"
        :rules="[{ required: true, message: $t('addCommunityPublicity.requiredContent'), trigger: 'blur' }]">
        <rich-text-editor v-model="formData.context" :height="300"
          :placeholder="$t('addCommunityPublicity.requiredContent')">
        </rich-text-editor>
      </el-form-item>

      <el-form-item class="text-right">
        <el-button @click="goBack">{{ $t('addCommunityPublicity.back') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('addCommunityPublicity.submit') }}</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { saveCommunityPublicity } from '@/api/community/addCommunityPublicityApi'
import { getCommunityId, getDict } from '@/api/community/communityApi'
import richTextEditor from '@/components/editor/RichTextEditor'

export default {
  name: 'AddCommunityPublicity',
  components: {
    UploadImageUrl,
    richTextEditor
  },
  data() {
    return {
      formData: {
        title: '',
        pubType: '',
        headerImg: '',
        context: ''
      },
      pubTypes: []
    }
  },
  created() {
    this.getPublicityTypes()
  },
  methods: {
    async getPublicityTypes() {
      try {
        const data = await getDict('community_publicity', 'pub_type')
        this.pubTypes = data
      } catch (error) {
        console.error('Failed to get publicity types:', error)
        this.$message.error(this.$t('addCommunityPublicity.fetchTypesFailed'))
      }
    },
    handleImageUpload(images) {
      if (images.length > 0) {
        this.formData.headerImg = images[0] || images[0]
      } else {
        this.formData.headerImg = ''
      }
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.savePublicity()
        } else {
          return false
        }
      })
    },
    async savePublicity() {
      try {
        this.formData.communityId = getCommunityId()
        const response = await saveCommunityPublicity(this.formData)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(response.msg || this.$t('addCommunityPublicity.saveFailed'))
        }
      } catch (error) {
        console.error('Save publicity error:', error)
        this.$message.error(this.$t('addCommunityPublicity.saveFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.add-publicity-container {
  margin: 20px;
}

.text-right {
  text-align: right;
}
</style>