export const messages = {
  en: {
    addNoticeView: {
      title: 'New Notice',
      titleLabel: 'Title',
      titlePlaceholder: 'Required, please fill in the title',
      noticeTypeLabel: 'Notice Type',
      noticeTypePlaceholder: 'Required, please select notice type',
      noticeTypeOption1: 'Owner Notification (WeChat notification to followers)',
      noticeTypeOption2: 'Staff Notification',
      contentLabel: 'Notice Content',
      contentPlaceholder: 'Required, please enter notice content',
      submit: 'Submit',
      cancel: 'Cancel',
      validate: {
        title: 'Title cannot be empty',
        noticeType: 'Notice type cannot be empty',
        content: 'Notice content cannot be empty'
      },
      saveSuccess: 'Added successfully',
      saveError: 'Failed to add notice'
    }
  },
  zh: {
    addNoticeView: {
      title: '新公告',
      titleLabel: '标题',
      titlePlaceholder: '必填，请填写标题',
      noticeTypeLabel: '公告类型',
      noticeTypePlaceholder: '必填，请选择公告类型',
      noticeTypeOption1: '业主通知(微信通知关注用户)',
      noticeTypeOption2: '员工通知',
      contentLabel: '公告内容',
      contentPlaceholder: '必填，请输入公告内容',
      submit: '提交',
      cancel: '取消',
      validate: {
        title: '标题不能为空',
        noticeType: '公告类型不能为空',
        content: '公告内容不能为空'
      },
      saveSuccess: '添加成功',
      saveError: '添加公告失败'
    }
  }
}