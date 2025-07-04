# Kotlin Motion Animation Framework

An Android keyframe-based animation framework with an interactive timeline editor. Core animation component of the Katika project.

## Overview

This project provides a sophisticated animation system supporting multiple animation channels with keyframe interpolation. Features an interactive timeline UI for creating and editing complex multi-property animations.

## Technologies

- **Language**: Kotlin
- **Framework**: Android SDK
- **Build System**: Gradle
- **Minimum SDK**: 21

## Animation Channels

The framework supports 9 animation channel types:

| Channel | Description |
|---------|-------------|
| TranslateX | Horizontal movement |
| TranslateY | Vertical movement |
| Rotate | Rotation angle |
| ScaleX | Horizontal scaling |
| ScaleY | Vertical scaling |
| Data | Shape/geometry data |
| FillColor | Fill color |
| StrokeColor | Border color |
| Alpha | Opacity/visibility |

## Features

### Animation System
- Keyframe-based animation with linear interpolation
- Multiple channels per motion
- Configurable frame rate (default 50 fps)
- Scale and offset support
- Multi-motion blending

### Timeline Editor UI
- Interactive clip visualization
- Drag handles for timing adjustment
- Playhead scrubber for navigation
- Real-time frame/second display
- SeekBar control

## Implementation

### Motion Structure
```kotlin
class Motion {
    var channels: Array<Channel> = Array(9) { Channel(ChannelType.values()[it]) }
    var startFrame: Int = 0
    var duration: Int = 100
}
```

### Keyframe Interpolation
```kotlin
class Channel(val type: ChannelType) {
    var keyframes: MutableList<Keyframe> = mutableListOf()

    fun valueAtFrame(frame: Int): Float {
        // Linear interpolation between keyframes
    }
}
```

### Clip UI Component
```kotlin
class Clip(context: Context) : View(context) {
    var motion: Motion? = null
    var leftHandle: View
    var rightHandle: View
    // Touch handling for drag/resize
}
```

## Project Structure

```
kotlin_motions/
├── app/src/main/java/com/oddinstitute/motions/
│   ├── Motion.kt           # Core motion class
│   ├── Channel.kt          # Animation channel
│   ├── Keyframe.kt         # Single keyframe data
│   ├── ChannelType.kt      # Channel type enum
│   ├── Clip.kt             # Timeline clip UI
│   ├── MainActivity.kt     # Main UI controller
│   ├── DrawView.kt         # Canvas rendering
│   ├── MotionData.kt       # Motion-clip binding
│   ├── AppData.kt          # Sample data
│   └── Generic.kt          # Utility extensions
└── app/src/main/res/
    └── layout/activity_main.xml
```

## Requirements

- Android Studio
- Android 5.0+ (API 21)

## License

MIT License

![Animation Timeline](https://github.com/The-Odd-Institute/kotlin_motions/blob/main/motions.png)
