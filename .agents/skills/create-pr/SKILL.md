---
name: create-pr
description: PR을 생성한다. "PR 올려줘", "PR 업로드 해줘", "PR 생성해줘", "PR 만들어줘", "/pr", "풀 리퀘스트" 등 PR 생성이 필요한 모든 상황에서 반드시 이 스킬을 먼저 실행해라. 브랜치 변경사항과 .Codex/plans 계획서를 분석해 PR 본문을 자동 작성하고, 이슈 연결·라벨·리뷰어 지정까지 한 번에 처리한다.
argument-hint: [이슈번호 또는 PR 제목]
---

현재 브랜치의 변경사항을 분석하고, PR 템플릿에 맞는 본문을 생성한 뒤 `gh pr create`로 PR을 올린다.

## 절차

### 1. 브랜치 및 이슈 파악

```bash
git branch --show-current
git log develop..HEAD --oneline
```

- 브랜치명에서 이슈 번호를 추출한다 (예: `feat/127` → `#127`, `fix/issue-42` → `#42`)
- 이슈 번호가 있으면 `gh issue view <번호>`로 제목, 본문, 라벨을 읽는다
- 이슈 번호가 없으면 `$ARGUMENTS` 또는 대화 컨텍스트에서 파악한다

### 2. 변경사항 분석

```bash
git diff develop...HEAD --stat
git diff develop...HEAD
```

변경된 모듈과 주요 파일을 파악한다. 어떤 레이어(presentation / domain / data / core)가 변경됐는지 확인한다.

### 3. 계획서 참조

`.Codex/plans/` 디렉토리에 현재 작업과 관련된 계획서가 있는지 확인한다.

- 있으면: 계획서의 **목적**, **완료 기준**, 체크리스트 완료 항목을 Work Description 작성에 반영한다
- 없으면: git diff와 커밋 메시지만으로 Work Description을 작성한다

Work Description은 **1~2문단** 수준으로 간략하게 작성한다. "무엇을 왜 변경했는가"에 집중한다. 변경 목록을 나열하는 대신 의도와 결과를 설명한다.

### 4. 라벨 결정

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

### 5. 리뷰어 결정

변경된 레이어와 파일을 보고, 리뷰어를 1~2명 선택한다. 팀 협업자 목록:
`HamBP`, `leeeyubin`

PR 작성자 본인은 리뷰어에서 제외한다. 변경 범위가 넓으면 최대 2명까지만 지정한다.

### 6. To Reviewers 작성

리뷰어가 집중해서 봐야 할 지점을 2~4개 bullet로 작성한다. 예시:

- 어떤 패턴/아키텍처 결정을 검토해야 하는지
- 사이드이펙트가 있을 수 있는 변경점
- 테스트가 없어 수동 검증이 필요한 부분
- 의존성 변경이나 새로운 라이브러리 도입

### 7. PR 생성

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

### 8. 완료 안내

PR URL을 출력하고, 지정한 리뷰어와 라벨을 한 줄로 확인해 준다.
