import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/personalaccount/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getPersonalDetail = (accId) => {
  return request({
    url: '/api/cnfic-contract-manage/personalaccount/detail',
    method: 'get',
    params: {
      accId
    }
  })
}

export const listOrder = (current, size, phoneNumber, mailbox) => {
  return request({
    url: '/api/cnfic-contract-manage/userordergoods/page',
    method: 'get',
    params: {
      phoneNumber,
      mailbox,
      current,
      size,
    }
  })
}

export const getUserDetail = (accountId) => {
  return request({
    url: '/api/cnfic-contract-manage/personaluser/detail',
    method: 'get',
    params: {
      accountId
    }
  })
}


