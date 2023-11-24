package com.example.demo.entity;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.util.List;
// 如果表名和类名不一致，需要加入下行注释将该类绑定对应的表
@TableName(value = "t_workflow",autoResultMap = true)
public class Workflow {
    private int flowid;
    private String flowname;
    private String requester;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray nodelist;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray  linelist;






    public JSONArray getNodelist() {
        return nodelist;
    }

    public void setNodelist(JSONArray nodelist) {
        this.nodelist = nodelist;
    }

    public JSONArray getLinelist() {
        return linelist;
    }

    public void setLinelist(JSONArray linelist) {
        this.linelist = linelist;
    }

    public int getFlowid() {
        return flowid;
    }

    public void setFlowid(int flowid) {
        this.flowid = flowid;
    }

    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }


    @Override
    public String toString() {
        return "Workflow{" +
                "flowid=" + flowid +
                ", flowname='" + flowname + '\'' +
                ", requester='" + requester + '\'' +
                ", nodelist=" + nodelist +
                ", linelist=" + linelist +
                '}';
    }
}
