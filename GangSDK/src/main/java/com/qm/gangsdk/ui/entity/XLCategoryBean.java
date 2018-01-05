package com.qm.gangsdk.ui.entity;

/**
 * 作者：shuzhou on 2017/8/9.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 *
 * 添加TabLayout使用的辅助类
 */
public class XLCategoryBean {
    public static final int TYPE_GANGIN_CHAT = 0X001;   //聊天
    public static final int TYPE_INFO = 0X002;          //信息
    public static final int TYPE_MEMBER = 0X003;        //成员
    public static final int TYPE_MANAGE = 0X004;        //管理
    public static final int TYPE_TASK = 0X005;          //任务

    public static final int TYPE_SORT = 0X006;          //排行
    public static final int TYPE_GANGOUT_CHAT = 0X007;  //聊天
    public static final int TYPE_RECOMMEND = 0X008;     //推荐
    public static final int TYPE_APPLY = 0X009;         //申请
    public static final int TYPE_INVITE = 0X00A;        //邀请

    public static final int TYPE_RENQI = 0X011;         //人气榜
    public static final int TYPE_LEVEL = 0X012;         //等级榜
    public static final int TYPE_CAIFU = 0X013;         //财富榜

    public static final int TYPE_GANGMINE = 0010;       //社群频道
    public static final int TYPE_GANGRECRUIT = 0011;    //招募频道
    public static final int TYPE_CHATSINGLE = 0012;     //私聊频道

    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XLCategoryBean(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
