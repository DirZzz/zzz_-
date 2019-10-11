const exchangeArea = {
  state: {
    checkPlan: "",
    formData: null,
    commentsDate: {}
  },
  mutations: {
    SET_checkPlan: (state, data) => {
      state.checkPlan = data;
    },
    SET_formData: (state, data) => {
      state.formData = data;
    },
    SET_commentsDate: (state, data) => {
      state.commentsDate = data;
    }
  }
};

export default exchangeArea;
