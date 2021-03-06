package pub.edholm.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.net.UrlEscapers
import pub.edholm.db.Swosh
import java.net.URI


data class StringValue(
        val value: String,
        val editable: Boolean = false
)

data class IntValue(
        val value: Int,
        val editable: Boolean = false
)

data class SwishDataDTO(
        val version: Int = 1,
        val payee: StringValue,
        val amount: IntValue,
        val message: StringValue
)

fun SwishDataDTO.generateUri(): URI {
    val asString = jacksonObjectMapper().writeValueAsString(this)
    val encodedData = UrlEscapers.urlFragmentEscaper().escape(asString)
    return URI.create("swish://payment?data=$encodedData")
}

fun Swosh.toSwishDataDTO(): SwishDataDTO {
    return SwishDataDTO(
            payee = StringValue(this.payee, false),
            amount = IntValue(this.amount, false),
            message = StringValue(this.description ?: "", true))
}
