import {mapGetters} from 'Vuex'

let hardProductMinixs={
    data(){
        return{
            dialogModelTextureVisible:false
        }
    },
    computed:{
        ...mapGetters(['gettersHardModelList','gettersAreaId','gettersSetPropsId','gettersModelId','gettersSKUInfos']),
        isHard(){
            return sessionStorage.getItem('addProductType')=='hard' ? true : false
        }
    },
    created(){

    },
    methods: {
        upLoadTexture(){ //新增硬装上传贴图
            this.assModelTextureDia.dialogModelTextureVisible=true
            this.dialogModelTextureVisible = true;
            this.assModelTextureDia.query.queryType = 'success'
            this.API.ListTexture(this.assModelTextureDia.query).then((res) => {
                this.TextureListData = res.list ? res.list : [];
                this.totalPage=res.total;
            })
        },
        addModelTexture(data){//编辑硬装上传贴图e
            this.assModelTextureDia.dialogModelTextureVisible = true;
            this.assModelTextureDia.areaId = data.res.areaId;
            this.assModelTextureDia.index = data.res.key;
            this.ListModelTexture()
        },
        //添加硬装产品的关联模型
        hardConnectModel(type){
            console.log(this.$store.state.usedModelIds)
            let existModelIds=[];
            this.gettersHardModelList.map(res=>{
                existModelIds.push(res.modelId ? res.modelId:res.id);
            });
            console.log(existModelIds)
            this.$nextTick(()=>{
                if(type=='edit'){ //编辑页面
                    this.$refs.addHardProduct.isShow(true);
                    this.$refs.addHardProduct.showModelList(this.selectCategoryCode, existModelIds, this.$store.state.usedModelIds);
                }else { //新增页面
                    if(this.ruleForm.productCategoryForm.length>0){
                        this.$refs.addHardProduct.isShow(true);
                        this.$refs.addHardProduct.showModelList(this.selectCategoryCode, existModelIds, []);
                    }else {
                        this.$message.error('请选择产品分类');
                    }
                }


            })
        },
        handleModelTextureAddConfirm() {
            if(sessionStorage.getItem('addProductType')=='soft'){ //之前新增的软装的逻辑 不改动
                this.dialogModelTextureVisible = false
                this.area[this.modelTexture.index].areaTextures = this.multipleSelection.map(it => ({
                    textureId: it.textureId,
                    thumbnailPath: it.thumbnailPath,
                    affectPrice: 0,
                    deleteBtnVisible: false
                }));
            }else { //硬装产品逻辑
                this.$store.dispatch('addTextTure',this.multipleSelection);
                this.dialogModelTextureVisible = false;
                console.log(this.multipleSelection)
                console.log(this.gettersAreaId)
                this.gettersHardModelList.map((item,index)=>{
                    item.modelTextureInfo.map((res,childIndex)=>{
                        if(res.areaId==this.gettersAreaId){
                            //res.rels = [...res.rels,...this.multipleSelection];
                            res.rels.push(...this.multipleSelection);
                            //添加一个图片地址属性
                            res.rels.map((pic)=>{
                                if(pic.thumbnailPath){
                                    pic.picPath=pic.thumbnailPath;
                                }
                            })
                        }
                    })
                })
                console.log(this.gettersHardModelList)
            }
        },
        getHardParam(){
            let totalData=[],flag=0,mainProductFlag='';//flag=0 模型列表中是否有主模型 flag=1是没有主模型
            this.gettersHardModelList.some(res=>{return res.mainProductFlag}) ? flag=0 : flag=1;
            this.gettersHardModelList.map((item,index)=>{
                let textureArr=[],params={},modelId=item.modelId ? item.modelId : item.id;
                item.modelTextureInfo.map(area=>{ //贴图里面的信息参数
                    //console.log(area)
                    area.rels.map(rel=>{
                        let textureInfo={};
                        textureInfo.areaId=area.areaId;
                        textureInfo.textureId=rel.textureId;
                        textureInfo.isDefault=rel.defaultFlag ? 1 : 0;
                        textureArr.push(textureInfo);
                    })
                })
                //修改单个规格信息 //
                if(flag==1){
                   index==0 ? mainProductFlag=true : mainProductFlag=false;
                }else {
                    mainProductFlag=item.mainProductFlag;
                }
                let sku={ mainProductFlag: mainProductFlag,modelTextureInfos:textureArr,productId:item.productId,modelId:modelId,propIds:[]};
                // console.log(sku)
                // console.log(this.gettersSetPropsId)

                if(this.gettersSetPropsId.some(res=>{ return modelId==res.modelId })){ //如果修改了规格信息的
                    this.gettersSetPropsId.some((res,propIndex)=>{
                        if(modelId==res.modelId){
                            totalData.push(Object.assign(sku,res));
                            //console.log(Object.assign(res,sku))
                        }
                    })
                }else{ //没有修改规格信息的
                    totalData.push(sku);
                }

            })
            this.$store.dispatch('setProductSKUInfos',totalData);
        },
    },
    destroyed(){
        this.$store.dispatch('setHardModel',[]);
        this.$store.dispatch('setProductSKUInfos',[]);
        this.$store.dispatch('setPropsId',[]);
    }
}
export default hardProductMinixs