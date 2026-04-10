<template>
  <el-dialog :title="$t('resourceStoreManage.editResource')" :visible.sync="dialogVisible" width="70%"
    :close-on-click-modal="false" @closed="handleClosed">
    <el-form ref="editForm" :model="formData" :rules="rules" label-width="180px" label-position="right">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.resourceName')" prop="resName">
            <el-input v-model.trim="formData.resName" :placeholder="$t('resourceStoreManage.enterResourceName')"
              :disabled="formData.shType === '2807'"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.resourceCode')" prop="resCode">
            <el-input v-model.trim="formData.resCode" :placeholder="$t('resourceStoreManage.enterResourceCode')"
              disabled></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.resourceType')" prop="parentRstId">
            <el-select v-model="formData.parentRstId" style="width:100%" :disabled="formData.shType === '2807'"
              @change="handleResourceTypeChange">
              <el-option v-for="(item, index) in parentRstIds" :key="index" :label="item.name"
                :value="item.rstId"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.secondCategory')" prop="rstId">
            <el-select v-model="formData.rstId" style="width:100%" :disabled="formData.shType === '2807'"
              @change="handleSonResourceTypeChange">
              <el-option :label="$t('resourceStoreManage.selectSecondCategory')" value="" disabled></el-option>
              <el-option v-for="(item, index) in rstIds" :key="index" :label="item.name"
                :value="item.rstId"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.resourceSpec')" prop="rssId">
            <el-select v-model="formData.rssId" style="width:100%" :disabled="formData.shType === '2807'">
              <el-option :label="$t('resourceStoreManage.selectSpec')" value=""></el-option>
              <el-option v-for="(item, index) in resourceStoreSpecifications" :key="index" :label="item.specName"
                :value="item.rssId"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.unit')" prop="unitCode">
            <el-select v-model="formData.unitCode" style="width:100%" :disabled="formData.shType === '2807'">
              <el-option v-for="(item, index) in unitCodes" :key="index" :label="item.name"
                :value="item.statusCd"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.fixedResource')" prop="isFixed">
            <el-select v-model="formData.isFixed" style="width:100%">
              <el-option :label="$t('resourceStoreManage.selectIsFixed')" value="" disabled></el-option>
              <el-option v-for="(item, index) in isFixeds" :key="index" :label="item.name"
                :value="item.statusCd"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.referencePrice')" prop="price">
            <el-input v-model.trim="formData.price" :placeholder="$t('resourceStoreManage.enterReferencePrice')"
              :disabled="formData.shType === '2807'">
              <template slot="prepend">￥</template>
            </el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.warningStock')" prop="warningStock">
            <el-input v-model.trim="formData.warningStock"
              :placeholder="$t('resourceStoreManage.enterWarningStock')"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.minUnit')" prop="miniUnitCode">
            <el-select v-model="formData.miniUnitCode" style="width:100%" :disabled="formData.shType === '2807'">
              <el-option :label="$t('resourceStoreManage.selectMinUnit')" value="" disabled></el-option>
              <el-option v-for="(item, index) in unitCodes" :key="index" :label="item.name"
                :value="item.statusCd"></el-option>
            </el-select>
            <div class="el-form-item__tip" v-if="formData.shType !== '2807'">
              {{ $t('resourceStoreManage.minUnitTip') }}
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.minUnitQty')" prop="miniUnitStock">
            <el-input v-model.trim="formData.miniUnitStock" :placeholder="$t('resourceStoreManage.enterMinUnitQty')"
              :disabled="formData.shType === '2807'"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.minCharge')" prop="outLowPrice">
            <el-input v-model.trim="formData.outLowPrice" :placeholder="$t('resourceStoreManage.enterMinCharge')"
              @change="validatePrices">
              <template slot="prepend">￥</template>
            </el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.maxCharge')" prop="outHighPrice">
            <el-input v-model.trim="formData.outHighPrice" :placeholder="$t('resourceStoreManage.enterMaxCharge')"
              @change="validatePrices">
              <template slot="prepend">￥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.remark')">
            <el-input type="textarea" :placeholder="$t('resourceStoreManage.enterRemark')"
              v-model.trim="formData.remark"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.description')">
            <el-input type="textarea" :placeholder="$t('resourceStoreManage.enterDescription')"
              v-model.trim="formData.description"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('resourceStoreManage.uploadImage')">
            <upload-image-url ref="uploadImage" :image-count="5"
              @notifyUploadCoverImage="handleImageUpload"></upload-image-url>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="updateResourceStore">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateResourceStore, listStorehouses, listResourceStoreTypes, getResourceStoreType, listResourceStoreSpecifications } from '@/api/resource/resourceStoreManageApi'
