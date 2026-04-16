export const messages = {
  en: {
    junkRequirement: {
      search: {
        title: 'Search Conditions',
        classification: 'Please select classification',
        publishUserName: 'Please enter publisher name',
        publishUserLink: 'Please enter contact information',
        state: 'Please select state'
      },
      list: {
        title: 'Junk Information'
      },
      table: {
        junkRequirementId: 'Code',
        classification: 'Classification',
        referencePrice: 'Reference Price',
        publishUserName: 'Publisher',
        publishUserLink: 'Contact',
        state: 'State'
      },
      edit: {
        title: 'Edit Junk',
        classification: 'Classification',
        context: 'Content',
        referencePrice: 'Reference Price',
        publishUserName: 'Publisher',
        publishUserLink: 'Contact',
        state: 'State',
        stateRequired: 'State is required',
        success: 'Review successful',
        error: 'Review failed'
      },
      delete: {
        title: 'Confirm Operation',
        confirm: 'Are you sure to delete this junk?',
        success: 'Delete successful',
        error: 'Delete failed'
      },
      classification: {
        furniture: 'Furniture',
        appliance: 'Appliance'
      },
      state: {
        unreviewed: 'Unreviewed',
        reviewed: 'Reviewed',
        reviewFailed: 'Review Failed',
        completed: 'Completed'
      },
      fetchError: 'Failed to fetch junk requirements'
    },
  },
  zh: {
    junkRequirement: {
      search: {
        title: '查询条件',
        classification: '请选择类别',
        publishUserName: '请输入发布人',
        publishUserLink: '请输入联系方式',
        state: '请选择状态'
      },
      list: {
        title: '旧货信息'
      },
      table: {
        junkRequirementId: '编码',
        classification: '类别',
        referencePrice: '参考价格',
        publishUserName: '发布人',
        publishUserLink: '联系方式',
        state: '状态'
      },
      edit: {
        title: '修改旧货',
        classification: '类别',
        context: '内容',
        referencePrice: '参考价格',
        publishUserName: '发布人',
        publishUserLink: '联系方式',
        state: '状态',
        stateRequired: '状态不能为空',
        success: '审核成功',
        error: '审核失败'
      },
      delete: {
        title: '请确认您的操作',
        confirm: '确定删除旧货吗？',
        success: '删除成功',
        error: '删除失败'
      },
      classification: {
        furniture: '家具',
        appliance: '电器'
      },
      state: {
        unreviewed: '未审核',
        reviewed: '审核通过',
        reviewFailed: '审核失败',
        completed: '处理完成'
      },
      fetchError: '获取旧货需求失败'
    },
  }
}