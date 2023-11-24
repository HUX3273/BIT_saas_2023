package com.example.demo.controller;
import com.example.demo.entity.User;
import com.example.demo.entity.Workflow;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.WorkflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin    // 允许该控制器内的方法跨域
public class WorkflowController {

    @Autowired
    private WorkflowMapper workflowMapper;


    // 返回所有流程数据
    @GetMapping("/workflow")
    public List queryWorkflowData(){    // 返回List，是json格式的
        List<Workflow> list = workflowMapper.selectList(null);
        System.out.println(list);
        return list;
    }


}
