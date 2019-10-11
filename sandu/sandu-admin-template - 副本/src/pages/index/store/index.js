import Vue from 'vue'
import Vuex from 'vuex'
import platform from './modules/platform'
import getters from './getters'
import product from './modules/product'
import packageBuy from './modules/package'
import hardProduct from './modules/hardProduct'
import textture from './modules/textture'
import menus from './modules/menus'
import shop from './modules/shop'
import homeManage from './modules/homeManage'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
      product,
      platform,
      packageBuy,
      hardProduct,
      homeManage,
      textture,
      menus,
      shop
  },
  getters
})

export default store
