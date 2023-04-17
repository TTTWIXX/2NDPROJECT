# 체육시설 예약 프로그램


##  목차 
    1. 팀원소개
    2. 개발 일정
    3. 개발 배경
    4. 기존 통합 예약 시스텀
    5. 향상된 통합 예약 시스템
    6. 프로그램 다이어그램
    7. 개발 내용
    8. 한줄평
    9. 향후 업데이트 계획

### 1. 팀원 
    조장 : 조경훈
    팀원 : 이상욱
    팀원 : 구빛나
### 2. 개발 일정
    23.4.10 ~ 23.4.14

### 3. 개발 배경
    Programing: JAVA
    VCS : Git Hub        

### 4. 기존 통합 예약 시스텀
    복잡한 예약 시스템
    구 주민 할인 부재
    주차 쿠폰 발급, 대여 물품 기능 대여 부태

### 5. 향상된 부분
    종목별 물품 대여 & 주차 쿠폰 발행 기능 추가
    지역구 주민 & 학생 연계 할인
    요금 바로 확인!
    1인 2시간 제한으로 공정한 예약

### 6. 프로그램 구상도
<img src="D:\2ndproject\discussion\screenshot\스크린샷 2023-04-17 174749.png">

### 7. 개발 내용
    1. 회원가입
        
    


    ### 3-2-1 서울시의 구를 선택한다.
        1. 구에 따라 예약할수 있는 경기장이 보여진다.
            (3개로 통일 할지 구에 따른 예약 가능 경기장을 따로 설정할지 판단 필요)
        2. 만약 유저의 거주 지역이 해당 구일 경우 discount를 적용
            (구마다 다르게 할지 아니면 일괄되게 할지 판단필요 )
   
  - ## 3-3 지역을 선택 후 예약할수 있는 경기장을 보여진다.

      ### 3-3-1 선택된 지역에서 예약 가능한 축구장, 농구장, 수영장을 보여준다.    
          공통되는 특징은 super클래스로 만들어 상속하는게 좋아보인다.
          축구장, 농구장, 수영장만의 특징들을 오버라이딩하는게 좋아보인다.
          0. 공통 특징
              ex> 예약대상(대상이 학생이면 discount), 이용기간(), 장소, 취소기간, 이용요금, 문의전화, 예약완료 메세지창, 용도(교육/학습)..등등
        
          1. 축구장
              ex> 승패별 요금 discount
              ex> 의류 및 장비 대여   
              ex> 한사람이 중복 예약 못함, 다음 하루는 빌리지 못한다.(ex 11월 1일 광나루 축구장1, 축구장2 예약 → 예약불가)
        
          2. 농구장
              ex> 어린이,청소년 농구 교실로 이용할시 discount, 시간 추가
              ex> 의류 및 장비 대여
              ex> 주차 유무(차번호 입력하여 주차장 1시간 무료 쿠폰 증정)
        
          3. 수영장
              ex> 사워장 샤용 유무
              ex> gym 사용 유무
              ex> 제한 사항 : 여름 7 - 8 월, 시간 9 - 6 한정
              ex> 대여 물품(수영모, 수영복)
              ex> 예약 인원 : 가족 3명 이상 할인

      ### 3-3-2 예약 완료 후 예약된 시설의 정보를 보여준다.    
        


### 5. 향산된 예약 시스템

1. 경기장 종류: 3개로 통일 할지 구에 따른 예약 가능 경기장을 따로 설정할지 판단 필요
2. 할인율: 구마다 다르게 할지 아니면 일괄되게 할지 판단필요
3. 경기장별 고유 특징 정리
4. 서울시 구의 개수
   

### 향후 개발 ( 현재 하기 어렵거나 애매한 부분 )
1. 3종 경기장뿐 아니라 다른 복지 시설 까지 포함할것인가?
2. 관리자 계정으로 로그인시 예약 목록을 확인하고 승인할수 있는 기능
3. 로그인 시 예약된 화면을 띄워준다.
4. 시설 예약하기 전에 주의사항을 띄워준다.
5. 장애 여부를 파악하여 장애인만 전용 구장을 이용할수 있도록 한다.


### 어려웠던점
1. github 이용
2. 주제 선정, 아이디어 제시
3. 클래스 다이어그램 작성(클래스 및 변수 통일)


### 역할분담
1. 구빛나 : 프로젝트 아이디어 구상, 방향성 제시
2. 이상욱 : md 작성
3. 조경훈 : 클래스 벤다이어그램 작성, git 생성



## 참고사항!
1. https://yeyak.seoul.go.kr/web/main.do
2. https://yeyak.seoul.go.kr/web/search/selectPageListDetailSearchImg.do?code=T100
