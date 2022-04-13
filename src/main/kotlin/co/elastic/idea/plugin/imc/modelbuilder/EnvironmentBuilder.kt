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

package co.elastic.idea.plugin.imc.modelbuilder

import java.net.InetAddress
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and

class EnvironmentBuilder {
    private val environment = HashMap<String, String>()
    private var anonymized: Boolean = false

    fun withAnonymizedData(anon: Boolean = true) : EnvironmentBuilder {
        anonymized = anon
        return this
    }

    fun withEnvironment() : EnvironmentBuilder {
        environment["os.name"] = System.getProperty("os.name")
        environment["os.arch"] = System.getProperty("os.arch")
        environment["os.version"] = System.getProperty("os.version")
        environment["user.name"] = System.getProperty("user.name")
        environment["host"] = resolveHostName()
        return this
    }

    fun build() : Map<String,String> =  environment.mapValues { maybeAnonymize(it.key, it.value) }


    fun withPluginVersion(pluginVersion: String) {
        this.environment["plugin-version"] = pluginVersion
    }

    private fun maybeAnonymize(key: String, value: String): String =
        if (shouldAnonymize(key)) checksum(value)
        else value

    private fun shouldAnonymize(key: String) = anonymized && ANONYMIZABLE_ITEMS.contains(key)

    @Suppress("MagicNumber")
    private fun checksum(input: String): String {
        val digest = MESSAGE_DIGEST.digest(input.encodeToByteArray())
        var result = ""
        for (i in digest.indices) {
            result += ((digest[i] and 0xff.toByte()) + 0x100).toString(16).substring(1)
        }
        return result
    }

    private fun resolveHostName(): String = InetAddress.getLocalHost().hostName

    companion object {
        val ANONYMIZABLE_ITEMS = setOf("user.name", "host")
        val MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
    }

}
