package org.example.gestiontaches.services;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	@Secured("ROLE_ADMIN")
	public String adminAction() {
		return "Vous êtes administrateur !";
	}

	@Secured("ROLE_USER")
	public String userAction() {
		return "Vous êtes utilisateur.";
	}
}
