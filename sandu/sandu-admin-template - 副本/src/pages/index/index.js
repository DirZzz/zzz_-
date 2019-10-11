import Vue from 'vue';
import 'babel-polyfill'
import App from './App';
import router from './router';
import store from './store';

import "./assets/css/index" //样式box
import "./plugins/index"   //公共插件及组件box
import "./utils/prototype" //挂载在vue实例上的box

Vue.config.productionTip = false;

router.beforeEach((to, from, next) => {
    let user = sessionStorage.getItem('loginUser');
    if (from.path == '/') {
        next();
    }else{
        if (from.path) {
            localStorage.setItem('from', from.path)
            next()
        }
    }
    if ((!user || user.length === 0) && to.path !== '/') {
        next({ path: '/' })
    } else {
        let platform = sessionStorage.getItem('platform') || 'one';
        if (!store.getters.hasRouters) {
            // 判断是否有动态路由， 如果没有就把SetRouters 设置为true, 下次进来时不走此方法
            store.dispatch('SetRouters').then(res => {
                store.dispatch('GenerateRoutes',platform).then(res => {  // 获取动态路由 数组，
                    let addRoutes=store.getters.addRouters;
                    router.addRoutes(addRoutes)
                    next(to.path)
                })
            })
        } else {
            next()
        }
    }
})

new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: {
        App
    }
})