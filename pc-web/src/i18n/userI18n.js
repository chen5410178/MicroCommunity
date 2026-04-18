import { messages as staffCommunityMessages } from '../views/staff/staffCommunityLang'
import { messages as staffAppAuthManageMessages } from '../views/staff/staffAppAuthManageLang'
import { messages as dataPrivilegeManageMessages } from '../views/org/dataPrivilegeManageLang'
import { messages as classesManageMessages } from '../views/org/classesManageLang'
import { messages as scheduleClassesMessages } from '../views/org/scheduleClassesLang'
import { messages as addScheduleClassesMessages } from '../views/org/addScheduleClassesLang'
import { messages as editScheduleClassesMessages } from '../views/org/editScheduleClassesLang'
import { messages as scheduleClassesPageMessages } from '../views/org/scheduleClassesPageLang'
import { messages as workflowManageMessages } from '../views/system/workflowManageLang'
import { messages as workflowSettingManageMessages } from '../views/system/workflowSettingManageLang'
import { messages as ownerDetailMessages } from '../views/owner/ownerDetailLang'
import { messages as auditAuthOwnerUndoMessages } from '../views/owner/auditAuthOwnerUndoLang'
import { messages as scheduleClassesStaffManageMessages } from '../views/org/scheduleClassesStaffManageLang'

export const messages = {
    en: {
        ...staffCommunityMessages.en,
        ...staffAppAuthManageMessages.en,
        ...dataPrivilegeManageMessages.en,
        ...classesManageMessages.en,
        ...scheduleClassesMessages.en,  
        ...addScheduleClassesMessages.en,
        ...editScheduleClassesMessages.en,
        ...scheduleClassesPageMessages.en,
        ...workflowManageMessages.en,
        ...workflowSettingManageMessages.en,
        ...ownerDetailMessages.en,
        ...auditAuthOwnerUndoMessages.en,
        ...scheduleClassesStaffManageMessages.en,
    },
    zh: {
        ...staffCommunityMessages.zh,
        ...staffAppAuthManageMessages.zh,
        ...dataPrivilegeManageMessages.zh,
        ...classesManageMessages.zh,
        ...scheduleClassesMessages.zh,
        ...addScheduleClassesMessages.zh,
        ...editScheduleClassesMessages.zh,
        ...scheduleClassesPageMessages.zh,
        ...workflowManageMessages.zh,
        ...workflowSettingManageMessages.zh,
        ...ownerDetailMessages.zh,
        ...auditAuthOwnerUndoMessages.zh,
...scheduleClassesStaffManageMessages.zh,
    },
    'zh-TW': {
        ...staffCommunityMessages['zh-TW'],
        ...staffAppAuthManageMessages['zh-TW'],
        ...dataPrivilegeManageMessages['zh-TW'],
        ...classesManageMessages['zh-TW'],
        ...scheduleClassesMessages['zh-TW'],
        ...addScheduleClassesMessages['zh-TW'],
        ...editScheduleClassesMessages['zh-TW'],
        ...scheduleClassesPageMessages['zh-TW'],
        ...workflowManageMessages['zh-TW'],
        ...workflowSettingManageMessages['zh-TW'],
        ...ownerDetailMessages['zh-TW'],
        ...auditAuthOwnerUndoMessages['zh-TW'],
        ...scheduleClassesStaffManageMessages['zh-TW'],
    }
}