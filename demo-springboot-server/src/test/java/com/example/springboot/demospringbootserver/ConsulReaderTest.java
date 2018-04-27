package com.example.springboot.demospringbootserver;

import com.demo.springboot.exception.DemoException;
import com.ecwid.consul.v1.ConsulClient;
import com.example.springboot.demo.config.ConsulConfigProperties;
import com.example.springboot.demo.config.ConsulReader;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class ConsulReaderTest extends BaseSpringTest {

    private ConsulReader consulReader;
    @Autowired
    private ConsulClient consulClient;
    @Autowired
    private ConsulConfigProperties properties;
    @Before
    public void init(){
        consulReader=new ConsulReader(consulClient,properties);
    }


    public void testread() throws DemoException{
        consulReader.readConfig("");
    }
}
