#!/usr/bin/env bash

set -e

sbt clean compile
sbt -J-Xmx200M run
