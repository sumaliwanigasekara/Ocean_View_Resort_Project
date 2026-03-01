package com.oceanview.mapper;

import com.oceanview.dto.BillItemDTO;
import com.oceanview.model.BillItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BillItemMapper {
    public BillItemDTO toDTO(BillItem item) {
        if (item == null) {
            return null;
        }
        BillItemDTO dto = new BillItemDTO();
        dto.setId(item.getId());
        dto.setBillId(item.getBillId());
        dto.setDescription(item.getDescription());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setCategory(item.getCategory());
        dto.setCreated_at(item.getCreated_at());
        return dto;
    }

    public BillItem toEntity(BillItemDTO dto) {
        if (dto == null) {
            return null;
        }
        BillItem item = new BillItem();
        item.setId(dto.getId());
        item.setBillId(dto.getBillId());
        item.setDescription(dto.getDescription());
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());
        item.setTotalPrice(dto.getTotalPrice());
        item.setCategory(dto.getCategory());
        item.setCreated_at(dto.getCreated_at());
        return item;
    }

    public List<BillItemDTO> toDTOList(List<BillItem> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream().filter(Objects::nonNull).map(this::toDTO).collect(Collectors.toList());
    }

    public List<BillItem> toEntityList(List<BillItemDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }
}
