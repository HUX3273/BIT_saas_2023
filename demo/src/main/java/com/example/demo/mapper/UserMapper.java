package com.example.demo.mapper;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {  // mybatis会根据User类去操作对应的user数据库表
                                                        // ！！！注意类名和表名应该保持一致！！！
// extends BaseMapper<User> 后以下代码全部无用了，因为在BaseMapper中有下面的这些方法
//    // 查询所有用户
//    @Select("select * from user")
//    public List<User> find();
//
//    @Select("select * from user where id=#{id}")
//    public User findById(int id);
//
//    @Insert("insert into user values(#{id},#{username},#{password},#{birthday})")
//    public int insert(User user);   // int 表示插入了几条记录，如果插入失败返回值是0
//
//    @Update("update user set username=#{username},password=#{password},birthday=#{birthday} where id=#{id}")
//    public int update(User user);
//
//    @Delete("delete from user where id=#{id}")
//    public int delete(int id);


    @Select("select * from t_user where id = #{id}")
    public User selectById(int id);
    // 查询用户及其所有的订单
    @Select("select * from t_user")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "password",property = "password"),
            @Result(column = "birthday",property = "birthday"),
            @Result(column = "id",property = "orders",javaType = List.class,
                many = @Many(select = "com.example.demo.mapper.OrderMapper.selectByUid")
            )   // 用many因为对象的javaType = List.class，
                // 调用OrderMapper中的selectByUid方法返回的List<Order>
                // 会被赋给property = "orders"里的orders，并被select查出，实现了跨表查询
        }
    )
    List<User> selectAllUserAndOrders();


}
