<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
      <header>
        <h1>编辑模型</h1>
      </header>
      <p><span>*</span>为必填项</p>

      <el-row :gutter="24" class="splineFrame">
        <el-col :span="2" class="productBaseInfo">模型基础信息</el-col>
        <el-col :span="22" class="rightLine"><hr></el-col>
      </el-row>

      <div>
        <el-form :model="model" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm"> <!--:rules="rules" ref="ruleForm"-->
          <el-row>
            <el-col :span="12">
              <el-form-item label="模型编码：">
                {{model.modelCode}}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="6">
              <el-form-item label="模型名称：" prop="modelName">
                <input  class="sizes" style="width: 100%" v-model="model.modelName" maxlength=30>
              </el-form-item>
            </el-col>
            <el-col :span="6" class="hint" style="margin-left: 20px">
              <el-form-item label="模型型号：" prop="modelModelNum">
                <input class="sizes"  style="width: 100%" v-model="model.modelModelNum"  maxlength=20>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="6">
              <el-form-item label="模型分类：" prop="categoryIds">
                <el-cascader style="width: 100%"
                  v-model="model.categoryIds"
                  placeholder="请选择"
                  :props="props"
                  :options="productCategoryDrop"
                  filterable>
                </el-cascader>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-form-item label="模型图片：">
              <single-image-upload :on-success="handleModelImgSuccess"
                                   @deleteClick="modelImageUrl = ''"
                                   :image-url="modelImageUrl"></single-image-upload>
            </el-form-item>
          </el-row>

          <el-row>
            <el-col>
              <el-form-item label="描述：">
                <el-input
                  class="textareaWidth"
                  type="textarea"
                  :rows="6"
                  placeholder="请输入内容"
                  v-model="model.modelDesc">
                </el-input>
                <p style="padding-top: 10px">此处最多允许填写200字</p>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="splineFrame">
            <el-col :span="2" class="productBaseInfo">模型3d文件</el-col>
            <el-col :span="22" class="rightLine"><hr></el-col>
          </el-row>

          <el-row>
            <el-col>
              <el-form-item label="模型3d文件：">
                <ul class="modelNameShow">
                  <li style="display: flex">
                    <span v-if="modelFile.path">{{modelFile.name}}</span>
                    <el-upload
                            :action="BASE_URL.modelUploadUrl  +'/model'"
                            :show-file-list="false"
                            :on-success="handleModelSuccess"
                            :before-upload="before3DUUpload"
                    >
                      <el-button style="background-color: #FF6419;border-radius: 20px;color: #fff;margin-left: 10px">选择文件</el-button>
                    </el-upload>
                  </li>
                </ul>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="splitLine">
            <span>规格：</span> <span>&nbsp;&nbsp;长：{{length}}&nbsp;&nbsp;宽：{{width}}&nbsp;&nbsp;高：{{height}}</span>
          </div>

          <!--可替换材质区域-->
          <el-row class="texture">
            <!--<el-col :span="24" class="texture-btn">-->
              <!--<el-button type="primary" class="confirmBtn marRight" round @click="operationTexture('addTexture')">添加材质区域</el-button>-->
              <!--<el-input-number class="confirmBtn marRight" v-model="num" :min="1" :max="10" label="描述文字"></el-input-number>-->
            <!--</el-col>-->
            <el-col :span="24" class="texture-area" v-if="textureList.length > 0">
              <div class="texture-area-item" v-for="(item, index) in textureList">
                <div class="header">
                  <div>
                    <span class="texture-code">{{Number(item.areaCode) + 1}}</span>
                    材质区域名称：<el-input class="texture-name" size="mini" v-model="item.areaName"></el-input>
                    <span class="setUV">UV(cm):
                        <em>长</em><el-input disabled class="texture-name" size="mini" v-model="item.height"></el-input>
                        <em>宽</em><el-input disabled class="texture-name" size="mini" v-model="item.width"></el-input>
                      </span>
                  </div>
                  <!--<span @click="operationTexture('deleteTexture', index)">删除材质区域</span>-->
                </div>
                <div class="content">
                  <div class="content-item">
                    <div class="avatar-uploader" @click="operationTexture('addImg', index)">
                      <div></div>
                    </div>
                  </div>
                  <div class="content-item"
                       @mouseover="showIndex = index; showImgIndex = imgIndex"
                       @mouseout="showIndex = -1; showImgIndex = -1"
                       v-for="(ImgItem, imgIndex) in item.rels">
                    <span class="default-icon" v-if="ImgItem.defaultFlag">主缩略图</span>
                    <span @click="operationTexture('DefaultImg', index, imgIndex)"
                          :class="(!ImgItem.defaultFlag&&showIndex == index && showImgIndex == imgIndex)? 'default-select-active' : 'default-select'">
                      设为主缩略图
                    </span>
                    <span class="content-item-delete"
                          v-show="showIndex == index && showImgIndex == imgIndex"
                          @click="operationTexture('deleteImg', index, imgIndex)"></span>
                    
             <template>
              <el-popover
                placement="top-start"
                width="300"
                trigger="hover"
               >
               <div>
                 <p>材质编码:{{item.rels[imgIndex].textureCode}}</p>
                 <p>材质型号:{{item.rels[imgIndex].modelNumber}}</p>
                 <p>材质名称:{{item.rels[imgIndex].textureName}}</p>
               </div>
                 <img slot="reference" :src="BASE_URL.sourceBaseUrl + (ImgItem.thumbnailPath || ImgItem.picPath)">
                  </el-popover>
                 </template>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
          <!--可替换材质区域-->
          <!--可以替换材质区域添加贴图弹框-->
          <el-dialog title="添加贴图" :visible.sync="showAddTexture" class="img-pop-up">
            <el-row class="img-pop-up-seek">
              <el-col :span="12" class="img-pop-up-input">
                <el-select v-model="texturePopUp.texture" clearable placeholder="请选择材质/材料" style="width: 100%">
                  <el-option
                          v-for="item in seekTexture"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="12" class="img-pop-up-input">
                <el-select v-model="texturePopUp.textureAttr" clearable placeholder="请选择贴图表面属性" style="width: 100%">
                  <el-option
                          v-for="item in seekSurfaceList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="12" class="img-pop-up-input">
                <el-select v-model="texturePopUp.textureType" clearable placeholder="请选择贴图类型" style="width: 100%">
                  <el-option
                          v-for="item in seekType"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="12" class="img-pop-up-input">
                <el-input placeholder="请输入材质贴图名称" v-model="texturePopUp.textureName"></el-input>
              </el-col>
              <el-col :span="12" class="img-pop-up-input">
                <el-input placeholder="请输入材质型号" v-model="texturePopUp.modelNumber"></el-input>
              </el-col>
              <el-col :span="12" class="img-pop-up-input">
                <el-input placeholder="请输入材质贴图编号" v-model="texturePopUp.textureCode"></el-input>
              </el-col>
              <el-col :span="24" class="img-pop-up-input">
                <el-button round @click="seekTextureList('reset')">重置</el-button>
                <el-button round @click="seekTextureList('seek')">搜索</el-button>
              </el-col>
            </el-row>
            <div class="table-txt">
              共 {{totalCount}} 张，已选 {{textureChange.length}} 张
              <el-button round class="el-button--primary" @click="confirmAdd">确定添加</el-button>
            </div>
            <div class="texture-table">
              <el-table
                      ref="multipleTable"
                      :data="tableImg"
                      tooltip-effect="dark"
                      style="width: 100%;"
                      @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        label="全选"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="thumbnailPath"
                        label="缩略图"
                        width="180">
                  <template slot-scope="scope">
                    <img v-if="scope.row.thumbnailPath" :src="BASE_URL.sourceBaseUrl + scope.row.thumbnailPath" class="texture-table-img">
                    <span v-else class="texture-table-img">暂无图片</span>
                  </template>
                </el-table-column>
                <el-table-column
                        prop="textureCode"
                        label="材质贴图编号"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="textureName"
                        label="材质贴图名称">
                </el-table-column>
                <el-table-column
                        prop="modelNumber"
                        label="材质贴图型号">
                </el-table-column>
                <el-table-column
                        prop="textureballFileId"
                        label="材质类型">
                  <template slot-scope="scope">
                    <span v-if="scope.row.textureballFileId > 0">材质球</span>
                    <span v-else>贴图</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="footer-paging">
              <el-pagination
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      :current-page="1"
                      :page-sizes="[10, 20, 30, 40]"
                      :page-size="10"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="totalCount">
              </el-pagination>
            </div>
          </el-dialog>
          <!--可以替换材质区域添加贴图弹框-->

          <el-form-item style="margin-top: 20px;">
            <el-button round class="confirmBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
            <el-button round class="cancelBtnBg" @click="resetForm()">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    </div>
  </transition>
