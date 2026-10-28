---
name: pr
description: 현재 브랜치로 PR을 생성한다. "PR 올려줘", "PR 만들어줘" 같은 PR 생성 요청에 사용한다.
argument-hint: [이슈번호] [작업 요약 한 줄]
context: fork
---

현재 브랜치의 변경사항을 분석하고, PR 템플릿에 맞는 본문을 생성한 뒤 `gh pr create`로 PR을 올린다. 입력: `$ARGUMENTS`

이 스킬은 서브에이전트에서 실행되어 호출한 쪽의 대화 내용을 모른다.
이슈 번호와 작업 요약은 `$ARGUMENTS`, 브랜치 이름, 커밋, `.claude/plans`에서만 파악하고, 그 밖의 내용을 추측해서 채우지 않는다.

## 절차

### 1. 작업 상태 확인과 푸시

```bash
git branch --show-current
git log develop..HEAD --oneline
```

이번 작업은 **브랜치에 커밋된 내용**(`git log develop..HEAD`)이다. 커밋되지 않은 변경은 개발자가 의도적으로 남겨 둔 것(다음 작업, 로컬 설정 등)으로 보고 이번 작업에 넣지 않는다. 그런 변경이 있어도 PR을 중단하지 않고, 커밋·스테이징·stash 같은 작업 트리 조작도 하지 않는다. PR은 커밋만으로 만들어지므로 작업 트리 상태는 PR 내용에 영향을 주지 않는다.

다음 경우에만 PR을 만들지 않고 이유를 반환한 뒤 종료한다.

- 현재 브랜치가 `develop`이거나, `develop` 대비 커밋이 없음

원격에 푸시되지 않았으면 푸시한다. 업스트림이 없거나(`git rev-parse --abbrev-ref @{u}` 실패) 원격보다 앞선 커밋이 있으면(`git status -sb`의 `ahead`) 아래를 실행한다.

```bash
git push -u origin HEAD
```

푸시가 실패하면(권한, 충돌, 원격이 앞서 있음 등) 강제 푸시하지 말고 에러를 반환하고 종료한다.

### 2. 브랜치 및 이슈 파악

- 브랜치명에서 이슈 번호를 추출한다 (예: `feat/127` → `#127`, `fix/issue-42` → `#42`)
- 이슈 번호가 있으면 `gh issue view <번호>`로 제목, 본문, 라벨을 읽는다
- `$ARGUMENTS`에 이슈 번호가 있으면 브랜치명보다 우선한다
- `$ARGUMENTS`의 작업 요약 한 줄은 PR 제목과 Work Description의 출발점으로 쓴다

### 3. 변경사항 분석

```bash
git diff develop...HEAD --stat
git diff develop...HEAD
```

변경된 모듈과 주요 파일을 파악한다. 어떤 레이어(presentation / domain / data / core)가 변경됐는지 확인한다.

### 4. 계획서 참조

`.claude/plans/` 디렉토리에 현재 작업과 관련된 계획서가 있는지 확인한다.

- 있으면: 계획서의 **목적**, **완료 기준**, 체크리스트 완료 항목을 Work Description 작성에 반영한다
- 없으면: git diff와 커밋 메시지만으로 Work Description을 작성한다

Work Description은 **1~2문단** 수준으로 간략하게 작성한다. "무엇을 왜 변경했는가"에 집중한다. 변경 목록을 나열하는 대신 의도와 결과를 설명한다.

### 5. 라벨 결정

브랜치명 접두사와 이슈 라벨을 조합해 라벨을 하나 선택한다:

| 브랜치 접두사 | 라벨 |
|-------------|------|
| `feat/`, `feature/` | `feature` |
| `fix/` | `fix` |
| `hotfix/` | `hotfix` |
| `refactor/` | `refactoring` |
| `chore/` | `chore` |
| `design/` | `design` |
| `docs/` | `documentation` |
| `improve/`, `enhance/` | `enhancement` |

이슈 라벨이 위 목록에 있으면 그것을 우선한다.

### 6. 리뷰어 결정

변경된 레이어와 파일을 보고, 리뷰어를 1~2명 선택한다. 팀 협업자 목록:
`HamBP`, `leeeyubin`

PR 작성자 본인은 리뷰어에서 제외한다. 변경 범위가 넓으면 최대 2명까지만 지정한다.

### 7. To Reviewers 작성

리뷰어가 집중해서 봐야 할 지점을 2~4개 bullet로 작성한다. 예시:

- 어떤 패턴/아키텍처 결정을 검토해야 하는지
- 사이드이펙트가 있을 수 있는 변경점
- 테스트가 없어 수동 검증이 필요한 부분
- 의존성 변경이나 새로운 라이브러리 도입

### 8. PR 생성

아래 PR 템플릿 형식을 **정확히** 따른다:

```
## Issue
- closed #<이슈번호>

## Work Description
<변경 내용을 1~2문단으로>

## Screenshot
<!-- <img src="이미지 주소" width=270 /> -->

## To Reviewers
- <리뷰 포인트 1>
- <리뷰 포인트 2>
- ...
```

이슈 번호가 없으면 `## Issue` 섹션을 생략한다.
스크린샷은 UI 변경이 없으면 주석 상태 그대로 둔다.

```bash
gh pr create \
  --base develop \
  --title "<PR 제목>" \
  --body "$(cat <<'EOF'
<위 템플릿대로 작성한 본문>
EOF
)" \
  --label "<라벨>" \
  --reviewer "<리뷰어1>,<리뷰어2>"
```

PR 제목은 이슈 제목이 있으면 그것을 기반으로, 없으면 변경 내용을 요약해 작성한다. 한국어로 작성한다.

### 9. 결과 반환

PR이 만들어지면 아래 세 줄만 반환한다. 호출한 쪽은 이 결과를 그대로 사용자에게 전달한다.

```
PR: <PR URL>
라벨: <라벨>
리뷰어: <리뷰어1>, <리뷰어2>
```

중간에 종료했다면 위 형식 대신 종료 사유를 한두 줄로 반환한다.
