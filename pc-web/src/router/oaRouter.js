export default [
    {
        path: '/pages/property/activitiesTypeManage',
        meta: { title: '活动类型' },
        name: '/pages/property/activitiesTypeManage',
        component: () => import('@/views/oa/activitiesTypeManageList.vue')
    },
    {
        path: '/pages/property/activitiesManage',
        meta: { title: '活动管理' },
        name: '/pages/property/activitiesManage',
        component: () => import('@/views/oa/activitiesManageList.vue')
    },
    {
        path: '/pages/common/noticeManage',
        meta: { title: '公告管理' },
        name: '/pages/common/noticeManage',
        component: () => import('@/views/oa/noticeManageList.vue')
    },
    {
        path: '/views/oa/addNoticeView',
        meta: { title: '添加公告' },
        name: '/views/oa/addNoticeView',
        component: () => import('@/views/oa/addNoticeViewList.vue')
    },
    {
        path: '/views/oa/editNoticeView',
        meta: { title: '编辑公告' },
        name: '/views/oa/editNoticeView',
        component: () => import('@/views/oa/editNoticeViewList.vue')
    },
    {
        path: '/views/oa/noticeDetail',
        meta: { title: '公告详情' },
        name: '/views/oa/noticeDetail',
        component: () => import('@/views/oa/noticeDetailList.vue')
    },
    {
        path: '/pages/complaint/complaintType',
        meta: { title: '投诉类型' },
        name: '/pages/complaint/complaintType',
        component: () => import('@/views/oa/complaintTypeList.vue')
    },
    {
        path: '/views/oa/addComplaintType',
        meta: { title: '添加投诉类型' },
        name: '/views/oa/addComplaintType',
        component: () => import('@/views/oa/addComplaintTypeList.vue')
    },
    {
        path: '/views/oa/editComplaintType',
        meta: { title: '编辑投诉类型' },
        name: '/views/oa/editComplaintType',
        component: () => import('@/views/oa/editComplaintTypeList.vue')
    },
    {
        path: '/views/oa/addComplaint',
        meta: { title: '添加投诉' },
        name: '/views/oa/addComplaint',
        component: () => import('@/views/oa/addComplaintList.vue')
    },
    {
        path: '/pages/complaint/complaint',
        meta: { title: '投诉管理' },
        name: '/pages/complaint/complaint',
        component: () => import('@/views/oa/complaintList.vue')
    },
    {
        path: '/pages/property/notepadManage',
        meta: { title: '便签管理' },
        name: '/pages/property/notepadManage',
        component: () => import('@/views/oa/notepadManageList.vue')
    },
    {
        path: '/pages/property/visitManage',
        meta: { title: '来访管理' },
        name: '/pages/property/visitManage',
        component: () => import('@/views/oa/visitManageList.vue')
    },
    {
        path: '/pages/question/questionTitle',
        meta: { title: '问卷标题' },
        name: '/pages/question/questionTitle',
        component: () => import('@/views/oa/questionTitleList.vue')
    },
    {
        path: '/pages/property/questionAnswerManage',
        meta: { title: '问卷管理' },
        name: '/pages/property/questionAnswerManage',
        component: () => import('@/views/oa/questionAnswerManageList.vue')
    },
    {
        path: '/views/oa/addQuestionAnswer',
        meta: { title: '添加问卷' },
        name: '/views/oa/addQuestionAnswer',
        component: () => import('@/views/oa/addQuestionAnswerList.vue')
    },
    {
        path: '/views/oa/editQuestionAnswer',
        meta: { title: '编辑问卷' },
        name: '/views/oa/editQuestionAnswer',
        component: () => import('@/views/oa/editQuestionAnswerList.vue')
    },
    {
        path: '/pages/question/ownerVoting',
        meta: { title: '业主投票' },
        name: '/pages/question/ownerVoting',
        component: () => import('@/views/oa/ownerVotingList.vue')
    },
    {
        path: '/views/oa/addOwnerVoting',
        meta: { title: '添加投票' },
        name: '/views/oa/addOwnerVoting',
        component: () => import('@/views/oa/addOwnerVotingList.vue')
    },
    {
        path: '/views/oa/editOwnerVoting',
        meta: { title: '编辑投票' },
        name: '/views/oa/editOwnerVoting',
        component: () => import('@/views/oa/editOwnerVotingList.vue')
    },
    {
        path: '/pages/property/oaWorkflowManage',
        meta: { title: '工作流' },
        name: '/pages/property/oaWorkflowManage',
        component: () => import('@/views/oa/oaWorkflowManageList.vue')
    },
    {
        path: '/pages/property/newOaWorkflowManage',
        meta: { title: '新工作流' },
        name: '/pages/property/newOaWorkflowManage',
        component: () => import('@/views/oa/newOaWorkflowManageList.vue')
    },
    {
        path: '/pages/property/newOaWorkflowDoing',
        meta: { title: '工作流进行中' },
        name: '/pages/property/newOaWorkflowDoing',
        component: () => import('@/views/oa/newOaWorkflowDoingList.vue')
    },
    {
        path: '/pages/property/attendanceClassesManage',
        meta: { title: '考勤班次' },
        name: '/pages/property/attendanceClassesManage',
        component: () => import('@/views/oa/attendanceClassesManageList.vue')
    },
    {
        path: '/pages/attendance/attendanceClassesStaffManage',
        meta: { title: '考勤班次员工' },
        name: '/pages/attendance/attendanceClassesStaffManage',
        component: () => import('@/views/oa/attendanceClassesStaffManageList.vue')
    },
    {
        path: '/pages/property/todayAttendanceManage',
        meta: { title: '今日考勤' },
        name: '/pages/property/todayAttendanceManage',
        component: () => import('@/views/oa/todayAttendanceManageList.vue')
    },
    {
        path: '/views/oa/addAttendanceClassesStaff',
        meta: { title: '添加考勤员工' },
        name: '/views/oa/addAttendanceClassesStaff',
        component: () => import('@/views/oa/addAttendanceClassesStaffList.vue')
    },
    {
        path: '/pages/property/monthAttendanceManage',
        meta: { title: '月考勤' },
        name: '/pages/property/monthAttendanceManage',
        component: () => import('@/views/oa/monthAttendanceManageList.vue')
    },
    {
        path: '/pages/property/staffAttendanceManage',
        meta: { title: '员工考勤' },
        name: '/pages/property/staffAttendanceManage',
        component: () => import('@/views/oa/staffAttendanceManageList.vue')
    },
    {
        path: '/pages/property/attendanceLogManage',
        meta: { title: '考勤记录' },
        name: '/pages/property/attendanceLogManage',
        component: () => import('@/views/oa/attendanceLogManageList.vue')
    },
    {
        path: '/pages/oa/workType',
        meta: { title: '工种' },
        name: '/pages/oa/workType',
        component: () => import('@/views/oa/workTypeList.vue')
    },
    {
        path: '/pages/oa/startWork',
        meta: { title: '开始工作' },
        name: '/pages/oa/startWork',
        component: () => import('@/views/oa/startWorkList.vue')
    },
    {
        path: '/views/oa/addWork',
        meta: { title: '添加工作' },
        name: '/views/oa/addWork',
        component: () => import('@/views/oa/addWorkList.vue')
    },
    {
        path: '/views/oa/editWork',
        meta: { title: '编辑工作' },
        name: '/views/oa/editWork',
        component: () => import('@/views/oa/editWorkList.vue')
    },
    {
        path: '/views/oa/workDetail',
        meta: { title: '工作详情' },
        name: '/views/oa/workDetail',
        component: () => import('@/views/oa/workDetailList.vue')
    },
    {
        path: '/pages/oa/doWork',
        meta: { title: '执行工作' },
        name: '/pages/oa/doWork',
        component: () => import('@/views/oa/doWorkList.vue')
    },
    {
        path: '/pages/oa/copyWork',
        meta: { title: '复制工作' },
        name: '/pages/oa/copyWork',
        component: () => import('@/views/oa/copyWorkList.vue')
    },
    {
        path: '/pages/oa/workPool',
        meta: { title: '工作池' },
        name: '/pages/oa/workPool',
        component: () => import('@/views/oa/workPoolList.vue')
    },
    {
        path: '/pages/oa/workDeduction',
        meta: { title: '工作扣款' },
        name: '/pages/oa/workDeduction',
        component: () => import('@/views/oa/workDeductionList.vue')
    },
    {
        path: '/views/oa/newOaWorkflow',
        meta: { title: '新工作流' },
        name: '/views/oa/newOaWorkflow',
        component: () => import('@/views/oa/newOaWorkflowList.vue')
    },
    {
        path: '/views/oa/newOaWorkflowDetail',
        meta: { title: '工作流详情' },
        name: '/views/oa/newOaWorkflowDetail',
        component: () => import('@/views/oa/newOaWorkflowDetailList.vue')
    },
    {
        path: '/views/oa/newOaWorkflowFormEdit',
        meta: { title: '工作流编辑' },
        name: '/views/oa/newOaWorkflowFormEdit',
        component: () => import('@/views/oa/newOaWorkflowFormEditList.vue')
    },
    {
        path: '/pages/property/simplifyNotepadManage',
        meta: { title: '简化便签' },
        name: '/pages/property/simplifyNotepadManage',
        component: () => import('@/views/oa/simplifyNotepadManageList.vue')
    },
    {
        path: '/pages/complaint/uodoComplaints',
        meta: { title: '待处理投诉' },
        name: '/pages/complaint/uodoComplaints',
        component: () => import('@/views/oa/uodoComplaintsList.vue')
    },
    {
        path: '/pages/complaint/complaintDetail',
        meta: { title: '投诉详情' },
        name: '/pages/complaint/complaintDetail',
        component: () => import('@/views/oa/complaintDetailList.vue')
    },
    {
        path: '/pages/property/visitUndo',
        meta: { title: '待处理来访' },
        name: '/pages/property/visitUndo',
        component: () => import('@/views/oa/visitUndoList.vue')
    },
    {
        path: '/pages/complaint/doHistoryComplaints',
        meta: { title: '投诉历史' },
        name: '/pages/complaint/doHistoryComplaints',
        component: () => import('@/views/oa/doHistoryComplaintsList.vue')
    },
    {
        path: '/pages/admin/myAuditHistoryOrders',
        meta: { title: '审核历史' },
        name: '/pages/admin/myAuditHistoryOrders',
        component: () => import('@/views/resource/myAuditHistoryOrdersList.vue')
    },
    {
        path: '/pages/property/visitFinish',
        meta: { title: '已完成来访' },
        name: '/pages/property/visitFinish',
        component: () => import('@/views/oa/visitFinishList.vue')
    },
    {
        path: '/pages/oa/workTaskDetail',
        meta: { title: '工作任务详情' },
        name: '/pages/oa/workTaskDetail',
        component: () => import('@/views/oa/workTaskDetailList.vue')
    },

    {
        path: '/views/oa/doCopyWork',
        meta: { title: '执行复制工作' },
        name: '/views/oa/doCopyWork',
        component: () => import('@/views/oa/doCopyWork.vue')
    }
]