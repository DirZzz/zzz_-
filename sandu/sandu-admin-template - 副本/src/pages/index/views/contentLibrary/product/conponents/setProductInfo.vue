<template>
    <el-dialog width="540px"
               top="20%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :before-close="beforeClose"
               :title="gettersSetProductInfoTitle"
               :visible.sync="gettersSetProductInfo">
        <div class="setProductInfo">
             <div class="box" v-if="gettersSetProductInfoNum">编号：{{gettersSetProductInfoNum}}</div>
             <div class="box" v-for="(item,key) in gettersProductPorps" :key="key">
                 <div class="left">{{item.name}}：</div>
                 <div class="right">
                     <span @click="hasChecked(item,index,key)" v-for="(res,index) in item.children" :key="index">
                        <el-checkbox v-model="res.active" style="padding-left: 20px" >{{res.name}}</el-checkbox>
                     </span>
                 </div>
             </div>
             <div class="btn">
                 <input type="button" value="确定" class="confirm" @click.self="confirm">
                 <input type="button" value="取消" class="cancel" @click.self="beforeClose">
             </div>
        </div>
    </el-dialog>
</template>
<script>
    import {mapGetters} from 'Vuex'
    export default {
        name: "setProductInfo",
        data(){
            return{
                setProductInfo:false,
                totalProps:[],
                groupProps:[]
            }
        },
        computed:{
            ...mapGetters(['gettersSetPropsId','gettersSKUInfos','gettersHardModelList','gettersModelId','gettersProductPorps','gettersSetProductInfo','gettersSetProductInfoTitle','gettersSetProductInfoNum'])
        },
        methods:{
            beforeClose(){
                this.$store.dispatch('dialogMd',false);
            },
            hasChecked(item,index,key){
                item.active=!item.active;
                this.$set(this.gettersProductPorps[key].children,index,this.gettersProductPorps[key].children[index]);
            },
            editSku(propId){ ////修改当前的规格信息

                 console.log(this.gettersSetPropsId,9999)
                // console.log(this.gettersModelId)
                if(this.gettersSetPropsId.length>0){ //之前是否有值

                    if(this.gettersSetPropsId.some(res=>{ return res.modelId==this.gettersModelId })){ //如果在之前上面修改的true
                        this.gettersSetPropsId.map(item=>{ //重新替换之前的值
                            if(item.modelId==this.gettersModelId){
                                item.propIds=propId;
                                this.$store.dispatch('setPropsId',{isConcat:true,PropsId:this.gettersSetPropsId});//存放propIds
                            }
                        })
                    }else { //否则就是新修改的模型规格
                        console.log(555)
                        this.gettersSetPropsId.push({propIds:propId,modelId:this.gettersModelId});
                        this.$store.dispatch('setPropsId',{isConcat:true,PropsId:this.gettersSetPropsId});//存放propIds
                    }

                }else { //初始化时添加
                    // console.log(this.totalProps)
                    this.gettersSetPropsId.push({propIds:propId,modelId:this.gettersModelId});
                    this.$store.dispatch('setPropsId',{isConcat:true,PropsId:this.gettersSetPropsId});//存放propIds
                }
                //console.log(this.gettersSetPropsId)
            },
            groupCheck(propId){ //多选规格
                let array=[];
                console.log(this.gettersHardModelList,666)
               this.gettersHardModelList.map(res=>{
                   if(res.hasChecked&&res.iSShow) {//选中的列表   iSShow控制帅选出来的选中列表
                       array.push({
                           modelId:res.modelId,
                           propIds:propId,
                       })
                   }
               })
                this.$store.dispatch('setPropsId',{isConcat:true,PropsId:array});
            },
            confirm(){
                let propId=[];
                this.gettersProductPorps.map(res=>{  //回显的id
                    res.children.map(item=>{
                        if(item.active){
                            propId.push(item.id);
                        }
                    })
                })
                this.$store.dispatch('dialogMd',false);
                if(this.gettersSetProductInfoTitle=='选择规格信息'){
                    this.groupCheck(propId);
                    this.$message.success('修改规格信息成功!');
                }else {
                    this.editSku(propId);
                     this.$message.success('修改规格信息成功!');
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .setProductInfo{
        .box{
            display: flex;
            flex-wrap: wrap;
            margin-bottom: 10px;
            .left{

            }
            .right{
                flex: 1;
            }
        }

        .btn{
            border-top: 1px solid #ddd;
            padding-top: 20px;
            padding-bottom: 20px;
            position: relative;
            input{
                width: 60px;
                height: 30px;
                background-color: #ff6419;
                border-radius: 15px;
                color: #fff;
                font-size: 14px;
                cursor: pointer;
            }
            .confirm{
                position: absolute;
                right: 80px;
            }
            .cancel{
                background-color: #ffdbc9;
                color: #ff6419;
                position: absolute;
                right: 0;
            }
        }
    }

</style>