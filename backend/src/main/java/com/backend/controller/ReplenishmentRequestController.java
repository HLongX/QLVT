package com.backend.controller;

import com.backend.dto.ReplenishmentRequestDTO;
import com.backend.entity.ReplenishmentRequest;
import com.backend.service.ReplenishmentRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replenishment-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class ReplenishmentRequestController {

    private final ReplenishmentRequestService service;

    public ReplenishmentRequestController(ReplenishmentRequestService service) {
        this.service = service;
    }

    /**
     * 📝 Tạo phiếu đề nghị mới (gọi từ FE)
     */
    @PostMapping
    public ReplenishmentRequest create(@Valid @RequestBody ReplenishmentRequestDTO dto) {
        return service.create(dto);
    }

    /**
     * 📋 Lấy toàn bộ danh sách phiếu (cho admin hoặc người dùng xem lịch sử)
     */
    @GetMapping
    public List<ReplenishmentRequest> getAll() {
        return service.findAll();
    }

    /**
     * ✅ Duyệt phiếu đề nghị
     */
    @PutMapping("/{id}/approve")
    public ReplenishmentRequest approve(@PathVariable Long id) {
        return service.approve(id);
    }

    /**
     * ❌ Từ chối phiếu đề nghị
     */
    @PutMapping("/{id}/reject")
    public ReplenishmentRequest reject(@PathVariable Long id) {
        return service.reject(id);
    }
}
