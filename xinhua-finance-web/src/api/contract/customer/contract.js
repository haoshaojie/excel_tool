import request from '@/router/axios';

export const submitpassdata = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/linkman/submitpassdata',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const checkpassdata = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/linkman/getImportData',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const removeContact = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/linkman/remove',
    method: 'post',
    params: {
      id,
    }
  })
}
export const findAllByCustId = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/linkman/findAllByCustId',
    method: 'get',
    params: {
      id,
    }
  })
}
