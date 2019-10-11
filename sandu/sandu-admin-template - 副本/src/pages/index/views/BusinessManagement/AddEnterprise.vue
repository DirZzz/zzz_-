<template>
 <div class="addEnterprise">
   <header class="headers">
     <h1>添加新企业</h1>
   </header>
   <el-row :gutter="20">
     <el-col :span="12">
       <div class="grid-content minHeight baseMsgFrame">
         <header>
           <h1 class="bg-purple">基础信息</h1>
         </header>
         <ul class="enterpriseBaseMessage">
           <li><span>企业编号：</span><span>Y132456789</span></li>
           <li><span>企业名称：</span><el-input placeholder="请输入企业名称"></el-input></li>
           <li>
             <span>合作生效日期：</span>
               <el-date-picker
                 class="pickerTime"
                 v-model="value7"
                 type="daterange"
                 align="right"
                 unlink-panels
                 range-separator="至"
                 start-placeholder="开始日期"
                 end-placeholder="结束日期"
                 :picker-options="pickerOptions2">
               </el-date-picker>
           </li>
           <li><span class="enterpriseAccount">企业超级管理账号：</span><el-input placeholder="请输入企业账号"></el-input></li>
           <li>
             <span>角色类型：</span>
             <el-select v-model="selectRole" class="selectRole" placeholder="请选择">
               <el-option
                 v-for="item in options"
                 :key="item.value"
                 :label="item.label"
                 :value="item.value">
               </el-option>
             </el-select>
           </li>
           <li>
             <span>备注：</span>
             <el-input
               type="textarea"
               :rows="2"
               placeholder="请输入内容"
               v-model="textarea">
             </el-input>
           </li>
         </ul>
       </div>
     </el-col>
     <el-col :span="12">
       <div class="grid-content minHeight baseMsgFrame">
         <header>
           <h1 class="bg-purple">企业描述信息</h1>
         </header>
         <ul class="enterpriseDesc">
           <li class="companylogo">
             <span>企业LOGO：</span>
             <div>
               <el-upload
                 action="https://jsonplaceholder.typicode.com/posts/"
                 list-type="picture-card"
                 :on-preview="handlePictureCardPreview"
                 :on-remove="handleRemove">
                 <i class="el-icon-plus"></i>
               </el-upload>
               <el-dialog :visible.sync="dialogVisible" size="tiny">
                 <img width="100%" :src="dialogImageUrl" alt="">
               </el-dialog>
             </div>
           </li>
           <li class="brand">
             <span>经营品牌：</span><el-input class="brandInput" placeholder="请输入品牌名称"></el-input>
           </li>
           <li class="brand">
             <span>经营范围：</span>
             <el-cascader class="brandRange"
               :options="optionsRange"
               v-model="selectedOptions3"
             ></el-cascader>
           </li>
           <li class="brand">
             <span>企业QQ：</span><el-input class="addQQ" placeholder="请输入企业QQ"></el-input> <b><el-button type="text">添加</el-button></b>
           </li>

         </ul>
       </div>
     </el-col>
   </el-row>
   <div class="submitButton">
     <el-button class="cancelButton" round>取消</el-button>
     <el-button class="submitSure" round>确定</el-button>
   </div>

 </div>
</template>

