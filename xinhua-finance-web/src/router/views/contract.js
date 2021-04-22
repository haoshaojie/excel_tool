export default [
  {
    path: '/contract/product/category-property',
    name: '属性配置',
    meta: {
      i18n: 'categoryProperty'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/contract/product/category-property')
  },
  {
    path: '/contract/userAccount/mailRecord',
    name: '邮件记录',
    meta: {
      i18n: 'mailRecord'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/contract/userAccount/mailRecord')
  },
  {
    path: '/contract/userAccount/mailTemplate',
    name: '邮件模板',
    meta: {
      i18n: 'mailTemplate'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/contract/userAccount/mailTemplate')
  },
  {
    path: '/contract/userAccount/productLink',
    name: '产品链接',
    meta: {
      i18n: 'productLink'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/contract/userAccount/productLink')
  }
]
