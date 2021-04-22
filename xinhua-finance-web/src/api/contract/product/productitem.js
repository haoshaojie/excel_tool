import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/list',
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
    url: '/api/cnfic-contract-manage/productitem/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/update',
    method: 'post',
    data: row
  })
}

// 校验产品项编码
export const checkPropCode = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/checkPropCode',
    method: 'post',
    params: params
  })
}
// 获取 产品类型下拉项列表
export const updateItemState = (ids, itemState) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/updateItemState',
    method: 'post',
    params: {
      ids: ids,
      itemState: itemState
    }
  })
}
// 导出产品项
export const exportData = () => {
  return '/api/cnfic-contract-manage/productitem/exportData';
}
// 扩展属性
export const itemValueByProdId = (id) => {
  return request({
    url:'/api/cnfic-contract-manage/productitem/itemValueByProdId',
    method: 'get',
    params:{
      prodId:id
    }
  })
}
// 分润比例
export const profitRateByProdId = (id, type) => {
  return request({
    url:'/api/cnfic-contract-manage/productitem/profitRateByProdId',
    method: 'get',
    params:{
      prodId:id,
      type:type
    }
  })
}
// 分润比例
export const getValuations = (params) => {
  return request({
    url:'/api/cnfic-contract-manage/productitem/getValuations',
    method: 'get',
    params: params
  })
}
// 图片
export const imagesByProdId = (id, type) => {
  return request({
    url:'/api/cnfic-contract-manage/productitem/imagesByProdId',
    method: 'get',
    params:{
      prodId:id,
      type:type
    }
  })
}

export const getItemAddedList = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/productitem/getAddedList',
    method: 'post',
    params:params
  })
}

