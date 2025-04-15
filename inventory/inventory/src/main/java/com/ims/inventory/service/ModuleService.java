package com.ims.inventory.service;

import com.ims.inventory.domen.request.CreateRequest;
import com.ims.inventory.domen.request.DropDownRequest;
import com.ims.inventory.domen.request.FilterRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.response.FilterResponse;
import com.ims.inventory.domen.response.MenuResponse;
import com.ims.inventory.domen.response.Responce;

import java.util.List;

public interface ModuleService {

    FilterResponse filterlist(FilterRequest filterRequest) throws Exception;

    Responce createModule(CreateRequest createRequest) throws Exception;

    Responce loadModule(LoadRequest loadRequest) throws Exception;

    String getTableDesign(CreateRequest createRequest) throws Exception;

    String getFormDesign(CreateRequest createRequest) throws Exception;

    Boolean updateModuleDesign(CreateRequest createRequest) throws Exception;

    List<MenuResponse> getModuleMenu() throws Exception;

    FilterResponse getDropDownOptions(DropDownRequest req) throws Exception;

}
