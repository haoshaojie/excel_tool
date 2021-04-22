import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/orguser/page',
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
    url: '/api/cnfic-contract-manage/orguser/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/orguser/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/orguser/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/orguser/submit',
    method: 'post',
    data: row
  })
}

export const getContractList = (custId) => {
  return request({
    url: '/api/cnfic-contract-manage/linkman/list',
    method: 'get',
    params: {
      custId
    }
  })
}
export const getDic = (url) => {
  return request({
    url: url,
    method: 'get',
    params: {
    }
  })
}
export const getContractById = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/linkman/getContractById',
    method: 'get',
    params: {
      id
    }
  })
}
