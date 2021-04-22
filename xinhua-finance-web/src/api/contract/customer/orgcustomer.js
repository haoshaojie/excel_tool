import request from '@/router/axios';

export const getList = (current, size, params) => {
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
export const getCustomerList = (current, size, params) => {
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

export const getDetail = (id) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/submit',
    method: 'post',
    data: row
  })
}
export const queryALl = (code) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcust/queryALl',
    method: 'post',
    params: {
      code,
    }
  })
}
export const exportData = (column) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/export',
    method: 'get',

  })
}
export const submitpassdata = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/submitpassdata',
    method: 'get',
    params: {
      ...params,
    }
  })
}
export const verifCustName = (custName) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/verifCustName',
    method: 'get',
    params: {
      custName,
    }
  })
}
export const checkpassdata = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/getImportData',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const custTree = (custLevel) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcust/tree',
    method: 'get',
    params:{
      custLevel
    }
  })
}
export const companySearch = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/orgcustomer/companySearch',
    method: 'get',
    params:{
      ...params,
    }
  })
}
