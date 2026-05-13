<template>
  <el-dialog 
    :title="$t('payFeeOrderResult.title')" 
    :visible.sync="visible" 
    width="50%" 
    @close="handleClose"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="true"
    :modal="true"
    :append-to-body="true">
    <p>{{ $t('payFeeOrderResult.success') }}</p>
    <p v-if="!payFeeOrderResultInfo.receiptIds">{{ $t('payFeeOrderResult.receiptTip') }}</p>
    <div slot="footer" class="dialog-footer">
      <el-button @click="back()">{{ $t('payFeeOrderResult.back') }}</el-button>
      <el-button v-if="payFeeOrderResultInfo.receiptIds" type="primary" @click="_printSmallAndBack">{{ $t('payFeeOrderResult.printSmall') }}</el-button>
      <el-button v-if="payFeeOrderResultInfo.receiptIds" type="primary" @click="_printAndBack('ON')">{{ $t('payFeeOrderResult.mergePrint') }}</el-button>
      <el-button v-if="payFeeOrderResultInfo.receiptIds" type="primary" @click="_printAndBack('OFF')">{{ $t('payFeeOrderResult.printReceipt') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFeeReceipt,listFeePrintPages } from '@/api/fee/payFeeOrderApi'

export default {
  name: 'PayFeeOrderResult',
  data() {
    return {
      visible: false,
      communityId: '',
      payFeeOrderResultInfo: {
        printUrl: '/#/pages/property/printPayFee',
        receiptIds: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listFeePrintPages()
  },
  methods: {
    open(_data) {
      console.log('PayFeeOrderResult: Opening dialog, visible:', this.visible)
      this.visible = true
      // 确保在下一个tick中对话框能正确显示
     this._queryPayFeeReceipt(_data)
    },

    handleClose() {
      this.visible = false
    
    },
    async _queryPayFeeReceipt(data) {
      try {
        const params = {
          receiptCode: data.receiptCode,
          communityId: this.communityId,
          page: 1,
          row: 100
        }
        const response = await queryFeeReceipt(params)
        if (response && response.data && response.data.length > 0) {
          let receiptIds = ''
          response.data.forEach(item => {
            receiptIds += (item.receiptId + ',')
          })
          this.payFeeOrderResultInfo.receiptIds = receiptIds
        }
      } catch (error) {
        console.error('请求失败处理', error)
        this.$message.error('请求失败')
      }
    },
    _printAndBack(merge) {
      window.open(this.payFeeOrderResultInfo.printUrl + '?receiptIds=' + this.payFeeOrderResultInfo.receiptIds + '&merge=' + merge)
    },
    _printSmallAndBack() {
      window.open('/#/pages/property/printSmallPayFee?receiptIds=' + this.payFeeOrderResultInfo.receiptIds)
    },
    async _listFeePrintPages() {
      try {
        const params = {
          page: 1,
          row: 1,
          state: 'T',
          communityId: this.communityId
        }
        // 假设有 listFeePrintPage API
        const { data } = await listFeePrintPages(params)
        if (data && data.length > 0) {
          this.payFeeOrderResultInfo.printUrl = data[0].url
        }
      } catch (error) {
        console.error('请求失败处理')
      }
    },
    back() {
      this.handleClose()
      this.$emit('back')
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}


</style>