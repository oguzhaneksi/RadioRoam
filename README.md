# RadioRoam

## Overview

RadioRoam is an advanced Android application designed for an exceptional radio streaming experience. It integrates Jetpack Media3 and MediaSession, offering robust media playback capabilities, including background play with `MediaSessionService`.

## Key Features

- **Advanced Media Playback**: Employs Jetpack Media3 and MediaSession for high-quality audio streaming.
- **Background Play**: Utilizes `MediaSessionService` for continuous playback, even when in the background.
- **MediaSession Integration**: Seamlessly manages media sessions, providing a cohesive user experience across different app states.
- **MediaController Support**: Offers detailed control over media playback, enhancing user interaction.
- **MediaBrowser Functionality**: Facilitates easy browsing and selection of radio stations.
- **Diverse Radio Stations**: Streams a wide variety of stations.
- **Efficient Data Management**: Uses Ktor for network operations and Kotlinx Serialization for data handling.
- **Modern User Interface**: Developed with Jetpack Compose for an intuitive UI.
- **Responsive Data Loading**: Implements a custom paging for smooth data presentation.
- **Asynchronous Operations**: Handled effectively with Kotlin Coroutines.
- **Optimized Image Processing**: Utilizes Coil for efficient image loading and caching.

## Technology Stack

- **Ktor**: Network communication for radio station data.
- **Koin**: Dependency injection.
- **Kotlinx Serialization**: JSON processing.
- **Jetpack Compose**: UI development.
- **Kotlin Coroutines**: Asynchronous task management.
- **Jetpack Media3**: Media playback integrated with MediaSession.
- **Coil**: Image loading with caching.

## Architecture

RadioRoam is built on the MVVM architecture, enhancing maintainability and testability. The application's media functionality is centered around Media3 and `MediaSessionService`, ensuring seamless media playback.

## Setup

1. **Clone the Repository**:
    ```
    git clone https://github.com/oguzhaneksi/RadioRoam.git
    ```

2. **Build and Run**:
    Open in Android Studio, build, and run on an emulator or a device.

## Contributing

We welcome contributions to RadioRoam. Please read our contributing guidelines for more information.

## License

Licensed under the [MIT License](LICENSE).
