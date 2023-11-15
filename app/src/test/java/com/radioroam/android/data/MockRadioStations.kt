package com.radioroam.android.data

import com.radioroam.android.data.model.station.RadioStationDtoItem

val mockRadioStationDtoItems = listOf(
    RadioStationDtoItem(
        bitrate = 128,
        changeuuid = "uuid1",
        clickcount = 100,
        clicktimestamp = "2023-01-01T00:00:00Z",
        clicktimestampIso8601 = "2023-01-01T00:00:00Z",
        clicktrend = 5,
        codec = "MP3",
        country = "United States",
        countrycode = "US",
        favicon = "https://example.com/favicon1.ico",
        hasExtendedInfo = true,
        hls = 0,
        homepage = "https://example.com/station1",
        language = "English",
        languagecodes = "EN",
        lastchangetime = "2023-01-02T00:00:00Z",
        lastchangetimeIso8601 = "2023-01-02T00:00:00Z",
        lastcheckok = 1,
        lastcheckoktime = "2023-01-03T00:00:00Z",
        lastcheckoktimeIso8601 = "2023-01-03T00:00:00Z",
        lastchecktime = "2023-01-04T00:00:00Z",
        lastchecktimeIso8601 = "2023-01-04T00:00:00Z",
        lastlocalchecktime = "2023-01-05T00:00:00Z",
        lastlocalchecktimeIso8601 = "2023-01-05T00:00:00Z",
        name = "Station One",
        serveruuid = "server1",
        sslError = 0,
        state = "active",
        stationuuid = "station1",
        tags = "rock,pop",
        url = "https://example.com/stream1",
        urlResolved = "https://example.com/stream1.mp3",
        votes = 50
    )
    // Add more mock items as needed
)