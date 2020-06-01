package com.springdistributed.configuration;

import com.springdistributed.zkservice.ZKService;
import com.springdistributed.zkservice.ZKServiceImpl;
import com.springdistributed.zkwatchers.ConnectStateChangeListener;
import com.springdistributed.zkwatchers.MasterChangeListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanConfiguration {

    @Bean(name="zkService")
    @Scope("singleton")
    public ZKService zkService() {
        return new ZKServiceImpl(System.getProperty("zk.url"));
    }

    @Bean(name="masterChangeListener")
    @Scope("singleton")
    public MasterChangeListener masterChangeListener() {
        MasterChangeListener masterChangeListener = new MasterChangeListener();
        masterChangeListener.setZkService(zkService());
        return masterChangeListener;
    }

    @Bean(name="connectStateChangeListener")
    @Scope("singleton")
    public ConnectStateChangeListener connectStateChangeListener() {
        ConnectStateChangeListener connectStateChangeListener = new ConnectStateChangeListener();
        connectStateChangeListener.setZkService(zkService());
        return connectStateChangeListener;
    }

}
