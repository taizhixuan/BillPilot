package com.billpilot.domain.invoice.mapper;

import com.billpilot.domain.invoice.dto.InvoiceResponse;
import com.billpilot.domain.invoice.entity.Invoice;
import com.billpilot.domain.invoice.entity.InvoiceLineItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @Mapping(source = "customer.name", target = "customerName")
    InvoiceResponse toResponse(Invoice invoice);

    List<InvoiceResponse> toResponseList(List<Invoice> invoices);

    InvoiceResponse.LineItemResponse toLineItemResponse(InvoiceLineItem item);
}
