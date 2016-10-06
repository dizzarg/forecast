package forecast.utils;

public enum RegularTimeEnum {
    YEARS("Годы"), MONTH("Месяцы"), DAYS("Дни"), QUARTER("Четверти");

    RegularTimeEnum(String desc) {
        this.desc = desc;
    }

    private String desc;

    @Override
    public String toString() {
        return desc;
    }
}
