import Router from 'vue-router'
import Vue from 'vue'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import NotFound from '../views/NotFound.vue'
import FlowPanel from '../components/ef/panel.vue'
import User from '../views/user.vue'

Vue.use(Router)

// 防止跳转同一路径出异常
const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

const router = new Router({
  mode: 'history', // 可以使用HTML5历史记录，去掉URL中的#
  routes: [
    {
        path: '/',
        name: 'home',
        component: Home
      },
      {
        path: '/about',
        name: 'about',
        component: About
      },
      {
        path: '/user',
        name: 'user',
        component: User
      },
      {
        path: '/flow',
        name: 'flow',
        component: FlowPanel
      },
  ]
})

export default router

