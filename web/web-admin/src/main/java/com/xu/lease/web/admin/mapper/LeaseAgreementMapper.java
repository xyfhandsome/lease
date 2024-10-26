package com.xu.lease.web.admin.mapper;

import com.xu.lease.model.entity.LeaseAgreement;
import com.xu.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.xu.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author liubo
* @description 针对表【lease_agreement(租约信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.atguigu.lease.model.LeaseAgreement
*/
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {

    IPage<AgreementVo> pageL(IPage<AgreementVo> page, AgreementQueryVo queryVo);
}




