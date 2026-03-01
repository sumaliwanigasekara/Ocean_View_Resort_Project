package com.oceanview.mapper;

import com.oceanview.dto.ReservationDTO;
import com.oceanview.model.Reservation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReservationMapper {
    private final GuestMapper guestMapper = new GuestMapper();
    private final RoomMapper roomMapper = new RoomMapper();

    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setGuestId(reservation.getGuestId());
        dto.setRoomId(reservation.getRoomId());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setStatus(reservation.getStatus());
        dto.setTotalAmount(reservation.getTotalAmount());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        dto.setGuest(guestMapper.toDTO(reservation.getGuest()));
        dto.setRoom(roomMapper.toDTO(reservation.getRoom()));
        return dto;
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setReservationId(dto.getReservationId());
        reservation.setGuestId(dto.getGuestId());
        reservation.setRoomId(dto.getRoomId());
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setStatus(dto.getStatus());
        reservation.setTotalAmount(dto.getTotalAmount());
        reservation.setCreatedAt(dto.getCreatedAt());
        reservation.setUpdatedAt(dto.getUpdatedAt());
        reservation.setGuest(guestMapper.toEntity(dto.getGuest()));
        reservation.setRoom(roomMapper.toEntity(dto.getRoom()));
        return reservation;
    }

    public List<ReservationDTO> toDTOList(List<Reservation> reservations) {
        if (reservations == null) {
            return Collections.emptyList();
        }
        return reservations.stream().filter(Objects::nonNull).map(this::toDTO).collect(Collectors.toList());
    }

    public List<Reservation> toEntityList(List<ReservationDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }
}
