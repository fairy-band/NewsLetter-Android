#!/usr/bin/env bash
# verifier-pre.sh / verifier-post.sh 공용 함수

VERIFIER_ROOT="${CLAUDE_PROJECT_DIR:-$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)}"
VERIFIER_ENV="${VERIFIER_ENV:-$VERIFIER_ROOT/.claude/verifier.env}"

# 훅 입력(JSON)에서 tool_input.command 를 꺼내 HOOK_COMMAND 에 담는다. 원문은 HOOK_RAW.
read_hook_command() {
  HOOK_RAW=$(cat)
  HOOK_COMMAND=""
  local py_script='import sys,json
d=json.loads(sys.stdin.buffer.read().decode("utf-8"))
sys.stdout.buffer.write(((d.get("tool_input") or {}).get("command") or "").encode("utf-8"))'
  local node_script='let s="";process.stdin.on("data",c=>s+=c).on("end",()=>{const d=JSON.parse(s);process.stdout.write(((d.tool_input||{}).command)||"")})'
  local py
  for py in python python3 py; do
    if command -v "$py" >/dev/null 2>&1; then
      HOOK_COMMAND=$(printf '%s' "$HOOK_RAW" | "$py" -c "$py_script" 2>/dev/null) && return 0
    fi
  done
  if command -v node >/dev/null 2>&1; then
    HOOK_COMMAND=$(printf '%s' "$HOOK_RAW" | node -e "$node_script" 2>/dev/null) && return 0
  fi
  return 1
}

# verifier.env 에서 KEY 값을 읽는다 (CRLF, 따옴표, 앞뒤 공백 제거)
env_value() {
  [ -f "$VERIFIER_ENV" ] || return 1
  grep -E "^[[:space:]]*$1=" "$VERIFIER_ENV" | tail -n1 | cut -d= -f2- \
    | tr -d "\r\"'" | sed -E 's/^[[:space:]]+//; s/[[:space:]]+$//'
}

load_verifier_env() {
  TARGET_PACKAGE=$(env_value TARGET_PACKAGE)
  ALLOWED_PACKAGES=$(env_value ALLOWED_PACKAGES)
  TARGET_RE=${TARGET_PACKAGE//./\\.}
  [ -n "$TARGET_PACKAGE" ]
}

contains_adb() {
  printf '%s' "$1" | grep -qE '(^|[^[:alnum:]_.-])adb(\.exe)?([^[:alnum:]_.-]|$)'
}

# 따옴표·백슬래시를 지우고 ; && || | & 줄바꿈 기준으로 명령을 나눈다
split_segments() {
  printf '%s\n' "$1" | tr -d "\"'\\\\" | sed -E 's/(&&|\|\||;|\||&)/\n/g'
}

one_line() {
  printf '%s' "$1" | tr '\r\n' '  ' | sed -E 's/[[:space:]]+/ /g'
}

# 사용할 기기 시리얼: 명령의 -s > 명령 앞 ANDROID_SERIAL= > 환경 변수 > verifier.env
detect_serial() {
  local flat s
  flat=$(printf '%s' "$1" | tr -d "\"'")
  s=$(printf '%s' "$flat" | grep -oE 'adb(\.exe)?[[:space:]]+-s[[:space:]]+[^[:space:]]+' | head -n1 | awk '{print $NF}')
  [ -z "$s" ] && s=$(printf '%s' "$flat" | grep -oE 'ANDROID_SERIAL=[^[:space:]]+' | head -n1 | cut -d= -f2)
  [ -z "$s" ] && s="$ANDROID_SERIAL"
  [ -z "$s" ] && s=$(env_value ANDROID_SERIAL)
  ADB_SERIAL="$s"
}
