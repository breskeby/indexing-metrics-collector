package com.breskeby.idea.plugin.isc

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.breskeby.idea.plugin.isc.settings.IscSettingsState
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient

class ElasticsearchClientFactory(val settingsState: IscSettingsState) {

    val jacksonJsonpMapper = JacksonJsonpMapper()
    init {
        jacksonJsonpMapper.objectMapper().registerKotlinModule()
        jacksonJsonpMapper.objectMapper().registerModule(JavaTimeModule())
    }

    fun newElasticsearchAsyncClient(): ElasticsearchAsyncClient {
        return ElasticsearchAsyncClient(newRestClientTransport())
    }

    fun newElasticsearchClient(): ElasticsearchClient {
        return ElasticsearchClient(newRestClientTransport())
    }

    private fun newRestClientTransport(): RestClientTransport {
        val restClientBuilder = RestClient.builder(
            HttpHost(settingsState.elasticsearchHost, settingsState.elasticsearchPort, "https")
        )
        if (!settingsState.elasticsearchUsername.isNullOrBlank() && !settingsState.elasticsearchPassword.isNullOrBlank()) {
            val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                UsernamePasswordCredentials(settingsState.elasticsearchUsername, settingsState.elasticsearchPassword)
            )
            restClientBuilder.setHttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            }
        }
        val transport = RestClientTransport(restClientBuilder.build(), jacksonJsonpMapper)
        return transport
    }


}