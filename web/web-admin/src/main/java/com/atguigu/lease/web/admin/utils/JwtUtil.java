package com.atguigu.lease.web.admin.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author peter
 * @date 2024/10/24 19:44
 **/
//jwt工具类
public class JwtUtil {
    private static long tokenExpiration = 60*60*1000L;
    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());
    public static String createToken(Long userId,String username){
        String token = Jwts.builder().setSubject("USER_INFO").setExpiration(new Date(System.currentTimeMillis()+tokenExpiration)).
                claim("userId",userId).claim("username",username).signWith(tokenSignKey,SignatureAlgorithm.HS256).compact();
        return token;
    }
//    解密token获取用户信息
    public static Claims parseToken(String token){

        if (token==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }

        try{
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(tokenSignKey).build();
            return jwtParser.parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
    public static void main(String[] args){
        System.out.println(JwtUtil.createToken(2L,"user")); }
}