<script>
    export default {
      name: 'add-enterprise',
      data () {
        return {
          brand: '',
          options: [{
            value: '选项1',
            label: '黄金糕'
          }, {
            value: '选项2',
            label: '双皮奶'
          }, {
            value: '选项3',
            label: '蚵仔煎'
          }, {
            value: '选项4',
            label: '龙须面'
          }, {
            value: '选项5',
            label: '北京烤鸭'
          }],
          textarea: '',
          selectRole: '', // 角色类型
          companyName: '',
          dialogImageUrl: '',
          dialogVisible: false,
          pickerOptions2: {
            shortcuts: [{
              text: '最近一周',
              onClick (picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近一个月',
              onClick (picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近三个月',
              onClick (picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
              }
            }]
          },
          value6: '',
          value7: '',
          // 经营范围 开始
          optionsRange: [{
            value: 'zhinan',
            label: '指南',
            children: [{
              value: 'shejiyuanze',
              label: '设计原则',
              children: [{
                value: 'yizhi',
                label: '一致'
              }, {
                value: 'fankui',
                label: '反馈'
              }, {
                value: 'xiaolv',
                label: '效率'
              }, {
                value: 'kekong',
                label: '可控'
              }]
            }, {
              value: 'daohang',
              label: '导航',
              children: [{
                value: 'cexiangdaohang',
                label: '侧向导航'
              }, {
                value: 'dingbudaohang',
                label: '顶部导航'
              }]
            }]
          }, {
            value: 'zujian',
            label: '组件',
            children: [{
              value: 'basic',
              label: 'Basic',
              children: [{
                value: 'layout',
                label: 'Layout 布局'
              }, {
                value: 'color',
                label: 'Color 色彩'
              }, {
                value: 'typography',
                label: 'Typography 字体'
              }, {
                value: 'icon',
                label: 'Icon 图标'
              }, {
                value: 'button',
                label: 'Button 按钮'
              }]
            }, {
              value: 'form',
              label: 'Form',
              children: [{
                value: 'radio',
                label: 'Radio 单选框'
              }, {
                value: 'checkbox',
                label: 'Checkbox 多选框'
              }, {
                value: 'input',
                label: 'Input 输入框'
              }, {
                value: 'input-number',
                label: 'InputNumber 计数器'
              }, {
                value: 'select',
                label: 'Select 选择器'
              }, {
                value: 'cascader',
                label: 'Cascader 级联选择器'
              }, {
                value: 'switch',
                label: 'Switch 开关'
              }, {
                value: 'slider',
                label: 'Slider 滑块'
              }, {
                value: 'time-picker',
                label: 'TimePicker 时间选择器'
              }, {
                value: 'date-picker',
                label: 'DatePicker 日期选择器'
              }, {
                value: 'datetime-picker',
                label: 'DateTimePicker 日期时间选择器'
              }, {
                value: 'upload',
                label: 'Upload 上传'
              }, {
                value: 'rate',
                label: 'Rate 评分'
              }, {
                value: 'form',
                label: 'Form 表单'
              }]
            }, {
              value: 'data',
              label: 'Data',
              children: [{
                value: 'table',
                label: 'Table 表格'
              }, {
                value: 'tag',
                label: 'Tag 标签'
              }, {
                value: 'progress',
                label: 'Progress 进度条'
              }, {
                value: 'tree',
                label: 'Tree 树形控件'
              }, {
                value: 'pagination',
                label: 'Pagination 分页'
              }, {
                value: 'badge',
                label: 'Badge 标记'
              }]
            }, {
              value: 'notice',
              label: 'Notice',
              children: [{
                value: 'alert',
                label: 'Alert 警告'
              }, {
                value: 'loading',
                label: 'Loading 加载'
              }, {
                value: 'message',
                label: 'Message 消息提示'
              }, {
                value: 'message-box',
                label: 'MessageBox 弹框'
              }, {
                value: 'notification',
                label: 'Notification 通知'
              }]
            }, {
              value: 'navigation',
              label: 'Navigation',
              children: [{
                value: 'menu',
                label: 'NavMenu 导航菜单'
              }, {
                value: 'tabs',
                label: 'Tabs 标签页'
              }, {
                value: 'breadcrumb',
                label: 'Breadcrumb 面包屑'
              }, {
                value: 'dropdown',
                label: 'Dropdown 下拉菜单'
              }, {
                value: 'steps',
                label: 'Steps 步骤条'
              }]
            }, {
              value: 'others',
              label: 'Others',
              children: [{
                value: 'dialog',
                label: 'Dialog 对话框'
              }, {
                value: 'tooltip',
                label: 'Tooltip 文字提示'
              }, {
                value: 'popover',
                label: 'Popover 弹出框'
              }, {
                value: 'card',
                label: 'Card 卡片'
              }, {
                value: 'carousel',
                label: 'Carousel 走马灯'
              }, {
                value: 'collapse',
                label: 'Collapse 折叠面板'
              }]
            }]
          }, {
            value: 'ziyuan',
            label: '资源',
            children: [{
              value: 'axure',
              label: 'Axure Components'
            }, {
              value: 'sketch',
              label: 'Sketch Templates'
            }, {
              value: 'jiaohu',
              label: '组件交互文档'
            }]
          }],
          selectedOptions3: ['zujian', 'data', 'tag']
          // 经营范围 结束
        };
      },
      methods: {
        handleRemove (file, fileList) {
        },
        handlePictureCardPreview (file) {
          this.dialogImageUrl = file.url;
          this.dialogVisible = true;
        }
      }
    };
</script>

<style lang="scss" scoped>
  .minHeight{
    min-height: 450px;
  }
  .brandRange{
    width: 100%;
  }
  .pickerTime{
    width: 100%;
  }
  .addEnterprise {
    background: #fff;
    border-radius: 6px;
    margin-top: 10px;
    padding:20px;
    .headers {
      margin-bottom: 20px;
      h1{
        font-size: 16px;
        font-weight: bold;
        color:#666;
        height:50px;
        line-height:50px;
        border-bottom: solid 1px #e3e3e3;
      }
    }
  }
  .selectBrandWidth{
    width: 100%;
  }
  .baseMsgFrame{
    header{
      border-bottom: solid 1px #e3e3e3;
    }
  }
  .enterpriseDesc{
    font-size:12px;
    padding: 20px;
  }
  .enterpriseAccount{
    width: 160px;
    display: inline-block;
  }
  .companylogo{
    display: flex;
    border-bottom: solid 1px #e3e3e3;
    padding-bottom: 15px;
  }
  .brand{
    display: flex;
    margin: 15px 0;
    line-height: 40px;
    span{
      min-width:70px;
      display: inline-block;
    }
    .addQQ{
      width:200px;
    }
    b{
      display: inline-block;
      padding:0 20px;
    }
  }
  .el-row {
    margin-bottom: 20px;
    &:last-child {
       margin-bottom: 0;
     }
  }
  .submitButton{
    border-top: solid 1px #e3e3e3;
    height: 93px;
    line-height: 93px;
    text-align: center;

    .cancelButton{
      background: #454545;
      color:#fff;
      padding:12px 36px;
    }
    .submitSure{
      background: #ff6419;
      color:#fff;
      padding:12px 36px;
    }
  }
  .selectRole{
    width: 100%;
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #f9f9f9;
    text-align: center;
    font-weight: normal;
    font-size: 16px;
    height: 50px;
    line-height: 50px;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border:solid 1px #d4d4d4;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .enterpriseBaseMessage{
    padding:0 20px 20px 20px;
    color:#666;
    li{
      padding:10px 0;
      display:flex;
      .companyName {
        border:solid 1px #e3e3e3;
        height: 30px;
        line-height: 30px;

      }
      span{
        display: block;
        min-width: 140px;
        line-height:38px;
      }
    }
  }
</style>
