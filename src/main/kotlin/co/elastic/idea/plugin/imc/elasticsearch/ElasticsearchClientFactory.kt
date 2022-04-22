/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * */

package co.elastic.idea.plugin.imc.elasticsearch

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import co.elastic.idea.plugin.imc.settings.ImcSettingsState
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.http.Header
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.message.BasicHeader
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import java.nio.charset.StandardCharsets
import java.util.*

class ElasticsearchClientFactory(private val state: ImcSettingsState) {

    private val jacksonJsonpMapper = JacksonJsonpMapper()

    init {
        jacksonJsonpMapper.objectMapper().registerKotlinModule()
        jacksonJsonpMapper.objectMapper().registerModule(JavaTimeModule())
    }

    fun newElasticsearchClient(): ElasticsearchClient {
        return ElasticsearchClient(newRestClientTransport())
    }

    private fun newRestClientTransport(): RestClientTransport {
        val restClientBuilder = RestClient.builder(
            HttpHost(state.esHost, state.esPort, "https")
        )
        return RestClientTransport(withAuthentication(restClientBuilder).build(), jacksonJsonpMapper)
    }

    private fun withAuthentication(restClientBuilder: RestClientBuilder): RestClientBuilder {
        when (state.authType) {
            ImcSettingsState.AuthType.BASIC_AUTH -> {
                val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()
                credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    UsernamePasswordCredentials(state.esUsername, state.esPassword)
                )
                restClientBuilder.setHttpClientConfigCallback { httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                }
            }
            ImcSettingsState.AuthType.ACCESS_TOKEN_AUTH -> {
                val defaultHeaders: Array<Header> = arrayOf(
                    BasicHeader(
                        "Authorization",
                        "Bearer ${state.esAccessToken}"
                    )
                )
                restClientBuilder.setDefaultHeaders(defaultHeaders)
            }
            ImcSettingsState.AuthType.API_KEYS_AUTH -> {
                val apiKeyAuth: String = Base64.getEncoder().encodeToString(
                    (state.esApiKey + ":" + state.esApiSecret).toByteArray(StandardCharsets.UTF_8)
                )
                val defaultHeaders = arrayOf<Header>(
                    BasicHeader(
                        "Authorization",
                        "ApiKey $apiKeyAuth"
                    )
                )
                restClientBuilder.setDefaultHeaders(defaultHeaders)
            }
            ImcSettingsState.AuthType.NO_AUTH -> {
                // nothing need to be done
            }
        }
        return restClientBuilder
    }
}

