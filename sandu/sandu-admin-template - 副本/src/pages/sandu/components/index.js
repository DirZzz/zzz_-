import page from './page'
import areaSelect from './areaSelect'
import belongIndustry from './commonSelect/belongIndustry' //所属行业
import myTree from './tree' //树形
import userType from './commonSelect/userType' //选择类型
import accountType from './commonSelect/accountType' //选择账号类型
import userOrigin from './commonSelect/userOrigin' //用户来源
import produceRange from './commonSelect/produceRange' //企业可见范围
import companyType from './commonSelect/companyType' //公司类型
import franchiserType from './commonSelect/franchiserType' //公司类型
import upload from './upload' //上传图片公用
export default (Vue)=>{
    Vue.component('page',page)
    Vue.component('areaSelect',areaSelect)
    Vue.component('belongIndustry',belongIndustry)
    Vue.component('myTree',myTree)
    Vue.component('userType',userType)
    Vue.component('accountType',accountType)
    Vue.component('upload',upload)
    Vue.component('userOrigin',userOrigin)
    Vue.component('produceRange',produceRange)
    Vue.component('companyType',companyType)
    Vue.component('franchiserType',franchiserType)
}