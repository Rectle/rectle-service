package com.rectle.compilation.model;

import com.rectle.model.entity.Model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "compilation")
public class Compilation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "create_date")
	@CreationTimestamp
	private Timestamp createDate;

	private String score;

	@ManyToOne
	@JoinColumn(name = "model_id", nullable = false)
	private Model model;

	@OneToMany(mappedBy = "compilation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Log> logs = new ArrayList<>();
}
