
import upload from './upload' //上传图片公用
import page from './page' //页面结构
import areaSelect from './areaSelect' //页面结构

export default (Vue)=>{
    Vue.component('upload',upload)
    Vue.component('page',page)
    Vue.component('areaSelect',areaSelect)
}