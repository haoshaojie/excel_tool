

export default {
    install(Vue)  {
        Vue.prototype.getDicValue = function (code){
            return "/api/cnfic-system/dict/dictionary?code="+code
        }
    }
}

