import timeit
import http.client
import urllib.request
import urllib.parse
import urllib.error
import base64
import requests
import json 
import threading
from threading import Thread, Lock
from picamera import PiCamera
from time import sleep
from socket import *
from serial import Serial
from pyfcm import FCMNotification
###################################################
ser = Serial("/dev/ttyACM0",9600)

list_b = []
check_avg = False

def push_message(message_title, message_body):
    push_service = FCMNotification(api_key="secret")
    registration_ids = 'secret'
    push_service.notify_single_device(registration_id=registration_ids, message_title = message_title, message_body=message_body)

def avg(list_b):
    avg = sum(list_b)/len(list_b)
    
    return avg
    
def bytes_to_int(byt):
    result = 0
    for b in byt:
        result = result + int(b)
        
    return result
###################################################

'''EmotionList = [
    'surprise',
    'anger',
    'fear',
    'happiness',
    'neutral',
    'disgust',
    'contempt',
    'sadness'
]

StateList = [
    'Normal',
    'Sleeping',
    'Crying',
    'Smile'
]
'''

CryingEmotion = [
    'anger', 
    'fear',
    'disgust', 
    'contempt',
    'sadness'    
]        

CurrentEmotion = ['Start']
CurrentState = ['Start']

subscription_key = 'secret'
uri_base = 'https://koreacentral.api.cognitive.microsoft.com'

# Request headers.
headers = {
    'Content-Type': 'application/octet-stream',
    'Ocp-Apim-Subscription-Key': subscription_key,
}

# Request parameters.
params = {
    # 'returnFaceId': 'true',
    # 'returnFaceLandmarks': 'false',
    # 'returnFaceAttributes': 'age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise',
    'returnFaceAttributes': 'emotion'
}

def Sockett(task):
    clientSock = socket(AF_INET, SOCK_STREAM)
    clientSock.connect(('192.168.43.39', 9000))
    
    #print('Check of Connect')
    clientSock.send(task.encode('utf-8'))

    #print('transpose of message')

    #data = clientSock.recv(1024)
    #print('receive data : ', data.decode('utf-8'))

def API(filename):
    
    f = open(filename, "rb")
    body = f.read()
    f.close()
    
    # Body. The URL of a JPEG image to analyze.
    body = body

    try:
        # Execute the REST API call and get the response.
        response = requests.request('POST', uri_base + '/face/v1.0/detect', data=body, headers=headers,
                                params=params)
        parsed = json.loads(response.text)
        
        # print(parsed)
        emotion = parsed[0]['faceAttributes']["emotion"]
        print(emotion)
        main_emotion = max(emotion, key=emotion.__getitem__)
        print('main_emotion= ', main_emotion)

        #State function
        State(main_emotion)

    except Exception as e:
        print('Error:')
        print(e)
        
def State(Emotion):
        
    if (Emotion == 'happiness') & (CurrentEmotion[-1] == 'happiness') :
        CurrentState.pop(0)
        CurrentState.append('Smile')

    elif (Emotion in CryingEmotion) & (CurrentEmotion[-1] in CryingEmotion) :
        CurrentState.pop(0)
        CurrentState.append('Crying')

    elif (Emotion == 'neutral') & (CurrentEmotion[-1] == 'neutral') :
        CurrentState.pop(0)
        CurrentState.append('Normal')    

    CurrentEmotion.append(Emotion)    

    if CurrentState[0] == 'Normal':
        sleep_check = True
        for emot in CurrentEmotion[-6:-1]:
            if emot != 'neutral':
                sleep_check = False
        if sleep_check ==True :
            CurrentState.pop(0)
            CurrentState.append('Sleeping')
            
    Sockett(CurrentState[0])
    print(CurrentState)
    
##########################################################################
def cam():
    picam = PiCamera()

    cap = cv2.VideoCapture(0)
    cap.set(3, 640)
    cap.set(4, 480) 

    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_alt.xml') 

    i = 0
    start = timeit.default_timer() 

    while(True):
    
        end = timeit.default_timer()
        ret, frame = cap.read()
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)    

        faces = face_cascade.detectMultiScale(gray, 1.3, 5) 

        for (x,y,w,h) in faces:
        
            cropped = frame[y - int(h/4):y + h + int(h/4), x - int(w/4):x + w + int(w/4)]
        
            if(end - start >= 3):
                cv2.imwrite(str(i) + '.jpg', cropped , params=[cv2.IMWRITE_PNG_COMPRESSION,0])
                filename = str(i) + '.jpg'
            
                API(filename)
                
def arduino():
    while(True):    
        try:
            a = ser.readline()
            b=bytes_to_int(a)
            
            list_b.append(b)
            Sockett(b)
            if avg(list_b) > 300 :
                if check_avg == False:
                    print('Activate')
                    push_message('Come','3169')
                    check_avg = True
            else :
                check_avg = False
                
            if len(list_b)==10 :            
                list_b.pop(0)   
                
        except Serial.SerialException:
            print("...")

#__init__

cam_thread = threading.Thread(target=cam)
arduino_thread = threading.Thread(target=arduino)

cam_thread.start()
arduino_thread.start()
