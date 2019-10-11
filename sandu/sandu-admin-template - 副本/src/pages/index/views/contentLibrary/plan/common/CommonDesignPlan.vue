<template>
    <div>
        <el-row :gutter="24" class="searchCondition">
            <el-col :span="3">
                <contains-not-open-product @change="page"
                                           v-model="query.containsSecrecyFlag"></contains-not-open-product>
            </el-col>
            <el-col :span="3">
                <el-select @change="page" clearable v-model="query.platformId"  placeholder="请选择上架情况">
                    <el-option
                            v-for="(item, index) in shelfData"
                            :key="index"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                    <el-option value="0" label="未上架"></el-option>
                </el-select>
            </el-col>
            <el-col :span="3">
                <el-select @change="page" v-model="query.salePriceChargeType" clearable
                           placeholder="方案售卖价格是否免费">
                    <el-option value="0" label="是"></el-option>
                    <el-option value="1" label="否"></el-option>
                </el-select>
            </el-col>

            <el-col :span="3">
                <el-select @change="page" v-model="query.chargeType" clearable placeholder="方案版权费是否免费">
                    <el-option value="0" label="是"></el-option>
                    <el-option value="1" label="否"></el-option>
                </el-select>
            </el-col>
            <el-col :span="3" v-if="isPer('solution:delivery')">
                <el-select v-model="query.deliverStatus" @change="page" clearable placeholder="请选择交付状态">
                    <el-option value="Y" label="已交付"></el-option>
                    <el-option value="N" label="未交付"></el-option>
                </el-select>
            </el-col>

            <el-col :span="3">
                <el-select v-model="query.origin" @change="page" clearable placeholder="请选择方案来源">
                    <el-option value="deliver" label="企业交付"></el-option>
                    <el-option value="diy" label="内部制作"></el-option>
                    <el-option value="share" label="分享方案"></el-option>
                </el-select>
            </el-col>
            <!-- <el-col :span="3">
                <el-select v-model="query.brandId" @change="page" filterable clearable placeholder="请选择品牌">
                    <el-option v-for="(item,index) in brandDrop"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    >{{item.name}}
                    </el-option>
                </el-select>
            </el-col> -->
            <el-col :span="3">
                <el-input v-model="query.planName" placeholder="请输入方案名称"></el-input>
            </el-col>
            <el-col :span="3"><el-input v-model="query.id" placeholder="请输入方案ID"></el-input></el-col>
            <el-col :span="3">
                <!--<el-input v-model="query.spaceCommonName" placeholder="请输入空间类型"></el-input>-->
                <el-select v-model="query.spaceCommonName" @change="page" clearable placeholder="请选择空间类型">
                    <el-option
                            v-for="item in spaceCommonName"
                            :key="item.id"
                            :label="item.name"
                            :value="item.name">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="3">
                <el-select @change="page" v-model="query.planGroupStyleId" filterable clearable popper-class="plan-style-css" placeholder="请选择方案风格">
                    <!--<el-option-group-->
                            <!--v-for="group in planStyles"-->
                            <!--:key="group.styleName"-->
                            <!--:label="group.styleName" >-->
                        <!--<el-option-->
                                <!--v-for="item in group.styles"-->
                                <!--:key="item.styleId"-->
                                <!--:label="item.styleName"-->
                                <!--:value="item.styleId">-->
                        <!--</el-option>-->
                    <!--</el-option-group>-->
                    <el-option
                            v-for="item in planStyles"
                            :key="item.groupId"
                            :label="item.styleName"
                            :value="item.groupId">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="3">
                <el-select @change="page" v-model="query.designerUserId" filterable clearable popper-class="plan-style-css" placeholder="请选择设计师">
                    <el-option
                        v-for="item in designerList"
                        :key="item.id"
                        :label="item.userName"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6" style="width:460px">
                <el-date-picker
                    v-model="query.startTime"
                    type="date"  
                    :picker-options="pickerOptions(0)"
                    value-format="yyyy-MM-dd"
                    :clearable="!query.endTime"
                    placeholder="上架时间">
                </el-date-picker>
                <span>-</span>
                <el-date-picker
                    v-model="query.endTime"
                    type="date"                            
                    :picker-options="pickerOptions(1)"
                    @change="query.startTime?null:$set(query,'startTime',query.endTime)"
                    value-format="yyyy-MM-dd"
                    placeholder="上架时间">
                </el-date-picker>
            </el-col>
        </el-row>
        <el-row :gutter="24" class="searchBtnFrame">
            <el-button round type="primary" size="small" @click="page">搜索</el-button>
            <el-button round class="searchBtn" size="small" @click="handleClearQuery">重置</el-button>
        </el-row>
        <div class="operationButton">
            <el-button type="text" class="outline" @click="handleBatchAllot" v-if="isPer('solution:exhibition')">批量上架
            </el-button>
            <el-button size="medium" type="text" class="delete" @click="batchDelete" v-if="isPer('solution:del')">批量删除
            </el-button>
            <el-button size="medium" type="text" class="delivery" @click="batchSecrecy" v-if="isPer('solution:manage')">
                批量公开
            </el-button>
            <el-button size="medium" type="text" class="delivery" @click="batchCancelSecrecy"
                       v-if="isPer('solution:manage')">批量取消公开
            </el-button>
            <el-button size="medium" type="text" class="delivery" @click="batchDelivery"
                       v-if="isPer('solution:delivery')">批量交付
            </el-button>
            <el-button size="small" type="text" @click="refreshData" class="refresh">刷新</el-button>
            <el-button size="small" type="text" @click="exportExcel" class="export">导出Excel</el-button>
        </div>
        <div class="planList">
            <el-table
                    :data="list"
                    style="width: 100%;"
                    ref="multipleTable"
                    v-loading="channelPlanCommon"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    @selection-change="handleSelectionChange"
                    tooltip-effect="dark"
                    :default-sort="{prop: 'completeDate', order: 'descending'}"
            >
                <el-table-column align="center" label=" " width="45">
                    <template slot-scope="scope">
                        <span class="fubenImg" v-if="scope.row.isChanged == 0">
                            <img :src="require('../../../../assets/images/fuben.png')">
                        </span>
                    </template>
                </el-table-column>
                <el-table-column
                        type="selection"
                        label="全选"
                        align="center"
                        width="30" title="全选">
                </el-table-column>
                <el-table-column
                        prop="picPath"
                        label="方案缩略图"
                        width="100">
                    <template slot-scope="scope">
                        <div v-if="scope.row.picPath">
                            <img style="cursor:pointer;" @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)"
                                 :src="BASE_URL.sourceBaseUrl+scope.row.picPath" width="80">
                        </div>
                        <div v-else>
                            <img src="../../../../assets/images/no_img.jpg" width="80" height="80" alt="">
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="planId"
                        label="方案ID"
                        width="120">
                </el-table-column>
                <el-table-column label="基本信息" align="left" min-width="160px">
                    <template slot-scope="scope" >
                        <p>
                            <span>方案名称：</span>
                            <span @click="handleDetail(scope.$index, scope.row)" style='color: #FF6419;cursor:pointer;'>{{scope.row.planName}}</span>
                        </p>
                        <p>
                            <span>空间类型：</span>
                            <span >{{scope.row.spaceTypeName}}</span>
                        </p>
                        <p>
                            <span>风&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;格：</span>
                            <span >{{scope.row.designStyleName}}</span>
                        </p>
                        <p>
                            <span>方案类型：</span>
                            <span >{{scope.row.solutionType}}</span>
                        </p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="brandName"
                        label="品牌">
                    <template slot-scope="scope">
                        <p class="remarkOverEllipsis" v-if="scope.row.brandName != ''">{{scope.row.brandName}}</p>
                        <p class="remarkOverEllipsis" v-else>————</p>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="putawayTime"
                    label="上架时间">
                </el-table-column>
                <el-table-column
                        label="上架情况"
                        prop="platformNames"
                        width="130"
                >
                    <template slot-scope="scope">
                        <div v-if="scope.row.platformNames">
                            <p v-for="(item,index) in scope.row.platformNames.split(',')" :key="index">{{item}}</p>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="designer"
                        label="设计师">
                </el-table-column>
                <el-table-column
                        prop="origin"
                        align="center"
                        width="100"
                        label="来源">
                    <template slot-scope="scope">
                        <div v-if="scope.row.origin">
                            <div v-if="scope.row.origin === 'diy'">内部制作</div>
                            <div v-if="scope.row.origin === 'deliver'">企业交付</div>
                            <div v-if="scope.row.origin === 'share'">分享方案</div>
                            <div v-if="scope.row.origin === 'huxing'">内部制作</div>
                        </div>
                        <div v-if="!scope.row.origin || scope.row.origin == '' || scope.row.origin == null">内部制作</div>
                    </template>
                </el-table-column>

                <!--<el-table-column
                  prop="distributionStatus"
                  label="上架情况"
                >
                  <template slot-scope="scope">
                    <div v-if="scope.row.distributionStatus">
                      <p v-if="scope.row.distributionStatus === '2b,'">渠道管理</p>
                      <p v-if="scope.row.distributionStatus === '2b,2c'">渠道管理, 线上管理</p>
                      <p v-if="scope.row.distributionStatus === '2c,2b'">线上管理, 渠道管理</p>
                      <p v-if="scope.row.distributionStatus === '2c,'">线上管理</p>
                    </div>
                    <div v-if="scope.row.distributionStatus == ''">—————</div>
                  </template>
                </el-table-column>-->

                <!--<el-table-column-->
                        <!--v-if="isPer('solution:delivery')"-->
                        <!--prop="deliverStatus"-->
                        <!--label="交付企业数量"-->
                <!--&gt;-->
                <el-table-column
                        v-if="isPer('solution:delivery')"
                        label="交付企业">
                    <template slot-scope="scope">
                        {{scope.row.delivers.length}}
                        <span v-if="scope.row.delivers && scope.row.delivers.length > 0" class="shop-name" @click="getDeliveryView(scope.row)">(查看)</span>
                    </template>
                </el-table-column>
                <!--<el-table-column-->
                        <!--prop="completeDate"-->
                        <!--label="入库时间"-->
                        <!--width="100"-->
                        <!--align="center"-->
                        <!--sortable>-->
                    <!--<template slot-scope="scope">-->
                        <!--<p style="width: 80px; text-align: center">{{scope.row.completeDate}}</p>-->
                    <!--</template>-->
                <!--</el-table-column>-->
                <el-table-column
                        prop="containsSecrecyFlag"
                        label="包含非公开产品"
                        width="100"
                >
                    <template slot-scope="scope">
                        <div style="text-align: center">
                            {{scope.row.containsSecrecyFlag === '0' ? "是" : "否"}}
                            <span v-if="scope.row.containsSecrecyFlag === '0'" @click="showSecrecyList(scope.row)"
                                  class="el-icon-question"></span>
                        </div>

                    </template>
                </el-table-column>

                <el-table-column prop="salePriceChargeType" label="是否免费" align="center">
                    <template slot-scope="scope">
                        <p class="remarkOverEllipsis" v-if="scope.row.salePriceChargeType == 0">免费</p>
                        <p v-else>收费</p>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="salePrice"
                        label="方案价格"
                        width="120"
                >
                    <template slot-scope="scope">
                        <div><p>售卖价格:{{scope.row.salePriceChargeType == 1 ? scope.row.salePrice : '免费'}}</p></div>
                        <div><p>版&nbsp;&nbsp;权&nbsp;&nbsp;费:{{scope.row.chargeType == 1 ? scope.row.planPrice : '免费'}}</p></div>
                    </template>
                </el-table-column>

                <el-table-column prop="secrecyFlag" label="公开状态" align="center">
                    <template slot-scope="scope">
                        <el-switch class="switchStyle" :disabled="isDisable(scope.row.origin)|| scope.row.isCheck == 1"
                                   v-model="scope.row.secrecyFlag"
                                   active-color="#ff6419"
                                   inactive-icon-class="inactive-icon-class"
                                   active-icon-class="active-icon-class"
                                   :active-value="1"
                                   inactive-color="#ddd"
                                   :inactive-value="0"
                                   @change="changeSwitch(scope.row)"
                        >
                        </el-switch>
                        <!--<p>{{scope.row.secrecyFlag == 0 ? '未公开' : '公开'}}</p>-->
                        <p>{{scope.row.secrecyFlag == 0 ? scope.row.statusSwitchTxt = '未公开' : scope.row.statusSwitchTxt
                            ='公开'}}</p>
                    </template>
                </el-table-column>
                <el-table-column width="100" align="center"
                                 prop="operation"
                                 label="操作"
                >
                    <template slot-scope="scope">
                        <el-button type="text" size="mini" style="margin-left: 0" v-if="isPer('solution:view')"
                                   @click="gotoAddProduct(scope.row)">编辑
                        </el-button>
                        <el-button type="text" size="mini" style="margin-left: 0" v-if="isPer('solution:del')"
                                   @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>
                        <el-button type="text" size="mini" style="margin-left: 0" v-if="scope.row.secrecyFlag == 1&&isPer('check:salePrice:edit')"
                                   @click="showPice('设置售卖价格',scope.row)">设置售卖价格
                        </el-button>
                        <el-button type="text" size="mini" style="margin-left: 0"  v-if="isPer('check:planPriceCopyRight:edit')"  @click="showPice('设置版权费',scope.row)">
                            设置版权费
                        </el-button>

                        <!--<el-button type="text" v-if="isPer('solution:manage')"
                                   @click="handleAllot(scope.$index, scope.row)">分配
                        </el-button>-->

                        <div v-if="parseInt(scope.row.isCheck) == 0">
                            <el-button type="text" size="mini" style="margin-left: 0"
                                       v-if="isPer('solution:delivery') && scope.row.origin !== 'deliver' && scope.row.origin !== 'share' && scope.row.origin === 'diy'"
                                       @click="handleDeliver(scope.$index, scope.row)">交付管理
                            </el-button>
                        </div>
                        <div>
                            <el-button type="text" size="mini" style="margin-left: 0"
                                       v-if="isPer('solution:delivery')"
                                       @click="handleShelvesConfirm(scope.row)">上架管理
                            </el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="query.page"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
                title="选择分配对象"
                :visible.sync="allotDialogVisible"
                width="20%"
                top="30vh"
                center>
            <el-checkbox-group class="center" v-model="allot.targetPlatform">
                <el-checkbox v-for="i in distributionStatus" :key="i.code" :label="i.code" :disabled="i.disabled">
                    {{i.label}}
                </el-checkbox>
            </el-checkbox-group>
            <span slot="footer" class="dialog-footer">
        <el-button round type="primary" style="width: 80px" size="medium" class="confirmButtonClass"
                   @click="handleAllotConfirm">确 定</el-button>
        <el-button round type="info" style="width: 80px" size="medium" class="cancelButtonClass"
                   @click="allotDialogVisible = false">取 消</el-button>
      </span>
        </el-dialog>
        <el-dialog
                title="选择交付企业"
                :visible.sync="deliverDialogVisible"
                @close="delivery.receiveBrandIds = []"
                width="25%"
                top="30vh"
                center>
            <template>
                <el-radio v-model="radio" label="企业" @change="radioChange">企业</el-radio>
                <el-radio v-model="radio" label="品牌">品牌</el-radio>
            </template>
            <el-input
                    placeholder="请输入内容"
                    prefix-icon="el-icon-search"
                    v-model="deliverSearch">
            </el-input>
            <el-checkbox-group v-model="delivery.receiveCompanyIds" v-if="radio === '企业'">
                <div class="deliver-box">
                    <el-checkbox style="display: none"></el-checkbox>
                    <el-checkbox class="deliver-box-line"
                                 @change="checkboxChanges"
                                 v-for="(c, index) in searchCompanys"
                                 :label="c.companyId"
                                 :key="index">
                        {{c.companyName}}
                    </el-checkbox>
                </div>
            </el-checkbox-group>
            <el-checkbox-group v-model="delivery.receiveBrandIds" v-if="radio === '品牌'">
                <div class="deliver-box">
                    <el-checkbox style="display: none"></el-checkbox>
                    <el-checkbox class="deliver-box-line"
                                 @change="checkboxChange"
                                 v-for="(c, index) in searchDrand"
                                 :label="c.brandId"
                                 :key="index">
                        {{c.brandName}}
                    </el-checkbox>
                </div>
            </el-checkbox-group>
            <!-- 分页 -->
            <!--<el-pagination small-->
                    <!--@size-change="handleDeliverSizeChange"-->
                    <!--@current-change="handleDeliverCurrentChange"-->
                    <!--:current-page="listDelivery.query.page"-->
                    <!--:page-sizes="[10, 50, 100]"-->
                    <!--:page-size="listDelivery.query.limit"-->
                    <!--layout="prev, pager, next, jumper"-->
                    <!--:total="listDelivery.total">-->
            <!--</el-pagination>-->
            <span slot="footer" class="dialog-footer">
                <el-button round type="primary" @click="handleDeliverConfirm">确 定</el-button>
                <el-button round type="info" @click="deliverDialogVisible = false;deliverSearch = '';delivery.receiveBrandIds = []">取 消</el-button>
            </span>
        </el-dialog>
        <el-dialog title="未公开产品" :visible.sync="dialogOnekeySecercy">
            <el-table :data="onekeySecrecy" border>
                <el-table-column prop="productCode" label="产品编号"></el-table-column>
                <el-table-column prop="productName" label="产品名称"></el-table-column>
            </el-table>
            <div class="pageFrame">
                <el-pagination
                        @size-change="secrecySizeChange"
                        @current-change="secrecyCurrentChange"
                        :current-page="0"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="10"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="secrecyTotal">
                </el-pagination>
            </div>
        </el-dialog>

        <el-dialog
                title="选择上架对象"
                :visible.sync="shelfDialogVisible"
                width="20%"
                top="30vh"
                center>
            <el-checkbox-group class="center" v-model="shelf.shelfStatus">
                <el-checkbox v-for="p in shelfData" :key="p.id" :label="p.id">{{p.name}}</el-checkbox>
            </el-checkbox-group>
            <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="shelfConfirm">确 定</el-button>
        <el-button round type="info" @click="shelfDialogVisible = false">取 消</el-button>
      </span>
        </el-dialog>


        <el-dialog title="已交付企业" :visible.sync="deliveredCompanysDialogVisible">
            <el-table :data="deliveredCompanys"
                      header-cell-class-name="hedStyle"
                      v-loading="deliveredCompanysLoading"
                      element-loading-text="拼命加载中"
                      element-loading-spinner="el-icon-loading"
                      element-loading-background="rgba(255, 255, 255, 0.8)"
            >
                <el-table-column prop="receiveCompanyName" label="企业名称"></el-table-column>
                <el-table-column prop="receiveDate" label="交付时间"></el-table-column>
            </el-table>
            <div class="pageFrame">
                <el-pagination
                        @size-change="deliverDiaSizeChange"
                        @current-change="deliverDiaCurrentChange"
                        :current-page="0"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="10"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="deliverDiaTotal">
                </el-pagination>
            </div>
        </el-dialog>
        <el-dialog :title="priceDialogTitle" :visible.sync="showPriceDialog" width="540px">
            <el-form style="width:440px;margin:0 auto;">
                <el-form-item label="设置收费方式：">
                    <el-radio-group v-model="planFee.chargeMode" v-on:change="clearcash">
                        <div style="margin:14px 0;">
                            <el-radio :label="0">免费</el-radio>
                        </div>

                        <el-radio :label="1">收费</el-radio>
                    </el-radio-group>
                    <div v-if="planFee.chargeMode == 1">
                        <p style="margin: 10px 0 0 114px;"><span style="color:red;">*</span>度币：
                            <el-input style="width:130px;" maxlength="5" v-model="planFee.cash" @keyup.native="number"></el-input>
                            <span class="huansuanImg"></span>人民币(元)：{{planFee.cash == 0?
                            planFee.cash:planFee.cash/ 10}}
                        </p>
                    </div>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" style="border-top:none;">
                  <el-button round type="primary" style="width: 80px" size="medium" class="confirmButtonClass"
                             @click="commitPrice">确 定</el-button>
                  <el-button round type="info" style="width: 80px" size="medium" class="cancelButtonClass"
                             @click="showPriceDialog=false">取 消</el-button>
              </span>
        </el-dialog>

        <!-- 查看交付企业数据 -->
        <el-dialog title="已交付企业" :visible.sync="deliverDialogViewVisible" width="30%">
            <template>
                <el-row :gutter="20"  v-if="currentDeliverViewData && currentDeliverViewData.length > 0">
                    <el-col :span="24">已交付企业数量：{{currentDeliverViewData.length}}</el-col>
                    <el-col :span="8" v-for="(item, index) in currentDeliverViewData" :key="index">
                        <div class="delivery-overflow" :title="item.companyName">
                            {{item.companyName}}
                        </div>
                    </el-col>
                </el-row>
                <div v-else style="color: orangered;">暂无交付企业</div>
            </template>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="deliverDialogViewVisible = false">确 定</el-button>
                <el-button @click="deliverDialogViewVisible = false">取 消</el-button>
            </span>
        </el-dialog>
        <img-pre-view ref="sonParams"></img-pre-view>
    </div>
