<template>
  <el-card class="buy-car-month-card-container">
    <div slot="header" class="flex justify-between">
      <div>{{ $t('buyCarMonthCard.title') }}</div>
    </div>

    <el-form ref="form" :model="buyCarMonthCardInfo" label-width="120px" class="text-left">
      <el-form-item :label="$t('buyCarMonthCard.carNum')" prop="carNum"
        :rules="[{ required: true, message: $t('buyCarMonthCard.rules.carNumRequired'), trigger: 'blur' }]">
        <el-input v-model="buyCarMonthCardInfo.carNum" :placeholder="$t('buyCarMonthCard.placeholders.carNum')"
          @blur="_queryOwnerCar" clearable>
        </el-input>
      </el-form-item>

      <el-form-item :label="$t('buyCarMonthCard.card')" prop="cardId"
        :rules="[{ required: true, message: $t('buyCarMonthCard.rules.cardRequired'), trigger: 'change' }]">
        <el-select v-model="buyCarMonthCardInfo.cardId" :placeholder="$t('buyCarMonthCard.placeholders.card')"
          @change="_changeCard" style="width:100%">
          <el-option v-for="(item, index) in buyCarMonthCardInfo.cards" :key="index"
            :label="`${item.cardName}(${item.cardMonth}${$t('common.month')})`" :value="item.cardId">
          </el-option>
        </el-select>
        <div class="tip">{{ $t('buyCarMonthCard.tip') }}</div>
      </el-form-item>

      <el-form-item :label="$t('buyCarMonthCard.paymentMethod')" prop="primeRate"
        :rules="[{ required: true, message: $t('buyCarMonthCard.rules.paymentMethodRequired'), trigger: 'change' }]">
        <el-select v-model="buyCarMonthCardInfo.primeRate" :placeholder="$t('buyCarMonthCard.placeholders.paymentMethod')"
          style="width:100%">
          <el-option v-for="(item, index) in filteredPrimeRates" :key="index" :label="item.name" :value="item.statusCd">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('buyCarMonthCard.receivableAmount')">
        <el-input v-model="buyCarMonthCardInfo.receivableAmount"
          :placeholder="$t('buyCarMonthCard.placeholders.receivableAmount')" disabled>
        </el-input>
      </el-form-item>

      <el-form-item :label="$t('buyCarMonthCard.receivedAmount')" prop="receivedAmount"
        :rules="[{ required: true, message: $t('buyCarMonthCard.rules.receivedAmountRequired'), trigger: 'blur' }]">
        <el-input v-model="buyCarMonthCardInfo.receivedAmount" type="number"
          :placeholder="$t('buyCarMonthCard.placeholders.receivedAmount')">
        </el-input>
      </el-form-item>

      <el-form-item :label="$t('buyCarMonthCard.remark')">
        <el-input v-model="buyCarMonthCardInfo.remark" type="textarea"
          :placeholder="$t('buyCarMonthCard.placeholders.remark')">
        </el-input>
      </el-form-item>

      <el-form-item class="text-right">
        <el-button type="primary" @click="saveCarMonthOrderInfo">
          <i class="el-icon-check"></i>{{ $t('common.submit') }}
        </el-button>
        <el-button type="warning" @click="goBack" style="margin-left:20px;">
          {{ $t('common.back') }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { buyCarMonthOrder, queryOwnerCars, getOpenApi } from '@/api/fee/buyCarMonthCardApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'BuyCarMonthCardList',
  data() {
    return {
      buyCarMonthCardInfo: {
        orderId: '',
        cardId: '',
        carNum: '',
        carId: '',
        primeRate: '',
        receivableAmount: '',
        receivedAmount: '',
        remark: '',
        cards: [],
        primeRates: [],
        areaNum: ''
      },
      communityId: ''
    }
  },
  computed: {
    filteredPrimeRates() {
      return this.buyCarMonthCardInfo.primeRates.filter(
        item => item.statusCd !== '5' && item.statusCd !== '6' && item.statusCd !== '8'
      )
    }
  },
  mounted() {
    this.communityId = getCommunityId()

    // 从路由参数获取车牌号
    if (this.$route.query.carNum) {
      this.buyCarMonthCardInfo.carNum = this.$route.query.carNum
      this._queryOwnerCar()
    }

    // 获取支付方式字典
    this.getPrimeRateDict()
  },
  methods: {
    async getPrimeRateDict() {
      try {
        const data = await getDict('pay_fee_detail', 'prime_rate')
        this.buyCarMonthCardInfo.primeRates = data
      } catch (error) {
        console.error('获取支付方式字典失败:', error)
        this.$message.error(this.$t('buyCarMonthCard.errors.getDictFailed'))
      }
    },

    async _queryOwnerCar() {
      if (!this.buyCarMonthCardInfo.carNum) return

      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          carNum: this.buyCarMonthCardInfo.carNum,
          carTypeCd: '1001'
        }

        const res = await queryOwnerCars(params)
        if (res.total < 1) {
          this.$message.warning(this.$t('buyCarMonthCard.warnings.carNotExist'))
          return
        }

        const carData = res.data[0]
        if (!carData.paId) {
          this.$message.warning(this.$t('buyCarMonthCard.warnings.carNoParkingSpace'))
          return
        }

        this.buyCarMonthCardInfo.carId = carData.carId
        this.buyCarMonthCardInfo.areaNum = carData.areaNum
        this._listBuyMonthCards()
      } catch (error) {
        console.error('查询车主车辆失败:', error)
        this.$message.error(this.$t('buyCarMonthCard.errors.queryCarFailed'))
      }
    },

    async _listBuyMonthCards() {
      if (!this.buyCarMonthCardInfo.areaNum) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          paNum: this.buyCarMonthCardInfo.areaNum,
          iotApiCode: 'queryCarMonthCardBmoImpl'
        }

        const res = await getOpenApi(params)
        this.buyCarMonthCardInfo.cards = res.data || []
      } catch (error) {
        console.error('获取月卡列表失败:', error)
        this.$message.error(this.$t('buyCarMonthCard.errors.getCardsFailed'))
      }
    },

    _changeCard() {
      const cardId = this.buyCarMonthCardInfo.cardId
      const selectedCard = this.buyCarMonthCardInfo.cards.find(item => item.cardId === cardId)

      if (selectedCard) {
        this.buyCarMonthCardInfo.receivableAmount = selectedCard.cardPrice
        this.buyCarMonthCardInfo.receivedAmount = selectedCard.cardPrice
      }
    },

    async saveCarMonthOrderInfo() {
      try {
        // 表单验证
        await this.$refs.form.validate()

        const params = {
          ...this.buyCarMonthCardInfo,
          communityId: this.communityId
        }

        const res = await buyCarMonthOrder(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg || this.$t('buyCarMonthCard.errors.saveFailed'))
        }
      } catch (error) {
        if (error instanceof Error) {
          console.error('保存月卡订单失败:', error)
          this.$message.error(error.message)
        }
        // 验证失败不显示错误
      }
    },

    clearBuyCarMonthCardInfo() {
      this.$refs.form.resetFields()
      this.buyCarMonthCardInfo.cards = []
      this.buyCarMonthCardInfo.areaNum = ''
      this.getPrimeRateDict()
    },

    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.buy-car-month-card-container {
  margin: 20px;
}

.tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>