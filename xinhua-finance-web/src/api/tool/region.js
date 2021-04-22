

export default {
    install(Vue)  {
        Vue.prototype.getRegionTree = function (regionLevel){
            return "/api/cnfic-system/region/tree?regionLevel="+regionLevel
        }
    }
}

