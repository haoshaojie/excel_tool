import request from '@/router/axios';
//账号编辑
export const update = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/account/update',
    method: 'post',
    data: row
  })
}
//账号锁定/解锁
export const resetLock = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/account/resetlock',
    method: 'post',
    data: row
  })
}
//密码重置
export const resetPassword = (row) => {
  return request({
    url: '/api/cnfic-contract-manage/account/resetpassword',
    method: 'post',
    data: row
  })
}

