export const messages = {
  en: {
    addProxyFee: {
      title: 'Pay by Quantity',
      feeType: 'Fee Type',
      feeTypePlaceholder: 'Required, please select fee type',
      feeItem: 'Fee Item',
      feeItemPlaceholder: 'Required, please select fee item',
      feeItemTip: 'Note: Display items with formula (usage * unit price + additional fee)',
      chargeObject: 'Charge Object',
      chargeObjectPlaceholder: 'Required, please fill in room',
      consumption: 'Consumption',
      consumptionPlaceholder: 'Required, please fill in consumption',
      quantity: 'Quantity',
      amount: 'Amount',
      amountPlaceholder: 'Required, please fill in amount',
      startTime: 'Start Time',
      startTimePlaceholder: 'Required, please fill in start time',
      endTime: 'End Time',
      endTimePlaceholder: 'Required, please fill in end time',
      waterFee: 'Water Fee',
      electricityFee: 'Electricity Fee',
      otherFee: 'Other',
      validate: {
        amountRequired: 'Amount is required',
        amountFormat: 'Invalid amount format, e.g. 3.00',
        consumptionRequired: 'Consumption is required',
        consumptionFormat: 'Invalid consumption format, e.g. 3.00',
        roomRequired: 'Room is required',
        feeRequired: 'Fee is required',
        startTimeRequired: 'Start time is required',
        endTimeRequired: 'End time is required',
        startBeforeEnd: 'Start time must be before end time',
        endAfterStart: 'End time must be after start time',
        invalidConfig: 'Invalid fee item or formula error',
        amountTooSmall: 'Amount is too small'
      },
      successMessage: 'Saved successfully'
    },
    addProxyFeeDemo: {
      openModal: 'Open Pay by Quantity Modal',
      successMessage: 'Saved successfully'
    }
  },
  zh: {
    addProxyFee: {
      title: '按量缴费',
      feeType: '费用类型',
      feeTypePlaceholder: '必填，请选择费用类型',
      feeItem: '收费项目',
      feeItemPlaceholder: '必填，请选择收费项目',
      feeItemTip: '说明：显示公式为(用量*单价+附加费)的费用项',
      chargeObject: '收费对象',
      chargeObjectPlaceholder: '必填，请填写房屋',
      consumption: '用量',
      consumptionPlaceholder: '必填，请填写用量',
      quantity: '个数',
      amount: '金额',
      amountPlaceholder: '必填，请填写金额',
      startTime: '开始时间',
      startTimePlaceholder: '必填，请填写开始时间',
      endTime: '结束时间',
      endTimePlaceholder: '必填，请填写结束时间',
      waterFee: '水费',
      electricityFee: '电费',
      otherFee: '其他',
      validate: {
        amountRequired: '金额不能为空',
        amountFormat: '金额格式错误，如3.00',
        consumptionRequired: '用量不能为空',
        consumptionFormat: '用量格式错误，如3.00',
        roomRequired: '房屋必填',
        feeRequired: '费用必填',
        startTimeRequired: '开始时间不能为空',
        endTimeRequired: '结束时间不能为空',
        startBeforeEnd: '开始时间必须小于结束时间',
        endAfterStart: '结束时间必须大于开始时间',
        invalidConfig: '未选择收费项目或者收费项目公式错误',
        amountTooSmall: '输入金额太小'
      },
      successMessage: '保存成功'
    },
    addProxyFeeDemo: {
      openModal: '打开按量缴费弹窗',
      successMessage: '保存成功'
    }
  },
  'zh-TW': {
    addProxyFee: {
      title: '按量繳費',
      feeType: '費用類型',
      feeTypePlaceholder: '必填，請選擇費用類型',
      feeItem: '收費項目',
      feeItemPlaceholder: '必填，請選擇收費項目',
      feeItemTip: '說明：顯示公式為(用量*單價+附加費)的費用項',
      chargeObject: '收費對象',
      chargeObjectPlaceholder: '必填，請填寫房屋',
      consumption: '用量',
      consumptionPlaceholder: '必填，請填寫用量',
      quantity: '個數',
      amount: '金額',
      amountPlaceholder: '必填，請填寫金額',
      startTime: '開始時間',
      startTimePlaceholder: '必填，請填寫開始時間',
      endTime: '結束時間',
      endTimePlaceholder: '必填，請填寫結束時間',
      waterFee: '水費',
      electricityFee: '電費',
      otherFee: '其他',
      validate: {
        amountRequired: '金額不能為空',
        amountFormat: '金額格式錯誤，如3.00',
        consumptionRequired: '用量不能為空',
        consumptionFormat: '用量格式錯誤，如3.00',
        roomRequired: '房屋必填',
        feeRequired: '費用必填',
        startTimeRequired: '開始時間不能為空',
        endTimeRequired: '結束時間不能為空',
        startBeforeEnd: '開始時間必須小於結束時間',
        endAfterStart: '結束時間必須大於開始時間',
        invalidConfig: '未選擇收費項目或者收費項目公式錯誤',
        amountTooSmall: '輸入金額太小'
      },
      successMessage: '保存成功'
    },
    addProxyFeeDemo: {
      openModal: '打開按量繳費彈窗',
      successMessage: '保存成功'
    }
  }
}