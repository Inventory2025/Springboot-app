package com.ims.inventory.controller;

import com.ims.inventory.domen.request.CreateRequest;
import com.ims.inventory.domen.request.DropDownRequest;
import com.ims.inventory.domen.request.FilterRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.response.FilterResponse;
import com.ims.inventory.domen.response.MenuResponse;
import com.ims.inventory.domen.response.Responce;
import com.ims.inventory.service.ModuleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/module/")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping("filter/list")
    public FilterResponse filterList(@RequestBody FilterRequest req) throws Exception{
        return moduleService.filterlist(req);
    }

    @PostMapping("table/design")
    public ResponseEntity<String> getTableDesign(@Valid @RequestBody CreateRequest createRequest) throws Exception{
        return ResponseEntity.ok().body(moduleService.getTableDesign(createRequest));
    }

    @PostMapping("form/design")
    public ResponseEntity<String> getFormDesign(@Valid @RequestBody CreateRequest createRequest) throws Exception{
        return ResponseEntity.ok().body(moduleService.getFormDesign(createRequest));
    }

    @GetMapping("menu")
    public ResponseEntity<List<MenuResponse>> getModuleMenu(HttpServletRequest request) throws Exception{
        return ResponseEntity.ok().body(moduleService.getModuleMenu(request));
    }


    @PostMapping("create")
    public Responce createModule(@Valid @RequestBody CreateRequest createRequest) throws Exception{
        return moduleService.createModule(createRequest);
    }

    @PostMapping("update")
    public Responce updateModule(@Valid @RequestBody CreateRequest createRequest) throws Exception{
        return moduleService.createModule(createRequest);
    }

    @PostMapping("load")
    public ResponseEntity<Responce> loadModule(@Valid @RequestBody LoadRequest loadRequest) throws Exception{
        return ResponseEntity.ok().body(moduleService.loadModule(loadRequest));
    }

    @PostMapping("update/design")
    public ResponseEntity<Boolean> updateModuleDesign(@Valid @RequestBody CreateRequest createRequest)
            throws Exception {
        return ResponseEntity.ok().body(moduleService.updateModuleDesign(createRequest));
    }

    @PostMapping("dropdown/options")
    public ResponseEntity<FilterResponse> getDropDownOptions(@Valid @RequestBody DropDownRequest req)
            throws Exception {
        return ResponseEntity.ok().body(moduleService.getDropDownOptions(req));
    }

    @PostMapping("soft/delete")
    public ResponseEntity<Responce> getSoftDelete(@Valid @RequestBody LoadRequest loadRequest)
            throws Exception {
        return ResponseEntity.ok().body(moduleService.softDelete(loadRequest));
    }

    @PostMapping(value = "export/csv", produces = "text/csv")
    public void exportCsv(@RequestBody FilterRequest filterRequest,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        moduleService.exportCsv(filterRequest, request, response);
    }

}
