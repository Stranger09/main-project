package com.company.mainproject;

import lombok.Setter;

public class StepResult {
    @Setter
    private boolean currentStepResult;

    // TODO: 07.10.2019 find out why lambok getter did not work for bool
    public boolean getCurrentStepResult() {
        return currentStepResult;
    }
}
