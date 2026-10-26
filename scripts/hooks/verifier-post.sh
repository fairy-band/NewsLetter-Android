#!/usr/bin/env bash
# verifier 에이전트 PostToolUse(Bash) 훅
# adb input 명령이나 대상 앱 실행 명령 뒤에 포그라운드 앱을 확인하고,
# 대상 앱/허용 앱이 아니면 FOREGROUND_MISMATCH 를 보고한다(exit 2).

source "$(dirname "${BASH_SOURCE[0]}")/verifier-common.sh"

read_hook_command || exit 0
contains_adb "$HOOK_COMMAND" || exit 0
load_verifier_env || exit 0

segs=$(split_segments "$HOOK_COMMAND")
AM='(^|[[:space:]])(am|cmd[[:space:]]+activity)[[:space:]]+(start|start-activity)([[:space:]]|$)'
is_input=$(printf '%s\n' "$segs" | grep -E '(^|[[:space:]])input([[:space:]]+[a-z]+)?[[:space:]]+(tap|swipe|text|keyevent|keycombination|press|roll|draganddrop|motionevent)([[:space:]]|$)')
is_launch=$(printf '%s\n' "$segs" | grep -E "$AM" | grep -E "(^|[[:space:]])-n[[:space:]]+${TARGET_RE}/")
[ -z "$is_launch" ] && is_launch=$(printf '%s\n' "$segs" | grep -E '(^|[[:space:]/])monkey([[:space:]]|$)' | grep -E "(^|[[:space:]])-p[[:space:]]+${TARGET_RE}([[:space:]]|$)")
[ -z "$is_input" ] && [ -z "$is_launch" ] && exit 0

detect_serial "$HOOK_COMMAND"
ADB=(adb)
[ -n "$ADB_SERIAL" ] && ADB+=(-s "$ADB_SERIAL")

# mCurrentFocus 의 패키지, 없으면 mFocusedApp 의 패키지
foreground_package() {
  local focus pkg
  focus=$("${ADB[@]}" shell dumpsys window 2>/dev/null | tr -d '\r' | grep -E 'mCurrentFocus=|mFocusedApp=')
  pkg=$(printf '%s\n' "$focus" | grep -m1 'mCurrentFocus=' | grep -oE '[A-Za-z][A-Za-z0-9_]*(\.[A-Za-z0-9_]+)+/' | head -n1 | tr -d /)
  [ -z "$pkg" ] && pkg=$(printf '%s\n' "$focus" | grep -m1 'mFocusedApp=' | grep -oE '[A-Za-z][A-Za-z0-9_]*(\.[A-Za-z0-9_]+)+/' | head -n1 | tr -d /)
  printf '%s' "$pkg"
}

is_allowed() {
  [ "$1" = "$TARGET_PACKAGE" ] && return 0
  local p
  for p in ${ALLOWED_PACKAGES//,/ }; do
    [ "$1" = "$p" ] && return 0
  done
  return 1
}

sleep 1.5
pkg=$(foreground_package)
# 화면 전환 중 잠깐 다른 창이 잡힐 수 있어, 이탈은 1초 간격으로 두 번 더 확인한 뒤 확정한다
for _ in 1 2; do
  { [ -z "$pkg" ] || is_allowed "$pkg"; } && break
  sleep 1
  pkg=$(foreground_package)
done

# 포그라운드를 알 수 없으면(기기 연결 끊김 등) 판단하지 않는다
[ -z "$pkg" ] && exit 0
is_allowed "$pkg" && exit 0

echo "FOREGROUND_MISMATCH: $pkg (직전 명령: $(one_line "$HOOK_COMMAND"))" >&2
exit 2
