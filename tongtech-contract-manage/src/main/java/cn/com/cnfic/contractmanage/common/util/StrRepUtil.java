package cn.com.cnfic.contractmanage.common.util;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串补充工具类
 * 字符串工具类统一使用hutool中的工具类，如果没有，可以在此类中补充
 * @Auther zhaijw
 * @Date 2021/3/26
 */
public class StrRepUtil {
    /**
     * 将字符串通过分隔符转化为list集合
     * @param source 需要转化的字符串
     * @param division 分隔符
     * @return 转化后的list集合
     */
    public static List<String> strConvertList(String source, String division){
        List<String> result = new ArrayList<String>();
        if(StrUtil.isEmpty(source) || StrUtil.isEmpty(division)){
            return result;
        }
        String[] arr = source.split(division);
        for(int i = 0;i< arr.length;i++){
            if(StrUtil.isNotEmpty(arr[i])){
                result.add(arr[i]);
            }
        }
        return result;
    }
}
