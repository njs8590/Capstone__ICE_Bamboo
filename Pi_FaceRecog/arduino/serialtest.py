from serial import Serial
from pyfcm import FCMNotification

ser = Serial("/dev/ttyACM0",9600)

list_b = []
check_avg = False

def push_message(message_title, message_body):
    push_service = FCMNotification(api_key="AAAAbvxqako:APA91bEfQcwyCtjwvDgkxS6w5ilhij2qJFm0QW-fGpu6eWXyx-li5ENgtIw4ULS2w9Z55jwEU_P6cBhFQRvaH6Hs-6Lw5bV6Fa8_VETZPNx17RbC9OvFWsBZOxcjw6Ftda9QDSHGLTmU")
    registration_ids = 'eTl6bv-xGhM:APA91bHePBBMw8-iCTuIFYPwrDPlunr3dL6Kd825imU7KhgVxBPIF0P-sziHkIVN6A2I4YiW0mNPCZHS3JIaEXZ2gqDq8RMPFd6AR_dmthsvzj68_FnbQkefzWVHsJvy0aACquDWDBHN'
    push_service.notify_single_device(registration_id=registration_ids, message_title = message_title, message_body=message_body)

def avg(list_b):
    avg = sum(list_b)/len(list_b)
    
    return avg
    
def bytes_to_int(byt):
    result = 0
    for b in byt:
        result = result + int(b)
        
    return result
    
while(1):
    
    try:
        a = ser.readline()
        b=bytes_to_int(a)
        
        list_b.append(b)
        print(list_b)
        
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
