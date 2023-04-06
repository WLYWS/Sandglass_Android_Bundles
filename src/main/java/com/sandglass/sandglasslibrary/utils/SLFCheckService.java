package com.sandglass.sandglasslibrary.utils;

import com.sandglass.sandglasslibrary.enums.ServiceType;

public class SLFCheckService {

    public static String BASE_URL;


    public static void checkService(ServiceType punServiceType){
        if(punServiceType == ServiceType.TEST){
            //测服
            BASE_URL = "https://rvi-test-app-gateway.hualaikeji.com";
        }else if(punServiceType == ServiceType.BETA){
            //beta
            BASE_URL = "https://rvi-app-gateway.kivolabs.com";
        }else if(punServiceType == ServiceType.OFFEICIAL){
            //official
            BASE_URL = "https://rvi-test-app-gateway.hualaikeji.com";
        }
    }
}

