import request from '@/utils/request'

export function getStaffInfo(params) {
  return request({
    url: '/user.queryAdminPropertyStaff',
    method: 'get',
    params
  })
}

export function getStaffPrivileges(params) {
  return request({
    url: '/user.queryAdminStaffPrivilege',
    method: 'get',
    params
  })
}

export function getStaffOrgs(params) {
  return request({
    url: '/user.listAdminStaffOrgs',
    method: 'get',
    params
  })
}

export function getStaffRoles(params) {
  return request({
    url: '/user.listAdminStaffRoles',
    method: 'get',
    params
  })
}

export function listStaffScheduleClasses(params) {
  return request({
    url: '/scheduleClasses.staffAdminMonthScheduleClasses',
    method: 'get',
    params
  })
}

export function queryAdminAttendanceClasses(params) {
  return request({
    url: '/attendanceClasses.queryAdminAttendanceClasses',
    method: 'get',
    params
  })
}

export function queryAttendanceLog(params) {
  return request({
    url: '/attendanceClass/queryAttendanceLog',
    method: 'get',
    params
  })
}

export function listAdminUndoRepairsByStaff(params) {
  return request({
    url: '/ownerRepair.listAdminUndoRepairsByStaff',
    method: 'get',
    params
  })
}

export function listAdminFinishRepairsByStaff(params) {
  return request({
    url: '/ownerRepair.listAdminFinishRepairsByStaff',
    method: 'get',
    params
  })
}

export function listAdminInspectionTaskDetails(params) {
  return request({
    url: '/inspectionTaskDetail.listAdminInspectionTaskDetails',
    method: 'get',
    params
  })
}

export function listAdminMaintainanceTaskDetail(params) {
  return request({
    url: '/maintainanceTask.listAdminMaintainanceTaskDetail',
    method: 'get',
    params
  })
}

export function listAdminPurchaseApplys(params) {
  return request({
    url: '/purchaseApply.listAdminPurchaseApplys',
    method: 'get',
    params
  })
}

export function queryAdminFeeDetail(params) {
  return request({
    url: '/fee.queryAdminFeeDetail',
    method: 'get',
    params
  })
}

export function listAdminReturnPayFees(params) {
  return request({
    url: '/returnPayFee.listAdminReturnPayFees',
    method: 'get',
    params
  })
}


export function listAdminContracts(params) {
  return request({
    url: '/contract.queryAdminContract',
    method: 'get',
    params
  })
}

export function listAdminAppAuths(params) {
  return request({
    url: '/user.queryAdminStaffAppAuth',
    method: 'get',
    params
  })
}

export function listAdminAccessControls(params) {
  return request({
    url: '/machineTranslate.listAdminMachineTranslates',
    method: 'get',
    params
  })
}