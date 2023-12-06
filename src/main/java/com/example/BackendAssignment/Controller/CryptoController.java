package com.example.BackendAssignment.Controller;

import com.example.BackendAssignment.Service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/getCoinData/{symbol}")
    public ResponseEntity<String> getCoinData(@PathVariable String symbol) {
        try {
            String response = cryptoService.getCoinData(symbol);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions appropriately (e.g., log, return an error response)
            return ResponseEntity.status(500).body("Error fetching cryptocurrency data");
        }
    }

}
