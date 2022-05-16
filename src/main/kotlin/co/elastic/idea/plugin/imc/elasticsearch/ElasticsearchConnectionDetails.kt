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

@Suppress("ComplexInterface")
interface ElasticsearchConnectionDetails {

    fun getIndex() : String
    fun getHost() : String
    fun getPort() : Int
    fun getAuthType() : AuthType
    fun getUsername() : String
    fun getPassword() : String
    fun getAccessToken() : String
    fun getApiKey(): String
    fun getApiSecret(): String
    fun useHttp(): Boolean

    enum class AuthType {
        NO_AUTH, BASIC_AUTH, ACCESS_TOKEN_AUTH, API_KEYS_AUTH
    }

}
