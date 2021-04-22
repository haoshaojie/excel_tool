import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/order/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const exportData = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/order/list',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/order/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/order/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

