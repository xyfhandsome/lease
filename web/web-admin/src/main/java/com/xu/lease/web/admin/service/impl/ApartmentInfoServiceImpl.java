package com.xu.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.lease.model.entity.*;
import com.xu.lease.model.enums.ItemType;
import com.xu.lease.web.admin.mapper.ApartmentInfoMapper;
import com.xu.lease.web.admin.mapper.RoomInfoMapper;
import com.xu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.xu.lease.web.admin.vo.graph.GraphVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private ApartmentFacilityService apartmentFacilityService;
    @Autowired
    private ApartmentLabelService apartmentLabelService;
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;
    @Autowired
    private RoomInfoMapper roomInfoMapper;


    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
//        判断是更新还是插入 更新没有id
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);

        if (isUpdate) {
//            删除公寓图片列表
            LambdaQueryWrapper<GraphInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            queryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
            graphInfoService.remove(queryWrapper);

//            删除公寓配套
            LambdaQueryWrapper<ApartmentFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
            facilityQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            apartmentFacilityService.remove(facilityQueryWrapper);
//            删除公寓标签
            LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
//            labelQueryWrapper.eq(ApartmentLabel::get)
            labelQueryWrapper.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
            apartmentLabelService.remove(labelQueryWrapper);

//            删除公寓杂费值
            LambdaQueryWrapper<ApartmentFeeValue> feeValueQueryWrapper = new LambdaQueryWrapper<>();
            feeValueQueryWrapper.eq(ApartmentFeeValue::getApartmentId,apartmentSubmitVo.getId());
//            feeValueQueryWrapper.eq(ApartmentFeeValue::get)
            apartmentFeeValueService.remove(feeValueQueryWrapper);

        }

//        插入公寓图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if(!CollectionUtils.isEmpty(graphVoList)){
            ArrayList<GraphInfo> list = new ArrayList<>();
            for(GraphVo graphVo:graphVoList){
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                list.add(graphInfo);

            }
            graphInfoService.saveBatch(list);
        }
//        插入公寓配套列表
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if(!CollectionUtils.isEmpty(facilityInfoIds)){
            ArrayList<ApartmentFacility> facilityInfoIdList = new ArrayList<>();
            for(Long facilityInfoId:facilityInfoIds){
//                使用构造者模式构造对象
               ApartmentFacility apartmentFacility = ApartmentFacility.builder().facilityId(facilityInfoId).apartmentId(apartmentSubmitVo.getId()).build();

//               apartmentFacility.setFacilityId(facilityInfoId);
//               apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
               facilityInfoIdList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(facilityInfoIdList);
        }

//        插入公寓标签
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if(!CollectionUtils.isEmpty(labelIds)){
            ArrayList<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for(Long labelId:labelIds){
                ApartmentLabel apartmentLabel = ApartmentLabel.builder().labelId(labelId).apartmentId(apartmentSubmitVo.getId()).build();
//                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
//                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);

            }
            apartmentLabelService.saveBatch(apartmentLabelList);
        }
//        插入公寓杂费值

        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if(!CollectionUtils.isEmpty(feeValueIds)){
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for(Long feeValueId:feeValueIds){
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder().feeValueId(feeValueId).apartmentId(apartmentSubmitVo.getId()).build();
//                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
//                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);

            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }



    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
        return roomInfoMapper.pageItem(page,queryVo)
                ;
    }
}




