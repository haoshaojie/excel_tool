import request from '@/router/axios';

/*****************************************企业账号管理-公共账号****************************************/

export const listUser = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/listuser',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getUserDetail = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/userdetail',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetailContract = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/userdetail/contract',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetailProduct = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/userdetail/product',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const getProductContract = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/userdetail/product/contract',
    method: 'get',
    params: {
      ...params,
    }
  })
}
//启用/禁用
export const resetAble = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/resetable',
    method: 'get',
    params: {
      ...params,
    }
  })
}

/*****************************************企业账号管理-公共账号****************************************/

export const listPublic = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/listpublic',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getPublicdetail = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/publicdetail',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const publicSubmit = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/publicsubmit',
    method: 'post',
    data: row
  })
}

export const publicRemove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/enterpriseaccount/publicremove',
    method: 'post',
    params: {
      ids,
    }
  })
}


