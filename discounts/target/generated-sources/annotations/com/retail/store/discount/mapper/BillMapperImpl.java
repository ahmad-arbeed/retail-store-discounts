package com.retail.store.discount.mapper;

import com.retail.store.discount.dto.BillResponse;
import com.retail.store.discount.dto.ItemDto;
import com.retail.store.discount.model.Bill;
import com.retail.store.discount.model.Item;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-31T21:14:59+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class BillMapperImpl implements BillMapper {

    @Override
    public BillResponse map(Bill bill) {
        if ( bill == null ) {
            return null;
        }

        BillResponse.BillResponseBuilder billResponse = BillResponse.builder();

        billResponse.id( bill.getId() );
        billResponse.userId( bill.getUserId() );
        billResponse.items( itemListToItemDtoList( bill.getItems() ) );
        billResponse.totalAmount( bill.getTotalAmount() );
        billResponse.discountAmount( bill.getDiscountAmount() );
        billResponse.netPayableAmount( bill.getNetPayableAmount() );

        return billResponse.build();
    }

    @Override
    public ItemDto map(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemDto.ItemDtoBuilder itemDto = ItemDto.builder();

        itemDto.itemId( item.getId() );
        itemDto.quantity( item.getQuantity() );
        itemDto.name( item.getName() );
        itemDto.price( item.getPrice() );
        itemDto.totalPrice( item.getTotalPrice() );
        itemDto.category( item.getCategory() );

        return itemDto.build();
    }

    protected List<ItemDto> itemListToItemDtoList(List<Item> list) {
        if ( list == null ) {
            return null;
        }

        List<ItemDto> list1 = new ArrayList<ItemDto>( list.size() );
        for ( Item item : list ) {
            list1.add( map( item ) );
        }

        return list1;
    }
}
