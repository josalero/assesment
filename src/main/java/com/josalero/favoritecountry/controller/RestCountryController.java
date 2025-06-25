package com.josalero.favoritecountry.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.josalero.favoritecountry.feign.RestCountryFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest-countries")
public class RestCountryController {

  private final RestCountryFeignClient restCountryFeignClient;

  public RestCountryController(RestCountryFeignClient restCountryFeignClient) {
    this.restCountryFeignClient = restCountryFeignClient;
  }

  @PostMapping("/all")
  public JsonNode getAllCountries() {
    return restCountryFeignClient.getAllCountries();
  }

  @GetMapping("/{country}")
  public JsonNode getCountryByName(@PathVariable("country") String country) {
    return restCountryFeignClient.getCountryByName(country);
  }

}
