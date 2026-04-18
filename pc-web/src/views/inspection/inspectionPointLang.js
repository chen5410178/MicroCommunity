export const messages = {
  en: {
    inspectionPoint: {
      title: 'Inspection Point',
      placeholder: {
        inspectionName: 'Please enter inspection point name'
      },
      inspectionPoint: 'Inspection Point',
      inspectionType: 'Inspection Type',
      inspectionLocation: 'Inspection Location',
      inspectionItem: 'Inspection Item',
      qrCode: 'QR Code',
      tabs: {
        pointTaskDetail: 'Inspection Details',
        pointRoute: 'Inspection Route',
        pointPlan: 'Inspection Plan',
        pointTask: 'Inspection Task'
      }
    },
    pointTaskDetail: {
      placeholder: {
        planUserName: 'Please enter inspector',
        inspectionStartTime: 'Actual inspection start time',
        inspectionEndTime: 'Actual inspection end time'
      },
      table: {
        taskDetailId: 'Task Detail ID',
        inspectionName: 'Inspection Point Name',
        inspectionPlanName: 'Inspection Plan Name',
        routeName: 'Inspection Route Name',
        planTime: 'Plan Time',
        pointTime: 'Point Time',
        inspectionTime: 'Actual Inspection Time',
        inspectionState: 'Inspection State',
        planUserName: 'Planned Inspector',
        actUserName: 'Actual Inspector',
        signTypeName: 'Sign-in Method',
        taskStateName: 'Task Status',
        stateName: 'Point Status',
        description: 'Inspection Result',
        photos: 'Inspection Photos',
        createTime: 'Create Time'
      },
      fetchError: 'Failed to fetch inspection details'
    },
    pointRoute: {
      fetchError: 'Failed to fetch inspection routes'
    },
    pointPlan: {
      table: {
        planName: 'Plan Name',
        routeName: 'Route Name',
        planPeriod: 'Plan Period',
        signType: 'Sign-in Method',
        dateRange: 'Date Range',
        timeRange: 'Time Range',
        beforeTime: 'Advance Time (minutes)',
        creator: 'Creator',
        createTime: 'Create Time',
        staffs: 'Inspectors',
        state: 'Status'
      },
      fetchError: 'Failed to fetch inspection plans'
    },
    pointTask: {
      fetchError: 'Failed to fetch inspection tasks'
    },
    addInspectionPoint: {
      title: 'Add Inspection Point',
      placeholder: {
        inspectionName: 'Required, enter inspection point name',
        pointObjType: 'Required, select inspection type',
        pointObjName: 'Required, enter inspection location',
        itemId: 'Required, select inspection item',
        nfcCode: 'Optional, enter NFC code',
        lng: 'Optional, select longitude',
        lat: 'Optional, select latitude',
        remark: 'Optional, enter remarks'
      },
      inspectionName: 'Inspection Point Name',
      pointObjType: 'Inspection Type',
      pointObjName: 'Location',
      machine: 'Equipment',
      itemId: 'Inspection Item',
      nfcCode: 'NFC Code',
      lng: 'Longitude',
      lat: 'Latitude',
      remark: 'Remarks',
      tip: {
        addMachine: 'Please add equipment in the equipment ledger'
      },
      rules: {
        inspectionName: 'Inspection point name is required',
        inspectionNameMax: 'Inspection point name cannot exceed 100 characters',
        pointObjType: 'Inspection type is required',
        pointObjName: 'Location is required',
        pointObjId: 'Equipment is required',
        itemId: 'Inspection item is required',
        remarkMax: 'Remarks cannot exceed 200 characters',
        machineRequired: 'Please select equipment',
        locationRequired: 'Please enter location'
      },
      saveSuccess: 'Inspection point added successfully',
      saveFailed: 'Failed to add inspection point'
    },
    editInspectionPoint: {
      title: 'Edit Inspection Point',
      placeholder: {
        inspectionName: 'Required, enter inspection point name',
        pointObjType: 'Required, select inspection type',
        pointObjName: 'Required, enter inspection location',
        itemId: 'Required, select inspection item',
        nfcCode: 'Optional, enter NFC code',
        lng: 'Optional, select longitude',
        lat: 'Optional, select latitude',
        remark: 'Optional, enter remarks'
      },
      inspectionName: 'Inspection Point Name',
      pointObjType: 'Inspection Type',
      pointObjName: 'Location',
      machine: 'Equipment',
      itemId: 'Inspection Item',
      nfcCode: 'NFC Code',
      lng: 'Longitude',
      lat: 'Latitude',
      remark: 'Remarks',
      rules: {
        inspectionName: 'Inspection point name is required',
        inspectionNameMax: 'Inspection point name cannot exceed 100 characters',
        pointObjType: 'Inspection type is required',
        pointObjName: 'Location is required',
        pointObjId: 'Equipment is required',
        itemId: 'Inspection item is required',
        remarkMax: 'Remarks cannot exceed 200 characters',
        machineRequired: 'Please select equipment',
        locationRequired: 'Please enter location'
      },
      updateSuccess: 'Inspection point updated successfully',
      updateFailed: 'Failed to update inspection point'
    },
    deleteInspectionPoint: {
      title: 'Confirm Operation',
      confirmText: 'Are you sure you want to delete this inspection point?',
      deleteSuccess: 'Inspection point deleted successfully',
      deleteFailed: 'Failed to delete inspection point'
    },
    inspectionPointQrCode: {
      title: 'Inspection QR Code',
      tip: 'Please take a screenshot and post it at the inspection point'
    },
    machineSelect2: {
      placeholder: 'Please select equipment',
      fetchError: 'Failed to fetch equipment list'
    }
  },
  zh: {
    inspectionPoint: {
      title: '巡检点',
      placeholder: {
        inspectionName: '请输入巡检点名称'
      },
      inspectionPoint: '巡检点',
      inspectionType: '巡检类型',
      inspectionLocation: '巡检位置',
      inspectionItem: '巡检项目',
      qrCode: '二维码',
      tabs: {
        pointTaskDetail: '巡检明细',
        pointRoute: '巡检路线',
        pointPlan: '巡检计划',
        pointTask: '巡检任务'
      }
    },
    pointTaskDetail: {
      placeholder: {
        planUserName: '请输入巡检人',
        inspectionStartTime: '实际巡检开始时间',
        inspectionEndTime: '实际巡检结束时间'
      },
      table: {
        taskDetailId: '任务详情ID',
        inspectionName: '巡检点名称',
        inspectionPlanName: '巡检计划名称',
        routeName: '巡检路线名称',
        planTime: '计划时间',
        pointTime: '点位时间',
        inspectionTime: '实际巡检时间',
        inspectionState: '实际签到状态',
        planUserName: '计划巡检人',
        actUserName: '实际巡检人',
        signTypeName: '巡检方式',
        taskStateName: '任务状态',
        stateName: '巡检点状态',
        description: '巡检情况',
        photos: '巡检照片',
        createTime: '创建时间'
      },
      fetchError: '获取巡检明细失败'
    },
    pointRoute: {
      fetchError: '获取巡检路线失败'
    },
    pointPlan: {
      table: {
        planName: '计划名称',
        routeName: '计划路线',
        planPeriod: '计划周期',
        signType: '签到方式',
        dateRange: '日期范围',
        timeRange: '时间范围',
        beforeTime: '任务提前(分钟)',
        creator: '制定人',
        createTime: '制定时间',
        staffs: '巡检人',
        state: '状态'
      },
      fetchError: '获取巡检计划失败'
    },
    pointTask: {
      fetchError: '获取巡检任务失败'
    },
    addInspectionPoint: {
      title: '添加巡检点',
      placeholder: {
        inspectionName: '必填，请填写巡检点名称',
        pointObjType: '必填，请选择巡检类型',
        pointObjName: '必填，请填写巡检位置',
        itemId: '必填，请选择巡检项目',
        nfcCode: '选填，请填写nfc编码',
        lng: '选填，请选择经度',
        lat: '选填，请选择纬度',
        remark: '选填，请填写备注'
      },
      inspectionName: '巡检点名称',
      pointObjType: '巡检类型',
      pointObjName: '位置',
      machine: '巡检设备',
      itemId: '巡检项目',
      nfcCode: 'nfc编码',
      lng: '经度',
      lat: '纬度',
      remark: '备注',
      tip: {
        addMachine: '请到设备台账中添加设备'
      },
      rules: {
        inspectionName: '巡检点名称不能为空',
        inspectionNameMax: '巡检点名称不能超过100位',
        pointObjType: '巡检类型不能为空',
        pointObjName: '巡检位置不能为空',
        pointObjId: '设备不能为空',
        itemId: '巡检项目不能为空',
        remarkMax: '备注信息不能超过200位',
        machineRequired: '请选择巡检设备',
        locationRequired: '请填写巡检位置'
      },
      saveSuccess: '巡检点添加成功',
      saveFailed: '巡检点添加失败'
    },
    editInspectionPoint: {
      title: '修改巡检点',
      placeholder: {
        inspectionName: '必填，请填写巡检点名称',
        pointObjType: '必填，请选择巡检类型',
        pointObjName: '必填，请填写巡检位置',
        itemId: '必填，请选择巡检项目',
        nfcCode: '选填，请填写nfc编码',
        lng: '选填，请选择经度',
        lat: '选填，请选择纬度',
        remark: '选填，请填写备注'
      },
      inspectionName: '巡检点名称',
      pointObjType: '巡检类型',
      pointObjName: '位置',
      machine: '巡检设备',
      itemId: '巡检项目',
      nfcCode: 'nfc编码',
      lng: '经度',
      lat: '纬度',
      remark: '备注',
      rules: {
        inspectionName: '巡检点名称不能为空',
        inspectionNameMax: '巡检点名称不能超过100位',
        pointObjType: '巡检类型不能为空',
        pointObjName: '巡检位置不能为空',
        pointObjId: '设备不能为空',
        itemId: '巡检项目不能为空',
        remarkMax: '备注信息不能超过200位',
        machineRequired: '请选择巡检设备',
        locationRequired: '请填写巡检位置'
      },
      updateSuccess: '巡检点修改成功',
      updateFailed: '巡检点修改失败'
    },
    deleteInspectionPoint: {
      title: '请确认您的操作',
      confirmText: '确定删除巡检点？',
      deleteSuccess: '巡检点删除成功',
      deleteFailed: '巡检点删除失败'
    },
    inspectionPointQrCode: {
      title: '巡检二维码',
      tip: '请截图贴在巡检点的地方'
    },
    machineSelect2: {
      placeholder: '请选择设备',
fetchError: '获取设备列表失败'
    }
  },
  'zh-TW': {
    inspectionPoint: {
      title: '巡檢點',
      placeholder: {
        inspectionName: '請輸入巡檢點名稱'
      },
      inspectionPoint: '巡檢點',
      inspectionType: '巡檢類型',
      inspectionLocation: '巡檢位置',
      inspectionItem: '巡檢項目',
      qrCode: '二維碼',
      tabs: {
        pointTaskDetail: '巡檢明細',
        pointRoute: '巡檢路線',
        pointPlan: '巡檢計劃',
        pointTask: '巡檢任務'
      }
    },
    pointTaskDetail: {
      placeholder: {
        planUserName: '請輸入巡檢人',
        inspectionStartTime: '實際巡檢開始時間',
        inspectionEndTime: '實際巡檢結束時間'
      },
      table: {
        taskDetailId: '任務詳情ID',
        inspectionName: '巡檢點名稱',
        inspectionPlanName: '巡檢計劃名稱',
        routeName: '巡檢路線名稱',
        planTime: '計劃時間',
        pointTime: '點位時間',
        inspectionTime: '實際巡檢時間',
        inspectionState: '實際簽到狀態',
        planUserName: '計劃巡檢人',
        actUserName: '實際巡檢人',
        signTypeName: '巡檢方式',
        taskStateName: '任務狀態',
        stateName: '巡檢點狀態',
        description: '巡檢情況',
        photos: '巡檢照片',
        createTime: '創建時間'
      },
      fetchError: '獲取巡檢明細失敗'
    },
    pointRoute: {
      fetchError: '獲取巡檢路線失敗'
    },
    pointPlan: {
      table: {
        planName: '計劃名稱',
        routeName: '計劃路線',
        planPeriod: '計劃週期',
        signType: '簽到方式',
        dateRange: '日期範圍',
        timeRange: '時間範圍',
        beforeTime: '任務提前(分鐘)',
        creator: '制定人',
        createTime: '制定時間',
        staffs: '巡檢人',
        state: '狀態'
      },
      fetchError: '獲取巡檢計劃失敗'
    },
    pointTask: {
      fetchError: '獲取巡檢任務失敗'
    },
    addInspectionPoint: {
      title: '添加巡檢點',
      placeholder: {
        inspectionName: '必填，請填寫巡檢點名稱',
        pointObjType: '必填，請選擇巡檢類型',
        pointObjName: '必填，請填寫巡檢位置',
        itemId: '必填，請選擇巡檢項目',
        nfcCode: '選填，請填寫nfc編碼',
        lng: '選填，請選擇經度',
        lat: '選填，請選擇緯度',
        remark: '選填，請填寫備註'
      },
      inspectionName: '巡檢點名稱',
      pointObjType: '巡檢類型',
      pointObjName: '位置',
      machine: '巡檢設備',
      itemId: '巡檢項目',
      nfcCode: 'nfc編碼',
      lng: '經度',
      lat: '緯度',
      remark: '備註',
      tip: {
        addMachine: '請到設備台帳中添加設備'
      },
      rules: {
        inspectionName: '巡檢點名稱不能為空',
        inspectionNameMax: '巡檢點名稱不能超過100位',
        pointObjType: '巡檢類型不能為空',
        pointObjName: '巡檢位置不能為空',
        pointObjId: '設備不能為空',
        itemId: '巡檢項目不能為空',
        remarkMax: '備註信息不能超過200位',
        machineRequired: '請選擇巡檢設備',
        locationRequired: '請填寫巡檢位置'
      },
      saveSuccess: '巡檢點添加成功',
      saveFailed: '巡檢點添加失敗'
    },
    editInspectionPoint: {
      title: '修改巡檢點',
      placeholder: {
        inspectionName: '必填，請填寫巡檢點名稱',
        pointObjType: '必填，請選擇巡檢類型',
        pointObjName: '必填，請填寫巡檢位置',
        itemId: '必填，請選擇巡檢項目',
        nfcCode: '選填，請填寫nfc編碼',
        lng: '選填，請選擇經度',
        lat: '選填，請選擇緯度',
        remark: '選填，請填寫備註'
      },
      inspectionName: '巡檢點名稱',
      pointObjType: '巡檢類型',
      pointObjName: '位置',
      machine: '巡檢設備',
      itemId: '巡檢項目',
      nfcCode: 'nfc編碼',
      lng: '經度',
      lat: '緯度',
      remark: '備註',
      rules: {
        inspectionName: '巡檢點名稱不能為空',
        inspectionNameMax: '巡檢點名稱不能超過100位',
        pointObjType: '巡檢類型不能為空',
        pointObjName: '巡檢位置不能為空',
        pointObjId: '設備不能為空',
        itemId: '巡檢項目不能為空',
        remarkMax: '備註信息不能超過200位',
        machineRequired: '請選擇巡檢設備',
        locationRequired: '請填寫巡檢位置'
      },
      updateSuccess: '巡檢點修改成功',
      updateFailed: '巡檢點修改失敗'
    },
    deleteInspectionPoint: {
      title: '請確認您的操作',
      confirmText: '確定刪除巡檢點？',
      deleteSuccess: '巡檢點刪除成功',
      deleteFailed: '巡檢點刪除失敗'
    },
    inspectionPointQrCode: {
      title: '巡檢二維碼',
      tip: '請截圖貼在巡檢點的地方'
    },
    machineSelect2: {
      placeholder: '請選擇設備',
      fetchError: '獲取設備列表失敗'
    }
  }
}