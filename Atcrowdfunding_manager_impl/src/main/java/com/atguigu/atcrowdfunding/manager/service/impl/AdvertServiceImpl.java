package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Advertisement;
import com.atguigu.atcrowdfunding.manager.dao.AdvertisementMapper;
import com.atguigu.atcrowdfunding.manager.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertServiceImpl implements AdvertService{

    @Autowired
    AdvertisementMapper advertisementMapper;

    @Override
    public int insertAdvert(Advertisement advert) {
        return advertisementMapper.insert(advert);
    }

    @Override
    public List<Advertisement> getAllAdverts() {
        return advertisementMapper.selectByExample(null);
    }
}
