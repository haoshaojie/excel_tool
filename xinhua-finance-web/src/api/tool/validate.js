export const validateNumber = (rule, value, callback) => {
  var reg = new RegExp("^[0-9]*$");
  if (!reg.test(value)) {
    callback(new Error('请输入数字'))
  } else {
    callback()
  }
}
export const validatePhone = (rule, value, callback) => {
  var reg = new RegExp("^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
  if (!reg.test(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}
export const validateEmail = (rule, value, callback) => {
  var reg = new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
  if (!reg.test(value)) {
    callback(new Error('请输入正确的邮箱'))
  } else {
    callback()
  }
}
export const validateTelephone = (rule, value, callback) => {
  var reg = new RegExp("^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$");
  if (!reg.test(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}
