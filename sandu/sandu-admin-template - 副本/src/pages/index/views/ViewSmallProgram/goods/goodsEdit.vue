<template>
  <div>
    <v-nav :nav="nav"></v-nav>
    <div class="goods-edit">
      <el-row :gutter="24" class="goods-message">
        <el-col :span="24">
          <h1>商品信息</h1>
          <p></p>
        </el-col>

        <el-form class="elFromItem">
          <el-form-item>
            <el-col style="width:110px;">
              <h2 class="starbox">
                <span class="star">*</span>产品名称：</h2>
            </el-col>
            <el-col style="width: 250px">
              <el-input v-model="goodsName" placeholder="请输入100个字以内" maxlength="100" size="medium" @change="verifyPiece('产品名称')"></el-input>
            </el-col>
          </el-form-item>

          <el-form-item  style="height: 400px">
            <el-col style="width:110px">产品描述：</el-col>
            <el-col :span="20">
              <vue-editor
                      style="display: block; height: 330px"
                      useCustomImageHandler
                      @image-added="handleImageAdded"
                      v-model="textarea3"
                      :editorOptions="editorSettings"
                      class="editor"></vue-editor>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-col style="width: 110px" >
              <h4>产品图片：</h4>
            </el-col>
            <el-col :span="27" class="shopPic">
              <div class="container-pic">
                <ul class="picbox">
                  <li class="box">
                    <el-upload :limit="10" :on-exceed="LimitPic" class="avatar-uploader" :data="uploadParams" :action='BASE_URL.uploadUrl' :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccessaa" style="width:100%;" :show-file-list="false" :file-list="fileList2">
                      <span class="avatar-uploader-icon">+</span>
                      <span class="text">上传图片</span>
                    </el-upload>
                  </li>

                  <!--<li class="uploadImgList" v-for="(file, index) in fileList2" :key="index" v-dragging="{ item: file, list: fileList2, group: 'file' }">-->
                    <!--<div class="div1">-->
                      <!--<img style="border-radius: 8px;" width="90" height="90" :src="file.url" />-->
                    <!--</div>-->
                    <!--<span class="suolue" v-if="index == current"> 主缩略图</span>-->
                    <!--<span v-if="fileList2[index].isS" @click="Show(file,index)" class="shezhisuolue">设置为缩略图</span>-->
                    <!--<span class="deletbtn" @click="deleteUploadImg(index)"></span>-->
                  <!--</li>-->

                  <li class="uploadImgList" v-for="(file, index) in fileList2" :key="index" v-dragging="{ item: file, list: fileList2, group: 'file' }">
                    <span class="posDelete" v-show="showDeletes[index]" @click="deleteUploadImg(index)"></span>
                    <img width="90" height="90"
                         :src="file.url"/>
                    <span v-if="file.id == defaultPicId" class="upload-mainpic__con">主缩略图</span>
                    <span v-show="showDeletes[index]" @click="setDefaultPic(file,index)" class="upload-notmainpic__con">设置为缩略图</span>
                  </li>

                </ul>
                <div class="pic-tip">文件大小2M以内，尺寸建议1:1</div>
              </div>

              <!-- <span>{{goodsInfo.productDesc}}</span> -->
            </el-col>
          </el-form-item>
        </el-form>
      </el-row>

      <el-row class="specification">
        <el-col :span="24">
          <h1>规格管理</h1>
          <p></p>
        </el-col>
        <el-col class="specification-header" :span="23">
          <span>总库存：{{overallInventory}}</span>
          <span>说明：商品规格图尺寸:xxx X XXX 商品主图:xxx X xx 商品主图为商品详情页顶部展示的轮播图片，商品规格图为商品选择规格时展示的图片</span>
        </el-col>
        <el-col :span="24">
          <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)" :data="goodsInfo.skuList" border style="width: 100%">
            <el-table-column  prop="modelNumber" label="产品型号">
              <template slot-scope="scope">
                <span>{{scope.row.modelNumber}}</span>
              </template>
            </el-table-column>
            <el-table-column v-for="(item, index) in goodsInfo.tableHeads" :key="index" prop="attributes" :label="item" width="120">
              <template slot-scope="scope">
                <span>{{scope.row.attributes[index]}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100" prop="salePrice" label="原价">
            </el-table-column>
            <el-table-column prop="price" label="优惠价格">
              <template slot-scope="scope">
                <el-input maxlength="7" v-model="scope.row.price" placeholder="请输入优惠价格" size="medium" @input="verifyPrimitivePrice(scope)"></el-input>
                <span v-show="showVerifyPrimitivePrice[scope.$index]" style="color: #ff6419;display: block">输入内容为大于等于0的整数或小数点后两位数</span>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="装修单价">
              <template slot-scope="scope">
                {{scope.row.decorationPrice}} {{scope.row.valuationUnit}}
              </template>
            </el-table-column>
            <el-table-column prop="inventory" label="库存">
              <template slot-scope="scope">
                <el-input maxlength="7" v-model="scope.row.inventory" placeholder="请输入内容" size="medium" @change="addInventory(scope)" @input="verifyInventory(scope)"></el-input>
                <span v-show="showVerifyInventory[scope.$index]" style="color: #ff6419;display: block">输入内容为大于等于0的整数</span>
              </template>
            </el-table-column>
            <!--   <el-table-column width="140" prop="listPic" label="列表图片/100*100">
                            <template slot-scope="scope" style="display: inline-block">
                                <div v-show="scope.row.listPic">
                                    <img :src="BASE_URL.sourceBaseUrl + scope.row.listPic" width="90" style="height: 90px">
                                </div>
                                <div class="nonImg" v-show="!scope.row.listPic">暂无图片</div>
                                <div @click.stop="getIifo(scope, 'listPic')">
                                    <el-upload class="avatar-uploader" :action="BASE_URL.uploadUrlMiniApp" :show-file-list="false" :on-success.stop="handleAvatarSuccess">
                                        <div>
                                            //<span class="add-img"></span>
                                            <span>重新上传</span>
                                        </div>
                                    </el-upload>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column width="140" prop="spePic" label="规格图片/100*100">
                            <template slot-scope="scope">
                                <div v-show="scope.row.spePic">
                                    <img :src="BASE_URL.sourceBaseUrl+scope.row.spePic" width="90" style="height: 90px">
                                </div>
                                <div class="nonImg" v-show="!scope.row.spePic"> 暂无图片 </div>
                                <div @click.stop="getIifo(scope, 'spePic')">
                                    <el-upload class="avatar-uploader" :action="BASE_URL.uploadUrlMiniApp" :show-file-list="false" :on-success.stop="handleAvatarSuccess">
                                        <div>
                                           //<span class="add-img"></span>
                                            <span>重新上传</span>
                                        </div>
                                    </el-upload>
                                </div>
                            </template>
                        </el-table-column> -->
            <el-table-column width="140" prop="spePic" label="主图/100*100">
              <template slot-scope="scope">
                <div v-show="scope.row.spePic">
                  <img :src="scope.row.spePic" width="90" style="height: 90px;object-fit: contain">
                </div>
                <div class="nonImg" v-show="!scope.row.spePic"> 暂无图片 </div>
                <div @click.stop="getIifo(scope, 'spePic')">
                  <el-upload class="avatar-uploader" :action="BASE_URL.uploadUrl" :show-file-list="false" :data="uploadParams" :before-upload="beforeAvatarUpload" :on-success.stop="handleAvatarSuccess">
                    <div>
                      <!--<span class="add-img"></span>-->
                      <span>重新上传</span>
                    </div>
                  </el-upload>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

      <el-row class="logistics-set  wuliuSetUp">
        <el-col :span="24">
          <h1>物流设置</h1>
          <p></p>
        </el-col>
        <el-col :span="8">
          <h2>购物信息：</h2>
          <div class="logistics-msg">
            <el-radio v-model="logisticsMsg" label="不限购"></el-radio>
            <el-radio v-model="logisticsMsg" label="限购">
              每人限购
              <el-input maxlength="7" class="price-input" :disabled="logisticsMsg ==='限购' ? false : true" size="medium" v-model="piece" @input="verifyPiece('件数')"></el-input>件
              <p v-show="showVerifyPiece">输入内容为大于0的整数</p>
            </el-radio>
            <!--<el-radio v-model="logisticsMsg" label="会员可购买" @change="logisticsMsgs"></el-radio>-->
          </div>
        </el-col>
        <el-col :span="8">
          <h2 style="min-width:110px;">最早发货时间：</h2>
          <div>
            <el-radio v-model="Earliest" label="1">最早下单
              <el-input maxlength="3" @blur="inputFunc"  v-model="EarTime" :disabled="Earliest ==='1' ? false : true" class="EarInput" type="text"></el-input>天后可发货
              <p v-show="isEarli" style="margin-top:10px;margin-left:24px;">输入内容为大于0的整数</p>
            </el-radio>
            <el-radio v-model="Earliest" label="2" style="margin-top:10px;">
              <el-date-picker size="medium"   @change="DateLimit" :disabled="Earliest ==='2' ? false : true" class="EarliestTime" v-model="EarDate" type="datetime" placeholder="选择日期时间">
              </el-date-picker>
            </el-radio>

          </div>
        </el-col>
        <!-- <el-col :span="8">
          <h2>最早发货时间：</h2>
                   <el-radio v-model="Earliest" label="1">最早下单
              <el-input maxlength="3" @blur="inputFunc" v-model="EarTime" class="EarInput" type="text"></el-input>天后可发货
              <p v-if="isEarli" style="margin-top:10px;">输入内容为大于等于0的数字</p>
            </el-radio>
        </el-col> -->
        <el-col :span="8">
          <h2>物流费用：</h2>
          <div class="logistics-price">
            <!--<el-radio v-model="logisticsPrice" label="系统统一配送费用" @change="logisticsMsgs">-->
            <!--系统统一配送费用-->
            <!--<el-select v-model="logisticsName" placeholder="请选择" size="medium">-->
            <!--<el-option-->
            <!--v-for="(item, index) in logisticsCompany"-->
            <!--:key="index"-->
            <!--:label="item.name"-->
            <!--:value="item.price">-->
            <!--</el-option>-->
            <!--</el-select>-->
            <!--</el-radio>-->
            <el-radio v-model="logisticsPrice" label="固定___元配送">
              固定
              <el-input maxlength="7" class="price-input" size="medium" v-model="price" @blur="verifyPiece('物流费用')"></el-input>元配送
              <p v-show="showVerifyPrice">输入内容为大于等于0的整数</p>
            </el-radio>
            <!-- <el-radio v-model="logisticsPrice" label="系统设置">
                        </el-radio> -->

          </div>
        </el-col>
        <!--<el-col :span="8">-->
        <!--<h2>赠送积分：</h2>-->
        <!--<el-input v-model="integral" placeholder="0" size="medium"></el-input>-->
        <!--</el-col>-->
      </el-row>

      <!--  <el-row class="presell-set" style="" v-if="true">
      //<el-col :span="24">
                    <h1>预售设置</h1>
                    <p></p>
                //</el-col>
        <el-col :span="16">
           <div>商品预售：
            <el-checkbox v-model="isPresell">预售商品</el-checkbox>
          </div>
          <div v-show="isPresell">
            <div>
              预售截止时间：
              <el-date-picker size="medium" v-model="presellData" type="datetime" placeholder="选择日期时间">
              </el-date-picker>
            </div>
            <div>
              支付尾款时间：
              <el-date-picker size="medium" v-model="priceStart" type="datetime" placeholder="选择日期时间">
              </el-date-picker>
              <span>至</span>
              <el-date-picker @change="isData" size="medium" v-model="priceOver" type="datetime" placeholder="选择日期时间">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col  v-show="isPresell" style="margin-top:20px;" :span="16">
          <div>定金金额：
            <el-input v-model="presellPrice" placeholder="请输入金额" size="medium"></el-input>
            <span>元</span>
          </div>
          <div>发货时间：
            <span>支付尾款后<input type="text" style="width:28px;border-bottom:1px solid black; text-align:center;" maxlength="3">天发货</span>
          </div>
        </el-col>
        <el-col :span="24" style="margin:0 0 20px 20px">
          <div>商品特卖：
            <el-checkbox v-model="isTEMAI" style="margin-left:10px;margin-top:30px;">商品特卖</el-checkbox>
          </div>
        </el-col>
      </el-row>
      -->
      <el-row class="yushou">
        <el-col>
          <div>商品预售：
            <el-checkbox v-model="isPresell">新品商品</el-checkbox>
          </div>
        </el-col>
      </el-row>

      <!-- <div class="timebox" v-if="isPresell">
        <el-row>
          <el-col>
          <span style="color:#ff6419">*</span>
            预售截止时间：
            <el-date-picker size="medium" v-model="presellData" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <span style="color:#ff6419">*</span>
            支付尾款时间：
            <el-date-picker size="medium" v-model="priceStart" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
            <span>至</span>
            <el-date-picker @change="isData" size="medium" v-model="priceOver" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <span style="color:#ff6419">*</span>
            定金金额：
            <el-input v-model="presellPrice"  style="width:120px;" @blur="verifyPiece('定金')" placeholder="请输入金额" size="medium"></el-input>
            <span>元</span>
           <span v-show="isDingJin" style="display:block;margin-top:10px;margin-left:7px;color:#ff6419;">输入内容为大于0的整数</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col style="height:65px;">
            <span style="color:#ff6419">*</span>
            发货时间：
            <span>支付尾款后
              <el-input maxlength="3" style="width:60px;margin:0 10px;" @input="WeiKuan" v-model="WKuan" class="EarInput" type="text"></el-input>天发货</br>
              <span v-show="isWeikuan" style="display:inline-block;margin-top:10px;margin-left:7px;color:#ff6419;">输入内容为大于0的整数</span>
            </span>

          </el-col>
        </el-row>
      </div> -->
      <el-row class="yushou">
        <el-col>
          <div>商品特卖：
            <el-checkbox v-model="isTEMAI">商品特卖</el-checkbox>
          </div>
        </el-col>
      </el-row>
      <div class="footer">
        <el-button round class="cancel" @click="cancelAll">取消</el-button>
        <el-button round class="preserve" @click="saveSubmit">保存</el-button>
      </div>
    </div>
    <!--编辑界面-->
    <!--富文本html区域-->
    <div v-show="false" v-html="textarea3" ref="editorContent"></div>
    <!--富文本html区域-->
  </div>
</template>
<script>
    import {formatDate, PointTwo} from "../../../filters/data";
    import '@/assets/css/vueEditer.scss'
    import VueEditor from '@/components/vueEditor/vueEditor'
    import Quill from "quill";
    import { ImageDrop } from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import {container, ImageExtend,QuillWatch} from 'quill-image-extend-module'

    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);
    Quill.register('modules/ImageExtend', ImageExtend)

