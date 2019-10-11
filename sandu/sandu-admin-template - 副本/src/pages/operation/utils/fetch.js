import axios from 'axios'
import {Message} from 'element-ui'
import qs from 'qs'

// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'; //标识这是一个 ajax 请求
axios.defaults.headers['Content-Type'] = 'application/json';
axios.defaults.baseURL = process.env.baseUrl;
const Token = qs.parse(sessionStorage.getItem('loginUser')).token;
axios.interceptors.request.use(config => {
    const platformCode=sessionStorage.getItem('platformCode');
    if (Token !== undefined) {
        config.headers['Authorization'] = Token;
        config.headers['Platform-Code'] = platformCode;
    }
    return config;
}, err=> {
    Message.error({message: '网络繁忙!'});
    return Promise.resolve(err);
})



axios.interceptors.response.use(data=> {
    return data;
}, err=> {
    return Promise.resolve(err);
})

export function fetch(methodType,url,params){
    let totalParams={
        method:methodType,
        url: url,
        data: methodType === 'post' || methodType === 'put' ? params : null,
        params: methodType === 'get' || methodType === 'delete' ? params : null,
        withCredentials:true  //配置允许跨域携带cookie
    };
    return new Promise((resolve,reject) => {
        axios(totalParams).then(response =>{
            resolve(response.data)
        },err =>{
            reject(err);
        })
    })
}
export function formPost(methodType,url,params){
    let totalParams={
        method:methodType,
        url: url,
        data: methodType === 'post' || methodType === 'put' ? params : null,
        params: methodType === 'get' || methodType === 'delete' ? params : null,
        headers:{
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        withCredentials:true  //配置允许跨域携带cookie
    };
    return new Promise((resolve,reject) => {
        axios(totalParams).then(response =>{
            resolve(response.data)
        },err =>{
            reject(err);
        })
    })
}

export function exportData(methodType,url, params) {
    return new Promise((resolve, reject) => {
        axios({
            method: methodType,
            url: url, // 请求地址
            params: (methodType === 'get' || methodType === 'delete') ? params : '',
            data: params, // 参数
            responseType: 'blob', // 表明返回服务器返回的数据类型
            headers: {
                'Content-methodType': 'application/json'
            }
        }).then(response => {
            if (response.data.code >= 400) {
                Message({methodType: 'error', message: response.data.message});
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
