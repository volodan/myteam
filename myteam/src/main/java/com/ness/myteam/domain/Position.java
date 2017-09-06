package com.ness.myteam.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Position to be filled/or filled. If filled, employee data are present.
 * Position should contain, if is freshly opened, at least role mapping, to have financial aspect for reports
 * @author P3501100
 */
@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = 100, name = "firstname", nullable = true)
    private String firstname;

    @Column(length = 100, name = "surname", nullable = true)
    private String surname;

    @Column(length = 200, name = "email", nullable = true)
    private String email;
    
    @Column(length = 20, name = "pers_number", nullable = true)
    private String personalNumber;
    
    @Column(length = 1000, name = "note", nullable = true)
    private String note;
    
    @Column(name = "join_date", nullable = true)
    private Date joinDate;

    @Column(name = "leave_date", nullable = true)
    private Date leaveDate;
    
    // projection, when position should be hired
    @Column(name = "proposedJoinDate", nullable = true)
    private Date proposedJoinDate;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<Salary> salaries;
    
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<PositionJobRole> positionJobRoles;
    
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<Billability> billabilities;
    
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<PositionProject> positionProjects;
}