export default {
  name: "goods-edit",
    components: {
        VueEditor
    },
  data() {
    return {
        editorSettings: {
            modules: {
                imageDrop: false,
                imageResize: {},
                ImageExtend:{
                    loading: false,
                    name:'file',
                    change: (xhr, formData) => {
                        xhr.setRequestHeader('Authorization',sessionStorage.getItem('token'));
                        formData.append("type", "image");
                        formData.append("platform", "web");
                        formData.append("module", "product");
                        formData.append("fileKey", "company.shop.casePic");
                    },
                    action: 'upload-error',   //拖拽功能 去掉
                    start: (value) => {

                    },
                    response: (res) =>{
                        return res.data.url
                    }
                }
            }
        },
        isDingJin: false,
        zhuURL: "", //主缩略图路径
        sku_List: [],
        WKuan: 30,
        isWeikuan: false,
        isEarli: false,
        EarTime: 0,
        imageList: "",
        Earliest: "1",
        isTEMAI: false,
        current: 0,
        defaultPicId: 0, //主缩略图id
        showDeletes: [],
        uploadParams: {
          // 上传的信息
          fileKey:'shop.goods.goodpic',
          platform: "web",
          module: "product",
          type: "image"
        },
        fileList2: [],
        textarea3: "",
        nav: [
          { nav: "商品管理", url: "/goods" },
          { nav: "商品编辑", url: "/goodsEdit" }
        ],
        showVerifyPrice: false,
        showVerifyPiece: false,
        showVerifyInventory: [],
        showVerifyGoodsName: false,
        showVerifyPrimitivePrice: [],
        showVerifyPrimitiveDecorationPrice: [],
        piece: 0,
        price: 0,
        logisticsMsg: "不限购",
        logisticsPrice: "固定___元配送",
        goodsName: "",
        goodsNames: "",
        goodsInfo: {},
        listUpImg: "",
        logisticsCompany: [], // 配送物流公司选择框数据
        logisticsName: "",
        integral: 0,
        isPresell: false,
        presellPrice: "",
        presellData: "",
        priceStart: "",
        priceOver: "",
        goodsInventory: "",
        loading: true,
        overallInventory: 0,
        index: -1,
        typeName: "",
        spuCode: [],
        EarDate: "",
        sku_imgURL: [],
        isName: false,
        introduce: '', // 富文本
        introducedPicIds: [],  // 富文本图片ID
        introducedPicImg: [], // 富文本图片信息
    };
  },
  created() {
    this.getGoodsEdit();
  },
    mounted(){
        this.$dragging.$on('dragged', ({ value }) => { //拖拽事件 触发
            this.defaultPicId=value.list[0].id;
        })
    },
  methods: {
      /*富文本上传图片*/
      async handleImageAdded(file,fileName, Editor, cursorLocation, resetUploader) {
          let formData = new FormData();
          formData.append('file', file,fileName);
          formData.append('businessId ', sessionStorage.getItem('shopId'));
          formData.append('type', 'introduced');
          formData.append("fileKey", 'shop.goods.goodpic');
          formData.append("platform", 'web');
          formData.append("module", 'product');
          formData.append("type", 'image');
          await this.API.upLoadImg(formData).then((res) => {
              if (res.code==200) {
                  //let url = this.BASE_URL.sourceBaseUrl + res.data.url; // Get url from response
                  let url =  res.data.url; // Get url from response
                  if (res.data.resId) {
                      this.introducedPicImg.push(res.data);
                  }
                  Editor.insertEmbed(cursorLocation, 'image', url);
                  resetUploader();
              } else {
                  this.$message({
                      type: 'error',
                      message: res.message
                  });
              }

          });
      },
      /*获取富文本内存在的图片的id*/
      acquireIntroducedPicId() {
          let img = this.$refs.editorContent.getElementsByTagName("img");
          let id = [];
          if (img.length > 0) {
              for (let i = 0; i < img.length; i++) {
                  for (let j = 0; j < this.introducedPicImg.length; j++) {
                      if (decodeURI(img[i].src.substring(this.BASE_URL.sourceBaseUrl.length)) == decodeURI(this.introducedPicImg[j].picPath)) {
                          id.push(this.introducedPicImg[j].id);
                      }
                  }
              }
          }
          this.introducedPicIds = id;
      },
    DateLimit(){
      let time = new Date().valueOf();
      if(this.EarDate.valueOf() < time){
        this.$message.warning('最早发货时间不能早于当前时间');
        this.EarDate = '';
      }else{
        return;
        }
    },
    ShopName() {
      this.goodsName == "" ? (this.isName = true) : (this.isName = false);
    },
    WeiKuan() {
      let reg = /^\+?[1-9]\d*$/;
      //console.log(reg.test(this.EarTime));
      if (this.WKuan == "") {
        this.isWeikuan = false;
      } else if (this.WKuan !== "") {
        if (reg.test(this.WKuan)) {
          this.isWeikuan = false;
        } else {
          this.isWeikuan = true;
        }
      }
    },
    inputFunc() {
      let reg = /^\+?[1-9]\d*$/;
      //console.log(reg.test(this.EarTime));
      if (this.EarTime == "") {
        this.isEarli = false;
      } else if (this.EarTime !== "") {
        if (reg.test(this.EarTime)) {
          this.isEarli = false;
        } else {
          this.isEarli = true;
          this.EarTime = "";
        }
      }
    },
    TimeStyle(time) {
      time.valueOf();
      time = formatDate(new Date(time), "yyyy-MM-dd hh:mm:ss");
      return time;
    },
    open() {
      this.$alert("最多上传10张照片", "提示", {
        confirmButtonText: "确定"
      });
    },
    LimitPic() {
      this.open();
    },
    deleteUploadImg(index) {
        this.fileList2.splice(index, 1)
        this.defaultPicId = ''
    },
    Show(file,index) {
        this.current = 0;//默认第一张为主缩略图
        console.log(this.fileList2[index].id);
        this.defaultPicId = this.fileList2[index].id;
        this.fileList2.splice(index,1);
        this.fileList2.unshift(file);
    },
    setDefaultPic(file,index) {
        this.defaultPicId = file.id;
        this.fileList2.splice(index,1);
        this.fileList2.unshift(file);
    },
    SetUpSuo(index) {
      if (this.current == index) {
        return;
      } else {
        this.fileList2[index].isS = true;
      }
    },
    Close(index) {
      this.fileList2[index].isS = false;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 4;

      if (!isJPG && !isPNG) {
        this.$message.error("上传图片只能是 JPG 或 PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("图片大小不能超过 4MB!");
      }
      return (isJPG || isPNG) && isLt2M;
    },
    handleAvatarSuccessaa(res, file) {
      // this.imageUrl = URL.createObjectURL(file.raw);
      this.fileList2.push({
        url: res.data.url,
        isS: false,
        id: res.data.resId
      });
      if (this.fileList2) {
        if (this.fileList2[1]) {
          this.defaultPicId = this.defaultPicId;
          console.log(this.defaultPicId);
        } else if (this.fileList2[0]) {
          this.defaultPicId = this.fileList2[0].id;
          console.log(this.defaultPicId);
        }
      }
    },
    verifyPrimitivePrice(scope) {
      // let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      console.log(this.goodsInfo.skuList[scope.$index]);
      if (this.goodsInfo.skuList[scope.$index].price.length == 0) {
        this.goodsInfo.skuList[scope.$index].price = 0;
      }
      if (!PointTwo(this.goodsInfo.skuList[scope.$index].price)) {
        this.showVerifyPrimitivePrice[scope.$index] = true;
      } else {
        this.showVerifyPrimitivePrice[scope.$index] = false;
        // if (this.goodsInfo.skuList[scope.$index].price.substring(0, 1) == 0) {
        //   this.goodsInfo.skuList[scope.$index].price = this.goodsInfo.skuList[
        //     scope.$index
        //   ].price.substring(1);
        // }
        if (this.goodsInfo.skuList[scope.$index].length > 0 && this.goodsInfo.skuList[scope.$index].substring(0, 1) == 0) {
          this.goodsInfo.skuList[scope.$index].price = this.goodsInfo.skuList[
            scope.$index
          ].price.substring(1);
        }
      }
      console.log(this.showVerifyPrimitivePrice);
    },
    verifyInventory(scope) {
      let i = /^[0-9]\d*$/;
      let inventory = this.goodsInfo.skuList[scope.$index].inventory;
      if (inventory.length == 0) {
        this.goodsInfo.skuList[scope.$index].inventory = 0;
      }
      if (!i.test(this.goodsInfo.skuList[scope.$index].inventory)) {
        this.showVerifyInventory[scope.$index] = true;
      } else {
        this.showVerifyInventory[scope.$index] = false;
        if (inventory.length > 0 && inventory.substring(0, 1) == 0) {
          this.goodsInfo.skuList[
            scope.$index
          ].inventory = this.goodsInfo.skuList[
            scope.$index
          ].inventory.substring(1);
        }
      }
    },
    /*验证物流设置内的输入框内容*/
    verifyPiece(type) {
      let i = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
      let j = /^[0-9]\d*$/;
      let reg = /^\+?[0-9]\d*$/;
      if (type == "件数") {
        if (this.piece.length == 0) {
          this.piece = "0";
        }
        if (!j.test(this.piece)) {
          this.showVerifyPiece = true;
        } else {
          this.showVerifyPiece = false;
          if (this.piece.length > 1 && this.piece.substring(0, 1) == 0) {
            this.piece = this.piece.substring(1);
          }
        }
        console.log(this.piece);
      }
      if (type == "定金") {
        if (reg.test(this.presellPrice) || this.presellPrice == "") {
          this.isDingJin = false;
        } else {
          this.isDingJin = true;
          this.presellPrice = "";
          // if (this.price.substring(0, 1) == 0 && this.price.length > 1) {
          //   this.price = this.price.substring(1);
          // }
        }
      }

      if (type == "物流费用") {
        // if (this.price.length == 0) {
        //   this.price = "0";
        // }
        if (reg.test(this.price) || this.price == "") {
          this.showVerifyPrice = false;
        } else {
          this.showVerifyPrice = true;
          this.price = "";
          // if (this.price.substring(0, 1) == 0 && this.price.length > 1) {
          //   this.price = this.price.substring(1);
          // }
        }
      }
      if (type == "产品名称") {
        if (
          this.goodsName.replace(/\s+/g, "").length > 100 ||
          this.goodsName.replace(/\s+/g, "").length == 0
        ) {
          this.showVerifyGoodsName = true;
        } else {
          this.showVerifyGoodsName = false;
        }
      }
    },
    /*请求商品详情数据*/
    getGoodsEdit() {
      this.API.getGoodsEdit({
        companyId: sessionStorage.getItem("companyID"),
        spuId: sessionStorage.getItem("goodsId")
      }).then(res => {

        res.data.skuList.map(item=>{
            item.spePic.indexOf('http')>-1 ? item.spePic :  item.spePic=this.BASE_URL.sourceBaseUrl+item.spePic;
        })
        console.log(res.data.skuList,'klkkk')
        this.loading = false;
        this.goodsInfo = res.data;
        this.goodsName = res.data.name;
        this.goodsNames = res.data.spuName;
        // this.piece = res.data.purhaseLimitation;
        this.price = res.data.fixTransCost;
        this.spuCode = res.data.skuList;
        this.textarea3 = res.data.describe; //产品描述
        this.overallInventory = res.data.totalRepertory; //总库存
        this.defaultPicId = res.data.mainPicId; //主图id
        if (res.data.isSpeSell == 0) {
          //判断特卖
          this.isTEMAI = false;
        } else if (res.data.isSpeSell == 1) {
          this.isTEMAI = true;
        }
        if (res.data.presell == 0) {
          //判断预售
          this.isPresell = false;
        } else if (res.data.presell == 1) {
          this.isPresell = true;
        }

        // this.presellData = this.isPresell
        //   ? (this.presellData = res.data.presellDDL)
        //   : ""; //预售结束时间
        // this.priceStart = this.isPresell
        //   ? (this.priceStart = res.data.payTailStarttime)
        //   : ""; //支付尾款开始时间
        // this.priceOver = this.isPresell
        //   ? (this.priceOver = res.data.payTailEndtime)
        //   : ""; //支付尾款结束时间
        // this.presellPrice = this.isPresell
        //   ? (this.presellPrice = res.data.earnest)
        //   : ""; //定金
        // this.WKuan = this.isPresell
        //   ? (this.WKuan = res.data.shipmentsPresell)
        //   : (this.WKuan = this.WKuan); //支付尾款后 发货时间

        if (res.data.shipmentsDay) {
          //判断最早发货时间
          this.Earliest = "1";
          this.EarTime = res.data.shipmentsDay;
        } else if (res.data.shipmentsDate) {
          this.Earliest = "2";
          this.EarDate = res.data.shipmentsDate;
        }
        if (res.data.limitation) {
          this.logisticsMsg = res.data.limitation; //限购
          if (this.logisticsMsg == 0) {
            //限购判断
            this.logisticsMsg = "不限购";
          } else if (this.logisticsMsg > 0) {
            this.logisticsMsg = "限购";
            this.piece = res.data.limitation;
          }
        }

        if (res.data.mainPic) {
          res.data.picList.unshift(res.data.mainPic); //把主图加入所有图片列表
          this.fileList2 = res.data.picList;
        }

        if (res.data.picList) {
          //把所有图片加入列表
          for (let i = 0; i < res.data.picList.length; i++) {
            this.fileList2[i].url =res.data.picList[i].picPath.indexOf('http')>-1 ? res.data.picList[i].picPath : this.BASE_URL.sourceBaseUrl + res.data.picList[i].picPath;
            this. $set(this.fileList2[i], "isS", false);
            this.fileList2[i].id = res.data.picList[i].picId;
          }
        }

        for (let i = 0; i < res.data.skuList.length; i++) {
          this.sku_List.push(res.data.skuList[i].attrValueIds);
          this.sku_imgURL.push(res.data.skuList[i].spePic.indexOf('http')>-1 ? res.data.skuList[i].spePic : this.BASE_URL.sourceBaseUrl+res.data.skuList[i].spePic);
        }


        // if (res.data.purhaseLimitation) {
        //   this.logisticsMsg = "限购";
        // } else {
        //   this.logisticsMsg = "不限购";
        // }
        // for (let i = 0; i < res.data.skuList.length; i++) {
        //   this.overallInventory =
        //     this.overallInventory + res.data.skuList[i].inventory;
        //   this.showVerifyInventory.push(false);
        //   this.showVerifyPrimitivePrice.push(false);
        // }
      });
    },
    /*计算总库存*/
    addInventory(scope) {
      let inventory = this.goodsInfo.skuList[scope.$index].inventory;
      if (!this.showVerifyInventory[scope.$index]) {
        this.overallInventory = [];
        for (let i = 0; i < this.goodsInfo.skuList.length; i++) {
          this.overallInventory =
            Number(this.overallInventory) +
            Number(this.goodsInfo.skuList[i].inventory);
        }
        // this.overallInventory = this.overallInventory + Number(scope.row.inventory);
        console.log(this.overallInventory);
      }
    },
    /*点击上传图片时获取当前行位置信息，当前行图片所在位置
            * scope： 当前行位置信息
            * type：当前行图片所在位置标识
            * 在上传图片成功时的回调时用到
            * */
    getIifo(scope, type) {
      this.typeName = type;
      this.index = scope.$index;
    },
    /*上传图片成功回调*/
    handleAvatarSuccess(res, file) {
      let arr = this.goodsInfo.skuList[this.index];
      if (this.typeName == "listPic") {
        arr.listPic = res.data.url;
        arr.listId = res.data.resId;
      }
      if (this.typeName == "spePic") {
        arr.spePic = res.data.url;
        arr.speId = res.data.resId;
      }
      if (this.typeName == "mainPic") {
        arr.mainPic = res.data.url;
        arr.mainId = res.data.resId;
      }
      this.goodsInfo.skuList.splice(this.index, 1, arr);
    },
    isData() {
      if (this.priceOver - this.priceStart < 0) {
        this.priceOver = "";
        this.$message({
          message: "结束时间应大于开始时间",
          type: "warning"
        });
      }
    },
    /*取消重置所有数据*/
    cancelAll() {
      // this.piece = 0;
      // this.price = 0;
      // this.goodsName = this.goodsNames;
      // this.overallInventory = 0;
      // for (let i = 0; i < this.goodsInfo.skuList.length; i++) {
      //   if (this.goodsInfo.skuList[i].primitivePrice) {
      //     this.goodsInfo.skuList[i].primitivePrice = "0";
      //   }
      //   this.goodsInfo.skuList[i].inventory = "0";
      // }
      this.$router.push("/goods");
    },
    /*点击保存*/
    saveSubmit() {
      for (let i = 0; i < this.showVerifyInventory.length; i++) {
        if (this.showVerifyInventory[i]) {
          this.$message({
            message: "请填写正确格式的库存信息",
            type: "warning"
          });
          return;
        }
      }
      for (let i = 0; i < this.showVerifyPrimitivePrice.length; i++) {
        if (this.showVerifyPrimitivePrice[i]) {
          this.$message({
            message: "请填写正确格式的优惠价格",
            type: "warning"
          });
          return;
        }
      }
      // if (this.isPresell) {
      //   if (
      //     this.presellData == "" ||
      //     this.priceStart == "" ||
      //     this.priceOver == "" ||
      //     this.presellPrice == "" ||
      //     this.WKuan == ""
      //   ) {
      //     this.$message({
      //       message: "商品预售信息不全，请填写全",
      //       type: "warning"
      //     });
      //     return;
      //   }
      // }  //判断预售信息
      // if(this.isPresell){
      //    let reg = /^\+?[1-9]\d*$/;
      // if (!reg.test(this.EarTime)) {
      //   this.$message({
      //     message: "请填写正确格式的信息",
      //     type: "warning"
      //   });
      //   return;
      // }
      // if (!reg.test(this.WKuan)) {
      //     this.$message({
      //       message: "请填写正确格式的尾款信息",
      //       type: "warning"
      //     });
      //     return;
      //   }
      // }  //判断预售信息
      if (
        // !this.showVerifyPrice &&
        //  !this.showVerifyPiece &&
        !this.showVerifyGoodsName
      ) {
        this.updateSpu();
        this.updateSku();
        //this.$router.push("/goods");
      } else {
        this.$message({
          message: "请填写产品名称信息",
          type: "warning"
        });
        return;
      }
    },

    /*保存商品spu信息*/
    updateSpu() {
      console.log(this.price, this.integral);
      let limitation;
      if (this.logisticsMsg == "不限购") {
        limitation = 0;
      } else {
        limitation = this.piece;
      }
      // if(this.fileList2.length==1){
      //    this.imageList = this.defaultPicId;
      // }else if(this.fileList2.length>1){
      //   for(let i=0;i<this.fileList2.length;i++){
      //           this.imageList.push(this.fileList2[i].id);
      //      }
      //    this.imageList.join(',');
      // }
      let arr1 = [];
      if (this.fileList2[0]) {
        for (let i = 0; i < this.fileList2.length; i++) {
          arr1.push(this.fileList2[i].id);
        }
        this.imageList = arr1.join(",");
      }

      this.defaultPicId == 0
        ? (this.defaultPicId = "")
        : (this.defaultPicId = this.defaultPicId);
      this.presellData
        ? (this.presellData = this.TimeStyle(this.presellData))
        : "";
      this.priceStart
        ? (this.priceStart = this.TimeStyle(this.priceStart))
        : "";

     if (this.goodsName !== '') {
          this.goodsName = this.goodsName.replace(/(^\s*)|(\s*$)/g,"");
            }  //去掉前后空格

      this.EarDate ? (this.EarDate = this.TimeStyle(this.EarDate)) : "";
      this.priceOver ? (this.priceOver = this.TimeStyle(this.priceOver)) : "";
      this.API.updateSpu({
        spuId: sessionStorage.getItem("goodsId"),
        name: this.goodsName || undefined,
        limitation: limitation,
        transId: undefined,
        fixTransCost: this.price,
        integral: this.integral || undefined,
        presellDDL: undefined,
        payTailStarttime: undefined,
        payTailEndtime: undefined,
        earnest: undefined,
        shipmentsTime: undefined,
        totalRepertory: this.overallInventory, //总库存
        text: undefined,
        picIds: this.imageList, //所有图片id 逗号分隔
        describe: this.textarea3, //商品描述
        mainPicId: this.defaultPicId, //主缩略图id
        presell: this.isPresell ? 1 : 0, //是否预售
        // presellDDL: this.isPresell ? this.presellData : "", //预售结束时间
        // payTailStarttime: this.isPresell ? this.priceStart : "", //支付尾款开始时间
        // payTailEndtime: this.isPresell ? this.priceOver : "", //支付尾款结束时间
        // earnest: this.isPresell ? this.presellPrice : "", //定金
        // shipmentsPresell: this.isPresell ? this.WKuan : "", //支付尾款后 发货时间
        isSpeSell: this.isTEMAI ? 1 : 0, //是否特卖
        shipmentsDay: this.Earliest == "1" ? this.EarTime : "", //最早发货时间   数字
        shipmentsDate: this.Earliest == "2" ? this.EarDate : "", //最早发货时间 日期对象
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {

        if (res.code == 200) {
            this.$message.success('保存成功');
            this.$router.push("/goods");
        } else {
            this.$message.error(res.message);
        }
      });
    },
    /*保存商品sku信息*/
    updateSku() {
      let arr = [];
      console.log(this.goodsInfo.skuList);
      for (let i = 0; i < this.goodsInfo.skuList.length; i++) {
        let obj = {
          cost: Number(this.goodsInfo.skuList[i].price),
          // listPictureId: this.goodsInfo.skuList[i].listId,
          // mainPictureId: this.goodsInfo.skuList[i].mainId,
          spePictureId: this.goodsInfo.skuList[i].speId,
          // spePictureId: this.goodsInfo.skuList[i].mainId,
          productId: this.goodsInfo.skuList[i].productId,
          repertory: Number(this.goodsInfo.skuList[i].inventory),
          attrValueIds: this.sku_List[i]
        };
        arr.push(obj);
      }
      console.log(arr);

      this.API.updateSku({
        goodsSKUAddList: arr,
        spuId: sessionStorage.getItem("goodsId"),
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {

      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../../../assets/css/mixin";

.header-nav {
  width: 100%;
  height: 15px;
  margin-bottom: 10px;
}
.goods-edit {
  color: #666666;
  background-color: #fff;
  overflow: hidden;
  padding: 0 20px 20px 20px;
  .el-row {
    margin-bottom: 20px;
    h2 {
      display: inline-block;
      font-size: 14px;
      margin-right: 10px;
    }
    span {
      color: #333333;
    }
    .el-col-24 {
      overflow: hidden;
      position: relative;
      margin-bottom: 40px;
      h1 {
        display: inline-block;
        width: 60px;
        height: 16px;
        font-size: 14px;

        line-height: 16px;
        border-left: 6px solid #ff6419;
        padding-left: 10px;
      }
      p {
        position: absolute;
        top: 8px;
        left: 90px;
        width: 1499px;
        border-top: 1px solid #dddddd;
        display: inline-block;
      }
    }
  }
  .elFromItem{
    padding-left: 15px;
    .el-form-item{
      margin: 0;
      padding:0
    }
  }
  .goods-message {
    margin-top: 40px;
    .starbox {
      position: relative;
      .star {
        color: #ff6419;
        position: absolute;
        left: -10px;
      }
    }
    .el-col-20,
    .el-col-8 {
      position: relative;
      padding-left: 20px;
      margin-bottom: 20px;
      font-size: 14px;
      color: #666666;
      height: 36px;
      line-height: 36px;

      .el-input {
        width: 300px;
        height: 36px;
      }
      p {
        position: absolute;
        top: 40px;
        left: 110px;
        height: 14px;
        line-height: 14px;
        color: #ff6419;
      }
    }
    .shopPic {
      min-height: 130px;
      > h4 {
        padding-left: 0;
        display: inline-block;
        font-size: 14px;
        position: relative;
        top: -50px;
      }
      .container-pic{
        padding: 10px 0 10px 10px;
        border: 1px solid #ddd;
        background: #fafafa;
        border-radius: 4px;
        width: 850px;
        min-height: 100px;
        .pic-tip{
          color: #999;
          font-size:14px;
          line-height: 16px;
        }
      }
      .picbox {
        width: 850px;
        min-height: 100px;
        align-items: center;
        display: flex;
        flex-wrap: wrap;
        .uploadImgList {
          width: 90px;
          text-align: center;
          position: relative;
          overflow: hidden;
          background-color: #fff;
          &:hover{
            .upload-notmainpic__con,.posDelete{
              display: inline-block!important;
            }
          }
          img{
            object-fit: contain;
            border-radius: 8px
          }
          .upload-notmainpic__con {
            position: relative;
            top: -45px;
            background: rgba(0, 0, 0, 0.7);
            width: 90px;
            color: #fff;
            display: inline-block;
            line-height: 22px;
            border-radius: 0 0 8px 8px;
            cursor: pointer;
          }
          .posDelete {
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
            cursor: pointer;
          }
          .upload-mainpic__con {
            position: relative;
            top: -45px;
            background-color: #ff641e;
            width: 90px;
            color: #fff;
            display: inline-block;
            line-height: 22px;
            border-radius: 0 0 8px 8px
          }
        }

        .box {
          border: 1px dashed #ccc;
          background: #ffffff;
          &:hover {
            border: 1px dashed #ff6419;
          }
        }
        li {
          width: 90px;
          height: 90px;
          display: inline-block;
          border-radius: 4px;
          margin: 6px;
          float: left;
          .div1{
            img {
              object-fit: contain;
              background-color: #fff;
            }
          }
          .avatar-uploader {
            user-select: none;
            width: 90px;
            height: 90px;
            position: relative;
            .avatar-uploader-icon {
              display: inline-block;
              width: 20px;
              height: 20px;
              background: #ff6419;
              color: white;
              font-size: 30px;
              text-align: center;
              line-height: 20px;
              border-radius: 50%;
              margin: 20px auto 0;
            }
            .text {
             height: 30px;
             width: 90px;
             line-height: 30px;
              text-align: center;
              color: #666666;
              display: inline-block;
            }
          }
        }
      }
    }
    .shopDesc {
      height: 130px;
      display: flex;
      align-items: center;
      > h3 {
        display: inline-block;
        font-size: 14px;
        margin-right: 12px;
        margin-bottom: 100px;
      }
      .descText {
        display: inline-block;
        width: 760px;
        font-size: 14px;
        font-family:"微软雅黑";
        ::-webkit-input-placeholder {
          color: #999999;
          font-size: 14px;
          font-family:"微软雅黑";
        }
      }
    }
  }
  .yushou {
    margin-left: 20px;
  }
  .timebox {
    margin-left: 110px;
  }
  .logistics-set {
    .el-col-8 {
      padding-left: 20px;
      margin-bottom: 20px;
      font-size: 14px;
      color: #666666;
      height: 147px;
      line-height: 36px;
      display: flex;
      justify-content: left;

      .el-input {
        width: 220px;
        height: 36px;
        margin-left: 10px;
      }
      .EarInput {
        width: 60px;
        height: 36px;
        margin-right: 10px;
      }
      .logistics-msg,
      .logistics-price {
        display: inline-block;
        margin-left: 10px;
        height: 100%;

        .price-input {
          width: 110px;
          height: 36px;
          margin-right: 5px;
        }
        .el-radio {
          position: relative;
          display: block;
          line-height: 36px;
          margin: 0 0 10px 0;
          color: #333333;
          p {
            position: absolute;
            top: 40px;
            left: 25px;
            height: 14px;
            line-height: 14px;
            color: #ff6419;
          }
        }
        .el-select {
          width: 200px;
          height: 36px;
          margin-left: 10px;
          color: #333333;
        }
      }
    }
  }
  .wuliuSetUp {
    height: 170px;
    margin-bottom: 0;
    .el-col-8 {
      margin-bottom: 0;
      height: 100px;
    }
    .inputErali {
    }
    .orange {
      border-bottom: 1px solid #ff6419;
    }
  }
  .presell-set {
    .el-col-8,
    .el-col-16 {
      display: block;
      height: 148px;
      padding-left: 20px;

      div {
        height: 36px;
        line-height: 36px;
        margin-bottom: 20px;
        .el-checkbox {
          margin-left: 10px;
          color: #333333;
        }
        .el-input {
          width: 200px;
          height: 36px;
          margin-left: 10px;
        }
        span {
          margin-left: 10px;
        }
      }
    }
    .el-col-8 {
      height: 30px;
    }
  }
  .specification {
    .specification-header {
      height: 36px;
      margin-left: 30px;
      display: flex;
      justify-content: space-between;
      color: #333333;
      line-height: 36px;
    }
    .avatar-uploader {
      display: inline-block;
      height: 15px;
      span {
        color: #ff6419;
      }
    }
    .el-input {
      width: 200px;
      height: 36px;
    }
    .nonImg {
      width: 90px;
      height: 90px;
      line-height: 90px;
      text-align: center;
      background-color: #f5f5f5;
      margin: 0 auto 5px;
    }
  }
  .goods-introduce {
    .el-col-12 {
      height: 703px;
    }
  }
  .footer {
    width: 100%;
    height: 80px;
    border-top: 1px solid #dddddd;
    line-height: 80px;
    padding-left: 20px;
    .el-button {
      width: 130px;
      height: 40px;
      border: none;
      color: #fff;
      font-size: 16px;
      font-weight: bold;
    }
    .preserve {
      background-color: #ff6419;
    }
    .cancel {
      background-color: #454545;
    }
  }
}
.clear {
  zoom: 1;
}
.clear:after {
  content: "";
  display: block;
  clear: both;
}
</style>