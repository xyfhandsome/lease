package com.xu.lease.web.admin.service;

import com.xu.lease.model.entity.ViewAppointment;
import com.xu.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.xu.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ViewAppointmentService extends IService<ViewAppointment> {

    IPage<AppointmentVo> pageV(IPage<AppointmentVo> page, AppointmentQueryVo queryVo);
}