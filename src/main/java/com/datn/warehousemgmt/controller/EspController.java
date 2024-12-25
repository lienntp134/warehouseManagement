package com.datn.warehousemgmt.controller;

import com.datn.warehousemgmt.dto.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/esp")
@Slf4j
@Tag(name = "API giao tiếp với phần cứng")
public class EspController {

    @Value("${esp32.base.url}")
    private String espBaseUrl;

    @Operation(summary = "Đọc thông tin thẻ RFID")
    @PostMapping("/rfid")
    public ResponseEntity<String> processRFID(@RequestBody Map<String, String> payload) {
        String cardID = payload.get("cardID");
        System.out.println("Received RFID Card ID: " + cardID);

        // Xử lý thông tin thẻ
        boolean isAuthorized = checkCardID(cardID);

        if (isAuthorized) {
            log.info("Card id: {}", cardID);
            return ResponseEntity.ok("Access granted");
        } else {
            log.info("Card id failed");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @Operation(summary = "Gửi hành động muốn thực hiện tới module mạch")
    @PostMapping("/send-action")
    public ResponseEntity<?> sendAction(@RequestBody Map<String, String> payload) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String espUrl = espBaseUrl + "/action"; // ESP32 IP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(espUrl, entity, String.class);

            return ResponseEntity.ok(new ServiceResponse("Task sent to ESP32: " + response.getBody(), 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with ESP32: " + e.getMessage());
        }
    }

    private boolean checkCardID(String cardID) {
        // Kiểm tra cardID trong database
        return true; // Giả sử thẻ hợp lệ
    }
}
