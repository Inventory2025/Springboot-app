package com.ims.inventory.domen.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

    @JsonProperty("link_code")
    private String linkCode;

    @JsonProperty("link_name")
    private String linkName;

    private String link;
    private String icon;

    @JsonProperty("sub_menu")
    private List<MenuResponse> subMenu;

}
