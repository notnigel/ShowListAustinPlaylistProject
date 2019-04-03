package model;

public enum Id {
    WEEKEND("1P3hy2Rt9fi91w823NiuuP"),
    TUESDAY("3AbqE6drcxWGFSnCh1w3K1"),
    WEDNESDAY("5H7MZL5YurhZRLkblGjgWa");

    private String action;

    public String getAction(){
        return this.action;
    }

    private Id(String action) {
        this.action = action;
    }
}
