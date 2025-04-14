package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.Broker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class BrokerDto {
    private String email;
    private String phone;
    private String facebook;
    private String instagram;
    private String tiktok;
    private String location;

    public BrokerDto(Broker broker) {
        if(Objects.nonNull(broker)) {
            this.email = broker.getEmail();
            this.phone = broker.getPhone();
            this.facebook = broker.getFacebook();
            this.instagram = broker.getInstagram();
            this.tiktok = broker.getTiktok();
            this.location = broker.getLocation();
        }
    }
}
