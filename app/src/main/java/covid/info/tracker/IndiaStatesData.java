package covid.info.tracker;

public class IndiaStatesData {

    public String tv_recovered, tv_active, tv_confirmed, tv_state, tv_deceased;

    public IndiaStatesData() {

    }

    public IndiaStatesData(String tv_recovered, String tv_active, String tv_confirmed, String tv_state, String tv_deceased) {
        this.tv_recovered = tv_recovered;
        this.tv_active = tv_active;
        this.tv_confirmed = tv_confirmed;
        this.tv_state = tv_state;
        this.tv_deceased = tv_deceased;
    }

    public String getTv_recovered() {
        return tv_recovered;
    }

    public void setTv_recovered(String tv_recovered) {
        this.tv_recovered = tv_recovered;
    }

    public String getTv_active() {
        return tv_active;
    }

    public void setTv_active(String tv_active) {
        this.tv_active = tv_active;
    }

    public String getTv_confirmed() {
        return tv_confirmed;
    }

    public void setTv_confirmed(String tv_confirmed) {
        this.tv_confirmed = tv_confirmed;
    }

    public String getTv_state() {
        return tv_state;
    }

    public void setTv_state(String tv_state) {
        this.tv_state = tv_state;
    }

    public String getTv_deceased() {
        return tv_deceased;
    }

    public void setTv_deceased(String tv_deceased) {
        this.tv_deceased = tv_deceased;
    }
}
