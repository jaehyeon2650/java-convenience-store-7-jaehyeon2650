# java-convenience-store-precourse

----

## 🛠️ 구현할 기능 목록

### View

#### Input

- [X] 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
- [ ] camp.nextstep.edu.missionutils.Console의 readLine()을 활용한다.
- [ ] 상품명 수량은 하이픈(-)으로, 각각 개별 상품은 대괄호([])로 묶어서 쉼표(,)로 구분한다.
- [ ] 프로모션 적용이 가능한 상품에서 손님이 해당 수량보다 적게 가져온 경우 그 수량만큼 추가적으로 받을 것인지 입력받는다.(Y,N)
- [ ] 프로모션 적용이 가능한 상품을 손님이 재고보다 더 많이 가져온 경우 나머지 일반 결재를 할 것인지 입력받는다.(Y,N)
- [ ] 맴버십 할인 적용 여부를 입력 받는다.(Y,N)
- [ ] 영수증 출력 후 추가 구매를 할 것인지 입력을 받는다.(Y,N)

#### Output

- [ ] 손님에 대한 인사말을 출력한다.
- [ ] 현재 재고 상태에 대해 출력한다.
- [ ] 재고가 없는 경우 상태에 "재고 없음"이라고 출력한다.
- [ ] 영수증을 간격을 맞춰서 출력한다.
    - [ ] 구매 상품 내역 : 구매한 상품명, 수량, 가격
    - [ ] 행사할인 : 프로모션에 의해 할인된 금액
    - [ ] 맴버십할인 : 맴버십에 의해 추가로 할인된 금액
    - [ ] 내실돈 : 최종 결제 금액

### 할인 정책

#### 프로모션 할인

- [ ] 현재 오늘 날짜가 포함된 경우만 할인을 적용한다.
    - [ ] camp.nextstep.edu.missionutils.DateTimes의 now()를 활용한다.
- [ ] 프로모션은 N개 구매시 1개 무료 증정 형태로 할인한다.
- [ ] 프로모션 혜택은 프로모션 재고 내에서만 적용한다.
- [ ] 프로모션 재고와 일반 재고가 있다면 프로모션 재고를 우선적으로 차감한다.
- [ ] 만일 프로모션 재고가 부족한 경우에는 일반재고를 사용한다.

#### 맴버십 할인

- [ ] 맴버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
- [ ] 프로모션 적용 후 남은 금액에 대해 맴버십 할인을 받는다.
- [ ] 최대 8000원까지 할인 받을 수 있다.

### 재고

- [ ] 각 상품의 결재 가능 여부를 확인한다.
- [ ] 구매한 수량 만큼 재고를 차감한다.
- [ ] 최신 상태를 정확히 유지한다.

----

## ❌ 예외 사항

- [ ] 사용자가 잘못된 값을 입력했을 때, "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.

- 매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
    - [ ] 상품명 수량은 하이픈(-)으로, 각각 개별 상품은 대괄호([])로 묶어서 쉼표(,)로 구분하지 않은 경우 예외가 발생한다.
    - [ ] 수량을 음수 혹은 0로 입력한 경우 예외가 발생한다.-> 양수만 입력 가능하게 한다.
- 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
    - [ ] 재고 목록에 있는 상품이 아닌 경우 예외가 발생한다.
- 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
    - [ ] 재고 목록에 있는 상품의 수량보다 큰 값을 입력한 경우 예외를 발생한다.
- 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

----

## 💪 프로그래밍 요구 사항

- indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
- 3항 연산자를 사용하지 않는다.
- 함수는 하나의 역할만 하도록 한다.
- 단위 테스트가 가능하도록 구현한다.
- else 예약어를 사용하지 않는다.(switch/case도 허용하지 않는다.)
- Enum 타입을 활용한다.
- 단위 테스트를 작성한다. 단, UI 관련은 제거한다.
- 함수의 길이가 10라인을 넘어가지 않도록 한다.
- 입출력을 담당하는 클래스를 분리한다.(InputView,OutputView)

----

## ⭐ 이번 주차 목표

- 공부한 "테스트 주도 개발" 책을 바탕으로 테스트 주도 개발(TDD)를 적용시키기
- 인터페이스 혹은 추상 클래스를 활용하기
- 공부한 함수형 인터페이스를 적용시켜보기
- 제어 하지 못하는 값(오늘의 날짜) 신경써서 단위테스트하기 쉽게 설계하기
- 3주차 피드백 적용
    - 도메인 흐름 정확히 파악 후 설계하기
    - 데이터 전송 객체 DTO에 비즈니스 로직 담지 않기
    - 공백도 코드 컨벤션이다! 일관적인 코드 작성하기
    - 함수형 인터페이스로 예외 처리 공통 로직 처리하기
    - 예외 사항을 자세히 기록하고 고민하기 -> 예외도 항상 테스트하기
    - 필드의 수 즉, 인스턴스 변수를 줄이기 위해 노력하기
