import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/logoffaccount/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getProductOfUnexpired = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/logoffaccount/productofunexpired',
    method: 'get',
    params: {
      id
    }
  })
}

export const logoff = (accId) => {
  return request({
    url: '/api/cnfic-contract-manage/logoffaccount/logoff',
    method: 'get',
    params: {
      accId
    }
  })
}

