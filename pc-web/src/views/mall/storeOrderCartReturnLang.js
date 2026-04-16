export const messages = {
  en: {
    storeOrderCartReturn: {
      search: {
        title: 'Search Conditions',
        cartId: 'Order Number',
        state: 'Please select status',
        prodName: 'Please enter product name'
      },
      list: {
        title: 'Return Orders'
      },
      table: {
        cartId: 'Order Number',
        prodName: 'Product Name',
        specValue: 'Product Specification',
        personName: 'User Name',
        returnPayPrice: 'Refund Amount',
        stateName: 'Order Status',
        returnReason: 'Refund Reason',
        createTime: 'Application Time'
      },
      state: {
        all: 'All Status',
        applyRefund: 'Apply for Refund',
        returnSuccess: 'Return Success',
        refunded: 'Refunded'
      },
      fetchError: 'Failed to fetch return orders'
    }
  },
  zh: {
    storeOrderCartReturn: {
      search: {
        title: '查询条件',
        cartId: '订单编号',
        state: '请选择状态',
        prodName: '请输入商品名称'
      },
      list: {
        title: '退货订单'
      },
      table: {
        cartId: '订单号',
        prodName: '商品名称',
        specValue: '商品规格',
        personName: '用户名称',
        returnPayPrice: '退款金额',
        stateName: '订单状态',
        returnReason: '退款原因',
        createTime: '申请时间'
      },
      state: {
        all: '全部状态',
        applyRefund: '申请退款',
        returnSuccess: '退货成功',
        refunded: '已退款'
      },
      fetchError: '获取退货订单失败'
    }
  }
}