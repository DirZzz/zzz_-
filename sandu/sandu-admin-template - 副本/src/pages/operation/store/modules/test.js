const test = {
    state: {
        testList:'testList'
    },
    getters: {
        testTestList:state=>state.testList,
    },
    mutations: {
        SET_TESTMODULE:(state,data)=>{
            state.testList=data;
        }
    },
    actions: {
        testModule({commit}, data) {
            commit('SET_TESTMODULE', data)
        }
    }
};
export default test

//getters 命名统一   模块名开头test***
//actions 命名统一  模块名开头 test***