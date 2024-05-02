package com.sigma.data.network.dto.dauth


data class DauthLoginRequest(
    val id: String,
    val pw: String,
    val clientId: String = "bb7fb3014255411a8384fa91146f3714767deb4d66b542f1863e202aa30c6a36",
//    val redirectUrl: String = "http://39.116.14.193:3000",
    val redirectUrl: String = "http://1.246.8.179:3000",
    val state: String = "null",
)
