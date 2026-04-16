export const messages = {
  en: {
    itemReleaseTypeManage: {
      search: {
        title: 'Search Conditions',
        typeNamePlaceholder: 'Please enter type name'
      },
      list: {
        title: 'Release Types'
      },
      table: {
        typeId: 'ID',
        typeName: 'Type Name',
        flowName: 'Flow Name',
        setFlow: 'Set Flow',
        state: 'Status',
        deployed: 'Deployed',
        pendingDeploy: 'Pending Deploy',
        remark: 'Remark',
        deploy: 'Deploy'
      },
      form: {
        typeName: 'Type Name',
        typeNamePlaceholder: 'Required, please enter type name',
        remark: 'Remark',
        remarkPlaceholder: 'Required, please enter remark'
      },
      validate: {
        typeNameRequired: 'Type name is required',
        typeNameMaxLength: 'Type name cannot exceed 30 characters',
        remarkRequired: 'Remark is required',
        remarkMaxLength: 'Remark cannot exceed 512 characters',
        typeIdRequired: 'Type ID is required'
      },
      add: {
        title: 'Add Release Type',
        success: 'Add successfully',
        error: 'Add failed'
      },
      edit: {
        title: 'Edit Release Type',
        success: 'Edit successfully',
        error: 'Edit failed'
      },
      delete: {
        title: 'Delete Confirmation',
        confirmText: 'Are you sure to delete this release type?',
        success: 'Delete successfully',
        error: 'Delete failed'
      },
      fetchError: 'Failed to fetch data',
      deploySuccess: 'Deploy successfully',
      deployError: 'Deploy failed'
    }
  },
  zh: {
    itemReleaseTypeManage: {
      search: {
        title: '查询条件',
        typeNamePlaceholder: '请输入类型名称'
      },
      list: {
        title: '放行类型'
      },
      table: {
        typeId: '编号',
        typeName: '类型名称',
        flowName: '流程名称',
        setFlow: '设置流程',
        state: '状态',
        deployed: '已部署',
        pendingDeploy: '待部署',
        remark: '备注',
        deploy: '部署'
      },
      form: {
        typeName: '类型名称',
        typeNamePlaceholder: '必填，请填写类型名称',
        remark: '备注',
        remarkPlaceholder: '必填，请填写备注'
      },
      validate: {
        typeNameRequired: '类型名称不能为空',
        typeNameMaxLength: '类型名称不能超过30个字符',
        remarkRequired: '备注不能为空',
        remarkMaxLength: '备注不能超过512个字符',
        typeIdRequired: '编号不能为空'
      },
      add: {
        title: '添加放行类型',
        success: '添加成功',
        error: '添加失败'
      },
      edit: {
        title: '修改放行类型',
        success: '修改成功',
        error: '修改失败'
      },
      delete: {
        title: '删除确认',
        confirmText: '确定删除该放行类型吗？',
        success: '删除成功',
        error: '删除失败'
      },
      fetchError: '获取数据失败',
      deploySuccess: '部署成功',
      deployError: '部署失败'
    }
  }
}