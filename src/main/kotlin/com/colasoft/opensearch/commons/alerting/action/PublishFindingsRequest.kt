package com.colasoft.opensearch.commons.alerting.action

import com.colasoft.opensearch.action.ActionRequest
import com.colasoft.opensearch.action.ActionRequestValidationException
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.commons.alerting.model.Finding
import java.io.IOException

class PublishFindingsRequest : ActionRequest {

    val monitorId: String

    val finding: Finding

    constructor(
        monitorId: String,
        finding: Finding
    ) : super() {
        this.monitorId = monitorId
        this.finding = finding
    }

    @Throws(IOException::class)
    constructor(sin: StreamInput) : this(
        monitorId = sin.readString(),
        finding = Finding.readFrom(sin)
    )

    override fun validate(): ActionRequestValidationException? {
        return null
    }

    override fun writeTo(out: StreamOutput) {
        out.writeString(monitorId)
        finding.writeTo(out)
    }
}