</template>
<script>
    import ContainsNotOpenProduct from '../../../base/plan/ContainsNotOpenProduct.vue'
    import qs from 'qs'
    import ImgPreView from '@/components/Img-preview'

    export default {
        components: {ContainsNotOpenProduct, ImgPreView},
        name: 'CommonDesignPlan',
        data() {
            return {
                priceDialogTitle: '设置售卖价格',
                RMBChange: 0,
                cash: '',
                Priceform: {
                    isFree: '免费'
                },
                planFee: {
                    cash: 0,
                    planId: '',
                    chargeMode: 0
                },
                showPriceDialog: false,
                brandDrop: [],
                shelf: {
                    shelfStatus: []
                },
                shelfDialogVisible: false,
                shelfData: [
                    {
                        id: 'TEMPLATE',
                        name: '样板方案'
                    }
                ],
                deliveredCompanys: [],
                deliveredCompanysLoading: false,
                deliverDiaTotal: 0,
                deliveredCompanysDialogVisible: false,
                deliveredPlanId: 0,
                deliveredDiaQuery: {
                    page: 0,
                    limit: 10
                },
                spaceCommonName: [], // 空间类型
                productPlanId: 0,
                secrecyTotal: 0,
                channelPlanCommon: true,
                onekeySecrecy: [],
                dialogOnekeySecercy: false,
                deliverDialogVisible: false,
                activeName: 'first',
                total: 0,
                allotDialogVisible: false,
                distributionStatus: [
                    {
                        code: '2b',
                        label: '渠道管理',
                        disabled: false
                    },
                    {
                        code: '2c',
                        label: '线上管理',
                        disabled: true
                    }
                ],
                distributionStatusTemp: [
                    {
                        code: '2b',
                        label: '渠道管理',
                        disabled: false
                    }
//          {
//            code: '',
//            label: '未分配',
//            disabled: true
//          }
                ],
                delivery: {
                    receiveCompanyIds: [],
                    receiveBrandIds: [],
                    delivererId: qs.parse(sessionStorage.getItem('loginUser')).id,
                    deliveryCompanyId: sessionStorage.getItem('companyID')
                },
                disabledDeliverCompanyIds: [],
                allot: {
                    targetPlatform: []
                },
                deliverSearch: '',
                query: {
                    page: 0,
                    limit: 10,
                    planStyleId: null,
                    planGroupStyleId: null,
                    companyId: sessionStorage.getItem('companyID'),
                    id:null
                    /*         distributionStatus: '',
                             isOpen: 0,
                             planName: '',
                             origin: '' */
                },
                list: [],
                sels: [],
                companys: [],
                brand: [],
                searchCompanys: [],
                batchAllotFlag: false,
                confirmAllotMsg: '确定要分配么?',
                confirmDeliverMsg: '确定要交付么?',
                radio: '企业',
                shelfId: [],
                shelfParams: {
                    designPlanType: 1,
                    planIds: [],
                    platformIds: 0
                },
                planStyles: [{
                    styleName: '',
                    styles: [
                        {
                            styleId: 0,
                            styleName: ''
                        }
                    ]}
                ],
                originDeliver: {
                    companies: [],
                    brands: []
                },
                listDelivery: {
                    data:[],
                    total: 10,
                    rows: [],
                    query:{
                        page: 0,
                        limit: 10,
                        keyword: null,
                        companyId: sessionStorage.getItem('companyID')
                    },
                    excludes: []
                },
                deliverDialogViewVisible: false,
                currentDeliverViewData: [],
                designerList:[],
            }
        },
        watch: {
            // 交付公司搜索
            // deliverSearch (val) {
            //   if (val) {
            //     this.searchCompanys = this.companys.filter(c => c.companyName.includes(val))
            //   } else {
            //     this.searchCompanys = this.companys
            //   }
            // }
            deliverSearch(val) {
                if (this.radio == '企业') {
                    if (val) {
                        this.searchCompanys = this.companys
                            .filter(c => c.companyName.includes(val))
                    } else {
                        this.searchCompanys = this.companys
                    }
                }
                if (this.radio == '品牌') {
                    if (val) {
                        console.log()
                        this.searchDrand = this.brand
                            .filter(c => c.brandName.includes(val))
                    } else {
                        this.searchDrand = this.brand
                    }
                }
            }
        },
        methods: {
            exportExcel(){
                if (this.sels.length === 0) {
                    this.$message.error("请选择一条记录");
                } else {
                    this.query.planIds = this.sels.map(res => res.planId).join(",");
                    this.API2.getDesignerListByCommon(this.query).then(res=>{
                        var t = new Blob([res], {
                            type: "application/vnd.ms-excel;charset=utf-8"
                        }),
                        n = URL.createObjectURL(t),
                        a = document.createElement("a");
                        (a.style.display = "none"),
                        (a.href = n),
                        a.setAttribute("download", "普通方案.xls"),
                        document.body.appendChild(a),
                        a.click();
                    });
                }
            },
            pickerOptions(index) {
            let self = this;
            let ops = [
                {
                    disabledDate(time) {
                        if (self.query.endTime)
                        return time.getTime() > new Date(self.query.endTime).getTime();
                        else return false;
                    }
                },
                {
                    disabledDate(time) {
                        if (self.query.startTime)
                        return (
                            time.getTime() + 1000 * 60 * 60 * 24 <
                            new Date(self.query.startTime).getTime()
                        );
                        else return false;
                    }
                }
            ];
            return ops[index];
            },
            gotoAddProduct(item) {
                // mainPlanId
                localStorage.setItem('breadcrumb3', '方案编辑')
                this.$router.push(`/plan/onekey/${item.planId}/edit`);
            },
            number() {
                this.planFee.cash = this.planFee.cash.replace(/[^\ .\d]/g, '');
                this.planFee.cash= this.planFee.cash.replace('.', '');
            },
            clearcash() {
                this.planFee.cash = 0;
            },
            isDisable(origin) {
                if (origin === 'huxing' || origin === 'diy') {
                    return false;
                } else {
                    return true;
                }
            },
            commitPrice() {
                //判断用户输入的金额是否包含小数
                if(String(this.planFee.cash).indexOf(".") > 0){
                    this.$message.warning("不能输入小数");
                    return;
                }
                if (this.priceDialogTitle === '设置售卖价格') {
                    let formData = this.buildPlanFeeHttpFormData("salePrice", "salePriceChargeType", "planId");
                    this.showPriceDialog = false;
                    this.API.setOneKeySalePrice(formData).then((res) => {
                        this.$message({
                            type: "success",
                            message: "设置成功!"
                        });
                        this.page();
                    })
                } else {
                    //设置版权费
                    let formDate = this.buildPlanFeeHttpFormData("planPrice", "chargeType", "planId");
                    this.showPriceDialog = false;
                    this.API.setOneKeyPlanPrice(formDate).then((res) => {
                        this.$message({
                            type: "success",
                            message: "设置成功!"
                        });
                        this.page();
                    })
                }
            },
            buildPlanFeeHttpFormData(planPrice, FeeType, id) {
                let formData = new FormData();
                formData.append(planPrice, this.planFee.cash);
                formData.append(FeeType, this.planFee.chargeMode);
                formData.append(id, this.planFee.planId);
                return formData;
            },
            //价格弹框
            showPice(priceType, row) {
                console.log(priceType);
                if (priceType === '设置售卖价格') {
                    //设置方案售卖价格
                    if (typeof(row.salePrice) != "undefined") {
                        this.planFee.cash = row.salePrice;
                    } else {
                        this.planFee.cash = 0;
                    }
                    this.priceDialogTitle = priceType;
                    this.showPriceDialog = true;
                    this.planFee.planId = row.planId;
                    this.planFee.chargeMode = row.salePriceChargeType;
                }else if(priceType === '设置版权费'){
                    //设置版权费价格
                    if (typeof(row.planPrice) != "undefined") {
                        this.planFee.cash = row.planPrice;
                    } else {
                        this.planFee.cash = 0;
                    }
                    this.planFee.planId = row.planId;
                    this.planFee.chargeMode = row.chargeType;
                    this.showPriceDialog = true;
                    this.priceDialogTitle = '设置方案版权费';
                    console.log(row.planId,row.chargeType)
                }

            }
            ,
            //品牌列表
            getBrandDropList()
            {
                this.API.ProductBrandDrop({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    if(res.data){
                        this.brandDrop = res.data
                    }
                })
            }
            ,
            //图片预览事件
            ImgView(ImgURL)
            {
                this.$refs.sonParams.sonFun({ImgURL: ImgURL, ImgShow: true});
            }
            ,
            isPer(per)
            {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            }
            ,
            // 批量公开
            batchSecrecy()
            {
                if (this.sels.length === 0) {
                    this.$message.error('请选择一条记录')
                    return
                }
                if (this.sels.filter(item => item.isCheck == 1).length > 0) {
                    this.$message.error('选择的记录中包含有未审核的方案')
                    return
                }
                this.$confirm('确认批量公开吗', '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'warning',
                    center: true
                }).then((res) => {
                    let idsArr = []
                    this.sels.map(sel => {
                        idsArr.push(sel.planId)
                    })
                    this.API.planSecrecy({
                        planIds: idsArr,
                        secrecyFlag: 1
                    }).then((res) => {
                        this.$message.success('批量公开成功')
                        this.page()
                    })
                })

            }
            ,
            batchCancelSecrecy()
            {
                if (this.sels.length === 0) {
                    this.$message.error('请选择一条记录')
                    return
                }
                this.$confirm('确认批量取消公开吗', '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'warning',
                    center: true
                }).then(res => {
                    let idsArr = []
                    this.sels.map(sel => {
                        idsArr.push(sel.planId)
                    })
                    this.API.planSecrecy({
                        planIds: idsArr,
                        secrecyFlag: 0
                    }).then((res) => {
                        this.page()
                        this.$message.success('批量取消公开成功')
                    })
                })

            }
            ,
            changeSwitch(row)
            {
                if (row) {
                    if (row.secrecyFlag) {
                        row.statusSwitchTxt = '未公开'
                        this.API.planSecrecy({
                            planIds: [row.planId],
                            secrecyFlag: 1
                        }).then((res) => {

                        })
                    } else {
                        row.statusSwitchTxt = '公开'
                        this.API.planSecrecy({
                            planIds: [row.planId],
                            secrecyFlag: 0
                        }).then((res) => {

                        })
                    }
                }
            }
            ,
            shelfConfirm()
            {
                this.shelfModel()
            }
            ,
            handleShelvesConfirm(row)
            { // 确定上架到某个平台
                if (row.designStyleName) {
                    this.shelfParams.planIds = [];
                    //  this.shelf.shelfStatus = [...(row.platformIds ? [row.platformIds] : [])]
                    this.shelf.shelfStatus = row.platformIds.split(',');
                    this.shelfParams.platformIds = this.shelf.shelfStatus
                    this.shelfParams.planIds.push(row.planId)
                    this.shelfDialogVisible = true
                } else {
                    this.$message.warning('请选择方案的风格再上架');
                }

            }
            ,
            // 上架

            batchShelf()
            {
                if (this.sels) {
                    this.sels.map(sel => {
                        this.shelfParams.planIds.push(sel.planId)
                    })
                    this.shelfParams.platformIds = this.shelf.shelfStatus.join(',')
                    this.API.contentLibraryPlanShelf(this.shelfParams).then(resp => {
                        this.$message.success('上架成功')
                        this.shelfParams.planIds = []
                        this.shelfDialogVisible = false
                        this.page()
                    })
                }
            }
            ,
            singleShelf()
            {
                this.shelfParams.platformIds = this.shelf.shelfStatus.join(',');
                this.API.contentLibraryPlanShelf(this.shelfParams).then(resp => {
                    this.$message.success('上架成功')
                    this.shelfParams.planIds = []
                    this.shelfDialogVisible = false
                    this.page()
                })
            }
            ,
            shelfModel()
            {
                if (this.sels.length > 0) {

                    this.batchShelf()
                } else {

                    this.singleShelf()
                }
            }
            ,
            // 批量上架
            handleBatchAllot()
            {
                this.shelf.shelfStatus = []
                if (this.sels.length <= 0) {
                    this.$message.error('请选择一条记录')
                    return
                }
                if (this.sels.some(res => {
                    return res.designStyleName == undefined
                })) {
                    this.$message.warning('请选择方案的风格再上架');
                } else {
                    this.shelfDialogVisible = true
                }
            }
            ,
            showSecrecyList(row)
            {
                if (row) {
                    this.dialogOnekeySecercy = true
                    this.productPlanId = row.planId
                    this.ProductSecrecyList()
                }
            }
            ,
            // 空间类型
            getSpaceCommonName()
            {
                this.API.FitTypeHose().then((res) => {
                    this.spaceCommonName = res.data
                    console.log(res, 'FitTypeHose')
                })
            }
            ,
            // 显示为空开产品列表
            ProductSecrecyList()
            {
                this.API.ListOneKeyPlanProducts({
                    planId: this.productPlanId,
                    secrecy: 0,
                    page: this.pages,
                    limit: this.limits
                }).then((res) => {
                    if (res.code == 204 || res.code >= 500) {
                        this.onekeySecrecyLoading = false
                    }
                    if (res) {
                        this.onekeySecrecyLoading = false
                        this.onekeySecrecy = res.data
                        this.secrecyTotal = res.total
                    }
                })
            }
            ,
            refreshData()
            {
                this.$nextTick(function () {
                    this.channelPlanCommon = true;
                    this.page()
                })
            }
            ,
            // 查询分页信息
            page()
            {
                this.API.CommonDesignPlanList(this.query).then((resp) => {
                    if (resp.code == 200 || resp.code >= 500) {
                        this.channelPlanCommon = false
                    }
                    if (resp.data) {
                        resp.data.list.map(item => {
                            if (item.origin === 'share') {
                                item.secrecyFlag = 0;
                            }
                        })
                        this.list = resp.data.list
                        console.log(this.list)
                        this.total = resp.data.total
                        this.channelPlanCommon = false
                    } else {
                        this.list = []
                        this.total = 0
                        this.channelPlanCommon = false
                    }
                })
            }
            ,
            // 显示交付公司列表
            showDeliveredCompanys(row)
            {
                this.deliveredCompanysDialogVisible = true
                this.deliveredCompanysLoading = true
                this.deliveredPlanId = row.planId
                this.listDeliveredCompanys()
            }
            ,
            // 获取交付公司列表
            listDeliveredCompanys()
            {
                this.API.ListDeliveredCompanys(this.deliveredPlanId, this.deliveredDiaQuery).then(res => {
                    this.deliveredCompanys = res.list
                    this.deliverDiaTotal = res.total
                    this.deliveredCompanysLoading = false
                })
            }
            ,
            deliverDiaSizeChange(val)
            {
                this.deliveredDiaQuery.limit = val
                this.listDeliveredCompanys()
            }
            ,
            deliverDiaCurrentChange(val)
            {
                this.deliveredDiaQuery.page = val
                this.listDeliveredCompanys()
            }
            ,
            handleSelectionChange(sels)
            {
                this.sels = sels;
            }
            ,
            handleDetail(index, row)
            {
                console.log(row)
                //return
                this.$store.commit('breadcrumb3', '方案详情')
                localStorage.setItem('isNowPlanType', 'false')
                localStorage.setItem('detailPlanType', '0')
                this.$router.push(`/plan/onekey/${row.planId}`)
            }
            ,
            handleAllot(index, row)
            { // 方案分配 打开弹窗
                if (row.containsSecrecyFlag === '0') {
                    this.confirmAllotMsg = '选定的方案包含未公开产品,确定要分配么?'
                }
                this.allot.planId = row.planId
                this.allotDialogVisible = true
                this.batchAllotFlag = false
                if (row.distributionStatus) {
                    this.allot.targetPlatform = row.distributionStatus.split(',')
                } else {
                    this.allot.targetPlatform = []
                }
            }
            ,
            handleAllotConfirm()
            { // 确定分配到某个平台
                this.$confirm(this.confirmAllotMsg, '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'warning',
                    center: true
                }).then(() => {
                    if (this.batchAllotFlag) {
                        const params = this.sels.map(item => ({
                            planId: item.planId,
                            targetPlatform: this.allot.targetPlatform
                        }))
                        this.API.CommonPlanBatchAllot(params).then(resp => {
                            this.allotDialogVisible = false
                            this.page()
                            this.$message.success('分配成功')
                        })
                    } else {
                        this.API.CommonPlanAllot(this.allot).then(resp => {
                            this.page()
                            this.$message({
                                message: '分配成功',
                                type: 'success'
                            })
                            this.allotDialogVisible = false
                        })
                    }
                })
            }
            ,

            checkboxChanges(){
                console.log(this.delivery.receiveCompanyIds);
            },
            /*当选择上品牌时提交参数的企业id(this.delivery.receiveCompanyIds)为选中品牌下所属的企业id
            * 当没有选上任意一个品牌的话企业id(this.delivery.receiveCompanyIds)为原来值
            * */
            checkboxChange(){
                try{
                    if (this.delivery.receiveBrandIds.length > 0) {
                        let arr = [];
                        for (let i = 0; i < this.delivery.receiveBrandIds.length; i++) {
                            for (let j = 0; j < this.brand.length; j++) {
                                if (this.delivery.receiveBrandIds[i] == this.brand[j].brandId) {
                                    arr.push(this.brand[j].brandCompanyId);
                                }
                            }
                        }
                        for (let i = 0; i < arr.length; i++) {
                            for (let j = i + 1; j < arr.length; j++) {
                                if (arr[i] == arr[j]) {
                                    arr.splice(j, 1);
                                    j--;
                                }
                            }
                        }
                        this.delivery.receiveCompanyIds = arr;
                    } else {
                        this.delivery.receiveCompanyIds = this.disabledDeliverCompanyIds;
                    }
                }catch(error){
                    console.log(error)
                }
                console.log(this.delivery.receiveCompanyIds);
            },
            /*但选择企业单选切换的时候清空已选的品牌id
            * 恢复原来企业id的值
            * */
            radioChange(){
                this.delivery.receiveBrandIds = [];
                this.delivery.receiveCompanyIds = this.disabledDeliverCompanyIds;
            },
            // 点击交付 打开弹窗
            handleDeliver(index, row)
            {
                // let startup = new Date().getTime();
                // if (row.containsSecrecyFlag === '0') {
                //   this.confirmDeliverMsg = '选定的方案包含未公开产品,确定要交付么?'
                // }
                // this.deliverDialogVisible = true
                // this.delivery.designPlanId = row.planId
                // this.deliverSearch = ''
                // this.API.ListCompanysWithDelivered({
                //   companyId: sessionStorage.getItem('companyID'),
                //   planId: row.planId
                // }).then((res) => {
                //   this.companys = res.data
                //   this.delivery.receiveCompanyIds = this.companys.filter(it=> it.delivered).map(it=> it.companyId)
                //   this.disabledDeliverCompanyIds = this.companys.filter(it=> it.delivered).map(it=> it.companyId)
                //   this.searchCompanys = this.companys
                // })
                if (this.sels.filter(item => item.isCheck == "1").length > 0) {
                    this.$message.error('选择的记录中包含有未审核的方案')
                    return
                }
                this.isAllDelivery = false
                this.deliverDialogVisible = true
                this.delivery.designPlanId = row.planId
                this.deliverSearch = ''
                if (row.containsSecrecyFlag === '0') {
                    this.confirmDeliverMsg = '选定的方案包含未公开产品,确定要交付么?'
                }
                // 获取需要交付的公司  查是否被交付过
                // this.API.ListCompanysWithDelivered({
                //     companyId: sessionStorage.getItem('companyID'),
                //     planId: row.planId
                // }).then((res) => {
                //     this.companys = res.data.companyInfo
                //     this.brand = res.data.brandInfo
                //     // for (let i = 0; i < this.brand.length; i++) {
                //     //   this.brand[i].index = i
                //     // }
                //     // console.log(this.brand)
                //     this.disabledDeliverCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
                //     this.delivery.receiveCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
                //     this.searchCompanys = this.companys
                //     // this.disabledDeliverBrandIds = this.brand.filter(it => it.delivered).map(it => it.brandCompanyId)
                //     // this.delivery.receiveBrandIds = this.brand.filter(it => it.delivered).map(it => it.brandCompanyId)
                //     this.searchDrand = this.brand
                // })

                // Modified by songjianming@sanduspace.cn on 2018/12/28
                // 商家后台方案交付优化及增加设计风格筛选条件(http://192.168.1.201:8080/browse/CMS-691)
                // 方案交付调整 => 方案交付管理，只显示未交付过的企业，不需要显示已交付的企业，已交付的企业不能修改
                let excludes = (row.delivers || []).map((item) => {
                    return item.companyId;
                });
                if (excludes && excludes.length > 0) {
                    // 企业
                    this.companys = this.getCompanyWithDelivered(this.originDeliver.companies, excludes, true);
                    // 品牌
                    this.brand = this.getCompanyWithDelivered(this.originDeliver.brands, excludes, false);
                } else {
                    Object.assign(this.companys, this.originDeliver.companies);
                    Object.assign(this.brand, this.originDeliver.brands);
                }

                this.searchCompanys = this.companys;
                this.searchDrand = this.brand;
                this.disabledDeliverCompanyIds = excludes;
                this.delivery.receiveCompanyIds = excludes;

                // this.listDelivery.excludes = excludes;
                // this.listDelivery.query.page = 0;
                // this.listDelivery.query.limit = 10;
                // this.initDelivered2();
            },
            doCleanupDeliveredView () {
                this.searchCompanys = [];
                this.searchDrand = [];
            },
            getCompanyWithDelivered(list, excludes, type) {
                if (type) {
                    return list.filter((item) => {
                        return excludes.indexOf(item.companyId) < 0;
                    });
                } else {
                    return list.filter((item) => {
                        return excludes.indexOf(item.brandCompanyId) < 0;
                    });
                }
            },
            // 确定交付行为
            handleDeliverConfirm()
            {
                /*   if (this.delivery.receiveCompanyIds.length <= 0) {
                     this.$message.error('请选择一个公司')
                     return
                   }*/
                //
                // let arr = this.delivery.receiveBrandIds
                // for (let i = 0; i < this.delivery.receiveCompanyIds.length; i++) {
                //   for (let j = 0; j < arr.length; j++) {
                //     if (this.delivery.receiveCompanyIds[i] == arr[j]) {
                //       this.delivery.receiveBrandIds = arr.slice(j, 1)
                //     }
                //   }
                // }
                this.$confirm(this.confirmDeliverMsg, '提示', {
                    type: 'warning'
                }).then(() => {
                    if (!this.isAllDelivery) {
                        this.API.OneKeyPlanDeliver(this.delivery).then((resp) => {
                            this.deliverDialogVisible = false
                            this.page()
                            this.$message({
                                type: 'success',
                                message: '交付成功!'
                            })
                        })
                    } else {
                        console.log('allDelivery')
                        this.API.allDelivery(this.delivery).then((res) => {
                            console.log(res)
                            this.deliverDialogVisible = false
                            this.deliverSearch = ''
                            this.page()
                            this.$refs.multipleTable.clearSelection();
                            this.$message({
                                type: 'success',
                                message: '交付成功!'
                            })
                        })
                    }

                    // 清空View
                    // this.doCleanupDeliveredView();
                })
            } ,
            // 批量交付
            batchDelivery()
            {

                if (this.sels.length === 0) {
                    this.$message.error('请选择一条记录')
                    return
                }
                if (this.sels.filter(item => item.isCheck == 1).length > 0) {
                    this.$message.error('选择的记录中包含有未审核的方案')
                    return
                }

                this.isAllDelivery = true
                if (this.sels.length === 0) {
                    this.$message({
                        type: 'error',
                        message: '请选择一条记录'
                    })
                    return
                }
                let arr = []
                try{
                    for (let i = 0; i < this.sels.length; i++) {
                        console.log(this.sels[i].origin)
                        if (this.sels[i].origin == 'diy' || this.sels[i].origin == 'huxing' || this.sels[i].origin == undefined) {
                            this.isReturn = false;
                        } else {
                            this.isReturn = true;
                        }
                        arr.push(this.sels[i].planId)
                        // if (this.sels[i].containsSecrecyFlag === '0') {
                        //   // this.confirmDeliverMsg = '选定的方案包含未公开产品,确定要交付么?'
                        // }
                    }
                }catch(error){
                    console.log(error)
                }

                console.log(this.isReturn)
                if (this.isReturn) {
                    this.$message({
                        type: 'error',
                        message: '来源为分享方案的无法交付'
                    })
                    return
                }
                this.delivery.designPlanId = arr
                this.API.ListCompanysWithDelivered({
                    companyId: sessionStorage.getItem('companyID'),
                    planId: -1
                }).then((res) => {
                    this.companys = res.data.companyInfo
                    this.brand = res.data.brandInfo
                    this.disabledDeliverCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
                    this.delivery.receiveCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
                    this.searchCompanys = this.companys
                    // this.disabledDeliverCompanyIds = this.brand.filter(it => it.delivered).map(it => it.brandCompanyId)
                    // this.delivery.receiveCompanyIds = this.brand.filter(it => it.delivered).map(it => it.brandCompanyId)
                    this.searchDrand = this.brand
                })
                this.deliverDialogVisible = true
            },
            handleEdit(index, row) {
                 this.$router.push(`/plan/common/${row.planId}/edit`)
            },
            handleDelete(index, row)
            {
                this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    center: true,
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'warning'
                }).then((index) => {
                    this.API.CommonDesignPlanDelete(row.planId).then((resp) => {
                        this.page()
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        })
                    })
                })
            }
            ,
            batchDelete()
            {
                if (this.sels.length <= 0) {
                    this.$message.error('请选择一条记录')
                    return;
                }
                const planIds = this.sels.map((sel) => sel.planId).join(',')
                this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    center: true,
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'warning'
                }).then((index) => {
                    this.API.CommonDesignPlanBatchDelete(planIds).then((resp) => {
                        this.page()
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        })
                    })
                })
            }
            ,
            // 未公开产品分页模块
            secrecySizeChange: function (size) {
                this.limits = size
                this.ProductSecrecyList()
            }
            ,
            secrecyCurrentChange: function (currentPage) {
                this.pages = currentPage
                this.ProductSecrecyList()
            }
            ,
            // 分页事件
            handleSizeChange: function (size) {
                this.query.limit = size
                this.page()
            }
            ,
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage
                this.page()
            }
            ,
            handleClearQuery()
            {
                this.query = {
                    page: 0,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                }
                this.page();
            }
            ,
            getCompany()
            {

            },
            initPlanStyle() {
                this.API.listDesignPlanStyle().then((resp) => {
                    this.planStyles = resp.data || [];
                });
            },
            initDelivered() {
                this.API.ListCompanysWithDelivered({
                    companyId: sessionStorage.getItem('companyID'),
                    planId: -1
                }).then((res) => {
                    this.originDeliver.companies = res.data.companyInfo;
                    this.originDeliver.brands = res.data.brandInfo;
                })
            },
            initDelivered2() {
                // this.API.MapCompanyDelivered(this.listDelivery.query).then((resp) => {
                //     let data = resp ? resp.data : {};
                //     // 企业
                //     this.companys = data.companies || [];
                //     // 品牌
                //     this.brand = data.brands || [];
                //
                //     this.disabledDeliverCompanyIds = this.listDelivery.excludes;
                //     this.delivery.receiveCompanyIds = this.listDelivery.excludes;
                //     this.searchCompanys = this.companys;
                //     this.searchDrand = this.brand;
                //
                //     // total
                //     this.listDelivery.total = data.total || 0;
                // });

                if (this.radio === '企业') {
                    this.searchCompanys = this.doDeliverSearch(this.companyName, this.originDeliver.companies);
                }
                if (this.radio === '品牌') {
                    this.searchDrand = this.doDeliverSearch(this.deliverSearch, this.originDeliver.brands);
                }

            },
            doDeliverSearch(keyword, rows) {
                let excludes = this.listDelivery.excludes;
                return rows.filter((item, index) => {
                    return (excludes.indexOf(item.companyId || item.brandCompanyId || -1) < 0)
                            && (keyword ? (item.companyName.include(keyword)
                            || item.brandName.include(keyword)) : true)
                        && index >= this.listDelivery.query.page
                        && index < this.listDelivery.query.limit;
                });
            },
            getDeliveryView(row) {
                this.deliverDialogViewVisible = true;
                this.currentDeliverViewData = row.delivers || [];
            },
            handleDeliverSizeChange: function (size) {
                this.listDelivery.query.limit = size;
                this.initDelivered2();
            }
            ,
            handleDeliverCurrentChange: function (currentPage) {
                this.listDelivery.query.page = currentPage;
                this.initDelivered2()
            },
            initDesigner(){
                this.API2.getDesignerList({companyId:sessionStorage.getItem('companyID'),recommendedType:1}).then(res=>{
                    this.designerList = res.data;
                })
            }
        },
        created() {
            this.getCompany();
            this.page();
            this.getSpaceCommonName();
            this.getBrandDropList();
            this.initPlanStyle();
            this.initDelivered();
            this.initDesigner();
        }
    }
