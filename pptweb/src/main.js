import Vue from 'vue'
import App from '@/App'
import router from '@/router' // api: https://github.com/vuejs/vue-router
import store from '@/store' // api: https://github.com/vuejs/vuex
import VueCookie from 'vue-cookie' // api: https://github.com/alfhen/vue-cookie
import 'element-ui/lib/theme-chalk/index.css'
import '@/element-ui' // api: https://github.com/ElemeFE/element
import '@/element-ui-theme'
import '@/assets/scss/index.scss'
import httpRequest from '@/utils/httpRequest' // api: https://github.com/axios/axios
import { isAuth } from '@/utils'
import cloneDeep from 'lodash/cloneDeep'
import moment from 'moment' // 时间工具
import selfUtil from './utils/util' // 自定义工具类
import '@/utils/scrollbar'
import 'perfect-scrollbar/css/perfect-scrollbar.css'
import JSONView from 'vue-json-viewer'

Vue.use(JSONView)
Vue.use(VueCookie)
Vue.config.productionTip = false

// 挂载全局
Vue.prototype.$http = httpRequest // ajax请求方法
Vue.prototype.isAuth = isAuth // 权限方法
Vue.prototype.dateUtil = moment // 权限方法
Vue.prototype.$util = selfUtil

// 保存整站vuex本地储存初始状态
window.SITE_CONFIG['storeState'] = cloneDeep(store.state)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
