![header](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=18&height=300&section=header&text=PETFAM&fontSize=90)

### [프론트 깃허브 주소](https://github.com/Dripmaster44/petfam_repo.git)

***

## 프로젝트 프리뷰

<details>

<summary> 프로젝트 프리뷰 </summary>
<figure>
  <img src="readme_asset/01.png" alt="메인" width="auto"/>
  <figcaption style="text-align: center">메인 페이지</figcaption>
</figure>
<figure>
  <img src="readme_asset/02.png" alt="전체 게시글 리스트(카테고리별)" width="auto"/>
  <figcaption style="text-align:center">전체 게시글 리스트(카테고리별)</figcaption>
</figure>
<figure>
  <img src="readme_asset/03.png" alt="상세 게시글(본문)" width="auto"/>
  <figcaption style="text-align: center">상세 게시글(본문)</figcaption>
</figure>
<figure>
  <img src="readme_asset/04.png" alt="상세 게시글(댓글)" width="auto"/>
  <figcaption style="text-align: center">상세 게시글(댓글)</figcaption>
</figure>
<figure>
  <img src="readme_asset/05.png" alt="글 작성 페이지" width="auto"/>
  <figcaption style="text-align: center">글 작성 페이지</figcaption>
</figure>
<figure>
  <img src="readme_asset/06.png" alt="로그인" width="auto"/>
  <figcaption style="text-align: center">로그인</figcaption>
</figure>
<figure>
  <img src="readme_asset/10.png" alt="회원가입" width="auto"/>
  <figcaption style="text-align: center">회원가입</figcaption>
</figure>
<figure>
  <img src="readme_asset/07.png" alt="메인 헤더(관리자용)" width="auto"/>
  <figcaption style="text-align: center">메인 헤더(관리자용)</figcaption>
</figure>
<figure>
  <img src="readme_asset/08.png" alt="전체유저 조회(관리자용)" width="auto"/>
  <figcaption style="text-align: center">전체유저 조회(관리자용)</figcaption>
</figure>
<figure>
  <img src="readme_asset/09.png" alt="유저 프로필 정보" width="auto"/>
  <figcaption style="text-align: center">유저 프로필 정보</figcaption>
</figure>
</details>

## 프로젝트 환경

### *Back-end*

<div>
<img src="https://img.shields.io/badge/SpringBoot v3.0.2-6DB33F?style=plastic&logo=SpringBoot&logoColor=white"/>
<img src="https://img.shields.io/badge/JDK 17-1E8CBE?style=plastic&logo=Conda-Forge&logoColor=white"/>
</div>
<div>
<img src="https://img.shields.io/badge/Redis-DC382D?style=plastic&logo=Redis&logoColor=white"/>
<img src="https://img.shields.io/badge/JUnit5-25A162?style=plastic&logo=JUnit5&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=plastic&logo=MySQL&logoColor=white"/>
</div>

### *Front-end*

<div>
<img src="https://img.shields.io/badge/HTML5-E34F26?style=plastic&logo=HTML5&logoColor=white"/>
<img src="https://img.shields.io/badge/CSS-1572B6?style=plastic&logo=CSS3&logoColor=white"/>
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=plastic&logo=JavaScript&logoColor=white"/>
</div>

### *Tools*

<div>
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=plastic&logo=IntelliJ IDEA&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717?style=plastic&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/VSCode-007ACC?style=plastic&logo=VisualStudioCode&logoColor=white"/>
<img src="https://img.shields.io/badge/EC2-FF9900?style=plastic&logo=Amazon EC2&logoColor=white"/>
<img src="https://img.shields.io/badge/S3-569A31?style=plastic&logo=Amazon S3&logoColor=white"/>
<img src="https://img.shields.io/badge/CodeDeploy-232F3E?style=plastic&logo=Amazon AWS&logoColor=white"/>
</div>

***

## 1. 팀 소개

* 리더 :
  김갑신[![GitHub](https://img.shields.io/badge/GitHub-181717?style=plastic&logo=GitHub&logoColor=white)](https://github.com/kapsin2)
* 부리더 :
  조은총[![GitHub](https://img.shields.io/badge/GitHub-181717?style=plastic&logo=GitHub&logoColor=white)](https://github.com/goodisgun)
* 팀원 :
  안해리 [![GitHub](https://img.shields.io/badge/GitHub-181717?style=plastic&logo=GitHub&logoColor=white)](https://github.com/ahnhadi),
  진용재[![GitHub](https://img.shields.io/badge/GitHub-181717?style=plastic&logo=GitHub&logoColor=white)](https://github.com/Dripmaster44)

## 2. 프로젝트 ERD

![Untitled (5)](https://user-images.githubusercontent.com/117059820/220878918-8df19b2d-c035-4c43-9c43-4e8672488a26.png)

## 3. 서비스 아키텍쳐

![ServiceArchitecture](https://user-images.githubusercontent.com/117059820/220859677-82fdf10e-2856-4684-ad13-41277a3bd82f.png)

## 4. 시연 영상

[유튜브 시연영상 링크](https://youtu.be/mbqhGZlpI0U)

## 5. MVP 목표

### 로그인/ 회원가입

- [x] JWT, Refresh 토큰 활용
- [x] 관리자, 유저 나누기
- [ ] 소셜 로그인 구현(카카오, 네이버, 구글)

### 메인 페이지

- [x] 최상단 이달/이주의 베스트 좋아요 반려동물 나오게하기
- [x] 최신순/인기순으로 나오게 하기
- [ ] 아래에 페이징된 반려동물 사진+이름+좋아요 갯수 나오게 하기 -> 클릭 시 상세 페이지로 이동

### 보조 페이지

- [x] 반려동물 관련 질문 페이지
- [x] 잡담 페이지
- [ ] 게시글의 조회수 불러오기(구현중)

### 1:1 채팅

- [x] DB 활용해서 구현
- [ ] 웹소켓 활용해서 구현

### 게시글 검색

- [ ] 해시태그기능(키워드) #냥스타그램🐱

![footer](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=18&height=250&section=footer&text=Thank%20You!)








