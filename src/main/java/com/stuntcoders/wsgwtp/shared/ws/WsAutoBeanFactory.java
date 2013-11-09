package com.stuntcoders.wsgwtp.shared.ws;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface WsAutoBeanFactory extends AutoBeanFactory {

    AutoBean<WsData> wsData();

    AutoBean<WsDataExec> wsDataExec();
}
