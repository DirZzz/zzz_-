const mutations = {
  // 新增模型
  newModel (state, newModel) {
    state.newMode = newModel;
  },
  modelListShow (state, modelListShow) {
    state.modelListShow = modelListShow;
  },
  breadcrumb1 (state, breadcrumb1) { // 一级面包宵
    state.breadcrumb1 = breadcrumb1;// localStorage.setItem('breadcrumb1', breadcrumb1);
  },
  breadcrumb2 (state, breadcrumb2) { // 二级面包宵
    state.breadcrumb2 = breadcrumb2; // localStorage.setItem('breadcrumb2', breadcrumb2);
  },
  breadcrumb3 (state, breadcrumb3) { // 三级面包宵
    state.breadcrumb3 = breadcrumb3; //localStorage.setItem('breadcrumb3', breadcrumb3);
  },
  breadcrumb4 (state, breadcrumb4) { // 三级面包宵
    state.breadcrumb4 = breadcrumb4; // localStorage.setItem('breadcrumb4', breadcrumb4);
  },
  getProductId (state, productId) { // 产品id
    state.productId = productId;
  },
  tabChange (state, activeName) {
    state.activeName = activeName
  }
};
export default mutations;
