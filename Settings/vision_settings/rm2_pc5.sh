#!/bin/sh
v4lctl bright 48%
v4lctl hue 67%
v4lctl contrast 45%
v4lctl color 85%
v4lctl setattr 'whitecrush lower' 55%
v4lctl setattr 'whitecrush upper' 60%
v4lctl setattr 'uv ratio' 45%
v4lctl setattr 'coring' 60%
v4lctl setattr 'chroma agc' off
v4lctl setattr 'color killer' on
v4lctl setattr 'comb filter' off
v4lctl setattr 'auto mute' on
v4lctl setattr 'luma decimation filter' off
v4lctl setattr 'agc crush' on
v4lctl setattr 'vcr hack' off
v4lctl setattr 'full luma range' on
