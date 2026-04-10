<template>
  <el-dialog :title="$t('activitiesManage.edit.title')" :visible.sync="dialogVisible" width="70%" @close="handleClose">
    <el-form ref="form" :model="form" label-width="120px" :rules="rules">
      <el-form-item :label="$t('activitiesManage.edit.title')" prop="title">
        <el-input v-model="form.title" :placeholder="$t('activitiesManage.edit.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.edit.typeCd')" prop="typeCd">
        <el-select v-model="form.typeCd" :placeholder="$t('activitiesManage.edit.typeCdPlaceholder')" style="width:100%">
          <el-option v-for="item in typeCds" :key="item.typeCd" :label="item.typeName" :value="item.typeCd" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.edit.headerImg')" prop="headerImg">
        <upload-image-url ref="uploadImage" :limit="1" @notifyUploadCoverImage="handleImageChange" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.edit.startTime')" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime"
          :placeholder="$t('activitiesManage.edit.startTimePlaceholder')" style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.edit.endTime')" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime"
          :placeholder="$t('activitiesManage.edit.endTimePlaceholder')" style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item :label="$t('activitiesManage.edit.context')" prop="context">
        <rich-text-editor ref="richTextEditor" v-model="form.context" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateActivities, getActivitiesDetail, listActivitiesType } from '@/api/oa/activitiesManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import RichTextEditor from "@/components/editor/RichTextEditor";

export default {
  name: 'EditActivitiesView',
  components: {
    UploadImageUrl,
    RichTextEditor
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        activitiesId: '',
        title: '',
        typeCd: '',
        headerImg: '',
        context: '',
        startTime: '',
        endTime: '',
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
        ],
        activitiesId: [
          { required: true, message: this.$t('activitiesManage.validate.activitiesIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.$nextTick(async () => {
        this.$refs.form && this.$refs.form.resetFields()
        this.loadActivitiesType()
        await this.loadActivitiesDetail(row.activitiesId)
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
    async loadActivitiesDetail(activitiesId) {
      try {
        const params = {
          activitiesId,
          page: 1,
          row: 50,
          communityId: getCommunityId()
        }
        const {activitiess} = await getActivitiesDetail(params)
        this.form = { ...activitiess[0] }
        this.$refs.uploadImage.setImages([activitiess[0].headerImg])
        this.$refs.richTextEditor.setContent(activitiess[0].context)
      } catch (error) {
        console.error('获取活动详情失败:', error)
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
            await updateActivities(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('更新活动失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>