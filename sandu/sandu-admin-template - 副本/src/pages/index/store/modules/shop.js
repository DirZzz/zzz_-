const shop = {
  state: {
    checkPlan: "",
    paramsData: null
  },
  mutations: {
    SET_checkPlan: (state, data) => {
      state.checkPlan = data;
    },
    SET_paramsData: (state, data) => {
      state.paramsData = data;
    }
  }
};

export default shop;
