import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/category/page',
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
    url: '/api/cnfic-contract-manage/category/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/category/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/category/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/category/update',
    method: 'post',
    data: row
  })
}
// 获取 产品类型下拉项列表
export const selectItems = () => {
  return request({
    url: '/api/cnfic-contract-manage/category/selectItems',
    method: 'post'
  })
}


