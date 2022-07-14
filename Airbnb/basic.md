# 에어비엔비

- Naver Map API
- ViewPager2
- FrameLayout
- CoordinatorLayout
- BottomSheetBehavior
- Retrofit
- Glide
- Mock API

## Naver Map API
### Fragment
- Activity의 수명 주기를 그대로 가져간다.
- Fragment안에서 Fragment로 가져오지 않는다.

### MapView
- Fragment안에서 map을 가져올 때 사용한다.
- Activity의 수명 주기를 그대로 가져가지 못해서 별도로 등록해줘야한다.
- onCreate/onStart/onResume/onPause/onSaveInstanceState/onStop/onDestroy/onLowMemory

## CoordinatorLayout
- FrameLayout의 확장 개념
- 인터렉션이 많은 layout일 때 사용
### FrameLayout
- layout 중첩
- 제일 하단에 위치한 layout이 제일 상단에 위치

## ViewPager2
- 화면 전환 시 많이 사용.