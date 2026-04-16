export function getDateYYYYMMDDHHMISS (){
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    if (month < 10) {
        month = '0' + month;
    }
    if (day < 10) {
        day = '0' + day;
    }
    if (hour < 10) {
        hour = '0' + hour;
    }
    if (minute < 10) {
        minute = '0' + minute;
    }
    if (second < 10) {
        second = '0' + second;
    }
    return year + "" + month + "" + day + "" + hour + "" + minute + "" + second;
}

export function getDateStr() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    if (month < 10) {
        month = '0' + month;
    }
    if (day < 10) {
        day = '0' + day;
    }
    if (hour < 10) {
        hour = '0' + hour;
    }
    if (minute < 10) {
        minute = '0' + minute;
    }
    if (second < 10) {
        second = '0' + second;
    }
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
}

export function getDateYYYYMMDD() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (day < 10) {
        day = '0' + day;
    }
    return year + "-" + month + "-" + day;
}

export function add0(m) {
    return m < 10 ? '0' + m : m
}

export function dateFormat (_time) {
    let _date = new Date(_time);
    let y = _date.getFullYear();
    let m = _date.getMonth() + 1;
    let d = _date.getDate();
    return y + '-' + add0(m) + '-' + add0(d);
}

export function dateFormatH (_time) {
    let _date = new Date(_time);
    let y = _date.getFullYear();
    let m = _date.getMonth() + 1;
    let d = _date.getDate();
    return y + '-' + add0(m) + '-' + add0(d) ;
}

export function timeFormat (_time) {
    let _date = new Date(_time);
    let h = _date.getHours();
    let mm = _date.getMinutes();
    let s = _date.getSeconds();
    return add0(h) + ':' + add0(mm) + ':' + add0(s);
}
export function timeMinFormat (_time) {
    let _date = new Date(_time);
    let h = _date.getHours();
    let mm = _date.getMinutes();
    return add0(h) + ':' + add0(mm);
}

export function dateSubOneDay (_startTime, _endTime, feeFlag) {
    if (!_endTime || _endTime == '-') {
        return _endTime
    }
    let dateTime = new Date(_endTime);
    let startTime = new Date(_startTime);
    //如果开始时间是31日 结束时间是30日 不做处理
    let _startTimeLastDay = startTime.getDate();
    let _endTimeLastDay = dateTime.getDate();
    if (_startTimeLastDay == 31 && _endTimeLastDay == 30) {
        return dateFormat(dateTime);
    }

    //2月份特殊处理
    let _endTimeMonth = dateTime.getMonth();
    if (_endTimeMonth == 1 && _endTimeLastDay > 26 && _startTimeLastDay > 26) {
        return dateFormat(dateTime);
    }

    if (feeFlag != "2006012") {
        dateTime = dateTime.setDate(dateTime.getDate() - 1);
    }
    dateTime =  dateFormat(dateTime)
    return dateTime;
}

export function dateSub (dateTime, feeFlag) {
    if (!dateTime || dateTime == '-') {
        return dateTime
    }
    console.log("feeFlag:" + feeFlag);
    dateTime = new Date(dateTime);
    if (feeFlag != "2006012") {
        dateTime = dateTime.setDate(dateTime.getDate() - 1);
    }
    dateTime = dateFormat(dateTime)
    return dateTime;
}
export function dateAdd (dateTime) {
    if (!dateTime || dateTime == '-') {
        return dateTime
    }
    dateTime = new Date(dateTime);
    dateTime = dateTime.setDate(dateTime.getDate() + 1);
    dateTime = dateFormat(dateTime)
    return dateTime;
}

export function addOneDay (date) {
    // 将给定的日期转换为Date对象
    let currentDate = new Date(date);
    // 获取当前日期的时间戳
    let timestamp = currentDate.getTime();
    // 将时间戳加上一天的毫秒数（24小时 * 60分钟 * 60秒 * 1000毫秒）
    timestamp += 24 * 60 * 60 * 1000;
    // 根据新的时间戳创建一个新的Date对象
    const newDate = new Date(timestamp);
    // 返回新的日期对象
    return newDate;
}

export function daysInMonth (year, month) {
    if (month == 1) {
        if (year % 4 == 0 && year % 100 != 0)
            return 29;
        else
            return 28;
    } else if ((month <= 6 && month % 2 == 0) || (month = 6 && month % 2 == 1))
        return 31;
    else
        return 30;
}
export function addMonth (_date, _month) {
    let y = _date.getFullYear();
    let m = _date.getMonth();
    let nextY = y;
    let nextM = m;
    //如果当前月+要加上的月>11 这里之所以用11是因为 js的月份从0开始
    if ((m + _month) > 11) {
        nextY = y + 1;
        nextM = parseInt(m + _month) - 12;
    } else {
        nextM = m + _month
    }
    let daysInNextMonth = daysInMonth(nextY, nextM);
    let day = _date.getDate();
    if (day > daysInNextMonth) {
        day = daysInNextMonth;
    }
    let _newDate = new Date(nextY, nextM, day)
    return _newDate.getFullYear() + '-' + (_newDate.getMonth() + 1) + '-' + _newDate.getDate() + " " + _date.getHours() + ":" + _date.getMinutes() + ":" + _date.getSeconds();
}