export const messages = {
  en: {
    adminAuthOwner: {
      search: {
        title: 'Search Conditions',
        state: 'Please select status',
        appUserName: 'Please enter owner name',
        idCard: 'Please enter ID card',
        link: 'Please enter phone number'
      },
      list: {
        title: 'Owner Authentication'
      },
      table: {
        communityName: 'Community Name',
        appUserName: 'Owner',
        link: 'Phone',
        roomName: 'Room',
        ownerTypeCdName: 'Person Type',
        idCard: 'ID Card',
        stateName: 'Status',
        remark: 'Audit Remark',
        createTime: 'Apply Time',
        appTypeName: 'App Type'
      },
      status: {
        all: 'All',
        pending: 'Pending',
        success: 'Success',
        failed: 'Failed'
      },
      community: {
        all: 'All Communities',
        fetchError: 'Failed to load communities'
      },
      fetchError: 'Failed to load owner authentication data'
    }
  },
  zh: {
    adminAuthOwner: {
      search: {
        title: '查询条件',
        state: '请选择状态',
        appUserName: '请输入关联业主名称',
        idCard: '请输入关联业主身份证',
        link: '请输入关联业主手机号'
      },
      list: {
        title: '业主认证'
      },
      table: {
        communityName: '小区名称',
        appUserName: '业主',
        link: '手机号',
        roomName: '房屋',
        ownerTypeCdName: '人员类型',
        idCard: '身份证',
        stateName: '状态',
        remark: '审核说明',
        createTime: '申请时间',
        appTypeName: '手机端类型'
      },
      status: {
        all: '全部',
        pending: '待审核',
        success: '审核成功',
        failed: '审核失败'
      },
      community: {
        all: '全部小区',
        fetchError: '加载小区列表失败'
      },
      fetchError: '加载业主认证数据失败'
    }
  }
}