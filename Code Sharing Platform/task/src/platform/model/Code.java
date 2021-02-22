package platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Code {

    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(columnDefinition = "Binary(16)")
    private UUID uuid;

    @Column
    private String code;

    @Column
    private LocalDateTime date;

    @Column
    private int time;

    @JsonIgnore
    @Column
    LocalDateTime validUntil;

    @Column
    private int views;

    @JsonIgnore
    @Transient
    private boolean lastView;

    public Code() {
        date = LocalDateTime.now();
        views=0;
        time=0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    public LocalDateTime getDateTime(){
        return date;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getTime() {
        if(time>0){
            return (int) LocalDateTime.now().until(getValidUntil(), ChronoUnit.SECONDS);
        }
        return time;
    }

    public void setTime(int time) {
        this.time = time;
        if(time>0){
            setValidUntil(LocalDateTime.now().plusSeconds(time));
        }else {
            setValidUntil(LocalDateTime.now().plusYears(1000));
        }
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void decreaseView(){
        if(views>0){
            this.views--;
        }
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public boolean isLastView() {
        return lastView;
    }

    public void setLastView(boolean lastView) {
        this.lastView = lastView;
    }

    @Override
    public String toString() {
        return "Code{" +
                "uuid=" + uuid +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", validUntil=" + validUntil +
                ", views=" + views +
                '}';
    }
}
