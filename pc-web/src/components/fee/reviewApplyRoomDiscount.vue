<template>
  <el-dialog :title="$t('applyRoomDiscount.review.title')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <template v-if="form.state !== '5'">
        <el-form-item :label="$t('applyRoomDiscount.review.discountType')" prop="discountType">
          <el-select v-model="form.discountType" :placeholder="$t('applyRoomDiscount.review.discountTypePlaceholder')"
            style="width:100%" @change="handleDiscountTypeChange">
            <el-option :label="$t('applyRoomDiscount.discountType.discount')" value="3003" />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('applyRoomDiscount.review.discountName')" prop="discountId">
          <el-select v-model="form.discountId" :placeholder="$t('applyRoomDiscount.review.discountNamePlaceholder')"
            style="width:100%">
            <el-option v-for="item in discounts" :key="item.discountId" :label="item.discountName"
              :value="item.discountId" />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('applyRoomDiscount.review.returnWay')" prop="returnWay">
          <el-select v-model="form.returnWay" :placeholder="$t('applyRoomDiscount.review.returnWayPlaceholder')"
            style="width:100%">
            <el-option :label="$t('applyRoomDiscount.returnWay.discount')" value="1001" />
            <el-option :label="$t('applyRoomDiscount.returnWay.account')" value="1002" />
          </el-select>
        </el-form-item>

        <div v-show="form.returnWay === '1002'">
          <el-form-item :label="$t('applyRoomDiscount.review.paymentRecords')">
            <div class="checkbox-group">
              <el-checkbox v-for="item in fees" :key="item.detailId" v-model="form.selectedFees" :label="item.detailId"
                @change="handleFeeSelection(item)">
                【{{ item.feeName }}】<span class="text-danger">{{ item.receivedAmount }}{{ $t('common.yuan') }}</span> -
                {{ item.createTime }}
              </el-checkbox>
            </div>
          </el-form-item>

          <el-form-item :label="$t('applyRoomDiscount.review.refundAmount')" prop="refundAmount">
            <el-input v-model="form.refundAmount"
              :placeholder="$t('applyRoomDiscount.review.refundAmountPlaceholder')" />
          </el-form-item>
        </div>
      </template>

      <el-form-item :label="$t('applyRoomDiscount.review.state')" prop="state">
        <el-select v-model="form.state" :placeholder="$t('applyRoomDiscount.review.statePlaceholder')"
          style="width:100%">
          <el-option :label="$t('applyRoomDiscount.status.reviewPass')" value="4" />
          <el-option :label="$t('applyRoomDiscount.status.reviewReject')" value="5" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.review.createRemark')">
        <el-input v-model="form.createRemark" type="textarea" :rows="2" disabled />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.review.checkRemark')">
        <el-input v-model="form.checkRemark" type="textarea" :rows="2" disabled />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.review.reviewRemark')" prop="reviewRemark">
        <el-input v-model="form.reviewRemark" type="textarea"
          :placeholder="$t('applyRoomDiscount.review.reviewRemarkPlaceholder')" :rows="3" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.review.images')">
        <div class="image-preview-group">
          <div v-for="(url, index) in form.images" :key="index" class="image-preview-item">
            <img :src="getImageUrl(url)" @click="showImage(url)" />
          </div>
        </div>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('common.save') }}
      </el-button>
    </div>

    <view-image ref="viewImage" />
  </el-dialog>
</template>

<script>
//import { getDict } from '@/api/community/communityApi'
import { queryFeeDiscount, updateReviewApplyRoomDiscount } from '@/api/fee/applyRoomDiscountManageApi'
import ViewImage from '@/components/system/viewImage'
import { getCommunityId } from '@/api/community/communityApi'
import { queryFeeDetail } from '@/api/fee/propertyFeeApi'

