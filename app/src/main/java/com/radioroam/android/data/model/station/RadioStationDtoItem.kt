package com.radioroam.android.data.model.station


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RadioStationDtoItem(
    @SerialName("bitrate")
    val bitrate: Int? = null,
    @SerialName("changeuuid")
    val changeuuid: String? = null,
    @SerialName("clickcount")
    val clickcount: Int? = null,
    @SerialName("clicktimestamp")
    val clicktimestamp: String? = null,
    @SerialName("clicktimestamp_iso8601")
    val clicktimestampIso8601: String? = null,
    @SerialName("clicktrend")
    val clicktrend: Int? = null,
    @SerialName("codec")
    val codec: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("countrycode")
    val countrycode: String? = null,
    @SerialName("favicon")
    val favicon: String? = null,
    @SerialName("has_extended_info")
    val hasExtendedInfo: Boolean? = null,
    @SerialName("hls")
    val hls: Int? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("language")
    val language: String? = null,
    @SerialName("languagecodes")
    val languagecodes: String? = null,
    @SerialName("lastchangetime")
    val lastchangetime: String? = null,
    @SerialName("lastchangetime_iso8601")
    val lastchangetimeIso8601: String? = null,
    @SerialName("lastcheckok")
    val lastcheckok: Int? = null,
    @SerialName("lastcheckoktime")
    val lastcheckoktime: String? = null,
    @SerialName("lastcheckoktime_iso8601")
    val lastcheckoktimeIso8601: String? = null,
    @SerialName("lastchecktime")
    val lastchecktime: String? = null,
    @SerialName("lastchecktime_iso8601")
    val lastchecktimeIso8601: String? = null,
    @SerialName("lastlocalchecktime")
    val lastlocalchecktime: String? = null,
    @SerialName("lastlocalchecktime_iso8601")
    val lastlocalchecktimeIso8601: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("serveruuid")
    val serveruuid: String? = null,
    @SerialName("ssl_error")
    val sslError: Int? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("stationuuid")
    val stationuuid: String? = null,
    @SerialName("tags")
    val tags: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("url_resolved")
    val urlResolved: String? = null,
    @SerialName("votes")
    val votes: Int? = null
)