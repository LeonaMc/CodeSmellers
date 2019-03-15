package CodeSmellers;

public class Reporter implements Reportable {

    public Reporter(){
        
    }

    @Override
    public Report returnReport() {

        return new Report();
    }

    @Override
    public void formatData() {

    }
}
