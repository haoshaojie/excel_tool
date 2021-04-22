import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/personaluser/page',
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
    url: '/api/cnfic-contract-manage/personaluser/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/personaluser/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/personaluser/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/personaluser/submit',
    method: 'post',
    data: row
  })
}
export const checkpassdata = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/orguser/getImportData',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const submitpassdata = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/orguser/submitpassdata',
    method: 'get',
    params: {
      ...params,
    }
  })
}
