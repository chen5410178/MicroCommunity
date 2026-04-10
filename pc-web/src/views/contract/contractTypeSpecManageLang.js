export const messages = {
  en: {
    contractTypeSpecManage: {
      search: {
        title: 'Search Conditions',
        specCd: 'Spec ID',
        specName: 'Spec Name',
        specShow: 'Spec Display',
        all: 'All',
        yes: 'Yes',
        no: 'No'
      },
      table: {
        title: 'Contract Extension Information',
        specCd: 'Spec ID',
        specName: 'Spec Name',
        required: 'Required',
        specShow: 'Display',
        specValueType: 'Value Type',
        specType: 'Spec Type',
        listShow: 'Query Display',
        operation: 'Operation'
      },
      add: {
        title: 'Add',
        specName: 'Spec Name',
        specNamePlaceholder: 'Required, please enter spec name',
        specHoldplace: 'Description',
        specHoldplacePlaceholder: 'Optional, please enter description',
        required: 'Required',
        requiredPlaceholder: 'Required, please select required',
        specShow: 'Display',
        specShowPlaceholder: 'Required, please select display',
        specValueType: 'Value Type',
        specValueTypePlaceholder: 'Required, please select value type',
        specType: 'Spec Type',
        specTypePlaceholder: 'Required, please select spec type',
        listShow: 'Query Display',
        listShowPlaceholder: 'Required, please select query display',
        success: 'Add successfully',
        error: 'Add failed'
      },
      edit: {
        title: 'Edit',
        specName: 'Spec Name',
        specNamePlaceholder: 'Required, please enter spec name',
        specHoldplace: 'Description',
        specHoldplacePlaceholder: 'Optional, please enter description',
        required: 'Required',
        requiredPlaceholder: 'Required, please select required',
        specShow: 'Display',
        specShowPlaceholder: 'Required, please select display',
        specValueType: 'Value Type',
        specValueTypePlaceholder: 'Required, please select value type',
        specType: 'Spec Type',
        specTypePlaceholder: 'Required, please select spec type',
        listShow: 'Query Display',
        listShowPlaceholder: 'Required, please select query display',
        success: 'Edit successfully',
        error: 'Edit failed'
      },
      delete: {
        title: 'Confirm Operation',
        confirmText: 'Are you sure to delete this contract type spec?',
        success: 'Delete successfully',
        error: 'Delete failed'
      },
      specValueType: {
        string: 'String',
        integer: 'Integer',
        amount: 'Amount',
        date: 'Date',
        time: 'Time'
      },
      specType: {
        input: 'Input',
        select: 'Select'
      },
      validate: {
        specNameRequired: 'Spec name is required',
        specNameMaxLength: 'Spec name is too long',
        specHoldplaceMaxLength: 'Description is too long',
        requiredRequired: 'Required is required',
        specShowRequired: 'Display is required',
        specValueTypeRequired: 'Value type is required',
        specTypeRequired: 'Spec type is required',
        listShowRequired: 'Query display is required',
        specCdRequired: 'Spec is required'
      },
      fetchError: 'Failed to fetch data'
    }
  },
  zh: {
    contractTypeSpecManage: {
      search: {
        title: '查询条件',
        specCd: '规格ID',
        specName: '规格名称',
        specShow: '规格是否显示',
        all: '全部',
        yes: '是',
        no: '否'
      },
      table: {
        title: '合同扩展信息',
        specCd: '规格ID',
        specName: '规格名称',
        required: '必填',
        specShow: '规格是否展示',
        specValueType: '值类型',
        specType: '规格类型',
        listShow: '查询显示',
        operation: '操作'
      },
      add: {
        title: '添加',
        specName: '规格名称',
        specNamePlaceholder: '必填，请填写规格名称',
        specHoldplace: '说明',
        specHoldplacePlaceholder: '选填，请填写说明',
        required: '必填',
        requiredPlaceholder: '必填，请选择必填',
        specShow: '展示',
        specShowPlaceholder: '必填，请选择展示',
        specValueType: '值类型',
        specValueTypePlaceholder: '必填，请选择值类型',
        specType: '规格类型',
        specTypePlaceholder: '必填，请选择规格类型',
        listShow: '查询显示',
        listShowPlaceholder: '必填，请选择查询显示',
        success: '添加成功',
        error: '添加失败'
      },
      edit: {
        title: '修改',
        specName: '规格名称',
        specNamePlaceholder: '必填，请填写规格名称',
        specHoldplace: '说明',
        specHoldplacePlaceholder: '选填，请填写说明',
        required: '必填',
        requiredPlaceholder: '必填，请选择必填',
        specShow: '展示',
        specShowPlaceholder: '必填，请选择展示',
        specValueType: '值类型',
        specValueTypePlaceholder: '必填，请选择值类型',
        specType: '规格类型',
        specTypePlaceholder: '必填，请选择规格类型',
        listShow: '查询显示',
        listShowPlaceholder: '必填，请选择查询显示',
        success: '修改成功',
        error: '修改失败'
      },
      delete: {
        title: '请确认您的操作',
        confirmText: '确定删除合同类型属性?',
        success: '删除成功',
        error: '删除失败'
      },
      specValueType: {
        string: '字符串',
        integer: '整数',
        amount: '金额',
        date: '日期',
        time: '时间'
      },
      specType: {
        input: '文本框',
        select: '选择框'
      },
      validate: {
        specNameRequired: '规格名称不能为空',
        specNameMaxLength: '规格名称太长',
        specHoldplaceMaxLength: '说明不能超过500位',
        requiredRequired: '必填不能为空',
        specShowRequired: '展示不能为空',
        specValueTypeRequired: '值类型不能为空',
        specTypeRequired: '规格类型不能为空',
        listShowRequired: '查询显示不能为空',
        specCdRequired: '规格不能为空'
      },
      fetchError: '获取数据失败'
    }
  }
}