<template>
  <div class="edit-notice-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('editNoticeView.title') }}</span>
      </div>

      <el-form ref="form" :model="editNoticeViewInfo" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editNoticeView.titleLabel')" prop="title">
              <el-input v-model="editNoticeViewInfo.title" :placeholder="$t('editNoticeView.titlePlaceholder')"
                clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editNoticeView.contentLabel')" prop="context">
              <div class="editor-wrapper">
                <rich-text-editor ref="richTextEditor" v-model="editNoticeViewInfo.context" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="button-group">
            
            <el-button type="warning" @click="closeEditNoticeInfo" style="margin-left: 20px;">
              <i class="el-icon-close"></i>
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="primary" @click="editNotice">
              <i class="el-icon-check"></i>
              {{ $t('common.save') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { updateNotice, getNoticeDetail } from '@/api/oa/editNoticeViewApi'
import { getCommunityId } from '@/api/community/communityApi'
import RichTextEditor from "@/components/editor/RichTextEditor";


export default {
  name: 'EditNoticeViewList',
  data() {
    return {
      editNoticeViewInfo: {
        noticeId: '',
        title: '',
        noticeTypeCd: '',
        context: '',
        startTime: '',
        endTime: '',
        communityId: ''
      }
    }
  },
  components: {
    RichTextEditor
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.editNoticeViewInfo.noticeId = this.$route.query.noticeId
      this.editNoticeViewInfo.communityId = getCommunityId()
      await this._initEditNoticeInfo()
    },
    async _initEditNoticeInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.editNoticeViewInfo.communityId,
          noticeId: this.editNoticeViewInfo.noticeId
        }
        const { notices } = await getNoticeDetail(params)
        this.editNoticeViewInfo = { ...notices[0] }
        this.$refs.richTextEditor.setContent(notices[0].context)

      } catch (error) {
        this.$message.error(this.$t('editNoticeView.fetchError'))
      }
    },
    editNoticeValidate() {


      return this.$refs.form.validate(valid => {
        if (!valid) {
          return false
        }
        return true
      })
    },
    async editNotice() {

      try {
        const { code, msg } = await updateNotice(this.editNoticeViewInfo)
        if (code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.closeEditNoticeInfo()
        } else {
          this.$message.error(msg)
        }
      } catch (error) {
        this.$message.error(this.$t('editNoticeView.updateError'))
      }
    },
    closeEditNoticeInfo() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-notice-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .editor-wrapper {
    width: 100%;
  }

  .button-group {
    text-align: right;
    margin-top: 20px;
  }
}
</style>