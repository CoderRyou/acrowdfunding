package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Advertisement;

import java.util.List;

public interface AdvertService {

    int insertAdvert(Advertisement advert);

    List<Advertisement> getAllAdverts();
}