</script>
<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";

    .operatonBtn {
        display: inline-block;
        margin-bottom: 8px;
        margin-left: 0 !important;
        font-size: 14px;
    }

    .operatonBtnTxt {
        color: #FF6419;
        cursor: pointer;
        padding-right: 20px;
    }

    .center {
        text-align: center;
    }

    .searchBtnFrame {
        border-top: solid 1px #ddd;
        margin-bottom: 15px;
        border-bottom: solid 15px #eaeaea;
        padding: 20px 42px;
        .el-button {
            width: 100px;
        }
    }

    .inactive-icon-class {
        background: #5daf34;
        border: solid 1px #ddd !important;
    }

    .active-icon-class {
        background: #5daf34;
        border: solid 1px #ddd !important;
    }

    .searchCondition {
        padding-left: 30px;
        padding-bottom: 20px;
        .el-select, .el-input {
            width: 210px;
        }
        .el-col {
            width: 200px;
            margin-right: 15px;
            padding-top: 10px;
        }
    }

    .planLibrary {
        box-sizing: padding-box;
        background: #fff;
        border-radius: $borderRadius;
    }

    .deliver-box {
        margin-top: 20px;
        height: 200px;
        overflow-y: scroll;
    }

    .deliver-box-line {
        display: block;
    }

    .tabClass {
        padding: 20px;
        margin-bottom: 20px;
    }

    .inputPlanKey {
        .el-input {
            width: 200px;
            margin-right: 10px;
        }
        .el-button {
            width: 130px;
            border: solid 1px $btnOrange;
            margin-left: 15px;
            color: $buttonOrange;
        }
    }

    .operationButton {
        padding: 20px 30px;
        @extend .planLibrary;
        .delivery {
            background: url('../../../../assets/images/icons/delivery.png') 0 center no-repeat;
            &:hover {
                background: url('../../../../assets/images/icons/delivery1.png') 0 center no-repeat;
            }
        }
        .outline {
            background: url('../../../../assets/images/icons/allot.png') 0 center no-repeat;
            &:hover {
                background: url('../../../../assets/images/icons/allot2.png') 0 center no-repeat;
            }
        }
        .delete {
            background: url('../../../../assets/images/icons/delete.png') 0 center no-repeat;
            &:hover {
                background: url('../../../../assets/images/icons/delete2.png') 0 center no-repeat;
            }
        }
        .refresh {
            background: url('../../../../assets/images/icons/reflesh.png') 0 center no-repeat;
            &:hover {
                background: url('../../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
            }
        }
        .export {
            background: url('../../../../assets/images/icons/btn_export_nor.png') 0 center no-repeat;
            &:hover {
                background: url('../../../../assets/images/icons/btn_export_pre.png') 0 center no-repeat;
            }
        }
        button {
            padding-left: 25px;
            color: #666;
            font-size: 14px;
            margin-right: 10px;
            &:hover {
                color: $overButtonOrange;
            }
        }
    }

    .planList {
        border-top: solid 1px #eee;
        @extend .planLibrary;
        .fubenImg{
            position: absolute;
            top: 0;
            left: 0;
        }
        .shop-name {
            width: 100%;
            border: none;
            color: #ff6419;
            cursor: pointer;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }

    .pageFrame {
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .huansuanImg {
        width: 12px;
        height: 14px;
        background: url('../../../../assets/images/icons/icon-huansuan.png') no-repeat;
        background-size: 12px 14px;
        display: inline-block;
        margin: 0 10px;
        vertical-align: middle;
    }
    .plan-style-css {
        .el-select-dropdown__item {
            color: #606266 !important;
        }
    }
    .delivery-overflow {
        overflow:hidden;
        text-overflow:ellipsis;
        white-space:nowrap;
        margin-left: 10px;
        margin-top: 5px;
        font-size: 14px;
        background-color: #f1f1f1;
        border-radius: 2px;
        padding: 2px;
        text-align: center;
    }
</style>
<style>
    .el-table th {
        height: 60px;
        background: #f3f3f3;
        text-align: center
    }

</style>
