package com.jobportal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration class Bootstrap {
 @Bean CommandLineRunner bootstrapAdmin(UserRepository users,RoleRepository roles,PasswordEncoder encoder,@Value("${ADMIN_EMAIL:}") String email,@Value("${ADMIN_PASSWORD:}") String password){
  return args->{if(email.isBlank()&&password.isBlank())return;if(email.isBlank()||password.length()<12)throw new IllegalStateException("Set ADMIN_EMAIL and an ADMIN_PASSWORD of at least 12 characters together");if(users.findByEmailIgnoreCase(email).isEmpty()){UserAccount u=new UserAccount();u.email=email.toLowerCase(java.util.Locale.ROOT);u.fullName="Platform administrator";u.passwordHash=encoder.encode(password);u.roles.add(roles.findByName("ADMIN").orElseThrow());users.save(u);}};
 }
}
