import { mapState, mapActions } from 'Vuex'
let commonMinix = {
    data() {
        return {
            breadcrumb: [] //面包屑 { path:'',title:''}
        }
    },
    computed: {
        ...mapState('userManage', ['userMangeAdd', 'userManageBatchAdd', 'packageEditDialog', 'packagePayDialog', 'isNewAdd', 'userMethod', 'serviceInfoList', 'packageData', 'userManageTabList', 'payType', 'serviceInfo', 'userTypelist', 'packageEditFrom', 'packageEditFromType'])
    },
    created() {},
    methods: {
        ...mapActions('userManage', ['setDialog', 'setServicelist', 'setTabList', 'setPackageData', 'setPayType', 'setServiceInfo', 'setIsNewAdd', 'setUserMethod', 'setUserType', 'setOldCategoryIds']),
        toLink(url, val) {
            let query = val || {};
            this.$router.push({ path: url, query: query });
        },
        getlist(userMethod, companyId) { //所有的用户列表  掉的接口
            this.loading = true;
            if (userMethod != 'overAll' && !companyId) {
                return
            }
            this.API.getUserList({
                page: this.page,
                limit: this.limit,
                userMethod: userMethod, //
                nickName: this.formParam.nickName || undefined,
                Mobile: this.formParam.Mobile || undefined,
                userName: this.formParam.userName || undefined,
                servicesName: this.formParam.servicesName || undefined,
                userType: this.formParam.userType || undefined,
                useType: this.formParam.useType == 0 ? 0 : this.formParam.useType || undefined,
                effectiveBegin: this.timeArr && this.timeArr[0] || undefined,
                effectiveEnd: this.timeArr && this.timeArr[1] || undefined,
                companyId: companyId ? companyId : this.formParam.companyId,
                masterSonType:this.selectValue
            }).then(res => {
                this.loading = false;
                if (res.success) {
                    this.userListData = res;
                } else {
                    //this.$message.error(res.message)
                }
            })
        },
        accountChange(val) {
            this.formParam.useType = val;
        },
        userChange(val) {
            this.formParam.userType = val;
        },
        provinceChange(code) {
            this.formParam.provinceCode = code;
        },
        cityChange(code) {
            this.formParam.cityCode = code;
        },
        areaChange(code) {
            this.formParam.areaCode = code;
        },
        streetChange(code) {
            this.formParam.streetCode = code;
        },
        pageReset() {
            this.page = 1;
        },
    }
};
export default commonMinix