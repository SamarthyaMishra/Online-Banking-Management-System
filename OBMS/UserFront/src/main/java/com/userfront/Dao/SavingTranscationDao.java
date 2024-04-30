package com.userfront.Dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.userfront.Domain.SavingTranscation;

public interface SavingTranscationDao extends CrudRepository<SavingTranscation, Long> {

	List<SavingTranscation>findAll();

}
