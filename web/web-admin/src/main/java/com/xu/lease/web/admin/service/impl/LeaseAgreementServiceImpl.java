package com.xu.lease.web.admin.service.impl;

//import com.atguigu.lease.model.entity.*;
//import com.atguigu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xu.lease.model.entity.*;
import com.xu.lease.web.admin.mapper.LeaseAgreementMapper;
import com.xu.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.xu.lease.web.admin.vo.agreement.AgreementVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {
    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;
    @Autowired
    private ApartmentInfoService apartmentInfoService;
    @Autowired
    private RoomInfoService roomInfoService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private LeaseTermService leaseTermService;


    @Override
    public IPage<AgreementVo> pageL(IPage<AgreementVo> page, AgreementQueryVo queryVo) {

        return leaseAgreementMapper.pageL(page, queryVo);
    }

    @Override
    public AgreementVo getByIdL(Long id) {
        LeaseAgreement leaseAgreement = super.getById(id);
        ApartmentInfo apartmentInfo = apartmentInfoService.getById(leaseAgreement.getApartmentId());
        RoomInfo roomInfo = roomInfoService.getById(leaseAgreement.getRoomId());
        PaymentType paymentType = paymentTypeService.getById(leaseAgreement.getPaymentTypeId());
        LeaseTerm leaseTerm = leaseTermService.getById(leaseAgreement.getLeaseTermId());


        AgreementVo agreementVo = new AgreementVo();
        BeanUtils.copyProperties(leaseAgreement, agreementVo);
        agreementVo.setApartmentInfo(apartmentInfo);
        agreementVo.setRoomInfo(roomInfo);
        agreementVo.setPaymentType(paymentType);
        agreementVo.setLeaseTerm(leaseTerm);
        return agreementVo;


    }
}




