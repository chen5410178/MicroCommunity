<template>
  <div class="add-reserve-dining-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addReserveDining.title') }}</span>
      </div>
      <el-form ref="form" :model="addReserveDiningInfo" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.goodsName')" prop="goodsName">
              <el-input v-model="addReserveDiningInfo.goodsName"
                :placeholder="$t('addReserveDining.goodsNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.paramsId')" prop="paramsId">
              <el-select v-model="addReserveDiningInfo.paramsId"
                :placeholder="$t('addReserveDining.paramsIdPlaceholder')" style="width:100%">
                <el-option v-for="(item, index) in addReserveDiningInfo.reserveParamss" :key="index" :label="item.name"
                  :value="item.paramsId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.price')" prop="price">
              <el-input v-model="addReserveDiningInfo.price" :placeholder="$t('addReserveDining.pricePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.sort')" prop="sort">
              <el-input v-model="addReserveDiningInfo.sort" :placeholder="$t('addReserveDining.sortPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.startDate')" prop="startDate">
              <el-date-picker v-model="addReserveDiningInfo.startDate" type="date" value-format="yyyy-MM-dd"
                :placeholder="$t('addReserveDining.startDatePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.endDate')" prop="endDate">
              <el-date-picker v-model="addReserveDiningInfo.endDate" type="date" value-format="yyyy-MM-dd"
                :placeholder="$t('addReserveDining.endDatePlaceholder')" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.imgUrl')" prop="imgUrl">

                <upload-image-url ref="uploadImage" :limit="1" @notifyUploadCoverImage="handleImageChange" />

            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addReserveDining.state')" prop="state">
              <el-select v-model="addReserveDiningInfo.state" :placeholder="$t('addReserveDining.statePlaceholder')"
                style="width:100%">
                <el-option :label="$t('addReserveDining.stateOff')" value="1001" />
                <el-option :label="$t('addReserveDining.stateOn')" value="2002" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addReserveDining.goodsDesc')" prop="goodsDesc">
              <el-input v-model="addReserveDiningInfo.goodsDesc" type="textarea" :rows="3"
                :placeholder="$t('addReserveDining.goodsDescPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addReserveDining.content')">
              <div class="editor-wrapper">
                <rich-text-editor ref="richTextEditor" v-model="addReserveDiningInfo.content" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="goBack">
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="primary" @click="saveReserveDiningInfo">
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
import { saveReserveGoods, listReserveParams } from '@/api/scm/addReserveDiningApi'
import { getCommunityId } from '@/api/community/communityApi'
import RichTextEditor from "@/components/editor/RichTextEditor";

export default {
  name: 'AddReserveDining',
  components: {
    UploadImageUrl,
    RichTextEditor
  },
  data() {
    return {
      addReserveDiningInfo: {
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
      }
    }
  },
  created() {
    this.addReserveDiningInfo.catalogId = this.$route.query.catalogId
    this.listReserveParamss()
    this.initGoodsContent()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    handleImageChange(images) {
      this.addReserveDiningInfo.imgUrl = images.length > 0 ? images[0] : ''
    },
    async saveReserveDiningInfo() {
      try {
        if (!this.validateForm()) {
          return
        }

        this.addReserveDiningInfo.communityId = getCommunityId()
        const response = await saveReserveGoods(this.addReserveDiningInfo)

        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    validateForm() {
      // 这里应该添加表单验证逻辑
      return true
    },
    async listReserveParamss() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const response = await listReserveParams(params)
        this.addReserveDiningInfo.reserveParamss = response.data
      } catch (error) {
        console.error('获取参数列表失败:', error)
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.add-reserve-dining-container {
  padding: 20px;

  .editor-wrapper {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
  }

  .text-right {
    text-align: right;
  }
}
</style>