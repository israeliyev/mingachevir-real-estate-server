package io.mingachevir.mingachevirrealestateserver.model.entity.base;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity extends BaseIdEntity {

    private static final long serialVersionUID = -8279504403798074639L;
    
    Boolean enabled;
    Boolean isDeleted;

}


