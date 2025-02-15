package com.nekods.nyaPlus.smallTools;

import com.nekods.nyaPlus.exceptions.*;

import com.alibaba.fastjson.*;

import java.util.ArrayList;

public class Toolbox {

    public Toolbox() {
    }

    static public String getStringByIndexes(String list,String[] indexes){
        JSONObject jsonObj = JSON.parseObject(list);
        int i = 0;
        for (i = 0; i < indexes.length - 1; i++){
            jsonObj = jsonObj.getJSONObject(indexes[i]);
        }
        return jsonObj.getString(indexes[i]);
    }


    static private String getStringByIndex(String list,String index){
        int length = 0;
        String result = null;
        try {
            if (list.charAt(0) == '{') {
                JSONObject json = JSON.parseObject(list);
                result = json.getString(index);
            } else if (list.charAt(0) == '[') {      //弱检查，但是无所谓，会报错的
                JSONArray json = JSON.parseArray(list);
                length = json.size();
                result = json.getString(Integer.parseInt(index));
            } else {
                throw new NyaInvalidJSONparamException(list);  //这个是给连开头都不对的神金
            }
            if (result == null){
                throw new NyaUndefinedJSONIndex(list,index);
            }else{
                return result;
            }
        /*}catch (NumberFormatException e){
            throw new NyaArrayIndexParamException(index);*/
        }catch (IndexOutOfBoundsException e){
            throw new NyaIndexOutOfBoundsException(Integer.parseInt(index),length,list);
        }catch (RuntimeException e){
            // throw new NyaInvalidJSONparamException(list);   //这个是给开头正确，内部错误的数组用的
            throw e;
        }
    }


    /**
     * 根据给定的索引列表从 JSON 字符串中提取嵌套的字符串值。*/
    static public String getStringByIndexes(String list, ArrayList<String> indexes){
        for (int i = 0; i < indexes.size() - 1/* 这里减一，因为最后一个要腾出来，是字符串 */; i++) {
            list = getStringByIndex(list, indexes.get(i));
        }
        list = getStringByIndex(list, indexes.getLast());
        return list;   //本来想要不要重新写一个变量，后来想想没必要，算了浪费，反正就一行词不达意
    }


    /**TIP
     * 将字符串依照指定分隔符进行分割，同时考虑到引号内部不进行分割，以达到分割出来的字符串中可以包含分隔符的效果
     *
     * 包含引号内的引号转录
     * @param source 待分割字符串
     * @param index 分隔符
     * @param limit 最大分割数量（超出的一律放在最后一个）
     *
     * @return 一个String数组
     *
     * @throws UnsupportedOperationException 我不允许你的分隔符是引号
     * */
    static public String[] splitStringByIndex(String source, char index, int limit){
        if(index == '"'){
            throw new UnsupportedOperationException("去你妈的别给我卡bug，我不想处理这个，你就非要用这个双引号吗");
        }
        ArrayList<String> result= new ArrayList<>();
        boolean isInQuotationMark = false;
        ArrayList<Character> charSet= new ArrayList<>();

        for(int i=0;i<source.length();i++){
            char character = source.charAt(i);

            if(character == index && !isInQuotationMark && source.charAt(i - 1) != '"'){
                //遇到分隔符，但是除去前面 不是 引号的情况
                result.add(new String(charAL_to_charArray(charSet)));
                charSet.clear();
                if(limit == 1){
                    charSet.add(character);
                    continue;
                } else if(result.size() >= limit - 1 && limit != 0){  //如果大小超限
                    char[] chars = new char[source.length() - 1 - i];
                    source.getChars(i + 1,source.length(),chars,0);
                    result.add(String.valueOf(chars));
                    return result.toArray(new String[result.size()]);
                }
            } else if(character == index && ((!isInQuotationMark && source.charAt(i - 1) == '"') || (i == 0))){
                //遇到分隔符，但是除去前面 是 引号的情况
                //或者是第一个就是分隔符（啥都不考虑）
            } else if(character == '\\' && source.charAt(i - 1) != '\\') {
                //转义符情况
            } else if(character == '"' && (((i == 0)) || (!isInQuotationMark && source.charAt(i - 1) != '\\'))){
                //第一次遇到引号（考虑转义和是否在括号内）
                //或者是第一个就是引号（啥都不考虑）
                isInQuotationMark = true;
            } else if(character == '"' && isInQuotationMark && source.charAt(i - 1) != '\\'){
                //第二次遇到引号
                isInQuotationMark = false;
                result.add(new String(charAL_to_charArray(charSet)));
                charSet.clear();
                if(limit == 1){
                    return result.toArray(new String[result.size()]);
                } else if(result.size() == limit - 1 && limit != 0){  //如果大小超限
                    char[] chars = new char[source.length() - 1 - i];
                    source.getChars(i + 1,source.length(),chars,0);
                    result.add(String.valueOf(chars));
                    return result.toArray(new String[result.size()]);
                }
            }else{
                charSet.add(character);
            }

            /**
             * 关于大小超限的解释
             * 原本想的是当大小刚好差一个的时候将之后的所有的东西塞进数组，
             * 但是实测的时候发现，当limit=1的时候，仅添加一次就已经满了，
             * 这个时候再添加就会超过，因此将1的这种情况列为特殊情况
             *
             * 如果被引号包裹，那么正好适配，直接输出即可
             *
             * 如果中间存在分隔符，只能说目前最好的解决方法就是汇入charSet流中并假装无事发生
             *
             * 这是应急之举，整个代码结构看上去很混乱，如果后期有什么可以整合的想法，大可以重新来写
             *
             * “为什么你的代码这么智能”
             * “因为老子if-else叠得多”
             * */

            //由于每一次遇见分隔符（或者括号）时只对前面负责，但放在最后又会浪费性能，故在添加之后进行判断，
        }
        result.add(new String(charAL_to_charArray(charSet)));
        return result.toArray(new String[result.size()]);
    }


    /**
     * 一个方法，把char类型的ArrayList转换成Array
     * @param l 一个讨人厌的char类型的ArrayList
     *
     * @return 一个崭新的char类型数组

     */
    private static char[] charAL_to_charArray(ArrayList<Character> l) {
        char[] result = new char[l.size()];

        for(int i = 0; i < l.size(); ++i) {
            result[i] = (Character)l.get(i);
        }

        return result;
    }

    /**
     * 加在列表的最前面*/
    public static Object[] addToFirst(Object a, Object[] b){
        ArrayList<Object> result = new ArrayList<>();
        result.add(a);
        for(Object o : b){
            result.add(o);
        }
        return result.toArray();
    }

    public  static <T> boolean setHas(T[] set,T obj){
        for(T o : set){
            if(o.equals(obj)){
                return true;
            }
        }
        return false;
    }

    public static boolean isJson(String json) {
        try {
            JSON.parseObject(json);
            return true;
        } catch (JSONException var2) {
            return false;
        }
    }
}
