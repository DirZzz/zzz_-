<template>
    <transition name="zoomIn">
        <div class="recommend">
            <div class="recommend-header">爆款推荐<span>（*必填项）</span></div>
            <div class="recommend-content">
                <div class="recommend-title">
                    <span><em>*</em>标题：</span>
                    <input type="text" maxlength="6" :value="moduleTitle" @input="changeInput" class="recommend-input">
                    <span class="recommend-tip">不超过6个字，4个字为最佳展示效果</span>
                </div>
                <div class="recommend-goods">
                    <div class="recommend-goods-text"><span><em></em>商品：</span> <span>商品数最多不超过10个，拖动选中的图片可对其排序</span></div>
                    <ul>
                        <li v-for="(item,index) in submitGoodsList" :key="index" v-dragging="{ item: item, list: submitGoodsList, group: 'files' }">
                             <img :src="BASE_URL.sourceBaseUrl+item.picAddress" alt="">
                             <div class="replace" @click="replace(item,index)">替换</div>
                             <i class="ic-close" @click="deleteGoodslist(item)"></i>
                        </li>
                        <li @click="addGoods" v-if="submitGoodsList.length<10">+ 添加商品</li>
                    </ul>
                </div>
            </div>
            <div class="bottom">
                <div class="btn" @click="save">保存</div><span>(新增、修改、删除操作后，请及时保存。)</span>
            </div>
        </div>
    </transition>
</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    export default {
        name: "recommend",
        data(){
            return{
                title:''
            }
        },
        created(){

        },
        computed:{
            ...mapState('homeManage',['addGoodsDialog','submitGoodsList','moduleTitle']),
        },
        methods:{
            ...mapActions('homeManage',['dialog','setSubmitGoodsList','setReplace','setSave','setModuleTitle']),
            changeInput(e){
                this.setModuleTitle(e.target.value);
            },
            addGoods(){
                this.dialog(true);
            },
            deleteGoodslist(item){
                this.setSubmitGoodsList({res:item,type:0});//从新的数组里删除 0删除
            },
            replace(item,index){ //替换功能
                this.setReplace({index:index,isReplace:true}); //是否是替换  替换第几个
                this.dialog(true);
            },
            save(){
                if(this.moduleTitle==null||this.moduleTitle==''){ this.$message.error('请输入标题');return };
                this.setSave({vm:this,title:this.moduleTitle})
            }
        }
    }
</script>

<style scoped lang="scss">
  .recommend{
      width: 540px;
      background-color: #fff;
      margin-top: 142px;
      margin-left: 30px;
      .recommend-header{
          width: 100%;
          height: 40px;
          line-height: 40px;
          background-color: #fafafa;
          text-indent: 21px;
          color: #000000;
          font-size: 14px;
          span{
              color: #ff2323;
          }
      }
      .recommend-content{
          .recommend-title{
              height: 36px;
              line-height: 36px;
              text-indent: 40px;
              margin-top: 34px;
              .recommend-tip{
                  font-size: 14px;
                  color: #999999;
                  margin-left: 10px;
              }
              em{
                  color: #ff2323;
                  margin-right: 5px;
              }
          }
          .recommend-input{
              width: 180px;
              height: 36px;
              background-color: #fafafa;
              border-radius: 2px;
              border: solid 1px #dddddd;
              text-indent: 16px;
          }
          .recommend-goods{
              .recommend-goods-text{
                  line-height: 48px;
                  span:nth-of-type(1){
                      color: #333333;
                      font-size: 14px;
                      margin-left: 40px;
                  }
                  span:nth-of-type(2){
                      color: #999999;
                      font-size: 14px;
                  }
                  em{
                      color: #ff2323;
                      margin-right: 5px;
                  }
              }
              ul{
                  display: flex;
                  flex-wrap: wrap;
                  margin-left: 40px;
                  padding-bottom: 30px;
                  box-sizing: border-box;
                  li{
                      width: 106px;
                      height: 106px;
                      background-color: #fafafa;
                      border-radius: 2px;
                      position: relative;
                      margin-right: 10px;
                      margin-bottom: 10px;
                      img{
                          width: 106px;
                          height: 106px;
                      }
                      .replace{
                          width: 48px;
                          height: 24px;
                          line-height: 24px;
                          background-color: #dbdbdb;
                          border-radius: 4px;
                          color: #333333;
                          font-size: 14px;
                          position: absolute;
                          left: 0;
                          top: 0;
                          text-align: center;
                          cursor: pointer;
                          &:hover{
                              background-color: #ff6419;
                              color: #fff;
                          }
                      }
                      .ic-close{
                          display: inline-block;
                          position: absolute;
                          top: -5px;
                          right: -5px;
                          width: 20px;
                          height: 20px;
                          background: no-repeat center url("../../../assets/images/ic_delete.png");
                          background-size: 70%;
                          cursor: pointer;
                      }
                  }
                  li:nth-child(4n){
                      margin-right: 0;
                  }
                  li:last-child{
                      line-height: 106px;
                      color: #ff6419;
                      text-align: center;
                      cursor: pointer;
                  }
              }
          }

      }
      .bottom{
          height: 56px;
          border-top: 1px solid #dddddd;
          display: flex;
          span{
              color: #ff6449;
              margin-top: 18px;
              margin-left: 10px;
          }
          .btn{
              width: 60px;
              height: 30px;
              background-color: #ff6419;
              border-radius: 15px;
              color: #ffffff;
              font-size: 14px;
              line-height: 30px;
              text-align: center;
              margin-left: 40px;
              margin-top: 14px;
              cursor: pointer;
              &:active{
                  opacity: .6;
              }
          }
      }
  }
</style>