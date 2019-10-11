<template>
    <el-dialog
            :visible.sync="selectMergeProduct"
            width="960px"
            class="selectMergeProduct"
            title="选择合并的产品"
            :append-to-body='true'
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="handleClose">
        <el-form :model="formInline" class="selectMergeProduct-form">
            <el-row>
                <el-col :span="12">
                    <el-form-item>
                        <el-input v-model="formInline.productCode" placeholder="请输入产品编码"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item>
                        <el-cascader
                                v-model="productValue"
                                :options="productCategory"
                                :props="props"
                                filterable
                                clearable
                                change-on-select
                                placeholder="请选择产品分类"
                                @change="handleChange">
                        </el-cascader>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row>
                <el-col :span="12">
                    <el-form-item>
                        <el-input v-model="formInline.productName" placeholder="请输入产品名称"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item>
                        <el-select v-model="formInline.modelingId" clearable placeholder="请输入产品款式">
                            <el-option
                                    v-for="item in productStyle"
                                    :key="item.id"
                                    :label="item.styleName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item>
                <el-input v-model="formInline.productModelNumber" placeholder="请输入产品型号"></el-input>
            </el-form-item>
        </el-form>
        <div class="selectMergeProduct-btn">
            <el-button type="primary" round @click="search">搜索</el-button>
            <el-button round @click="reset">重置</el-button>
        </div>
        <div style="text-align: right">
            <el-button type="primary" round @click="confirmAdd">确认添加</el-button>
        </div>
        <div class="tableList">
            <el-table
                    :header-cell-style="rowStyle"
                    ref="multipleTable"
                    :data="tableData"
                    v-loading="loading"
                    height="600"
                    @selection-change="handleSelectionChange"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%;margin-top: 20px"
                    align="center"
            >
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="序号" type="index"  align="center" ></el-table-column>
                <el-table-column label="产品缩略图" header-align="center" align="center">
                    <template slot-scope="scope">
                        <img :src="scope.row.picPath" alt="" class="pic">
                    </template>
                </el-table-column>
                <el-table-column label="产品编码" prop="code" header-align="center" align="center"></el-table-column>
                <el-table-column label="产品名称" prop="productName" header-align="center" align="center"></el-table-column>
                <el-table-column label="产品分类" prop="categoryNames" header-align="center" align="center"></el-table-column>
                <el-table-column label="产品型号" prop="modelNumber" header-align="center" align="center"></el-table-column>
                <el-table-column label="产品款式" prop="modelingName" header-align="center" align="center"></el-table-column>
            </el-table>
        </div>

        <!--分页组件-->
        <div class="pageFrame">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[5,10, 50, 100, 200,500]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </el-dialog>
</template>

<script>
    import { map } from 'lodash'
    export default {
        name: "selectMergeProduct",
        props:{
            tableList: {
                type: Array,
                default: []
            }
        },
        data(){
           return{
               currentPage:1,
               pageSize:10,
               productValue:[],
               total:0,
               loading:false,
               selectMergeProduct:false,
               formInline:{
                   page:1,
                   limit:10,
                   mergeFlag:1,
                   queryType:'library',
                   companyId:sessionStorage.getItem('companyID'),
                   bigProductType:null,
                   productType:null,
                   productCode:null,
                   productName:null,
                   modelingId:null,
                   productModelNumber:null,
                   excludeProductIds:[]
               },
               productCategory:[],
               tableData:[],
               productStyle:[],
               selectList:[],
               props: {
                   label: "name",
                   value: "keyCode",
                   children: "children"
               },
           }
        },
        created(){

        },
        methods:{
            pageList(){
                for (let key in this.formInline) {
                    this.formInline[key] ? '' : delete this.formInline[key]
                }
                this.API.ProductList(this.formInline).then(res=>{
                    this.tableData=res.list;
                    this.total=res.total;
                })
            },
            search(){
                this.formInline.page=1;
                this.pageList();
            },
            reset(){
                for (let key in this.formInline) {
                    if(key!='page' && key!='limit'&& key!='queryType' && key!='companyId'&& key!='excludeProductIds'){
                        this.formInline[key]='';
                    }
                }
                this.productValue=[];
                this.pageList();
            },
            getProductCategory(){
                this.API.ProductCategory().then(res=>{
                    this.productCategory=res.data;
                })
            },
            getProductStyleDrop(){
                this.API.ProductStyleDrop().then(res=>{
                    this.productStyle=res.data;
                })
            },
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '60px!important',background:'#f5f5f5',color: '#666'}
                }
            },
            toggle(type){
                this.selectMergeProduct=type;
                if(type){
                    this.getProductCategory(); //产品分类
                    this.getProductStyleDrop();////获取产品款式
                    this.formInline.excludeProductIds=map(this.tableList,'id').join(',');
                    this.pageList();
                }
            },
            handleClose(){
                this.selectMergeProduct=false;
                this.$refs.multipleTable.clearSelection();
            },
            handleSelectionChange(val){
                this.selectList=val;
            },
            confirmAdd(){
                if(this.selectList.length>0){
                    this.selectMergeProduct=false;
                    this.$emit('tableList',this.selectList);
                    this.$refs.multipleTable.clearSelection();
                }else {
                    this.$message.error('至少选择一个');
                }

            },
            handleChange(){
               this.formInline.bigProductType=this.productValue[1];
               this.formInline.productType=this.productValue[2];
            },
            handleSizeChange(val){
                this.formInline.limit=val;
                this.pageList();
            },
            handleCurrentChange(val){
                this.formInline.page=val;
                this.pageList();
            }
        }
    }
</script>

<style lang="scss" scoped>
  .selectMergeProduct{
      &-btn{
          text-align: center;
       }
      &-form{
          //text-align: center;
      }
      .pageFrame{
          text-align: center;
          padding-top: 20px;
      }
      .pic{
          width: 100%;
          height: 100px;
          object-fit: contain;
      }
      .tableList{

      }
  }
</style>
<style lang="scss">
    .selectMergeProduct{
        &-form{
            .el-input__inner{
                width: 420px;
            }
        }
    }

</style>