import { messages as machineTranslateManageMessages } from '../views/machine/machineTranslateManageLang'
import { messages as accessControlInoutMessages } from '../views/machine/accessControlInoutLang'
import { messages as machinePrinterManageMessages } from '../views/machine/machinePrinterManageLang'
import { messages as printerRuleMessages } from '../views/machine/printerRuleLang'
import { messages as machineTypeTreeManageMessages } from '../views/machine/machineTypeTreeManageLang'
import { messages as equipmentAccountMessages } from '../views/machine/equipmentAccountLang'
import { messages as addEquipmentAccountMessages } from '../views/machine/addEquipmentAccountLang'
import { messages as editEquipmentAccountMessages } from '../views/machine/editEquipmentAccountLang'
import { messages as equipmentAccountDetailMessages } from '../views/machine/equipmentAccountDetailLang'
import { messages as videoControlMessages } from '../views/machine/videoControlLang'
export const messages ={
    en:{
        ...machineTypeTreeManageMessages.en,
        ...equipmentAccountMessages.en,
        ...addEquipmentAccountMessages.en,
        ...editEquipmentAccountMessages.en,
        ...equipmentAccountDetailMessages.en,
        ...machineTranslateManageMessages.en,
        ...accessControlInoutMessages.en,
        ...machinePrinterManageMessages.en,
        ...printerRuleMessages.en,
        ...videoControlMessages.en,
    },
    zh:{    
        ...machineTypeTreeManageMessages.zh,
        ...equipmentAccountMessages.zh,
        ...addEquipmentAccountMessages.zh,
        ...editEquipmentAccountMessages.zh,
        ...equipmentAccountDetailMessages.zh,
        ...machineTranslateManageMessages.zh,   
        ...accessControlInoutMessages.zh, 
        ...machinePrinterManageMessages.zh,
        ...printerRuleMessages.zh,
        ...videoControlMessages.zh,
    }
}