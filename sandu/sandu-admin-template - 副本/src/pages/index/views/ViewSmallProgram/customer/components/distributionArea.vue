<template>
    <el-dialog
            title="分配区域"
            :before-close="beforeClose"
            :visible.sync="distributionArea"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            width="640px" top="30vh">
        <div class="distributionArea">
            <el-row class="selectSearchCond">
                <el-col>
                    <span>所属经销商企业 ：</span>
                    <el-select v-model="belongValue" disabled placeholder="所属经销商企业">
                        <el-option v-for="item in conpanyList"
                                   :key="item.id"
                                   :label="item.companyName"
                                   :value="item.id"
                        ></el-option>
                    </el-select>
                </el-col>
            </el-row>
            <addArea ref="addArea" @closeMd="closeMd"></addArea>
            <div class="btn">
                <el-button type="primary" @click="confirm" style="width: 80px" class="confirmButtonClass" size="medium">确 定</el-button>
                <el-button @click="isShow('close')" style="width: 80px" class="cancelButtonClass" size="medium">取 消</el-button>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import addArea from './addArea'
    import city from '@/filters/city'
    export default {
        name: "distributionArea",
        props:['conpanyList'],
        mixins:[city],
        components: {
            addArea
        },
        data(){
            return{
                belongValue:'',
                distributionArea:false,
                citySelect:{
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
            }
        },
        methods:{
            beforeClose(){
                this.distributionArea=false;
            },
            isShow(type,channelCompanyId,channelCompanyName){
                //this.$refs.addArea.getChannelCompanyId(channelCompanyId);
                 sessionStorage.setItem('channelCompanyId',channelCompanyId);
                if(type=='show'){
                    this.belongValue=channelCompanyName;
                    this.API.customerRule({companyId:channelCompanyId}).then(res=>{
                        if(res.code==200){
                            this.distributionArea=true;
                            this.$nextTick(()=>{
                                this.$refs.addArea.getChannelCompanyList(res.data.allCode);
                            })
                        }else if(res.code==204){ //没有数据
                            this.distributionArea=true;
                        }else {
                            this.$message(res.message);
                        }
                    })
                }else{
                    this.distributionArea=false
                }

            },
            confirm(){
                this.$refs.addArea.save();
            },
            closeMd(){
                this.distributionArea=false;
                this.$emit('refresh');
            }
        }
    }
</script>

<style lang="scss" scoped>
    .distributionArea{
        .addArea{
            padding: 20px 0;
            em{
                font-style: normal;
                color: #ff6419;
                cursor: pointer;
            }
        }
        .citySection{
            padding-bottom: 30px;
            .ic_del{
                display: inline-block;
                width: 18px;
                height: 18px;
                background: no-repeat center url("../../../../assets/images/icons/ic_del.png");
                background-size: 100%;
                position: relative;
                top: 10px;
                left: 10px;
            }
        }
        .btn{
            display: flex;
            justify-content: flex-end;
            .cancelButtonClass{
                border: 0!important;
                background: #ffdbc9!important;
                color: #ff6419!important;
            }
        }
    }
</style>