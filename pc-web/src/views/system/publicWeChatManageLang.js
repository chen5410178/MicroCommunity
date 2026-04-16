export const messages = {
  en: {
    publicWeChatManage: {
      title: 'My Public WeChat',
      addTitle: 'Add Public WeChat',
      editTitle: 'Edit Public WeChat',
      deleteTitle: 'Confirm Operation',
      deleteConfirm: 'Are you sure to delete this public WeChat?',
      table: {
        name: 'Name',
        appSecret: 'App Secret',
        remarks: 'Description',
        operation: 'Operation'
      },
      form: {
        name: 'Name',
        appSecret: 'App Secret',
        remarks: 'Description'
      },
      placeholder: {
        name: 'Required, please fill in the name',
        appId: 'Required, please fill in APPID',
        appSecret: 'Required, please fill in App Secret',
        remarks: 'Optional, please fill in description'
      },
      validate: {
        nameRequired: 'Name is required',
        appIdRequired: 'APPID is required',
        appSecretRequired: 'App Secret is required'
      },
      message: {
        addSuccess: 'Added successfully',
        editSuccess: 'Modified successfully',
        deleteSuccess: 'Deleted successfully',
        fetchError: 'Failed to fetch data'
      }
    }
  },
  zh: {
    publicWeChatManage: {
      title: '我的公众号',
      addTitle: '添加公众号',
      editTitle: '修改公众号',
      deleteTitle: '请确认您的操作',
      deleteConfirm: '确定删除该公众号吗？',
      table: {
        name: '名称',
        appSecret: '应用密钥',
        remarks: '描述',
        operation: '操作'
      },
      form: {
        name: '名称',
        appSecret: '应用密钥',
        remarks: '描述'
      },
      placeholder: {
        name: '必填，请填写名称',
        appId: '必填，请填写APPID',
        appSecret: '必填，请填写应用密钥',
        remarks: '选填，请填写描述信息'
      },
      validate: {
        nameRequired: '名称不能为空',
        appIdRequired: 'APPID不能为空',
        appSecretRequired: '应用密钥不能为空'
      },
      message: {
        addSuccess: '添加成功',
        editSuccess: '修改成功',
        deleteSuccess: '删除成功',
        fetchError: '获取数据失败'
      }
    }
  }
}