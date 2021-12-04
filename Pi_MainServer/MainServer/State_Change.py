#import sys
import numpy as np

#Task = sys.modules[__name__]

Cur_State = 'Start'
Next_State = 'Normal'
strtemp = Cur_State + Next_State
sub_strtemp = strtemp

State_num = 6
Num_of_Task = 4

weight = np.zeros((State_num,Num_of_Task))

'''
for i in range(Num_of_Task):
    t = setattr(Task, 'Task{}'.format(i+1), weight[:,i])
'''
#Alarm
def push_message(message_title, message_body):
    push_service = FCMNotification(api_key="AAAAbvxqako:APA91bEfQcwyCtjwvDgkxS6w5ilhij2qJFm0QW-fGpu6eWXyx-li5ENgtIw4ULS2w9Z55jwEU_P6cBhFQRvaH6Hs-6Lw5bV6Fa8_VETZPNx17RbC9OvFWsBZOxcjw6Ftda9QDSHGLTmU")
    registration_ids = 'eTl6bv-xGhM:APA91bHePBBMw8-iCTuIFYPwrDPlunr3dL6Kd825imU7KhgVxBPIF0P-sziHkIVN6A2I4YiW0mNPCZHS3JIaEXZ2gqDq8RMPFd6AR_dmthsvzj68_FnbQkefzWVHsJvy0aACquDWDBHN'
    push_service.notify_single_device(registration_id=registration_ids, message_title = message_title, message_body=message_body)

#TaskList = ['Task1', 'Task2', 'Task3', 'Task4']

hashmap = {
    #'SleepingNormal' : ,
    'CryingSmile' : weight[0],
    'NormalSmile' : weight[1],
    'SleepingSmile' : weight[2],
    'SleepingCrying' : weight[3],
    #'NormalSleeping' : ,
    #'NormalSmile': ,
    'NormalCrying': weight[4],
    'SmileCrying' : weight[5],
    #'SmileNormal' : 
}

same_state = False
do_some_task = False

task_num = 0

def Weight(do,strtemp,task_num):
    
    Weight = hashmap[strtemp][task_num]
    
    if(do=='plus'):
        hashmap[strtemp][task_num] = Weight +1
        
    elif(do=='minus'):
        hashmap[strtemp][task_num] = Weight-1

def Task_selection(strtemp):
    global weight
    global task_num
    global do_some_task
    global sub_strtemp
    
    sub_strtemp = strtemp
    
    if do_some_task == True:
        new_hashmap = np.ma.array(hashmap[strtemp], mask=False)
        new_hashmap.mask[task_num] = True
        task_num = np.argmax(new_hashmap)
        return sub_strtemp, task_num   
    else:
        task_num = np.argmax(hashmap[strtemp])
        do_some_task = True
        return sub_strtemp, do_some_task,task_num

    
def Judge():
    global same_state
    global do_some_task
    global strtemp
    global task_num

    
    if Next_State == 'Crying':
        if same_state == True:
            Weight('minus',strtemp,task_num)
            Task_selection(strtemp)
            
        else :
            push_message('Baby is Crying','Crying')
            print("Alarm")
            Task_selection(strtemp)
        
    elif (do_some_task == True)&(Next_State == 'Smile'):
        Weight('plus',strtemp,task_num)
        
    if(Cur_State == 'Crying')&(same_state == False):
        do_some_task = False
        Weight('plus',sub_strtemp,task_num)
        
    print(hashmap)
    
def State_change(State):
    
    global Next_State
    global Cur_State
    global same_state
    global strtemp
    global task_num
    
    if(Next_State == State) :
        same_state = True
    else :
        Cur_State = Next_State
        Next_State = State
        strtemp = Cur_State + Next_State
        same_state = False   
        
    Judge()
    
    return task_num
