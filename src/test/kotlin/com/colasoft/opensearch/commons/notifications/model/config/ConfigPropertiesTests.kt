package com.colasoft.opensearch.commons.notifications.model.config

import org.junit.jupiter.api.Test
import com.colasoft.opensearch.commons.notifications.model.Chime
import com.colasoft.opensearch.commons.notifications.model.ConfigType
import com.colasoft.opensearch.commons.notifications.model.Email
import com.colasoft.opensearch.commons.notifications.model.EmailGroup
import com.colasoft.opensearch.commons.notifications.model.EmailRecipient
import com.colasoft.opensearch.commons.notifications.model.MethodType
import com.colasoft.opensearch.commons.notifications.model.Slack
import com.colasoft.opensearch.commons.notifications.model.SmtpAccount
import com.colasoft.opensearch.commons.notifications.model.Webhook
import com.colasoft.opensearch.commons.notifications.model.config.ConfigDataProperties.createConfigData
import com.colasoft.opensearch.commons.notifications.model.config.ConfigDataProperties.getReaderForConfigType
import com.colasoft.opensearch.commons.utils.createObjectFromJsonString
import com.colasoft.opensearch.commons.utils.getJsonString
import kotlin.test.assertEquals

internal class ConfigPropertiesTests {
    @Test
    fun `Validate config property reader slack`() {
        assertEquals(getReaderForConfigType(ConfigType.SLACK), Slack.reader)
    }

    @Test
    fun `Validate config property reader chime`() {
        assertEquals(getReaderForConfigType(ConfigType.CHIME), Chime.reader)
    }

    @Test
    fun `Validate config property reader webhook`() {
        assertEquals(getReaderForConfigType(ConfigType.WEBHOOK), Webhook.reader)
    }

    @Test
    fun `Validate config property reader email`() {
        assertEquals(getReaderForConfigType(ConfigType.EMAIL), Email.reader)
    }

    @Test
    fun `Validate config property reader EmailGroup`() {
        assertEquals(getReaderForConfigType(ConfigType.EMAIL_GROUP), EmailGroup.reader)
    }

    @Test
    fun `Validate config property reader SmtpAccount`() {
        assertEquals(getReaderForConfigType(ConfigType.SMTP_ACCOUNT), SmtpAccount.reader)
    }

    @Test
    fun `Validate config data parse  slack`() {
        val sampleSlack = Slack("https://domain.com/sample_url#1234567890")
        val jsonString = getJsonString(sampleSlack)
        val recreatedObject = createObjectFromJsonString(jsonString) { createConfigData(ConfigType.SLACK, it) }
        assertEquals(sampleSlack, recreatedObject)
    }

    @Test
    fun `Validate config data parse chime`() {
        val sampleChime = Chime("https://domain.com/sample_url#1234567890")
        val jsonString = getJsonString(sampleChime)
        val recreatedObject = createObjectFromJsonString(jsonString) { createConfigData(ConfigType.CHIME, it) }
        assertEquals(sampleChime, recreatedObject)
    }

    @Test
    fun `Validate config data parse webhook`() {
        val sampleWebhook = Webhook("https://domain.com/sample_url#1234567890")
        val jsonString = getJsonString(sampleWebhook)
        val recreatedObject = createObjectFromJsonString(jsonString) { createConfigData(ConfigType.WEBHOOK, it) }
        assertEquals(sampleWebhook, recreatedObject)
    }

    @Test
    fun `Validate config data parse EmailGroup`() {
        val sampleEmailGroup = EmailGroup(listOf(EmailRecipient("email1@email.com"), EmailRecipient("email2@email.com")))
        val jsonString = getJsonString(sampleEmailGroup)
        val recreatedObject = createObjectFromJsonString(jsonString) { createConfigData(ConfigType.EMAIL_GROUP, it) }
        assertEquals(sampleEmailGroup, recreatedObject)
    }

    @Test
    fun `Validate config data parse SmtpAccount`() {
        val sampleSmtpAccount = SmtpAccount("domain.com", 1234, MethodType.SSL, "from@domain.com")
        val jsonString = getJsonString(sampleSmtpAccount)
        val recreatedObject = createObjectFromJsonString(jsonString) { createConfigData(ConfigType.SMTP_ACCOUNT, it) }
        assertEquals(sampleSmtpAccount, recreatedObject)
    }
}
