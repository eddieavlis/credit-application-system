package com.eddie.credit.application.system.dto.request

import com.eddie.credit.application.system.entity.Credit
import com.eddie.credit.application.system.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field:Future val dayFirstInstallment: LocalDate,
    @field:Min(value = 1) @field:Max(value = 48) val numberOfInstallments: Int,
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId),
        dayFirstInstallment = dayFirstInstallment
    )
}
