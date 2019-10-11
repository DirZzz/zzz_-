<template>
    <div class="hardProduct">
        <div class="productBaseInfo">模型相关信息</div>
        <div class="hardProduct-header">

            <span class="productText" :class="{'active':seclected}" v-if="isShowProInfo" @click="chooseProduct"><i :class="{'active':seclected}"></i>选择规格信息</span>

             <!--<el-select class="searchSelec" :clearable="true" v-model="isModelName" filterable   placeholder="请输入模型名称"></el-select>-->
                <el-select @change="searchModelBtn({modelcode:isModelCode,modelname:isModelName})" class="searchSelec" :clearable="true" v-model="isModelName" filterable   placeholder="请输入模型名称">
                     <el-option
                     v-for="(item,index) in gettersHardModelList"
                     :key="index"
                     :label="item.modelName"
                     :value="item.modelName"
                     >
                     </el-option>
                  </el-select>
                  <el-select @change="searchModelBtn({modelcode:isModelCode,modelname:isModelName})"  class="searchSelec" v-model="isModelCode" filterable  :clearable="true"  placeholder="请输入模型编号">
                     <el-option
                     v-for="(item,index) in gettersHardModelList"
                     :key="index"
                     :label="item.modelCode"
                     :value="item.modelCode"
                      >
                     </el-option>
                  </el-select>
              <el-button round  class="searchModelbtn"  @click="searchModelBtn({modelcode:isModelCode,modelname:isModelName})" plain>搜索</el-button>

            <el-button round type="primary" class="connectModel" @click="connectModel">关联模型</el-button>
        </div>
        <div class="tableBox">
            <div class="header-tab">
                <el-checkbox v-model="checked" style="padding-left: 20px" @click.native="allChecked">全选</el-checkbox>
                <span>序号</span>
                <span>缩略图</span>
                <span>名称</span>
                <span>编号</span>
                <span>尺寸(cm)</span>
                <span>操作</span>
                <em></em>
            </div>
            <div class="noMsg" v-if="gettersHardModelList.length==0">   </div>
            <div class="hardProduct-list">
                <ul>
                    <li v-for="(item,index) in gettersHardModelList" :key="index" v-if="computerPage(index)&&item.iSShow==true">
                        <div class="hardProduct-top">
                            <el-checkbox v-model="item.hasChecked" style="padding-left: 20px" @click.native="hasChecked(item,index)"></el-checkbox>
                            <span>{{index+1}}</span>
                            <span><img :src="item.thumbPicPath | filter" alt=""></span>
                            <span>{{item.modelName}}</span>
                            <span>{{item.modelCode}}</span>
                            <span>长:{{item.length}}     宽:{{item.width}}     高:{{item.height}}</span>
                            <div class="operation">
                                <div @click="setMainModel(item,index)" v-if="item.mainProductFlag" class="disabledMainModel">已设为标准模型</div>
                                <p @click="setMainModel(item,index)" v-else>设为标准模型</p>
                                <p @click="editInfo(item,index)">修改规格信息</p>
                              <p @click="cancelModelConnect(item,index)" v-if="gettersHardModelList.length>1">取消关联</p> 
                            </div>
                            <div class="pullDownSection" @click="toggle(item,index)">
                                <div class="pullDownSection-content">
                                    <p v-if="!item.active">展开({{item.textureCount}})<strong></strong></p>
                                    <p v-else style="color: #999">收起({{item.textureCount}})<em></em></p>
                                </div>
                            </div>
                        </div>
                        <div class="hardProduct-bottom" v-show="item.active">
                            <div class="hardProductBox" v-for="(res,key) in item.modelTextureInfo" :key="key">
                                <div class="materialName">{{Number(res.areaCode)+1}}.材质区域名称：<span>{{res.areaName}}</span><em @click="materialOptation(res)" v-if="index==0">材质应用</em></div>
                                <div class="upload-pic">
                                    <div class="upload-pic-box" @click="handleAddModelTexture(res,key)">
                                        <img src="../../../../assets/images/icons/uploadPlug.png" alt="">
                                        <p>上传贴图</p>
                                    </div>
                                    <div class="upload-pic-list" v-for="(texture,val) in res.rels" :key="val" @mouseenter="showDelImg('enter',texture,index,key,val)" @mouseout="showDelImg('out',texture,index,key,val)">

                                        <template>
                                            <el-popover
                                                    placement="top-start"
                                                    width="300"
                                                    trigger="hover"
                                            >
                                                <div>
                                                    <p>材质编码:{{res.rels[val].textureCode}}</p>
                                                    <p>材质型号:{{res.rels[val].modelNumber}}</p>
                                                    <p>材质名称:{{res.rels[val].textureName}}</p>

                                                </div>
                                                <img slot="reference" :src="texture.picPath | filter" title="item">
                                                <!-- <img  :src="BASE_URL.sourceBaseUrl + (ImgItem.thumbnailPath || ImgItem.picPath)"> -->
                                            </el-popover>
                                        </template>
                                        <em v-if="val==0">主缩略图</em>
                                        <img src="../../../../assets/images/icons/delete3.png" alt="" class="delete-img" v-show="texture.active" @click="deleteImg(texture,index,key,val)" @mouseenter="showDelImg('enter',texture,index,key,val)">
                                        <div class="setDefaultPic" v-if="val!=0" v-show="texture.active" @mouseenter="showDelImg('enter',texture,index,key,val)" @click="setDefaultPic(texture,index,key,val)">设为主缩略图</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="pagePagination" v-if="gettersHardModelList.length>0">
            <!-- <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[10, 50, 100, 200, 500]"
                    :page-size="pageSize"
                    layout="sizes, prev, pager, next"
                    :total="totalPage">
            </el-pagination> -->

             <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="0"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="10"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalPage">
                </el-pagination>
        </div>

    </div>
