export const messages = {
  en: {
    integralConfigManage: {
      search: {
        title: 'Search Conditions',
        configNamePlaceholder: 'Please enter standard name'
      },
      list: {
        title: 'Points Standard'
      },
      table: {
        configName: 'Standard Name',
        computingFormula: 'Calculation Formula',
        squarePrice: 'Amount/Month',
        additionalAmount: 'Points',
        scale: 'Rounding'
      },
      form: {
        configName: 'Standard Name',
        configNamePlaceholder: 'Required, please enter standard name',
        computingFormula: 'Calculation Formula',
        computingFormulaPlaceholder: 'Required, please select calculation formula',
        amount: 'Amount',
        amountPlaceholder: 'Required, please enter amount',
        month: 'Month',
        monthPlaceholder: 'Required, please enter month',
        points: 'Points',
        pointsPlaceholder: 'Required, please enter points',
        scale: 'Rounding',
        scalePlaceholder: 'Required, please select rounding'
      },
      formula: {
        fullAmount: 'Full amount give points',
        fullMonth: 'Full month give points'
      },
      scale: {
        roundUp: 'Round up',
        roundDown: 'Round down'
      },
      validate: {
        configIdRequired: 'Points standard cannot be empty',
        configNameRequired: 'Standard name cannot be empty',
        configNameMaxLength: 'Standard name cannot exceed 100 characters',
        computingFormulaRequired: 'Calculation formula cannot be empty',
        squarePriceRequired: 'Amount/month cannot be empty',
        squarePriceMaxLength: 'Amount/month cannot exceed 12 characters',
        additionalAmountRequired: 'Points cannot be empty',
        additionalAmountMaxLength: 'Points cannot exceed 12 characters',
        scaleRequired: 'Rounding cannot be empty'
      },
      message: {
        addSuccess: 'Add points standard successfully',
        addFailed: 'Add points standard failed',
        editSuccess: 'Edit points standard successfully',
        editFailed: 'Edit points standard failed',
        deleteSuccess: 'Delete points standard successfully',
        deleteFailed: 'Delete points standard failed',
        fetchError: 'Failed to get points standard data'
      },
      add: {
        title: 'Add Points Standard'
      },
      edit: {
        title: 'Edit Points Standard'
      },
      delete: {
        title: 'Delete Points Standard',
        confirmMessage: 'Are you sure to delete this points standard?'
      }
    }
  },
  zh: {
    integralConfigManage: {
      search: {
        title: '查询条件',
        configNamePlaceholder: '请输入标准名称'
      },
      list: {
        title: '积分标准'
      },
      table: {
        configName: '标准名称',
        computingFormula: '计算公式',
        squarePrice: '金额/月',
        additionalAmount: '赠送积分',
        scale: '进位'
      },
      form: {
        configName: '标准名称',
        configNamePlaceholder: '必填，请填写标准名称',
        computingFormula: '计算公式',
        computingFormulaPlaceholder: '必填，请选择计算公式',
        amount: '金额',
        amountPlaceholder: '必填，请填写金额',
        month: '月份',
        monthPlaceholder: '必填，请填写月份',
        points: '积分',
        pointsPlaceholder: '必填，请填写积分',
        scale: '进位',
        scalePlaceholder: '必填，请选择进位'
      },
      formula: {
        fullAmount: '满(金额)送积分',
        fullMonth: '满(月)送积分'
      },
      scale: {
        roundUp: '向上取整',
        roundDown: '向下取整'
      },
      validate: {
        configIdRequired: '积分标准不能为空',
        configNameRequired: '标准名称不能为空',
        configNameMaxLength: '标准名称不能超过100个字符',
        computingFormulaRequired: '计算公式不能为空',
        squarePriceRequired: '金额/月不能为空',
        squarePriceMaxLength: '金额/月不能超过12个字符',
        additionalAmountRequired: '积分不能为空',
        additionalAmountMaxLength: '积分不能超过12个字符',
        scaleRequired: '进位不能为空'
      },
      message: {
        addSuccess: '添加积分标准成功',
        addFailed: '添加积分标准失败',
        editSuccess: '修改积分标准成功',
        editFailed: '修改积分标准失败',
        deleteSuccess: '删除积分标准成功',
        deleteFailed: '删除积分标准失败',
        fetchError: '获取积分标准数据失败'
      },
      add: {
        title: '添加积分标准'
      },
      edit: {
        title: '修改积分标准'
      },
      delete: {
        title: '删除积分标准',
        confirmMessage: '确定删除该积分标准吗？'
      }
    }
  }
}