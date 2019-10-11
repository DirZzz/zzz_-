const SET_RIGHT_SIDE = "SET_RIGHT_SIDE";
export default {
  //namespaced:true,
  state: {
    rightSide: "", //
    routerIndex: sessionStorage.getItem("currentIndex") || 0
  },
  getters: {
    getRightSide: state => state.rightSide
  },
  mutations: {
    [SET_RIGHT_SIDE](state, data) {
      state.rightSide = data;
    },
    setRouterIndex(state, data) {
      sessionStorage.setItem("currentIndex", data);
      state.routerIndex = data;
    }
  },
  actions: {
    setRightSide({ commit }, data) {
      commit("SET_RIGHT_SIDE", data);
    }
  }
};
