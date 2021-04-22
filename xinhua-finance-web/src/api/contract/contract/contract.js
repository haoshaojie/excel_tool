import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/contract/page',
    method: 'get',
    params: {
    ...params,
    current,
    size,
    }
  })
}

export const saveBasicInfo = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/contract/saveBasicInfo',
    method: 'post',
    data:params
  })
}

export const saveUserAgreementInfo = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/contract/saveUserAgreementInfo',
    method: 'post',
    data:params
  })
}

export const removeContract = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/contract/removeContract',
    method: 'post',
    data:params
  })
}
