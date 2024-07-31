package com.retail.store.discount.mapper;

import com.retail.store.discount.dto.BillResponse;
import com.retail.store.discount.dto.ItemDto;
import com.retail.store.discount.model.Bill;
import com.retail.store.discount.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillMapper {

    BillResponse map(Bill bill);

    @Mapping(target = "itemId", source = "id")
    ItemDto map(Item item);
}