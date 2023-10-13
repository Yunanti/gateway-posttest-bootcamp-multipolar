package com.bootcamp.multipolar.gatewaybank.controller;

import com.bootcamp.multipolar.gatewaybank.dto.AccountDTO;
import com.bootcamp.multipolar.gatewaybank.dto.ErrorMessageDTO;
import com.bootcamp.multipolar.gatewaybank.kafka.AccessLog;
import com.bootcamp.multipolar.gatewaybank.service.AccessLogService;
import com.bootcamp.multipolar.gatewaybank.util.RestTemplateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final String ACCOUNT_URL = "http://localhost:8081/accounts";
    private final RestTemplateUtil restTemplateUtil;
    private final ObjectMapper objectMapper;
    private final AccessLogService accessLogService;
//    private final HttpServletRequest requestIP;
//    private final HttpServletRequest requestUser;
    @Autowired
    private HttpServletRequest request;


    public ApiController(RestTemplateUtil restTemplateUtil, ObjectMapper objectMapper, AccessLogService accessLogService, HttpServletRequest request, HttpServletRequest requestIP, HttpServletRequest requestUser) {
        this.restTemplateUtil = restTemplateUtil;
        this.objectMapper = objectMapper;
        this.accessLogService = accessLogService;
//        this.requestIP = requestIP;
//        this.requestUser = requestUser;
    }

    @GetMapping("/getAccounts")
    public ResponseEntity<?> getAccounts() throws JsonProcessingException {
//        akses API account dan dapatkan datanya
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        try {
            ResponseEntity<?> response = restTemplateUtil.getList(ACCOUNT_URL, new ParameterizedTypeReference<>() {});
//            ResponseEntity.status(response.getStatusCode()).body(response.getBody());
//            kirim accesslog
            AccessLog accessLog = new AccessLog("GET", ACCOUNT_URL, response.getStatusCodeValue(), "Successful", clientIP, userAgent, LocalDateTime.now()); //Create an access log
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            AccessLog accessLog = new AccessLog("GET", ACCOUNT_URL, ex.getRawStatusCode(), "Error: " + ex.getLocalizedMessage(), clientIP, userAgent, LocalDateTime.now());
            accessLogService.logAccess(accessLog);
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    //    Buat
    @PostMapping("/createAccount")
    public ResponseEntity<?> postAccount(@RequestBody AccountDTO accountDTO) throws JsonProcessingException {
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        try {
            ResponseEntity<?> response = restTemplateUtil.post(ACCOUNT_URL, accountDTO, AccountDTO.class);
            AccessLog accessLog = new AccessLog("POST", ACCOUNT_URL, response.getStatusCodeValue(), "Successful", clientIP, userAgent, LocalDateTime.now()); //Create an access log
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            AccessLog accessLog = new AccessLog("POST", ACCOUNT_URL, ex.getRawStatusCode(), "Error: " + ex.getLocalizedMessage(), clientIP, userAgent, LocalDateTime.now());
            accessLogService.logAccess(accessLog);
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    //    delete account
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountId(@PathVariable String id) throws JsonProcessingException {
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        try {
            ResponseEntity<Void> response = restTemplateUtil.delete(ACCOUNT_URL+"/"+id);
            AccessLog accessLog = new AccessLog("DELETE", ACCOUNT_URL, response.getStatusCodeValue(), "Successful", clientIP, userAgent, LocalDateTime.now()); //Create an access log
            accessLogService.logAccess(accessLog);
            return ResponseEntity.notFound().build();
        } catch (HttpClientErrorException ex) {
            AccessLog accessLog = new AccessLog("DELETE", ACCOUNT_URL, ex.getRawStatusCode(), "Error: " + ex.getLocalizedMessage(), clientIP, userAgent, LocalDateTime.now());
            accessLogService.logAccess(accessLog);
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            return ResponseEntity.notFound().build();
        }
    }

    //    put account
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount( @PathVariable String id, @RequestBody AccountDTO accountDTO) throws JsonProcessingException {
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        try {
            ResponseEntity<?> response = restTemplateUtil.put(ACCOUNT_URL+"/"+id, accountDTO, AccountDTO.class);
            AccessLog accessLog = new AccessLog("PUT", ACCOUNT_URL, response.getStatusCodeValue(), "Successful", clientIP, userAgent, LocalDateTime.now()); //Create an access log
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            AccessLog accessLog = new AccessLog("PUT", ACCOUNT_URL, ex.getRawStatusCode(), "Error: " + ex.getLocalizedMessage(), clientIP, userAgent, LocalDateTime.now());
            accessLogService.logAccess(accessLog);
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    //    get id account
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable String id) throws JsonProcessingException {
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        try {
            ResponseEntity<?> response = restTemplateUtil.getList(ACCOUNT_URL+"/"+id, new ParameterizedTypeReference<>() {});
            AccessLog accessLog = new AccessLog("GET", ACCOUNT_URL, response.getStatusCodeValue(), "Successful", clientIP, userAgent, LocalDateTime.now()); //Create an access log
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            AccessLog accessLog = new AccessLog("PUT", ACCOUNT_URL, ex.getRawStatusCode(), "Error: " + ex.getLocalizedMessage(), clientIP, userAgent, LocalDateTime.now());
            accessLogService.logAccess(accessLog);
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }
}
