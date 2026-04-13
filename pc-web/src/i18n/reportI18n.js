import { messages as reportCustomManageMessages } from '../views/report/reportCustomManageLang'
import { messages as reportCustomComponentManageMessages } from '../views/report/reportCustomComponentManageLang'

import { messages as reportCustomComponentRelManageMessages } from '../views/report/reportCustomComponentRelManageLang'
import { messages as componentConditionManageMessages } from '../views/report/componentConditionManageLang'
import { messages as reportCustomComponentFooterManageMessages } from '../views/report/reportCustomComponentFooterManageLang'

import { messages as reportFeeSummaryMessages } from '../views/report/reportFeeSummaryLang'
import { messages as reportFeeDetailMessages } from '../views/report/reportFeeDetailLang'
import { messages as dataReportMessages } from '../views/report/dataReportLang'
import { messages as reportOweFeeDetailMessages } from '../views/report/reportOweFeeDetailLang'
import { messages as reportPayFeeDetailMessages } from '../views/report/reportPayFeeDetailLang'
import { messages as reportRepairMessages } from '../views/report/reportRepairLang'
import { messages as reportNoFeeRoomMessages } from '../views/report/reportNoFeeRoomLang'
import { messages as reportHuaningMessages } from '../views/report/reportHuaningLang'
import { messages as reportPayFeeDepositMessages } from '../views/report/reportPayFeeDepositLang'
import { messages as commonReportMessages } from '../views/report/commonReportLang'
import { messages as printCommonReportTableMessages } from '../views/report/printCommonReportTableLang'

export const messages = {
    en: {

        ...reportCustomComponentRelManageMessages.en,
        ...componentConditionManageMessages.en,
        ...reportCustomComponentFooterManageMessages.en,
        ...reportCustomManageMessages.en,
        ...reportCustomComponentManageMessages.en,
        ...reportFeeSummaryMessages.en,
        ...reportFeeDetailMessages.en,
        ...dataReportMessages.en,
        ...reportOweFeeDetailMessages.en,
        ...reportPayFeeDetailMessages.en,
        ...reportRepairMessages.en,
        ...reportNoFeeRoomMessages.en,
        ...reportHuaningMessages.en,
        ...reportPayFeeDepositMessages.en,
        ...commonReportMessages.en,
        ...printCommonReportTableMessages.en,
    },
    zh: {
        ...reportCustomComponentRelManageMessages.zh,
        ...componentConditionManageMessages.zh,
        ...reportCustomComponentFooterManageMessages.zh,
        ...reportCustomManageMessages.zh,
        ...reportCustomComponentManageMessages.zh,
        ...reportFeeSummaryMessages.zh,
        ...reportFeeDetailMessages.zh,
        ...dataReportMessages.zh,
        ...reportOweFeeDetailMessages.zh,
        ...reportPayFeeDetailMessages.zh,
        ...reportRepairMessages.zh,
        ...reportNoFeeRoomMessages.zh,
        ...reportHuaningMessages.zh,
        ...reportPayFeeDepositMessages.zh,
        ...commonReportMessages.zh,
        ...printCommonReportTableMessages.zh,
    }
}