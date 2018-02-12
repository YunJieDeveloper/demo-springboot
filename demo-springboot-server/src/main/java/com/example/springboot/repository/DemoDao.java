package com.example.springboot.repository;


import com.demo.springboot.entity.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class DemoDao {
    @Resource(name="demo2JdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public  int insertBean(Bean bean){
       String sql ="INSERT into " + getTable()+"(id ,b1,b2,createtime)"+" VALUES"+"(?,?,?,?)";
        return jdbcTemplate.update(sql, bean.getId(), bean.getB1(), bean.getCreatTime());
    }

    public  String getB1ById(Long id){
        String sql ="SELECT b1 FROM " + getTable()+" where id =?";
        return jdbcTemplate.queryForObject(sql,String.class, id);
    }

    private String getTable() {
        return "t_bean";
    }
}


