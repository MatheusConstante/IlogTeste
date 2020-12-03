package com.ilog.teste.Repository;

import com.ilog.teste.Model.CourseMembership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMembershipRepository extends JpaRepository<CourseMembership, Long> {
}
