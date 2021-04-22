import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/partner/page',
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
    url: '/api/cnfic-contract-manage/partner/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/partner/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/partner/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/partner/submit',
    method: 'post',
    data: row
  })
}


/**
 * 合作商产品、产品项列表
 */
export const getPartnerProductList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/partner/partnerProductList',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

/** 合作商分润明细列表 */
export const getPartnerOrderProfitTotal = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/partner/partnerOrderProfitTotal',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
// 获取 产品类型下拉项列表
export const selectPartnerItems = () => {
  return request({
    url: '/api/cnfic-contract-manage/partner/selectItems',
    method: 'post'
  })
}
