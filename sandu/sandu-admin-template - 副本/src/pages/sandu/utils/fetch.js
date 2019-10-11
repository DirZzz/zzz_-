import axios from 'axios'
import { Message } from 'element-ui'
import qs from 'qs'

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'; //标识这是一个 ajax 请求
axios.defaults.headers['Content-Type'] = 'application/json';
axios.defaults.baseURL = process.env.baseUrl;
const Token = qs.parse(sessionStorage.getItem('loginUser')).token;
axios.interceptors.request.use(config => {
    const platformCode = sessionStorage.getItem('platformCode');
    if (Token !== undefined) {
        config.headers['Authorization'] = Token;
        config.headers['Platform-Code'] = platformCode;
    }
    return config;
}, err => {
    Message.error({ message: '网络繁忙!' });
    return Promise.resolve(err);
})



axios.interceptors.response.use(data => {
    return data;
}, err => {
    return Promise.resolve(err);
})


export function exportData(url, params, type) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'get',
            url: url, // 请求地址
            params: (type === 'get' || type === 'delete') ? params : '',
            data: params, // 参数
            responseType: 'blob', // 表明返回服务器返回的数据类型
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.data.code >= 400) {
                Message({ type: 'error', message: response.data.message });
                resolve(response.data)
            }
            resolve(response.data)
        }, err => {
            Message.error('网络繁忙')
            console.log(url);
            reject(err)
        })
    })

}
export function formPostExportdata(methodType, url, params, responseType) {
    let totalParams = {
        method: methodType,
        url: url,
        data: methodType === 'post' || methodType === 'put' ? params : null,
        params: methodType === 'get' || methodType === 'delete' ? params : null,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        transformRequest: [function (data) {
            return qs.stringify(data);
        }],
        responseType: 'blob', // 表明返回服务器返回的数据类型
        withCredentials: true //配置允许跨域携带cookie
    };
    return new Promise((resolve, reject) => {
        axios(totalParams).then(response => {
            resolve(response.data)
        }, err => {
            reject(err);
        })
    })
}
export function exportDataPost(url, params, type) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: url, // 请求地址
            params: (type === 'get' || type === 'delete') ? params : '',
            data: params, // 参数
            responseType: 'blob', // 表明返回服务器返回的数据类型
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.data.code >= 400) {
                Message({ type: 'error', message: response.data.message });
                resolve(response.data)
            }
            resolve(response.data)
        }, err => {
            Message.error('网络繁忙')
            console.log(url);
            reject(err)
        })
    })
}
export function exportDataPostByForm(url, params, type) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: url, // 请求地址
            params: (type === 'get' || type === 'delete') ? params : '',
            data: params, // 参数
            responseType: 'blob', // 表明返回服务器返回的数据类型
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(response => {
            if (response.data.code >= 400) {
                Message({ type: 'error', message: response.data.message });
                resolve(response.data)
            }
            resolve(response.data)
        }, err => {
            Message.error('网络繁忙')
            console.log(url);
            reject(err)
        })
    })
}

export function fetch(methodType, url, params) {
    let totalParams = {
        method: methodType,
        url: url,
        //timeout: 10000,
        data: methodType === 'post' || methodType === 'put' ? params : null,
        params: methodType === 'get' || methodType === 'delete' ? params : null,
        withCredentials: true  //配置允许跨域携带cookie
    }
    return new Promise((resolve, reject) => {
        axios(totalParams).then(response => {
            // alert(response)
            resolve(response.data)
        }, err => {
            reject(err);
        })
    })
}
export function formPost(methodType, url, params, responseType) {
    let totalParams = {
        method: methodType,
        url: url,
        data: methodType === 'post' || methodType === 'put' ? params : null,
        params: methodType === 'get' || methodType === 'delete' ? params : null,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        transformRequest: [function (data) {
            return qs.stringify(data);
        }],
        responseType: responseType, // 表明返回服务器返回的数据类型
        withCredentials: true  //配置允许跨域携带cookie
    };
    return new Promise((resolve, reject) => {
        axios(totalParams).then(response => {
            resolve(response.data)
        }, err => {
            reject(err);
        })
    })
}