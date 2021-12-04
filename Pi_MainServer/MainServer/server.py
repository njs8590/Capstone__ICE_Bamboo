#2019-05-12 jsnam 
#2019-05-21 wontae
import socket
import pygame
import random
import RPi.GPIO as GPIO         
import time 
from time import sleep
import motor
import State_Change
import os,sys
from pyfcm import FCMNotification
from threading import Thread, Lock
import threading

#from multiprocessing import Process
HOST = ""
PORT = 9000

doing = False

#server on
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print("Socket created")
server.bind((HOST, PORT))
print("Socket bind complete")
server.listen(1)
print("Socket now listening")

SoundTrack = [
	"music1.mp3",
	"music2.mp3",
	"music3.mp3",
	"music4.mp3",
	"music5.mp3",
]
rand_mp3 = random.choice(SoundTrack)

#record_sys

def record():
	print("start recording")
	cmd = "arecord -D plughw:1,0 -d 5 micTest.wav"
	os.system(cmd)

thr_record = threading.Thread(target=record)

#Sound
def MP3(mp3_file):
	
	#delay = 10
	#close_time = time.time()+delay
	if mp3_file == rand_mp3:
		mp3_file = random.choice(SoundTrack)
	pygame.mixer.init()
	pygame.mixer.music.load(mp3_file)
	#if time.time() < close_time :
	pygame.mixer.music.play()
	while pygame.mixer.music.get_busy() == True:
		continue
		
def wav():
	cmd = "python record_sound.py"
	os.system(cmd)

#Task

class Task :
	
	#t2 = threading.Thread(target=motor.motor1)
	def task1():
		thr_motor1 = threading.Thread(target=motor.motor1)
		thr_motor1.start()
		thr_mp3 = threading.Thread(target=MP3,args=(rand_mp3,))
		thr_mp3.start()
		thr_motor1.join()
		thr_mp3.join()
		
	def task2():
		thr_motor2 = threading.Thread(target=motor.motor2)
		thr_motor2.start()
		thr_mp3 = threading.Thread(target=MP3,args=(rand_mp3,))
		thr_mp3.start()
		thr_motor2.join()
		thr_mp3.join()
		
	def task3():
		thr_mp3 = threading.Thread(target=wav)
		thr_mp3.start()
		thr_mp3.join()
		
	def task4():
		thr_motor1 = threading.Thread(target=motor.motor1)
		thr_motor1.start()
		thr_motor2 = threading.Thread(target=motor.motor2)
		thr_motor2.start()
		thr_mp3 = threading.Thread(target=MP3,args=(rand_mp3,))
		thr_mp3.start()
		thr_motor1.join()
		thr_motor2.join()
		thr_mp3.join()

#Alarm
def push_message(message_title, message_body):
    push_service = FCMNotification(api_key="AAAAbvxqako:APA91bEfQcwyCtjwvDgkxS6w5ilhij2qJFm0QW-fGpu6eWXyx-li5ENgtIw4ULS2w9Z55jwEU_P6cBhFQRvaH6Hs-6Lw5bV6Fa8_VETZPNx17RbC9OvFWsBZOxcjw6Ftda9QDSHGLTmU")
    registration_ids = 'eTl6bv-xGhM:APA91bHePBBMw8-iCTuIFYPwrDPlunr3dL6Kd825imU7KhgVxBPIF0P-sziHkIVN6A2I4YiW0mNPCZHS3JIaEXZ2gqDq8RMPFd6AR_dmthsvzj68_FnbQkefzWVHsJvy0aACquDWDBHN'
    push_service.notify_single_device(registration_id=registration_ids, message_title = message_title, message_body=message_body)

#Modify this func for tasks
def do_with_input(input_string):
	#global doing
	#doing = True
	
	'''
	if input_string =="Sleeping":
		#Task.task1()
		push_message('Baby is Sleeping','Sleeping')
		reply = "Sleeping done"
	'''
	if input_string =="task1":
		Task.task1()
		reply = "task1 done"
		
	elif input_string =="task2":
		Task.task2()
		reply = "task2 done"
		
	elif input_string =="task3":
		Task.task3()
		reply = "task3 done"
	
	elif input_string =="task4":
		Task.task4()
		reply = "task4 done"
	
	elif input_string =="record":
		record()
		reply = "recording.."
		
	else :	
		reply = "No command"

	return reply

task_list = ['task0', 'task1', 'task2', 'task3', 'task4', 'task5', 'record']
state_list = ['Normal', 'Sleeping', 'Crying', 'Smile']
'''
while True:
	
	client, addr = server.accept()
	print("Connected by ",addr)
	
	data = client.recv(1024)
	data = data.decode("utf8").strip()
	
	print("Received : " +data)
	
	if data in task:
		#print('task')
		if doing == False:
			res = do_with_input(data)
			doing = False
	else:
		tasknum = State_Change.State_change(data)
		tasknum = 'task'+str(tasknum)

		if doing == False:
			res = do_with_input(tasknum)
			doing = False
	
	print("command :: "+res)
	
	#if not data: break
	
	client.sendall(res.encode("utf-8"))
	client.close()
'''

receive = []

def server_receive():
	global server

	while True:
			client, addr = server.accept()
			print("Connected by ",addr)

			data = client.recv(1024)
			data = data.decode("utf8").strip()
			res =""
			if(data=='record'):
				del receive[:]
				res = do_with_input(data)

			else:
				print(data)
				receive.append(data)

			client.sendall(res.encode("utf-8"))
			client.close()

	server.close()

	
def doing():
	
	while True:
			if len(receive) != 0:
				command = receive[-1]
				if command in task_list:
					do_with_input(command)
					del receive[:]
					
				elif command in state_list:
					tasknum = State_Change.State_change(command) + 1
					tasknum = 'task'+str(tasknum)
					do_some_task = State_Change.do_some_task

					if do_some_task == True:
						print(tasknum)
						do_with_input(tasknum)
						del receive[:]
				else :
					print("No comamnd in Task")
			sleep(1)

#__init__

receive_thread = threading.Thread(target=server_receive)
doing = threading.Thread(target=doing)

receive_thread.start()
doing.start()
