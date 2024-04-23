package com.bdtc.techradar.controller;

import com.bdtc.techradar.dto.item.ItemDetailDto;
import com.bdtc.techradar.dto.item.ItemPreviewDto;
import com.bdtc.techradar.dto.item.ItemRequestDto;
import com.bdtc.techradar.dto.item.ItemUpdateDto;
import com.bdtc.techradar.service.item.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<ItemDetailDto> createItem(
            @AuthenticationPrincipal Jwt tokenJWT,
            @RequestBody @Valid ItemRequestDto itemRequestDto,
            UriComponentsBuilder uriBuilder
    ) {
        ItemDetailDto itemDetailDto = itemService.createItem(tokenJWT, itemRequestDto);
        var uri = uriBuilder.path("tech-radar/items/{id}").build(itemDetailDto.id());
        return ResponseEntity.created(uri).body(itemDetailDto);
    }

    @GetMapping()
    public ResponseEntity<List<ItemPreviewDto>> getItems() {
        List<ItemPreviewDto> itemPreviewDtos = itemService.getItemsPreview();
        return ResponseEntity.ok(itemPreviewDtos);
    }

    @GetMapping("all")
    public ResponseEntity<List<ItemPreviewDto>> getAllItems() {
        List<ItemPreviewDto> itemPreviewDtos = itemService.getAllItemsPreview();
        return ResponseEntity.ok(itemPreviewDtos);
    }

    @GetMapping("/{itemId}/detail")
    public ResponseEntity<ItemDetailDto> getItemDetail(
            @AuthenticationPrincipal Jwt tokenJWT,
            @PathVariable UUID itemId
    ) {
        return ResponseEntity.ok(itemService.getItemDetail(itemId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDetailDto> updateItem(
            @AuthenticationPrincipal Jwt tokenJWT,
            @PathVariable UUID itemId,
            @RequestBody ItemUpdateDto itemUpdateDto
    ) {
        return ResponseEntity.ok(itemService.updateItem(tokenJWT, itemId, itemUpdateDto));
    }

    @PatchMapping("/{itemId}/publish")
    public ResponseEntity<ItemDetailDto> publishItem(
            @AuthenticationPrincipal Jwt tokenJWT,
            @PathVariable UUID itemId
    ) {
        return ResponseEntity.ok(itemService.publishItem(tokenJWT, itemId));
    }

    @PatchMapping("/{itemId}/archive")
    public ResponseEntity<ItemDetailDto> archiveItem(
            @AuthenticationPrincipal Jwt tokenJWT,
            @PathVariable UUID itemId
    ) {
        return ResponseEntity.ok(itemService.archiveItem(tokenJWT, itemId));
    }
}
