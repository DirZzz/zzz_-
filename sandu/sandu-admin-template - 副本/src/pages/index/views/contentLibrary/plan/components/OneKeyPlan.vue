<template>
  <div>
    <div class="planLibrary tabClass">
      <div class="searchCondition">
        <el-row :gutter="10">
            <el-col :span="3.5">
              <el-select v-model="classProgramme" @change="GoPlan" placeholder="请选择方案">
                <el-option value="oneProgramme" label="单个方案/组合方案"></el-option>
                <el-option value="allProgramme" label="全屋方案"></el-option>
                <el-option value="single" label="单个方案"></el-option>
                <el-option value="group" label="组合方案"></el-option>
              </el-select>
            </el-col>
            <el-col :span="3.5">
              <el-input v-if="!isAllProgramme" v-model="query.planName" placeholder="请输入方案名称"></el-input>
              <el-input v-else v-model="query.planName" placeholder="请输入设计师/方案名称/编号"></el-input>
            </el-col>
            <el-col :span="3.5"><el-input v-model="query.id" placeholder="请输入方案ID"></el-input></el-col>
            <el-col :span="3.5">
              <el-select
                      @change="page"
                      v-model="query.planGroupStyleId"
                      filterable
                      clearable
                      popper-class="plan-style-css"
                      placeholder="请选择方案风格">
                <el-option
                        v-for="item in planStyles"
                        :key="item.groupId"
                        :label="item.styleName"
                        :value="item.groupId"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="3.5">
              <el-select
                      v-model="query.spaceCommonName"
                      @change="spanceTypeSelect"
                      :disabled="isAllProgramme"
                      clearable
                      placeholder="请选择空间类型"
              >
                <el-option
                        v-for="item in spaceCommonName"
                        :key="item.id"
                        :label="item.name"
                        :value="item.valueKey"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="3.5">
              <el-select
                      @change="page"
                      v-model="query.designerUserId"
                      filterable
                      clearable
                      popper-class="plan-style-css"
                      placeholder="请选择设计师"
              >
                <el-option
                        v-for="item in designerList"
                        :key="item.id"
                        :label="item.userName"
                        :value="item.id"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="3.5">
              <el-select v-model="query.origin" @change="page" clearable placeholder="请选择方案来源">
                <el-option value="deliver" label="企业交付"></el-option>
                <el-option value="diy" label="内部制作"></el-option>
                <el-option value="share" label="分享方案"></el-option>
              </el-select>
            </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="3.5">
            <el-select @change="page" clearable v-model="query.platformId" placeholder="请选择上架情况">
              <el-option
                      v-for="(item, index) in platformList"
                      :key="index"
                      :label="item.name"
                      :value="item.id"
              ></el-option>
              <el-option value="0" label="未上架"></el-option>
              <!-- <el-option value="ALL" label="渠道&线上"></el-option>
              <el-option value="NONE" label="未分配"></el-option>-->
            </el-select>
          </el-col>
          <el-col :span="8.5">
            <el-date-picker
                    v-model="query.startTime"
                    type="date"
                    :picker-options="pickerOptions(0)"
                    value-format="yyyy-MM-dd"
                    placeholder="上架时间"
                    :clearable="!query.endTime"
            ></el-date-picker>
            <span>-</span>
            <el-date-picker
                    v-model="query.endTime"
                    type="date"
                    :picker-options="pickerOptions(1)"
                    @change="query.startTime?null:$set(query,'startTime',query.endTime)"
                    value-format="yyyy-MM-dd"
                    placeholder="上架时间"
            ></el-date-picker>
          </el-col>
          <el-col :span="3.5">
            <el-select
                    v-model="query.brandId"
                    @change="page"
                    filterable
                    clearable
                    placeholder="请选择品牌"
            >
              <el-option
                      v-for="(item,index) in brandDrop"
                      :key="index"
                      :label="item.name"
                      :value="item.id"
              >{{item.name}}</el-option>
            </el-select>
          </el-col>
          <el-col :span="3.5">
            <el-select v-model="query.deliverStatus" @change="page" clearable placeholder="请选择交付状态">
              <el-option value="Y" label="已交付"></el-option>
              <el-option value="N" label="未交付"></el-option>
            </el-select>
          </el-col>
          <el-col :span="3.5">
            <contains-not-open-product @change="page" v-model="query.containsSecrecyFlag"></contains-not-open-product>
          </el-col>
          <el-col :span="3.5">
            <el-select @change="page" v-model="query.openState" clearable placeholder="是否为公开方案">
              <el-option value="1" label="是"></el-option>
              <el-option value="0" label="否"></el-option>
            </el-select>
          </el-col>
          <el-col :span="3.5">
            <el-select
                    @change="page"
                    v-model="query.salePriceChargeType"
                    clearable
                    placeholder="方案售卖价格是否免费"
            >
              <el-option value="0" label="是"></el-option>
              <el-option value="1" label="否"></el-option>
            </el-select>
          </el-col>
          <el-col :span="3.5">
            <el-select @change="page" v-model="query.chargeType" clearable placeholder="方案版权费是否免费">
              <el-option value="0" label="是"></el-option>
              <el-option value="1" label="否"></el-option>
            </el-select>
          </el-col>

          <el-col :span="3.5">
            <el-select
                    v-model="query.applyAreaValue"
                    filterable
                    clearable
                    popper-class="plan-style-css"
                    @focus="applyAreaFocus"
                    placeholder="请选择适用面积">
              <el-option
                      v-for="item in applyAreaList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
              ></el-option>
            </el-select>
          </el-col>
        </el-row>
        <!--<el-col :span="3" v-if="isPer('solution:manage')">
                  <el-select @change="page" clearable v-model="query.distributionStatus" placeholder="请选择分配情况">
                    <el-option
                      v-for="(item,index) in distributionStatus"
                      :key="index"
                      :label="item.label"
                      :value="item.code">
                    </el-option>
                    <el-option value="ALL" label="渠道&线上"></el-option>
                    <el-option value="NONE" label="未分配"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="3"  v-if="isPer('solution:delivery')">
                  <el-select v-model="query.deliverStatus" @change="page" clearable placeholder="请选择交付状态">
                    <el-option value="Y" label="已交付"></el-option>
                    <el-option value="N" label="未交付"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="3">
                  &lt;!&ndash;<el-input v-model="query.spaceCommonName" placeholder="请输入空间类型"></el-input>&ndash;&gt;
                  <el-select v-model="query.spaceCommonName" @change="page" clearable placeholder="请选择空间类型">
                    <el-option
                      v-for="item in spaceCommonName"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                    </el-option>
                  </el-select>
        </el-col>-->
      </div>
      <el-row class="searchBtnFrame">
        <el-button round size="small" type="primary" @click="searchPage">搜索</el-button>
        <el-button round class="searchBtn" size="small" @click="handleClearQuery">重置</el-button>
      </el-row>
      <div>
        <div class="operationButton">
          <el-button
                  type="text"
                  class="allocation"
                  v-if="isPer('solution:exhibition')"
                  @click="handleBatchAllot"
          >批量上下架</el-button>
          <el-button
                  size="medium"
                  type="text"
                  class="delete"
                  v-if="isPer('solution:del')"
                  @click="batchDelete"
          >批量删除</el-button>
          <el-button
                  size="medium"
                  type="text"
                  class="delivery"
                  @click="batchSecrecy"
                  v-if="isPer('solution:manage')"
          >批量公开</el-button>
          <el-button
                  size="medium"
                  type="text"
                  class="delivery"
                  @click="batchCancelSecrecy"
                  v-if="isPer('solution:manage')"
          >批量取消公开</el-button>
          <el-button
                  size="medium"
                  type="text"
                  class="delivery"
                  @click="batchDelivery"
                  v-if="isPer('solution:delivery')"
          >批量交付</el-button>
          <el-button size="small" type="text" @click="refreshData" class="refresh">刷新</el-button>
          <el-button size="small" type="text" @click="exportExcel" class="export">导出Excel</el-button>
        </div>
        <div class="planList">
          <el-table
                  :data="list"
                  style="width: 100%;"
                  ref="multipleTable"
                  v-loading="planLoading"
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
            <el-table-column type="selection" label="全选" align="left" title="全选"></el-table-column>
            <el-table-column prop="picPath" label="方案缩略图" width="100">
              <template slot-scope="scope">
                <div v-if="scope.row.picPath">
                  <img
                          style="cursor:pointer;"
                          @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)"
                          :src="BASE_URL.sourceBaseUrl+scope.row.picPath"
                          width="80"
                  >
                </div>
                <div v-else>
                  <img
                          :src="require('../../../../assets/images/no_img.jpg')"
                          width="80"
                          height="80"
                          alt
                  >
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="planId" label="方案ID" sortable></el-table-column>
            <el-table-column label="基本信息" align="left" min-width="160px">
              <template slot-scope="scope">
                <p>
                  <span>方案名称：</span>
                  <span
                          @click="handleDetail(scope.$index, scope.row)"
                          style="color: #FF6419;cursor:pointer;"
                  >{{scope.row.planName}}</span>
                </p>
                <p>
                  <span>空间类型：</span>
                  <span>{{scope.row.spaceTypeName}}</span>
                </p>
                <p>
                  <span>风&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;格：</span>
                  <span>{{scope.row.designStyleName}}</span>
                </p>
                <p>
                  <span>方案类型：</span>
                  <span>{{scope.row.solutionType}}</span>
                </p>
                <!--<p>-->
                  <!--<span>通用面积（m²）：</span>-->
                  <!--<span>{{scope.row.applyAreaNames && scope.row.applyAreaNames.join(',')}}</span>-->
                <!--</p>-->
              </template>
            </el-table-column>
            <el-table-column prop="brandName" label="品牌">
              <template slot-scope="scope">
                <p class="remarkOverEllipsis" v-if="scope.row.brandName">{{scope.row.brandName}}</p>
                <p v-else>———</p>
              </template>
            </el-table-column>
            <!--<el-table-column prop="designer" label="设计师"></el-table-column>-->
            <el-table-column prop="putawayTime" label="上架时间"></el-table-column>
            <el-table-column label="上架情况" prop="platformNames" width="140">
              <template slot-scope="scope">
                <div v-if="scope.row.platformNames">
                  <p
                          v-for="(item,index) in scope.row.platformNames.split(',')"
                          :key="index"
                  >{{item}}</p>
                </div>
              </template>
            </el-table-column>
            <!--<el-table-column-->
            <!--v-if="isPer('solution:delivery')"-->
            <!--prop="deliverStatus"-->
            <!--label="交付企业数量"-->
            <!--&gt;-->
            <!--</el-table-column>-->
            <el-table-column prop="containsSecrecyFlag" label="包含非公开产品" width="140">
              <template slot-scope="scope">
                <div style="text-align: center" class="question">
                  {{scope.row.containsSecrecyFlag === '0' ? "是" : "否"}}
                  <span
                          v-if="scope.row.containsSecrecyFlag === '0'"
                          @click="showSecrecyList(scope.row)"
                          class="el-icon-question"
                  ></span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="适用面积">
              <template slot-scope="scope">
                  <!--<span>{{scope.row.applyAreaNames && scope.row.applyAreaNames.slice(0,2).join(',')}}{{scope.row.applyAreaNames.length>2 ? "..." : ''}}</span>-->
                      <span>{{scope.row.applyAreaNames && scope.row.applyAreaNames.join(',')}}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="isPer('solution:delivery')" label="交付企业">
              <template slot-scope="scope">
                {{scope.row.delivers.length}}
                <span
                        v-if="scope.row.delivers && scope.row.delivers.length > 0"
                        class="shop-name"
                        @click="getDeliveryView(scope.row)"
                >(查看)</span>
              </template>
            </el-table-column>
            <el-table-column label="附加信息" width="160" align="left">
              <template slot-scope="scope">
                <div>
                  <span v-if="scope.row.origin === 'diy'">来源:内部制作</span>
                  <span v-else-if="scope.row.origin === 'deliver'">来源:企业交付</span>
                  <span v-else-if="scope.row.origin === 'share'">来源:分享方案</span>
                  <span v-else-if="scope.row.origin === 'huxing'">来源:内部制作</span>
                  <span v-else>来源:内部制作</span>
                </div>
                <div>设计师：{{scope.row.designer}}</div>
                <div :class="{'question':scope.row.usedInFullPlanCount}">
                  <span>是否关联全屋：{{scope.row.usedInFullPlanCount ? '是' : '否'}}</span>
                  <span v-if="scope.row.usedInFullPlanCount" @click="showFullPlan(scope.row)" class="el-icon-question"></span>
                </div>
              </template>
            </el-table-column>
            <!--<el-table-column prop="completeDate" label="入库时间" width="100" align="center" sortable>-->
              <!--<template slot-scope="scope">-->
                <!--<p style="width: 80px; text-align: center">{{scope.row.completeDate}}</p>-->
              <!--</template>-->
            <!--</el-table-column>-->

            <el-table-column prop="salePrice" label="方案价格" width="110" align="left">
              <template slot-scope="scope">
                <div>
                  <p>售卖价格:{{scope.row.salePriceChargeType == 1 ? scope.row.salePrice : '免费'}}</p>
                </div>
                <div>
                  <p>版&nbsp;&nbsp;权&nbsp;&nbsp;费:{{scope.row.chargeType == 1 ? scope.row.planPrice : '免费'}}</p>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="secrecyFlag" label="公开状态" align="center" width="80">
              <!-- && scope.row.isCheck == 1-->
              <template slot-scope="scope">
                <el-switch
                        class="switchStyle"
                        :disabled="showTrue(scope.row.origin) || isPer('solution:manage') && scope.row.isCheck == 1"
                        v-model="scope.row.secrecyFlag"
                        active-color="#ff6419"
                        inactive-icon-class="inactive-icon-class"
                        active-icon-class="active-icon-class"
                        :active-value="1"
                        inactive-color="#ddd"
                        :inactive-value="0"
                        @change="changeSwitch(scope.row)"
                ></el-switch>
                <!--<p>{{scope.row.secrecyFlag == 0 ? '未公开' : '公开'}}</p>-->

                <p>
                  {{scope.row.origin === 'share'? scope.row.statusSwitchTxt = '未公开' :scope.row.secrecyFlag == 0 ? scope.row.statusSwitchTxt = '未公开' :
                  scope.row.statusSwitchTxt ='公开'}}
                </p>
                <!-- <p>{{scope.row.secrecyFlag == 0 ? scope.row.statusSwitchTxt = '未公开' :
                scope.row.statusSwitchTxt ='公开'}}</p>-->
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="110px">
              <template slot-scope="scope" class="operationBtns">
                <!-- class="operatonBtn" -->
                <el-button
                        type="text"
                        size="mini"
                        style="margin-left: 0"
                        v-if="isPer('solution:edit')"
                        @click="gotoAddProduct(scope.row)"
                >编辑</el-button>
                <!-- <span class="operatonBtnTxt" v-if="isPer('solution:manage')"
                                       @click="handleAllot(scope.$index, scope.row)">公开
                </span>-->
                <!--<el-button-->
                <!--type="text"-->
                <!--style="margin-left: 0"-->
                <!--size="mini"-->
                <!--v-if="isPer('solution:del') &&classProgramme!='oneProgramme'"-->
                <!--@click="handleDelete(scope.$index, scope.row)"-->
                <!--&gt;删除</el-button>-->
                <div v-if=" parseInt(scope.row.isCheck) == 0">
                  <!--<span class="operatonBtnTxt"
                                          v-if="isPer('solution:delivery') && scope.row.origin !== 'deliver' && scope.row.origin!='share'"
                                          @click="handleDeliver(scope.row)">交付管理 sss
                  </span>-->
                  <el-button
                          type="text"
                          size="mini"
                          v-if="isPer('solution:delivery') && scope.row.origin !== 'deliver' && scope.row.origin!='share' && scope.row.origin === 'diy'"
                          @click="handleDeliver(scope.row)"
                  >交付管理</el-button>
                </div>
                <div>
                  <el-button
                          type="text"
                          size="mini"
                          v-if="isPer('solution:exhibition')"
                          @click="shelfManagement(scope.row)"
                  >上架管理</el-button>
                </div>
                <div>
                  <el-button
                          v-if="scope.row.secrecyFlag == 1&&isPer('check:salePrice:edit')"
                          type="text"
                          size="mini"
                          @click="showPice(scope.row,0)"
                  >设置售卖价格</el-button>
                </div>
                <div>
                  <span
                          v-if="isPer('check:planPriceCopyRight:edit')"
                          class="operatonBtnTxt"
                          @click="showPice(scope.row,1)"
                  >设置版权费</span>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="pageFrame">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="query.page"
                    :page-sizes="[10, 50, 100, 200,500]"
                    :page-size="query.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
          </div>
        </div>
      </div>
      <el-dialog title="上下架管理" :visible.sync="shelfDialogVisible" width="540px" top="30vh">
        <div>
          <el-checkbox :indeterminate="isIndeterminate" v-model="bothChecked" style="margin-left: 70px;margin-bottom: 10px" @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group style="width:400px;margin-left: 40px" v-model="shelf.targetPlatform" @change="handleCheckedCitiesChange">
            <el-checkbox
                    style="width:120px;margin-left: 30px"
                    v-for="(ds, index) in platformList"
                    :key="index"
                    :value="ds.id"
                    :label="ds.id"
            >{{ds.name}}</el-checkbox>
          </el-checkbox-group>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button
                  round
                  type="primary"
                  style="width: 80px"
                  size="medium"
                  class="confirmButtonClass"
                  @click="shelfConfirm"
          >确 定</el-button>
          <el-button
                  round
                  type="info"
                  style="width: 80px"
                  size="medium"
                  class="cancelButtonClass"
                  @click="shelfDialogVisible = false"
          >取 消</el-button>
        </span>
      </el-dialog>

      <el-dialog title="选择分配对象" :visible.sync="allotDialogVisible" width="20%" top="30vh" center>
        <el-checkbox-group class="center" v-model="allot.targetPlatform">
          <el-checkbox
                  v-for="(ds, index) in distributionStatus"
                  :key="index"
                  :label="ds.code"
          >{{ds.label}}</el-checkbox>
        </el-checkbox-group>
        <span slot="footer" class="dialog-footer">
          <el-button
                  round
                  type="primary"
                  style="width: 80px"
                  size="medium"
                  class="confirmButtonClass"
                  @click="handleAllotConfirm"
          >确 定</el-button>
          <el-button
                  round
                  type="info"
                  style="width: 80px"
                  size="medium"
                  class="cancelButtonClass"
                  @click="allotDialogVisible = false"
          >取 消</el-button>
        </span>
      </el-dialog>
      <el-dialog
              title="选择交付企业"
              :visible.sync="deliverDialogVisible"
              @close="delivery.receiveBrandIds = []"
              width="25%"
              top="30vh"
              center
      >
        <template>
          <el-radio v-model="radio" label="企业" @change="radioChange">企业</el-radio>
          <el-radio v-model="radio" label="品牌">品牌</el-radio>
        </template>
        <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="deliverSearch"></el-input>

        <el-checkbox-group v-model="delivery.receiveCompanyIds" v-if="radio === '企业'">
          <div class="deliver-box">
            <el-checkbox style="display: none"></el-checkbox>
            <el-checkbox
                    class="deliver-box-line"
                    @change="checkboxChanges"
                    v-for="(c, index) in searchCompanys"
                    :label="c.companyId"
                    :key="index"
            >{{c.companyName}}</el-checkbox>
          </div>
        </el-checkbox-group>

        <el-checkbox-group v-model="delivery.receiveBrandIds" v-if="radio === '品牌'">
          <div class="deliver-box">
            <el-checkbox style="display: none"></el-checkbox>
            <el-checkbox
                    class="deliver-box-line"
                    @change="checkboxChange"
                    v-for="(c, index) in searchDrand"
                    :label="c.brandId"
                    :key="index"
            >{{c.brandName}}</el-checkbox>
          </div>
        </el-checkbox-group>

        <span slot="footer" class="dialog-footer">
          <el-button round type="primary" @click="handleDeliverConfirm">确 定</el-button>
          <el-button
                  round
                  type="info"
                  @click="deliverDialogVisible = false; deliverSearch = '';delivery.receiveBrandIds = []"
          >取 消</el-button>
        </span>
      </el-dialog>
    </div>
    <!--注意有2个地方公用-->
    <el-dialog :title="title" :visible.sync="dialogOnekeySecercy">
      <el-table
              :data="onekeySecrecy"
              border
              header-cell-class-name="hedStyle"
              v-loading="onekeySecrecyLoading"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(255, 255, 255, 0.8)"
      >
        <el-table-column v-for="(item,index) in productTitleList" :key="index" :prop="item.prop" :label="item.name"></el-table-column>
      </el-table>
      <div class="pageFrame">
        <el-pagination
                @size-change="secrecySizeChange"
                @current-change="secrecyCurrentChange"
                :current-page.sync="currentPage"
                :page-sizes="[10, 50, 100, 200,500]"
                :page-size="10"
                layout="total, sizes, prev, pager, next, jumper"
                :total="secrecyTotal"
        ></el-pagination>
      </div>
    </el-dialog>

    <el-dialog title="已交付企业" :visible.sync="deliveredCompanysDialogVisible">
      <el-table
              :data="deliveredCompanys"
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
                :total="deliverDiaTotal"
        ></el-pagination>
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
            <p style="margin: 10px 0 0 94px;">
              <span style="color:red;">*</span>度币：
              <!--@keyup.native="number"-->
              <el-input style="width:130px;" maxlength="5" v-model="planFee.cash"></el-input>
              <span class="huansuanImg"></span>
              人民币(元)：{{planFee.cash == 0?
              planFee.cash:planFee.cash/ 10}}
            </p>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" style="border-top:none;">
        <el-button
                round
                type="primary"
                style="width: 80px"
                size="medium"
                class="confirmButtonClass"
                @click="commitPrice"
        >确 定</el-button>
        <el-button
                round
                type="info"
                style="width: 80px"
                size="medium"
                class="cancelButtonClass"
                @click="showPriceDialog=false"
        >取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog title="已交付企业" :visible.sync="deliverDialogViewVisible" width="30%">
      <template>
        <el-row :gutter="20" v-if="currentDeliverViewData && currentDeliverViewData.length > 0">
          <el-col :span="24">已交付企业数量：{{currentDeliverViewData.length}}</el-col>
          <el-col :span="8" v-for="(item, index) in currentDeliverViewData" :key="index">
            <div class="delivery-overflow" :title="item.companyName">{{item.companyName}}</div>
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
    import CommonDesignPlan from "../common/CommonDesignPlan.vue";
    import ContainsNotOpenProduct from "../../../base/plan/ContainsNotOpenProduct.vue";
    import qs from "qs";
    import ImgPreView from "@/components/Img-preview";
    import {map} from 'lodash'
    const distributionStatus = [
        {
            code: "2b",
            label: "渠道管理"
        },
        {
            code: "2c",
            label: "线上管理"
        }
    ];
    export default {
        inject: ["reload"],
        components: {
            ContainsNotOpenProduct,
            CommonDesignPlan,
            ImgPreView
        },
        watch: {
            // 做交付公司的搜素
            deliverSearch(val) {
                if (this.radio == "企业") {
                    if (val) {
                        this.searchCompanys = this.companys.filter(c =>
                            c.companyName.includes(val)
                        );
                    } else {
                        this.searchCompanys = this.companys;
                    }
                }
                if (this.radio == "品牌") {
                    if (val) {
                        console.log();
                        this.searchDrand = this.brand.filter(c => c.brandName.includes(val));
                    } else {
                        this.searchDrand = this.brand;
                    }
                }
            }
        },
        computed:{
            productTitleList(){
                if(this.title=='未公开产品'){
                    return this.headerTitleList.product;
                }else {
                    return this.headerTitleList.fullPlan;
                }
            }
        },
        data() {
            return {
                headerTitleList:{
                    product:[{ type:0,name:'产品编号',prop:'productCode' }, { type:0,name:'产品名称',prop:'productName' }],
                    fullPlan:[{ type:1,name:'方案ID',prop:'fullPlanId' }, { type:1,name:'方案名称',prop:'fullPlanName' }],
                },
                title:'未公开产品',
                isIndeterminate:true,
                bothChecked:false,
                applyAreaList:[],
                planFee: {
                    cash: 0,
                    planId: "",
                    chargeMode: 0
                },
                priceDialogTitle: "设置售卖价格",
                RMBChange: 0,
                Priceform: {
                    isFree: "免费"
                },
                showPriceDialog: false,
                classProgramme: sessionStorage.getItem('classProgramme') || "oneProgramme",
                isAllProgramme: false,
                deliveredCompanys: [],
                deliveredCompanysLoading: false,
                deliverDiaTotal: 0,
                deliveredCompanysDialogVisible: false,
                deliveredPlanId: 0,
                currentPage: 1,
                deliveredDiaQuery: {
                    page: 0,
                    limit: 10
                },
                isShare: false,
                planLoading: true,
                onekeySecrecyLoading: true,
                onekeySecrecy: [],
                dialogOnekeySecercy: false,
                deliverDialogVisible: false,
                activeName: "first",
                total: 0,
                secrecyTotal: 0,
                allotDialogVisible: false,
                distributionStatus: distributionStatus,
                delivery: {
                    receiveCompanyIds: [],
                    receiveBrandIds: [],
                    delivererId: qs.parse(sessionStorage.getItem("loginUser")).id,
                    deliveryCompanyId: sessionStorage.getItem("companyID")
                },
                disabledDeliverCompanyIds: [],
                disabledDeliverBrandIds: [],
                allot: {
                    targetPlatform: []
                },
                confirmAllotMsg: "确定要分配么?",
                confirmDeliverMsg: "确定要交付么?",
                limits: 10,
                pages: 0,
                deliverSearch: "",
                query: {
                    page: 1,
                    limit: 10,
                    planStyleId: null,
                    planGroupStyleId: null,
                    companyId: sessionStorage.getItem("companyID"),
                    id:null,
                    applyAreaValue:null
                },
                list: [],
                sels: [],
                radio: "企业",
                companysBrand: {},
                brandDrop: [],
                companys: [],
                brand: [],
                searchCompanys: [],
                searchDrand: [],
                batchAllotFlag: false,
                productPlanId: 0,
                spaceCommonName: [], // 空间类型
                isAllDelivery: false,
                isReturn: true,
                platformList: [],
                isOneKey: true,
                shelfDialogVisible: false,
                shelf: {
                    targetPlatform: []
                },
                /*
                            designPlanType (string, optional): 方案类型(1:普通方案;2-智能方案) ,
                            planIds (Array[integer], optional): 方案id集合 ,
                            platformIds (string, optional): OPEN(公开方案),ONEKEY(一键方案),TEMPLATE(样板方案),2c平台ids
                          */
                shelfParams: {
                    designPlanType: 2,
                    planIds: [],
                    platformIds: 0
                },
                platformNamesShow: [],
                planStyles: [
                    {
                        styleName: "",
                        styles: [
                            {
                                styleId: 0,
                                styleName: ""
                            }
                        ]
                    }
                ],
                originDeliver: {
                    companies: [],
                    brands: []
                },
                deliverDialogViewVisible: false,
                currentDeliverViewData: [],
                designerList: []
            };
        },
        methods: {
            spanceTypeSelect(val){
                if(val){
                    this.query.spaceCommonName=this.spaceCommonName.filter(res=>{ return val==res.valueKey })[0].name;
                    this.API2.getApplyArea({type:val}).then(res=>{
                        this.applyAreaList=res.data;
                    })
                }else {
                    this.query.spaceCommonName='';
                    this.applyAreaList=[];
                }
                this.query.applyAreaValue='';
            },
            applyAreaFocus(){
                if(this.applyAreaList.length==0){
                    this.$message.error('请选择空间类型');
                }
            },
            handleCheckAllChange(val){
                this.shelf.targetPlatform=val ? map(this.platformList,'id') : [];
                this.isIndeterminate = false;
            },
            handleCheckedCitiesChange(value){
                let checkedCount = value.length;
                this.bothChecked = checkedCount === map(this.platformList,'id').length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < map(this.platformList,'id').length;
            },
            exportExcel() {
                if (this.sels.length === 0) {
                    this.$message.error("请选择一条记录");
                } else {
                    this.query.planIds = this.sels.map(res => res.planId).join(",");
                    this.API2.exportListExcelByOnekey(this.query).then(res => {
                        let blob = new Blob([res], {
                            type: "application/vnd.ms-excel;charset=utf-8"
                        });
                        let url = URL.createObjectURL(blob);
                        let link = document.createElement("a");
                        link.style.display = "none";
                        link.href = url;
                        link.setAttribute("download", "智能方案.xls");
                        document.body.appendChild(link);
                        link.click();
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
                localStorage.setItem("breadcrumb3", "方案编辑");
                this.$router.push(`/plan/onekey/${item.planId}/edit`);
            },
            number() {
                this.planFee.cash = this.planFee.cash.replace(/[^\ .\d]/g, "");
                this.planFee.cash = this.planFee.cash.replace(".", "");
                this.planFee.cash = this.planFee.cash.replace(/\s+/g, "");
            },
            showTrue(shareType) {
                if (shareType === "huxing" || shareType === "diy") {
                    console.log(shareType);
                    return false;
                } else {
                    return true;
                }
            },
            clearcash() {
                this.planFee.cash = 0;
            },
            isVendor() {
                //判断当前用户是否为厂商,是=>{} 不显示设置版权费按钮 否=>{} 显示
                let userType = qs.parse(sessionStorage.getItem("loginUser")).userType;
                if (userType == 2) {
                    return false;
                }
                return true;
            },
            commitPrice() {
                //判断用户输入的金额是否包含小数
                if (String(this.planFee.cash).indexOf(".") > 0) {
                    this.$message.warning("不能输入小数");
                    return;
                }
                if (this.priceDialogTitle == "设置售卖价格") {
                    let formData = this.buildPlanFeeHttpFormData(
                        "salePrice",
                        "salePriceChargeType",
                        "planId"
                    );
                    this.showPriceDialog = false;
                    this.API.setOneKeySalePrice(formData).then(res => {
                        this.$message({
                            type: "success",
                            message: "设置成功!"
                        });
                        this.page();
                    });
                } else {
                    //设置版权费
                    let formDate = this.buildPlanFeeHttpFormData(
                        "planPrice",
                        "chargeType",
                        "planId"
                    );
                    this.showPriceDialog = false;
                    this.API.setOneKeyPlanPrice(formDate).then(res => {
                        this.$message({
                            type: "success",
                            message: "设置成功!"
                        });
                        this.page();
                    });
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
            showPice(row, type) {
                if (type == 0) {
                    //设置方案售卖价格
                    if (typeof row.salePrice != "undefined") {
                        this.planFee.cash = row.salePrice;
                    } else {
                        this.planFee.cash = 0;
                    }
                    this.planFee.planId = row.planId;
                    this.planFee.chargeMode = row.salePriceChargeType;
                    this.showPriceDialog = true;
                    this.priceDialogTitle = "设置售卖价格";
                } else if (type == 1) {
                    //设置版权费价格
                    if (typeof row.planPrice != "undefined") {
                        this.planFee.cash = row.planPrice;
                    } else {
                        this.planFee.cash = 0;
                    }
                    this.planFee.planId = row.planId;
                    this.planFee.chargeMode = row.chargeType;
                    this.showPriceDialog = true;
                    this.priceDialogTitle = "设置方案版权费";
                    console.log(
                        this.planFee.cash,
                        this.planFee.chargeMode,
                        this.priceDialogTitle
                    );
                }
            },
            getBrandDropList() {
                this.API.ProductBrandDrop({
                    companyId: sessionStorage.getItem("companyID")
                }).then(res => {
                    if (res.data) {
                        this.brandDrop = res.data;
                    }
                });
            },
            //图片预览事件
            ImgView(ImgURL) {
                this.$refs.sonParams.sonFun({ ImgURL: ImgURL, ImgShow: true });
            },
            isPer(per, shareType) {
                let permiss = qs.parse(sessionStorage.getItem("loginUser"));
                return this.isMainPer(per, permiss);
            },
            // 批量公开
            batchSecrecy() {
                if (this.sels.length === 0) {
                    this.$message.error("请选择一条记录");
                    return;
                }
                let idsArr = [];
                this.sels.map(sel => {
                    idsArr.push(sel.planId);
                });
                if (this.sels.filter(item => item.isCheck == "1").length > 0) {
                    this.$message.error("选择的记录中包含有未审核的方案");
                    return;
                }
                this.$confirm("确认批量公开吗", "提示", {
                    cancelButtonClass: "cancelButtonClass",
                    confirmButtonClass: "confirmButtonClass",
                    type: "warning",
                    center: true
                }).then(res => {
                    this.API.planSecrecy({
                        planIds: idsArr,
                        secrecyFlag: 1
                    }).then(res => {
                        this.$message.success("批量公开成功");
                        this.page();
                    });
                });
            },
            batchCancelSecrecy() {
                if (this.sels.length === 0) {
                    this.$message.error("请选择一条记录");
                    return;
                }
                this.$confirm("确认批量取消公开吗", "提示", {
                    cancelButtonClass: "cancelButtonClass",
                    confirmButtonClass: "confirmButtonClass",
                    type: "warning",
                    center: true
                }).then(res => {
                    let idsArr = [];
                    this.sels.map(sel => {
                        idsArr.push(sel.planId);
                    });
                    this.API.planSecrecy({
                        planIds: idsArr,
                        secrecyFlag: 0
                    }).then(res => {
                        this.page();
                        this.$message.success("批量取消公开成功");
                    });
                });
            },
            changeSwitch(row) {
                if (row.isCheck == "1") {
                    this.$message.error("方案未审核");
                    return;
                }
                if (row.secrecyFlag) {
                    row.statusSwitchTxt = "未公开";
                    this.API.planSecrecy({
                        planIds: [row.planId],
                        secrecyFlag: 1
                    }).then(res => {});
                } else {
                    row.statusSwitchTxt = "公开";
                    this.API.planSecrecy({
                        planIds: [row.planId],
                        secrecyFlag: 0
                    }).then(res => {});
                }
            },

            shelfManagement(row) {
                // 上架管理 platformIds
                // this.shelf.targetPlatform = [...(row.platformIds ? [row.platformIds] : [])]
                if (row.origin == "share") {
                    this.isShare = true;
                } else {
                    this.isShare = false;
                }
                console.log(row,'row')
                if (row.designStyleName) {
                    let twoc = row.platformNames.split(",");
                    let ids = row.platformIds.split(",");
                    let sel = [];
                    for (let i = 0; i < twoc.length; i++) {
                        ids[i] && ids[i]!=3 && ids[i]!=5 ? sel.push(ids[i]) : '';  //去除品牌2C-网站,2C-移动端
                    }
                    this.shelf.targetPlatform = sel;
                    this.bothChecked = this.shelf.targetPlatform.length === map(this.platformList,'id').length;
                    this.isIndeterminate = this.shelf.targetPlatform.length > 0 && this.shelf.targetPlatform.length < map(this.platformList,'id').length;
                    this.shelfParams.planIds = [];
                    this.shelfParams.planIds.push(row.planId);
                    this.shelfDialogVisible = true;
                } else {
                    this.$message.warning("请选择方案的风格再上架");
                }
            },
            // 确认上架
            sureShelf() {
                if (this.shelf.targetPlatform) {
                    this.shelfParams.platformIds = this.shelf.targetPlatform.join(",");
                }
                if (this.isShare) {
                    this.shelfParams.platformIds.split(",").every((value, index) => {
                        if (value == "ONEKEY") {
                            //console.log(value)// 4
                            this.isOneKey = false;
                            return false;
                        } else {
                            //  console.log(value)// 1 2 3
                            this.isOneKey = true;
                            return true;
                        }
                    });
                }
                let sharesPlan = false;

                this.sels.filter(item => {
                    if (
                        item.origin.includes("share") &&
                        this.shelfParams.platformIds.includes("ONEKEY")
                    ) {
                        sharesPlan = true;
                    }
                });
                if (sharesPlan) {
                    this.$message.warning("分享方案不能上架一键方案！");
                    return;
                }
                if (!this.isOneKey) {
                    this.$message.warning("分享方案不能上架一键方案！");
                    return;
                }
                this.API.contentLibraryPlanShelf(this.shelfParams).then(res => {

                    this.shelfDialogVisible = false;
                    this.page();
                    this.shelfParams.planIds = [];
                    this.shelf.targetPlatform=[];
                    this.isIndeterminate = true;
                    this.$message.success("上架成功");
                });
            },
            shelfConfirm() {
                if (this.sels) {
                    this.sels.map(ids => {
                        this.shelfParams.planIds.push(ids.planId);
                    });
                }
                this.sureShelf();
            },
            // 获取上架平台列表
            getShelfPlatformList() {
                this.API.platformShelf({
                    menuType: 1
                }).then(res => {
                    if (res) {
                        this.platformList = res.data;
                        console.log(this.platformList);
                    }
                });
            },
            // 空间类型
            getSpaceCommonName() {
                this.API.FitTypeHose().then(res => {
                    this.spaceCommonName = res.data;
                    console.log(res, "FitTypeHose");
                });
            },
            refreshData() {
                this.$nextTick(function() {
                    this.planLoading = true;
                    this.page();
                });
            },
            GoPlan() {
                if(this.classProgramme=='allProgramme'){ //全屋方案
                    this.$emit("GetisFullHousePlan", false);
                }else {
                    this.classProgramme!='oneProgramme' ? this.query.queryType=this.classProgramme : delete this.query.queryType;
                    this.query.page=1;
                    this.page();
                }
            },
            searchPage() {
                this.query.page = 1;
                this.page();
            },
            page() {
                this.API.OneKeyDesignPlanList(this.query).then(resp => {
                    if (resp.code == 204 || resp.code >= 500) {
                        this.planLoading = false;
                    }
                    if (resp.data) {
                        this.planLoading = false;
                        this.list = resp.data.list.map(it => {
                            if (it.origin === "deliver" || it.origin === "share") {
                                it.secrecyFlag = 0;
                            }
                            // this.platformNamesShow = it.platformNames.split(',')
                            // return it.platformNames ? Object.assign({}, it, {platformNamesShow: it.platformNames.split(',')}) : it
                            return it;
                        });
                        console.log("-----------------", this.list);
                        this.total = resp.data.total;
                    } else {
                        this.list = [];
                        this.total = 0;
                    }
                });
            },
            showFullPlan(row){
                this.title='关联的全屋方案信息';
                this.dialogOnekeySecercy = true;
                this.productPlanId = row.planId;
                this.pages = 0;
                this.currentPage = 1;
                this.getWithFullPlan();
            },
            showSecrecyList(row) {
                if (row) {
                    this.title='未公开产品';
                    this.dialogOnekeySecercy = true;
                    this.productPlanId = row.planId;
                    this.pages = 0;
                    this.currentPage = 1;
                    this.ProductSecrecyList();
                }
            },
            getWithFullPlan(){
                this.API2.getWithFullPlan({
                    page:this.pages,
                    limit:this.limits,
                    planId:this.productPlanId
                }).then(res=>{
                    this.onekeySecrecyLoading = false;
                    var list =
                        this.onekeySecrecy = res.data.map((item,index)=>{
                            return item[0];
                        });
                    this.secrecyTotal = res.total;
                })
            },
            showDeliveredCompanys(row) {
                this.deliveredCompanysDialogVisible = true;
                this.deliveredCompanysLoading = true;
                this.deliveredPlanId = row.planId;
                this.listDeliveredCompanys();
            },
            listDeliveredCompanys() {
                this.API.ListDeliveredCompanys(
                    this.deliveredPlanId,
                    this.deliveredDiaQuery
                ).then(res => {
                    this.deliveredCompanys = res.list;
                    this.deliverDiaTotal = res.total;
                    this.deliveredCompanysLoading = false;
                });
            },
            deliverDiaSizeChange(val) {
                this.deliveredDiaQuery.limit = val;
                this.listDeliveredCompanys();
            },
            deliverDiaCurrentChange(val) {
                this.deliveredDiaQuery.page = val;
                this.listDeliveredCompanys();
            },
            ProductSecrecyList() {
                this.API.ListOneKeyPlanProducts({
                    planId: this.productPlanId,
                    secrecy: 0,
                    page: this.pages,
                    limit: this.limits
                }).then(res => {
                    this.onekeySecrecyLoading = false;
                    this.onekeySecrecy = res.data;
                    this.secrecyTotal = res.total;
                });
            },
            handleSelectionChange(sels) {
                this.sels = sels;
                console.log(this.sels, "selsd");
            },
            handleDetail(index, row) {
                localStorage.setItem("breadcrumb3", "方案详情");
                this.$store.commit("breadcrumb3", "方案详情");
                this.$router.push(`/plan/onekey/${row.planId}`);
                localStorage.setItem("detailPlanType", "0");
                localStorage.setItem("isNowPlanType", false); //当前方案类型：true 全屋方案  false 智能方案
            },

            handleAllot(index, row) {
                // 方案分配 打开弹窗
                if (row.containsSecrecyFlag === "0") {
                    this.confirmAllotMsg = "选定的方案包含未公开产品,确定要分配么?";
                }
                this.allot.planId = row.planId;
                this.allotDialogVisible = true;
                this.batchAllotFlag = false;
                if (row.distributionStatus) {
                    this.allot.targetPlatform = row.distributionStatus;
                } else {
                    this.allot.targetPlatform = [];
                }
            },
            handleAllotConfirm() {
                // 确定交付到某个平台
                this.$confirm(this.confirmAllotMsg, "提示", {
                    cancelButtonClass: "cancelButtonClass",
                    confirmButtonClass: "confirmButtonClass",
                    type: "warning",
                    center: true
                }).then(() => {
                    if (this.batchAllotFlag) {
                        const params = this.sels.map(item => ({
                            planId: item.planId,
                            targetPlatform: this.allot.targetPlatform
                        }));
                        this.API.OneKeyPlanBatchAllot(params).then(resp => {
                            this.allotDialogVisible = false;
                            this.page();
                            this.$message.success("分配成功");
                        });
                    } else {
                        this.API.OneKeyPlanAllot(this.allot).then(resp => {
                            this.allotDialogVisible = false;
                            this.page();
                            this.$message({
                                message: "分配成功",
                                type: "success"
                            });
                        });
                    }
                });
            },
            // 批量上架
            handleBatchAllot() {
                this.shelf.targetPlatform = [];
                if (this.sels.length === 0) {
                    this.$message.error("请选择一条记录");
                } else {
                    /*this.allotDialogVisible = true
                                this.batchAllotFlag = true*/
                    if (
                        this.sels.some(res => {
                            return res.designStyleName == undefined;
                        })
                    ) {
                        this.$message.warning("请选择方案的风格再上架");
                    } else {
                        this.shelfDialogVisible = true;
                    }
                }
                console.log(this.sels);
            },
            checkboxChanges() {
                console.log(this.delivery.receiveCompanyIds);
            },
            /*当选择上品牌时提交参数的企业id(this.delivery.receiveCompanyIds)为选中品牌下所属的企业id
             * 当没有选上任意一个品牌的话企业id(this.delivery.receiveCompanyIds)为原来值
             * */
            checkboxChange() {
                try {
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
                } catch (error) {
                    console.log(error);
                }
                console.log(this.delivery.receiveCompanyIds);
            },
            /*但选择企业单选切换的时候清空已选的品牌id
             * 恢复原来企业id的值
             * */
            radioChange() {
                this.delivery.receiveBrandIds = [];
                this.delivery.receiveCompanyIds = this.disabledDeliverCompanyIds;
            },
            // 交付
            handleDeliver(row) {
                if (this.sels.filter(item => item.isCheck == "1").length > 0) {
                    this.$message.error("选择的记录中包含有未审核的方案");
                    return;
                }
                this.isAllDelivery = false;
                this.deliverDialogVisible = true;
                this.delivery.designPlanId = row.planId;
                this.deliverSearch = "";
                if (row.containsSecrecyFlag === "0") {
                    this.confirmDeliverMsg = "选定的方案包含未公开产品,确定要交付么?";
                }
                // 获取需要交付的公司  查是否被交付过
                // this.API.ListCompanysWithDelivered({
                //     companyId: sessionStorage.getItem('companyID'),
                //     planId: row.planId
                // }).then((res) => {
                //     this.companys = res.data.companyInfo
                //     this.brand = res.data.brandInfo;
                //     // for (let i = 0; i < this.brand.length; i++) {
                //     //   this.brand[i].index = i
                //     // }
                //     // console.log(this.brand)
                //     this.disabledDeliverCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
                //     this.delivery.receiveCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
                //
                //     this.searchCompanys = this.companys
                //     // this.disabledDeliverBrandIds = this.brand.filter(it => it.delivered).map(it => it.brandId)
                //     // this.delivery.receiveBrandIds = this.brand.filter(it => it.delivered).map(it => it.brandId)
                //     this.searchDrand = this.brand
                //     // console.log(this.delivery.receiveCompanyIds, '企业');
                //     // console.log(this.delivery.receiveBrandIds, '品牌id');
                // })

                // 凉凉~~~~优化功能，比以前快了-2s，O(∩_∩)O哈哈~
                // Modified by songjianming@sanduspace.cn on 2018/12/28
                // 商家后台方案交付优化及增加设计风格筛选条件(http://192.168.1.201:8080/browse/CMS-691)
                // 方案交付调整 => 方案交付管理，只显示未交付过的企业，不需要显示已交付的企业，已交付的企业不能修改
                let excludes = (row.delivers || []).map(item => {
                    return item.companyId;
                });
                if (excludes && excludes.length > 0) {
                    // 企业
                    this.companys = this.getCompanyWithDelivered(
                        this.originDeliver.companies,
                        excludes,
                        true
                    );
                    // 品牌
                    this.brand = this.getCompanyWithDelivered(
                        this.originDeliver.brands,
                        excludes,
                        false
                    );
                } else {
                    Object.assign(this.companys, this.originDeliver.companies);
                    Object.assign(this.brand, this.originDeliver.brands);
                }

                this.disabledDeliverCompanyIds = excludes;
                this.delivery.receiveCompanyIds = excludes;
                this.searchCompanys = this.companys;
                this.searchDrand = this.brand;
            },
            doCleanupDeliveredView() {
                this.searchCompanys = [];
                this.searchDrand = [];
            },
            getCompanyWithDelivered(list, excludes, type) {
                if (type) {
                    return list.filter(item => {
                        return excludes.indexOf(item.companyId) < 0;
                    });
                } else {
                    return list.filter(item => {
                        return excludes.indexOf(item.brandCompanyId) < 0;
                    });
                }
            },
            // 点击确定交付
            handleDeliverConfirm() {
                /* if (this.delivery.receiveCompanyIds.length <= 0) {
                             this.$message.error('请选择一个公司')
                             return
                           }*/
                this.$confirm(this.confirmDeliverMsg, "提示", {
                    type: "warning"
                }).then(() => {
                    if (!this.isAllDelivery) {
                        this.API.OneKeyPlanDeliver(this.delivery).then(resp => {
                            this.deliverDialogVisible = false;
                            this.page();
                            this.$message({
                                type: "success",
                                message: "交付成功!"
                            });
                        });
                    } else {
                        console.log("allDelivery");
                        this.API.allDelivery(this.delivery).then(res => {
                            console.log(res);
                            this.deliverDialogVisible = false;
                            this.deliverSearch = "";
                            this.page();
                            this.$refs.multipleTable.clearSelection();
                            this.$message({
                                type: "success",
                                message: "交付成功!"
                            });
                        });
                    }

                    // 清空View
                    // this.doCleanupDeliveredView();
                });
            },
            // 批量交付
            batchDelivery() {
                this.isAllDelivery = true;
                if (this.sels.length === 0) {
                    this.$message.error("请选择一条记录");
                    return;
                }
                if (this.sels.filter(item => item.isCheck == 1).length > 0) {
                    this.$message.error("选择的记录中包含有未审核的方案");
                    return;
                }
                let arr = [];
                try {
                    for (let i = 0; i < this.sels.length; i++) {
                        if (
                            this.sels[i].origin == "diy" ||
                            this.sels[i].origin == "huxing" ||
                            this.sels[i].origin == "" ||
                            this.sels[i].origin == null
                        ) {
                            this.isReturn = false;
                        } else {
                            this.isReturn = true;
                        }
                        arr.push(this.sels[i].planId);
                        // if (this.sels[i].containsSecrecyFlag === '0') {
                        //   // this.confirmDeliverMsg = '选定的方案包含未公开产品,确定要交付么?'
                        // }
                    }
                } catch (error) {
                    console.log(error);
                }

                console.log(this.isReturn, "11");
                if (this.isReturn) {
                    this.$message({
                        type: "error",
                        message: "只能交付来源为内部制作的方案"
                    });
                    return;
                }
                this.deliverDialogVisible = true;
                this.delivery.designPlanId = arr;
                this.API.ListCompanysWithDelivered({
                    companyId: sessionStorage.getItem("companyID"),
                    planId: -1
                }).then(res => {
                    this.companys = res.data.companyInfo;
                    this.brand = res.data.brandInfo;
                    this.disabledDeliverCompanyIds = this.companys
                        .filter(it => it.delivered)
                        .map(it => it.companyId);
                    this.delivery.receiveCompanyIds = this.companys
                        .filter(it => it.delivered)
                        .map(it => it.companyId);
                    this.searchCompanys = this.companys;
                    // this.disabledDeliverCompanyIds = this.brand.filter(it => it.delivered).map(it => it.brandCompanyId)
                    // this.delivery.receiveCompanyIds = this.brand.filter(it => it.delivered).map(it => it.brandCompanyId)
                    this.searchDrand = this.brand;
                });
            },
            handleEdit(index, row) {
                this.$router.push(`/plan/onekey/${row.planId}/edit`);
            },
            handleDelete(index, row) {
                var self=this;
                // window.test1=this.test2;
                //** this.$confirm("<p>此操作将永久删除, 是否继续?</p><div onclick='test1()' style='color: #ff6419;position: absolute;right: 20px;bottom: -50px;cursor: pointer'>导出全屋方案</div>", "提示", {
                this.$confirm("此操作将永久删除, 是否继续?", "提示", {
                    dangerouslyUseHTMLString: true,
                    center: true,
                    cancelButtonClass: "cancelButtonClass",
                    confirmButtonClass: "confirmButtonClass",
                    type: "warning"
                }).then(index => {
                    this.API.OneKeyDesignPlanDelete(row.planId).then(resp => {
                        this.page();
                        this.$message({
                            type: "success",
                            message: "删除成功!"
                        });
                    });
                });
            },
            batchDelete() {
                const planIds = this.sels.map(sel => sel.planId).join(",");
                if (planIds) {
                    this.$confirm("此操作将永久删除, 是否继续?", "提示", {
                        center: true,
                        cancelButtonClass: "cancelButtonClass",
                        confirmButtonClass: "confirmButtonClass",
                        type: "warning"
                    }).then(index => {
                        this.API.OneKeyDesignPlanBatchDelete(planIds).then(resp => {
                            this.page();
                            this.$message({
                                type: "success",
                                message: "删除成功!"
                            });
                        });
                    });
                } else {
                    this.$message.error("请选择一条或多条记录再做删除");
                }
            },
            // 未公开产品分页模块
            secrecySizeChange: function(size) {
                if(this.title='未公开产品'){
                    this.limits = size;
                    this.ProductSecrecyList();
                }else {
                    this.limits = size;
                    this.getWithFullPlan();
                }
            },
            secrecyCurrentChange: function(currentPage) {
                if(this.title='未公开产品'){
                    this.pages = currentPage - 1;
                    this.currentPage = currentPage;
                    this.ProductSecrecyList();
                }else {
                    this.pages = currentPage - 1;
                    this.currentPage = currentPage;
                    this.getWithFullPlan();
                }
            },

            // 分页事件
            handleSizeChange: function(size) {
                this.query.limit = size;
                this.page();
            },
            handleCurrentChange: function(currentPage) {
                this.planLoading = true;
                this.query.page = currentPage;
                this.page();
            },
            handleClearQuery() {
                this.applyAreaList=[];
                this.query = {
                    page: 1,
                    limit: 10,
                    companyId: sessionStorage.getItem("companyID")
                };
                try {
                    this.page();
                } catch (e) {
                    console.error(e);
                }
                console.log("----------------------");
            },
            getCompanys() {},
            initPlanStyle() {
                this.API.listDesignPlanStyle().then(resp => {
                    this.planStyles = resp.data || [];
                });
            },
            initDelivered() {
                this.API.ListCompanysWithDelivered({
                    companyId: sessionStorage.getItem("companyID"),
                    planId: -1
                }).then(res => {
                    this.originDeliver.companies = res.data.companyInfo;
                    this.originDeliver.brands = res.data.brandInfo;
                });
            },
            getDeliveryView(row) {
                this.deliverDialogViewVisible = true;
                this.currentDeliverViewData = row.delivers || [];
                // this.
            },
            initDesigner() {
                this.API2.getDesignerList({
                    companyId: sessionStorage.getItem("companyID"),
                    recommendedType: 2
                }).then(res => {
                    this.designerList = res.data;
                });
            }
        },
        activated() {
            this.getShelfPlatformList();
            this.page();
            this.getSpaceCommonName();
            //this.showSecrecyList()
        },
        created() {
            sessionStorage.getItem('classProgramme') ?  this.query.queryType=this.classProgramme : delete this.query.queryType;
            this.getShelfPlatformList();
            this.page();
            // this.page()
            this.getSpaceCommonName();
            this.getBrandDropList();
            this.initPlanStyle();
            this.initDelivered();
            localStorage.setItem("isNowPlanType", false); //当前方案类型：true 全屋方案  false 智能方案
            this.initDesigner();
        }
    };
</script>
<style lang="scss" scoped>
  @import "../../../../assets/css/mixin";

  .searchCondition {
    padding-left: 30px;
    padding-bottom: 20px;
    .el-select,
    .el-input {
      width: 210px;
    }
    .el-col {
      //width: 200px;
      padding-top: 10px;
     // margin-right: 15px;
    }
  }

  .operatonBtnTxt {
    color: #ff6419;
    cursor: pointer;
    padding-right: 10px;
  }

  .operationBtns {
    .el-button {
      margin-left: 0 !important;
    }
  }

  .center {
    text-align: center;
  }

  .question {
    &:hover {
      cursor: pointer;
      color: $btnOrange;
    }
  }

  .operatonBtn {
    display: inline-block;
    margin-bottom: 8px;
    margin-left: 0 !important;
    font-size: 14px;
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
    // padding: 20px;
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
    .allocation {
      background: url("../../../../assets/images/icons/allot.png") 0 center
      no-repeat;
      &:hover {
        background: url("../../../../assets/images/icons/allot2.png") 0 center
        no-repeat;
      }
    }
    .delivery {
      background: url("../../../../assets/images/icons/delivery.png") 0 center
      no-repeat;
      &:hover {
        background: url("../../../../assets/images/icons/delivery1.png") 0 center
        no-repeat;
      }
    }
    .delete {
      background: url("../../../../assets/images/icons/delete.png") 0 center
      no-repeat;
      &:hover {
        background: url("../../../../assets/images/icons/delete2.png") 0 center
        no-repeat;
      }
    }
    .refresh {
      background: url("../../../../assets/images/icons/reflesh.png") 0 center
      no-repeat;
      &:hover {
        background: url("../../../../assets/images/icons/reflesh2.png") 0 center
        no-repeat;
      }
    }
    .export {
      background: url("../../../../assets/images/icons/btn_export_nor.png") 0
      center no-repeat;
      &:hover {
        background: url("../../../../assets/images/icons/btn_export_pre.png") 0
        center no-repeat;
      }
    }
    button {
      color: #666;
      font-size: 14px;
      margin-right: 10px;
      padding-left: 25px;
      &:hover {
        color: $overButtonOrange;
      }
    }
  }

  .planList {
    border-top: solid 1px #eee;
    @extend .planLibrary;
    .fubenImg {
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
    background: url("../../../../assets/images/icons/icon-huansuan.png") no-repeat;
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
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-left: 10px;
    margin-top: 5px;
    font-size: 14px;
    background-color: #f1f1f1;
    border-radius: 2px;
    padding: 2px;
    text-align: center;
  }
</style>