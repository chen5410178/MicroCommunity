<template>
  <div class="edit-community-publicity-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editCommunityPublicity.title') }}</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="_goBack"
        >
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
      </div>

      <el-form ref="form" :model="editCommunityPublicityInfo" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item 
              :label="$t('editCommunityPublicity.publicityTitle')" 
              prop="title"
              :rules="[{ required: true, message: $t('editCommunityPublicity.validate.titleRequired'), trigger: 'blur' }]">
              <el-input
                v-model="editCommunityPublicityInfo.title"
                :placeholder="$t('editCommunityPublicity.publicityTitlePlaceholder')"
                clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item 
              :label="$t('editCommunityPublicity.publicityType')" 
              prop="pubType"
              :rules="[{ required: true, message: $t('editCommunityPublicity.validate.typeRequired'), trigger: 'change' }]">
              <el-select
                v-model="editCommunityPublicityInfo.pubType"
                style="width:100%"
                :placeholder="$t('editCommunityPublicity.publicityTypePlaceholder')">
                <el-option
                  v-for="item in editCommunityPublicityInfo.pubTypes"
                  :key="item.statusCd"
                  :label="item.name"
                  :value="item.statusCd">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item 
          :label="$t('editCommunityPublicity.headerImage')" 
          prop="headerImg"
          :rules="[{ required: true, message: $t('editCommunityPublicity.validate.imageRequired'), trigger: 'change' }]">
          <upload-image-url
            ref="uploadImage"
            :image-count="1"
            @notifyUploadCoverImage="handleUploadImage">
          </upload-image-url>
        </el-form-item>

        <el-form-item 
          :label="$t('editCommunityPublicity.activityContent')" 
          prop="context"
          :rules="[{ required: true, message: $t('editCommunityPublicity.validate.contentRequired'), trigger: 'blur' }]">
          <rich-text-editor
            v-model="editCommunityPublicityInfo.context"
            ref="richTextEditor"
            :height="300"
            :placeholder="$t('editCommunityPublicity.activityContentPlaceholder')">
          </rich-text-editor>
        </el-form-item>

        <el-form-item class="text-right">
          <el-button @click="_goBack">{{ $t('common.back') }}</el-button>
          <el-button type="primary" @click="saveCommunityPublicityInfo">
            <i class="el-icon-check"></i>{{ $t('common.submit') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import richTextEditor from '@/components/editor/RichTextEditor'
import { updateCommunityPublicity, listCommunityPublicity } from '@/api/community/editCommunityPublicityApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditCommunityPublicity',
  components: {
    UploadImageUrl,
    richTextEditor
  },
  data() {
    return {
      editCommunityPublicityInfo: {
        pubId: '',
        title: '',
        pubType: '',
        pubTypes: [],
        headerImg: '',
        context: ''
      }
    }
  },
  created() {
    this._initData()
  },
  methods: {
    async _initData() {
      this.editCommunityPublicityInfo.pubId = this.$route.query.pubId
      this.communityId = await getCommunityId()
      await this._loadCommunityPublicity()
      await this._loadDictData()
    },
    async _loadDictData() {
      try {
        const data = await getDict('community_publicity', 'pub_type')
        this.editCommunityPublicityInfo.pubTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _loadCommunityPublicity() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          pubId: this.editCommunityPublicityInfo.pubId
        }
        const { data } = await listCommunityPublicity(params)
        if (data && data.length > 0) {
          Object.assign(this.editCommunityPublicityInfo, data[0])
          // 设置图片，如果有的话
          if (this.editCommunityPublicityInfo.headerImg) {
            this.$refs.uploadImage.setImages([this.editCommunityPublicityInfo.headerImg])
          }
          this.$refs.richTextEditor.setContent(this.editCommunityPublicityInfo.context)
        }
      } catch (error) {
        console.error('加载公示信息失败:', error)
      }
    },
    handleUploadImage(images) {
      if (images.length > 0) {
        this.editCommunityPublicityInfo.headerImg = images[0]
      } else {
        this.editCommunityPublicityInfo.headerImg = ''
      }
    },
    async saveCommunityPublicityInfo() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this._saveData()
        } else {
          return false
        }
      })
    },
    async _saveData() {
      try {
        const data = {
          ...this.editCommunityPublicityInfo,
          communityId: this.communityId
        }
        const res = await updateCommunityPublicity(data)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg || this.$t('editCommunityPublicity.message.updateFailed'))
        }
      } catch (error) {
        console.error('保存公示信息失败:', error)
        this.$message.error(this.$t('editCommunityPublicity.message.updateFailed'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-community-publicity-container {
  padding: 20px;

  .el-card {
    width: 100%;
  }

  .el-form-item {
    margin-bottom: 22px;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>