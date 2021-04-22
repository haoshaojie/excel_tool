import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelist/list',
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
    url: '/api/cnfic-contract-manage//blackwhitelist/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelist/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelist/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelist/update',
    method: 'post',
    data: row
  })
}

export const checkpassdata = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelist/checkpassdata',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const checkfaildata = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage//blackwhitelist/checkfaildata',
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
    url: '/api/cnfic-contract-manage//blackwhitelist/submitpassdata',
    method: 'get',
    params: {
      ...params,
    }
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


