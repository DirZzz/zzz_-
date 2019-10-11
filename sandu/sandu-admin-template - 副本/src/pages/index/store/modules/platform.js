import { OneParentsName,twoParentsName,constantRouterMapOne,constantRouterMaptwo } from '@/router'

const platform = {
  state: {
    platform:'',  // 平台
    addRouters: [], // 动态路由
    parentsName:{}, // 平台左测菜单最顶层
    hasRouters:false, // 判断是否有动态路由
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
    },
    SET_PARENTSNAME: (state, parentsName) => {
      state.parentsName = parentsName
    },
    SET_PLATFORM: (state, platform) => {
      state.platform = platform
    },
    SET_HASROUTERS: (state, hasRouters) => {
      state.hasRouters = hasRouters
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {  // 获取动态路由
      return new Promise(resolve => {  // 根据data平台选择不同路由数据，
        if(data=="one"){
          let Routers= constantRouterMapOne,
              parentsName=OneParentsName
              commit('SET_ROUTERS', Routers) // 给store下的 addRouters 赋值
              commit('SET_PARENTSNAME',parentsName) // 设置顶级菜单名称 ， 因为顶级菜单没有路由功能。

        }else if(data=="two"){
          let Routers= constantRouterMaptwo,
              parentsName=twoParentsName
              commit('SET_ROUTERS', Routers)
              commit('SET_PARENTSNAME',parentsName)
        }
    
        resolve()
      })
    },
    clearName({commit},data){
        commit('SET_PLATFORM', data)
    },
    SetRouters({
      commit
    }){

      return new Promise((resolve, reject) => {
         commit('SET_HASROUTERS',true)  // 设置已经增加动态路由，
        resolve()
      })
     

    },
  }
}

export default platform
