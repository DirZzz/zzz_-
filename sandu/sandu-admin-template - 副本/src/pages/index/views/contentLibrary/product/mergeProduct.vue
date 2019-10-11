<template>
    <div class="mergeProduct">
        <v-nav :nav="nav"></v-nav>
        <div class="mergeProduct-header">产品合并</div>
        <div class="mergeProduct-content">
            <div class="title"><span></span>合并基本信息</div>
            <div class="form">
                <el-form label-width="120px" :model="formLabelAlign">
                    <el-row :gutter="78">
                        <el-col :span="8">
                            <el-form-item label="产品名称：">
                                <el-input v-model="formLabelAlign.productName" style="width: 300px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="产品系列：">
                                <el-select v-model="formLabelAlign.seriesId" clearable placeholder="请选择" style="width: 300px">
                                    <el-option
                                            v-for="item in productSeries"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="78">
                        <el-col :span="8">
                            <el-form-item label="产品型号：">
                                <el-input v-model="formLabelAlign.modelNumber" style="width: 300px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="产品款式：">
                                <el-select v-model="formLabelAlign.modelingId" clearable  placeholder="请选择" style="width: 300px">
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
                    <el-form-item label="产品图片：">
                        <ul class="uploadPic">
                            <li style="margin: 5px 0 5px 20px;background-color: #fff">
                                <el-upload
                                        class="avatar-uploader"
                                        :action='BASE_URL.uploadUrl'
                                        :file-list="fileList2"
                                        :show-file-list="false"
                                        :headers="headerToken"
                                        :data="uploadParams"
                                        list-type="picture-card"
                                        multiple
                                        :on-success="handleAvatarSuccess"
                                        :before-upload="beforeAvatarUpload">
                                    <!--<i class="el-icon-plus avatar-uploader-icon"></i>-->
                                    <div class="avatar-uploader-icon">
                                        <div class="across"></div>
                                        <div class="vertical"></div>
                                    </div>
                                    <p class="img-text">上传图片</p>
                                </el-upload>
                            </li>
                            <li class="uploadImgList"
                                v-for="(file, index) in fileList2" :key="index"
                                v-dragging="{ item: file, list: fileList2, group: 'file' }"
                            >
                                    <span class="posDelete" v-show="showDeletes[index]" @click="deleteUploadImg(index)">
                                        <img src="../../../assets/images/icons/delete3.png" width="17" height="16" alt="">
                                    </span>
                                <img style="border-radius: 8px;object-fit: contain" width="90" height="90"
                                     :src="file.url"/>
                                <span v-if="file.id == (defaultPicId || fileList2[0].id)"
                                      class="upload-mainpic__con">主缩略图</span>
                                <span v-show="showDeletes[index]" @click="setDefaultPic(file,index)"
                                      class="upload-notmainpic__con">设置为缩略图</span>
                            </li>
                        </ul>
                    </el-form-item>
                </el-form>
            </div>
            <div class="title"><span></span>合并产品编码</div>
            <div class="tableList">
                <el-button type="primary" round class="btn" @click="chooseMerge">选择合并产品</el-button>
                <el-table
                        :header-cell-style="rowStyle"
                        :data="currentTableList"
                        v-loading="loading"
                        @selection-change="handleSelectionChange"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        style="width: 100%"
                        align="center"
                >
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column label="序号" type="index"  align="center" ></el-table-column>
                    <el-table-column label="产品图片" header-align="center" align="center">
                        <template slot-scope="scope">
                            <img :src="filterImg(scope.row.picPath)" alt="" class="pic">
                        </template>
                    </el-table-column>
                    <el-table-column label="产品编码" prop="code" header-align="center" align="center"></el-table-column>
                    <el-table-column label="产品名称" prop="productName" header-align="center" align="center"></el-table-column>
                    <el-table-column label="模型编码" prop="modelCode" header-align="center" align="center"></el-table-column>
                    <el-table-column label="模型尺寸" header-align="center" align="center">
                        <template slot-scope="scope">
                            <span>长：{{scope.row.length}} 宽：{{scope.row.width}} 高：{{scope.row.height}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" header-align="center" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" @click="setProduct(scope.row)" v-if="!scope.row.isMainId">设为标准产品</el-button>
                            <el-button type="text" @click="$router.push(`/editProduct/${scope.row.id}`)">编辑</el-button>
                            <el-button type="text" @click="deleteMerge(scope.row)">取消合并</el-button>
                        </template>
                    </el-table-column>
                </el-table>
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
            </div>
        </div>
        <div class="mergeBtn">
            <el-button type="primary" round @click="confirmMerge">确定合并</el-button>
            <el-button round style="width: 104px" @click="$router.push('/product')">取消</el-button>
        </div>
        <selectMergeProduct ref="selectMergeProduct" :tableList="tableData" @tableList="getTableList"></selectMergeProduct>
    </div>
</template>

<script>
    import qs from 'qs'
    import { map } from 'lodash'
    import selectMergeProduct from './conponents/selectMergeProduct';
    export default {
        name: "mergeProduct",
        components:{
            selectMergeProduct
        },
        data(){
            return{
                loading:false,
                currentPage:1,
                pageSize:10,
                total:0,
                nav:[
                    {nav: '产品库', url: '/product'},
                    {nav: '产品合并', url: ''}
                ],
                value:'',
                headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
                uploadParams: {
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                formLabelAlign:{
                    curMainProductId:null,
                    defaultPicId:null,
                    modelNumber:null,
                    modelingId:null,
                    picIds:null,
                    preMainProductId:null, //之前的主产品ID ,
                    productIds:null,
                    productName :null,
                    seriesId :null
                },
                popupImageUrl:'',
                defaultPicId:'',
                onePic:'',
                showDeletes: [],
                fileList2:[],
                tableData:[],//列表所有数据
                currentTableList:[],//当前列表的数据
                productSeries:[],
                productStyle:[],
            }
        },
        created(){
            this.getProductSeriesDrop();//获取产品系列
            this.getProductStyleDrop();//获取产品款式
            if(this.$route.params.id !=0){
                this.productDetail()
            }
        },
        methods:{
            filterImg(url){
                if(url && url.indexOf('http')>-1){
                    return url;
                }else {
                    return this.BASE_URL.sourceBaseUrl+url;
                }
            },
            productDetail(){
                this.API.ProductDetail({
                    productId:this.$route.params.id,
                    platformType:'library'
                }).then(res=>{
                    this.formLabelAlign.defaultPicId=res.data.defaultPicId;
                    this.formLabelAlign.modelNumber=res.data.modelNumber;
                    this.formLabelAlign.productName=res.data.name;
                    this.formLabelAlign.modelingId=res.data.modelingId;
                    this.formLabelAlign.seriesId=res.data.seriesId;
                    this.formLabelAlign.preMainProductId=this.$route.params.id;
                    res.data.picInfos.map(res=>{
                        this.fileList2.push({
                            id:res.id,
                            url: res.path.indexOf('http')>-1 ? res.path : this.BASE_URL.sourceBaseUrl+res.path
                        })
                    })
                    this.tableData=res.data.mergeProducts;
                    //回显主产品
                    this.tableData.map(res=>{
                        res.id == this.$route.params.id ? res.isMainId=true : ''
                    })
                    this.total=this.tableData.length;
                    this.currentTableList=this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize);
                    console.log(this.tableData,'tableData')
                })
            },
            getProductSeriesDrop(){
                this.API.ProductSeriesDrop().then(res=>{
                     this.productSeries=res.data;
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
            getTableList(list){
                if(this.tableData){
                    this.tableData=[...this.tableData,...list];
                    let obj={}; //去重
                    this.tableData=this.tableData.reduce((cur,next) =>{
                        obj[next.id] ? '' : obj[next.id]=true && cur.push(next);
                        return cur
                    },[])
                }else {
                    this.tableData=Object.assign([],list);
                }
                this.total=this.tableData.length;

                this.hasMainId();
                this.currentTableList=this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize);
            },
            setProduct(item){
                this.tableData.map(res=> res.isMainId=false);
                this.tableData.map((res,index)=>{
                    if(res.id == item.id){
                        res.isMainId=true;
                        this.$set(this.tableData,index,this.tableData[index]);
                    }
                })
                this.currentTableList=this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize);
            },
            deleteMerge(item){
                this.tableData=this.tableData.filter(res=>{
                    return res.id!=item.id;
                });
                this.total=this.tableData.length;
                this.hasMainId();
                this.currentTableList=this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize);
            },
            hasMainId(){
                if(!this.tableData.some(item=>item.isMainId)){ //判断是否有主id
                    this.tableData.map((res,index)=>{
                        index == 0 ? res.isMainId=true : '';
                    })
                }
            },
            confirmMerge(){
                this.fillParam();
            },
            fillParam(){
                if(this.tableData.length>0){
                    this.fileList2.length>0 ? this.formLabelAlign.picIds=map(this.fileList2,'id').join(',') : '';
                    this.formLabelAlign.productIds=map(this.tableData,'id');
                    this.fileList2.length>0 ? this.formLabelAlign.defaultPicId =this.defaultPicId || this.fileList2[0].id : '';
                    let fil=this.tableData.filter(res=>{ return res.isMainId });
                    this.formLabelAlign.curMainProductId=(fil.length>0 && fil[0].id) || this.tableData[0].id;

                    this.API2.mergeHardProduct(this.formLabelAlign).then(res=>{
                        if(res.success){
                            this.$message.success('合并成功！');
                            this.$router.push('/product');
                        }else {
                            this.$message.error(res.message);
                        }
                    })
                }else {
                    this.$message.error('请选择合并的产品');
                }

            },
            handleAvatarSuccess(res, file) {
                // this.imageUrl = URL.createObjectURL(file.raw);
                this.fileList2.push({
                    url: res.data.url,
                    id: res.data.resId
                })
                this.onePic = this.fileList2[0].id
            },
            setDefaultPic(file,index) {
                this.defaultPicId = file.id;
                this.fileList2.splice(index,1);
                this.fileList2.unshift(file);
            },
            deleteUploadImg(index) {
                this.fileList2.splice(index, 1)
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg'
                const isPNG = file.type === 'image/png'
                const isLt2M = file.size / 1024 / 1024 < 10

                if (!isJPG && !isPNG) {
                    this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
                }
                if (!isLt2M) {
                    this.$message.error('图片大小不能超过 10MB!')
                }
                return (isJPG || isPNG) && isLt2M
            },
            chooseMerge(){
                this.$refs.selectMergeProduct.toggle(true);
            },
            handleSizeChange(val){
                this.pageSize=val;
                this.currentTableList=this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize);
            },
            handleCurrentChange(val){
                this.currentPage=val;
                this.currentTableList=this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize);
            },
            handleSelectionChange(){

            }
        }
    }
