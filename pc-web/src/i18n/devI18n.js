import { messages as appMessages } from '../views/dev/app/appLang'
import { messages as serviceMessages } from '../views/dev/serviceLang'
import { messages as routeMessages } from '../views/dev/routeLang'
import { messages as addRouteMessages } from '../views/dev/addRouteLang'
import { messages as serviceBusinessMessages } from '../views/dev/serviceBusinessLang'
import { messages as businessTableHisMessages } from '../views/dev/businessTableHisLang'
import { messages as menuCatalogMessages } from '../views/dev/menuCatalogLang'
import { messages as menuGroupCatalogMessages } from '../views/dev/menuGroupCatalogLang'
import { messages as menuGroupMessages } from '../views/dev/menuGroupLang'
import { messages as mappingDomainMessages } from '../views/dev/mappingDomainLang'
import { messages as mappingMessages } from '../views/dev/mappingLang'
import { messages as businessDatabusMessages } from '../views/dev/businessDatabusLang'
import { messages as dictMessages } from '../views/dev/dictLang'
import { messages as cacheMessages } from '../views/dev/cacheLang'
import { messages as menuMessages } from '../views/dev/menuLang'
import { messages as privilegeMessages } from '../views/dev/privilegeLang'
import { messages as taskMessages } from '../views/dev/taskLang'
import { messages as attrSpecMessages } from '../views/dev/attrSpecLang'
import { messages as attrValueMessages } from '../views/dev/attrValueLang'
import { messages as serviceProvideManageMessages } from '../views/dev/serviceProvideManageLang'
import { messages as devServiceProvideMessages } from '../views/dev/devServiceProvideLang'

export const messages = {
    en: {
        ...appMessages.en,
        ...serviceMessages.en,
        ...routeMessages.en,
        ...addRouteMessages.en,
        ...serviceBusinessMessages.en,
        ...businessTableHisMessages.en,
        ...menuCatalogMessages.en,
        ...menuGroupCatalogMessages.en,
        ...menuGroupMessages.en,
        ...mappingDomainMessages.en,
        ...mappingMessages.en,
        ...businessDatabusMessages.en,
        ...dictMessages.en,
        ...cacheMessages.en,
        ...menuMessages.en,
        ...privilegeMessages.en,
        ...taskMessages.en,
        ...attrSpecMessages.en,
        ...attrValueMessages.en,
        ...serviceProvideManageMessages.en,
        ...devServiceProvideMessages.en,
    },
    zh: {
        ...appMessages.zh,
        ...serviceMessages.zh,
        ...routeMessages.zh,
        ...addRouteMessages.zh,
        ...serviceBusinessMessages.zh,
        ...businessTableHisMessages.zh,
        ...menuCatalogMessages.zh,
        ...menuGroupCatalogMessages.zh,
        ...menuGroupMessages.zh,
        ...mappingDomainMessages.zh,
        ...mappingMessages.zh,
        ...businessDatabusMessages.zh,
        ...dictMessages.zh,
        ...cacheMessages.zh,
        ...menuMessages.zh,
        ...privilegeMessages.zh,
        ...taskMessages.zh,
        ...attrSpecMessages.zh,
        ...attrValueMessages.zh,
        ...serviceProvideManageMessages.zh,
        ...devServiceProvideMessages.zh,
    }
}