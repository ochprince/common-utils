package com.colasoft.opensearch.commons.alerting.model

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.common.io.stream.Writeable
import java.io.IOException

data class Table(
    val sortOrder: String,
    val sortString: String,
    val missing: String?,
    val size: Int,
    val startIndex: Int,
    val searchString: String?
) : Writeable {

    @Throws(IOException::class)
    constructor(sin: StreamInput) : this(
        sortOrder = sin.readString(),
        sortString = sin.readString(),
        missing = sin.readOptionalString(),
        size = sin.readInt(),
        startIndex = sin.readInt(),
        searchString = sin.readOptionalString()
    )

    @Throws(IOException::class)
    override fun writeTo(out: StreamOutput) {
        out.writeString(sortOrder)
        out.writeString(sortString)
        out.writeOptionalString(missing)
        out.writeInt(size)
        out.writeInt(startIndex)
        out.writeOptionalString(searchString)
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun readFrom(sin: StreamInput): Table {
            return Table(sin)
        }
    }
}
