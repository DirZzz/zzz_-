<template>
  <transition name="fade">
    <div class="editProduct">
      <el-dialog title="添加运营分类信息" :visible.sync="infoClassifyVisible" class="info-ypop-up" width="800px">
        <el-row>
          <el-col :span="24">
            选择分类：
          </el-col>
        </el-row>
        <el-row class="Classify-list">
          <el-col :span="3">
            <p
                    v-for="(item, index) in productCategorylist1"
                    :key="index"
                    @click="selectType(index, 1,item)"
                    :class="indexList.index1 == index ? 'p-active' : ''">
              {{item.name}}<span>></span>
            </p>
          </el-col>
          <el-col :span="5">
            <p
                    v-for="(item, index) in productCategorylist2"
                    @click="selectType(index, 2,item)"
                    :key="index"
                    :class="indexList.index2 == index ? 'p-active' : ''">
              {{item.name}}<span>></span>
            </p>
          </el-col>
          <el-col :span="5">
            <p
                    v-for="(item, index) in productCategorylist3"
                    @click="selectType(index, 3,item)"
                    :key="index"
                    :class="indexList.index3 == index ? 'p-active' : ''">
              {{item.name}}<span>></span>
            </p>
          </el-col>
          <el-col :span="5">
            <p
                    v-for="(item, index) in productCategorylist4"
                    @click="selectType(index, 4,item)"
                    :key="index"
                    :class="indexList.index4 == index ? 'p-active' : ''">
              {{item.name}}<span>></span>
            </p>
          </el-col>
          <el-col :span="6">
            <div class="checkboxs" v-for="(item, index) in productCategorylist5" @click="selectType(index, 5,item)">
              <div :class="item.checked?'actives':''">
                  <i class="el-icon-circle-check"></i>{{item.name}}
              </div>
              <!-- <div v-else>
                  <i class="el-icon-circle-check"></i>{{item.name}}
              </div> -->
            </div>
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
          <el-button round type="primary" @click="verifyProductClass">确 定</el-button>
          <el-button round @click="infoClassifyVisible = false">取 消</el-button>
        </div>
      </el-dialog>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>编辑产品</h1>
        </header>
        <p class="addProduct_p"><span>*</span>为必填项</p>
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">产品基础信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>
        <div>
          <el-form :model="product" :rules="rules" ref="ruleForm" label-width="165px" class="demo-ruleForm">
            <el-row class="">
              <el-col :span="8">
                <el-form-item label="产品编号：">
                  {{product.code}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="">
              <el-col :span="8">
                <el-form-item label="产品名称：" prop="name">
                  <el-input class=""  v-model="product.name"></el-input>
                  <!--<el-input class="" clearable v-model="product.name"></el-input> &lt;!&ndash;product.name&ndash;&gt;-->
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品品牌：" prop="brandId">
                  <el-select
                         filterable
                          @change="addBrandId"
                          v-model="product.brandId"
                          class=""
                          clearable
                          placeholder="请选择">
                    <el-option
                            v-for="(item,index) in productBrand"
                            :key="index"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="">
              <el-col :span="8">
                <el-form-item label="产品分类：" prop="categoryCodes">
                  <!--<el-cascader-->
                    <!--clearable-->
                    <!--@change="selectCategoryNO"-->
                    <!--v-model="product.categoryCodes"-->
                    <!--placeholder="请选择"-->
                    <!--:props="props"-->
                    <!--class=""-->
                    <!--:options="productCategoryDrop"-->
                    <!--filterable>-->
                  <!--</el-cascader>-->
                  <el-input
                          style="width:321px"
                          placeholder="请输入内容"
                          v-model="categoryCodeName"
                          :disabled="true">
                  </el-input>
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="产品型号：" prop="modelNumber">
                  <!--<el-input class="" clearable v-model="product.modelNumber"/> --><!--product.modelNumber-->
                  <el-autocomplete
                          v-model="product.modelNumber"
                          :fetch-suggestions="querySearchAsync"
                          placeholder="请输入内容"
                          @select="handleSelect"
                  ></el-autocomplete>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="">
              <el-col :span="8">
                <!-- <el-form-item label="产品系列：" prop="productSeriesStr">
                    <el-autocomplete
                            v-model="product.productSeriesStr"
                            :fetch-suggestions="querySearchSeries"
                            placeholder="请输入内容"
                            @select="handleSelect"
                    ></el-autocomplete>
                  </el-form-item> -->
                 <el-form-item label="产品系列：" prop="productSeriesStr">
                  <el-select
                  filterable
                    clearable
                    v-model="product.seriesId"
                    @change="selectedSeriesID"
                    class="" placeholder="请选择">
                    <el-option
                      v-for="(item,index) in productSeries"
                      :key="index"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品款式：">
                  <el-select
                    filterable
                    clearable
                    v-model="product.modelingId"
                    class="" placeholder="请选择">
                    <el-option
                      v-for="(item,index) in productStyle"
                      :key="index"
                      :label="item.styleName"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="">
              <el-col :span="8" style="max-height: 70px;">
                <el-form-item label="长：" prop="length">
                  <el-input class="" v-model="product.length"  @blur="verifyLength">
                    <template slot="append">cm</template>
                  </el-input>
                  <p v-show="lengthWhos" class="verify">{{productLengthTxt}}</p>
                </el-form-item>
              </el-col>
              <el-col :span="8" style="max-height: 70px;">
                <el-form-item label="宽：" prop="width">
                  <el-input class="" v-model="product.width" @blur="verifyWidth">
                    <template slot="append">cm</template>
                  </el-input>
                  <p v-show="widthWhos"  class="verify">{{productWidthTxt}}</p>
                </el-form-item>
              </el-col>
              <el-col :span="8" style="max-height: 70px;">
                <el-form-item label="高：" prop="height">
                  <el-input class="" v-model="product.height" @blur="verifyHeight">
                    <template slot="append">cm</template>
                  </el-input>
                  <p v-show="heightWhos"  class="verify">{{productHeightTxt}}</p>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品规格：">
                  <el-input class="" clearable v-model="product.spec" maxlength="50"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="是否有特效：">
                  <el-checkbox size="medium" v-model="product.isSpecialEffects"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8" style="max-height: 70px;" v-if="product.isBedsideDimensions == 1">
                <el-form-item label="床沿尺寸：" prop="bedsideDimensions">
                  <el-input class="" clearable v-model="product.bedsideDimensions" @blur="verifybedsideDimensions">
                    <template slot="append">cm</template>
                  </el-input>
                  <p v-show="bedsideDimensionsWhos"  class="verify">{{bedsideDimensionsTxt}}</p>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="" v-if="productTypes==1" v-for="(item,index) in product.ceilingCrossSectionDataDTOList">
                <el-col :span="8" style="">
                    <el-form-item :label="'截面数据'+(++index)+'：'">
                      <textarea class="productDesc"
                                type="textarea" :rows="3"
                                @change="disabledFun(index-1)"
                                style="width:100%;box-sizing: border-box;"
                                v-model="item.ceilingInfo">
                        </textarea>
                        <!-- <el-input class="" clearable v-model="item.ceilingInfo" @change="disabledFun(index-1)"></el-input> -->
                    </el-form-item>
                </el-col>
                <el-col :span="8" style="">
                    <el-form-item :label="'灯带数据'+index+'：'">
                        <textarea class="productDesc"
                                type="textarea" :rows="3"
                                resize='none'
                                style="width:100%;box-sizing: border-box;"
                                v-model="item.lightInfo">
                        </textarea>
                        <!-- <el-input class="" clearable v-model="item.lightInfo"></el-input> -->
                    </el-form-item>
                </el-col>
                <el-col :span="8" style="">
                    <el-form-item label="适用面积：">
                        <el-select :disabled="disabled[index-1]" v-model="applyAreaObj[index-1].applyArea" @change="applyChange(index-1,item.applyArea)" @remove-tag="removeTag" multiple placeholder="请选择">
                            <el-option
                            v-for="v in applyAreaList[index-1]"
                            :key="v.id"
                            :disabled="v.disabled?true:false"
                            :label="v.name"
                            :value="v.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row class="" v-if="productTypes==2">
                <el-col :span="10" style="max-height: 70px;">
                    <el-form-item label="挡水边(靠墙)截面数据：">
                        <el-input class="" clearable @change="testChange('crossSectionDataForWall')" v-model="proCtopsBOObj.crossSectionDataForWall"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="10" style="max-height: 70px;">
                    <el-form-item label="挡水边(外)截面数据：">
                        <el-input class="" clearable @change="testChange('crossSectionDataForOut')" v-model="proCtopsBOObj.crossSectionDataForOut"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="挡水边(靠墙)材质 :">
                        <div class="imgRows">
                            <div class="zzcli" @click="addImg('wall')"></div>
                            <i class="el-icon-circle-plus icons"></i>
                            <span>上传图片</span>
                            <div v-if="shuibian.wall.id&&shuibian.wall.src" class="delIcon">
                                <i class="el-icon-error" @click="delImg('wall')"></i>
                            </div>
                            <img v-if="shuibian.wall.id&&shuibian.wall.src" :src="BASE_URL.sourceBaseUrl+shuibian.wall.src">
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="挡水边(外)材质 :">
                        <div class="imgRows">
                            <div class="zzcli" @click="addImg('out')"></div>
                            <i class="el-icon-circle-plus icons"></i>
                            <span>上传图片</span>
                            <div v-if="shuibian.out.id&&shuibian.out.src" class="delIcon">
                                <i class="el-icon-error" @click="delImg('out')"></i>
                            </div>
                            <img v-if="shuibian.out.id&&shuibian.out.src" :src="BASE_URL.sourceBaseUrl+shuibian.out.src">
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <!--<el-row :gutter="24">-->
            <!--<el-col :span="12">-->
            <!--<el-form-item label="产品材质">-->
            <!--<el-select v-model="product.textureId" @change="selectedTexture" class="selectWidth" placeholder="请选择">-->
            <!--<el-option-->
            <!--v-for="item in productTexture"-->
            <!--:key="item.id"-->
            <!--:label="item.label"-->
            <!--:value="item.id">-->
            <!--</el-option>-->
            <!--</el-select>-->
            <!--</el-form-item>-->
            <!--</el-col>-->
            <!--</el-row>-->

            <transition name="fade">
              <el-row :gutter="24" style="width:870px; margin:1px 1px 20px 1px; padding-top:15px; background: #f8f8f8">
                <h2 class="propTitle"><span class="el-icon-setting"></span>产品规格信息</h2>
                <el-col :span="24" class="propTitle-content" v-for="(item,index) in getProductPorps" :key="index">
                  <h2>{{item.parent.name}}：</h2>
                  <div>
                    <span>{{item.name}}</span>
                  </div>
                </el-col>
                <!--<el-col :span="11" style="height: 60px" v-for="(item,index) in getProductPorps" :key="index">-->
                  <!--<el-form-item :label="item.name">-->
                    <!--<el-select-->
                      <!--v-model="selectedProps[index]"-->
                      <!--value-key="id"-->
                      <!--clearable-->
                      <!--@change="selectPropValues(selectedProps[index],index,item)" placeholder="请选择">-->
                      <!--<el-option-->
                        <!--v-for="chk in item.children"-->
                        <!--:label="chk.name"-->
                        <!--:key="chk.id"-->
                        <!--:value="chk"-->
                      <!--&gt;-->
                      <!--</el-option>-->
                    <!--</el-select>-->
                  <!--</el-form-item>-->
                <!--</el-col>-->
              </el-row>
            </transition>

            <!--<el-row class="">-->
            <!--<el-col :span="12">-->
            <!--<el-form-item label="产品规格：">-->
            <!--<el-input-->
            <!--class="textareaWidth"-->
            <!--type="textarea"-->
            <!--:rows="6"-->
            <!--placeholder="请输入内容"-->
            <!--v-model="baseTextarea">-->
            <!--</el-input>-->
            <!--</el-form-item>-->
            <!--</el-col>-->
            <!--</el-row>-->
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">产品描述信息</el-col>
              <el-col :span="22" class="rightLine">
                <hr>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="产品风格：">
                  <el-select
                    multiple
                    clearable
                    v-model="product.baseStyleId"
                    class="formWidth"
                    placeholder="请选择">
                    <el-option
                      v-for="(item,index) in baseProductStyle"
                      :key="index"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <!--<el-col :span="8">-->
              <!--<el-form-item label="建议售价：">-->
                  <!--<input-->
                    <!--placeholder="请输入内容"-->
              <!--v-model="product.advicePrice"-->
                    <!--class="sizes"-->
              <!--maxlength="10"-->
              <!--clearable-->
              <!--oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"-->
                    <!--style="width: 200px"-->
                  <!--&gt;-->
              <!--<span>{{selectSaleUnionName}}</span>-->
              <!--<p v-if="product.advicePrice < 0" style="color: red">请输入大于等于0的数字</p>-->
                <!--</el-form-item>-->
              <!--</el-col>-->

              <el-col :span="8">
                <el-form-item label="原价：">
                  <input
                          placeholder="请输入内容"
                          v-model="product.price"
                          class="sizes"
                          style="width: 200px"
                  >
                  <el-select v-model="product.saleUnitId"
                             clearable
                             style="background:#eee; width: 110px; " placeholder="请选择">
                    <el-option
                            v-for="(item,index) in unitList"
                            :key="index"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                  </el-select>
                  <p v-if="product.price < 0" style="color: red">请输入大于等于0的数字</p>
                </el-form-item>
              </el-col>

              <el-col :span="10">
                <el-form-item label="装修单价：" label-width="120">
                  <el-input
                          placeholder="请输入内容"
                          v-model="product.decorationPrice"
                          class="input-with-select"
                          style="width: 200px"
                          @blur="verifydecorationPrice"
                  >
                    <template slot="append">{{product.valuationUnit}}</template>
                  </el-input>
                  <p v-show="this.decorationPriceWhos"  class="verify">{{this.decorationPriceTxt}}</p>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row>
              <el-col :span="24">
                <div class="productPic"><span>*</span>产品图片
                  <el-tooltip effect="dark" placement="right">
                      <div slot="content">建议上传图片像素不超 <br> 过2M,尺寸比例1:1</div>
                      <el-button type="text" class="tooltip-btn">?</el-button>
                  </el-tooltip>：
                </div>
                <el-form-item >
                  <ul class="uploadPic">
                    <li>
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
                    <li class="uploadImgList" v-for="(file, index) in fileList2" :key="index" v-dragging="{ item: file, list: fileList2, group: 'file' }">
                      <span class="posDelete" v-show="showDeletes[index]" @click="deleteUploadImg(index)"></span>
                      <img style="border-radius: 8px" width="90" height="90"
                           :src="file.url"/>
                      <span v-if="file.id == defaultPicId" class="upload-mainpic__con">主缩略图</span>
                      <span v-show="showDeletes[index]" @click="setDefaultPic(file,index)" class="upload-notmainpic__con">设置为缩略图</span>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="">
              <el-col :span="24">
                <el-form-item label="产品描述：">
                  <el-input
                    type="textarea"
                    :rows="6"
                    placeholder="请输入内容"
                    v-model="product.desc">
                  </el-input>
                  <p>此处最多允许填写200字</p>
                </el-form-item>
              </el-col>
            </el-row>

            <!--运营分类信息-->
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">关联产品运营信息</el-col>
              <el-col :span="22" class="rightLine">
                <hr>
              </el-col>
            </el-row>
            <el-row class="goods-operating-info">
              <el-col :span="24">

                <h2 class="title" ><span class="star">*</span>运营分类信息：</h2><span @click="productCategoryTree">添加</span>
              </el-col>
            </el-row>
            <!-- <el-row class="goods-operating-infos" v-show="GoodsOperatingList" v-for="(item, index) in productClassList" :key="index" v-if="item.list.length > 0">
              <div class="headline">
                {{item.name}}：
              </div>
              <div class="content">
                <p v-for="(items, key) in item.list" :key="key">{{items}}</p>
              </div>
            </el-row> -->
            <el-row  v-for="(itemI, index) in newProducLists">
                <span class="productName">{{itemI.name}}：</span>
                <el-row class="goods-operating-infos" v-show="GoodsOperatingList" :key="jndex" v-for="(item, jndex) in itemI.list" v-if="item.list.length > 0">
                    <div class="headline">
                        {{item.name}}：
                    </div>
                    <div class="content">
                        <p v-for="(items, jndex) in item.list" :key="jndex">{{items.name}}</p>
                    </div>
                </el-row>
            </el-row>
            <!--运营分类信息-->
            <!--硬装产品-->
            <hardProduct @connectModel="hardConnectModel('edit')" @handleAddModelTexture="addModelTexture" v-if="isHard"></hardProduct>

            <el-row :gutter="24" class="splineFrame" v-if="!isHard">
              <el-col :span="2" class="productBaseInfo">模型相关信息</el-col>
              <el-col :span="22" class="rightLine">
                <hr>
              </el-col>
            </el-row>

            <el-row :gutter="24" v-if="!isHard">
              <el-col :span="24">
                <el-form-item label="模型和贴图：">
                  <el-button round size="samll" v-if="visible.isModel" class="el-button--primary"
                             @click="handleReAssoscateModel">重新关联模型
                  </el-button>
                  <el-button round size="samll" v-if="!visible.isModel" class="el-button--primary"
                             @click="handleReAssTexture">重新关联贴图
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>

            <!--<div class="modelTextureFrame" v-if="!visible.isModel" style="border: none">-->
              <!--<el-row :gutter="24">-->
                <!--<el-col :span="12" class="currentModel">-->
                  <!--<div class="modelPic">-->
                    <!--<img :src="BASE_URL.sourceBaseUrl+product.texture.picPath" width="90" height="90" alt="">-->
                  <!--</div>-->
                  <!--<div class="selectedModel">-->
                    <!--<div>-->
                      <!--<p>模型名称： {{product.texture.name}}</p>-->
                      <!--<p>模型编号： {{product.texture.code}} </p>-->
                    <!--</div>-->
                  <!--</div>-->
                <!--</el-col>-->
                <!--<el-col :span="12" class="addDelModel">-->
                  <!--&lt;!&ndash;<el-button round size="small" @click="handleAddArea">添加区域</el-button>&ndash;&gt;-->
                  <!--<el-button round size="small" @click="handleRemoveModel">取消关联</el-button>-->
                <!--</el-col>-->
              <!--</el-row>-->
            <!--</div>-->
            <!--模型贴图区域-->
            <!--v-if="visible.isModel"-->
            <div class="modelTextureFrame" v-if="visible.isModel" style="border: none">
              <el-row :gutter="24" v-if="assModelInfo.modelCode">
                <el-col :span="12" class="currentModel">
                  <div class="modelPic">
                    <img :src="BASE_URL.sourceBaseUrl + modelList.thumbPicPath" width="90" height="90" alt="">
                  </div>
                  <div class="selectedModel">
                    <div>
                      <p>模型名称： {{modelList.modelName}}</p>
                      <p>模型编号： {{modelList.modelCode}} </p>
                      <p>模型规格：长 {{modelList.length}} X 宽 {{modelList.width}} X 高 {{modelList.height}}</p>
                    </div>
                  </div>
                </el-col>
                 <!--<el-col :span="12" class="addDelModel">
                 //<el-button round size="small" @click="handleAddArea">添加区域</el-button>
                  <el-button round size="small" @click="handleRemoveModel">取消关联</el-button>
                </el-col>-->
              </el-row>
              <!--模型贴图区域-->

              <el-row class="modelTextList" v-if="false">
                <el-row class="textureTitle" v-for="(item,index) in area" :key="index">
                  <el-row class="title">
                    <el-col :span="23">{{item.name}}</el-col>
                  </el-row>
                  <el-row class="uploadTexture">
                    <el-col :span="2">
                      <div class="uploadButton" @click="handleAddModelTexture(item,index)">
                        <p><img class="addPlug" src="../../../assets/images/icons/uploadPlug.png" alt=""></p>
                        <p>上传贴图</p>
                      </div>
                      <span class="priceOffset">售价影响 / 元</span>
                    </el-col>
                    <el-col class="uploadButton" v-for=" (jtem,jndex) in item.areaTextures" :key="jndex">
                      <div @mouseenter="jtem.deleteBtnVisible =true" @mouseleave="jtem.deleteBtnVisible = false"
                           style="position: relative;">
                        <span class="posDelete" v-show="jtem.deleteBtnVisible" style=" z-index:11;"
                              @click="deleteAreaTexture(item,jndex)"></span>
                        <!--<el-col v-if="item.defalutTextureId === jtem.id" class="mainPic">主缩略图</el-col>-->
                        <img v-if="item.isDefault==jndex"
                             style="position: absolute; left:75px; top:-3px; width: 16px; height: 16px; z-index: 11;"
                             src="../../../assets/images/icons/select.png" alt="">
                        <el-radio fill="red" size="medium" style="margin-left: 15px" v-model="item.isDefault"
                                  :label="jndex">&nbsp;
                        </el-radio>
                        <img :src="BASE_URL.sourceBaseUrl+jtem.thumbnailPath" alt="">
                        <span
                          v-if="item.defalutTextureId && item.defalutTextureId === jtem.id"
                          class="mainPic">主缩略图
                        </span>
                      </div>

                      <el-input class="numPrice" v-model="jtem.affectPrice" size="mini"
                                @change="handleChange"></el-input>
                    </el-col>
                  </el-row>
                </el-row>
              </el-row>
            </div>
            <div class="modelTextureFrame" v-if="!visible.isModel">
              <el-row :gutter="24" v-if="product.texture && product.texture.id"> <!--名称-->
                <el-col :span="12" class="currentModel">
                  <div class="modelPic">
                    <img :src="BASE_URL.sourceBaseUrl+product.texture.picPath" alt="">
                  </div>
                  <div class="selectedModel">
                    <div>
                      <p>贴图名称：{{product.texture.name}}</p>
                      <p>贴图编号：{{product.texture.code}}</p>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12" class="addDelModel">
                  <!--<el-button round size="small" @click="handleAddArea">添加区域</el-button>-->
                  <!-- <el-button round size="small" @click="handleRemoveTexture" >取消关联</el-button> -->
                </el-col>
              </el-row>
            </div>
            <el-form-item style="margin-top: 20px">
              <el-button round class="confirmBtn marRight" type="primary" @click="submitForm('ruleForm')">确定</el-button>
              <el-button class="cancelBtnBg" round @click="resetForm('ruleForm')">取消</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!--模型贴图区域 start-->
        <el-dialog title="关联模型" :visible.sync="assModelDia.dialogModelVisible" :before-close="handleClose">
          <el-form :model="assModelDia.query">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item>
                  <el-input clearable v-model="assModelDia.query.modelCode" placeholder="请输入模型编号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item>
                  <el-input clearable v-model="assModelDia.query.modelModelNum" placeholder="请输入模型型号"></el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
                <el-form-item>
                  <el-input clearable v-model="assModelDia.query.author" placeholder="请输入模型作者"></el-input>
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item>
                  <el-input clearable v-model="assModelDia.query.modelName" placeholder="请输入模型名称"></el-input>
                </el-form-item>
              </el-col>
               <el-col :span="12">
                <el-form-item >
                  <el-button round class="searchBtn" @click="handleReAssoscateModel">搜索</el-button>
                </el-form-item>
              </el-col>
            </el-row>


          </el-form>
          <el-table :data="assModelDia.datas" height="550">
            <el-table-column property="thumbPicPath" label="模型缩略图">
              <template slot-scope="scope">
                <img :src="BASE_URL.sourceBaseUrl+scope.row.thumbPicPath" width="90" height="90" alt="">
              </template>
            </el-table-column>
            <el-table-column prop="modelCode" label="模型编号"></el-table-column>
            <el-table-column prop="modelName" label="模型名称"></el-table-column>
            <el-table-column prop="categoryNames" label="模型分类"></el-table-column>
            <el-table-column
                    width="100"
                    label="模型规格">
              <template slot-scope="scope">
                长：{{scope.row.length}}  宽：{{scope.row.width}}  高：{{scope.row.height}}
              </template>
            </el-table-column>
            <el-table-column prop="modelModelNum" label="模型型号"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="primary" @click="handleReAssModelConfirm(scope.row)">选用</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>
        <!--模型贴图区域 end-->
        <!--模型上传贴图-->
        <el-dialog :close-on-click-modal="false" title="上传贴图" top="1vh"
                   :before-close="modelClose"
                   :visible.sync="assModelTextureDia.dialogModelTextureVisible">
          <el-form class="textureSearch" :model="assModelTextureDia.query">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-select clearable v-model="assModelTextureDia.query.texture" @change="ListModelTexture"
                           placeholder="请选择材质/材料">
                  <el-option
                    v-for="(item,index) in textureTextures"
                    :key="index"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="12">
                <el-select clearable v-model="assModelTextureDia.query.textureAttr" @change="ListModelTexture"
                           placeholder="请选择材质贴图表面属性">
                  <el-option v-for="(item,index) in textureAttrDrop"
                             :key="index"
                             :label="item.name"
                             :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-input clearable placeholder="请输入材质贴图名称" v-model="assModelTextureDia.query.textureName"></el-input>
              </el-col>
              <el-col :span="12">
                <el-input clearable placeholder="请输入材质贴图编号" v-model="assModelTextureDia.query.textureCode"></el-input>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-select v-model="assModelTextureDia.query.textureType" clearable @change="ListModelTexture"
                           placeholder="请选择材质贴图类型">
                  <el-option
                    v-for="(item,index) in textureType"
                    :key="index"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="12">
                <el-input clearable placeholder="请输入材质型号" v-model="assModelTextureDia.query.modelNumber"></el-input>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-button round class="searchBtn" @click="resetTextrue">重置</el-button>
                <el-button round class="searchBtn" @click="ListModelTexture(1)">搜索</el-button>
              </el-col>
            </el-row>
            <div class="splitLine"></div>
            <el-row :gutter="24">
              <el-col :span="12" style="text-align: left">
                共 {{total}} 张，已选 {{multipleSelection.length}} 张
              </el-col>
              <el-col :span="12" style="text-align: right">
                <el-button class="el-button--primary" round @click="handleModelTextureAddConfirm">确定添加</el-button>
              </el-col>
            </el-row>
            <el-row class="uploadTexture2">
              <el-table
                height="450"
                :data="assModelTextureDia.datas"
                @selection-change="handleSelectionChange"
              >
                <el-table-column
                  type="selection"
                  label="全选"
                  width="55" title="全选"></el-table-column>
                <el-table-column prop="thumbnailPath" label="缩略图">
                  <template slot-scope="scope">
                    <div v-if="scope.row.thumbnailPath">
                      <img :src="BASE_URL.sourceBaseUrl + scope.row.thumbnailPath" width="90" height="90">
                    </div>
                    <div class="nonImg" v-else> 暂无图片</div>
                  </template>
                </el-table-column>
                <el-table-column prop="textureCode" label="材质贴图编号"></el-table-column>
                <el-table-column prop="textureName" label="材质贴图名称"></el-table-column>
                <el-table-column prop="modelNumber" label="材质贴图型号"></el-table-column>
                <el-table-column prop="textureballFileId" label="材质类型">
                  <template slot-scope="scope">
                    <p>{{scope.row.textureballFileId == 0 ? '贴图' : '材质球'}}</p>
                  </template>
                </el-table-column>
              </el-table>
              <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="page"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="assModelTextureDia.query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
              </div>

              <!--<el-checkbox-group v-model="assModelTextureDia.tempSelectedModelTextures">
                <el-checkbox label="" style="display: none"></el-checkbox>
                <div class="itemsPos" v-for="tl in assModelTextureDia.datas" :key="tl.textureId">
                  <img src="../../../assets/images/icons/select.png"
                       v-if="selectedChk(tl.textureId)"
                       class="selected"
                       width="17" height="16">
                  <el-tooltip placement="top">
                    <div slot="content" style="line-height: 22px"> &lt;!&ndash;:label="tl.textureId"&ndash;&gt;
                      材质名称：{{tl.textureName}}<br/>
                      材质编号：{{tl.textureCode}}<br/>
                      材质型号：{{tl.modelNumber}}<br/>
                      贴图类型：{{tl.textureballFileId == 0 ? '贴图' : '材质球'}}
                    </div>
                    <el-checkbox class="chks" :key="tl.textureId" :label="tl.textureId"></el-checkbox>
                  </el-tooltip>
                  <el-col :span="3" class="textureImg">
                    <img width="90" height="90" :src="BASE_URL.sourceBaseUrl+tl.thumbnailPath"/>
                  </el-col>
                </div>
              </el-checkbox-group>-->
            </el-row>
          </el-form>
        </el-dialog>
        <!--关联贴图上传贴图-->
        <el-dialog title="上传贴图" :close-on-click-modal="false" top="5vh" :visible.sync="assTextureDia.dialogAddTextureVisible"
                   :before-close="beforeClose">
          <el-form label-width="110px" :model="assTextureDia.query">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="材质/材料">
                  <el-select style="width:100%" clearable v-model="assTextureDia.query.texture" @change="searchTexture"
                             placeholder="请选择">
                    <el-option
                      v-for="(item,index) in ProductTextureDrop"
                      :key="index"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="贴图表面属性">
                  <el-select style="width:100%" clearable v-model="assTextureDia.query.textureAttr"
                             @change="searchTexture" placeholder="请选择">
                    <el-option v-for="(item,index) in textureAttrDrop"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="贴图类型">
                  <el-select v-model="assTextureDia.query.textureType" clearable @change="searchTexture"
                             placeholder="请选择材质类型">
                    <el-option
                      v-for="(item,index) in textureType"
                      :key="index"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="材质贴图名称">
                  <el-input clearable v-model="assTextureDia.query.textureName"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="材质型号">
                  <el-input clearable v-model="assTextureDia.query.modelNumber"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="材质贴图编号">
                  <el-input clearable v-model="assTextureDia.query.textureCode"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-col :span="12">
              <el-form-item>
                <el-button round class="el-button--primary" @click="searchTexture">搜索</el-button>
              </el-form-item>
            </el-col>
          </el-form>
          <el-table :data="TextureListData" height="420" v-loading="textureLoading">
            <el-table-column prop="thumbnailPath" label="缩略图">
              <template slot-scope="scope">
                <img width="90" height="90" :src="BASE_URL.sourceBaseUrl+scope.row.thumbnailPath" alt="">
              </template>
            </el-table-column>
            <el-table-column prop="textureCode" label="材质贴图编号"></el-table-column>
            <el-table-column prop="textureName" label="材质贴图名称"></el-table-column>
            <el-table-column prop="modelNumber" label="材质型号">
              <template slot-scope="scope">
                <div class="modifyTime" v-if="scope.row.modelNumber">{{scope.row.modelNumber}}</div>
                <div v-else>———</div>
              </template>
            </el-table-column>
            <el-table-column prop="textureballFileId" label="贴图类型" align="center" width="100">
              <template slot-scope="scope">
                <div class="modifyTime">{{scope.row.textureballFileId == 0 ? '贴图' : '材质球'}}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button @click="handleAssocateTextureConfirm(scope.row)">选用</el-button>
              </template>
            </el-table-column>
          </el-table>
           <div class="pagePagination">
                  <el-pagination
                          @size-change="handleSizeChange"
                          @current-change="handleTextureChange"
                          :current-page.sync="assTextureDia.query.page"
                          :page-sizes="[10, 50, 100, 200, 500]"
                          :page-size="assTextureDia.query.limit"
                          layout="total, sizes, prev, pager, next, jumper"
                          :total="textureTotal">
                  </el-pagination>
              </div>
        </el-dialog>

        <!--添加硬装模型弹窗-->
        <addHardProduct ref="addHardProduct"></addHardProduct>
        <!--设置产品规格信息-->
        <setProductInfo ref="setProductInfo"></setProductInfo>
      </div>
    </div>
  </transition>
</template>
<script>

    import ElRow from 'element-ui/packages/row/src/row'
    import {PointNum, PointTwo} from "../../../filters/data";
    import hardProductMinixs from "../../../filters/hardProductMinixs";
    import hardProduct from './conponents/hardProduct'; //硬装产品
    import addHardProduct from './conponents/addHardProduct'; //硬装产品
    import setProductInfo from './conponents/setProductInfo'; //硬装产品
    import qs from 'qs'

    export default {
    components: {ElRow,hardProduct,addHardProduct,setProductInfo},
    mixins:[hardProductMinixs],
    data() {
      return {
        loading:'',
          nav: [
              { nav: "产品库", url: "/product" },
              { nav: "产品编辑", url: "" }
          ],
          headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
          page:1,
          total:0,
          textureTotal:0,
          disabled:[true,true,true],
          shuibian:{wall:{},out:{}},
          textureLoading: true,
          smallType:'',
          restaurants: [],
          state4: '',
          proCtopsBOObj:{crossSectionDataForWall:undefined,crossSectionDataForOut:undefined},
          timeout:  null,
          checked:true,
          modelList: {},
          infoClassifyVisible: false,
          isReset: false,
          GoodsOperatingList: [],
          productCategorylist1: [], // 产品分类总列表
          productCategorylist2: [], // 产品分类第2层列表
          productCategorylist3: [], // 产品分类第3层列表
          productCategorylist4: [], // 产品分类第4层列表
          productCategorylist5: [], // 产品分类第5层列表
          checkProductList: [], // 最终选择的产品分类列表
          productClassList: [], // 选择完产品分类，所需要展示在页面上的分类列表
          productClassLists: [], // 零时保存产品分类列表
          productClassIdLists: [], // 产品分类的ID列表
          indexList: {index1: 0, index2: 0, index3: 0, index4: 0, index5: 0}, // 每一层分类的index
          showProductClassList: [], // 页面加载保存处理前的产品分类数据
        productHeightTxt: '',
        productWidthTxt: '',
        productLengthTxt: '',
        bedsideDimensionsTxt: '',
        heightWhos: false,
        lengthWhos: false,
        widthWhos: false,
        bedsideDimensionsWhos: false,
        showTextureDeletes: [],
        showDeletes: [],
        area: [],
        visible: {
          isModel: false,
          isTexture: false
        },
        textureType: [
          {
            value: 'texture',
            label: '贴图'
          },
          {
            value: 'ball',
            label: '材质球'
          }
        ],
        assModelTextureDia: { // 关联模型贴图的dialog的数据
          query: {
            page: 0,
            limit: 10,
            companyId: sessionStorage.getItem('companyID')
          },
          datas: [],
          tempSelectedModelTextures: [],
          areaId: 0,
          index: 0,
          dialogModelTextureVisible:false
        },
        assModelDia: { // 关联模型 模型列表窗口的数据
          dialogModelVisible: false,
          query: {
            page: 0,
            limit: 10,
            isUsed: 0,
            companyId: sessionStorage.getItem('companyID'),
            transStatus: 'SUCCESS',
            modelType: ''
          },
          datas: []
        },
        assModelInfo: { // 关联模型信息
          modelName: '',
          modelCode: '',
          thumbPicPath: ''
        },
        assTextureDia: {
          dialogAddTextureVisible: false,
          texture: {
            id: 0
          },
          query: {
            companyId: sessionStorage.getItem('companyID')
          }
        },

        selectedProps: [], // 选中的属性
        editProID: '',
        uploadParams: { // 上传的信息
          platform: 'web',
          module: 'product',
          type: 'image'
        },
        multipleSelection: [],
        selectedTextureId: '', // 材质ID
        selectStyleId: '', // 产品风格ID
        selectedSeriesId: '', // 产品系列ID
        selectedBrandId: '', // 产品品牌ID
        selectCategoryCode: '', // 产品分类编码
        unitList: [], // 数字字典
        input5: '',
        select: '',
        baseTextarea: '',
        prodTextarea: '',
        advicePriceInput1: '',
        advicePriceInput2: '',
        productCategoryDrop: [], // 获取分类下拉框
        productCategory: '',
        productSeries: [], // 产品系列
        seriesValue: '',
        productStyle: [], // 产品款式
        StyleValue: '',
        productTexture: [], // 产品材质
        TextureValue: '',
        baseProductStyle: [], // 产品风格
        brandValue: '',
        baseValue: '',
        TextureValue2: '', // 产品材质
        props: { // 产品分类属性名
            label: 'name',
            value: 'keyCode',
            disabled: 'checkModel',
            children: 'children'
        },
        getProductPorps: [], // 产品属性
        // 上传图片文件列表
        imageUrl: '',
        fileList2: [],
        productBrand: [],
        productPropBO: [],
        textureAttrDrop: [],
        textureTextures: [],
        categoryCodeName: '',
        selectSaleUnionName: "元",
        product: {
          productSpec:'',
          categoryCodes: [],
          proCtopsBO:[],
          baseStyleId: []
        }, // 产品更新
        // 品牌结束
        defaultPicId: 0,
        modelId: -1,
        TextureListData: [],
        ProductTextureDrop: [],
        // 验证表单规则
        rules: {
          name: [
            {required: true, message: '请输入产品名称', trigger: 'blur'},
            {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'},
          ],
          // productSeriesStr:[
          //     {required: true, message: '请选择产品系列', trigger: 'blur'},
          //     {min: 1, max: 40, message: '长度在 40 个字符', trigger: 'blur'}
          // ],
            modelNumber: [
            {required: true, message: '请选择产品型号', trigger: 'blur'},
            {required: true, message: '请选择产品型号', trigger: 'change'},
            {min: 1, max: 40, message: '长度在 40 个字符', trigger: 'blur'}
            //   { required: true, message: '请输入活动名称', trigger: 'blur' },
            //   { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          // categoryCodes: [
          //   {required: true, message: '请选择产品分类', trigger: 'change'}
          // ],
          brandId: [
            {required: true, message: '请选择产品品牌', trigger: 'change'}
          ],
            bedsideDimensions: [
                {required: true, message: '请输入床沿尺寸', trigger: 'blur'}
            ]
          // length:[
          //   { validator: verifyLength, trigger: 'blur' }
          // ],
          // width: [
          //   { validator: verifyWidth, trigger: 'blur' }
          // ],
          // height: [
          //   { validator: verifyHeight, trigger: 'blur' }
          // ]
        },
        popupType: '', // 弹窗类型，用于判断选择贴图属于贴图还是模型
        productTypes:0,
        producListNum:[],
        applyAreaObj:[
          {applyArea:''},
          {applyArea:''},
          {applyArea:''},
        ],
        popupType2: '', // 弹窗类型，挡水边贴图~
        applyAreaList:[[
            {id:1,name:'0~5'},{id:2,name:'5~10'},{id:3,name:'10~15'},{id:4,name:'15~20'},{id:5,name:'20~25'},{id:6,name:'25~30'},
            {id:7,name:'30~35'},{id:8,name:'35~40'},{id:9,name:'40~50'},{id:10,name:'50~60'},{id:11,name:'60~70'},{id:14,name:'70~~'}
        ],[
            {id:1,name:'0~5'},{id:2,name:'5~10'},{id:3,name:'10~15'},{id:4,name:'15~20'},{id:5,name:'20~25'},{id:6,name:'25~30'},
            {id:7,name:'30~35'},{id:8,name:'35~40'},{id:9,name:'40~50'},{id:10,name:'50~60'},{id:11,name:'60~70'},{id:14,name:'70~~'}
        ],[
            {id:1,name:'0~5'},{id:2,name:'5~10'},{id:3,name:'10~15'},{id:4,name:'15~20'},{id:5,name:'20~25'},{id:6,name:'25~30'},
            {id:7,name:'30~35'},{id:8,name:'35~40'},{id:9,name:'40~50'},{id:10,name:'50~60'},{id:11,name:'60~70'},{id:14,name:'70~~'}
        ]],
        decorationPrice: '',
        decorationPriceTxt: '',
        decorationPriceWhos: false,
        isMultistageList:false,//添加运营分类信息是否可多级
        lastIsMultistageList:false,
        newProducLists: [], // 展示的产品分类
        lastNewProducLists:{},
      }
    },
    computed:{
      isConnectBtn(){
          if(sessionStorage.getItem('isConnectBtn')=='false'){
            return false;
          }else{
            return true;
          }
      },
    },
    mounted() {
      this.$store.commit('breadcrumb3', '产品编辑');
      this.$dragging.$on('dragged', ({ value }) => { //拖拽事件 触发
          this.defaultPicId=value.list[0].id;
      })
    },
    methods: {
        handleCurrentChange(val){
            this.page=val;
            this.assModelTextureDia.query.page=val-1;
            this.ListModelTexture()
        },

        handleTextureChange(){
            this.searchTexture();
        },

        handleSizeChange(val){
            this.page=1;
            this.assModelTextureDia.query.page=0;
            this.assModelTextureDia.query.limit=val;
            this.ListModelTexture()
        },
        addBrandId() {
          this.selectedBrandId = this.product.brandId;
        },
        /*页面加载后把请求后没有处理过的数据（this.showProductClassList）处理成需要的结构，在页面创建前调用*/
        disposeProductClassList() {
            let arr = [];
            let idArr = [];
            for (let i = 0; i < this.showProductClassList.length; i++) {
                let obj = {};
                obj.name = this.showProductClassList[i].parentName;
                obj.list = [];
                arr.push(obj);
                idArr.push(this.showProductClassList[i].id);
            }
            for (let i = 0; i < arr.length; i++) {
                for (let j = i + 1; j < arr.length; j++) {
                    if (arr[i].name == arr[j].name) {
                        arr.splice(j, 1);
                        j--
                    }
                }
            }
            for (let i = 0; i < this.showProductClassList.length; i++) {
                for (let j = 0; j < arr.length; j++) {
                    if (this.showProductClassList[i].parentName == arr[j].name) {
                        arr[j].list.push(this.showProductClassList[i].name)
                    }
                }
            }
            this.productClassList = arr;
            //this.checkProductList = idArr;
        },
        /*回显产品分类信息*/
        showProductCategory () {
            this.API.AddProductCategory().then((res) => {
                if (res.data) {
                    res.data.map((a,ai)=>{
                        a.children?a.children.map(b=>{b.children?b.children.map(c=>{c.children?c.children.map(d=>{d.children?d.children.map(e=>{
                          e.secondLevelName=b.name;
                          e.parentName=d.name;
                          e.checked = '';
                          // this.checkProductList.map((che,index)=>{
                          //   if(che.id==e.id){
                          //     e.checked = true;
                          //   }
                          // })
                        }):''}):''}):''}):'';
                    })
                    this.productCategorylist1 = res.data;
                }
            });
        },
        /*点击添加，弹出产品分类信息弹窗*/
        productCategoryTree() {
            this.infoClassifyVisible = true;
        },
        /*
       * 分类树结构点击筛选出所选择目录下的分类，
       * i: 所属分类树的层级；
       * index: 所选择当前分类层级下的下标；
       * 在i=3的时候（第三层级），创建一个数组this.productClassIdLists列表用于存储将要显示在页面上的品牌类和名称
       * 在i=5的是后（最后一层），把已选品牌下的种类名称添加到this.productClassIdLists下每个对应品牌下的list
       * */
        selectType(index, i,items) {
            let self=this;
                if (i == 1) {
                    if (this.indexList.index1 != index) {
                        this.productCategorylist2 = [];
                        this.productCategorylist3 = [];
                        this.productCategorylist4 = [];
                        this.productCategorylist5 = [];
                    }
                    this.indexList.index1 = index;
                    this.productCategorylist2 = items.children
                }
                if (i == 2) {
                    if (this.indexList.index2 != index) {
                        this.productCategorylist3 = [];
                        this.productCategorylist4 = [];
                        this.productCategorylist5 = [];
                    }
                    this.indexList.index2 = index;
                    this.productCategorylist3 = items.children
                    this.isMultistageList = this.productCategorylist2[index].isMultiSelect?true:false;
                }
                if (i == 3) {
                    if (this.indexList.index3 != index) {
                        this.productCategorylist4 = [];
                        this.productCategorylist5 = [];
                    }
                    this.indexList.index3 = index;
                    let newArr = [],delItem = [],saveItem = [];
                    this.checkProductList?this.checkProductList.map(item=>{
                        let fl = (item.pIndex2||item.pIndex2==0)&&this.productCategorylist2[item.pIndex2].id==this.productCategorylist3[index].pid&&index!=item.pIndex3;
                        fl?delItem.push(item):saveItem.push(item);
                    }):'';
                    
                    this.checkProductList = saveItem;
                    this.productCategorylist4 = items.children
                }
                if (i == 4) {
                    this.indexList.index4 = index;
                    this.productCategorylist5 = [];
                    this.productCategorylist5 = items.children;
                    this.productCategorylist5.map((v,k)=>{
                      v.checked = '';
                      this.checkProductList.map(h=>{
                          if(v.id==h.id){  
                            v.checked = true;     
                          }
                      })
                    })
                }
                
                if (i == 5) {
                  if(this.productCategorylist5[index].checked){
                    this.productCategorylist5[index].checked = false;
                    let delIndex = '';
                    this.checkProductList.map((v,i)=>{
                      if(this.productCategorylist5[index].id==v.id){
                        delIndex = i;
                      }
                    })
                    this.checkProductList.splice(delIndex,1);
                  }else{
                    this.productCategorylist5[index].checked = true;
                    this.checkProductList.push(this.productCategorylist5[index])
                  }
                  this.$set(this.productCategorylist5,index,this.productCategorylist5[index])
                  
                }
        },
        /*分类树赛选完后点击确认*/
        verifyProductClass() {
            let flag = false;
                let pNameArr = [],pNameArr2 = [],indexArr = [],adArr = [];
                this.checkProductList.map((v,i)=>{
                    this.productCategorylist1.forEach((a,ai)=>{
                        a.children?a.children.forEach((b,bi)=>{
                            b.children?b.children.forEach((c,ci)=>{
                                c.children?c.children.forEach((d,di)=>{
                                    d.children?d.children.forEach((e,ei)=>{
                                        if(e.id==v.id){
                                            indexArr[i] = [ai,bi,ci,di,ei]
                                            v.pIndex3 = ci;
                                            v.pIndex2 = bi;
                                        }
                                    }):'';
                                }):'';
                            }):'';
                        }):'';
                    })
                    this.isMultistageList&&this.lastIsMultistageList?pNameArr.push(v.secondLevelName):(pNameArr = [v.secondLevelName]);
                    pNameArr2.push(v.parentName);
                })
                this.lastIsMultistageList = this.isMultistageList;
                
                pNameArr = Array.from(new Set(pNameArr));
                pNameArr2 = Array.from(new Set(pNameArr2));
                let objs = [];
                let newList = [];
                pNameArr.forEach((pName,pIndex)=>{
                    objs[pIndex] = {name: pName, list: []}
                    pNameArr2.forEach((pName2,pIndex2)=>{
                        objs[pIndex].list[pIndex2] = { name: '', list: []}
                        this.checkProductList.forEach((v,i)=>{
                            let flags = true;
                            objs[pIndex].list[pIndex2].list.map(li=>{
                              if(li.id == v.id) flags = false;
                            })
                            if(v.parentName==pName2&&v.secondLevelName==pName&&flags){
                                objs[pIndex].list[pIndex2].list.push(v);
                                objs[pIndex].list[pIndex2].name = v.parentName
                                newList.push(v);
                            } 
                        })
                    })
                })
                this.checkProductList = newList;
                this.newProducLists = objs;
                this.infoClassifyVisible = false;
        },

        /*点击添加，弹出产品分类信息弹窗*/
        productCategoryTree() {
            this.infoClassifyVisible = true;
            this.API.AddProductCategory().then((res) => {
                if (res.data) {
                    res.data.map((a,ai)=>{
                        a.children?a.children.map(b=>{b.children?b.children.map(c=>{c.children?c.children.map(d=>{d.children?d.children.map(e=>{
                          e.secondLevelName=b.name;
                          e.parentName=d.name;
                          this.checkProductList.map(che=>{
                            if(che.id==e.id){
                              //e.checked = true;
                            }
                          })
                        }):''}):''}):''}):'';
                    })
                    this.productCategorylist1 = res.data;
                }
            })
        },
        // querySearchSeries(queryString,cb){
        // this.API.ProductSeriesDrop().then((res) => {
        //   if(res.data.length){
        //             let productSeries = this.productSeries;
        //             let results = queryString ? productSeries.filter(this.createStateFilter(queryString)) : productSeries;
        //             clearTimeout(this.timeout);
        //             this.timeout = setTimeout(() => {
        //                 cb(res.data);
        //             }, 1000 * Math.random());
        //   }
         
        // })
        // },

        querySearchAsync(queryString, cb) {
            this.API.modelNumberRecommand({
                modelNumber: this.product.modelNumber,
                brandId: this.selectedBrandId,
                companyId: sessionStorage.getItem('companyID')
            }).then((res) => {
                if(res.data.length) {
                    let restaurants = this.restaurants;
                    let results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
                    clearTimeout(this.timeout);
                    this.timeout = setTimeout(() => {
                        cb(res.data);
                    }, 1000 * Math.random());
                }else {
                    setTimeout(() => {
                        cb([{value:'暂无数据'}]);
                    }, 1000 * Math.random());
                }
            })
        },
        createStateFilter(queryString) {
            return (state) => {
                return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        handleSelect(item) {
            console.log(item);
        },

        getRecommand(){
            this.API.modelNumberRecommand({
                modelNumber: this.ruleForm.productTypeCodeStr,
                brandId: this.selectedBrandId,
                companyId: sessionStorage.getItem('companyID')
            }).then((res) => {
                if(res.data.length) {
                    this.recommandData = res.data;
                }
            })
        },

        loadAll() {
            return this.recommandData
        },
      verifyLength() {
          if(!PointNum(this.product.length)&&this.product.length){
              this.productLengthTxt = '请输产品长度为正整数';
               this.product.length = '';
               this.lengthWhos = true;
          }else {
          this.lengthWhos = false;
        } 
      },
      verifyWidth () {
        // let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
        // if (!i.test(this.product.width)) {
        //   this.productWidthTxt = '请输产品宽度为正整数或小数点后两位数';
        //   this.widthWhos = true;
        //    this.product.width = '';
        // } else {
        //   this.widthWhos = false;
        // }
         if(!PointNum(this.product.width)&&this.product.width){
              this.productWidthTxt = '请输产品宽度度为正整数';
               this.product.width = '';
               this.widthWhos = true;
          }else {
          this.widthWhos = false;
        } 
      },
      verifyHeight () {
      //  let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      //   if (!i.test(this.product.height)) {
      //     this.productHeightTxt = '请输产品高度为正整数或小数点后两位数';
      //     this.heightWhos = true;
      //     this.product.height = '';
      //   } else {
      //     this.heightWhos = false;
      //   }
      if(!PointNum(this.product.height)&&this.product.height){
              this.productHeightTxt = '请输产品高度为正整数';
               this.product.height = '';
               this.heightWhos = true;
          }else {
          this.heightWhos = false;
        } 
      },
      verifybedsideDimensions () {
          if(!PointNum(this.product.bedsideDimensions)&&this.product.bedsideDimensions){
              this.bedsideDimensionsTxt = '请输床沿尺寸为正整数';
              this.bedsideDimensionsWhos = true;
              this.product.bedsideDimensions   = '';
          }else {
              this.bedsideDimensionsWhos = false;
          }
      },
      verifydecorationPrice () {
          if(!PointTwo(this.product.decorationPrice)&&this.product.decorationPrice){
              this.decorationPriceTxt = '请输装修单价为正整数或小数点后两位数';
              this.decorationPriceWhos = true;
              this.product.decorationPrice   = '';
          }else {
              this.decorationPriceWhos = false;
          }
      },
      resetTextrue() {
        this.assModelTextureDia.query = {
          page: 0,
          limit: 10,
          companyId: sessionStorage.getItem('companyID')
        }
      },
      deleteAreaTexture(item, j) {
        item.areaTextures.splice(j, 1)
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      selectedChk(id) {
        return this.assModelTextureDia.tempSelectedModelTextures.includes(id)
      },
      // 关联贴图
      handleReAssTexture() {
        this.textureLoading = true
        this.assTextureDia.dialogAddTextureVisible = true
        this.assTextureDia.query.queryType = 'success'
        this.assTextureDia.query.page=1
        this.assModelDia.query.limit=10
        this.API.ListTexture(this.assTextureDia.query).then((res) => {
          if(res){
              this.textureTotal = res.total
              this.textureLoading = false
              this.TextureListData = res.list
          }
   
        })
      },
      // 选用贴图
      handleAssocateTextureConfirm(row) {
        this.assTextureDia.dialogAddTextureVisible = false
        if(this.popupType2){
            this.shuibian[this.popupType2] = {src:row.thumbnailPath,id:row.textureId}
            this.popupType2 = '';
            return;
        } 
        this.assTextureDia.query.texture = ''
        this.assTextureDia.query.textureAttr = ''
        this.assTextureDia.query.textureType = ''
        this.assTextureDia.query.textureName = ''
        this.assTextureDia.query.modelNumber = ''
        this.assTextureDia.query.textureCode = ''
        this.product.texture = {
          name: row.textureName,
          code: row.textureCode,
          picPath: row.thumbnailPath,
          id: row.textureId
        };
        this.visible.isTexture = true
      },
        beforeClose() {
        this.assTextureDia.dialogAddTextureVisible = false;
        this.assTextureDia.query.texture = ''
        this.assTextureDia.query.textureAttr = ''
        this.assTextureDia.query.textureType = ''
        this.assTextureDia.query.textureName = ''
        this.assTextureDia.query.modelNumber = ''
        this.assTextureDia.query.textureCode = ''
      },
      modelClose(){
          this.assModelTextureDia.dialogModelTextureVisible=false;
          this.assModelTextureDia.query.textureAttr='';
          this.assModelTextureDia.query.texture='';
          this.assModelTextureDia.query.textureName='';
          this.assModelTextureDia.query.textureCode='';
          this.assModelTextureDia.query.textureType='';
          this.assModelTextureDia.query.modelNumber='';
      },
      // 材质材料
      getProductTextureDrop() {
        this.API.textureCaiLiao().then((res) => {
          this.ProductTextureDrop = res.data
        })
      },
      // 上传贴图搜索
      searchTexture() {
        this.textureLoading = true
        this.assTextureDia.query.queryType = 'success'
        this.API.ListTexture(this.assTextureDia.query).then((res) => {
              if (res && res.code == 200) {
                    this.textureLoading = false
                    this.TextureListData = res.list ? res.list : []
                    this.textureTotal = res.total
              }else{
                  this.textureLoading = false
                  this.TextureListData = res.list ? res.list : []
              }          
        })
      },
      // 移除贴图
      handleRemoveTexture() {
        this.product.texture={};
        this.assTextureDia.texture = {}
        this.visible.isTexture = true
      },
      // 移除模型
      handleRemoveModel() {
        this.visible.isModel = true
        this.assModelInfo = {}
        this.area = []
        this.modelId = -1
      },
//       handleModelTextureAddConfirm() { // click the model texture button an then invoke this method
//         /* this.assModelTextureDia.dialogModelTextureVisible = false
//         let tempSelectedModelTextures = this.assModelTextureDia.tempSelectedModelTextures
// //        this.area[this.modelTexture.index].isDefault = 1
//         this.area[this.assModelTextureDia.index].areaTextures = this.assModelTextureDia.datas
//           .filter(it => tempSelectedModelTextures.includes(it.textureId))
//           .map(it => Object.assign(it, {id: it.textureId, thumbnailPath: it.thumbnailPath, affectPrice: 0})) */
//         // 把选择的贴图列表放入到区域的地方
//         this.assModelTextureDia.dialogModelTextureVisible = false;
//         this.area[this.assModelTextureDia.index].areaTextures = this.multipleSelection.map(it => ({
//           id: it.textureId,
//           thumbnailPath: it.thumbnailPath,
//           affectPrice: 0,
//           deleteBtnVisible: false
//         }));
//         /* this.area[this.assModelTextureDia.index].areaTextures = this.assModelTextureDia.datas
//            .filter(it => tempSelectedModelTextures.includes(it.textureId))
//            .map(it => Object.assign(it, {id: it.textureId, thumbnailPath: it.thumbnailPath, affectPrice: 0})) */
//       },
        handleModelTextureAddConfirm() {
            if(sessionStorage.getItem('addProductType')=='soft'){ //之前编辑软装的逻辑 不改动
                this.assModelTextureDia.dialogModelTextureVisible = false;
                this.area[this.assModelTextureDia.index].areaTextures = this.multipleSelection.map(it => ({
                    id: it.textureId,
                    thumbnailPath: it.thumbnailPath,
                    affectPrice: 0,
                    deleteBtnVisible: false
                }));
            }else { //硬装产品逻辑
                this.$store.dispatch('addTextTure',this.multipleSelection);
                this.assModelTextureDia.dialogModelTextureVisible = false;
            
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
                this.$store.dispatch('setHardModel',this.gettersHardModelList)
            }
        },
      handleReAssModelConfirm(row) { // 重新关联模型确认
        if (row.modelId) {
          this.modelId = row.modelId
          this.assModelInfo = row
          this.modelList = row;
//           this.API.modelTextureDetail({
//             modelId: row.modelId
//           }).then((res) => {
//
//             this.area = res.data.map(it => {
//                 Object.assign({}, it, {
//                 areaTextures: it.rels.map(tit => Object.assign({}, {
//                   thumbnailPath: tit.picPath,
//                  deleteBtnVisible: false
//                 }, tit))
//               })}
//             )
//             this.assModelDia.query.modelCode = ''
//             this.assModelDia.query.author = ''
//             this.assModelDia.query.modelName = ''
// //            this.relateModelInfo.isShow = true
//           })
          this.assModelDia.dialogModelVisible = false;
        }
      },
      handleClose() {
        this.assModelDia.dialogModelVisible = false
        this.assModelDia.query.modelCode = ''
        this.assModelDia.query.author = ''
        this.assModelDia.query.modelName = ''
      },
      handleReAssoscateModel() { // 点击重新关联模型
        this.assModelDia.dialogModelVisible = true
        this.assModelDia.query.categoryCodes = this.selectCategoryCode
        this.API.ListModel(this.assModelDia.query).then((res) => {
          this.assModelDia.datas = res.list
        })
      },
      handleAddModelTexture(area, index) { // 点击添加区域贴图
        this.assModelTextureDia.dialogModelTextureVisible = true
        this.assModelTextureDia.areaId = area.id
        this.assModelTextureDia.index = index
        this.ListModelTexture()
//        this.area[index].push({price: 10, defaultThumb: true})
      },
      ListModelTexture(type) { // 获取模型材质
        type==1 ? this.assModelTextureDia.query.page=0 : '';
        this.assModelTextureDia.query.queryType = 'success';
        this.API.ListTexture(this.assModelTextureDia.query).then((res) => {
          this.assModelTextureDia.datas = res.list;
          this.total=res.total;
        })
      },
      selectPropValues(val, index, item) { // 选产品属性
        this.productPropBO[index] = val
        this.productPropBO[index].parent = {
          code: item.code,
          id: item.id,
          name: item.name
        }
      },
      // 设置为主缩略图
      setDefault(imgs) {
      },
      // 获取产品属性
      // getProductProps() {
      //   this.selectedProps = []
      //   if (this.selectCategoryCode) {
      //     this.API.ProductProps({
      //       code: this.selectCategoryCode
      //     }).then((res) => {
      //       this.getProductPorps = res.data
      //     })
      //   }
      // },
      // 上传图片
      getProduct() {
        this.API.ProductDetail({
          productId: this.editProID,
          platformType: 'library'
        }).then((res) => {
          if (res) {
              
              // 硬装部分需要加的
              if(res.data.hardProductModelInfo){
                res.data.hardProductModelInfo.map((item,index) => {
                   this.$set(item,'iSShow',true);
                })
                  let toatalProps=[];
                  this.$store.state.usedModelIds = res.data.hardProductModelInfo.map(it => it.id);
                  //this.$store.dispatch('setHardModel',res.data.hardProductModelInfo);//赋值 setPropsId
                  res.data.hardProductModelInfo.map(item=>{ //回显已选择的规格
                      item.modelId=item.id;
                      let propIdsBox=[];
                      // propIdsBox.push({propIds:})
                      item.props.map(prop=>{
                          propIdsBox.push(prop.id)
                      })
                      toatalProps.push({propIds:propIdsBox,modelId:item.id});
                  });
                  this.$store.dispatch('setPropsId',toatalProps);
                  this.$store.dispatch('setHardModel',res.data.hardProductModelInfo);

              }

              //---------------------------------------
              //this.smallType=res.data.smallType;
              this.selectCategoryCode=res.data.smallType;
              this.selectedBrandId = res.data.brandId;
              this.visible.isModel = res.data.checkIsModel;
              this.modelList = res.data.model;
              this.getProductPorps = res.data.props;
              this.showProductClassList = res.data.categoryNodes;
              this.disposeProductClassList();
              // this.selectCategoryCode = res.data.categoryCodes[res.data.categoryCodes.length - 1];
              this.categoryCodeName = res.data.attrNames;
              let filter = this.unitList.filter(e => {
                if (e.suitableCodes)
                  return e.suitableCodes.indexOf(res.data.smallType) != -1;
              });
              this.selectSaleUnionName = filter.length?filter[filter.length-1].name : this.unitList[0].name;
              this.assModelDia.query.modelType = res.data.smallType;
            res.data.brandId = res.data.brandId ? parseInt(res.data.brandId) : ''
            res.data.seriesId = res.data.seriesId ? parseInt(res.data.seriesId) : ''
            res.data.modelingId = res.data.modelingId ? parseInt(res.data.modelingId) : ''
            res.data.proStyleId = res.data.proStyleId ? parseInt(res.data.proStyleId) : ''
            res.data.saleUnitId = res.data.saleUnitId ? parseInt(res.data.saleUnitId) : ''
            res.data.baseStyleId = res.data.baseStyleId ? res.data.baseStyleId : []
              if(res.data.proCtopsBO){
                this.shuibian.out.id = res.data.proCtopsBO.textureIdForOut||undefined;
                this.shuibian.wall.id = res.data.proCtopsBO.textureIdForWall||undefined;
                this.shuibian.out.src = res.data.proCtopsBO.texturePicPathForOut||undefined;
                this.shuibian.wall.src = res.data.proCtopsBO.texturePicPathForWall||undefined;
              }
            this.product = res.data;
              this.product.isSpecialEffects=res.data.isSpecialEffects ? true : false;
            let arrName = [],arrName2 = [],listArr = [],newList = [];
            res.data.categoryNodes.map((v,i)=>{
              arrName.push(v.secondLevelName);
              arrName2.push(v.parentName);
            })
            arrName = Array.from(new Set(arrName))
            arrName2 = Array.from(new Set(arrName2))
            if(arrName.length>1||arrName[0]=='地面'||arrName[0]=='瓷砖'||arrName[0]=='木地板'){
              this.lastIsMultistageList = true;
            }
            arrName.map((a,ai)=>{
              listArr[ai] = {name: a, list: []}
              arrName2.forEach((b,bi)=>{
                  listArr[ai].list[bi] = { name: '', list: []}
                  res.data.categoryNodes.forEach((c,ci)=>{
                      if(c.parentName==b&&c.secondLevelName==a){
                          //c.checked = true;
                          listArr[ai].list[bi].list.push(c);
                          listArr[ai].list[bi].name = c.parentName
                          newList.push(c);
                      }
                  })
              })
            })
            let adArr = [];
            this.checkProductList = newList;
            this.checkProductList.map(h=>{adArr.push(h.id)})
            this.productClassIdLists = adArr;
            this.newProducLists = listArr;
            
            
            switch(res.data.attrNames){
                case '天花/截面天花':
                    this.productTypes = 1;
                    if(!this.product.ceilingCrossSectionDataDTOList){
                      this.product.ceilingCrossSectionDataDTOList = [
                        {ceilingInfo:undefined,lightInfo:undefined,applyArea:''},
                        {ceilingInfo:undefined,lightInfo:undefined,applyArea:''},
                        {ceilingInfo:undefined,lightInfo:undefined,applyArea:''}
                      ]
                    }else{
                      
                      if(this.product.ceilingCrossSectionDataDTOList.length<3){
                        for(let i = this.product.ceilingCrossSectionDataDTOList.length;i<3;i++){
                          this.product.ceilingCrossSectionDataDTOList[i] = {ceilingInfo:undefined,lightInfo:undefined,applyArea:''};
                        }
                      }
                      console.log(this.product.ceilingCrossSectionDataDTOList);
                      let len = this.product.ceilingCrossSectionDataDTOList
                      let item = len[0].applyArea.split(',').concat(len[1].applyArea.split(','),len[2].applyArea.split(','));
                      len.map((res,index)=>{
                        this.applyAreaList.map((a,ai)=>{
                          index!=ai?a.map(n=>{
                              n.disabled = false
                              item.map(v=>{ if(v==n.name) n.disabled = true; })
                          }):'';
                        })
                      })
                      
                      len.map((res,index)=>{
                        if(res.ceilingInfo){
                          this.disabled[index] = false;
                          res.applyArea = res.applyArea?res.applyArea.split(','):[];
                          res.applyArea.map((bb,bbi)=>{
                            this.applyAreaList[index].map((cc,ci)=>{
                              if(bb==cc.name){ cc.disabled = false;}  
                            })
                          })
                        }
                      })
                      
                    }
                    console.log(this.product.ceilingCrossSectionDataDTOList);
                    this.setApplyAreaFun2();
                    this.resetType1();
                    break;
                case '厨房地柜/橱柜全台面':
                    this.productTypes = 2;
                    if(!this.product.proCtopsBO){
                      this.product.proCtopsBO = {};
                    }
                    this.product.proCtopsBO.dataTypeForWall = res.data.proCtopsBO.dataTypeForWall?res.data.proCtopsBO.dataTypeForWall:undefined;
                    this.product.proCtopsBO.dataTypeForOut = res.data.proCtopsBO.dataTypeForOut?res.data.proCtopsBO.dataTypeForOut:undefined;
                    this.product.proCtopsBO.textureIdForWall = res.data.proCtopsBO.textureIdForWall?res.data.proCtopsBO.textureIdForWall:undefined;
                    this.product.proCtopsBO.textureIdForOut = res.data.proCtopsBO.textureIdForOut?res.data.proCtopsBO.textureIdForOut:undefined;
                    this.product.proCtopsBO.crossSectionDataForWall = res.data.proCtopsBO.crossSectionDataForWall?res.data.proCtopsBO.crossSectionDataForWall:'';
                    this.proCtopsBOObj.crossSectionDataForWall = res.data.proCtopsBO.crossSectionDataForWall?res.data.proCtopsBO.crossSectionDataForWall:'';
                    this.proCtopsBOObj.crossSectionDataForOut = res.data.proCtopsBO.crossSectionDataForOut?res.data.proCtopsBO.crossSectionDataForOut:'';
                    this.product.proCtopsBO.crossSectionDataForOut = res.data.proCtopsBO.crossSectionDataForOut?res.data.proCtopsBO.crossSectionDataForOut:'';
                    this.resetType2();
                    break;
                default :
                    this.productTypes = 0;
                    this.resetType1();
                    this.resetType2();
                    break;
            }
            if (this.product.model) {
              this.modelId = this.product.model.modelId
              this.assModelInfo = this.product.model
            }
            this.assTextureDia.texture = this.product.texture
            if (this.product.defaultPicId) {
              this.defaultPicId = this.product.defaultPicId
            }
            if (res.data.modelTextureInfo) {
              this.area = res.data.modelTextureInfo.map(it => Object.assign({}, it, {
                areaTextures: it.areaTextures && it.areaTextures.map(tit => Object.assign({}, {
                  thumbnailPath: tit.picPath,
                  deleteBtnVisible: false
                }, tit))
              }))
            }
            // this.getProductPorps = res.data.props;
            this.$nextTick(
              () => {
                if (this.product.props) {
                  this.selectedProps = this.product.props.map((item, index) => ({id: item.id}))
                  this.productPropBO = this.product.props
                }
              }
            )
            this.fileList2 = res.data.picInfos.map(res => {
              return {
                url: res.path,
                id: res.id
              }
            })

          //获取硬装规格信息
              this.API.ProductProps({
                  code: this.selectCategoryCode
              }).then(res=>{
                  this.$store.dispatch('saveProductPorps',res.data);
              })

          }
        })
      },
      resetType1(){
        if(this.product.proCtopsBO){
          this.product.proCtopsBO.dataTypeForWall = undefined;
          this.product.proCtopsBO.dataTypeForOut = undefined;
          this.product.proCtopsBO.textureIdForWall = undefined;
          this.product.proCtopsBO.textureIdForOut = undefined;
          this.product.proCtopsBO.crossSectionDataForWall = undefined;
          this.product.proCtopsBO.crossSectionDataForOut = undefined;
        }
      },
      testChange(v){
        this.product.proCtopsBO[v] = this.proCtopsBOObj[v]
        console.log(v,'----',this.product.proCtopsBO[v]);
      },
      resetType2(){
          this.product.ceilingCrossSectionDataDTOList = [
              {ceilingInfo:undefined,lightInfo:undefined,applyArea:undefined},
              {ceilingInfo:undefined,lightInfo:undefined,applyArea:undefined},
              {ceilingInfo:undefined,lightInfo:undefined,applyArea:undefined}
          ]
          this.applyAreaList = [[
              {id:1,name:'0~5'},{id:2,name:'5~10'},{id:3,name:'10~15'},{id:4,name:'15~20'},{id:5,name:'20~25'},{id:6,name:'25~30'},
              {id:7,name:'30~35'},{id:8,name:'35~40'},{id:9,name:'40~50'},{id:10,name:'50~60'},{id:11,name:'60~70'},{id:14,name:'70~~'}
          ],[
              {id:1,name:'0~5'},{id:2,name:'5~10'},{id:3,name:'10~15'},{id:4,name:'15~20'},{id:5,name:'20~25'},{id:6,name:'25~30'},
              {id:7,name:'30~35'},{id:8,name:'35~40'},{id:9,name:'40~50'},{id:10,name:'50~60'},{id:11,name:'60~70'},{id:14,name:'70~~'}
          ],[
              {id:1,name:'0~5'},{id:2,name:'5~10'},{id:3,name:'10~15'},{id:4,name:'15~20'},{id:5,name:'20~25'},{id:6,name:'25~30'},
              {id:7,name:'30~35'},{id:8,name:'35~40'},{id:9,name:'40~50'},{id:10,name:'50~60'},{id:11,name:'60~70'},{id:14,name:'70~~'}
          ]];
      },
      disabledFun(index){
        let flag = !this.product.ceilingCrossSectionDataDTOList[index].ceilingInfo.length>0;
        flag?this.product.ceilingCrossSectionDataDTOList[index].applyArea=[]:'';
        flag?applyAreaObj.applyArea=[]:'';
        this.disabled[index] = flag
        this.$set(this.disabled,index,this.disabled[index]);
        
      },
      // 材质选择ID
      selectedTexture(val) {
        this.selectedTextureId = val
      },
      // 产品款式选择ID
      selectStyleID(val) {
        this.selectStyleId = val
      },
      // 产品系列选择ID
      selectedSeriesID(val) {
        this.selectedSeriesId = val
      },
      // 品牌选择ID
      selectedBrandID(val) {
        this.selectedBrandId = val
      },
      // 产品分类编号
      // selectCategoryNO(val) {
      //   // this.selectCategoryCode = val[val.length - 1]
      //   this.productPropBO = []
      //   this.API.modelTexture({
      //     code: this.selectCategoryCode
      //   }).then((res) => {
      //     this.visible.isModel = res.data == 1
      //     this.visible.isTexture = res.data == 2
      //   })
      //   this.getProductProps()
      // },
      /* 上传图片 */
      handleAvatarSuccess(res, file) {
        // this.imageUrl = URL.createObjectURL(file.raw);
        console.log(file)
        this.fileList2.push({
          url: res.data.url,
          id: res.data.resId
        })
        if (this.fileList2) {
         if (this.fileList2[1]) {
          this.defaultPicId = this.defaultPicId;
        } else if (this.fileList2[0]) {
          this.defaultPicId = this.fileList2[0].id;
        }
        }
      },
      handleChange(file, fileList) {
      },
      deleteUploadImg(index) {
        this.fileList2.splice(index, 1)
        this.defaultPicId = ''
      },
      showTextureDeleteEnter(index) {
        this.$set(this.showTextureDeletes, index, true)
      },
      showTextureDeleteOut(index) {
        this.$set(this.showTextureDeletes, index, false)
      },
      showDelete(index) {
        this.$set(this.showDeletes, index, true)
      },
      showDelete2(index) {
        this.$set(this.showDeletes, index, false)
      },
      beforeAvatarUpload(file) {
        console.log(file)
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

      /* 上传图片 结束 */
      // 产品系列下拉框
      getProductSeriesList() {
        this.API.ProductSeriesDrop().then((res) => {
           this.productSeries = res.data
        })
      },
      setDefaultPic(file,index) {
        this.defaultPicId = file.id;
        this.fileList2.splice(index,1);
        this.fileList2.unshift(file);
      },
      textureAttrDropList() {
        this.API.textureAttr().then((res) => {
          this.textureAttrDrop = res.data
        })
      },
      textureTexturesDrop() {
        this.API.textureCaiLiao().then((res) => {
          this.textureTextures = res.data
        })
      },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let modelTextureInfos = []
                      this.area.forEach(it => {
                          it.areaTextures&&it.areaTextures.forEach((tit, index) => {
                          modelTextureInfos.push(
                            {
                              affectPrice: tit.affectPrice,
                              areaId: it.id,
                              isDefault: it.isDefault === index ? 1 : 0,
                              textureId: tit.id
                            }
                          )
                        })
                      })
                      let that = this
                      let adArr = [];
                      this.checkProductList.map(h=>{adArr.push(h.id)})
                      this.productClassIdLists = Array.from(new Set(adArr));
                      let formDatas = {
                        categoryIds: that.productClassIdLists, // 运营分类ID
                        advicePrice: that.product.advicePrice, //  (number, optional): 建议售价 ,
                        modelingId: that.product.modelingId, //  (integer, optional): 产品款式 ,
                        brandId: that.product.brandId, //  (integer): 品牌ID ,
                        proCtopsBO:{},
                        categoryCode: that.assModelDia.query.modelType, // this.product.categoryCodes[2], //  (string): 所属分类编码 ,
                        code: that.product.code, //  (string): 产品编码 ,
                        desc: that.product.desc, //  (string, optional): 产品描述 ,
                        editType: 'libraryEdit', //  (string, optional): 编辑类型, ,
                        length: that.product.length, // 长
                        width: that.product.width, // 宽
                        height: that.product.height, // 高
                        id: that.editProID, //  (integer): 产品id ,
                        userId: sessionStorage.getItem('userId'),
                        modelNumber: that.product.modelNumber,
                        name: that.product.name, //  (string): 产品名称 ,
                        price: that.product.price, //  (number, optional): 售价 ,
                        decorationPrice: that.product.decorationPrice ? that.product.decorationPrice : "", //  装修单价 ,
                        saleUnionId: that.product.saleUnitId, //  (integer, optional): 售价单位 ,
                        seriesId: that.product.seriesId, //  (integer, optional): 产品系列 ,
                        baseStyleId: that.product.baseStyleId, //  (integer, optional): 产品风格,
                        modelTextureInfos: modelTextureInfos,
                        modelId: that.modelId,
                        picIds: that.fileList2.map(item => item.id).join(','),
                        defaultPicId: that.defaultPicId,
                        textureId: that.product.texture ? (that.product.texture.id ? that.product.texture.id : -1) : -1,
                        props: that.productPropBO, // 产品属性
                        productSpec:that.product.spec,
                        bedsideDimensions:that.product.bedsideDimensions,
                        isSpecialEffects:that.product.isSpecialEffects ? 1 : 0

                      }
                      this.productTypes==2?formDatas.proCtopsBO = {
                          dataTypeForWall:that.product.proCtopsBO.crossSectionDataForWall?1:0,
                          dataTypeForOut:that.product.proCtopsBO.crossSectionDataForOut?2:0,
                          textureIdForWall:that.shuibian.wall.id,
                          textureIdForOut:that.shuibian.out.id,
                          crossSectionDataForWall:that.product.proCtopsBO.crossSectionDataForWall,
                          crossSectionDataForOut:that.product.proCtopsBO.crossSectionDataForOut,
                          idForOut:that.product.proCtopsBO.idForOut||undefined,
                          idForWall:that.product.proCtopsBO.idForWall||undefined,
                      }:'';
                      let newObj = [];
                      if(that.product.ceilingCrossSectionDataDTOList){
                          that.product.ceilingCrossSectionDataDTOList.map((v,i)=>{
                              if(v.applyArea){
                                  let arr = [];
                                  v.applyArea.map(b=>{
                                      that.applyAreaList[0].map(a=>{if(a.name==b){arr.push(a.name)}})
                                  })
                                  newObj[i] = { ceilingInfo:v.ceilingInfo,lightInfo:v.lightInfo, applyArea:arr.join(',')}
                              }
                          })
                      }
                      formDatas.ceilingCrossSectionDataDTOList = newObj;
                      for (let key in formDatas) {
                        if(key!="decorationPrice")
                        if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
                          delete formDatas[key]
                        }
                      }

                      if(formDatas.defaultPicId =='' ||formDatas.defaultPicId ==undefined || formDatas.defaultPicId == null){
                            this.$message.error('请上传产品图片');
                            return;
                      }
                      
                      if(!formDatas.categoryIds){
                          this.$message.error('请填写运营分类信息');
                          return;
                      }
                    if(this.isHard&&this.gettersHardModelList.length==0){
                        this.$message.error('请选择主模型');
                        return;
                    }
                      this.$confirm('确定更新产品', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        cancelButtonClass: 'cancelButtonClass',
                        confirmButtonClass: 'confirmButtonClass',
                        type: 'info',
                        center: true
                      }).then(() => {
                         this.loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                    });
                        if(sessionStorage.getItem('addProductType')=='soft'){ //之前的逻辑 软装
                            this.API.UpdateProductLibrary(formDatas).then((res) => {
                              this.loading.close();
                                if (res.success) {
                                    this.$message.success('更新产品成功')
                                    this.$router.push('/product')
                                }
                            })
                        }else { //现在的硬装 逻辑
                            this.getHardParam();
                            formDatas.productSKUInfos=this.gettersSKUInfos; // SKU信息
                            this.API.newUpdateProductLibrary(formDatas).then((res) => {
                               this.loading.close();
                                if (res.success) {
                                    this.$message.success('更新产品成功')
                                    this.$router.push('/product')
                                }
                            })
                        }

                      }).catch(() => {
                        this.$router.push('/product')
                      })
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            // this.$refs[formName].resetFields();
              this.$router.push('/product')
              this.$refs[formName].resetFields()
        },
      // submitForm() {
      //   let modelTextureInfos = []
      //   this.area.forEach(it => {
      //     it.areaTextures.forEach((tit, index) => {
      //       modelTextureInfos.push(
      //         {
      //           affectPrice: tit.affectPrice,
      //           areaId: it.id,
      //           isDefault: it.isDefault === index ? 1 : 0,
      //           textureId: tit.id
      //         }
      //       )
      //     })
      //   })
      //   let that = this
      //   let formDatas = {
      //     categoryIds: this.productClassIdLists,
      //     advicePrice: that.product.advicePrice, //  (number, optional): 建议售价 ,
      //     modelingId: that.product.modelingId, //  (integer, optional): 产品款式 ,
      //     brandId: that.product.brandId, //  (integer): 品牌ID ,
      //     // categoryCode: that.selectCategoryCode, // this.product.categoryCodes[2], //  (string): 所属分类编码 ,
      //     code: that.product.code, //  (string): 产品编码 ,
      //     desc: that.product.desc, //  (string, optional): 产品描述 ,
      //     editType: 'libraryEdit', //  (string, optional): 编辑类型, ,
      //     length: that.product.length, // 长
      //     width: that.product.width, // 宽
      //     height: that.product.height, // 高
      //     id: that.editProID, //  (integer): 产品id ,
      //     userId: sessionStorage.getItem('userId'),
      //     modelNumber: that.product.modelNumber,
      //     name: that.product.name, //  (string): 产品名称 ,
      //     price: that.product.price, //  (number, optional): 售价 ,
      //     saleUnionId: that.product.saleUnitId, //  (integer, optional): 售价单位 ,
      //     seriesId: that.product.seriesId, //  (integer, optional): 产品系列 ,
      //     baseStyleId: that.product.baseStyleId, //  (integer, optional): 产品风格,
      //     modelTextureInfos: modelTextureInfos,
      //     modelId: that.modelId,
      //     picIds: that.fileList2.map(item => item.id).join(','),
      //     defaultPicId: that.defaultPicId,
      //     textureId: that.assTextureDia.texture ? (that.assTextureDia.texture.id ? that.assTextureDia.texture.id : -1) : -1,
      //     props: that.productPropBO // 产品属性
      //   }
      //   for (let key in formDatas) {
      //     if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
      //       delete formDatas[key]
      //     }
      //   }
      //   this.$confirm('确定更新产品', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     cancelButtonClass: 'cancelButtonClass',
      //     confirmButtonClass: 'confirmButtonClass',
      //     type: 'info',
      //     center: true
      //   }).then(() => {
      //     this.API.UpdateProductLibrary(formDatas).then((res) => {
      //       if (res) {
      //         this.$message.success('更新产品成功')
      //         this.$router.push('/product')
      //       }
      //     })
      //   }).catch(() => {
      //     this.$router.push('/product')
      //   })
      // },
      // resetForm(formName) {
      //   this.$router.push('/product')
      //   this.$refs[formName].resetFields()
      // },
      // 产品分类下拉框
      // getProductCategory() {
      //   // this.API.ProductCategory().then((res) => {
      //   //   this.productCategoryDrop = res.data
      //   // })
      //     this.API.ProductCategory().then((res) => {
      //         this.productCategoryDrop = res.data
      //     })
      // },
      // 获取产品款式下拉框
      getProductStyleDrop() {
        this.API.ProductStyleDrop().then((res) => {
          this.productStyle = res.data
        })
      },
      // 获取产品品牌下拉框
      getProductBrand() {
        this.API.ProductBrandDrop({
          companyId: sessionStorage.getItem('companyID')
        }).then((res) => {
          this.productBrand = res.data
        })
      },
      // 产品风格下拉框
      getBaseProductStyleDrop() {
        this.API.BaseProductStyleDrop().then((res) => {
          this.baseProductStyle = res.data
        })
      },
      // 获取数字字典，常用单位
      getDisctionaryUnit() {
        this.API.DisctionaryUnit().then((res) => {
          this.unitList = res.data
        })
      },
      removeTag(v){
          this.applyAreaList.map(a=>{
              a.map(h=>{ 
                  if(h.name==v) {
                      h.disabled = false; 
                  }
              })
          })
      },
      setApplyAreaFun(){
        this.applyAreaObj.map((v,i)=>{ 
          this.product.ceilingCrossSectionDataDTOList[i].applyArea = v.applyArea
        })
      },
      setApplyAreaFun2(){
        this.product.ceilingCrossSectionDataDTOList.map((v,i)=>{
          this.applyAreaObj[i].applyArea = v.applyArea
        })
      },
      applyChange(i,v){
          this.setApplyAreaFun();
          let list = this.product.ceilingCrossSectionDataDTOList;
          
          let item = list[0].applyArea.concat(list[1].applyArea,list[2].applyArea);
           console.log(item);
          this.applyAreaList.map((a,ai)=>{
              if(i!=ai){
                  a.map(n=>{
                      n.disabled = false
                      item.map(v=>{ 
                          if(v==n.name)  n.disabled = true;
                      })
                  })
              }
          })
      },

      //添加水边材质图
      addImg(type){
          this.popupType2 = type
          this.handleReAssTexture();
      },
      //删除水边材质图
      delImg(type){
          this.shuibian[type] = {};
      },
    },
    
    created() {
      this.editProID = this.$route.params.proID
      // this.getProductCategory()
        
      this.getProductBrand()
      this.getProductSeriesList()
      this.getProductStyleDrop()
      this.getBaseProductStyleDrop()
      this.getDisctionaryUnit()
      this.getProduct()
      this.getProductTextureDrop()
      this.textureAttrDropList()
      this.textureTexturesDrop();
      this.showProductCategory()
    },
    // watch: {
    //   'product.categoryCodes': {
    //     handler: function (val, oldVal) {
    //       this.selectCategoryNO(val)
    //     }
    //   }
    // }
  }
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/mixin";

  .selectRadio {

  }

  .textureSearch {
    .el-col {
      margin-bottom: 15px;
    }
    .el-input, .el-select {
      width: 100%;
    }
  }
  .verify{
    color: red;
  }
  .mainPic {
    width: 56px;
    position: absolute;
    top: 4px;
    left: 0;
    padding-left: 6px;
    text-align: left;
    height: 22px !important;
    line-height: 22px;
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
    z-index: 111;
    color: #fff;
    font-size: 12px;
    background: #ff3f3f;

  }
  .checkboxs{
    width: 100%;
    padding: 0 10px;
    height: 32px;
    line-height: 32px;
    position: relative;
    padding-left: 35px;
    box-sizing: border-box;
    cursor: pointer;
    div{
      i{
        font-size: 15px;
        position: absolute;
        left: 10px;
        height: 32px;
        top: 0;
        color: #ccc;
        line-height: 34px;
      }
    }
    div.actives,div.actives i{
      color: #ff6419;
    }
    
  }

  .info-ypop-up{
    .el-col-2{
      height: 36px;
      line-height: 36px;
      span{
        display: inline-block;
        width: 100%;
        text-align: center;
        cursor:pointer;
        color: #ff6419;
      }
    }
    .Classify-list{
      border-top: 1px solid #dddddd;
      border-right: 1px solid #dddddd;
      .el-col-6{
        overflow: auto;
        height: 360px;
        border-bottom: 1px solid #dddddd;
        border-left: 1px solid #dddddd;
        .el-checkbox{
          display: block;
          margin: 0;
          padding: 10px 20px;
          height: 12px;
          line-height: 12px;
          color: #000000;
        }
        p{
          padding: 10px 20px;
          height: 12px;
          line-height: 12px;
          display: flex;
          justify-content: space-between;
          cursor: pointer;
          color: #000000;
          &:hover{
            color: #ff6419;
          }
        }
        .p-active{
          color: #ff6419;
        }
      }
      .el-col-3, .el-col-4, .el-col-5{
        overflow: auto;
        height: 360px;
        border-bottom: 1px solid #dddddd;
        border-left: 1px solid #dddddd;
        p{
          padding: 10px 20px;
          height: 12px;
          line-height: 12px;
          display: flex;
          justify-content: space-between;
          cursor: pointer;
          color: #000000;
          &:hover{
            color: #ff6419;
          }
        }
        .p-active{
          color: #ff6419;
        }
      }
    }
    .el-col-24{
      height: 30px;
      margin: 0 auto 10px;
      line-height: 30px;
    }
  }
  .uploadTexture2 {
    margin-top: 20px;
    background: #f2f2f2;
    .itemsPos {
      height: 90px;
      width: 90px;
      display: inline-block;
      margin: 10px;
      position: relative;
      .selected {
        position: absolute;
        z-index: 12;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
      }
      .textureImg {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        z-index: 8;
      }
      .chks {
        opacity: 0;
        border: solid red 1px;
        height: 90px;
        width: 90px;
        z-index: 10;
      }
    }
  }

  .uploadPic {
    background: #eee;
    padding: 15px 0 0 15px;
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
      &:hover{
        .upload-notmainpic__con,.posDelete,.posDelete2{
          display: inline-block!important;
        }
      }
    }
  }

  .uploadImgList {
    width: 90px;
    text-align: center;
    position: relative;
    img{
      object-fit: contain;
    }
  }

  .sizes {
    width: 240px;
    border: solid 1px #ddd;
    height: 40px;
    border-radius: 6px;
    padding-left: 10px;

  }
    .content {
      display: inline-block;

      .el-col-22 {
        line-height: 40px;
        p {
          display: inline-block;
          padding: 0 12px 0 12px;
          height: 12px;
          line-height: 12px;
          color: #333333;
          border-right: 1px solid #dddddd;
        }
      }
      .el-col-24 {
        padding: 5px 0 5px 20px;
        height: 40px;
        line-height: 30px;
        h2 {
          display: inline-block;
          font-size: 14px;
          width: 110px;
          text-align: right;
        }
        p {
          display: inline-block;
          padding: 0 12px 0 12px;
          height: 12px;
          line-height: 12px;
          color: #333333;
          border-right: 1px solid #dddddd;
        }
        span {
          color: #ff6419;
          margin-left: 10px;
          cursor: pointer
        }
      }
    }
    .goods-operating-info {
      margin-bottom: 20px;
      .title{
        position: relative;
        .star{
          color: #ff6419;
          position: absolute;
          left: -6px;
        }
      }
      .el-col-2 {
        font-size: 14px;
        width: 110px;
        text-align: right;
        line-height: 40px;
        margin-left: 20px;
      }
      .el-col-22 {
        line-height: 40px;
        p {
          display: inline-block;
          padding: 0 12px 0 12px;
          height: 12px;
          line-height: 12px;
          color: #333333;
          border-right: 1px solid #dddddd;
        }
      }
      .el-col-24 {
        padding: 5px 0 5px 20px;
        height: 40px;
        line-height: 30px;
        h2 {
          display: inline-block;
          font-size: 14px;
          width: 110px;
          text-align: right;
        }
        p {
          display: inline-block;
          padding: 0 12px 0 12px;
          height: 12px;
          line-height: 12px;
          color: #333333;
          border-right: 1px solid #dddddd;
        }
        span {
          color: #ff6419;
          margin-left: 10px;
          cursor: pointer
        }
      }
    }
    .goods-operating-infos{
      display: flex;
      justify-content: left;
      .headline{
        font-size: 14px;
        min-width: 110px;
        text-align: right;
        line-height: 40px;
        margin-left: 20px;
      }
      .content{
        display: inline-block;
        line-height: 40px;
        p{
          display: inline-block;
          padding: 0 12px 0 12px;
          height: 12px;
          line-height: 12px;
          color: #333333;
          border-right: 1px solid #dddddd;
        }
      }
      .el-col-22{
        padding: 5px 0 5px 20px;
        height: 40px;
        line-height: 30px;
        h2{
          display: inline-block;
          font-size: 14px;
          width: 110px;
          text-align: right;
        }
        p{
          display: inline-block;
          padding: 0 12px 0 12px;
          height: 12px;
          line-height: 12px;
          color: #333333;
          border-right: 1px solid #dddddd;
        }
        span{
          color: #ff6419;
          margin-left: 10px;
          cursor:pointer
        }
      }
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

    .modelTextureFrame {
      border: solid 1px #e3e3e3;
      border-radius: 4px;
      margin-left: 100px;
      margin-bottom: 30px;
      background: #fff;
      .modelTextList {
        background: #f2f2f2;
      }
      .addDelModel {
        text-align: right;
        line-height: 150px;
        .el-button:last-of-type {
          margin-right: 30px;
        }
      }
      .currentModel {
        box-sizing: padding-box;
      }
      .selectedModel {
        display: inline-block;
        box-sizing: padding-box;
        vertical-align: 40px;
        position: relative;
        width: 70%;
        div {
          position: absolute;
          margin-left: 20px;
          width: 100%;
          p {
            line-height: 30px;
            color: #666;
          }
        }

      }
      .modelPic {
        text-align: left;
        margin-top: 20px;
        margin-left: 15px;
        vertical-align: middle;
        display: inline-block;
        position: relative;
        img {
          border-radius: 6px;
          height: 90px;
          width: 90px;
          padding: 12px;
          border: dashed 1px #e3e3e3;
        }
      }
      .textureTitle {
        border-radius: 4px;
        margin: 15px;
        background: #fff;
        .title, .delete {
          height: 36px;
          border-bottom: solid 1px #e3e3e3;
        }
        .title {
          line-height: 36px;
          padding-left: 15px;
        }
        .delete {
          text-align: center;
          border-left: solid 1px #e3e3e3;
          img {
            margin-top: 7px;
          }
        }
      }
      .radioStyle {
        -webkit-appearance: none;
        height: 16px;
        width: 16px;
        border-radius: 8px;

      }
      .uploadTexture {
        background: #fff;
        height: 156px;
        padding: 20px;
        .el-col {
          width: 90px;
          .el-input {
            height: 28px;
            text-align: center !important;
          }
        }
        .priceOffset {
          display: block;
          color: #666;
          padding-top: 12px;
          font-size: 12px;
          text-align: center;
        }
        .uploadButton {
          margin-left: 10px;
          border: dashed 1px #e3e3e3;
          border-radius: 4px;
          text-align: center;
          width: 90px;
          height: 90px;
          display: table-cell;
          vertical-align: middle;
          margin-bottom: 50px;
          &:first-of-type {
            background: #fff;
          }
          background: #f6f6f6;
          div {
            width: 90px;
            height: 90px;
            margin-bottom: 8px;
            position: relative;
            .el-radio {
              position: absolute;
              right: -7px;
              top: 6px;
            }
            img {
              position: absolute;
              left: 50%;
              right: 50%;
              transform: translate(-50%, 50%);
            }
          }
          p {
            line-height: normal;
            height: 30px;
            .addPlug {
              width: 20px;
              height: 20px;
            }
          }
          img {
            width: 48px;
            height: 48px;
          }
        }
      }
    }

    .propTitle-content {
      display: flex;
      justify-content: left;
      h2 {
        min-width: 100px;
        text-align: right;
        font-size: 14px;
        line-height: 40px;
      }
      span {
        width: 100px;
        font-size: 14px;
        line-height: 40px;
        margin: 0 10px;
      }
    }

    .propTitle {
      display: block;
      font-size: 14px;
      padding: 0 15px 15px 30px;
      border-bottom: solid 1px #dedede;
      margin-bottom: 15px;
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
      .addProduct_p {
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
    .imgRows{
        width: 90px;
        height: 90px;
        background-color: #fbfdff;
        border-radius: 4px;
        border: 1px dashed #c0ccda;
        position: relative;
        .zzcli{
            cursor: pointer;
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            z-index: 4;
            opacity: 0;
        }
        .icons{
            position: absolute;
            top: 20px;
            left: 50%;
            font-size: 20px;
            width: 20px;
            margin-left: -10px;
            color: #ff6419;
            line-height: 20px;
        }
        &:hover{
            border-color: #FF6419;
        }
        .delIcon{
            width: 100%;
            height: 100%;
            position: absolute;
            z-index: 7;
            top: 0;
            left: 0;
            transition-duration: .3s;
            background: rgba(255,255,255,.3);
            font-size: 20px;
            line-height: 92px;
            text-align: center;
            color: #333333;
            opacity: 0;
            i{
              cursor: pointer;
            }
        }
        &:hover .delIcon{
          opacity: 1;
        }
        span{
            position: absolute;
            display: block;
            width: 100%;
            text-align: center;
            left: 0;
            bottom: 19px;
            font-size: 14px;
            line-height: 1;
            color: #999;
        }
        img{
            position: absolute;
            z-index: 5;
            left: -1px;
            top: -1px;
            border-radius: 4px;
            width: 92px;
            height: 92px;
        }
    }
    /*上传图片样式 */
    .avatar-uploader {
      background: #eee;
      box-sizing: border-box;
      // padding: 15px 0 0 15px;
      border-radius: 4px;
    }

    .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
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

    .avatar {
      width: 90px;
      height: 90px;
      display: block;
    }

    .demo-ruleForm {
      .el-select {
        width: 100%;
      }
      .el-cascader {
        width: 100%;
      }
    }
    .productPic{
      width: 150px;
      text-align: right;
      position: absolute;
      left: 0;
      span{
        color: #ff6419;
        margin-right: 5px;
      }
      .tooltip-btn{
        display: inline-block;
        border: 1px solid #ff6419;
        border-radius: 100%;
        width: 18px;
        height: 18px;
        line-height: 18px;
        text-align: center;
        cursor: pointer;
        padding: 0;
      }
    }
    .pagePagination{
      margin-top: 20px;
    }
  /*上传图片样式 end*/
</style>
<style lang="scss">
  .editProduct{
    .el-upload-list__item img {
      width: 90px;
      height: 90px;
    }

    .el-upload-list__item-thumbnail {
      width: 90px !important;
      height: 90px !important;
    }

    .el-upload-list--picture-card .el-upload-list__item {
      overflow: hidden;
      background-color: #fff;
      border: 1px solid #c0ccda;
      border-radius: 6px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      width: 90px;
      height: 90px;
      margin: 0 8px 8px 0;
      display: inline-block;
    }

    .el-upload--picture-card {
      background-color: #fbfdff;
      border: 1px dashed #c0ccda;
      border-radius: 6px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      width: 90px;
      height: 90px;
      line-height: 90px;
      vertical-align: top;
    }

    .el-progress--circle .el-progress__text {
      position: absolute;
      top: 50%;
      left: 0;
      width: 100%;
      text-align: center;
      margin: 0;
      -webkit-transform: translate(0, -50%);
      transform: translate(0, -50%)
    }

    .el-progress--circle .el-progress__text i {
      vertical-align: middle;
      display: inline-block
    }

    .is-uploading {
      width: 90px;
      height: 90px;
    }
    .pageFrame {
      background: #fff;
      height: 110px;
      padding-top: 40px;
      box-sizing: border-box;
      text-align: center;
    }
    .el-upload-list--picture-card .el-progress {
      top: 50%;
      left: 50%;
      -webkit-transform: translate(-50%, -50%) scale(0.6);
      transform: translate(-50%, -50%) scale(0.6);
      bottom: auto;
      width: 126px;
      height: 126px;
    }
    .productName{
      margin-left: 40px;
    }
  }
</style>

