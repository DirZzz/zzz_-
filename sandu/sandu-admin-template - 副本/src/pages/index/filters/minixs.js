import qs from 'qs';
import md5 from 'js-md5';
let commonMethods={
    inject:['reload'],
    data(){
        return{
            platform:''
        }
    },
    methods: {
         encrypt() {
             const salt = 'WeB';
             let ps=this.password ? this.password : sessionStorage.getItem('loginPassword');
             const result = md5(salt + ps);
             return md5(result);
         },
        created(){
             this.loginTime()
        },
         commonModule(resp,platform) {
               sessionStorage.setItem('loginUser', qs.stringify(resp.obj));
               console.log(resp,platform);
               sessionStorage.setItem('token', resp.obj.token);
               sessionStorage.setItem('isInterior', '');
               let loginUser = qs.parse(sessionStorage.getItem('loginUser'));
               // 套餐到期提醒
               /*
               serviceType => 0.正式套餐  1.试用套餐
               maturityFlag =>{}套餐到期标识 1.false 不过期 2.true 过期
               tipsFlag =true =>{}表明套餐只剩下小于等于5天 false =>{} 表示套餐大于5天
               RemainingDays  -->当tipsFlag =true时这个值才有 套餐剩余最后5天的时候才会有
               * */
               sessionStorage.setItem('firstLoginToday', resp.obj.firstLoginToday) // 是否首次登录
               sessionStorage.setItem('maturityFlag', loginUser.maturityFlag) //套餐到期标识
               sessionStorage.setItem('tipsFlag', loginUser.tipsFlag) //表明套餐只剩下小于等于5天
               sessionStorage.setItem('remainingDays', loginUser.remainingDays) // 套餐剩余最后5天的时候才会有
               sessionStorage.setItem('serviceType', loginUser.serviceType) // 0.正式套餐  1.试用套餐
               sessionStorage.setItem('exceptionCode', resp.exceptionCode) //错误码
               sessionStorage.setItem('leftTime', resp.obj.leftTime) //账号到期的天数
               sessionStorage.setItem('serviceName', loginUser.serviceName) //套餐名称
               // 根据用户获取企业ID/品牌ID
               sessionStorage.setItem('userId', loginUser.id)
               sessionStorage.setItem('loginName', loginUser.loginName)
               sessionStorage.setItem('nickName', loginUser.nickName);
               this.companyId = resp.obj.companyId;
               sessionStorage.setItem('userType', loginUser.userType);
               sessionStorage.setItem('companyID', this.companyId);
               sessionStorage.setItem('userName', resp.obj.userName);
               sessionStorage.setItem('masterSonType', resp.obj.masterSonType);
               if(platform){  //点击切换平台调用
                   this.tapToPlatform(platform);
               }else {  //第一次登陆进入
                   if (resp.obj.businessAdministrationId && resp.obj.businessAdministrationId != 0) {
                       sessionStorage.setItem('businessAdministrationId', resp.obj.businessAdministrationId);
                       this.API.getCompanyType({
                           companyId: resp.obj.businessAdministrationId,
                       }).then(res => {
                           sessionStorage.setItem('companyType', res.obj.companyType);
                           sessionStorage.setItem('amdinUser', res.obj.amdinUser);
                           this.toPlatform();//跳转品台 逻辑
                       });
                   } else {
                       this.dialogVisible = true;
                       this.API.getDealerList({
                           companyId: sessionStorage.getItem('companyID')
                       }).then((res) => {
                           if (res.datalist) {
                               this.company = res.datalist;
                           }
                       });
                   }
               }
         },
        toPlatform(){
            for (let i=0;i<this.platform.obj.length;i++){
                let item=this.platform.obj[i];
                if(item.platformCode=='merchantManage'){
                    let bothper= qs.parse(sessionStorage.getItem('loginUser'));
                    if(bothper.permissions == true){
                        if(Object.values(bothper.permissions).includes('company:view:panel')){ //是否有概况的权限
                            this.$router.replace('/survey');
                        }else {
                            this.$router.replace('/personal/personalSet');
                        }
                    }else {
                        this.$router.replace('/personal/personalSet');
                    }
                    break;
                }else if(item.platformCode=='sanduManagerNew'){
                    location.href="./sandu.html";
                    break
                }else if(item.platformCode=='operationPlatform'){
                    location.href="./operation.html";
                    break
                }
            }
        },
        tapToPlatform(platform){
            if(platform=='merchantManage'){
                this.$router.replace('/personal/accountSet');
            }else if(platform=='sanduManagerNew'){
                location.href="./sandu.html";
            }else if(platform=='operationPlatform'){
                location.href="./operation.html";
            }
        },
        loginTime(){
            /*let leftTime = (new Date(enddate)) - new Date(); //计算剩余的毫秒数
            let days = parseInt(leftTime / 1000 / 60 / 60 / 24, 10); //计算剩余的天数
            let hours = parseInt(leftTime / 1000 / 60 / 60 % 24, 10); //计算剩余的小时
            let minutes = parseInt(leftTime / 1000 / 60 % 60, 10);//计算剩余的分钟*/
            let now = new Date()
            //alert(now.getHours())
        },
        async login(id,platform) {
            if(!id){
                var params = {
                    account: this.userName || sessionStorage.getItem('mobile'),
                    password: this.encrypt()
                }
               this.loginTime()
                if(!platform){
                    this.platform=await this.API.getLoginUserAdminPlatform(params);
                    sessionStorage.setItem('platformList',qs.stringify(this.platform.obj));
                }//切换不同的平台
               this.API.login(qs.stringify(params)).then(resp => {
                    if(resp.data.flag){
                        this.commonModule(resp.data,platform);
                        this.getDefaultShow();
                        this.getBrandList();
                        this.getDealerUserType();
                        this.getArea(0, '省');
                        sessionStorage.setItem('mobile', this.userName);
                        sessionStorage.setItem('pass', this.encrypt());
                        sessionStorage.setItem('loginPassword',this.password);
                        console.log(resp.data)
                    }else {
                        let errorMsg = resp.data.message;
                        this.$message({
                            type: 'error',
                            message: errorMsg,
                            duration: 1500
                        });
                    }
               })
            }else{
                 let form = new FormData();
                 form.append('account', sessionStorage.getItem('mobile'));
                 form.append('password', sessionStorage.getItem('pass'));
                 form.append('loginCompanyId', id);
                this.API.switchcompany(form).then(resp => {
                    if (resp.exceptionCode == '10010014') {
                         this.chosecompany = false;
                         setTimeout(() => {
                               this.updatedia = true;
                         }, 400);
                    }else{
                        this.commonModule(resp)
                        sessionStorage.setItem('mobile', resp.obj.mobile);
                        sessionStorage.setItem('pass', sessionStorage.getItem('pass'));
                        window.location.reload();
                    }
                  
                })
               
            }
        },
    }
}
export default commonMethods