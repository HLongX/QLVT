package com.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReplenishmentRequestDTO {

    @NotEmpty
    private String note;

    @NotEmpty
    private List<ItemDTO> items;

    @Getter @Setter
    public static class ItemDTO {
        private Long productId;
        private String productName;
        private Integer quantity;
        private String unit;
        private String source;
    }
}
