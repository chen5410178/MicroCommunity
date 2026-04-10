export const messages = {
  en: {
    storeInfoManage: {
      search: {
        title: 'Search Conditions',
        storeName: 'Please enter store name',
        convenienceType: 'Please select convenience type',
        isShow: 'Please select whether to display'
      },
      list: {
        title: 'Store Information'
      },
      table: {
        storeInfoId: 'Store ID',
        storeName: 'Store Name',
        icon: 'Image URL',
        tel: 'Store Phone',
        site: 'Store Location',
        seq: 'Display Sequence',
        isShow: 'Is Show',
        convenienceType: 'Convenience Type',
        workTime: 'Work Time',
        remark: 'Remark'
      },
      delete: {
        confirmDelete: 'Are you sure to delete store info'
      },
      error: {
        fetchStoreInfoFailed: 'Failed to get store information',
        fetchConvenienceMenusFailed: 'Failed to get convenience menus',
        saveFailed: 'Failed to save store info',
        updateFailed: 'Failed to update store info',
        deleteFailed: 'Failed to delete store info',
        uploadFailed: 'Failed to upload image',
        uploadImageOnly: 'Only image files can be uploaded',
        imageSizeLimit: 'Image size cannot exceed 2MB',
        maxLength50: 'Cannot exceed 50 characters',
        maxLength13: 'Cannot exceed 13 characters',
        maxLength100: 'Cannot exceed 100 characters',
        maxLength200: 'Cannot exceed 200 characters',
        maxLength5000: 'Cannot exceed 5000 characters',
        required: 'This field is required',
        number: 'Please enter a valid number'
      },
      validation: {
        storeNameRequired: 'Store name is required',
        seqRequired: 'Display sequence is required',
        seqNumber: 'Display sequence must be a number',
        telMaxLength: 'Phone number cannot exceed 13 characters',
        siteMaxLength: 'Location cannot exceed 100 characters',
        workTimeMaxLength: 'Work time cannot exceed 200 characters',
        remarkMaxLength: 'Remark cannot exceed 5000 characters'
      }
    }
  },
  zh: {
    storeInfoManage: {
      search: {
        title: '查询条件',
        storeName: '请输入商户名称',
        convenienceType: '请选择便民种类',
        isShow: '请选择是否显示'
      },
      list: {
        title: '商户信息'
      },
      table: {
        storeInfoId: '商户信息ID',
        storeName: '商户名称',
        icon: '图片地址',
        tel: '商户电话',
        site: '商户位置',
        seq: '显示序号',
        isShow: '是否显示',
        convenienceType: '便民类型',
        workTime: '工作时间',
        remark: '备注'
      },
      delete: {
        confirmDelete: '确定删除商户信息'
      },
      error: {
        fetchStoreInfoFailed: '获取商户信息失败',
        fetchConvenienceMenusFailed: '获取便民菜单失败',
        saveFailed: '保存商户信息失败',
        updateFailed: '更新商户信息失败',
        deleteFailed: '删除商户信息失败',
        uploadFailed: '上传图片失败',
        uploadImageOnly: '只能上传图片文件',
        imageSizeLimit: '图片大小不能超过2MB',
        maxLength50: '不能超过50个字符',
        maxLength13: '不能超过13个字符',
        maxLength100: '不能超过100个字符',
        maxLength200: '不能超过200个字符',
        maxLength5000: '不能超过5000个字符',
        required: '此字段为必填项',
        number: '请输入有效的数字'
      },
      validation: {
        storeNameRequired: '商户名称为必填项',
        seqRequired: '显示序号为必填项',
        seqNumber: '显示序号必须为数字',
        telMaxLength: '电话号码不能超过13个字符',
        siteMaxLength: '位置不能超过100个字符',
        workTimeMaxLength: '工作时间不能超过200个字符',
        remarkMaxLength: '备注不能超过5000个字符'
      }
    }
  }
}