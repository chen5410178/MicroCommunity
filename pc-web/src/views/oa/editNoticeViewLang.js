export const messages = {
  en: {
    editNoticeView: {
      title: 'Edit Notice',
      titleLabel: 'Title',
      titlePlaceholder: 'Required, please enter title',
      contentLabel: 'Content',
      contentPlaceholder: 'Required, please enter notice content',
      validate: {
        titleRequired: 'Title cannot be empty',
        titleLength: 'Title must be between 4 to 100 characters',
        contentRequired: 'Content cannot be empty',
        contentMaxLength: 'Content cannot exceed 10000 characters',
        noticeIdRequired: 'Notice ID cannot be empty'
      },
      fetchError: 'Failed to fetch notice details',
      updateSuccess: 'Notice updated successfully',
      updateError: 'Failed to update notice'
    }
  },
  zh: {
    editNoticeView: {
      title: '修改公告',
      titleLabel: '标题',
      titlePlaceholder: '必填，请填写标题',
      contentLabel: '公告内容',
      contentPlaceholder: '必填，请输入公告内容',
      validate: {
        titleRequired: '标题不能为空',
        titleLength: '标题必须在4至100字符之间',
        contentRequired: '公告内容不能为空',
        contentMaxLength: '公告内容不能超过10000个字',
        noticeIdRequired: '公告ID不能为空'
      },
      fetchError: '获取公告详情失败',
      updateSuccess: '公告修改成功',
      updateError: '公告修改失败'
    }
  }
}