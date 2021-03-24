package com.example.springboot03;

import com.example.springboot03.service.ParserFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication implements CommandLineRunner{

	@Autowired
	private ParserFiles parser;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 0){
			parser.readFileInStdout("C:\\\\Users\\\\SheeRMooN\\\\Desktop\\\\parse1.csv");
			parser.readFileInStdout("C:\\\\Users\\\\SheeRMooN\\\\Desktop\\\\parse2.json");
		}else {
			for (String string: args){
				parser.readFileInStdout(string);
			}
		}
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext context){
//		return args -> {
//			parser.stdout(args);
//		};
//	}
}
