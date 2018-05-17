package cn.comico.activity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.comico.activity.model.PersonModel;

public interface PersonReporitory extends JpaRepository<PersonModel, Long>{


	@Query(value = "select p from PersonModel p where p.id = :id")
	PersonModel getPersonss(@Param("id") long id);
}
