package com.example.project_three_event_management_system.controller;


import com.example.project_three_event_management_system.entity.*;
import com.example.project_three_event_management_system.payload.request.LoginRequest;
import com.example.project_three_event_management_system.payload.request.SignupRequest;
import com.example.project_three_event_management_system.payload.response.JwtResponse;
import com.example.project_three_event_management_system.payload.response.MessageResponse;
import com.example.project_three_event_management_system.repository.RoleRepository;
import com.example.project_three_event_management_system.repository.SpeakerRepository;
import com.example.project_three_event_management_system.repository.UserRepository;
import com.example.project_three_event_management_system.security.JwtUtils;
import com.example.project_three_event_management_system.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

//import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
//import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private SpeakerRepository speakerRepository;

    @MockBean
    private JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;
    private User user;
    private Role role;
    private UserDetailsImpl userDetails;
    @Mock
    private Authentication authentication;
    private Speaker speaker;
    private String message;


    @BeforeEach
    void setUp() {

        /*when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");*/

        objectMapper = new ObjectMapper();

        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_USER);

        user = new User("John", "john@example.com", "encodedPass");
        user.setId(1L);
        user.setRole(role);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("John");
        when(userDetails.getEmail()).thenReturn("john@example.com");
        when(userDetails.getAuthorities())
                .thenReturn((Collection) Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        speaker = new Speaker();
        speaker.setName("Anna");
        speaker.setBio("She is expert in her field");
    }

    @Test
    @DisplayName("Should return error when email already exists during signup")
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.message", is("Error: Email is already in use!")));

        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should register user successfully with default USER role")
    void testRegisterUser_Success() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encodedPass");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("User registered successfully as ROLE_USER")));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return bad request for invalid role in signup")
    void testRegisterUser_InvalidRole() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole("INVALID_ROLE");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.message", is("Error: Invalid role specified.")));
    }

    @Test
    @DisplayName("Should return unauthorized when accessing /me without authentication")
    void testGetCurrentUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));

    }

    @Test
    @DisplayName("Should return authenticated user details when accessing /me")
    void testGetCurrentUser_Success() throws Exception {
        JwtResponse jwtResponse = new JwtResponse(null, 1L, "John", "john@example.com", "ROLE_USER");

        mockMvc.perform(get("/api/auth/me").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.role", is("ROLE_USER")));
    }

}

