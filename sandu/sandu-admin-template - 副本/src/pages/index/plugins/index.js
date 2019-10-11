import Vue from 'vue';
import  ElementUI from 'element-ui';
//集成Vue-echarts插件
import ECharts from 'vue-echarts' // 在 webpack 环境下指向 components/ECharts.vue
// 手动引入 ECharts 各模块来减小打包体积
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
import VueDND from 'awe-dnd';
import VueClipboard from 'vue-clipboard2';
import components from '../components/index' //全局组件注册
// 注册组件后即可使用
Vue.component('v-chart', ECharts);
Vue.use(components);

Vue.use(ElementUI);
Vue.use(VueClipboard);
Vue.use(VueDND); //拖拽排序事件