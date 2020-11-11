package paket.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import paket.model.Kupovina;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty; 
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name="user")
public class User {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer id;
	
	@Column(nullable=false)
	@NotEmpty(message = "*Please provide a username")
	@Length(min = 5, message = "*Your user name must have at least 5 characters")
	private String username;
	
	@Column(nullable=false)
	@NotEmpty(message = "*Please provide a firstname")
	private String firstname;
	
	@Column(nullable=false)
	@NotEmpty(message = "*Please provide a lastname")
	private String lastname;
	
	@Column(nullable=false, unique=true)
	@NotEmpty(message = "*Please provide an email")
	@Email(message="{errors.invalid_email}")
	private String email;
	
	@Column(nullable=false)
	@NotEmpty
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	private String password;
	
	@Column(nullable=false)
	@NotEmpty
	private String mesto;
	
	@Column(nullable=false)
	@NotEmpty
	private String adresa;
	
	@Column(nullable=false)
	@NotEmpty
	@Length(min = 13, max = 13 ,  message = "*Your jmbg name must have 13 characters")
	private String jmbg;
	
	@Column(nullable=false)
	@NotEmpty
	@Length(min = 9,  message = "*Your telephon  must have at least 9 characters")
	private String telefon;
	
	@Column(nullable=false)
	@NotEmpty
	private String brojracuna;
	
	@Column(nullable=false)
	@PositiveOrZero(message = "*Only positive number")
	private Double stanje;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	List<Kupovina> kupovine = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
		
	public String getMesto() {
		return mesto;
	}
	public void setMesto(String mesto) {
		this.mesto = mesto;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getBrojracuna() {
		return brojracuna;
	}
	public void setBrojracuna(String brojracuna) {
		this.brojracuna = brojracuna;
	}
	public Double getStanje() {
		return stanje;
	}
	public void setStanje(Double stanje) {
		this.stanje = stanje;
	}	
	
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public void addRole(Role role) {
		roles.add(role);
		role.getUsers().add(this);
	}
	public void removeRole(Role role) {
		roles.remove(role);
		role.getUsers().remove(this);
    }
	
	
	
	
	public List<Kupovina> getKupovine() {
		return kupovine;
	}
	public void setKupovine(List<Kupovina> kupovine) {
		this.kupovine = kupovine;
	}
	public void addKupovina(Kupovina kupovina) {
		if(kupovina.getUser() != this) {
			kupovina.setUser(this);
		}
		kupovine.add(kupovina);
	}
	
	
	
	
}
