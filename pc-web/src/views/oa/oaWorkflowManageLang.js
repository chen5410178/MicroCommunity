export const messages = {
  en: {
    oaWorkflowManage: {
      search: {
        title: 'Search Conditions',
        flowNamePlaceholder: 'Please enter workflow name',
        flowTypePlaceholder: 'Please select workflow type',
        selectAll: 'All'
      },
      list: {
        title: 'Workflow Instances',
        addWorkflow: 'New Workflow'
      },
      table: {
        flowId: 'Workflow ID',
        flowName: 'Workflow Name',
        flowType: 'Workflow Type',
        normalFlow: 'Normal Workflow',
        unknown: 'Unknown',
        modelId: 'Model ID',
        state: 'Status',
        deployed: 'Deployed',
        pendingDeploy: 'Pending Deploy',
        describle: 'Description',
        createTime: 'Create Time',
        workflow: 'Workflow',
        designForm: 'Design Form',
        deploy: 'Deploy'
      },
      add: {
        title: 'New Workflow',
        flowName: 'Workflow Name',
        flowNamePlaceholder: 'Required, please enter workflow name',
        flowType: 'Workflow Type',
        flowTypePlaceholder: 'Required, please select workflow type',
        describle: 'Remark',
        describlePlaceholder: 'Optional, please enter remark',
        success: 'Add successfully',
        error: 'Add failed'
      },
      edit: {
        title: 'Edit Workflow',
        flowName: 'Workflow Name',
        flowNamePlaceholder: 'Required, please enter workflow name',
        describle: 'Remark',
        describlePlaceholder: 'Optional, please enter remark',
        success: 'Edit successfully',
        error: 'Edit failed'
      },
      delete: {
        title: 'Confirm Operation',
        confirmText: 'Are you sure to delete this workflow instance?',
        success: 'Delete successfully',
        error: 'Delete failed'
      },
      validate: {
        flowNameRequired: 'Workflow name is required',
        flowNameMaxLength: 'Workflow name cannot exceed 64 characters',
        flowTypeRequired: 'Workflow type is required',
        flowIdRequired: 'Workflow ID is required'
      },
      fetchError: 'Failed to get workflow list',
      deployError: 'Failed to deploy workflow'
    }
  },
  zh: {
    oaWorkflowManage: {
      search: {
        title: '查询条件',
        flowNamePlaceholder: '请输入流程名称',
        flowTypePlaceholder: '请选择流程类型',
        selectAll: '全部'
      },
      list: {
        title: '流程实例',
        addWorkflow: '新建流程'
      },
      table: {
        flowId: '工作流ID',
        flowName: '流程名称',
        flowType: '流程类型',
        normalFlow: '普通流程',
        unknown: '未知',
        modelId: '模型ID',
        state: '状态',
        deployed: '已部署',
        pendingDeploy: '待部署',
        describle: '描述',
        createTime: '创建时间',
        workflow: '流程',
        designForm: '设计表单',
        deploy: '发布'
      },
      add: {
        title: '新建流程',
        flowName: '流程名称',
        flowNamePlaceholder: '必填，请填写流程名称',
        flowType: '流程类型',
        flowTypePlaceholder: '必填，请选择流程类型',
        describle: '备注',
        describlePlaceholder: '选填，请填写备注',
        success: '添加成功',
        error: '添加失败'
      },
      edit: {
        title: '修改流程',
        flowName: '流程名称',
        flowNamePlaceholder: '必填，请填写流程名称',
        describle: '备注',
        describlePlaceholder: '选填，请填写备注',
        success: '修改成功',
        error: '修改失败'
      },
      delete: {
        title: '请确认您的操作',
        confirmText: '确定删除流程实例',
        success: '删除成功',
        error: '删除失败'
      },
      validate: {
        flowNameRequired: '流程名称不能为空',
        flowNameMaxLength: '流程名称超过64位',
        flowTypeRequired: '流程类型不能为空',
        flowIdRequired: '工作流ID不能为空'
      },
      fetchError: '获取流程列表失败',
      deployError: '发布流程失败'
    }
  }
}