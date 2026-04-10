export const messages = {
  en: {
    complaint: {
      search: {
        title: 'Search Conditions',
        type: 'Select Type',
        room: 'Building-Unit-Room e.g. 1-1-1',
        contact: 'Contact Person',
        phone: 'Contact Phone',
        id: 'Complaint ID',
        startTime: 'Start Time',
        endTime: 'End Time'
      },
      list: {
        title: 'Complaints & Suggestions',
        add: 'Add Complaint'
      },
      status: {
        all: 'All',
        processing: 'Processing',
        completed: 'Completed'
      },
      table: {
        id: 'ID',
        type: 'Type',
        room: 'Room',
        contact: 'Contact',
        phone: 'Phone',
        status: 'Status',
        handler: 'Handler',
        handlerPhone: 'Handler Phone',
        createTime: 'Create Time'
      },
      edit: {
        title: 'Edit Information',
        type: 'Type',
        contact: 'Contact',
        phone: 'Phone',
        content: 'Content',
        typePlaceholder: 'Required, please select type',
        contactPlaceholder: 'Required, please enter contact',
        phonePlaceholder: 'Required, please enter phone',
        contentPlaceholder: 'Required, please enter content',
        success: 'Edit successful'
      },
      delete: {
        title: 'Confirm Operation',
        confirm: 'Are you sure to delete this complaint?',
        success: 'Delete successful'
      },
      detail: {
        title: 'Details',
        room: 'Room',
        id: 'ID',
        type: 'Type',
        contact: 'Contact',
        phone: 'Phone',
        status: 'Status',
        handler: 'Handler',
        handlerPhone: 'Handler Phone',
        handlerId: 'Handler ID',
        content: 'Content',
        photos: 'Photos',
        serial: 'No.',
        handleTime: 'Handle Time',
        duration: 'Duration',
        comment: 'Comment'
      },
      validate: {
        type: 'Type is required',
        contact: 'Contact is required',
        contactMax: 'Contact cannot exceed 200 characters',
        phone: 'Phone is required',
        phoneFormat: 'Invalid phone format',
        content: 'Content is required',
        contentMax: 'Content cannot exceed 4000 characters'
      },
      fetchError: 'Failed to fetch complaints data'
    }
  },
  zh: {
    complaint: {
      search: {
        title: '查询条件',
        type: '请选择类型',
        room: '楼栋-单元-房屋 如1-1-1',
        contact: '请输入联系人',
        phone: '请输入联系电话',
        id: '请输入工单编号',
        startTime: '请选择开始时间',
        endTime: '请选择结束时间'
      },
      list: {
        title: '投诉建议',
        add: '投诉'
      },
      status: {
        all: '全部',
        processing: '处理中',
        completed: '处理完成'
      },
      table: {
        id: '订单编号',
        type: '类型',
        room: '房屋',
        contact: '联系人',
        phone: '联系电话',
        status: '状态',
        handler: '处理人',
        handlerPhone: '处理人电话',
        createTime: '创建时间'
      },
      edit: {
        title: '修改信息',
        type: '类型',
        contact: '联系人',
        phone: '联系电话',
        content: '内容',
        typePlaceholder: '必填，请选择类型',
        contactPlaceholder: '必填，请填写联系人',
        phonePlaceholder: '必填，请填写联系电话',
        contentPlaceholder: '必填，请填写内容',
        success: '修改成功'
      },
      delete: {
        title: '请确认您的操作',
        confirm: '确定删除投诉建议吗？',
        success: '删除成功'
      },
      detail: {
        title: '详情',
        room: '房屋',
        id: 'ID',
        type: '类型',
        contact: '联系人',
        phone: '联系电话',
        status: '状态',
        handler: '处理人',
        handlerPhone: '处理人电话',
        handlerId: '处理人编号',
        content: '内容',
        photos: '图片',
        serial: '序号',
        handleTime: '处理时间',
        duration: '耗时',
        comment: '意见'
      },
      validate: {
        type: '投诉类型不能为空',
        contact: '投诉人不能为空',
        contactMax: '投诉人不能大于200位',
        phone: '投诉电话不能为空',
        phoneFormat: '投诉电话格式错误',
        content: '投诉内容不能为空',
        contentMax: '投诉状态超过4000位'
      },
      fetchError: '获取投诉数据失败'
    }
  }
}