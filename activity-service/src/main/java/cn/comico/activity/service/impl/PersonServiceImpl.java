package cn.comico.activity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.comico.activity.dao.PersonReporitory;
import cn.comico.activity.model.PersonModel;
import cn.comico.activity.service.IPersonService;

@Service(value = "personService")
public class PersonServiceImpl implements IPersonService{
	
	@Autowired
	private PersonReporitory personRepository;

	@Override
	public PersonModel getPerson(long id) {
		
		return personRepository.getPersonss(id);
	}

}
