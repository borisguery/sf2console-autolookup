#!/usr/bin/env bash
#
# Author:   Boris Guéry <guery.b@gmail.com>
# Homepage: https://github.com/borisguery/sf2console-autolookup
#
#
# Configuration
# =============

# APP_ROOT & WEB_ROOT are the two minimum folder presents in a Symfony2 project
SF2_COMMON_APP_ROOT="app"
SF2_COMMON_WEB_ROOT="web"
SF2_COMMON_CONSOLE_FILENAME="console"
# Show or not the project root path when running the console
SHOW_PATH=0
DRY_RUN=0
TMP_CACHE_FILE=$(printf "/tmp/.sf2autolookup-%s" $(whoami))
VERBOSE=0
# Backward search depth limit
DEPTH_LIMIT=2

# Main
# ====
START_CWD=$(pwd)
LAST_CWD=$START_CWD
CURRENT_CWD="$START_CWD"
CONSOLE_PATH_FOUND=0
CONSOLE_PATH=""
SF2_PROJECT_ROOT=""
QUIET=0
USE_CACHE=0

usage() {
    cat << EOF
    usage: $0 options

    OPTIONS:
      -h Show this message
      -s Display the path of the closest console without running it
EOF
}

# Because OSX doesn't provide any tool to get the absolute path easily, afaik.
realpath() {
    [[ $1 = /* ]] && echo "$1" || echo "$PWD/${1#./}" | sed 's/\/\(\.\)$//g;s/\/$//g'
}

verbose() {
    if [[ $VERBOSE -eq 1 ]]; then
        printf "%s\n" "$@"
    fi
}

if [ -f $TMP_CACHE_FILE ]
then
    verbose "File cache found."
    source $TMP_CACHE_FILE
    if [ "$LAST_CWD" != "$(pwd)" ]; then
        verbose "Current directory is different since the last run ("$LAST_CWD" != "$(pwd)")"
        START_CWD=$(pwd)
        CONSOLE_PATH=""
        CONSOLE_FILENAME=""
    else
        verbose "Using cache: $CONSOLE_PATH/$CONSOLE_FILENAME"
        USE_CACHE=1
        CONSOLE_PATH_FOUND=1
    fi
fi

if [[ "$1" = "--show-path" ]]; then
    SHOW_PATH=1
    DRY_RUN=1
elif [[ "$1" = "--path-only" ]]; then
    SHOW_PATH=1
    QUIET=1
    DRY_RUN=1
fi

if [ "$QUIET" -eq 0 -a  "$USE_CACHE" -eq 0 ]; then
    printf "Looking up for console path..."
fi

if [ -d "app" -a "$USE_CACHE" -eq 0 ]; then
    CONSOLE_PATH=$(find .*/app -name "$SF2_COMMON_CONSOLE_FILENAME" 2>/dev/null)
    if [ -n "$CONSOLE_PATH" ]; then
        CONSOLE_FILENAME=$(basename $CONSOLE_PATH | sed 's/\/\(\.\)$//g;s/\/$//g')
        CONSOLE_PATH=$(cd "$(dirname "$CONSOLE_PATH")"; pwd)
        CONSOLE_PATH_FOUND=1
    fi
fi

CURRENT_DEPTH=0
while [ "$CONSOLE_PATH_FOUND" -eq 0 -a "$START_CWD" != "/" -a "$USE_CACHE" -eq 0 -a "$CURRENT_DEPTH" -lt "$DEPTH_LIMIT" ]; do
    OLD_IFS="$IFS"
    IFS=$"
    ";
    for DIR in $(find "$START_CWD" -type d -print 2>/dev/null);
    do
        if [ -d "$DIR/$SF2_COMMON_WEB_ROOT" -a -d "$DIR/$SF2_COMMON_APP_ROOT" ]; then
            DIR=$(realpath $DIR)
            DIRS=( ${DIRS[@]} $DIR )
            for DIR in "${DIRS[@]}"
            do
                CONSOLE_PATH=$(find "$DIR"/app -name "$SF2_COMMON_CONSOLE_FILENAME" 2>/dev/null)
                if [ -n "$CONSOLE_PATH" ]; then
                    CONSOLE_FILENAME=$(basename $CONSOLE_PATH | sed 's/\/\(\.\)$//g;s/\/$//g')
                    CONSOLE_PATH=$(cd "$(dirname "$CONSOLE_PATH")"; pwd)
                    CONSOLE_PATH_FOUND=1
                    break
                fi
            done
        fi
    done
    IFS="$OLD_IFS";
    START_CWD=$(realpath "$START_CWD/..")
    CURRENT_DEPTH=$((CURRENT_DEPTH+1))
done;

printf "\r\033[0K"

if [ "$CONSOLE_PATH_FOUND" -eq 0 ]; then
    printf "console not found"
    if [ "$CURRENT_DEPTH" -ge "$DEPTH_LIMIT" ]; then
        printf " (depth limit reached: %d/%d)" $CURRENT_DEPTH $DEPTH_LIMIT
    fi
    printf ".\n"
    exit 1
fi

if [ "$SHOW_PATH" -eq 1 ]; then
    if [ "$QUIET" -eq 0 ]; then
        printf "Project root: \033[4m%s\033[0m\n" $(cd "$(dirname "$CONSOLE_PATH"../)"; pwd)
        printf "Console path: \033[4m%s\033[0m\n" "$CONSOLE_PATH/$CONSOLE_FILENAME"
    else
        printf "%s" "$CONSOLE_PATH/$CONSOLE_FILENAME"
    fi
fi

if [ "$DRY_RUN" -eq 0 ]; then
    verbose "Writing cache.."
    touch "$TMP_CACHE_FILE"
    cat <<< "LAST_CWD=\""$(pwd)\""
CONSOLE_PATH=\""$CONSOLE_PATH\""
CONSOLE_FILENAME=\""$CONSOLE_FILENAME\""
" > $(realpath "$TMP_CACHE_FILE")

    $CONSOLE_PATH/$CONSOLE_FILENAME $@
fi
