package com.turchenkov.parsing.parsingmethods;

public class MeasuringTimeController implements MeasuringTimeControllerMBean {
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    @Override
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
