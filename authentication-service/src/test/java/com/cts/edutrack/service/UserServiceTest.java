package com.cts.edutrack.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cts.edutrack.dto.LoginRequest;
import com.cts.edutrack.dto.LoginResponse;
import com.cts.edutrack.dto.UserRequest;
import com.cts.edutrack.dto.UserResponse;
import com.cts.edutrack.exception.AlreadyExistsException;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.feignClient.EnrollmentClient;
import com.cts.edutrack.model.User;
import com.cts.edutrack.repository.UserRepository;
import com.cts.edutrack.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private EnrollmentClient enrollmentClient;

	@InjectMocks
	private UserService userService;

	@Test
	void registerUser_success() {
		UserRequest req = new UserRequest();
		req.setEmail("a@b.com");
		req.setPassword("plain");
		req.setUserName("A");
		String role = "STUDENT";

		when(userRepository.existsByEmail("a@b.com")).thenReturn(false);

		User mapped = new User();
		when(modelMapper.map(req, User.class)).thenReturn(mapped);
		when(passwordEncoder.encode("plain")).thenReturn("enc");

		User saved = new User();
		saved.setUserId(1L);
		saved.setEmail("a@b.com");
		saved.setRole(role);
		saved.setAccountStatus("APPROVED");
		when(userRepository.save(mapped)).thenReturn(saved);

		UserResponse resp = new UserResponse();
		when(modelMapper.map(saved, UserResponse.class)).thenReturn(resp);

		UserResponse out = userService.registerUser(req, role);

		assertNotNull(out);
		assertEquals("enc", mapped.getPassword());
		assertEquals("STUDENT", mapped.getRole());
		assertEquals("APPROVED", mapped.getAccountStatus());
		verify(userRepository).existsByEmail("a@b.com");
		verify(userRepository).save(mapped);
	}

	@Test
	void registerUser_emailExists_throws() {
		UserRequest req = new UserRequest();
		req.setEmail("dup@x.com");

		when(userRepository.existsByEmail("dup@x.com")).thenReturn(true);

		assertThrows(AlreadyExistsException.class, () -> userService.registerUser(req, "STUDENT"));
		verify(userRepository).existsByEmail("dup@x.com");
		verifyNoMoreInteractions(userRepository);
	}

	// ---------------------------
	// login
	// ---------------------------
	@Test
	void login_success() {
		LoginRequest req = new LoginRequest();
		req.setEmail("a@b.com");
		req.setPassword("plain");

		User user = new User();
		user.setUserId(11L);
		user.setEmail("a@b.com");
		user.setPassword("enc");
		user.setRole("STUDENT");
		user.setAccountStatus("APPROVED");

		when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("plain", "enc")).thenReturn(true);
		when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

		LoginResponse resp = userService.login(req);

		assertNotNull(resp);
		assertEquals("jwt-token", resp.getToken());
		assertEquals("STUDENT", resp.getRole());
		verify(enrollmentClient).markAttendance(11L);
	}

	@Test
	void login_invalidPassword_throws() {
		LoginRequest req = new LoginRequest();
		req.setEmail("a@b.com");
		req.setPassword("wrong");

		User user = new User();
		user.setPassword("enc");
		user.setAccountStatus("APPROVED");

		when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("wrong", "enc")).thenReturn(false);

		assertThrows(RuntimeException.class, () -> userService.login(req));
		verify(userRepository).findByEmail("a@b.com");
		verifyNoInteractions(enrollmentClient, jwtUtil);
	}

	@Test
	void login_pending_throws() {
		LoginRequest req = new LoginRequest();
		req.setEmail("p@q.com");
		req.setPassword("plain");

		User user = new User();
		user.setPassword("enc");
		user.setAccountStatus("PENDING");

		when(userRepository.findByEmail("p@q.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("plain", "enc")).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login(req));
		assertTrue(ex.getMessage().contains("pending"));
		verifyNoInteractions(enrollmentClient, jwtUtil);
	}

	// ---------------------------
	// getAllUsers
	// ---------------------------
	@Test
	void getAllUsers_mapsList() {
		User u1 = new User();
		User u2 = new User();
		when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

		UserResponse r1 = new UserResponse();
		UserResponse r2 = new UserResponse();
		when(modelMapper.map(u1, UserResponse.class)).thenReturn(r1);
		when(modelMapper.map(u2, UserResponse.class)).thenReturn(r2);

		List<UserResponse> out = userService.getAllUsers();

		assertEquals(2, out.size());
		verify(userRepository).findAll();
	}

	// ---------------------------
	// getUserById
	// ---------------------------
	@Test
	void getUserById_notFound_throws() {
		when(userRepository.findById(99L)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> userService.getUserById(99L));
	}

	// ---------------------------
	// updateUser
	// ---------------------------
	@Test
	void updateUser_success() {
		Long id = 7L;
		UserRequest req = new UserRequest();
		req.setUserName("N");
		req.setEmail("n@x.com");
		req.setPhoneNumber("123");
		req.setPassword("new");

		User existing = new User();
		existing.setUserId(id);
		existing.setPassword("old");

		when(userRepository.findById(id)).thenReturn(Optional.of(existing));
		when(passwordEncoder.encode("new")).thenReturn("encNew");

		User saved = new User();
		when(userRepository.save(existing)).thenReturn(saved);

		UserResponse resp = new UserResponse();
		when(modelMapper.map(saved, UserResponse.class)).thenReturn(resp);

		UserResponse out = userService.updateUser(id, req);

		assertNotNull(out);
		assertEquals("N", existing.getUserName());
		assertEquals("n@x.com", existing.getEmail());
		assertEquals("123", existing.getPhoneNumber());
		assertEquals("encNew", existing.getPassword());
		verify(userRepository).save(existing);
	}

	// ---------------------------
	// deleteUser
	// ---------------------------
	@Test
	void deleteUser_success() {
		User u = new User();
		u.setUserId(5L);
		when(userRepository.findById(5L)).thenReturn(Optional.of(u));

		userService.deleteUser(5L);

		verify(userRepository).delete(u);
	}

	// ---------------------------
	// updateAccountStatusByEmail
	// ---------------------------
	@Test
	void updateAccountStatusByEmail_success() {
		User u = new User();
		u.setEmail("x@y.com");
		when(userRepository.findByEmail("x@y.com")).thenReturn(Optional.of(u));

		userService.updateAccountStatusByEmail("x@y.com", "APPROVED");

		assertEquals("APPROVED", u.getAccountStatus());
		verify(userRepository).save(u);
	}

	// ---------------------------
	// getUserByEmail
	// ---------------------------
	@Test
	void getUserByEmail_success() {
		User u = new User();
		u.setEmail("a@b.com");
		when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(u));

		User out = userService.getUserByEmail("a@b.com");

		assertSame(u, out);
		verify(userRepository).findByEmail("a@b.com");
	}
}