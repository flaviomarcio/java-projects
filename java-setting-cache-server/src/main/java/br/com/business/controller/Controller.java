package br.com.business.controller;

import br.com.business.dto.Setting;
import br.com.business.dto.SettingResponse;
import br.com.business.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/settings")
@Tag(name = "Streaming Controller")
public class Controller {

    private final SettingService settingService;

    @Operation(description = "Writer ou replace settings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."
                    , content = {@Content(schema = @Schema(implementation = Map.class))})
    })
    @PostMapping("/write")
    ResponseEntity<SettingResponse> write(@RequestBody Setting settings) {
        var out = settingService.write(settings);
        if (out == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(out);
    }

    @Operation(description = "Verify existing setting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."
                    , content = {@Content(schema = @Schema(implementation = Setting.class))})
    })
    @GetMapping("/exists")
    ResponseEntity<Boolean> exists(String name) {
        if (!settingService.exists(name))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Returns settings object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."
                    , content = {@Content(schema = @Schema(implementation = Setting.class))})
    })
    @GetMapping("/read")
    ResponseEntity<Setting> read(String name) {
        var out = settingService.read(name);
        if (out == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(out);
    }

    @Operation(description = "Returns only settings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."
                    , content = {@Content(schema = @Schema(implementation = List.class))})
    })
    @GetMapping("/setting")
    ResponseEntity<Object> setting(String name) {
        var out = settingService.setting(name);
        if (out == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(out);
    }

}
