// Added by songjianming@sanduspace.cn on 2018/12/21
// 商家后台使用共享方案提示信息优化(http://jira.3du-inc.net/browse/CMS-667)
// WTF WARNING::: 下面的代码不要动，有魔法

let editPlan = {
    inject: ['reload'],
    data() {
        return {
            tipMsg: "方案使用成功，\n请到方案库中进行方案名称等信息的修改及完善！"
        }
    },
    methods: {
        goEditPlanView(row, resp, view, fn) {
            if (resp || resp.code === 200) {
                let planName = row && row.planName ? row.planName : '';
                let context = planName ? "“" + planName + "”" + this.tipMsg : this.tipMsg;

                this.$confirm(context, '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    confirmButtonText: '去修改',
                    type: 'info',
                    center: true
                }).then(() => {
                    view ? view() : '';
                }).catch(() => {
                    fn ? fn() : '';
                });
            } else {
                this.$message.error('系统错误，请稍后重试！');
            }
        }
    }
}

export default editPlan