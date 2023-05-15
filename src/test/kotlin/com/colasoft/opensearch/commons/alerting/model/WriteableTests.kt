package com.colasoft.opensearch.commons.alerting.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import com.colasoft.opensearch.common.io.stream.BytesStreamOutput
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.commons.alerting.model.action.Action
import com.colasoft.opensearch.commons.alerting.model.action.ActionExecutionPolicy
import com.colasoft.opensearch.commons.alerting.model.action.Throttle
import com.colasoft.opensearch.commons.alerting.randomAction
import com.colasoft.opensearch.commons.alerting.randomActionExecutionPolicy
import com.colasoft.opensearch.commons.alerting.randomBucketLevelTrigger
import com.colasoft.opensearch.commons.alerting.randomDocumentLevelTrigger
import com.colasoft.opensearch.commons.alerting.randomQueryLevelMonitor
import com.colasoft.opensearch.commons.alerting.randomQueryLevelTrigger
import com.colasoft.opensearch.commons.alerting.randomThrottle
import com.colasoft.opensearch.commons.alerting.randomUser
import com.colasoft.opensearch.commons.alerting.randomUserEmpty
import com.colasoft.opensearch.commons.authuser.User
import com.colasoft.opensearch.search.builder.SearchSourceBuilder

class WriteableTests {

    @Test
    fun `test throttle as stream`() {
        val throttle = randomThrottle()
        val out = BytesStreamOutput()
        throttle.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newThrottle = Throttle(sin)
        Assertions.assertEquals(throttle, newThrottle, "Round tripping Throttle doesn't work")
    }

    @Test
    fun `test action as stream`() {
        val action = randomAction()
        val out = BytesStreamOutput()
        action.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newAction = Action(sin)
        Assertions.assertEquals(action, newAction, "Round tripping Action doesn't work")
    }

    @Test
    fun `test action as stream with null subject template`() {
        val action = randomAction().copy(subjectTemplate = null)
        val out = BytesStreamOutput()
        action.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newAction = Action(sin)
        Assertions.assertEquals(action, newAction, "Round tripping Action doesn't work")
    }

    @Test
    fun `test action as stream with null throttle`() {
        val action = randomAction().copy(throttle = null)
        val out = BytesStreamOutput()
        action.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newAction = Action(sin)
        Assertions.assertEquals(action, newAction, "Round tripping Action doesn't work")
    }

    @Test
    fun `test action as stream with throttled enabled and null throttle`() {
        val action = randomAction().copy(throttle = null).copy(throttleEnabled = true)
        val out = BytesStreamOutput()
        action.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newAction = Action(sin)
        Assertions.assertEquals(action, newAction, "Round tripping Action doesn't work")
    }

    @Test
    fun `test query-level monitor as stream`() {
        val monitor = randomQueryLevelMonitor().copy(inputs = listOf(SearchInput(emptyList(), SearchSourceBuilder())))
        val out = BytesStreamOutput()
        monitor.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newMonitor = Monitor(sin)
        Assertions.assertEquals(monitor, newMonitor, "Round tripping QueryLevelMonitor doesn't work")
    }

    @Test
    fun `test query-level trigger as stream`() {
        val trigger = randomQueryLevelTrigger()
        val out = BytesStreamOutput()
        trigger.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newTrigger = QueryLevelTrigger.readFrom(sin)
        Assertions.assertEquals(trigger, newTrigger, "Round tripping QueryLevelTrigger doesn't work")
    }

    @Test
    fun `test bucket-level trigger as stream`() {
        val trigger = randomBucketLevelTrigger()
        val out = BytesStreamOutput()
        trigger.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newTrigger = BucketLevelTrigger.readFrom(sin)
        Assertions.assertEquals(trigger, newTrigger, "Round tripping BucketLevelTrigger doesn't work")
    }

    @Test
    fun `test doc-level trigger as stream`() {
        val trigger = randomDocumentLevelTrigger()
        val out = BytesStreamOutput()
        trigger.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newTrigger = DocumentLevelTrigger.readFrom(sin)
        Assertions.assertEquals(trigger, newTrigger, "Round tripping DocumentLevelTrigger doesn't work")
    }

    @Test
    fun `test searchinput as stream`() {
        val input = SearchInput(emptyList(), SearchSourceBuilder())
        val out = BytesStreamOutput()
        input.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newInput = SearchInput(sin)
        Assertions.assertEquals(input, newInput, "Round tripping MonitorRunResult doesn't work")
    }

    @Test
    fun `test user as stream`() {
        val user = randomUser()
        val out = BytesStreamOutput()
        user.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newUser = User(sin)
        Assertions.assertEquals(user, newUser, "Round tripping User doesn't work")
    }

    @Test
    fun `test empty user as stream`() {
        val user = randomUserEmpty()
        val out = BytesStreamOutput()
        user.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newUser = User(sin)
        Assertions.assertEquals(user, newUser, "Round tripping User doesn't work")
    }

    @Test
    fun `test action execution policy as stream`() {
        val actionExecutionPolicy = randomActionExecutionPolicy()
        val out = BytesStreamOutput()
        actionExecutionPolicy.writeTo(out)
        val sin = StreamInput.wrap(out.bytes().toBytesRef().bytes)
        val newActionExecutionPolicy = ActionExecutionPolicy.readFrom(sin)
        Assertions.assertEquals(
            actionExecutionPolicy,
            newActionExecutionPolicy,
            "Round tripping ActionExecutionPolicy doesn't work"
        )
    }
}
