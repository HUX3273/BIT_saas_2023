package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/user")
    public List query(){    // 返回List，是json格式的
        List<User> list = userMapper.selectList(null);
        System.out.println(list);
        return list;
    }


    @PostMapping("/insert")
    public String save(User user){
        int i = userMapper.insert(user);
        if(i >0){
            return "插入成功";
        } else {
            return "插入失败";
        }
    }

    @PostMapping("/deleteById")
    public String deleteById(int id){
        int i = userMapper.deleteById(id);
        if(i >0){
            return "删除成功";
        } else {
            return "删除失败";
        }
    }


    // 返回所有用户
    @GetMapping("/user/findAll")
    public List<User> find(){ return userMapper.selectAllUserAndOrders(); }


    // 条件查询用户
    @GetMapping("/user/find")
    public  List<User> findByCond(){
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username","xiaomi");// 这个为条件，按照username=xiaomi来查表
        return userMapper.selectList(queryWrapper); // 把条件输入selectList中
    }

    // 分页查询，把数据库分成很多页，按页查询
    // 因为有时候数据库里面可能有几万条数据，不可能说一次性把所有数据都查询出来都给前端
    // 当前端点了下一页后继续查询下一页
    @GetMapping("/user/findByPage")
    public IPage findByPage(){
        // 设置起始页（current）及每页条数（size）
        Page<User> page = new Page<>(2,2);
        IPage iPage=userMapper.selectPage(page,null);// queryWapper还能包装条件查询
        return iPage;
    }








// 下面的是POST模板代码，可删
//    // POST1
//    @RequestMapping(value ="/postTest1",method = RequestMethod.POST)
//    public String postTest1(){
//        return "POST请求1";
//    }
//
//    // POST2
//    @RequestMapping(value ="/postTest2",method = RequestMethod.POST)
//    public String postTest2(String name,String age){
//        System.out.println("username:"+name);
//        System.out.println("password:"+age);
//
//        return "POST请求2";
//    }
//
//    // POST3 接受web传来的表单数据
//    @RequestMapping(value ="/postTest3",method = RequestMethod.POST)
//    public String postTest3(com.example.demo.entity.User user){
//        System.out.println(user);
//
//        return "POST请求3";
//    }
//
//    // POST4 接受web传来的json格式数据
//    @RequestMapping(value ="/postTest4",method = RequestMethod.POST)
//    public String postTest4(@RequestBody com.example.demo.entity.User user){    // 必须在此处加@RequestBody！！！！！！
//        System.out.println(user);
//
//        return "POST请求4";
//    }

}
