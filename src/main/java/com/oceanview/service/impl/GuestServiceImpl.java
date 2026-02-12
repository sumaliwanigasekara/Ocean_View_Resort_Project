package com.oceanview.service.impl;

import com.oceanview.dao.GuestDAO;
import com.oceanview.model.Guest;
import com.oceanview.service.GuestService;
import com.oceanview.util.ValidationUtil;

import java.util.List;
import java.util.Objects;

public class GuestServiceImpl implements GuestService {
    private final GuestDAO guestDAO;

    public GuestServiceImpl(GuestDAO guestDAO) {
        this.guestDAO = Objects.requireNonNull(guestDAO, "guestDAO");
    }


    @Override
    public Guest createGuest(Guest guest) {
        ValidationUtil.requireNonNull(guest, "Guest");
        ValidationUtil.requireNonBlank(guest.getGuestName(), "Guest name");
        ValidationUtil.requireEmail(guest.getGuestEmail());
        ValidationUtil.requirePhone(guest.getGuestContact());
        ValidationUtil.requireNonBlank(guest.getGuestNIC(), "Guest NIC");

        return guestDAO.save(guest);
    }


    @Override
    public Guest getGuest(Long guestId) {
        ValidationUtil.requirePositive(guestId, "Guest id");
        Guest guest = guestDAO.findById(guestId);
        if (guest == null) {
            throw new IllegalArgumentException("Guest not found.");
        }
        return guest;
    }

    @Override
    public List<Guest> searchGuests(String term) {
        ValidationUtil.requireNonBlank(term, "Search term");
        return guestDAO.search(term);
    }
}
