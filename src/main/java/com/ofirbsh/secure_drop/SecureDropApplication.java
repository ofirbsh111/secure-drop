package com.ofirbsh.secure_drop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

@Push
@SpringBootApplication
public class SecureDropApplication implements AppShellConfigurator
{
	public static void main(String[] args) 
	{
		SpringApplication.run(SecureDropApplication.class, args);
		System.out.println("\n ====================");
        System.out.println("==> Secure-Drop running...");
	}

}
