# 🐾keugeugeuk

“긁어서 확인하는” 재미를 주는 스크래치 카드 앱
Lottie 애니메이션을 활용해 긁기 완료 시 다양한 동물 캐릭터를 보여줍니다.

## 📂 프로젝트 구조
```
app/src/main/java/com/keugeugeuk/
  ui/                 # UI 레이어 (Activity, Fragment, View)
    scratch/
      ScratchFragment.kt
      ScratchActivity.kt
      ScratchView.kt
  data/               # 데이터 레이어 (Model, Repository)
    model/
      ScratchResult.kt
    repository/
      ScratchRepository.kt
  domain/             # 비즈니스 로직 (UseCase)
    GetScratchResultUseCase.kt
  util/               # 공통 유틸
    ScreenUtils.kt
  animation/          # Lottie JSON
    animals/
      dog.json
      cat.json
      pig.json
      bear.json
```

## 🚀 주요 기능
	•	스크래치 카드 긁기 UI
	•	긁기 완료 시 동물 애니메이션 재생 (Lottie)
	•	긁기 진행률 계산
	•	향후 AdMob 광고 연동 지원
 
## 🔧 빌드 & 실행
1.	저장소 클론
```bash
git clone https://github.com/goodplayer-kggk/keugeugeuk.git
```
2.	Android Studio에서 프로젝트 열기
3.	app 모듈 빌드 & 실행

## 📌 향후 계획
	•	Lottie 애니메이션 직접 제작
	•	다양한 동물 캐릭터 추가
	•	사용자 진행 상황 기록 & 통계
	•	광고 및 보상 시스템 연동






 
