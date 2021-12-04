# UserManual

최종 결과물은 요람과 안드로이드 앱 입니다.<br>
요람에는 총 2개의 라즈베리 파이와 1개의 아두이노가 사용되었습니다.

0. 전체적인 실행 순서<br>
	0.1 메인서버를 담당하는 파이1에서 server.py 파일을 실행합니다.<br>
	0.2 파이1에서 mjpg스크립트 파일을 이용해 실시간 영상스트리밍 서버를 실행합니다.<br>
	0.3 영유아 얼굴인식, 감정분석을 담당하는 파이2에서 API.py 를 실행하여 영유아의 상태 분석을 수행합니다.<br>
	0.4 전체적으로 실행되는 파일은 총 3개 입니다. 나머지는 자동으로 연동되는 소스들 입니다.<br>
	0.5 안드로이드 앱에서는 실시간 영상스트리밍 서비스와 알람서비스를 제공받고, 원한다면 UI 버튼을 통해 요람을 수동제어 할 수 있습니다.

1. 라즈베리파이1 - 메인서버, 하드웨어제어, 영유아의 상태판단<br>
	1.1 Pi_Mainserver/server.py <br>
	메인서버를 담당하는 소스코드 입니다.<br>
	안드로이드, 파이2와의 통신을 위한 소켓 통신 기반의 서버 입니다.<br>
	server_receive() 함수에서 소켓의 listening을 담당하고 있습니다.<br>
	doing() 함수에서 감정분석 결과값과 소켓 메세지의 값에 따라 어떤 동작을 수행할지를 결정합니다.<br>
	두 함수는 스레드 단위로 동시에 수행되고 있습니다.<br>
	do_with_input() 함수에서 하드웨어 동작을 수행하게 됩니다.<br>
	하드웨어의 동작에는 모터 제어와 스피커를 이용한 mp3 재생이 있습니다.<br>
	push_message() 함수를 이용해 안드로이드 앱으로 알람을 보낼 수 있습니다. 구글 FCM API를 사용했습니다.

	1.2 Pi_Mainserver/motor.py<br>
	모터 제어를 담당하는 소스코드 입니다.<br>
	GPIO 라이브러리를 이용하여 모터 제어를 수행합니다.<br>

	1.3 Pi_Mainserver/State_Change.py<br>
	파이2로부터 감정결과값을 받아 아이의 상태를 판단하는 소스코드 입니다.<br>
	Task_selection() 함수에서 영유아의 상태변화에 따라 가장 우선순위(Weight) 가 높은 하드웨어 동작을 선택합니다.<br>
	Weight() 함수에서 하드웨어 동작 이후에 계속되는 영유아의 상태변화 값에 따라 하드웨어 동작의 Weight 값이 조절됩니다.<br>
	Judge() 함수에서 전체적인 부분을 제어하고 아이가 잠에서 깨거나, 우는 등 돌발상황 시에는 알람을 보내게 됩니다.
	
	1.4 Pi_Mainserver/mjpg<br>
	mjpg-streamer 라이브러리를 이용하여 실시간 영상스트리밍 서비스를 제공합니다.

2. 라즈베리파이2 - 얼굴인식, 감정분석API, 아두이노<br>
	2.1 Pi_FaceRecog/API.py<br>
	Sockett() 함수에서 파이1로 영유아의 상태값 메시지를 전송할 수 있습니다.<br>
	cam() 함수에서 파이카메라 모듈을 이용해 얼굴인식을 수행합니다.<br>
	API() 함수에서 얼굴인식을 통해 저장된 이미지 파일을 http 통신으로 MS Azure 서비스에 접근하여 영유아 상태 분석값을 받을 수 있습니다.<br>
	arduino() 함수에서는 영유아 상태분석 요소의 일부인 MQ135 센서값을 저장하고 이를 이용합니다.<br>
	얼굴인식과 아두이노 함수는 스레드 단위로 동시에 수행됩니다.

3. 아두이노 - MQ135 센서
	3.1 Pi_FaceRecog/arduino/fart/fart.ino<br>
	아두이노 소스코드 입니다.<br>
	MQ-135 센서로부터 값을 입력받을 수 있습니다.

	3.2 Pi_FaceRecog/arduino/serialtest.py<br>
	센서로부터 입력받은 값을 분석하는 데 사용합니다.<br>
	연속적인 센서값을 저장하여 평균을 내고, 알람이나 영유아 상태분석에 이용할 수 있게 됩니다.<br>

4. 안드로이드 앱<br>
	4.1 Android/app/src/main/java/com/example/namjiseong/bamboo1/ControllerActivity.java<br>
	메인 컨트롤러 액티비티 입니다.<br>
	웹 기반의 실시간 영상 스트리밍 서비스를 제공받을 수 있습니다.<br>
	요람을 컨트롤하는 메인 서버를 담당하는 라즈베리파이로 소켓메시지를 전송하여 요람을 원격제어 할 수 있습니다.<br>
	사용자는 해당 컨트롤러를 쉽게 사용할 수 있도록 버튼식 UI 를 제공 받게 됩니다.<br>
	
	4.2 Capstone_ICE_Bamboo_/Android/app/src/main/java/com/example/namjiseong/bamboo1/MyFirebaseMessagingService.java<br>
	사용자는 라즈베리파이로부터 실시간 알람 서비스를 제공받을 수 있습니다.<br>
	앱이 실행중이지 않을 때도 백그라운드에서 알람 서비스를 받게 됩니다.<br>
	
	4.3 Capstone_ICE_Bamboo_/Android/app/src/main/res/layout/*.xml<br>
	안드로이드 앱의 뷰를 담당합니다.<br>
