import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelistaudit/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelistaudit/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const audit = (row) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelistaudit/audit',
    method: 'post',
    data: row
  })
}

export const choosecustomer = (row) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelistaudit/choosecustomer',
    method: 'post',
    data: row
  })
}

export const listcustomer = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/page',
    method: 'get',
    params: {
      ...params,
    current,
    size,
  }
})
}


