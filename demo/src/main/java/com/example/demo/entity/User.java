package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.List;
// 如果表名和类名不一致，需要加入下行注释将该类绑定对应的表
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)    // 标识主键自增
    private int id;
    private String username;
    private String password;
    private String birthday;

    // 描述用户的所有订单
    @TableField(exist = false)  // 声明orders不存在与t_user表中，BaseMapper的查询不用去查orders字段
    private List<Order> orders;


    // alt + insert

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
