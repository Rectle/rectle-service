package com.rectle.team.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rectle.project.model.Project;
import com.rectle.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "create_date")
	@CreationTimestamp
	private Timestamp createDate;

	@ManyToMany(mappedBy = "teams")
	@JsonIgnore
	@ToString.Exclude
	private Set<User> users = new HashSet<>();

	@OneToMany(mappedBy = "team")
	@JsonIgnore
	@ToString.Exclude
	private Set<Project> projects = new HashSet<>();
}
