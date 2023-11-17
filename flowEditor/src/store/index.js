// createStore 创建store的实例
import createPersistedstate from 'vuex-persistedstate'
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
 

import user from './modules/user'
import workflow from './modules/workflow'
import workflowBrief from './modules/workflowBrief'
 
// 创建vuex仓库并导出
const store = new Vuex.Store({

  modules: {
    // 分模块
    user,
    workflow,
    workflowBrief
  },
  // 将插件配置到Vuex的plugins中
  plugins: [
    createPersistedstate({
      key: 'workflow-client-pc-store', // 存数据的key名   自定义的  要有语义化
      paths: ['user','workflow','workflowBrief'] // 要把那些模块加入缓存
    })
  ]
})

export default store

