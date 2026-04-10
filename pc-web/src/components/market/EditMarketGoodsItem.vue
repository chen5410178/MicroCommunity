<template>
  <el-dialog 
    :title="$t('common.edit')" 
    :visible.sync="visible" 
    width="50%"
    @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('marketGoodsItem.table.prodName')" prop="prodName">
        <el-input 
          v-model="form.prodName" 
          :placeholder="$t('marketGoodsItem.table.prodName')" />
      </el-form-item>
      <el-form-item :label="$t('marketGoodsItem.table.prodDesc')" prop="prodDesc">
        <el-input 
          v-model="form.prodDesc" 
          type="textarea" 
          :placeholder="$t('marketGoodsItem.table.prodDesc')" />
      </el-form-item>
      <el-form-item :label="$t('marketGoodsItem.table.picUrl')" prop="picUrl">
        <upload-image-url ref="uploadImage" :image-count="1"  @notifyUploadCoverImage="handleImageChange" />
      </el-form-item>
      <el-form-item :label="$t('marketGoodsItem.table.picLink')" prop="picLink">
        <el-input 
          v-model="form.picLink" 
          :placeholder="$t('marketGoodsItem.table.picLink')" />
      </el-form-item>
      <el-form-item :label="$t('marketGoodsItem.table.price')" prop="price">
        <el-input 
          v-model="form.price" 
          :placeholder="$t('marketGoodsItem.table.price')" />
      </el-form-item>
  
      <el-form-item :label="$t('marketGoodsItem.table.shopName')" prop="shopName">
        <el-input 
          v-model="form.shopName" 
          :placeholder="$t('marketGoodsItem.table.shopName')" />
      </el-form-item>
      <el-form-item :label="$t('marketGoodsItem.table.sort')" prop="sort">
        <el-input-number 
          v-model="form.sort" 
          :min="1" 
          :placeholder="$t('marketGoodsItem.table.sort')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateMarketGoodsItem } from '@/api/market/marketGoodsItemManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'EditMarketGoodsItem',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        itemId: '',
        goodsId: '',
        prodName: '',
        prodDesc: '',
        picUrl: '',
        picLink: '',
        price: '',
        shopName: '',
        sort: 1
      },
      rules: {
        prodName: [
          { required: true, message: this.$t('marketGoodsItem.validate.prodNameRequired'), trigger: 'blur' },
          { max: 128, message: this.$t('marketGoodsItem.validate.prodNameMaxLength'), trigger: 'blur' }
        ],
        prodDesc: [
          { required: true, message: this.$t('marketGoodsItem.validate.prodDescRequired'), trigger: 'blur' },
          { max: 256, message: this.$t('marketGoodsItem.validate.prodDescMaxLength'), trigger: 'blur' }
        ],
        picUrl: [
          { required: true, message: this.$t('marketGoodsItem.validate.picUrlRequired'), trigger: 'blur' }
        ],
        picLink: [
          { required: true, message: this.$t('marketGoodsItem.validate.picLinkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('marketGoodsItem.validate.picLinkMaxLength'), trigger: 'blur' }
        ],
        price: [
          { required: true, message: this.$t('marketGoodsItem.validate.priceRequired'), trigger: 'blur' },
          { max: 10, message: this.$t('marketGoodsItem.validate.priceMaxLength'), trigger: 'blur' }
        ],
        shopName: [
          { required: true, message: this.$t('marketGoodsItem.validate.shopNameRequired'), trigger: 'blur' },
          { max: 128, message: this.$t('marketGoodsItem.validate.shopNameMaxLength'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('marketGoodsItem.validate.sortRequired'), trigger: 'blur' }
        ],
        itemId: [
          { required: true, message: this.$t('marketGoodsItem.validate.itemIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        ...data
      }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })

      setTimeout(() => {
        this.$refs.uploadImage.setImages([data.picUrl])
      },500)
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleImageChange(images) {
      if (images && images.length > 0) {
        this.form.picUrl = images[0]
      } else {
        this.form.picUrl = ''
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMarketGoodsItem(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>