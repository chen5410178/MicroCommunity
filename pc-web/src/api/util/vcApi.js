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
export function initSystem() {
    var _hmt = _hmt || [];
    (function () {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?adedae9bc250561cc17e96dc1fb46079";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
}
