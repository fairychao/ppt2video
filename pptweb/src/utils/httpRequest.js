import Vue from 'vue'
import axios from 'axios'
import router from '@/router'
import qs from 'qs'
import merge from 'lodash/merge'
import { clearLoginInfo } from '@/utils'

const http = axios.create({
  timeout: 1000 * 300,
  withCredentials: true,
  head: ''
})

// 统一设置post请求头
http.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8'

/**
 * 请求拦截
 */
http.interceptors.request.use(
  config => {
    config.headers['token'] = Vue.cookie.get('token') // 请求头带上token
    // 下载文件时，因为返回的是二进制流，所以需要设置请求响应类型为 arraybuffer
    if (config.head !== '' && config.head.includes('download')) {
      config.headers['responseType'] = 'arraybuffer'
    }
    // 文件上传时，发送的是二进制流，所以需要设置请求头的'Content-Type'
    if (config.head !== '' && config.head.includes('upload')) {
      config.headers['Content-Type'] = 'multipart/form-data; boundary = ' + new Date().getTime()
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * 响应拦截
 */
http.interceptors.response.use(
  response => {
    if (response.data && response.data.code === 401) {
      // 401, token失效
      clearLoginInfo()
      router.push({ name: 'login' })
    }
    return response
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = actionName => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi/' : window.SITE_CONFIG.baseUrl) + actionName
}

/**
 * get请求参数处理
 * @param {*} params 参数对象
 * @param {*} openDefultParams 是否开启默认参数?
 */
http.adornParams = (params = {}, openDefultParams = true) => {
  var defaults = {
    t: new Date().getTime()
  }
  return openDefultParams ? merge(defaults, params) : params
}

/**
 * post请求数据处理
 * @param {*} data 数据对象
 * @param {*} openDefultdata 是否开启默认数据?
 * @param {*} contentType 数据格式
 *  json: 'application/json; charset=utf-8'
 *  form: 'application/x-www-form-urlencoded; charset=utf-8'
 */
http.adornData = (data = {}, openDefultdata = true, contentType = 'json') => {
  var defaults = {
    t: new Date().getTime()
  }
  data = openDefultdata ? merge(defaults, data) : data
  return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
}

/**
 * 设置请求头
 * 上传：upload
 * 下载：download
 */
http.adornHeader = header => {
  return header
}

export default http
