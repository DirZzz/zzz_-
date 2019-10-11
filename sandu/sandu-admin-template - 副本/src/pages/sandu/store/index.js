const SET_RIGHT_SIDE='SET_RIGHT_SIDE';
export default {
    namespaced:true,
    state: {
        rightSide:'' //
    },
    getters:{
        getRightSide: state=> state.rightSide
    },
    mutations: {
        [SET_RIGHT_SIDE](state,data){
            state.rightSide=data;
        }
    },
    actions:{
       setRightSide({commit},data){
           commit('SET_RIGHT_SIDE',data);
       }
    }
}