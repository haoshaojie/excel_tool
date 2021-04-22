import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/product/list',
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
    url: '/api/cnfic-contract-manage/product/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/product/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/product/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/product/update',
    method: 'post',
    data: row
  })
}
export const updateState = (ids, state) => {
  return request({
    url: '/api/cnfic-contract-manage/product/updateState',
    method: 'post',
    params:{
      ids: ids,
      state: state
    }
  })
}
export const validProdCode = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/product/validProdCode',
    method: 'post',
    params:params
  })
}
export const getProdAddedList = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/product/getAddedList',
    method: 'post',
    params:params
  })
}
// 导出产品项
export const exportData = () => {
  return '/api/cnfic-contract-manage/product/exportData';
}

