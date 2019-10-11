<template>
    <el-dialog
            :visible.sync="addGoodsDialog"
            width="990px"
            class="addGoods"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="handleClose">
        <div slot="title" class="title">{{title}}<em v-if="submitList.length>0">（已选够{{submitList.length}}个）</em></div>
        <el-row style="margin-bottom: 20px">
            <el-col :span="4">
                <el-select v-model="hasModelOrMaterial" placeholder="有无贴图/模型" clearable>
                    <el-option
                      v-for="item in modelOrMaterialOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
            </el-col>
            <el-col :span="6" style="margin-left: 20px">
                <el-input v-model="goodsName" placeholder="请输入商品名称" clearable></el-input>
            </el-col>
            <el-col :span="6" style="margin: 0 20px">
                <el-input v-model="goodsCode" placeholder="请输入商品编码" clearable></el-input>
            </el-col>
            <el-col :span="4">
                <el-button type="text" class="left" @click="search">搜索</el-button>
            </el-col>
        </el-row>
        <el-table
                :data="list"
                style="width: 100%">
            <el-table-column
                    type="index"
                    label="编号"
                    width="70">
            </el-table-column>
            <el-table-column
                    label="商品"
                    label-class-name="wordLeft"
                    width="310">
                <template slot-scope="scope">
                    <div class="goods-content">
                        <div class="goods-img">
                            <img :src="BASE_URL.sourceBaseUrl+scope.row.pic" alt="">
                            <i :class="{'ic-select-active':scope.row.active}"></i>
                        </div>
                        <div class="goods-text">
                            <el-tooltip class="item" effect="dark" :content="scope.row.spuName" placement="top" v-if="scope.row.spuName.length>30">
                                <p>{{scope.row.spuName}}</p>
                            </el-tooltip>
                            <p v-else>{{scope.row.spuName}}</p>
                            <p>编码：{{scope.row.spuCode}} </p>
                            <p>分类：{{scope.row.bigType}} >{{scope.row.smallType}} </p>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="modelOrMaterial"
                    width="200"
                    label="模型/贴图">
            </el-table-column>
            <el-table-column
                    prop="minPrice"
                    width="120"
                    label="售价">
            </el-table-column>
            <el-table-column
                    prop="totalInventory"
                    width="100"
                    label="库存">
            </el-table-column>
            <el-table-column
                    label="操作">
                <template slot-scope="scope">
                    <div class="addBtn" @click="addGoodList(scope.row,scope.$index)" v-if="!scope.row.active">添加</div>
                    <div class="addBtn" @click="cancelGoodList(scope.row,scope.$index)" v-else>取消</div>
                </template>
            </el-table-column>
        </el-table>
        <div class="pageFrame">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-size="6"
                    layout="total,prev, pager, next, jumper"
                    :total="totalPage">
            </el-pagination>
        </div>

    </el-dialog>
</template>

<script>
    import { mapState,mapActions } from 'Vuex'
    export default {
        name: "addGoods",
        data(){
            return{
                dialogVisible:false,
                title:'添加商品',
                currentPage:1,
                goodsName:'',
                goodsCode:'',
                hasModelOrMaterial: null,
                modelOrMaterialOptions:[{
                    label: "有",
                    value: 1
                },{
                    label: "无",
                    value: 0
                }]
            }
        },
        computed:{
            ...mapState('homeManage',['addGoodsDialog','goodsList','submitGoodsList','isHot','newGoodsList','submitNewGoodsList','replaceIndex','isReplace','totalPage','moreGoodsList','submitMoreGoodsList']),
            list(){
                if(this.isHot==0){
                  return  this.goodsList
                }else if(this.isHot==1){
                    return  this.newGoodsList
                }else {
                    return  this.moreGoodsList
                }
            },
            submitList(){
                if(this.isHot==0){
                    return  this.submitGoodsList
                }else if(this.isHot==1){
                    return  this.submitNewGoodsList
                }else {
                    return  this.submitMoreGoodsList
                }
            }
        },
        created(){

        },
        methods:{
            ...mapActions('homeManage',['dialog','setGoodsList','setSubmitGoodsList','setSubmitNewGoodsList','setReplace','setSubmitMoreGoodsList']),
            handleCurrentChange(val){
                this.setGoodsList({vm:this,page:val,type:this.isHot,name:this.goodsName,code:this.goodsCode, hasModelOrMaterial: this.hasModelOrMaterial})
            },
            handleClose(){
                this.dialog(false);
                this.goodsName='';
                this.goodsCode='';
                this.hasModelOrMaterial = null;
                this.currentPage=1;
                this.setReplace({index:'',isReplace:false}); //清除替换
                this.search();
            },
            search(){
                this.setGoodsList({vm:this,page:1,name:this.goodsName,code:this.goodsCode,type:this.isHot, hasModelOrMaterial: this.hasModelOrMaterial})
            },
            addGoodList(res,index){ //isHot=0爆款  1新品
                res.picAddress=res.pic;
                if(this.isReplace){//替换功能
                    switch (this.isHot){
                        case 0 : this.setSubmitGoodsList({res:res,type:2});break;
                        case 1 : this.setSubmitNewGoodsList({res:res,type:2});break;
                        case 2 : this.setSubmitMoreGoodsList({res:res,type:2});break;
                    }
                    this.$message.success('替换成功');
                    this.handleClose();
                }else { //添加
                    if(this.submitList.length<10){
                        res.active=true;
                        Object.assign(this.list[index],res);
                        this.$set(this.list,index,this.list[index]);
                        switch (this.isHot){ //添加到新的数组 1添加
                            case 0 : this.setSubmitGoodsList({res:res,type:1});break;
                            case 1 : this.setSubmitNewGoodsList({res:res,type:1});break;
                            case 2 : this.setSubmitMoreGoodsList({res:res,type:1});break;
                        }
                    }else {
                        this.$message.error('已达到上限');
                    }
                }
            },
            cancelGoodList(res,index){
                res.active=false;
                Object.assign(this.list[index],res);
                this.$set(this.list,index,this.list[index]);
                switch (this.isHot){ //从新的数组里删除 0删除
                    case 0 : this.setSubmitGoodsList({res:res,type:0});break;
                    case 1 : this.setSubmitNewGoodsList({res:res,type:0});break;
                    case 2 : this.setSubmitMoreGoodsList({res:res,type:0});break;
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
.addGoods{
    .title{
        em{
            color: #ff2323;
            font-style: normal;
        }
    }
   .pageFrame{
       text-align: right;
       margin-top: 20px;
   }
    .goods-content{
        display: flex;
        .goods-img{
            position: relative;
            width: 60px;
            height: 60px;
            img{
                width: 60px;
                height: 60px;
            }
            .ic-select-active{
                position: absolute;
                right: 0;
                bottom: 0;
                display: inline-block;
                width: 20px;
                height: 20px;
                background: no-repeat center url("../../../assets/images/select_active.png");
                background-size: 100%;
            }
        }

        .goods-text{
            color: #999;
            margin-left: 20px;
            p{
                text-align: left;
            }
            p:nth-of-type(1){
                width: 420px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                cursor: pointer;
            }
        }
    }
    .addBtn{
        color: #FF6419;
        font-size: 14px;
        cursor: pointer;
    }
}
</style>
<style lang="scss">
    .addGoods{
        .wordLeft{
            text-align: left!important;
        }
    }

</style>
