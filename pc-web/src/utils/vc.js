export function setCommunitys(_communityInfos){
    localStorage.setItem('hc_communityInfos', JSON.stringify(_communityInfos));
}

export function getCommunitys () {
    return JSON.parse(window.localStorage.getItem('hc_communityInfos'));
}

export function clearCacheData () {
    window.localStorage.clear();
    window.sessionStorage.clear();
}

export function setCurrentCommunity (_currentCommunityInfo) {
    window.localStorage.setItem('hc_currentCommunityInfo', JSON.stringify(_currentCommunityInfo));
}

export function getCurrentCommunity() {
    let _community = JSON.parse(window.localStorage.getItem('hc_currentCommunityInfo'));
    if (!_community) {
        return {
            communityId: '-1',
            communityName: '未知'
        };
    }
    return _community;
}

export function getCommunityId(){
    let _community = getCurrentCommunity();
    if(!_community){
        return "";
    }
    return _community.communityId
}

/**
* Deep copy a JavaScript object.
* @param {Object} srcObj - The source object to be deep copied.
* @param {Object} targetObj - The target object to merge the copied properties into.
* @returns {Object} A deep copy of the object or the merged target object.
*/
export function deepCopy(srcObj, targetObj = {}) {
    if (srcObj === null || typeof srcObj !== 'object') {
        return srcObj;
    }
    let copy = targetObj || (Array.isArray(srcObj) ? [] : {});
    for (let key in srcObj) {
        if (Object.prototype.hasOwnProperty.call(srcObj, key)) {
            copy[key] = deepCopy(srcObj[key], copy[key]);
        }
    }
    return copy;
}

/**
 * Check if a value is a valid date.
 * @param {*} value - The value to check.
 * @returns {boolean} True if the value is a valid date, false otherwise.
 */
export function isDate(value) {
    return value instanceof Date && !isNaN(value.getTime());
}

export function copyObject (org, dst) {

    if (!org || !dst) {
        return;
    }
    //for(key in Object.getOwnPropertyNames(dst)){
    for (let key in dst) {
        if (Object.prototype.hasOwnProperty.call(org, key)) {
            dst[key] = org[key]
        }
    }
}