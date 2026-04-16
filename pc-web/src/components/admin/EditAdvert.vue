<template>
  <el-dialog :title="$t('advertManage.edit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('advertManage.edit.adName')" prop="adName">
        <el-input v-model="form.adName" :placeholder="$t('advertManage.edit.adNamePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.classify')" prop="classify">
        <el-select v-model="form.classify" :placeholder="$t('advertManage.edit.classifyPlaceholder')" style="width:100%">
          <el-option v-for="item in classifyOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.locationTypeCd')" prop="locationTypeCd">
        <el-select v-model="form.locationTypeCd" :placeholder="$t('advertManage.edit.locationPlaceholder')" style="width:100%">
          <el-option v-for="item in locationOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.advertType')" prop="advertType">
        <el-select v-model="form.advertType" :placeholder="$t('advertManage.edit.advertTypePlaceholder')" style="width:100%">
          <el-option v-for="item in advertTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.pageUrl')">
        <el-input v-model="form.pageUrl" :placeholder="$t('advertManage.edit.pageUrlPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.seq')" prop="seq">
        <el-input v-model="form.seq" :placeholder="$t('advertManage.edit.seqPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.startTime')" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime"
          :placeholder="$t('advertManage.edit.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%"/>
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.endTime')" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime" :placeholder="$t('advertManage.edit.endTimePlaceholder')" style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>

      <el-form-item :label="$t('advertManage.edit.viewType')" prop="viewType">
        <el-select v-model="form.viewType" :placeholder="$t('advertManage.edit.viewTypePlaceholder')" style="width:100%">
          <el-option v-for="item in viewTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.viewType === '8888'" :label="$t('advertManage.edit.image')">
        <upload-image-url ref="uploadImage" :limit="1" @notifyUploadCoverImage="handleImageChange" />
      </el-form-item>

      <div class="dialog-footer">
        <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script>
import { updateAdvert, getAdvertItems } from '@/api/admin/advertManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'EditAdvert',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        advertId: '',
        adName: '',
        classify: '',
        locationTypeCd: '',
        advertType: '',
        pageUrl: '',
        seq: '',
        startTime: '',
        endTime: '',
        viewType: '8888'
      },
      defaultImages: [],
      rules: {
        adName: [
          { required: true, message: this.$t('advertManage.validate.adNameRequired'), trigger: 'blur' }
        ],
        classify: [
          { required: true, message: this.$t('advertManage.validate.classifyRequired'), trigger: 'change' }
        ],
        locationTypeCd: [
          { required: true, message: this.$t('advertManage.validate.locationRequired'), trigger: 'change' }
        ],
        advertType: [
          { required: true, message: this.$t('advertManage.validate.advertTypeRequired'), trigger: 'change' }
        ],
        seq: [
          { required: true, message: this.$t('advertManage.validate.seqRequired'), trigger: 'blur' },
          { pattern: /^\d+$/, message: this.$t('advertManage.validate.seqNumber'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('advertManage.validate.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('advertManage.validate.endTimeRequired'), trigger: 'change' }
        ],
        viewType: [
          { required: true, message: this.$t('advertManage.validate.viewTypeRequired'), trigger: 'change' }
        ]
      },
      classifyOptions: [
        { value: '9001', label: this.$t('advertManage.classify.logistics') },
        { value: '9002', label: this.$t('advertManage.classify.catering') },
        { value: '9003', label: this.$t('advertManage.classify.travel') },
        { value: '9004', label: this.$t('advertManage.classify.hotel') },
        { value: '9005', label: this.$t('advertManage.classify.education') },
        { value: '9006', label: this.$t('advertManage.classify.internet') }
      ],
      locationOptions: [
        { value: '1000', label: this.$t('advertManage.location.device') },
        { value: '2000', label: this.$t('advertManage.location.ownerHome') },
        { value: '3000', label: this.$t('advertManage.location.businessHome') },
        { value: '5000', label: this.$t('advertManage.location.serviceHome') },
        { value: '6000', label: this.$t('advertManage.location.convenienceHome') },
        { value: '4000', label: this.$t('advertManage.location.staffHome') }
      ],
      advertTypeOptions: [
        { value: '1', label: this.$t('advertManage.advertType.site') },
        { value: '2', label: this.$t('advertManage.advertType.outside') },
        { value: '3', label: this.$t('advertManage.advertType.noJump') }
      ],
      viewTypeOptions: [
        { value: '8888', label: this.$t('advertManage.edit.image') }
      ]
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.resetForm()
      this.form = { ...row }
      this.loadAdvertItems(row.advertId)
    },
    handleImageChange(images) {
      if (images && images.length > 0) {
        this.form.photos = images
      } else {
        this.form.photos = []
      }
    },
    async loadAdvertItems(advertId) {
      try {
        const { advertItems } = await getAdvertItems({ advertId,page:1,row:100 })
        this.defaultImages = advertItems.filter(item => item.itemTypeCd === '8888').map(item => (item.url))
        this.form.photos = this.defaultImages
        setTimeout(() => {
          this.$refs.uploadImage.setImages(this.defaultImages)
        }, 500)
      } catch (error) {
        console.error(error)
      }
    },
    resetForm() {
      this.$refs.form && this.$refs.form.resetFields()
      this.$refs.uploadImage && this.$refs.uploadImage.clear()
      this.defaultImages = []
    },
    handleClose() {
      this.resetForm()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

   
        const params = {
          ...this.form,
          photos: this.form.photos
        }

        await updateAdvert(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>