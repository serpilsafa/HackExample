package com.safa.hackexample;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class MainActivityTest {
    private final String fakeTime = "232767";

    @Test
    public void convertTime_returnCorrectTime() throws Exception{

        MainActivity activity = new MainActivity();

        String time = activity.convertTime(fakeTime);

        Assertions.assertEquals("23:27:67", time);


    }
}
