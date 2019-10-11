export function formatDate(date, fmt) {//时间戳转换成 字符串形式的方法
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
    };
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + '';
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
        }
    }
    return fmt;
};

function padLeftZero(str) {
    return ('00' + str).substr(str.length);
};

export function PointTwo(str) { //判断两位小数点的方法
    if (str !== parseInt(str) ) {
        let i = /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
        return i.test(str);
    } else {
        let i = /^[1-9]\d*$/;
        return i.test(str);
    }
}

export function PointNum(str) { //判断是否正整数的方法
    let i = /^[1-9]\d*$/;
    return i.test(str);
}


export function PointZero(str) { //可以等于0判断两位小数点的方法
    if ( str !== parseInt(str)) {
        let i = /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
        return i.test(str);
    } else {
        let i = /^[0-9]\d*$/;
        return i.test(str);
    }
}
/* 手机号码*/
export function validateAlphone(str) {
    const reg = /^1[3|4|5|6|7|8][0-9]\d{4,8}$/;
    return reg.test(str);
  }