export const messages = {
  en: {
    initializeCommunityManage: {
      search: {
        title: 'Search Conditions',
        communityId: 'Please enter community ID',
        name: 'Please enter community name'
      },
      list: {
        title: 'Initialize Community Data'
      },
      table: {
        communityId: 'Community ID',
        name: 'Community Name',
        nearbyLandmarks: 'Nearby Landmarks',
        cityCode: 'City Code',
        state: 'Status'
      },
      button: {
        initialize: 'Initialize'
      },
      initializeSuccess: 'Initialize successfully',
      fetchError: 'Failed to fetch community data'
    },
    chooseinitializeCommunity: {
      dialog: {
        title: 'Warning',
        warning: 'Please operate carefully',
        communityInfo: 'This operation will clear all data of community [{name}], community ID is [{id}]. Please confirm again with relevant personnel!',
        passwordPlaceholder: 'Please enter developer password'
      },
      button: {
        cancel: 'Cancel',
        confirm: 'Confirm Initialize'
      },
      validation: {
        passwordRequired: 'Developer password is required'
      }
    }
  },
  zh: {
    initializeCommunityManage: {
      search: {
        title: '查询条件',
        communityId: '请输入小区ID',
        name: '请输入小区名称'
      },
      list: {
        title: '初始化小区数据'
      },
      table: {
        communityId: '小区ID',
        name: '小区名称',
        nearbyLandmarks: '附近地标',
        cityCode: '城市编码',
        state: '状态'
      },
      button: {
        initialize: '格式化'
      },
      initializeSuccess: '初始化成功',
      fetchError: '获取小区数据失败'
    },
    chooseinitializeCommunity: {
      dialog: {
        title: '温馨提示',
        warning: '请慎重操作',
        communityInfo: '此操作将清空所有【{name}】小区数据,小区编码为【{id}】。谨慎期间，请再次跟相关人员核实确认！',
        passwordPlaceholder: '请输入开发者密码'
      },
      button: {
        cancel: '点错了',
        confirm: '确认格式化'
      },
      validation: {
        passwordRequired: '开发者密码不能为空'
      }
    }
  }
}