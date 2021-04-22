import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-system/dept/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const remove = (ids) => {
  return request({
    url: '/api/cnfic-system/dept/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-system/dept/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-system/dept/submit',
    method: 'post',
    data: row
  })
}

export const getDept = (id) => {
  return request({
    url: '/api/cnfic-system/dept/detail',
    method: 'get',
    params: {
      id,
    }
  })
}
export const getDeptTree = (tenantId) => {
  return request({
    url: '/api/cnfic-system/dept/tree',
    method: 'get',
    params: {
      tenantId,
    }
  })
}
export const getDeptList = (tenantId) => {
  return request({
    url: '/api/cnfic-system/dept/list',
    method: 'get',
    params: {
      tenantId,
    }
  })
}

export const listById = (ids) => {
  return request({
    url: '/api/cnfic-system/dept/listbyids',
    method: 'post',
    params: {
      ids,
    }
  })
}

