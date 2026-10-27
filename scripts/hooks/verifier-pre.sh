#!/usr/bin/env bash
# verifier 에이전트 PreToolUse(Bash) 훅
# 대상 앱(TARGET_PACKAGE) 외의 앱을 실행할 수 있는 adb 명령을 exit 2 로 차단한다.

source "$(dirname "${BASH_SOURCE[0]}")/verifier-common.sh"

block() {
  echo "BLOCKED: $1" >&2
  echo "명령: $(one_line "$HOOK_COMMAND")" >&2
  exit 2
}

if ! read_hook_command; then
  # 입력을 해석하지 못했는데 adb 가 보이면 안전하게 차단한다
  contains_adb "$HOOK_RAW" && block "훅 입력(JSON)을 해석하지 못했습니다. python 또는 node 가 필요합니다."
  exit 0
fi
contains_adb "$HOOK_COMMAND" || exit 0
load_verifier_env || block "$VERIFIER_ENV 에 TARGET_PACKAGE 가 설정되어 있지 않습니다."

# 1. adb shell / exec-out 인자 안의 셸 우회 (sh -c, eval, $(...), 백틱, 스크립트 실행, stdin 주입)
while IFS= read -r shell_part; do
  [ -z "$shell_part" ] && continue
  if printf '%s' "$shell_part" | grep -qE '(shell|exec-out)[[:space:]]*(<|$)'; then
    block "adb shell 에 표준 입력으로 명령을 넘길 수 없습니다. 명령을 인자로 직접 적으세요."
  fi
  if printf '%s' "$shell_part" | grep -qE '\$\(|`'; then
    block "adb shell 인자 안에 \$(...) 또는 백틱을 쓸 수 없습니다. 명령을 나눠서 실행하세요."
  fi
  if printf '%s' "$shell_part" | grep -qE '(^|[^[:alnum:]_-])eval([[:space:]]|$)'; then
    block "adb shell 인자 안에 eval 을 쓸 수 없습니다."
  fi
  if printf '%s' "$shell_part" | grep -qE '(^|[[:space:];&|(])(sh|bash|su)([[:space:]]|$)'; then
    block "adb shell 인자 안에서 sh/bash/su 로 다른 셸을 실행할 수 없습니다 (sh -c 포함)."
  fi
done < <(printf '%s\n' "$HOOK_COMMAND" \
  | grep -oE 'adb(\.exe)?([[:space:]]+-[^[:space:]]+([[:space:]]+[^-[:space:]][^[:space:]]*)?)*[[:space:]]+(shell|exec-out)([[:space:]].*)?$')

# 세그먼트 안의 모든 "-n 패키지/" 가 대상 패키지인지
components_ok() {
  local p
  for p in $(printf '%s' "$1" | grep -oE '(^|[[:space:]])-n[[:space:]]+[^[:space:]/]+/' | awk '{print $NF}' | tr -d /); do
    [ "$p" = "$TARGET_PACKAGE" ] || return 1
  done
}

# 세그먼트 안의 모든 "-p 패키지" 가 대상 패키지인지 (하나도 없으면 실패)
packages_ok() {
  local p found=1
  for p in $(printf '%s' "$1" | grep -oE '(^|[[:space:]])-p[[:space:]]+[^[:space:]]+' | awk '{print $NF}'); do
    [ "$p" = "$TARGET_PACKAGE" ] || return 1
    found=0
  done
  return $found
}

BLOCKED_KEYS=" HOME APP_SWITCH CALL CAMERA EXPLORER ENVELOPE CONTACTS CALENDAR MUSIC CALCULATOR SETTINGS ASSIST VOICE_ASSIST SEARCH 3 187 5 27 64 65 207 208 209 210 176 219 231 84 "

AM='(^|[[:space:]])(am|cmd[[:space:]]+activity)[[:space:]]+'

while IFS= read -r seg; do
  [ -z "$seg" ] && continue

  # 2. 서비스 시작 / 브로드캐스트: -n 대상/ 또는 -p 대상 필요
  if printf '%s' "$seg" | grep -qE "${AM}(start-service|startservice|start-foreground-service|startforegroundservice|broadcast)([[:space:]]|$)"; then
    components_ok "$seg" || block "대상 앱($TARGET_PACKAGE)이 아닌 컴포넌트로 서비스/브로드캐스트를 보낼 수 없습니다."
    if ! printf '%s' "$seg" | grep -qE "(^|[[:space:]])(-n[[:space:]]+${TARGET_RE}/|-p[[:space:]]+${TARGET_RE}([[:space:]]|$))"; then
      block "am start-service / start-foreground-service / broadcast 에는 -n $TARGET_PACKAGE/... 또는 -p $TARGET_PACKAGE 를 지정해야 합니다."
    fi
    printf '%s' "$seg" | grep -qE '(^|[[:space:]])-p[[:space:]]' && { packages_ok "$seg" || block "-p 에는 $TARGET_PACKAGE 만 지정할 수 있습니다."; }

  # 3. 액티비티 시작: -n 대상/ 필요
  elif printf '%s' "$seg" | grep -qE "${AM}(start|start-activity)([[:space:]]|$)"; then
    printf '%s' "$seg" | grep -qE "(^|[[:space:]])-n[[:space:]]+${TARGET_RE}/" \
      || block "am start 에는 -n $TARGET_PACKAGE/<액티비티> 를 지정해야 합니다."
    components_ok "$seg" || block "대상 앱($TARGET_PACKAGE)이 아닌 액티비티는 실행할 수 없습니다."
  fi

  # 4. monkey: -p 대상 필요
  if printf '%s' "$seg" | grep -qE '(^|[[:space:]/])monkey([[:space:]]|$)'; then
    packages_ok "$seg" || block "monkey 에는 -p $TARGET_PACKAGE 만 지정할 수 있습니다."
  fi

  # 5. 앱을 실행하는 키 이벤트
  if printf '%s' "$seg" | grep -qE '(^|[[:space:]])input([[:space:]]+[a-z]+)?[[:space:]]+(keyevent|keycombination)([[:space:]]|$)'; then
    for tok in $(printf '%s' "$seg" | sed -E 's/.*(keyevent|keycombination)//'); do
      case "$tok" in -*) continue ;; esac
      key=${tok^^}
      key=${key#KEYCODE_}
      case "$BLOCKED_KEYS" in *" $key "*) block "앱을 실행하거나 전환하는 키($tok)는 보낼 수 없습니다. BACK 은 허용됩니다." ;; esac
    done
  fi
done < <(split_segments "$HOOK_COMMAND")

exit 0
