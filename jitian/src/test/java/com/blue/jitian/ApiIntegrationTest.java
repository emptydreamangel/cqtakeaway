package com.blue.jitian;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * API 集成测试
 * 测试主要 Controller 接口是否正常工作
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== 已实现的 API 测试 ====================
    
    @Test
    void testGetNotificationPage() throws Exception {
        mockMvc.perform(get("/api/notifications/page")
                .param("current", "1")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    void testGetUserCouponPage() throws Exception {
        mockMvc.perform(get("/api/user-coupons/page")
                .param("current", "1")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    void testGetPaymentRecordPage() throws Exception {
        mockMvc.perform(get("/api/payment-records/page")
                .param("current", "1")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    void testGetPaymentStats() throws Exception {
        mockMvc.perform(get("/api/payment-records/stats/status"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetRefundRecordPage() throws Exception {
        mockMvc.perform(get("/api/refund-records/page")
                .param("current", "1")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    void testGetRefundStats() throws Exception {
        mockMvc.perform(get("/api/refund-records/stats/status"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void testContextLoads() {
        // 验证Spring上下文能正常加载
    }
}
