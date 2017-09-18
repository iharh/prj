package sprestapi.configs;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.actuate.autoconfigure.ManagementServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Artyom.Gabeev on 7/2/15.
 */
@SpringBootApplication
//@Import({})
//@ComponentScan("com.clarabridge.sprestapi.controllers")
public class RestAppConfig {
/*
    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        conversionService.setConverters(getConverters());
        return conversionService;
    }

    @Bean
    @ConditionalOnProperty("management.http")
    public BeanPostProcessor managementConfigurer() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (beanName.equals(ManagementServerPropertiesAutoConfiguration.class.getName())) {
                    final ManagementServerPropertiesAutoConfiguration config = (ManagementServerPropertiesAutoConfiguration)bean;
                    config.serverProperties().getSsl().setEnabled(false);
                }
                return bean;
            }
        };
    }
*/
}
