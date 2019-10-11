const menus = {
    state: {
      menus: '', // 平台
    },
    mutations: {
      SET_MENUS: (state, menus) => {
        state.menus = menus
      },
    },
    actions: {
      SetMenus({
        commit
      }, data) {

          commit('SET_MENUS', data) // 设置已经增加动态路由，

  },
}
}

export default menus
