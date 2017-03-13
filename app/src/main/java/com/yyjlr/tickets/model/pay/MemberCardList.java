package com.yyjlr.tickets.model.pay;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Elvira on 2017/3/2.
 * 绑定会员卡返回的数据
 */

public class MemberCardList implements Serializable {
    private List<MemberCard> memberCardList;//会员卡

    public MemberCardList() {
    }

    public List<MemberCard> getMemberCardList() {
        return memberCardList;
    }

    public void setMemberCardList(List<MemberCard> memberCardList) {
        this.memberCardList = memberCardList;
    }
}
