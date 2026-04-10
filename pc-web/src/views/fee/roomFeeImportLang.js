export const messages = {
  en: {
    roomFeeImport: {
      search: {
        title: 'Search Conditions',
        importFeeId: 'Please enter import ID',
        feeTypeCd: 'Please select fee type'
      },
      table: {
        title: 'Fee Import',
        importFeeId: 'Import ID',
        feeTypeCd: 'Fee Type',
        createTime: 'Create Time',
        remark: 'Remark'
      },
      button: {
        import: 'Import Fee'
      },
      fetchError: 'Failed to fetch fee import data'
    },
    importRoomFee: {
      title: 'Fee Import',
      form: {
        feeTypeCd: 'Fee Type',
        objType: 'Fee Object',
        file: 'Select File',
        template: 'Download Template'
      },
      placeholder: {
        feeTypeCd: 'Required, please select fee type',
        objType: 'Required, please select fee object'
      },
      options: {
        room: 'Room',
        parking: 'Parking'
      },
      rules: {
        feeTypeCdRequired: 'Fee type is required',
        objTypeRequired: 'Fee object is required',
        fileRequired: 'File is required'
      },
      tips: {
        fileFormat: 'Only .xls, .xlsx files can be uploaded',
        downloadTemplate1: 'Please download',
        downloadTemplate2: 'import template',
        downloadTemplate3: 'to prepare data before uploading'
      },
      button: {
        selectFile: 'Select File'
      },
      message: {
        importSuccess: 'Import successfully',
        importFailed: 'Import failed',
        downloadSuccess: 'Download template successfully',
        downloadFailed: 'Download template failed'
      }
    }
  },
  zh: {
    roomFeeImport: {
      search: {
        title: '查询条件',
        importFeeId: '请输入导入ID',
        feeTypeCd: '请选择费用类型'
      },
      table: {
        title: '费用导入',
        importFeeId: '导入ID',
        feeTypeCd: '费用类型',
        createTime: '创建时间',
        remark: '备注'
      },
      button: {
        import: '费用导入'
      },
      fetchError: '获取费用导入数据失败'
    },
    importRoomFee: {
      title: '费用导入',
      form: {
        feeTypeCd: '费用类型',
        objType: '费用对象',
        file: '选择文件',
        template: '下载模板'
      },
      placeholder: {
        feeTypeCd: '必填，请选择费用类型',
        objType: '必填，请选择费用对象'
      },
      options: {
        room: '房屋',
        parking: '车位车辆'
      },
      rules: {
        feeTypeCdRequired: '费用类型不能为空',
        objTypeRequired: '费用对象不能为空',
        fileRequired: '文件不能为空'
      },
      tips: {
        fileFormat: '只能上传.xls, .xlsx格式文件',
        downloadTemplate1: '请先下载',
        downloadTemplate2: '导入模板',
        downloadTemplate3: '准备数据后上传导入'
      },
      button: {
        selectFile: '选择文件'
      },
      message: {
        importSuccess: '导入成功',
        importFailed: '导入失败',
        downloadSuccess: '下载模板成功',
        downloadFailed: '下载模板失败'
      }
    }
  }
}