const userManage = {
    namespaced: true,
    state: {
        oldCategoryIds: [], //企业可见范围选中的id;
        flag: false, //点击确定后的企业可见范围选中的id
        treeTagName: '',
        produceRangeData: '', //企业可见范围分类数据
        userManageTabList: [], //用户列表的面包屑
        userMangeAdd: false, //用户新增弹窗
        userManageBatchAdd: false, //用户批量新增弹窗
        packageEditDialog: false, //套餐修改弹窗
        packagePayDialog: false, //套餐修改弹窗
        isNewAdd: null, //是否是从 1新增  2.批量新增  进入设置套餐
        userMethod: '', //
        serviceInfoList: '', // 套餐列表
        packageData: '', //每个列表续传的数据
        payType: 4, //  //4升级修改  0购买
        serviceInfo: '', //新增 批量新增  设置套餐 保存的信息
        userTypelist: [],
        packageEditFrom: 0, // 默认0 ，为1表示批量设置套餐进入
        packageEditFromType: 1, // 默认1 ，为1表示用戶列表进入，2表示廠商用戶列表進入，3表示經銷商用戶列表進入，4表示公司列表進入
        batchStopTime:''//批量暂停时间
    },
    getters: {

    },
    mutations: {
        ['SET_PRODUCE_RANGE'](state, data) {
            state.produceRangeData = data;
        },
        ['SET_OLD_CATEGORYID'](state, data) {
            state.oldCategoryIds = data;
        },
        ['SET_FLAG'](state, data) {
            state.flag = data;
        },
        ['SET_TAB_LIST'](state, data) {
            state.userManageTabList = data;
        },
        ['SET_DIALOG'](state, data) {
            state.userMangeAdd = data.userMangeAdd;
            state.userManageBatchAdd = data.userManageBatchAdd;
            state.packageEditDialog = data.packageEditDialog;
            state.packagePayDialog = data.packagePayDialog;
            state.stateServicesName = data.name;
            state.statePid = data.pid;
            state.packageEditFrom = data.packageEditFrom;
            state.packageEditFromType = data.packageEditFromType
        },
        ['SET_USER_METHOD'](state, data) {
            state.userMethod = data;
        },
        ['SET_SERVICE_LIST'](state, data) {
            if (state.userMethod && state.userMethod == 'vendor') {
                data.item.userType = 2;
            } else if (state.userMethod && state.userMethod == 'franchiser') {
                data.item.userType = 3;
            }
            if (data.item.fromType == 0) {
                data.vm.API.matchPackage({ userScope: data.item.userType, saleChannel: 2 }).then(res => {
                    setValue(res);
                })
            } else {
                data.vm.API.selectListAllService({
                    page: 1,
                    limit: 20,
                    companyId: data.item.companyId,
                    saleChannel: 2,
                    userScope: data.item.userType
                }).then(res => {
                    setValue(res);
                })
            }

            function setValue(res) {
                if (res.success) {
                    state.serviceInfoList = (state.packageData.servicesId || state.isNewAdd == 4) ? res.datalist.filter(res => { return res.servicesId != state.packageData.servicesId }) : res.datalist
                    state.packageEditDialog = true;

                    if (state.stateServicesName) {
                        res.datalist.map((v, i) => {
                            if (v.servicesName == state.stateServicesName) {
                                res.datalist[i].choosed = (v.servicesId);
                                if (state.statePid) {
                                    v.priceList.map((h, j) => {
                                        if (h.id == state.statePid) {
                                            res.datalist[i].priceList[j].active = true;
                                        }
                                    })
                                } else {
                                    res.datalist[i].priceList[0].active = true;
                                }
                            }
                        })
                    } else {
                        res.datalist[0].choosed = (res.datalist[0].servicesId);
                        res.datalist[0].priceList[0].active = true;
                    }

                }
            }
        },
        ['SET_RESET_SERVICE_LIST'](state, data) {
            state.serviceInfoList = data;
        },
        ['SET_PACKAGE_DATA'](state, data) {
            state.packageData = data;
        },
        ['SET_PAYTYPE'](state, data) {
            state.payType = data;
        },
        ['SET_SERVICE_INFO'](state, data) {
            state.serviceInfo = data;
        },
        ['SET_ISNEWADD'](state, data) {
            state.isNewAdd = data;
        },
        ['SET_USERTYPE'](state, data) {
            if (state.userMethod == 'vendor') {
                data.companyType = 1;
            } else if (state.userMethod == 'franchiser') {
                data.companyType = 2;
            } else {
                data.companyType = undefined;
            }
            data.vm.API.getCreatUserType({
                companyType: data.companyType,
                companyId: data.companyId
            }).then(res => {
                if (res.success) { state.userTypelist = res.obj }
            })
        },
        ['SET_USERTYPE2'](state, data) {
            state.userTypelist = data;
        },
        ['SET_STOP_TIME'](state,data){
            state.batchStopTime=data;
        }
    },
    actions: {
        setProduceRange({ commit }, data) {
            commit('SET_PRODUCE_RANGE', data)
        },
        setOldCategoryIds({ commit }, data) {
            commit('SET_OLD_CATEGORYID', data)
        },
        setFlag({ commit }, data) {
            commit('SET_FLAG', data)
        },
        setTabList({ commit }, data) {
            commit('SET_TAB_LIST', data);
        },
        setDialog({ commit }, data) {
            commit('SET_DIALOG', data);
        },
        setUserMethod({ commit }, data) {
            commit('SET_USER_METHOD', data);
        },
        setServicelist({ commit }, data) {
            if (data.reset) {
                commit('SET_RESET_SERVICE_LIST', data.res);
            } else {
                commit('SET_SERVICE_LIST', data);
            }

        },
        setPackageData({ commit }, data) {
            commit('SET_PACKAGE_DATA', data);
        },
        setPayType({ commit }, data) {
            commit("SET_PAYTYPE", data)
        },
        setServiceInfo({ commit }, data) {
            commit("SET_SERVICE_INFO", data)
        },
        setIsNewAdd({ commit }, data) {
            commit("SET_ISNEWADD", data)
        },
        setUserType({ commit }, data) {

            if (data.companyId) {
                commit("SET_USERTYPE", data)
            } else {
                commit("SET_USERTYPE2", data)
            }

        },
        setBatchStopTime({ commit },data){
            commit('SET_STOP_TIME',data)
        }
    }


};
export default userManage