</template>

<script>
    // import ElButton from '../../../../node_modules/element-ui/packages/button/src/button.vue'
    import SingleImageUpload from '../../base/upload/SingleImageUpload.vue'

    export default {
    components: { SingleImageUpload},
    data () {
      return {
          nav: [
              { nav: "模型库", url: "/model" },
              { nav: "模型编辑", url: "" }
          ],
          num: 1,
          index: 0,
          textureList: [], // 总材质贴图区域
          textureIndex: '', // 添加贴图对应某个材质区域下标
          radio: '', // 选择默认图
          showIndex: -1,
          showImgIndex: -1,
          showAddTexture: false,
          seekTexture: [], // 材质材料列表
          seekSurfaceList: [], // 表面材料列表
          seekType: [{id: 'texture', name: '贴图'}, {id: 'ball', name: '材质球'}], // 类型列表
          totalCount: 0,
          textureChange: [], // 勾选上的贴图
          texturePopUp: { // 搜索材质贴图参数
              companyId: sessionStorage.getItem('companyID'),
              texture: undefined,
              textureAttr: undefined,
              textureType: undefined,
              textureName: undefined,
              modelNumber: undefined,
              textureCode: undefined,
              limit: 10,
              page: 0,
          },
          tableImg: [], // 材质贴图表格
          height: '',
          width: '',
          length: '',
        fileShow: '',
        model: {},
        modelId: 0,
        modelImageUrl: '',
        modelFile: {
          name: '',
          path: ''
        },
        productCategoryDrop: [], // 获取分类下拉框
        props: { // 模型分类属性名
            label: 'name',
            value: 'keyCode',
            children: 'children'
        },
        categoryNames: '',
        rules: {
          modelName: [
            {required: true, message: '请输入模型名称', trigger: 'change'},
            {min: 1, max: 50, message: '长度在 50 个字符内', trigger: 'change'}
          ],
            modelModelNum: [
                {required: true, message: '请输入模型型号', trigger: 'change'},
                {min: 1, max: 20, message: '长度在 20 个字符内', trigger: 'change'}
            ],
            categoryIds: [
                {required: true, message: '请选择模型分类', trigger: 'change'}
            ]
        },
      }
    },
    methods: {
        /*区域贴图添加，删除操作
        * type：操作类型
        * index：总区域下某个贴图区域下标
        * imgIndex： 单个区域下某个贴图下标
        * */
        operationTexture(type, index, imgIndex) {
            if (type == 'addTexture') { // 添加区域
                for (let i = 0, len = this.num; i < len; i++) {
                    let obj = {
                        areaName: this.index,
                        areaCode: this.index,
                        rels: []
                    }
                    this.textureList.push(obj);
                    this.index++;
                }
            }
            if (type == 'deleteTexture') { // 删除区域
                this.textureList.splice(index, 1);
                if (this.textureList.length == 0) {
                    this.index = 0;
                }
            }
            if (type == 'addImg') { // 添加区域贴图
                this.textureIndex = index;
                this.showAddTexture = true;
            }
            if (type == 'deleteImg') { // 删除区域贴图
                this.textureList[index].rels.splice(imgIndex, 1);
                this.textureList[index].rels[0].defaultFlag = true;
            }
            if (type == 'DefaultImg') { // 设置主缩略图
                // console.log(this.textureList[index]);return;
                let obj = this.textureList[index].rels[imgIndex];
                let len = this.textureList[index].rels.length;
                for (let i = 0; i < len; i++) {
                    if (this.textureList[index].rels[i].defaultFlag) {
                        this.textureList[index].rels[i].defaultFlag = false;
                    }
                }
                obj.defaultFlag = true;
                this.textureList[index].rels.splice(imgIndex, 1);
                this.textureList[index].rels.splice(0, 0, obj);
            }
        },
        /*勾选贴图*/
        handleSelectionChange(val) {
            let arr = [];
            for (let i = 0, len = val.length; i < len; i++) {
                //let obj = {thumbnailPath: val[i].thumbnailPath, textureId: val[i].textureId, defaultFlag: false};
                let obj = {thumbnailPath: val[i].thumbnailPath, textureId: val[i].textureId, defaultFlag: false,
                    textureCode:val[i].textureCode,textureName:val[i].textureName,modelNumber:val[i].modelNumber};
                arr.push(obj);
            }
            this.textureChange = arr;
        },
        /*确定添加贴图*/
        confirmAdd() {
            this.textureList[this.textureIndex].rels.push(...this.textureChange);
            let len = this.textureList[this.textureIndex].rels.length;
            this.$refs.multipleTable.clearSelection();
            this.showAddTexture = false;
            for (let i = 0; i < len; i++) {
                if (this.textureList[this.textureIndex].rels[i].defaultFlag) {
                    return;
                }
            }
            this.textureList[this.textureIndex].rels[0].defaultFlag = true;
        },
        /*添加贴图材质列表*/
        getTextureList() {
            this.API.ListTexture(this.texturePopUp).then(res => {
                this.totalCount = res.total;
                this.tableImg = res.list;
            });
        },
        /*请求搜索条件下拉框列表*/
        getSeekList() {
            this.API.textureCaiLiao().then(res => {
                this.seekTexture = res.data;
            });
            this.API.textureAttr().then(res => {
                this.seekSurfaceList = res.data;
            });
        },
        /*搜索，重置贴图*/
        seekTextureList(type) {
            if (type == 'reset') {
                this.texturePopUp.texture = undefined;
                this.texturePopUp.textureAttr = undefined;
                this.texturePopUp.textureType = undefined;
                this.texturePopUp.textureName = undefined;
                this.texturePopUp.modelNumber = undefined;
                this.texturePopUp.textureCode = undefined;
                this.getTextureList();
            }
            if (type == 'seek') {
                this.getTextureList();
            }
        },
        /*每页加载条数*/
        handleSizeChange(val) {
            this.texturePopUp.limit = val;
            this.getTextureList();
        },
        /*翻页*/
        handleCurrentChange(val) {
            this.texturePopUp.page = val;
            this.getTextureList();
        },
        //
      handleModelImgSuccess (resp) {
        this.modelImageUrl = resp.data.url
      },
      before3DUUpload (file) {
        let sdu = file.name.substring(file.name.lastIndexOf('.') + 1)
        let ext = sdu === '3DU'
        let sets = sdu === '3du';
        let assetbundle = sdu == 'assetbundle'
        if (!ext && !assetbundle && !sets) {
          this.$message.error('上传文件只能是 3DU 或 assetbundle 格式!')
          return false
        }
        this.fileShow = file.name
        return ext || assetbundle || sets
      },
      // 模型分类编号
      selectCategoryNO (val) {
        this.selectCategoryCode = val[val.length - 1]
      },
      /* 上传图片 */
      handleModelSuccess (resp, file) {
        this.modelFile.path = resp.data.url
        this.modelFile.name = file.raw.name
      },
      submitForm (formName) {
          if (this.model.modelDesc && this.model.modelDesc.length > 200) {
              this.$message.error('描述信息小于200个字符');
              return;
          }
          let arr = [];
          console.log(this.textureList)
          for (let i = 0, len = this.textureList.length; i < len; i++) {
              let obj = {
                  areaCode: this.textureList[i].areaCode,
                  areaId: this.textureList[i].areaId ? this.textureList[i].areaId : null,
                  areaName: this.textureList[i].areaName,
                  width:this.textureList[i].width,
                  height:this.textureList[i].height,
                  rels: []
              }
              for (let j = 0, relsLen = this.textureList[i].rels.length; j < relsLen; j++) {
                  let relsObj = {defaultFlag: this.textureList[i].rels[j].defaultFlag, textureId: this.textureList[i].rels[j].textureId}
                  obj.rels.push(relsObj);
              }
              arr.push(obj);
          }
        const params = {
          modelName: this.model.modelName,
          categoryIds: this.model.categoryIds.join(','),
          smallType: this.model.categoryIds[this.model.categoryIds.length - 1],
          thumbPicPath: this.modelImageUrl.replace(this.BASE_URL.sourceBaseUrl, ''),
          modelId: this.modelId,
          modelModelNum: this.model.modelModelNum,
          modelDesc: this.model.modelDesc,
          categoryNames: this.categoryNames,
          companyId: sessionStorage.getItem('companyID'),
          modelPath: this.modelFile.path.replace(this.BASE_URL.sourceBaseUrl, '') == 'undefined' ? undefined : this.modelFile.path.replace(this.BASE_URL.sourceBaseUrl, ''),
          modelAreaTextureRelAdd: arr
        }
        for (let key in params) {
          if (params[key] == '' || params[key] == undefined || params[key] == null) {
              params['thumbPicPath'] ? params['thumbPicPath'] : -1;
              key == 'thumbPicPath' ? '' : delete params[key]
          }
        };
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('确定更新模型产品?', '提示', {
              cancelButtonClass: 'cancelButtonClass',
              confirmButtonClass: 'confirmButtonClass',
              type: 'info',
              center: true
            }).then(() => {
                if (params.modelAreaTextureRelAdd) {
                    params.modelAreaTextureRelAdd.forEach((val) => {
                        let len = val.rels.length;
                        for (let i = 0; i < len; i++) {
                            if (val.defalutTextureId) {
                                val.defalutTextureId = undefined;
                            }
                            if (!val.areaId || val.areaId == '') {
                                val.areaId = null;
                            }
                            if (val.modelId) {
                                val.modelId = undefined;
                            }
                            if (val.rels[i].thumbnailPath) {
                                val.rels[i].thumbnailPath = undefined;
                            }
                            if (val.rels[i].picPath) {
                                val.rels[i].picPath = undefined;
                            }
                        }
                    });
                }
              this.API.EditModel(params).then((res) => {
                if (res) {
                  this.$message.success('模型产品更新成功')
                  this.$router.push('/model')
                }
              })
            })
          }
        })


      },
      // 模型分类下拉框
      getProductCategory () {
        this.API.ProductCategory({type: 'model'}).then((res) => {
          if (res) {
            this.productCategoryDrop = res.data
          }
        })
      },
      getModelDetail () {
        this.API.GetModel(this.modelId).then((res) => {
            this.length = res.data.length;
            this.width = res.data.width;
            this.height = res.data.height;
            this.textureList = res.data.modelTextureInfo;
            this.textureList.sort((a ,b) => {
                return a.areaCode - b.areaCode;
            });
            this.modelImageUrl = this.BASE_URL.sourceBaseUrl + res.data.thumbPicPath
          this.modelFile.path = this.BASE_URL.sourceBaseUrl + res.data.file3duPath
          this.modelFile.name = res.data.file3duPath ? res.data.file3duPath.slice(res.data.file3duPath.lastIndexOf('/') + 1) : ''
          this.model = res.data
          console.log(res.data, 'modelModelNum')
            for (let i = 0; i< this.productCategoryDrop.length; i++) {
              for (let j = 0; i < this.productCategoryDrop[i].length; j++) {

              }
            }
          if (this.model.categoryIds) {
            this.model.categoryIds = this.model.categoryIds.split(',')
          } else {
            this.model.categoryIds = []
          }
        })
      },
      resetForm () {
        this.$router.push('/model')
        this.getModelDetail()
      }
    },
    created () {
      this.modelId = this.$route.params.modelId
      this.getProductCategory()
      this.getModelDetail();
      this.getTextureList();
      this.getSeekList();
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/mixin";

  .modelNameShow {
    li {
      display: inline-block;
      margin-right: 25px;
    }
  }
  .sizes {
    border: solid 1px #ddd;
    height: 40px;
    border-radius: 6px;
    padding-left: 10px;
  }

  .texture{
    padding: 0 30px;
    .texture-btn{
      margin-bottom: 20px;
    }
    .texture-area{
      background-color: #f6f6f6;
      padding: 0 10px 10px 10px;
      .texture-area-item{
        background-color: #ffffff;
        margin-top: 10px;
        .header{
          line-height: 28px;
          padding: 10px;
          border-bottom: 2px solid #f6f6f6;
          display: flex;
          justify-content: space-between;
          div{
            .texture-code{
              margin-right: 10px;
              color: #ff6419;
            }
            .texture-name{
              width: 100px;
              margin-right: 20px;
            }
            span{
              color: #666;
            }
            .setUV{
              em{
                font-style: normal;
                display: inline-block;
                width: 40px;
                border: 1px solid #ddd;
                border-radius: 4px;
                height: 28px;
                line-height: 28px;
                text-align: center;
                box-sizing: border-box;
                vertical-align: middle;
                border-right: none;
              }
            }
          }
          span{
            color: #FF6419;
            cursor:pointer;
          }
        }
        .content{
          padding: 15px 15px 0 15px;
          display: flex;
          justify-content: left;
          flex-wrap: wrap;
          .content-item{
            width: 90px;
            height: 90px;
            margin-bottom: 15px;
            overflow: hidden;
            margin-right: 10px;
            position: relative;
            .default-icon{
              position: absolute;
              top: 5px;
              left: 0;
              width: 65px;
              height: 20px;
              line-height: 20px;
              font-size: 8px;
              color: #ffffff;
              text-align: center;
              background-color: #FF6419;
              border-bottom-right-radius: 10px;
              border-top-right-radius: 10px;
            }
            .default-select{
              position: absolute;
              bottom: -20px;
              left: 0;
              width: 100%;
              height: 20px;
              text-align: center;
              background-color: #FF6419;
              color: #ffffff;
              cursor: pointer;
              transition: all 0.3s linear;
            }
            .default-select-active{
              position: absolute;
              bottom: 0px;
              left: 0;
              width: 100%;
              height: 20px;
              text-align: center;
              background-color: #FF6419;
              color: #ffffff;
              cursor: pointer;
              transition: all 0.3s linear;
            }
            .content-item-delete{
              position: absolute;
              display: block;
              top: 36px;
              left: 36px;
              width: 18px;
              height: 16px;
              background: url("../../../assets/images/icons/delete3.png");
            }
            img{
              height: 90px;
              width: 90px;
              border-radius: 5px;
              border: 1px solid #eee;
              box-sizing: border-box;
              display: block;
            }
            .avatar-uploader{
              height: 90px;
              border: 1px dotted #7e8c8d;
              box-sizing: border-box;
              border-radius: 5px;
              overflow: hidden;
              div{
                width: 22px;
                height: 22px;
                margin: 34px 34px;
                background: url("../../../assets/images/icons/uploadPlug.png") no-repeat 0 0;
              }
            }
          }
        }
      }
    }
  }

  .img-pop-up{
    .img-pop-up-seek{
      border-bottom: 1px solid #c0ccda;
      display: flex;
      flex-wrap:wrap;
      justify-content: flex-start;
      .img-pop-up-input{
        padding: 0 30px;
        margin-bottom: 20px;
      }
    }
    .table-txt{
      display: flex;
      justify-content: space-between;
      padding: 20px 30px;
    }
    .texture-table{
      max-height: 300px;
      overflow-y: auto;
      .texture-table-img{
        display: inline-block;
        border: 1px solid $greyBorderEEE;
        border-radius: 5px;
        width: 90px;
        height: 90px;
      }
    }
    .footer-paging{
      background-color: #ffffff;
      padding: 20px 40px;
      text-align: center;
    }
  }

  .addProduct {
    background: #fff;
    box-sizing: padding-box;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: $borderRadius;
    header {
      h1 {
        font-weight: normal;
        font-size: 18px;
        padding-bottom: 15px;
        color: $greyOverButton;
        border-bottom: solid 1px $greyBorderEEE;
      }
    }
    p {
      height: 30px;
      line-height: 30px;
      padding-top: 30px;
      font-size: 14px;
      color: #666;
      span {
        height: 20px;
        line-height: 20px;
        display: inline-block;
        padding-right: 5px;
        color: red;
        font-weight: bold;
        font-size: 20px;
        vertical-align: -6px;
      }
    }

    .selectWidth {
      width: 305px;
    }
    .textareaWidth {
      width: 705px;
      height: 130px;
      background-color: #eee;
    }
  }

  .el-select .el-input {
    width: 130px;
    margin-right: -10px;
  }

  .input-with-select .el-input-group__append {
    background-color: #fff;
    width: 130px;
  }
  .splitLine{
    height: 50px;
    line-height: 50px;
    padding-left: 30px;
    border: none;
  }

</style>

