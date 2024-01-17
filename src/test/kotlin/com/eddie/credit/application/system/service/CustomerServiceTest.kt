package com.eddie.credit.application.system.service

import com.eddie.credit.application.system.entity.Customer
import com.eddie.credit.application.system.exception.BusinessException
import com.eddie.credit.application.system.repository.CustomerRepository
import com.eddie.credit.application.system.service.impl.CustomerService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    private val customerRepository: CustomerRepository = mockk()
    private val customerService: CustomerService = CustomerService(customerRepository)

    @Test
    fun `should save customer`() {
        val customerId = 1L
        val customer = Customer(
            firstName = "Antonio",
            lastName = "Silva", cpf = "94692505834", email = "antonio@hotmail.com",
            income = BigDecimal.valueOf(9000.0), password = "1234_veryGood",
            zipCode = "06298030"
        )

        every { customerRepository.save(any()) } returns customer

        val savedCustomer = customerService.save(customer)

        assertThat(savedCustomer).isEqualTo(customer)
    }

    @Test
    fun `should find customer by ID`() {
        val customerId = 1L
        val customer = Customer(
            firstName = "Antonio",
            lastName = "Silva",
            email = "antonio@hotmail.com",
            password = "1234_veryGood",
            id = customerId,
            zipCode = "06298030",

            )

        every { customerRepository.findById(customerId) } returns java.util.Optional.of(customer)

        val foundCustomer = customerService.findById(customerId)

        assertThat(foundCustomer).isEqualTo(customer)
    }

    @Test
    fun `should throw BusinessException when customer ID not found`() {
        val customerId = 1L

        every { customerRepository.findById(customerId) } returns java.util.Optional.empty()

        assertThatThrownBy { customerService.findById(customerId) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("Id $customerId not found")
    }

    @Test
    fun `should delete customer`() {
        val customerId = 1L
        val customer = Customer(
            firstName = "Antonio",
            lastName = "Silva",
            email = "antonio@hotmail.com",
            password = "1234_veryGood",
            id = customerId,
            zipCode = "06298030",
            )

        every { customerRepository.findById(customerId) } returns java.util.Optional.of(customer)
        every { customerRepository.delete(customer) } returns Unit

        customerService.delete(customerId)
    }
}
