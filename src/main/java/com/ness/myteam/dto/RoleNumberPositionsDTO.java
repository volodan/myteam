package com.ness.myteam.dto;

import org.springframework.util.StringUtils;

/**
 * DTO contains data for counting numbers for positions
 */
public class RoleNumberPositionsDTO {

    private String roleName;
    private Integer counter;
 
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getCouner() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            if (o instanceof RoleNumberPositionsDTO) {

                RoleNumberPositionsDTO dto = (RoleNumberPositionsDTO)o; 
                if (!StringUtils.isEmpty(dto.getRoleName()) && !StringUtils.isEmpty(this.roleName) && dto.getRoleName().equals(this.roleName)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        return StringUtils.isEmpty(this.roleName) ? super.hashCode() : this.roleName.hashCode();
    }
}
