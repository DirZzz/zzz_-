import Vue from 'vue'
import Vuex from 'vuex'
import common from './store/index'
import userManage from './store/modules/userManage'
Vue.use(Vuex);

export default new Vuex.Store({
    modules:{
        userManage
    },
    ...common,
})
