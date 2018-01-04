package com.qm.gangsdk.ui.custom.style;

import android.content.res.ColorStateList;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.entity.SpannableStringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: mengbo
 * Time: 2017/11/8 18:10
 * Description: 多样字符串风格类
 */

public class SpannableStringStyle {

    /**
     * 从某个字符的在字符串中最后一个字符开始 到字符串结束，改变此段字符颜色
     *
     * @param content 字符串内容
     * @param color 改变的颜色
     * @param lastChar 最后一个符号
     * @return
     */
    public static SpannableStringBuilder buildStyleFromLastcharToEnd(String content, int color, String lastChar){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        int msgStart = content.lastIndexOf(lastChar) + 1;
        int msgEnd = content.length();
        spannableStringBuilder.setSpan(new ForegroundColorSpan(color), msgStart, msgEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }


    /**
     * 从某个字符的在字符串中第一个字符开始 到字符串结束，改变此段字符颜色
     *
     * @param content       字符串内容
     * @param color         改变的颜色
     * @param firstchar     要改变的字符
     * @param changesize    要改变的字符大小
     * @return
     */
    public static SpannableStringBuilder buildStyleFromFirstChar(String content, int color, String firstchar, float changesize){
        ColorStateList stateList = ColorStateList.valueOf(color);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        int msgStart = 0;
        int msgEnd = firstchar.length();
        spannableStringBuilder.setSpan(new TextAppearanceSpan(null, 0, (int)changesize, stateList, null), msgStart, msgEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }


    /**
     * 传入的list中，片段字符串SpannableStringBean如果是默认色值，可以设置参数0，如：new SpannableStringBean("abc",0);
     *
     * @param list 片段字符及对应色值集合
     * @param defaultColor 字符串默认色值
     * @return
     */
    public static SpannableStringBuilder buildStyle(List<SpannableStringBean> list, int defaultColor){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for(SpannableStringBean textBean: list){
            String text = StringUtils.getString(textBean.getText(), "");
            int color = textBean.getColor();
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            if(color == 0){
                builder.setSpan(new ForegroundColorSpan(defaultColor), 0, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }else{
                builder.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            spannableStringBuilder.append(builder);
        }
        return spannableStringBuilder;
    }

    /**
     *将字符串分割，得到多样风格字符串。
     *
     * @param content 字符内容
     * @param regexSpannableList 带正则表达式的List<SpannableStringBean> 如:text="\\{\\$nickname\\$\\}" ;replaceText为替换内容
     * @param defaultColor 默认色值
     * @return
     */
    public static SpannableStringBuilder buildStyleReplaceParts(String content, List<SpannableStringBean> regexSpannableList, int defaultColor){
        content = "{$}"+content+"{$}";
        List<SpannableStringBean> resultSplitList = new ArrayList<>();
        SpannableStringBean spannableStringBean = new SpannableStringBean(content, 0, true);
        resultSplitList.add(spannableStringBean);

        for(SpannableStringBean regexSpannable :regexSpannableList){
            for(int i = 0; i<resultSplitList.size(); i++){
                boolean isSplitable = resultSplitList.get(i).isSplitable();
                String text = resultSplitList.get(i).getText();
                if(isSplitable){
                    List<SpannableStringBean> newSplitList = splitParts(text, regexSpannable, defaultColor);
                    resultSplitList.remove(i);
                    resultSplitList.addAll(i,newSplitList);
                    i += newSplitList.size()-1;
                }
            }
        }
        SpannableStringBuilder spannableStringBuilder = buildStyle(resultSplitList, defaultColor);
        spannableStringBuilder = spannableStringBuilder.delete(0,3).delete(spannableStringBuilder.length()-3,spannableStringBuilder.length());
        return spannableStringBuilder;
    }

    /**
     *将字符串分割，返回List<SpannableStringBean>。
     *
     * @param content 字符内容
     * @param regexSpannable 带正则表达式的SpannableStringBean 如:text="\\{\\$nickname\\$\\}"
     * @param defaultColor 默认色值
     * @return
     */
    public static List<SpannableStringBean> splitParts(String content, SpannableStringBean regexSpannable, int defaultColor){
        List<SpannableStringBean> list = new ArrayList<>();
        String regexText = regexSpannable.getText();
        String replaceText = regexSpannable.getReplaceText();
        int replaceColor = regexSpannable.getColor();
        String[] parts = content.split(regexText);

        if(parts.length > 1){
            for(int i=0; i<parts.length; i++){
                if(i != parts.length-1){
                    list.add(new SpannableStringBean(parts[i], 0, true));
                    list.add(new SpannableStringBean(replaceText, replaceColor, false));
                }else{
                    list.add(new SpannableStringBean(parts[i], 0, true));
                }
            }
        }else{
            if(content.contains(regexText)){
                if(content.indexOf(regexText) == 0){
                    list.add(new SpannableStringBean(replaceText, replaceColor, false));
                    list.add(new SpannableStringBean(content, defaultColor, false));
                }else{
                    list.add(new SpannableStringBean(content, defaultColor, false));
                    list.add(new SpannableStringBean(replaceText, replaceColor, false));
                }
            }else{
                list.add(new SpannableStringBean(content, defaultColor, false));
            }
        }
        return list;
    }

}
