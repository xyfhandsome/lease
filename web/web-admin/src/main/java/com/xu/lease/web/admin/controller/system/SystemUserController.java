package com.xu.lease.web.admin.controller.system;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.lease.common.result.Result;
import com.xu.lease.model.entity.SystemPost;
import com.xu.lease.model.entity.SystemUser;
import com.xu.lease.model.enums.BaseStatus;
import com.xu.lease.web.admin.service.SystemPostService;
import com.xu.lease.web.admin.service.SystemUserService;
import com.xu.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.xu.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemPostService systemPostService;

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        IPage<SystemUserItemVo> page = new Page<>(current, size);
        IPage<SystemUserItemVo> result = systemUserService.pageU(page,queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
       SystemUser systemUser= systemUserService.getById(id);
       SystemUserItemVo vo = new SystemUserItemVo();
        SystemPost systemPost = systemPostService.getById(systemUser.getPostId());
        BeanUtils.copyProperties(systemUser,vo);
        vo.setPostName(systemPost.getName());
        return Result.ok(vo);
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
//        密码非空 加密密码
        if(systemUser.getPassword()!=null){
            systemUser.setPassword(DigestUtils.md5Hex(systemUser.getPassword()));
        }
//        默认字段非空更新
        systemUserService.saveOrUpdate(systemUser);
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
//        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<SystemUser>();
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername,username);
        if( systemUserService.count(queryWrapper)==0)
        {return Result.ok(false);}
        return Result.ok(true);}

        





    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        systemUserService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemUser::getId,id);
        updateWrapper.set(SystemUser::getStatus,status);
        systemUserService.update(updateWrapper);
        return Result.ok();
    }
}
