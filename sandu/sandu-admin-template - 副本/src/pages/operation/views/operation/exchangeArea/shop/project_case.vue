<template>
    <div class="project-case">
        <v-nav :nav="nav"></v-nav>
        <div class="project-case-center">
            <div class="header">
                <span>工程案例</span>
                <el-button type="primary" round size="medium " @click="goAddProjectCase('新增')">新增</el-button>
            </div>
            <div class="project-case-list">
                <div class="vacancy" v-if="showVacancy">暂无数据</div>
                <div class="item" v-for="(item, index) in projectCaseList">
                    <span :class="item.releaseStatus == 1? 'item-img-active': 'item-img'"></span>
                    <img v-if="item.picPath" :src="BASE_URL.sourceBaseUrl + item.picPath" alt="">
                    <div class="default-img" v-else></div>
                    <p class="name">{{item.caseTitle}}</p>
                    <p class="data">创建时间：{{item.createDate}}</p>
                    <div class="operation">
                        <span @click="goAddProjectCase('详情', item.caseId)">详情</span>|
                        <span @click="goAddProjectCase('编辑', item.caseId)">编辑</span>|
                        <span @click="projectCaseDelete(index)">删除</span>|
                        <span @click="projectCasePublish(index, $event)">{{item.releaseStatus == 1? '取消发布' : '发布'}}</span>
                    </div>
                </div>
            </div>
            <div class="footer-paging" v-if="!showVacancy">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="start"
                        :page-sizes="[8, 12, 16, 24]"
                        :page-size="10"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalCount">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "project_case",
        data() {
            return {
                nav:[
                    {nav: '店铺管理', url: '/business/shop'},
                    {nav: '工程案例', url: '/business/projectCase'}
                ],
                projectCaseList: [],
                showVacancy: false,
                start: 0,
                limit: 8,
                totalCount: 0,
                shopType: this.$route.query.shopType
            };
        },
        created() {
            this.getProjectCaseList();
        },
        methods:{
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.limit = val;
                this.getProjectCaseList();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.start = (val - 1) * this.limit;
                this.getProjectCaseList();
            },
            /*请求工程案例*/
            getProjectCaseList() {
                this.API.projectCaseList({
                    shopId: sessionStorage.getItem('shopId'),
                    start: this.start,
                    limit: this.limit
                }).then(res => {
                    if (res.datalist && res.datalist.length > 0) {
                        this.projectCaseList = res.datalist;
                        this.totalCount = res.totalCount;
                    }  else {
                        this.showVacancy = true;
                    }
                });
            },
            /*进入编辑，详情页面*/
            goAddProjectCase(type, id) {
                sessionStorage.setItem('isProjectEdit', type);
                sessionStorage.setItem('caseId', id);
                this.$router.push({path:'/business/addProjectCase',query:{shopType: this.shopType}});
            },
            /*发布，取消发布*/
            projectCasePublish(index, event) {
                this.$confirm('此操作将' + event.target.innerText +'当前店铺信息，是否继续','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.projectCasePublish({
                        caseId: this.projectCaseList[index].caseId,
                        releaseStatus: event.target.innerText == '发布' ? 1 : 0,
                        shopType:  this.shopType
                    }).then(res => {
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: res.message
                            });
                            this.projectCaseList[index].releaseStatus = event.target.innerText == '发布' ? 1 : 0;
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            /*删除*/
            projectCaseDelete(index) {
                this.$confirm('此操作将删除当前店铺信息，是否继续','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.projectCaseDelete({
                        caseId: this.projectCaseList[index].caseId,
                        shopType: this.shopType
                    }).then(res => {
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: res.message
                            });
                            this.projectCaseList.splice(index ,1);
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                        }
                    });

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";
    .project-case{
        .project-case-center{
            background-color: #ffffff;
            .header{
                padding: 20px;
                display: flex;
                justify-content: space-between;
                border-bottom: 1px solid #dddddd;
                span{
                    color: #333333;
                    font-size: 20px;
                    line-height: 36px;;
                }
                .el-button{
                    width: 125px;
                }
            }
            .project-case-list{
                min-height: 700px;
                padding: 10px 10px;
                display: flex;
                flex-wrap:wrap;
                justify-content: left;
                .vacancy{
                    width: 100%;
                    height: 700px;
                    font-size: 30px;
                    line-height: 700px;
                    text-align: center;
                }
                .item{
                    width: 380px;
                    height: 430px;
                    margin: 10px;
                    border: 1px solid #DDDDDD;
                    box-sizing: border-box;
                    background-color: #fafafa;
                    position: relative;
                    .default-img{
                        width: 100%;
                        height: 300px;
                        // background: url("../../../assets/images/project_case.png");
                    }
                    .item-img{
                        position: absolute;
                        left: 347px;
                        display: block;
                        width: 18px;
                        height: 56px;
                        // background: url("../../../assets/images/icons/publish.png");
                    }
                    .item-img-active{
                        position: absolute;
                        left: 347px;
                        display: block;
                        width: 18px;
                        height: 56px;
                        // background: url("../../../assets/images/icons/publish1.png");
                    }
                    img{
                        display: block;
                        width: 100%;
                        height: 300px;
                    }
                    .name{
                        padding-left: 20px;
                        margin-top: 20px;
                        color: #333333;
                        overflow: hidden;
                        text-overflow:ellipsis;
                        white-space: nowrap;
                    }
                    .data{
                        padding-left: 20px;
                        margin-top: 10px;
                        color: #999999;
                    }
                    .operation{
                        margin-top: 25px;
                        color: #999999;
                        line-height: 19px;
                        span{
                            color: #ff6419;
                            cursor:pointer;
                            margin: 0 20px;
                        }
                    }
                }
            }
            .footer-paging{
                background-color: #ffffff;
                padding: 20px 40px;
                text-align: center;
            }
        }
    }
</style>