package com.colasoft.opensearch.commons.alerting.model

import com.colasoft.opensearch.common.settings.Settings
import com.colasoft.opensearch.common.xcontent.LoggingDeprecationHandler
import com.colasoft.opensearch.common.xcontent.XContentType
import com.colasoft.opensearch.core.xcontent.NamedXContentRegistry
import com.colasoft.opensearch.core.xcontent.XContentBuilder
import com.colasoft.opensearch.core.xcontent.XContentParser
import com.colasoft.opensearch.search.SearchModule

interface XContentTestBase {
    fun builder(): XContentBuilder {
        return XContentBuilder.builder(XContentType.JSON.xContent())
    }

    fun parser(xc: String): XContentParser {
        val parser = XContentType.JSON.xContent().createParser(xContentRegistry(), LoggingDeprecationHandler.INSTANCE, xc)
        parser.nextToken()
        return parser
    }

    fun xContentRegistry(): NamedXContentRegistry {
        return NamedXContentRegistry(
            listOf(SearchInput.XCONTENT_REGISTRY) + SearchModule(Settings.EMPTY, emptyList()).namedXContents
        )
    }
}
