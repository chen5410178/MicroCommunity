import { setCommunitys,setCurrentCommunity,getCurrentCommunity } from "@/utils/vc"
import request from '@/utils/request'
export function _loadCommunityInfo(_param) {
    return new Promise((resolve, reject) => {
        request({
            url: '/community.listMyEnteredCommunitys',
            method: 'get',
            params: {
                page: 1,
                row: 3
            }
        }).then(response => {
            const res = response.data
            let _communityInfos = res.communitys;
            if (_communityInfos && _communityInfos.length > 0) {
                setCommunitys(_communityInfos);
                let _currentCommunity = JSON.parse(window.localStorage.getItem('hc_currentCommunityInfo'));
                if(_currentCommunity){
                    resolve(res)
                    return ;
                }

                setCurrentCommunity(_communityInfos[0]);
                if (_param && _param.communityId) {
                    let _communityId = _param.communityId;
                    _communityInfos.forEach(_c => {
                        if (_c.communityId == _communityId) {
                            setCurrentCommunity(_c);
                        }
                    })
                }
            }
            // 获取验证码成功
            resolve(res)
        }).catch(error => {
            reject(error)
        })
    })
}

export function getDict(tableName,tableColumns) {
    return new Promise((resolve, reject) => {
        request({
            url: '/callComponent/core/list',
            method: 'get',
            params: {
                name: tableName,
                type: tableColumns,
                page:1,
                row:500
            }
        }).then(response => {
            const res = response.data
            // 获取验证码成功
            resolve(res)
        }).catch(error => {
            reject(error)
        })
    })
}

export function getCommunityId() {
    return getCurrentCommunity().communityId
}



export function getCommunity() {
    return getCurrentCommunity()
}



export function getCommunityName() {
    return getCurrentCommunity().name
}

export function getMyEnteredCommunitys(_param) {
    return new Promise((resolve, reject) => {
        request({
            url: '/community.listMyEnteredCommunitys',
            method: 'get',
            params: {
                ..._param
            }
        }).then(response => {
            const res = response.data
            // 获取验证码成功
            resolve(res)
        }).catch(error => {
            reject(error)
        })
    })
}