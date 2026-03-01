package com.oceanview.mapper;

import com.oceanview.dto.GuestDTO;
import com.oceanview.model.Guest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuestMapper {
    public String ping() {
        return "ok";
    }

    public GuestDTO toDTO(Guest guest) {
        if (guest == null) {
            return null;
        }
        GuestDTO dto = new GuestDTO();
        dto.setGuestId(guest.getGuestId());
        dto.setGuestName(guest.getGuestName());
        dto.setGuestPassport(guest.getGuestPassport());
        dto.setGuestEmail(guest.getGuestEmail());
        dto.setGuestContact(guest.getGuestContact());
        dto.setGuestAddress(guest.getGuestAddress());
        dto.setGuestNIC(guest.getGuestNIC());
        dto.setCreated_at(guest.getCreated_at());
        dto.setUpdated_at(guest.getUpdated_at());
        return dto;
    }

    public Guest toEntity(GuestDTO dto) {
        if (dto == null) {
            return null;
        }
        Guest guest = new Guest();
        guest.setGuestId(dto.getGuestId());
        guest.setGuestName(dto.getGuestName());
        guest.setGuestPassport(dto.getGuestPassport());
        guest.setGuestEmail(dto.getGuestEmail());
        guest.setGuestContact(dto.getGuestContact());
        guest.setGuestAddress(dto.getGuestAddress());
        guest.setGuestNIC(dto.getGuestNIC());
        guest.setCreated_at(dto.getCreated_at());
        guest.setUpdated_at(dto.getUpdated_at());
        return guest;
    }

    public List<GuestDTO> toDTOList(List<Guest> guests) {
        if (guests == null) {
            return Collections.emptyList();
        }
        return guests.stream().filter(Objects::nonNull).map(this::toDTO).collect(Collectors.toList());
    }

    public List<Guest> toEntityList(List<GuestDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }
}
