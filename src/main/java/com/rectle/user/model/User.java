package com.rectle.user.model;

import com.rectle.model.entity.Model;
import com.rectle.team.model.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(
			name = "membership",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "team_id"))
	@ToString.Exclude
	private Set<Team> teams = new HashSet<>();

	private String provider;

	private String password;

	private String email;

	@OneToMany(mappedBy = "user")
	private Set<Model> models = new HashSet<>();

	public void addTeam(Team team) {
		this.teams.add(team);
		team.getUsers().add(this);
	}

	public void removeTeam(Team team) {
		this.teams.remove(team);
		team.getUsers().remove(this);
	}
}
