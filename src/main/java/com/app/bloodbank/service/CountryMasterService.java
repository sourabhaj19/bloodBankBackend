package com.app.bloodbank.service;

import com.app.bloodbank.criteria.CountryMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.repository.CountryMasterRepository;
import com.app.bloodbank.repository.UserRepository;
import com.app.bloodbank.specifications.CountryMasterQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CountryMasterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryMasterRepository countryMasterRepository;
    @Autowired
    private CountryMasterQueryService countryMasterQueryService;
    public PagedResponse<CountryMaster> getAllCountries(CountryMasterCriteria criteria, Pageable pageable) {
        Page<CountryMaster> page = countryMasterQueryService.findByCriteria(criteria, pageable);
        return new PagedResponse<>(page);
    }

    public String deletCountry(Long id) {

        Optional<CountryMaster> countryOptional = countryMasterRepository.findById(id);

        if (!countryOptional.isPresent()) {
            return "Country not found!";
        }
        CountryMaster country = countryOptional.get();

        boolean isCountryUsed = userRepository.existsByCountry(country.getName());

        if (isCountryUsed) {
            return "Cannot delete country as it is being used by users!";
        }
        countryMasterRepository.deleteById(id);

        return "Country deleted successfully!";
    }
}
