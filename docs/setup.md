# Interactive Floor System Setup Guide

## Hardware Requirements

### Camera Setup
- IR Camera with minimum 640x480 resolution
- Wide-angle lens (recommended: 120° field of view)
- Camera mounting height: 2.5-3.5 meters
- IR illumination source for consistent tracking

### Display System
- Projector (minimum 2000 ANSI lumens)
- Minimum resolution: 1024x768
- Mounting height matching camera setup

### Computer Requirements
- CPU: Intel i5 or equivalent (recommended)
- RAM: 8GB minimum
- Graphics: Integrated graphics sufficient
- OS: Windows 10/11, Linux (Ubuntu 20.04+)
- USB 3.0 port for camera

### Physical Space
- Minimum area: 3x3 meters
- Flat, light-colored surface
- Controlled lighting conditions

## Software Installation

### 1. Prerequisites
```bash
# Install JDK 17
sudo apt-get update                   # For Linux
sudo apt-get install openjdk-17-jdk
```

For Windows:
- Download and install [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Add JAVA_HOME to environment variables

### 2. Dependencies
- Processing 3.3.7
- Maven (for building)

### 3. Project Setup
```bash
# Clone the repository
git clone https://github.com/yourusername/interactive-floor.git
cd interactive-floor

# Build the project
mvn clean install
```

## Configuration

### 1. Camera Configuration
1. Run the test utility:
```bash
java -cp target/interactive-floor-1.0-SNAPSHOT.jar com.interactivefloor.test.BlobDetectionTest
```

2. Adjust camera settings:
- Use UP/DOWN arrows to adjust threshold
- Use LEFT/RIGHT arrows to adjust range
- Press 'B' to toggle binary view
- Recommended starting values:
  - Threshold: 200
  - Range: 20

### 2. System Configuration
Create `config.json` in the project root:
```json
{
  "camera": {
    "width": 640,
    "height": 480,
    "threshold": 200,
    "thresholdRange": 20
  },
  "display": {
    "fullscreen": true,
    "fps": 60
  }
}
```

## Testing

### 1. Running System Test
```bash
java -cp target/interactive-floor-1.0-SNAPSHOT.jar com.interactivefloor.test.SystemTest
```
Verify:
- Camera connection
- Frame rate
- Memory usage
- Display output

### 2. Blob Detection Test
```bash
java -cp target/interactive-floor-1.0-SNAPSHOT.jar com.interactivefloor.test.BlobDetectionTest
```
Verify:
- IR detection
- Blob tracking
- Threshold settings

### 3. Main Application
```bash
java -jar target/interactive-floor-1.0-SNAPSHOT.jar
```

## Controls
- 'D': Toggle debug mode
- 'M': Open animation menu
- 'S': Toggle sound
- '+/-': Adjust volume
- 'ESC': Exit

## Troubleshooting

### Common Issues

1. Camera Not Detected
```bash
# Linux: Check USB permissions
sudo usermod -a -G video $USER
```

2. Performance Issues
- Check camera frame rate
- Verify IR lighting
- Adjust blob detection thresholds

3. Tracking Problems
- Clean camera lens
- Adjust IR illumination
- Fine-tune threshold values

### System Checks
1. Verify camera functionality:
```bash
java -cp target/interactive-floor-1.0-SNAPSHOT.jar com.interactivefloor.test.SystemTest
```

2. Check tracking performance:
```bash
java -cp target/interactive-floor-1.0-SNAPSHOT.jar com.interactivefloor.test.BlobDetectionTest
```

## Maintenance

### Regular Checks
- Clean camera lens weekly
- Check IR illumination monthly
- Update threshold values as needed
- Monitor system performance

### Software Updates
- Check for Processing updates
- Update Java security patches
- Maintain current drivers

## Performance Optimization

### Camera Settings
- Set exposure manually
- Disable auto white balance
- Use maximum USB bandwidth

### System Settings
- Close unnecessary applications
- Set process priority to high
- Monitor CPU/memory usage

## Support
For technical support:
- Email: hakmesyo@gmail.com
- GitHub Issues: [Project Issues](https://github.com/yourusername/interactive-floor/issues)
