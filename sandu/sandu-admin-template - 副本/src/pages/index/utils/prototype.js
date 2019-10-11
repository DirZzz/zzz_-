import Vue from 'vue';
import api from '../api/api.js';
import newApi from '../api/newApi.js';
import mallApi from '../api/imall.js'; //积分商城的api
import printJS from 'print-js';
import { Loading } from 'element-ui';
import permiss from '../permissions';
import global from '../utils/global'// 外包部分 积分商城相关


Vue.prototype.isMainPer = permiss;
Vue.prototype.API = api;
Vue.prototype.API2 = newApi;
Vue.prototype.mallApi = mallApi;
Vue.prototype.BASE_URL = process.env;
Vue.prototype.printData = printJS;
Vue.prototype.Loading = Loading;
Vue.prototype.GLOBAL = global;