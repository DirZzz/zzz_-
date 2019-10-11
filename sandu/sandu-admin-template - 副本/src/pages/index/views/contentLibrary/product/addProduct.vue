<template>
    <div class="addProduct">
        <!--弹出框-->
        <el-dialog title="添加运营分类信息"  :visible.sync="infoClassifyVisible" class="info-ypop-up" width="800px">
            <el-row>
                <!--<el-col :span="20">-->
                <!--<el-input v-model="seekClassify" size="medium" placeholder="请输入内容"></el-input>-->
                <!--</el-col>-->
                <!--<el-col :span="2">-->
                <!--<span>搜索</span>-->
                <!--</el-col>-->
                <!--<el-col :span="2">-->
                <!--<span>重置</span>-->
                <!--</el-col>-->
                <el-col :span="24">
                    选择分类：
                </el-col>
            </el-row>
            <el-row class="Classify-list">
                <el-col :span="3">
                    <p
                            v-for="(item, index) in productCategorylist1" :key="index"
                            @click="selectType(index, 1)"
                            :class="indexList.index1 == index ? 'p-active' : ''">
                        {{item.name}}<span>></span>
                    </p>
                </el-col>
                <el-col :span="5">
                    <p
                            v-for="(item, index) in productCategorylist2"
                            :key="index"
                            @click="selectType(index, 2)"
                            :class="indexList.index2 == index ? 'p-active' : ''">
                        {{item.name}}<span>></span>
                    </p>
                </el-col>
                <el-col :span="5">
                    <p
                            v-for="(item, index) in productCategorylist3"
                            :key="index"
                            @click="selectType(index, 3)"
                            :class="indexList.index3 == index ? 'p-active' : ''">
                        {{item.name}}<span>></span>
                    </p>
                </el-col>
                <el-col :span="5">
                    <p
                            v-for="(item, index) in productCategorylist4"
                            :key="index"
                            @click="selectType(index, 4)"
                            :class="indexList.index4 == index ? 'p-active' : ''">
                        {{item.name}}<span>></span>
                    </p>
                </el-col>
                <el-col :span="6">
                    <el-checkbox-group v-model="checkProductList">
                        <el-checkbox  v-for="(item, index) in productCategorylist5"
                                      :label="item"
                                      :key="index"
                                      @change="selectType(index, 5)">

                            {{item.name}}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button round type="primary" @click="verifyProductClass">确 定</el-button>
                <el-button round @click="infoClassifyVisible = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="增加贴图" :visible.sync="imgVisible" class="img-ypop-up" width="800px" @close="onImgVisible">
            <el-form ref="addImgForm" :model="addImgForm" label-width="120px">
                <el-form-item label="材质/材料">
                    <el-select v-model="addImgForm.materials" clearable placeholder="请选择材质/材料">
                        <el-option
                                v-for="(item, index) in materials"
                                :key="index"
                                :label="item.label"
                                :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="贴图表面属性">
                    <el-select v-model="addImgForm.surface" clearable placeholder="请选择贴图表面属性">
                        <el-option
                                v-for="(item, index) in surface"
                                :key="index"
                                :label="item.name"
                                :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="贴图类型">
                    <el-select v-model="addImgForm.textureType" clearable placeholder="请选择贴图类型">
                        <el-option label="材质球" value="ball"></el-option>
                        <el-option label="贴图" value="texture"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="材质型号">
                    <el-input v-model="addImgForm.modelNumber"></el-input>
                </el-form-item>
                <el-form-item label="材质名称">
                    <el-input v-model="addImgForm.textureName"></el-input>
                </el-form-item>
                <el-form-item label="材质编号">
                    <el-input v-model="addImgForm.textureCode"></el-input>
                </el-form-item>
                <el-form-item style="display: block">
                    <el-button type="primary" round @click="seekIng(1)">搜索</el-button>
                </el-form-item>
            </el-form>
            <el-table  :data="imgInfoList" v-loading="addTextureLoading"
                       border
                       style="width: 100%">
                <el-table-column
                        prop="thumbnailPath"
                        label="贴图">
                    <template slot-scope="scope">
                        <div v-if="scope.row.thumbnailPath">
                            <img :src="BASE_URL.sourceBaseUrl+scope.row.thumbnailPath" width="90">
                        </div>
                        <div class="nonImg" v-else> 暂无图片 </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="textureCode"
                        label="贴图编号">
                </el-table-column>
                <el-table-column
                        prop="textureName"
                        label="贴图名称">
                </el-table-column>
                <el-table-column
                        prop="modelNumber"
                        label="材质型号">
                    <template slot-scope="scope">
                        <span v-if="scope.row.modelNumber">{{scope.row.modelNumber}}</span>
                        <span v-else>————</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="textureballFileId"
                        label="贴图类型">
                    <template slot-scope="scope">
                        <span v-if="scope.row.textureballFileId > 0">材质球</span>
                        <span v-else>贴图</span>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <!--<button class="set-img">选用</button>-->
                        <el-button size="mini" :class="'set-img'" @click="selectImg(scope)">选用</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                    @current-change="handleCurrentChange"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </el-dialog>

        <el-dialog title="贴图详情" :visible.sync="imgDatailsVisible" class="img-datails-ypop-up" width="540px">
            <div class="img-datails-content">
                <img src="" alt="">
                <div>
                    <p>贴图名称：<span>2132132132</span></p>
                    <p>贴图编号：<span>2132131321</span></p>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button round @click="imgDatailsVisible = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="添加模型" :visible.sync="addModelVisible" class="add-model-ypop-up" width="668px">
            <div class="add-model-box" v-show="!isUpImg">
                <!--<div class="progress-bar ">-->
                <!--<span class="model-status"></span>-->
                <!--<span :class="isAddModel ? 'link-line' : 'link-line-active'"></span>-->
                <!--<span :class="isAddModel ? 'img-status' : 'img-status-active'"></span>-->
                <!--<p>-->
                <!--<span class="model-txt">选用模型</span>-->
                <!--<span class="img-txt">选用贴图</span>-->
                <!--</p>-->
                <!--</div>-->
                <el-row class="model-list" v-show="isAddModel">
                    <el-col :span="12">
                        模型编号 <el-input v-model="popupModelCode" size="medium"></el-input>
                    </el-col>
                    <el-col :span="12">
                        模型型号 <el-input v-model="popupModelNum" size="medium"></el-input>
                    </el-col>
                    <el-col :span="12">
                        模型名称 <el-input v-model="popupModelName" size="medium"></el-input>
                    </el-col>
                    <el-col :span="12">
                        <el-button round @click="seekModel">搜索</el-button>
                    </el-col>
                    <el-table
                            ref="multipleTable"
                            @selection-change="handleSelectionChange2"
                            element-loading-text="拼命加载中"
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(255, 255, 255, 0.8)"
                            :data="modelTable"
                            border
                            style="width: 100%">
                        <el-table-column
                                prop="thumbPicPath"
                                label="模型缩略图" width="110">
                            <template slot-scope="scope">
                                <div v-if="scope.row.thumbPicPath">
                                    <img :src="BASE_URL.sourceBaseUrl+scope.row.thumbPicPath" width="90">
                                </div>
                                <div class="nonImg" v-else> 暂无图片 </div>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="modelCode"
                                label="模型编号">
                        </el-table-column>
                        <el-table-column
                                prop="modelName"
                                label="模型名称">
                        </el-table-column>
                        <el-table-column
                                prop="categoryNames"
                                label="模型分类">
                        </el-table-column>
                        <el-table-column
                                label="模型规格">
                            <template slot-scope="scope">
                                长：{{scope.row.length}}宽：{{scope.row.width}}高：{{scope.row.height}}
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="modelModelNum"
                                label="模型型号">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                        size="small"
                                        class="model-choose1"
                                        v-if="scope.row.isShow || scope.row.isShow == 1" disabled>已选</el-button>
                                <el-button
                                        size="small"
                                        class="model-choose"
                                        @click="affirmModel(scope)"
                                        v-else>选用</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-row>
                <el-row class="img-list" v-show="false">
                    <el-col :span="24" class="first">
                        <img :src="BASE_URL.sourceBaseUrl + modelImgInfo.thumbPicPath" alt="">
                        <div>
                            <p>贴图名称：<span>{{modelImgInfo.modelName}}</span></p>
                            <p>贴图编号：<span>{{modelImgInfo.modelCode}}</span></p>
                        </div>
                    </el-col>
                    <el-col :span="24" v-for="(item, index) in modelImgList" :key="index">
                        <h1>{{item.name}}</h1>
                        <div class="up-img">
                            <div class="first-up-img">
                                <div @click="uploadIndex(index)">
                                    <span class="add-img"></span>
                                    <p>添加贴图</p>
                                </div>
                                <p class="img-price">售价影响/元</p>
                            </div>
                            <div class="item-img"
                                 @mouseout="imgIndexShow = -1;indexShow = -1"
                                 @mouseover="imgIndexShow = imgIndex;indexShow = index"
                                 v-for="(itemImg, imgIndex) in item.areaTextures" :key="imgIndex">
                                <img :src="BASE_URL.sourceBaseUrl + itemImg.picPath" alt="">
                                <p class="img-price" v-if="item.defalutTextureId == itemImg.id">默认缩略图</p>
                                <p class="img-price1"
                                   v-if="item.defalutTextureId != itemImg.id && imgIndexShow == imgIndex && indexShow == index"
                                   @click="selectImage(index, imgIndex)">设为默认缩略图</p>
                                <span class="img-delete"
                                      @click="deleteImg(index,imgIndex)"
                                      v-if="imgIndexShow == imgIndex && indexShow == index"></span>
                                <el-input
                                        name="item-img-price"
                                        v-model="itemImg.affectPrice"
                                        placeholder="请输入内容"
                                        size="mini"></el-input>
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <el-row class="row-footer" v-show="false">
                    <el-col :span="24">
                        <el-button size="medium" round class="add-model-confirm" @click="affirmModel">确认</el-button>
                        <el-button size="medium" round class="add-model-cancel" @click="showAddModel('取消')">取消</el-button>
                    </el-col>
                </el-row>
            </div>
            <div class="add-img-box" v-show="isUpImg">
                <el-form ref="UpImgForm" :model="UpImgForm" label-width="100px" :inline="true">
                    <el-form-item label="材质/材料">
                        <el-select v-model="UpImgForm.surface" placeholder="请选择材质/材料">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="贴图表面属性">
                        <el-select v-model="UpImgForm.texture" placeholder="请选择贴图表面属性">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="贴图类型">
                        <el-select v-model="UpImgForm.type" placeholder="请选择贴图表面属性">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="材质型号">
                        <el-input v-model="UpImgForm.modelNumber" placeholder="材质型号"></el-input>
                    </el-form-item>
                    <el-form-item label="贴图名称">
                        <el-input v-model="UpImgForm.imgName" placeholder="贴图名称"></el-input>
                    </el-form-item>
                    <el-form-item label="贴图编号">
                        <el-input v-model="UpImgForm.imgCode" placeholder="贴图名称"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <span round class="reset">重置</span>
                        <el-button type="primary" round class="seek">搜索</el-button>
                    </el-form-item>
                </el-form>
                <el-table
                        ref="upImgTable"
                        tooltip-effect="dark"
                        @selection-change="upImg"
                        :data="upImgTable"
                        border
                        style="width: 100%">
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            prop="img"
                            label="贴图" width="110">
                        <template slot-scope="scope">
                            <div v-if="scope.row.img">
                                <img :src="BASE_URL.sourceBaseUrl+scope.row.img" width="90">
                            </div>
                            <div class="nonImg" v-else> 无缩贴图 </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="code"
                            label="贴图编号">
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="贴图名称">
                    </el-table-column>
                    <el-table-column
                            prop="texture"
                            label="贴图型号">
                        <template slot-scope="scope">
                            <span v-if="scope.row.texture">{{scope.row.texture}}</span>
                            <span v-else>————</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="type"
                            label="贴图类型">
                    </el-table-column>
                </el-table>
                <el-row class="row-footer">
                    <el-col :span="24">
                        <el-button size="medium" round class="add-model-confirm">确认</el-button>
                        <el-button size="medium" round class="add-model-cancel" @click="showUpImg">取消</el-button>
                    </el-col>
                </el-row>
            </div>
        </el-dialog>

        <el-dialog title="模型详情" :visible.sync="modelDatailsVisible" class="model-datails-ypop-up" width="668px">
            <div class="model-datails-content">
                <img src="" alt="">
                <div>
                    <p>名称：<span>2132132132</span></p>
                    <p>贴图编号：<span>2132131321</span></p>
                </div>
            </div>
            <el-row>
                <el-col :span="24">
                    <div>
                        <p>区域一贴图：</p>
                        <p>售价影响/元：</p>
                    </div>
                    <div class="model-img">
                        <img src="" alt="">
                        <span>¥50.00</span>
                    </div>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button round @click="imgDatailsVisible = false">取 消</el-button>
            </div>
        </el-dialog>
        <!--弹出框-->
        <div><v-nav :nav="nav"></v-nav></div>
        <transition name="fade">
            <div class="addProduct">
                <header>
                    <h1 v-if="!editProID">新增产品</h1>
                    <h1 v-else>编辑产品</h1>
                </header>
                <p class="ptop"><span>*</span>为必填项</p>
                <div class="productBaseInfo">产品基础信息</div>
                <div class="productBaseItem">
                    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="165px" class="demo-ruleForm">
                        <el-row class="">
                            <el-col :span="8">
                                <el-form-item label="产品名称：" prop="productNameStr">
                                    <el-input class="" clearable v-model="ruleForm.productNameStr"></el-input>
                                </el-form-item>
                            </el-col>


                            <el-col :span="8">
                                <el-form-item label="产品品牌：" prop="brandValueStr">
                                    <el-select v-model="ruleForm.brandValueStr"
                                               filterable
                                               clearable
                                               @change="selectedBrandID"
                                               class=""
                                               placeholder="请选择">
                                        <el-option
                                                v-for="item in productBrand"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>

                        </el-row>

                        <el-row class="">
                            <el-col :span="8">
                                <el-form-item label="产品分类：" prop="productCategoryForm">
                                    <el-cascader
                                            @change="selectCategoryNO"
                                            @active-item-change="handleItemChange"
                                            v-model="ruleForm.productCategoryForm"
                                            placeholder="请选择"
                                            :props="props"
                                            class=""
                                            clearable
                                            :options="productCategoryDrop"
                                            filterable>
                                    </el-cascader>
                                </el-form-item>

                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="产品型号：" prop="productTypeCodeStr">
                                    <!--<el-input-->
                                    <!--style="width: 320px;"-->
                                    <!--v-model="ruleForm.productTypeCodeStr"-->
                                    <!--placeholder="请输入内容"></el-input>-->
                                    <el-autocomplete
                                            v-model="ruleForm.productTypeCodeStr"
                                            :fetch-suggestions="querySearchAsync"
                                            @select="handleSelect"
                                    ></el-autocomplete>
                                    <!-- @select="handleSelect"-->
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-row class="">
                            <el-col :span="8">
                                <el-form-item label="产品系列：">
                                    <el-select
                                            filterable
                                            v-model="ruleForm.productSeriesStr"
                                            @change="selectedSeriesID"
                                            class=""
                                            clearable
                                            placeholder="请选择">
                                        <el-option
                                                v-for="item in productSeries"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="产品款式：">
                                    <el-select filterable
                                               v-model="ruleForm.productStyleStr"
                                               clearable
                                               @change="selectStyleID"
                                               class=""
                                               placeholder="请选择">
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
                        <el-row v-if="isHard">
                            <el-col :span="8" >
                                <el-form-item label="产品规格：">
                                    <el-input class="" clearable v-model="ruleForm.productSpec" maxlength="50"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="是否有特效：">
                                    <el-checkbox size="medium" v-model="ruleForm.isSpecialEffects"></el-checkbox>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <!--软装显示-->
                        <el-row class="" v-if="!isHard">
                            <el-col :span="8" style="max-height: 70px;">
                                <el-form-item label="长：" prop="productLength">
                                    <el-input class="" clearable v-model="ruleForm.productLength" @blur="verifyLength">
                                        <template slot="append">cm</template>
                                    </el-input>
                                    <p v-show="lengthWhos" class="verify">{{productLengthTxt}}</p>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" style="max-height: 70px;">
                                <el-form-item label="宽：" prop="productWidth">
                                    <el-input class="" clearable v-model="ruleForm.productWidth" @blur="verifyWidth">
                                        <template slot="append">cm</template>
                                    </el-input>
                                    <p v-show="widthWhos"  class="verify">{{productWidthTxt}}</p>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" style="max-height: 70px;">
                                <el-form-item label="高：" prop="productHeight">
                                    <el-input class="" clearable v-model="ruleForm.productHeight" @blur="verifyHeight">
                                        <template slot="append">cm</template>
                                    </el-input>
                                    <p v-show="heightWhos"  class="verify">{{productHeightTxt}}</p>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="产品规格：">
                                    <el-input clearable v-model="ruleForm.productSpec" maxlength="50"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="是否有特效：">
                                    <el-checkbox size="medium" v-model="ruleForm.isSpecialEffects"></el-checkbox>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" style="max-height: 70px;" v-if="isBedsideDimensions">
                                <el-form-item label="床沿尺寸：" prop="bedsideDimensions">
                                    <el-input class="" clearable v-model="ruleForm.bedsideDimensions" @blur="verifybedsideDimensions">
                                        <template slot="append">cm</template>
                                    </el-input>
                                    <p v-show="bedsideDimensionsWhos"  class="verify">{{bedsideDimensionsTxt}}</p>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        
                        <el-row class="" v-if="productTypes==1" v-for="(item,index) in ruleForm.ceilingCrossSectionDataDTOList">
                            <el-col :span="8">
                                <el-form-item :label="'截面数据'+(++index)+'：'">
                                    <!-- <el-input class="" clearable v-model="item.ceilingInfo"></el-input> -->
                                    <textarea class="productDesc"
                                            type="textarea" :rows="3"
                                            @change="disabledFun(index-1)"
                                            style="width:100%;box-sizing: border-box;"
                                            v-model="item.ceilingInfo">
                                    </textarea>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
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
                            <el-col :span="8">
                                <el-form-item label="适用面积：">
                                    <el-select 
                                        :disabled="disabled[index-1]" v-model="item.applyArea" @change="applyChange(index-1)" 
                                        @remove-tag="removeTag" multiple :placeholder="'请先输入截面数据'+(index-1)">
                                        <el-option
                                        v-for="v in applyAreaList[index-1]"
                                        :key="v.id"
                                        :disabled="v.disabled?true:false"
                                        :label="v.name"
                                        :value="v.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="" v-if="productTypes==2">
                            <el-col :span="8" style="max-height: 70px;">
                                <el-form-item label="挡水边(靠墙)截面数据：">
                                    <el-input class="" clearable v-model="ruleForm.crossSectionDataForWall"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" style="max-height: 70px;">
                                <el-form-item label="挡水边(外)截面数据：">
                                    <el-input class="" clearable v-model="ruleForm.crossSectionDataForOut"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="" v-if="productTypes==2">
                            <el-col :span="8">
                                <el-form-item label="挡水边(靠墙)材质 :">
                                    <div class="imgRows">
                                        <div class="zzcli" @click="addImg('wall')" v-show="!shuibian.wall.id"></div>
                                        <i class="el-icon-circle-plus icons"></i>
                                        <span>上传图片</span>
                                        <div v-if="shuibian.wall.id" class="delIcon">
                                            <i class="el-icon-error" @click="delImg('wall')"></i>
                                        </div>
                                        <img v-if="shuibian.wall.id" :src="BASE_URL.sourceBaseUrl+shuibian.wall.src">
                                    </div>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="挡水边(外)材质 :">
                                    <div class="imgRows">
                                        <div class="zzcli" @click="addImg('out')" v-show="!shuibian.out.id"></div>
                                        <i class="el-icon-circle-plus icons"></i>
                                        <span>上传图片</span>
                                        <div v-if="shuibian.out.id" class="delIcon">
                                            <i class="el-icon-error" @click="delImg('out')"></i>
                                        </div>
                                        <img v-if="shuibian.out.id" :src="BASE_URL.sourceBaseUrl+shuibian.out.src">
                                    </div>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <!--<transition name="fade">-->
                        <!--<el-row :gutter="24" v-if="getProductPorps.length"-->
                        <!--style="width:860px; margin:1px 1px 20px 1px; padding-top:15px; background: #f8f8f8">-->
                        <!--<h2 class="propTitle"><span class="el-icon-setting"></span>产品属性1111</h2>-->
                        <!--<el-col :span="11" style="height:60px" v-for="(item,index) in getProductPorps" :key="index">-->
                        <!--<el-form-item :label="item.name">-->
                        <!--<el-select v-model="selectedProps[index]" value-key="id"-->
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
                        <!--</el-row>-->
                        <!--</transition>-->
                        <div class="productBaseInfo">产品描述信息</div>

                        <el-row class="formFrameWidth">
                            <el-col>
                                <el-form-item label="产品风格：">
                                    <el-select
                                            multiple
                                            v-model="baseValue"
                                            @change="selectFenge"
                                            class=""
                                            clearable
                                            placeholder="请选择">
                                        <el-option
                                                v-for="item in baseProductStyle"
                                                :key="item.id"
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
                                    <!--<el-input-->
                                            <!--placeholder="请输入内容"-->
                            <!--v-model.number="advicePriceInput1"-->
                                            <!--class="input-with-select"-->
                                            <!--style="width: 200px"-->
                                    <!--&gt;-->
                            <!--<template slot="append">{{selectSaleUnionName}}</template>-->
                            <!--<p v-if="advicePriceInput1 < 0" style="color: red">请输入大于等于0的数字</p>-->
                                    <!--</el-input>-->

                                <!--</el-form-item>-->
                            <!--</el-col>-->
                            <el-col :span="8">
                                <el-form-item label="原价：" label-width="120">
                                    <el-input
                                            placeholder="请输入内容"
                                            v-model.number="advicePriceInput2"
                                            class="input-with-select"
                                            style="width: 200px"
                                    >
                                        <el-select v-model="selectSaleUnionId"
                                                   style="background:#eee; width: 110px; margin-right: -140px;"
                                                   clearable
                                                   slot="suffix" placeholder="请选择">
                                            <el-option
                                                    v-for="item in unitList"
                                                    :key="item.id"
                                                    :label="item.name"
                                                    :value="item.id">
                                            </el-option>
                                        </el-select>
                                        <p v-if="advicePriceInput2 < 0" style="color: red">请输入大于等于0的数字</p>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="10">
                                <el-form-item label="装修单价：" label-width="120">
                                    <el-input
                                            placeholder="请输入内容"
                                            v-model="decorationPrice"
                                            class="input-with-select"
                                            style="width: 200px"
                                            @blur="verifydecorationPrice"
                                    >
                                        <template slot="append">{{this.selectSaleUnionName}}</template>
                                    </el-input>
                                    <p v-show="this.decorationPriceWhos"  class="verify">{{this.decorationPriceTxt}}</p>
                                </el-form-item>
                            </el-col>

                        </el-row>

                        <el-row class="formFrameWidth">
                            <div class="productPic"><span>*</span>产品图片
                                <el-tooltip effect="dark" placement="right">
                                    <div slot="content">建议上传图片像素不超 <br> 过2M,尺寸比例1:1</div>
                                    <el-button type="text" class="tooltip-btn">?</el-button>
                                </el-tooltip>：
                            </div>
                            <el-form-item>
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
                            <!--<p v-if="!fileList2.length" class="uploadTip">请上传图片</p>-->
                        </el-row>

                        <el-row class="formFrameWidth">
                            <el-form-item label="产品描述：">
                <textarea class="productDesc"
                          maxlength="200"
                          type="textarea"
                          :rows="6"
                          style="width: 777px"
                          placeholder="请输入内容"
                          v-model="prodTextarea">
                </textarea>
                                <p style="font-size: 14px; color: #999">此处最多允许填写200字</p>
                            </el-form-item>
                        </el-row>

                        <div class="productBaseInfo">关联产品运营信息</div>
                        <el-row class="goods-operating-info">
                            <el-col :span="24">
                                <h2 class="title"><span class="star">*</span>运营分类信息：</h2><span @click="productCategoryTree">添加</span>
                            </el-col>
                        </el-row>
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
                        <div class="productBaseInfo" v-if="getProductPorps.length > 0&&!isHard">产品规格信息</div>
                        <el-row class="specification-info" v-for="(item, itemIndex) in getProductPorps" :key="itemIndex" v-if="!isHard">
                            <el-col :span="2">
                                {{item.name}}：
                            </el-col>
                            <el-col :span="20">
                                <el-checkbox-group v-model="infoCheckList[itemIndex]">
                                    <el-checkbox :label="items" v-for="(items, itemsIndex) in item.children" :key="itemsIndex" @change="infoChageBox(itemIndex, itemsIndex,items.id)">
                                        {{items.name}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-col>
                        </el-row>
                        <el-row class="specification-info" v-if="getProductPorps.length > 0&&!isHard">
                            <el-col :span="2">
                                产品SKU信息：
                            </el-col>
                            <el-col :span="21">
                                <el-table
                                        header-cell-class-name="SKU-table-header"
                                        :data="SKUInfo"
                                        border>
                                    <el-table-column
                                            prop="name"
                                            label="产品名称"
                                            width="320">
                                        <template slot-scope="scope">
                                            <span style="color: #333">{{ruleForm.productNameStr}}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column
                                            v-for="(item, index) in tableNameList"
                                            :key="index"
                                            :prop="item.key"
                                            :label= item.name>
                                    </el-table-column>
                                    <el-table-column
                                            label="模型/贴图信息">
                                        <template slot-scope="scope">

                                            <img  v-if="scope.row.thumbnailPath"
                                                  :src="BASE_URL.sourceBaseUrl + scope.row.thumbnailPath"
                                                  alt=""
                                                  style="width: 90px;height: 90px;display: block;margin: 0 auto">
                                            <span v-if="!scope.row.thumbnailPath && !isImg"
                                                  @click="SKUTexture(scope, '贴图')">添加贴图</span>
                                            <span v-if="!scope.row.thumbnailPath && isImg"
                                                  @click="SKUTexture(scope, '模型')">添加模型</span>
                                            <!--<span @click="imgDatailsVisible = true">详情</span>-->
                                            <span v-if="scope.row.thumbnailPath && !isImg"  @click="SKUTexture(scope, '贴图')">更改</span>
                                            <span v-if="scope.row.thumbnailPath && isImg"  @click="SKUTexture(scope, '模型')">更改</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="操作">
                                        <template slot-scope="scope">
                                            <span @click="deleteSKU(scope)">移除</span>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </el-col>
                            <!--<el-col :span="24">-->
                            <!--<h2>分配：</h2>-->
                            <!--&lt;!&ndash;<el-radio v-model="allocation" label="1">渠道</el-radio>&ndash;&gt;-->
                            <!--&lt;!&ndash;<el-radio v-model="allocation" label="2">线上</el-radio>&ndash;&gt;-->
                            <!--<el-checkbox-group v-model="allocation" @change="getAllocation">-->
                            <!--<el-checkbox label="渠道">渠道</el-checkbox>-->
                            <!--<el-checkbox label="线上">线上</el-checkbox>-->
                            <!--</el-checkbox-group>-->
                            <!--</el-col>-->
                        </el-row>
                        <!--硬装产品-->
                        <hardProduct @connectModel="hardConnectModel" @handleAddModelTexture="upLoadTexture" v-if="isHard"></hardProduct>
                        <!--end-->
                        <div class="productBaseInfo"  v-if="isModel&&!isHard">模型相关信息</div>
                        <el-row :gutter="24"  v-if="isModel&&!isHard">
                            <el-col :span="24">
                                <el-form-item label="模型和贴图：" class="model-img-txt">
                                    <el-button round size="samll" v-if="isModel && isImg" @click="showModelList" class="el-button--primary">关联模型
                                    </el-button>
                                    <el-button round size="samll" v-if="isModel && !isImg" class="el-button--primary"
                                               @click="handleAssocateTexture">关联贴图
                                    </el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <!--模型贴图区域-->
                        <div class="modelTextureFrame" v-if="relateModelInfo.isShow">
                            <el-row :gutter="24">
                                <el-col :span="12" class="currentModel">
                                    <div class="modelPic">
                                        <img :src="BASE_URL.sourceBaseUrl+relateModelInfo.picPath" alt="">
                                    </div>
                                    <div class="selectedModel">
                                        <div>
                                            <p>模型名称：{{relateModelInfo.modelName}}</p>
                                            <p>模型编号：{{relateModelInfo.modelCode}}</p>
                                            <p>模型规格：长 {{relateModelInfo.length}} X 宽 {{relateModelInfo.width}} X 高 {{relateModelInfo.height}}</p>
                                        </div>
                                    </div>
                                </el-col>
                                <el-col :span="12" class="addDelModel">
                                    <!--<el-button round size="small" @click="handleAddArea">添加区域</el-button>-->
                                    <el-button round size="small" @click="removeModel">删除模型</el-button>
                                </el-col>
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
                                            <div @mouseenter="jtem.deleteBtnVisible = true" @mouseleave="jtem.deleteBtnVisible = false"
                                                 style="position: relative;">
                        <span class="posDelete2" v-show="jtem.deleteBtnVisible" style=" z-index:110;"
                              @click="deleteAreaTexture(item,jndex)"></span>
                                                <img v-if="item.isDefault==jndex"
                                                     style="position: absolute; left: 64px; top:-1px; width: 16px; height: 16px; z-index: 11;"
                                                     src="../../../assets/images/icons/select.png" alt="">
                                                <el-radio size="medium" v-model="item.isDefault" :label="jndex">&nbsp;</el-radio>
                                                <img :src="BASE_URL.sourceBaseUrl+jtem.thumbnailPath" alt="">
                                            </div>
                                            <el-input style="text-align: center!important;" v-model="jtem.affectPrice" size="mini"
                                                      @change="handleChange"></el-input>
                                        </el-col>
                                    </el-row>
                                </el-row>
                            </el-row>
                        </div>
                        <!--模型贴图区域 END-->

                        <div class="modelTextureFrame" v-if="relateTexture.isShow">
                            <el-row :gutter="24">
                                <el-col :span="12" class="currentModel">
                                    <div class="modelPic">
                                        <img :src="BASE_URL.sourceBaseUrl+relateTexture.thumbnailPath" alt="">
                                    </div>
                                    <div class="selectedModel">
                                        <div>
                                            <p>贴图名称：{{relateTexture.textureName}}</p>
                                            <p>贴图编号：{{relateTexture.textureCode}}</p>
                                        </div>
                                    </div>
                                </el-col>
                                <el-col :span="12" class="addDelModel">
                                    <!--<el-button round size="small" @click="handleAddArea">添加区域</el-button>-->
                                    <el-button round size="small" @click="handleRemoveTexture">删除贴图</el-button>
                                </el-col>
                            </el-row>
                        </div>
                        <!--<el-row :gutter="24" class="selectAllotFrame" v-if="relateModelInfo.isShow || relateTexture.isShow">
              <el-col :span="24">
                <el-form-item label="分配渠道：">
                  <el-checkbox-group v-model="distrAllotList">
                    <el-checkbox label="2b">渠道</el-checkbox>
                    <el-checkbox label="2c">线上</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>-->
                        <div class="splitLine"></div>
                        <el-form-item>
                            <el-button type="primary" class="confirmBtn" round @click="submitForm('ruleForm')">录入</el-button>
                            <el-button round class="cancelBtnBg marRight" @click="resetForm('ruleForm')">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <el-dialog title="关联模型" @close="resetSearchModel" :visible.sync="dialogModelVisible">
                    <el-form class="textureSearch" :model="assModelDia.query">
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-input clearable placeholder="请输入模型编号" v-model="assModelDia.query.modelCode"></el-input>
                            </el-col>
                            <el-col :span="12">
                                <el-input clearable placeholder="请输入模型型号" v-model="assModelDia.query.modelModelNum"></el-input>
                            </el-col>
                        </el-row>

                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-input clearable placeholder="请输入模型名称" v-model="assModelDia.query.modelName"></el-input>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item>
                                    <el-button round class="searchBtn" @click="showModelList">搜索</el-button>
                                    <el-button round class="searchBtn" @click="resetModelCondition">重置</el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>

                    <el-table :data="modelListData" v-loading="modelListLoading" height="550">
                        <el-table-column property="thumbPicPath" label="模型缩略图">
                            <template slot-scope="scope">
                                <img :src="BASE_URL.sourceBaseUrl+scope.row.thumbPicPath" width="90" height="90" alt="">
                            </template>
                        </el-table-column>
                        <el-table-column prop="modelCode" label="模型编号"></el-table-column>
                        <el-table-column prop="modelName" label="模型名称"></el-table-column>
                        <el-table-column prop="categoryNames" label="模型分类"></el-table-column>
                        <el-table-column label="模型规格" width="80">
                            <template slot-scope="scope">
                                长：{{scope.row.length}}宽：{{scope.row.width}}高：{{scope.row.height}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="modelModelNum" label="模型型号"></el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button type="primary" @click="modelSelectUse(scope.row)">选用</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-dialog>
                <!--关联贴图上传贴图-->
                <el-dialog title="上传贴图" :close-on-click-modal="false" @close="resetSearch"
                           :visible.sync="dialogAddTextureVisible">
                    <el-form :model="assTextureDia.query">
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-form-item>
                                    <el-select style="width:100%" @change="searchTexture" clearable v-model="assTextureDia.query.texture"
                                               placeholder="请选择材质/材料">
                                        <el-option
                                                v-for="item in ProductTextureDrop"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item>
                                    <el-select style="width:100%" clearable v-model="assTextureDia.query.textureAttr"
                                               @change="searchTexture" placeholder="请选择材质贴图表面属性">
                                        <el-option v-for="item in textureAttrDrop"
                                                   :key="item.id"
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
                                <el-form-item>
                                    <el-input clearable placeholder="请输入材质贴图名称" v-model="assTextureDia.query.textureName"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item>
                                    <el-select v-model="assTextureDia.query.textureType" clearable style="width: 100%"
                                               @change="searchTexture" placeholder="请选择贴图类型">
                                        <el-option
                                                v-for="item in textureType"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-form-item>
                                    <el-input clearable v-model="assTextureDia.query.modelNumber" placeholder="请输入材质型号"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item>
                                    <el-input clearable v-model="assTextureDia.query.textureCode" placeholder="请输入材质贴图编号"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-col :span="12">
                            <el-form-item>
                                <el-button round class="searchBtn" @click="resetTextrue2">重置</el-button>
                                <el-button round class="searchBtn" @click="searchTexture">搜索</el-button>
                            </el-form-item>
                        </el-col>
                    </el-form>
                    <el-table :data="TextureListData" v-loading="textureLoading" height="550">
                        <el-table-column prop="thumbnailPath" label="贴图">
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
                <!--上传贴图 END -->

                <!--模型上传贴图-->
                <el-dialog top="1vh" :close-on-click-modal="false" title="上传贴图" @close="clearSelectModelTexture"
                           :visible.sync="dialogModelTextureVisible">
                    <el-form class="textureSearch" :model="assModelTextureDia.query">
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-select style="width:100%" clearable v-model="assModelTextureDia.query.texture"
                                           @change="searchTextureImg" placeholder="请选择材质/材料">
                                    <el-option
                                            v-for="item in ProductTextureDrop"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-col>
                            <el-col :span="12">
                                <el-select style="width:100%" clearable @change="searchTextureImg"
                                           v-model="assModelTextureDia.query.textureAttr" placeholder="请选择材质贴图表面属性">
                                    <el-option v-for="item in textureAttrDrop"
                                               :key="item.id"
                                               :label="item.name"
                                               :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-select v-model="assModelTextureDia.query.textureType" clearable @change="searchTextureImg"
                                           placeholder="请选择材质贴图类型">
                                    <el-option
                                            v-for="item in textureType"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-col>
                            <el-col :span="12">
                                <el-input clearable placeholder="请输入材质型号" v-model="assModelTextureDia.query.modelNumber"></el-input>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-input clearable v-model="assModelTextureDia.query.textureName" placeholder="请输入材质贴图名称"></el-input>
                            </el-col>
                            <el-col :span="12">
                                <el-input clearable v-model="assModelTextureDia.query.textureCode" placeholder="请输入材质贴图编号"></el-input>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-button round class="searchBtn" @click="resetTextrue">重置</el-button>
                            <el-button round class="searchBtn" @click="searchTextureImg(1)">搜索</el-button>
                        </el-row>
                        <br>
                        <div class="splitLine"></div>
                        <el-row :gutter="24">
                            <el-col :span="20">
                                共 {{totalPage}} 张，<span style="color: #ff6419">已选 {{multipleSelection.length}} 张</span>
                            </el-col>
                            <el-col :span="3">
                                <el-button round class="el-button--primary" @click="handleModelTextureAddConfirm">确定添加</el-button>
                            </el-col>
                        </el-row>
                        <el-row class="selectCHKS">
                            <el-table
                                    :data="TextureListData"
                                    height="450"
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
                                <el-table-column prop="modelNumber" label="材质贴图名称"></el-table-column>
                                <el-table-column prop="textureballFileId" label="材质类型">
                                    <template slot-scope="scope">
                                        <p>{{scope.row.textureballFileId == 0 ? '贴图' : '材质球'}}</p>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <div class="pageFrame">
                                <el-pagination
                                        @size-change="handleSizeChange"
                                        @current-change="pageFrameChange"
                                        :current-page.sync="page"
                                        :page-sizes="[10, 50, 100, 200,500]"
                                        :page-size="assModelTextureDia.query.limit"
                                        layout="total, sizes, prev, pager, next, jumper"
                                        :total="totalPage">
                                </el-pagination>
                            </div>
                            <!--    <el-checkbox-group v-model="modelTexture.tempSelectedModelTextures">
                    &lt;!&ndash;<el-checkbox label="" style="display: none"></el-checkbox>&ndash;&gt;
                    <div class="item" v-for="(tl,index) in TextureListData" :key="index">
                      <img src="../../../assets/images/icons/select.png" v-if="selectedChk(tl.textureId)" class="selected"
                           width="17" height="16" alt="">
                      <el-tooltip placement="top">
                        <div slot="content" style="line-height: 22px">
                          材质名称：{{tl.textureName}}<br/>
                          材质编号：{{tl.textureCode}}<br/>
                          材质型号：{{tl.modelNumber}}<br/>
                          贴图类型：{{tl.textureballFileId == 0 ? '贴图' : '材质球'}}
                        </div>
                        <el-checkbox class="chks" :key="tl.textureId" :label="tl.textureId"></el-checkbox>
                      </el-tooltip>
                      <el-col class="imgPos">
                        <img width="90" height="90" :src="BASE_URL.sourceBaseUrl+tl.thumbnailPath"/>
                      </el-col>
                    </div>
                  </el-checkbox-group>-->

                        </el-row>

                    </el-form>
                </el-dialog>
                <!--添加硬装模型弹窗-->
                <addHardProduct ref="addHardProduct"></addHardProduct>
                <!--设置产品规格信息-->
                <setProductInfo ref="setProductInfo"></setProductInfo>
            </div>
        </transition>
    </div>
</template>
<script type="es6">
    import hardProduct from './conponents/hardProduct'; //硬装产品
    import addHardProduct from './conponents/addHardProduct'; //硬装产品
    import setProductInfo from './conponents/setProductInfo'; //硬装产品
    import hardProductMinixs from "../../../filters/hardProductMinixs";
    import {PointNum, PointTwo} from "../../../filters/data";
    import {mapGetters} from 'Vuex'
    import qs from 'qs'

    export default {
        mixins:[hardProductMinixs],
        components: {
            hardProduct,
            addHardProduct,
            setProductInfo
        },

        data() {
            // let verifyLength = (rule, value, callback) => {
            //   if (value === '') {
            //     // callback(new Error('请输产品长度'))
            //   } else {
            //     let i = /^[1-9]\d*$/
            //     if (!i.test(value)) {
            //       callback(new Error('请输产品长度为正整数'))
            //     }
            //     // if (this.ruleForm.productLength !== '') {
            //     //   this.$refs.ruleForm.validateField('productLength')
            //     // }
            //     // callback()
            //   }
            // };
            // let verifyWidth = (rule, value, callback) => {
            //   if (value === '') {
            //     // callback(new Error('请输产品长度'))
            //   } else {
            //     let i = /^[1-9]\d*$/
            //     if (!i.test(value)) {
            //       callback(new Error('请输产品长度为正整数'))
            //     }
            //     // if (this.ruleForm.productLength !== '') {
            //     //   this.$refs.ruleForm.validateField('productLength')
            //     // }
            //     // callback()
            //   }
            // };
            // let verifyHeight = (rule, value, callback) => {
            //   if (value === '') {
            //     // callback(new Error('请输产品长度'))
            //   } else {
            //     let i = /^[1-9]\d*$/
            //     if (!i.test(value)) {
            //       callback(new Error('请输产品长度为正整数'))
            //     }
            //     // if (this.ruleForm.productLength !== '') {
            //     //   this.$refs.ruleForm.validateField('productLength')
            //     // }
            //     // callback()
            //   }
            // };
            return {
                loading:'',
                nav: [
                    { nav: "产品库", url: "/product" },
                    { nav: "新增产品", url: "" }
                ],
                headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
                totalPage:0,
                page:0,
                productSKUInfos:[],
                tableNameList:[],
                restaurants: [],
                state4: '',
                timeout:  null,
                ProductPorpsName: [],
                recommandData:[],
                imgInfoList: [], // 添加贴图弹框内的贴图信息表格
                upImgTable: [
                    {img: '', code: '', name: '', texture: '123', type: ''},
                    {img: '', code: '', name: '', texture: '123', type: ''},
                    {img: '', code: '', name: '', texture: '123', type: ''},
                    {img: '', code: '', name: '', texture: '123', type: ''},
                    {img: '', code: '', name: '', texture: '123', type: ''},
                ],
                addImgForm: {},
                materials: [], // 材质选择类型列表
                surface: [], // 材质选择表面属性列表
                UpImgForm: {texture: '', surface: '', type: '', modelNumber: '', imgName: '', imgCode: ''},
                seekClassify: '',
                infoClassifyVisible: false,
                imgVisible: false,
                imgDatailsVisible: false,
                addModelVisible: false,
                modelDatailsVisible: false,
                total: 0, // 弹窗分页
                textureTotal: 0, // 关联贴图弹窗分页
                modelTotal: 0, // 关联模型弹窗分页
                isImg: null, // 判断是否为模型或者贴图
                SKUId: [],
                SKUIndex: '',
                productCategorylist1: [], // 产品分类总列表
                productCategorylist2: [], // 产品分类第2层列表
                productCategorylist3: [], // 产品分类第3层列表
                productCategorylist4: [], // 产品分类第4层列表
                productCategorylist5: [], // 产品分类第5层列表
                checkProductList: [], // 最终选择的产品分类列表
                productClassList: [], // 选择完产品分类，所需要展示在页面上的分类列表
                productClassLists: [], // 零时保存产品分类列表
                newProducLists: [], // 展示的产品分类
                lastNewProducLists:{},
                producListNum:[],
                productClassIdLists:[],
                nameArr:[],
                lastNameArr:'',
                newProductClassList:[],
                indexList: {index1: 0, index2: 0, index3: 0, index4: 0, index5: 0}, // 每一层分类的index
                GoodsOperatingList: [],
                disabled:[true,true,true],
                infoCheckList: [],
                getProductPorps: [], // 产品属性
                sizeInfoList: [], // 产品规格信息列表
                popupModelCode: '',
                //popupModelAuthor: '',
                popupModelNum:'', //产品模型型号参数
                popupModelName: '',
                isAddModel: true,
                isUpImg: false,
                addModelVisibleTxt: '添加模型',
                // modelIdList: [], // 用于判断模型是否已经被选
                isDefaultList: [], // 模型材质贴图里，当选择的不是默认贴图时为0，默认为1
                popupType: '', // 弹窗类型，用于判断选择贴图属于贴图还是模型
                popupType2: '', // 弹窗类型，挡水边贴图
                shuibian:{wall:{},out:{}},
                popupImageUrl: '',
                uploadImgIndex: -1, // 上传贴图选中区域下标
                modelImgInfo: {}, // 选中的模型贴图详情
                modelImgList: [], // 选中模型，贴图区域列表
                modelTable: [], // 添加模型弹窗模型信息列表
                SKUInfo: [], // SKU信息列表
                SKUInfoHeader: [], // SKU信息动态表头
                imgIndexShow: -1,
                indexShow: -1,
                allocation: [],
                productHeightTxt: '',
                productWidthTxt: '',
                productLengthTxt: '',
                bedsideDimensionsTxt: '',
                decorationPriceTxt: '',
                onePic: '',
                textureLoading: true,
                addTextureLoading:true,
                modelListLoading: true,
                uploadPic: false,
                relateModelInfo: {
                    isShow: false,
                    modelName: '',
                    modelCode: '',
                    length: '',
                    width: '',
                    height: '',
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
                relateTexture: {
                    isShow: false,
                    textureId: 0,
                    textureName: '',
                    textureCode: ''
                },
                modelTexture: {
                    tempSelectedModelTextures: [],
                    selectedModelTextures: [],
                    areaId: 0,
                    index: 0
                },
                assTextureDia: {
                    query: {
                        page:0,
                        limit: 10,
                        companyId: sessionStorage.getItem('companyID')
                    }
                },
                assModelDia: {
                    query: {
                        limit: 10,
                        isUsed: 0,
                        companyId: sessionStorage.getItem('companyID'),
                        transStatus: 'SUCCESS'
                    }
                },
                assModelTextureDia: {
                    query: {
                        page:1,
                        limit: 10,
                        companyId: sessionStorage.getItem('companyID')
                    }
                },
                dialogAddTextureVisible: false,
                dmFormTexture: {},
                dtFormTexture: {},
                dialogModelVisible: false, // 弹框模型列表
                modelListData: [], // 模型列表
                TextureListData: [], // 贴图列表
                formLabelAlign: {
                    name: '',
                    author: '',
                    code: ''
                },
                formTexture: {
                    texture: '',
                    textureAttr: '',
                    textureName: '',
                    textureCode: ''
                },
                query: {
                    page: 0,
                    limit: 10
                },
                area: [],
                num1: 0,
                modelRadio: '',
                distrAllotList: [], // 分配渠道
                selectedProps: [], // 选中的属性
                editProID: '',
                uploadParams: {
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                isMultistageList:false,//添加运营分类信息是否可多级
                lastIsMultistageList:false,
                advicePriceInput1: '',
                advicePriceInput2: '',
                decorationPrice: '',
                textureAttrDrop: [], // 贴图表面属性
                ProductTextureDrop: [], // 材质材料
                selectedTextureId: '', // 材质ID
                selectStyleId: '', // 产品风格ID
                selectedSeriesId: '', // 产品系列ID
                selectedBrandId: '', // 产品品牌ID
                selectCategoryCode: '', // 产品分类编码
                unitList: [], // 数字字典
                selectSaleUnionId: '',
                selectSaleUnionName: "元",
                baseTextarea: '',
                prodTextarea: '',
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
                    value:'keyCode',
                    children: 'children'
                },
                // 上传图片文件列表
                imageUrl: '',
                fileList2: [],
                ruleForm: { // 验证表单
                    productSpec:'',
                    productNameStr: '',
                    productCategoryForm: [],
                    productTypeCodeStr: '',
                    brandValueStr: '',
                    productSeriesStr: '',
                    productStyleStr: '',
                    productLength: '',
                    productWidth: '',
                    productHeight: '',
                    ceilingCrossSectionDataDTOList:[
                        {ceilingInfo:undefined,lightInfo:undefined,applyArea:undefined},
                        {ceilingInfo:undefined,lightInfo:undefined,applyArea:undefined},
                        {ceilingInfo:undefined,lightInfo:undefined,applyArea:undefined}
                    ],
                    dataTypeForWall:undefined,
                    dataTypeForOut:undefined,
                    textureIdForWall:undefined,
                    textureIdForOut:undefined,
                    crossSectionDataForWall:undefined,
                    crossSectionDataForOut:undefined,
                    bedsideDimensions: '',
                    isSpecialEffects:false
                },
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
                productTypes:0,
                
                // 验证表单规则
                rules: {
                    productNameStr: [
                        {required: true, message: '请输入产品名称', trigger: 'blur'},
                        {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    ],
                    productTypeCodeStr: [
                        {required: true, message: '请选择产品型号', trigger: 'blur'},
                        {required: true, message: '请选择产品型号', trigger: 'change'},
                        {min: 1, max: 40, message: '长度在 40 个字符', trigger: 'blur'}
                    ],
                    productCategoryForm: [
                        {required: true, message: '请选择产品分类', trigger: 'change'}
                    ],
                    brandValueStr: [
                        {required: true, message: '请选择产品品牌', trigger: 'change'}
                    ],
                    bedsideDimensions: [
                        {required: true, message: '请输入床沿尺寸', trigger: 'blur'}
                    ],
                },
                // 品牌
                productBrand: [],
                productPropBO: [],
                isTexture: false, // 贴图
                isModel: false, // 模型
                defaultPicId: 0,
                modelId: 0,
                showDeletes: [],
                multipleSelection: [],
                heightWhos: false,
                lengthWhos: false,
                widthWhos: false,
                bedsideDimensionsWhos: false,
                decorationPriceWhos: false,
                valuationUnit: "元/个",
                isBedsideDimensions: false,
                typeArr: []
            }
        },
        computed:{
            ...mapGetters(['gettersHardModelList','gettersAreaId','gettersSetPropsId','gettersModelId','gettersSKUInfos']),
            isHard(){
                return sessionStorage.getItem('addProductType')=='hard' ? true : false
            }
        },
        methods: {
            pageFrameChange(val){
                this.page=val;
                this.assTextureDia.query.page=val;
                this.searchTexture();
            },

            handleModelSizeChange(val){
                 this.page=val;
                this.seekModel();
            },

            handleSizeChange(val){
                this.page=1;
                this.assTextureDia.query.page=1;
                this.assTextureDia.query.limit=val;
                this.searchTexture();
            },
            getAllocation() {
            },
            /*移除当前一条SKU信息表*/
            querySearchAsync(queryString, cb) {

                this.API.modelNumberRecommand({
                    modelNumber: this.ruleForm.productTypeCodeStr,
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
            },
            handleCurrentChange(val){
                this.seekIng(val);
            },

            handleTextureChange(){
                this.searchTexture();
            },

            handleModelChange(){
                this.seekModel();
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
            handleSelect(item) {
            },
            deleteSKU(scope) {
                for (let item in scope.row) {
                    if (item.substring(0,4) == 'list') {
                        this.isitem(scope.row[item])
                    }
                }
                this.SKUInfo.splice(scope.$index, 1);
                this.SKUId.splice(scope.$index, 1);
            },
            isitem(item) {
                let cout = 0;
                for (let i = 0; i < this.SKUInfo.length - 1; i++) {
                    for (let items in this.SKUInfo[i]) {
                        if (item == this.SKUInfo[i][items]) {
                            //  cout++;
                        }
                    }
                }
                if (cout == 0) {
                    for (let i = 0; i < this.infoCheckList.length; i++) {
                        for (let j = 0; j < this.infoCheckList[i].length; j++) {
                            if (this.infoCheckList[i][j].name == item) {
                                this.infoCheckList[i].splice(j, 1);
                            }
                        }
                    }
                }
            },
            /*选中规格信息，生成对应选中的SKU信息*/
            infoChageBox(itemIndex, itemsIndex,id) {
                // this.ProductPorpsName.push(this.getProductPorps[itemIndex].name);
                this.tableNameList=[];

                var k=[];
                let arr = []
                for (let i = 0; i < this.infoCheckList.length; i++) {
                    if (this.infoCheckList[i].length > 0) {
                        arr.push(this.infoCheckList[i]);

                        k.push(this.getProductPorps[i].name);

                        // this.ProductPorpsName.push(this.getProductPorps[i].name)
                        // if (this.ProductPorpsName.length == 0) {
                        //     this.ProductPorpsName.push(this.getProductPorps[i].name)
                        // } else {
                        //     for (let j = 0; j < this.ProductPorpsName.length; j++) {
                        //         if (!this.ProductPorpsName[j] == this.getProductPorps[i].name) {
                        //             // this.ProductPorpsName[j] = this.getProductPorps[i].name
                        //             this.ProductPorpsName.push(this.getProductPorps[i].name)
                        //         }
                        //     }
                        // }
                    }


                }
                if (arr.length == 0) {
                    this.SKUInfo = [];
                    return;
                }
                if (arr.length > 1) {
                    this.typeArr = this.doExchange(arr);
                    //  let dataArr=[];
                    let dataArr=[];
                    for(let i=0;i<this.typeArr.length; i++) {
                        let string=this.typeArr[i];
                        let stringArr=string.split("|");
                        let ArrLength=stringArr.length;
                        if(i==0){
                            for (let y=0;y<ArrLength;y++){
                                let tabName={};
                                tabName.name=k[y];
                                tabName.key='list'+y;
                                tabName.id=this.getProductPorps[y]['id']
                                this.tableNameList.push(tabName)
                            }
                        }
                        let dataObj={};
                        for (let y=0;y<ArrLength;y++){
                            // dataObj[this.getProductPorps[y]['cn']]=stringArr[y]
                            dataObj['list'+y]=stringArr[y];
                            dataObj.textureId = '';
                            dataObj.thumbnailPath = '';
                            dataObj.modelId = '';
                            dataObj.modelTextureInfos = '';
                        }
                        dataArr.push(dataObj);
                    }
                    this.SKUInfo = dataArr;
                    let newDtatarr = this.deepClone(dataArr)

                    let SKUId = [];
                    // for (let i = 0; i < this.SKUInfo.length; i++) {
                    //     for (let j in this.SKUInfo[i]) {
                    //         this.SKUInfo[i][j] = this.SKUInfo[i][j].split(':')[0]
                    //     }
                    // }
                    for (let i = 0; i < newDtatarr.length; i++) {
                        let idArr = [];
                        for (let j in newDtatarr[i]) {
                            if (newDtatarr[i][j].indexOf(':') > 0) {
                                let id=newDtatarr[i][j].split(':')[1];
                                idArr.push(id);
                                this.SKUInfo[i][j] = newDtatarr[i][j].split(':')[0]
                                // let id = dataArr[i][j].slice(dataArr[i][j].indexOf(':') + 1);
                                // idArr.push(id);
                            }
                        }
                        SKUId.push(idArr);
                    }
                    this.SKUId = SKUId;
                    //
                } else {
                    this.typeArr = this.doExchange(arr);
                    let dataArr = [];
                    let tabName={};
                    if(this.typeArr.length>this.SKUInfo.length){
                        for (let i = 0; i < this.typeArr.length; i++) {
                            if(this.SKUInfo[i]){
                                if(this.SKUInfo[i].textureId!=null){
                                    let obj = {};
                                    obj.list0 = this.typeArr[i].name + ':' + this.typeArr[i].id;
                                    obj.id = this.typeArr[i].id;
                                    obj.textureId = this.SKUInfo[i].textureId;
                                    obj.thumbnailPath = this.SKUInfo[i].thumbnailPath   ;
                                    obj.modelId = '';
                                    obj.modelTextureInfos = [];
                                    dataArr.push(obj);
                                }else{
                                    let obj = {};
                                    obj.list0 = this.typeArr[i].name + ':' + this.typeArr[i].id;
                                    obj.id = this.typeArr[i].id;
                                    obj.textureId = this.typeArr[i].textureId;
                                    obj.thumbnailPath = this.typeArr[i].thumbnailPath   ;
                                    obj.modelId = '';
                                    obj.modelTextureInfos = [];
                                    dataArr.push(obj);
                                }
                            }else{
                                let obj = {};
                                obj.list0 = this.typeArr[i].name + ':' + this.typeArr[i].id;
                                obj.id = this.typeArr[i].id;
                                obj.textureId = this.typeArr[i].textureId;
                                obj.thumbnailPath = this.typeArr[i].thumbnailPath   ;
                                obj.modelId = '';
                                obj.modelTextureInfos = [];
                                dataArr.push(obj);
                            }
                        }
                    }else{
                        dataArr = this.SKUInfo;
                        for(let i=0;i<dataArr.length;i++){
                            if(this.SKUInfo[i].id==id){
                                dataArr.splice(i,1);
                            }
                        }


                    }
                    // for (let i = 0; i < this.typeArr.length; i++) {
                    //     let obj = {};
                    //     obj.list0 = this.typeArr[i].name + ':' + this.typeArr[i].id;
                    //     obj.id = this.typeArr[i].id;
                    //     obj.textureId = '';
                    //     obj.thumbnailPath = '';
                    //     obj.modelId = '';
                    //     obj.modelTextureInfos = [];
                    //     dataArr.push(obj);
                    // }
                    tabName.key = 'list0';
                    tabName.name = this.getProductPorps[itemIndex].name;
                    this.tableNameList.push(tabName);
                    this.SKUInfo = dataArr;
                    for (let i = 0; i < this.SKUInfo.length; i++) {
                        this.SKUInfo[i].list0 = this.SKUInfo[i].list0.split(':')[0]
                    }
                    let SKUId = [];
                    for (let i = 0; i < dataArr.length; i++) {
                        let idArr = [];
                        idArr.push(dataArr[i].id)
                        SKUId.push(idArr);
                    }
                    this.SKUId = SKUId;
                }
            },
            deepClone(data){
                var type = this.getType(data);
                var obj;
                if(type === 'array'){
                    obj = [];
                } else if(type === 'object'){
                    obj = {};
                } else {
                    //不再具有下一层次
                    return data;
                }
                if(type === 'array'){
                    for(var i = 0, len = data.length; i < len; i++){
                        obj.push(this.deepClone(data[i]));
                    }
                } else if(type === 'object'){
                    for(var key in data){
                        obj[key] = this.deepClone(data[key]);
                    }
                }
                return obj;
            },
            getType(obj){
                //tostring会返回对应不同的标签的构造函数
                var toString = Object.prototype.toString;
                var map = {
                    '[object Boolean]'  : 'boolean',
                    '[object Number]'   : 'number',
                    '[object String]'   : 'string',
                    '[object Function]' : 'function',
                    '[object Array]'    : 'array',
                    '[object Date]'     : 'date',
                    '[object RegExp]'   : 'regExp',
                    '[object Undefined]': 'undefined',
                    '[object Null]'     : 'null',
                    '[object Object]'   : 'object'
                };
                if(obj instanceof Element) {
                    return 'element';
                }
                return map[toString.call(obj)];
            },
            doExchange(arr){
                // 当数组大于等于2个的时候
                if(arr.length >= 2){
                    //  申明一个新数组,做数据暂存
                    var items = new Array(arr[0].length * arr[1].length);
                    // 申明新数组的索引
                    var index = 0;
                    // 2层嵌套循环,将组合放到新数组中
                    for(var i=0; i<arr[0].length; i++){
                        for(var j=0; j<arr[1].length; j++){
                            items[index] = arr[0][i].name?(arr[0][i].name+":"+arr[0][i].id +"|"+ arr[1][j].name+":"+arr[1][j].id):(arr[0][i] +"|"+ arr[1][j].name+":"+arr[1][j].id);
                            index++;
                        }
                    }
                    // 将新组合的数组并到原数组中
                    var newArr = new Array(arr.length -1);
                    for(var i=2;i<arr.length;i++){
                        newArr[i-1] = arr[i];
                    }
                    newArr[0] = items;
                    // 执行回调
                    return this.doExchange(newArr);
                }else{
                    return arr[0];
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
                    this.isMultistageList&&this.lastIsMultistageList?pNameArr.push(v.pName):(pNameArr = [v.pName]);
                    pNameArr2.push(v.pName2);
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
                            if(v.pName2==pName2&&v.pName==pName){
                                objs[pIndex].list[pIndex2].list.push(v);
                                objs[pIndex].list[pIndex2].name = v.pName2
                                newList.push(v);
                            }
                                
                        })
                    })
                })
                this.checkProductList = newList;
                this.checkProductList.map(h=>{adArr.push(h.id)})
                this.productClassIdLists = adArr;
                this.newProducLists = objs;
                this.infoClassifyVisible = false;

            },
            /*
        * 分类树结构点击筛选出所选择目录下的分类，
        * i: 所属分类树的层级；
        * index: 所选择当前分类层级下的下标；
        * 在i=3的时候（第三层级），创建一个数组this.productClassIdLists列表用于存储将要显示在页面上的品牌类和名称
        * 在i=5的是后（最后一层），把已选品牌下的种类名称添加到this.productClassIdLists下每个对应品牌下的list
        * */
            selectType(index, i) {
                this.producListNum[i-1] = index;
                if (i == 1) {
                    if (this.indexList.index1 != index) {
                        this.productCategorylist2 = [];
                        this.productCategorylist3 = [];
                        this.productCategorylist4 = [];
                        this.productCategorylist5 = [];
                    }
                    this.indexList.index1 = index;
                    this.productCategorylist2 = this.productCategorylist1[index].children
                }
                if (i == 2) {
                    if (this.indexList.index2 != index) {
                        this.productCategorylist3 = [];
                        this.productCategorylist4 = [];
                        this.productCategorylist5 = [];
                    }
                    this.indexList.index2 = index;
                    this.lastNameArr = this.productCategorylist2[index].name;
                    this.productCategorylist3 = this.productCategorylist2[index].children
                    this.isMultistageList = this.productCategorylist2[index].isMultiSelect?true:false;
                }
                if (i == 3) {
                    if (this.indexList.index3 != index) {
                        this.productCategorylist4 = [];
                        this.productCategorylist5 = [];
                    }
                    this.indexList.index3 = index;
                    let newArr = [],delItem = [],saveItem = [];
                    this.checkProductList.map(item=>{
                        let fl = (item.pIndex2||item.pIndex2==0)&&this.productCategorylist2[item.pIndex2].id==this.productCategorylist3[index].pid&&index!=item.pIndex3;
                        fl?delItem.push(item):saveItem.push(item);
                    });
                    this.checkProductList = saveItem;
                    this.productCategorylist4 = this.productCategorylist3[index].children
                }
                if (i == 4) {
                    // if (this.indexList.index4 != index) {
                    //     this.productCategorylist5 = [];
                    // }
                    this.indexList.index4 = index;
                    this.productCategorylist5 = this.productCategorylist4[index].children;
                }
                
                if (i == 5) {

                }
            },
            /*点击添加，弹出产品分类信息弹窗*/
            productCategoryTree() {
                this.infoClassifyVisible = true;
                this.API.AddProductCategory().then((res) => {
                    if (res.data) {
                        res.data.map((a,ai)=>{
                            a.children?a.children.map(b=>{b.children?b.children.map(c=>{c.children?c.children.map(d=>{d.children?d.children.map(e=>{e.pName=b.name;e.pName2=d.name;}):''}):''}):''}):'';
                        })
                        this.productCategorylist1 = res.data;
                    }
                })
            },
            showUpImg() {
                this.isUpImg = false;
                this.addModelVisibleTxt = '添加模型';
            },
            /*上传贴图表格*/
            upImg() {},
            handleAvatarSuccess(res, file) {
                this.popupImageUrl = URL.createObjectURL(file.raw);
            },
            verifyLength() {
                //  let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
                //   if (!i.test(this.ruleForm.productLength)) {
                //     this.productLengthTxt = '请输产品长度为正整数或小数点后两位数';
                //     this.lengthWhos = true;
                //      this.ruleForm.productLength = '';
                //   } else {
                //     this.lengthWhos = false
                //   }

                if(!PointNum(this.ruleForm.productLength)&&this.ruleForm.productLength){
                    this.productLengthTxt = '请输产品宽度为正整数';
                    this.lengthWhos = true;
                    this.ruleForm.productLength = '';
                }else{
                    this.lengthWhos = false;
                }
            },
            verifyWidth () {
                //  let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
                // if (!i.test(this.ruleForm.productWidth)) {
                //  this.productWidthTxt = '请输产品宽度为正整数或小数点后两位数';
                //   this.widthWhos = true;
                //     this.ruleForm.productWidth = '';
                // } else {
                //   this.widthWhos = false;
                // }

                if(!PointNum(this.ruleForm.productWidth)&&this.ruleForm.productWidth){
                    this.productWidthTxt = '请输产品宽度为正整数';
                    this.widthWhos = true;
                    this.ruleForm.productWidth = '';
                }else{
                    this.widthWhos = false;
                }

            },
            verifyHeight () {
                // let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
                // if (!i.test(this.ruleForm.productHeight)) {
                //  this.productHeightTxt = '请输产品高度为正整数或小数点后两位数';
                //   this.heightWhos = true;
                //    this.ruleForm.productHeight   = '';
                // } else {
                //   this.heightWhos = false
                // }
                if(!PointNum(this.ruleForm.productHeight)&&this.ruleForm.productHeight){
                    this.productHeightTxt = '请输产品高度为正整数';
                    this.heightWhos = true;
                    this.ruleForm.productHeight   = '';
                }else {
                    this.heightWhos = false;
                }
            },
            verifybedsideDimensions () {
                if(!PointNum(this.ruleForm.bedsideDimensions)&&this.ruleForm.bedsideDimensions){
                    this.bedsideDimensionsTxt = '请输床沿尺寸为正整数';
                    this.bedsideDimensionsWhos = true;
                    this.ruleForm.bedsideDimensions   = '';
                }else {
                    this.bedsideDimensionsWhos = false;
                }
            },
            verifydecorationPrice () {
                if(!PointTwo(this.decorationPrice)&&this.decorationPrice){
                    this.decorationPriceTxt = '请输装修单价为正整数或小数点后两位数';
                    this.decorationPriceWhos = true;
                    this.decorationPrice   = '';
                }else {
                    this.decorationPriceWhos = false;
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handleSelectionChange2(val){ //硬装全选选择

            },
            deleteAreaTexture(item, j) {
                item.areaTextures.splice(j, 1)
            },
            resetModelCondition() {
                this.assModelDia.query = {
                    limit: 10,
                    isUsed: 0,
                    companyId: sessionStorage.getItem('companyID'),
                    transStatus: 'SUCCESS'
                }
            },
            resetTextrue() {
                this.assModelTextureDia.query = {
                    page: 0,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                }
            },
            resetTextrue2() {
                this.assTextureDia.query = {
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                }
            },
            handleRemoveTexture() {
                this.relateTexture = {}
            },
            // 关闭弹窗时关闭清空选择贴图
            clearSelectModelTexture(){
                this.modelTexture.tempSelectedModelTextures = []
            },
            // 删除模型
            removeModel() {
                this.relateModelInfo = {}
                this.modelId = 0
            },
            resetSearch() { // 关闭对话时清空搜索条件内容
                this.assTextureDia.query.textureAttr = ''
                this.assTextureDia.query.texture = ''
                this.assTextureDia.query.textureName = ''
                this.assTextureDia.query.textureCode = ''
                this.assTextureDia.query.textureType = ''
                this.assTextureDia.query.modelNumber = ''
            },
            resetSearchModel() {
                // this.assModelDia.query = {}
                this.assModelDia.query.modelCode = ''
                this.assModelDia.query.modelModelNum = ''
                this.assModelDia.query.modelName = ''
            },
            toback() {
                let back_url = localStorage.getItem('from');
                this.$router.push({path: back_url})
            },
            parsePercentage(val) {
                return parseInt(val, 10)
            },
            selectedChk(id) {
                return this.modelTexture.tempSelectedModelTextures.includes(id)
            },
            onImgVisible() {
                this.addImgForm.textureType = '';
                this.addImgForm.materials = '';
                this.addImgForm.modelNumber = '';
                this.addImgForm.surface = '';
                this.addImgForm.textureCode = '';
                this.addImgForm.textureName = '';
            },
            // 搜索贴图
            seekIng(page) {
                this.addTextureLoading = true;
                let obj = {
                    textureType: (this.addImgForm.textureType && this.addImgForm.textureType.length > 0) ? this.addImgForm.textureType : undefined,
                    //   materials: (this.addImgForm.materials) ? this.addImgForm.materials : undefined,
                    texture: (this.addImgForm.materials) ? this.addImgForm.materials : undefined,
                    modelNumber: (this.addImgForm.modelNumber && this.addImgForm.modelNumber.length > 0) ? this.addImgForm.modelNumber : undefined,
                    textureAttr: (this.addImgForm.surface) ? this.addImgForm.surface : undefined,
                    textureCode: (this.addImgForm.textureCode && this.addImgForm.textureCode.length > 0) ? this.addImgForm.textureCode : undefined,
                    textureName: (this.addImgForm.textureName && this.addImgForm.textureName.length > 0) ? this.addImgForm.textureName : undefined,
                    limit: 10,
                    page:page,
                    companyId: sessionStorage.getItem('companyID'),
                    queryType: 'success'
                }
                this.API.ListTexture(obj).then((res) => {
                    if (res) {
                        this.addTextureLoading = false;
                        this.total = res.total;
                        this.imgInfoList = res.list;
                    }else{
                        this.addTextureLoading = false;
                    }
                })
            },
            // 选择贴图
            selectImg(scope) {
                if (this.popupType == '贴图') {
                    this.SKUInfo[this.SKUIndex].textureId = scope.row.textureId;
                    this.SKUInfo[this.SKUIndex].thumbnailPath = scope.row.thumbnailPath;
                }
                if (this.popupType == '模型') {
                    let obj = {
                        affectPrice: 0,
                        id: scope.row.textureId,
                        picPath: scope.row.thumbnailPath
                    }
                    this.modelImgList[this.uploadImgIndex].areaTextures.push(obj);
                }
                this.imgVisible = false;
            },
            // 设置当前图片为默认图
            selectImage(index, imgIndex) {
                this.modelImgList[index].defalutTextureId = this.modelImgList[index].areaTextures[imgIndex].id;
            },
            //删除贴图
            deleteImg(index,imgIndex){
                let i = this.modelImgList[index].areaTextures.indexOf(this.modelImgList[index].areaTextures[imgIndex])
                this.modelImgList[index].areaTextures.splice(i, 1)
            },
            // 模型添加贴图
            uploadIndex(index) {
                this.uploadImgIndex = index;
                this.imgVisible = true;
                this.assTextureDia.query.queryType = 'success'
                this.API.ListTexture(this.assTextureDia.query).then((res) => {
                    if (res) {
                        this.total = res.total;
                        this.imgInfoList = res.list;
                    }
                });
                this.API.ProductTextureDrop().then((res) => {
                    this.materials = res.data;
                });
                this.API.textureAttr().then((res) => {
                    this.surface = res.data;
                })
            },
            /*确认使用模型*/
            affirmModel(scope) {
                if (scope == '取消') {
                    this.isAddModel = true;
                    return;
                }
                this.showAddModel(scope);
                this.SKUInfo[this.SKUIndex].modelId = this.modelImgInfo.modelId;
                this.SKUInfo[this.SKUIndex].thumbnailPath = this.modelImgInfo.thumbPicPath
                let arr = [];
                for (let i = 0; i < this.modelImgList.length; i++) {
                    for (let j = 0; j < this.modelImgList[i].areaTextures.length; j++) {
                        let obj = {
                            affectPrice: this.modelImgList[i].areaTextures[j].affectPrice,
                            areaId: this.modelImgList[i].id,
                            isDefault: this.isDefaultList[i],
                            textureId: this.modelImgList[i].areaTextures[j].id
                        };
                        arr.push(obj);
                    }
                }
                this.SKUInfo[this.SKUIndex].modelTextureInfos = arr;
                this.addModelVisible = false;
                this.isAddModel = true;

            },
            /*选用模型打开关联贴图，取消回到模型表格*/
            showAddModel(scope) {
                this.isAddModel = false;
                this.isDefaultList = [];
                this.modelImgInfo = {
                    thumbPicPath: scope.row.thumbPicPath,
                    modelCode: scope.row.modelCode,
                    modelName: scope.row.modelName,
                    modelId: scope.row.modelId
                };
                // this.API.modelTextureDetail({modelId: scope.row.modelId}).then((res) => {
                //     if (res.data) {
                //         this.modelImgList = res.data;
                //         for (let i = 0; i < this.modelImgList.length; i++) {
                //             this.isDefaultList.push(1);
                //         }
                //     }
                // });
            },
            // 搜索模型
            seekModel() {
                let obj = {
                    modelCode: (this.popupModelCode && this.popupModelCode.length > 0) ? this.popupModelCode : undefined,
                    modelModelNum: (this.popupModelNum && this.popupModelNum.length > 0) ? this.popupModelNum: undefined,
                    modelName: (this.popupModelName && this.popupModelName.length > 0) ? this.popupModelName : undefined,
                    isUsed: 0,
                    companyId: sessionStorage.getItem('companyID'),
                    modelType: this.selectCategoryCode,
                    // categoryCodes: 'dis',
                    transStatus: 'success'
                };
                this.API.ListModel(obj).then((res) => {
                    let arr = [];
                    for (let i = 0; i < this.SKUInfo.length; i++) {
                        if (this.SKUInfo[i].modelId != '') {
                            arr.push(this.SKUInfo[i].modelId);
                        }
                    }
                    let arrList = res.list;
                    for (let i = 0; i < arrList.length; i++) {
                        for (let j = 0; j < arr.length; j++) {
                            if (arrList[i].modelId == arr[j]) {
                                arrList[i].isShow = 1;
                            }
                        }
                    }
                    this.modelTable = arrList;
                    this.modelTotal = res.total
                });
            },
            // SKU添加贴图
            SKUTexture(scope, type) {
                this.popupType = type;
                this.SKUIndex = scope.$index;
                if (type == '模型') {
                    this.addModelVisible = true;
                    let obj = {
                        isUsed: 0,
                        companyId: sessionStorage.getItem('companyID'),
                        modelType: this.selectCategoryCode,
                        // categoryCodes: 'dis',
                        transStatus: 'success'
                    };
                    let arr = [];
                    for (let i = 0; i < this.SKUInfo.length; i++) {
                        if (this.SKUInfo[i].modelId != '') {
                            arr.push(this.SKUInfo[i].modelId);
                        }
                    }
                    this.API.ListModel(obj).then((res) => {
                        let arrList = res.list;
                        for (let i = 0; i < arrList.length; i++) {
                            for (let j = 0; j < arr.length; j++) {
                                if (arrList[i].modelId == arr[j]) {
                                    arrList[i].isShow = 1;
                                }
                            }
                        }
                        this.modelTable = arrList;
                        this.modelTotal = res.total
                    });
                }
                if (type == '贴图') {
                    this.addTextureLoading = true;
                    this.imgVisible = true;
                    this.assTextureDia.query.queryType = 'success'
                    this.API.ListTexture(this.assTextureDia.query).then((res) => {
                        if (res) {
                            this.total = res.total;
                            this.imgInfoList = res.list;
                            this.addTextureLoading = false;
                        }else{
                            this.addTextureLoading = false;
                        }
                    });
                    this.API.ProductTextureDrop().then((res) => {
                        this.materials = res.data;
                    });
                    this.API.textureAttr().then((res) => {
                        this.surface = res.data;
                    })
                }
            },
            //添加水边材质图
            addImg(type){
                this.popupType2 = type
                this.handleAssocateTexture();
            },
            //删除水边材质图
            delImg(type){
                this.shuibian[type] = {};
            },
            productCeilingApplyArea(){
                this.API.productCeilingApplyArea().then(res=>{
                    if(res.code==200){
                      this.applyAreaList = res.data
                    }
                })
            },
            // 关联贴图
            handleAssocateTexture() {
                this.dialogAddTextureVisible = true
                this.textureLoading = true
                this.assTextureDia.query.queryType = 'success'
                this.assTextureDia.query.page = 1
                this.assTextureDia.query.limit = 10
                this.API.ListTexture(this.assTextureDia.query).then((res) => {
                    if (res) {
                        this.textureLoading = false
                        this.TextureListData = res.list ? res.list : []
                        this.textureTotal = res.total
                    }
                    if (res.code == 204) {
                        this.textureLoading = false
                    }
                })
            },
            // 选用贴图
            handleAssocateTextureConfirm(row) {
                this.dialogAddTextureVisible = false
                if(this.popupType2){
                    this.shuibian[this.popupType2] = {src:row.thumbnailPath,id:row.textureId}
                    this.popupType2 = '';
                    return;
                }   
                
                this.relateTexture = Object.assign({}, {isShow: true}, row)
                // this.SKUInfo[this.SKUIndex].textureId = this.relateTexture.textureId;
                // this.SKUInfo[this.SKUIndex].thumbnailPath = this.relateTexture.thumbnailPath;
            },
            // 材质材料
            getProductTextureDrop() {
                this.API.textureCaiLiao().then((res) => {
                    this.ProductTextureDrop = res.data
                })
            },
            // 贴图表面属性
            textureAttrDropList() {
                this.API.textureAttr().then((res) => {
                    this.textureAttrDrop = res.data
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
            handleAddArea() { // 添加区域
                // this.area.push([])
            },
            handleAddModelTexture(area, index) {
                this.dialogModelTextureVisible = true
                this.modelTexture.areaId = area.id
                this.modelTexture.index = index
                this.assModelTextureDia.query.queryType = 'success'
                this.API.ListTexture(this.assModelTextureDia.query).then((res) => {
                    this.TextureListData = res.list ? res.list : [];
                })
//        this.area[index].push({price: 10, defaultThumb: true})
            },
            handleChange(value) {
            },
            selectPropValues(val, index, item) {
                this.productPropBO[index] = val
                this.productPropBO[index].parent = {
                    code: item.code,
                    id: item.id,
                    name: item.name
                }
            },
            // 获取产品属性
            getProductProps() {
                let arr= [];
                this.infoCheckList = [];
                this.selectedProps = []
                if (this.selectCategoryCode) {
                    this.API.ProductProps({
                        code: this.selectCategoryCode
                    }).then((res) => {
                        this.getProductPorps = res.data;
                        this.$store.dispatch('saveProductPorps',res.data);
                        for (let i = 0; i < res.data.length; i++) {
                            this.infoCheckList.push(arr);
                        }
                        if (res.data.length == 0) {
                            this.isModel = true;
                        } else {
                            this.isModel = false;
                        }
                    })
                }
            },
            /* 上传图片 */
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
            handleItemChange(val) {
//                console.log('kkkkk='+val);
            },
            // 产品分类编号
            selectCategoryNO(val) {
                console.log('-----'+val);
                this.$store.dispatch('setHardModel',[]);
                this.SKUInfo = [];
                this.selectCategoryCode = val[val.length - 1];
                let filter = this.unitList.filter(e => {
                    if (e.suitableCodes)
                        return e.suitableCodes.indexOf(this.selectCategoryCode) != -1;
                });
                this.selectSaleUnionId = filter.length?filter[filter.length-1].id : this.unitList[0].id;
                this.selectSaleUnionName = filter.length?filter[filter.length-1].name : this.unitList[0].name;
                var productTypeValuekey = val[val.length - 2];
                // this.modelAndTexture()
                this.productPropBO = []
                this.isModelImg();
                this.getProductProps()
                this.relateModelInfo = {
                    isShow: false
                }
                this.relateTexture = {
                    isShow: false
                }
                this.chageBedsideDimensions(productTypeValuekey);
                switch(this.ruleForm.productCategoryForm[2]){
                    case 'jiem':
                        this.productTypes = 1;
                        break;
                    case 'ctqki':
                        this.productTypes = 2;
                        this.resetType2();
                        break;
                    default :
                        this.productTypes = 0;
                        this.resetType2();
                        break;
                }

                this.valuationUnit = "元/个";
                this.unitList.map(res=>{
                    if (res.suitableCodes && res.suitableCodes.indexOf(this.selectCategoryCode + ",") != -1) {
                        this.valuationUnit = res.name;
                    }
                });

            },
            resetType2(){
                    this.ruleForm.ceilingCrossSectionDataDTOList = [
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
            // 床沿尺寸
            chageBedsideDimensions(val){
                if(val != null && val == 'bd'){
                    this.isBedsideDimensions = true;
                }else{
                    this.ruleForm.bedsideDimensions = '';
                    this.isBedsideDimensions = false;
                }
            },
            // 判断模型和贴图
            isModelImg() {
                this.API.isModel({code: this.selectCategoryCode}).then((res) => {
                    this.isImg = res.data;
                });
            },
            //  产品风格
            selectFenge(val) {
                this.selectFengeId = val
            },
            handleAvatarSuccess(res, file) {
                // this.imageUrl = URL.createObjectURL(file.raw);
                this.fileList2.push({
                    url: res.data.url,
                    id: res.data.resId
                })
                this.onePic = this.fileList2[0].id
            },
            deleteUploadImg(index) {
                this.fileList2.splice(index, 1)
            },
            showDelete(index) {
                this.$set(this.showDeletes, index, true)
            },
            showDelete2(index) {
                this.$set(this.showDeletes, index, false)
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

            submitForm(formName) {
                this.getHardParam();
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let modelTextureInfos = []
                        this.area.forEach(it => {   // 组装后台需要的材质参数
                            it.areaTextures&&it.areaTextures.forEach((tit, index) => {
                                modelTextureInfos.push(
                                    {
                                        affectPrice: tit.affectPrice,
                                        areaId: it.id,
                                        isDefault: it.isDefault === index ? 1 : 0,
                                        textureId: tit.textureId
                                    }
                                )
                            })
                        })
                        let productSKUInfos = [];
                        for (let i = 0; i < this.SKUInfo.length; i++) {
                            let propIdsObj={
                                propIds: this.SKUId[i],
                                modelId: (this.SKUInfo[i].modelId && this.SKUInfo[i].modelId != '') ? this.SKUInfo[i].modelId : null,
                                modelTextureInfos: (this.SKUInfo[i].modelTextureInfos && this.SKUInfo[i].modelTextureInfos.length > 0) ? this.SKUInfo[i].modelTextureInfos : null,
                                textureId: (this.SKUInfo[i].textureId && this.SKUInfo[i].textureId != '') ? this.SKUInfo[i].textureId : null,
                            };
                            productSKUInfos.push(propIdsObj);
                        }
                        let formDatas = {
                            categoryIds: this.productClassIdLists, // 运营分类信息
                            productSKUInfos: sessionStorage.getItem('addProductType')=='hard' ? this.gettersSKUInfos : productSKUInfos, // SKU信息
                            addProductType:sessionStorage.getItem('addProductType'),
                            modelId: this.modelId,
                            advicePrice: this.advicePriceInput1, // (number, optional): 建议售价 ,
                            modelingId: this.selectStyleId, //  (integer, optional): 产品款式 ,
                            brandId: this.selectedBrandId, //  (integer): 品牌ID ,
                            productSmallType: this.selectCategoryCode, //  (string): 所属分类编码 ,
                            categoryCode: this.selectCategoryCode, //  (string): 所属分类编码 ,
                            code: '', //  (string): 产品编码 ,
                            desc: this.prodTextarea, //  (string, optional): 产品描述 ,
                            modelNumber: this.ruleForm.productTypeCodeStr, //  (string, optional): 产品型号 ,
                            name: this.ruleForm.productNameStr, //  (string): 产品名称 ,this.productName
                            length: this.ruleForm.productLength, // 长
                            width: this.ruleForm.productWidth, // 宽
                            height: this.ruleForm.productHeight, // 高
                            proCtopsBO:{},
                            platformType: this.distrAllotList.map(type => type).join(','), //  (string, optional): (多选框)要分配的平台类型;渠道:2b/线上:2c ,
                            price: this.advicePriceInput2, //  (number, optional): 售价 ,
                            decorationPrice: this.decorationPrice, //  装修单价 ,
                            userId: sessionStorage.getItem('userId'),
                            saleUnionId: this.selectSaleUnionId, //  (integer, optional): 售价单位 ,
                            seriesId: this.selectedSeriesId, //  (integer, optional): 产品系列 ,
                            baseStyleId: this.selectFengeId, //  (integer, optional): 产品风格
                            picIds: this.fileList2.map(item => item.id).join(','),
                            defaultPicId: this.defaultPicId || this.onePic,  // 没有设置主缩略图时 this.fileList2[0].id 默认第一张为主缩略图
                            modelTextureInfos: modelTextureInfos,
                            textureId: this.relateTexture.textureId,
                            props: this.productPropBO,
                            productSpec:this.ruleForm.productSpec,
                            bedsideDimensions:this.ruleForm.bedsideDimensions,
                            isSpecialEffects:this.ruleForm.isSpecialEffects ? 1 : 0
                        }
                        
                        formDatas.proCtopsBO = {
                            dataTypeForWall:this.ruleForm.crossSectionDataForWall?1:0,
                            dataTypeForOut:this.ruleForm.crossSectionDataForOut?2:0,
                            textureIdForWall:this.shuibian.wall.id,
                            textureIdForOut:this.shuibian.out.id,
                            crossSectionDataForWall:this.ruleForm.crossSectionDataForWall,
                            crossSectionDataForOut:this.ruleForm.crossSectionDataForOut,
                        }
                        let newObj = [];
                        if(this.ruleForm.ceilingCrossSectionDataDTOList){
                            this.ruleForm.ceilingCrossSectionDataDTOList.map((v,i)=>{
                                if(v.applyArea){
                                    let arr = [];
                                    v.applyArea.map(b=>{
                                        this.applyAreaList[0].map(a=>{if(a.id==b){arr.push(a.name)}})
                                    })
                                    newObj[i] = { ceilingInfo:v.ceilingInfo,lightInfo:v.lightInfo, applyArea:arr.join(',')}
                                }
                            })
                        }
                        formDatas.ceilingCrossSectionDataDTOList = newObj;
                        for (let key in formDatas) {
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

                        let jsonData = JSON.stringify(formDatas)
                        this.$confirm('确定录入产品?', '提示', {
                            confirmButtonText: '确定',
                            type: 'info',
                            cancelButtonClass: 'cancelButtonClass',
                            confirmButtonClass: 'confirmButtonClass',
                            center: true
                        }).then(() => {
                             this.loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                    });
                            this.API.AddProduct(jsonData).then((res) => {
                                this.loading.close()
                                if (res && res.code == 200) {
                                    this.$message.success('产品录入成功')
                                    this.$router.replace('/product')
                                   
                                } else {
                                    this.$message.error(res.message)
                                }
                            })
                        })
                    } else {
                        return false
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
                this.$router.push('/product')
            },
            // 产品分类下拉框
            getProductCategory() {
                // this.$nextTick(() => {
                //     this.productCategoryDrop = this.cases
                // })
                this.API.ProductCategory().then((res) => {
                    this.productCategoryDrop = res.data
                })
            },
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
            // 模型和贴图
            modelAndTexture() {
                if (this.selectCategoryCode) {
                    this.API.modelTexture({
                        code: this.selectCategoryCode
                    }).then((res) => {
                        if (res.data === 1) {
                            this.isModel = true
                        } else {
                            this.isModel = false
                        }

                        if (res.data === 2) {
                            this.isTexture = true
                        } else {
                            this.isTexture = false
                        }
                    })
                }
            },
            showModelList() {
                this.dialogModelVisible = true
                this.assModelDia.query.categoryCodes = this.selectCategoryCode
                this.assModelDia.query.modelType = this.selectCategoryCode
                this.modelListLoading = true
                this.API.ListModel(this.assModelDia.query).then((res) => {
                    if (res) {
                        this.modelListLoading = false
                        this.modelListData = res.list
                    }
                    if (res.code == 204) {
                        this.modelListLoading = false
                    }
                })
            },
            searchTextureImg(type) {
                type==1 ? this.page=1:'';
                this.assModelTextureDia.query.queryType = 'success';
                this.API.ListTexture(this.assModelTextureDia.query).then((res) => {
                    this.TextureListData = res.list ? res.list : []
                })
            },
            // 模型选用之后吧模型区域和模型材质放入
            modelSelectUse(row) {
                if (row.modelId) {
                    this.modelId = row.modelId;
                    this.dialogModelVisible = false
                    this.relateModelInfo.isShow = true
                    this.relateModelInfo.modelName = row.modelName
                    this.relateModelInfo.modelCode = row.modelCode
                    this.relateModelInfo.picPath = row.thumbPicPath
                    this.relateModelInfo.length = row.length;
                    this.relateModelInfo.width = row.width;
                    this.relateModelInfo.height = row.height;
                    // this.API.modelTextureDetail({
                    //   modelId: row.modelId
                    // }).then((res) => {
                    //   this.area = res.data.map(it => Object.assign({}, it, {
                    //     isDefault: 0,
                    //     areaTextures: it.areaTextures.map(tit => Object.assign({}, {
                    //       thumbnailPath: tit.picPath,
                    //       deleteBtnVisible: false,
                    //       textureId: tit.id,
                    //     }, tit))
                    //   }))
                    //   this.dialogModelVisible = false
                    //   this.relateModelInfo.isShow = true
                    //   this.relateModelInfo.modelName = row.modelName
                    //   this.relateModelInfo.modelCode = row.modelCode
                    //   this.relateModelInfo.picPath = row.thumbPicPath
                    //   this.relateModelInfo.length = row.length;
                    //   this.relateModelInfo.width = row.width;
                    //   this.relateModelInfo.height = row.height;
                    // })
                }
            },
            // 贴图选用
            textureSelectUse(row) {
                if (row.modelId) {
                    this.API.modelTextureDetail({
                        modelId: row.modelId
                    }).then((res) => {
                        this.area = res.data
                        this.dialogAddTextureVisible = false
                    })
                }
            },
            removeTag(v){
                this.applyAreaList.map(a=>{
                    a.map(h=>{ 
                        if(h.id==v) {
                            h.disabled = false; 
                        }
                            
                    })
                })
            },
            applyChange(i){
                let list = this.ruleForm.ceilingCrossSectionDataDTOList;
                let item = list[0].applyArea.concat(list[1].applyArea,list[2].applyArea);
                this.applyAreaList.map((a,ai)=>{
                    if(i!=ai){
                        a.map(n=>{
                            n.disabled = false
                            item.map(v=>{ 
                                if(v==n.id)  n.disabled = true;
                            })
                        })
                    }
                })
            },
            disabledFun(index){
                let flag = !this.ruleForm.ceilingCrossSectionDataDTOList[index].ceilingInfo;
                flag?this.ruleForm.ceilingCrossSectionDataDTOList[index].applyArea=[]:'';
                this.disabled[index] = flag
                this.$set(this.ruleForm.ceilingCrossSectionDataDTOList,index,this.ruleForm.ceilingCrossSectionDataDTOList[index]);
                this.$set(this.disabled,index,this.disabled[index]);
            }

        },
        
        created() {
            this.editProID = this.$route.params.proID
            this.getProductTextureDrop()
            this.textureAttrDropList()
            this.modelAndTexture()
            this.getProductCategory()
            this.getProductBrand()
            this.getProductSeriesList()
            this.getProductStyleDrop()
            this.getBaseProductStyleDrop()
            this.getDisctionaryUnit()
            this.getRecommand()
            //this.productCeilingApplyArea();
        },
        mounted(){
            this.restaurants = this.loadAll();
            //拖拽事件
            this.$dragging.$on('dragged', ({ value }) => { //拖拽事件 触发
                this.defaultPicId=value.list[0].id;
            })

        },
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";
    .nonImg{
        width:90px;
        height: 90px;
        line-height: 90px;
        text-align: center;
        background-color: #f5f5f5;
        display: inline-block;
        margin-right: 10px;
    }
    .model-img-txt{
        .el-form-item__label{

        }
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
    .add-model-ypop-up{
        .model-list{
            .el-col-12{
                margin-bottom: 20px;
                .el-input{
                    width: 220px;
                    margin-left: 10px;
                }
                .el-button{
                    width: 133px;
                    height: 40px;
                    margin-left: 70px;
                    background-color: #ff6419;
                    color: #fff;
                    font-size: 16px;
                }
            }
            .el-table{
                max-height: 460px;
                overflow: auto;
                .model-choose{
                    border:none;
                    background-color: #ff6419;
                    color: #fff;
                }
                .model-choose1{
                    border:none;
                    background-color: #999999;
                    color: #fff;
                }
            }
        }
        .img-list{
            max-height: 520px;
            overflow: auto;
            background-color: #fafafa;
            padding-top: 10px;
            .first{
                border: 1px solid #dddddd;
                padding: 20px;
                display: flex;
                justify-content: left;
                margin-bottom: 20px;
                img{
                    width: 90px;
                    height: 90px;
                    margin-right: 20px;
                }
                div{
                    p{
                        height: 36px;
                        line-height: 36px;
                        margin-top: 5px;
                        color: #666666;
                        span{
                            color: #333333;
                            margin-left: 10px;
                        }
                    }
                }
            }
            .el-col-24{
                background-color: #fff;
                margin-top: 10px;
                border: 1px solid #dddddd;
                h1{
                    height: 30px;
                    font-size: 14px;
                    line-height: 30px;
                    padding-left: 20px;
                    border-bottom: 1px solid #dddddd;
                }
                .up-img{
                    display: flex;
                    justify-content: left;
                    padding: 10px 20px;
                    .img-price{
                        background-color: #fafafa;
                    }
                    .first-up-img{
                        width: 90px;
                        height: 118px;
                        border: 1px solid #dddddd;
                        overflow: hidden;
                        margin-right: 20px;
                        p{
                            height: 20px;
                            line-height: 28px;
                            text-align: center;
                        }
                        div{
                            width: 90px;
                            height: 90px;
                            box-sizing: border-box;
                            border-bottom: 1px solid #dddddd;
                            overflow: hidden;
                            .add-img{
                                display: block;
                                width: 21px;
                                height: 20px;
                                margin: 26px auto 5px;
                                background: url("../../../assets/images/icons/uploadPlug.png");
                            }
                        }

                    }
                    .item-img{
                        width: 90px;
                        height: 118px;
                        margin-left: 10px;
                        border: 1px solid #dddddd;
                        overflow: hidden;
                        position: relative;
                        .img-price1{
                            cursor: pointer;
                            width: 100%;
                            height: 20px;
                            position: absolute;
                            top: 70px;
                            color: #ffffff;
                            background-color: #333333;
                            line-height: 20px;
                            text-align: center;
                            font-size: 12px;
                        }
                        .img-price{
                            width: 100%;
                            height: 20px;
                            position: absolute;
                            top: 70px;
                            color: #ffffff;
                            background-color: #333333;
                            line-height: 20px;
                            text-align: center;
                            font-size: 12px;
                        }
                        .img-delete{
                            position: absolute;
                            top: 35px;
                            left: 35px;
                            width: 20px;
                            height: 20px;
                            background: url("../../../assets/images/icons/delete3.png") no-repeat;
                            background-size: cover;
                        }
                        img{
                            display: block;
                            width: 90px;
                            height: 90px;
                            box-sizing: border-box;
                            border-bottom: 1px solid #dddddd;
                        }
                    }
                }
            }
        }
        .row-footer{
            padding-top: 20px;
            .el-col-24{
                display: flex;
                justify-content: flex-end;
                .add-model-confirm{
                    background-color: #ff6419;
                    color: #fff;
                    border: none;
                }
                .add-model-cancel{
                    background-color: #ffdbc9;
                    color: #ff6419;
                    border: none;
                }
            }
        }
        .add-img-box{
            .el-form-item{
                .el-select{
                    width: 200px;
                }
                .seek{
                    width: 133px;
                    height: 40px;
                    margin-left: 100px;
                    margin-right: 20px;
                    font-size: 16px;
                }
                .reset{
                    font-size: 16px;
                    color: #ff6419;
                }
            }
            .el-table{
                max-height: 350px;
                overflow: auto;
            }
        }
        .add-model-box{
            .progress-bar{
                position: relative;
                width: 100%;
                margin-bottom: 20px;
                .model-status{
                    display: inline-block;
                    margin-left: 80px;
                    width: 30px;
                    height: 30px;
                    background: url("../../../assets/images/icons/status2.png");
                }
                .model-status-active{
                    display: inline-block;
                    margin-left: 80px;
                    width: 30px;
                    height: 30px;
                    background: url("../../../assets/images/icons/status1.png");
                }
                .link-line{
                    position: absolute;
                    top: 12.5px;
                    left: 110px;
                    display: inline-block;
                    width: 400px;
                    height: 3px;
                    background-color: #dddddd;
                }
                .link-line-active{
                    position: absolute;
                    top: 12.5px;
                    left: 110px;
                    display: inline-block;
                    width: 400px;
                    height: 3px;
                    background-color: #ff6419;
                }
                .img-status{
                    display: inline-block;
                    margin-left: 395px;
                    width: 30px;
                    height: 30px;
                    background: url("../../../assets/images/icons/status1.png");
                }
                .img-status-active{
                    display: inline-block;
                    margin-left: 395px;
                    width: 30px;
                    height: 30px;
                    background: url("../../../assets/images/icons/status2.png");
                }
                p{
                    color: #333333;
                    .model-txt{
                        margin-left: 65px;
                    }
                    .img-txt{
                        margin-left: 370px;
                    }
                }
            }
        }
    }
    .model-datails-ypop-up{
        .el-button{
            background-color: #ffdbc9;
            border: none;
            color: #ff6419;
        }
        .el-row{
            max-height: 450px;
            overflow: auto;
        }
        .el-col-24{
            display: flex;
            justify-content: left;
            width: 100%;
            border: 1px solid #dddddd;
            margin-top: 20px;
            padding: 20px 0;
            div{
                width: 90px;
                height: 110px;
                margin-left: 10px;
                margin-right: 20px;
                p{
                    margin-top: 32px;
                }
            }
            .model-img{
                margin-right: 0;
                img{
                    display: block;
                    width: 90px;
                    height: 90px;
                    border: 1px solid #dddddd;
                    box-sizing: border-box;
                }
                span{
                    display: block;
                    width: 90px;
                    height: 20px;
                    line-height: 20px;
                    text-align: center;
                }
            }
        }
        .model-datails-content{
            display: flex;
            justify-content: left;
            width: 100%;
            border: 1px solid #dddddd;
            padding: 20px 0;
            box-sizing: border-box;
            img{
                margin:0 20px;
                width: 90px;
                height: 90px;
            }
            div{
                margin-top: 10px;
                p{
                    height: 36px;
                    line-height: 36px;
                }
            }
        }
    }
    .img-datails-ypop-up{
        .el-button{
            background-color: #ffdbc9;
            border: none;
            color: #ff6419;
        }
        .img-datails-content{
            display: flex;
            justify-content: left;
            width: 100%;
            img{
                margin:0 20px;
                width: 90px;
                height: 90px;
            }
            div{
                margin-top: 10px;
                p{
                    height: 36px;
                    line-height: 36px;
                }
            }
        }
    }
    .img-ypop-up{
        .el-pagination{
            margin-top: 20px;
            text-align: center;
        }
        .el-form-item{
            display: inline-block;
        }
        .el-input{
            width: 217px;
        }
        .el-button{
            width: 133px;
            height: 40px;
            font-size: 16px;
            font-weight: bold;
        }
        .el-table{
            height: 500px;
            overflow: auto;
            .set-img{
                width: 55px;
                height: 26px;
                font-size: 14px;
                text-align: center;
                background-color: #ff6419;
                border: none;
                color: #fff;
                border-radius: 4px;
            }
            .set-img-active{
                background-color: #ffcab0;
                width: 55px;
                height: 26px;
                font-size: 14px;
                text-align: center;
                border: none;
                color: #fff;
                border-radius: 4px;
            }
        }
        .nonImg{
            width:90px;
            height: 90px;
            line-height: 90px;
            text-align: center;
            background-color: #f5f5f5;
            display: inline-block;
            margin-right: 10px;
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
    .uploadTip {
        color: red;
        margin-left: 100px;
        margin-bottom: 15px;
        margin-top: -16px
    }

    .left {
        position: relative;
        &:before {
            content: '';
            background: #fff;
            position: absolute;
            width: 16px;
            height: 16px;
            bottom: 14px;
        }
    }

    .uploadImgList {
        width: 90px;
        text-align: center;
        margin: 5px 0 5px 20px;
        position: relative;
    }
    .productName{
        margin-left: 40px;
    }


    .selectCHKS {
        margin-top: 20px;
        background: #f2f2f2;
        .item {
            height: 90px;
            width: 90px;
            display: inline-block;
            margin: 10px;
            position: relative;
            .chks {
                opacity: 0;
                border: solid red 1px;
                height: 90px;
                width: 90px;
                z-index: 10;
            }
            .selected {
                position: absolute;
                z-index: 12;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
            }
            .imgPos {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                z-index: 8;
            }
        }
    }

    .productBaseItem {
        .el-form-item {
            margin-bottom: 30px;
        }
    }

    .modelTextureFrame {
        border: solid 1px #e3e3e3;
        border-radius: 4px;
        margin-left: 100px;
        margin-bottom: 30px;

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
        .uploadTexture {
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
                        right: 5px;
                        top: 8px;
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

    .propTitle {
        display: block;
        font-size: 14px;
        padding: 0 15px 15px 30px;
        border-bottom: solid 1px #dedede;
        margin-bottom: 15px;
    }

    .selectAllotFrame {
        border-top: solid 1px #e3e3e3;
        border-bottom: solid 1px #e3e3e3;
        padding-top: 15px;
        margin-bottom: 25px;
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
        .ptop {
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
            .el-col-24{
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
        .goods-operating-info{
            margin-bottom: 20px;
            .title{
                position: relative;
                .star{
                    color: #ff6419;
                    position: absolute;
                    left: -6px;
                }
            }
            .el-col-2{
                font-size: 14px;
                width: 110px;
                text-align: right;
                line-height: 40px;
                margin-left: 20px;
            }
            .el-col-22{
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
            .el-col-24{
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
        .specification-info{
            .SKU-table-header{
                padding: 0;
                height: 30px;
            }
            .el-checkbox-group{
                display: inline-block;
                margin-left: 5px;
            }
            .el-col-24{
                border-top: 1px solid #dddddd;
                height: 74px;
                line-height: 74px;
                h2{
                    display: inline-block;
                    font-size: 14px;
                    width: 110px;
                    text-align: right;
                }
                .el-radio{
                    margin-left: 20px;
                }
            }
            .el-col-2{
                margin-bottom: 20px;
                font-size: 14px;
                width: 110px;
                text-align: right;

            }
            .el-col-21, .el-col-20{
                padding-left: 10px;
                .el-checkbox-group{
                    .el-checkbox{
                        width: 180px;
                        margin: 0 0 20px 0;
                    }
                }
                .el-table{
                    margin: 0 0 20px 0;
                    span{
                        color: #ff6419;
                        cursor:pointer
                    }
                }
            }
        }
        .productBaseInfo {
            height: 40px;
            line-height: 40px;
            background-color: #f6f6f6;
            border-left: solid 4px $leaveButtonStatus;
            color: $color666;
            font-size: 16px;
            padding-left: 10px;
            font-weight: bold;
            margin-bottom: 30px;
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

    .demo-ruleForm {
        .el-select {
            width: 100%;
        }
        .el-cascader {
            width: 100%;
        }
    }

    .avatar {
        width: 90px;
        height: 90px;
        display: block;
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



    .pos {
        margin-bottom: 12px;
        min-height: 80px;
        background: #fff;
        border-radius: 6px;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
        .nav-top {
            margin-top: 35px;
        }

        ul {
            display: flex;
            -webkit-justify-content: space-between;
            justify-content: space-between;
            font-size: 13px;
            color: #626262;
            li {
                padding: 0 20px;
                line-height: 80px;
                font-weight: bold;
                b {
                    display: inline-block;
                    background: $btnOrange;
                    height: 40px;
                    line-height: 40px;
                    padding: 0 20px;
                    color: #fff;
                    border-radius: 20px;
                }
            }
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
    /*上传图片样式 end*/
</style>
<style lang="scss">
    .addProduct{
        .el-upload-list__item img {
            width: 90px;
            height: 90px;
        }

        .modelNameStyle {
            white-space: nowrap;
            height: 50px;
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
            line-height: 88px;
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

    }

</style>

