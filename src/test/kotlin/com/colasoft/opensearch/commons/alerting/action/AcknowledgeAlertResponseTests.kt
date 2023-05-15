package com.colasoft.opensearch.commons.alerting.action

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import com.colasoft.opensearch.common.io.stream.BytesStreamOutput
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.commons.alerting.alerts.AlertError
import com.colasoft.opensearch.commons.alerting.model.ActionExecutionResult
import com.colasoft.opensearch.commons.alerting.model.Alert
import com.colasoft.opensearch.commons.alerting.randomUser
import java.time.Instant

class AcknowledgeAlertResponseTests {

    @Test
    fun `test acknowledge alert response`() {

        val acknowledged = mutableListOf(
            Alert(
                "1234", 0L, 1, "monitor-1234", "test-monitor", 0L, randomUser(),
                "trigger-14", "test-trigger", ArrayList(), ArrayList(), Alert.State.ACKNOWLEDGED,
                Instant.now(), Instant.now(), Instant.now(), Instant.now(), null, ArrayList(),
                "sev-2", ArrayList(), null
            )
        )
        val failed = mutableListOf(
            Alert(
                "1234", 0L, 1, "monitor-1234", "test-monitor", 0L, randomUser(),
                "trigger-14", "test-trigger", ArrayList(), ArrayList(), Alert.State.ERROR, Instant.now(), Instant.now(),
                Instant.now(), Instant.now(), null, mutableListOf(AlertError(Instant.now(), "Error msg")),
                "sev-2", mutableListOf(ActionExecutionResult("7890", null, 0)), null
            )
        )
        val missing = mutableListOf("1", "2", "3", "4")

        val req = AcknowledgeAlertResponse(acknowledged, failed, missing)
        assertNotNull(req)

        val out = BytesStreamOutput()
        req.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newReq = AcknowledgeAlertResponse(sin)
        assertEquals(1, newReq.acknowledged.size)
        assertEquals(1, newReq.failed.size)
        assertEquals(4, newReq.missing.size)
    }
}
