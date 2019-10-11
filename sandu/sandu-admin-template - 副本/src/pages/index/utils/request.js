import qs from "qs";
import axios from "axios";
import { Message } from "element-ui";
import router from "../router";
let basePath = process.env;
let platformCode = "merchantManage";
axios.defaults.headers["Content-Type"] = "application/json";


axios.interceptors.request.use(
    config => {
        config.headers["Platform-Code"] = sessionStorage.getItem("platformCode") || platformCode;
        const loginUser = qs.parse(sessionStorage.getItem("loginUser"));
        if (loginUser) {
            config.headers["Authorization"] = loginUser.token;
        } else {
            router.replace("/login");
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    res => {
        if (
            res.request.responseURL.indexOf("getLoginUserAdminPlatform") > -1 &&
            res.data.obj
        ) {
            for (let i = 0; i < res.data.obj.length; i++) {
                let item = res.data.obj[i];
                if (item.platformCode == "merchantManage") {
                    platformCode = "merchantManage";
                    sessionStorage.setItem("platformCode", platformCode);
                    break;
                } else if (item.platformCode == "sanduManagerNew") {
                    platformCode = "sanduManagerNew";
                    sessionStorage.setItem("platformCode", platformCode);
                    break;
                } else if (item.platformCode == "operationPlatform") {
                    platformCode = "operationPlatform";
                    sessionStorage.setItem("platformCode", platformCode);
                    break;
                } else {
                    Message({ type: "error", message: "未开通权限" });
                    return;
                }
            }
        }
        return res;
    },
    error => {
        return Promise.reject(error);
    }
);

export function fetch(url, params, type, isNeedUserId) {
    //isNeedUserId 是否需要userId
    if (!isNeedUserId &&
        params instanceof Object &&
        sessionStorage.getItem("loginUser")
    ) {
        params.userId = qs.parse(sessionStorage.getItem("loginUser")).id;
    }
    axios.defaults.baseURL = basePath.baseUrl;
    return new Promise((resolve, reject) => {
        axios({
            method: type,
            url: url,
            params: type === "get" || type === "delete" ? params : "",
            data: params,
            withCredentials: true
        }).then(
            response => {
                if (response.data.code >= 400) {
                    Message({ type: "error", message: response.data.message });
                    resolve(response.data);
                }
                resolve(response.data);
            },
            err => {
                Message.error("网络繁忙");
                console.log(url);
                reject(err);
            }
        );
    });
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
export function exportData(url, params, type) {
    return new Promise((resolve, reject) => {
        axios({
            method: type,
            url: url, // 请求地址
            params: type === "get" || type === "delete" ? params : "",
            data: params, // 参数
            responseType: "blob", // 表明返回服务器返回的数据类型
            headers: {
                "Content-Type": "application/json"
            }
        }).then(
            response => {
                if (response.data.code >= 400) {
                    Message({ type: "error", message: response.data.message });
                    resolve(response.data);
                }
                resolve(response.data);
            },
            err => {
                Message.error("网络繁忙");
                console.log(url);
                reject(err);
            }
        );
    });
}