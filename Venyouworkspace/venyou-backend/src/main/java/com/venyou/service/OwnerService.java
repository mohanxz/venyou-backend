package com.venyou.service;

import com.venyou.model.Owner;
import java.util.List;

public interface OwnerService {
    Owner addOwner(Owner owner);
    Owner getOwnerById(Long ownerId);
    List<Owner> getAllOwners();
    void deleteOwner(Long ownerId);
}
