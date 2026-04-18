import { messages as communitySettingManageMessages } from '../views/system/communitySettingManageLang'
import { messages as storeInfoManageMessages } from '../views/system/storeInfoManageLang'
import { messages as publicWeChatManageMessages } from '../views/system/publicWeChatManageLang'
import { messages as smallWeChatManageMessages } from '../views/system/smallWeChatManageLang'
import { messages as paymentPoolMessages } from '../views/system/paymentPoolLang'
import { messages as operateDataLogMessages } from '../views/system/operateDataLogLang'
import { messages as feePrintSpecManageMessages } from '../views/system/feePrintSpecManageLang'
import { messages as feePrintPageManageMessages } from '../views/system/feePrintPageManageLang'
import { messages as downloadTempFileMessages } from '../views/system/downloadTempFileLang'
import { messages as assetImportLogMessages } from '../views/system/assetImportLogLang'
import { messages as assetImportLogDetailMessages } from '../views/system/assetImportLogDetailLang'
import { messages as payFeeQrcodeMessages } from '../views/fee/payFeeQrcodeLang'
import { messages as menuUserManageMessages } from '../views/system/menuUserManageLang'

export const messages = {
    en: {
        ...communitySettingManageMessages.en,
        ...storeInfoManageMessages.en,
        ...publicWeChatManageMessages.en,
        ...smallWeChatManageMessages.en,
        ...paymentPoolMessages.en,
        ...operateDataLogMessages.en,
        ...feePrintSpecManageMessages.en,
        ...feePrintPageManageMessages.en,
        ...downloadTempFileMessages.en,
        ...assetImportLogMessages.en,
        ...assetImportLogDetailMessages.en,
        ...payFeeQrcodeMessages.en,
        ...menuUserManageMessages.en,
    },
    zh: {
        ...communitySettingManageMessages.zh,
        ...storeInfoManageMessages.zh,
        ...publicWeChatManageMessages.zh,
        ...smallWeChatManageMessages.zh,
        ...paymentPoolMessages.zh,
        ...operateDataLogMessages.zh,
        ...feePrintSpecManageMessages.zh,
        ...feePrintPageManageMessages.zh,
        ...downloadTempFileMessages.zh,
        ...assetImportLogMessages.zh,
        ...assetImportLogDetailMessages.zh,
        ...payFeeQrcodeMessages.zh,
...menuUserManageMessages.zh,
    },
    'zh-TW': {
        ...communitySettingManageMessages['zh-TW'],
        ...storeInfoManageMessages['zh-TW'],
        ...publicWeChatManageMessages['zh-TW'],
        ...smallWeChatManageMessages['zh-TW'],
        ...paymentPoolMessages['zh-TW'],
        ...operateDataLogMessages['zh-TW'],
        ...feePrintSpecManageMessages['zh-TW'],
        ...feePrintPageManageMessages['zh-TW'],
        ...downloadTempFileMessages['zh-TW'],
        ...assetImportLogMessages['zh-TW'],
        ...assetImportLogDetailMessages['zh-TW'],
        ...payFeeQrcodeMessages['zh-TW'],
        ...menuUserManageMessages['zh-TW'],
    }
}