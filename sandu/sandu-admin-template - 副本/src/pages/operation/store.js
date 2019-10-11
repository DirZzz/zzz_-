import Vue from "vue";
import Vuex from "vuex";
import test from "./store/modules/test";
import content from "./store/modules/content";
import exchangeArea from "./store/modules/exchangeArea";
import common from "./store/index";
Vue.use(Vuex);

export default new Vuex.Store({
  // strict: process.env.NODE_ENV !='dev',
  ...common,
  modules: {
    test,
    content,
    exchangeArea
  }
});
