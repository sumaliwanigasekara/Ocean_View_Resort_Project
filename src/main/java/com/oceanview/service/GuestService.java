package com.oceanview.service;

import com.oceanview.model.Guest;

import java.util.List;

public interface GuestService {
    Guest createGuest(Guest guest);

    Guest getGuest(Long guestId);

    List<Guest> searchGuests(String term);
}
