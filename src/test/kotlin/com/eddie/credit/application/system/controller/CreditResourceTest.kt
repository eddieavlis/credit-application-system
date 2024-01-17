package com.eddie.credit.application.system.controller

import com.eddie.credit.application.system.dto.request.CreditDto
import com.eddie.credit.application.system.entity.Credit
import com.eddie.credit.application.system.service.impl.CreditService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@WebMvcTest(CreditResource::class)
@AutoConfigureMockMvc
class CreditResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var creditService: CreditService

    @Test
    fun `should save credit and return 201 status`() {
        // given
        val creditDto = CreditDto(
            creditValue = BigDecimal.valueOf(9000),
            numberOfInstallments = 6,
            dayFirstInstallment = LocalDate.now().plusDays(1),
            customerId = 1L
        )
        val credit = Credit(
            creditCode = UUID.randomUUID(),
            creditValue = creditDto.creditValue,
            numberOfInstallments = creditDto.numberOfInstallments,
            dayFirstInstallment = creditDto.dayFirstInstallment
        )

        `when`(creditService.save(any())).thenReturn(credit)

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/credits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creditDto))
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().string("Credit ${credit.creditCode} - Customer null saved!"))
    }

    @Test
    fun `should find all credits by customer id`() {
        // given
        val customerId = 1L
        val credit1 = Credit(
            creditCode = UUID.randomUUID(),
            creditValue = BigDecimal.valueOf(9000),
            numberOfInstallments = 6,
            dayFirstInstallment = LocalDate.now()
        )
        val credit2 = Credit(
            creditCode = UUID.randomUUID(),
            creditValue = BigDecimal.valueOf(9000),
            numberOfInstallments = 6,
            dayFirstInstallment = LocalDate.now()
        )
        `when`(creditService.findAllByCustomer(customerId)).thenReturn(listOf(credit1, credit2))

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/credits")
                .param("customerId", customerId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").value(credit1.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditCode").value(credit2.creditCode.toString()))
    }

    @Test
    fun `should find credit by credit code`() {
        // given
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val credit = Credit(
            creditCode = creditCode,
            creditValue = BigDecimal.valueOf(9000),
            numberOfInstallments = 6,
            dayFirstInstallment = LocalDate.now()
        )
        `when`(creditService.findByCreditCode(customerId, creditCode)).thenReturn(credit)

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/credits/{creditCode}", creditCode)
                .param("customerId", customerId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(creditCode.toString()))
    }
}
