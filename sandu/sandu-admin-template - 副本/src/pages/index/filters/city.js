
let city={
    data(){
        return{
            provide:[],
            provideSelect:[],
            citySelects:[],
            areaSelect: [],
        }
    },
    created(){
        this.getArea(0,'省');
    },
    methods: {
        // 区域选择-------------
        getArea(code, type){
            this.API.areaList({areaCode:code}).then((res => {
                if(res) {
                    if(type == '省') {
                        this.provide = res.datalist
                    }else if(type == '市') {
                        this.citySelects = res.datalist
                    }else if(type == '区') {
                        this.areaSelect = res.datalist
                    }
                }
            }))
        },
        getProvideCode(code) {
            this.citySelect.city =''
            this.citySelect.area =''
            this.citySelect.origin =''
            this.citySelects =[]
            this.areaSelect = []
            this.getArea(code,'市');
            try{
                this.form.province=code;
                this.companyList();
            }catch (e) {

            }

        },
        getCityCode (code){
            this.citySelect.area =''
            this.citySelect.origin =''
            this.areaSelect = []
            this.getArea(code,'区');
            try{
                this.form.city=code;
                this.companyList();
            }catch (e) {

            }
        },
        getAreaCode (code){
            this.citySelect.origin =''
            this.originSelect = []
            try{
                this.form.area=code;
                this.companyList();
            }catch (e) {

            }
            //this.getArea(code,'街道')
        }
    }
}
export default city