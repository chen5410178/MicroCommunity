export const messages = {
  en: {
    reportCustomGroupManage: {
      search: {
        title: 'Search Conditions',
        groupId: 'Please enter group ID',
        name: 'Please enter group name',
        url: 'Please enter group URL'
      },
      list: {
        title: 'Report Group Information'
      },
      table: {
        groupId: 'Group ID',
        name: 'Group Name',
        url: 'Group URL',
        remark: 'Description'
      },
      add: {
        namePlaceholder: 'Required, please enter group name',
        urlPlaceholder: 'Required, please enter group URL',
        remarkPlaceholder: 'Required, please enter description',
        success: 'Add report group successfully'
      },
      edit: {
        namePlaceholder: 'Required, please enter group name',
        urlPlaceholder: 'Required, please enter group URL',
        remarkPlaceholder: 'Required, please enter description',
        success: 'Update report group successfully'
      },
      delete: {
        confirmText: 'Are you sure to delete this report group?',
        success: 'Delete report group successfully'
      },
      validate: {
        groupIdRequired: 'Group ID is required',
        nameRequired: 'Group name is required',
        nameMaxLength: 'Group name cannot exceed 128 characters',
        urlRequired: 'Group URL is required',
        urlMaxLength: 'Group URL cannot exceed 512 characters',
        remarkRequired: 'Description is required',
        remarkMaxLength: 'Description cannot exceed 512 characters'
      },
      fetchError: 'Failed to fetch report group data'
    }
  },
  zh: {
    reportCustomGroupManage: {
      search: {
        title: '查询条件',
        groupId: '请输入组ID',
        name: '请输入组名称',
        url: '请输入组URL'
      },
      list: {
        title: '报表组信息'
      },
      table: {
        groupId: '组ID',
        name: '组名称',
        url: '组URL',
        remark: '描述'
      },
      add: {
        namePlaceholder: '必填，请填写组名称',
        urlPlaceholder: '必填，请填写组URL',
        remarkPlaceholder: '必填，请填写描述',
        success: '添加报表组成功'
      },
      edit: {
        namePlaceholder: '必填，请填写组名称',
        urlPlaceholder: '必填，请填写组URL',
        remarkPlaceholder: '必填，请填写描述',
        success: '更新报表组成功'
      },
      delete: {
        confirmText: '确定删除该报表组吗？',
        success: '删除报表组成功'
      },
      validate: {
        groupIdRequired: '组ID不能为空',
        nameRequired: '组名称不能为空',
        nameMaxLength: '组名称不能超过128个字符',
        urlRequired: '组URL不能为空',
        urlMaxLength: '组URL不能超过512个字符',
        remarkRequired: '描述不能为空',
        remarkMaxLength: '描述不能超过512个字符'
      },
      fetchError: '获取报表组数据失败'
    }
  }
}