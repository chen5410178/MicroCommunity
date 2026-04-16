import { getUuid } from '@/utils/commonUtil'
import { getDateYYYYMMDDHHMISS } from '@/utils/dateUtil'
import config from '@/conf/config'

export function getHeader() {
    // 这里可以添加token等认证信息
    const token = localStorage.getItem('token')
    let authorization = ""
    if (token) {
        authorization = `Bearer ${token}`
    }
    return {
        Authorization: authorization, // Adjust as needed
        'app-id': config.appId,
        'TRANSACTION-ID': getUuid(),
        'REQ-TIME': getDateYYYYMMDDHHMISS(),
        'SIGN': '',
        'user-id': '-1'
    }
}