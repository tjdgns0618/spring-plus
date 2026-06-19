# SPRING PLUS
# 1. 쿼리 메서드를 사용한 조회
[쿼리 메서드 코드]

<img width="423" height="27" alt="Image" src="https://github.com/user-attachments/assets/4b31b35e-9cb8-4f91-8075-528b574ec4d6" />

[첫 번째 조회 결과]

<img width="599" height="357" alt="Image" src="https://github.com/user-attachments/assets/a6989d29-dfd6-4df5-a6b7-7c33e841150b" />

첫 조회 속도는 795ms가 걸렸습니다.

# 2. JPQL을 사용한 조회
[JPQL 코드]

<img width="525" height="55" alt="Image" src="https://github.com/user-attachments/assets/a0ffb02c-f8b8-44a6-88e6-4cf7b186d58a" />

[첫 번째 조회 결과]

<img width="599" height="359" alt="Image" src="https://github.com/user-attachments/assets/ae3c45d9-338b-45de-906f-8c7050925543" />

첫 조회 속도는 752ms가 걸렸습니다.

- 두 조회 방식은 비슷한 속도가 나온다는 것을 알았습니다.

# nickname 컬럼에 인덱스를 생성해서 조회하기
[nickname 컬럼에 인덱스를 생성 코드]

<img width="703" height="88" alt="Image" src="https://github.com/user-attachments/assets/e4a9c878-bbeb-4f35-9688-fb18bf87ad6d" />

users 테이블의 nickname 컬럼에 인덱스를 생성해주었습니다.

## 쿼리 메서드를 사용한 조회
<img width="600" height="349" alt="Image" src="https://github.com/user-attachments/assets/6ae6b0ea-c78b-4c81-a81a-4f911e75a72f" />

조회 속도가 306ms로 2배 이상 빨라졌습니다.

## JPQL을 사용한 조회
<img width="601" height="360" alt="Image" src="https://github.com/user-attachments/assets/96b7c39c-a05c-4c53-b0e2-8f6da3f140c4" />

조회 속도가 322ms로 2배 이상 빨라졌고 쿼리 메서드와 비슷한 비율로 개선이 되는 것으로 두 방식의 성능은 비슷하다는 것을 알았습니다.

# 트러블 슈팅
```
상황에 따라서 첫 조회가 1800~2000이 나올때가 있었습니다.

다른 앱들의 CPU나 메모리의 사용량이 높아(이미 90퍼 가까지 사용 중) IntelliJ가 사용할 CPU/메모리를 빼앗겨서 쿼리가 느려져서 조회가 느려진 일이 있었습니다.

다른 앱들의 사용량을 줄여서 이를 해결했습니다.
```