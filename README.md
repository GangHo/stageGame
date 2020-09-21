# stageGame
KU 컴퓨터응용프로그래밍2 - MVC모델을 활용한 프로젝트

----------------------------------

 MVC모델 적용
---------------
게임속에서 캐릭터,몹 등의 Entity(Model)마다의 data가 update되는 시점에 View를 갱신하는 Controller를 생성하면 MVC모델의 형태로 설계.

MVC모델의 목표는 각 요소를 분리하여 상호 의존성을 줄이기 위함에 있다.

캐릭터객체에 데이터 정보와 뷰 정보가 같이 들어가있어 Model의 데이터의 정보를 넘겨주는 Update()와 View를 그려주는 draw()와 이 둘을 스레드 속에서 프레임속도와 함께 Controll하는 GamePanel() 로 나누었다. 

<img src="https://user-images.githubusercontent.com/23518342/93781730-a7c73180-fc64-11ea-9b81-c30a22bd862f.png" width="100%" height="100%"></img>


Preview - W: 점프 F: 파이어볼 Ctrl: 찌르기
----------------
<img src="https://user-images.githubusercontent.com/23518342/93784449-fc1fe080-fc67-11ea-8d4a-f637bfdeade0.gif" width="50%" height="50%"></img>
