package com.atguigu.atcrowdfunding.utils;

import com.atguigu.atcrowdfunding.bean.MemberCert;

import java.util.ArrayList;
import java.util.List;

public class Data {

    List<Integer> ids = new ArrayList<>();

    List<MemberCert> certimgs = new ArrayList<>();

    public List<MemberCert> getCertimgs() {
        return certimgs;
    }

    public void setCertimgs(List<MemberCert> certimgs) {
        this.certimgs = certimgs;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
