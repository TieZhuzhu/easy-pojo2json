package com.augustlee.tool.easypojo2json.parser.el;

import java.util.Random;

public interface RandomTypeValue extends PresetTypeValue {

    Random random = new Random();

    Object getRandomValue();
}
