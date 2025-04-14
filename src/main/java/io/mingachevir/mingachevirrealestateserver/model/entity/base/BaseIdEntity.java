package io.mingachevir.mingachevirrealestateserver.model.entity.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseIdEntity implements Serializable {

	private static final long serialVersionUID = -1264476886112842001L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
