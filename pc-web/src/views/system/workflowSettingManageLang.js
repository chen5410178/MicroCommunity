export const messages = {
  en: {
    workflowSettingManage: {
      title: 'Process Settings',
      flowName: 'Process Name',
      flowNamePlaceholder: 'Required, please enter the title',
      description: 'Process Description',
      descriptionPlaceholder: 'Optional, please enter the process description',
      submitterEnd: 'Submitter End',
      submitterEndPlaceholder: 'Required, please select whether the submitter needs to confirm',
      yes: 'Yes',
      no: 'No',
      flowSteps: 'Process Steps',
      addStep: 'Add Step',
      step: 'Step',
      selectStaff: 'Select Staff',
      normalFlow: 'Normal Process',
      countersign: 'Countersign',
      deleteStep: 'Delete Step',
      addStaff: 'Add Staff',
      deleteStaff: 'Delete Staff',
      instructions: 'Instructions',
      instruction1: 'At least two steps are required, the last step must be a fixed warehouse manager',
      instruction2: 'Warehouse managers cannot participate in the review process',
      instruction3: 'Warehouse managers cannot be dynamically assigned',
      instruction4: 'Must configure the corresponding process',
      invalidOperation: 'Invalid operation'
    },
    orgTree: {
      fetchError: 'Failed to fetch organization tree'
    }
  },
  zh: {
    workflowSettingManage: {
      title: '流程设置',
      flowName: '流程名称',
      flowNamePlaceholder: '必填，请填写标题',
      description: '流程说明',
      descriptionPlaceholder: '选填，请填写流程说明',
      submitterEnd: '提交者结束',
      submitterEndPlaceholder: '必填，请选择最后环节是否提交者确认',
      yes: '是',
      no: '否',
      flowSteps: '流程步骤',
      addStep: '添加步骤',
      step: '第',
      selectStaff: '选择员工',
      normalFlow: '普通流程',
      countersign: '会签',
      deleteStep: '删除步骤',
      addStaff: '添加员工',
      deleteStaff: '删除员工',
      instructions: '说明',
      instruction1: '添加步骤必须要两步以上，最后一步为固定仓库管理员',
      instruction2: '仓库管理不能参与审核流程',
      instruction3: '仓库管理员不能为动态指定人员',
      instruction4: '必须配置对应的流程',
      invalidOperation: '操作错误'
    },
    orgTree: {
      fetchError: '获取组织树失败'
    }
  }
}