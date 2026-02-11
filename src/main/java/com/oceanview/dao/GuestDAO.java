package com.oceanview.dao;

import com.oceanview.model.Guest;

import java.util.List;

public interface GuestDAO {
    Guest findById(Long guestId);

    Guest save(Guest guest);

    List<Guest> search(String term);
}
