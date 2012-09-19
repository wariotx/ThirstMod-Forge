#!/bin/bash
python runtime/recompile.py "$@"
python runtime/getchangedsrc.py "$@"
python runtime/reobfuscate.py "$@"
python build.py "$@"

