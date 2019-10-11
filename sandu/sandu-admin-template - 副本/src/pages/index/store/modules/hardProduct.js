const hardProduct = {
    state:{
        productSKUInfos:[], //硬装需要传的参数  最终拼的参数（重要部分）
        setProductInfo:false,
        lookHardProductDetail:false,
        setProductInfoTitle:'',//设置和修改规格信息标题
        setProductInfoNum:'',//修改规格编号
        setPropsId:[],//规格信息的id ,选中了的都塞进去 带modelId 最后根据其 合并其他的参数 （重要部分）
        modelId:'',//修改规格信息当前模型的id （点击修改规格时 存放当前的modelId）
        hardModelList:[],//硬装模型列表   （重要部分）
        hardTexttureList:[],//硬装贴图列表
        areaId:'',//点击硬装贴图上传 需要的参数
        productPorps:'',//设置产品规格信息的数据
        lookProductInfo:{},//点击存放查看规格信息数据 
       // Copy_hardModelList:[],
    },
    getters:{
       gettersHardModelList:state=>state.hardModelList,
       gettersHardTexttureList:state=>state.hardTexttureList,
      // gettersCopyHardModelList: state => state.Copy_hardModelList,
       gettersAreaId:state=>state.areaId,
       gettersProductPorps:state=>state.productPorps,
       gettersSetProductInfo:state=>state.setProductInfo,
       gettersSetProductInfoTitle:state=>state.setProductInfoTitle,
       gettersSetProductInfoNum:state=>state.setProductInfoNum,
       gettersSetPropsId:state=>state.setPropsId,
       gettersModelId:state=>state.modelId,
       gettersSKUInfos:state=>state.productSKUInfos,
       gettersLookHardProductDetail:state=>state.lookHardProductDetail,
       gettersLookProductInfo:state=>state.lookProductInfo,
    },
    mutations: {
        AddShow:(state,_this)=>{
            state.hardModelList.map((item) => {
              _this.$set(item, 'iSShow', true);
            })
        },
        SET_HARD_MODEL:(state,data)=>{
            state.hardModelList=data;
        },
        SET_ADD_TEXTTURE:(state,data)=>{
            state.hardTexttureList=data;
        },
        SET_AREAID:(state,data)=>{
            state.areaId=data;
        },
        SET_PRODUCTPORPS:(state,data)=>{
            state.productPorps=data;
        },
        SET_CLOSEMD:(state,data)=>{
            state.setProductInfo=data;
        },
        SET_PRODUCTINFO:(state,data)=>{
            state.setProductInfoTitle=data.setProductInfoTitle;
            state.setProductInfoNum=data.setProductInfoNum;
            state.modelId=data.modelId;
        },
        SET_PROPSID:(state,data)=>{
            if(data.isConcat){
                state.setPropsId=data.PropsId;
            }else {
                state.setPropsId=[...state.setPropsId,...data];
            }
            //console.log(state.setPropsId,555)
        },
        SET_SKUINFO:(state,data)=>{
            state.productSKUInfos=data;
        },
        SET_CLOSEMD_DETAIL:(state,data)=>{
            state.lookHardProductDetail=data;
        },
        SET_LOOKINFO:(state,data)=>{
            let parentArr=[],childName=[];
            state.lookProductInfo.modelId=data.modelId;
            state.lookProductInfo.modelCode=data.modelCode;
            if(data.textureList){

                data.textureList.map(res=>{
                    //把所有的parentid 塞入一个数组中 不重复的

                    if(parentArr.some(item=>{ return item.parentId==res.parent.id})){
                        parentArr.map(parent=>{
                            if(parent.parentId==res.parent.id){
                                parent.childName.push(res.name);
                            }
                        })
                    }else {
                        //childName.push(res.name);
                        parentArr.push({parentId:res.parent.id,parentName:res.parent.name,childName:[res.name]});
                    }
                });
                console.log(parentArr)
                state.lookProductInfo.textureList=parentArr; //产品规格信息的列表
            }

        },
    },
    actions:{
        setHardModel({commit},data){
           commit('SET_HARD_MODEL',data);
        },
        addTextTure({commit},data){
            commit('SET_ADD_TEXTTURE',data);
        },
        setAreaId({commit},data){
            commit('SET_AREAID',data);
        },
        saveProductPorps({commit},data){
            commit('SET_PRODUCTPORPS',data)
        },
        dialogMd({ commit },data){  //弹窗改变
            commit('SET_CLOSEMD',data)
        },
        dialogMdDetail({ commit },data){  //弹窗改变
            commit('SET_CLOSEMD_DETAIL',data)
        },
        setProductInfo({ commit },data){
            commit('SET_PRODUCTINFO',data)
        },
        setPropsId({commit},data){
            commit('SET_PROPSID',data)
        },
        setProductSKUInfos({commit},data){
            commit('SET_SKUINFO',data)
        },
        lookProductInfo({commit},data){
            commit('SET_LOOKINFO',data)
        },
    }
}
export default hardProduct;