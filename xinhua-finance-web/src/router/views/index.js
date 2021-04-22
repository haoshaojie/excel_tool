import Layout from '@/page/index/'
import ContractRouter from './contract'

export default [{
  path: '/wel',
  component: Layout,
  redirect: '/wel/index',
  children: [{
    path: 'index',
    name: '首页',
    meta: {
      i18n: 'dashboard'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/wel/index')
  }, {
    path: 'dashboard',
    name: '控制台',
    meta: {
      i18n: 'dashboard',
      menu: false,
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/wel/dashboard')
  }]
}, {
  path: '/test',
  component: Layout,
  redirect: '/test/index',
  children: [{
    path: 'index',
    name: '测试页',
    meta: {
      i18n: 'test'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/util/test')
  }]
}, {
  path: '/info',
  component: Layout,
  redirect: '/info/index',
  children: [{
    path: 'index',
    name: '个人信息',
    meta: {
      i18n: 'info'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/user/info')
  }]
}, {
  path: '/info',
  component: Layout,
  redirect: '/info/index',
  children: [{
    path: 'index',
    name: '个人信息',
    meta: {
      i18n: 'info'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/user/info')
  }]
},
{
  path: '/partner',
  component: Layout,
  redirect: '/partner/partner',
  children: [{
    path: 'orderprofit',
    name: '分润明细',
    meta: {
      i18n: 'orderprofit'
    },
    component: () =>
      import( '@/views/partner/orderprofit')
  },{
    path: 'partnerProduct',
    name: '合作商产品',
    meta: {
      i18n: 'partnerProduct'
    },
    component: () =>
      import( '@/views/partner/partnerProduct')
  }]
}, 
{
  path: '/contract',
  component: Layout,
  children: ContractRouter
}]

