<template>
  <div>
    <!-- 标签列表页(0) -->
    <template v-if="pageStatus==0">
      <headers></headers>
      <searchBar class="t_join" :searchs="searchs" :onSearch="getUserTagsList" :pageData="pageData"></searchBar>
      <el-row class="t_row">
        <el-button type="primary" @click="pageStatus=1">新增用户标签</el-button>
        <el-button type="primary" @click="pageStatus=4">元件信息</el-button>
      </el-row>
      <el-row class="t_row t_join">
        <el-table :data="pageList" :header-cell-style="rowStyle" ref="refTable" v-loading="loading">
          <el-table-column prop="index" width="90" label="序号" align="center">
            <template slot-scope="scope">{{ scope.row.index }}</template>
          </el-table-column>
          <el-table-column
            prop="labelName"
            label="标签名称"
            align="center"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">{{ scope.row.labelName }}</template>
          </el-table-column>
          <el-table-column prop="labelRules" label="标签规则" align="center">
            <template slot-scope="scope">{{ scope.row.labelRules }}</template>
          </el-table-column>
          <el-table-column
            prop="labelState"
            label="标签说明"
            align="center"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">{{ scope.row.labelState }}</template>
          </el-table-column>
          <el-table-column prop="priority" label="优先级" align="center">
            <template slot-scope="scope">{{ scope.row.priority }}</template>
          </el-table-column>
          <el-table-column prop="id" label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="editUserTag(scope.row)">编辑</el-button>
              <el-button type="text" @click="editUserTag(scope.row,1)">详情</el-button>
              <el-button type="text" @click="removeUserTag(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:20px;text-align:right">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="pageData.page"
            :page-sizes="[10, 20, 30, 40]"
            :page-size.sync="pageData.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total.sync="pageData.total"
          ></el-pagination>
        </el-row>
      </el-row>
    </template>
    <!-- 新增(1)/编辑(2) 用户标签页 -->
    <template v-else-if="pageStatus==1 || pageStatus==2">
      <div class="t_row">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>
            <a @click.stop="pageStatus=0">配置用户标签</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a>{{pageStatus==1?"新增":"编辑"}}</a>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-form
        :model="userTag"
        ref="userTag"
        label-width="100px"
        class="t_row t_join"
        :rules="rules"
      >
        <el-form-item prop="labelName" label="标签名称：">
          <el-input style="width:400px" v-model="userTag.labelName"></el-input>
        </el-form-item>
        <template v-for="(domain,index) in userTag.labelRules">
          <el-form-item :key="index" :label="index==0?'标签规则：':''" required>
            <div style="display:inline-block" v-if="index!=0">
              <el-form-item
                style="width:130px"
                :prop="'labelRules.'+index+'.connectionSymbol'"
                :rules="[{ required: true, message: '请选择连接符' }]"
              >
                <el-select v-model="domain.connectionSymbol" size="small" placeholder="请选择连接符">
                  <el-option
                    v-for="item in connector"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display:inline-block">
              <el-form-item
                style="width:130px"
                :prop="'labelRules.'+index+'.elementType'"
                :rules="[{ required: true, message: '请选择元件' }]"
              >
                <el-select v-model="domain.elementType" size="small" placeholder="请选择元件">
                  <el-option
                    v-for="item in userTags"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display:inline-block">
              <el-form-item
                style="width:130px"
                :prop="'labelRules.'+index+'.symbol'"
                :rules="[{ required: true, message: '请选择符号' }]"
              >
                <el-select v-model="domain.symbol" size="small" placeholder="请选择符号">
                  <el-option
                    v-for="item in operator"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display:inline-block">
              <el-form-item
                :prop="'labelRules.'+index+'.num'"
                :rules="[{ required: true, message: '请输入数字',trigger: 'blur' }]"
              >
                <el-input-number v-model.number="domain.num" size="small" :min="0"></el-input-number>
              </el-form-item>
            </div>
            <el-button-group>
              <el-button @click.prevent="addDomain(index)" type="primary" size="small">添加</el-button>
              <el-button v-if="index!=0" @click.prevent="removeDomain(index)" size="small">删除</el-button>
            </el-button-group>
          </el-form-item>
        </template>
        <el-form-item prop="labelState" label="标签说明：">
          <el-input
            style="width:500px"
            type="textarea"
            placeholder="请输入内容"
            v-model="userTag.labelState"
            :autosize="{ minRows: 2}"
          ></el-input>
        </el-form-item>
        <el-form-item prop="priority" label="优先级：">
          <el-input-number v-model.number="userTag.priority" size="small" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </template>
    <!-- 用户标签 详情(3) 页 -->
    <template v-else-if="pageStatus==3">
      <div class="t_row">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>
            <a @click.stop="pageStatus=0">配置用户标签</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a>详情</a>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-form :model="userTag" ref="userTag" label-width="100px" class="t_row t_join">
        <el-form-item label="标签名称：">
          <span>{{userTag.labelName}}</span>
        </el-form-item>
        <el-form-item label="标签规则：">
          <span>{{userTag.labelRules}}</span>
        </el-form-item>
        <el-form-item label="标签说明：">
          <span style="word-break:break-word;">{{userTag.labelState}}</span>
        </el-form-item>
        <el-form-item label="优先级：">
          <span>{{userTag.priority}}</span>
        </el-form-item>
        <el-form-item>
          <el-button @click="cancel">关闭</el-button>
        </el-form-item>
      </el-form>
    </template>
    <!-- 元件的 信息(4)/修改(5) 页 -->
    <template v-else-if="pageStatus==4 || pageStatus==5">
      <div class="t_row">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>
            <a @click.stop="pageStatus=0">配置用户标签</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a>{{pageStatus==4?"元件信息":"元件信息编辑"}}</a>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-form class="t_row t_join">
        <el-form-item>
          <pre v-if="pageStatus==4" style="line-height:20px;white-space:pre-wrap;">{{elementInfo.remark || "暂无元件信息"}}</pre>
          <el-input
            v-else
            :autosize="{ minRows: 4}"
            type="textarea"
            placeholder="请输入元件信息"
            v-model="elementInfo.remark"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button v-if="pageStatus==4" type="primary" @click="pageStatus=5">修改</el-button>
          <el-button v-if="pageStatus==4" @click="cancel">返回</el-button>
          <el-button v-if="pageStatus==5" type="primary" @click="editElementInfo">保存修改</el-button>
          <el-button v-if="pageStatus==5" @click="pageStatus=4">返回</el-button>
        </el-form-item>
      </el-form>
    </template>
  </div>
