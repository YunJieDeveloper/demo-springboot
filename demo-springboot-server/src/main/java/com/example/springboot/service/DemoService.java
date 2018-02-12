package com.example.springboot.service;


import com.example.springboot.repository.DemoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DemoService {

    @Autowired
    DemoDao demoDao;


    //增删改需要加事务
  /*  @Transactional(rollbackFor = DemoException.class)
    public int insert(Bean bean) throws DemoException{
        try {
            return demoDao.insertBean(bean);
        } catch (Exception e) {
            e.printStackTrace();
            //{}为替换符
            log.error("bean:[{}],insert to db failed",bean);
            throw new DemoException("insert failed",e);
        }
    }*/

  public String getB1(Long id){
      return demoDao.getB1ById(id);
  }

    public String getB2(Long id){
        return id+"==";
    }
}