import { getDict, getCommunityId } from '@/api/community/communityApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'EditResourceStore',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      dialogVisible: false,
      formData: {
        resId: '',
        resName: '',
        resCode: '',
        parentRstId: '',
        rstId: '',
        rssId: '',
        price: '',
        description: '',
        outLowPrice: '0',
        outHighPrice: '0',
        remark: '',
        unitCode: '',
        miniUnitCode: '',
        miniUnitStock: '',
        warningStock: '',
        isFixed: '',
        shType: '',
        fileUrls: [],
        communityId: getCommunityId()
      },
      parentRstIds: [],
      rstIds: [],
      resourceStoreSpecifications: [],
      storehouses: [],
      unitCodes: [],
      isFixeds: [],
      rules: {
        resName: [
          { required: true, message: this.$t('resourceStoreManage.resourceNameRequired'), trigger: 'blur' },
          { min: 2, max: 100, message: this.$t('resourceStoreManage.resourceNameLength'), trigger: 'blur' }
        ],
        resCode: [
          { required: true, message: this.$t('resourceStoreManage.resourceCodeRequired'), trigger: 'blur' },
          { max: 50, message: this.$t('resourceStoreManage.resourceCodeMaxLength'), trigger: 'blur' }
        ],
        parentRstId: [
          { required: true, message: this.$t('resourceStoreManage.resourceTypeRequired'), trigger: 'change' }
        ],
        rstId: [
          { required: true, message: this.$t('resourceStoreManage.secondCategoryRequired'), trigger: 'change' }
        ],
        unitCode: [
          { required: true, message: this.$t('resourceStoreManage.unitRequired'), trigger: 'change' }
        ],
        isFixed: [
          { required: true, message: this.$t('resourceStoreManage.isFixedRequired'), trigger: 'change' }
        ],
        price: [
          { required: true, message: this.$t('resourceStoreManage.referencePriceRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('resourceStoreManage.priceFormatError'), trigger: 'blur' }
        ],
        warningStock: [
          { required: true, message: this.$t('resourceStoreManage.warningStockRequired'), trigger: 'blur' }
        ],
        miniUnitCode: [
          { required: true, message: this.$t('resourceStoreManage.minUnitRequired'), trigger: 'change' }
        ],
        miniUnitStock: [
          { required: true, message: this.$t('resourceStoreManage.minUnitQtyRequired'), trigger: 'blur' }
        ],
        outLowPrice: [
          { required: true, message: this.$t('resourceStoreManage.minChargeRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('resourceStoreManage.chargeFormatError'), trigger: 'blur' }
        ],
        outHighPrice: [
          { required: true, message: this.$t('resourceStoreManage.maxChargeRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('resourceStoreManage.chargeFormatError'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.formData = { ...row }
      this.$nextTick(() => {
        if (this.$refs.uploadImage) {
          this.$refs.uploadImage.setImages(row.fileUrls || [])
        }
      })
      this.loadData()
    },

    async loadData() {
      await this.loadDictData()
      await this.listResourceStoreTypes()
      await this.listStorehouses()
      await this.loadResourceStoreType()
      await this.listSonResourceStoreTypes()
      await this.listResourceStoreSpecifications()
    },

    async loadDictData() {
      try {
        // 单位字典
        const unitData = await getDict('resource_store', 'unit_code')
        this.unitCodes = unitData || []

        // 是否固定物品字典
        const fixedData = await getDict('resource_store', 'is_fixed')
        this.isFixeds = fixedData || []
      } catch (error) {
        console.error('加载字典数据失败:', error)
      }
    },

    async listResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId(),
          parentId: '0'
        }
        const response = await listResourceStoreTypes(params)
        this.parentRstIds = response.data || []
      } catch (error) {
        console.error('加载物品类型失败:', error)
      }
    },

    async loadResourceStoreType() {
      if (!this.formData.parentRstId) return

      try {
        const response = await getResourceStoreType(this.formData.parentRstId)
        if (response) {
          this.formData.parentRstId = response.rstId
        }
      } catch (error) {
        console.error('加载物品类型详情失败:', error)
      }
    },

    async listSonResourceStoreTypes() {
      if (!this.formData.parentRstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          rstId: this.formData.parentRstId,
          flag: "0"
        }
        const response = await listResourceStoreTypes(params)
        this.rstIds = response.data || []
      } catch (error) {
        console.error('加载二级分类失败:', error)
      }
    },

    async listStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const response = await listStorehouses(params)
        this.storehouses = response || []
      } catch (error) {
        console.error('加载仓库列表失败:', error)
      }
    },

    handleResourceTypeChange(value) {
      this.formData.rstId = ''
      this.rstIds = []
      this.resourceStoreSpecifications = []

      if (!value) return

      this.listSonResourceStoreTypes(value)
    },

    handleSonResourceTypeChange(value) {
      if (!value) return
      this.listResourceStoreSpecifications(value)
    },

    async listResourceStoreSpecifications(rstId) {
      try {
        const params = {
          page: 1,
          row: 100,
          rstId: rstId
        }
        const response = await listResourceStoreSpecifications(params)
        this.resourceStoreSpecifications = response.data || []
      } catch (error) {
        console.error('加载物品规格失败:', error)
      }
    },

    validatePrices() {
      const low = parseFloat(this.formData.outLowPrice)
      const high = parseFloat(this.formData.outHighPrice)

      if (low > high) {
        this.$message.error(this.$t('resourceStoreManage.maxChargeError'))
        this.formData.outHighPrice = ''
      }
    },

    handleImageUpload(images) {
      this.formData.fileUrls = images
    },

    updateResourceStore() {
      this.$refs.editForm.validate(async valid => {
        if (valid) {
          try {
            await updateResourceStore(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('更新物品失败:', error)
            this.$message.error(error.message || this.$t('resourceStoreManage.updateFailed'))
          }
        }
      })
    },

    handleClosed() {
      this.$refs.editForm.resetFields()
      if (this.$refs.uploadImage) {
        this.$refs.uploadImage.clearImages()
      }
      this.formData = {
        resId: '',
        resName: '',
        resCode: '',
        parentRstId: '',
        rstId: '',
        rssId: '',
        price: '',
        description: '',
        outLowPrice: '0',
        outHighPrice: '0',
        remark: '',
        unitCode: '',
        miniUnitCode: '',
        miniUnitStock: '',
        warningStock: '',
        isFixed: '',
        shType: '',
        fileUrls: [],
        communityId: getCommunityId()
      }
      this.rstIds = []
      this.resourceStoreSpecifications = []
    }
  }
}
</script>

<style scoped>
.el-form-item__tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>