</template>

<script>
import headers from "./../content/headers";
import searchBar from "./searchBar";
export default {
  components: {
    headers,
    searchBar
  },
  data() {
    let stringEmpty = (rule, value, callback) => {
      if (value.trim() === "") {
        callback(new Error("不能输入空值！"));
      } else {
        callback();
      }
    };
    let numberRestrict = (rule, value, callback) => {
      if (value < 1 || value > 100) {
        callback(new Error("优先度限制在 1-100 的范围内"));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      searchs: [
        {
          fields: "labelName",
          placeholder: "请输入标签名称",
          type: "text"
        }
      ],
      userTags: [],
      operator: [],
      connector: [],
      pageData: { page: 1, limit: 10 },
      pageList: [],
      pageStatus: 0,
      userTag: { labelRules: [{}] },
      elementInfo: ``,
      rules: {
        labelName: [
          { required: true, message: "请输入标签名称" },
          { validator: stringEmpty }
        ],
        priority: [
          { required: true, message: "请输入优先级" },
          { validator: numberRestrict }
        ]
      }
    };
  },
  computed: {},
  created() {
    this.getUserTagsList();
    this.initOptionsByType();
  },
  methods: {
    initOptionsByType() {
      this.API.getDictionaryList({ type: "userTag" }).then(res => {
        this.userTags = res.datalist;
      });
      this.API.getDictionaryList({ type: "operator" }).then(res => {
        this.operator = res.datalist;
      });
      this.API.getDictionaryList({ type: "connector" }).then(res => {
        this.connector = res.datalist;
      });
      this.API.getDictionaryList({ type: "tagRule" }).then(res => {
        this.elementInfo = res.datalist[0] || {};
      });
    },
    // 获取表格数据
    getUserTagsList() {
      this.loading = true;
      this.searchs.forEach(item => {
        this.pageData[item.fields] = item.data;
      });
      this.API.getUserTagsList(this.pageData).then(res => {
        if (res.success) {
          this.pageList = res.datalist.map((e, i) => {
            e.index = (this.pageData.page - 1) * this.pageData.limit + i + 1;
            return e;
          });
          this.pageData.total = res.totalCount;
        } else {
          this.pageList = [];
          this.pageData.total = 0;
        }
        this.loading = false;
      });
    },
    // 新增标签规则
    addDomain(index) {
      this.userTag.labelRules.splice(index + 1, 0, {});
    },
    // 删除标签规则
    removeDomain(index) {
      this.userTag.labelRules.splice(index, 1);
    },
    // 将后台返回的字符串规则切割成表单呈现（配合修改用户标签使用）
    parseDomins(data) {
      let temp = [];
      let reg1 = this.connector.map(res => "[" + res.name + "]").join("|");
      let reg2 = this.userTags.map(res => res.name).join("");
      let reg3 = this.operator.map(res => "[" + res.name + "]").join("|");

      data = data.replace(
        new RegExp(`(${reg1})`, "g"),
        (match, p1) => `@${p1}`
      );
      data.split("@").forEach(item => {
        temp.push({});
        //   /([&]|[||])?([ABCD])([≥]|[≤]|[>]|[<]|[=])(d+)/
        item.replace(
          new RegExp(`(${reg1})?([${reg2}])(${reg3})(\\d+)`),
          (match, connectionSymbol, elementType, symbol, num) => {
            temp[temp.length - 1].connectionSymbol = connectionSymbol;
            temp[temp.length - 1].elementType = elementType;
            temp[temp.length - 1].symbol = symbol;
            temp[temp.length - 1].num = num;
          }
        );
      });
      return temp;
    },
    removeUserTag(id) {
      this.$confirm("此操作将删除该条记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.API.deleteUserTags({ labelId: id }).then(res => {
            this.$message({
              message: res.message,
              type: res.success ? "success" : "error"
            });
            if (res.success) {
              this.pageStatus = 0;
              this.getUserTagsList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 进入编辑用户标签页
    editUserTag(data, noEdit) {
      this.userTag = Object.assign({}, data);
      if (noEdit) {
        this.pageStatus = 3;
      } else {
        this.userTag.labelRules = this.parseDomins(data.labelRules);
        this.pageStatus = 2;
      }
    },
    // 新增或编辑用户标签
    submitForm() {
      this.$refs.userTag.validate(valid => {
        if (!valid) return false;
        (() => {
          if (this.userTag.id) return this.API.updateUserTags(this.userTag);
          else return this.API.addUserTags(this.userTag);
        })().then(res => {
          this.$message({
            message: res.message,
            type: res.success ? "success" : "error"
          });
          if (res.success) {
            this.pageStatus = 0;
            this.getUserTagsList();
            this.userTag = { labelRules: [{}] };
          }
        });
      });
    },

    cancel() {
      this.pageStatus = 0;
      this.userTag = { labelRules: [{}] };
    },
    editElementInfo() {
      this.API.updateDictionary(this.elementInfo).then(res => {
        this.pageStatus = 4;
      });
    },
    // 分页模块f
    handleSizeChange: function(size) {
      this.pageData.limit = size;
      this.pageData.page = 1;
      this.pageList = [];
      this.getUserTagsList();
    },
    handleCurrentChange: function(currentPage) {
      this.pageData.page = currentPage;
      this.getUserTagsList();
    },
    rowStyle({ row, rowIndex }) {
      if (rowIndex === 0) {
        return {
          height: "30px!important",
          background: "#f5f5f5",
          color: "#666",
          lineHeight: "30px!important"
        };
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.t_row {
  margin: 20px 20px 0;
  padding: 20px 30px;
  background-color: #fff;
  text-align: left;
}
.t_join {
  margin-top: -20px;
}
</style>
