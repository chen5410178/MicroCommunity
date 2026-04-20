export const messages = {
  en: {
    ownerVoting: {
      title: 'Owner Voting',
      search: {
        title: 'Search Conditions',
        qaId: 'Please enter voting ID',
        qaName: 'Please enter voting name'
      },
      list: {
        title: 'Voting List'
      },
      table: {
        qaName: 'Voting Name',
        validity: 'Validity Period',
        type: 'Type',
        options: 'Options',
        peopleChose: 'people chose',
        voteCount: 'Vote Count',
        state: 'Status',
        createTime: 'Create Time'
      },
      state: {
        drafting: 'Draft',
        publishing: 'Published',
        deleted: 'Deleted',
        published: 'Published',
        unpublished: 'Unpublished'
      },
      type: {
        agree: 'Agreement',
        agreeAndDisagree: 'Agree / Disagree',
        multi: 'Multiple Choice',
        single: 'Single Choice',
        multiple: 'Multiple Choice'
      },
      operation: {
        edit: 'Edit',
        delete: 'Delete',
        publish: 'Publish',
        detail: 'Voting Result',
        result: 'Voting Result'
      },
      add: {
        title: 'Add Voting',
        votingName: 'Voting Name',
        votingNamePlaceholder: 'Required, please enter voting name',
        votingType: 'Voting Type',
        selectVotingType: 'Please select voting type',
        votingStartTime: 'Start Time',
        selectStartTime: 'Required, please select start time',
        votingEndTime: 'End Time',
        selectEndTime: 'Required, please select end time',
        votingDesc: 'Voting Description',
        votingDescPlaceholder: 'Please enter voting description',
        optionTitle: 'Voting Options',
        addOption: 'Add Option',
        optionPlaceholder: 'Please enter option',
        save: 'Save',
        cancel: 'Cancel',
        validate: {
          votingName: 'Voting name cannot be empty',
          votingType: 'Voting type cannot be empty',
          votingStartTime: 'Start time cannot be empty',
          votingEndTime: 'End time cannot be empty',
          endTimeAfterStart: 'End time must be later than start time',
          atLeastOneOption: 'At least one option is required',
          optionNotEmpty: 'Option cannot be empty'
        },
        addSuccess: 'Add successful',
        addFailed: 'Add failed'
      },
      edit: {
        title: 'Edit Voting',
        save: 'Save',
        cancel: 'Cancel',
        updateSuccess: 'Update successful',
        updateFailed: 'Update failed'
      },
      delete: {
        title: 'Confirm Operation',
        confirm: 'Are you sure to delete this voting?',
        success: 'Delete successful',
        failed: 'Delete failed'
      },
      detail: {
        title: 'Voting Result',
        votingName: 'Voting Name',
        votingType: 'Voting Type',
        votingStartTime: 'Start Time',
        votingEndTime: 'End Time',
        optionTitle: 'Option',
        optionValue: 'Votes',
        optionPercent: 'Percentage',
        total: 'Total Votes',
        fetchError: 'Failed to fetch voting result'
      },
      validate: {
        votingName: 'Voting name cannot be empty',
        votingType: 'Voting type cannot be empty',
        votingStartTime: 'Start time cannot be empty',
        votingEndTime: 'End time cannot be empty',
        endTimeAfterStart: 'End time must be later than start time',
        atLeastOneOption: 'At least one option is required',
        optionNotEmpty: 'Option cannot be empty'
      },
      fetchError: 'Failed to fetch voting list'
    }
  },
  zh: {
    ownerVoting: {
      title: '业主投票',
      search: {
        title: '查询条件',
        qaId: '请输入投票编号',
        qaName: '请输入投票名称'
      },
      list: {
        title: '投票列表'
      },
      table: {
        qaName: '投票名称',
        validity: '有效期',
        type: '类型',
        options: '选项',
        peopleChose: '人选择',
        voteCount: '投票数',
        state: '状态',
        createTime: '创建时间'
      },
      state: {
        drafting: '草稿',
        publishing: '已发布',
        deleted: '已删除',
        published: '已发布',
        unpublished: '未发布'
      },
      type: {
        agree: '同意',
        agreeAndDisagree: '同意/反对',
        multi: '多选',
        single: '单选',
        multiple: '多选'
      },
      operation: {
        edit: '修改',
        delete: '删除',
        publish: '发布',
        detail: '投票结果',
        result: '投票结果'
      },
      add: {
        title: '新增投票',
        votingName: '投票名称',
        votingNamePlaceholder: '必填，请填写投票名称',
        votingType: '投票类型',
        selectVotingType: '请选择投票类型',
        votingStartTime: '开始时间',
        selectStartTime: '必填，请选择开始时间',
        votingEndTime: '结束时间',
        selectEndTime: '必填，请选择结束时间',
        votingDesc: '投票说明',
        votingDescPlaceholder: '请输入投票说明',
        optionTitle: '投票选项',
        addOption: '添加选项',
        optionPlaceholder: '请输入选项',
        save: '保存',
        cancel: '取消',
        validate: {
          votingName: '投票名称不能为空',
          votingType: '投票类型不能为空',
          votingStartTime: '开始时间不能为空',
          votingEndTime: '结束时间不能为空',
          endTimeAfterStart: '结束时间必须晚于开始时间',
          atLeastOneOption: '至少需要一个选项',
          optionNotEmpty: '选项不能为空'
        },
        addSuccess: '新增成功',
        addFailed: '新增失败'
      },
      edit: {
        title: '修改投票',
        save: '保存',
        cancel: '取消',
        updateSuccess: '修改成功',
        updateFailed: '修改失败'
      },
      delete: {
        title: '请确认您的操作',
        confirm: '确定删除投票吗？',
        success: '删除成功',
        failed: '删除失败'
      },
      detail: {
        title: '投票结果',
        votingName: '投票名称',
        votingType: '投票类型',
        votingStartTime: '开始时间',
        votingEndTime: '结束时间',
        optionTitle: '选项',
        optionValue: '票数',
        optionPercent: '占比',
        total: '总票数',
        fetchError: '获取投票结果失败'
      },
      validate: {
        votingName: '投票名称不能为空',
        votingType: '投票类型不能为空',
        votingStartTime: '开始时间不能为空',
        votingEndTime: '结束时间不能为空',
        endTimeAfterStart: '结束时间必须晚于开始时间',
        atLeastOneOption: '至少需要一个选项',
        optionNotEmpty: '选项不能为空'
      },
      fetchError: '获取投票列表失败'
    }
  },
  'zh-TW': {
    ownerVoting: {
      title: '業主投票',
      search: {
        title: '查詢條件',
        qaId: '請輸入投票編號',
        qaName: '請輸入投票名稱'
      },
      list: {
        title: '投票列表'
      },
      table: {
        qaName: '投票名稱',
        validity: '有效期',
        type: '類型',
        options: '選項',
        peopleChose: '人選擇',
        voteCount: '投票數',
        state: '狀態',
        createTime: '創建時間'
      },
      state: {
        drafting: '草稿',
        publishing: '已發布',
        deleted: '已刪除',
        published: '已發布',
        unpublished: '未發布'
      },
      type: {
        agree: '同意',
        agreeAndDisagree: '同意/反對',
        multi: '多選',
        single: '單選',
        multiple: '多選'
      },
      operation: {
        edit: '修改',
        delete: '刪除',
        publish: '發布',
        detail: '投票結果',
        result: '投票結果'
      },
      add: {
        title: '新增投票',
        votingName: '投票名稱',
        votingNamePlaceholder: '必填，請填寫投票名稱',
        votingType: '投票類型',
        selectVotingType: '請選擇投票類型',
        votingStartTime: '開始時間',
        selectStartTime: '必填，請選擇開始時間',
        votingEndTime: '結束時間',
        selectEndTime: '必填，請選擇結束時間',
        votingDesc: '投票說明',
        votingDescPlaceholder: '請輸入投票說明',
        optionTitle: '投票選項',
        addOption: '添加選項',
        optionPlaceholder: '請輸入選項',
        save: '保存',
        cancel: '取消',
        validate: {
          votingName: '投票名稱不能為空',
          votingType: '投票類型不能為空',
          votingStartTime: '開始時間不能為空',
          votingEndTime: '結束時間不能為空',
          endTimeAfterStart: '結束時間必須晚於開始時間',
          atLeastOneOption: '至少需要一個選項',
          optionNotEmpty: '選項不能為空'
        },
        addSuccess: '新增成功',
        addFailed: '新增失敗'
      },
      edit: {
        title: '修改投票',
        save: '保存',
        cancel: '取消',
        updateSuccess: '修改成功',
        updateFailed: '修改失敗'
      },
      delete: {
        title: '請確認您的操作',
        confirm: '確定刪除投票嗎？',
        success: '刪除成功',
        failed: '刪除失敗'
      },
      detail: {
        title: '投票結果',
        votingName: '投票名稱',
        votingType: '投票類型',
        votingStartTime: '開始時間',
        votingEndTime: '結束時間',
        optionTitle: '選項',
        optionValue: '票數',
        optionPercent: '佔比',
        total: '總票數',
        fetchError: '獲取投票結果失敗'
      },
      validate: {
        votingName: '投票名稱不能為空',
        votingType: '投票類型不能為空',
        votingStartTime: '開始時間不能為空',
        votingEndTime: '結束時間不能為空',
        endTimeAfterStart: '結束時間必須晚於開始時間',
        atLeastOneOption: '至少需要一個選項',
        optionNotEmpty: '選項不能為空'
      },
      fetchError: '獲取投票列表失敗'
    }
  },
};