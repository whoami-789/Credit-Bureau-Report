package org.creditbureaureport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class CreditBureauReportApplication {


    public static void main(String[] args) {
        String disabledAlgorithms = Security.getProperty("jdk.tls.disabledAlgorithms");
        disabledAlgorithms = disabledAlgorithms.replace("TLSv1, TLSv1.1,", "");
        Security.setProperty("jdk.tls.disabledAlgorithms", disabledAlgorithms);
        SpringApplication.run(CreditBureauReportApplication.class, args);
    }

}
