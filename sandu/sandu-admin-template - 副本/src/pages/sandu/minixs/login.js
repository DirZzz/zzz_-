import qs from 'qs';
import md5 from 'js-md5';
let login={
    data(){
        return{
            platform:''
        }
    },
    methods: {
         encrypt() {
             const salt = 'WeB';
             const result = md5(salt + sessionStorage.getItem('loginPassword'));
             return md5(result);
         },
        created(){

        },
         commonModule(resp,platform) {
               sessionStorage.setItem('loginUser', qs.stringify(resp.obj));
               sessionStorage.setItem('token', resp.obj.token);
               let loginUser = qs.parse(sessionStorage.getItem('loginUser'));

               // 根据用户获取企业ID/品牌ID
               sessionStorage.setItem('userId', loginUser.id)
               sessionStorage.setItem('loginName', loginUser.loginName)
               sessionStorage.setItem('nickName', loginUser.nickName);
               this.companyId = resp.obj.companyId;
               sessionStorage.setItem('userType', loginUser.userType);
               sessionStorage.setItem('companyID', this.companyId);
               sessionStorage.setItem('userName', resp.obj.userName);
               this.toPlatform(platform);//跳转品台 逻辑

         },
        toPlatform(platform){
            if(platform=='merchantManage'){
                location.href="/index.html#/personal/personalSet";
            }else if(platform=='sanduManagerNew'){
                location.href="./sandu.html";
            }else if(platform=='operationPlatform'){
                location.href="./operation.html";
            }
        },
        async login(platform) {
            var params = {
                account: sessionStorage.getItem('mobile'),
                password: this.encrypt()
            }
            this.API.login(qs.stringify(params)).then(resp => {
                if(resp.data.flag){
                    this.commonModule(resp.data,platform);
                    sessionStorage.setItem('pass', this.encrypt());
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
        },
    }
}
export default login