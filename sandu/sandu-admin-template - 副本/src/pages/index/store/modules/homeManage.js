import { pick,filter,cloneDeep } from 'lodash'
const homeManage={
    namespaced:true,
    state:{
        addGoodsDialog:false,
        addplanDialog:false, //添加方案
        bannerList:[],//banner列表数据
        goodsList:[],//爆款商品列表
        submitGoodsList:[],//爆款商品需要提交的商品列表数据 1
        newGoodsList:[],//新品商品列表
        submitNewGoodsList:[],//新品商品需要提交的商品列表数据 2
        plansList:[],//方案列表
        submitPlansList:[],//需提交的方案列表  3
        isHot:0, //当前列表显示的是 0爆款  1新品 列表 2新模块
        isReplace:false,//是否是替换 弹出
        replaceIndex:'', //当前替换的是第几个
        companyIntroduceVO:{},//企业设置初始化数据
        totalData:'', //总的编辑初始数据
        // titleList:{  //新品,爆款的标题
        //     baoTitle:'',
        //     newTitle:'',
        // },
        self:'',
        totalPage:0,
        moreIndex:0,
        planTotalPage:'',
        brandList:[],//品牌列表
        spaceList:[],//空间类型列表
        moreGoods:[], //新增商品模块  自定义模块
        submitMoreGoodsList:[],//新模块需要提交的商品列表数据
        moreGoodsList:[], //新模块商品列表
        moduleType:[  //自定义模块 各模块之间允许拖拽
            // {type:'banner',configDetails:[]},
            // {type:'nav',configDetails:[
            //     {imgUrl:'',title:'',show:true},
            //     {imgUrl:'',title:'',show:true},
            //     {imgUrl:'',title:'',show:true},
            //     {imgUrl:'',title:'',show:true}
            //  ]
            // },
            // {type:'dialog',title:'',configDetails:[],isShowHome:1,secondTitle:''},   //弹窗模块
            // {type:'plan',title:'',configDetails:[]},   //方案模块
            // {type:'obtainMobile',title:'',amount:'',uuid:'',configDetails:[]},   //免费获取方案
            // {type:'act',title:'',configDetails:[]},    //活动模块
            // {type:'newRecommend',configDetails:[]}, //新品推荐
            // {type:'hotRecommend',configDetails:[]}, //热门推荐
            // {type:'moreGoods',title:'',configDetails:[]}, //更多商品模块
            // {type:'company',title:'公司简介',configDetails:[]},  //公司介绍
        ],
        moduleTitle:'',//各模块的标题
        actImgList:[],//已选择的活动列表
        tapIndex:0, //点击哪个模块
        // 公司介绍
        companyData:{type:'company',active:true,isShowHome:1,title:'公司简介',richContext:'',configDetails:[]},
        navList:[],  //导航栏数据
        copyNavList:[], //导航栏默认图片
        actList:[],//活动列表
        actDefault:[],//活动默认列表
        selectArr:[],
        freePlanData:'', //免费获取方案的数据
        setDialogData:'', //首页弹窗设置的数据
        baseInfoData:'', //公司基本信息的数据
        appId:'',
    },
    getters:{

    },
    mutations:{
        ['SET_DIOLOG'](state,data){
            state.addGoodsDialog=data;
        },
        ['SET_PLAN_DIOLOG'](state,data){
            state.addplanDialog=data;
        },
        ['SET_GOODSLIST'](state,data){ //爆款
            state.totalPage=data.total;
            data.list.map((res,index)=>{ //回显之前的选中商品
                state.submitGoodsList && state.submitGoodsList.map((item,val)=>{
                    if(res.id==item.id){
                        Object.assign(data.list[index],item);
                    }
                })
            });
            state.goodsList=data.list;
        },
        ['SET_NEWGOODSLIST'](state,data){  //新品
            state.totalPage=data.total;
            data.list.map((res,index)=>{ //回显之前的选中商品
                state.submitNewGoodsList && state.submitNewGoodsList.map((item,val)=>{
                    if(res.id==item.id){
                        Object.assign(data.list[index],item);
                    }
                })
            });
            state.newGoodsList=data.list;
        },
        ['SET_SUBMITLIST'](state,data){//爆款

            if(data.type==0){ //删除
                state.goodsList.map((item,index)=>{
                    data.res.id==item.id ? item.active=false : '';
                    state.self.$set(state.goodsList,index,state.goodsList[index]);
                });//取消回显
                state.submitGoodsList=state.submitGoodsList.filter(item=>{ return item.id!=data.res.id });
            }else if(data.type==1){//新增
                state.submitGoodsList.length<10 ? state.submitGoodsList.push(data.res) : '';
            }else if(data.type==2){ //替换
                state.goodsList.map((item,index)=>{
                    item.id==state.submitGoodsList[state.replaceIndex].id ? item.active=false : '';
                    item.id==data.res.id ? item.active=true : '';
                    state.self.$set(state.goodsList,index,state.goodsList[index]);
                });//取消回显
                state.submitGoodsList.splice(state.replaceIndex,1,data.res);
            }
        },
        ['SET_NEWSUBMITLIST'](state,data){ //新品
            if(data.type==0){ //删除
                state.newGoodsList.map((item,index)=>{
                    data.res.id==item.id ? item.active=false : '';
                    state.self.$set(state.newGoodsList,index,state.newGoodsList[index]);
                });//取消回显
                state.submitNewGoodsList=state.submitNewGoodsList.filter(item=>{ return item.id!=data.res.id });
            }else if(data.type==1){ //新增
                state.submitNewGoodsList.length<10 ? state.submitNewGoodsList.push(data.res) : ''
            }else if(data.type==2){ //替换
                state.newGoodsList.map((item,index)=>{
                    item.id==state.submitNewGoodsList[state.replaceIndex].id ? item.active=false : '';
                    item.id==data.res.id ? item.active=true : '';
                    state.self.$set(state.newGoodsList,index,state.newGoodsList[index]);
                });//取消回显
                state.submitNewGoodsList.splice(state.replaceIndex,1,data.res);
            }
        },
        ['SET_SUBMIT_PLANSLIST'](state,data){ //方案
            if(data.type==0){ //删除
                state.plansList.map((item,index)=>{
                    data.res.planId==item.planId ? item.active=false : '';
                    state.self.$set(state.plansList,index,state.plansList[index]);
                });//取消回显
                state.submitPlansList=state.submitPlansList.filter(item=>{ return item.planId!=data.res.planId });
            }else if(data.type==1){ //新增
                state.submitPlansList.length<10 ? state.submitPlansList.push(data.res) : ''
            }else if(data.type==2){ //替换
                state.plansList.map((item,index)=>{
                    item.planId==state.submitPlansList[state.replaceIndex].planId ? item.active=false : '';
                    item.planId==data.res.planId ? item.active=true : '';
                    state.self.$set(state.plansList,index,state.plansList[index]);
                });//取消回显
                state.submitPlansList.splice(state.replaceIndex,1,data.res);
            }else {
                data.list && data.list.map(res=>{ res.planId ? '' : res.planId=res.id })
                state.submitPlansList=data.list || [];
                state.moduleTitle=data.title;
            }
        },
        ['SET_MORESUBMITLIST'](state,data){ //新增更多模块
            if(data.type==0){ //删除
                state.moreGoodsList.map((item,index)=>{
                    data.res.id==item.id ? item.active=false : '';
                    state.self.$set(state.moreGoodsList,index,state.moreGoodsList[index]);
                });//取消回显
                state.submitMoreGoodsList=state.submitMoreGoodsList.filter(item=>{ return item.id!=data.res.id });
            }else if(data.type==1){ //新增
                state.submitMoreGoodsList.length<10 ? state.submitMoreGoodsList.push(data.res) : ''
            }else if(data.type==2){ //替换
                state.moreGoodsList.map((item,index)=>{
                    item.id==state.submitMoreGoodsList[state.replaceIndex].id ? item.active=false : '';
                    item.id==data.res.id ? item.active=true : '';
                    state.self.$set(state.moreGoodsList,index,state.moreGoodsList[index]);
                });//取消回显
                state.submitMoreGoodsList.splice(state.replaceIndex,1,data.res);
            }else if(data.type==3){
                state.submitMoreGoodsList=data.res || [];
            }
        },
        ['SET_HOT'](state,data){
            state.isHot=data;
        },
        ['SET_REPLACE'](state,data){
            state.replaceIndex=data.index;
            state.isReplace=data.isReplace;
        },
        ['SET_BANNER_LIST'](state,data){
            state.bannerList=data;
            //console.log(state.bannerList)
        },
        ['SET_INIT_BANNER'](state,list){  //初始化 banner数据
            list.map(item=>{
                if(item.type=='banner'){
                    item.configDetails.map(res=>{ res.dialogTip=false });
                    state.bannerList=cloneDeep(item.configDetails) || [];
                    //console.log(state.bannerList,'state.bannerList')
                }
            })

        },
        ['SET_INIT_BAO'](state,list){ //初始化爆款数据
            list.map(item=>{
                if(item.type=='hotRecommend'){
                    state.submitGoodsList=item.configDetails || [];
                }
            })
            state.submitGoodsList && state.submitGoodsList.map(item=>{
                item.active=true;
            })
        },
        ['SET_INIT_NEW'](state,list){ //初始化新品数据
            list.map(item=>{
                if(item.type=='newRecommend'){
                    state.submitNewGoodsList=item.configDetails || [];
                }
            })
            state.submitNewGoodsList && state.submitNewGoodsList.map(item=>{
                item.active=true;
            })
        },
        ['SET_INIT_NAV'](state,list){
            list.map(item=>{
                if(item.type=='nav'){
                    item.configDetails.map((res,index)=>{
                        res.navValue ? '' : res.navValue=index+1; //初始化1 2 3 4
                    })
                    state.navList=cloneDeep(item.configDetails) || [];
                }
            })
            state.self.API.obtainDefaultNAVPicPath().then(res=>{
                let arr=[],k=1;
                for (let i in res.data){
                    arr.push({
                        navValue:k,
                        imageUrl:res.data[i]
                    })
                    k++
                }
                state.copyNavList=arr;
            })
        },
        ['SET_THIS'](state,data){
            state.self=data;
        },
        ['SET_APPID'](state,data){
            state.appId=data;
        },
        //推荐方案
        ['SET_PLAN_LIST'](state,data){
            data.map((res,index)=>{ //回显之前的选中商品
                state.submitPlansList && state.submitPlansList.map((item,val)=>{
                    item.active=true;
                    if(res.planId==item.id){
                        Object.assign(data[index],item);
                    }
                })
            });
            state.plansList=data || [];
        },
        ['SET_TOTAL_PAGE'](state,data){
            state.planTotalPage=data;
        },
        ['SET_BRAND_LIST'](state,data){
            state.brandList=data;
        },
        ['SET_SPACE_LIST'](state,data){
            state.spaceList=data;
        },
        ['SET_SAVE'](state,data){
            console.log(state.submitPlansList)
             console.log(state.tapIndex)
             console.log(state.moduleType)
            //data.isDelete是否是删除模块
            switch (state.moduleType[state.tapIndex].type){
                case 'banner':
                    !data.isDelete ? state.moduleType[state.tapIndex].configDetails=cloneDeep(state.bannerList) : '';
                    break;
                case 'nav':
                    !data.isDelete ? state.moduleType[state.tapIndex].configDetails=cloneDeep(state.navList) : '';
                    break;
                case 'dialog':
                    !data.isDelete ? state.moduleType[state.tapIndex]=Object.assign({},state.setDialogData) : '';
                    break;
                case 'baseInfo':
                    !data.isDelete ? state.moduleType[state.tapIndex]=Object.assign({},state.baseInfoData) : '';
                    break;
                case 'plan'://推荐方案
                    if(!data.isDelete){
                        let arr=[];
                        state.submitPlansList.map(res=>{
                            res.detailName ? '' : res.detailName=res.planName;
                            res.spaceArea ? '' : res.spaceArea=res.spaceAreas;
                            res.id ? '' : res.id=res.planId;
                            arr.push(pick(res,'id','detailName','spaceArea','designStyleName','picAddress'));
                        });
                        state.moduleType[state.tapIndex].title=data.title;
                        state.moduleType[state.tapIndex].configDetails=arr;
                    }else {
                       //如果这个模块只有一个则加入一个空的传给后台
                        filter(state.moduleType,['type','plan']).length>1
                            ? state.moduleType.splice(state.tapIndex,1)
                            : state.moduleType.splice(state.tapIndex,1,{ type:'plan',title:'',configDetails:[]});
                    }
                    break;
                case 'obtainMobile':
                    if(!data.isDelete){
                        state.moduleType[state.tapIndex]=cloneDeep(state.freePlanData)
                    }else {
                        filter(state.moduleType,['type','obtainMobile']).length>1
                            ? state.moduleType.splice(state.tapIndex,1)
                            : state.moduleType.splice(state.tapIndex,1,{ type:'obtainMobile',title:'',amount:'',uuid:'',configDetails:[]});
                    }
                    break;
                case 'act'://热门活动
                    if(!data.isDelete){
                        state.moduleType[state.tapIndex].title=data.title;
                        state.moduleType[state.tapIndex].configDetails=cloneDeep(data.list);
                    }else {
                        filter(state.moduleType,['type','act']).length>1
                            ? state.moduleType.splice(state.tapIndex,1)
                            : state.moduleType.splice(state.tapIndex,1,{ type:'act',title:'',configDetails:[]});
                    }
                    break;
                case 'newRecommend'://新品推荐
                    if(!data.isDelete){
                        let newArr=[];
                        state.submitNewGoodsList.map(res=>{
                            res.detailName ? '' : res.detailName=res.spuName;
                            res.price ? '' : res.price=res.minPrice;
                            newArr.push(pick(res,'id','detailName','picAddress','price'));
                        });
                        state.moduleType[state.tapIndex].title=data.title;
                        state.moduleType[state.tapIndex].configDetails=newArr;
                    }else {
                        state.moduleType.splice(state.tapIndex,1)
                    }
                    break;
                case 'hotRecommend': //热门推荐
                    if(!data.isDelete){
                        let hotArr=[];
                        state.submitGoodsList.map(res=>{
                            res.detailName ? '' : res.detailName=res.spuName;
                            res.price ? '' : res.price=res.minPrice;
                            hotArr.push(pick(res,'id','detailName','picAddress','price'));
                        });
                        state.moduleType[state.tapIndex].title=data.title;
                        state.moduleType[state.tapIndex].configDetails=hotArr;
                    }else {
                        state.moduleType.splice(state.tapIndex,1)
                    }
                    break;
                case 'moreGoods': //更多商品
                    if(!data.isDelete){
                        let moreArr=[];
                        state.submitMoreGoodsList.map(res=>{
                            res.detailName ? '' : res.detailName=res.spuName;
                            res.price ? '' : res.price=res.minPrice;
                            moreArr.push(pick(res,'id','detailName','picAddress','price'));
                        });
                        state.moduleType[state.tapIndex].title=data.title;
                        state.moduleType[state.tapIndex].configDetails=moreArr;
                    }else {
                        filter(state.moduleType,['type','moreGoods']).length>1
                            ? state.moduleType.splice(state.tapIndex,1)
                            : state.moduleType.splice(state.tapIndex,1,{ type:'moreGoods',title:'',configDetails:[]});
                    }

                    break;
                case 'company': //公司介绍
                    !data.isDelete ? state.moduleType[state.tapIndex]=cloneDeep(state.companyData) : '';
                    break;
            }
            data.vm.$set(state.moduleType,state.tapIndex,state.moduleType[state.tapIndex]);
           // console.log(state.moduleType)
           data.vm.API.createorupdate({
               configList:state.moduleType,
               companyId:sessionStorage.getItem('companyID'),
               appId:state.appId
           }).then(res=>{
                if(res.code==200){
                    data.vm.$message.success('保存成功');
                }else {
                    data.vm.$message.error('保存失败');
                }
            })
        },
        ['SET_MOREGOODS'](state,data){
            state.moreGoods.push(data);
        },
        ['SET_MOREGOODSLIST'](state,data){
            state.totalPage=data.total;
            //console.log(state.submitMoreGoodsList,'state.submitMoreGoodsList')
            data.list.map((res,index)=>{ //回显之前的选中商品
                state.submitMoreGoodsList && state.submitMoreGoodsList.map((item,val)=>{
                    item.active=true;
                    if(res.id==item.id){
                        Object.assign(data.list[index],item);
                    }
                })
            });
            state.moreGoodsList=data.list;
            //console.log(state.moreGoodsList,'state.moreGoodsList')
        },
        ['SET_TAP_INDEX'](state,data){
            state.tapIndex=data;
        },
        ['SET_ACT_LIST'](state,data){
            state.actImgList=data;
        },
        ['SET_MODULE_TITLE'](state,data){
            state.moduleTitle=data;
        },
        ['SET_INIT_MODULE_LIST'](state,list){
            list.map(res=>{
               if(res.type=='company'){
                   res.configDetails ? res.configDetails: res.configDetails=[];
               }
            });
            state.moduleType=cloneDeep(list) || [];
            console.log(state.moduleType,'state.moduleType')
        },
        ['SET_FREE_PLAN'](state,data){
            state.freePlanData=data;
        },
        ['SET_ACTLIST'](state,list){

            list.map(res=>{
                if(res.type=='act'){
                    res.configDetails ? '' : res.configDetails=[];
                    res.configDetails.map(item=>{
                        state.selectArr.push(parseInt(item.value))
                    })
                }
            });
            //活动列表
            state.self.API.homeActiveList().then(res=>{
                if(res.success){
                    state.actList=res.data;
                    state.actDefault=cloneDeep(res.data);
                }
            })
        },
        ['SET_INIT_COMPANY'](state,list){
            list.map(res=>{
                if(res.type=='company'){
                    res.configDetails ? '' : res.configDetails=[];
                    state.companyData=cloneDeep(res);
                }
            });
        },
        ['SET_INIT_PLAN'](state,list){
            list.map(res=>{
                if(res.type=='dialog'){
                   state.setDialogData=Object.assign({},res) //浅拷贝
                }
            });
           // console.log(list)
        },
        ['SET_INIT_DIALOG'](state,list){

        },
        ['SET_SELECT_ARR'](state,data){
            state.selectArr=data;
        },
        ['SET_DEFAULT_ACT'](state,data){
            state.actList=cloneDeep(state.actDefault);
        },
        ['SET_BASE_INFO'](state,data){
            state.baseInfoData=data;
        }
    },
    actions:{
        dialog({commit},data){
            commit('SET_DIOLOG',data);
        },
        planDialog({ commit },data){
            commit('SET_PLAN_DIOLOG',data);
        },
        setGoodsList({commit},data){
            data.vm.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)'
            })
            data.vm.API.getGoodsList({hasModelOrMaterial: data.hasModelOrMaterial,name:data.name,code:data.code,companyId:sessionStorage.getItem("companyID"),page:data.page,limit:6,putaway:1}).then((res)=>{
                if(res.code==200){
                    data.vm.$loading().close();
                    commit('SET_HOT',data.type);
                    switch (data.type){
                        case 1:  commit('SET_NEWGOODSLIST',res);break;
                        case 0:  commit('SET_GOODSLIST',res);break;
                        case 2:  commit('SET_MOREGOODSLIST',res);break;
                    }
                }else {
                    data.vm.$message.error(res.message);
                }
            });
        },
        setPlansList({commit},data){
            data.vm.API.OneKeyDesignPlanList({
                page:data.page,
                limit:5,
                companyId:sessionStorage.getItem("companyID"),
                spaceCommonName:data.spaceCommonName || undefined,
                brandId:data.brandId || undefined,
                planName:data.planName || undefined,
                origin:data.origin || undefined,
                chargeType:data.chargeType || undefined,
                platformId: 14  //企业小程序
            }).then(res=>{ //方案列表
               if(res.code==200){
                   commit('SET_PLAN_LIST',res.data.list);
                   commit('SET_TOTAL_PAGE',res.data.total)
               }else if(res.code==204){ //没有数据
                   commit('SET_PLAN_LIST',[]);
                   commit('SET_TOTAL_PAGE',0)
               }
            })
        },
        setSearchList({ commit },data){
            data.vm.API.FitTypeHose().then(res=>{ //空间类型列表
                if(res.code==200){
                    commit('SET_SPACE_LIST',res.data);
                }
            });
            data.vm.API.ProductBrandDrop({companyId:sessionStorage.getItem('companyID')}).then(res=>{ //品牌列表
                if(res.code==200){
                    commit('SET_BRAND_LIST',res.data);
                }
            })
        },
        setSubmitGoodsList({commit},data){
            commit('SET_SUBMITLIST',data);
        },
        setSubmitNewGoodsList({commit},data){
            commit('SET_NEWSUBMITLIST',data);
        },
        setSubmitMoreGoodsList({commit},data){
            commit('SET_MORESUBMITLIST',data);
        },
        setSubmitPlansList({ commit },data){
            commit('SET_SUBMIT_PLANSLIST',data)
        },
        setFreePlan({ commit },data){
            commit('SET_FREE_PLAN',data)
        },
        setReplace({ commit },data){
            commit('SET_REPLACE',data)
        },
        setBannerList({ commit },data){
            commit('SET_BANNER_LIST',data)
        },
        setCompanySet({ commit },data){
            commit('SET_COMPANY_SET',data)
        },
        setInitdata({ commit },data){ //初始化数据
            data.API.dashboardconfig({companyId:sessionStorage.getItem('companyID')}).then(res=>{
                commit('SET_THIS',data);// 初始化标题
                if(res.code==200){
                    commit('SET_APPID',res.data.appId);
                    commit('SET_INIT_BANNER',res.data.list);//初始化 banner数据
                    commit('SET_INIT_NAV',res.data.list) //初始化 导航数据
                    commit('SET_INIT_DIALOG',res.data.list) //初始化 导航数据
                    commit('SET_INIT_PLAN',res.data.list) //初始化 方案数据
                    commit('SET_INIT_BAO',res.data.list);//初始化 爆款数据
                    commit('SET_INIT_NEW',res.data.list);//初始化新品数据
                    commit('SET_ACTLIST',res.data.list);//初始化活动列表
                    commit('SET_INIT_COMPANY',res.data.list);//初始化公司列表
                    commit('SET_INIT_MODULE_LIST',res.data.list);//初始化模块数据
                }else {
                    //data.$message.error('网络异常');
                }
            });
        },
        setSave({ commit },data){  //保存
            commit('SET_SAVE',data);
        },
        setMoreGoods({ commit },data){
            commit('SET_MOREGOODS',data);
        },
        setTapIndex({ commit },data){
            commit('SET_TAP_INDEX',data);
        },
        setActList({ commit },data){
            commit('SET_ACT_LIST',data);
        },
        setModuleTitle({ commit },data){
            commit('SET_MODULE_TITLE',data);
        },
        setSelectArr({ commit },data){
            commit('SET_SELECT_ARR',data);
        },
        setDefauleAct({ commit },data){
            commit('SET_DEFAULT_ACT',data)
        },
        setBaseInfo({ commit },data){
            commit('SET_BASE_INFO',data)
        }
    }
}
export default homeManage