</script>

<style scoped lang="scss">
   .mergeProduct{
       &-header{
           height: 60px;
           line-height: 60px;
           background-color: #fff;
           border-bottom: 1px solid #f5f5f5;
           text-indent: 20px;
           font-size: 18px;
       }
       &-content{
           .title{
               background-color: #fff;
               font-size: 16px;
               line-height: 40px;
               text-indent: 20px;
               span{
                   display: inline-block;
                   width: 5px;
                   height: 15px;
                   background-color: #ff6419;
                   vertical-align: middle;
                   margin-right: 10px;
               }
           }
           .form{
               background-color: #fff;
               padding: 20px 0;

               .uploadPic {
                   margin-right: 25px;
                   background: #eee;
                   padding-top: 15px;
                   box-sizing: padding-box;
                   li {
                       font-size: 12px;
                       margin: 5px;
                       height: 90px;
                       width: 90px;
                       overflow: hidden;
                       display: inline-block;
                       background-color: #fff;
                       border-radius: 8px;
                       position: relative;
                       text-align: center;
                       &:hover{
                           .upload-notmainpic__con,.posDelete,.posDelete2{
                               display: inline-block!important;
                           }
                       }
                       .upload-notmainpic__con {
                           position: relative;
                           top: -45px;
                           background: rgba(0, 0, 0, 0.7);
                           width: 90px;
                           color: #fff;
                           //display: inline-block;
                           line-height: 22px;
                           border-radius: 0 0 8px 8px;
                           cursor: pointer;
                           display: none;
                       }
                       .posDelete {
                           position: absolute;
                           width: 40px;
                           height: 40px;
                           left: 50%;
                           top: 50%;
                           transform: translate(-50%, -50%);
                           line-height: 40px;
                           color: #fff;
                           font-size: 20px;
                           border-radius: 6px;
                           text-align: center;
                           display: none;
                       }

                       .posDelete2 {
                           position: absolute;
                           background: url('../../../assets/images/icons/delete3.png') 0 center no-repeat;
                           width: 17px;
                           height: 16px;
                           left: 50%;
                           top: 50%;
                           transform: translate(-50%, -50%);
                           line-height: 40px;
                           color: #fff;
                           font-size: 20px;
                           border-radius: 6px;
                           text-align: center;
                           display: none;
                       }
                   }
               }
               .avatar-uploader-icon {
                   position: relative;
                   background-color: #FF6419;
                   width: 20px;
                   height: 20px;
                   margin: 20px auto 10px;
                   border-radius: 50%;
                   .across {
                       position: absolute;
                       top: 9px;
                       left: 2px;
                       background-color: #fff;
                       width: 16px;
                       height: 2px;
                   }
                   .vertical {
                       position: absolute;
                       top: 2px;
                       left: 9px;
                       background-color: #fff;
                       width: 2px;
                       height: 16px;
                   }
               }
               .img-text {
                   width: 100%;
                   height: 20px;
                   font-size: 14px;
                   line-height: 20px;
                   color: #999;
               }
               .upload-mainpic__con {
                   position: relative;
                   top: -45px;
                   background-color: #ff641e;
                   width: 90px;
                   color: #fff;
                   display: inline-block;
                   line-height: 22px;
                   border-radius: 0 0 8px 8px;
               }
           }
           .tableList{
               background-color: #fff;
               .btn{
                   margin: 10px 20px 20px 20px;
               }
               .pageFrame{
                   text-align: center;
                   padding: 20px 0;
               }
           }
       }
       .pic{
           width: 100%;
           height: 100px;
           object-fit: contain;
       }
       .mergeBtn{
           background-color: #fff;
           text-align: center;
           padding: 20px 0;
       }
   }
</style>