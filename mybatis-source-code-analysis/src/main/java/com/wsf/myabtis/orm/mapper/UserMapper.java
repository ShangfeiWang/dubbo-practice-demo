package com.wsf.myabtis.orm.mapper;

import com.wsf.myabtis.orm.model.UserDO;

import java.util.List;

/**
 * @author wangshangfei
 * @date 2024/03/11
 * @description
 */
public interface UserMapper {

    List<UserDO> findAll();

}
