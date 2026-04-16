export const messages = {
  en: {
    contractChangeManage: {
      search: {
        title: 'Search Conditions',
        contractName: 'Please enter contract name',
        contractCode: 'Please enter contract code',
        contractType: 'Please select contract type'
      },
      list: {
        title: 'Contract Change Information',
        mainBodyChange: 'Main Body Change',
        leaseAdjustment: 'Lease Adjustment',
        assetsChange: 'Assets Change'
      },
      table: {
        contractName: 'Contract Name',
        contractCode: 'Contract Code',
        contractType: 'Contract Type',
        partyA: 'Party A',
        partyB: 'Party B',
        changeType: 'Change Type',
        changePerson: 'Change Person',
        applyTime: 'Apply Time',
        remark: 'Remark',
        state: 'State'
      },
      note: 'Note: Please set up the contract change process in System Management > Process Management before using this function',
      fetchError: 'Failed to get contract change list'
    },
    deleteContractChangePlan: {
      title: 'Confirm Operation',
      confirmText: 'Are you sure to delete the contract change information?',
      deleteSuccess: 'Delete successfully',
      deleteError: 'Failed to delete contract change'
    }
  },
  zh: {
    contractChangeManage: {
      search: {
        title: '查询条件',
        contractName: '请输入合同名称',
        contractCode: '请输入合同编号',
        contractType: '请选择合同类型'
      },
      list: {
        title: '合同变更信息',
        mainBodyChange: '主体变更',
        leaseAdjustment: '租期调整',
        assetsChange: '资产变更'
      },
      table: {
        contractName: '合同名称',
        contractCode: '合同编号',
        contractType: '合同类型',
        partyA: '甲方',
        partyB: '乙方',
        changeType: '变更类型',
        changePerson: '变更人',
        applyTime: '申请时间',
        remark: '说明',
        state: '状态'
      },
      note: '注意：此功能使用前请先到系统管理>流程管理中设置合同变更流程',
      fetchError: '获取合同变更列表失败'
    },
    deleteContractChangePlan: {
      title: '请确认您的操作',
      confirmText: '确定删除合同变更信息？',
      deleteSuccess: '删除成功',
      deleteError: '删除合同变更失败'
    }
  }
}