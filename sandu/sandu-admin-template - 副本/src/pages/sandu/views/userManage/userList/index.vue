<template>
  <page>
    <div class="main">
      <div class="main-header">
        <el-row>
          <el-col :span="4">
            <el-input v-model="formParam.nickName" placeholder="请输入登录名" clearable></el-input>
          </el-col>
          <el-col :span="4">
            <el-input v-model="formParam.Mobile" placeholder="请输入手机号" maxlength="11" clearable></el-input>
          </el-col>
          <el-col :span="4">
            <el-input v-model="formParam.userName" placeholder="请输入昵称" clearable></el-input>
          </el-col>
          <el-col :span="4">
            <el-input v-model="formParam.servicesName" placeholder="请输入套餐名称" clearable></el-input>
          </el-col>
          <el-col :span="4">
            <accountType @change="accountChange" ref="accountType"></accountType>
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="formParam.companyId"
              placeholder="请选择所属企业"
              filterable
              clearable
              @change="companyChange"
            >
              <el-option
                v-for="item in companyList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-col>
          <el-date-picker
            v-model="timeArr"
            value-format="yyyy-MM-dd HH:mm:ss"
            @change="dateChange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-row>
        <div class="btn">
          <el-button type="primary" size="medium" round @click="search">搜索</el-button>
          <el-button size="medium" round @click="reset">重置</el-button>
        </div>
      </div>
      <userList
        @handleSelectionChange="handleSelectionChange"
        @showBatch="showBatch"
        :userdata="userListData"
        :page="page"
        :loading="loading"
        @pageChange="pageChange"
        @refresh="refresh"
        ref="userList"
        :showNewAdd="true"
        @pageReset="pageReset"
        @sizeChange="sizeChange"
        @resetLoad="resetLoad"
      ></userList>
      <batch v-if="batchShow" @pwdClose="pwdClose" @pwdSure="pwdSure"></batch>
      <packageEdit v-if="userPackShow" ref="packageEdit" @mealSure="mealSure" @goUser="comPack"></packageEdit>
    </div>
  </page>
</template>

<script>
import { merge, uniq, map, find, filter, toString } from "lodash";
import { validateAlphone } from "@s/utils/validate";
import minixs from "@s/minixs/index";
import userList from "@s/components/userList/userList";
import batch from "./../batch/index";
import packageEdit from "@s/components/userList/packageEdit";
export default {
  name: "index",
  mixins: [minixs],
  components: {
    userList,
    batch,
    packageEdit
  },
  data() {
    return {
      userListData: "",
      selectValue: "",
      loading: true,
      companyList: [],
      page: 1,
      limit: 10,
      timeArr: "",
      formParam: {
        nickName: "",
        Mobile: "",
        userName: "",
        servicesName: "",
        userType: null,
        useType: null,
        effectiveBegin: "",
        effectiveEnd: "",
        companyId: ""
      },
      multipleSelection: [],
      batchShow: false,
      userPackShow: false
    };
  },
  activated() {
    let arr = [
      {
        path: "/userManage/userList",
        name: "用户列表"
      }
    ];
    this.setTabList(arr);
    this.getlist("overAll");
    this.setUserMethod("overAll");
    this.API.belongCompanyList().then(res => {
      console.log(this.companyList);
      this.companyList = res.obj;
    });
  },
  created() {
    this.userPackShow = true;
  },
  deactivated() {
    this.userPackShow = false;
  },
  methods: {
    showBatch(type) {
      if (this.multipleSelection.length > 0) {
        let arrm = this.multipleSelection;
        let boolUserType = true;
        if (type == 1) {
          function arr(n) {
            if (n > 0) {
              if (arrm[n].userType == arrm[n - 1].userType) {
                boolUserType = true;
                return arr(n - 1);
              } else {
                boolUserType = false;
                return;
              }
            }
          }
          arr(arrm.length - 1);
          if (!boolUserType) {
            this.$message.error("只能批量修改统一用户类型的账号！");
          } else {
            let item = {
              userType: arrm[0].userType,
              fromType: 0
            };
            this.setServicelist({
              vm: this,
              item: item
            });
            this.setDialog({
              packageEditDialog: true,
              packageEditFrom: 1,
              packageEditFromType: 1
            });
            this.setIsNewAdd(3);
          }
        } else {
          this.batchShow = true;
        }
      } else {
        this.$message.error("请至少勾选一个用户");
      }
    },
    mealClose() {
      this.batchShow = false;
    },
    comPack(num) {
      console.log("收到信息", num);
    },
    mealSure(id) {
      console.log("進入用戶列表的index");
      let mark = {
        servicesId: id,
        userIds: ""
      };
      var arrm = this.multipleSelection;
      function arr(n) {
        return n == 0
          ? (mark.userIds += arrm[0].id)
          : (mark.userIds += arrm[n].id + "," + arr(n - 1));
      }
      arr(arrm.length - 1);
      this.API.batchModify(mark).then(res => {
        if (res.success) {
          this.$message.success("设置成功！");
          this.refresh();
          this.pwdClose();
        } else {
          this.$message.error(res.message);
        }
      });
    },
    pwdClose() {
      this.batchShow = false;
    },
    pwdSure(obj) {
      let mark = {
        random: obj.random,
        password: obj.password,
        userIds: ""
      };
      var arrm = this.multipleSelection;

      function arr(n) {
        return n == 0
          ? (mark.userIds += arrm[0].id)
          : (mark.userIds += arrm[n].id + "," + arr(n - 1));
      }
      arr(arrm.length - 1);
      this.API.batchModifyPassword(mark).then(result => {
        if (result && result.size > 0) {
          this.$message.success("保存成功!");
          let url = window.URL.createObjectURL(result);
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", "修改密码.xls");
          document.body.appendChild(link);
          link.click();
          this.pwdClose();
        } else {
          this.loadingTwo.close();
          this.$message.error("没有数据需要导出!");
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(this.multipleSelection);
    },
    search() {
      // if(this.formParam.Mobile&&!validateAlphone(this.formParam.Mobile)){
      //     this.$message.error('手机输入有误！')
      //     return;
      // }
      this.page = 1;
      this.getlist("overAll");
      this.$refs.userList.reset();
    },
    reset() {
      //重置
      for (let key in this.formParam) {
        this.formParam[key] = null;
      }
      this.$refs.accountType.reset();
      this.$refs.userList.reset();
      this.page = 1;
      this.timeArr = "";
      this.getareaSelectlist("overAll");
    },
    resetLoad() {
      this.getlist("overAll");
    },
    refresh() {
      this.getlist("overAll");
    },
    dateChange(val) {
      console.log(val);
    },
    pageChange(val) {
      this.loading = true;
      this.page = val;
      this.getlist("overAll");
    },
    sizeChange(val) {
      this.loading = true;
      this.limit = val;
      this.getlist("overAll");
    },
    companyChange(val) {
      this.formParam.companyId = val;
    }
  },
  deactivated() {
    //this.reset();
  }
};
</script>

<style scoped lang="scss">
.main {
  .btn {
    text-align: center;
    margin-bottom: 20px;
  }

  .newbtn {
    text-align: left;
    margin-bottom: 20px;
  }
}
</style>