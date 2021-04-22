import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/membergoods/page',
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
    url: '/api/cnfic-contract-manage/membergoods/exportData',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/membergoods/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}


