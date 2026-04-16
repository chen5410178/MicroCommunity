<template>
  <el-dialog :title="$t('activitiesManage.add.title')" :visible.sync="dialogVisible" width="70%" @close="handleClose">
    <el-form ref="form" :model="form" label-width="120px" :rules="rules">
      <el-form-item :label="$t('activitiesManage.add.title')" prop="title">
        <el-input v-model="form.title" :placeholder="$t('activitiesManage.add.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.add.typeCd')" prop="typeCd">
        <el-select v-model="form.typeCd" :placeholder="$t('activitiesManage.add.typeCdPlaceholder')" style="width:100%">
          <el-option v-for="item in typeCds" :key="item.typeCd" :label="item.typeName" :value="item.typeCd" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.add.isMoreCommunity')">
        <el-select v-model="form.isMoreCommunity" :placeholder="$t('activitiesManage.add.isMoreCommunityPlaceholder')"
          style="width:100%">
          <el-option :label="$t('common.yes')" value="Y" />
          <el-option :label="$t('common.no')" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.add.headerImg')" prop="headerImg">
        <upload-image-url ref="uploadImage" :limit="1" @notifyUploadCoverImage="handleImageChange" />

      </el-form-item>
      <el-form-item :label="$t('activitiesManage.add.startTime')" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime"
          :placeholder="$t('activitiesManage.add.startTimePlaceholder')" style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.add.endTime')" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime"
          :placeholder="$t('activitiesManage.add.endTimePlaceholder')" style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.add.context')" prop="context">
        <rich-text-editor ref="richTextEditor" v-model="form.context" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { saveActivities, listActivitiesType } from '@/api/oa/activitiesManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import RichTextEditor from "@/components/editor/RichTextEditor";

export default {
  name: 'AddActivitiesView',
  components: {
    UploadImageUrl,
    RichTextEditor
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        title: '',
        typeCd: '',
        headerImg: '',
        context: '',
        startTime: '',
        endTime: '',
        isMoreCommunity: '',
        communityId: ''
      },
      typeCds: [],
      rules: {
        title: [
          { required: true, message: this.$t('activitiesManage.validate.titleRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('activitiesManage.validate.titleMaxLength'), trigger: 'blur' }
        ],
        typeCd: [
          { required: true, message: this.$t('activitiesManage.validate.typeCdRequired'), trigger: 'change' }
        ],
        headerImg: [
          { required: true, message: this.$t('activitiesManage.validate.headerImgRequired'), trigger: 'change' }
        ],
        context: [
          { required: true, message: this.$t('activitiesManage.validate.contextRequired'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('activitiesManage.validate.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('activitiesManage.validate.endTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
        this.$refs.uploadImage && this.$refs.uploadImage.clearImages()
        this.loadActivitiesType()
      })
    },
    async loadActivitiesType() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId()
        }
        const { data } = await listActivitiesType(params)
        this.typeCds = data
      } catch (error) {
        console.error('获取活动类型失败:', error)
      }
    },
    handleImageChange(images) {
      this.form.headerImg = images.length > 0 ? images[0] : ''
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            this.form.communityId = getCommunityId()
            await saveActivities(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('保存活动失败:', error)
          }
        }
      })
    },
    handleClose() {
      if (this.$refs.richTextEditor) {
        this.$refs.richTextEditor.clear();
      }
      this.$refs.form.resetFields()
    }
  }
}
</script>