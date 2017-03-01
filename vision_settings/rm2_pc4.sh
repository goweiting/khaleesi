#!/bin/sh
v4lctl bright 40%
v4lctl hue 60%
v4lctl contrast 60%
v4lctl color 80%
v4lctl setattr 'whitecrush lower' 50%
v4lctl setattr 'whitecrush upper' 50%
v4lctl setattr 'uv ratio' 50%
v4lctl setattr 'coring' 0%
v4lctl setattr 'chroma agc' off
v4lctl setattr 'color killer' on
v4lctl setattr 'comb filter' off
v4lctl setattr 'auto mute' on
v4lctl setattr 'luma decimation filter' off
v4lctl setattr 'agc crush' on
v4lctl setattr 'vcr hack' off
v4lctl setattr 'full luma range' on
