#!/bin/bash

##### 必須 #####

### gitの設定 ###
# コメント先頭に#を使えるようにする
# ※コメント先頭に;は使えなくなる（-mオプションでのみ使える）
git config core.commentChar ';'

##### オプショナル #####
### gitの設定 ###
# git config core.quotePath false
# git config core.ignorecase false
# git config core.autocrlf true
# git config core.autoCRLF false
# git config core.fileMode false
# git config core.symlinks true

### JAVA_HOME ###
# - GLOBALで環境設定していない &
# - gradlをコマンドラインから利用するなど
# の理由でJAVA_HOMEを設定したい場合に利用する
# ※パスは適時個人の環境で設定すること
# ※特にLinuxの場合はandroid-studioのインストール先が異なる場合があるので注意
set_java_home() {
    export JAVA_HOME=$1
    echo "JAVA_HOME is set to $JAVA_HOME"
}

detect_os_and_set_java_home() {
    case "$OSTYPE" in
      darwin*)  set_java_home "/Applications/Android Studio.app/Contents/jbr/Contents/Home" ;;
      linux*)   set_java_home "/opt/android-studio/jbr" ;;
      msys*)    set_java_home "/c/Program Files/Android/Android Studio/jbr" ;;
      *)        echo "unknown: $OSTYPE" ;;
    esac
}

# detect_os_and_set_java_home
