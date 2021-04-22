import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-contract-manage/mailRecord/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const sendTemplateMail = (params) => {
  return request({
    url: '/api/cnfic-contract-manage/mailRecord/sendtemplatemail',
    method: 'post',
    data: params
  })
}



