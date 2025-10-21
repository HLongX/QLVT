package com.backend.service;

import com.backend.dto.ReplenishmentRequestDTO;
import com.backend.entity.ReplenishmentRequest;
import com.backend.entity.ReplenishmentRequestItem;
import com.backend.repository.ReplenishmentRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReplenishmentRequestService {

    private final ReplenishmentRequestRepository requestRepository;

    public ReplenishmentRequestService(ReplenishmentRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * 📝 Tạo một phiếu đề nghị mới từ DTO gửi lên từ FE
     */
    @Transactional
    public ReplenishmentRequest create(ReplenishmentRequestDTO dto) {
        ReplenishmentRequest request = new ReplenishmentRequest();
        request.setNote(dto.getNote());
        request.setRequesterId(1); // Tạm thời gán cứng, sau này có thể lấy từ user đăng nhập

        // Tạo mã phiếu dạng PDN-YYYY-000X
        String code = String.format("PDN-%d-%04d",
                LocalDate.now().getYear(),
                requestRepository.count() + 1);
        request.setCode(code);

        // Thêm các mặt hàng chi tiết vào phiếu
        dto.getItems().forEach(i -> {
            ReplenishmentRequestItem item = new ReplenishmentRequestItem();
            item.setProductId(i.getProductId());
            item.setProductName(i.getProductName());
            item.setQuantity(i.getQuantity());
            item.setUnit(i.getUnit());
            item.setSource(i.getSource());
            request.addItem(item);
        });

        // Lưu toàn bộ phiếu (cascade sẽ tự lưu items)
        return requestRepository.save(request);
    }

    /**
     * 📋 Lấy danh sách tất cả phiếu đề nghị
     */
    public List<ReplenishmentRequest> findAll() {
        return requestRepository.findAll();
    }

    /**
     * ✅ Duyệt một phiếu theo ID
     */
    @Transactional
    public ReplenishmentRequest approve(Long id) {
        ReplenishmentRequest req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu ID=" + id));
        req.setStatus(ReplenishmentRequest.Status.APPROVED);
        return requestRepository.save(req);
    }

    /**
     * ❌ Từ chối một phiếu theo ID
     */
    @Transactional
    public ReplenishmentRequest reject(Long id) {
        ReplenishmentRequest req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu ID=" + id));
        req.setStatus(ReplenishmentRequest.Status.REJECTED);
        return requestRepository.save(req);
    }
}
