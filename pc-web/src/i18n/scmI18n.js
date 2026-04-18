import { messages as couponMarketMessages } from '../views/scm/couponMarketLang'
import { messages as couponPropertyPoolManageMessages } from '../views/scm/couponPropertyPoolManageLang'
import { messages as couponRuleMessages } from '../views/scm/couponRuleLang'
import { messages as couponPropertyPoolDetailMessages } from '../views/scm/couponPropertyPoolDetailLang'
import { messages as couponPropertyUserManageMessages } from '../views/scm/couponPropertyUserManageLang'
import { messages as couponPropertyUserDetailMessages } from '../views/scm/couponPropertyUserDetailLang'
import { messages as integralConfigManageMessages } from '../views/scm/integralConfigManageLang'
import { messages as integralRuleMessages } from '../views/scm/integralRuleLang'
import { messages as integralGiftDetailManageMessages } from '../views/scm/integralGiftDetailManageLang'
import { messages as communityIntegralMessages } from '../views/scm/communityIntegralLang'
import { messages as goldMessages } from '../views/scm/goldLang'

export const messages = {
    en: {

        ...couponMarketMessages.en,
        ...couponPropertyPoolManageMessages.en,
        ...couponRuleMessages.en,
        ...couponPropertyPoolDetailMessages.en,
        ...couponPropertyUserManageMessages.en,
        ...couponPropertyUserDetailMessages.en,
        ...integralConfigManageMessages.en,
        ...integralRuleMessages.en,
        ...integralGiftDetailManageMessages.en,
        ...communityIntegralMessages.en,
        ...goldMessages.en,
    },  
    zh: {
        ...couponMarketMessages.zh,
        ...couponPropertyPoolManageMessages.zh,
        ...couponRuleMessages.zh,
        ...couponPropertyPoolDetailMessages.zh,
        ...couponPropertyUserManageMessages.zh,
        ...couponPropertyUserDetailMessages.zh,
        ...integralConfigManageMessages.zh,
        ...integralRuleMessages.zh,
        ...integralGiftDetailManageMessages.zh,
        ...communityIntegralMessages.zh,
...goldMessages.zh,
    },
    'zh-TW': {
        ...couponMarketMessages['zh-TW'],
        ...couponPropertyPoolManageMessages['zh-TW'],
        ...couponRuleMessages['zh-TW'],
        ...couponPropertyPoolDetailMessages['zh-TW'],
        ...couponPropertyUserManageMessages['zh-TW'],
        ...couponPropertyUserDetailMessages['zh-TW'],
        ...integralConfigManageMessages['zh-TW'],
        ...integralRuleMessages['zh-TW'],
        ...integralGiftDetailManageMessages['zh-TW'],
        ...communityIntegralMessages['zh-TW'],
        ...goldMessages['zh-TW'],
    }
}