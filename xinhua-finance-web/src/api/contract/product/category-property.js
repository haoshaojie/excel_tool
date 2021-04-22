import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/categoryproperty/propertyPageInCategory',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getListAdd = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/categoryproperty/propertyNotInCategory',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/property/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/categoryproperty/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/categoryproperty/submit',
    method: 'post',
    params: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/categoryproperty/submit',
    method: 'post',
    data: row
  })
}

