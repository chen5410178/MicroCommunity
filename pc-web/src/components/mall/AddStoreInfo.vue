<template>
  <el-dialog
    :title="$t('storeInfoManage.common.add')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item
        :label="$t('storeInfoManage.table.storeName')"
        prop="name"
      >
        <el-input
          v-model="form.name"
          :placeholder="$t('storeInfoManage.search.storeName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.convenienceType')"
        prop="convenienceMenusId"
      >
        <el-select
          v-model="form.convenienceMenusId"
          :placeholder="$t('storeInfoManage.search.convenienceType')"
          style="width: 100%"
        >
          <el-option
            v-for="item in menuInfos"
            :key="item.convenienceMenusId"
            :label="item.name"
            :value="item.convenienceMenusId"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.isShow')"
        prop="isShow"
      >
        <el-select
          v-model="form.isShow"
          :placeholder="$t('storeInfoManage.search.isShow')"
          style="width: 100%"
        >
          <el-option
            :label="$t('storeInfoManage.common.yes')"
            value="Y"
          />
          <el-option
            :label="$t('storeInfoManage.common.no')"
            value="N"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.icon')"
        prop="icon"
      >
        <upload-image
          ref="uploadImage"
          :image-count="1"
          @change="handleImageChange"
        />
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.tel')"
        prop="tel"
      >
        <el-input
          v-model="form.tel"
          :placeholder="$t('storeInfoManage.search.storeName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.site')"
        prop="site"
      >
        <el-input
          v-model="form.site"
          :placeholder="$t('storeInfoManage.search.storeName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.seq')"
        prop="seq"
      >
        <el-input
          v-model="form.seq"
          :placeholder="$t('storeInfoManage.search.storeName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.workTime')"
        prop="workTime"
      >
        <el-input
          v-model="form.workTime"
          :placeholder="$t('storeInfoManage.search.storeName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('storeInfoManage.table.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="4"
          :placeholder="$t('storeInfoManage.search.storeName')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('storeInfoManage.common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('storeInfoManage.common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveStoreInfo } from '@/api/mall/storeInfoManageApi'
import UploadImage from '@/components/mall/UploadImage'

export default {
  name: 'AddStoreInfo',
  components: {
    UploadImage
  },
  props: {
    menuInfos: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      visible: false,
      form: {
        name: '',
        convenienceMenusId: '',
        isShow: '',
        icon: '',
        tel: '',
        site: '',
        seq: '',
        workTime: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('storeInfoManage.search.storeName'), trigger: 'blur' },
          { max: 50, message: this.$t('storeInfoManage.error.maxLength50'), trigger: 'blur' }
        ],
        tel: [
          { max: 13, message: this.$t('storeInfoManage.error.maxLength13'), trigger: 'blur' }
        ],
        site: [
          { max: 100, message: this.$t('storeInfoManage.error.maxLength100'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('storeInfoManage.error.required'), trigger: 'blur' },
          { type: 'number', message: this.$t('storeInfoManage.error.number'), trigger: 'blur' }
        ],
        workTime: [
          { max: 200, message: this.$t('storeInfoManage.error.maxLength200'), trigger: 'blur' }
        ],
        remark: [
          { max: 5000, message: this.$t('storeInfoManage.error.maxLength5000'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
        this.$refs.uploadImage && this.$refs.uploadImage.clear()
      })
    },
    handleClose() {
      this.visible = false
      this.$emit('close')
    },
    handleImageChange(images) {
      this.form.icon = images.length > 0 ? images[0] : ''
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        const formData = { ...this.form }
        if (formData.seq) {
          formData.seq = Number(formData.seq)
        }
        
        await saveStoreInfo(formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        if (error !== 'validate') {
          this.$message.error(error.message || this.$t('storeInfoManage.error.saveFailed'))
        }
      }
    }
  }
}
</script>