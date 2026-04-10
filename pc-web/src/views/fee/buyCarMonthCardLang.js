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
  }
}