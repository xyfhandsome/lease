package com.xu.lease.web.admin.service.impl;

//import com.atguigu.lease.model.entity.*;
//import com.atguigu.lease.web.admin.mapper.*;
//import com.atguigu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xu.lease.model.entity.*;
import com.xu.lease.model.enums.ItemType;
import com.xu.lease.model.enums.ReleaseStatus;
import com.xu.lease.web.admin.mapper.*;
import com.xu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.lease.web.admin.vo.attr.AttrValueVo;
import com.xu.lease.web.admin.vo.graph.GraphVo;
import com.xu.lease.web.admin.vo.room.RoomDetailVo;
import com.xu.lease.web.admin.vo.room.RoomItemVo;
import com.xu.lease.web.admin.vo.room.RoomQueryVo;
import com.xu.lease.web.admin.vo.room.RoomSubmitVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private RoomAttrValueService roomAttrValueService;
    @Autowired
    private RoomFacilityService roomFacilityService;
    @Autowired
    private RoomLabelService roomLabelService;
    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;
    @Autowired
    private RoomLeaseTermService roomLeaseTermService;
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
//        判断更新还是插入
        boolean is_update =roomSubmitVo.getId() != null;
//        保存匹配的room_info
        super.saveOrUpdate(roomSubmitVo);
        //若为更新操作，则先删除与Room相关的各项信息列表
        if(is_update){
            //1.删除原有graphInfoList
            LambdaQueryWrapper<GraphInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GraphInfo::getItemId, roomSubmitVo.getId());
            queryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphInfoService.remove(queryWrapper);
            //2.删除原有roomAttrValueList
            LambdaQueryWrapper<RoomAttrValue> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(RoomAttrValue::getRoomId,roomSubmitVo.getId());
            roomAttrValueService.remove(queryWrapper1);
            //3.删除原有roomFacilityList
            LambdaQueryWrapper<RoomFacility> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(RoomFacility::getRoomId,roomSubmitVo.getId());
            roomFacilityService.remove(queryWrapper2);
            //4.删除原有roomLabelList
            LambdaQueryWrapper<RoomLabel> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(RoomLabel::getRoomId,roomSubmitVo.getId());
            roomLabelService.remove(queryWrapper3);
            //5.删除原有paymentTypeList
            LambdaQueryWrapper<RoomPaymentType> queryWrapper4 = new LambdaQueryWrapper<>();
            queryWrapper4.eq(RoomPaymentType::getRoomId,roomSubmitVo.getId());
            roomPaymentTypeService.remove(queryWrapper4);
            //6.删除原有leaseTermList
            LambdaQueryWrapper<RoomLeaseTerm> queryWrapper5 = new LambdaQueryWrapper<>();
            queryWrapper5.eq(RoomLeaseTerm::getRoomId,roomSubmitVo.getId());
            roomLeaseTermService.remove(queryWrapper5);
        }
        //1.保存新的graphInfoList
        List<GraphVo> list = roomSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(list)) {
            List<GraphInfo> list1 = new ArrayList<>();
            for (GraphVo graphVo : list) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoService.save(graphInfo);
                list1.add(graphInfo);
            }
            graphInfoService.saveBatch(list1);
        }

        //2.保存新的roomAttrValueList
        List<Long> attrIds = roomSubmitVo.getAttrValueIds();
        if (!CollectionUtils.isEmpty(attrIds)) {
            List<RoomAttrValue> list2 =new ArrayList<>();
            for (Long attrId : attrIds) {
                RoomAttrValue roomAttrValue = RoomAttrValue.builder().build();
                roomAttrValue.setRoomId(roomSubmitVo.getId());
                roomAttrValue.setAttrValueId(attrId);
                list2.add(roomAttrValue);
            }
            roomAttrValueService.saveBatch(list2);
        }

        //3.保存新的facilityInfoList
        List<Long>  facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            List<RoomFacility> list3 =new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                RoomFacility roomFacility = RoomFacility.builder().build();
                roomFacility.setFacilityId(facilityInfoId);
                roomFacility.setRoomId(roomSubmitVo.getId());
                list3.add(roomFacility);
            }
            roomFacilityService.saveBatch(list3);
        }

        //4.保存新的labelInfoList
        List<Long>  labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollectionUtils.isEmpty(labelInfoIds)) {
            List<RoomLabel> list4 =new ArrayList<>();
            for (Long labelInfoId : labelInfoIds) {
                RoomLabel roomLabel = RoomLabel.builder().build();
                roomLabel.setLabelId(labelInfoId);
                roomLabel.setRoomId(roomSubmitVo.getId());
                list4.add(roomLabel);
            }
            roomLabelService.saveBatch(list4);
        }

        //5.保存新的paymentTypeList
        List<Long>  paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (!CollectionUtils.isEmpty(paymentTypeIds)) {
            List<RoomPaymentType> list5 =new ArrayList<>();
            for (Long paymentTypeId : paymentTypeIds) {
                RoomPaymentType roomPaymentType = RoomPaymentType.builder().build();
                roomPaymentType.setPaymentTypeId(paymentTypeId);
                roomPaymentType.setRoomId(roomSubmitVo.getId());
                list5.add(roomPaymentType);
            }
            roomPaymentTypeService.saveBatch(list5);
        }

        //6.保存新的leaseTermList
        List<Long>  leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollectionUtils.isEmpty(leaseTermIds)) {
            List<RoomLeaseTerm> list6 =new ArrayList<>();
            for (Long leaseTermId : leaseTermIds) {
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder().build();
                roomLeaseTerm.setLeaseTermId(leaseTermId);
                roomLeaseTerm.setRoomId(roomSubmitVo.getId());
                list6.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(list6);
        }


    }

    @Override
    public IPage<RoomItemVo> pageRoomItemByQuery(IPage<RoomItemVo> page, RoomQueryVo queryVo) {

        return roomInfoMapper.pageRoomItemByQuery(page,queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        //1.查询RoomInfo
       RoomInfo roomInfo = super.getById(id);
        //2.查询所属公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());
        //3.查询graphInfoList
        List<GraphVo> graphInfoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);
        //4.查询attrValueList
        List<AttrValueVo> attrValueVoList = attrValueMapper.selectListByRoomId(id);
        //5.查询facilityInfoList
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);
        //6.查询labelInfoList
        List<LabelInfo> labelInfoList = labelInfoMapper.selectByRoomId(id);
        //7.查询paymentTypeList
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectByRoomId(id);
        //8.查询leaseTermList
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectByRoomId(id);
        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, roomDetailVo);
        roomDetailVo.setGraphVoList(graphInfoList);
        roomDetailVo.setAttrValueVoList(attrValueVoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setLabelInfoList(labelInfoList);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setLeaseTermList(leaseTermList);

        return roomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {
        super.removeById(id);
        //1.删除原有graphInfoList
        LambdaQueryWrapper<GraphInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GraphInfo::getItemId,id);
        queryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphInfoService.remove(queryWrapper);
        //2.删除原有roomAttrValueList
        LambdaQueryWrapper<RoomAttrValue> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(RoomAttrValue::getRoomId,id);
        roomAttrValueService.remove(queryWrapper1);
        //3.删除原有roomFacilityList
        LambdaQueryWrapper<RoomFacility> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(RoomFacility::getRoomId,id);
        roomFacilityService.remove(queryWrapper2);
        //4.删除原有roomLabelList
        LambdaQueryWrapper<RoomLabel> queryWrapper3 = new LambdaQueryWrapper<>();
        queryWrapper3.eq(RoomLabel::getRoomId,id);
        roomLabelService.remove(queryWrapper3);
        //5.删除原有paymentTypeList
        LambdaQueryWrapper<RoomPaymentType> queryWrapper4 = new LambdaQueryWrapper<>();
        queryWrapper4.eq(RoomPaymentType::getRoomId,id);
        roomPaymentTypeService.remove(queryWrapper4);
        //6.删除原有leaseTermList
        LambdaQueryWrapper<RoomLeaseTerm> queryWrapper5 = new LambdaQueryWrapper<>();
        queryWrapper5.eq(RoomLeaseTerm::getRoomId,id);
        roomLeaseTermService.remove(queryWrapper5);

    }

    @Override
    public void updateReleaseStatusById(Long id, ReleaseStatus status) {
        LambdaUpdateWrapper<RoomInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoomInfo::getId,id);
       updateWrapper.set(RoomInfo::getIsRelease,status);
       super.update(updateWrapper);

    }

    @Override
    public List<RoomInfo> listBasicByApartmentId(Long id) {
        LambdaQueryWrapper<RoomInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoomInfo::getApartmentId,id);
        queryWrapper.eq(RoomInfo::getIsRelease,ReleaseStatus.RELEASED);
        List<RoomInfo> list = super.list(queryWrapper);
        return list;
    }
}




