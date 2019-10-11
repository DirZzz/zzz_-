const packageBuy = {
    state:{
        loginPackageTimeout:false,//登录套餐到期提醒弹窗
        choosePackage:false, //选择套餐的弹窗
        packageTimeout:false,//已到期的正式套餐
        dialogTimeoutPackage:false,//非套餐用户 账号过期
        dialogBuy:false,//套餐购买续费
        packagePurchase:false,//套餐购买的弹窗
        payCode:false,//二维码的弹窗
        businessType:'',//参数 升级=4 或续费=1
        servicesType:'',//参数 正式=0 或适用=1
        paySuccessParam:'',
        packageList:'',
        serviceId:'',//当前的套餐id
        priceId:'',//当前选择套餐id
        userId:'',//点击该用户的userid
        timeOut:'',
        packageParam:{
            giveDuration:'',//赠送时长
            sanduCurrency:'',//赠送独臂
            freeRenderDuration:'',//渲染时长
            price:'',//应付价格
            payType:1,  //默认微信支付
            differPrice:'',//差价
        }
    },
    getters:{
        gettersPackageList:state=>state.packageList,
        gettersPackageParam:state=>state.packageParam,
        gettersServiceId:state=>state.serviceId,
        gettersPriceId:state=>state.priceId,
        gettersCodeUrl:state=>state.paySuccessParam,
        gettersChoosePackage:state=>state.choosePackage,
        gettersPackageTimeout:state=>state.packageTimeout,
        gettersDialogTimeoutPackage:state=>state.dialogTimeoutPackage,
        gettersDialogBuy:state=>state.dialogBuy,
        gettersPackagePurchase:state=>state.packagePurchase,
        gettersLoginPackageTimeout:state=>state.loginPackageTimeout,
        gettersPayCode:state=>state.payCode,
        gettersUserId:state=>state.userId,
        gettersBusinessType:state=>state.businessType,
        gettersServicesType:state=>state.servicesType,
        gettersTimeOut:state=>state.timeOut,
    },
    mutations: {
        SET_PACKAGE:(state,data)=>{
            state.packageList=data.res;
            state.packageList.servicesPriceVoList.map((item,index)=>{
                index == 0 ? item.active=true : item.active=false;
                if(index == 0){
                    state.packageParam.giveDuration=state.packageList.servicesPriceVoList[index].giveDuration;
                    state.packageParam.sanduCurrency=state.packageList.servicesPriceVoList[index].sanduCurrency;
                    state.packageParam.freeRenderDuration=state.packageList.servicesPriceVoList[index].freeRenderDuration;
                    state.packageParam.price=state.packageList.servicesPriceVoList[index].price;
                    state.packageParam.differPrice=state.packageList.servicesPriceVoList[index].differPrice;
                    state.priceId=state.packageList.servicesPriceVoList[index].id;
                }
            });
            state.serviceId=data.res.id;
            state.businessType=data.businessType;
            data.businessType==1 ? state.dialogBuy=false : state.choosePackage=false;
            setTimeout(()=>{
                state.packagePurchase=true;
            },200)
        },
        SET_PATTYPEID:(state,data)=>{
            state.serviceId=data;
        },
        SET_PRICEID:(state,data)=>{
            state.priceId=data;
        },
        SET_USERID:(state,data)=>{
            state.userId=data;
        },
        SET_CODEURL:(state,data)=>{
            state.paySuccessParam=data;
        },
        SET_SERVICESTYPE:(state,data)=>{
            state.servicesType=data;
        },
        SET_PACKAGEPATAM:(state,data)=>{
            Object.assign(state.packageParam,data);
        },
        SET_BUSINESSTYPE:(state,data)=>{
            state.businessType=data;
        },
        SET_TIMEOUT:(state,data)=>{
            state.timeOut=data;
        },
        SET_CLOSEMD:(state,data)=>{
            switch (data.name){
                case 'choosePackage': state.choosePackage=data.isShow;break;
                case 'packageTimeout': state.packageTimeout=data.isShow;break;
                case 'dialogTimeoutPackage': state.dialogTimeoutPackage=data.isShow;break;
                case 'dialogBuy': state.dialogBuy=data.isShow;break;
                case 'packagePurchase': state.packagePurchase=data.isShow;break;
                case 'loginPackageTimeout': state.loginPackageTimeout=data.isShow;break;
                case 'payCode': state.payCode=data.isShow;break;
            }
        },
        SET_ERROR:(state,data)=>{
            data.vm.$message(data.res.message);
        }
     },
    actions:{
        packageAction({ commit },data){ //升级续费的套餐
            data.vm.API.updagrade({userId:data.userId,saleChannel:3,serviceId:data.id}).then(res=>{
                if(res.success){
                    commit('SET_PACKAGE',{res:res.obj,businessType:4});//businessType=4 升级
                }else {
                    commit('SET_ERROR',{vm:data.vm,res:res});
                }

            })
        },
        goOnPackageAction({ commit },data){ //继续续费的套餐
            data.vm.API.renewals({userId:data.userId,saleChannel:3}).then(res=>{
                if(res.success){
                    commit('SET_PACKAGE',{res:res.obj,businessType:1});//businessType=1 续费
                }else {
                    commit('SET_ERROR',{vm:data.vm,res:res});
                }
            })
        },
        setBusinessType({commit},data){
            commit('SET_BUSINESSTYPE',data)
        },
        setServiceId({commit},data){ //当前的套餐id
            commit('SET_PATTYPEID',data)
        },
        setPriceId({commit},data){ //当前的套餐id
            commit('SET_PRICEID',data)
        },
        setPackageParam({commit},data){
           commit('SET_PACKAGEPATAM',data);
        },
        setCodeUrl({commit},data){
            commit('SET_CODEURL',data);
        },
        setUserId({commit},data){
            commit('SET_USERID',data);
        },
        setServicesType({commit},data){
            commit('SET_SERVICESTYPE',data);
        },
        setTimeOut({commit},data){
            commit('SET_TIMEOUT',data);
        },
        dialog({ commit },data){  //弹窗改变
            console.log(222)
            commit('SET_CLOSEMD',data)
        }
    }
}
export default packageBuy;