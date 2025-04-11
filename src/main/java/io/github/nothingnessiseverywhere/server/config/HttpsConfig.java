package io.github.nothingnessiseverywhere.server.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig {

    // 创建一个ServletWebServerFactory Bean，用于配置Tomcat服务器
    @Bean
    public ServletWebServerFactory servletContainer() {
        // 创建一个TomcatServletWebServerFactory对象
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            // 重写postProcessContext方法，用于配置Tomcat的安全约束
            @Override
            protected void postProcessContext(Context context) {
                // 创建一个SecurityConstraint对象，用于配置安全约束
                SecurityConstraint securityConstraint = new SecurityConstraint();
                // 设置用户约束为CONFIDENTIAL，表示需要HTTPS连接
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                // 创建一个SecurityCollection对象，用于配置URL模式
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*"); // 所有URL都应用安全约束
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    // 创建一个Connector对象，用于配置HTTP重定向到HTTPS
    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080); // HTTP端口
        connector.setSecure(false);
        connector.setRedirectPort(8443); // 重定向到HTTPS端口
        return connector;
    }
}
