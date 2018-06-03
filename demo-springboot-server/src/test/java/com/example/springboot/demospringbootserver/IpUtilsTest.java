package com.example.springboot.demospringbootserver;

import com.example.springboot.utils.IpUtils;
import org.junit.Test;

public class IpUtilsTest {

    @Test
    public void testGetIp(){
        String myIpInfo = IpUtils.Instance.getMyIpInfo();
        System.out.println(myIpInfo);
    }

    @Test
    public void testStr(){
       StringBuffer id =new StringBuffer("123");
       change(id);
        System.out.println(id);
    }

   void change(StringBuffer s){
       s=s.append("1234");
       // return s;
    }
}
