import Vue from 'vue'
import 'babel-polyfill';
import App from './App.vue'
import router from './router'
import store from './store'
import storage from './utils/storage'
import API from './api/index.js'
import './assets/css/common.scss'
import './assets/css/element.scss'
import permission from './permission'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/css/reset.scss'
import components from './components/index' //全局组件注册
import filters from './filters/index' //全局过滤器
for (let key in filters){
    Vue.filter(key,filters[key]);
}
Vue.use(components)
Vue.use(ElementUI);
Vue.prototype.API=API;
Vue.prototype.BASE_URL = process.env;
Vue.prototype.permission=permission;
Vue.config.productionTip = false;
Vue.prototype.storage=storage; //

//集成Vue-echarts插件
import ECharts from 'vue-echarts' // 在 webpack 环境下指向 components/ECharts.vue
// 手动引入 ECharts 各模块来减小打包体积
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
// 注册组件后即可使用
Vue.component('v-chart', ECharts);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
