Flo 클론 코딩

**2021.09 - 2021.12**

- 반응형 레이아웃 설계
    - Viewpager , Tablayout, Recyclerview, Collapsing Toolbar Layout 등 고급 위젯 사용
- 화면 전환 및 화면 간 데이터 전송
- 멀티 스레딩 작업 구현
    - Thread, Handler 사용하여 Seek Bar 이동, 음악 전환, Splash 화면 생성
    - MediaPlayer 사용하여 백그라운드 음악 재생
- 로컬 데이터 저장
    - SharedPreference, editor 사용하여 자동 로그인 및 현재 음악 저장
    - gson 통해 객체를 json으로 변환
    - Room 사용하여 로컬 데이터베이스 구축
        - 앨범 삽입, 삭제, 좋아요 반영, 노래 불러오기
        - 앨범, 노래 데이터 클래스 정의(Entity)
        - Dao 인터페이스, Database 클래스 생성 및 SQL 작성
- 서버와 데이터 연동
    - Retrofit2 라이브러리 사용
    - Service 인터페이스, Response 데이터 클래스 작성
    - 앨범, 노래 객체 클래스
    - 앨범 정보, 상세 앨범 정보 불러오기, 자동 로그인, 회원 가입, 로그인 구현
