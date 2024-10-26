package com.xu.lease.web.admin.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xu.lease.model.entity.LeaseAgreement;
import com.xu.lease.model.enums.LeaseStatus;
import com.xu.lease.web.admin.service.LeaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author peter
 * @date 2024/10/23 19:16
 **/
@Component
public class ScheduledTasks {
    @Autowired
    private LeaseAgreementService leaseAgreementService;

//    通过定时任务定时检查租约是否到期。
    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseStatus(){
        LambdaUpdateWrapper<LeaseAgreement> updateWrapper = new LambdaUpdateWrapper<>();
        Date date = new Date();
        updateWrapper.le(LeaseAgreement::getLeaseEndDate, date);
        updateWrapper.eq(LeaseAgreement::getStatus, LeaseStatus.SIGNED);
        updateWrapper.in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWING);
        leaseAgreementService.update(updateWrapper);


    }
}
