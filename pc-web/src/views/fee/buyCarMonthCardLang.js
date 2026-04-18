export const messages = {
  en: {
    buyCarMonthCard: {
      title: 'Buy Monthly Card',
      carNum: 'License Plate',
      card: 'Monthly Card',
      paymentMethod: 'Payment Method',
      receivableAmount: 'Receivable Amount',
      receivedAmount: 'Received Amount',
      remark: 'Remark',
      tip: 'Monthly cards need to be added in the IoT system and must be added to the parking lot where the vehicle is located',
      placeholders: {
        carNum: 'Required, please enter license plate number',
        card: 'Required, please select monthly card',
        paymentMethod: 'Required, please select payment method',
        receivableAmount: 'Required, receivable amount',
        receivedAmount: 'Required, received amount',
        remark: 'Optional, please enter remark'
      },
      rules: {
        carNumRequired: 'License plate number is required',
        cardRequired: 'Monthly card is required',
        paymentMethodRequired: 'Payment method is required',
        receivedAmountRequired: 'Received amount is required'
      },
      warnings: {
        carNotExist: 'Monthly rental car does not exist, please add it first',
        carNoParkingSpace: 'Monthly rental car is not bound to a parking space'
      },
      errors: {
        getDictFailed: 'Failed to get payment methods',
        queryCarFailed: 'Failed to query vehicle information',
        getCardsFailed: 'Failed to get monthly card list',
        saveFailed: 'Failed to save monthly card order'
      },
      success: {
        saveSuccess: 'Monthly card purchased successfully'
      }
    }
  },
  zh: {
    buyCarMonthCard: {
      title: '购买月卡',
      carNum: '车牌号',
      card: '月卡',
      paymentMethod: '支付方式',
      receivableAmount: '应收金额',
      receivedAmount: '实收金额',
      remark: '备注',
      tip: '月卡请到物联网系统添加，需要添加到车辆所在的停车场',
      placeholders: {
        carNum: '必填，请填写车牌号',
        card: '必填，请选择月卡',
        paymentMethod: '必填，请选择支付方式',
        receivableAmount: '必填，应收金额',
        receivedAmount: '必填，实收金额',
        remark: '选填，请填写备注'
      },
      rules: {
        carNumRequired: '车牌号不能为空',
        cardRequired: '月卡不能为空',
        paymentMethodRequired: '支付方式不能为空',
        receivedAmountRequired: '实收金额不能为空'
      },
      warnings: {
        carNotExist: '月租车不存在,请先添加为月租车',
        carNoParkingSpace: '月租车没有绑定车位'
      },
      errors: {
        getDictFailed: '获取支付方式失败',
        queryCarFailed: '查询车辆信息失败',
        getCardsFailed: '获取月卡列表失败',
        saveFailed: '保存月卡订单失败'
      },
success: {
        saveSuccess: '月卡购买成功'
      }
    }
  },
  'zh-TW': {
    buyCarMonthCard: {
      title: '購買月卡',
      carNum: '車牌號',
      card: '月卡',
      paymentMethod: '支付方式',
      receivableAmount: '應收金額',
      receivedAmount: '實收金額',
      remark: '備註',
      tip: '月卡請到物聯網系統添加，需要添加到車輛所在的停車場',
      placeholders: {
        carNum: '必填，請填寫車牌號',
        card: '必填，請選擇月卡',
        paymentMethod: '必填，請選擇支付方式',
        receivableAmount: '必填，應收金額',
        receivedAmount: '必填，實收金額',
        remark: '選填，請填寫備註'
      },
      rules: {
        carNumRequired: '車牌號不能為空',
        cardRequired: '月卡不能為空',
        paymentMethodRequired: '支付方式不能為空',
        receivedAmountRequired: '實收金額不能為空'
      },
      warnings: {
        carNotExist: '月租車不存在,請先添加為月租車',
        carNoParkingSpace: '月租車沒有綁定車位'
      },
      errors: {
        getDictFailed: '獲取支付方式失敗',
        queryCarFailed: '查詢車輛信息失敗',
        getCardsFailed: '獲取月卡列表失敗',
        saveFailed: '保存月卡訂單失敗'
      },
      success: {
        saveSuccess: '月卡購買成功'
      }
    }
  }
}