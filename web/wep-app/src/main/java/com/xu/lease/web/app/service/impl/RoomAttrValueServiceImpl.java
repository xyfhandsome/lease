package com.xu.lease.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.lease.model.entity.RoomAttrValue;
import com.xu.lease.web.app.service.RoomAttrValueService;
import com.xu.lease.web.app.mapper.RoomAttrValueMapper;
import org.springframework.stereotype.Service;

/**
* @author liubo
* @description 针对表【room_attr_value(房间&基本属性值关联表)】的数据库操作Service实现
* @createDate 2023-07-26 11:12:39
*/
@Service
public class RoomAttrValueServiceImpl extends ServiceImpl<RoomAttrValueMapper, RoomAttrValue>
    implements RoomAttrValueService{

}



