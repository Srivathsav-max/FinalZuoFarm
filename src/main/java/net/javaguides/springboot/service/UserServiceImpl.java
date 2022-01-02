package net.javaguides.springboot.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


// java exception handling
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import net.bytebuddy.utility.RandomString;
@Service
public class UserServiceImpl implements UserService{


	private static UserRepository userRepository;

	private User user;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public void UserDetails(User user) {
        this.user = user;
    }

	@Override
	public User save(UserRegistrationDto registrationDto) {
		User user = new User(registrationDto.getFirstName(), 
				registrationDto.getLastName(), registrationDto.getEmail(),
				passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
				
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	public static void generateOneTimePassword(User user) throws UnsupportedEncodingException, MessagingException {
		String otp = RandomString.make(8);
		System.out.println("OTP "+ otp);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedOTP = passwordEncoder.encode(otp);

		user.setOneTimePassword(encodedOTP);
		user.setOtpRequestedTime(new Date());

		userRepository.save(user);
		//sendOtpEmail(user, otp);
	}
	/*public void sendOtpEmail (User user, String otp) throws UnsupportedEncodingException, MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(user.getEmail());
		helper.setFrom("190030018cse@gmail.com", "sample mail");
		String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
     
    String content = "<p>Hello " + user.getFirstName() + "</p>"
            + "<p>For security reason, you're required to use the following "
            + "One Time Password to login:</p>"
            + "<p><b>" + otp + "</b></p>"
            + "<br>"
            + "<p>Note: this OTP is set to expire in 5 minutes.</p>";
     
    helper.setSubject(subject);
     
    helper.setText(content, true);
     
    mailSender.send(message);
	System.out.println("Mail sent successfully");
	}
	@Autowired 
	JavaMailSender mailSender;*/
	public String getPassword() {
		if(user.isOTPRequired()){
			return user.getOneTimePassword();
		}
		return user.getPassword();
	}

	
}
