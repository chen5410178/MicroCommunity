<template>
  <div class="edit-reserve-dining-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>{{ $t('editReserveDining.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form label-width="120px" label-position="right">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.goodsName')">
                  <el-input v-model="editReserveDiningInfo.goodsName"
                    :placeholder="$t('editReserveDining.requiredGoodsName')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.params')">
                  <el-select v-model="editReserveDiningInfo.paramsId" style="width:100%"
                    :placeholder="$t('editReserveDining.requiredParams')">
                    <el-option v-for="(item, index) in editReserveDiningInfo.reserveParamss" :key="index"
                      :label="item.name" :value="item.paramsId" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.price')">
                  <el-input v-model="editReserveDiningInfo.price"
                    :placeholder="$t('editReserveDining.requiredPrice')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.sort')">
                  <el-input v-model="editReserveDiningInfo.sort" :placeholder="$t('editReserveDining.requiredSort')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.startDate')">
                  <el-date-picker v-model="editReserveDiningInfo.startDate" type="datetime" style="width:100%"
                    :placeholder="$t('editReserveDining.requiredStartDate')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.endDate')">
                  <el-date-picker v-model="editReserveDiningInfo.endDate" type="datetime" style="width:100%"
                    :placeholder="$t('editReserveDining.requiredEndDate')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.coverImage')">
                  <upload-image-url ref="uploadImage" :limit="1" 
                    :default-images="[editReserveDiningInfo.imgUrl]"
                    @notifyUploadCoverImage="handleImageChange" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editReserveDining.state')">
                  <el-select v-model="editReserveDiningInfo.state" style="width:100%"
                    :placeholder="$t('editReserveDining.requiredState')">
                    <el-option :label="$t('editReserveDining.stateNotOnShelf')" value="1001" />
                    <el-option :label="$t('editReserveDining.stateOnShelf')" value="2002" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('editReserveDining.goodsDesc')">
                  <el-input v-model="editReserveDiningInfo.goodsDesc" type="textarea" :rows="3"
                    :placeholder="$t('editReserveDining.requiredGoodsDesc')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('editReserveDining.content')">
                  <div class="editor-wrapper">
                    <rich-text-editor ref="richTextEditor" v-model="editReserveDiningInfo.content" />
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24" class="text-right">
                <el-button type="warning" @click="handleCancel">
                  {{ $t('common.cancel') }}
                </el-button>
                <el-button type="primary" @click="saveReserveDiningInfo">
                  {{ $t('common.save') }}
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import RichTextEditor from "@/components/editor/RichTextEditor";
import { updateReserveGoods, listReserveParams, listReserveGoods } from '@/api/scm/editReserveDiningApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditReserveDining',
  components: {
    UploadImageUrl,
    RichTextEditor
  },
  data() {
    return {
      editReserveDiningInfo: {
        goodsId: '',
        goodsName: '',
        goodsDesc: '',
        type: '1001',
        paramsId: '',
        price: '',
        sort: '',
        state: '',
        startDate: '',
        endDate: '',
        imgUrl: '',
        catalogId: '',
        content: '',
        reserveParamss: [],
        communityId: getCommunityId()
      }
    }
  },
  created() {
    this.editReserveDiningInfo.goodsId = this.$route.query.goodsId
    this.listReserveParamss()
    this.listReserveDinings()
  },
  methods: {
    handleImageChange(images) {
      this.editReserveDiningInfo.imgUrl = images.length > 0 ? images[0] : ''
    },
    validateForm() {
      if (!this.editReserveDiningInfo.goodsName) {
        this.$message.error(this.$t('editReserveDining.validate.goodsName'))
        return false
      }
      if (!this.editReserveDiningInfo.paramsId) {
        this.$message.error(this.$t('editReserveDining.validate.paramsId'))
        return false
      }
      if (!this.editReserveDiningInfo.price) {
        this.$message.error(this.$t('editReserveDining.validate.price'))
        return false
      }
      if (!this.editReserveDiningInfo.sort) {
        this.$message.error(this.$t('editReserveDining.validate.sort'))
        return false
      }
      if (!this.editReserveDiningInfo.startDate) {
        this.$message.error(this.$t('editReserveDining.validate.startDate'))
        return false
      }
      if (!this.editReserveDiningInfo.endDate) {
        this.$message.error(this.$t('editReserveDining.validate.endDate'))
        return false
      }
      if (!this.editReserveDiningInfo.imgUrl) {
        this.$message.error(this.$t('editReserveDining.validate.imgUrl'))
        return false
      }
      if (!this.editReserveDiningInfo.state) {
        this.$message.error(this.$t('editReserveDining.validate.state'))
        return false
      }
      if (!this.editReserveDiningInfo.goodsDesc) {
        this.$message.error(this.$t('editReserveDining.validate.goodsDesc'))
        return false
      }
      if (!this.editReserveDiningInfo.content) {
        this.$message.error(this.$t('editReserveDining.validate.content'))
        return false
      }
      return true
    },
    async saveReserveDiningInfo() {
      if (!this.validateForm()) {
        return
      }

      try {
        const res = await updateReserveGoods(this.editReserveDiningInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.saveError'))
      }
    },
    async listReserveParamss() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.editReserveDiningInfo.communityId
        }
        const res = await listReserveParams(params)
        this.editReserveDiningInfo.reserveParamss = res.data
      } catch (error) {
        this.$message.error(this.$t('editReserveDining.fetchParamsError'))
      }
    },
    async listReserveDinings() {
      try {
        const params = {
          page: 1,
          row: 1,
          goodsId: this.editReserveDiningInfo.goodsId,
          communityId: this.editReserveDiningInfo.communityId
        }
        const res = await listReserveGoods(params)
        if (res.data && res.data.length > 0) {
          Object.assign(this.editReserveDiningInfo, res.data[0])
          this.$refs.richTextEditor.setContent(this.editReserveDiningInfo.content)
          this.$refs.uploadImage.setImages([this.editReserveDiningInfo.imgUrl])
        }
      } catch (error) {
        this.$message.error(this.$t('editReserveDining.fetchGoodsError'))
      }
    },
    handleCancel() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-reserve-dining-container {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;
  }

  .text-right {
    text-align: right;
  }

  .editor-wrapper {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
  }
}
</style>