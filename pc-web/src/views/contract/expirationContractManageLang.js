export const messages = {
  en: {
    expirationContractManage: {
      search: {
        title: 'Search Conditions',
        contractNameLike: 'Please enter contract name',
        contractCode: 'Please enter contract code',
        contractType: 'Please select contract type'
      },
      list: {
        title: 'Expiration Contracts'
      },
      table: {
        contractName: 'Contract Name',
        contractCode: 'Contract Code',
        contractType: 'Contract Type',
        partyA: 'Party A',
        partyB: 'Party B',
        operator: 'Operator',
        amount: 'Amount',
        startTime: 'Start Time',
        endTime: 'End Time',
        status: 'Status'
      },
      button: {
        renew: 'Renew',
        stop: 'Stop'
      },
      fetchError: 'Failed to fetch contract data'
    },
    stopContract: {
      dialog: {
        title: 'Confirm Operation',
        content: 'Are you sure to stop this contract? It cannot be restarted after stopping!'
      },
      button: {
        cancel: 'Cancel',
        confirm: 'Confirm Stop'
      },
      message: {
        success: 'Contract stopped successfully',
        error: 'Failed to stop contract'
      }
    }
  },
  zh: {
    expirationContractManage: {
      search: {
        title: '查询条件',
        contractNameLike: '请输入合同名称',
        contractCode: '请输入合同编号',
        contractType: '请选择合同类型'
      },
      list: {
        title: '到期合同'
      },
      table: {
        contractName: '合同名称',
        contractCode: '合同编号',
        contractType: '合同类型',
        partyA: '甲方',
        partyB: '乙方',
        operator: '经办人',
        amount: '合同金额',
        startTime: '开始时间',
        endTime: '结束时间',
        status: '状态'
      },
      button: {
        renew: '续签',
        stop: '终止'
      },
      fetchError: '获取合同数据失败'
    },
    stopContract: {
      dialog: {
        title: '确认操作',
        content: '确定要终止此合同吗？终止后将无法重新启动！'
      },
      button: {
        cancel: '取消',
        confirm: '确认终止'
      },
      message: {
        success: '合同终止成功',
        error: '合同终止失败'
      }
    }
  }
}