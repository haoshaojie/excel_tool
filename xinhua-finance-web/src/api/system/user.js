import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cnfic-user/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const remove = (ids) => {
  return request({
    url: '/api/cnfic-user/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cnfic-user/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cnfic-user/update',
    method: 'post',
    data: row
  })
}

export const grant = (userIds, roleIds) => {
  return request({
    url: '/api/cnfic-user/grant',
    method: 'post',
    params: {
      userIds,
      roleIds,
    }
  })
}

export const getUser = (id) => {
  return request({
    url: '/api/cnfic-user/detail',
    method: 'get',
    params: {
      id,
    }
  })
}

export const getUserInfo = () => {
  return request({
    url: '/api/cnfic-user/info',
    method: 'get',
  })
}

export const resetPassword = (userIds) => {
  return request({
    url: '/api/cnfic-user/reset-password',
    method: 'post',
    params: {
      userIds,
    }
  })
}

export const updatePassword = (oldPassword, newPassword, newPassword1) => {
  return request({
    url: '/api/cnfic-user/update-password',
    method: 'post',
    params: {
      oldPassword,
      newPassword,
      newPassword1,
    }
  })
}

/**
 * 用户菜单授权
 * @param userIds 用户id集合
 * @param menuIds 菜单id集合
 * @returns {AxiosPromise}
 */
export const menuGrant = (userIds, menuIds) => {
  return request({
    url: '/api/cnfic-user/menuGrant',
    method: 'post',
    data: {
      userIds,
      menuIds
    }
  });
}

/**
 * 获取已授权的用户菜单
 * @param userIds 用户id字符串(逗号隔开)
 * @returns {AxiosPromise}
 */
export const getMenuRole = (userIds) => {
  return request({
    url: '/api/cnfic-user/user-menu-tree-keys',
    method: 'get',
    params: {
      userIds,
    }
  });
}

/**
 * 获取已授权的用户角色
 * @param userIds 用户id字符串(逗号隔开)
 * @returns {AxiosPromise}
 */
export const getUserRole = (userIds) => {
  return request({
    url: '/api/cnfic-user/user-role-tree-keys',
    method: 'get',
    params: {
      userIds,
    }
  });
}
