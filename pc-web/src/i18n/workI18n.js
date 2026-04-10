import { messages as itemReleaseTypeManageMessages } from '../views/work/itemReleaseTypeManageLang'
import { messages as itemReleaseManageMessages } from '../views/work/itemReleaseManageLang'

import { messages as addItemReleaseViewMessages } from '../views/work/addItemReleaseViewLang'
import { messages as editItemReleaseViewMessages } from '../views/work/editItemReleaseViewLang'
import { messages as itemReleaseDetailMessages } from '../views/work/itemReleaseDetailLang'
import { messages as printRepairDetailMessages } from '../views/work/printRepairDetailLang'
import { messages as finishRepairMessages } from '../views/work/finishRepairLang'
import { messages as addRoomRepairMessages } from '../views/work/addRoomRepairLang'
export const messages = {
    zh: {
        ...itemReleaseTypeManageMessages.zh,
        ...itemReleaseManageMessages.zh,
        ...addItemReleaseViewMessages.zh,
        ...editItemReleaseViewMessages.zh,
        ...itemReleaseDetailMessages.zh,
        ...printRepairDetailMessages.zh,
        ...finishRepairMessages.zh,
        ...addRoomRepairMessages.zh,
    },
    en: {
        ...itemReleaseTypeManageMessages.en,
        ...itemReleaseManageMessages.en,
        ...addItemReleaseViewMessages.en,
        ...editItemReleaseViewMessages.en,
        ...itemReleaseDetailMessages.en,
        ...printRepairDetailMessages.en,
        ...finishRepairMessages.en,
        ...addRoomRepairMessages.en,
    }
}