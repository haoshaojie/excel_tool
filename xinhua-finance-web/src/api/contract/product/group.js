import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/group/list',
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
    url: '/api/cnfic-contract-manage/group/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/group/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/group/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/group/update',
    method: 'post',
    data: row
  })
}
export const updateState = (ids, state) => {
  return request({
    url: '/api/cnfic-contract-manage/group/updateState',
    method: 'post',
    params:{
      ids: ids,
      state: state
    }
  })
}
export const validProdName = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/group/validProdName',
    method: 'post',
    params:params
  })
}
