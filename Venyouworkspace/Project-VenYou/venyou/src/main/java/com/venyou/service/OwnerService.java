package com.venyou.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.venyou.model.Owner;

@Service
public class OwnerService {

    private static final String OWNER_URL = "http://localhost:8086/api/owners";

    @Autowired
    private RestTemplate restTemplate;

    public List<Owner> getAllOwners() {
        try {
            Owner[] owners = restTemplate.getForObject(OWNER_URL, Owner[].class);
            return owners != null ? Arrays.asList(owners) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching owners: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Owner getOwnerById(Long id) {
        try {
            return restTemplate.getForObject(OWNER_URL + "/" + id, Owner.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error fetching owner by ID: " + e.getMessage());
            return null;
        }
    }

    public Owner createOwner(Owner owner) {
        try {
            return restTemplate.postForObject(OWNER_URL, owner, Owner.class);
        } catch (RestClientException e) {
            System.err.println("❌ Error creating owner: " + e.getMessage());
            return null;
        }
    }
}
