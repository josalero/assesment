package com.josalero.favoritecountry.feign;

import com.fasterxml.jackson.databind.JsonNode;
import com.josalero.favoritecountry.config.FeignClientConfig;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "rest-countries",
    url = "https://restcountries.com/v3.1",
    configuration = FeignClientConfig.class
)
public interface RestCountryFeignClient {

  @GetMapping("/all?fields=name,region,capital,population")
  JsonNode getAllCountries();

  @GetMapping("/name/{country}?fields=name,region,capital,population")
  JsonNode getCountryByName(@PathVariable("country") String country);

}
