<template>
  <div class="add-notice-view-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addNoticeView.title') }}</span>
      </div>

      <el-form ref="form" :model="addNoticeViewInfo" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addNoticeView.titleLabel')" prop="title">
              <el-input v-model="addNoticeViewInfo.title" :placeholder="$t('addNoticeView.titlePlaceholder')" clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addNoticeView.noticeTypeLabel')" prop="noticeTypeCd">
              <el-select v-model="addNoticeViewInfo.noticeTypeCd" :placeholder="$t('addNoticeView.noticeTypePlaceholder')"
                style="width:100%">
                <el-option :label="$t('addNoticeView.noticeTypeOption1')" value="1000" />
                <el-option :label="$t('addNoticeView.noticeTypeOption2')" value="1001" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addNoticeView.contentLabel')" prop="context">
              <div class="editor-wrapper">
                <rich-text-editor ref="richTextEditor" v-model="addNoticeViewInfo.context" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="button-group">
            
            <el-button type="warning" @click="closeNoticeInfo">
              <i class="el-icon-close"></i>
              {{ $t('addNoticeView.cancel') }}
            </el-button>
            <el-button type="primary" @click="saveNoticeInfo">
              <i class="el-icon-check"></i>
              {{ $t('addNoticeView.submit') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { saveNotice } from '@/api/oa/addNoticeViewApi'
import { getCommunityId } from '@/api/community/communityApi'
import RichTextEditor from "@/components/editor/RichTextEditor";

export default {
  name: 'AddNoticeViewList',
  components: {
    RichTextEditor
  },
  data() {
    return {
      addNoticeViewInfo: {
        title: '',
        noticeTypeCd: '',
        context: '',
        startTime: '',
        endTime: '',
        objType: '',
        objId: '',
        floorId: '',
        unitId: '',
        roomId: '',
        state: '3000',
        isAll: 'N'
      },
    }
  },
  created() {
    this.addNoticeViewInfo.communityId = getCommunityId()
  },
  methods: {
    onEditorChange({ quill, html, text }) {
      console.log('editor change!', quill, html, text)
      this.addNoticeViewInfo.context = html
    },
    validateForm() {
      let isValid = true
      if (!this.addNoticeViewInfo.title) {
        this.$message.error(this.$t('addNoticeView.validate.title'))
        isValid = false
      }
      if (!this.addNoticeViewInfo.noticeTypeCd) {
        this.$message.error(this.$t('addNoticeView.validate.noticeType'))
        isValid = false
      }
      if (!this.addNoticeViewInfo.context) {
        this.$message.error(this.$t('addNoticeView.validate.content'))
        isValid = false
      }
      return isValid
    },
    async saveNoticeInfo() {
      if (!this.validateForm()) {
        return
      }

      try {
        const response = await saveNotice(this.addNoticeViewInfo)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('addNoticeView.saveError'))
      }
    },
    closeNoticeInfo() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-notice-view-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .button-group {
    text-align: right;
    margin-top: 20px;
  }

  .editor-wrapper {
    height: 300px;
    margin-bottom: 20px;

    .ql-container {
      height: 250px;
    }
  }
}
</style>