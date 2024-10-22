package com.xu.lease.web.admin.mapper;

import com.xu.lease.common.result.Result;
import com.xu.lease.model.entity.ApartmentInfo;
import com.xu.lease.model.enums.LeaseStatus;
//import com.xu.lease.web.admin.vo.apartment.ApartmentItemVo;
//import com.xu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentQueryVo;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.atguigu.lease.model.ApartmentInfo
*/
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo);

}




