package com.jobportal;
import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.*;
import org.springframework.data.domain.Page;
interface UserRepository extends JpaRepository<UserAccount,Long>{Optional<UserAccount> findByEmailIgnoreCase(String email);}
interface RoleRepository extends JpaRepository<Role,Long>{Optional<Role> findByName(String name);}
interface ProfileRepository extends JpaRepository<SeekerProfile,Long>{}
interface CompanyRepository extends JpaRepository<Company,Long>{Optional<Company> findByOwnerUserId(Long id);}
interface JobRepository extends JpaRepository<Job,Long>{Page<Job> findByCompanyId(Long companyId,org.springframework.data.domain.Pageable p); long countByStatus(String status); @Query("select j from Job j where j.status='OPEN' and (j.deadline is null or j.deadline>=current_date) and (:q is null or lower(j.title) like lower(concat('%',:q,'%')) or lower(j.description) like lower(concat('%',:q,'%'))) and (:location is null or lower(j.location) like lower(concat('%',:location,'%'))) and (:jobType is null or j.jobType=:jobType) and (:category is null or j.category=:category) and (:experience is null or j.experienceLevel=:experience)") Page<Job> search(@Param("q") String q,@Param("location") String location,@Param("jobType") String jobType,@Param("category") String category,@Param("experience") String experience,org.springframework.data.domain.Pageable p); }
interface ApplicationRepository extends JpaRepository<Application,Long>{Optional<Application> findByJobIdAndSeekerId(Long j,Long s); Page<Application> findBySeekerId(Long id,org.springframework.data.domain.Pageable p); Page<Application> findByJobId(Long id,org.springframework.data.domain.Pageable p); long countByJobId(Long id); long countByStatus(String s);}
interface SavedJobRepository extends JpaRepository<SavedJob,SavedJobId>{long countByIdUserId(Long id); Page<SavedJob> findByIdUserId(Long id,org.springframework.data.domain.Pageable p);}
interface HistoryRepository extends JpaRepository<ApplicationHistory,Long>{}
