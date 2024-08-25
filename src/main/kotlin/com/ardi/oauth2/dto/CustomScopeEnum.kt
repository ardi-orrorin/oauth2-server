package com.ardi.oauth2.dto

enum class CustomScopeEnum(
    val value: String,
    val description: String
) {
    OPENID("openid", "계정 필수 정보(아이디)"),
    NAME("name", "계정 이름"),
    BIRTHDAY("birthday", "생년월일"),
    PHONE("phone", "연락처(핸드폰번호 및 전화번호)"),
    ADDRESS("address", "주소 및 상세주소")
}
