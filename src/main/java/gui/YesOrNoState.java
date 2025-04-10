package src.main.java.gui;

public enum YesOrNoState {
    YES("Да"),
    No("Нет");

    private String title;

    YesOrNoState(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
