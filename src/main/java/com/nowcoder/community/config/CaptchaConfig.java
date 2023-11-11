package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.apache.catalina.authenticator.jaspic.PersistentProviderRegistrations;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ObjectInputFilter;
import java.util.Properties;

@Configuration
public class CaptchaConfig {
    @Bean
    public Producer KaptchaProducer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","100");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.image.color","0,0,0");

        properties.setProperty("kaptcha.textproducer.char.string","1234567890asdfghjklqw");
        properties.setProperty("kaptcha.textproducer.font.length","4");
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);

        return kaptcha;
    }
}
