const content = {
  state: {
    type: "",
    isShowPopups: false,
    isAdd: false,
    popupData: {},
    triggerGetBannerList: 0
  },
  getters: {},
  mutations: {
    content_setType: (state, data) => {
      state.type = data;
    },
    content_togglePopups: (state, data) => {
      state.isShowPopups = !state.isShowPopups;
    },
    content_setIsAdd: (state, data) => {
      state.isAdd = data;
    },
    content_setPopupData: (state, data) => {
      state.popupData = data;
    },
    content_triggerGetBannerList: (state, data) => {
      state.triggerGetBannerList++;
    }
  }
};
export default content;
