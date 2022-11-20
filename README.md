# Platter
## Brief introduction
The idea of this project was originally put forward by my tutor Li Binbin, and I was responsible for the specific code. It can realize that multiple devices can play a video at the same time, and each device presents a part of the picture, finally realizing the effect of splicing small screens into large screens.
## Technical realization
- Get the sizes of different mobile phones, take the smallest mobile phone as the video size, and use LAN communication to transmit video files.
- Using multithreading technology, one device can control the playing progress of other devices.
- Instead of cutting the original video, we use attribute animation to make the screen play only a part of the video.
## Future version upgrades
- Using WAN communication to realize video frame-by-frame transmission, the time-consuming file transfer operation is cancelled.
- Increase the performance of the program and the number of devices connected.
- Fix known bugs to make them support higher versions such as android12.

Source code address: https://github.com/Recycle1/Platter
