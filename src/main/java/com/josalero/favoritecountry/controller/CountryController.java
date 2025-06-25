package com.josalero.favoritecountry.controller;


import com.josalero.favoritecountry.model.Country;
import com.josalero.favoritecountry.service.CountryService;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

  private final CountryService countryService;

  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @PostMapping("/favorites")
  public ResponseEntity<Country> saveCountryAsFavorite(
      @RequestBody String name) {

    return ResponseEntity.ok(countryService.saveCountryAsFavorite(name));
  }

  @GetMapping("/favorites")
  public ResponseEntity<List<Country>> listAllFavorites(
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      @RequestParam(value = "region", required = false, defaultValue = "") String region) {

    return ResponseEntity.ok(countryService.listAllFavorites(sort, region));

  }

  @GetMapping("/favorites/{id}")
  public ResponseEntity<Country> getCountryById(
      @PathVariable("id") Long id) {

    return ResponseEntity.ok(countryService.getCountryById(id));

  }

  @PutMapping("/favorites/{id}")
  public ResponseEntity<Country> getCountryById(
      @PathVariable("id") Long id,
      @RequestBody String notes) {

    return ResponseEntity.ok(countryService.updateCountryNotes(id, notes));
  }

  @DeleteMapping("/favorites/{id}")
  public ResponseEntity<Void> deleteCountryById(
      @PathVariable("id") Long id,
      @RequestBody String notes) {
    countryService.deleteCountryById(id);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/all")
  public ResponseEntity<Set<Country>> syncCountries() {

    return ResponseEntity.ok(countryService.syncCountries());
  }

  @DeleteMapping("/regions/{region}")
  public ResponseEntity<Void> deleteCountryById(
      @PathVariable("region") String region) {
    countryService.deleteCountryByRegion(region);

    return ResponseEntity.noContent().build();
  }

}
