package com.josalero.favoritecountry.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.josalero.favoritecountry.exception.NotFoundException;
import com.josalero.favoritecountry.feign.RestCountryFeignClient;
import com.josalero.favoritecountry.model.Country;
import com.josalero.favoritecountry.repository.CountryRepository;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CountryService {

  private final CountryRepository countryRepository;

  private final RestCountryFeignClient restCountryFeignClient;

  public CountryService(CountryRepository countryRepository,
      RestCountryFeignClient restCountryFeignClient) {
    this.countryRepository = countryRepository;
    this.restCountryFeignClient = restCountryFeignClient;
  }

  public Country saveCountryAsFavorite(String name) {
    JsonNode response = restCountryFeignClient.getCountryByName(name);

    if (response != null && response.isArray()) {
      Optional<Country> countryOptional = countryRepository.findByNameEqualsIgnoreCase(name);
      Country country = new Country();
      if (countryOptional.isEmpty()) {
        country =  buildCountry(country, response);
      } else {
        country = buildCountry(countryOptional.get(), response);
      }

      return countryRepository.save(country);
    }

    throw new NotFoundException("Country not found");
  }

  @Transactional(readOnly = true)
  public List<Country> listAllFavorites(String sort, String region) {

    if (!region.isBlank()) {
      return countryRepository.findAllByFavoriteTrueAndRegionEqualsIgnoreCase(region, Sort.by(sort));
    }

    return countryRepository.findAllByFavoriteTrue(Sort.by(sort));
  }

  @Transactional(readOnly = true)
  public Country getCountryById(Long id) {

    return countryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Country not found"));
  }

  public void deleteCountryById(Long id) {

    Country country =  countryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Country not found"));

   countryRepository.delete(country);
  }

  public Country updateCountryNotes(Long id, String notes) {

    Country country =  countryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Country not found"));

    country.setNotes(notes);

    return countryRepository.save(country);
  }

  public void deleteCountryByRegion(String region) {

    int removedCount = countryRepository.deleteAllByRegionEqualsIgnoreCase(region);

    if (removedCount == 0) {
      throw new NotFoundException("Region not found");
    }

  }

  public Set<Country> syncCountries() {

    Map<String, Country> countryMap = countryRepository.findAll()
        .stream()
        .collect(Collectors.toMap(Country::getName, Function.identity()));

    if (countryMap == null || countryMap.isEmpty()) {
      return Set.of();
    }

    Set<Country> countrySet = new HashSet<>();

    JsonNode response = restCountryFeignClient.getAllCountries();
    if (response != null && response.isArray()) {
      ArrayNode nodes = (ArrayNode) response;
      Iterator<JsonNode> iterator = nodes.iterator();
      while (iterator.hasNext()) {
        JsonNode countryNode = iterator.next();
        String name = countryNode.get("name").asText();

        if (countryMap.containsKey(name)) {
          Country country = countryMap.get(name);

          countrySet.add(countryRepository.save(buildCountry(country, countryNode)));
        }
      }
    }

    return countrySet;
  }


  private Country buildCountry(Country country, JsonNode response){
    JsonNode innerNode = response.get(0);
    country.setName(innerNode.get("name").get("common").asText());
    country.setFavorite(true);
    country.setRegion(innerNode.get("region").asText());
    country.setPopulation(innerNode.get("population").asLong());

    return country;
  }

}

