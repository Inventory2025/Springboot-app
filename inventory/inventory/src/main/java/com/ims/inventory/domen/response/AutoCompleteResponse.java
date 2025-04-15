package com.ims.inventory.domen.response;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class AutoCompleteResponse {
    String id;
    String name;
    String option;
}
