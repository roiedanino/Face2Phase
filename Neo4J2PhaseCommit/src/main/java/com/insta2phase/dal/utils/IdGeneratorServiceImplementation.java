package com.insta2phase.dal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdGeneratorServiceImplementation implements IdGeneratorService{

    private IdGeneratorDao idGeneratorDao;

    @Autowired
    public IdGeneratorServiceImplementation(IdGeneratorDao idGeneratorDao){
        setIdGeneratorDao(idGeneratorDao);
    }

    @Transactional
    @Override
    public String nextId() {
        Long id = idGeneratorDao.save( new IdGenerator()).getId();
        idGeneratorDao.deleteById(id);
        return id + "";
    }

    public void setIdGeneratorDao(IdGeneratorDao idGeneratorDao) {
        this.idGeneratorDao = idGeneratorDao;
    }
}
