package com.xu.lease.web.admin.service.impl;

//import com.atguigu.lease.model.entity.*;
//import com.atguigu.lease.web.admin.mapper.*;
//import com.atguigu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.lease.common.exception.LeaseException;
import com.xu.lease.common.result.ResultCodeEnum;
import com.xu.lease.model.entity.*;
import com.xu.lease.model.enums.ItemType;
import com.xu.lease.web.admin.mapper.*;
import com.xu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.xu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.xu.lease.web.admin.vo.fee.FeeValueVo;
import com.xu.lease.web.admin.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
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
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private FeeValueMapper feeValueMapper;
    @Autowired
    private LabelInfoMapper labelInfoMapper;
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
        return apartmentInfoMapper.pageItem(page,queryVo);
    }
    @Override
    public ApartmentDetailVo getDetailById(Long id){

//        查询apartment_info
//        apartmentInfoMapper.selectById(id);
       ApartmentInfo apartmentInfo =  this.getById(id);
        if (apartmentInfo == null) {
            return null;
        }
//        查询label
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);

//        查询graph_info
//        LambdaQueryWrapper<GraphInfo> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(GraphInfo::getItemId, id);
//        queryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
//        List<GraphVo> graphVoList = graphInfoService.getObj(queryWrapper);
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT,id);
//        查询facility_info
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
//        查询fee_value
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);

        ApartmentDetailVo adminApartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, adminApartmentDetailVo);
        adminApartmentDetailVo.setLabelInfoList(labelInfoList);
        adminApartmentDetailVo.setGraphVoList(graphVoList);
        adminApartmentDetailVo.setFacilityInfoList(facilityInfoList);
        adminApartmentDetailVo.setFeeValueVoList(feeValueVoList);
        return adminApartmentDetailVo;


    }

    @Override
    public void removeByIdApartment(Long id) {
//        删除公寓信息
        LambdaQueryWrapper<RoomInfo> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(RoomInfo::getApartmentId, id);
        Long count = roomInfoMapper.selectCount(roomQueryWrapper);
        if (count > 0) {
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }
//        super.removeById(id);
        //1.删除GraphInfo
        LambdaQueryWrapper<GraphInfo> graphInfoQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphInfoQueryWrapper);
        //2.删除ApartmentLabel
        LambdaQueryWrapper<ApartmentLabel> apartmentLabelQueryWrapper = new LambdaQueryWrapper<>();
        apartmentLabelQueryWrapper.eq(ApartmentLabel::getApartmentId, id);
        apartmentLabelService.remove(apartmentLabelQueryWrapper);
        //3.删除ApartmentFacility
        LambdaQueryWrapper<ApartmentFacility> apartmentFacilityQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFacilityQueryWrapper.eq(ApartmentFacility::getApartmentId, id);
        apartmentFacilityService.remove(apartmentFacilityQueryWrapper);

        //4.删除ApartmentFeeValue
        LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId, id);
        apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);
        //5.删除ApartmentInfo
        super.removeById(id);


//

    }
}




