package com.breskeby.idea.plugin.isc.modelbuilder

import com.intellij.openapi.util.NlsSafe
import java.net.InetAddress
import java.security.MessageDigest
import java.util.*
import java.util.HashMap
import javax.xml.bind.DatatypeConverter

class EnvironmentBuilder {

    companion object {
        val ANONYMIZABLE_ITEMS = setOf("user.name", "host")
    }

    private var pluginVersion: String = "unknown"
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

    private fun resolveHostName(): String = InetAddress.getLocalHost().hostName

    fun build() : Map<String,String> =  environment.mapValues { maybeAnonymize(it.key, it.value) }

    private fun maybeAnonymize(key: String, value: String): String = if (shouldAnonymize(key)) checksum(value) else value

    private fun shouldAnonymize(key: String) = anonymized && ANONYMIZABLE_ITEMS.contains(key)

    private fun checksum(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        md.update(input.encodeToByteArray())
        val digest = md.digest()
        return DatatypeConverter.printHexBinary(digest).uppercase(Locale.getDefault())
    }

    fun withPluginVersion(pluginVersion: String) {
        this.environment["isc-version"] = pluginVersion
    }

}
