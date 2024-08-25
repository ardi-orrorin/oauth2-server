package com.ardi.oauth2.config

import com.ardi.oauth2.dto.UserDetailsDto
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class AuthenticationTokenModule: SimpleModule() {
    init {
        addSerializer(UsernamePasswordAuthenticationToken::class.java, AuthTokenSerializer())
//        addDeserializer(UsernamePasswordAuthenticationToken::class.java, AuthTokenDeserializer())
    }

    class AuthTokenSerializer : JsonSerializer<UsernamePasswordAuthenticationToken>() {
        override fun serialize(value: UsernamePasswordAuthenticationToken, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartObject()
            gen.writeStringField("@class", UsernamePasswordAuthenticationToken::class.java.name)
            gen.writeObjectField("principal", value.principal)
            gen.writeObjectField("credentials", value.credentials)
            gen.writeBooleanField("authenticated", value.isAuthenticated)
            gen.writeObjectField("details", value.details)
            gen.writeObjectField("authorities", value.authorities)
            gen.writeEndObject()
            serializers.defaultSerializeValue(value, gen)
        }
    }

    class AuthTokenDeserializer : JsonDeserializer<UsernamePasswordAuthenticationToken>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UsernamePasswordAuthenticationToken {
            val node: JsonNode = p.codec.readTree(p)
            val principal = ctxt.readValue(node.get("principal").traverse(p.codec), UserDetailsDto::class.java)
            val credentials = node.get("credentials").textValue()
            val authenticated = node.get("authenticated").booleanValue()
            val authorities = node.get("authorities").map { SimpleGrantedAuthority(it.textValue()) }

            return UsernamePasswordAuthenticationToken(principal, credentials, authorities)
        }
    }
}