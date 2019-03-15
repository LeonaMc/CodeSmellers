package CodeSmellers;

public class Reporter implements Reportable {
    @Override
    public Report returnReport() {

        return new Report();
    }

    @Override
    public void formatData() {

    }
}
