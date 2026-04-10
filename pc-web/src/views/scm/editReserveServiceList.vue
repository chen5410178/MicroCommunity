<template>
  <div class="edit-reserve-service-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('editReserveService.title') }}</span>
      </div>

      <el-form ref="form" :model="editReserveServiceInfo" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('editReserveService.goodsName')" prop="goodsName">
              <el-input
                v-model="editReserveServiceInfo.goodsName"
                :placeholder="$t('editReserveService.goodsNamePlaceholder')"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.params')" prop="paramsId">
              <el-select
                v-model="editReserveServiceInfo.paramsId"
                :placeholder="$t('editReserveService.paramsPlaceholder')"
                style="width:100%"
              >
                <el-option
                  v-for="(item,index) in editReserveServiceInfo.reserveParamss"
                  :key="index"
                  :label="item.name"
                  :value="item.paramsId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.price')" prop="price">
              <el-input
                v-model="editReserveServiceInfo.price"
                :placeholder="$t('editReserveService.pricePlaceholder')"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.sort')" prop="sort">
              <el-input
                v-model="editReserveServiceInfo.sort"
                :placeholder="$t('editReserveService.sortPlaceholder')"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.state')" prop="state">
              <el-select
                v-model="editReserveServiceInfo.state"
                :placeholder="$t('editReserveService.statePlaceholder')"
                style="width:100%"
              >
                <el-option
                  :label="$t('editReserveService.stateOff')"
                  value="1001"
                />
                <el-option
                  :label="$t('editReserveService.stateOn')"
                  value="2002"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.startDate')" prop="startDate">
              <el-date-picker
                v-model="editReserveServiceInfo.startDate"
                type="datetime"
                :placeholder="$t('editReserveService.startDatePlaceholder')"
                style="width:100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.endDate')" prop="endDate">
              <el-date-picker
                v-model="editReserveServiceInfo.endDate"
                type="datetime"
                :placeholder="$t('editReserveService.endDatePlaceholder')"
                style="width:100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editReserveService.imgUrl')" prop="imgUrl">
              <upload-image-url
                ref="uploadImage"
                :limit="1"
                :default-images="[editReserveServiceInfo.imgUrl]"
                @notifyUploadCoverImage="handleImageChange"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('editReserveService.goodsDesc')" prop="goodsDesc">
              <el-input
                v-model="editReserveServiceInfo.goodsDesc"
                type="textarea"
                :placeholder="$t('editReserveService.goodsDescPlaceholder')"
                :rows="3"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('editReserveService.content')">
              <div class="editor-wrapper">
                <rich-text-editor ref="richTextEditor" v-model="editReserveServiceInfo.content" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="handleCancel">
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="primary" @click="handleSave">
              {{ $t('common.save') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import RichTextEditor from "@/components/editor/RichTextEditor";
import { updateReserveGoods, listReserveParams, listReserveGoods } from '@/api/scm/editReserveServiceApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditReserveServiceList',
  components: {
    UploadImageUrl,
    RichTextEditor
  },
  data() {
    return {
      editReserveServiceInfo: {
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
        reserveParamss: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.editReserveServiceInfo.goodsId = this.$route.query.goodsId
    this.listReserveParamss()
    this.listReserveServices()
  },
  methods: {
    handleImageChange(images) {
      this.editReserveServiceInfo.imgUrl = images.length > 0 ? images[0] : ''
    },
    async listReserveParamss() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listReserveParams(params)
        this.editReserveServiceInfo.reserveParamss = data
      } catch (error) {
        console.error('获取参数列表失败:', error)
        this.$message.error(this.$t('editReserveService.loadParamsFailed'))
      }
    },
    async listReserveServices() {
      try {
        const params = {
          page: 1,
          row: 1,
          goodsId: this.editReserveServiceInfo.goodsId,
          communityId: this.communityId
        }
        const { data } = await listReserveGoods(params)
        if (data && data.length > 0) {
          Object.assign(this.editReserveServiceInfo, data[0])
          this.$refs.richTextEditor.setContent(this.editReserveServiceInfo.content)
          this.$refs.uploadImage.setImages([this.editReserveServiceInfo.imgUrl])

        }
      } catch (error) {
        console.error('获取服务详情失败:', error)
        this.$message.error(this.$t('editReserveService.loadServiceFailed'))
      }
    },
    validateForm() {
      const requiredFields = [
        'goodsName',
        'paramsId',
        'price',
        'sort',
        'startDate',
        'endDate',
        'imgUrl',
        'state',
        'goodsDesc',
        'content'
      ]

      for (const field of requiredFields) {
        if (!this.editReserveServiceInfo[field]) {
          this.$message.error(this.$t(`editReserveService.${field}Required`))
          return false
        }
      }
      return true
    },
    async handleSave() {
      try {
        if (!this.validateForm()) return

        this.editReserveServiceInfo.communityId = this.communityId
        const { code, msg } = await updateReserveGoods(this.editReserveServiceInfo)
        
        if (code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(msg)
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error(this.$t('common.saveError'))
      }
    },
    handleCancel() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-reserve-service-container {
  padding: 20px;

  .box-card {
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