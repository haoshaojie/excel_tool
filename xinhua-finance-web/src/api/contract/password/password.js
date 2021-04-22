import request from '@/router/axios';

export const queryPassword = (userId) => {
  return request({
    url: '/api/cnfic-contract-manage/password/queryPassword',
    method: 'get',
    params: {
      userId
    }
  })
}
