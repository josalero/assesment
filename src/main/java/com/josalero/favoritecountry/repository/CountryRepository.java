package com.josalero.favoritecountry.repository;

import com.josalero.favoritecountry.model.Country;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface CountryRepository extends JpaRepository<Country, Long> {

  Optional<Country> findByIdAndFavoriteTrue(Long id);

  Optional<Country> findByNameEqualsIgnoreCase(String name);

  List<Country> findAllByFavoriteTrueAndRegionEqualsIgnoreCase(String name, Sort sort);

  List<Country> findAllByFavoriteTrue(Sort sort);

  @Modifying
  int deleteAllByRegionEqualsIgnoreCase(String region);

}
