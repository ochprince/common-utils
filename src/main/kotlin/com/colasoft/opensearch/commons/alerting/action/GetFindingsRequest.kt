package com.colasoft.opensearch.commons.alerting.action

import com.colasoft.opensearch.action.ActionRequest
import com.colasoft.opensearch.action.ActionRequestValidationException
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.commons.alerting.model.Table
import java.io.IOException

class GetFindingsRequest : ActionRequest {
    val findingId: String?
    val table: Table
    val monitorId: String?
    val monitorIds: List<String>?
    val findingIndex: String?

    constructor(
        findingId: String?,
        table: Table,
        monitorId: String? = null,
        findingIndexName: String? = null,
        monitorIds: List<String>? = null
    ) : super() {
        this.findingId = findingId
        this.table = table
        this.monitorId = monitorId
        this.findingIndex = findingIndexName
        this.monitorIds = monitorIds
    }

    @Throws(IOException::class)
    constructor(sin: StreamInput) : this(
        findingId = sin.readOptionalString(),
        table = Table.readFrom(sin),
        monitorId = sin.readOptionalString(),
        findingIndexName = sin.readOptionalString(),
        monitorIds = sin.readOptionalStringList()
    )

    override fun validate(): ActionRequestValidationException? {
        return null
    }

    @Throws(IOException::class)
    override fun writeTo(out: StreamOutput) {
        out.writeOptionalString(findingId)
        table.writeTo(out)
        out.writeOptionalString(monitorId)
        out.writeOptionalString(findingIndex)
        out.writeOptionalStringCollection(monitorIds)
    }
}
