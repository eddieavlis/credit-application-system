package com.eddie.credit.application.system.service

import com.eddie.credit.application.system.entity.Credit
import com.eddie.credit.application.system.entity.Customer
import com.eddie.credit.application.system.exception.BusinessException
import com.eddie.credit.application.system.repository.CreditRepository
import com.eddie.credit.application.system.service.impl.CreditService
import com.eddie.credit.application.system.service.impl.CustomerService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class CreditServiceTest {
    @Mock
    private lateinit var creditRepository: CreditRepository

    @Mock
    private lateinit var customerService: CustomerService

    @InjectMocks
    private lateinit var creditService: CreditService

    private val dayFirstInstallment = LocalDate.now().plusMonths(1)

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testSaveValidCredit() {
        val customer = Customer(id = 1)
        val credit = Credit(
            customer = customer,
            dayFirstInstallment = dayFirstInstallment,
            creditValue = BigDecimal.valueOf(9000),
            numberOfInstallments = 6
        )

        `when`(customerService.findById(customer.id!!)).thenReturn(customer)
        `when`(creditRepository.save(credit)).thenReturn(credit)

        val savedCredit = creditService.save(credit)

        Assertions.assertEquals(credit, savedCredit)
    }

    @Test
    fun testFindAllByCustomer() {
        val customerId = 1L
        val creditsList = listOf(Credit(dayFirstInstallment = dayFirstInstallment), Credit(dayFirstInstallment = dayFirstInstallment))

        `when`(creditRepository.findAllByCustomerId(customerId)).thenReturn(creditsList)

        val foundCredits = creditService.findAllByCustomer(customerId)

        Assertions.assertEquals(creditsList, foundCredits)
    }

    @Test
    fun testFindByCreditCodeValid() {
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val credit = Credit(
            creditCode = creditCode,
            customer = Customer(id = customerId),
            dayFirstInstallment = dayFirstInstallment
        )

        `when`(creditRepository.findByCreditCode(creditCode)).thenReturn(credit)

        val foundCredit = creditService.findByCreditCode(customerId, creditCode)

        Assertions.assertEquals(credit, foundCredit)
    }

    @Test
    fun testFindByCreditCodeInvalidCustomerId() {
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val credit = Credit(
            creditCode = creditCode,
            customer = Customer(id = 2L),
            dayFirstInstallment = dayFirstInstallment
        )

        `when`(creditRepository.findByCreditCode(creditCode)).thenReturn(credit)

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            creditService.findByCreditCode(customerId, creditCode)
        }
    }

    @Test
    fun testValidDayFirstInstallment() {
        val validDate = LocalDate.now().plusMonths(1)

        val isValid = creditService.validDayFirstInstallment(validDate)

        Assertions.assertTrue(isValid)
    }

    @Test
    fun testInvalidDayFirstInstallment() {
        val invalidDate = LocalDate.now().plusMonths(4)

        Assertions.assertThrows(BusinessException::class.java) {
            creditService.validDayFirstInstallment(invalidDate)
        }
    }
}
