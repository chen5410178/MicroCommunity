export const messages = {
  en: {
    housekeepingSjServ: {
      search: {
        title: 'Search Conditions',
        servName: 'Please enter service name',
        hktId: 'Please select service type'
      },
      list: {
        title: 'On Shelf Services',
        today: 'Today On Shelf',
        all: 'All On Shelf'
      },
      table: {
        servId: 'Service ID',
        servName: 'Service Name',
        servDesc: 'Service Description',
        hktName: 'Service Type',
        repairWay: 'Dispatch Method',
        returnVisitFlag: 'Return Visit Method',
        sales: 'Sales',
        defaultFee: 'Default Fee',
        sort: 'Sort',
        state: 'Shelf Status'
      },
      repairWay: {
        grab: 'Grab Order',
        assign: 'Assign',
        roundRobin: 'Round Robin'
      },
      returnVisit: {
        none: 'No Return Visit',
        ratedNoVisit: 'No Visit After Rated',
        allVisit: 'All Return Visit'
      },
      state: {
        notOnShelf: 'Not On Shelf',
        onShelf: 'On Shelf'
      },
      fetchError: 'Failed to fetch service list',
      fetchTypeError: 'Failed to fetch service types'
    }
  },
  zh: {
    housekeepingSjServ: {
      search: {
        title: '查询条件',
        servName: '请输入服务名称',
        hktId: '请选择服务类型'
      },
      list: {
        title: '上架服务',
        today: '今日上架',
        all: '所有上架'
      },
      table: {
        servId: '服务编号',
        servName: '服务名称',
        servDesc: '服务描述',
        hktName: '服务类型',
        repairWay: '派单方式',
        returnVisitFlag: '回访方式',
        sales: '销量',
        defaultFee: '默认费用',
        sort: '排序',
        state: '上架状态'
      },
      repairWay: {
        grab: '抢单',
        assign: '指派',
        roundRobin: '轮训'
      },
      returnVisit: {
        none: '都不回访',
        ratedNoVisit: '已评价不回访',
        allVisit: '都回访'
      },
      state: {
        notOnShelf: '未上架',
        onShelf: '上架'
      },
      fetchError: '获取服务列表失败',
      fetchTypeError: '获取服务类型失败'
    }
  }
}