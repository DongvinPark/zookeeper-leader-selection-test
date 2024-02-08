# Apache ZooKeeper Simple Leader Selection Test

<br><br/>
- 1 대의 머신으로만 구성된 zookeeper 클러스터를 셋팅하고 스프링부트 컨테이너들을 이용해서 주키퍼의 리더 선출 동작을 확인한다.

<br><br/>
- 두 대의 물리 컴퓨터를 준비하고, 둘 다 동일한 wifi 라우터에 접속 시킨다.
  <br>두 대의 물리 컴퓨터가 없다면, EC2를 만들고 거기에서 주키퍼를 실행하면 된다.
  <br>한 대는 ubuntu 리눅스로 준비하여 이곳에서 주키퍼를 실행시킬 예정이고,
  <br>나머지 한 대는 본 리포지토리의 스프링부트를 실행하여 리더 선출 후보 컨테이너 노드들을 실행시킨다.
  <br>카프카가 실행되는 우분투 내부에서 ifconfig 명령어를 쳐서 해당 우분투의 public IP를 기억해 놓는다.
  <br>이 IP 주소는 나중에 application.yml 내의 zookeeper connectionString을 설정할 때,
  <br>zookeeper-IP-Addr:2181 이렇게 설정하게 된다.

<br><br/>
- 본 스프링부트 프로젝트의 build.gradle 파일에서 반드시 임포트 해야 하는 라이브러리가 2 개 있다.
  <br>implementation 'org.apache.curator:curator-framework:5.1.0'
  <br>implementation 'org.apache.curator:curator-recipes:5.6.0'
  <br>이렇다. 이 두 가지를 포함하고 gradle을 빌드해야 정상 작동한다.

<br><br/>
- application.yml 파일에서 스프링부트의 포트 번호를 8080, 8081, 8082로 바꿔 가면서 도커 이미지를 빌드해준다.
  <br>이렇게 하면 하나의 물리 컴퓨터에서 마치 3 개의 별개 스프링부트 프로젝트가 돌아가는 것처럼 만들 수 있다.

<br><br/>
- 주키퍼를 실행할 리눅스 머신에서 /home/{your user name} 디렉토리에 zookeeper라는 디렉토리를 만들고,
  <br>그 디렉토리로 이동한 후, sudo wget {주키퍼 다운로드 https url} 명령어를 실행한다.
  <br>그러면 주키퍼 바이너리 파일이 입축된 형태로 다운로드 된다.
  <br>주키퍼 다운로드 https url은 https://zookeeper.apache.org/releases.html 여기에서
  <br>latest stable release 부분 아래에 있는 Apache Zookeeper 3.X.X(asc, sha512)
  <br>이 부분을 클릭했을 때 리다이렉트 되는 사이트에서
  <br>가장 상단에 존재하는 We suggess the following ... 이라는 문구 바로 아래의 url을 복사 붙여넣기 하면 된다.
  <br>그 후, sudo tar -xzf {아까 다운로드 받은 주키퍼 바이너리 압축파일 이름} 명령어를 실행해서 압축을 해제한다.

<br><br/>
- 압축을 해제하고 나면, 주키퍼 디렉토리가 생긴다. 그 안에서 다시 conf 디렉토리로 이동한다.
  <br>그 후, conf 디렉토리 내에 존재하는 zoo_sample.cfg의 내용을 그대로 복사해 둔다.
  <br>conf 디렉토리 내부에서 zoo.cfg라는 파일을 만들고, 그 안에 아까 복사한 내용을 그대로 붙여넣기 한다.
  <br>그 후, conf 디렉토리와 동일한 디렉토리에 속해 있는 bin 디렉토리로 이동해서
  <br>./zkServer.sh start 라는 명령어를 실행하면 주키퍼가 실행된다.

<br><br/>
- 이제 스프링부트 도커 이미지 3개를 각각 새로운 터미널 창에서 컨테이너로 실행하고, 로그를 관찰한다.
  <br>그러다 보면, 3개의 컨테이너 중 가장 먼저 실행된 것이 리더로 선출될 것이다.
  <br>그 컨테이너를 강제 종료 시키면, 약 20~30초 후에 두 번째로 실행되었던 스프링부트 컨테이너가 자동으로 리더로 선출되는 것을 볼 수 있다.