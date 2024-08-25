package com.ardi.oauth2.config

import com.ardi.oauth2.dto.UserDetailsDto
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.stereotype.Component

@Component
class UserDetailsDtoModule: SimpleModule()  {
    init {
        addSerializer(UserDetailsDto::class.java, UserDetailsDtoSerializer())
//        addDeserializer(UserDetailsDto::class.java, UserDetailsDtoDeserializer())
    }

    private class UserDetailsDtoSerializer : JsonSerializer<UserDetailsDto>() {
        override fun serialize(value: UserDetailsDto, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartObject()
            gen.writeStringField("@class", UserDetailsDto::class.java.name)
            gen.writeNumberField("id", value.id)
            gen.writeStringField("userId", value.userId)
            gen.writeStringField("pwd", value.pwd)
            gen.writeStringField("username", value.username)
            gen.writeStringField("password", value.password)
            gen.writeBooleanField("enabled", value.isEnabled)
            gen.writeBooleanField("accountNonExpired", value.isAccountNonExpired)
            gen.writeBooleanField("credentialsNonExpired", value.isCredentialsNonExpired)
            gen.writeBooleanField("accountNonLocked", value.isAccountNonLocked)
            gen.writeArrayFieldStart("authorities")
            value.authorities.forEach {
                gen.writeString(it.authority)
            }
            gen.writeEndArray()

            // 필요한 경우 추가 필드를 여기에 작성
            gen.writeEndObject()
            serializers.defaultSerializeValue(value, gen)
        }
    }

    private class UserDetailsDtoDeserializer : JsonDeserializer<UserDetailsDto>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UserDetailsDto {
            val node: JsonNode = p.codec.readTree(p)
            val id = node.get("id").asLong()
            val userId = node.get("username").asText()
            val pwd = node.get("password").asText()


            return UserDetailsDto(id, userId, pwd)
        }
    }
}