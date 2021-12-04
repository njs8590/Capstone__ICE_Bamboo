from picamera import PiCamera
from time import sleep

picam = PiCamera()

picam.resolution = (320,240)
picam.start_preview()
while(True):
    sleep(3)
    picam.capture('image.jpg')
    picam.stop_preview()


