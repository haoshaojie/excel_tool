import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/infogoods/page',
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
    url: '/api/cnfic-contract-manage/infogoods/exportData',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const getDetail = (id, type) => {
  return request({
    url: '/api/cnfic-contract-manage/infogoods/detail',
    method: 'get',
    params: {
      id, type
    }
  })
}

export const remove = (infoIds, reportIds) => {
  return request({
    url: '/api/cnfic-contract-manage/infogoods/remove',
    method: 'post',
    params: {
      infoIds, reportIds
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/infogoods/submit',
    method: 'post',
    data: row
  })
}

export const update = (infoIds, reportIds, disposeSuggest) => {
  return request({
    url: '/api/cnfic-contract-manage/infogoods/update',
    method: 'post',
    params: {
      infoIds, reportIds, disposeSuggest
    }
  })
}

