export const messages = {
  en: {
    contractTypeManage: {
      search: {
        title: 'Search Conditions',
        typeNamePlaceholder: 'Please enter type name',
        auditPlaceholder: 'Please select audit status'
      },
      list: {
        title: 'Contract Types',
        setAudit: 'Set Auditor'
      },
      table: {
        typeName: 'Type Name',
        audit: 'Audit Status',
        remark: 'Description',
        createTime: 'Create Time',
        specInfo: 'Spec Info',
        template: 'Template'
      },
      add: {
        title: 'Add Contract Type',
        typeName: 'Type Name',
        typeNamePlaceholder: 'Required, please enter type name',
        audit: 'Audit Status',
        auditPlaceholder: 'Please select audit status',
        remark: 'Description',
        remarkPlaceholder: 'Optional, please enter description'
      },
      edit: {
        title: 'Edit Contract Type',
        typeName: 'Type Name',
        typeNamePlaceholder: 'Required, please enter type name',
        audit: 'Audit Status',
        auditPlaceholder: 'Please select audit status',
        remark: 'Description',
        remarkPlaceholder: 'Optional, please enter description'
      },
      delete: {
        title: 'Confirm Operation',
        confirmText: 'Are you sure to delete this contract type?'
      },
      template: {
        title: 'Contract Template',
        content: 'Template Content',
        placeholder: 'Required, please enter contract template'
      },
      validate: {
        contractTypeIdRequired: 'Contract type ID is required',
        typeNameRequired: 'Type name is required',
        typeNameMaxLength: 'Type name cannot exceed 64 characters',
        auditRequired: 'Audit status is required',
        remarkMaxLength: 'Description cannot exceed 200 characters',
        contentRequired: 'Template content is required',
        contractTypeRequired: 'Contract type is required'
      },
      fetchError: 'Failed to fetch contract types'
    }
  },
  zh: {
    contractTypeManage: {
      search: {
        title: '查询条件',
        typeNamePlaceholder: '请输入类型名称',
        auditPlaceholder: '请选择审核状态'
      },
      list: {
        title: '合同类型',
        setAudit: '设置审核人员'
      },
      table: {
        typeName: '类型名称',
        audit: '是否审核',
        remark: '描述',
        createTime: '创建时间',
        specInfo: '扩展信息',
        template: '合同模板'
      },
      add: {
        title: '添加合同类型',
        typeName: '类型名称',
        typeNamePlaceholder: '必填，请填写类型名称',
        audit: '是否审核',
        auditPlaceholder: '请选择审核状态',
        remark: '描述',
        remarkPlaceholder: '选填，请填写描述'
      },
      edit: {
        title: '修改合同类型',
        typeName: '类型名称',
        typeNamePlaceholder: '必填，请填写类型名称',
        audit: '是否审核',
        auditPlaceholder: '请选择审核状态',
        remark: '描述',
        remarkPlaceholder: '选填，请填写描述'
      },
      delete: {
        title: '请确认您的操作',
        confirmText: '确定删除该合同类型吗？'
      },
      template: {
        title: '合同模板',
        content: '模板内容',
        placeholder: '必填，请输入合同模板'
      },
      validate: {
        contractTypeIdRequired: '合同类型ID不能为空',
        typeNameRequired: '类型名称不能为空',
        typeNameMaxLength: '类型名称不能超过64个字符',
        auditRequired: '审核状态不能为空',
        remarkMaxLength: '描述不能超过200个字符',
        contentRequired: '模板内容必填',
        contractTypeRequired: '合同类型不能为空'
      },
      fetchError: '获取合同类型失败'
    }
  }
}