</template>

<script>
    import { mapGetters } from 'Vuex'
    export default {
        name: "hardProduct",
        data(){
            return{
                isModelName:'',
                isModelCode:'',
                checked:false,
                materialProduct:false,
                rels:[],
                checkList:[],
                areaCode:'',
                currentPage:1,
                pageSize:10,
              //  iSShoNone:false
                 totalPage:0
            }
        },
        watch: {
            gettersHardModelList(val){
                this.totalPage = this.gettersHardModelList.length;
            }
        },
        computed:{
            ...mapGetters(['gettersSKUInfos','gettersHardModelList','gettersHardTexttureList','gettersProductPorps','gettersSetProductInfo','gettersSetPropsId']),
            seclected(){
                return this.gettersHardModelList.some((item,index)=>{
                    return item.hasChecked;
                })
            },
            // totalPage(){
            //     return this.gettersHardModelList.length;
            // },
            isShowProInfo(){
                return this.gettersProductPorps.length>0;
            },
            isConnectBtn(){
                if(sessionStorage.getItem('isConnectBtn')=='false'){
                    return false;
                }else{
                    return true;
                }
            },

        },
        filters:{
            filter(val){
                return process.env.sourceBaseUrl+val;
            }
        },
        methods:{
            searchModelBtn(data) {
            //  this.$forceUpdate();
             this.restartCheck();
         for (let key in data) {
              if (data[key] == '' || data[key] == undefined || data[key] == null) {
                delete data[key]
              }
            } 

           this.gettersHardModelList.map(item=>{
                if(data.modelname&&data.modelcode){
                    if(item.modelName==data.modelname&&item.modelCode==data.modelcode){
                        item.iSShow =true;
                    }else{
                        item.iSShow =false;
                    }

                }else if(data.modelname){
                   if(item.modelName==data.modelname){
                        item.iSShow =true;
                    }else{
                        item.iSShow =false;
                    }

                }else if(data.modelcode){
                   
                    if(item.modelCode==data.modelcode){
                        item.iSShow =true;
                    }else{
                         item.iSShow =false;
                    }
                }else{
                    item.iSShow =true;
                }
           })
           let arr = [];
             arr =  this.gettersHardModelList.filter(item=>{
                 return item.iSShow == true;
             })
             this.totalPage = arr.length;  
            },
            computerPage(index){
                return index>=(this.currentPage-1)*this.pageSize&&index<this.currentPage*this.pageSize;
            },
            handleSizeChange(val){
                this.pageSize=val;
                //this.restartCheck();
            },
            handleCurrentChange(val){
                this.currentPage=val;
                //this.restartCheck();
            },
            restartCheck(){
                this.checked=false;
                this.gettersHardModelList.map((item,index)=>{
                    item.hasChecked=false;
                    this.$set(this.gettersHardModelList,index,this.gettersHardModelList[index]);
                })
            },
            showDelImg(type,texture,index,key,val){
                if(type=='enter'){
                    texture.active=true;
                    this.$set(this.gettersHardModelList[index].modelTextureInfo[key].rels,val,this.gettersHardModelList[index].modelTextureInfo[key].rels[val]);
                }else {
                    texture.active=false;
                    this.$set(this.gettersHardModelList[index].modelTextureInfo[key].rels,val,this.gettersHardModelList[index].modelTextureInfo[key].rels[val]);
                }

            },
            deleteImg(texture,index,key,val){ //点击删除图片
                this.gettersHardModelList[index].modelTextureInfo[key].rels.splice(val,1);
            },
            setDefaultPic(texture,index,key,val){ //设置主缩略图
                this.gettersHardModelList[index].modelTextureInfo[key].rels.splice(val,1);
                this.gettersHardModelList[index].modelTextureInfo[key].rels.unshift(texture);
            },
            connectModel(){ //关联模型
                this.$emit('connectModel');
            },
            handleAddModelTexture(res,key){
                // areaId 点击时存储
                this.$store.dispatch('setAreaId',res.areaId);
                this.$emit('handleAddModelTexture',{key:key,res:res})
            },
            toggle(item,index){
                item.active=!item.active;
                this.$set(this.gettersHardModelList,index,this.gettersHardModelList[index]);
            },
            hasChecked(item,index){
                console.log(item)
                item.checked=!item.checked;
                this.$set(this.gettersHardModelList,index,this.gettersHardModelList[index]);
            },
            allChecked(){//全选
                //console.log(this.gettersHardModelList,44444)
                this.gettersHardModelList.map((item,index)=>{
                    if(!this.checked){
                        if(item.iSShow){
                            item.hasChecked=true;
                            this.$set(this.gettersHardModelList,index,this.gettersHardModelList[index]);
                        }
                    }else {
                        if(item.iSShow){
                            item.hasChecked=false;
                            this.$set(this.gettersHardModelList,index,this.gettersHardModelList[index]);
                        }
                    }
                })
            },
            cancelModelConnect(item,index){ //删除关联
                this.gettersHardModelList.splice(index,1);
                let modelId=item.modelId ? item.modelId : item.id;
                this.gettersSetPropsId.map((res,i)=>{ //删除已选的propsId
                    if(res.modelId==modelId){
                        this.gettersSetPropsId.splice(i,1);
                    }
                });
                //console.log(this.gettersSetPropsId)
                this.$store.dispatch('setPropsId',{PropsId:this.gettersSetPropsId,isConcat:true});
                this.$store.dispatch('setHardModel',this.gettersHardModelList);
            },
            setMainModel(item,index){ //设置主模型
                this.gettersHardModelList.map(res=>{
                    res.mainProductFlag=false;
                })
                console.log(this.gettersHardModelList)
                this.$set(this.gettersHardModelList,index,this.gettersHardModelList[index]);
                this.gettersHardModelList.splice(index,1);
                this.gettersHardModelList.unshift(item);
                this.gettersHardModelList[0].mainProductFlag=true;
                this.$store.dispatch('setHardModel',this.gettersHardModelList)
            },
            editInfo(item,curentIndex){ //修改规格信息
                this.$store.dispatch('setProductInfo',{setProductInfoTitle:'修改规格信息',setProductInfoNum:item.modelCode,modelId:item.modelId ? item.modelId : item.id});
                this.$store.dispatch('dialogMd',true);
                //console.log(item)
                //回显默认的选中的
                //  console.log(curentIndex)
                //  console.log(this.gettersSetPropsId,2222)
                //  console.log(this.gettersProductPorps,3333)
                this.gettersSetPropsId.map((res,index)=>{  //回显已经选了的规格
                    if(res.modelId == item.modelId){
                        this.gettersProductPorps.map((resChild,indexChild)=>{
                            resChild.children.map((sunChild,sunIndex)=>{
                                //if (curentIndex == index) {
                                if (res.propIds.includes(sunChild.id)) {
                                    sunChild.active = true;
                                    this.$set(this.gettersProductPorps[indexChild].children, sunIndex, this.gettersProductPorps[indexChild].children[sunIndex]);
                                } else {
                                    sunChild.active = false;
                                    this.$set(this.gettersProductPorps[indexChild].children, sunIndex, this.gettersProductPorps[indexChild].children[sunIndex]);
                                }
                                // }
                            })
                        })
                    }

                })
                if(curentIndex>this.gettersSetPropsId.length-1) { //当前设置了的规格长度大于当前点击的模型  说明该模型没设置规格信息
                    this.gettersProductPorps.map((resChild,indexChild)=>{
                        resChild.children.map((sunChild,sunIndex)=>{
                            sunChild.active = false;
                            this.$set(this.gettersProductPorps[indexChild].children, sunIndex, this.gettersProductPorps[indexChild].children[sunIndex]);
                        })
                    })
                }
            },
            materialOptation(item){ //材质应用
                this.areaCode=item.areaCode; //根据这个areaCode应用之后所有的模型区域
                this.rels=item.rels; //需要替换的内容
                this.gettersHardModelList.map((res,index)=>{
                    res.modelTextureInfo.map((model,modelChild)=>{
                        if(this.areaCode==model.areaCode){
                            model.rels=[];
                            model.rels.push(...this.rels);
                        }
                    })
                })
                this.$message.success('材质应用成功');
            },
            chooseProduct(){ //选择规格信息
                if(this.seclected){
                    this.$store.dispatch('setProductInfo',{setProductInfoTitle:'选择规格信息',setProductInfoNum:''});
                    this.gettersProductPorps.map(res=>{ //清除之前的回显
                        res.children.map(item=>{
                            item.active=false;
                        })
                    })
                    this.$store.dispatch('dialogMd',true);
                    console.log(this.gettersHardModelList)
                }else {
                    this.$message({
                        message: '请选择模型',
                        type: 'warning'
                    })
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .hardProduct{
        .pagePagination{
            text-align: center;
            padding: 30px 0;
        }
        .productBaseInfo {
            height: 40px;
            line-height: 40px;
            background-color: #f6f6f6;
            border-left: 4px solid #ff6419;
            color: #666;
            font-size: 16px;
            padding-left: 10px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .searchSelec{
            margin-left: 10px;
        }
        .searchModelbtn{
            margin-left: 10px;
            width: 80px;
        }
        .noMsg{
            text-align: center;
            line-height: 40px;
            color: #999;
        }
        .hardProduct-header{
            height: 60px;
            position: relative;
            padding-left: 20px;
            .productText{
                color: #999999;
                font-size: 14px;
                position: relative;
                top: 15px;
                cursor: pointer;
                i{
                    display: inline-block;
                    width: 14px;
                    height: 14px;
                    background: no-repeat center url("../../../../assets/images/icons/cube.png");
                    background-size: 100%;
                    margin-right: 5px;
                    vertical-align: middle;
                }
                .active{
                    background: no-repeat center url("../../../../assets/images/icons/cube_sct.png");
                    background-size: 100%;
                }
            }
            .active{
                color: #ff6419;
            }
            .connectModel{
                position: absolute;
                right: 20px;
                top: 0;
            }
        }
        .tableBox{
            border: solid 1px #dddddd;
            border-bottom: 0;
            .header-tab{
                display: flex;
                height: 40px;
                line-height: 40px;
                background-color: #fafafa;
                border-radius: 4px 4px 0px 0px;
                border-bottom: solid 1px #dddddd;
                align-items: center;
                span{
                    flex: 1;
                    text-align: center;
                }
                em{
                    display: inline-block;
                    width: 100px;
                    height: 40px;
                }
                i{
                    display: inline-block;
                    width: 14px;
                    height: 14px;
                    background-color: #ffffff;
                    border: solid 1px #ff6419;
                    margin-left: 20px;
                    cursor: pointer;
                }
                .active{
                    background: no-repeat center url('../../../../assets/images/icons/delivery1.png');
                    background-size: 100%;
                }
            }
            .hardProduct-list{
                ul{
                    li{
                        .hardProduct-top{
                            display: flex;
                            height: 132px;
                            //line-height: 132px;
                            align-items: center;
                            position: relative;
                            border-bottom: 1px solid #ddd;
                            z-index: 6;
                            i{
                                display: inline-block;
                                width: 14px;
                                height: 14px;
                                background-color: #ffffff;
                                border: solid 1px #ff6419;
                                margin-left: 20px;
                                cursor: pointer;
                            }
                            span,.operation{
                                flex: 1;
                                text-align: center;
                                position: relative;
                                img{
                                    position: absolute;
                                    top: 50%;
                                    left: 50%;
                                    transform: translate(-50%,-50%);
                                    width: 50px;
                                    height: 50px;
                                }
                                .disabledMainModel{
                                    color: #999999;
                                    font-size: 14px;
                                    line-height: 20px;
                                }
                            }
                            p{
                                line-height: 20px;
                                font-size: 14px;
                                color: #ff6419;
                                cursor: pointer;
                            }
                            .pullDownSection{
                                display: inline-block;
                                width: 80px;
                                height: 132px;
                                border-left: 1px solid #dddddd;
                                text-indent: 20px;
                                color: #ff6419;
                                position: relative;
                                cursor: pointer;
                                .pullDownSection-content{
                                    position: absolute;
                                    top: 50%;
                                    left: 50%;
                                    transform: translate(-50%,-50%);
                                    width: 80px;
                                }
                                strong{
                                    position: absolute;
                                    top: 20px;
                                    left: 50%;
                                    display: inline-block;
                                    width: 0;
                                    height: 0;
                                    border-left: 5px solid transparent;
                                    border-right: 5px solid transparent;
                                    border-top: 8px solid #ff6419;
                                }
                                em{
                                    position: absolute;
                                    top: 20px;
                                    left: 50%;
                                    display: inline-block;
                                    width: 0;
                                    height: 0;
                                    border-left: 5px solid transparent;
                                    border-right: 5px solid transparent;
                                    border-bottom: 8px solid #999;
                                }
                            }
                        }
                        .hardProduct-bottom{
                            position: relative;
                            z-index: 5;
                            border-top:1px solid #dddddd;
                            background-color: #fafafa;
                            padding: 20px;
                            .hardProductBox{
                                min-height: 178px;
                                background-color: #fff;
                                border:1px solid #dddddd;
                                padding: 20px;
                                box-sizing: border-box;
                                .materialName{
                                    font-size: 14px;
                                    color: #333333;
                                    span{
                                        width: 100px;
                                        background-color: #ffffff;
                                        border-radius: 4px;
                                        border: solid 1px #dddddd;
                                        color: #999999;
                                        font-size: 14px;
                                        padding: 2px 5px;
                                    }
                                    em{
                                        font-style: normal;
                                        width: 84px;
                                        background-color: #ffffff;
                                        border-radius: 15px;
                                        border: solid 1px #ff6419;
                                        color: #ff6419;
                                        padding: 5px 8px;
                                        margin-left: 40px;
                                        cursor: pointer;
                                    }
                                }
                                .upload-pic{
                                    display: flex;
                                    flex-wrap: wrap;
                                    .upload-pic-box{
                                        height: 90px;
                                        width: 90px;
                                        border: 1px dotted #ddd;
                                        margin-top: 20px;
                                        border-radius: 4px;
                                        img{
                                            display: block;
                                            margin: 25px auto 0 auto;
                                            object-fit: contain;
                                        }
                                        p{
                                            text-align: center;
                                            line-height: 32px;
                                            color: #666666;
                                            font-size: 14px;
                                        }
                                    }
                                    .upload-pic-list{
                                        height: 90px;
                                        width: 90px;
                                        border: 1px dotted #ddd;
                                        margin-top: 20px;
                                        border-radius: 4px;
                                        position: relative;
                                        img{
                                            position: absolute;
                                            top: 50%;
                                            left: 50%;
                                            transform: translate(-50%,-50%);
                                            width: 90px;
                                            height: 90px;
                                            //object-fit: none;
                                        }
                                        .delete-img{
                                            width: 18px;
                                            height: 18px;
                                            cursor: pointer;
                                        }
                                        .setDefaultPic{
                                            position: absolute;
                                            bottom: 0;
                                            width: 90px;
                                            line-height:24px;
                                            background: rgba(0,0,0,0.3);
                                            color: #fff;
                                            font-size: 12px;
                                            text-align: center;
                                            cursor: pointer;
                                        }
                                        em{
                                            position: absolute;
                                            top: 6px;
                                            left: 0;
                                            width: 64px;
                                            background-color: #ff6419;
                                            border-radius: 0 10px 10px 0;
                                            color: #fff;
                                            font-size: 12px;
                                            line-height: 16px;
                                            font-style: normal;
                                            padding: 1px 0;
                                            text-indent: 5px;
                                        }
                                    }
                                    .upload-pic-list:nth-child(2n+2){
                                        margin-left: 10px;
                                        margin-right: 10px;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

</style>