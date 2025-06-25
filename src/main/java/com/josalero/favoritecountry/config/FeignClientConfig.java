package com.josalero.favoritecountry.config;

import com.josalero.favoritecountry.feign.RestCountryFeignClient;
import feign.Logger.Level;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {RestCountryFeignClient.class})
public class FeignClientConfig {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new Default();
  }

  @Bean
  public Retryer retryer() {
    return new Retryer.Default(1000, 2000, 3);
  }

  @Bean
  public Level level() {
    return Level.FULL;
  }

}
