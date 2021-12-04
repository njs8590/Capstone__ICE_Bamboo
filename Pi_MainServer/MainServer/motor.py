import RPi.GPIO as GPIO          
from time import sleep
import time
import threading

def motor1():
    
    delay = 5
    close_time = time.time()+delay

    in1 = 23
    in2 = 24
    ena = 25
    

    #shake
    GPIO.setmode(GPIO.BCM)
    
    GPIO.setup(in1,GPIO.OUT)
    GPIO.setup(in2,GPIO.OUT)
    GPIO.setup(ena,GPIO.OUT)
    
    GPIO.setwarnings(False)
    
    GPIO.output(in1,GPIO.LOW)
    GPIO.output(in2,GPIO.LOW)
    pa=GPIO.PWM(ena,1000)
    pa.start(100)
    pa.ChangeDutyCycle(100)
    
    while(1):
        GPIO.output(in1,GPIO.HIGH)
        GPIO.output(in2,GPIO.LOW)
        sleep(0.1)
        pa.ChangeDutyCycle(50)
        sleep(1.25)
        GPIO.output(in1,GPIO.LOW)
        GPIO.output(in2,GPIO.HIGH)
        sleep(1.25)
        if time.time() > close_time :
            
            GPIO.output(in1,GPIO.LOW)
            GPIO.output(in2,GPIO.LOW)
            break

    GPIO.cleanup()

def motor2():
    
    delay = 5
    close_time = time.time()+delay
    
    in3 = 16
    in4 = 20
    enb = 21
    
    #mobile
    GPIO.setmode(GPIO.BCM)
    
    GPIO.setup(in3,GPIO.OUT)
    GPIO.setup(in4,GPIO.OUT)
    GPIO.setup(enb,GPIO.OUT)
    
    GPIO.setwarnings(False)
    
    GPIO.output(in3,GPIO.LOW)
    GPIO.output(in4,GPIO.LOW)
    pb=GPIO.PWM(enb,1000)
    pb.start(100)
    pb.ChangeDutyCycle(100)
    GPIO.output(in3,GPIO.HIGH)
    GPIO.output(in4,GPIO.LOW)
    sleep(0.1)
    pb.ChangeDutyCycle(30)
    
    while(1):
        if time.time() > close_time :
            GPIO.output(in3,GPIO.LOW)
            GPIO.output(in4,GPIO.LOW)
            break

    GPIO.cleanup()

    
    
    
#thread

#t= threading.Thread(target=motor2)
#t.start()
#motor1()    
    
#motor1(_
#GPIO.cleanup()

#motor2()
