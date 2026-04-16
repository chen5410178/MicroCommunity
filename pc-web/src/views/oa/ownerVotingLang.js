export const messages = {
  en: {
    ownerVoting: {
      search: {
        title: 'Search Conditions',
        qaId: 'Please enter voting ID',
        qaName: 'Please enter voting name'
      },
      list: {
        title: 'Voting Information'
      },
      table: {
        qaName: 'Voting Name',
        validity: 'Validity Period',
        type: 'Type',
        options: 'Options',
        voteCount: 'Total Votes/Voted',
        state: 'Status',
        createTime: 'Create Time',
        operation: 'Operation',
        peopleChose: ' people chose'
      },
      type: {
        single: 'Single Choice',
        multiple: 'Multiple Choice'
      },
      state: {
        published: 'Published',
        unpublished: 'Unpublished'
      },
      operation: {
        result: 'Result',
        publish: 'Publish'
      },
      add: {
        title: 'Add Voting',
        success: 'Add voting successfully',
        error: 'Failed to add voting'
      },
      edit: {
        title: 'Edit Voting',
        noId: 'No voting ID provided',
        fetchError: 'Failed to get voting details'
      },
      delete: {
        title: 'Delete Voting',
        confirm: 'Are you sure to delete this voting?',
        success: 'Delete voting successfully',
        error: 'Failed to delete voting'
      },
      deleteQuestionAnswer: {
        title: 'Delete Questionnaire',
        confirm: 'Are you sure to delete this questionnaire?',
        success: 'Delete questionnaire successfully',
        error: 'Failed to delete questionnaire'
      },
      publish: {
        title: 'Publish Voting',
        name: 'Name',
        tip: 'Tips',
        tipContent: 'After publishing, it cannot be modified again. Please confirm before publishing!',
        notifyWay: 'Notification Method',
        notifyWayPlaceholder: 'Required, please select notification method',
        sms: 'SMS',
        wechat: 'WeChat Message',
        noNotify: 'No Notification',
        notifyWayRequired: 'Notification method is required',
        publishBtn: 'Publish',
        success: 'Publish successfully',
        error: 'Failed to publish'
      },
      form: {
        qaName: 'Voting Name',
        qaNamePlaceholder: 'Please enter voting name',
        type: 'Type',
        typePlaceholder: 'Please select type',
        startTime: 'Start Time',
        startTimePlaceholder: 'Please select start time',
        endTime: 'End Time',
        endTimePlaceholder: 'Please select end time',
        options: 'Options',
        optionPlaceholder: 'Option {num}',
        addOption: 'Add Option'
      },
      validate: {
        qaName: 'Voting name is required',
        type: 'Type is required',
        startTime: 'Start time is required',
        endTime: 'End time is required',
        endTimeAfterStart: 'End time must be after start time',
        atLeastOneOption: 'At least one option is required',
        optionNotEmpty: 'Option cannot be empty'
      },
      fetchError: 'Failed to get voting list'
    }
  },
  zh: {
    ownerVoting: {
      search: {
        title: '查询条件',
        qaId: '请输入投票ID',
        qaName: '请输入投票名称'
      },
      list: {
        title: '投票信息'
      },
      table: {
        qaName: '投票名称',
        validity: '有效期',
        type: '类型',
        options: '选项',
        voteCount: '总投票/已投票',
        state: '状态',
        createTime: '创建时间',
        operation: '操作',
        peopleChose: '人选择'
      },
      type: {
        single: '单选',
        multiple: '多选'
      },
      state: {
        published: '已发布',
        unpublished: '待发布'
      },
      operation: {
        result: '结果',
        publish: '发布'
      },
      add: {
        title: '添加投票',
        success: '添加投票成功',
        error: '添加投票失败'
      },
      edit: {
        title: '编辑投票',
        noId: '未提供投票ID',
        fetchError: '获取投票详情失败'
      },
      delete: {
        title: '删除投票',
        confirm: '确定删除该投票吗？',
        success: '删除投票成功',
        error: '删除投票失败'
      },
      deleteQuestionAnswer: {
        title: '删除问卷',
        confirm: '确定删除该问卷吗？',
        success: '删除问卷成功',
        error: '删除问卷失败'
      },
      publish: {
        title: '发布投票',
        name: '名称',
        tip: '温馨提示',
        tipContent: '发布后将无法再次修改，请确认无误后再发布！',
        notifyWay: '通知方式',
        notifyWayPlaceholder: '必填，请选择通知方式',
        sms: '短信',
        wechat: '微信消息',
        noNotify: '不通知',
        notifyWayRequired: '通知方式不能为空',
        publishBtn: '发布',
        success: '发布成功',
        error: '发布失败'
      },
      form: {
        qaName: '投票名称',
        qaNamePlaceholder: '请输入投票名称',
        type: '类型',
        typePlaceholder: '请选择类型',
        startTime: '开始时间',
        startTimePlaceholder: '请选择开始时间',
        endTime: '结束时间',
        endTimePlaceholder: '请选择结束时间',
        options: '选项',
        optionPlaceholder: '选项{num}',
        addOption: '添加选项'
      },
      validate: {
        qaName: '投票名称不能为空',
        type: '类型不能为空',
        startTime: '开始时间不能为空',
        endTime: '结束时间不能为空',
        endTimeAfterStart: '结束时间必须晚于开始时间',
        atLeastOneOption: '至少需要一个选项',
        optionNotEmpty: '选项不能为空'
      },
      fetchError: '获取投票列表失败'
    }
  }
}