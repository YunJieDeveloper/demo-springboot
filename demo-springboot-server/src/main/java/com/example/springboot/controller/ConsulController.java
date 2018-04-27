package com.example.springboot.controller;

import java.util.List;

import com.example.springboot.demo.consul.ConsulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.orbitz.consul.model.health.ServiceHealth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/***
 * @Date 2018/4/22
 * @Description    consul 案例
 * @author zhanghesheng
 * */

@Api("consul相关API")
@RestController
@RequestMapping("/consul")
public class ConsulController {
    @Autowired
    private ConsulService consulService;

    /*******************************服务注册与发现*******************************/
    @ApiOperation("注册服务")
    @RequestMapping(value = "/registerService/{servicename}/{serviceid}", method = RequestMethod.POST)
    public void registerService(@PathVariable("servicename") String serviceName,
                                @PathVariable("serviceid") String serviceId) {
        consulService.registerService(serviceName, serviceId);
    }

    @ApiOperation("发现服务")
    @RequestMapping(value = "/discoverService/{servicename}", method = RequestMethod.GET)
    public List<ServiceHealth> discoverService(@PathVariable("servicename") String serviceName) {
        return consulService.findHealthyService(serviceName);
    }

    /*******************************KV*******************************/
    @ApiOperation("store KV")
    @RequestMapping(value = "/kv/{key}/{value}", method = RequestMethod.POST)
    public void storeKV(@PathVariable("key") String key,
                        @PathVariable("value") String value) {
        consulService.storeKV(key, value);
    }

    @ApiOperation("get KV")
    @RequestMapping(value = "/kv/{key}", method = RequestMethod.GET)
    public String getKV(@PathVariable("key") String key) {
        return consulService.getKV(key);
    }

    /*******************************server*******************************/
    @ApiOperation("获取同一个DC中的所有server节点")
    @RequestMapping(value = "/raftpeers", method = RequestMethod.GET)
    public List<String> findRaftPeers() {
        return consulService.findRaftPeers();
    }

    @ApiOperation("获取leader")
    @RequestMapping(value = "/leader", method = RequestMethod.GET)
    public String leader() {
        return consulService.findRaftLeader();
    }
}