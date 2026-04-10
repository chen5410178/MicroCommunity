export const messages = {
  en: {
    cache: {
      flush:'flush',

      list: {
        title: 'cache List'
      },
      dialog: {
        addTitle: 'Add Cache',
        editTitle: 'Edit Cache',
        delTitle: 'Delete Cache',
        cancel: 'Cancel',
        confirm: 'Confirm'
      },
      table: {
        cacheCode: 'cacheCode',
        serviceCode: 'serviceCode',
        name: 'name',
        seq: 'seq',
        group: 'group',
        createTime: 'createTime',
        url: 'url',
      },
      add: {
        success: 'Add Cache success'
      },
      edit: {
        success: 'Edit Cache success'
      },
      delete: {
        success: 'Delete Cache success',
        confirm: 'Are you sure to delete this cache?'
      },
      fetchError: 'Failed to fetch cache list'
    }
  },
  zh: {
    cache: {
      flush:'刷新',
      list: {
        title: '刷新缓存'
      },
      dialog: {
        addTitle: '添加刷新缓存',
        editTitle: '编辑刷新缓存',
        delTitle: '删除刷新缓存',
        cancel: '取消',
        confirm: '确定'
      },
      table: {
        cacheCode: '缓存编码',
        serviceCode: '服务编码',
        name: '名称',
        seq: '顺序',
        group: '组',
        createTime: '创建时间',
        url: '地址',
      },
      validate: {
        cacheCode: '请输入缓存编码 开始于1001',
        serviceCode: '请输入调用服务编码 对应 c_service',
        name: '请输入前台显示名称',
        seq: '请输入列顺序',
        group: '请输入组，缓存属于哪个组',
        createTime: '请输入创建时间',
        url: '请输入地址',
      },
      add: {
        success: '添加刷新缓存成功'
      },
      edit: {
        success: '编辑刷新缓存成功'
      },
      delete: {
        success: '删除刷新缓存成功',
        confirm: '确认删除该刷新缓存吗？'
      },
      fetchError: '获取刷新缓存列表失败'
    }
  }
}