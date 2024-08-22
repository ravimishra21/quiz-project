package com.quiz.project.service;

import java.util.Optional;

import com.quiz.project.entity.StoreToken;
import com.quiz.project.entity.User;
import com.quiz.project.serviceImpl.UserDetailsImpl;

public interface UserService {

	User getUserDetailById(Integer user_id);

	User updateUserById(User user, Integer id);

	String deleteUserById(Integer id);


	void updateToken(UserDetailsImpl userDetails, String jwt, StoreToken tokenDtl);

}
