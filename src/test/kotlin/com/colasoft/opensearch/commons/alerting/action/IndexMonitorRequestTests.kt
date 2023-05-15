package com.colasoft.opensearch.commons.alerting.action

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import com.colasoft.opensearch.action.support.WriteRequest
import com.colasoft.opensearch.common.io.stream.BytesStreamOutput
import com.colasoft.opensearch.common.io.stream.NamedWriteableAwareStreamInput
import com.colasoft.opensearch.common.io.stream.NamedWriteableRegistry
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.settings.Settings
import com.colasoft.opensearch.commons.alerting.model.SearchInput
import com.colasoft.opensearch.commons.alerting.randomBucketLevelMonitor
import com.colasoft.opensearch.commons.alerting.randomQueryLevelMonitor
import com.colasoft.opensearch.commons.utils.recreateObject
import com.colasoft.opensearch.rest.RestRequest
import com.colasoft.opensearch.search.SearchModule
import com.colasoft.opensearch.search.builder.SearchSourceBuilder

class IndexMonitorRequestTests {

    @Test
    fun `test index monitor post request`() {

        val req = IndexMonitorRequest(
            "1234", 1L, 2L, WriteRequest.RefreshPolicy.IMMEDIATE, RestRequest.Method.POST,
            randomQueryLevelMonitor().copy(inputs = listOf(SearchInput(emptyList(), SearchSourceBuilder())))
        )
        Assertions.assertNotNull(req)

        val out = BytesStreamOutput()
        req.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newReq = IndexMonitorRequest(sin)
        Assertions.assertEquals("1234", newReq.monitorId)
        Assertions.assertEquals(1L, newReq.seqNo)
        Assertions.assertEquals(2L, newReq.primaryTerm)
        Assertions.assertEquals(RestRequest.Method.POST, newReq.method)
        Assertions.assertNotNull(newReq.monitor)
    }

    @Test
    fun `test index bucket monitor post request`() {
        val req = IndexMonitorRequest(
            "1234", 1L, 2L, WriteRequest.RefreshPolicy.IMMEDIATE, RestRequest.Method.POST,
            randomBucketLevelMonitor()
        )
        Assertions.assertNotNull(req)

        val out = BytesStreamOutput()
        req.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val namedWriteableRegistry = NamedWriteableRegistry(SearchModule(Settings.EMPTY, emptyList()).namedWriteables)
        val newReq = IndexMonitorRequest(NamedWriteableAwareStreamInput(sin, namedWriteableRegistry))
        Assertions.assertEquals("1234", newReq.monitorId)
        Assertions.assertEquals(1L, newReq.seqNo)
        Assertions.assertEquals(2L, newReq.primaryTerm)
        Assertions.assertEquals(RestRequest.Method.POST, newReq.method)
        Assertions.assertNotNull(newReq.monitor)
    }

    @Test
    fun `Index bucket monitor serialize and deserialize transport object should be equal`() {
        val bucketLevelMonitorRequest = IndexMonitorRequest(
            "1234", 1L, 2L, WriteRequest.RefreshPolicy.IMMEDIATE, RestRequest.Method.POST,
            randomBucketLevelMonitor()
        )

        Assertions.assertThrows(UnsupportedOperationException::class.java) {
            recreateObject(bucketLevelMonitorRequest) { IndexMonitorRequest(it) }
        }

        val recreatedObject = recreateObject(bucketLevelMonitorRequest, NamedWriteableRegistry(SearchModule(Settings.EMPTY, emptyList()).namedWriteables)) { IndexMonitorRequest(it) }
        Assertions.assertEquals(bucketLevelMonitorRequest.monitorId, recreatedObject.monitorId)
        Assertions.assertEquals(bucketLevelMonitorRequest.seqNo, recreatedObject.seqNo)
        Assertions.assertEquals(bucketLevelMonitorRequest.primaryTerm, recreatedObject.primaryTerm)
        Assertions.assertEquals(bucketLevelMonitorRequest.method, recreatedObject.method)
        Assertions.assertNotNull(recreatedObject.monitor)
        Assertions.assertEquals(bucketLevelMonitorRequest.monitor, recreatedObject.monitor)
    }

    @Test
    fun `test index monitor put request`() {

        val req = IndexMonitorRequest(
            "1234", 1L, 2L, WriteRequest.RefreshPolicy.IMMEDIATE, RestRequest.Method.PUT,
            randomQueryLevelMonitor().copy(inputs = listOf(SearchInput(emptyList(), SearchSourceBuilder())))
        )
        Assertions.assertNotNull(req)

        val out = BytesStreamOutput()
        req.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newReq = IndexMonitorRequest(sin)
        Assertions.assertEquals("1234", newReq.monitorId)
        Assertions.assertEquals(1L, newReq.seqNo)
        Assertions.assertEquals(2L, newReq.primaryTerm)
        Assertions.assertEquals(RestRequest.Method.PUT, newReq.method)
        Assertions.assertNotNull(newReq.monitor)
    }
}
