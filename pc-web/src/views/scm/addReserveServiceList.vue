<template>
  <div class="add-reserve-service-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addReserveService.title') }}</span>
      </div>
      <el-form ref="form" :model="addReserveServiceInfo" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.name')" prop="goodsName">
              <el-input v-model="addReserveServiceInfo.goodsName" :placeholder="$t('addReserveService.namePlaceholder')"
                clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.params')" prop="paramsId">
              <el-select v-model="addReserveServiceInfo.paramsId"
                :placeholder="$t('addReserveService.paramsPlaceholder')" style="width:100%">
                <el-option v-for="(item, index) in addReserveServiceInfo.reserveParamss" :key="index" :label="item.name"
                  :value="item.paramsId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.price')" prop="price">
              <el-input v-model="addReserveServiceInfo.price" :placeholder="$t('addReserveService.pricePlaceholder')"
                clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.sort')" prop="sort">
              <el-input v-model="addReserveServiceInfo.sort" :placeholder="$t('addReserveService.sortPlaceholder')"
                clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.startDate')" prop="startDate">
              <el-date-picker v-model="addReserveServiceInfo.startDate" type="date" value-format="yyyy-MM-dd"
                :placeholder="$t('addReserveService.startDatePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.endDate')" prop="endDate">
              <el-date-picker v-model="addReserveServiceInfo.endDate" type="date" value-format="yyyy-MM-dd"
                :placeholder="$t('addReserveService.endDatePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.coverImage')" prop="imgUrl">
              <upload-image-url ref="uploadImage" :limit="1" @notifyUploadCoverImage="handleImageChange" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveService.state')" prop="state">
              <el-select v-model="addReserveServiceInfo.state" :placeholder="$t('addReserveService.statePlaceholder')"
                style="width:100%">
                <el-option :label="$t('addReserveService.stateOff')" value="1001" />
                <el-option :label="$t('addReserveService.stateOn')" value="2002" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addReserveService.goodsDesc')" prop="goodsDesc">
              <el-input v-model="addReserveServiceInfo.goodsDesc" type="textarea" :rows="3"
                :placeholder="$t('addReserveService.goodsDescPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addReserveService.content')">
              <div class="editor-wrapper">
                <rich-text-editor ref="richTextEditor" v-model="addReserveServiceInfo.content" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="handleCancel">
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="primary" @click="handleSubmit">
              {{ $t('common.save') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { saveReserveGoods, listReserveParams } from '@/api/scm/addReserveServiceApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import RichTextEditor from "@/components/editor/RichTextEditor";
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddReserveService',
  components: {
    UploadImageUrl,
    RichTextEditor
  },
  data() {
    return {
      addReserveServiceInfo: {
        goodsId: '',
        goodsName: '',
        goodsDesc: '',
        type: '2002',
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
    this.addReserveServiceInfo.catalogId = this.$route.query.catalogId
    this.loadReserveParams()
  },
  methods: {
    async loadReserveParams() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.addReserveServiceInfo.communityId
        }
        const { data } = await listReserveParams(params)
        this.addReserveServiceInfo.reserveParamss = data
      } catch (error) {
        console.error('Failed to load reserve params:', error)
        this.$message.error(this.$t('addReserveService.loadParamsFailed'))
      }
    },
    handleImageChange(images) {
      this.addReserveServiceInfo.imgUrl = images.length > 0 ? images[0] : ''
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
        if (!this.addReserveServiceInfo[field]) {
          this.$message.error(this.$t(`addReserveService.${field}Required`))
          return false
        }
      }

      return true
    },
    async handleSubmit() {
      if (!this.validateForm()) return

      try {
        const response = await saveReserveGoods(this.addReserveServiceInfo)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('Failed to save reserve service:', error)
        this.$message.error(this.$t('addReserveService.saveFailed'))
      }
    },
    handleCancel() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-reserve-service-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .editor-wrapper {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
  }

  .text-right {
    text-align: right;
  }
}
</style>