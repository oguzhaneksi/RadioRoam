package com.radioroam.android.data.model.station


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class RadioStationDtoItem(
    @SerialName("bitrate")
    @Ignore
    var bitrate: Int? = null,
    @SerialName("changeuuid")
    @Ignore
    var changeuuid: String? = null,
    @SerialName("clickcount")
    @Ignore
    var clickcount: Int? = null,
    @SerialName("clicktimestamp")
    @Ignore
    var clicktimestamp: String? = null,
    @SerialName("clicktimestamp_iso8601")
    @Ignore
    var clicktimestampIso8601: String? = null,
    @SerialName("clicktrend")
    @Ignore
    var clicktrend: Int? = null,
    @SerialName("codec")
    @Ignore
    var codec: String? = null,
    @SerialName("country")
    @Ignore
    var country: String? = null,
    @SerialName("countrycode")
    @Ignore
    var countrycode: String? = null,
    @SerialName("favicon")
    @ColumnInfo("favicon")
    var favicon: String? = null,
    @SerialName("has_extended_info")
    @Ignore
    var hasExtendedInfo: Boolean? = null,
    @SerialName("hls")
    @Ignore
    var hls: Int? = null,
    @SerialName("homepage")
    @Ignore
    var homepage: String? = null,
    @SerialName("language")
    @Ignore
    var language: String? = null,
    @SerialName("languagecodes")
    @Ignore
    var languagecodes: String? = null,
    @SerialName("lastchangetime")
    @Ignore
    var lastchangetime: String? = null,
    @SerialName("lastchangetime_iso8601")
    @Ignore
    var lastchangetimeIso8601: String? = null,
    @SerialName("lastcheckok")
    @Ignore
    var lastcheckok: Int? = null,
    @SerialName("lastcheckoktime")
    @Ignore
    var lastcheckoktime: String? = null,
    @SerialName("lastcheckoktime_iso8601")
    @Ignore
    var lastcheckoktimeIso8601: String? = null,
    @SerialName("lastchecktime")
    @Ignore
    var lastchecktime: String? = null,
    @SerialName("lastchecktime_iso8601")
    @Ignore
    var lastchecktimeIso8601: String? = null,
    @SerialName("lastlocalchecktime")
    @Ignore
    var lastlocalchecktime: String? = null,
    @SerialName("lastlocalchecktime_iso8601")
    @Ignore
    var lastlocalchecktimeIso8601: String? = null,
    @SerialName("name")
    @ColumnInfo("name")
    var name: String? = null,
    @SerialName("serveruuid")
    @Ignore
    var serveruuid: String? = null,
    @SerialName("ssl_error")
    @Ignore
    var sslError: Int? = null,
    @SerialName("state")
    @Ignore
    var state: String? = null,
    @SerialName("stationuuid")
    @ColumnInfo("stationuuid")
    @PrimaryKey(autoGenerate = false)
    var stationuuid: String = "",
    @SerialName("tags")
    @ColumnInfo("tags")
    var tags: String? = null,
    @SerialName("url")
    @Ignore
    var url: String? = null,
    @SerialName("url_resolved")
    @ColumnInfo("url_resolved")
    var urlResolved: String? = null,
    @SerialName("votes")
    @Ignore
    var votes: Int? = null
)