
## 카카오 과제 사용법 & trouble shooting

### 사용법
![Image](https://github.com/KOOSANGYOON/ToDoList/blob/master/todo.png?raw=true)

---
### trouble shooting

1. ToDo - ToDo 관계 매핑문제 발생

  - 기존에 ToDo 내에 List<ToDo> referingToDos / List<ToDo> referedToDos 를 두고 각 리스트들을
  @OneToMany로 설정하고 세팅했지만, 테이블에 제대로 들어가지 않음.

  - 로직 상에서는 모든 테스트가 통과하는 것을 확인했지만, DB에 들어갈 때, 값이 중복되서 들어감.

  - 객체로 따로 빼서 테이블을 생성하고, @ManyToMany 로 관계를 설정하여 두 객체사이를 연관지었다.

2. A -> B 일때, B -> A로 설정하려고 하면 에러가 나와야한다. (참조를 설정할 때에,)

  - OneToMany 관계로 양 측을 구성하다보니 생기는 예외

  - 추가적으로 A -> B , B -> C , C -> D , D -> A 와 같이 chain형식으로 참조가 이어지게 되더라도
  에러가 나와야한다.

  > 참조시에 참조를 거는 쪽에서, 참조 걸 당시에 미리 걸린 참조들의 리스트를 참조가 걸리는 쪽 ToDo로 보낸다.
  >
  > 예를 들어, A -> B 로 참조가 걸려있고, B -> C 로 참조를 걸려고 할 때, B의 상위조건인 A를 미리 C로 보내줘서
  >
  > C의 선행조건에 A를 넣어준다.

3. service 단에서 ToDo참조를 걸 때, A registring B / B registered A 를 private로 만들어 두고 계속 사용.

  - refering 시에 이미 유효성 체크를 완료하고, refered 시에는 같은 refered ToDo가 있는지 정도의 유효성만 체크해도
  문제없게 구성.

4. FETCH_TYPE=EAGER / LAZY 차이로 인한 test case error.

  - FETCH_TYPE에 대해서 제대로 알지 못하고 사용했던 것을 발견. 대략적인 개념을 알았지만, 개발시에 신경을 쓰지 않음.
  - type을 eager로 바꿔주면서 성능은 떨어질 지 몰라도, 테스트 로직을 모두 만족함.

5. front-end 적용 문제

  - back-end의 테스트 케이스는 통과했지만, (로직은 구성됬지만,) View단에서 연결되지 않아서 가시적인 테스트가 불가했고,
  결국 명확하게 완성하지 못한 결과 발생.
  - window.location.reload() 를 사용했다. -> ajax를 사용하는 의미가 없어짐..