export default {
  name: 'ReviewApplyRoomDiscount',
  components: {
    ViewImage
  },
  data() {
    return {
      visible: false,
      form: {
        ardId: '',
        discountId: '',
        discountType: '',
        startTime: '',
        endTime: '',
        createRemark: '',
        checkRemark: '',
        reviewRemark: '',
        refundAmount: '',
        state: '',
        roomId: '',
        discounts: [],
        fees: [],
        selectedFees: [],
        images: [],
        returnWay: '1001',
        feeId: ''
      },
      discounts: [],
      fees: [],
      rules: {
        discountId: [
          { required: true, message: this.$t('applyRoomDiscount.validate.discountIdRequired'), trigger: 'change' }
        ],
        discountType: [
          { required: true, message: this.$t('applyRoomDiscount.validate.discountTypeRequired'), trigger: 'change' }
        ],
        returnWay: [
          { required: true, message: this.$t('applyRoomDiscount.validate.returnWayRequired'), trigger: 'change' }
        ],
        refundAmount: [
          {
            required: true,
            message: this.$t('applyRoomDiscount.validate.refundAmountRequired'),
            trigger: 'blur',
            validator: (rule, value, callback) => {
              if (this.form.returnWay === '1002' && !value) {
                callback(new Error(this.$t('applyRoomDiscount.validate.refundAmountRequired')))
              } else {
                callback()
              }
            }
          },
          {
            pattern: /^\d+(\.\d{1,2})?$/,
            message: this.$t('applyRoomDiscount.validate.refundAmountFormat'),
            trigger: 'blur'
          }
        ],
        startTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.endTimeRequired'), trigger: 'change' }
        ],
        state: [
          { required: true, message: this.$t('applyRoomDiscount.validate.stateRequired'), trigger: 'change' }
        ],
        reviewRemark: [
          { required: true, message: this.$t('applyRoomDiscount.validate.reviewRemarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('applyRoomDiscount.validate.reviewRemarkFormat'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        ...data,
        communityId: getCommunityId(),
        selectedFees: [],
        refundAmount: '',
        state:''
      }
      //多费用申请：列表不再返回单 feeId，取第一个勾选费用用于加载缴费记录
      if (data.feeIds && data.feeIds.length > 0) {
        this.form.feeId = data.feeIds[0]
      }
      this.visible = true
      this.loadDiscounts()
      this.loadFees()
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        ardId: '',
        discountId: '',
        discountType: '',
        startTime: '',
        endTime: '',
        createRemark: '',
        checkRemark: '',
        reviewRemark: '',
        refundAmount: '',
        state: '',
        roomId: '',
        discounts: [],
        fees: [],
        selectedFees: [],
        images: [],
        returnWay: '1001',
        feeId: ''
      }
    },
    async loadDiscounts() {
      try {
        const { data } = await queryFeeDiscount({
          page: 1,
          row: 100,
          communityId: this.form.communityId,
          discountType: this.form.discountType || '3003'
        })
        this.discounts = data
      } catch (error) {
        console.error('加载折扣信息失败:', error)
      }
    },
    async loadFees() {
      try {
        const { data: feeDetails } = await queryFeeDetail({
          page: 1,
          row: 50,
          communityId: this.form.communityId,
          feeId: this.form.feeId,
          state: '1400'
        })
        this.fees = feeDetails
      } catch (error) {
        console.error('加载缴费记录失败:', error)
      }
    },
    handleDiscountTypeChange() {
      this.form.discountId = ''
      this.loadDiscounts()
    },
    handleFeeSelection(item) {
      console.log(item)
      this.calculateRefundAmount()
    },
    calculateRefundAmount() {
      if (this.form.returnWay !== '1002') return

      let total = 0
      this.fees.forEach(item => {
        if (this.form.selectedFees.includes(item.detailId)) {
          total += parseFloat(item.receivedAmount) || 0
        }
      })
      this.form.refundAmount = total.toFixed(2)
    },
    getImageUrl(url) {
      if (url.startsWith('http') || url.startsWith('https') || url.startsWith('data:')) {
        return url
      }
      return `/callComponent/download/getFile/file?fileId=${url}&communityId=-1&time=${new Date().getTime()}`
    },
    showImage(url) {
      setTimeout(() => {
        this.$refs.viewImage.open(this.getImageUrl(url))
      }, 100)
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.updateReview()
        }
      })
    },
    async updateReview() {
      try {
        await updateReviewApplyRoomDiscount(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('applyRoomDiscount.message.reviewFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 22px;
}

.checkbox-group {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;

  .el-checkbox {
    display: block;
    margin: 5px 0;
  }
}

.text-danger {
  color: #f56c6c;
}

.image-preview-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;

  .image-preview-item {
    width: 60px;
    height: 60px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}
</style>