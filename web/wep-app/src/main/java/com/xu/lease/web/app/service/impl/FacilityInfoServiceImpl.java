package com.xu.lease.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.lease.model.entity.FacilityInfo;
import com.xu.lease.web.app.service.FacilityInfoService;
import com.xu.lease.web.app.mapper.FacilityInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author liubo
* @description 针对表【facility_info(配套信息表)】的数据库操作Service实现
* @createDate 2023-07-26 11:12:39
*/
@Service
public class FacilityInfoServiceImpl extends ServiceImpl<FacilityInfoMapper, FacilityInfo>
    implements FacilityInfoService{

}




