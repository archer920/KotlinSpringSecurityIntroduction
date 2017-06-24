package com.stonesoupprogramming

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@SpringBootApplication
class KotlinSpringSecurityIntroductionApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinSpringSecurityIntroductionApplication::class.java, *args)
}

@Configuration //Makr this as a configuration class
@EnableWebSecurity //Turn on Web Security
class SecurityWebInitializer : WebSecurityConfigurerAdapter(){
    override fun configure(http: HttpSecurity) {
        //This tells Spring Security to authorize all requests
        //We use formLogin and httpBasic
        http
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                .and()
                    .httpBasic()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        //This code sets up a user store in memory. This is
        //useful for debugging and development
        auth
                .inMemoryAuthentication()
                    .withUser("bob")
                    .password("belcher")
                    .roles("USER")
                .and()
                    .withUser("admin")
                    .password("admin")
                    .roles("USER", "ADMIN")
    }
}

@Controller
@RequestMapping("/")
class IndexController {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun doGet(model : Model) : String {
        //We can access the current user like this
        val authorization = SecurityContextHolder.getContext().authentication

        //Send the user name back to the view
        model.addAttribute("username", authorization.name)
        return "index"
    }
}