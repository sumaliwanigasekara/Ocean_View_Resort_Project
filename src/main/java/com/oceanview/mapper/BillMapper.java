package com.oceanview.mapper;

import com.oceanview.dto.BillDTO;
import com.oceanview.model.Bill;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BillMapper {
    private final ReservationMapper reservationMapper = new ReservationMapper();

    public BillDTO toDTO(Bill bill) {
        if (bill == null) {
            return null;
        }
        BillDTO dto = new BillDTO();
        dto.setBillId(bill.getBillId());
        dto.setReservationId(bill.getReservationId());
        dto.setRoomCharges(bill.getRoomCharges());
        dto.setServiceCharges(bill.getServiceCharges());
        dto.setTaxAmount(bill.getTaxAmount());
        dto.setDiscountAmount(bill.getDiscountAmount());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setStatus(bill.getStatus());
        dto.setPaymentMethod(bill.getPaymentMethod());
        dto.setPaidAt(bill.getPaidAt());
        dto.setCreated_at(bill.getCreated_at());
        dto.setUpdated_at(bill.getUpdated_at());
        dto.setReservation(reservationMapper.toDTO(bill.getReservation()));
        return dto;
    }

    public Bill toEntity(BillDTO dto) {
        if (dto == null) {
            return null;
        }
        Bill bill = new Bill();
        bill.setBillId(dto.getBillId());
        bill.setReservationId(dto.getReservationId());
        bill.setRoomCharges(dto.getRoomCharges());
        bill.setServiceCharges(dto.getServiceCharges());
        bill.setTaxAmount(dto.getTaxAmount());
        bill.setDiscountAmount(dto.getDiscountAmount());
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setStatus(dto.getStatus());
        bill.setPaymentMethod(dto.getPaymentMethod());
        bill.setPaidAt(dto.getPaidAt());
        bill.setCreated_at(dto.getCreated_at());
        bill.setUpdated_at(dto.getUpdated_at());
        bill.setReservation(reservationMapper.toEntity(dto.getReservation()));
        return bill;
    }

    public List<BillDTO> toDTOList(List<Bill> bills) {
        if (bills == null) {
            return Collections.emptyList();
        }
        return bills.stream().filter(Objects::nonNull).map(this::toDTO).collect(Collectors.toList());
    }

    public List<Bill> toEntityList(List<BillDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }
}
